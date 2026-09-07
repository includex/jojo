#!/usr/bin/env python3
"""Re-nest decompiled scenario branches against the original Python bytecode.

The restored ``decompiled-python`` sources come out of a general-purpose
decompiler that occasionally attaches an ``elif`` arm to the wrong ``if``.  The
scenarios are flat ``goto``/``label`` scripts, so a mis-attached arm silently
becomes unreachable: ``R_00.scene1`` lost the ``sel == 2/3/4`` arms of the mode
selection that way, which stalls the campaign right after the mode prompt.

The original custom-container bytecode is unambiguous about nesting -- every
branch is a forward conditional jump -- so this tool rebuilds the *shape* of
each function from that bytecode and re-parents the decompiled statements into
it.  Statement text is never rewritten: statements are moved, never edited, and
a function is left exactly as decompiled unless its statement stream lines up
one-for-one with the bytecode.
"""

from __future__ import annotations

import argparse
import ast
import re
from dataclasses import dataclass
from pathlib import Path
from typing import Iterator, Optional

# Opcode table of the game's custom container format, in the encoding order the
# packer used.  Only the control-flow and statement-boundary names matter here.
OP_NAMES = [
    "LOAD_CONST", "MAKE_FUNCTION", "STORE_NAME", "LOAD_NAME", "CALL_FUNCTION",
    "COMPARE_OP", "POP_JUMP_IF_FALSE", "POP_TOP", "JUMP_FORWARD", "JUMP_ABSOLUTE",
    "RETURN_VALUE", "LOAD_GLOBAL", "EXTENDED_ARG", "BUILD_LIST", "POP_JUMP_IF_TRUE",
    "STORE_FAST", "LOAD_FAST", "BINARY_ADD", "BINARY_SUBTRACT", "BINARY_MULTIPLY",
    "STORE_GLOBAL", "GET_ITER", "FOR_ITER", "INPLACE_ADD", "BINARY_SUBSCR",
    "STORE_SUBSCR", "DUP_TOP_TWO", "INPLACE_SUBTRACT", "ROT_THREE", "STORE_ATTR",
    "LOAD_ATTR", "BUILD_CONST_KEY_MAP", "DUP_TOP", "ROT_TWO", "BUILD_MAP",
    "JUMP_IF_FALSE_OR_POP", "LOAD_METHOD", "CALL_METHOD", "BINARY_MODULO",
    "JUMP_IF_TRUE_OR_POP", "UNARY_NEGATIVE", "BINARY_FLOOR_DIVIDE", "IMPORT_NAME",
    "IMPORT_STAR", "DELETE_SUBSCR", "BINARY_TRUE_DIVIDE", "UNARY_NOT", "NOP",
    "LIST_EXTEND", "BUILD_TUPLE", "LIST_APPEND", "FORMAT_VALUE", "BUILD_STRING",
    "CONTAINS_OP", "BINARY_XOR", "BINARY_OR", "BUILD_SLICE", "INPLACE_MULTIPLY",
    "INPLACE_TRUE_DIVIDE", "BINARY_AND", "BINARY_LSHIFT", "INPLACE_MODULO",
]

ABSOLUTE_JUMPS = {
    "POP_JUMP_IF_FALSE", "POP_JUMP_IF_TRUE", "JUMP_ABSOLUTE",
    "JUMP_IF_FALSE_OR_POP", "JUMP_IF_TRUE_OR_POP",
}
RELATIVE_JUMPS = {"JUMP_FORWARD", "FOR_ITER"}
CONDITIONAL_JUMPS = {"POP_JUMP_IF_FALSE", "POP_JUMP_IF_TRUE"}
UNCONDITIONAL_JUMPS = {"JUMP_FORWARD", "JUMP_ABSOLUTE"}

# One of these ends exactly one source statement; nothing else does.  Condition
# expressions never contain them, which is what lets statements be counted
# without reconstructing any expression.
TERMINATORS = {
    "POP_TOP", "STORE_FAST", "STORE_GLOBAL", "STORE_SUBSCR", "STORE_NAME",
    "STORE_ATTR", "DELETE_SUBSCR", "IMPORT_STAR", "RETURN_VALUE",
}


@dataclass
class Instruction:
    start: int
    end: int
    name: str
    arg: int
    target: Optional[int] = None


@dataclass
class Function:
    name: str
    code: bytes


class Reader:
    """Cursor over the container's null-terminated strings and varints."""

    def __init__(self, data: bytes):
        self.data = data
        self.index = 0

    def read_string(self) -> str:
        end = self.data.find(b"\0", self.index)
        if end < 0:
            raise ValueError("unterminated string")
        value = self.data[self.index:end].decode("utf-8")
        self.index = end + 1
        return value

    def read_integer(self) -> int:
        if self.index >= len(self.data):
            raise ValueError("unexpected end of integer")
        value = self.data[self.index]
        self.index += 1
        byte_count = 0
        prefix_mask = 0
        for bit in range(7, -1, -1):
            mask = 1 << bit
            if not value & mask:
                break
            prefix_mask |= mask
            byte_count += 1
        value &= ~prefix_mask
        for _ in range(byte_count):
            if self.index >= len(self.data):
                raise ValueError("unexpected end of integer")
            value = value * 256 + self.data[self.index]
            self.index += 1
        return value


def parse_module(path: Path) -> Optional[list[Function]]:
    """Read a container's function table, keeping only names and bytecode."""
    reader = Reader(path.read_bytes())
    try:
        if not reader.read_string().endswith("python"):
            return None
        functions = []
        for _ in range(reader.read_integer()):
            name = reader.read_string()
            for _ in range(reader.read_integer()):  # constants
                reader.read_string()
            for _ in range(reader.read_integer()):  # local names
                reader.read_string()
            for _ in range(reader.read_integer()):  # global names
                reader.read_string()
            for _ in range(reader.read_integer()):  # line table
                reader.read_integer()
                reader.read_integer()
            for _ in range(reader.read_integer()):  # label table
                reader.read_integer()
                reader.read_integer()
            length = reader.read_integer()
            code = reader.data[reader.index:reader.index + length]
            reader.index += length
            if len(code) != length or len(code) % 2:
                raise ValueError("invalid bytecode length")
            if any(code[index] >= len(OP_NAMES) for index in range(0, len(code), 2)):
                raise ValueError("invalid custom opcode")
            functions.append(Function(name, code))
        if reader.index != len(reader.data):
            raise ValueError("trailing container data")
        return functions
    except (UnicodeDecodeError, ValueError, IndexError):
        return None


def decode_instructions(code: bytes) -> list[Instruction]:
    instructions: list[Instruction] = []
    extended = 0
    extended_start = 0
    for offset in range(0, len(code), 2):
        name = OP_NAMES[code[offset]]
        arg = (extended << 8) | code[offset + 1]
        if name == "EXTENDED_ARG":
            if not extended:
                extended_start = offset
            extended = arg
            continue
        start = extended_start if extended else offset
        end = offset + 2
        target = None
        if name in ABSOLUTE_JUMPS:
            target = arg * 2
        elif name in RELATIVE_JUMPS:
            target = end + arg * 2
        instructions.append(Instruction(start, end, name, arg, target))
        extended = 0
    if extended:
        raise ValueError("dangling EXTENDED_ARG")
    return instructions


def module_names(assets_directory: Path) -> dict[Path, str]:
    """Map each bytecode container to the module name its import record gives."""
    pattern = re.compile(r'\[\[0,"([^"]+)","\.bin"\]')
    names: dict[str, str] = {}
    for path in (assets_directory / "Game" / "import").rglob("*.json"):
        match = pattern.search(path.read_text("utf-8"))
        if match:
            names[path.name[:36]] = match.group(1)
    result: dict[Path, str] = {}
    for path in sorted((assets_directory / "Game" / "native").rglob("*.bin")):
        result[path] = names.get(path.name[:36], path.name[:36])
    return result


@dataclass
class Group:
    """One source-level condition: the jumps of an ``and``/``or`` chain."""

    first: int
    last: int
    target: int


def condition_groups(instructions: list[Instruction]) -> dict[int, Group]:
    """Collapse short-circuit jump chains so one group is one source ``if``."""
    boundary = TERMINATORS | {"NOP"}
    groups: dict[int, Group] = {}
    index = 0
    while index < len(instructions):
        if instructions[index].name not in CONDITIONAL_JUMPS:
            index += 1
            continue
        last = index
        while True:
            probe = last + 1
            while (probe < len(instructions)
                   and instructions[probe].name not in CONDITIONAL_JUMPS
                   and instructions[probe].name not in boundary):
                probe += 1
            if probe >= len(instructions) or instructions[probe].name not in CONDITIONAL_JUMPS:
                break
            # ``a and b`` leaves both jumps aimed at the same escape; ``a or b``
            # aims the first past the second, straight at the body.
            same_escape = instructions[probe].target == instructions[last].target
            short_circuit = (
                instructions[last].name == "POP_JUMP_IF_TRUE"
                and probe + 1 < len(instructions)
                and instructions[last].target == instructions[probe + 1].start
            )
            if not (same_escape or short_circuit):
                break
            last = probe
        groups[index] = Group(index, last, instructions[last].target)
        index = last + 1
    return groups


class Shape:
    """Nesting recovered from bytecode: ``None`` marks a plain statement."""

    def __init__(self, body: list["Shape"], orelse: list["Shape"]):
        self.body = body
        self.orelse = orelse


class Unstructured(Exception):
    """The bytecode does not form a plain forward-branching tree."""


def build_shape(
    instructions: list[Instruction],
    groups: dict[int, Group],
    index_of: dict[int, int],
    low: int,
    high: int,
) -> list[Optional[Shape]]:
    shape: list[Optional[Shape]] = []
    index = low
    while index < high:
        group = groups.get(index)
        if group is None:
            if instructions[index].name in TERMINATORS:
                shape.append(None)
            index += 1
            continue
        escape = index_of.get(group.target)
        if escape is None or not group.last < escape <= high:
            raise Unstructured(f"branch at {instructions[index].start} escapes its region")
        orelse: list[Optional[Shape]] = []
        resume = escape
        previous = instructions[escape - 1]
        if previous.name in UNCONDITIONAL_JUMPS and previous.target is not None:
            join = index_of.get(previous.target)
            if join is not None and escape < join <= high:
                orelse = build_shape(instructions, groups, index_of, escape, join)
                resume = join
        body = build_shape(instructions, groups, index_of, group.last + 1, escape)
        shape.append(Shape(body, orelse))
        index = resume
    return shape


def flatten(statements: list[ast.stmt]) -> Iterator[ast.stmt]:
    """Emit statements in the order bytecode visits them; drop filler ``pass``."""
    for statement in statements:
        if isinstance(statement, ast.If):
            yield statement
            yield from flatten(statement.body)
            yield from flatten(statement.orelse)
        elif not isinstance(statement, ast.Pass):
            yield statement


def take(shape: list[Optional[Shape]], stream: list[ast.stmt], cursor: list[int]) -> list[ast.stmt]:
    """Re-parent the decompiled statement stream onto the recovered shape."""
    rebuilt: list[ast.stmt] = []
    for item in shape:
        if cursor[0] >= len(stream):
            raise Unstructured("decompiled statements ran out")
        statement = stream[cursor[0]]
        cursor[0] += 1
        if (item is not None) != isinstance(statement, ast.If):
            raise Unstructured("statement stream diverges from bytecode")
        if item is None:
            rebuilt.append(statement)
            continue
        assert isinstance(statement, ast.If)
        statement.body = take(item.body, stream, cursor) or [
            ast.copy_location(ast.Pass(), statement)
        ]
        statement.orelse = take(item.orelse, stream, cursor)
        rebuilt.append(statement)
    return rebuilt


def repair_function(function: Function, node: ast.FunctionDef) -> Optional[list[ast.stmt]]:
    """Return re-nested statements, or ``None`` when nothing can be proven."""
    instructions = decode_instructions(function.code)
    index_of = {item.start: index for index, item in enumerate(instructions)}
    index_of[len(function.code)] = len(instructions)
    groups = condition_groups(instructions)
    stream = list(flatten(node.body))
    cursor = [0]
    try:
        shape = build_shape(instructions, groups, index_of, 0, len(instructions))
        rebuilt = take(shape, stream, cursor)
    except Unstructured:
        return None
    if cursor[0] != len(stream):
        return None
    return rebuilt


def repair_module(tree: ast.Module, functions: list[Function]) -> list[str]:
    """Re-nest a parsed module in place; return the functions that moved.

    Only parent/child links change.  Every statement keeps its object, its text
    and its recovered source location, so line-pinned evidence built from the
    restored ``.py`` stays valid against the repaired program.
    """
    defined = {node.name: node for node in tree.body if isinstance(node, ast.FunctionDef)}
    repaired: list[str] = []
    for function in functions:
        node = defined.get(function.name)
        if node is None:
            continue
        before = ast.dump(ast.Module(body=node.body, type_ignores=[]))
        rebuilt = repair_function(function, node)
        if rebuilt is None or ast.dump(ast.Module(body=rebuilt, type_ignores=[])) == before:
            continue
        node.body = rebuilt
        repaired.append(function.name)
    return repaired


def load_containers(assets_directory: Path) -> dict[str, list[Function]]:
    """Read every scenario container, keyed by the module name it declares."""
    containers: dict[str, list[Function]] = {}
    for path, name in module_names(assets_directory).items():
        functions = parse_module(path)
        if functions:
            containers[name] = functions
    return containers


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path, help="restored decompiled-python directory")
    parser.add_argument("assets", type=Path, help="cocos assets directory holding the containers")
    arguments = parser.parse_args()

    containers = load_containers(arguments.assets.resolve())
    repaired = 0
    for path in sorted(arguments.source.resolve().glob("*.py")):
        functions = containers.get(path.stem)
        if not functions:
            continue
        tree = ast.parse(path.read_text(encoding="utf-8"), filename=str(path))
        names = repair_module(tree, functions)
        if not names:
            continue
        repaired += 1
        print(f"{path.stem}: re-nested {', '.join(names)}")
    print(f"Re-nested branches in {repaired} scenario modules")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

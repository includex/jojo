#!/usr/bin/env python3
"""Write branch-repaired scenario ``.py`` files for the original desktop runtime.

The port repairs decompiler mis-nesting while exporting its AST cache
(``export_python_ast.py``), but the original Electron build executes the
``decompiled-python/*.py`` text directly (``StageLayer.js`` reads it through
``desktopBridge.readResourceText``).  ``R_00.scene1`` therefore still lost its
``sel == 2/3/4`` arms there: choosing ``게임 시작`` fell out of the mis-nested
block, ``scene1`` returned early, and the Hall sat with nothing on screen
instead of moving on to ``S_00``.

This tool applies the same ``repair_scenario_branch_nesting`` pass and splices
only the repaired functions back into the original text, so every other line
stays byte-identical.
"""
from __future__ import annotations

import argparse
import ast
import importlib.util
import sys
from pathlib import Path


def load_repair():
    path = Path(__file__).with_name("repair_scenario_branch_nesting.py")
    spec = importlib.util.spec_from_file_location("jojo_scenario_branch_repair", path)
    module = importlib.util.module_from_spec(spec)
    sys.modules[spec.name] = module
    spec.loader.exec_module(module)
    return module


def repair_text(source: str, functions) -> tuple[str, list[str]]:
    tree = ast.parse(source)
    spans = {
        node.name: (node.lineno, node.end_lineno)
        for node in tree.body
        if isinstance(node, ast.FunctionDef)
    }
    repaired = REPAIR.repair_module(tree, functions)
    if not repaired:
        return source, []
    lines = source.splitlines(keepends=True)
    replacements = []
    for node in tree.body:
        if isinstance(node, ast.FunctionDef) and node.name in repaired:
            start, end = spans[node.name]
            replacements.append((start, end, ast.unparse(node) + "\n"))
    for start, end, text in sorted(replacements, reverse=True):
        lines[start - 1:end] = [text]
    return "".join(lines), list(repaired)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path, help="decompiled-python directory to read")
    parser.add_argument("assets", type=Path, help="cocos assets directory holding the containers")
    parser.add_argument("output", type=Path, help="directory to write repaired .py files into (may equal source)")
    arguments = parser.parse_args()
    containers = REPAIR.load_containers(arguments.assets)
    arguments.output.mkdir(parents=True, exist_ok=True)
    changed = 0
    for path in sorted(arguments.source.glob("*.py")):
        functions = containers.get(path.stem)
        if not functions:
            continue
        text, names = repair_text(path.read_text(encoding="utf-8"), functions)
        if names:
            (arguments.output / path.name).write_text(text, encoding="utf-8")
            print(f"{path.stem}: re-nested {', '.join(names)}")
            changed += 1
    print(f"Re-nested branches in {changed} scenario modules")
    return 0


REPAIR = load_repair()

if __name__ == "__main__":
    raise SystemExit(main())

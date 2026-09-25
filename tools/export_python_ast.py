#!/usr/bin/env python3
"""Export restored Python source into a portable JSON AST cache for Kotlin."""

from __future__ import annotations

import ast
import importlib.util
import json
import shutil
import sys
from pathlib import Path
from typing import Any


def load_repair():
    """Load the branch-nesting repair that sits beside this exporter."""
    path = Path(__file__).with_name("repair_scenario_branch_nesting.py")
    spec = importlib.util.spec_from_file_location("jojo_scenario_branch_repair", path)
    module = importlib.util.module_from_spec(spec)
    assert spec.loader is not None
    # `dataclass` resolves annotations through `sys.modules`, so register the
    # module before executing it.
    sys.modules[spec.name] = module
    spec.loader.exec_module(module)
    return module


REPAIR = load_repair()


def encode(value: Any, dialogue_keys: dict[int, str] | None = None) -> Any:
    if isinstance(value, ast.AST):
        encoded = {
            "type": type(value).__name__,
            # Keep source locations in the portable cache. The interpreter
            # uses these only for coverage evidence; execution remains driven
            # exclusively by the recovered AST fields.
            "location": {"line": getattr(value, "lineno", None)},
            "fields": {field: encode(getattr(value, field), dialogue_keys) for field in value._fields},
        }
        dialogue_key = (dialogue_keys or {}).get(id(value))
        if dialogue_key is not None:
            encoded["fields"]["args"][0]["fields"]["value"] = f"@dialogue:{dialogue_key}"
        return encoded
    if isinstance(value, list):
        return [encode(item, dialogue_keys) for item in value]
    if isinstance(value, (str, int, float, bool)) or value is None:
        return value
    raise TypeError(f"Unsupported AST value: {type(value)!r}")


def collect_dialogue_texts(tree: ast.Module, module_name: str) -> tuple[dict[int, str], dict[str, str]]:
    call_keys: dict[int, str] = {}
    entries: dict[str, str] = {}
    for function in (node for node in tree.body if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef))):
        for node in ast.walk(function):
            if not isinstance(node, ast.Call) or ast.unparse(node.func) != "stage.say":
                continue
            if not node.args or not isinstance(node.args[0], ast.Constant) or not isinstance(node.args[0].value, str):
                raise ValueError(f"{module_name}:{node.lineno} stage.say requires a literal string")
            key = f"{module_name}:{function.name}:{node.lineno}"
            if key in entries:
                raise ValueError(f"duplicate scenario dialogue key: {key}")
            call_keys[id(node)] = key
            entries[key] = node.args[0].value
    return call_keys, entries


def main() -> None:
    if len(sys.argv) != 4:
        raise SystemExit("usage: export_python_ast.py SOURCE_DIR ASSETS_DIR OUTPUT_DIR")
    source_dir = Path(sys.argv[1]).resolve()
    # The decompiler that produced SOURCE_DIR sometimes attaches an `elif` arm
    # to the wrong `if`, which strands whole branches.  Re-nest against the
    # original bytecode here, where the executed program is built, so the
    # restored sources keep their recovered line numbers untouched.
    containers = REPAIR.load_containers(Path(sys.argv[2]).resolve())
    output_dir = Path(sys.argv[3]).resolve()
    if output_dir.exists():
        shutil.rmtree(output_dir)
    output_dir.mkdir(parents=True)

    exported = 0
    repaired = 0
    dialogue_entries: dict[str, str] = {}
    for source_path in sorted(source_dir.glob("*.py")):
        tree = ast.parse(source_path.read_text(encoding="utf-8"), filename=str(source_path), feature_version=(3, 9))
        functions = containers.get(source_path.stem)
        if functions and REPAIR.repair_module(tree, functions):
            repaired += 1
        dialogue_keys, module_dialogue_entries = collect_dialogue_texts(tree, source_path.stem)
        dialogue_entries.update(module_dialogue_entries)
        payload = {
            "format": "jojo-python-ast/v1",
            "module": source_path.stem,
            "source": source_path.name,
            "ast": encode(tree, dialogue_keys),
        }
        (output_dir / f"{source_path.stem}.json").write_text(
            json.dumps(payload, ensure_ascii=False, separators=(",", ":")), encoding="utf-8"
        )
        exported += 1
    print(f"Exported {exported} Python AST files with {len(dialogue_entries)} dialogue references to {output_dir}; re-nested branches in {repaired}")


if __name__ == "__main__":
    main()

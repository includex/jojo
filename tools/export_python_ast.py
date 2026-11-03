#!/usr/bin/env python3
"""Export restored Python source into a portable JSON AST cache for Kotlin."""

from __future__ import annotations

import ast
import importlib.util
import json
import re
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


def collect_dialogue_texts(tree: ast.Module, module_name: str, catalog: dict[str, str]) -> set[str]:
    """Validate authored references without deriving identity from source locations."""
    seen: set[str] = set()
    for function in (node for node in tree.body if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef))):
        for node in ast.walk(function):
            if not isinstance(node, ast.Call) or ast.unparse(node.func) != "stage.say":
                continue
            if not node.args:
                raise ValueError(f"{module_name}:{node.lineno} stage.say requires a text argument")
            # Computed strings are resolved by the same host API at runtime.
            if not isinstance(node.args[0], ast.Constant) or not isinstance(node.args[0].value, str):
                continue
            value = node.args[0].value
            if not value.startswith("@dialogue:"):
                continue
            key = value.removeprefix("@dialogue:")
            if not re.fullmatch(r"[A-Za-z0-9][A-Za-z0-9_.-]*", key):
                raise ValueError(f"{module_name}:{node.lineno} malformed dialogue ID: {key}")
            if key in seen:
                raise ValueError(f"duplicate scenario dialogue ID: {key}")
            if key not in catalog:
                raise ValueError(f"{module_name}:{node.lineno} dialogue ID missing from catalog: {key}")
            seen.add(key)
    return seen


def main() -> None:
    if len(sys.argv) not in (3, 4):
        raise SystemExit("usage: export_python_ast.py SOURCE_DIR [LEGACY_ASSETS_DIR] OUTPUT_DIR")
    source_dir = Path(sys.argv[1]).resolve()
    # The decompiler that produced SOURCE_DIR sometimes attaches an `elif` arm
    # to the wrong `if`, which strands whole branches.  Re-nest against the
    # original bytecode here, where the executed program is built, so the
    # restored sources keep their recovered line numbers untouched.
    # Local compatibility containers preserve recovered Jojo branches. A new
    # game can omit compatibility/ and author its control flow directly.
    compat_dir = source_dir / "compatibility"
    containers = (REPAIR.load_containers(Path(sys.argv[2]).resolve()) if len(sys.argv) == 4
                  else REPAIR.load_containers(compat_dir) if compat_dir.is_dir() else {})
    output_dir = Path(sys.argv[-1]).resolve()
    if output_dir.exists():
        shutil.rmtree(output_dir)
    output_dir.mkdir(parents=True)

    exported = 0
    repaired = 0
    catalog_path = source_dir / "dialogue-text.json"
    catalog = json.loads(catalog_path.read_text(encoding="utf-8"))["entries"]
    seen_dialogue_ids: set[str] = set()
    for source_path in sorted(source_dir.glob("*.py")):
        tree = ast.parse(source_path.read_text(encoding="utf-8"), filename=str(source_path), feature_version=(3, 9))
        module_dialogue_ids = collect_dialogue_texts(tree, source_path.stem, catalog)
        functions = containers.get(source_path.stem)
        if functions and REPAIR.repair_module(tree, functions):
            repaired += 1
        repaired_ids = collect_dialogue_texts(tree, source_path.stem, catalog)
        if not repaired_ids <= module_dialogue_ids:
            raise ValueError(f"branch repair introduced unknown dialogue IDs in {source_path.name}")
        overlap = seen_dialogue_ids & module_dialogue_ids
        if overlap:
            raise ValueError(f"duplicate scenario dialogue IDs: {sorted(overlap)}")
        seen_dialogue_ids.update(module_dialogue_ids)
        payload = {
            "format": "jojo-python-ast/v1",
            "module": source_path.stem,
            "source": source_path.name,
            "ast": encode(tree),
        }
        (output_dir / f"{source_path.stem}.json").write_text(
            json.dumps(payload, ensure_ascii=False, separators=(",", ":")), encoding="utf-8"
        )
        exported += 1
    # Lua modules are executed directly, but share the same stable text catalog.
    for lua_path in source_dir.glob("*.lua"):
        for key in re.findall(r"@dialogue:([A-Za-z0-9][A-Za-z0-9_.-]*)", lua_path.read_text(encoding="utf-8")):
            if key not in catalog:
                raise ValueError(f"{lua_path.name} dialogue ID missing from catalog: {key}")
            seen_dialogue_ids.add(key)
    unused = set(catalog) - seen_dialogue_ids
    if unused:
        raise ValueError(f"dialogue catalog has {len(unused)} unreferenced IDs (first: {sorted(unused)[0]})")
    print(f"Exported {exported} Python AST files with {len(seen_dialogue_ids)} dialogue references to {output_dir}; re-nested branches in {repaired}")


if __name__ == "__main__":
    main()

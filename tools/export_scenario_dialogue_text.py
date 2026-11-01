#!/usr/bin/env python3
"""Validate explicit scenario dialogue IDs and copy their authored catalog."""
import ast
import json
import re
import sys
from pathlib import Path

def collect(source_dir, entries):
    seen = set()
    for source in sorted(source_dir.glob("*.py")):
        tree = ast.parse(source.read_text(encoding="utf-8"), filename=str(source), feature_version=(3, 9))
        for function in (node for node in tree.body if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef))):
            for node in ast.walk(function):
                if not isinstance(node, ast.Call) or ast.unparse(node.func) != "stage.say":
                    continue
                if not node.args or not isinstance(node.args[0], ast.Constant) or not isinstance(node.args[0].value, str):
                    raise ValueError(f"{source.name}:{node.lineno} stage.say requires a @dialogue:ID string")
                value = node.args[0].value
                if not value.startswith('@dialogue:'):
                    raise ValueError(f"{source.name}:{node.lineno} stage.say needs an explicit @dialogue:ID")
                key = value[len('@dialogue:'):]
                if not re.fullmatch(r'[A-Za-z0-9][A-Za-z0-9_.-]*', key):
                    raise ValueError(f"{source.name}:{node.lineno} malformed dialogue ID: {key}")
                if key in seen:
                    raise ValueError(f'duplicate scenario dialogue ID: {key}')
                if key not in entries:
                    raise ValueError(f'{source.name}:{node.lineno} dialogue ID missing from catalog: {key}')
                seen.add(key)
    unused = set(entries) - seen
    if unused:
        raise ValueError(f'catalog has {len(unused)} unreferenced dialogue IDs (first: {sorted(unused)[0]})')
    return seen


def main():
    if len(sys.argv) != 3:
        raise SystemExit("usage: export_scenario_dialogue_text.py SOURCE_DIR OUTPUT_JSON")
    source_dir, output = Path(sys.argv[1]), Path(sys.argv[2])
    document = json.loads((source_dir / 'dialogue-text.json').read_text(encoding='utf-8'))
    collect(source_dir, document['entries'])
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(document, ensure_ascii=False, separators=(",", ":")) + "\n", encoding="utf-8")
    print(f"Exported {len(document['entries'])} scenario dialogue strings to {output}")


if __name__ == "__main__":
    main()

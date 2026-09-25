#!/usr/bin/env python3
"""Generate the complete scenario dialogue catalog from restored scenario modules."""
import ast
import json
import sys
from pathlib import Path

ALIASES = {
    "opening_scene1_page1": "대장님, 서둘러야 해요!",
    "opening_scene1_page2": "알아!",
    "opening_scene1_page3": "잠시만 기다려 주세요!",
    "yingchuan_round2_followup_speaker32": "이것은 만민의 분노입니다!",
}


def collect(source_dir):
    entries = {}
    for source in sorted(source_dir.glob("*.py")):
        tree = ast.parse(source.read_text(encoding="utf-8"), filename=str(source), feature_version=(3, 9))
        for function in (node for node in tree.body if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef))):
            for node in ast.walk(function):
                if not isinstance(node, ast.Call) or ast.unparse(node.func) != "stage.say":
                    continue
                if not node.args or not isinstance(node.args[0], ast.Constant) or not isinstance(node.args[0].value, str):
                    raise ValueError(f"{source.name}:{node.lineno} stage.say requires a literal string")
                key = f"{source.stem}:{function.name}:{node.lineno}"
                if key in entries:
                    raise ValueError(f"duplicate scenario dialogue key: {key}")
                entries[key] = node.args[0].value
    return entries


def main():
    if len(sys.argv) != 3:
        raise SystemExit("usage: export_scenario_dialogue_text.py SOURCE_DIR OUTPUT_JSON")
    source_dir, output = Path(sys.argv[1]), Path(sys.argv[2])
    document = {
        "format": "jojo-scenario-dialogue-text/v1",
        "entries": collect(source_dir),
        "aliases": ALIASES,
    }
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(document, ensure_ascii=False, separators=(",", ":")) + "\n", encoding="utf-8")
    print(f"Exported {len(document['entries'])} scenario dialogue strings to {output}")


if __name__ == "__main__":
    main()

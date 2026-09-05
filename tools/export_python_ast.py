#!/usr/bin/env python3
"""Export restored Python source into a portable JSON AST cache for Kotlin."""

from __future__ import annotations

import ast
import json
import shutil
import sys
from pathlib import Path
from typing import Any


def encode(value: Any) -> Any:
    if isinstance(value, ast.AST):
        return {
            "type": type(value).__name__,
            # Keep source locations in the portable cache.  The interpreter
            # uses these only for coverage evidence; execution remains driven
            # exclusively by the recovered AST fields.
            "location": {"line": getattr(value, "lineno", None)},
            "fields": {field: encode(getattr(value, field)) for field in value._fields},
        }
    if isinstance(value, list):
        return [encode(item) for item in value]
    if isinstance(value, (str, int, float, bool)) or value is None:
        return value
    raise TypeError(f"Unsupported AST value: {type(value)!r}")


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: export_python_ast.py SOURCE_DIR OUTPUT_DIR")
    source_dir = Path(sys.argv[1]).resolve()
    output_dir = Path(sys.argv[2]).resolve()
    if output_dir.exists():
        shutil.rmtree(output_dir)
    output_dir.mkdir(parents=True)

    exported = 0
    for source_path in sorted(source_dir.glob("*.py")):
        tree = ast.parse(source_path.read_text(encoding="utf-8"), filename=str(source_path), feature_version=(3, 9))
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
    print(f"Exported {exported} Python AST files to {output_dir}")


if __name__ == "__main__":
    main()

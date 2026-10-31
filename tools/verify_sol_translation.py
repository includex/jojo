#!/usr/bin/env python3
"""Read-only validation for Sol's Chinese-to-Korean localization batches.

Scans localization/sol recursively. A translation file is a JSON object whose
keys and values are strings, or a list of records with a recognized Chinese
source field and Korean target field. Source/manifest/progress/assignment files
are ignored. The sibling sgccz-desktop catalog provides source-key coverage.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from collections import Counter, defaultdict
from pathlib import Path
from typing import Any


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_SOL_DIR = ROOT / "localization" / "sol"
DEFAULT_SIBLING = ROOT.parent / "jojo_mobile" / "sgccz-desktop"
HAN = re.compile(r"[\u3400-\u9fff]")
PROTECTED = re.compile(
    r"&\d+"                         # speaker / control codes
    r"|\*\.\d+"                    # runtime interpolation references
    r"|\[C[0-9A-Fa-f]{2}"              # color controls such as [C28, followed by colored text
    r"|%\{[^{}]+\}"                 # named percent placeholders
    r"|%(?:\d+\$)?[-+#0 ]*(?:\d+|\*)?(?:\.(?:\d+|\*))?[diouxXeEfFgGcrsa%]"
)
SOURCE_FIELDS = ("zh", "source", "original", "text")
TARGET_FIELDS = ("ko", "translation", "translated", "target")


def load_json(path: Path) -> Any:
    with path.open(encoding="utf-8") as stream:
        return json.load(stream)


def collect_reference_strings(sibling: Path) -> set[str]:
    """Union the source-string inventory and exact translation-cache keys."""
    refs: set[str] = set()
    source_path = sibling / "tools" / "ko-source-strings.json"
    cache_path = sibling / "tools" / "ko-translations.json"
    if source_path.is_file():
        source = load_json(source_path)
        if isinstance(source, list):
            refs.update(item["text"] for item in source
                        if isinstance(item, dict) and isinstance(item.get("text"), str))
    if cache_path.is_file():
        cache = load_json(cache_path)
        if isinstance(cache, dict):
            refs.update(key for key in cache if isinstance(key, str))
    return refs


def mappings_from_json(value: Any) -> list[tuple[str, str]]:
    pairs: list[tuple[str, str]] = []
    if isinstance(value, dict):
        # Ignore metadata/progress dictionaries; only accept a pure string map.
        if value and all(isinstance(k, str) and isinstance(v, str) for k, v in value.items()):
            pairs.extend(value.items())
    elif isinstance(value, list):
        for item in value:
            if not isinstance(item, dict):
                continue
            source = next((item[k] for k in SOURCE_FIELDS if isinstance(item.get(k), str)), None)
            target = next((item[k] for k in TARGET_FIELDS if isinstance(item.get(k), str)), None)
            if source is not None and target is not None:
                pairs.append((source, target))
    return pairs


def protected_tokens(text: str) -> Counter[str]:
    return Counter(PROTECTED.findall(text))


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--sol-dir", type=Path, default=DEFAULT_SOL_DIR)
    parser.add_argument("--sibling", type=Path, default=DEFAULT_SIBLING,
                        help="sgccz-desktop root used for source/cache coverage")
    parser.add_argument("--require-complete", action="store_true", help="Fail if any reference original is missing")
    args = parser.parse_args()
    if not args.sol_dir.is_dir():
        print(f"ERROR: localization directory not found: {args.sol_dir}", file=sys.stderr)
        return 2

    references = collect_reference_strings(args.sibling)
    files: list[Path] = []
    translations: dict[str, str] = {}
    file_pairs: dict[Path, list[tuple[str, str]]] = {}
    errors: list[str] = []
    for path in sorted(args.sol_dir.rglob("*.json")):
        if path.name.lower() in {"source.json", "manifest.json", "progress.json", "assigned.json"}:
            continue
        try:
            pairs = mappings_from_json(load_json(path))
        except (OSError, json.JSONDecodeError) as error:
            errors.append(f"{path}: invalid JSON ({error})")
            continue
        if not pairs:
            continue
        files.append(path)
        file_pairs[path] = pairs
        for source, target in pairs:
            if source in translations and translations[source] != target:
                errors.append(f"conflicting translations for {source!r}: {translations[source]!r} vs {target!r} ({path})")
            else:
                translations[source] = target
            if not source.strip() or not target.strip():
                errors.append(f"empty source/translation in {path}: {source!r}")
            if not HAN.search(source):
                errors.append(f"source has no Chinese Han characters in {path}: {source!r}")
            if HAN.search(target):
                errors.append(f"translation still contains Han characters in {path}: {source!r} -> {target!r}")
            source_tokens, target_tokens = protected_tokens(source), protected_tokens(target)
            if source_tokens != target_tokens:
                errors.append(f"protected marker mismatch in {path}: {source_tokens} != {target_tokens} for {source!r}")
            if re.findall(r'&\d+', source) != re.findall(r'&\d+', target):
                errors.append(f"speaker order mismatch in {path}: {source!r}")

    keys = set(translations)
    covered = keys & references
    unknown = keys - references
    missing = references - keys
    if args.require_complete and missing:
        errors.append(f"{len(missing)} original strings have not been translated")
    if unknown:
        errors.append(f"{len(unknown)} translation keys do not match an original")
    print(f"Translation files: {len(files)}; mapping rows: {sum(map(len, file_pairs.values()))}; unique source keys: {len(keys)}")
    print(f"Reference source/cache keys: {len(references)}; batch keys covered: {len(covered)}; unknown batch keys: {len(unknown)}")
    print(f"Remaining originals: {len(missing)}")
    if unknown:
        print("Unknown keys (first 20):")
        for key in sorted(unknown)[:20]:
            print(f"  {key!r}")
    print(f"Errors: {len(errors)}")
    for error in errors:
        print(f"ERROR: {error}")
    return 1 if errors else 0


if __name__ == "__main__":
    raise SystemExit(main())

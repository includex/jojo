#!/usr/bin/env python3
"""Resolve the original sibling checkout without assuming a fixed nesting.

The game can be checked out directly beside ``jojo_mobile`` or be located in
an isolated subdirectory.  Source-vs-game gates must use the real sibling
checkout in either layout instead of silently reusing old build outputs.
"""
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
SOURCE_CANDIDATES = [
    ROOT.parent / "jojo_mobile" / "sgccz-desktop" / "recovered-js" / "modules",
    ROOT.parents[2] / "jojo_mobile" / "sgccz-desktop" / "recovered-js" / "modules",
]
SOURCE = next((candidate for candidate in SOURCE_CANDIDATES if candidate.is_dir()), SOURCE_CANDIDATES[0])

if not SOURCE.is_dir():
    raise SystemExit(f"SOURCE_ROOT_MISSING: {SOURCE}")

print(f"SOURCE_ROOT_RESOLUTION_OK source={SOURCE} checked={len(list((ROOT / 'tools').glob('*source*')))}")

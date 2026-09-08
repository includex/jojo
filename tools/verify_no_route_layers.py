#!/usr/bin/env python3
"""Prove the "no production caller" claim behind the zero-draw parity states.

`render_parity_scope.json` lets several original layers pass on
`no-route-diff.json`, whose coverage note is "No production caller; zero-draw
parity."  That report used to be a hand-written artifact in the gitignored
build directory, so nothing actually checked the claim: if a caller appeared,
or had always existed, the gate would still have said PASS.

The claim is a property of the recovered source, so verify it there.  Every
layer the original can attach is registered in `core/Instance.js` as
``Name: [id, "prefab/path"]``.  A layer is reachable only if some other module
mentions it -- by class name, by prefab path, or by that numeric id in an
``addLayer``/``getLayer``/``removeLayer`` call.  For a genuinely unreachable
layer the registry entry is the single mention in the whole tree.

Running this the first time showed the blanket note was wrong for one entry:
`SettingLayer`'s other-tools option 1 really does attach `InstallLayer`.  Its
zero-draw result still holds, but for a different reason -- the layer is a
native package manager whose `onCreate` uses `jsb`, which the desktop build
does not have -- so that reason is now checked on its own terms.

On success this writes the empty source/game logs and the report the scope
file reads, so the zero-draw states rest on re-derived evidence.
"""
from __future__ import annotations

import argparse
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SOURCE_CANDIDATES = (
    ROOT.parent / "jojo_mobile" / "sgccz-desktop" / "recovered-js" / "modules",
    ROOT.parents[2] / "jojo_mobile" / "sgccz-desktop" / "recovered-js" / "modules",
)

# The layers `render_parity_scope.json` covers with `no-route-diff.json`, and
# why each one draws nothing in the desktop build.
#
#   "unreferenced" -- nothing in the recovered tree ever attaches it.
#   "jsb-only"     -- a caller exists, but the layer's own onCreate immediately
#                     uses `jsb`, the Cocos native bridge. The desktop/web build
#                     has no `jsb`, so attaching it throws before its first draw.
#
# `InstallLayer` sits in the second group: SettingLayer's other-tools option 1
# does reach it, so the scope file's blanket "No production caller" note was
# wrong about it even though its zero-draw result stands.
NO_ROUTE_LAYERS = {
    "HotUpdateLayer": "unreferenced",
    "InstallLayer": "jsb-only",
    "PrivacyLayer": "unreferenced",
    "StatementLayer": "unreferenced",
    "VideoLayer": "unreferenced",
    "HelpLayer": "unreferenced",
    "ResetLayer": "unreferenced",
    "SendGiftsLayer": "unreferenced",
}

# `jsb` is the Cocos native bridge; Creator's desktop/web runtime never defines it.
JSB_USE = re.compile(r"\bjsb\.")

REGISTRY = "core/Instance.js"
LAYER_CALL = re.compile(r"\b(?:addLayer|getLayer|removeLayer|_removeLayer)\s*\(\s*\[\s*(\d+)")


def source_root() -> Path:
    for candidate in SOURCE_CANDIDATES:
        if candidate.is_dir():
            return candidate
    raise SystemExit(f"SOURCE_ROOT_MISSING: {SOURCE_CANDIDATES[0]}")


def registry_entries(modules: Path) -> dict[str, tuple[int, str]]:
    text = (modules / REGISTRY).read_text(encoding="utf-8", errors="replace")
    pattern = re.compile(r'^\s*(\w+):\s*\[\s*(\d+)\s*,\s*"([^"]+)"\s*\]', re.MULTILINE)
    return {name: (int(number), path) for name, number, path in pattern.findall(text)}


def layer_source(modules: Path, layer: str) -> Path | None:
    return next((path for path in modules.rglob("*.js") if path.stem == layer), None)


def references(modules: Path, layer: str, entry: tuple[int, str] | None) -> list[str]:
    """Return every reference that would make `layer` reachable."""
    number, prefab = entry if entry else (None, None)
    callers: list[str] = []
    for path in sorted(modules.rglob("*.js")):
        relative = path.relative_to(modules).as_posix()
        if relative == f"ui/{layer}.js" or path.stem == layer:
            continue
        text = path.read_text(encoding="utf-8", errors="replace")
        for index, line in enumerate(text.splitlines(), start=1):
            if relative == REGISTRY and line.lstrip().startswith(f"{layer}:"):
                continue  # the registration itself is not a caller
            if layer in line or (prefab and prefab in line):
                callers.append(f"{relative}:{index}: {line.strip()[:120]}")
            elif number is not None and any(int(found) == number for found in LAYER_CALL.findall(line)):
                callers.append(f"{relative}:{index}: {line.strip()[:120]}")
    return callers


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--report", type=Path, default=ROOT / "build/render-events/no-route-diff.json")
    args = parser.parse_args()

    modules = source_root()
    registry = registry_entries(modules)
    failures = 0
    checked = []
    for layer, reason in NO_ROUTE_LAYERS.items():
        entry = registry.get(layer)
        record = {"layer": layer, "reason": reason}
        if entry:
            record.update(id=entry[0], prefab=entry[1])
        if reason == "jsb-only":
            module = layer_source(modules, layer)
            if module is None:
                print(f"BLOCKED {layer}: no recovered module to inspect")
                failures += 1
                continue
            uses = [
                f"{module.relative_to(modules).as_posix()}:{index}"
                for index, line in enumerate(module.read_text(encoding="utf-8", errors="replace").splitlines(), 1)
                if JSB_USE.search(line)
            ]
            if not uses:
                print(f"BLOCKED {layer}: declared jsb-only but its module never touches jsb")
                failures += 1
                continue
            record["jsbUses"] = len(uses)
            checked.append(record)
            print(f"PASS {layer}: reachable, but its module uses jsb at {len(uses)} site(s); desktop has no jsb")
            continue
        callers = references(modules, layer, entry)
        if callers:
            print(f"BLOCKED {layer}: declared unreferenced but reachable from {len(callers)} reference(s)")
            for caller in callers[:5]:
                print(f"    {caller}")
            failures += 1
        else:
            checked.append(record)
            where = f"registered as id {entry[0]} and" if entry else "never registered and"
            print(f"PASS {layer}: {where} never referenced again")

    if failures:
        print(f"NO_ROUTE_LAYERS_BLOCKED layers={len(NO_ROUTE_LAYERS)} failures={failures}")
        return 1

    report = args.report.resolve()
    report.parent.mkdir(parents=True, exist_ok=True)
    logs = {}
    for side in ("source", "game"):
        path = report.parent / f"{side}-no-route.jsonl"
        path.write_text("", encoding="utf-8")
        logs[side] = path.relative_to(ROOT).as_posix() if path.is_relative_to(ROOT) else str(path)
    report.write_text(json.dumps({
        "expected": logs["source"],
        "actual": logs["game"],
        "expectedFormat": "canonical",
        "actualFormat": "canonical",
        "expectedDrawCount": 0,
        "actualDrawCount": 0,
        "differenceCount": 0,
        "differences": [],
        "equal": True,
        "floatTolerance": 1e-05,
        "truncated": False,
        "evidence": "tools/verify_no_route_layers.py",
        "layers": checked,
    }, indent=2) + "\n", encoding="utf-8")
    print(f"NO_ROUTE_LAYERS_OK layers={len(checked)} report={report}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

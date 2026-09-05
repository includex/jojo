#!/usr/bin/env python3
"""Evidence-first source/port matrix for MenuLayer, TerrainLayer and WinCon.

The tool deliberately reports a state as ``missing`` instead of comparing a
different screen.  That makes absent capture-state injection visible in CI and
prevents a menu or map screenshot from being used as evidence for a terrain
tab or modal callback.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path

from PIL import Image, ImageChops, ImageStat


STATES = (
    (
        "menu-open",
        "python-source-battle-verification-menu.png",
        "port-menu-open.png",
        "Canvas/Layer/menu_button TOUCH_END",
        "MenuLayer.onCreate(weather, round, max_round)",
    ),
    (
        "terrain-rise",
        "python-source-battle-verification-layer-TerrainLayer-rise.png",
        "port-terrain-rise.png",
        "MenuLayer.button6 TOUCH_END → TerrainLayer.onCreate → sel(0)",
        "TerrainLayer._initPanel0 (rise)",
    ),
    (
        "terrain-expend",
        "python-source-battle-verification-layer-TerrainLayer-expend.png",
        "port-terrain-expend.png",
        "TerrainLayer.bg/button1 TOUCH_END",
        "TerrainLayer.sel(1) → _initPanel1 (expend)",
    ),
    (
        "terrain-close",
        "python-source-battle-verification-layer-TerrainLayer-close.png",
        "port-terrain-close.png",
        "TerrainLayer.bg/button2 TOUCH_END",
        "TerrainLayer.removeFromParent; map input resumes",
    ),
    (
        "wincon-open",
        "python-source-battle-verification-layer-WinConBoxLayer-open.png",
        "port-wincon-open.png",
        "MenuLayer.button9 TOUCH_END → BattleLayer WIN_CONDITION",
        "WinConBoxLayer.onCreate(info, func); scrollToTop",
    ),
    (
        "wincon-confirm",
        "python-source-battle-verification-layer-WinConBoxLayer-confirm.png",
        "port-wincon-confirm.png",
        "WinConBoxLayer.bg0/button TOUCH_END",
        "removeFromParent then original continuation func",
    ),
)


def compare(source: Path, port: Path) -> dict[str, object]:
    expected = Image.open(source).convert("RGB")
    actual = Image.open(port).convert("RGB")
    if expected.size != actual.size:
        return {"status": "dimension-mismatch", "source_size": expected.size, "port_size": actual.size}
    delta = ImageChops.difference(expected, actual)
    changed = sum(value != (0, 0, 0) for value in delta.getdata())
    total = expected.width * expected.height
    return {
        "status": "compared",
        "source_size": expected.size,
        "port_size": actual.size,
        "changed_pixels": changed,
        "total_pixels": total,
        "changed_ratio": changed / total if total else 0,
        "mean_absolute_channel_delta": ImageStat.Stat(delta).mean,
    }


def asserted_menu_stack(image: Path, owner: str) -> tuple[bool, str]:
    stack = image.with_name(image.stem + "-stack.json")
    if not stack.exists():
        return False, f"normalized {owner} MenuLayer stack fixture missing"
    try:
        data = json.loads(stack.read_text())
    except json.JSONDecodeError as error:
        return False, f"invalid {owner} MenuLayer stack fixture: {error}"
    if not data.get("requestedPresent"):
        return False, f"MenuLayer missing in {owner} stack"
    if data.get("activeOverlayCountAfter") != 0:
        return False, f"{owner} menu stack still contains SayLayer/DialogueLayer/choice overlay"
    return True, ""


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source_dir", type=Path)
    parser.add_argument("port_dir", type=Path)
    parser.add_argument("output", type=Path)
    args = parser.parse_args()
    rows = []
    for state, source_name, port_name, source_input, expected_result in STATES:
        source, port = args.source_dir / source_name, args.port_dir / port_name
        row = {
            "state": state,
            "source": str(source),
            "port": str(port),
            "source_input": source_input,
            "expected_result": expected_result,
        }
        if not source.exists():
            row.update(status="missing-source", reason="original Electron fixture was not generated")
        elif state == "menu-open" and not asserted_menu_stack(source, "source")[0]:
            row.update(status="invalid-source-stack", reason=asserted_menu_stack(source, "source")[1])
        elif not port.exists():
            row.update(status="missing-port", reason="no matching Kotlin capture-state output")
        elif state == "menu-open" and not asserted_menu_stack(port, "port")[0]:
            row.update(status="invalid-port-stack", reason=asserted_menu_stack(port, "port")[1])
        else:
            row.update(compare(source, port))
        rows.append(row)
    report = {"matrix_version": 1, "states": rows}
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n")
    print(json.dumps(report, ensure_ascii=False, indent=2))
    if any(row["status"] != "compared" for row in rows):
        raise SystemExit(2)


if __name__ == "__main__":
    main()

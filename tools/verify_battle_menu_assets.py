#!/usr/bin/env python3
"""Verify every rendered MenuLayer SpriteFrame against the live Cocos atlas."""
from __future__ import annotations

import json
import sys
from pathlib import Path

from PIL import Image, ImageChops


def source_frame(atlas: Image.Image, rect: dict[str, int], uv: list[float]) -> Image.Image:
    """Convert Cocos bottom-origin rect/UV into the normal PNG used by LibGDX."""
    x, y = rect["x"], rect["y"]
    width, height = rect["width"], rect["height"]
    image = atlas.crop((x, atlas.height - y - height, x + width, atlas.height - y))
    # MenuLayer's dynamic-atlas SpriteFrames have their first UV row at the
    # visual bottom, so Cocos presents the crop upside-down in a top-origin
    # PNG.  Derive this from the recorded UV, never from a filename rule.
    if len(uv) >= 6 and uv[1] > uv[5]:
        image = image.transpose(Image.Transpose.FLIP_TOP_BOTTOM)
    return image.convert("RGBA")


def main() -> None:
    if len(sys.argv) != 4:
        raise SystemExit("usage: verify_battle_menu_assets.py <source-build> <game-assets> <source-menu-snapshot>")
    source_build, game_assets, snapshot_path = map(Path, sys.argv[1:])
    snapshot = json.loads(snapshot_path.read_text())
    atlas = Image.open(source_build / "battle-menu-atlas.png").convert("RGBA")
    output = game_assets / "ui" / "battle-menu"
    expected_paths = {
        "Canvas/Layer/bg": "background.png",
        "Canvas/Layer/bg/box1": "frame.png",
        "Canvas/Layer/bg/bg0": "box.png",
        "Canvas/Layer/bg/progressBar/bg": "title-bar.png",
        "Canvas/Layer/bg/progressBar/bar": "progress-bar.png",
        "Canvas/Layer/bg/box2/node0": "minimap.png",
        **{f"Canvas/Layer/bg/contain/button{i}/Background": "button.png" for i in range(12)},
        **{f"Canvas/Layer/bg/contain/button{i}/Background/tool1": f"tool{i + 1}.png" for i in range(12)},
        "Canvas/Layer/bg/contain/button13/Background": "button.png",
        "Canvas/Layer/bg/contain/button13/Background/edit": "help.png",
    }
    nodes = {node.get("path"): node for node in snapshot["nodes"]}
    checks = 0
    for source_path, filename in expected_paths.items():
        node = nodes.get(source_path)
        if node is None:
            raise AssertionError(f"source MenuLayer node missing: {source_path}")
        sprite = node.get("sprite")
        if not sprite:
            raise AssertionError(f"source MenuLayer sprite missing: {source_path}")
        expected = source_frame(atlas, sprite["rect"], sprite["uv"])
        actual = Image.open(output / filename).convert("RGBA")
        if expected.size != actual.size or ImageChops.difference(expected, actual).getbbox():
            raise AssertionError(f"MenuLayer asset differs: {filename} from {source_path}")
        checks += 1
    print(f"BATTLE_MENU_ASSETS_OK checks={checks} uniqueAssets={len(set(expected_paths.values()))}")


if __name__ == "__main__":
    main()

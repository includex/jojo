#!/usr/bin/env python3
"""Exact selected-icon regression for a paired Cocos/LibGDX MenuLayer capture."""
from __future__ import annotations

import sys
from pathlib import Path

from PIL import Image, ImageChops


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: verify_battle_menu_render.py <source-menu.png> <game-menu.png>")
    source, game = (Image.open(Path(path)).convert("RGBA") for path in sys.argv[1:])
    if source.size != game.size:
        raise AssertionError(f"menu capture dimensions differ: {source.size} vs {game.size}")
    # Source MenuLayer has 12 consecutive buttons and HELP in button13's
    # last visual slot.  These rectangles isolate the 72×72 rendered icon
    # from its nine-patch border, text, and weather animation.
    starts = [20 + 176 * index for index in range(12)] + [2142]
    for index, x in enumerate(starts):
        box = (x + 16, 820, x + 160, 944)
        if ImageChops.difference(source.crop(box), game.crop(box)).getbbox():
            raise AssertionError(f"MenuLayer rendered icon differs at visible slot {index}")
    print("BATTLE_MENU_RENDER_OK icons=13")


if __name__ == "__main__":
    main()

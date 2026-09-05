#!/usr/bin/env python3
"""Deterministic RGBA pixel comparison used by source-versus-game fixtures."""
from __future__ import annotations

import sys
from pathlib import Path

from PIL import Image, ImageChops


def main() -> None:
    if len(sys.argv) not in (3, 4):
        raise SystemExit("usage: compare_render_frames.py <source.png> <game.png> [diff.png]")
    source_path, game_path = map(Path, sys.argv[1:3])
    source = Image.open(source_path).convert("RGBA")
    game = Image.open(game_path).convert("RGBA")
    if source.size != game.size:
        raise AssertionError(f"frame dimensions differ: source={source.size} game={game.size}")
    diff = ImageChops.difference(source, game)
    bbox = diff.getbbox()
    changed = sum(pixel != (0, 0, 0, 0) for pixel in diff.getdata())
    total = source.width * source.height
    if len(sys.argv) == 4:
        # Amplify tiny channel differences only for inspection; pass/fail
        # always uses the unmodified RGBA image above.
        diff.point(lambda value: min(255, value * 4)).save(sys.argv[3])
    print(f"PIXEL_COMPARE size={source.width}x{source.height} changed={changed}/{total} bbox={bbox}")
    if changed:
        raise SystemExit(1)


if __name__ == "__main__":
    main()

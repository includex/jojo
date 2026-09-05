#!/usr/bin/env python3
"""Compare an isolated port sprite fixture against the original atlas frame.

The original and desktop decoders have different RGB premultiplication paths,
so this gate first requires exact alpha geometry (selection, crop, flip,
scale, and offset).  It separately reports RGB drift for the later colour
renderer gate.
"""
from __future__ import annotations

import sys
from PIL import Image, ImageChops


def main() -> None:
    if len(sys.argv) != 7:
        raise SystemExit("usage: verify_sprite_fixture.py <source-atlas> <port.png> <y> <width> <height> <flip-x>")
    source_path, port_path, y, width, height, flip = sys.argv[1:]
    y, width, height = map(int, (y, width, height))
    flip = bool(int(flip))
    source = Image.open(source_path).convert("RGBA")
    port = Image.open(port_path).convert("RGBA")
    scale = 12  # BattleSpriteFixtureScreen: 384 world px × desktop 2x / 64px.
    frame = source.crop((0, y, width, y + height))
    if flip:
        frame = frame.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    frame = frame.resize((width * scale, height * scale), Image.Resampling.NEAREST)
    expected = Image.new("RGBA", port.size, (0, 0, 0, 0))
    expected.alpha_composite(frame, ((port.width - width * scale) // 2, (port.height - height * scale) // 2))
    alpha_diff = ImageChops.difference(expected.getchannel("A"), port.getchannel("A"))
    alpha_changed = sum(pixel != 0 for pixel in alpha_diff.getdata())
    rgb_diff = ImageChops.difference(expected.convert("RGB"), port.convert("RGB"))
    rgb_changed = sum(pixel != (0, 0, 0) for pixel in rgb_diff.getdata())
    print(f"SPRITE_FIXTURE alpha_changed={alpha_changed} rgb_changed={rgb_changed}")
    if alpha_changed:
        raise SystemExit(1)


if __name__ == "__main__":
    main()

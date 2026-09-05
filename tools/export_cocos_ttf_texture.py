#!/usr/bin/env python3
"""Convert a Cocos `cc.Label._ttfTexture` raw RGBA dump into a PNG cache asset.

The source capture uses WebGL's bottom-left `readPixels` convention. LibGDX
samples decoded textures in the same visual orientation for a bottom-origin
SpriteBatch draw, so the PNG deliberately retains row order rather than
flipping it. The tool records the source text/hash in a small JSON sidecar. It
is intentionally generic:
any Cocos RichText capture with a raw dump and source string can use it.
"""

from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path

from PIL import Image


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--raw", type=Path, required=True)
    parser.add_argument("--width", type=int, required=True)
    parser.add_argument("--height", type=int, required=True)
    parser.add_argument("--text", required=True)
    parser.add_argument("--world-x", type=float, required=True)
    parser.add_argument("--world-y", type=float, required=True)
    parser.add_argument("--draw-width", type=float, required=True)
    parser.add_argument("--draw-height", type=float, required=True)
    parser.add_argument("--output-dir", type=Path, required=True)
    args = parser.parse_args()

    raw = args.raw.read_bytes()
    expected = args.width * args.height * 4
    if len(raw) != expected:
        raise SystemExit(f"raw size {len(raw)} != {args.width}x{args.height} RGBA ({expected})")

    key = hashlib.sha256(args.text.encode("utf-8")).hexdigest()
    args.output_dir.mkdir(parents=True, exist_ok=True)
    image = Image.frombytes("RGBA", (args.width, args.height), raw)
    png = args.output_dir / f"{key}.png"
    image.save(png, optimize=False)
    (args.output_dir / f"{key}.json").write_text(
        json.dumps(
            {
                "text": args.text,
                "source": "Cocos cc.Label._ttfTexture gl.readPixels RGBA/UNSIGNED_BYTE",
                "rawOrigin": "bottom-left",
                "width": args.width,
                "height": args.height,
                "worldOrigin": [args.world_x, args.world_y],
                "drawSize": [args.draw_width, args.draw_height],
                "rawSha256": hashlib.sha256(raw).hexdigest(),
            },
            ensure_ascii=False,
            indent=2,
        )
        + "\n",
        encoding="utf-8",
    )
    print(png)


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""Extract opaque framebuffer-space LoadGame confirmation composites."""
from pathlib import Path
import sys

WIDTH = 2560
HEIGHT = 1376
LEFT, BOTTOM, RIGHT, TOP = 734, 433, 1826, 943

source_dir = Path(sys.argv[1])
output_dir = Path(sys.argv[2])
output_dir.mkdir(parents=True, exist_ok=True)

for row in range(8):
    source = source_dir / f"source-login-1-blank-row{row}.rgba"
    data = source.read_bytes()
    if len(data) != WIDTH * HEIGHT * 4:
        raise SystemExit(f"invalid framebuffer size: {source}")
    stride = WIDTH * 4
    crop = b"".join(
        data[y * stride + LEFT * 4:y * stride + RIGHT * 4]
        for y in range(BOTTOM, TOP)
    )
    (output_dir / f"load-confirm-row{row}.rgba").write_bytes(crop)

print("TITLE_LOAD_CONFIRMATION_CROPS_OK rows=8 size=1092x510")

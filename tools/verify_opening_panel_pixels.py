#!/usr/bin/env python3
"""Strict bottom-left RGBA8 equality for the cumulative isolated first R00 dialogue stages."""
import argparse
import hashlib
import json
from pathlib import Path


def compare(source, game, width=2560, height=1376, stage="panel"):
    if stage not in ("panel", "portrait", "speaker", "text"):
        raise ValueError("stage must be panel, portrait, speaker or text")
    expected = width * height * 4
    if len(source) != expected or len(game) != expected:
        raise ValueError(f"both RGBA buffers must have {expected} bytes")
    if not any(source[i] for i in range(len(source)) if i % 4 != 3):
        raise ValueError("source contains no visible panel color")
    changed = 0
    absolute_error = 0
    squared_error = 0
    min_x, min_y, max_x, max_y = width, height, -1, -1
    for i in range(0, expected, 4):
        if source[i:i+4] == game[i:i+4]:
            continue
        changed += 1
        for channel in range(4):
            delta = source[i + channel] - game[i + channel]
            absolute_error += abs(delta)
            squared_error += delta * delta
        x, y = (i // 4) % width, (i // 4) // width
        min_x, min_y = min(min_x, x), min(min_y, y)
        max_x, max_y = max(max_x, x), max(max_y, y)
    return {"contract": f"isolated-opening-{stage}-rgba8", "width": width, "height": height,
            "changedPixels": changed, "equal": changed == 0,
            "changedBoundsBottomLeft": [min_x, min_y, max_x + 1, max_y + 1] if changed else None,
            "absoluteRgbaError": absolute_error, "squaredRgbaError": squared_error,
            "sourceSha256": hashlib.sha256(source).hexdigest(),
            "gameSha256": hashlib.sha256(game).hexdigest(),
            "scope": ("panel isolated after first R00 dialogue; no full-screen, portrait, text or audio claim"
                      if stage == "panel" else
                      "panel plus portrait isolated after first R00 dialogue; no full-screen, text or audio claim"
                      if stage == "portrait" else
                      "panel plus portrait and speaker isolated after first R00 dialogue; no full-screen, body text or audio claim"
                      if stage == "speaker" else
                      "completed first R00 dialogue isolated; no full-screen or audio claim")}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path)
    parser.add_argument("game", type=Path)
    parser.add_argument("--report", type=Path)
    parser.add_argument("--stage", choices=("panel", "portrait", "speaker", "text"), default="panel")
    args = parser.parse_args()
    report = compare(args.source.read_bytes(), args.game.read_bytes(), stage=args.stage)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, indent=2) + "\n")
    print(json.dumps(report))
    return 0 if report["equal"] else 1


if __name__ == "__main__":
    raise SystemExit(main())

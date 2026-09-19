#!/usr/bin/env python3
"""Strict bottom-left RGBA8 equality for the isolated first R00 dialogue panel or panel plus portrait."""
import argparse
import hashlib
import json
from pathlib import Path


def compare(source, game, width=2560, height=1376, stage="panel"):
    if stage not in ("panel", "portrait"):
        raise ValueError("stage must be panel or portrait")
    expected = width * height * 4
    if len(source) != expected or len(game) != expected:
        raise ValueError(f"both RGBA buffers must have {expected} bytes")
    if not any(source[i] for i in range(len(source)) if i % 4 != 3):
        raise ValueError("source contains no visible panel color")
    changed = sum(source[i:i+4] != game[i:i+4] for i in range(0, expected, 4))
    return {"contract": f"isolated-opening-{stage}-rgba8", "width": width, "height": height,
            "changedPixels": changed, "equal": changed == 0,
            "sourceSha256": hashlib.sha256(source).hexdigest(),
            "gameSha256": hashlib.sha256(game).hexdigest(),
            "scope": ("panel isolated after first R00 dialogue; no full-screen, portrait, text or audio claim"
                      if stage == "panel" else
                      "panel plus portrait isolated after first R00 dialogue; no full-screen, text or audio claim")}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path)
    parser.add_argument("game", type=Path)
    parser.add_argument("--report", type=Path)
    parser.add_argument("--stage", choices=("panel", "portrait"), default="panel")
    args = parser.parse_args()
    report = compare(args.source.read_bytes(), args.game.read_bytes(), stage=args.stage)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, indent=2) + "\n")
    print(json.dumps(report))
    return 0 if report["equal"] else 1


if __name__ == "__main__":
    raise SystemExit(main())

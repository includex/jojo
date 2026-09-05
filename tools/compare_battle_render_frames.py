#!/usr/bin/env python3
"""Strict, GPU-tolerant comparison for fresh source/game battle frames."""
from __future__ import annotations

import argparse
import json
from pathlib import Path

from PIL import Image, ImageChops, ImageFilter, ImageOps, ImageStat


def structural_delta(source: Image.Image, game: Image.Image) -> Image.Image:
    """Return a GPU-colour-tolerant edge delta.

    Cocos/WebGL and LibGDX/OpenGL apply slightly different texture filtering
    and colour conversion to the same map.  Those broad RGB shifts are not a
    gameplay/render contract, while displaced silhouettes, HP bars and effect
    geometry are.  A one-pixel pre-blur suppresses sampler noise before edge
    extraction without hiding a different 48x48 animation pose.
    """
    def edges(image: Image.Image) -> Image.Image:
        return ImageOps.grayscale(image).filter(ImageFilter.GaussianBlur(1)).filter(ImageFilter.FIND_EDGES)
    return ImageChops.difference(edges(source), edges(game))


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path)
    parser.add_argument("game", type=Path)
    parser.add_argument("report", type=Path)
    parser.add_argument("--max-structural-mae", type=float, default=1.1)
    parser.add_argument("--structural-pixel-threshold", type=int, default=8)
    parser.add_argument("--max-structural-changed-ratio", type=float, default=0.003)
    args = parser.parse_args()

    source = Image.open(args.source).convert("RGB")
    game = Image.open(args.game).convert("RGB")
    if source.size != game.size:
        raise AssertionError(f"frame dimensions differ: source={source.size} game={game.size}")
    rgb_delta = ImageChops.difference(source, game)
    rgb_mae = tuple(ImageStat.Stat(rgb_delta).mean)
    delta = structural_delta(source, game)
    structural_mae = ImageStat.Stat(delta).mean[0]
    changed = sum(pixel > args.structural_pixel_threshold for pixel in delta.getdata())
    total = source.width * source.height
    ratio = changed / total if total else 1.0
    passed = structural_mae <= args.max_structural_mae and ratio <= args.max_structural_changed_ratio
    report = {
        "format": "jojo-fresh-battle-frame-compare/v2",
        "source": str(args.source),
        "game": str(args.game),
        "size": list(source.size),
        "rgbChannelMaeDiagnostic": list(rgb_mae),
        "structuralMae": structural_mae,
        "maxStructuralMae": args.max_structural_mae,
        "structuralPixelThreshold": args.structural_pixel_threshold,
        "structuralChangedPixels": changed,
        "totalPixels": total,
        "structuralChangedRatio": ratio,
        "maxStructuralChangedRatio": args.max_structural_changed_ratio,
        "passed": passed,
    }
    args.report.parent.mkdir(parents=True, exist_ok=True)
    args.report.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(
        f"BATTLE_FRAME_COMPARE_{'PASS' if passed else 'FAIL'} "
        f"structuralMae={structural_mae:.6f} changed={changed}/{total} "
        f"rgbDiagnostic={rgb_mae}"
    )
    if not passed:
        raise SystemExit(1)


if __name__ == "__main__":
    main()

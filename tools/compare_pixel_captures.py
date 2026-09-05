#!/usr/bin/env python3
"""Strict framebuffer pixel comparison with explicit ICC normalization.

This tool is intentionally separate from compare_render_logs.py: draw-event
parity and framebuffer parity are different contracts.  Raw RGB comparison is
the default.  Source ICC conversion happens only when explicitly requested.
"""
from __future__ import annotations

import argparse
import io
import json
import math
from pathlib import Path
from typing import Any

from PIL import Image, ImageChops, ImageCms, ImageStat


THRESHOLDS = (1, 2, 4, 8, 16, 32, 64)


def profile_metadata(image: Image.Image) -> dict[str, Any]:
    payload = image.info.get("icc_profile")
    if not payload:
        return {"embedded": False, "byteLength": 0, "name": None}
    profile = ImageCms.ImageCmsProfile(io.BytesIO(payload))
    return {
        "embedded": True,
        "byteLength": len(payload),
        "name": ImageCms.getProfileName(profile).strip(),
        "description": ImageCms.getProfileDescription(profile).strip(),
        "colorSpace": profile.profile.xcolor_space.strip(),
    }


def metrics(expected: Image.Image, actual: Image.Image) -> dict[str, Any]:
    delta = ImageChops.difference(expected.convert("RGB"), actual.convert("RGB"))
    stat = ImageStat.Stat(delta)
    total = delta.width * delta.height
    changed = 0
    above = {threshold: 0 for threshold in THRESHOLDS}
    for pixel in delta.getdata():
        maximum = max(pixel)
        if maximum:
            changed += 1
        for threshold in THRESHOLDS:
            if maximum > threshold:
                above[threshold] += 1
    channel_mae = stat.mean
    return {
        "pixelEqual": changed == 0,
        "changedPixels": changed,
        "totalPixels": total,
        "changedRatio": changed / total if total else 0.0,
        "meanAbsoluteChannelDelta": channel_mae,
        "rgbMae": sum(channel_mae) / 3.0,
        "rgbRmse": math.sqrt(sum(value * value for value in stat.rms) / 3.0),
        "maxChannelResidualRatios": {
            f"above{threshold}": above[threshold] / total if total else 0.0
            for threshold in THRESHOLDS
        },
    }


def compare(source_path: Path, game_path: Path, color_space: str) -> dict[str, Any]:
    source_file = Image.open(source_path)
    game_file = Image.open(game_path)
    source_profile = profile_metadata(source_file)
    game_profile = profile_metadata(game_file)
    report: dict[str, Any] = {
        "comparisonKind": "framebuffer-pixels",
        "source": str(source_path),
        "game": str(game_path),
        "sourceSize": list(source_file.size),
        "gameSize": list(game_file.size),
        "sourceProfile": source_profile,
        "gameProfile": game_profile,
        "raw": None,
        "normalized": None,
    }
    if source_file.size != game_file.size:
        report.update({"status": "dimension-mismatch", "pixelEqual": False})
        source_file.close()
        game_file.close()
        return report

    source_rgb = source_file.convert("RGB")
    game_rgb = game_file.convert("RGB")
    report["raw"] = metrics(source_rgb, game_rgb)
    if color_space == "source-to-srgb":
        payload = source_file.info.get("icc_profile")
        if not payload:
            raise ValueError("--color-space=source-to-srgb requires an embedded source ICC profile")
        source_rgb = ImageCms.profileToProfile(
            source_file,
            ImageCms.ImageCmsProfile(io.BytesIO(payload)),
            ImageCms.createProfile("sRGB"),
            outputMode="RGB",
        )
        report["normalization"] = {
            "mode": color_space,
            "source": "embedded ICC",
            "target": "sRGB",
            "gameInterpretation": "sRGB (untagged framebuffer)" if not game_profile["embedded"] else "embedded ICC not transformed",
        }
    else:
        report["normalization"] = {"mode": "raw", "source": "none", "target": "none"}
    report["normalized"] = metrics(source_rgb, game_rgb)
    report["pixelEqual"] = report["normalized"]["pixelEqual"]
    report["status"] = "pass" if report["pixelEqual"] else "fail"
    source_file.close()
    game_file.close()
    return report


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path)
    parser.add_argument("game", type=Path)
    parser.add_argument("--color-space", choices=("raw", "source-to-srgb"), default="raw")
    parser.add_argument("--output", type=Path, help="write the complete JSON report")
    args = parser.parse_args(argv)
    try:
        report = compare(args.source, args.game, args.color_space)
    except (OSError, ValueError) as error:
        parser.error(str(error))
    encoded = json.dumps(report, ensure_ascii=False, indent=2) + "\n"
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(encoded, encoding="utf-8")
    print(encoded, end="")
    if report["status"] == "dimension-mismatch":
        return 2
    return 0 if report["pixelEqual"] else 1


if __name__ == "__main__":
    raise SystemExit(main())

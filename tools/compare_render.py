#!/usr/bin/env python3
"""Exact pixel metrics for paired Cocos and LibGDX render captures."""
from __future__ import annotations

import argparse
import json
from pathlib import Path

from PIL import Image, ImageChops, ImageStat


def parse_crop(value: str) -> tuple[int, int, int, int]:
    try:
        x, y, width, height = (int(part) for part in value.split(","))
    except ValueError as error:
        raise argparse.ArgumentTypeError("crop must be x,y,width,height") from error
    if width <= 0 or height <= 0:
        raise argparse.ArgumentTypeError("crop width and height must be positive")
    return x, y, width, height


def main(
    reference: Path,
    actual: Path,
    diff: Path | None = None,
    crop: tuple[int, int, int, int] | None = None,
) -> None:
    expected = Image.open(reference).convert("RGB")
    rendered = Image.open(actual).convert("RGB")
    report: dict[str, object] = {
        "reference": str(reference),
        "actual": str(actual),
        "reference_size": expected.size,
        "actual_size": rendered.size,
    }
    if expected.size != rendered.size:
        report["pixel_equal"] = False
        report["reason"] = "dimension mismatch"
        print(json.dumps(report, ensure_ascii=False, indent=2))
        raise SystemExit(2)
    if crop:
        x, y, width, height = crop
        if x < 0 or y < 0 or x + width > expected.width or y + height > expected.height:
            report["pixel_equal"] = False
            report["reason"] = "crop outside image bounds"
            report["crop"] = {"x": x, "y": y, "width": width, "height": height}
            print(json.dumps(report, ensure_ascii=False, indent=2))
            raise SystemExit(2)
        expected = expected.crop((x, y, x + width, y + height))
        rendered = rendered.crop((x, y, x + width, y + height))
        report["crop"] = {"x": x, "y": y, "width": width, "height": height}
    delta = ImageChops.difference(expected, rendered)
    histogram = delta.histogram()
    changed = sum(histogram) - sum(histogram[channel * 256] for channel in range(3))
    # Histogram totals every RGB channel; count changed pixels directly for
    # a clear pass/fail metric independent of alpha channel representation.
    changed_pixels = sum(1 for value in delta.getdata() if value != (0, 0, 0))
    total = expected.width * expected.height
    report.update({
        "pixel_equal": changed_pixels == 0,
        "changed_pixels": changed_pixels,
        "total_pixels": total,
        "changed_ratio": changed_pixels / total if total else 0,
        "mean_absolute_channel_delta": ImageStat.Stat(delta).mean,
        "histogram_channel_total": changed,
    })
    if diff:
        diff.parent.mkdir(parents=True, exist_ok=True)
        # Amplify tiny differences to make layer boundaries legible in reviews.
        delta.point(lambda value: min(255, value * 4)).save(diff)
        report["diff"] = str(diff)
    print(json.dumps(report, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("reference", type=Path, help="original Cocos capture")
    parser.add_argument("actual", type=Path, help="current LibGDX game capture")
    parser.add_argument("diff", type=Path, nargs="?", help="optional amplified diff PNG")
    parser.add_argument("--crop", type=parse_crop, metavar="X,Y,W,H", help="compare this absolute image region only")
    args = parser.parse_args()
    main(args.reference, args.actual, args.diff, args.crop)

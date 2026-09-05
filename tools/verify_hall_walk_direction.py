#!/usr/bin/env python3
"""Validate street-dialogue HallUnit walk direction from draw-event logs.

Unlike a screenshot check this uses the actual emitted sprite records.  It
checks the time axis, logical grid delta, authored direction and mirror flag,
then compares the source and port streams.  Hall direction values are
``0=up, 1=right, 2=down, 3=left`` (the latter two are easy to accidentally
reverse while porting).
"""
from __future__ import annotations

import argparse
import json
import re
import sys
from pathlib import Path


GRID = re.compile(r"grid=(-?\d+(?:\.\d+)?),(-?\d+(?:\.\d+)?);dir=(\d+);action=(\d+)")


def read(path: Path) -> list[dict]:
    return [json.loads(line) for line in path.read_text(encoding="utf-8").splitlines() if line.strip()]


def audit(rows: list[dict], label: str) -> list[str]:
    errors: list[str] = []
    previous: dict[str, tuple[int, int, int, float]] = {}
    for index, row in enumerate(rows):
        match = GRID.search(str(row.get("text", "")))
        if not match:
            errors.append(f"{label}[{index}] missing logical walk metadata")
            continue
        x, y = float(match.group(1)), float(match.group(2))
        direction, action = int(match.group(3)), int(match.group(4))
        if action != 20:
            errors.append(f"{label}[{index}] unexpected action={action}")
        path = str(row.get("nodePath", ""))
        timestamp = float(row.get("timestamp", row.get("time", index * .04)))
        if path in previous:
            px, py, _, previous_time = previous[path]
            dx, dy = x - px, y - py
            # HallLayer._move2 uses authored grid Y increasing as DOWN (2)
            # and decreasing as UP (0); this is opposite to screen Y.
            expected = (2 if dy > 0 else 0 if dy < 0 else 1 if dx > 0 else 3 if dx < 0 else direction)
            if (dx or dy) and direction != expected:
                errors.append(f"{label}[{index}] backward walk path={path} from={px},{py} to={x},{y} direction={direction} expected={expected}")
            if timestamp <= previous_time:
                errors.append(f"{label}[{index}] non-monotonic timestamp {timestamp} <= {previous_time}")
            elif timestamp - previous_time > .0401:
                errors.append(f"{label}[{index}] unexpected step duration {timestamp - previous_time:.5f}s")
        flip = row.get("flipX")
        asset = str(row.get("assetFrameId", ""))
        expected_flip = direction in (1, 3)
        if flip is not None and bool(flip) != expected_flip:
            errors.append(f"{label}[{index}] mirror mismatch direction={direction} flipX={flip}")
        if "flipX=" in asset and asset.rsplit("flipX=", 1)[1].lower() != str(expected_flip).lower():
            errors.append(f"{label}[{index}] asset mirror mismatch direction={direction} asset={asset}")
        previous[path] = (x, y, direction, timestamp)
    return errors


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("source", type=Path)
    parser.add_argument("port", type=Path)
    parser.add_argument("--output", type=Path)
    args = parser.parse_args()
    source, port = read(args.source), read(args.port)
    errors = audit(source, "source") + audit(port, "port")
    if len(source) != len(port):
        errors.append(f"draw count differs source={len(source)} port={len(port)}")
    # Keep the report useful even when an implementation emits a wrong stream.
    report = {"source": str(args.source), "port": str(args.port), "sourceDraws": len(source), "portDraws": len(port), "errors": errors, "equal": not errors}
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    if errors:
        print("HALL_WALK_DIRECTION_BLOCKED " + json.dumps(report, ensure_ascii=False))
        return 1
    print(f"HALL_WALK_DIRECTION_OK draws={len(source)}/{len(port)} timestamps=0.04s direction=0:up,1:right,2:down,3:left")
    return 0


if __name__ == "__main__":
    sys.exit(main())

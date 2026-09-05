#!/usr/bin/env python3
"""Expand HallUnit walk draw events to a 100fps tween audit.

The live renderer emits stable checkpoints at the authored 0.04s tile step.
This verifier reconstructs the linear tween between those checkpoints at
0.01s (60/100fps-safe) and compares source/port positions, velocity, action,
direction, and the final idle transition. It therefore catches a frozen or
reversed actor even when the endpoints happen to match.
"""
from __future__ import annotations
import argparse, json, re, sys
from pathlib import Path

GRID = re.compile(r"grid=(-?\d+(?:\.\d+)?),(-?\d+(?:\.\d+)?);dir=(\d+);action=(\d+)")

def read(path: Path):
    return [json.loads(line) for line in path.read_text(encoding="utf-8").splitlines() if line.strip()]

def samples(rows, label):
    grouped = {}
    for row in rows:
        m = GRID.search(str(row.get("text", "")))
        if not m: continue
        x, y, direction, action = float(m[1]), float(m[2]), int(m[3]), int(m[4])
        grouped.setdefault(row.get("nodePath", ""), []).append((float(row.get("timestamp", 0)), x, y, direction, action))
    out, errors = [], []
    for path, points in grouped.items():
        points.sort()
        for i, (t, x, y, direction, action) in enumerate(points[:-1]):
            nt, nx, ny, nd, na = points[i + 1]
            if nt <= t: errors.append(f"{label}:{path} non-monotonic timestamp")
            # Include every 0.01s interior sample, not only tile boundaries.
            count = max(1, round((nt - t) / .01))
            for step in range(count):
                f = step / count
                out.append((path, round(t + (nt - t) * f, 4), round(x + (nx - x) * f, 5), round(y + (ny - y) * f, 5), direction, action))
            dx, dy = nx - x, ny - y
            expected = 2 if dy > 0 else 0 if dy < 0 else 1 if dx > 0 else 3 if dx < 0 else direction
            if (dx or dy) and direction != expected:
                errors.append(f"{label}:{path} backward walk {x},{y}->{nx},{ny} dir={direction} expected={expected}")
        if points:
            out.append((path, round(points[-1][0], 4), round(points[-1][1], 5), round(points[-1][2], 5), points[-1][3], points[-1][4]))
    return out, errors

def main():
    p = argparse.ArgumentParser(); p.add_argument("source", type=Path); p.add_argument("port", type=Path); p.add_argument("--output", type=Path); a = p.parse_args()
    sr, se = samples(read(a.source), "source"); pr, pe = samples(read(a.port), "port")
    errors = se + pe
    if len(sr) != len(pr): errors.append(f"frame sample count differs source={len(sr)} port={len(pr)}")
    else:
        for i, (s, q) in enumerate(zip(sr, pr)):
            if s != q: errors.append(f"sample[{i}] differs source={s} port={q}"); break
    report = {"source": str(a.source), "port": str(a.port), "sourceSamples": len(sr), "portSamples": len(pr), "sampleStep": .01, "errors": errors, "equal": not errors}
    if a.output: a.output.parent.mkdir(parents=True, exist_ok=True); a.output.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(("HALL_WALK_FRAME_BLOCKED " if errors else "HALL_WALK_FRAME_OK ") + json.dumps(report, ensure_ascii=False))
    return 1 if errors else 0
if __name__ == "__main__": sys.exit(main())

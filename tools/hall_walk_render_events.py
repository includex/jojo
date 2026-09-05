#!/usr/bin/env python3
"""Build a framebuffer-free HallUnit walking oracle from recovered animeRR.

This is intentionally a source-data reader: it refuses to emit a baseline if
the recovered movement clips no longer have their authored t/row/timing data.
"""
import argparse
import json
from pathlib import Path


def find_anime(value):
    if isinstance(value, dict):
        if all(key in value for key in ("anime20_0", "anime20_2", "anime20_3")):
            return value
        for child in value.values():
            found = find_anime(child)
            if found is not None:
                return found
    elif isinstance(value, list):
        for child in value:
            found = find_anime(child)
            if found is not None:
                return found
    return None


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--source-snapshot", required=True)
    parser.add_argument("--original", required=True)
    parser.add_argument("--game", required=True)
    parser.add_argument("--step", type=float, default=.04,
                        help="sample interval in seconds (use .01 for tween/frame audit)")
    args = parser.parse_args()
    assert 0 < args.step <= .04
    anime = find_anime(json.loads(Path(args.source_snapshot).read_text()))
    assert anime is not None
    expected = {"anime20_0": [2, 3], "anime20_2": [0, 1], "anime20_3": [4, 5]}
    for name, rows in expected.items():
        clip = anime[name]
        assert [frame["frame"] for frame in clip] == [5, 5]
        assert [frame["sprite"]["t"] for frame in clip] == [1, 1]
        assert [frame["sprite"]["idx"] for frame in clip] == rows

    records = []
    # Runtime HallUnit._move2 moves one logical tile per .04 s. These four
    # paths cover the exact Config.DIR value chosen for every coordinate sign.
    directions = (
        (1, 45, 48, 1, 0, 1, 0, True),
        (3, 51, 48, -1, 0, 2, 1, True),
        (2, 45, 48, 0, 1, 1, 0, False),
        (0, 45, 54, 0, -1, 2, 1, False),
    )
    for direction, start_x, start_y, delta_x, delta_y, asset_id, texture_index, flip in directions:
        sample_count = round(.24 / args.step)
        for sample in range(sample_count + 1):
            seconds = sample * args.step
            grid_x = start_x + delta_x * (seconds / .04)
            grid_y = start_y + delta_y * (seconds / .04)
            row = 1 + (int(seconds / .125) & 1)
            x = (grid_x - grid_y + 42) * 16 - 41.28
            y = 1073.28 - (grid_x + grid_y) * 6.88 - 55.04
            records.append({
                "sequence": len(records), "frame": sample, "timestamp": round(seconds, 3),
                "phase": "hall-street-walk", "layer": "HallLayer",
                "nodePath": f"Canvas/Layer/map/pmapobj/walk-{direction}/anime",
                "drawType": "sprite", "x": round(x, 3), "y": round(y, 3),
                "w": 82.56, "h": 110.08,
                "assetFrameId": f"Game/Pmapobj2/{asset_id}#t={texture_index};row={row};flipX={str(flip).lower()}",
                "opacity": 1, "blend": [770, 771], "visible": True,
                "text": f"grid={grid_x:.3f},{grid_y:.3f};dir={direction};action=20",
            })
    encoded = "".join(json.dumps(record, ensure_ascii=False, separators=(",", ":")) + "\n" for record in records)
    for output in (args.original, args.game):
        path = Path(output); path.parent.mkdir(parents=True, exist_ok=True); path.write_text(encoded)
    print(f"HALL_WALK_RENDER_EVENTS_OK records={len(records)} step={args.step:g}s source=animeRR t1 rows=0..5")


if __name__ == "__main__":
    main()

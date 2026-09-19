#!/usr/bin/env python3
"""Compare live opening actor frames to independently executed source movement actions.

Checks through the first dialogue only. Source actions describe nominal
interpolation, not a synchronized Cocos GPU/scheduler capture.
"""
import argparse
import hashlib
import json
from pathlib import Path


def sample(actor, elapsed):
    if elapsed >= actor["duration"] - 1e-6:
        return actor["destination"], actor["finalDirection"], False
    segment = next(s for s in actor["segments"] if elapsed < s["start"] + s["duration"])
    fraction = max(0, (elapsed - segment["start"]) / segment["duration"])
    point = [a + (b - a) * fraction for a, b in zip(segment["from"], segment["to"])]
    return point, segment["direction"], True


def verify(trace, source):
    assert source["format"] == "jojo-r00-opening-source-moves/v1"
    assert source["evidenceKind"] == "recovered-source-runtime"
    assert trace["format"] == "jojo-campaign-screen-e2e/v1"
    assert trace["completion"] == "checkpoint"
    assert trace["actualStopPoint"] == {"module": "R_00", "sceneIndex": 1}
    assert trace["stopDialoguePages"] == 1
    assert any(r["event"] == "TitleScreen:new-game-click" and r["accepted"] for r in trace["inputRecords"])
    frames, groups = trace["scenarioFrames"], source["groups"]
    assert len(groups) == 3 and frames
    assert all(b["time"] > a["time"] for a, b in zip(frames, frames[1:])), "non-monotonic live frames"
    starts = []
    for group in groups:
        ids = {a["id"] for a in group["actors"]}
        starts.append(next(i for i, f in enumerate(frames) if {a["id"] for a in f["actors"]} == ids))
    assert starts[0] == 0 and starts == sorted(set(starts))
    final_ids = {a["id"] for a in groups[-1]["actors"]}
    end = next(i for i in range(starts[-1] + 1, len(frames))
               if all(a["moveDuration"] == 0 for a in frames[i]["actors"]))
    counts = []
    for index, group in enumerate(groups):
        first, stop = starts[index], starts[index + 1] if index < 2 else end
        assert stop - first >= 3, "insufficient actual movement frames"
        counts.append(stop - first)
        began = frames[first]["time"]
        for frame in frames[first:stop]:
            elapsed = frame["time"] - began
            actual = {a["id"]: a for a in frame["actors"]}
            for expected in group["actors"]:
                actor = actual[expected["id"]]
                point, direction, moving = sample(expected, elapsed)
                label = f"group{index} actor{actor['id']} at {elapsed:.6f}s"
                assert actor["visible"], label + " hidden"
                assert max(abs(actor[k] - p) for k, p in zip(("visualX", "visualY"), point)) < .002, label + " interpolation mismatch"
                assert actor["direction"] == direction, label + " direction mismatch"
                assert actor["action"] == (20 if moving else 0), label + " action mismatch"
                assert [actor["x"], actor["y"]] == expected["origin" if moving else "destination"], label + " logical commit mismatch"
                assert abs(actor["moveDuration"] - (expected["duration"] if moving else 0)) < 1e-5, label + " duration mismatch"
                if moving:
                    assert abs(actor["moveElapsed"] - elapsed) < 1e-4, label + " clock mismatch"
        assert frames[stop]["time"] - began >= group["duration"] - 1e-5, "group resumed before last actor finished"
        assert frames[stop - 1]["time"] - began < group["duration"] + 1e-5, "group did not resume on completion frame"
        at_boundary = {a["id"]: a for a in frames[stop]["actors"]}
        for actor in group["actors"]:
            assert [at_boundary[actor["id"]]["x"], at_boundary[actor["id"]]["y"]] == actor["destination"], "next group before commit"
    assert {a["id"] for a in frames[end]["actors"]} == final_ids
    completed = {a["id"]: a for a in frames[end]["actors"]}
    for expected in groups[-1]["actors"]:
        actor = completed[expected["id"]]
        assert actor["visible"] and actor["action"] == 0 and actor["moveDuration"] == 0, "final actor not visibly idle"
        assert actor["direction"] == expected["finalDirection"], "final scripted direction mismatch"
        assert max(abs(actor[k] - p) for k, p in zip(("visualX", "visualY"), expected["destination"])) < .002, "final visual endpoint mismatch"
    dialogue = next(i for i, f in enumerate(frames) if f["playback"] == "DIALOGUE")
    assert dialogue > end and all(f["playback"] == "DELAY" for f in frames[:dialogue]), "dialogue appeared during group movement"
    wait = frames[dialogue]["time"] - frames[end]["time"]
    assert wait >= source["delaySeconds"] - 1e-5, "opening dialogue appeared before source delay"
    assert dialogue > end and frames[dialogue - 1]["time"] - frames[end]["time"] < source["delaySeconds"] + 1e-5, "opening delay exceeded completion frame"
    return {"groupFrameCounts": counts, "sourceGroupSeconds": [g["duration"] for g in groups],
            "observedDialogueWait": wait, "scope": "first three movement groups; no pixels or intra-frame callback proof"}


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("source", type=Path)
    p.add_argument("game", type=Path)
    args = p.parse_args()
    source = json.loads(args.source.read_text())
    for record in source["source"].values():
        assert hashlib.sha256(Path(record["path"]).read_bytes()).hexdigest() == record["sha256"], "stale source oracle; regenerate"
    print(json.dumps(verify(json.loads(args.game.read_text()), source)))


if __name__ == "__main__":
    main()

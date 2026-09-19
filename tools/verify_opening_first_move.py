#!/usr/bin/env python3
"""Audit observed R00 first-move frames against its recovered source contract.

Scope: actor 181's first straight move, interpolation, logical commit and the
next group's appearance. This does not observe Cocos scheduler frames, the
intra-frame idle callback, z order, sprites, or subsequent group paths.
"""
import argparse
import ast
import hashlib
import json
from pathlib import Path


def source_contract(source, hall_source):
    scene = next(n for n in ast.parse(source).body if isinstance(n, ast.FunctionDef) and n.name == "scene1")
    calls = []
    allowed = {"model.initLocalVar", "stage.clsUnit", "stage.setMenuVisible", "model.setAmbition",
               "stage.loadBg", "stage.setEventName", "stage.draw", "stage.effectSound", "stage.showUnit"}
    for statement in scene.body:
        assert isinstance(statement, ast.Expr) and isinstance(statement.value, ast.Call), "unsupported opening control flow"
        call = statement.value
        name = ast.unparse(call.func)
        calls.append(call)
        if name == "stage.unit(181).move":
            break
        assert name in allowed, f"unsupported opening call: {name}"
    show_index = next(i for i, c in enumerate(calls) if ast.unparse(c.func) == "stage.showUnit")
    show, move = calls[show_index:show_index + 2]
    assert [ast.literal_eval(a) for a in show.args] == [181, 40, 5, 2], "opening spawn contract changed"
    assert ast.unparse(move.func) == "stage.unit(181).move"
    assert [ast.literal_eval(a) for a in move.args] == [40, 15, 2], "opening move contract changed"
    # Pin the reviewed _move2 timing statement; report the full source hashes.
    assert "cc.moveTo(.04 * o, u.x, u.y)" in hall_source, "re-review recovered movement timing"
    return .4


def verify(trace, duration=.4):
    assert trace["format"] == "jojo-campaign-screen-e2e/v1"
    assert trace["completion"] == "checkpoint"
    assert trace["actualStopPoint"] == {"module": "R_00", "sceneIndex": 1}
    assert any(r["event"] == "TitleScreen:new-game-click" and r["accepted"] for r in trace["inputRecords"])
    frames = trace["scenarioFrames"]
    assert len(frames) >= 3, "missing live motion frames"
    actor = lambda f: next(a for a in f["actors"] if a["id"] == 181)
    boundary = next((i for i, f in enumerate(frames) if any(a["id"] in (0, 157) for a in f["actors"])), None)
    assert boundary is not None and boundary >= 2, "missing isolated movement or group boundary"
    moving = frames[:boundary]
    start = moving[0]["time"]
    assert abs(actor(moving[0])["visualY"] - 5) < 1e-4, "initial movement frame missing"
    assert actor(moving[0])["moveElapsed"] == 0
    assert all(b["time"] > a["time"] for a, b in zip(frames[:boundary], frames[1:boundary+1]))
    for f in moving:
        a = actor(f)
        assert len(f["actors"]) == 1 and a["visible"], "group appeared before first move completed"
        assert (a["x"], a["y"]) == (40, 5), "logical position committed before completion"
        assert (a["direction"], a["action"]) == (2, 20), "wrong movement direction/action"
        assert abs(a["moveDuration"] - duration) < 1e-5
        elapsed = f["time"] - start
        assert abs(a["moveElapsed"] - elapsed) < 1e-4, "movement clock differs from observed frame time"
        assert abs(a["visualX"] - 40) < 1e-4
        assert abs(a["visualY"] - (5 + 10 * elapsed / duration)) < 1e-3, "non-source interpolation"
    assert any(5 < actor(f)["visualY"] < 15 for f in moving), "no actual intermediate positions observed"
    after = frames[boundary]
    a = actor(after)
    assert (a["x"], a["y"]) == (40, 15), "next group appeared before logical completion"
    assert abs(a["visualX"] - 40) < 1e-4 and abs(a["visualY"] - 15) < 1e-4
    assert {u["id"] for u in after["actors"]} == {181, 0, 157}
    assert all(u["visible"] for u in after["actors"]), "next group is present but hidden"
    # Completion is quantized to the actual observed frame, not a made-up fps.
    assert moving[-1]["time"] - start < duration + 1e-5
    assert after["time"] - start >= duration - 1e-5, "script resumed before authored duration"
    return {"observedMovingFrames": len(moving), "nominalSeconds": duration,
            "observedCompletionSeconds": after["time"] - start,
            "scope": "first straight move and group appearance; no pixel or intra-frame callback claim"}


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("trace", type=Path)
    p.add_argument("--source-root", type=Path, default=Path("../jojo_mobile/sgccz-desktop"))
    args = p.parse_args()
    source = (args.source_root / "decompiled-python/R_00.py").read_text()
    hall = (args.source_root / "recovered-js/modules/game-data/HallUnit.js").read_text()
    report = verify(json.loads(args.trace.read_text()), source_contract(source, hall))
    report["sourceHashes"] = {"R_00": hashlib.sha256(source.encode()).hexdigest(), "HallUnit": hashlib.sha256(hall.encode()).hexdigest()}
    print(json.dumps(report, ensure_ascii=False))


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""Compare the first three R_00 scene1 dialogue pages to recovered source.

This is a bounded semantic check, not pixel or movement/callback parity.
Hall's DialogueLayer._next breaks at a speaker marker or three lines. The opening two
literal say calls have explicit speakers and fewer than three lines per page.
Fail closed if this source prefix changes beyond that supported shape.
"""
import argparse
import ast
import hashlib
import json
from pathlib import Path

# Only non-dialogue calls observed in the current straight-line source prefix.
# Unknown calls may hide control flow or dialogue and must not be skipped.
PREFIX_CALLS = {
    "model.initLocalVar", "model.setAmbition", "stage.clsUnit",
    "stage.setMenuVisible", "stage.loadBg", "stage.setEventName", "stage.draw",
    "stage.effectSound", "stage.showUnit", "stage.showUnits", "stage.unitsMove",
    "stage.delay", "stage.unit(181).move", "stage.unit(181).setAction",
    "stage.unit(0).setAction", "stage.unit(182).setAction", "stage.unit(157).setAction",
}


def source_pages(source):
    scene = next(n for n in ast.parse(source).body
                 if isinstance(n, ast.FunctionDef) and n.name == "scene1")
    pages = []
    for statement in scene.body:
        if not isinstance(statement, ast.Expr) or not isinstance(statement.value, ast.Call):
            raise AssertionError("unsupported control flow in opening source prefix")
        call = statement.value
        name = ast.unparse(call.func)
        if name != "stage.say":
            assert name in PREFIX_CALLS, f"unsupported call in opening prefix: {name}"
            continue
        raw = ast.literal_eval(call.args[0])
        speaker, lines = None, []
        for line in raw.split("\n") + ["&END"]:
            if line.startswith("&"):
                if lines:
                    assert speaker is not None and len(lines) <= 3
                    pages.append({"speakerId": speaker, "text": "\n".join(lines)})
                speaker, lines = line[1:], []
            else:
                lines.append(line)
        if len(pages) >= 3:
            assert len(pages) == 3, "opening say boundary changed"
            return pages
    raise AssertionError("source has fewer than three opening pages")


def verify(trace, expected):
    assert trace["format"] == "jojo-campaign-screen-e2e/v1"
    assert trace["completion"] == "checkpoint"
    assert trace["actualStopPoint"] == {"module": "R_00", "sceneIndex": 1}
    assert trace["stopDialoguePages"] == 3
    assert any(r["event"] == "TitleScreen:new-game-click" and r["accepted"]
               for r in trace["inputRecords"]), "missing production new-game input"
    pages = trace["dialoguePages"]
    assert len(pages) == 3, f"expected 3 observed pages, got {len(pages)}"
    revisions = []
    for index, (actual, wanted) in enumerate(zip(pages, expected), 1):
        assert actual["module"] == "R_00" and actual["sceneIndex"] == 1
        for key, value in wanted.items():
            assert actual[key] == value, f"page {index} {key}: {actual[key]!r} != {value!r}"
        revisions.append(actual["revision"])
    assert all(b > a for a, b in zip(revisions, revisions[1:])), "page revisions did not advance"
    return {"verifiedPages": 3, "scope": "R_00 scene1 speaker/text prefix; no visual or timing claim"}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("trace", type=Path)
    parser.add_argument("--source", type=Path, default=Path("../jojo_mobile/sgccz-desktop/decompiled-python/R_00.py"))
    args = parser.parse_args()
    source = args.source.read_text()
    report = verify(json.loads(args.trace.read_text()), source_pages(source))
    report["sourceSha256"] = hashlib.sha256(source.encode()).hexdigest()
    print(json.dumps(report, ensure_ascii=False))


if __name__ == "__main__":
    main()

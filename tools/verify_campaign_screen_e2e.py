#!/usr/bin/env python3
import json
import pathlib
import sys

trace_path = pathlib.Path(sys.argv[1])
battle_path = pathlib.Path(sys.argv[2])
trace = json.loads(trace_path.read_text(encoding="utf-8"))
battle = json.loads(battle_path.read_text(encoding="utf-8"))
expected = [
    "TitleScreen",
    "ScenarioPreviewScreen:R_00",
    "ScenarioPreviewScreen:R_00:scene0",
    "ScenarioPreviewScreen:R_00:scene1",
    "ScenarioPreviewScreen:R_00:scene2",
    "ScenarioPreviewScreen:R_00:scene3",
    "BattleLayer:S_00",
    "BattleLayer:S_00:scene1",
    "BattleLayer:S_00:result-scene1",
    "BattleLayer:S_00:scene2",
    "BattleLayer:S_00:save-prompt",
    "ScenarioPreviewScreen:R_01",
    "ScenarioPreviewScreen:R_01:scene0",
    "ScenarioPreviewScreen:R_01:scene1",
]
assert trace["format"] == "jojo-campaign-screen-e2e/v1", trace
assert trace["route"] == expected, trace["route"]
assert trace["screenClassesVerified"] is True, trace
assert trace["transitionEnterCount"] == 0, trace
# Initial battle scene1 contains the authored startOper hand-off; accepting a
# tactical move before it was the former port-only bootstrap shortcut.
assert trace["playerMoveBeforeScene1"] is False, trace
assert "->" in trace["committedPlayerMove"], trace
assert trace["campaignStages"] == [0, 1, 2], trace
assert "TitleScreen:new-game-click" in trace["inputs"], trace["inputs"]
assert not any("BattlePreparationScreen" in value for value in trace["route"]), trace["route"]
assert not any(value.endswith(":transition") for value in trace["inputs"]), trace["inputs"]
assert "S_00:auto-battle-confirm" in trace["inputs"], trace["inputs"]
assert "S_00:save-prompt-no" in trace["inputs"], trace["inputs"]
records = trace.get("inputRecords")
assert isinstance(records, list), "missing auditable inputRecords"


def accepted_transition(event, before, after):
    matches = [record for record in records if record.get("event") == event]
    assert matches, (event, records)
    for record in matches:
        if record.get("accepted") is not True:
            continue
        before_state = dict(field.split("=", 1) for field in record.get("before", "").split(";") if "=" in field)
        after_state = dict(field.split("=", 1) for field in record.get("after", "").split(";") if "=" in field)
        if all(before_state.get(key) == value for key, value in before.items()) and all(
            after_state.get(key) == value for key, value in after.items()
        ):
            return
    raise AssertionError((event, before, after, matches))


accepted_transition(
    "S_00:auto-battle-confirm",
    {"autoBattleOverlay": "PROMPT", "collocation": "false"},
    {"autoBattleOverlay": "TUOGUAN", "collocation": "true"},
)
accepted_transition(
    "S_00:save-prompt-no",
    {"savePromptOpen": "true"},
    {"savePromptOpen": "false"},
)
assert battle["format"] == "jojo-yingchuan-full-battle-trace/v1", battle.get("format")
assert battle["reason"] == "battle-end", battle.get("reason")
assert battle["summary"]["end"] is True, battle["summary"]
assert battle["summary"]["outcome"] == "PLAYER_VICTORY", battle["summary"]
assert len(battle["frames"]) > 100, len(battle["frames"])
collocation = [frame["collocation"] for frame in battle["frames"]]
assert False in collocation and True in collocation, "entrusted battle was not enabled through live UI"
assert collocation.index(True) > collocation.index(False), "collocation did not transition false -> true"
positions = {}
moved = False
for frame in battle["frames"]:
    if not frame["collocation"] or frame["camp"] != 0:
        continue
    for unit in frame["units"]:
        if unit[2] != 0:
            continue
        key = (unit[1], unit[2])
        pos = (unit[3], unit[4])
        if key in positions and positions[key] != pos:
            moved = True
        positions[key] = pos
assert moved, "full battle trace contains no committed PLAYER movement after collocation"
print("CAMPAIGN_SCREEN_E2E_VERIFIED: " + " -> ".join(expected) + "; transition Enter=0")

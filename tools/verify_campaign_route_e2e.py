#!/usr/bin/env python3
"""Validate a production-screen campaign trace.

The trace is emitted while pointer/key events are sent to each production
screen's installed InputProcessor. An authored branch may jump forward, but
it may not omit the R -> matching S -> result/save -> next R lifecycle.
"""

from __future__ import annotations

import json
import pathlib
import re
import sys
from typing import Any


SCENARIO_SCREEN = re.compile(r"^ScenarioPreviewScreen:(R_[0-9]{2})$")
BATTLE_SCREEN = re.compile(r"^BattleLayer:(S_[0-9]{2})$")
# Recovered R scripts whose stage.jumpScene() may replace Hall directly,
# without entering the same-numbered battle. Keep this source-derived and
# closed so an arbitrary missing S screen cannot pass as a branch jump.
AUTHORED_HALL_JUMPS = {
    ("R_03", "R_04"),
    ("R_07", "R_09"),
    ("R_08", "R_09"),
    ("R_27", "R_28"),
    ("R_32", "R_46"),
    ("R_36", "R_38"),
}
# ScenarioStage.jumpScene() calls recovered from the battle scripts.  Unlike
# the ordinary S_N -> R_(N+1) result route, only these battles may skip a
# numbered Hall checkpoint.  Keep this list closed: accepting every forward
# S -> R edge would let a broken S_00 -> R_58 replacement verify the entire
# campaign without traversing it.
AUTHORED_BATTLE_JUMPS = {
    ("S_27", "R_29"),
    ("S_27", "R_30"),
    ("S_28", "R_30"),
    ("S_45", "R_58"),
}


def _module_stage(module: str) -> int:
    number = int(module[2:])
    return number * 2 + (1 if module.startswith("S_") else 0)


def _screen_modules(route: list[str]) -> list[str]:
    modules = []
    for entry in route:
        match = SCENARIO_SCREEN.fullmatch(entry) or BATTLE_SCREEN.fullmatch(entry)
        if match:
            modules.append(match.group(1))
    return modules


def transition_evidence(modules: list[str]) -> list[dict[str, Any]]:
    """Describe every observed production R/S replacement without hiding jumps."""
    evidence: list[dict[str, Any]] = []
    for source, target in zip(modules, modules[1:]):
        source_number = int(source[2:])
        target_number = int(target[2:])
        authored_hall_jump = (source, target) in AUTHORED_HALL_JUMPS
        authored_battle_jump = (source, target) in AUTHORED_BATTLE_JUMPS
        if source.startswith("R_") and target == f"S_{source_number:02d}":
            kind = "scenario-battle"
        elif authored_hall_jump:
            kind = "authored-hall-forward-jump"
        elif source.startswith("S_") and target.startswith("R_") and target_number == source_number + 1:
            kind = "battle-next-scenario"
        elif authored_battle_jump:
            kind = "battle-forward-branch"
        else:
            kind = "invalid"
        evidence.append({
            "from": source,
            "to": target,
            "kind": kind,
            "stageDelta": _module_stage(target) - _module_stage(source),
            "authored": authored_hall_jump or authored_battle_jump,
        })
    return evidence


def _ordered(route: list[str], values: list[str], before: int) -> None:
    positions = [route.index(value) for value in values]
    assert positions == sorted(positions) and positions[-1] < before, (values, positions)


def _observable_state(record: dict[str, Any], side: str) -> dict[str, str]:
    value = record.get(side)
    assert isinstance(value, str) and value, (side, record)
    return dict(field.split("=", 1) for field in value.split(";") if "=" in field)


def _accepted_input(
    records: list[dict[str, Any]],
    event: str,
    before: dict[str, str] | None = None,
    after: dict[str, str] | None = None,
) -> None:
    """Require a real accepted dispatch, optionally with a visible state transition."""
    matching = [record for record in records if record.get("event") == event]
    assert matching, (event, records)
    for record in matching:
        if record.get("accepted") is not True:
            continue
        before_state = _observable_state(record, "before")
        after_state = _observable_state(record, "after")
        if (before is None or all(before_state.get(key) == value for key, value in before.items())) and (
            after is None or all(after_state.get(key) == value for key, value in after.items())
        ):
            return
    raise AssertionError((event, before, after, matching))


def validate(trace: dict[str, Any], stop_module: str, stop_scene: int) -> dict[str, Any]:
    assert re.fullmatch(r"R_(?:[0-5][0-9])", stop_module) and int(stop_module[2:]) <= 58, stop_module
    assert stop_scene >= 0, stop_scene
    assert trace["format"] == "jojo-campaign-screen-e2e/v1", trace.get("format")
    assert trace["screenClassesVerified"] is True, trace
    assert trace["transitionEnterCount"] == 0, trace
    requested_stop = {"module": stop_module, "sceneIndex": stop_scene}
    assert trace["stopPoint"] == requested_stop, trace["stopPoint"]

    # v1 traces created before checkpoint batching did not distinguish the
    # requested checkpoint from the production screen actually reached.
    actual_stop = trace.get("actualStopPoint", requested_stop)
    assert isinstance(actual_stop, dict), actual_stop
    actual_module = actual_stop.get("module")
    actual_scene = actual_stop.get("sceneIndex")
    assert re.fullmatch(r"R_(?:[0-5][0-9])", str(actual_module)) and int(str(actual_module)[2:]) <= 58, actual_stop
    assert isinstance(actual_scene, int) and actual_scene >= 0, actual_stop
    completion = trace.get("completion", "checkpoint")
    assert completion in {"checkpoint", "forward-overshoot"}, completion

    route = trace["route"]
    inputs = trace["inputs"]
    input_records = trace.get("inputRecords")
    assert isinstance(input_records, list), "missing auditable inputRecords"
    assert all(isinstance(record, dict) for record in input_records), input_records
    assert route and route[0] == "TitleScreen", route
    assert route[-1] == f"ScenarioPreviewScreen:{actual_module}:scene{actual_scene}", route
    assert not any(value.endswith(":transition") for value in inputs), inputs

    modules = _screen_modules(route)
    assert modules and modules[0] == "R_00", modules
    assert modules[-1] == actual_module, modules
    assert len(modules) == len(set(modules)), f"production screen revisited: {modules}"
    transitions = transition_evidence(modules)
    for index, module in enumerate(modules[:-1]):
        following = modules[index + 1]
        number = int(module[2:])
        if module.startswith("R_"):
            assert following == f"S_{number:02d}" or (module, following) in AUTHORED_HALL_JUMPS, (
                module,
                following,
                modules,
            )
        else:
            assert following == f"R_{number + 1:02d}" or (module, following) in AUTHORED_BATTLE_JUMPS, (
                module,
                following,
                modules,
            )
    assert not any(row["kind"] == "invalid" for row in transitions), transitions

    checkpoint_status = "reached"
    overshoot_transition = None
    if completion == "checkpoint":
        assert actual_module == stop_module and actual_scene >= stop_scene, (requested_stop, actual_stop)
        requested_marker = f"ScenarioPreviewScreen:{stop_module}:scene{stop_scene}"
        assert requested_marker in route, (requested_marker, route)
    else:
        assert _module_stage(str(actual_module)) > _module_stage(stop_module), (requested_stop, actual_stop)
        # The skipped checkpoint must lie strictly inside a source-authorized
        # Hall or battle replacement observed in this trace. Merely arriving
        # at a later screen is not enough to turn a routing bug into an
        # accepted branch.
        candidates = [
            row for row in transitions
            if row["authored"] is True and "forward" in str(row["kind"])
            and _module_stage(str(row["from"])) < _module_stage(stop_module) < _module_stage(str(row["to"]))
            and row["to"] == actual_module
        ]
        assert len(candidates) == 1, (requested_stop, actual_stop, transitions)
        overshoot_transition = candidates[0]
        checkpoint_status = "authored-forward-jump-overshoot"

    expected_stages = [_module_stage(module) for module in modules]
    assert trace["campaignStages"] == expected_stages, (trace["campaignStages"], expected_stages)

    scenario_modules = [module for module in modules if module.startswith("R_")]
    battle_modules = [module for module in modules if module.startswith("S_")]
    for module in scenario_modules:
        base = route.index(f"ScenarioPreviewScreen:{module}")
        scene0 = route.index(f"ScenarioPreviewScreen:{module}:scene0")
        assert base < scene0, (module, base, scene0)

    for module in battle_modules:
        base = route.index(f"BattleLayer:{module}")
        next_screen = min(
            (index for index in range(base + 1, len(route)) if SCENARIO_SCREEN.fullmatch(route[index])),
            default=len(route),
        )
        markers = [
            f"BattleLayer:{module}:scene1",
            f"BattleLayer:{module}:result-scene1",
            f"BattleLayer:{module}:scene2",
            f"BattleLayer:{module}:save-prompt",
        ]
        _ordered(route, markers, next_screen)
        assert f"{module}:auto-battle-confirm" in inputs, (module, inputs)
        assert f"{module}:save-prompt-no" in inputs, (module, inputs)
        _accepted_input(input_records, f"{module}:auto-battle-confirm")
        _accepted_input(input_records, f"{module}:save-prompt-no")

    # Labels alone were formerly sufficient evidence, even when the installed
    # InputProcessor rejected the tap.  S_00 is the bootstrap proof: require
    # both acceptance and the observed control transitions caused by the live
    # UI rather than merely a driver label.
    if "S_00" in battle_modules:
        _accepted_input(
            input_records,
            "S_00:auto-battle-confirm",
            before={"autoBattleOverlay": "PROMPT", "collocation": "false"},
            after={"autoBattleOverlay": "TUOGUAN", "collocation": "true"},
        )
        _accepted_input(
            input_records,
            "S_00:save-prompt-no",
            before={"savePromptOpen": "true"},
            after={"savePromptOpen": "false"},
        )

    preparations = trace.get("battlePreparations", [])
    preparation_battles = set()
    for preparation in preparations:
        transition, counts = preparation.split(":", 1)
        return_scenario, source_scenario = transition.split("->", 1)
        selected, limits = counts.split("/", 1)
        minimum, maximum = limits.split("-", 1)
        assert int(minimum) <= int(selected) <= int(maximum), preparation
        assert source_scenario in battle_modules, (preparation, battle_modules)
        assert return_scenario in scenario_modules, (preparation, scenario_modules)
        assert source_scenario not in preparation_battles, preparations
        preparation_battles.add(source_scenario)
        screen = f"BattlePreparationScreen:{return_scenario}->{source_scenario}"
        start_input = f"{source_scenario}:preparation-start"
        battle = f"BattleLayer:{source_scenario}"
        assert screen in route, (screen, route)
        assert start_input in inputs, (start_input, inputs)
        assert route.index(screen) < route.index(battle), preparation

    if "S_01" in battle_modules:
        assert preparations, "R_01 -> S_01 did not traverse BattlePreparationScreen"
        assert trace.get("sawR01DepartureDialogue") is True, trace
        hall_button = "ScenarioPreviewScreen:R_01:hall-battle-button"
        scene8 = "ScenarioPreviewScreen:R_01:scene8"
        assert hall_button in route, route
        assert scene8 in route, route
        assert "R_01:hall-battle-button" in inputs, inputs
        assert route.index(hall_button) < route.index(scene8), route

    return {
        "scenarioModules": scenario_modules,
        "battleModules": battle_modules,
        "numberedModules": modules,
        "numberedCoverage": len(modules),
        "checkpointStatus": checkpoint_status,
        "actualStopPoint": actual_stop,
        "transitions": transitions,
        "forwardJumps": [row for row in transitions if "forward" in str(row["kind"])],
        "overshootTransition": overshoot_transition,
    }


def main(argv: list[str]) -> int:
    if len(argv) != 4:
        raise SystemExit("usage: verify_campaign_route_e2e.py TRACE STOP_MODULE STOP_SCENE")
    trace = json.loads(pathlib.Path(argv[1]).read_text(encoding="utf-8"))
    coverage = validate(trace, argv[2], int(argv[3]))
    print(
        "CAMPAIGN_ROUTE_E2E_VERIFIED: "
        f"stop={argv[2]}:scene{argv[3]}; status={coverage['checkpointStatus']}; "
        f"actual={coverage['actualStopPoint']['module']}:scene{coverage['actualStopPoint']['sceneIndex']}; "
        f"stages={trace['campaignStages']}; "
        f"R={len(coverage['scenarioModules'])}; S={len(coverage['battleModules'])}; "
        f"preparations={len(trace.get('battlePreparations', []))}"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv))

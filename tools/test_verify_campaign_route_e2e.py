import importlib.util
import pathlib
import unittest


SCRIPT = pathlib.Path(__file__).with_name("verify_campaign_route_e2e.py")
SPEC = importlib.util.spec_from_file_location("verify_campaign_route_e2e", SCRIPT)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
SPEC.loader.exec_module(MODULE)


def input_records(inputs):
    records = []
    for event in inputs:
        before = "screen=BattleScreen"
        after = "screen=BattleScreen"
        if event == "S_00:auto-battle-confirm":
            before += ";autoBattleOverlay=PROMPT;collocation=false"
            after += ";autoBattleOverlay=TUOGUAN;collocation=true"
        elif event.endswith(":auto-battle-confirm"):
            before += ";autoBattleOverlay=PROMPT"
            after += ";autoBattleOverlay=TUOGUAN"
        elif event.endswith(":save-prompt-no"):
            before += ";savePromptOpen=true"
            after += ";savePromptOpen=false"
        records.append({"event": event, "accepted": True, "before": before, "after": after})
    return records


def valid_trace():
    route = [
        "TitleScreen",
        "ScenarioScreen:R_00", "ScenarioScreen:R_00:scene0",
        "ScenarioScreen:R_00:scene1", "ScenarioScreen:R_00:scene2",
        "ScenarioScreen:R_00:scene3",
        "BattleScreen:S_00", "BattleScreen:S_00:scene1",
        "BattleScreen:S_00:result-scene1", "BattleScreen:S_00:scene2",
        "BattleScreen:S_00:save-prompt",
        "ScenarioScreen:R_01", "ScenarioScreen:R_01:scene0",
        "ScenarioScreen:R_01:hall-battle-button", "ScenarioScreen:R_01:scene8",
        "BattlePreparationScreen:R_01->S_01",
        "BattleScreen:S_01", "BattleScreen:S_01:scene1",
        "BattleScreen:S_01:result-scene1", "BattleScreen:S_01:scene2",
        "BattleScreen:S_01:save-prompt",
        "ScenarioScreen:R_02", "ScenarioScreen:R_02:scene0",
        "ScenarioScreen:R_02:scene1",
    ]
    inputs = [
        "S_00:auto-battle-confirm", "S_00:save-prompt-no",
        "R_01:hall-battle-button", "S_01:preparation-start",
        "S_01:auto-battle-confirm", "S_01:save-prompt-no",
    ]
    return {
        "format": "jojo-campaign-screen-e2e/v1",
        "screenClassesVerified": True,
        "transitionEnterCount": 0,
        "stopPoint": {"module": "R_02", "sceneIndex": 1},
        "route": route,
        "inputs": inputs,
        "inputRecords": input_records(inputs),
        "campaignStages": [0, 1, 2, 3, 4],
        "battlePreparations": ["R_01->S_01:4/4-7"],
        "sawR01DepartureDialogue": True,
        "actualStopPoint": {"module": "R_02", "sceneIndex": 1},
        "completion": "checkpoint",
    }


def r32_to_r46_trace(stop="R_46", completion="checkpoint"):
    route = ["TitleScreen"]
    inputs = []
    stages = []
    for number in range(32):
        scenario = f"R_{number:02d}"
        battle = f"S_{number:02d}"
        route += [f"ScenarioScreen:{scenario}", f"ScenarioScreen:{scenario}:scene0"]
        stages.append(number * 2)
        if number == 1:
            route += [
                "ScenarioScreen:R_01:hall-battle-button",
                "ScenarioScreen:R_01:scene8",
                "BattlePreparationScreen:R_01->S_01",
            ]
            inputs += ["R_01:hall-battle-button", "S_01:preparation-start"]
        route += [
            f"BattleScreen:{battle}", f"BattleScreen:{battle}:scene1",
            f"BattleScreen:{battle}:result-scene1", f"BattleScreen:{battle}:scene2",
            f"BattleScreen:{battle}:save-prompt",
        ]
        inputs += [f"{battle}:auto-battle-confirm", f"{battle}:save-prompt-no"]
        stages.append(number * 2 + 1)
    route += [
        "ScenarioScreen:R_32", "ScenarioScreen:R_32:scene0",
        "ScenarioScreen:R_46", "ScenarioScreen:R_46:scene0",
    ]
    actual_scene = 0
    if completion == "checkpoint":
        route += ["ScenarioScreen:R_46:scene1"]
        actual_scene = 1
    stages += [64, 92]
    return {
        "format": "jojo-campaign-screen-e2e/v1",
        "screenClassesVerified": True,
        "transitionEnterCount": 0,
        "stopPoint": {"module": stop, "sceneIndex": 1},
        "actualStopPoint": {"module": "R_46", "sceneIndex": actual_scene},
        "completion": completion,
        "route": route,
        "inputs": inputs,
        "inputRecords": input_records(inputs),
        "campaignStages": stages,
        "battlePreparations": ["R_01->S_01:4/4-7"],
        "sawR01DepartureDialogue": True,
    }


class CampaignRouteVerifierTest(unittest.TestCase):
    def test_accepts_prebattle_r00_checkpoint_without_move_or_battle(self):
        trace = {
            "format": "jojo-campaign-screen-e2e/v1",
            "screenClassesVerified": True,
            "transitionEnterCount": 0,
            "stopPoint": {"module": "R_00", "sceneIndex": 1},
            "actualStopPoint": {"module": "R_00", "sceneIndex": 1},
            "completion": "checkpoint",
            "route": [
                "TitleScreen",
                "ScenarioScreen:R_00",
                "ScenarioScreen:R_00:scene0",
                "ScenarioScreen:R_00:scene1",
            ],
            "inputs": ["TitleScreen:new-game-click", "R_00:dialogue"],
            "inputRecords": input_records(["TitleScreen:new-game-click", "R_00:dialogue"]),
            "campaignStages": [0],
            "battlePreparations": [],
            "sawR01DepartureDialogue": False,
            "committedPlayerMove": None,
        }
        coverage = MODULE.validate(trace, "R_00", 1)
        self.assertEqual("reached", coverage["checkpointStatus"])
        self.assertEqual([], coverage["battleModules"])

    def test_accepts_complete_production_lifecycle(self):
        coverage = MODULE.validate(valid_trace(), "R_02", 1)
        self.assertEqual(["R_00", "R_01", "R_02"], coverage["scenarioModules"])
        self.assertEqual(["S_00", "S_01"], coverage["battleModules"])

    def test_rejects_s00_legacy_label_when_the_dispatch_was_rejected(self):
        trace = valid_trace()
        next(record for record in trace["inputRecords"] if record["event"] == "S_00:auto-battle-confirm")["accepted"] = False
        with self.assertRaises(AssertionError):
            MODULE.validate(trace, "R_02", 1)

    def test_rejects_s00_accepted_label_without_the_visible_transition(self):
        trace = valid_trace()
        next(record for record in trace["inputRecords"] if record["event"] == "S_00:save-prompt-no")["after"] = (
            "screen=BattleScreen;savePromptOpen=true"
        )
        with self.assertRaises(AssertionError):
            MODULE.validate(trace, "R_02", 1)

    def test_rejects_skipped_matching_battle(self):
        trace = valid_trace()
        trace["route"] = [entry for entry in trace["route"] if "S_01" not in entry]
        trace["campaignStages"] = [0, 1, 2, 4]
        with self.assertRaises(AssertionError):
            MODULE.validate(trace, "R_02", 1)

    def test_rejects_missing_result_scene_even_if_battle_routes_forward(self):
        trace = valid_trace()
        trace["route"].remove("BattleScreen:S_01:result-scene1")
        with self.assertRaises((AssertionError, ValueError)):
            MODULE.validate(trace, "R_02", 1)

    def test_rejects_arbitrary_battle_forward_jump(self):
        trace = valid_trace()
        trace["route"][-3:] = [
            "ScenarioScreen:R_46",
            "ScenarioScreen:R_46:scene0",
            "ScenarioScreen:R_46:scene1",
        ]
        trace["stopPoint"] = {"module": "R_46", "sceneIndex": 1}
        trace["actualStopPoint"] = {"module": "R_46", "sceneIndex": 1}
        trace["campaignStages"][-1] = 92
        with self.assertRaises(AssertionError):
            MODULE.validate(trace, "R_46", 1)

    def test_rejects_checkpoint_when_requested_scene_marker_is_missing(self):
        trace = {
            "format": "jojo-campaign-screen-e2e/v1",
            "screenClassesVerified": True,
            "transitionEnterCount": 0,
            "stopPoint": {"module": "R_00", "sceneIndex": 1},
            "actualStopPoint": {"module": "R_00", "sceneIndex": 8},
            "completion": "checkpoint",
            "route": [
                "TitleScreen", "ScenarioScreen:R_00",
                "ScenarioScreen:R_00:scene0",
                "ScenarioScreen:R_00:scene8",
            ],
            "inputs": [],
            "campaignStages": [0],
            "battlePreparations": [],
            "sawR01DepartureDialogue": False,
        }
        with self.assertRaises(AssertionError):
            MODULE.validate(trace, "R_00", 1)

    def test_accepts_only_source_authorized_battle_forward_jump(self):
        evidence = MODULE.transition_evidence(["S_45", "R_58"])
        self.assertEqual(
            {"from": "S_45", "to": "R_58", "kind": "battle-forward-branch", "stageDelta": 25, "authored": True},
            evidence[0],
        )

    def test_accepts_source_r32_direct_hall_jump_but_not_arbitrary_missing_battle(self):
        trace = r32_to_r46_trace()
        coverage = MODULE.validate(trace, "R_46", 1)
        self.assertEqual("R_46", coverage["numberedModules"][-1])

        trace["route"][-3:] = [
            "ScenarioScreen:R_47", "ScenarioScreen:R_47:scene0",
            "ScenarioScreen:R_47:scene1",
        ]
        trace["campaignStages"][-1] = 94
        trace["stopPoint"] = {"module": "R_47", "sceneIndex": 1}
        trace["actualStopPoint"] = {"module": "R_47", "sceneIndex": 1}
        with self.assertRaises(AssertionError):
            MODULE.validate(trace, "R_47", 1)

    def test_classifies_source_authorized_checkpoint_overshoot(self):
        trace = r32_to_r46_trace("R_33", "forward-overshoot")
        coverage = MODULE.validate(trace, "R_33", 1)
        self.assertEqual("authored-forward-jump-overshoot", coverage["checkpointStatus"])
        self.assertIn(
            {"from": "R_32", "to": "R_46", "kind": "authored-hall-forward-jump", "stageDelta": 28, "authored": True},
            coverage["forwardJumps"],
        )

    def test_rejects_overshoot_not_bracketed_by_an_observed_authored_jump(self):
        trace = valid_trace()
        trace["stopPoint"] = {"module": "R_01", "sceneIndex": 1}
        trace["actualStopPoint"] = {"module": "R_02", "sceneIndex": 1}
        trace["completion"] = "forward-overshoot"
        with self.assertRaises(AssertionError):
            MODULE.validate(trace, "R_01", 1)

    def test_rejects_stage_list_that_hides_a_jump_stage_bug(self):
        trace = valid_trace()
        trace["campaignStages"] = [0, 1, 2, 3]
        with self.assertRaises(AssertionError):
            MODULE.validate(trace, "R_02", 1)


if __name__ == "__main__":
    unittest.main()

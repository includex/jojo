#!/usr/bin/env python3

import contextlib
import importlib.util
import io
import json
from pathlib import Path
import tempfile
import unittest


SCRIPT = Path(__file__).with_name("run_port_campaign_checkpoint_batch.py")
SPEC = importlib.util.spec_from_file_location("campaign_batch", SCRIPT)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
SPEC.loader.exec_module(MODULE)


def input_records(inputs):
    records = []
    for event in inputs:
        before = "screen=BattleLayer"
        after = "screen=BattleLayer"
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


def route_trace(stop="R_01", actual=None, completion="checkpoint"):
    actual = actual or stop
    if actual == "R_46":
        route = ["TitleScreen"]
        inputs = []
        stages = []
        for number in range(32):
            scenario = f"R_{number:02d}"
            battle = f"S_{number:02d}"
            route += [f"ScenarioPreviewScreen:{scenario}", f"ScenarioPreviewScreen:{scenario}:scene0"]
            stages.append(number * 2)
            if number == 1:
                route += [
                    "ScenarioPreviewScreen:R_01:hall-battle-button",
                    "ScenarioPreviewScreen:R_01:scene8",
                    "BattlePreparationScreen:R_01->S_01",
                ]
                inputs += ["R_01:hall-battle-button", "S_01:preparation-start"]
            route += [
                f"BattleLayer:{battle}", f"BattleLayer:{battle}:scene1",
                f"BattleLayer:{battle}:result-scene1", f"BattleLayer:{battle}:scene2",
                f"BattleLayer:{battle}:save-prompt",
            ]
            inputs += [f"{battle}:auto-battle-confirm", f"{battle}:save-prompt-no"]
            stages.append(number * 2 + 1)
        route += [
            "ScenarioPreviewScreen:R_32", "ScenarioPreviewScreen:R_32:scene0",
            "ScenarioPreviewScreen:R_46", "ScenarioPreviewScreen:R_46:scene0",
        ]
        if completion == "checkpoint":
            route += ["ScenarioPreviewScreen:R_46:scene1"]
            actual_scene = 1
        else:
            actual_scene = 0
        stages += [64, 92]
        return {
            "format": "jojo-campaign-screen-e2e/v1",
            "screenClassesVerified": True,
            "transitionEnterCount": 0,
            "stopPoint": {"module": stop, "sceneIndex": 1},
            "actualStopPoint": {"module": actual, "sceneIndex": actual_scene},
            "completion": completion,
            "route": route,
            "inputs": inputs,
            "inputRecords": input_records(inputs),
            "campaignStages": stages,
            "battlePreparations": ["R_01->S_01:4/4-7"],
            "sawR01DepartureDialogue": True,
        }
    route = [
        "TitleScreen",
        "ScenarioPreviewScreen:R_00", "ScenarioPreviewScreen:R_00:scene0",
        "BattleLayer:S_00", "BattleLayer:S_00:scene1",
        "BattleLayer:S_00:result-scene1", "BattleLayer:S_00:scene2",
        "BattleLayer:S_00:save-prompt",
    ]
    stages = [0, 1]
    route += [
        f"ScenarioPreviewScreen:{actual}",
        f"ScenarioPreviewScreen:{actual}:scene0",
        f"ScenarioPreviewScreen:{actual}:scene1",
    ]
    stages += [int(actual[2:]) * 2]
    actual_scene = 1
    inputs = ["S_00:auto-battle-confirm", "S_00:save-prompt-no"]
    return {
        "format": "jojo-campaign-screen-e2e/v1",
        "screenClassesVerified": True,
        "transitionEnterCount": 0,
        "stopPoint": {"module": stop, "sceneIndex": 1},
        "actualStopPoint": {"module": actual, "sceneIndex": actual_scene},
        "completion": completion,
        "route": route,
        "inputs": inputs,
        "inputRecords": input_records(inputs),
        "campaignStages": stages,
        "battlePreparations": [],
        "sawR01DepartureDialogue": False,
    }


class CampaignCheckpointBatchTest(unittest.TestCase):
    def test_default_and_selected_checkpoints_cover_r00_through_r58(self):
        self.assertEqual(59, len(MODULE.selected_checkpoints(None)))
        self.assertEqual("R_00", MODULE.selected_checkpoints(None)[0])
        self.assertEqual("R_58", MODULE.selected_checkpoints(None)[-1])
        self.assertEqual(
            ["R_01", "R_58"],
            MODULE.selected_checkpoints(["r_01,R_58", "R_01"]),
        )

    def test_invocation_uses_only_production_campaign_driver_arguments(self):
        options = MODULE.parse_args(["--checkpoint", "R_02"])
        command = MODULE.invocation(
            options,
            "R_02",
            Path("trace.json"),
        )
        self.assertIn("--campaign-e2e-stop=R_02:1", command)
        self.assertIn("--campaign-e2e-trace=trace.json", command)
        self.assertFalse(any(argument.startswith("--full-battle-") for argument in command))
        self.assertFalse(any(argument == "--verify" for argument in command))
        self.assertFalse(any(argument.startswith("--scenario=") for argument in command))
        self.assertFalse(any(argument == "--battle" for argument in command))
        self.assertFalse(any("choice-script" in argument for argument in command))

    def test_rejects_runner_arguments_that_bypass_production_route(self):
        for argument in (
            "--verify", "--scenario=R_20", "--battle", "--verify-choice-script=1",
            "--campaign-e2e-stop=R_00:0", "--campaign-e2e-trace=/tmp/shared.json",
            "--full-battle-trace=/tmp/shared.json.gz", "--full-battle-time-scale=1000",
        ):
            with self.subTest(argument=argument):
                with contextlib.redirect_stderr(io.StringIO()):
                    with self.assertRaises(SystemExit):
                        MODULE.parse_args(["--runner-arg", argument])

    def test_resume_filters_rows_outside_selected_checkpoints(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "manifest.json"
            path.write_text(json.dumps({
                "format": MODULE.FORMAT,
                "results": [
                    {"checkpoint": "R_00", "passed": True},
                    {"checkpoint": "R_58", "passed": True},
                ],
            }), encoding="utf-8")
            rows = MODULE.prior_results(path, True, {"R_00"})
        self.assertEqual(["R_00"], list(rows))

    def test_inspects_reached_checkpoint(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "R_01.json"
            path.write_text(json.dumps(route_trace()), encoding="utf-8")
            checked = MODULE.inspect_trace(path, "R_01", 1)
        self.assertTrue(checked["verified"])
        self.assertEqual("reached", checked["checkpointStatus"])

    def test_keeps_authorized_overshoot_distinct_from_reached_checkpoint(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "R_33.json"
            path.write_text(
                json.dumps(route_trace("R_33", "R_46", "forward-overshoot")),
                encoding="utf-8",
            )
            checked = MODULE.inspect_trace(path, "R_33", 1)
        self.assertTrue(checked["verified"])
        self.assertEqual("authored-forward-jump-overshoot", checked["checkpointStatus"])
        self.assertEqual("R_32", checked["forwardJumps"][-1]["from"])

    def test_manifest_aggregates_forward_jumps_and_does_not_count_overshoot_as_reached(self):
        exact = {
            "checkpoint": "R_32", "reached": True, "classifiedOvershoot": False,
            "passed": True, "forwardJumps": [],
        }
        skipped = {
            "checkpoint": "R_33", "reached": False, "classifiedOvershoot": True,
            "passed": False,
            "forwardJumps": [{
                "from": "R_32", "to": "R_46", "kind": "authored-hall-forward-jump",
                "stageDelta": 28, "authored": True,
            }],
        }
        manifest = {}
        MODULE.aggregate_manifest(manifest, {"R_32": exact, "R_33": skipped})
        self.assertEqual(
            {"requested": 2, "reached": 1, "authoredForwardJumpOvershoots": 1, "failed": 1},
            manifest["summary"],
        )
        self.assertEqual("R_46", manifest["observedForwardJumps"][0]["to"])


if __name__ == "__main__":
    unittest.main()

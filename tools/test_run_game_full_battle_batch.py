#!/usr/bin/env python3

import importlib.util
import contextlib
import gzip
import io
import json
from pathlib import Path
import tempfile
import unittest


MODULE_PATH = Path(__file__).with_name("run_game_full_battle_batch.py")
SPEC = importlib.util.spec_from_file_location("game_batch", MODULE_PATH)
game_batch = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
SPEC.loader.exec_module(game_batch)


class GameFullBattleBatchTest(unittest.TestCase):
    def trace(self, scenario="S_01", evidence=True, frames=None):
        frames = frames or [{}]
        summary = {"scenario": scenario, "end": True, "frameCount": len(frames)}
        if evidence:
            summary["gameScenario"] = {
                "requestedScenario": scenario,
                "expectedScript": f"Game/data/RS/{scenario}",
                "loadedScript": f"Game/data/RS/{scenario}",
                "loadBgCalls": [{"mapIndex": 1}],
                "loadedMap": {"mapIndex": 1, "textureName": "maps/battle-maps/2.png", "width": 20, "height": 20},
            }
        return {"format": "jojo-yingchuan-full-battle-trace/v1", "config": {"scenario": scenario, "driver": "production-input"},
                "reason": "battle-end", "frames": frames, "summary": summary}

    def inspect(self, value, expected="S_01"):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "trace.json"
            path.write_text(json.dumps(value), encoding="utf-8")
            return game_batch.inspect_trace(path, expected)

    def test_default_and_selected_scenarios(self):
        self.assertEqual(58, len(game_batch.selected_scenarios(None)))
        self.assertEqual(["S_01", "S_57"], game_batch.selected_scenarios(["s_01,S_57", "S_01"]))

    def test_parallel_job_count_is_explicit_and_positive(self):
        options = game_batch.parse_args(["--scenario", "S_01,S_02", "--jobs", "3"])
        self.assertEqual(3, options.jobs)
        self.assertEqual(["S_01", "S_02"], options.scenarios)
        with contextlib.redirect_stderr(io.StringIO()):
            with self.assertRaises(SystemExit):
                game_batch.parse_args(["--jobs", "0"])

    def test_gzip_trace_path_is_selected_before_runner_launch(self):
        manifest = Path("build/manifest.json")
        self.assertEqual("S_18.json", game_batch.trace_path(manifest, "S_18").name)
        self.assertEqual("S_18.json.gz", game_batch.trace_path(manifest, "S_18", compressed=True).name)

    def test_accepts_real_runtime_evidence(self):
        result = self.inspect(self.trace())
        self.assertTrue(result["terminal"])
        self.assertIsNone(result["error"])

    def test_rejects_label_only_trace(self):
        result = self.inspect(self.trace(evidence=False))
        self.assertIn("evidence missing", result["error"])

    def test_rejects_requested_loaded_scenario_mismatch(self):
        result = self.inspect(self.trace("S_00"), expected="S_01")
        self.assertIn("scenario/production-input evidence mismatch", result["error"])

    def test_inspects_gzipped_trace_without_losing_schema_evidence(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "S_01.json.gz"
            with gzip.open(path, "wt", encoding="utf-8") as handle:
                json.dump(self.trace(), handle)
            result = game_batch.inspect_trace(path, "S_01")
            self.assertTrue(result["terminal"])
            self.assertIsNone(result["error"])

    def authored_frames(self, scenario, observations):
        return [
            {"mapObjects": [[index, 17, index, 0] for index in range(12)], "observation": observation}
            if index == 0 else {"observation": observation}
            for index, observation in enumerate(observations)
        ]

    def test_s52_requires_exact_canonical_initial_object_payload_and_order(self):
        canonical = list(game_batch.AUTHORED_EXACT_OBSERVATION_CONTRACTS["S_52"]["objectsPrefix"])
        result = self.inspect(self.trace("S_52", frames=self.authored_frames("S_52", canonical)), expected="S_52")
        self.assertIsNone(result["error"])
        self.assertEqual(6, result["authoredCoverage"]["observed"]["objects"])

        wrong = canonical.copy()
        wrong[1], wrong[2] = wrong[2], wrong[1]
        result = self.inspect(self.trace("S_52", frames=self.authored_frames("S_52", wrong)), expected="S_52")
        self.assertIn("objects payload/order", result["error"])

    def test_s57_contract_uses_exact_common_sequence_and_either_authored_branch(self):
        exact = game_batch.AUTHORED_EXACT_OBSERVATION_CONTRACTS["S_57"]
        observations = list(exact["objectsPrefix"]) + list(exact["center"])
        observations.append(exact["setUnitStatusAlternatives"][0][0] + ":resolved=0,3,9")
        frames = self.authored_frames("S_57", observations)
        result = self.inspect(self.trace("S_57", frames=frames), expected="S_57")
        self.assertIsNone(result["error"])
        self.assertEqual(9, result["authoredCoverage"]["observed"]["center"])

        observations[-1] = exact["setUnitStatusAlternatives"][1][0] + ":resolved=0,3,9"
        result = self.inspect(self.trace("S_57", frames=self.authored_frames("S_57", observations)), expected="S_57")
        self.assertIsNone(result["error"])

    def test_s57_rejects_right_counts_with_wrong_center_or_status_payload(self):
        exact = game_batch.AUTHORED_EXACT_OBSERVATION_CONTRACTS["S_57"]
        observations = list(exact["objectsPrefix"]) + list(exact["center"])
        observations[7] = "transition:camera:center:12:20"
        observations.append("transition:setUnitStatus:rect=0,0,12,39,39:hp=0:mp=0:states=8:resolved=0")
        result = self.inspect(self.trace("S_57", frames=self.authored_frames("S_57", observations)), expected="S_57")
        self.assertIn("center payload/order", result["error"])
        self.assertIn("setUnitStatus payload/order", result["error"])


if __name__ == "__main__":
    unittest.main()

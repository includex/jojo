import importlib.util
import json
import tempfile
import unittest
from pathlib import Path

MODULE_PATH = Path(__file__).with_name("summarize_battle_trace.py")
spec = importlib.util.spec_from_file_location("summarizer", MODULE_PATH)
summarizer = importlib.util.module_from_spec(spec)
assert spec.loader is not None
spec.loader.exec_module(summarizer)


def unit(uid, x, y, hp, mp=10):
    return [0, uid, 0, x, y, hp, mp, 0, 0, 1, 1, 0, 0, 0, "anime0_0", 0, None, {}]


class BattleTraceSummaryTest(unittest.TestCase):
    def test_streams_frames_and_reports_state_edges(self):
        frames = [
            {"f": 1, "round": 1, "playerCount": 2, "friendCount": 1, "enemyCount": 1,
             "actions": ["r1/PLAYER/1:1,1->1,1:target=2:magic=null"], "units": [unit(1, 1, 1, 20), unit(2, 2, 1, 30)]},
            {"f": 2, "round": 1, "actions": ["transition:center:1,1", "transition:setUnitStatus:2:hp=-30"],
             "units": [unit(1, 1, 2, 20), unit(2, 2, 1, 0)]},
            {"f": 3, "round": 2, "actions": ["input-rejected stale-target=2"],
             "driver": {"input": "S_57:player-unit-select;player-command-attack;player-attack-target"},
             "end": True, "scriptedOutcome": "win", "units": [unit(1, 1, 2, 20), unit(2, 2, 1, 5)]},
        ]
        with tempfile.TemporaryDirectory() as root:
            path = Path(root) / "S_01.json"
            path.write_text(json.dumps({"summary": {"scenario": "S_01"}, "frames": frames}), encoding="utf-8")
            report = summarizer.summarize_trace(path, leaders=[2])
        self.assertEqual(3, report["frameCount"])
        self.assertEqual(1, len(report["actionAttempts"]))
        self.assertEqual(2, report["deaths"][0]["unit"])
        self.assertEqual(1, len(report["revivals"]))
        self.assertEqual([30, 0, 5], [x["hp"] for x in report["sourceLeaderHpHistories"]["2"]])
        self.assertEqual(1, report["inputRejections"]["count"])
        self.assertTrue(report["terminal"]["end"])
        self.assertEqual("win", report["terminal"]["outcome"])
        self.assertEqual(1, report["driverInputMarkers"]["player-command-attack"]["frames"])
        self.assertEqual(1, report["driverInputMarkers"]["S_57:player-unit-select"]["count"])

    def test_cumulative_action_journal_is_consumed_once(self):
        rows = [unit(1, 1, 1, 20), unit(2, 2, 1, 20)]
        frames = [
            {"f": 1, "round": 1, "actions": ["r1/PLAYER/1:1,1->1,1:target=2:magic=null"], "units": rows},
            {"f": 2, "round": 1, "actions": ["r1/PLAYER/1:1,1->1,1:target=2:magic=null"], "units": rows},
            {"f": 3, "round": 1, "actions": ["r1/PLAYER/1:1,1->1,1:target=2:magic=null", "r1/PLAYER/2:2,1->2,1:target=1:magic=null"], "units": rows},
        ]
        with tempfile.TemporaryDirectory() as root:
            path = Path(root) / "S_57.json"
            path.write_text(json.dumps({"frames": frames}), encoding="utf-8")
            report = summarizer.summarize_trace(path)
        self.assertEqual(2, len(report["actionAttempts"]))


if __name__ == "__main__":
    unittest.main()

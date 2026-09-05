import json
from pathlib import Path
import tempfile
import unittest

from verify_s22_trace_gate import build_report


def unit(character, *, action=0, hp=100, growth=True):
    metadata = {"visual": [0, 0], "statuses": [1], "statusRounds": [0]}
    if growth:
        metadata.update(level=1, abilities=[10, 10, 10, 10, 10], posts=0, arm=0, experience=0)
    return [0, character, 2, 0, 0, hp, 10, 0, action, 1, 1, 0, 0, 0,
            f"anime{action}_0", 0, None, metadata]


def trace(*, growth=True, callbacks=True, terminal=True):
    base = {"f": 0, "round": 6, "camp": 2, "end": False,
            "units": [unit(217, growth=growth), unit(5, growth=growth)]}
    attack = {"f": 1, "round": 6, "camp": 2, "end": False,
              "units": [unit(217, action=25, growth=growth), unit(5, growth=growth)]}
    last = {"f": 2, "round": 6, "camp": 2, "end": terminal,
            "units": [unit(217, growth=growth), unit(5, growth=growth)]}
    if callbacks:
        attack["callbacks"] = [{"kind": "attack", "actor": 217, "target": 5},
                                {"kind": "presentation", "actor": 217, "target": 5}]
        last["callbacks"] = [{"kind": "battle-end"}]
    return {"format": "jojo-yingchuan-full-battle-trace/v1", "frames": [base, attack, last],
            "summary": {"scenario": "S_22", "end": terminal}}


class S22TraceGateTest(unittest.TestCase):
    def report(self, source, port):
        with tempfile.TemporaryDirectory() as root:
            source_path, port_path = Path(root) / "source.json", Path(root) / "port.json"
            source_path.write_text(json.dumps(source), encoding="utf-8")
            port_path.write_text(json.dumps(port), encoding="utf-8")
            return build_report(source_path, port_path)

    def test_complete_matching_log_evidence_passes(self):
        report = self.report(trace(), trace())
        self.assertTrue(report["passed"])

    def test_missing_growth_is_a_gate_failure_not_a_skipped_profile(self):
        report = self.report(trace(growth=False), trace())
        self.assertFalse(report["passed"])
        self.assertEqual("growth", report["comparisonBlockers"][0]["observation"])

    def test_flat_trace_growth_metadata_is_compared(self):
        port = trace()
        # This is the current port trace shape: growth components live directly
        # in unit metadata, not under a synthetic ``growth`` object.
        for frame in port["frames"]:
            frame["units"][0][17]["experience"] = 1
        report = self.report(trace(), port)
        self.assertFalse(report["passed"])
        self.assertEqual(1, report["campBoundaries"]["profiles"]["growth"]["mismatchCount"])

    def test_co_present_target_without_explicit_callback_is_not_assumed(self):
        report = self.report(trace(callbacks=False), trace(callbacks=False))
        self.assertFalse(report["passed"])
        blockers = [item["observation"] for item in report["comparisonBlockers"]]
        self.assertIn("round6-217-to-5-callback", blockers)

    def test_terminal_requires_end_frame_and_callback_evidence(self):
        report = self.report(trace(terminal=False), trace(terminal=False))
        self.assertFalse(report["passed"])
        blockers = [item["observation"] for item in report["comparisonBlockers"]]
        self.assertIn("terminal-callback-sequence", blockers)

    def test_callback_order_drift_fails(self):
        port = trace()
        port["frames"][1]["callbacks"].reverse()
        report = self.report(trace(), port)
        self.assertFalse(report["passed"])
        self.assertIsNotNone(report["round6Attack"]["callbackOrderMismatch"])


if __name__ == "__main__":
    unittest.main()

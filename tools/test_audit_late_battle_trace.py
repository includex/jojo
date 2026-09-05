import gzip
import json
from pathlib import Path
import tempfile
import unittest

from audit_late_battle_trace import audit_trace, iter_frames
from authored_observation_contracts import EXACT_CONTRACTS


def unit(unit_id, hp, animation, statuses=None):
    return [-1, unit_id, 0, 1, 1, hp, 0, 0, 0, 1, 1, 0, 0, 0, animation, 0, None,
            {"statuses": statuses or [1] * 15}]


class LateBattleTraceAuditTest(unittest.TestCase):
    def write_trace(self, root, frames, gzip_output=False):
        path = Path(root) / ("S_47.json.gz" if gzip_output else "S_47.json")
        value = {"format": "test", "frames": frames, "summary": {"frameCount": len(frames)}}
        if gzip_output:
            with gzip.open(path, "wt", encoding="utf-8") as handle:
                json.dump(value, handle)
        else:
            path.write_text(json.dumps(value), encoding="utf-8")
        return path

    def test_streams_plain_and_gzip_frames(self):
        frames = [{"f": index, "payload": "x" * 1000} for index in range(20)]
        with tempfile.TemporaryDirectory() as root:
            for compressed in (False, True):
                path = self.write_trace(root, frames, compressed)
                self.assertEqual(list(range(20)), [row["f"] for row in iter_frames(path, chunk_size=37)])

    def test_attack_action_chain_is_observable(self):
        base = {"camera": [0, 0], "mapObjectRevision": 1, "mapObjects": [], "fight": None}
        frames = [
            {**base, "f": 0, "observation": "transition:attackAction:82:53:1",
             "units": [unit(82, 100, "anime21_1"), unit(53, 100, "anime0_0")]},
            {**base, "f": 1, "mapObjects": None,
             "units": [unit(82, 100, "anime21_1"), unit(53, 100, "anime32_3")]},
            {**base, "f": 2, "mapObjects": None,
             "units": [unit(82, 100, "anime0_1"), unit(53, 50, "anime4_3")]},
        ]
        with tempfile.TemporaryDirectory() as root:
            path = self.write_trace(root, frames, True)
            report = audit_trace(path, "S_47")
        self.assertEqual([], report["capabilityErrors"])
        self.assertEqual([], report["evidenceErrors"])
        self.assertTrue(all(report["attackChains"][0][key]
                            for key in ("attackerAnimation", "targetReaction", "targetHpReduced")))

    def test_legacy_trace_reports_missing_capabilities(self):
        with tempfile.TemporaryDirectory() as root:
            path = self.write_trace(root, [{"f": 0, "units": []}])
            report = audit_trace(path, "S_52")
        self.assertEqual({"camera", "mapObjectRevision", "mapObjects", "fight"},
                         set(report["capabilityErrors"]))

    def test_s57_exact_contract_rejects_correct_counts_in_wrong_order(self):
        exact = EXACT_CONTRACTS["S_57"]
        observations = list(exact["objectsPrefix"]) + list(exact["center"])
        observations[7], observations[8] = observations[8], observations[7]
        observations.append(exact["setUnitStatusAlternatives"][0][0] + ":resolved=0")
        base = {"camera": [0, 0], "mapObjectRevision": 1, "mapObjects": [], "fight": None, "units": []}
        frames = [{**base, "f": index, "observation": value} for index, value in enumerate(observations)]
        with tempfile.TemporaryDirectory() as root:
            path = self.write_trace(root, frames)
            report = audit_trace(path, "S_57")
        self.assertIn("authored center payload/order does not match canonical source sequence",
                      report["evidenceErrors"])


if __name__ == "__main__":
    unittest.main()

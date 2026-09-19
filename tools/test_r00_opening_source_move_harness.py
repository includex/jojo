#!/usr/bin/env python3
import json
import os
from pathlib import Path
import subprocess
import tempfile
import unittest


class R00OpeningSourceMoveHarnessTest(unittest.TestCase):
    def test_executes_recovered_opening_groups(self):
        script = Path(__file__).with_name("r00_opening_source_move_harness.js")
        with tempfile.TemporaryDirectory() as directory:
            output = Path(directory) / "oracle.json"
            result = subprocess.run(["node", script, output], capture_output=True, text=True, timeout=10)
            self.assertEqual(0, result.returncode, result.stderr)
            report = json.loads(output.read_text())
        self.assertEqual("recovered-source-runtime", report["evidenceKind"])
        self.assertEqual([1, 3, 4], [len(group["actors"]) for group in report["groups"]])
        self.assertEqual([181], [actor["id"] for actor in report["groups"][0]["actors"]])
        self.assertEqual([181, 0, 157], [actor["id"] for actor in report["groups"][1]["actors"]])
        self.assertEqual([181, 0, 182, 157], [actor["id"] for actor in report["groups"][2]["actors"]])
        self.assertEqual(.3, report["delaySeconds"])
        self.assertEqual(5406, report["source"]["assetConfig"]["pmap30PathIndex"])
        self.assertEqual("f7359", report["source"]["assetConfig"]["pmap30ImportVersion"])
        self.assertEqual(64, len(report["source"]["assetConfig"]["sha256"]))
        for group in report["groups"]:
            for actor in group["actors"]:
                self.assertEqual(actor["origin"], actor["path"][0])
                self.assertEqual(actor["destination"], actor["path"][-1])
                self.assertAlmostEqual(actor["duration"], .04 * (len(actor["path"]) - 1))
        second_group_cao = report["groups"][2]["actors"][1]
        self.assertEqual(23, len(second_group_cao["path"]))
        self.assertEqual([2, 1, 2, 3], [segment["direction"] for segment in second_group_cao["segments"]])
        self.assertEqual(3, second_group_cao["movementFinalDirection"])
        self.assertEqual(2, second_group_cao["finalDirection"])

    def test_rejects_an_injected_opening_statement(self):
        script = Path(__file__).with_name("r00_opening_source_move_harness.js")
        source_root = (script.parent / "../../jojo_mobile/sgccz-desktop").resolve()
        with tempfile.TemporaryDirectory() as directory:
            fake_root = Path(directory) / "source"
            (fake_root / "decompiled-python").mkdir(parents=True)
            for name in ("assets", "recovered-js"):
                (fake_root / name).symlink_to(source_root / name, target_is_directory=True)
            r00 = (source_root / "decompiled-python/R_00.py").read_text()
            r00 = r00.replace(
                "    stage.unit(181).move(40, 15, 2)\n",
                "    stage.unit(181).move(40, 15, 2)\n    return helper()\n",
                1,
            )
            (fake_root / "decompiled-python/R_00.py").write_text(r00)
            result = subprocess.run(
                ["node", script, Path(directory) / "oracle.json"],
                capture_output=True, text=True, timeout=10,
                env={**os.environ, "JOJO_SOURCE_ROOT": str(fake_root)},
            )
        self.assertNotEqual(0, result.returncode)
        self.assertIn("R_00 opening prefix changed", result.stderr)


if __name__ == "__main__":
    unittest.main()

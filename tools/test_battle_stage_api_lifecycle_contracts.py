#!/usr/bin/env python3
import json
import tempfile
import unittest
from pathlib import Path

from build_battle_stage_api_lifecycle_contracts import CONTRACTS, FORMAT, build, main


TOOLS_ROOT = Path(__file__).resolve().parent
SOURCE_ROOT = TOOLS_ROOT.parents[1] / "jojo_mobile/sgccz-desktop/recovered-js/modules"


class BattleStageApiLifecycleContractsTest(unittest.TestCase):
    def test_all_six_flagged_apis_have_source_anchored_contracts(self):
        report = build(SOURCE_ROOT)
        self.assertEqual(FORMAT, report["format"])
        self.assertEqual(
            {"stage.info", "stage.loadBg", "stage.setRectUnitHide", "stage.setStageName", "stage.setUnitAttr", "stage.unit().setPosts"},
            {item["api"] for item in report["contracts"]},
        )
        for item in report["contracts"]:
            self.assertTrue(item["sourceFunctions"])
            self.assertTrue(item["eagerMutation"])
            self.assertIn("pause", item)
            self.assertIn("resume", item)
            self.assertIn("assetFailure", item)
            for ref in item["sourceFunctions"]:
                self.assertEqual(64, len(ref["bodySha256"]))
                self.assertGreater(ref["line"], 0)

    def test_generator_writes_machine_and_review_reports(self):
        with tempfile.TemporaryDirectory() as directory:
            json_path = Path(directory) / "contracts.json"
            md_path = Path(directory) / "contracts.md"
            self.assertEqual(0, main(["--source-root", str(SOURCE_ROOT), "--json", str(json_path), "--markdown", str(md_path)]))
            report = json.loads(json_path.read_text(encoding="utf-8"))
            self.assertEqual(len(CONTRACTS), len(report["contracts"]))
            markdown = md_path.read_text(encoding="utf-8")
            self.assertIn("Asset/failure path:", markdown)
            self.assertIn("`stage.unit().setPosts`", markdown)

    def test_checked_in_json_matches_current_recovered_source(self):
        expected = build(SOURCE_ROOT)
        actual = json.loads((TOOLS_ROOT / "battle_stage_api_lifecycle_contracts.json").read_text(encoding="utf-8"))
        self.assertEqual(expected, actual)


if __name__ == "__main__":
    unittest.main()

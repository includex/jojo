#!/usr/bin/env python3
import importlib.util
import json
import sys
import tempfile
import unittest
from pathlib import Path

TOOLS = Path(__file__).parent
sys.path.insert(0, str(TOOLS))
SPEC = importlib.util.spec_from_file_location("verify_render_parity_reports", TOOLS / "verify_render_parity_reports.py")
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC and SPEC.loader
SPEC.loader.exec_module(MODULE)


class RenderParityReportGateTest(unittest.TestCase):
    def test_report_is_recomputed_instead_of_trusting_equal_flag(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            left, right, report = root / "left.jsonl", root / "right.jsonl", root / "report.json"
            event = {"nodePath": "Canvas/bg", "drawType": "sprite", "x": 0, "y": 0, "w": 1280, "h": 688,
                     "assetFrameId": "bg", "opacity": 1, "blend": [770, 771], "visible": True, "text": None}
            left.write_text(json.dumps(event) + "\n", encoding="utf-8")
            right.write_text(json.dumps(event) + "\n", encoding="utf-8")
            report.write_text(json.dumps({"equal": True, "expected": "left.jsonl", "actual": "right.jsonl",
                                          "expectedDrawCount": 1, "actualDrawCount": 1,
                                          "expectedFormat": "canonical", "actualFormat": "canonical",
                                          "floatTolerance": 1e-5}), encoding="utf-8")
            self.assertTrue(MODULE.verify(report, root)[0])
            right.write_text(json.dumps(dict(event, assetFrameId="wrong")) + "\n", encoding="utf-8")
            self.assertFalse(MODULE.verify(report, root)[0])


if __name__ == "__main__":
    unittest.main()

#!/usr/bin/env python3
import importlib.util
import json
import tempfile
import unittest
import sys
from pathlib import Path

MODULE_PATH = Path(__file__).with_name("compare_render_logs.py")
SPEC = importlib.util.spec_from_file_location("compare_render_logs", MODULE_PATH)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC and SPEC.loader
sys.modules[SPEC.name] = MODULE
SPEC.loader.exec_module(MODULE)


class RenderLogComparatorTest(unittest.TestCase):
    def canonical(self, draws, timestamp=1):
        return {"viewport": [1280, 688], "timestamp": timestamp, "draws": draws}

    def test_timing_noise_and_float_serialization_are_ignored(self):
        left = self.canonical([{"path": "ui/panel", "rect": [1, 2, 3, 4], "asset": "panel", "opacity": 255,
                                "blend": [770, 771], "visible": True, "text": "대화"}], 10)
        right = self.canonical([{"path": "ui/panel", "rect": [1.000001, 2, 3, 4], "asset": "panel", "opacity": 255,
                                 "blend": [770, 771], "visible": True, "text": "대화", "elapsedMs": 99}], 20)
        _, a = MODULE.adapt(left); _, b = MODULE.adapt(right)
        self.assertEqual([], MODULE.compare(a, b, 1e-5))

    def test_semantic_and_order_differences_are_actionable(self):
        left = self.canonical([{"path": "a", "drawType": "sprite", "text": "원본"}, {"path": "b", "visible": True}])
        right = self.canonical([{"path": "b", "visible": False}, {"path": "a", "drawType": "nine-patch", "text": "포트"}])
        _, a = MODULE.adapt(left); _, b = MODULE.adapt(right)
        diffs = MODULE.compare(a, b, 1e-5)
        self.assertIn("draw-order", {d["kind"] for d in diffs})
        self.assertIn(("a#0", "text"), {(d.get("path"), d.get("field")) for d in diffs})
        self.assertIn(("a#0", "draw_type"), {(d.get("path"), d.get("field")) for d in diffs})
        self.assertIn(("b#0", "visible"), {(d.get("path"), d.get("field")) for d in diffs})

    def test_cli_returns_nonzero_for_semantic_difference(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory); expected = root / "expected.json"; actual = root / "actual.json"
            expected.write_text(json.dumps(self.canonical([{"path": "x", "asset": "a"}])), encoding="utf-8")
            actual.write_text(json.dumps(self.canonical([{"path": "x", "asset": "b"}])), encoding="utf-8")
            self.assertEqual(1, MODULE.main([str(expected), str(actual)]))

    def test_jsonl_events_are_loaded_and_timestamps_ignored(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory); one = root / "one.jsonl"; many = root / "many.jsonl"
            event = {"sequence": 0, "frame": 7, "timestamp": 1234, "nodePath": "Canvas/panel",
                     "x": 0, "y": 0, "w": 1280, "h": 688, "assetFrameId": "panel",
                     "opacity": 1, "blend": [770, 771], "visible": True, "text": None}
            one.write_text(json.dumps(event) + "\n", encoding="utf-8")
            later = dict(event, timestamp=9876)
            many.write_text(json.dumps(later) + "\n", encoding="utf-8")
            one_format, left = MODULE.adapt(MODULE.load_input(one))
            many_format, right = MODULE.adapt(MODULE.load_input(many))
            self.assertEqual(("canonical", "canonical"), (one_format, many_format))
            self.assertEqual([], MODULE.compare(left, right, 1e-5))
            self.assertEqual("panel", left[0].asset)

    def test_jsonl_atlas_crop_and_final_mirror_are_part_of_frame_identity(self):
        base = {"sequence": 0, "nodePath": "Canvas/map/unit/mask/node", "drawType": "sprite",
                "x": 1, "y": 2, "w": 96, "h": 96, "assetFrameId": "shared-atlas#generated",
                "opacity": 1, "blend": [770, 771], "visible": True, "text": None,
                "sourceRect": [0, 201, 48, 48], "flipX": False, "flipY": False}
        with tempfile.TemporaryDirectory() as directory:
            paths = [Path(directory) / name for name in ("expected.jsonl", "row.jsonl", "flip.jsonl")]
            for path, event in zip(paths, (base, dict(base, sourceRect=[0, 151, 48, 48]), dict(base, flipX=True))):
                path.write_text(json.dumps(event) + "\n", encoding="utf-8")
            draws = [MODULE.adapt(MODULE.load_input(path))[1] for path in paths]
        self.assertIn("sourceRect", draws[0][0].asset)
        self.assertTrue(any(diff.get("field") == "asset" for diff in MODULE.compare(draws[0], draws[1], 0)))
        self.assertTrue(any(diff.get("field") == "asset" for diff in MODULE.compare(draws[0], draws[2], 0)))


if __name__ == "__main__":
    unittest.main()

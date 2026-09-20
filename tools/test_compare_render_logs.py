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
        right = self.canonical([{"path": "b", "visible": False}, {"path": "a", "drawType": "nine-patch", "text": "게임"}])
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

    def test_dynamic_atlas_texture_prefix_is_not_part_of_a_named_frame_identity(self):
        """`<url>#Logo_9-1` and `Logo_9-1` are the same draw.

        The Cocos harness only keeps the texture URL while the frame has not
        been repacked into a runtime DynamicAtlas handle, which depends on how
        much UI the route has built. An `<unnamed-frame>` has no name to fall
        back on, so its texture path stays part of the identity.
        """
        base = {"sequence": 0, "nodePath": "Canvas/Layer/bg0", "drawType": "tiled-sprite",
                "x": 1, "y": 2, "w": 3, "h": 4, "opacity": 1, "blend": [770, 771],
                "visible": True, "text": None}
        named = dict(base, assetFrameId="assets/resources/native/3c/3c05c264.68a7f.png#Logo_9-1")
        packed = dict(base, assetFrameId="Logo_9-1")
        unnamed_one = dict(base, assetFrameId="assets/Game/native/4a/one.jpg#<unnamed-frame>")
        unnamed_two = dict(base, assetFrameId="assets/Game/native/4a/two.jpg#<unnamed-frame>")
        with tempfile.TemporaryDirectory() as directory:
            def draws(name, event):
                path = Path(directory) / name
                path.write_text(json.dumps(event) + "\n", encoding="utf-8")
                return MODULE.adapt(MODULE.load_input(path))[1]
            left, right = draws("named.jsonl", named), draws("packed.jsonl", packed)
            first, second = draws("u1.jsonl", unnamed_one), draws("u2.jsonl", unnamed_two)
        self.assertEqual("Logo_9-1", left[0].asset)
        self.assertEqual([], MODULE.compare(left, right, 0))
        self.assertTrue(any(diff.get("field") == "asset" for diff in MODULE.compare(first, second, 0)))

    def test_label_colour_is_a_comparable_semantic_field(self):
        """Black vs white must be able to fail a gate.

        The MenuLayer bar labels are engine-default white in the source
        prefab. Before `color` joined SEMANTIC_FIELDS a renderer could draw
        them black and every field still matched.
        """
        def label(color):
            return self.canonical([{"path": "Canvas/Layer/bg/bg0/label", "type": "label",
                                    "rect": [41, 36, 304, 44], "text": "영천의 전투", "color": color}])
        white = MODULE.adapt(label("#ffffffff"))[1]
        black = MODULE.adapt(label("#000000ff"))[1]
        self.assertIn("color", MODULE.SEMANTIC_FIELDS)
        self.assertTrue(any(diff.get("field") == "color" for diff in MODULE.compare(white, black, 0)))
        # The same colour spelled differently by the two runtimes still matches.
        self.assertEqual([], MODULE.compare(white, MODULE.adapt(label("FFFFFF"))[1], 0))
        self.assertEqual([], MODULE.compare(white, MODULE.adapt(label({"r": 255, "g": 255, "b": 255, "a": 255}))[1], 0))

    def test_a_log_without_colour_is_not_reported_as_a_colour_difference(self):
        """One-sided colour is skipped so existing producers keep passing."""
        with_color = MODULE.adapt(self.canonical([
            {"path": "p", "type": "label", "rect": [0, 0, 1, 1], "text": "t", "color": "#ffffffff"}]))[1]
        without = MODULE.adapt(self.canonical([
            {"path": "p", "type": "label", "rect": [0, 0, 1, 1], "text": "t"}]))[1]
        self.assertIsNone(without[0].color)
        self.assertEqual([], MODULE.compare(with_color, without, 0))

    def test_label_outline_is_a_comparable_semantic_field(self):
        """A wrong `cc.LabelOutline` colour must be able to fail a gate.

        The outline is a component of its own, so the node `color` of the
        MsgBox4 labels is identical whether the outline is the prefab's
        (255,226,110) or the port's old (255,250,110).
        """
        def label(outline):
            return self.canonical([{"path": "Canvas/Layer/bg0/label", "type": "label",
                                    "rect": [41, 36, 304, 44], "text": "모든 부대의 명령을 종료하시겠습니까?",
                                    "color": "#936100", "outline": outline}])
        authored = MODULE.adapt(label("#ffe26e"))[1]
        ported = MODULE.adapt(label("#fffa6e"))[1]
        self.assertIn("outline", MODULE.SEMANTIC_FIELDS)
        self.assertEqual([], MODULE.compare(authored, MODULE.adapt(label("FFE26E"))[1], 0))
        diffs = MODULE.compare(authored, ported, 0)
        self.assertTrue(any(diff.get("field") == "outline" for diff in diffs))
        self.assertFalse(any(diff.get("field") == "color" for diff in diffs))

    def test_a_log_without_outline_is_not_reported_as_an_outline_difference(self):
        """One-sided outline is skipped so existing producers keep passing."""
        with_outline = MODULE.adapt(self.canonical([
            {"path": "p", "type": "label", "rect": [0, 0, 1, 1], "text": "t", "outline": "#ffe26e"}]))[1]
        without = MODULE.adapt(self.canonical([
            {"path": "p", "type": "label", "rect": [0, 0, 1, 1], "text": "t"}]))[1]
        self.assertIsNone(without[0].outline)
        self.assertEqual([], MODULE.compare(with_outline, without, 0))

    def test_cocos_snapshot_outline_comes_from_the_label_component(self):
        """`rendererSnapshot` records the outline per label component."""
        snapshot = {"snapshot": {"viewport": [1488, 800], "nodes": [{
            "path": "Canvas/Layer/bg0/label", "labels": ["예"],
            "labelComponents": [{"string": "예", "outline": {"color": [124, 243, 153, 255], "width": 2}}],
            "screen": [10, 10], "size": [100, 40], "anchor": [0.5, 0.5], "scale": [1, 1],
        }]}}
        draws = MODULE.adapt(snapshot)[1]
        self.assertEqual("#7cf399ff", draws[0].outline)


if __name__ == "__main__":
    unittest.main()

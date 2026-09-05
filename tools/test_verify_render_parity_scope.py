import json
import time
import tempfile
import unittest
from pathlib import Path

import verify_render_parity_scope


class VerifyRenderParityScopeTest(unittest.TestCase):
    def test_fresh_required_rejects_artifacts_from_before_run_marker(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            event = {"nodePath": "Canvas/bg", "drawType": "sprite", "x": 0, "y": 0,
                     "w": 1, "h": 1, "assetFrameId": "bg", "opacity": 1,
                     "blend": [770, 771], "visible": True, "text": None}
            for name in ("expected.jsonl", "actual.jsonl"):
                (root / name).write_text(json.dumps(event) + "\n", encoding="utf-8")
            (root / "report.json").write_text(json.dumps({
                "expected": "expected.jsonl", "actual": "actual.jsonl",
                "expectedDrawCount": 1, "actualDrawCount": 1,
                "expectedFormat": "canonical", "actualFormat": "canonical", "floatTolerance": 0,
            }), encoding="utf-8")
            names = ["report.json", "expected.jsonl", "actual.jsonl"]
            (root / "manifest.json").write_text(json.dumps({
                "format": "jojo-render-freshness/v1", "runId": "test",
                "startedNs": time.time_ns() + 1_000_000_000, "artifacts": names,
            }), encoding="utf-8")
            scope = root / "scope.json"
            scope.write_text(json.dumps({
                "freshnessManifest": "manifest.json",
                "phases": [{"id": "p", "states": [{"id": "s", "report": "report.json", "freshRequired": True}]}],
            }), encoding="utf-8")
            okay, messages = verify_render_parity_scope.verify_scope(scope, root)
            self.assertFalse(okay)
            self.assertTrue(any("stale/not-in-run" in message for message in messages))

    def test_missing_report_keeps_screenshot_gate_blocked(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            scope = root / "scope.json"
            scope.write_text(json.dumps({
                "phases": [{"id": "p", "states": [{"id": "s", "report": None}]}]
            }), encoding="utf-8")
            okay, messages = verify_render_parity_scope.verify_scope(scope, root)
            self.assertFalse(okay)
            self.assertIn("BLOCKED p/s", messages[0])
            self.assertEqual("SCREENSHOT_GATE_BLOCKED states=1 failures=1", messages[-1])

    def _write_inventory(self, root: Path, layers: list[dict]) -> None:
        (root / "source/ui").mkdir(parents=True)
        for name in ("VisibleLayer.js", "BaseLayer.js"):
            (root / "source/ui" / name).write_text("module.exports = {};", encoding="utf-8")
        (root / "inventory.json").write_text(json.dumps({
            "sourceDirectory": "source",
            "sourceGlobs": ["ui/*Layer.js"],
            "layers": layers,
        }), encoding="utf-8")

    def test_required_layer_without_source_state_blocks_gate(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            self._write_inventory(root, [
                {"source": "ui/VisibleLayer.js", "name": "VisibleLayer", "classification": "required", "reason": "visible"},
                {"source": "ui/BaseLayer.js", "name": "BaseLayer", "classification": "infrastructure/no-render", "reason": "base"},
            ])
            scope = root / "scope.json"
            scope.write_text(json.dumps({
                "layerInventory": "inventory.json",
                "phases": [{"id": "p", "states": [{"id": "s", "sourceLayers": [], "report": None}]}],
            }), encoding="utf-8")
            okay, messages = verify_render_parity_scope.verify_scope(scope, root)
            self.assertFalse(okay)
            self.assertIn("BLOCKED layer-coverage: required VisibleLayer has no parity state", messages)

    def test_new_recovered_layer_must_be_explicitly_classified(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            self._write_inventory(root, [
                {"source": "ui/VisibleLayer.js", "name": "VisibleLayer", "classification": "required", "reason": "visible"},
            ])
            scope = root / "scope.json"
            scope.write_text(json.dumps({
                "layerInventory": "inventory.json",
                "phases": [{"id": "p", "states": [{"id": "s", "sourceLayers": ["VisibleLayer"], "report": None}]}],
            }), encoding="utf-8")
            okay, messages = verify_render_parity_scope.verify_scope(scope, root)
            self.assertFalse(okay)
            self.assertIn("BLOCKED layer-inventory: unclassified recovered layer ui/BaseLayer.js", messages)

    def test_inventory_enabled_scope_requires_source_layers_on_every_state(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            self._write_inventory(root, [
                {"source": "ui/VisibleLayer.js", "name": "VisibleLayer", "classification": "required", "reason": "visible"},
                {"source": "ui/BaseLayer.js", "name": "BaseLayer", "classification": "infrastructure/no-render", "reason": "base"},
            ])
            scope = root / "scope.json"
            scope.write_text(json.dumps({
                "layerInventory": "inventory.json",
                "phases": [{"id": "p", "states": [{"id": "s", "report": None}]}],
            }), encoding="utf-8")
            okay, messages = verify_render_parity_scope.verify_scope(scope, root)
            self.assertFalse(okay)
            self.assertIn("BLOCKED p/s: missing sourceLayers declaration", messages)

    def test_complete_inventory_and_mapping_are_reported(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            self._write_inventory(root, [
                {"source": "ui/VisibleLayer.js", "name": "VisibleLayer", "classification": "required", "reason": "visible"},
                {"source": "ui/BaseLayer.js", "name": "BaseLayer", "classification": "infrastructure/no-render", "reason": "base"},
            ])
            scope = root / "scope.json"
            scope.write_text(json.dumps({
                "layerInventory": "inventory.json",
                "phases": [{"id": "p", "states": [{"id": "s", "sourceLayers": ["VisibleLayer"], "report": None}]}],
            }), encoding="utf-8")
            okay, messages = verify_render_parity_scope.verify_scope(scope, root)
            self.assertFalse(okay)  # report:null intentionally keeps the screenshot gate closed
            self.assertEqual("PASS layer-inventory: inventoried=2 required=1 mapped=1", messages[0])
            self.assertFalse(any("layer-coverage" in message for message in messages))


if __name__ == "__main__":
    unittest.main()

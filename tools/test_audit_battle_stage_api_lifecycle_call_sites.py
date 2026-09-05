#!/usr/bin/env python3
"""Independent tests for the S/R lifecycle call-site audit."""
from __future__ import annotations

import ast
import json
import tempfile
import unittest
from pathlib import Path

try:
    from .audit_battle_stage_api_lifecycle_call_sites import (
        LifecycleCallVisitor,
        audit,
        callback_assessment,
        markdown,
    )
except ImportError:  # Direct ``python tools/test_*.py`` execution.
    from audit_battle_stage_api_lifecycle_call_sites import (
        LifecycleCallVisitor,
        audit,
        callback_assessment,
        markdown,
    )


class LifecycleCallSiteAuditTest(unittest.TestCase):
    def test_ast_normalizes_unit_factory_and_tracks_function_branch(self):
        source = """
def scene0():
    if ready:
        stage.draw()
        stage.unit(gvars[0]).setPosts(0)
    else:
        stage.loadBg(1)
"""
        visitor = LifecycleCallVisitor("S_00", "S")
        visitor.visit(ast.parse(source))
        self.assertEqual(
            ["stage.unit().setPosts", "stage.loadBg"],
            [call["api"] for call in visitor.calls],
        )
        self.assertEqual("scene0", visitor.calls[0]["functionPath"])
        self.assertEqual("if@3:then", visitor.calls[0]["branch"])
        self.assertEqual("if@3:else", visitor.calls[1]["branch"])
        self.assertEqual(1, len(visitor.draw_sites))

    def test_current_source_is_exactly_s_r_scope_and_reconciles_prior_train(self):
        root = Path(__file__).resolve().parents[1]
        source_dir = root.parent / "jojo_mobile/sgccz-desktop/decompiled-python"
        source_root = root.parent / "jojo_mobile/sgccz-desktop/recovered-js/modules"
        report = audit(
            source_dir,
            source_root,
            root / "tools/battle_stage_api_lifecycle_contracts.json",
            root / "tools/battle_stage_api_audit.json",
            root.parent / "jojo_mobile/sgccz-desktop/recovered-js/porting/python-api-usage.json",
        )
        self.assertEqual({"S": 58, "R": 59}, report["scriptsByFamily"])
        self.assertEqual(
            {
                "stage.loadBg": 416,
                "stage.setUnitAttr": 441,
                "stage.unit().setPosts": 4640,
                "stage.resumeCtrl": 130,
            },
            report["summary"]["callCounts"],
        )
        self.assertEqual(
            [
                {"api": "stage.loadBg", "priorCount": 417, "currentSRCount": 416,
                 "reason": "prior_inventory_includes_non_SR_sources_or_scope_differs"},
                {"api": "stage.resumeCtrl", "priorCount": 131, "currentSRCount": 130,
                 "reason": "prior_inventory_includes_non_SR_sources_or_scope_differs"},
            ],
            report["priorAuditDiscrepancies"],
        )

    def test_route_and_callback_findings_are_explicit(self):
        root = Path(__file__).resolve().parents[1]
        report = audit(
            root.parent / "jojo_mobile/sgccz-desktop/decompiled-python",
            root.parent / "jojo_mobile/sgccz-desktop/recovered-js/modules",
            root / "tools/battle_stage_api_lifecycle_contracts.json",
        )
        rows = {row["api"]: row for row in report["apis"]}
        load_bg = rows["stage.loadBg"]
        self.assertEqual({"S": 58, "R": 358}, load_bg["callCountByFamily"])
        self.assertEqual("BattleLayer", load_bg["routeByFamily"]["S"]["owner"])
        self.assertEqual("HallLayer", load_bg["routeByFamily"]["R"]["owner"])
        self.assertIn("R_route_uses_HallLayer_not_battle_lifecycle_contract", load_bg["findings"])
        self.assertEqual(416, load_bg["callCountsByCallbackRequirement"]["required_for_script_continuation"])
        posts = rows["stage.unit().setPosts"]
        self.assertEqual(
            {"conditional_avatar_reload": 4640},
            posts["callCountsByCallbackRequirement"],
        )
        resume = rows["stage.resumeCtrl"]
        self.assertEqual({"none": 130}, resume["callCountsByCallbackRequirement"])
        self.assertEqual({"after": 130}, resume["callCountsByModuleDrawPhase"])
        self.assertIn("lifecycle_contract_not_present", resume["findings"])

    def test_static_argument_patterns_and_draw_proof_are_preserved(self):
        source = """
def scene0():
    stage.loadBg(0)
    stage.draw()
    if ready:
        stage.setUnitAttr(gvars[0], 17, gvars[3])
"""
        visitor = LifecycleCallVisitor("S_00", "S")
        visitor.visit(ast.parse(source))
        report_calls = visitor.calls
        # The visitor is intentionally pure; audit() adds draw/callback
        # annotations.  A tiny temporary fixture makes this assertion fully
        # independent of the checked-in scenario corpus.
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            scripts = root / "scripts"
            modules = root / "modules"
            (modules / "battle").mkdir(parents=True)
            (modules / "ui").mkdir()
            scripts.mkdir()
            (scripts / "S_00.py").write_text(source, encoding="utf-8")
            (modules / "battle/BattleLayer.js").write_text(
                "x.prototype.loadBg=function(){};\n"
                "x.prototype.setUnitAttr=function(){};\n",
                encoding="utf-8",
            )
            (modules / "battle/BattleUnit.js").write_text(
                "x.prototype.setPosts=function(){};\n", encoding="utf-8"
            )
            (modules / "ui/StageLayer.js").write_text(
                "x.prototype.setUnitAttr=function(){};\n", encoding="utf-8"
            )
            result = audit(scripts, modules)
        rows = {row["api"]: row for row in result["apis"]}
        self.assertEqual({"int:0": 1}, rows["stage.loadBg"]["staticArgumentPatterns"])
        self.assertEqual(
            {"gvars[int:0]|int:17|gvars[int:3]": 1},
            rows["stage.setUnitAttr"]["staticArgumentPatterns"],
        )
        self.assertEqual("before", rows["stage.loadBg"]["calls"][0]["drawPhase"])
        self.assertEqual("after", rows["stage.setUnitAttr"]["calls"][0]["drawPhase"])
        self.assertEqual(2, len(report_calls))

    def test_callback_assessment_uses_original_set_posts_default_flag(self):
        call = {
            "api": "stage.unit().setPosts",
            "arguments": [{"kind": "literal", "type": "int", "value": 0}],
        }
        result = callback_assessment(call, {"api": call["api"]})
        self.assertTrue(result["possible"])
        self.assertEqual(19, result["effectiveFlag"])

    def test_markdown_is_concise_but_keeps_findings(self):
        root = Path(__file__).resolve().parents[1]
        report = audit(
            root.parent / "jojo_mobile/sgccz-desktop/decompiled-python",
            root.parent / "jojo_mobile/sgccz-desktop/recovered-js/modules",
            root / "tools/battle_stage_api_lifecycle_contracts.json",
        )
        rendered = markdown(report)
        self.assertIn("stage.loadBg", rendered)
        self.assertIn("R_route_uses_HallLayer_not_battle_lifecycle_contract", rendered)
        self.assertIn("Full per-call evidence", rendered)
        self.assertLess(len(rendered), 20_000)


if __name__ == "__main__":
    unittest.main()

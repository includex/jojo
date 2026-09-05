#!/usr/bin/env python3
import ast
import unittest

from audit_battle_stage_api_surface import (
    CallVisitor, audit, expanded_source_body, port_has_battle_barrier,
    port_battle_barrier_evidence, runtime_handlers, source_has_barrier,
    source_methods, source_signals,
)


class BattleStageApiAuditTest(unittest.TestCase):
    def test_ast_normalizes_stage_unit_calls_and_keeps_factory_call(self):
        visitor = CallVisitor("S_00")
        visitor.visit(ast.parse("stage.say('x')\nstage.unit(3).setAction(21)\n"))
        self.assertEqual(["stage.say", "stage.unit().setAction", "stage.unit"], [call["api"] for call in visitor.calls])

    def test_runtime_handler_parser_supports_aliases(self):
        text = '''private fun invokeCall() {\n            "stage.say" -> suspendFor(1f)\n            "stage.loseTest", "stage.isLose" -> return false\n        }\n        private fun stageVariableValue() = 0'''
        handlers = runtime_handlers(text)
        self.assertEqual({"stage.say", "stage.loseTest", "stage.isLose"}, set(handlers))

    def test_source_expansion_detects_wrapper_pause_callback(self):
        text = '''x.prototype.setObject=function(){this.setObject2();};\n+x.prototype.setObject2=function(){this.pause();this.loadRes(function(){this.resume();});};'''
        methods = source_methods(text)
        signals = source_signals(expanded_source_body("setObject", methods))
        self.assertTrue(source_has_barrier(signals))

    def test_r_only_suspend_is_not_a_battle_barrier(self):
        self.assertFalse(port_has_battle_barrier('if (moduleName.startsWith("R_")) suspendForInfo(text)'))
        self.assertTrue(port_has_battle_barrier('suspendFor(Float.MAX_VALUE)'))

    def test_dedicated_battle_resource_barrier_is_structurally_recognized(self):
        evidence = port_battle_barrier_evidence(
            'if (externalBattlePresentation) suspendForBattleBackgroundLoad(mapIndex)'
        )
        self.assertEqual("suspendForBattleBackgroundLoad(", evidence[0]["expression"])
        self.assertTrue(port_has_battle_barrier("suspendForBattleBackgroundLoad(0)"))

    def test_set_unit_attr_condition_uses_fresh_ast_draw_proof(self):
        import tempfile
        from pathlib import Path

        def run(script: str):
            with tempfile.TemporaryDirectory() as directory:
                root = Path(directory)
                scripts = root / "scripts"; modules = root / "modules"
                scripts.mkdir(); (modules / "battle").mkdir(parents=True); (modules / "ui").mkdir()
                (scripts / "S_00.py").write_text(script, encoding="utf-8")
                (modules / "battle/BattleLayer.js").write_text(
                    "x.prototype.setUnitAttr=function(){this.unit(e);this._loadAvatar(i);};\n"
                    "x.prototype._loadAvatar=function(){this.pause();this.resume();};\n",
                    encoding="utf-8",
                )
                (modules / "battle/BattleUnit.js").write_text("", encoding="utf-8")
                (modules / "ui/StageLayer.js").write_text("", encoding="utf-8")
                runtime = root / "Runtime.kt"
                runtime.write_text(
                    'private fun invokeCall() {\n'
                    ' "stage.setUnitAttr" -> stage.setUnitAttr()\n'
                    '}\n private fun stageVariableValue() = 0',
                    encoding="utf-8",
                )
                return next(row for row in audit(scripts, runtime, modules)["apis"]
                            if row["api"] == "stage.setUnitAttr")

        before = run("stage.setUnitAttr(1, 27, 2)\nstage.draw()\n")
        self.assertEqual([], before["blockingFindings"])
        self.assertEqual(
            {"live_unit_absent_before_first_stage_draw": 1},
            before["sourceBarrierCallSiteAssessment"]["classifications"],
        )
        after = run("stage.draw()\nstage.setUnitAttr(1, 27, 2)\n")
        self.assertEqual(["eager-mutation-without-source-callback-barrier"], after["blockingFindings"])

    def test_source_noop_unknown_is_informational_but_real_unknown_blocks(self):
        import tempfile
        from pathlib import Path
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            scripts = root / "scripts"; modules = root / "modules"
            scripts.mkdir(); (modules / "battle").mkdir(parents=True); (modules / "ui").mkdir()
            (scripts / "S_00.py").write_text("stage.guide()\nstage.center(1, 2)\n", encoding="utf-8")
            (modules / "battle/BattleLayer.js").write_text(
                "x.prototype.guide=function() {};\nx.prototype.center=function(){this.camera=1;};\n",
                encoding="utf-8",
            )
            (modules / "battle/BattleUnit.js").write_text("", encoding="utf-8")
            (modules / "ui/StageLayer.js").write_text("", encoding="utf-8")
            runtime = root / "Runtime.kt"
            runtime.write_text("private fun invokeCall() {\n        }\n        private fun stageVariableValue() = 0", encoding="utf-8")

            report = audit(scripts, runtime, modules)
            rows = {row["api"]: row for row in report["apis"]}

            self.assertEqual(["unknown-handler-source-noop"], rows["stage.guide"]["findings"])
            self.assertEqual([], rows["stage.guide"]["blockingFindings"])
            self.assertEqual(["unknown-handler"], rows["stage.center"]["blockingFindings"])

    def test_eager_mutation_without_original_callback_barrier_is_explicit(self):
        import tempfile
        from pathlib import Path
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            scripts = root / "scripts"; modules = root / "modules"
            scripts.mkdir(); (modules / "battle").mkdir(parents=True); (modules / "ui").mkdir()
            (scripts / "S_00.py").write_text("stage.loadBg('x')\n", encoding="utf-8")
            (modules / "battle/BattleLayer.js").write_text(
                "x.prototype.loadBg=function(){this.pause();this.loadRes(function(){this.resume();});};\n",
                encoding="utf-8",
            )
            (modules / "battle/BattleUnit.js").write_text("", encoding="utf-8")
            (modules / "ui/StageLayer.js").write_text("", encoding="utf-8")
            runtime = root / "Runtime.kt"
            runtime.write_text(
                'private fun invokeCall() {\n "stage.loadBg" -> stage.loadBg()\n }\n private fun stageVariableValue() = 0',
                encoding="utf-8",
            )

            row = audit(scripts, runtime, modules)["apis"][0]
            self.assertEqual(
                ["eager-mutation-without-source-callback-barrier"],
                row["blockingFindings"],
            )

    def test_current_implementation_has_no_surface_blockers(self):
        from pathlib import Path

        root = Path(__file__).resolve().parents[1]
        report = audit(
            root.parent / "jojo_mobile/sgccz-desktop/decompiled-python",
            root / "core/src/main/kotlin/com/jojo/port/PythonAstRuntime.kt",
            root.parent / "jojo_mobile/sgccz-desktop/recovered-js/modules",
            root / "tools/battle_stage_api_lifecycle_call_sites.json",
        )
        self.assertEqual(0, report["summary"]["blockingFindingApis"])
        rows = {row["api"]: row for row in report["apis"]}
        self.assertTrue(rows["stage.loadBg"]["runtimeBattleBarrier"])
        self.assertEqual(0, rows["stage.setUnitAttr"]["sourceBarrierCallSiteAssessment"]["requiredCallSites"])


if __name__ == "__main__":
    unittest.main()

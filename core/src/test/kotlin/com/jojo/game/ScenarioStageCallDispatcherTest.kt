// Test
package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.application.scenario.Frame
import com.jojo.game.application.scenario.RuntimeFunction
import com.jojo.game.application.scenario.ScenarioBattleScriptContext
import com.jojo.game.application.scenario.ScenarioConditionEnvironment
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.application.scenario.ScenarioRandomTrace
import com.jojo.game.application.scenario.ScenarioStageCallDispatcher
import com.jojo.game.application.scenario.ScenarioStageCallEnvironment
import com.jojo.game.application.scenario.ScenarioStage
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ScenarioStageCallDispatcherTest {
    @Test
    fun `routes state battle and presentation commands without changing their effects`() {
        val stage = ScenarioStage()
        val info = mutableListOf<Triple<String, ScenarioModalKind, Float>>()
        val env = environment(stage = stage, moduleName = "R_01", suspendForInfo = { text, kind, delay ->
            info += Triple(text, kind, delay)
        })

        assertEquals(0, dispatch("stage.loadBg", listOf(2, 30), env))
        assertEquals(71, stage.backgroundId)
        assertEquals(30, stage.backgroundVariant)

        assertEquals(0, dispatch("stage.draw", emptyList(), env))
        assertEquals(0, dispatch("stage.setStageName", listOf("Hulao Gate"), env))
        assertEquals("Hulao Gate", stage.stageName)
        assertEquals(listOf(Triple("Hulao Gate", ScenarioModalKind.EVENT, 1f)), info)

        env.gvars[4071] = 8
        assertEquals(0, dispatch("stage.info", listOf("controlled"), env))
        assertEquals(null, env.gvars[4071])
        assertEquals(listOf(8 to "controlled"), stage.controlledInfos)
    }

    @Test
    fun `routes random tracing and battle predicates while preserving fallback`() {
        val traces = mutableListOf<ScenarioRandomTrace>()
        var ended = 0
        val env = environment(
            moduleName = "S_22",
            randomTrace = traces,
            stopAfterRandomTraceCount = 1,
            nextModelRandom = { 37 },
            onEnd = { ended++ },
            battleContext = ScenarioBattleScriptContext(round = 3, camp = 2, playerDefeated = true),
        )
        val randomNode = JsonReader().parse("{ location: { line: 91 } }")

        assertEquals(37, dispatch("model.random", emptyList(), env, randomNode))
        assertEquals(listOf(ScenarioRandomTrace("S_22", "scene0", 91, 37)), traces)
        assertEquals(1, ended)
        assertEquals(3, dispatch("stage.round", emptyList(), env))
        assertEquals(2, dispatch("stage.curCamp", emptyList(), env))
        assertEquals(true, dispatch("stage.isLose", emptyList(), env))
        assertNull(ScenarioStageCallDispatcher.dispatch("stage.unknown", JsonReader().parse("{}"), emptyList(), frame(), env))
    }

    private fun dispatch(
        path: String,
        args: List<Any?>,
        env: ScenarioStageCallEnvironment,
        node: JsonValue = JsonReader().parse("{}"),
    ): Any? = assertNotNull(ScenarioStageCallDispatcher.dispatch(path, node, args, frame(), env)).value

    private fun frame() = Frame(RuntimeFunction("scene0", emptyList(), emptyMap()))

    private fun environment(
        stage: ScenarioStage = ScenarioStage(),
        moduleName: String = "S_01",
        randomTrace: MutableList<ScenarioRandomTrace> = mutableListOf(),
        stopAfterRandomTraceCount: Int? = null,
        nextModelRandom: () -> Int = { 0 },
        onEnd: () -> Unit = {},
        battleContext: ScenarioBattleScriptContext = ScenarioBattleScriptContext(round = 1, camp = 0),
        suspendForInfo: (String, ScenarioModalKind, Float) -> Unit = { _, _, _ -> },
    ): ScenarioStageCallEnvironment {
        val gvars = mutableMapOf<Int, Any?>()
        return ScenarioStageCallEnvironment(
            moduleName = moduleName,
            stage = stage,
            campaign = CampaignState(),
            battleContext = battleContext,
            gvars = gvars,
            pvars = mutableMapOf(),
            randomTrace = randomTrace,
            stopAfterRandomTraceCount = stopAfterRandomTraceCount,
            stagePresentationSkipped = false,
            externalBattlePresentation = false,
            pendingAskResult = null,
            suspendFor = {},
            suspendForBattleBackgroundLoad = {},
            suspendForInfo = suspendForInfo,
            suspendForAmbition = {},
            suspendForMapInfo = { _, _, _, _ -> },
            suspendForSection = { _, _ -> },
            suspendForWinCondition = {},
            nextModelRandom = nextModelRandom,
            onEnd = onEnd,
            onSetState = { _: PlaybackState -> },
            suspendForExternalFightCommand = {},
            conditionEnvironment = {
                ScenarioConditionEnvironment(gvars, mutableMapOf(), battleContext, stage::unitAttribute)
            },
        )
    }
}

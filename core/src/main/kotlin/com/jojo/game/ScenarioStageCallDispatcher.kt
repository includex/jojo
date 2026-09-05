package com.jojo.game

import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.application.scenario.Frame
import com.jojo.game.application.scenario.ScenarioBattleScriptContext
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.application.scenario.ScenarioRandomTrace
import com.jojo.game.application.scenario.ScenarioStageCallBattleDispatcher
import com.jojo.game.application.scenario.ScenarioStageCallConditionDispatcher
import com.jojo.game.application.scenario.ScenarioStageCallPresentationDispatcher
import com.jojo.game.application.scenario.ScenarioStageCallStateDispatcher
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.application.scenario.ScenarioConditionEnvironment

internal data class ScenarioStageCallEnvironment(
    val moduleName: String,
    val stage: ScenarioStage,
    val campaign: CampaignState,
    val battleContext: ScenarioBattleScriptContext,
    val gvars: MutableMap<Int, Any?>,
    val pvars: MutableMap<Int, Any?>,
    val randomTrace: MutableList<ScenarioRandomTrace>,
    val stopAfterRandomTraceCount: Int?,
    val stagePresentationSkipped: Boolean,
    val externalBattlePresentation: Boolean,
    val pendingAskResult: Int?,
    val suspendFor: (Float) -> Unit,
    val suspendForBattleBackgroundLoad: (Int) -> Unit,
    val suspendForInfo: (String, ScenarioModalKind, Float) -> Unit,
    val suspendForAmbition: (Int) -> Unit,
    val suspendForMapInfo: (String, Boolean, Boolean, Boolean) -> Unit,
    val suspendForSection: (Int, String) -> Unit,
    val suspendForWinCondition: (String) -> Unit,
    val nextModelRandom: () -> Int,
    val onEnd: () -> Unit,
    val onSetState: (PlaybackState) -> Unit,
    val suspendForExternalFightCommand: () -> Unit,
    val conditionEnvironment: () -> ScenarioConditionEnvironment,
)

internal fun interface ScenarioStageCallFamily {
    fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): ScenarioStageCallDispatcher.Result?
}

internal object ScenarioStageCallDispatcher {
    class Result(val value: Any?)

    private val families = listOf(
        ScenarioStageCallStateDispatcher,
        ScenarioStageCallBattleDispatcher,
        ScenarioStageCallPresentationDispatcher,
        ScenarioStageCallConditionDispatcher,
    )

    fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): Result? = families.firstNotNullOfOrNull { it.dispatch(path, node, args, frame, env) }
}

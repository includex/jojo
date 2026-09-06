// Game
package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState

/**
 * `ScenarioStageCallEnvironment` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

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

/**
 * `interface`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun interface ScenarioStageCallFamily {
    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): ScenarioStageCallDispatcher.Result?
}

/**
 * `ScenarioStageCallDispatcher` 싱글턴 객체: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object ScenarioStageCallDispatcher {
    /**
     * `Result` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    class Result(val value: Any?)

    /**
     * `families` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val families = listOf(
        ScenarioStageCallStateDispatcher,
        ScenarioStageCallBattleDispatcher,
        ScenarioStageCallPresentationDispatcher,
        ScenarioStageCallConditionDispatcher,
    )

    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioStageCallEnvironment,
    ): Result? = families.firstNotNullOfOrNull { it.dispatch(path, node, args, frame, env) }
}

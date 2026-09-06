// 시나리오 재생·오버레이 렌더링 정책
package com.jojo.game.presentation.scenario

import com.jojo.game.domain.scenario.ScenarioCompletionRoute
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.application.runtime.RuntimeScenarioOverlay

/** ScenarioRenderPhaseResult: 프레임 갱신 뒤 Screen이 계속 그릴지, 다음 화면으로 전환됐는지 구분한다. */
internal enum class ScenarioRenderPhaseResult {
    CONTINUE,
    ROUTED,
    CAPTURED,
}

/** ScenarioRenderPolicy: 재생 완료와 runtime 오버레이 점유 조건을 공통 순수 판정으로 제공한다. */
internal object ScenarioRenderPolicy {
    /** 독립 모달로 취급해 장면 기본 렌더와 분리해야 하는 거점 오버레이 집합이다. */
    private val isolatedHallOverlays = setOf(
        RuntimeScenarioOverlay.INFO, RuntimeScenarioOverlay.GET_ITEM_EQUIPMENT,
        RuntimeScenarioOverlay.GET_ITEM_PROPERTY, RuntimeScenarioOverlay.ITEM_EQUIPMENT,
        RuntimeScenarioOverlay.ITEM_PROPERTY, RuntimeScenarioOverlay.ITEM_DISCARD_CONFIRM,
        RuntimeScenarioOverlay.MAP_INFO, RuntimeScenarioOverlay.CHOICE, RuntimeScenarioOverlay.AMBITION,
        RuntimeScenarioOverlay.ASK, RuntimeScenarioOverlay.COMMAND, RuntimeScenarioOverlay.MENU,
        RuntimeScenarioOverlay.SAVE, RuntimeScenarioOverlay.SAVE_CONFIRM,
    )

    /** shouldContinueNaturally: 일반 scene 연쇄를 계속할 수 있는 완료 상태인지 판별한다. */
    fun shouldContinueNaturally(
        externalRuntimeOpen: Boolean,
        playbackState: PlaybackState,
        naturalSceneIndex: Int,
        menuVisible: Boolean,
        battleEndedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = !externalRuntimeOpen &&
        playbackState == PlaybackState.COMPLETE && (naturalSceneIndex == 0 || !menuVisible) &&
        !battleEndedByScript && sceneJumpTarget == null

    /** shouldRouteAfterCompletion: 스크립트 종료 결과를 다음 화면 전환으로 넘겨야 하는지 판별한다. */
    fun shouldRouteAfterCompletion(
        externalRuntimeOpen: Boolean,
        playbackState: PlaybackState,
        menuVisible: Boolean,
        battleEndedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = !externalRuntimeOpen &&
        ScenarioCompletionRoute.shouldRoute(
            playbackState,
            menuVisible,
            battleEndedByScript,
            sceneJumpTarget,
        )

    /**
     * `isStandaloneHallOverlay`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun isStandaloneHallOverlay(overlay: RuntimeScenarioOverlay?): Boolean = overlay in isolatedHallOverlays

    /**
     * `autoCloseEnabled`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun autoCloseEnabled(externalRuntimeOpen: Boolean, autoCloseSettingEnabled: Boolean): Boolean =
        !externalRuntimeOpen && autoCloseSettingEnabled
}

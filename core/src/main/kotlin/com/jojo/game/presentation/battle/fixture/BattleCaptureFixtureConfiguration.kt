// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleActionSample
import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleRoute

/** BattleCaptureFixtureConfiguration: 검증·캡처 전용 runtime route를 화면 준비 조건으로 판별한다. */
internal class BattleCaptureFixtureConfiguration(
    presentation: RuntimeBattlePresentation,
) {
    /** 캡처 fixture가 해석할 원본 runtime route다. */
    private val route = presentation.route

    /** 액션 fixture가 요구하는 원본 action과 sample 시점이다. */
    val actionSample: RuntimeBattleActionSample? = presentation.actionSample

    /** 액션 표본 시점으로 animation clock을 재생해야 하는지 나타낸다. */
    val actionSampleMode get() = actionSample != null

    /** 컷신 공격 capture route 여부를 나타낸다. */
    val cutsceneAttackCapture = route == RuntimeBattleRoute.CUTSCENE_ATTACK

    /** 컷신 피격 뒤 대사 capture route 여부를 나타낸다. */
    val cutscenePostHitCapture = route == RuntimeBattleRoute.CUTSCENE_POST_HIT

    /** 477 컷신 capture route 여부를 나타낸다. */
    val cutscene477Capture = route == RuntimeBattleRoute.CUTSCENE_477

    /** 대사 단계에서 공격을 자동 진행해야 하는 컷신 capture route 여부를 나타낸다. */
    val cutsceneCapture = cutsceneAttackCapture || cutscenePostHitCapture || cutscene477Capture

    /** 자동으로 진행할 대사 입력 횟수 fixture 값이다. */
    val dialogueStepCapture: Int? = presentation.dialogueStep

    /** 맵만 기록하는 capture route 여부를 나타낸다. */
    val mapOnlyCapture = route == RuntimeBattleRoute.MAP_ONLY

    /** 선택 영역 overlay를 고정해 기록하는 capture route 여부를 나타낸다. */
    val selectionOverlayCapture = route == RuntimeBattleRoute.SELECTION

    /** 시작 직후 fixture가 열어야 하는 모달 route를 반환한다. */
    val initialModalRoute = route.takeIf { it in MODAL_ROUTES }

    /** 모달 렌더 캡처를 위해 opening 대사를 한 번 넘겨야 하는지 나타낸다. */
    val modalRenderCapture = initialModalRoute != null

    /** 시작 대사 화면을 기록하는 capture route 여부를 나타낸다. */
    val openingSayRoute = route == RuntimeBattleRoute.OPENING_SAY

    /** 전투 HUD만 기록하는 capture route 여부를 나타낸다. */
    val hudRoute = route == RuntimeBattleRoute.HUD

    /** 대사 배경·인물 합성 상태를 기록하는 capture route 여부를 나타낸다. */
    val battleDialogueBlendRoute = route == RuntimeBattleRoute.DIALOGUE_BLEND

    private companion object {
        /** 시작 모달 fixture로 해석하는 runtime routes다. */
        val MODAL_ROUTES = setOf(
            RuntimeBattleRoute.MODAL_TERRAIN,
            RuntimeBattleRoute.MODAL_PROPERTY,
            RuntimeBattleRoute.MODAL_TREASURE,
            RuntimeBattleRoute.MODAL_SETTING,
            RuntimeBattleRoute.MODAL_SAVE,
            RuntimeBattleRoute.MODAL_LOAD,
            RuntimeBattleRoute.MODAL_FORCES,
        )
    }
}

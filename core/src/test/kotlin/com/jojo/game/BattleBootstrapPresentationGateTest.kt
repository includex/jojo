package com.jojo.game

import com.jojo.game.presentation.battle.BattleBootstrapCallbackState
import com.jojo.game.presentation.battle.completeInitialBattleOperation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleBootstrapPresentationGateTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleBootstrapPresentationGateTest {
    @Test
    fun `persistent unit poses are not bootstrap callbacks`() {
        // setAction(4/9) is intentionally absent from callback state: those
        // authored poses remain rendered after scene0 without delaying scene1.
        assertEquals(emptyList(), BattleBootstrapCallbackState().blockingReasons())
    }

    @Test
    fun `every finite authored callback blocks bootstrap until drained`() {
        assertEquals(
            listOf("move", "attackAction", "hide", "show", "fight"),
            BattleBootstrapCallbackState(
                move = true,
                attackAction = true,
                hide = true,
                show = true,
                fight = true,
            ).blockingReasons(),
        )
    }

    @Test
    fun `initial scene callback opens operation when scenario has no round-one startOper`() {
        val stage = ScenarioStage()
        assertFalse(stage.battleOperationStarted)

        completeInitialBattleOperation(stage)

        assertTrue(stage.battleOperationStarted)
        // Idempotent when a scenario did issue startOper itself.
        completeInitialBattleOperation(stage)
        assertTrue(stage.battleOperationStarted)
    }
}

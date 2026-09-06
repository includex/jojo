// Test
package com.jojo.game

import com.jojo.game.presentation.battle.bootstrap.BattleBootstrapCallbackState
import com.jojo.game.presentation.battle.bootstrap.completeInitialBattleOperation
import com.jojo.game.application.scenario.ScenarioStage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** BattleBootstrapPresentationGateTest: BattleBootstrapPresentationGate의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleBootstrapPresentationGateTest {
    @Test
    fun `persistent unit poses are not bootstrap callbacks`() {
        // 테스트 근거: 연출 프레임과 콜백 처리 순서을 검증한다.
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
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        completeInitialBattleOperation(stage)
        assertTrue(stage.battleOperationStarted)
    }
}

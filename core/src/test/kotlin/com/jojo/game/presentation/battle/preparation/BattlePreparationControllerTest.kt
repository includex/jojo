package com.jojo.game.presentation.battle.preparation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `BattlePreparationControllerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattlePreparationControllerTest {
    @Test fun `required selection is stable and maximum blocks additions`() {
        val controller = BattlePreparationController(listOf(1, 2, 3), listOf(2), minimum = 2, maximum = 2)

        controller.toggle(2)
        controller.toggle(1)
        controller.toggle(3)

        assertEquals(listOf(2, 1), controller.selection)
        assertTrue(controller.canStart)
        assertEquals(listOf(2, 1), controller.commit())
    }

    @Test fun `cursor wraps authored row and commit rejects too few units`() {
        val controller = BattlePreparationController(listOf(4, 8, 9), emptyList(), minimum = 2, maximum = 3)

        controller.moveCursor(-1)

        assertEquals(9, controller.cursorId)
        assertFalse(controller.canStart)
        assertNull(controller.commit())
    }

    @Test fun `touch prioritizes open sort modal and mutates roster hit`() {
        val controller = BattlePreparationController(listOf(1, 2), emptyList(), minimum = 1, maximum = 2)

        assertEquals(BattlePreparationAction.CancelSort, controller.touch(954f, 50f, sortOpen = true))
        assertEquals(BattlePreparationAction.SelectSort(0), controller.touch(700f, 275f, sortOpen = true))
        assertEquals(BattlePreparationAction.SelectionChanged, controller.touch(233.686f * .86f, 667.5f * .86f, false))
        assertEquals(listOf(1), controller.selection)
    }
}

// Test
package com.jojo.game.presentation.battle.preparation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** BattlePreparationControllerTest: BattlePreparationController의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

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

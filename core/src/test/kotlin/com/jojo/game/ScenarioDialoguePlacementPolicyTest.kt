// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.ScenarioDialoguePlacementPolicy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** ScenarioDialoguePlacementPolicyTest: 원본 DialogueLayer의 대화창 배치 규칙을 검증한다. */
class ScenarioDialoguePlacementPolicyTest {
    @Test
    fun `same speaker keeps bg index and changed speaker alternates`() {
        val policy = ScenarioDialoguePlacementPolicy()
        val y = mapOf(0 to 0f, 157 to 0f)

        assertEquals(0, policy.resolve(0, y::get).side)
        assertEquals(0, policy.resolve(0, y::get).bubbleIndex)
        assertEquals(1, policy.resolve(157, y::get).side)
        assertEquals(1, policy.resolve(157, y::get).bubbleIndex)
    }

    @Test
    fun `speaker zero resets original bubble sequence`() {
        val policy = ScenarioDialoguePlacementPolicy()
        val y = mapOf(0 to 0f, 157 to 0f)

        policy.resolve(157, y::get)
        assertEquals(0, policy.resolve(0, y::get).side)
        assertEquals(0, policy.resolve(0, y::get).bubbleIndex)
    }

    @Test
    fun `hall y position selects top and missing unit keeps previous side`() {
        val policy = ScenarioDialoguePlacementPolicy()
        val y = mapOf(157 to -60f)

        val first = policy.resolve(157, y::get)
        assertTrue(first.atTop)
        assertEquals(1, first.side)

        val missing = policy.resolve(999, y::get)
        assertFalse(missing.atTop)
        assertEquals(1, missing.side)
    }
}

// Test
package com.jojo.game

import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*
import com.jojo.game.application.runtime.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** FullBattleCampaignSeedTest: FullBattleCampaignSeed의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class FullBattleCampaignSeedTest {
    @Test fun `S00 fresh profile matches original single-unit prerequisite`() {
        val state = CampaignState().apply {
            joinedUnits += 99
            roster.restoreBattleRoster(listOf(99))
        }

        assertEquals(listOf(0), prepareDirectBattleCampaign(state, "S_00"))
        assertEquals(listOf(0), state.joinedUnits.toList())
        assertEquals(listOf(0), state.roster.battleRoster)
        assertEquals(3, state.unitAttribute(0, 18, 0))
    }

    @Test fun `later battles honor authored maximum required and excluded units`() {
        val state = CampaignState()
        val limit = ScenarioJoinBattleLimit(1, 11, listOf(22, 8), listOf(3))
        val expected = listOf(0, 22, 8, 1, 2, 4, 5, 6, 7, 9, 10)

        assertEquals(expected, prepareDirectBattleCampaign(state, "S_22", limit))
        assertEquals(expected, state.joinedUnits.toList())
        assertEquals(expected, state.roster.battleRoster)
        expected.forEach { assertEquals(3, state.unitAttribute(it, 18, 0)) }
    }

    @Test fun `later battle cannot silently use a fabricated generic roster`() {
        assertFailsWith<IllegalArgumentException> {
            prepareDirectBattleCampaign(CampaignState(), "S_22")
        }
    }

    @Test fun `out-of-manifest scenarios cannot produce misleading traces`() {
        assertFailsWith<IllegalArgumentException> {
            prepareDirectBattleCampaign(CampaignState(), "S_58")
        }
    }
}

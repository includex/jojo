package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FullBattleCampaignSeedTest {
    @Test fun `S00 fresh profile matches original single-unit prerequisite`() {
        val state = CampaignState().apply {
            joinedUnits += 99
            battleRoster += 99
        }

        assertEquals(listOf(0), prepareDirectFullBattleTraceCampaign(state, "S_00"))
        assertEquals(listOf(0), state.joinedUnits.toList())
        assertEquals(listOf(0), state.battleRoster)
        assertEquals(3, state.unitAttribute(0, 18, 0))
    }

    @Test fun `later battles honor authored maximum required and excluded units`() {
        val state = CampaignState()
        val limit = ScenarioJoinBattleLimit(1, 11, listOf(22, 8), listOf(3))
        val expected = listOf(0, 22, 8, 1, 2, 4, 5, 6, 7, 9, 10)

        assertEquals(expected, prepareDirectFullBattleTraceCampaign(state, "S_22", limit))
        assertEquals(expected, state.joinedUnits.toList())
        assertEquals(expected, state.battleRoster)
        expected.forEach { assertEquals(3, state.unitAttribute(it, 18, 0)) }
    }

    @Test fun `later battle cannot silently use a fabricated generic roster`() {
        assertFailsWith<IllegalArgumentException> {
            prepareDirectFullBattleTraceCampaign(CampaignState(), "S_22")
        }
    }

    @Test fun `out-of-manifest scenarios cannot produce misleading traces`() {
        assertFailsWith<IllegalArgumentException> {
            prepareDirectFullBattleTraceCampaign(CampaignState(), "S_58")
        }
    }
}

package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * class  `FullBattleCampaignSeedTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class FullBattleCampaignSeedTest {
    @Test fun `S00 fresh profile matches original single-unit prerequisite`() {
        val state = CampaignState().apply {
            joinedUnits += 99
            roster.restoreBattleRoster(listOf(99))
        }

        assertEquals(listOf(0), prepareDirectFullBattleTraceCampaign(state, "S_00"))
        assertEquals(listOf(0), state.joinedUnits.toList())
        assertEquals(listOf(0), state.roster.battleRoster)
        assertEquals(3, state.unitAttribute(0, 18, 0))
    }

    @Test fun `later battles honor authored maximum required and excluded units`() {
        val state = CampaignState()
        val limit = ScenarioJoinBattleLimit(1, 11, listOf(22, 8), listOf(3))
        val expected = listOf(0, 22, 8, 1, 2, 4, 5, 6, 7, 9, 10)

        assertEquals(expected, prepareDirectFullBattleTraceCampaign(state, "S_22", limit))
        assertEquals(expected, state.joinedUnits.toList())
        assertEquals(expected, state.roster.battleRoster)
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

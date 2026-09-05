package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * class  `BattleQiLingTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleQiLingTest {
    private fun damage(withTargetCampNeighbor: Boolean): Int {
        val units = mutableListOf(
            BattleUnit("a", "a", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(92 to 0, 176 to 20)),
            BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 20, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500),
        )
        if (withTargetCampNeighbor) units += BattleUnit("near", "near", Faction.ENEMY, 2, 0)
        val battle = Battle(units, events = emptyList())
        return assertIs<TacticalActionResult.Attack>(battle.attack("a", "t")).damage
    }

    @Test
    fun `QI_LING applies only when target BU_BING area has no same camp unit`() {
        // Source filterHitAreaUnit(target, BU_BING, 13) checks an existing
        // same-isMine neighbour and stops after its first match.
        assertEquals(11, damage(false) - damage(true))
    }
}

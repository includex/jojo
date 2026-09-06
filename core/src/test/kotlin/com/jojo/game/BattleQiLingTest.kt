// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** BattleQiLingTest: BattleQiLing의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleQiLingTest {
    private fun damage(withTargetCampNeighbor: Boolean): Int {
        val units = mutableListOf(
            BattleUnit("a", "a", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(92 to 0, 176 to 20)),
            BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 20, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500),
        )
        if (withTargetCampNeighbor) units += BattleUnit("near", "near", Faction.ENEMY, 2, 0)
        val battle = Battle(units, events = emptyList())
        return assertIs<TacticalActionResult.Attack>(battle.combat.attack("a", "t")).damage
    }

    @Test
    fun `QI_LING applies only when target BU_BING area has no same camp unit`() {
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (BU_BING)을 검증한다.
        assertEquals(11, damage(false) - damage(true))
    }
}

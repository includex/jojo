// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** BattleMpAttackTest: BattleMpAttack의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleMpAttackTest {
    @Test
    fun `MPGJ consumes one MP for normal hit and skips MRSP hit`() {
/** battle: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun battle(skills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "a", Faction.PLAYER, 0, 0, magicPoints = 2, maxMagicPoints = 2, critical = 1, morale = 1, skills = skills + (92 to 0)),
                BattleUnit("t", "t", Faction.ENEMY, 1, 0, critical = 100, morale = 100),
            ), events = emptyList(),
        )
        val normal = battle(mapOf(4 to 0))
        assertIs<TacticalActionResult.Attack>(normal.combat.attack("a", "t"))
        assertEquals(1, normal.units.getValue("a").magicPoints)
        val mrsp = battle(mapOf(4 to 0, 156 to 0))
        assertIs<TacticalActionResult.Attack>(mrsp.combat.attack("a", "t"))
        assertEquals(2, mrsp.units.getValue("a").magicPoints)
    }

    @Test
    fun `MPGJ also consumes one MP in forced attack calculation`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "a", Faction.PLAYER, 0, 0, magicPoints = 2, maxMagicPoints = 2, critical = 1, morale = 1, skills = mapOf(4 to 0, 92 to 0)),
                BattleUnit("t", "t", Faction.ENEMY, 1, 0, critical = 100, morale = 100),
            ), events = emptyList(),
        )
        assertIs<TacticalActionResult.Attack>(battle.combat.forcedAttack("a", "t"))
        assertEquals(1, battle.units.getValue("a").magicPoints)
    }
}

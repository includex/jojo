// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** BattleKnockbackTest: BattleKnockback의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleKnockbackTest {
    @Test
    fun `TPGJ pushes a defender one passable tile directly away`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(92 to 0, 221 to 5)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, defense = 20, hitPoints = 500, maxHitPoints = 500, attackOffsets = emptySet()),
            ), events = emptyList(),
        )
        assertIs<TacticalActionResult.Attack>(battle.combat.attack("a", "t"))
        assertEquals(2 to 0, battle.units.getValue("t").let { it.tileX to it.tileY })
    }

    @Test
    fun `TPGJ and YI_BU use source canBack result for blocked retreat`() {
/** battle: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun battle(blocked: Boolean) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(92 to 0, 221 to 5)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, defense = 20, hitPoints = 500, maxHitPoints = 500, attackOffsets = emptySet(), skills = mapOf(250 to 10)),
            ), events = emptyList(), blockedTiles = if (blocked) setOf(2 to 0) else emptySet(),
        )
        val open = battle(false)
        val blocked = battle(true)
        val openResult = assertIs<TacticalActionResult.Attack>(open.combat.attack("a", "t"))
        val blockedResult = assertIs<TacticalActionResult.Attack>(blocked.combat.attack("a", "t"))
        // 테스트 근거: 전투 계산·난수 소비·경계값 (TPGJ, YI_BU)을 검증한다.
        assertEquals(6, blockedResult.damage - openResult.damage)
        assertEquals(1 to 0, blocked.units.getValue("t").let { it.tileX to it.tileY })
    }
}

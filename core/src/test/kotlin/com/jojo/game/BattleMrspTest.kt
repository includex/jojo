// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleMrspDamage

import java.util.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** BattleMrspTest: BattleMrsp의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleMrspTest {
    @Test
    fun `MRSP source random ladder preserves every boundary`() {
        assertEquals(100, BattleMrspDamage.percent(0))
        assertEquals(100, BattleMrspDamage.percent(4))
        assertEquals(80, BattleMrspDamage.percent(5))
        assertEquals(80, BattleMrspDamage.percent(9))
        assertEquals(60, BattleMrspDamage.percent(10))
        assertEquals(60, BattleMrspDamage.percent(16))
        assertEquals(40, BattleMrspDamage.percent(17))
        assertEquals(40, BattleMrspDamage.percent(25))
        assertEquals(20, BattleMrspDamage.percent(26))
        assertEquals(20, BattleMrspDamage.percent(99))
        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        assertEquals(20, BattleMrspDamage.percent(100))
    }

    @Test
    fun `MRSP replaces ordinary and critical physical formula with original five-step max HP roll`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 999, critical = 100, morale = 100, skills = mapOf(156 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, hitPoints = 100, maxHitPoints = 100, defense = 999, critical = 1, morale = 1),
            ), events = emptyList(), random = object : Random() {
                // 테스트 근거: 전투 계산·난수 소비·경계값 (MRSP)을 검증한다.
                private val values = intArrayOf(26, 26); private var index = 0
                override fun nextInt(bound: Int) = values.getOrElse(index++) { 100 }.mod(bound)
            },
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "target"))
        // 테스트 근거: 전투 계산·난수 소비·경계값 (MRSP)을 검증한다.
        assertEquals(20, result.damage)
        assertEquals(20, result.followUpDamage)
        assertEquals(60, battle.units.getValue("target").hitPoints)
    }

    @Test
    fun `MRSP also replaces BattleScreen attackAction forced attack formula`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 999, critical = 100, morale = 100, skills = mapOf(156 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, hitPoints = 100, maxHitPoints = 100, defense = 999, critical = 1, morale = 1),
            ), events = emptyList(), random = Random(0),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.forcedAttack("attacker", "target"))
        assertEquals(20, result.damage)
    }
}

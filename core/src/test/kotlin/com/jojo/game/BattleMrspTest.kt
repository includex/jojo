package com.jojo.game
import com.jojo.game.domain.battle.BattleMrspDamage
import com.jojo.game.domain.battle.*

import java.util.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * class  `BattleMrspTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
        // Model.random() is Tool.random(0, 100), so the inclusive upper
        // endpoint is a real source outcome too.
        assertEquals(20, BattleMrspDamage.percent(100))
    }

    @Test
    fun `MRSP replaces ordinary and critical physical formula with original five-step max HP roll`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 999, critical = 100, morale = 100, skills = mapOf(156 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, hitPoints = 100, maxHitPoints = 100, defense = 999, critical = 1, morale = 1),
            ), events = emptyList(), random = object : Random() {
                // countRate handles hit/critical/continuous deterministically;
                // only MRSP itself consumes these two source random values.
                private val values = intArrayOf(26, 26); private var index = 0
                override fun nextInt(bound: Int) = values.getOrElse(index++) { 100 }.mod(bound)
            },
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "target"))
        // Every `_attack2` pass calls count_attackHarm. MRSP replaces both
        // the initial strike and the continuous strike with its five-step
        // max-HP roll.
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

// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** BattleAttackEffectAreaTest: BattleAttackEffectArea의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleAttackEffectAreaTest {
    @Test
    fun `physical CTGJ effect area applies original 20 percent rate reduction without a second hit roll`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 100, morale = 1, critical = 1,
                    skills = mapOf(92 to 0, 226 to 0), attackEffectOffsets = setOf(0 to 1)),
                BattleUnit("primary", "주대상", Faction.ENEMY, 1, 0, defense = 1, morale = 100, critical = 100),
                BattleUnit("splash", "범위대상", Faction.ENEMY, 1, 1, defense = 1, morale = 100, critical = 1),
            ),
            events = emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "primary"))

        val splash = result.splashTargets.single()
        assertEquals("splash", splash.targetId)
        assertEquals(100, splash.hitRate)
        // 테스트 근거: 전투 계산·난수 소비·경계값 (CTGJ)을 검증한다.
        assertEquals(result.damage * 80 / 100, splash.damage)
        assertEquals(100 - splash.damage, battle.units.getValue("splash").hitPoints)
    }

    @Test
    fun `physical CTGJ subtracts 20 from critical multiplier rather than multiplying critical harm by 80 percent`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 100, morale = 1, critical = 1,
                    skills = mapOf(92 to 0, 226 to 0, 270 to 0), attackEffectOffsets = setOf(0 to 1)),
                BattleUnit("primary", "주대상", Faction.ENEMY, 1, 0, defense = 1, morale = 100, critical = 100),
                BattleUnit("splash", "범위대상", Faction.ENEMY, 1, 1, defense = 1, morale = 100, critical = 1),
            ), events = emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "primary"))
        assertEquals(true, result.critical)
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (ZMYJZS)을 검증한다.
        assertEquals(120, result.splashTargets.single().damage)
        // 테스트 근거: 연출 프레임과 콜백 처리 순서을 검증한다.
        assertEquals(100, result.physicalPasses.single().targets.single { it.targetId == "splash" }.damage)
    }

    @Test
    fun `physical LIANGGE derives CTGJ target from attacker to target direction`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 100, morale = 1, critical = 1,
                    skills = mapOf(92 to 0, 226 to 0), attackEffectAreaId = 4),
                BattleUnit("primary", "주대상", Faction.ENEMY, 1, 0, defense = 1, morale = 100, critical = 100),
                // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (EFFAREA, LIANGGE)을 검증한다.
                BattleUnit("behind", "후방", Faction.ENEMY, 2, 0, defense = 1, morale = 100, critical = 1),
            ), events = emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "primary"))
        assertEquals(listOf("behind"), result.splashTargets.map(PhysicalTarget::targetId))
    }

    @Test
    fun `physical KUANGWU anchors static effect offsets at attacker not selected target`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 100, morale = 1, critical = 1,
                    skills = mapOf(92 to 0, 226 to 0), attackEffectAreaId = 10, attackEffectOffsets = setOf(2 to 0)),
                BattleUnit("primary", "주대상", Faction.ENEMY, 1, 0, defense = 1, morale = 100, critical = 100),
                BattleUnit("from-attacker", "광무대상", Faction.ENEMY, 2, 0, defense = 1, morale = 100, critical = 1),
                BattleUnit("from-target", "오답위치", Faction.ENEMY, 3, 0, defense = 1, morale = 100, critical = 1),
            ), events = emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "primary"))
        assertEquals(listOf("from-attacker"), result.splashTargets.map(PhysicalTarget::targetId))
    }

    @Test
    fun `physical HUIXUAN selects side cells from attacker to target direction`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 100, morale = 1, critical = 1,
                    skills = mapOf(92 to 0, 226 to 0), attackEffectAreaId = 9),
                BattleUnit("primary", "주대상", Faction.ENEMY, 1, 0, defense = 1, morale = 100, critical = 100),
                BattleUnit("side", "회선대상", Faction.ENEMY, 1, 1, defense = 1, morale = 100, critical = 1),
                BattleUnit("front", "오답위치", Faction.ENEMY, 2, 0, defense = 1, morale = 100, critical = 1),
            ), events = emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "primary"))
        assertEquals(listOf("side"), result.splashTargets.map(PhysicalTarget::targetId))
    }

    @Test
    fun `physical ZHUORE ignores table offsets exactly like source filterEffAreaUnit`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 100, morale = 1, critical = 100,
                    attackEffectAreaId = 0, attackEffectOffsets = setOf(1 to 0)),
                BattleUnit("primary", "주대상", Faction.ENEMY, 1, 0, defense = 1, morale = 100, critical = 1),
                BattleUnit("other", "오프셋대상", Faction.ENEMY, 2, 0, defense = 1, morale = 100, critical = 1),
            ), events = emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("attacker", "primary"))
        assertEquals(emptyList(), result.splashTargets)
    }
}

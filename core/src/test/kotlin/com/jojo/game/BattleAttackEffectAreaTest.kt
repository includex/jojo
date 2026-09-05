package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * class  `BattleAttackEffectAreaTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
        // Same defense/stat fixture: CTGJ rate is 80%, applied before the
        // final normal/critical multiplier; critical is disabled above.
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
        // Missing ZMYJZS returns 255, which is truthy in source JavaScript:
        // 180 - 20 = 160, so floor(75 * 160 / 100) = 120.
        assertEquals(120, result.splashTargets.single().damage)
        // countAtkHarm retains 120 for presentation while _attack3 can only
        // subtract the target's remaining 100 HP.  Both values are required.
        assertEquals(100, result.physicalPasses.single().targets.single { it.targetId == "splash" }.damage)
    }

    @Test
    fun `physical LIANGGE derives CTGJ target from attacker to target direction`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, attack = 100, morale = 1, critical = 1,
                    skills = mapOf(92 to 0, 226 to 0), attackEffectAreaId = 4),
                BattleUnit("primary", "주대상", Faction.ENEMY, 1, 0, defense = 1, morale = 100, critical = 100),
                // EFFAREA.LIANGGE's _filter3 starts one cell beyond target.
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

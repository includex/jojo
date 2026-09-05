package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * class  `BattleMpAttackTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleMpAttackTest {
    @Test
    fun `MPGJ consumes one MP for normal hit and skips MRSP hit`() {
/**
 * 공개 메서드 `battle`
 *
 * ### 파라미터
- `skills` (`Map<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

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

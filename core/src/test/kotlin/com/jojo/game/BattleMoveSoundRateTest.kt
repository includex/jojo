package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * class  `BattleMoveSoundRateTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleMoveSoundRateTest {
    private fun damage(targetMoveSound: Int, attackerSkills: Map<Int, Int>): Int {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "a", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = attackerSkills),
                BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 20, critical = 1, morale = 1, armMoveSound = targetMoveSound),
            ),
            events = emptyList(),
        )
        return assertIs<TacticalActionResult.Attack>(battle.combat.attack("a", "t")).damage
    }

    @Test
    fun `JMGJ PCGJ and BBGJ select target arm move sound exactly`() {
        val skills = mapOf(129 to 20, 164 to 30, 11 to 40)
        val none = damage(3, skills)
        // Base harm is 56, so source percentage application gives 67, 72,
        // and 78 respectively after truncation.
        assertEquals(67, damage(0, skills))
        assertEquals(72, damage(1, skills))
        assertEquals(78, damage(2, skills))
        assertEquals(56, none)
    }
}

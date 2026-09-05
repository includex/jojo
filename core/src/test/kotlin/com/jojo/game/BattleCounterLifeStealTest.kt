package com.jojo.game
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattleCounterLifeStealTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleCounterLifeStealTest {
    @Test
    fun `counterattack life steal is retained separately from first attack result`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, hitPoints = 500, maxHitPoints = 500, skills = mapOf(92 to 0)),
                BattleUnit("d", "반격", Faction.ENEMY, 1, 0, hitPoints = 40, maxHitPoints = 100, attack = 100, skills = mapOf(238 to 50)),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("a", "d", damage = 10) as TacticalActionResult.Attack

        assertEquals(40 - result.damage + result.counterLifeStealHealing, battle.units.getValue("d").hitPoints)
        assertEquals(true, result.counterLifeStealHealing >= 0)
    }
}

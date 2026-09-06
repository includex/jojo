// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** BattleUnitAttributeLiftTest: BattleUnitAttributeLift의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleUnitAttributeLiftTest {
    @Test fun `expired attribute lift retains configured packed round while normal`() {
        val enemy = BattleUnit(
            "enemy", "enemy", Faction.ENEMY, 1, 0,
            attributeLifts = linkedMapOf(BattleAttribute.MORALE to -1),
            attributeLiftRounds = linkedMapOf(BattleAttribute.MORALE to 1),
        )
        val battle = Battle(
            listOf(BattleUnit("mine", "mine", Faction.PLAYER, 0, 0), enemy),
            emptyList(),
        )

        battle.roundLifecycle.endTurn() // FRIEND
        battle.roundLifecycle.endTurn() // ENEMY: active DOWN expires through setStateRound(remove)

        assertNull(enemy.attributeLifts[BattleAttribute.MORALE])
        assertEquals(3, enemy.attributeLiftRounds[BattleAttribute.MORALE])

        repeat(4) { battle.roundLifecycle.endTurn() } // reach the next ENEMY start
        assertNull(enemy.attributeLifts[BattleAttribute.MORALE])
        assertEquals(3, enemy.attributeLiftRounds[BattleAttribute.MORALE])
    }

    @Test fun `source attribute lift moves one enum step and refreshes round first`() {
        val unit = BattleUnit("u", "unit", Faction.PLAYER, 0, 0)
        val attribute = BattleAttribute.ATTACK

        assertEquals(1, unit.applyAttributeLift(attribute, 1, 3))
        assertEquals(1, unit.attributeLifts[attribute])
        assertEquals(3, unit.attributeLiftRounds[attribute])

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (DOWN, NORMAL)을 검증한다.
        assertEquals(0, unit.applyAttributeLift(attribute, -1, 2))
        assertNull(unit.attributeLifts[attribute])
        assertEquals(2, unit.attributeLiftRounds[attribute])

        assertEquals(-1, unit.applyAttributeLift(attribute, -1, 1))
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (DOWN, NORMAL)을 검증한다.
        assertEquals(0, unit.applyAttributeLift(attribute, 1, 3))
        assertNull(unit.attributeLifts[attribute])

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(0, unit.applyAttributeLift(attribute, 0, 1))
        assertEquals(1, unit.attributeLiftRounds[attribute])
    }
}

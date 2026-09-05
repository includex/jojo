package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

        battle.endTurn() // FRIEND
        battle.endTurn() // ENEMY: active DOWN expires through setStateRound(remove)

        assertNull(enemy.attributeLifts[BattleAttribute.MORALE])
        assertEquals(3, enemy.attributeLiftRounds[BattleAttribute.MORALE])

        repeat(4) { battle.endTurn() } // reach the next ENEMY start
        assertNull(enemy.attributeLifts[BattleAttribute.MORALE])
        assertEquals(3, enemy.attributeLiftRounds[BattleAttribute.MORALE])
    }

    @Test fun `source attribute lift moves one enum step and refreshes round first`() {
        val unit = BattleUnit("u", "unit", Faction.PLAYER, 0, 0)
        val attribute = BattleAttribute.ATTACK

        assertEquals(1, unit.applySourceAttributeLift(attribute, 1, 3))
        assertEquals(1, unit.attributeLifts[attribute])
        assertEquals(3, unit.attributeLiftRounds[attribute])

        // Source UP(2) -> requested DOWN(0) reaches NORMAL(1), not DOWN.
        assertEquals(0, unit.applySourceAttributeLift(attribute, -1, 2))
        assertNull(unit.attributeLifts[attribute])
        assertEquals(2, unit.attributeLiftRounds[attribute])

        assertEquals(-1, unit.applySourceAttributeLift(attribute, -1, 1))
        // Source DOWN(0) -> requested UP(2) likewise reaches NORMAL first.
        assertEquals(0, unit.applySourceAttributeLift(attribute, 1, 3))
        assertNull(unit.attributeLifts[attribute])

        // _setStatusRound runs before the duplicate-state early return.
        assertEquals(0, unit.applySourceAttributeLift(attribute, 0, 1))
        assertEquals(1, unit.attributeLiftRounds[attribute])
    }
}

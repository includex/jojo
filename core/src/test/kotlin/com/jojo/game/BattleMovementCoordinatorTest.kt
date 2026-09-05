package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleMovementCoordinatorTest {

    @Test
    fun distanceCalculatesManhattanDistanceCorrectly() {
        val u1 = BattleUnit(id = "1", name = "A", faction = Faction.PLAYER, tileX = 2, tileY = 3)
        val u2 = BattleUnit(id = "2", name = "B", faction = Faction.ENEMY, tileX = 5, tileY = 7)
        assertEquals(7, BattleMovementCoordinator.distance(u1, u2))
    }

    @Test
    fun facingDirectionDeterminesFourWayHeading() {
        // 0 up, 1 right, 2 down, 3 left
        assertEquals(2, BattleMovementCoordinator.facingDirection(5, 5, 5, 7)) // down
        assertEquals(0, BattleMovementCoordinator.facingDirection(5, 5, 5, 3)) // up
        assertEquals(1, BattleMovementCoordinator.facingDirection(5, 5, 7, 5)) // right
        assertEquals(3, BattleMovementCoordinator.facingDirection(5, 5, 3, 5)) // left
    }

    @Test
    fun backPositionStepsDirectlyAwayFromAttacker() {
        val attacker = BattleUnit(id = "1", name = "Atk", faction = Faction.PLAYER, tileX = 5, tileY = 5)
        val defender = BattleUnit(id = "2", name = "Def", faction = Faction.ENEMY, tileX = 5, tileY = 6)
        val back = BattleMovementCoordinator.backPosition(defender, attacker, null, emptySet()) { _, _ -> null }
        assertEquals(5 to 7, back)
    }
}

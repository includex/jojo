package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleAiValueTest {
    @Test
    fun `turn entry clears source AIValue only for the active camp`() {
        val player = BattleUnit("p", "player", Faction.PLAYER, 0, 0, aiValue = 99)
        val enemy = BattleUnit("e", "enemy", Faction.ENEMY, 1, 0, aiValue = 77)
        val battle = Battle(listOf(player, enemy), emptyList())

        // Player -> enemy.  BattleLayer's turn init clears the camp that is
        // about to act; the inactive unit's AIValue remains available until
        // its own next turn entry.
        battle.endTurn()

        assertEquals(99, player.aiValue)
        assertEquals(0, enemy.aiValue)
    }

    @Test
    fun `only active and hold Control subclasses persist selected action score`() {
        fun run(ai: Int): BattleUnit {
            val enemy = BattleUnit("enemy-$ai", "적", Faction.ENEMY, 0, 0, ai = ai, attack = 100)
            val battle = Battle(
                listOf(BattleUnit("player-$ai", "아군", Faction.PLAYER, 1, 0), enemy),
                emptyList(),
            )
            battle.endTurn()
            battle.resolveAiTurn(maxUnits = 1)
            return enemy
        }

        // CtrlBDCJ extends base Control whose _AIProcess4 is empty. It can
        // attack but must not retain the transient action score.
        assertEquals(0, run(ControlAi.PASSIVE).aiValue)
        // CtrlZDCJ overrides _AIProcess4 and writes info.value.
        kotlin.test.assertTrue(run(ControlAi.ACTIVE).aiValue > 0)
        // CtrlJSYD uses the same override on its current-tile decision.
        kotlin.test.assertTrue(run(ControlAi.HOLD).aiValue > 0)
    }
}

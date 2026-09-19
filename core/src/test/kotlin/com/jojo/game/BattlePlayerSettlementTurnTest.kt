package com.jojo.game

import com.jojo.game.application.battle.Battle
import com.jojo.game.application.battle.BattleTurnController
import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.CampSettlementStage
import com.jojo.game.presentation.battle.settlement.BattleSettlementPresentationController
import com.jojo.game.presentation.battle.settlement.TurnSettlementOp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattlePlayerSettlementTurnTest {
    @Test
    fun `manual end turn waits for an active local settlement to finish`() = checkSettlementBarrier(false)

    @Test
    fun `delegation waits for an active local settlement to finish`() = checkSettlementBarrier(true)

    private fun checkSettlementBarrier(delegated: Boolean) {
        val battle = Battle(
            units = listOf(
                BattleUnit("mine", "mine", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "enemy", Faction.ENEMY, 4, 0),
            ),
            events = emptyList(),
        )
        val presentation = BattleSettlementPresentationController()
        var restoreCalls = 0
        var aiCalls = 0
        val controller = BattleTurnController(
            battle,
            showCamp = {},
            runCampScript = { true },
            runAi = { aiCalls++; AiTurnResult(0, 0, 0) },
            presentCampRestore = { restoreCalls++; false },
            playerPresentationReady = { !presentation.isActive() },
        )
        // A prompt can be opened before a callback starts an action-local settlement.
        assertTrue(controller.canEndPlayerTurn())
        presentation.start(
            BattleSettlementPlan(CampSettlementStage.START_STATE, Faction.PLAYER, emptyList(), emptyList()),
            listOf(TurnSettlementOp.Focus("mine", 1f, forceCenter = true)),
            local = true,
        )
        presentation.tick(0f) { true }
        val before = controller.snapshot
        assertFalse(controller.canEndPlayerTurn())
        repeat(2) {
            assertFalse(if (delegated) controller.runCollocatedPlayerTurn() else controller.endPlayerTurn())
        }
        assertEquals(before, controller.snapshot)
        assertEquals(Faction.PLAYER, battle.activeFaction)
        assertTrue(presentation.isActive())
        assertEquals(0, restoreCalls)
        assertEquals(0, aiCalls)

        presentation.tick(1f) { true }
        assertFalse(presentation.isActive())
        assertTrue(controller.canEndPlayerTurn())
        assertTrue(if (delegated) controller.runCollocatedPlayerTurn() else controller.endPlayerTurn())
        assertEquals(1, restoreCalls)
        assertEquals(if (delegated) 1 else 0, aiCalls)
    }
}

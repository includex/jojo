package com.jojo.game.presentation.battle.settlement

import com.jojo.game.presentation.battle.TurnSettlementOp
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.CampSettlementStage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BattleSettlementPresentationControllerTest {
    @Test
    fun `serial operations preserve focus wait synchronous sound and info barrier`() {
        val controller = BattleSettlementPresentationController()
        assertEquals(false, controller.start(plan(), listOf(
            TurnSettlementOp.Focus("u", 1f, forceCenter = true),
            TurnSettlementOp.Sound(3),
            TurnSettlementOp.Info2("short"),
        ), local = false))

        assertIs<BattleSettlementPresentationController.Effect.Focus>(controller.tick(0f) { true }.single())
        assertTrue(controller.tick(.9f) { true }.isEmpty())
        val afterFocus = controller.tick(1f) { true }
        assertEquals(2, afterFocus.size)
        assertIs<BattleSettlementPresentationController.Effect.Sound>(afterFocus[0])
        assertIs<BattleSettlementPresentationController.Effect.Info2>(afterFocus[1])
        assertEquals("short", controller.info2View()?.text)
        assertIs<BattleSettlementPresentationController.Effect.Finished>(controller.tick(2.2f) { true }.single())
    }

    @Test
    fun `actions wait for explicit completion before the next action`() {
        val controller = BattleSettlementPresentationController()
        controller.start(plan(), listOf(TurnSettlementOp.Actions("u", listOf(11, 12))), local = true)

        assertEquals(11, assertIs<BattleSettlementPresentationController.Effect.Actions>(controller.tick(0f) { true }.single()).actionId)
        controller.actionCompleted()
        assertEquals(12, assertIs<BattleSettlementPresentationController.Effect.Actions>(controller.tick(0f) { true }.single()).actionId)
        controller.actionCompleted()
        assertIs<BattleSettlementPresentationController.Effect.Finished>(controller.tick(0f) { true }.single())
    }

    private fun plan() = BattleSettlementPlan(CampSettlementStage.START_STATE, Faction.PLAYER, emptyList(), emptyList())
}

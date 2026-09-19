// Test
package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.CampSettlementStage
import com.jojo.game.domain.battle.settlement.SettlementGrowthGrant
import com.jojo.game.domain.battle.settlement.SettlementGrowthKind
import com.jojo.game.domain.battle.settlement.SettlementInfoDelta
import com.jojo.game.domain.battle.settlement.SettlementInfoKind
import com.jojo.game.domain.battle.settlement.SettlementInfoPanel
import com.jojo.game.domain.battle.settlement.SettlementUnitPlan
import com.jojo.game.domain.campaign.CampaignExperienceResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BattleSettlementPresentationControllerTest {
    @Test
    fun `other panel does not wait for experience rows that only mine displays`() {
        val grant = SettlementGrowthGrant(
            SettlementGrowthKind.UNIT_EXP, 5, CampaignExperienceResult(5, 1, 5, false),
        )
        for (panel in listOf(SettlementInfoPanel.OTHER, SettlementInfoPanel.MINE)) {
            val controller = BattleSettlementPresentationController()
            val unit = SettlementUnitPlan(
                "u", Faction.FRIEND, Faction.FRIEND, Faction.FRIEND, panel,
                listOf(SettlementInfoDelta(SettlementInfoKind.HP, 119, 104)), emptyList(),
            )
            controller.start(plan(), listOf(TurnSettlementOp.UnitInfo(unit, listOf(grant))), local = true)
            controller.tick(0f) { true }
            val mine = panel == SettlementInfoPanel.MINE
            assertEquals(if (mine) listOf(grant) else emptyList(), controller.infoView()?.grants)
            val barrier = if (mine) 2.4f else 1.4f
            assertTrue(controller.tick(barrier - .01f) { true }.isEmpty())
            assertIs<BattleSettlementPresentationController.Effect.Finished>(controller.tick(barrier + .001f) { true }.single())
            assertEquals(5, grant.unitResult?.gained, "filtering display rows does not change awarded experience")
        }
    }

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

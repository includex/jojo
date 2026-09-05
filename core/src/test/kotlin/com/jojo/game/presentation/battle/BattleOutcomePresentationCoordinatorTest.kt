package com.jojo.game.presentation.battle

import com.jojo.game.BattleOutcome
import com.jojo.game.LoseSceneFlow
import com.jojo.game.domain.battle.settlement.ResolvedBattleReward
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BattleOutcomePresentationCoordinatorTest {
    @Test
    fun `reward mutation is published before modal resume and next sync`() {
        val events = mutableListOf<String>()
        val port = FakePort(events).apply {
            reward = ResolvedBattleReward(money = 10, flag = 0, itemIds = emptyList(), end = false)
        }
        val coordinator = BattleOutcomePresentationCoordinator(port)

        coordinator.openRewardRequestIfNeeded()
        assertEquals("MONEY", coordinator.rewardFlow?.phase?.name)
        coordinator.advanceRewardFlow()

        assertEquals(listOf("campaign-mutation", "resume-modal", "sync"), events)
        assertNull(coordinator.rewardFlow)
    }

    @Test
    fun `item upgrade callback clears flow before notifying owner`() {
        val events = mutableListOf<String>()
        val port = FakePort(events).apply { upgrade = upgradePresentation() }
        val coordinator = BattleOutcomePresentationCoordinator(port)

        coordinator.openEquipmentUpgradeIfNeeded()
        coordinator.closeItemUpgrade()

        assertEquals(1, coordinator.itemUpgradeCallbackCount)
        assertNull(coordinator.itemUpgradeFlow)
        assertEquals(listOf("item-owner-notified"), events)
    }

    private fun upgradePresentation() = BattleOutcomePresentationCoordinator.UpgradePresentation(
        CampaignEquipmentExperienceResult(
            unitId = 1, slot = CampaignEquipmentSlot.WEAPON, itemId = 2, gained = 1,
            oldLevel = 1, newLevel = 2, oldExperience = 0, newExperience = 1,
            oldValue = 1, newValue = 2,
        ),
        ownerName = "유비", itemName = "단검", attributeName = "공격력",
    )

    private class FakePort(private val events: MutableList<String>) : BattleOutcomePresentationCoordinator.Port {
        var reward: ResolvedBattleReward? = null
        var upgrade: BattleOutcomePresentationCoordinator.UpgradePresentation? = null
        override fun visibleOutcome(): BattleOutcome? = BattleOutcome.PLAYER_VICTORY
        override fun rewardRequest(): ResolvedBattleReward? = reward?.also {
            events += "campaign-mutation"
            reward = null
        }
        override fun resumeRewardModal() { events += "resume-modal" }
        override fun syncScriptedUnits() { events += "sync" }
        override fun scene2Available() = false
        override fun startScene2() = Unit
        override fun scriptIsBlocked() = false
        override fun scriptState() = PlaybackState.COMPLETE
        override fun openSaveLayer() = Unit
        override fun nextScenario() = "next"
        override fun completeBattle(nextScenario: String) = Unit
        override fun showNextScenario(nextScenario: String) = Unit
        override fun finishTrace() = Unit
        override fun showVictoryPrompt() = Unit
        override fun campaignEquipmentUpgrade() = upgrade
        override fun equipmentUpgradeAllowed() = true
        override fun settlementUpgrade(request: CampaignEquipmentExperienceResult) = upgrade!!
        override fun itemUpgradeCompleted() { events += "item-owner-notified" }
        override fun createLoseScene() = LoseSceneFlow({}, {})
        override fun transitionBusy() = false
        override fun naturalTransitionAllowed() = false
        override fun routeCompleted() = false
        override fun battleEndedByScript() = false
        override fun runNaturalScene1() = Unit
    }
}

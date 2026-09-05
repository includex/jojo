package com.jojo.game.presentation.battle

import com.jojo.game.ItemUpgradeFlow
import com.jojo.game.LoseSceneFlow
import com.jojo.game.application.battle.BattleRewardFlow
import com.jojo.game.NaturalBattleTransition
import com.jojo.game.BattleOutcome
import com.jojo.game.domain.battle.settlement.ResolvedBattleReward
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.scenario.PlaybackState

/**
 * Owns the callback-driven result, reward and equipment-upgrade state.
 * Campaign writes are performed by the port before the corresponding modal is
 * published; this keeps the source ordering (mutation -> layer -> callback)
 * explicit and testable without leaking flow state into BattleScreen.
 */
internal class BattleOutcomePresentationCoordinator(private val port: Port) {
    enum class ResultFlow { NONE, LOSE_SCENE, WIN_SAVE_PROMPT }

    internal interface Port {
        fun visibleOutcome(): BattleOutcome?
        fun rewardRequest(): ResolvedBattleReward?
        fun resumeRewardModal()
        fun syncScriptedUnits()
        fun scene2Available(): Boolean
        fun startScene2()
        fun scriptIsBlocked(): Boolean
        fun scriptState(): PlaybackState
        fun openSaveLayer()
        fun nextScenario(): String
        fun completeBattle(nextScenario: String)
        fun showNextScenario(nextScenario: String)
        fun finishTrace()
        fun showVictoryPrompt()
        fun campaignEquipmentUpgrade(): UpgradePresentation?
        fun equipmentUpgradeAllowed(): Boolean
        fun settlementUpgrade(request: CampaignEquipmentExperienceResult): UpgradePresentation
        fun itemUpgradeCompleted()
        fun createLoseScene(): LoseSceneFlow
        fun transitionBusy(): Boolean
        fun naturalTransitionAllowed(): Boolean
        fun routeCompleted(): Boolean
        fun battleEndedByScript(): Boolean
        fun runNaturalScene1()
    }

    internal data class UpgradePresentation(
        val request: CampaignEquipmentExperienceResult,
        val ownerName: String,
        val itemName: String,
        val attributeName: String,
    )

    var resultFlow: ResultFlow = ResultFlow.NONE
        private set
    var rewardFlow: BattleRewardFlow? = null
        private set
    var itemUpgradeFlow: ItemUpgradeFlow? = null
        private set
    var postBattleSceneStarted = false
        private set
    var victorySaveAnswerPressed: Int? = null
    var postBattleSaveLayer = false
        private set
    var naturalOutcomeScriptStarted = false
        private set
    var itemUpgradeCallbackCount = 0
        private set
    var itemUpgradeRouteInstalled = false
        private set
    var loseSceneFlow: LoseSceneFlow? = null
        private set

    val loseSceneActive: Boolean get() = resultFlow == ResultFlow.LOSE_SCENE
    val winPromptActive: Boolean get() = resultFlow == ResultFlow.WIN_SAVE_PROMPT
    val resultIsNone: Boolean get() = resultFlow == ResultFlow.NONE
    val rewardActive: Boolean get() = rewardFlow != null
    val itemUpgradeActive: Boolean get() = itemUpgradeFlow != null

    fun continueAfterOutcome() {
        when (port.visibleOutcome()) {
            BattleOutcome.PLAYER_VICTORY -> {
                rewardFlow?.let { advanceRewardFlow(); return }
                if (!postBattleSceneStarted && port.scene2Available()) {
                    postBattleSceneStarted = true
                    port.startScene2()
                    port.syncScriptedUnits()
                    openRewardRequestIfNeeded()
                    if (port.scriptIsBlocked() || rewardFlow != null) return
                }
                if (port.scriptIsBlocked()) return
                openVictorySavePrompt()
            }
            BattleOutcome.ENEMY_VICTORY -> enterLoseScene()
            null -> Unit
        }
    }

    fun openVictorySavePrompt() {
        if (winPromptActive || postBattleSaveLayer || port.routeCompleted()) return
        port.finishTrace()
        resultFlow = ResultFlow.WIN_SAVE_PROMPT
        port.showVictoryPrompt()
    }

    fun answerVictorySavePrompt(answer: Int) {
        resultFlow = ResultFlow.NONE
        if (answer == 0) {
            postBattleSaveLayer = true
            port.openSaveLayer()
        } else finishVictoryRoute()
    }

    fun finishVictoryRoute() {
        if (postBattleSaveLayer) postBattleSaveLayer = false
        val next = port.nextScenario()
        port.completeBattle(next)
        port.showNextScenario(next)
    }

    fun openRewardRequestIfNeeded() {
        if (rewardFlow != null) return
        val resolved = port.rewardRequest() ?: return
        rewardFlow = BattleRewardFlow(resolved)
        if (rewardFlow?.complete == true) advanceRewardFlow()
    }

    fun advanceRewardFlow() {
        val flow = rewardFlow ?: return
        flow.advance()
        if (!flow.complete) return
        rewardFlow = null
        port.resumeRewardModal()
        port.syncScriptedUnits()
        openRewardRequestIfNeeded()
        if (postBattleSceneStarted && !port.scriptIsBlocked() && rewardFlow == null) openVictorySavePrompt()
    }

    fun driveNaturalBattleCompletion() {
        val transitionBusy = port.transitionBusy()
        if (!port.naturalTransitionAllowed() || port.routeCompleted() ||
            transitionBusy || port.visibleOutcome() != BattleOutcome.PLAYER_VICTORY
        ) return
        when (NaturalBattleTransition.completionAction(
            port.visibleOutcome(), transitionBusy, port.scriptState(), rewardFlow != null,
            port.battleEndedByScript(), naturalOutcomeScriptStarted,
        )) {
            NaturalBattleTransition.CompletionAction.WAIT -> Unit
            NaturalBattleTransition.CompletionAction.RUN_SCENE1 -> {
                naturalOutcomeScriptStarted = true
                port.runNaturalScene1()
                port.syncScriptedUnits()
                openRewardRequestIfNeeded()
            }
            NaturalBattleTransition.CompletionAction.START_SCENE2 -> continueAfterOutcome()
        }
    }

    fun openEquipmentUpgradeIfNeeded() {
        if (itemUpgradeFlow != null || !port.equipmentUpgradeAllowed()) return
        val details = port.campaignEquipmentUpgrade() ?: return
        itemUpgradeFlow = ItemUpgradeFlow(
            details.request, details.ownerName, details.itemName, details.attributeName,
        ) { completeItemUpgrade() }
    }

    fun openSettlementItemUpgrade(request: CampaignEquipmentExperienceResult) {
        check(itemUpgradeFlow == null) { "overlapping settlement ItemUpgradeLayer" }
        val details = port.settlementUpgrade(request)
        itemUpgradeFlow = ItemUpgradeFlow(
            details.request, details.ownerName, details.itemName, details.attributeName,
        ) { completeItemUpgrade() }
    }

    private fun completeItemUpgrade() {
        itemUpgradeCallbackCount++
        itemUpgradeFlow = null
        port.itemUpgradeCompleted()
    }

    fun closeItemUpgrade() {
        itemUpgradeFlow?.panelCancelTouchEnd()
    }

    fun markItemUpgradeRouteInstalled() {
        itemUpgradeRouteInstalled = true
    }

    fun enterLoseScene() {
        if (loseSceneActive) return
        resultFlow = ResultFlow.LOSE_SCENE
        loseSceneFlow = port.createLoseScene()
    }
}

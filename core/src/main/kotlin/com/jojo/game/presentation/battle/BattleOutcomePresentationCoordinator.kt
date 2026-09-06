package com.jojo.game.presentation.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.ItemUpgradeFlow
import com.jojo.game.LoseSceneFlow
import com.jojo.game.application.battle.BattleRewardFlow
import com.jojo.game.application.battle.NaturalBattleTransition
import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.settlement.ResolvedBattleReward
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.scenario.PlaybackState

/** 전투 결과·보상·장비 강화 흐름과 콜백 순서를 조정합니다. */
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

    /** 전투 결과에 따라 보상, 승리 저장 또는 패배 화면으로 진행합니다. */
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

    /** 승리 후 저장 확인창을 엽니다. */
    fun openVictorySavePrompt() {
        if (winPromptActive || postBattleSaveLayer || port.routeCompleted()) return
        port.finishTrace()
        resultFlow = ResultFlow.WIN_SAVE_PROMPT
        port.showVictoryPrompt()
    }

    /** 승리 저장 확인 결과를 처리합니다. */
    fun answerVictorySavePrompt(answer: Int) {
        resultFlow = ResultFlow.NONE
        if (answer == 0) {
            postBattleSaveLayer = true
            port.openSaveLayer()
        } else finishVictoryRoute()
    }

    /** 승리 처리를 완료하고 다음 시나리오로 이동합니다. */
    fun finishVictoryRoute() {
        if (postBattleSaveLayer) postBattleSaveLayer = false
        val next = port.nextScenario()
        port.completeBattle(next)
        port.showNextScenario(next)
    }

    /** 대기 중인 보상 요청이 있으면 보상 흐름을 시작합니다. */
    fun openRewardRequestIfNeeded() {
        if (rewardFlow != null) return
        val resolved = port.rewardRequest() ?: return
        rewardFlow = BattleRewardFlow(resolved)
        if (rewardFlow?.complete == true) advanceRewardFlow()
    }

    /** 현재 보상 흐름을 한 단계 진행합니다. */
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

    /** 자연 전투 종료 조건을 확인하고 후속 장면을 진행합니다. */
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

    /** 전투 보상에 따른 장비 강화창을 필요할 때 엽니다. */
    fun openEquipmentUpgradeIfNeeded() {
        if (itemUpgradeFlow != null || !port.equipmentUpgradeAllowed()) return
        val details = port.campaignEquipmentUpgrade() ?: return
        itemUpgradeFlow = ItemUpgradeFlow(
            details.request, details.ownerName, details.itemName, details.attributeName,
        ) { completeItemUpgrade() }
    }

    /** 정산 결과를 기반으로 장비 강화창을 엽니다. */
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

    /** 현재 장비 강화창을 취소합니다. */
    fun closeItemUpgrade() {
        itemUpgradeFlow?.panelCancelTouchEnd()
    }

    /** 장비 강화 경로가 설치되었음을 기록합니다. */
    fun markItemUpgradeRouteInstalled() {
        itemUpgradeRouteInstalled = true
    }

    /** 패배 장면 흐름으로 전환합니다. */
    fun enterLoseScene() {
        if (loseSceneActive) return
        resultFlow = ResultFlow.LOSE_SCENE
        loseSceneFlow = port.createLoseScene()
    }
}

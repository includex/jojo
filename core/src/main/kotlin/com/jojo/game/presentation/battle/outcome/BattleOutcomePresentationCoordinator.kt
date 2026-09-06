// Battle
package com.jojo.game.presentation.battle.outcome

import com.jojo.game.domain.battle.*

import com.jojo.game.presentation.battle.overlay.ItemUpgradeFlow
import com.jojo.game.application.battle.BattleRewardFlow
import com.jojo.game.application.battle.NaturalBattleTransition
import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.settlement.ResolvedBattleReward
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.scenario.PlaybackState

/** 전투 결과·보상·장비 강화 흐름과 콜백 순서를 조정합니다. */
internal class BattleOutcomePresentationCoordinator(private val port: Port) {
    /**
     * `ResultFlow`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class ResultFlow { NONE, LOSE_SCENE, WIN_SAVE_PROMPT }

    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        /**
         * `visibleOutcome`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun visibleOutcome(): BattleOutcome?
        /**
         * `rewardRequest`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun rewardRequest(): ResolvedBattleReward?
        /**
         * `resumeRewardModal`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun resumeRewardModal()
        /**
         * `syncScriptedUnits`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun syncScriptedUnits()
        /**
         * `scene2Available`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun scene2Available(): Boolean
        /**
         * `startScene2`: 흐름을 실행하거나 다음 단계로 전달한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun startScene2()
        /**
         * `scriptIsBlocked`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun scriptIsBlocked(): Boolean
        /**
         * `scriptState`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun scriptState(): PlaybackState
        /**
         * `openSaveLayer`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun openSaveLayer()
        /**
         * `nextScenario`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun nextScenario(): String
        /**
         * `completeBattle`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun completeBattle(nextScenario: String)
        /**
         * `showNextScenario`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun showNextScenario(nextScenario: String)
        /**
         * `finishTrace`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun finishTrace()
        /**
         * `showVictoryPrompt`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun showVictoryPrompt()
        /**
         * `campaignEquipmentUpgrade`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun campaignEquipmentUpgrade(): UpgradePresentation?
        /**
         * `equipmentUpgradeAllowed`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun equipmentUpgradeAllowed(): Boolean
        /**
         * `settlementUpgrade`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun settlementUpgrade(request: CampaignEquipmentExperienceResult): UpgradePresentation
        /**
         * `itemUpgradeCompleted`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun itemUpgradeCompleted()
        /**
         * `createLoseScene`: 객체나 결과를 생성한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun createLoseScene(): LoseSceneFlow
        /**
         * `transitionBusy`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun transitionBusy(): Boolean
        /**
         * `naturalTransitionAllowed`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun naturalTransitionAllowed(): Boolean
        /**
         * `routeCompleted`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun routeCompleted(): Boolean
        /**
         * `battleEndedByScript`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun battleEndedByScript(): Boolean
        /**
         * `runNaturalScene1`: 흐름을 실행하거나 다음 단계로 전달한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun runNaturalScene1()
    }
    /**
     * `UpgradePresentation`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class UpgradePresentation(
        /**
         * `request` (CampaignEquipmentExperienceResult,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request: CampaignEquipmentExperienceResult,
        /**
         * `ownerName` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val ownerName: String,
        /**
         * `itemName` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val itemName: String,
        /**
         * `attributeName` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attributeName: String,
    )

    /**
     * `resultFlow` (ResultFlow): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var resultFlow: ResultFlow = ResultFlow.NONE
        private set
    /**
     * `rewardFlow` (BattleRewardFlow?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var rewardFlow: BattleRewardFlow? = null
        private set
    /**
     * `itemUpgradeFlow` (ItemUpgradeFlow?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var itemUpgradeFlow: ItemUpgradeFlow? = null
        private set
    /**
     * `postBattleSceneStarted` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var postBattleSceneStarted = false
        private set
    /**
     * `victorySaveAnswerPressed` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var victorySaveAnswerPressed: Int? = null
    /**
     * `postBattleSaveLayer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var postBattleSaveLayer = false
        private set
    /**
     * `naturalOutcomeScriptStarted` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var naturalOutcomeScriptStarted = false
        private set
    /**
     * `itemUpgradeCallbackCount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var itemUpgradeCallbackCount = 0
        private set
    /**
     * `itemUpgradeRouteInstalled` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var itemUpgradeRouteInstalled = false
        private set
    /**
     * `loseSceneFlow` (LoseSceneFlow?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var loseSceneFlow: LoseSceneFlow? = null
        private set

    /**
     * `loseSceneActive` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loseSceneActive: Boolean get() = resultFlow == ResultFlow.LOSE_SCENE
    /**
     * `winPromptActive` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val winPromptActive: Boolean get() = resultFlow == ResultFlow.WIN_SAVE_PROMPT
    /**
     * `resultIsNone` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val resultIsNone: Boolean get() = resultFlow == ResultFlow.NONE
    /**
     * `rewardActive` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rewardActive: Boolean get() = rewardFlow != null
    /**
     * `itemUpgradeActive` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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

    /**
     * `completeItemUpgrade`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

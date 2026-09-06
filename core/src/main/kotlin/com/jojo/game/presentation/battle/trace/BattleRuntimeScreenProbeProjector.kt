// Battle Trace
package com.jojo.game.presentation.battle.trace

import com.jojo.game.application.runtime.BattleRuntimeProbe
import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.scenario.PlaybackState

/** 전투 화면 probe 입력: 검증 구동기에 노출할 화면·시나리오·좌표 상태를 한 프레임 값으로 묶는다. */
internal data class BattleRuntimeScreenProbeInput(
    val scenario: String,
    val playback: PlaybackState,
    val outcome: BattleOutcome?,
    val bootstrapComplete: Boolean,
    val initialScene1Started: Boolean,
    val resultScene1Started: Boolean,
    val scene2Started: Boolean,
    val rewardOpen: Boolean,
    val winConditionsOpen: Boolean,
    val savePromptOpen: Boolean,
    val losePromptOpen: Boolean,
    val loseTitle: Pair<Int, Int>,
    val playerMoveCommitted: Boolean,
    val campaignStage: Int,
    val turnPhase: String,
    val battleMenuOpen: Boolean,
    val battleCommandOpen: Boolean,
    val battleTargetSelectionOpen: Boolean,
    val magickListOpen: Boolean,
    val magicTargetSelection: Boolean,
    val commandWait: Pair<Int, Int>,
    val menuEndRound: Pair<Int, Int>,
    val battleMenuButton: Pair<Int, Int>,
    val autoBattleToggle: Pair<Int, Int>,
    val autoBattleConfirm: Pair<Int, Int>,
    val autoBattleOverlay: String,
    val autoBattleChecked: Boolean,
    val collocation: Boolean,
    val committedPlayerMove: String?,
    val selectedChoice: Int,
    val selectedUnitId: String?,
)

/** 전투 화면 probe 투영기: 화면 전용 상태를 자동 전투 검증기의 불변 조회 계약으로 변환한다. */
internal object BattleRuntimeScreenProbeProjector {
    /** 투영: 화면 플래그와 입력 좌표를 런타임 구동기의 BattleRuntimeScreenProbe로 조립한다. */
    fun project(input: BattleRuntimeScreenProbeInput, battle: BattleRuntimeProbe): BattleRuntimeScreenProbe = BattleRuntimeScreenProbe(
        scenario = input.scenario,
        playback = input.playback,
        outcome = input.outcome,
        bootstrapComplete = input.bootstrapComplete,
        initialScene1Started = input.initialScene1Started,
        resultScene1Started = input.resultScene1Started,
        scene2Started = input.scene2Started,
        rewardOpen = input.rewardOpen,
        winConditionsOpen = input.winConditionsOpen,
        savePromptOpen = input.savePromptOpen,
        losePromptOpen = input.losePromptOpen,
        loseTitleScreenX = input.loseTitle.first,
        loseTitleScreenY = input.loseTitle.second,
        playerMoveCommitted = input.playerMoveCommitted,
        campaignStage = input.campaignStage,
        turnPhase = input.turnPhase,
        battleMenuOpen = input.battleMenuOpen,
        battleCommandOpen = input.battleCommandOpen,
        battleTargetSelectionOpen = input.battleTargetSelectionOpen,
        magickListOpen = input.magickListOpen,
        magicTargetSelection = input.magicTargetSelection,
        commandWaitScreenX = input.commandWait.first,
        commandWaitScreenY = input.commandWait.second,
        menuEndRoundScreenX = input.menuEndRound.first,
        menuEndRoundScreenY = input.menuEndRound.second,
        battleMenuButtonScreenX = input.battleMenuButton.first,
        battleMenuButtonScreenY = input.battleMenuButton.second,
        autoBattleToggleScreenX = input.autoBattleToggle.first,
        autoBattleToggleScreenY = input.autoBattleToggle.second,
        autoBattleConfirmScreenX = input.autoBattleConfirm.first,
        autoBattleConfirmScreenY = input.autoBattleConfirm.second,
        autoBattleOverlay = input.autoBattleOverlay,
        autoBattleChecked = input.autoBattleChecked,
        collocation = input.collocation,
        committedPlayerMove = input.committedPlayerMove,
        selectedChoice = input.selectedChoice,
        selectedUnitId = input.selectedUnitId,
        battle = battle,
    )
}

package com.jojo.game.verification.campaign

internal class CampaignE2eBattleStateProjection {
    fun project(board: CampaignE2eBattlePlanningBoard, movePlan: CampaignE2eMovePlan, inputs: CampaignE2eActionInputs): CampaignE2eBattleState {
        val screen = board.screen
        val snapshot = board.probe.snapshot
        val actionRemaining = board.scenario == "S_01" && snapshot.units.any {
            it.visible && it.type() == com.jojo.game.domain.battle.Faction.PLAYER && !it.hasActed && productionManualUnitEligible(it.statuses)
        }
        return CampaignE2eBattleState(
            scenario = screen.scenario,
            playback = screen.playback,
            outcome = screen.outcome.takeIf { screen.bootstrapComplete },
            initialScene1Started = screen.initialScene1Started,
            resultScene1Started = screen.resultScene1Started,
            scene2Started = screen.scene2Started,
            rewardOpen = screen.rewardOpen,
            winConditionsOpen = screen.winConditionsOpen,
            savePromptOpen = screen.savePromptOpen,
            losePromptOpen = screen.losePromptOpen,
            loseTitleScreenX = screen.loseTitleScreenX,
            loseTitleScreenY = screen.loseTitleScreenY,
            playerMoveCommitted = screen.playerMoveCommitted,
            campaignStage = screen.campaignStage,
            round = snapshot.round,
            activeFaction = snapshot.activeFaction,
            turnPhase = screen.turnPhase,
            battleMenuOpen = screen.battleMenuOpen,
            battleCommandOpen = screen.battleCommandOpen,
            battleTargetSelectionOpen = screen.battleTargetSelectionOpen,
            selectedUnit = screen.selectedUnitId != null,
            manualMoveInput = movePlan.manualMove,
            manualAttackInput = inputs.manualAttack,
            magickListOpen = screen.magickListOpen,
            magicTargetSelection = screen.magicTargetSelection,
            manualMagicInput = inputs.manualMagic,
            commandWaitScreenX = screen.commandWaitScreenX,
            commandWaitScreenY = screen.commandWaitScreenY,
            menuEndRoundScreenX = screen.menuEndRoundScreenX,
            menuEndRoundScreenY = screen.menuEndRoundScreenY,
            battleMenuButtonScreenX = screen.battleMenuButtonScreenX,
            battleMenuButtonScreenY = screen.battleMenuButtonScreenY,
            autoBattleToggleScreenX = screen.autoBattleToggleScreenX,
            autoBattleToggleScreenY = screen.autoBattleToggleScreenY,
            autoBattleConfirmScreenX = screen.autoBattleConfirmScreenX,
            autoBattleConfirmScreenY = screen.autoBattleConfirmScreenY,
            manualMoveDebug = snapshot.units.joinToString(";") { unit ->
                "${unit.id}/${unit.faction}/v=${unit.visible}/a=${unit.hasActed}/${unit.x},${unit.y}/r=${board.probe.reachableTiles(unit.id).size}"
            },
            autoBattleOverlay = screen.autoBattleOverlay,
            autoBattleChecked = screen.autoBattleChecked,
            collocation = screen.collocation,
            committedPlayerMove = screen.committedPlayerMove,
            selectedChoice = screen.selectedChoice,
            guidedAuthoredRoute = board.guidedAuthoredRoute,
            authoredRouteHoldFire = board.s57Route.holdFire,
            s01EligibleMineActionRemaining = actionRemaining,
        )
    }
}

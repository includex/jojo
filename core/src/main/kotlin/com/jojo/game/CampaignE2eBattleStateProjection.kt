package com.jojo.game
import com.jojo.game.domain.battle.*

internal class CampaignE2eBattleStateProjection {
    fun project(
        board: CampaignE2eBattlePlanningBoard,
        movePlan: CampaignE2eMovePlan,
        actionInputs: CampaignE2eActionInputs,
    ): CampaignE2eBattleState {
        val ctx = board.ctx
        val battle = board.battle
        val s57Route = board.s57Route
        val waitCommand = ctx.waitCommandScreenPoint
        val endRoundCommand = ctx.endRoundCommandScreenPoint
        val battleMenuButton = ctx.battleMenuButtonScreenPoint
        val autoBattleToggleButton = ctx.autoBattleToggleScreenPoint
        val autoBattleConfirmButton = ctx.autoBattleConfirmScreenPoint
        val loseTitleButton = ctx.loseTitleScreenPoint
        val s01EligibleMineActionRemaining = board.scenario == "S_01" && battle.units.values.any { unit ->
            unit.visible && unit.type() == Faction.PLAYER && !unit.hasActed && productionManualUnitEligible(unit.statuses)
        }
        return CampaignE2eBattleState(
            scenario = board.scenario,
            playback = ctx.scriptState,
            outcome = battle.outcome().takeIf { ctx.bootstrapPhase == BattleBootstrapPhase.COMPLETE },
            initialScene1Started = ctx.initialPlayerCampScriptStarted,
            resultScene1Started = ctx.resultScene1Observed || ctx.naturalOutcomeScriptStarted,
            scene2Started = ctx.postBattleSceneStarted,
            rewardOpen = ctx.rewardOpen,
            winConditionsOpen = ctx.winConditionsOpen,
            savePromptOpen = ctx.savePromptOpen,
            losePromptOpen = ctx.losePromptOpen,
            loseTitleScreenX = loseTitleButton.first,
            loseTitleScreenY = loseTitleButton.second,
            playerMoveCommitted = ctx.playerMoveCommitted,
            campaignStage = ctx.campaignStage,
            round = battle.round,
            activeFaction = battle.activeFaction,
            turnPhase = ctx.turnPhase,
            battleMenuOpen = ctx.battleMenuOpen,
            battleCommandOpen = ctx.battleCommandOpen,
            battleTargetSelectionOpen = ctx.battleTargetSelectionOpen,
            selectedUnit = ctx.selectedUnitId != null,
            manualMoveInput = movePlan.manualMove,
            manualAttackInput = actionInputs.manualAttack,
            magickListOpen = ctx.magickListOpen,
            magicTargetSelection = ctx.magicMode,
            manualMagicInput = actionInputs.manualMagic,
            commandWaitScreenX = waitCommand.first,
            commandWaitScreenY = waitCommand.second,
            menuEndRoundScreenX = endRoundCommand.first,
            menuEndRoundScreenY = endRoundCommand.second,
            battleMenuButtonScreenX = battleMenuButton.first,
            battleMenuButtonScreenY = battleMenuButton.second,
            autoBattleToggleScreenX = autoBattleToggleButton.first,
            autoBattleToggleScreenY = autoBattleToggleButton.second,
            autoBattleConfirmScreenX = autoBattleConfirmButton.first,
            autoBattleConfirmScreenY = autoBattleConfirmButton.second,
            manualMoveDebug = battle.units.values.joinToString(";") {
                "${it.id}/${it.faction}/v=${it.visible}/a=${it.hasActed}/${it.tileX},${it.tileY}/r=${
                    battle.movement.reachableTiles(
                        it.id
                    ).size
                }"
            },
            autoBattleOverlay = ctx.autoBattleOverlay,
            autoBattleChecked = ctx.autoBattleChecked,
            collocation = ctx.collocation,
            committedPlayerMove = ctx.committedPlayerMove,
            selectedChoice = ctx.selectedChoice,
            guidedAuthoredRoute = board.guidedAuthoredRoute,
            authoredRouteHoldFire = s57Route.holdFire,
            s01EligibleMineActionRemaining = s01EligibleMineActionRemaining,
        )
    }
}

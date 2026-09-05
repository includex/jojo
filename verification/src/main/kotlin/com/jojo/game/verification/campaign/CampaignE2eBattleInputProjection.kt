package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot
import com.jojo.game.domain.battle.BattleStatus

internal data class CampaignE2eActionInputs(
    val manualAttack: CampaignE2eAttackInput?,
    val manualMagic: CampaignE2eMagicInput?,
)

internal class CampaignE2eBattleInputProjection {
    fun project(board: CampaignE2eBattlePlanningBoard, movePlan: CampaignE2eMovePlan): CampaignE2eActionInputs {
        val selected = board.selected ?: return CampaignE2eActionInputs(null, null)
        val current = selected.tile()
        val attackTarget = board.attackableFrom(selected, current).firstOrNull()
        val attack = attackTarget?.takeIf { target ->
            selected.visible && !selected.hasActed && selected.effectiveFaction() == board.screen.activeFaction &&
                BattleStatus.PARALYSIS !in selected.statuses && BattleStatus.CONFUSION !in selected.statuses &&
                !board.s57Route.holdFire && !(board.protectS57MineMaster && selected.characterId == 0 && !movePlan.s57CriticalFinisherActive)
        }?.let { target ->
            val command = board.probe.projectWorldPoint(803.6f, 351.175f)
            val point = board.probe.screenPoint(target.tile().point())
            CampaignE2eAttackInput(command.x, command.y, point.x, point.y, target.id)
        }
        val magicPlan = board.guidedMagicPlanFor(selected)
        val magic = magicPlan?.let { plan ->
            val target = board.units.firstOrNull { it.id == plan.targetId } ?: return@let null
            val row = selected.magic.filter { it.expendMp != 255 }.sortedBy { it.id }.indexOfFirst { it.id == plan.magicId }
            if (row < 0) return@let null
            val command = board.probe.projectWorldPoint(931.6f, 351.175f)
            val rowPoint = board.probe.projectWorldPoint(if (row % 2 == 0) 611.686f else 874.686f, 574.5f - (row / 2) * 142f)
            val point = board.probe.screenPoint(target.tile().point())
            CampaignE2eMagicInput(command.x, command.y, rowPoint.x, rowPoint.y, point.x, point.y)
        }
        return CampaignE2eActionInputs(attack, magic)
    }
}

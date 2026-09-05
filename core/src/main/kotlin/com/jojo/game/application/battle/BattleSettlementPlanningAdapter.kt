package com.jojo.game.application.battle

import com.jojo.game.BattleUnit
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.MagicLocalSettlement
import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.BattleSettlementPlanner
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.settlement.SettlementStateChange
import com.jojo.game.domain.battle.settlement.SettlementUnitSnapshot

/** Translates mutable battle aggregates into the immutable planner boundary. */
object BattleSettlementPlanningAdapter {
    fun plan(
        settlement: CampSettlement,
        unitsById: Map<String, BattleUnit>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan = BattleSettlementPlanner.plan(settlement, unitsById.snapshots(), resolveMeffId)

    fun planMagicLocal(
        settlement: MagicLocalSettlement,
        camp: Faction,
        unitsById: Map<String, BattleUnit>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan = BattleSettlementPlanner.planMagicLocal(settlement, camp, unitsById.snapshots(), resolveMeffId)

    private fun Map<String, BattleUnit>.snapshots(): Map<String, SettlementUnitSnapshot> = mapValues { (_, unit) ->
        SettlementUnitSnapshot(unit.id, unit.baseFaction, unit.skills.keys.toSet(), BattleStatus.LOST in unit.statuses)
    }
}

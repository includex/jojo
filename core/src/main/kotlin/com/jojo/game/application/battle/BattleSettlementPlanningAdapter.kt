// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.MagicLocalSettlement
import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.BattleSettlementPlanner
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.settlement.SettlementStateChange
import com.jojo.game.domain.battle.settlement.SettlementUnitSnapshot

/** BattleSettlementPlanningAdapter: 가변 전투 유닛을 정산 계획 입력으로 변환해, 계산 경계에 불변 값을 전달한다. */
object BattleSettlementPlanningAdapter {
    /**
     * `plan`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun plan(
        settlement: CampSettlement,
        unitsById: Map<String, BattleUnit>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan = BattleSettlementPlanner.plan(settlement, unitsById.snapshots(), resolveMeffId)

    /**
     * `planMagicLocal`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun planMagicLocal(
        settlement: MagicLocalSettlement,
        camp: Faction,
        unitsById: Map<String, BattleUnit>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan = BattleSettlementPlanner.planMagicLocal(settlement, camp, unitsById.snapshots(), resolveMeffId)

    /**
     * `Map`: 입력을 규칙에 따라 계산·변환한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun Map<String, BattleUnit>.snapshots(): Map<String, SettlementUnitSnapshot> = mapValues { (_, unit) ->
        SettlementUnitSnapshot(unit.id, unit.baseFaction, unit.skills.keys.toSet(), BattleStatus.LOST in unit.statuses)
    }
}

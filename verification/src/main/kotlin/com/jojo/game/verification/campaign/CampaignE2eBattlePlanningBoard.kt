// Verification
package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.isEnemySide

/** CampaignE2eBattlePlanningBoard: 전투 검증 상태와 선택 정보를 모으는 타입이다. */
internal class CampaignE2eBattlePlanningBoard(val ctx: CampaignE2eProjectionContext) {
    val screen = ctx.screen
    val probe = screen.battle
    val units = probe.snapshot.units
    val selected = screen.selectedUnitId?.let { id -> units.firstOrNull { it.id == id } }
    val scenario = screen.scenario
    val guidedAuthoredRoute = scenario == "S_52" || scenario == "S_57"
    val visibleEnemies = units.filter { it.visible && it.hitPoints > 0 && it.type().isEnemySide() }
    val playerTiles = units.asSequence().filter { it.visible && it.type() == Faction.PLAYER }.map { it.tile() }.toList()
    val s57FirstRoomFocus = s57FirstRoomEscortFocus(visibleEnemies.mapNotNull { enemy ->
        enemy.characterId?.takeIf { it in FIRST_ROOM_LEADERS }?.let { id ->
            S57FirstRoomLeader(enemy.id, id, enemy.hitPoints, enemy.tile())
        }
    })
    val s57Route = s57AuthoredRouteSignal(
        scenario,
        visibleEnemies.mapNotNull { it.characterId },
        units.any { it.visible && it.type() == Faction.PLAYER && it.characterId == 0 && it.x in 2..16 && it.y in 11..23 },
        playerTiles.size,
    )
    val s57GateTarget = s57Route.gateTarget
    val waitForAuthoredAttrition = s57Route.waitForAttrition
    val protectS57MineMaster = scenario == "S_57" && s57FirstRoomFocus != null
    val s57FirstRoomFocusUnit = s57FirstRoomFocus?.unitId?.let { id -> units.firstOrNull { it.id == id } }
    val routedVisibleEnemies = visibleEnemies
    val occupiedTiles = units.asSequence().filter { it.visible && it.hitPoints > 0 }.map { it.tile() }.toSet()
    val strategicTarget = strategicTarget()

    /** targetPriority: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun targetPriority(unit: RuntimeBattleUnitSnapshot): Int = targetOrder().indexOf(unit.characterId).takeIf { it >= 0 } ?: 10_000

    /** canAttack: 공격 가능 여부를 전투 규칙으로 판정한다. */
    fun canAttack(unit: RuntimeBattleUnitSnapshot, tile: Pair<Int, Int>, target: RuntimeBattleUnitSnapshot): Boolean =
        unit.attackAllScreen || (target.x - tile.first to target.y - tile.second) in unit.attackOffsets.map { it.tile() }

    /** attackableFrom: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun attackableFrom(unit: RuntimeBattleUnitSnapshot, tile: Pair<Int, Int>): List<RuntimeBattleUnitSnapshot> {
        val attackable = routedVisibleEnemies.filter { canAttack(unit, tile, it) }
        if (scenario == "S_01") {
            val byId = attackable.associateBy { it.id }
            return s01PreferredAttackTargets(attackable.map { S01EnemyTarget(it.id, it.characterId, it.hitPoints) }, visibleEnemies.size)
                .mapNotNull { byId[it.unitId] }
        }
        return attackable.sortedWith(compareBy<RuntimeBattleUnitSnapshot>(::targetPriority).thenBy { it.hitPoints })
    }

    /** guidedMagicPlanFor: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun guidedMagicPlanFor(caster: RuntimeBattleUnitSnapshot): CampaignE2eGuidedMagicPlan? {
        if (BattleStatus.SILENCE in caster.statuses) return null
        return s57GuidedOffensiveMagicPlan(
            scenario, guidedAuthoredRoute, s57Route.holdFire, protectS57MineMaster,
            caster.characterId, caster.x, caster.y, caster.magicPoints,
            caster.magic.map { magic -> CampaignE2eMagicOption(magic.id, magic.target, magic.expendMp, magic.power, magic.category, magic.hitArea.allScreen, magic.hitArea.offsets) },
            routedVisibleEnemies.map { CampaignE2eMagicTarget(it.id, it.x, it.y) },
        )
    }

    /** strategicTarget: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    private fun strategicTarget(): Pair<Int, Int>? {
        val waypoint = s57GateTarget ?: ctx.authoredMechanicRoute.target(playerTiles)
        if (scenario == "S_01") {
            val byId = visibleEnemies.associateBy { it.id }
            return s01PreferredAttackTargets(visibleEnemies.map { S01EnemyTarget(it.id, it.characterId, it.hitPoints) }, visibleEnemies.size)
                .firstOrNull()?.let { byId[it.unitId] }?.tile() ?: waypoint
        }
        val candidates = s57Route.combatTargetIds.takeIf { it.isNotEmpty() }?.let { ids -> visibleEnemies.filter { it.characterId in ids } }
            ?: visibleEnemies
        val comparator = if (scenario == "S_57") compareBy<RuntimeBattleUnitSnapshot>(::distanceFromParty).thenBy(::targetPriority)
        else compareBy<RuntimeBattleUnitSnapshot>(::targetPriority).thenBy(::distanceFromParty)
        return candidates.minWithOrNull(comparator)?.tile() ?: waypoint
    }

    /** distanceFromParty: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    private fun distanceFromParty(enemy: RuntimeBattleUnitSnapshot): Int = playerTiles.minOfOrNull { (x, y) ->
        kotlin.math.abs(enemy.x - x) + kotlin.math.abs(enemy.y - y)
    } ?: Int.MAX_VALUE

    /** targetOrder: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    private fun targetOrder(): List<Int> = when (scenario) {
        "S_52" -> listOf(170, 171, 172, 173)
        "S_57" -> listOf(165, 162, 169, 166, 167, 168, 163, 164, 35)
        else -> emptyList()
    }

    private companion object { val FIRST_ROOM_LEADERS = setOf(165, 162, 169) }
}

package com.jojo.game
import com.jojo.game.domain.battle.*

internal class CampaignE2eBattlePlanningBoard(
    val ctx: BattleCampaignE2eAdapter.ProjectionContext,
) {
    val battle = ctx.battle
    val selected = ctx.selectedUnitId?.let(battle.units::get)
    val scenario = ctx.scenario
    val guidedAuthoredRoute = scenario == "S_52" || scenario == "S_57"
    val visibleEnemies = battle.units.values.filter { it.visible && it.hitPoints > 0 && it.type().isEnemySide() }
    val playerTiles = battle.units.values.asSequence()
        .filter { it.visible && it.type() == Faction.PLAYER }
        .map { it.tileX to it.tileY }
        .toList()
    val s57FirstRoomFocus = s57FirstRoomEscortFocus(visibleEnemies.mapNotNull { enemy ->
        enemy.characterId?.takeIf { it in FIRST_ROOM_LEADERS }?.let { characterId ->
            S57FirstRoomLeader(enemy.id, characterId, enemy.hitPoints, enemy.tileX to enemy.tileY)
        }
    })
    val s57Route = s57AuthoredRouteSignal(
        scenario,
        visibleEnemies.mapNotNull { it.characterId },
        battle.units.values.any { unit ->
            unit.visible && unit.type() == Faction.PLAYER && unit.characterId == 0 &&
                    unit.tileX in 2..16 && unit.tileY in 11..23
        },
        playerTiles.size,
    )
    val s57GateTarget = s57Route.gateTarget
    val waitForAuthoredAttrition = s57Route.waitForAttrition
    val protectS57MineMaster = scenario == "S_57" && s57FirstRoomFocus != null
    val s57FirstRoomFocusUnit = s57FirstRoomFocus?.unitId?.let(battle.units::get)
    val routedVisibleEnemies = visibleEnemies
    val occupiedTiles = battle.units.values.asSequence()
        .filter { it.visible && it.hitPoints > 0 }
        .map { it.tileX to it.tileY }
        .toSet()
    val strategicTarget = strategicTarget()

    fun targetPriority(unit: BattleUnit): Int = targetOrder().indexOf(unit.characterId).takeIf { it >= 0 } ?: 10_000

    fun canAttack(unit: BattleUnit, tile: Pair<Int, Int>, target: BattleUnit): Boolean =
        unit.attackAllScreen || (target.tileX - tile.first to target.tileY - tile.second) in unit.attackOffsets

    fun attackableFrom(unit: BattleUnit, tile: Pair<Int, Int>): List<BattleUnit> {
        val attackable = routedVisibleEnemies.filter { target -> canAttack(unit, tile, target) }
        if (scenario == "S_01") {
            val byId = attackable.associateBy { it.id }
            return s01PreferredAttackTargets(
                attackable.map { S01EnemyTarget(it.id, it.characterId, it.hitPoints) },
                visibleEnemies.size,
            ).mapNotNull { byId[it.unitId] }
        }
        return attackable.sortedWith(compareBy<BattleUnit>(::targetPriority).thenBy { it.hitPoints })
    }

    fun guidedMagicPlanFor(caster: BattleUnit): CampaignE2eGuidedMagicPlan? {
        if (BattleStatus.SILENCE in caster.statuses) return null
        return s57GuidedOffensiveMagicPlan(
            scenario = scenario,
            guidedAuthoredRoute = guidedAuthoredRoute,
            holdFire = s57Route.holdFire,
            firstRoomLeaderVisible = protectS57MineMaster,
            casterCharacterId = caster.characterId,
            casterX = caster.tileX,
            casterY = caster.tileY,
            magicPoints = caster.magicPoints,
            options = caster.magic.map { magic ->
                CampaignE2eMagicOption(
                    magic.id, magic.target, magic.expendMp, magic.power, magic.category,
                    magic.hitArea.allScreen, magic.hitArea.offsets,
                )
            },
            visibleEnemies = routedVisibleEnemies.map { CampaignE2eMagicTarget(it.id, it.tileX, it.tileY) },
        )
    }

    private fun strategicTarget(): Pair<Int, Int>? {
        val waypoint = s57GateTarget ?: ctx.authoredMechanicRoute.target(playerTiles)
        if (scenario == "S_01") {
            val byId = visibleEnemies.associateBy { it.id }
            return s01PreferredAttackTargets(
                visibleEnemies.map { S01EnemyTarget(it.id, it.characterId, it.hitPoints) }, visibleEnemies.size,
            ).firstOrNull()?.let { byId[it.unitId] }?.let { it.tileX to it.tileY } ?: waypoint
        }
        val strategicEnemies = s57Route.combatTargetIds.takeIf { it.isNotEmpty() }
            ?.let { ids -> visibleEnemies.filter { it.characterId in ids } } ?: visibleEnemies
        val comparator = if (scenario == "S_57") {
            compareBy<BattleUnit>(::distanceFromParty).thenBy(::targetPriority)
        } else {
            compareBy<BattleUnit>(::targetPriority).thenBy(::distanceFromParty)
        }
        return strategicEnemies.minWithOrNull(comparator)?.let { it.tileX to it.tileY } ?: waypoint
    }

    private fun distanceFromParty(enemy: BattleUnit): Int = playerTiles.minOfOrNull { (x, y) ->
        kotlin.math.abs(enemy.tileX - x) + kotlin.math.abs(enemy.tileY - y)
    } ?: Int.MAX_VALUE

    private fun targetOrder(): List<Int> = when (scenario) {
        "S_52" -> listOf(170, 171, 172, 173)
        "S_57" -> listOf(165, 162, 169, 166, 167, 168, 163, 164, 35)
        else -> emptyList()
    }

    private companion object {
        val FIRST_ROOM_LEADERS = setOf(165, 162, 169)
    }
}

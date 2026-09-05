package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleMovementPlanner

/** Builds movement-only dependencies, including the journal's route command. */
internal object BattleMovementEnvironmentAssembler {
    fun build(
        units: () -> Map<String, BattleUnit>,
        unitAt: (Int, Int) -> BattleUnit?,
        activeFaction: () -> Faction,
        weather: () -> BattleWeather,
        terrain: BattleTerrainGrid?,
        blockedTiles: MutableSet<Pair<Int, Int>>,
        movementPlanner: BattleMovementPlanner<BattleUnit>,
        allPresentationUnits: () -> List<BattleUnit>,
        isBattleEnded: () -> Boolean,
        recordMove: (String, List<Pair<Int, Int>>, Int) -> Unit,
    ): BattleMovementEnvironment = BattleMovementEnvironment(
        units = units,
        unitAt = unitAt,
        activeFaction = activeFaction,
        weather = weather,
        terrain = terrain,
        blockedTiles = blockedTiles,
        movementPlanner = movementPlanner,
        allPresentationUnits = allPresentationUnits,
        isBattleEnded = isBattleEnded,
        onMoveExecuted = recordMove,
    )
}

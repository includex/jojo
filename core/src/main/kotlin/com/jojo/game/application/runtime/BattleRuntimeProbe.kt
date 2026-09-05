package com.jojo.game.application.runtime

import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction

/** Immutable coordinate value used across the diagnostics boundary. */
data class RuntimeGridPoint(val x: Int, val y: Int)

data class RuntimeMagicSnapshot(
    val id: Int,
    val target: Int,
    val cost: Int,
    val power: Int,
    val category: Int,
    val allScreen: Boolean,
    val offsets: Set<RuntimeGridPoint>,
)

data class RuntimeBattleUnitSnapshot(
    val id: String,
    val faction: Faction,
    val effectiveFaction: Faction,
    val characterId: Int?,
    val x: Int,
    val y: Int,
    val hitPoints: Int,
    val magicPoints: Int,
    val level: Int,
    val attack: Int,
    val defense: Int,
    val visible: Boolean,
    val hasActed: Boolean,
    val statuses: Set<BattleStatus>,
    val attackOffsets: Set<RuntimeGridPoint>,
    val attackAllScreen: Boolean,
    val magic: List<RuntimeMagicSnapshot>,
    val retreatCount: Int,
    val hasAuthoredX: Boolean,
    val hasAuthoredY: Boolean,
)

data class BattleRuntimeSnapshot(
    val round: Int,
    val activeFaction: Faction,
    val units: List<RuntimeBattleUnitSnapshot>,
)

/**
 * A read-only live query facade paired with an immutable [snapshot].  The
 * queries have no mutation capability and retain tactical calculations inside
 * core while allowing diagnostics to choose their own projection policy.
 */
interface BattleRuntimeProbe {
    val snapshot: BattleRuntimeSnapshot

    fun reachableTiles(unitId: String): Set<RuntimeGridPoint>

    fun canEnterTilesIgnoringEnemyWithinMoves(
        unitId: String,
        ignoredEnemyId: String,
        start: RuntimeGridPoint,
        targetTiles: Set<RuntimeGridPoint>,
        moves: Int = 2,
    ): Boolean

    fun physicalDamagePreview(attackerId: String, targetId: String): Int

    fun screenPoint(tile: RuntimeGridPoint): RuntimeGridPoint

    fun projectWorldPoint(x: Float, y: Float): RuntimeGridPoint
}

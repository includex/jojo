package com.jojo.game.application.runtime

/**
 * Neutral adapter for the read-only battle runtime contract.  Screen code
 * supplies domain queries; diagnostics receive only immutable coordinates and
 * snapshots, with no dependency on a LibGDX screen or capture route.
 */
internal class BattleRuntimeProbeFactory(
    private val initialSnapshot: BattleRuntimeSnapshot,
    private val reachable: (String) -> Set<RuntimeGridPoint>,
    private val canEnter: (String, String, RuntimeGridPoint, Set<RuntimeGridPoint>, Int) -> Boolean,
    private val damagePreview: (String, String) -> Int,
    private val screenPointQuery: (RuntimeGridPoint) -> RuntimeGridPoint,
    private val projectWorldPointQuery: (Float, Float) -> RuntimeGridPoint,
) {
    fun create(): BattleRuntimeProbe = object : BattleRuntimeProbe {
        override val snapshot: BattleRuntimeSnapshot = initialSnapshot
        override fun reachableTiles(unitId: String): Set<RuntimeGridPoint> = reachable(unitId)
        override fun canEnterTilesIgnoringEnemyWithinMoves(
            unitId: String,
            ignoredEnemyId: String,
            start: RuntimeGridPoint,
            targetTiles: Set<RuntimeGridPoint>,
            moves: Int,
        ): Boolean = canEnter(unitId, ignoredEnemyId, start, targetTiles, moves)
        override fun physicalDamagePreview(attackerId: String, targetId: String): Int = damagePreview(attackerId, targetId)
        override fun screenPoint(tile: RuntimeGridPoint): RuntimeGridPoint = screenPointQuery(tile)
        override fun projectWorldPoint(x: Float, y: Float): RuntimeGridPoint = projectWorldPointQuery(x, y)
    }
}

// Runtime
package com.jojo.game.application.runtime

/** BattleRuntimeProbeFactory: 화면이 제공한 전장 조회 함수를 자동 구동기용 BattleRuntimeProbe로 조립한다. */
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

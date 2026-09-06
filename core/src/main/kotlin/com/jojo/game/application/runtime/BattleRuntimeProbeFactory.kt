// Runtime
package com.jojo.game.application.runtime

/** BattleRuntimeProbeFactory: 화면이 제공한 전장 조회 함수를 자동 구동기용 BattleRuntimeProbe로 조립한다. */
internal class BattleRuntimeProbeFactory(
    /**
     * `initialSnapshot` (BattleRuntimeSnapshot,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val initialSnapshot: BattleRuntimeSnapshot,
    /**
     * `reachable` ((String) -> Set<RuntimeGridPoint>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val reachable: (String) -> Set<RuntimeGridPoint>,
    /**
     * `canEnter` ((String, String, RuntimeGridPoint, Set<RuntimeGridPoint>, Int) -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val canEnter: (String, String, RuntimeGridPoint, Set<RuntimeGridPoint>, Int) -> Boolean,
    /**
     * `damagePreview` ((String, String) -> Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val damagePreview: (String, String) -> Int,
    /**
     * `screenPointQuery` ((RuntimeGridPoint) -> RuntimeGridPoint,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val screenPointQuery: (RuntimeGridPoint) -> RuntimeGridPoint,
    /**
     * `projectWorldPointQuery` ((Float, Float) -> RuntimeGridPoint,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val projectWorldPointQuery: (Float, Float) -> RuntimeGridPoint,
) {
    /**
     * `create`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun create(): BattleRuntimeProbe = object : BattleRuntimeProbe {
        /**
         * `snapshot` (BattleRuntimeSnapshot): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val snapshot: BattleRuntimeSnapshot = initialSnapshot
        /**
         * `reachableTiles`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun reachableTiles(unitId: String): Set<RuntimeGridPoint> = reachable(unitId)
        /**
         * `canEnterTilesIgnoringEnemyWithinMoves`: 조건과 입력 상태를 검증한다.
         * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        override fun canEnterTilesIgnoringEnemyWithinMoves(
            unitId: String,
            ignoredEnemyId: String,
            start: RuntimeGridPoint,
            targetTiles: Set<RuntimeGridPoint>,
            moves: Int,
        ): Boolean = canEnter(unitId, ignoredEnemyId, start, targetTiles, moves)
        /**
         * `physicalDamagePreview`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun physicalDamagePreview(attackerId: String, targetId: String): Int = damagePreview(attackerId, targetId)
        /**
         * `screenPoint`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun screenPoint(tile: RuntimeGridPoint): RuntimeGridPoint = screenPointQuery(tile)
        /**
         * `projectWorldPoint`: 필요한 객체나 결과를 생성한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun projectWorldPoint(x: Float, y: Float): RuntimeGridPoint = projectWorldPointQuery(x, y)
    }
}

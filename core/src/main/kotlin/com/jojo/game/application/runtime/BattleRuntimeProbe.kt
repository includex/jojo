// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction

/** RuntimeGridPoint: 런타임 탐침이 좌표 질의 결과를 주고받을 때 사용하는 격자 위치다. */
data class RuntimeGridPoint(val x: Int, val y: Int)

/** RuntimeMagicSnapshot: 한 유닛이 현재 사용할 수 있는 마법의 비용·범위·위력을 고정한 조회 모델이다. */
data class RuntimeMagicSnapshot(
    val id: Int,
    val target: Int,
    val cost: Int,
    val power: Int,
    val category: Int,
    val allScreen: Boolean,
    val offsets: Set<RuntimeGridPoint>,
)

/** RuntimeBattleUnitSnapshot: 자동 전투 구동기가 판단에 사용하는 유닛의 위치·상태·행동 가능 정보를 담는다. */
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

/** BattleRuntimeSnapshot: 현재 라운드와 진영, 전장 유닛 목록을 한 번에 조회하는 전투 상태 스냅샷이다. */
data class BattleRuntimeSnapshot(
    val round: Int,
    val activeFaction: Faction,
    val units: List<RuntimeBattleUnitSnapshot>,
)

/** BattleRuntimeProbe: 화면 구현을 노출하지 않고 자동 구동기에 전장 질의와 좌표 변환을 제공하는 계약이다. */
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

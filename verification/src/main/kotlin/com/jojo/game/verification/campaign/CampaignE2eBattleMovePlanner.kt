// Verification
package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot
import com.jojo.game.domain.battle.Faction

/** CampaignE2eMovePlan: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class CampaignE2eMovePlan(
    /** manualMove: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val manualMove: CampaignE2eMoveInput?,
    /** s57CriticalFinisherActive: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val s57CriticalFinisherActive: Boolean,
)

/** CampaignE2eBattleMovePlanner: 스냅샷과 질의 결과에서 합법 칸만 선택하며 Battle을 변경하지 않는다. */
internal class CampaignE2eBattleMovePlanner {
    /** plan: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun plan(board: CampaignE2eBattlePlanningBoard): CampaignE2eMovePlan {
        val selected = board.selected
        val candidates = sequenceOf(selected).filterNotNull() + board.units.asSequence().filter {
            it.visible && it.type() == Faction.PLAYER && !it.hasActed && productionManualUnitEligible(it.statuses)
        }
        val unit = candidates.distinctBy { it.id }.firstOrNull() ?: return CampaignE2eMovePlan(null, false)
        val current = unit.tile()
        val reachable = executableProductionMoveTiles(
            current,
            board.probe.reachableTiles(unit.id).map { it.tile() },
            board.occupiedTiles,
        )
        val focus = board.s57FirstRoomFocusUnit
        val s57Critical = focus != null && unit.characterId == 0 &&
            board.probe.physicalDamagePreview(unit.id, focus.id) >= focus.hitPoints
        val target = when {
            focus != null && board.protectS57MineMaster -> focus.tile()
            else -> board.strategicTarget
        }
        val destination = reachable
            .filter { tile -> target == null || board.canAttack(unit, tile, focus ?: board.visibleEnemies.firstOrNull() ?: unit) || tile == current }
            .minWithOrNull(compareBy<Pair<Int, Int>> { tile ->
                target?.let { kotlin.math.abs(tile.first - it.first) + kotlin.math.abs(tile.second - it.second) } ?: 0
            }.thenBy { it.first }.thenBy { it.second })
            ?: current
        val sourcePoint = board.probe.screenPoint(current.point())
        val destinationPoint = board.probe.screenPoint(destination.point())
        return CampaignE2eMovePlan(
            CampaignE2eMoveInput(sourcePoint.x, sourcePoint.y, destinationPoint.x, destinationPoint.y),
            s57Critical,
        )
    }
}

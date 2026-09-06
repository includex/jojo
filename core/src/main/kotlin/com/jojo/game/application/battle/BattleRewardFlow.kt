// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.settlement.ResolvedBattleReward

/** BattleRewardFlow: 전투 보상 표시 흐름으로, 금전·아이템·종료 보상을 순서대로 공개한다. */
class BattleRewardFlow(val reward: ResolvedBattleReward) {
    enum class Phase { MONEY, ITEMS, END, COMPLETE }

    var phase: Phase = when {
        reward.end -> Phase.END
        reward.money > 0 -> Phase.MONEY
        reward.itemIds.isNotEmpty() -> Phase.ITEMS
        else -> Phase.COMPLETE
    }
        private set
    var visibleItemCount: Int = if (phase == Phase.ITEMS) 1 else 0
        private set

    val complete: Boolean get() = phase == Phase.COMPLETE

    /** advance: 현재 보상 단계를 진행하고, 다음 보상 또는 완료 상태로 전환한다. */
    fun advance() {
        phase = when (phase) {
            Phase.MONEY -> if (reward.itemIds.isNotEmpty()) Phase.ITEMS else Phase.COMPLETE
            Phase.ITEMS -> if (visibleItemCount < reward.itemIds.size) {
                visibleItemCount++
                Phase.ITEMS
            } else Phase.COMPLETE
            Phase.END, Phase.COMPLETE -> Phase.COMPLETE
        }
        if (phase == Phase.ITEMS && visibleItemCount == 0) visibleItemCount = 1
    }
}

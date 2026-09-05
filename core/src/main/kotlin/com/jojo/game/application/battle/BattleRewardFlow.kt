package com.jojo.game.application.battle

import com.jojo.game.domain.battle.settlement.ResolvedBattleReward

/** Input-driven progression state for the source RewardLayer coroutine. */
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

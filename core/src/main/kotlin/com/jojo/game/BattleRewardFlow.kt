package com.jojo.game

/** Input-driven view state for the source RewardLayer coroutine. */
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
            Phase.ITEMS -> {
                if (visibleItemCount < reward.itemIds.size) {
                    visibleItemCount++
                    Phase.ITEMS
                } else Phase.COMPLETE
            }
            Phase.END -> Phase.COMPLETE
            Phase.COMPLETE -> Phase.COMPLETE
        }
        if (phase == Phase.ITEMS && visibleItemCount == 0) visibleItemCount = 1
    }
}

data class ResolvedBattleReward(
    val money: Int,
    val flag: Int,
    val itemIds: List<Int>,
    val end: Boolean,
)

/** Exact arithmetic from recovered BattleScreen.reward. */
object BattleRewardResolver {
    fun resolve(
        request: ScenarioRewardRequest,
        averageLevel: Int,
        round: Int,
        maxRound: Int,
        mineDeaths: Int,
        enemiesRemaining: Int,
        objectivesComplete: Boolean,
    ): ResolvedBattleReward {
        var flag = 0
        if (mineDeaths == 0) flag = flag or 1
        if (enemiesRemaining == 0) flag = flag or 2
        if (objectivesComplete) flag = flag or 4
        var rate = Integer.bitCount(flag) * 30
        rate += kotlin.math.floor(60.0 * (1.0 - round.toDouble() / maxRound.coerceAtLeast(1))).toInt()
        val halfBase = (100 * (averageLevel + 7)) / 2
        val money = maxOf(800, request.bonusMoney + halfBase + (halfBase * rate) / 100)
        return ResolvedBattleReward(money, flag, request.items.chunked(2).mapNotNull { it.firstOrNull() }.filter { it < 255 }, request.end)
    }
}

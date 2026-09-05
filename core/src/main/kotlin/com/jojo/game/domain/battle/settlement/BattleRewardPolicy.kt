package com.jojo.game.domain.battle.settlement

import com.jojo.game.domain.scenario.ScenarioRewardRequest

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
        val rate = Integer.bitCount(flag) * 30 +
            kotlin.math.floor(60.0 * (1.0 - round.toDouble() / maxRound.coerceAtLeast(1))).toInt()
        val halfBase = 100 * (averageLevel + 7) / 2
        return ResolvedBattleReward(
            money = maxOf(800, request.bonusMoney + halfBase + halfBase * rate / 100),
            flag = flag,
            itemIds = request.items.chunked(2).mapNotNull { it.firstOrNull() }.filter { it < 255 },
            end = request.end,
        )
    }
}

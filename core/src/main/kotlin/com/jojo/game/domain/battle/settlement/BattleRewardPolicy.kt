// Battle
package com.jojo.game.domain.battle.settlement

import com.jojo.game.domain.scenario.ScenarioRewardRequest

/**
 * `ResolvedBattleReward` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ResolvedBattleReward(
    val money: Int,
    val flag: Int,
    val itemIds: List<Int>,
    val end: Boolean,
)

/** BattleRewardResolver: 전투 보상 판별기이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
object BattleRewardResolver {
    /**
     * `resolve`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun resolve(
        request: ScenarioRewardRequest,
        averageLevel: Int,
        round: Int,
        maxRound: Int,
        mineDeaths: Int,
        enemiesRemaining: Int,
        objectivesComplete: Boolean,
    ): ResolvedBattleReward {
        /**
         * `flag` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var flag = 0
        if (mineDeaths == 0) flag = flag or 1
        if (enemiesRemaining == 0) flag = flag or 2
        if (objectivesComplete) flag = flag or 4
        /**
         * `rate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val rate = Integer.bitCount(flag) * 30 +
            kotlin.math.floor(60.0 * (1.0 - round.toDouble() / maxRound.coerceAtLeast(1))).toInt()
        /**
         * `halfBase` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val halfBase = 100 * (averageLevel + 7) / 2
        return ResolvedBattleReward(
            money = maxOf(800, request.bonusMoney + halfBase + halfBase * rate / 100),
            flag = flag,
            itemIds = request.items.chunked(2).mapNotNull { it.firstOrNull() }.filter { it < 255 },
            end = request.end,
        )
    }
}

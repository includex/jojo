package com.jojo.game

/** Input-driven view state for the source RewardLayer coroutine. */
class BattleRewardFlow(val reward: ResolvedBattleReward) {
    /**
     * enum class  `Phase`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * 공개 메서드 `advance`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

/**
 * data class  `ResolvedBattleReward`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
        return ResolvedBattleReward(
            money,
            flag,
            request.items.chunked(2).mapNotNull { it.firstOrNull() }.filter { it < 255 },
            request.end
        )
    }
}

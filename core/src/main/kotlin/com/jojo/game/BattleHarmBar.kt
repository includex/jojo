package com.jojo.game

/** Pure target-preview HP/MP bar calculation. */
object BattleHarmBar {
    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(
        val bar0: Float? = null,
        val bar1: Float? = null,
        val bar2: Float? = null,
        val amountText: String? = null,
        val hitRateText: String? = null,
    )

    /**
     * HP_ADD is read first, then MP_ADD intentionally overwrites it exactly
     * as the source object-property checks do. Bars use the pre-effect value.
     */
    fun show(
        hp: Int,
        maxHp: Int,
        mp: Int,
        maxMp: Int,
        hpAdd: Int? = null,
        mpAdd: Int? = null,
        hitRate: Number? = null,
    ): View {
        var current = 0
        var maximum = 1
        var value: Int? = null
        hpAdd?.let { current = hp; maximum = maxHp; value = it }
        mpAdd?.let { current = mp; maximum = maxMp; value = it }
        val rateText = hitRate?.toInt()?.let { "$it%" }
        val change = value ?: return View(hitRateText = rateText)
        val bounded = if (change >= 0) minOf(maximum - current, change) else maxOf(-current, change)
        val oldProgress = current.toFloat() / maximum.coerceAtLeast(1)
        val newProgress = (current + bounded).toFloat() / maximum.coerceAtLeast(1)
        return if (bounded >= 0) {
            View(
                bar1 = newProgress,
                bar2 = oldProgress,
                amountText = kotlin.math.abs(bounded).toString(),
                hitRateText = rateText
            )
        } else {
            View(
                bar0 = oldProgress,
                bar2 = newProgress,
                amountText = kotlin.math.abs(bounded).toString(),
                hitRateText = rateText
            )
        }
    }
}

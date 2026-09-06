// Battle
package com.jojo.game.presentation.battle.unit

import com.jojo.game.*
/**
 * `BattleHarmBar`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object BattleHarmBar {

    /** View: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
    data class View(
        /**
         * `bar0` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val bar0: Float? = null,
        /**
         * `bar1` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val bar1: Float? = null,
        /**
         * `bar2` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val bar2: Float? = null,
        /**
         * `amountText` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val amountText: String? = null,
        /**
         * `hitRateText` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitRateText: String? = null,
    )
    /**
     * `show`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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
        /**
         * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var current = 0
        /**
         * `maximum` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var maximum = 1
        /**
         * `value` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var value: Int? = null
        hpAdd?.let { current = hp; maximum = maxHp; value = it }
        mpAdd?.let { current = mp; maximum = maxMp; value = it }
        /**
         * `rateText` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val rateText = hitRate?.toInt()?.let { "$it%" }
        /**
         * `change` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val change = value ?: return View(hitRateText = rateText)
        /**
         * `bounded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val bounded = if (change >= 0) minOf(maximum - current, change) else maxOf(-current, change)
        /**
         * `oldProgress` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val oldProgress = current.toFloat() / maximum.coerceAtLeast(1)
        /**
         * `newProgress` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.settlement.SettlementInfoKind
import com.jojo.game.domain.battle.settlement.SettlementInfoTiming
import com.jojo.game.presentation.shared.InfoBaseValueAnimation

/** All values from one settlement share their start time; each retains its own speed. */
internal object SettlementValueAnimation {
    data class Frame(val numbers: Map<Int, Int>, val ratios: Map<Int, Float>)

    fun frame(values: List<InfoBaseValueAnimation.Value>, elapsed: Float): Frame {
        val numbers = values.associate { it.index to it.source }.toMutableMap()
        fun InfoBaseValueAnimation.Value.progress(amount: Int) =
            (amount.coerceAtLeast(0).toFloat() / max.coerceAtLeast(1)).coerceIn(0f, 1f)
        val ratios = values.associate { it.index to it.progress(it.source) }.toMutableMap()
        if (elapsed <= 0f) return Frame(numbers, ratios)
        values.forEach { value ->
            val steps = InfoBaseValueAnimation.steps(value)
            if (steps.isEmpty()) return@forEach
            val kind = if (value.index == 0) SettlementInfoKind.HP else SettlementInfoKind.MP
            val step = (elapsed / SettlementInfoTiming.tickSeconds(kind)).toInt()
            if (step >= 1) numbers[value.index] = steps[(step - 1).coerceAtMost(steps.lastIndex)]
            val progress = (elapsed / SettlementInfoTiming.barTweenSeconds(kind)).coerceIn(0f, 1f)
            val from = value.progress(value.source)
            ratios[value.index] = from + (value.progress(value.destination) - from) * progress
        }
        return Frame(numbers, ratios)
    }
}

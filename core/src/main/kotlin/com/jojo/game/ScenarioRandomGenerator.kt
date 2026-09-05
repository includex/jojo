package com.jojo.game

import java.util.ArrayDeque
import kotlin.random.Random

internal class ScenarioRandomGenerator(
    initialSeed: Double = Random.Default.nextDouble() * 1_000.0,
) {
    var toolRandomSeed: Double = initialSeed
        private set
    private val injectedRandomValues = ArrayDeque<Int>()
    var randomDrawCount: Int = 0
        private set
    val remainingInjectedRandomCount: Int get() = injectedRandomValues.size
    var stopAfterRandomTraceCount: Int? = null
        private set

    fun reset() {
        randomDrawCount = 0
        stopAfterRandomTraceCount = null
    }

    fun setRandomSequence(values: Iterable<Int>) {
        injectedRandomValues.clear()
        values.forEach { value ->
            require(value in 0..100) { "Model.random() value must be in 0..100: $value" }
            injectedRandomValues.addLast(value)
        }
        randomDrawCount = 0
    }

    fun stopAfterRandomTrace(count: Int) {
        require(count > 0) { "random trace count must be positive" }
        stopAfterRandomTraceCount = count
    }

    fun nextModelRandom(): Int {
        randomDrawCount++
        if (injectedRandomValues.isNotEmpty()) return injectedRandomValues.removeFirst()
        val (nextSeed, value) = toolRandomFromSeed(toolRandomSeed)
        toolRandomSeed = nextSeed
        return value
    }

    companion object {
        internal fun toolRandomFromSeed(seed: Double): Pair<Double, Int> {
            val nextSeed = (9301.0 * seed + 49297.0) % 233280.0
            return nextSeed to ((nextSeed / 233280.0 * 201.0).toInt() % 101)
        }
    }
}

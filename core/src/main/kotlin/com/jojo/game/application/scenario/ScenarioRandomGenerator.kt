// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.ScenarioRandomSequence
import java.util.*
import kotlin.random.Random

/** 시나리오 난수와 검증용 주입 시퀀스를 관리한다. */
internal class ScenarioRandomGenerator(
    initialSeed: Double = Random.nextDouble() * 1_000.0,
) {
    var toolRandomSeed: Double = initialSeed
        private set
    private val injectedRandomValues = ArrayDeque<Int>()
    var randomDrawCount: Int = 0
        private set
    val remainingInjectedRandomCount: Int get() = injectedRandomValues.size
    var stopAfterRandomTraceCount: Int? = null
        private set

    /** 난수 추적 횟수와 필요하면 중단 조건을 초기화한다. */
    fun reset(retainTraceStop: Boolean = false) {
        randomDrawCount = 0
        if (!retainTraceStop) stopAfterRandomTraceCount = null
    }

    /** 재현 가능한 난수 값을 순서대로 주입한다. */
    fun setRandomSequence(values: Iterable<Int>) {
        injectedRandomValues.clear()
        values.forEach { value ->
            require(value in 0..100) { "Model.random() value must be in 0..100: $value" }
            injectedRandomValues.addLast(value)
        }
        randomDrawCount = 0
    }

    /** 지정 횟수의 난수 추적 후 실행을 멈출 조건을 설정한다. */
    fun stopAfterRandomTrace(count: Int) {
        require(count > 0) { "random trace count must be positive" }
        stopAfterRandomTraceCount = count
    }

    /** 주입된 값 또는 현재 시드로 다음 모델 난수를 생성한다. */
    fun nextModelRandom(): Int {
        randomDrawCount++
        if (injectedRandomValues.isNotEmpty()) return injectedRandomValues.removeFirst()
        val (nextSeed, value) = ScenarioRandomSequence.nextFromSeed(toolRandomSeed)
        toolRandomSeed = nextSeed
        return value
    }
}

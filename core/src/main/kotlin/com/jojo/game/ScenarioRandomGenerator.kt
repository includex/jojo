package com.jojo.game

import com.jojo.game.domain.scenario.ScenarioRandomSequence
import java.util.*
import kotlin.random.Random

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

    /**
     * 공개 메서드 `reset`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun reset() {
        randomDrawCount = 0
        stopAfterRandomTraceCount = null
    }

    /**
     * 공개 메서드 `setRandomSequence`
     *
     * ### 파라미터
    - `values` (`Iterable<Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setRandomSequence(values: Iterable<Int>) {
        injectedRandomValues.clear()
        values.forEach { value ->
            require(value in 0..100) { "Model.random() value must be in 0..100: $value" }
            injectedRandomValues.addLast(value)
        }
        randomDrawCount = 0
    }

    /**
     * 공개 메서드 `stopAfterRandomTrace`
     *
     * ### 파라미터
    - `count` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun stopAfterRandomTrace(count: Int) {
        require(count > 0) { "random trace count must be positive" }
        stopAfterRandomTraceCount = count
    }

    /**
     * 공개 메서드 `nextModelRandom`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun nextModelRandom(): Int {
        randomDrawCount++
        if (injectedRandomValues.isNotEmpty()) return injectedRandomValues.removeFirst()
        val (nextSeed, value) = ScenarioRandomSequence.nextFromSeed(toolRandomSeed)
        toolRandomSeed = nextSeed
        return value
    }
}

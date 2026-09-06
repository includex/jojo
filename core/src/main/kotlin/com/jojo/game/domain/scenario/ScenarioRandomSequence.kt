// Scenario
package com.jojo.game.domain.scenario

/** ScenarioRandomSequence: 스크립트 난수 호출에 재현 가능한 값 순서를 주입하고 소비 내역을 추적한다. */
object ScenarioRandomSequence {
    fun nextFromSeed(seed: Double): Pair<Double, Int> {
        val nextSeed = (9301.0 * seed + 49297.0) % 233280.0
        return nextSeed to ((nextSeed / 233280.0 * 201.0).toInt() % 101)
    }
}

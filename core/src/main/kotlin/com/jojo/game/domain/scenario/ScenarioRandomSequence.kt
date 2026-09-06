// Scenario
package com.jojo.game.domain.scenario

/** ScenarioRandomSequence: 스크립트 난수 호출에 재현 가능한 값 순서를 주입하고 소비 내역을 추적한다. */
object ScenarioRandomSequence {
    /**
     * `nextFromSeed`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun nextFromSeed(seed: Double): Pair<Double, Int> {
        val nextSeed = (9301.0 * seed + 49297.0) % 233280.0
        return nextSeed to ((nextSeed / 233280.0 * 201.0).toInt() % 101)
    }
}

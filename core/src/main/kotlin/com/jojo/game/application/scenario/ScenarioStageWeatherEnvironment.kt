// Game
package com.jojo.game.application.scenario

import com.jojo.game.domain.battle.BattleWeather

/** ScenarioStageWeatherEnvironment: 시나리오가 지정한 전투 날씨와 1라운드 해석을 관리한다. 압축된 날씨 종류와 오프셋을 날씨 표로 변환해 전투 설정이 날씨 표를 직접 알지 않게 한다. */
class ScenarioStageWeatherEnvironment {
    /**
     * `type` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var type: Int = 6
        private set
    /**
     * `offset` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var offset: Int = 0
        private set

    /**
     * `configure`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun configure(type: Int, offset: Int) {
        this.type = type
        this.offset = offset
    }

    /**
     * `schedule`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun schedule(): List<BattleWeather> = when (type) {
        0 -> listOf(
            BattleWeather.CLEAR,
            BattleWeather.CLOUDY,
            BattleWeather.WINDY,
            BattleWeather.WINDY,
            BattleWeather.WINDY,
            BattleWeather.HEAVY_RAIN
        )

        1 -> listOf(
            BattleWeather.CLEAR,
            BattleWeather.CLEAR,
            BattleWeather.CLEAR,
            BattleWeather.CLOUDY,
            BattleWeather.WINDY,
            BattleWeather.HEAVY_RAIN
        )

        2 -> listOf(
            BattleWeather.CLEAR,
            BattleWeather.CLOUDY,
            BattleWeather.WINDY,
            BattleWeather.HEAVY_RAIN,
            BattleWeather.HEAVY_RAIN,
            BattleWeather.HEAVY_RAIN
        )

        3 -> listOf(
            BattleWeather.CLEAR,
            BattleWeather.CLOUDY,
            BattleWeather.CLOUDY,
            BattleWeather.CLOUDY,
            BattleWeather.WINDY,
            BattleWeather.HEAVY_RAIN
        )

        4 -> listOf(
            BattleWeather.CLEAR,
            BattleWeather.CLOUDY,
            BattleWeather.WINDY,
            BattleWeather.SNOW,
            BattleWeather.SNOW,
            BattleWeather.SNOW
        )

        5 -> listOf(BattleWeather.WINDY)
        7 -> listOf(BattleWeather.HEAVY_RAIN)
        8 -> listOf(BattleWeather.CLOUDY)
        else -> listOf(BattleWeather.CLEAR)
    }

    /**
     * `initialWeather`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun initialWeather(): BattleWeather {
        val sequence = schedule()
        return sequence[Math.floorMod(1 + offset, sequence.size)]
    }
}

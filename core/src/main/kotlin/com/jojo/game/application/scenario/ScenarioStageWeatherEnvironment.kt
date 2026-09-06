// Game
package com.jojo.game.application.scenario

import com.jojo.game.domain.battle.BattleWeather

/** ScenarioStageWeatherEnvironment: 시나리오가 지정한 전투 날씨와 1라운드 해석을 관리한다. 압축된 날씨 종류와 오프셋을 날씨 표로 변환해 전투 설정이 날씨 표를 직접 알지 않게 한다. */
class ScenarioStageWeatherEnvironment {
    var type: Int = 6
        private set
    var offset: Int = 0
        private set

    fun configure(type: Int, offset: Int) {
        this.type = type
        this.offset = offset
    }

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

    fun initialWeather(): BattleWeather {
        val sequence = schedule()
        return sequence[Math.floorMod(1 + offset, sequence.size)]
    }
}

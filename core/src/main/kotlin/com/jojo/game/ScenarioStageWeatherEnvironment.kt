package com.jojo.game

/**
 * Authored battle weather configuration and its round-one resolution.
 *
 * The scenario format stores a compact weather type plus an offset.  Keeping
 * that translation here prevents battle setup from having to know the weather
 * table while preserving the source's one-based initial-round lookup.
 */
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

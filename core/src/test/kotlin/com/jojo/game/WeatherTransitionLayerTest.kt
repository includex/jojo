// Test
package com.jojo.game

import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.presentation.battle.overlay.WeatherTransitionLayer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** WeatherTransitionLayerTest: 라운드 전환 날씨 안내의 3초 정지와 교차 페이드를 검증한다. */
class WeatherTransitionLayerTest {
    @Test
    fun `weather layer holds the round loop for the original three-second sequence`() {
        var removed = 0
        var completed = 0
        val layer = WeatherTransitionLayer({ removed++ }, { completed++ })
        layer.onCreate(BattleWeather.CLEAR, BattleWeather.CLOUDY, round = 2, maxRound = 20)

        // 테스트 근거: 원본 MenuLayer는 delayTime(1)+delayTime(2)를 지난 뒤에야 fn을 부른다.
        assertFalse(layer.elapsed(0.5f))
        assertEquals(0, completed)
        assertTrue(layer.elapsed(1f), "delayTime(1) 뒤 날씨 소리는 한 번만 난다")
        assertFalse(layer.elapsed(1.5f))
        layer.elapsed(2.99f)
        assertEquals(0, completed)
        layer.elapsed(3f)
        layer.elapsed(4f)
        assertEquals(1, removed)
        assertEquals(1, completed)
    }

    @Test
    fun `weather layer mirrors the source cross-fade and progress bar inputs`() {
        val layer = WeatherTransitionLayer({}, {})
        layer.onCreate(BattleWeather.WINDY, BattleWeather.HEAVY_RAIN, round = 4, maxRound = 20)

        val created = requireNotNull(layer.view)
        assertEquals(BattleWeather.WINDY, created.previous)
        assertEquals(BattleWeather.HEAVY_RAIN, created.current)
        assertEquals(0f, created.fade)
        assertEquals(4f / 20f, created.progress)

        // 테스트 근거: 원본 fadeOut(2)/fadeIn(2)는 2초에 교차를 끝내고 그대로 머문다.
        layer.elapsed(1f)
        assertEquals(0.5f, requireNotNull(layer.view).fade)
        layer.elapsed(2f)
        assertEquals(1f, requireNotNull(layer.view).fade)
        layer.elapsed(2.5f)
        assertEquals(1f, requireNotNull(layer.view).fade)
    }

    @Test
    fun `weather layer clamps the round to max_round like the source progress bar`() {
        val layer = WeatherTransitionLayer({}, {})
        layer.onCreate(BattleWeather.CLEAR, BattleWeather.SNOW, round = 21, maxRound = 20)

        val view = requireNotNull(layer.view)
        assertEquals(20, view.round)
        assertEquals(1f, view.progress)
    }
}

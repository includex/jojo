// Test
package com.jojo.game

import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.presentation.battle.overlay.WeatherTransitionLayer
import com.jojo.game.presentation.battle.overlay.WeatherTransitionLayout

import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** WeatherTransitionLayoutTest: 날씨 안내 패널의 배치·진행률·교차 페이드·시트 선택을 검증한다. */
class WeatherTransitionLayoutTest {

    @Test
    fun `panel geometry matches the source MenuLayer prefab nodes`() {
        // 테스트 근거: 프리팹 938f52a8-237a-48dd-a378-a4ff4ee82c37의 `bg`는 _contentSize 1280x212이고
        // 좌우 정렬 Widget으로 실제 화면 너비 1488.3721까지 늘어난다.
        assertEquals(1488.3721f, WeatherTransitionLayout.PANEL_WIDTH)
        assertEquals(212f, WeatherTransitionLayout.PANEL_HEIGHT)
        // `bg/bg0`과 `bg/progressBar`는 같은 304x44 상자이며 y=36에 놓인다.
        assertEquals(41f, WeatherTransitionLayout.NAME_BOX_X)
        assertEquals(425f, WeatherTransitionLayout.PROGRESS_BOX_X)
        assertEquals(304f, WeatherTransitionLayout.BOX_WIDTH)
        assertEquals(44f, WeatherTransitionLayout.BOX_HEIGHT)
        assertEquals(36f, WeatherTransitionLayout.BOX_Y)
        // `bg/box2`는 436x104, 그 안의 날씨 노드는 216x50에 scale 2를 먹은 432x100이다.
        assertEquals(830.232f, WeatherTransitionLayout.WEATHER_BOX_X)
        assertEquals(436f, WeatherTransitionLayout.WEATHER_BOX_WIDTH)
        assertEquals(104f, WeatherTransitionLayout.WEATHER_BOX_HEIGHT)
        assertEquals(832.232f, WeatherTransitionLayout.WEATHER_X)
        assertEquals(8f, WeatherTransitionLayout.WEATHER_Y)
        assertEquals(432f, WeatherTransitionLayout.WEATHER_WIDTH)
        assertEquals(100f, WeatherTransitionLayout.WEATHER_HEIGHT)
        // `Panel_cancel`은 _opacity 30의 검정 막이다.
        assertEquals(30f / 255f, WeatherTransitionLayout.SCRIM_ALPHA)
    }

    @Test
    fun `command buttons keep the source eighty-eight pixel pitch and skip button12`() {
        // 테스트 근거: `bg/contain`의 단추는 88 간격이고, button12는 프리팹에서 _active=false다.
        assertEquals(15.13372f, WeatherTransitionLayout.buttonX(0))
        assertEquals(15.13372f + 88f, WeatherTransitionLayout.buttonX(1))
        assertEquals(15.13372f + 11 * 88f, WeatherTransitionLayout.buttonX(11))
        assertEquals(12, WeatherTransitionLayout.VISIBLE_BUTTON_COUNT)
        assertEquals(1071.1337f, WeatherTransitionLayout.HELP_BUTTON_X)
    }

    @Test
    fun `plate labels keep the anchors the prefab gives them`() {
        // 테스트 근거: `bg0/label` 앵커 0.5(중심 193), `progressBar/label` 앵커 x=0(왼쪽 431.853),
        // `progressBar/label0` 앵커 x=1(오른쪽 723.131 고정). 오른쪽 기준이라 글자가 길어져도
        // 판을 넘지 않고 왼쪽으로 자란다.
        assertEquals(193f, WeatherTransitionLayout.NAME_LABEL_CENTER_X)
        assertEquals(431.853f, WeatherTransitionLayout.TURN_LABEL_LEFT_X)
        assertEquals(723.131f, WeatherTransitionLayout.ROUND_LABEL_RIGHT_X)
        // 두 라벨 모두 `bg/progressBar`(425..729) 안에 있다.
        assertTrue(WeatherTransitionLayout.TURN_LABEL_LEFT_X > WeatherTransitionLayout.PROGRESS_BOX_X)
        assertTrue(
            WeatherTransitionLayout.ROUND_LABEL_RIGHT_X <
                WeatherTransitionLayout.PROGRESS_BOX_X + WeatherTransitionLayout.BOX_WIDTH
        )
    }

    @Test
    fun `progress bar fills left to right by round over max_round`() {
        // 테스트 근거: 원본 cc.ProgressBar는 _N$totalLength=300, 앵커 x=0의 가로 정방향 막대다.
        val layer = WeatherTransitionLayer({}, {})
        layer.onCreate(BattleWeather.CLOUDY, BattleWeather.WINDY, round = 3, maxRound = 20)
        val view = requireNotNull(layer.view)

        assertEquals(3f / 20f, view.progress)
        assertEquals(45f, WeatherTransitionLayout.barWidth(view.progress))
        assertEquals(0f, WeatherTransitionLayout.barWidth(0f))
        assertEquals(300f, WeatherTransitionLayout.barWidth(1f))
    }

    @Test
    fun `cross fade alphas follow the two-second fadeOut and fadeIn pair`() {
        // 테스트 근거: MenuLayer.js:93~96은 이전 노드에 fadeOut(2), 새 노드에 fadeIn(2)를 건다.
        val layer = WeatherTransitionLayer({}, {})
        layer.onCreate(BattleWeather.WINDY, BattleWeather.HEAVY_RAIN, round = 4, maxRound = 20)

        fun alphas(seconds: Float): Pair<Float, Float> {
            layer.elapsed(seconds)
            val fade = requireNotNull(layer.view).fade
            return WeatherTransitionLayout.previousAlpha(fade) to WeatherTransitionLayout.currentAlpha(fade)
        }

        assertEquals(1f to 0f, alphas(0f))
        assertEquals(0.75f to 0.25f, alphas(0.5f))
        assertEquals(0.5f to 0.5f, alphas(1f))
        assertEquals(0f to 1f, alphas(2f))
        // 2초 뒤에도 새 날씨만 남는다(원본 액션은 끝난 상태로 유지된다).
        assertEquals(0f to 1f, alphas(2.5f))
    }

    @Test
    fun `weather values select the authored Weather sheets`() {
        // 테스트 근거: _create_weather는 QING=1, YIN=2, FENG=3, HAO_YU=4, XUE=5를 고르고
        // `Game/Weather/Weather_<n>-1`을 불러온다. 어떤 짝이 나오는지는 시나리오 자료가 정한다.
        assertEquals(2, WeatherTransitionLayout.sheet(BattleWeather.CLOUDY))
        assertEquals(3, WeatherTransitionLayout.sheet(BattleWeather.WINDY))
        assertEquals(4, WeatherTransitionLayout.sheet(BattleWeather.HEAVY_RAIN))
        assertEquals(1, WeatherTransitionLayout.sheet(BattleWeather.CLEAR))
        assertEquals(5, WeatherTransitionLayout.sheet(BattleWeather.SNOW))
    }

    @Test
    fun `progress plate frames slice to a flat middle band`() {
        // 테스트 근거: `Mark_64-1`/`Mark_65-1` SpriteFrame의 capInsets는 Cocos 순서로 [1,3,1,3]이다
        // (resources/import/55/55729fa6-....json, resources/import/48/489c4bf4-....json).
        // 위·아래 3줄이 고정 덮개이므로 9분할로 늘리면 가운데가 한 색으로 평평해진다.
        // 원본 캡처가 밝은 한 줄 뒤 평탄한 색을 보이는 이유이며, 통짜로 늘리면 긴 그러데이션이 된다.
        val root = Path.of("build/resources/main/maps/ui/battle-menu")
        listOf("title-bar.png", "progress-bar.png").forEach { name ->
            val file = root.resolve(name)
            if (!Files.exists(file)) return@forEach
            val image = ImageIO.read(file.toFile())
            assertEquals(9, image.height, "$name 은 9줄짜리 원본 프레임이다")
            val column = (0 until image.height).map { image.getRGB(image.width / 2, it) }
            val middle = column.subList(3, 6)
            assertEquals(1, middle.toSet().size, "$name 의 가운데 3줄(늘어나는 구간)은 한 색이어야 한다")
            assertTrue(column[2] != middle[0], "$name 의 위쪽 덮개는 가운데와 달라야 한다")
            assertTrue(column[6] != middle[0], "$name 의 아래쪽 덮개는 가운데와 달라야 한다")
        }
    }

    @Test
    fun `weather clip loops four frames at six frames per second`() {
        // 테스트 근거: _create_weather는 4장을 6fps Loop 클립으로 재생한다.
        assertEquals(0, WeatherTransitionLayout.frameAt(0f))
        assertEquals(1, WeatherTransitionLayout.frameAt(1f / 6f))
        assertEquals(3, WeatherTransitionLayout.frameAt(3f / 6f))
        assertEquals(0, WeatherTransitionLayout.frameAt(4f / 6f))
        assertEquals(2, WeatherTransitionLayout.frameAt(1f))
    }
}

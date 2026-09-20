// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.presentation.shared.overlay.MenuLayer

/**
 * WeatherTransitionLayout: `switch_weather` 모드 `MenuLayer` 패널의 배치와 자원 선택을 담는다.
 *
 * 원본 `BattleLayer._switchWeather`(recovered-js/modules/battle/BattleLayer.js:5485)는 평소의
 * 메뉴와 같은 프리팹 `Battle/scene/MenuLayer`를 띄우고, `switch_weather` 분기에서는 단추만
 * `interactable=false`로 바꾼 뒤 `bg/box2`에 날씨 노드 두 개를 만들어 교차 페이드시킨다
 * (recovered-js/modules/ui/MenuLayer.js:74~116, 221). 즉 패널 자체의 배치는 메뉴와 동일하다.
 *
 * 여기의 수치는 모두 원본 프리팹
 * `assets/resources/import/93/938f52a8-237a-48dd-a378-a4ff4ee82c37.c59e5.json`의 노드
 * `_contentSize`/`_trs`/`_anchorPoint`와, 같은 프리팹을 실제로 띄워 기록한
 * `BattleMenuRenderEvents`의 좌표에서 옮긴 값이다. 눈대중으로 고른 값은 없다.
 */
object WeatherTransitionLayout {

    /** `SCRIM_ALPHA`: 프리팹 `Canvas/Layer/Panel_cancel`의 `_opacity` 30(검정)을 옮긴 값이다. */
    const val SCRIM_ALPHA = 30f / 255f

    /** `SCREEN_WIDTH`: `Panel_cancel`은 Widget 45로 화면 전체를 덮는다. */
    const val SCREEN_WIDTH = 1488.3721f

    /** `SCREEN_HEIGHT`: 원본 설계 해상도의 세로 길이다. */
    const val SCREEN_HEIGHT = 800f

    /** `PANEL_WIDTH`: `bg` 노드(`_contentSize` 1280×212)가 좌우 정렬로 늘어난 실제 너비다. */
    const val PANEL_WIDTH = 1488.3721f

    /** `PANEL_HEIGHT`: `bg` 노드의 `_contentSize` 높이 212다. */
    const val PANEL_HEIGHT = 212f

    /** `NAME_BOX_X`: `bg/bg0`(304×44)의 왼쪽 모서리다. */
    const val NAME_BOX_X = 41f

    /** `BOX_Y`: `bg/bg0`과 `bg/progressBar`가 공유하는 아래쪽 모서리다. */
    const val BOX_Y = 36f

    /** `BOX_WIDTH`: `bg/bg0`·`bg/progressBar`의 `_contentSize` 너비 304다. */
    const val BOX_WIDTH = 304f

    /** `BOX_HEIGHT`: 같은 두 노드의 `_contentSize` 높이 44다. */
    const val BOX_HEIGHT = 44f

    /** `NAME_BAR_X`: `bg/bg0/Mark_64-1`(300×40)의 왼쪽 모서리다. */
    const val NAME_BAR_X = 43f

    /** `BAR_Y`: `Mark_64-1`/`bar`가 공유하는 아래쪽 모서리다. */
    const val BAR_Y = 38f

    /** `BAR_WIDTH`: `cc.ProgressBar._N$totalLength`와 같은 300이다. */
    const val BAR_WIDTH = 300f

    /** `BAR_HEIGHT`: `bg`/`bar` 노드의 `_contentSize` 높이 40이다. */
    const val BAR_HEIGHT = 40f

    /** `PROGRESS_BOX_X`: `bg/progressBar`의 왼쪽 모서리다. */
    const val PROGRESS_BOX_X = 425f

    /** `PROGRESS_BAR_X`: `bg/progressBar/bg`와 `bar`의 왼쪽 모서리다(앵커 x=0). */
    const val PROGRESS_BAR_X = 427f

    /** `WEATHER_BOX_X`: `bg/box2`(436×104)의 왼쪽 모서리다. */
    const val WEATHER_BOX_X = 830.232f

    /** `WEATHER_BOX_Y`: `bg/box2`의 아래쪽 모서리다. */
    const val WEATHER_BOX_Y = 6f

    /** `WEATHER_BOX_WIDTH`: `bg/box2`의 `_contentSize` 너비 436이다. */
    const val WEATHER_BOX_WIDTH = 436f

    /** `WEATHER_BOX_HEIGHT`: `bg/box2`의 `_contentSize` 높이 104다. */
    const val WEATHER_BOX_HEIGHT = 104f

    /** `WEATHER_X`: `_create_weather`가 만든 216×50 스프라이트를 scale 2로 둔 왼쪽 모서리다. */
    const val WEATHER_X = 832.232f

    /** `WEATHER_Y`: 같은 스프라이트의 아래쪽 모서리다. */
    const val WEATHER_Y = 8f

    /** `WEATHER_WIDTH`: 216 × scale 2다. */
    const val WEATHER_WIDTH = 432f

    /** `WEATHER_HEIGHT`: 50 × scale 2다. */
    const val WEATHER_HEIGHT = 100f

    /**
     * `NAME_LABEL_CENTER_X`: `bg/bg0/label`은 앵커 (0.5,0.5)에 `_trs` 0이라
     * `bg/bg0`(중심 193)의 한가운데에 놓인다. 글자 수가 달라져도 중앙이 유지된다.
     */
    const val NAME_LABEL_CENTER_X = 193f

    /**
     * `TURN_LABEL_LEFT_X`: `bg/progressBar/label`은 앵커 (0,0.5), `_trs` -145.147이라
     * `bg/progressBar`(중심 577)에서 왼쪽 끝이 431.853이다.
     */
    const val TURN_LABEL_LEFT_X = 431.853f

    /**
     * `ROUND_LABEL_RIGHT_X`: `bg/progressBar/label0`은 앵커 (1,0.5), `_trs` +146.131이라
     * 오른쪽 끝이 723.131에 고정된다. 왼쪽이 아니라 오른쪽이 기준점이다.
     */
    const val ROUND_LABEL_RIGHT_X = 723.131f

    /**
     * `LABEL_BASELINE_Y`: 세 라벨이 공유하는 글자 기준선이다. 세 `cc.Label` 노드는 모두
     * `bg0`/`progressBar`(아래 36, 높이 44)의 세로 중앙에 앵커 y=0.5로 놓이고
     * `_lineHeight`가 30이다.
     */
    const val LABEL_BASELINE_Y = 69f

    /**
     * `LABEL_COLOR`: 세 라벨의 색이다. 프리팹의 `bg/bg0/label`·`bg/progressBar/label`·
     * `label0` 어느 노드에도 `_color`가 없어 `cc.Label`의 기본값인 흰색이 그대로 쓰인다.
     * 렌더 증거 로그와 실제 그리기가 같은 값을 쓰도록 여기에 한 번만 적는다.
     */
    const val LABEL_COLOR = "#ffffffff"

    /** `BUTTON_Y`: `bg/contain/button*`(88×88)의 아래쪽 모서리다. */
    const val BUTTON_Y = 116.29f

    /** `BUTTON_SIZE`: 같은 단추의 `_contentSize` 88이다. */
    const val BUTTON_SIZE = 88f

    /** `TOOL_Y`: `Background/tool1`(48×48, scale 1.5)의 아래쪽 모서리다. */
    const val TOOL_Y = 124.572f

    /** `TOOL_SIZE`: 48 × scale 1.5다. */
    const val TOOL_SIZE = 72f

    /** `HELP_BUTTON_X`: `bg/contain/button13`의 왼쪽 모서리다. */
    const val HELP_BUTTON_X = 1071.1337f

    /** `HELP_X`: `button13/Background/edit`(72×72)의 왼쪽 모서리다. */
    const val HELP_X = 1079.1337f

    /** `HELP_Y`: 같은 도움말 아이콘의 아래쪽 모서리다. */
    const val HELP_Y = 124.29f

    /** `VISIBLE_BUTTON_COUNT`: `button12`는 프리팹에서 `_active=false`라 12개만 보인다. */
    const val VISIBLE_BUTTON_COUNT = 12

    /** `buttonX`: `bg/contain`의 가로 배치(88 간격)에서 index번째 단추의 왼쪽 모서리다. */
    fun buttonX(index: Int): Float = 15.13372f + index * BUTTON_SIZE

    /** `barWidth`: 원본 `cc.ProgressBar`가 가로·정방향으로 채우는 폭이다. */
    fun barWidth(progress: Float): Float = BAR_WIDTH * progress.coerceIn(0f, 1f)

    /** `previousAlpha`: 사라지는 쪽 노드의 `cc.fadeOut(2)` 불투명도다. */
    fun previousAlpha(fade: Float): Float = 1f - fade.coerceIn(0f, 1f)

    /** `currentAlpha`: 나타나는 쪽 노드의 `cc.fadeIn(2)` 불투명도다. */
    fun currentAlpha(fade: Float): Float = fade.coerceIn(0f, 1f)

    /** `menuWeather`: 포트의 날씨 값을 원본 `BattleConfg.WEATHER` 대응 값으로 옮긴다. */
    fun menuWeather(weather: BattleWeather): MenuLayer.Weather = when (weather) {
        BattleWeather.CLEAR -> MenuLayer.Weather.QING
        BattleWeather.CLOUDY -> MenuLayer.Weather.YIN
        BattleWeather.WINDY -> MenuLayer.Weather.FENG
        BattleWeather.HEAVY_RAIN -> MenuLayer.Weather.HAO_YU
        BattleWeather.SNOW -> MenuLayer.Weather.XUE
    }

    /** `sheet`: `_create_weather`가 고르는 `Game/Weather/Weather_<n>-1` 시트 번호다. */
    fun sheet(weather: BattleWeather): Int = MenuLayer.weatherSheet(menuWeather(weather))

    /** `frameAt`: 6fps·4장 반복 클립에서 경과 시간에 해당하는 장면 번호다. */
    fun frameAt(seconds: Float): Int = MenuLayer.weatherFrameAt(seconds)
}

// Battle
package com.jojo.game.presentation.battle.render
import com.jojo.game.presentation.shared.overlay.*

// `MenuLayer` 프리팹의 배치 상수는 이 한 곳에만 있다. 그리기 쪽 `drawBattleMenu`/
// `drawWeatherLayer`도 같은 상수를 읽으므로 증거와 화면이 갈라질 수 없다.
import com.jojo.game.presentation.battle.overlay.WeatherTransitionLayout as Layout
import com.jojo.game.presentation.shared.overlay.MenuLayer
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 전투 메뉴 렌더 증거: 메뉴 뷰를 원본 노드 경로·좌표·자원 식별자가 담긴 JSONL로 직렬화한다. */
object BattleMenuRenderEvents {

    /** JSONL 생성: 현재 전투명·턴·날씨·진행률을 메뉴의 고정 출력 순서로 기록한다. */
    fun jsonl(view: MenuLayer.View): String {
        val log = RenderEventLog()
        val phase = "battle-menu"
        val spriteBlend = listOf(770, 771)
        val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        /**
         * `draw`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", blend: Any = spriteBlend,
            color: String? = null
        ) =
            log.draw(
                phase, if (path == "Canvas/Layer/Panel_cancel") "HallLayer" else "MenuLayer",
                path, type, x, y, w, h, asset, blend = blend, text = text, color = color
            )


        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun label(path: String, x: Float, y: Float, w: Float, h: Float, text: String) =
            draw(
                path, "label", x, y, w, h, text = text, blend = labelBlend,
                // 그리기 쪽 `drawMenuBarLabels`가 쓰는 것과 같은 상수다.
                color = Layout.LABEL_COLOR
            )

        draw(
            "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f,
            Layout.SCREEN_WIDTH, Layout.SCREEN_HEIGHT, "default_sprite_splash"
        )
        draw("Canvas/Layer/bg", "sprite", 0f, 0f, Layout.PANEL_WIDTH, Layout.PANEL_HEIGHT, "bg1")
        draw("Canvas/Layer/bg/box1", "sliced-sprite", 0f, 0f, Layout.PANEL_WIDTH, Layout.PANEL_HEIGHT, "box1")
        draw(
            "Canvas/Layer/bg/bg0", "sliced-sprite", Layout.NAME_BOX_X, Layout.BOX_Y,
            Layout.BOX_WIDTH, Layout.BOX_HEIGHT, "box2"
        )
        draw(
            "Canvas/Layer/bg/bg0/Mark_64-1", "sprite", Layout.NAME_BAR_X, Layout.BAR_Y,
            Layout.BAR_WIDTH, Layout.BAR_HEIGHT, "Mark_64-1"
        )
        label("Canvas/Layer/bg/bg0/label", 123.96f, 39.1f, 138.08f, 37.8f, view.battleName)
        draw(
            "Canvas/Layer/bg/progressBar", "sliced-sprite", Layout.PROGRESS_BOX_X, Layout.BOX_Y,
            Layout.BOX_WIDTH, Layout.BOX_HEIGHT, "box2"
        )
        draw(
            "Canvas/Layer/bg/progressBar/bg", "sliced-sprite", Layout.PROGRESS_BAR_X, Layout.BAR_Y,
            Layout.BAR_WIDTH, Layout.BAR_HEIGHT, "Mark_64-1"
        )
        draw(
            "Canvas/Layer/bg/progressBar/bar", "sliced-sprite", Layout.PROGRESS_BAR_X, Layout.BAR_Y,
            Layout.barWidth(view.progress), Layout.BAR_HEIGHT, "Mark_65-1"
        )
        label("Canvas/Layer/bg/progressBar/label", Layout.TURN_LABEL_LEFT_X, 39.1f, 60.23f, 37.8f, "턴 수")
        label("Canvas/Layer/bg/progressBar/label0", 648.071f, 39.1f, 75.06f, 37.8f, "${view.round} / ${view.maxRound}")
        draw(
            "Canvas/Layer/bg/box2", "sliced-sprite", Layout.WEATHER_BOX_X, Layout.WEATHER_BOX_Y,
            Layout.WEATHER_BOX_WIDTH, Layout.WEATHER_BOX_HEIGHT, "box2"
        )
        draw(
            "Canvas/Layer/bg/box2/node${view.weather.ordinal}", "sprite", Layout.WEATHER_X, Layout.WEATHER_Y,
            Layout.WEATHER_WIDTH, Layout.WEATHER_HEIGHT, "<unnamed-frame>"
        )
        (0 until Layout.VISIBLE_BUTTON_COUNT).forEach { index ->
            val x = Layout.buttonX(index)
            val base = "Canvas/Layer/bg/contain/button$index/Background"
            draw(base, "sliced-sprite", x, Layout.BUTTON_Y, Layout.BUTTON_SIZE, Layout.BUTTON_SIZE, "box3")
            draw(
                "$base/tool1", "sprite", x + 8f, Layout.TOOL_Y,
                Layout.TOOL_SIZE, Layout.TOOL_SIZE, "tool${index + 1}"
            )
        }
        draw(
            "Canvas/Layer/bg/contain/button13/Background", "sliced-sprite",
            Layout.HELP_BUTTON_X, Layout.BUTTON_Y, Layout.BUTTON_SIZE, Layout.BUTTON_SIZE, "box3"
        )
        draw(
            "Canvas/Layer/bg/contain/button13/Background/edit", "sprite",
            Layout.HELP_X, Layout.HELP_Y, Layout.TOOL_SIZE, Layout.TOOL_SIZE, "help"
        )
        return log.jsonl()
    }
}

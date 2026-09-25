// Battle
package com.jojo.game.presentation.battle.render

import com.jojo.game.presentation.i18n.SystemMessage
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
            color: String? = SPRITE_WHITE
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
            Layout.SCREEN_WIDTH, Layout.SCREEN_HEIGHT, "default_sprite_splash",
            // 흐림막은 포트가 색을 실제로 아는 유일한 스프라이트다. `BattleScreen`이
            // `Color(0f, 0f, 0f, SCRIM_ALPHA)`로 채우므로 RGB는 검정이고,
            // 투명도는 별도 `opacity` 항목의 몫이라 여기 섞지 않는다.
            // 나머지 스프라이트는 색조 없이 텍스처 색을 그대로 그리므로 비워 둔다.
            color = SCRIM_BLACK
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
        label("Canvas/Layer/bg/progressBar/label", Layout.TURN_LABEL_LEFT_X, 39.1f, 60.23f, 37.8f, SystemMessage.S_7A2ACD7CB6)
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

    /**
     * 흐림막 색: 원본 `MenuLayer` 프리팹의 `Layer/Panel_cancel` `_color`는 4278190080 = (0,0,0)이며
     * 하네스도 이 노드에 `#000000`을 낸다. 투명도(`_opacity`)는 `WeatherTransitionLayout.SCRIM_ALPHA`가
     * 따로 들고 있고 비교 항목도 따로다.
     */
    private const val SCRIM_BLACK = "#000000"

    /**
     * 스프라이트 색조: 포트의 `BattleScreen.drawBattleMenu`는 첫 줄에서 `batch.color = Color.WHITE`를
     * 세우고 `batch.end()`까지 한 번도 바꾸지 않는다(중간에 부르는 `drawMenuBarLabels`도 `font.color`만
     * 만진다). 따라서 이 화면의 스프라이트가 흰색 색조로 그려진다는 것은 짐작이 아니라 포트가 실제로
     * 아는 사실이며, 원본 프리팹의 해당 노드들도 `_color`가 없어 엔진 기본 흰색이다. 값을 적어 두면
     * 누군가 색조를 넣었을 때 게이트가 떨어진다.
     */
    private const val SPRITE_WHITE = "#ffffff"
}

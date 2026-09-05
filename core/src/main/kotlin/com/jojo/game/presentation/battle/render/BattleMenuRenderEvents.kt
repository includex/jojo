package com.jojo.game.presentation.battle.render
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.MenuLayer
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** Draw submissions produced by the actual BattleScreen menu-button route. */
object BattleMenuRenderEvents {
    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `view` (`MenuLayer.View`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(view: MenuLayer.View): String {
        val log = RenderEventLog()
        val phase = "battle-menu"
        val spriteBlend = listOf(770, 771)
        val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", blend: Any = spriteBlend
        ) =
            log.draw(
                phase, if (path == "Canvas/Layer/Panel_cancel") "HallLayer" else "MenuLayer",
                path, type, x, y, w, h, asset, blend = blend, text = text
            )

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(path: String, x: Float, y: Float, w: Float, h: Float, text: String) =
            draw(path, "label", x, y, w, h, text = text, blend = labelBlend)

        draw("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash")
        draw("Canvas/Layer/bg", "sprite", 0f, 0f, 1488.372f, 212f, "bg1")
        draw("Canvas/Layer/bg/box1", "sliced-sprite", 0f, 0f, 1488.372f, 212f, "box1")
        draw("Canvas/Layer/bg/bg0", "sliced-sprite", 41f, 36f, 304f, 44f, "box2")
        draw("Canvas/Layer/bg/bg0/Mark_64-1", "sprite", 43f, 38f, 300f, 40f, "Mark_64-1")
        label("Canvas/Layer/bg/bg0/label", 123.96f, 39.1f, 138.08f, 37.8f, view.battleName)
        draw("Canvas/Layer/bg/progressBar", "sliced-sprite", 425f, 36f, 304f, 44f, "box2")
        draw("Canvas/Layer/bg/progressBar/bg", "sliced-sprite", 427f, 38f, 300f, 40f, "Mark_64-1")
        draw("Canvas/Layer/bg/progressBar/bar", "sliced-sprite", 427f, 38f, 300f * view.progress, 40f, "Mark_65-1")
        label("Canvas/Layer/bg/progressBar/label", 431.853f, 39.1f, 60.23f, 37.8f, "턴 수")
        label("Canvas/Layer/bg/progressBar/label0", 648.071f, 39.1f, 75.06f, 37.8f, "${view.round} / ${view.maxRound}")
        draw("Canvas/Layer/bg/box2", "sliced-sprite", 830.232f, 6f, 436f, 104f, "box2")
        draw("Canvas/Layer/bg/box2/node${view.weather.ordinal}", "sprite", 832.232f, 8f, 432f, 100f, "<unnamed-frame>")
        (0..11).forEach { index ->
            val x = 15.134f + index * 88f
            val base = "Canvas/Layer/bg/contain/button$index/Background"
            draw(base, "sliced-sprite", x, 116.29f, 88f, 88f, "box3")
            draw("$base/tool1", "sprite", x + 8f, 124.572f, 72f, 72f, "tool${index + 1}")
        }
        draw("Canvas/Layer/bg/contain/button13/Background", "sliced-sprite", 1071.134f, 116.29f, 88f, 88f, "box3")
        draw("Canvas/Layer/bg/contain/button13/Background/edit", "sprite", 1079.134f, 124.29f, 72f, 72f, "help")
        return log.jsonl()
    }
}

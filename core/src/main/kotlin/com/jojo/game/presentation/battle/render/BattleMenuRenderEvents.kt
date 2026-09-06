// Battle
package com.jojo.game.presentation.battle.render
import com.jojo.game.presentation.shared.overlay.*

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
            asset: String? = null, text: String = "", blend: Any = spriteBlend
        ) =
            log.draw(
                phase, if (path == "Canvas/Layer/Panel_cancel") "HallLayer" else "MenuLayer",
                path, type, x, y, w, h, asset, blend = blend, text = text
            )


        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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

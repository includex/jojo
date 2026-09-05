package com.jojo.game

/** Draw submissions produced by the actual BattleScreen menu-button route. */
object BattleMenuRenderEvents {
    fun jsonl(view: MenuLayer.View): String {
        val log = RenderEventLog()
        val phase = "battle-menu"
        val spriteBlend = listOf(770, 771)
        val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                 asset: String? = null, text: String = "", blend: Any = spriteBlend) =
            log.draw(phase, if (path == "Canvas/Layer/Panel_cancel") "HallLayer" else "MenuLayer",
                path, type, x, y, w, h, asset, blend = blend, text = text)
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

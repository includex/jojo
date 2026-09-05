package com.jojo.game

/** Visible submissions shared by the real Lose draw and its JSONL route. */
object LoseSceneRenderEvents {
    const val PHASE = "battle-lose-restart-prompt"

    fun append(log: RenderEventLog, flow: LoseSceneFlow, phase: String = PHASE) {
        val sprites = listOf(770, 771)
        val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun draw(layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                 asset: String? = null, text: String = "") =
            log.draw(phase, layer, path, type, x, y, w, h, asset, 1f,
                if (type == "label") labels else sprites, true, text)

        draw(
            "HallLayer", "Canvas/Logo_8-1", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/resources/native/21/21fe73fb-bef8-411e-9656-591057b26aae.30628.jpg#Logo_8-1",
        )
        if (flow.state != LoseSceneFlow.State.PROMPT) return
        draw("Lose", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
        draw("Lose", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
        draw("Lose", "Canvas/Layer/bg0/Logo_3-1", "sprite", 453.005f, 373.951f, 106f, 124f, "Logo_3-1")
        draw("Lose", "Canvas/Layer/bg0/label", "label", 573.686f, 335f, 463f, 190f, text = LoseSceneFlow.PROMPT_TEXT)
        listOf(
            Triple(1, 554.186f, "비"),
            Triple(0, 754.186f, "예"),
        ).forEach { (tag, x, text) ->
            draw("Lose", "Canvas/Layer/bg0/btns/button$tag/Background", "sliced-sprite", x, 271.285f, 180f, 50f, "box3")
            val labelX = if (tag == 1) 557.336f else 757.586f
            val labelWidth = if (tag == 1) 168.1f else 169.4f
            draw("Lose", "Canvas/Layer/bg0/btns/button$tag/Background/Label", "label", labelX, 279.085f, labelWidth, 40f, text = text)
        }
    }
}

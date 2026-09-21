// Game
package com.jojo.game.presentation.battle.outcome
import com.badlogic.gdx.graphics.Color
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** LoseSceneRenderEvents: 패배 화면과 기록 경로가 함께 사용하는 표시 요청이다. */
object LoseSceneRenderEvents {
    /**
     * `PHASE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PHASE = "battle-lose-restart-prompt"
    const val NO_LABEL = "비"
    const val YES_LABEL = "예"
    const val WHITE_HEX = "#ffffff"
    const val PROMPT_HEX = "#0004c4"
    const val NO_HEX = "#ff0000"
    const val YES_HEX = "#0a6900"
    val promptColor: Color = Color.valueOf("0004c4ff")
    val noColor: Color = Color.RED
    val yesColor: Color = Color.valueOf("0a6900ff")


    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, flow: LoseSceneFlow, phase: String = PHASE) {
        val sprites = listOf(770, 771)
        val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        /**
         * `draw`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun draw(
            layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = ""
        ) =
            log.draw(
                phase, layer, path, type, x, y, w, h, asset, 1f,
                if (type == "label") labels else sprites, true, text,
                when (path) {
                    "Canvas/Layer/bg0/label" -> PROMPT_HEX
                    "Canvas/Layer/bg0/btns/button1/Background/Label" -> NO_HEX
                    "Canvas/Layer/bg0/btns/button0/Background/Label" -> YES_HEX
                    else -> WHITE_HEX
                },
            )

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
            Triple(1, 554.186f, NO_LABEL),
            Triple(0, 754.186f, YES_LABEL),
        ).forEach { (tag, x, text) ->
            draw("Lose", "Canvas/Layer/bg0/btns/button$tag/Background", "sliced-sprite", x, 271.285f, 180f, 50f, "box3")
            val labelX = if (tag == 1) 557.336f else 757.586f
            val labelWidth = if (tag == 1) 168.1f else 169.4f
            draw(
                "Lose",
                "Canvas/Layer/bg0/btns/button$tag/Background/Label",
                "label",
                labelX,
                279.085f,
                labelWidth,
                40f,
                text = text
            )
        }
    }
}

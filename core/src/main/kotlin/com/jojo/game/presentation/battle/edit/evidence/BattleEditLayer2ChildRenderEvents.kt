// Battle
    package com.jojo.game.presentation.battle.edit.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog
    /**
     * `BattleEditLayer2ChildRenderEvents`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal object BattleEditLayer2ChildRenderEvents {
        /**
         * `alphaBlend` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, phase: String, layer: String = "EditLayer3") {

        /**
         * `d`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun d(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            opacity: Float = 1f,
            blend: Any = listOf(770, 771)
        ) =
            log.draw(phase, layer, path, type, x, y, w, h, asset, opacity, blend, true, text)
        d("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .314f)
        d("Canvas/Layer/bg", "tiled-sprite", 444.186f, 195f, 600f, 410f, "Logo_9-1")
        d("Canvas/Layer/bg/bg1", "sprite", 444.186f, 555f, 600f, 50f, "bg1")
        d("Canvas/Layer/bg/bg1/label", "label", 629.271f, 554.8f, 229.83f, 50.4f, text = "전역 변수 편집", blend = alphaBlend)
        d("Canvas/Layer/bg/label", "label", 625.117f, 396.8f, 80.31f, 50.4f, text = "야심:", blend = alphaBlend)
        d("Canvas/Layer/bg/editbox0/BACKGROUND_SPRITE", "sliced-sprite", 715.31f, 397f, 225.2f, 50f, "box1")
        d("Canvas/Layer/bg/editbox0/TEXT_LABEL", "label", 717.31f, 397f, 223.2f, 50f, text = "50", blend = alphaBlend)
        d("Canvas/Layer/bg/label", "label", 625.117f, 314.8f, 80.31f, 50.4f, text = "금전:", blend = alphaBlend)
        d("Canvas/Layer/bg/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 715.31f, 315f, 225.2f, 50f, "box1")
        d("Canvas/Layer/bg/editbox1/TEXT_LABEL", "label", 717.31f, 315f, 223.2f, 50f, text = "0", blend = alphaBlend)
        d("Canvas/Layer/bg/label", "label", 544.957f, 477.8f, 160.63f, 50.4f, text = "장면 이동:", blend = alphaBlend)
        d("Canvas/Layer/bg/bg3", "sliced-sprite", 714.91f, 479f, 250f, 50f, "box1")
        d("Canvas/Layer/bg/bg3/label", "label", 718.51f, 478.8f, 243.4f, 50.4f, text = "영천의 전투R", blend = alphaBlend)
        val buttons = listOf(
            floatArrayOf(876.797f, 212.983f, 150.4f, 58.5f, 901.997f, 222.233f, 100f, 40f) to "수정",
            floatArrayOf(719.152f, 212.983f, 150.4f, 58.5f, 744.352f, 222.233f, 100f, 40f) to "폐쇄",
            floatArrayOf(487.035f, 212.95f, 221.5f, 58.5f, 505.73f, 217f, 184.11f, 50.4f) to "창고 비우기"
        )
        buttons.forEachIndexed { i, (r, t) ->
            d(
                "Canvas/Layer/bg/button$i/Background",
                "sliced-sprite",
                r[0],
                r[1],
                r[2],
                r[3],
                "box3"
            ); d(
            "Canvas/Layer/bg/button$i/Background/Label",
            "label",
            r[4],
            r[5],
            r[6],
            r[7],
            text = t,
            blend = alphaBlend
        )
        }
    }
    }

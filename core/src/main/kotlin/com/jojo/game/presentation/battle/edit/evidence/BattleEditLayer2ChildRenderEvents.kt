    package com.jojo.game.presentation.battle.edit.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog

    internal object BattleEditLayer2ChildRenderEvents {
        private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    fun append(log: RenderEventLog, phase: String, layer: String = "EditLayer3") {
        /**
         * 공개 메서드 `d`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String?=null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String=""`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `opacity` (`Float=1f`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `blend` (`Any=listOf(770,771`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
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

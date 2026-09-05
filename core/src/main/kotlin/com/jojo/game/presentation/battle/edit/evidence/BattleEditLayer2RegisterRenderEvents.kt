    package com.jojo.game.presentation.battle.edit.evidence

    import com.jojo.game.RenderEventLog

    internal object BattleEditLayer2RegisterRenderEvents {
        private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    fun append(log: RenderEventLog, phase: String) {
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
            blend: Any = listOf(770, 771)
        ) =
            log.draw(phase, "RegisterLayer", path, type, x, y, w, h, asset, blend = blend, text = text)
        d("Canvas/Layer/bg0", "tiled-sprite", 344.186f, 163.5f, 800f, 473f, "Logo_12-1")
        d("Canvas/Layer/bg0/bg1", "sprite", 344.186f, 586.5f, 800f, 50f, "bg1")
        d(
            "Canvas/Layer/bg0/bg1/label",
            "label",
            624.186f,
            586.3f,
            264.43f,
            50.4f,
            text = "등록 코드 생성기",
            blend = alphaBlend
        )
        d("Canvas/Layer/bg0/box3", "sliced-sprite", 344.186f, 163.5f, 800f, 473f, "box1")
        d("Canvas/Layer/bg0/box1", "sliced-sprite", 355.686f, 520f, 773f, 54f, "box1")
        d(
            "Canvas/Layer/bg0/box1/editbox/PLACEHOLDER_LABEL",
            "label",
            369.186f,
            522f,
            748f,
            50f,
            text = "활성화 코드를 입력하세요",
            blend = alphaBlend
        )
        d("Canvas/Layer/bg0/label", "label", 360.186f, 239f, 768f, 118f, text = "Label", blend = alphaBlend)
        d("Canvas/Layer/bg0/button0/Background", "sliced-sprite", 916.163f, 180.272f, 200f, 50f, "box3")
        d(
            "Canvas/Layer/bg0/button0/Background/Label",
            "label",
            939.408f,
            181.071f,
            153.51f,
            54.4f,
            text = "생성 공유",
            blend = alphaBlend
        )
        d("Canvas/Layer/bg0/button1/Background", "sliced-sprite", 698.334f, 180.272f, 200f, 50f, "box3")
        d(
            "Canvas/Layer/bg0/button1/Background/Label",
            "label",
            748.334f,
            188.271f,
            100f,
            40f,
            text = "취소",
            blend = alphaBlend
        )
        d("Canvas/Layer/bg0/label0", "label", 360.186f, 393.717f, 768f, 118f, text = "Label", blend = alphaBlend)
    }
    }

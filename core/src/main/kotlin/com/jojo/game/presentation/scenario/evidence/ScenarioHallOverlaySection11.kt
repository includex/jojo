package com.jojo.game.presentation.scenario.evidence

internal fun appendMagic(writer: ScenarioHallOverlayEventWriter) = with(writer) {
        val magic = requireNotNull(input.magic)

        /**
         * 공개 메서드 `sprite`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `opacity` (`Float = 1f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun sprite(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String,
            opacity: Float = 1f
        ) =
            event("MagicLayer", path, type, x, y, w, h, asset, "", opacity, true)

        /**
         * 공개 메서드 `text`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float = 50.4f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun text(path: String, value: String, x: Float, y: Float, w: Float, h: Float = 50.4f) =
            label("MagicLayer", path, value, x, y, w, h, true)

        event(
            "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "default_sprite_splash", "", .392f, true
        )
        sprite("Canvas/Layer/bg1", "tiled-sprite", 452.686f, 130f, 583f, 540f, "Logo_9-1")
        sprite("Canvas/Layer/bg1/box2", "sliced-sprite", 452.686f, 130f, 583f, 540f, "box3")
        text("Canvas/Layer/bg1/label", magic.name, 577.509f, 604.008f, 103.8f)
        sprite("Canvas/Layer/bg1/skill_0", "sprite", 478.186f, 562f, 80f, 80f, "${magic.icon + 1}-1")
        sprite("Canvas/Layer/bg1/bg0", "sliced-sprite", 465.636f, 434f, 340.3f, 100f, "box1")
        text("Canvas/Layer/bg1/bg0/label", "위력:", 476.336f, 479.826f, 80.31f)
        text("Canvas/Layer/bg1/bg0/label0", "${magic.power ?: 0}%", 566.719f, 480.13f, 80.06f)
        text("Canvas/Layer/bg1/bg0/label", "MP 소모:", 470.776f, 436.826f, 151.43f)
        text("Canvas/Layer/bg1/bg0/label1", magic.cost.toString(), 627.053f, 436.675f, 22.25f)
        sprite("Canvas/Layer/bg1/bg1", "sliced-sprite", 465.636f, 147f, 340.3f, 274f, "box2")
        text("Canvas/Layer/bg1/bg1/scrollview/view/content/label", magic.intro, 470.786f, 144.76f, 330f, 275.44f)
        sprite("Canvas/Layer/bg1/bg2", "sliced-sprite", 814.213f, 436.061f, 200f, 200f, "box1")
        sprite("Canvas/Layer/bg1/bg2/bg", "sliced-sprite", 830.713f, 614.117f, 167f, 40f, "bg1")
        text("Canvas/Layer/bg1/bg2/bg/label", "가능 범위", 839.654f, 611.005f, 149.51f)
        sprite("Canvas/Layer/bg1/bg2/img", "sprite", 834.213f, 450.755f, 160f, 160f, "${magic.hit + 1}-1")
        sprite("Canvas/Layer/bg1/bg3", "sliced-sprite", 814.213f, 204.673f, 200f, 200f, "box1")
        sprite("Canvas/Layer/bg1/bg3/bg", "sliced-sprite", 831.713f, 384.673f, 165f, 40f, "bg1")
        text("Canvas/Layer/bg1/bg3/bg/label", "영향 범위", 839.654f, 381.561f, 149.51f)
        sprite("Canvas/Layer/bg1/bg3/img", "sprite", 834.213f, 219.367f, 160f, 160f, "${magic.eff + 1}-1")
        sprite("Canvas/Layer/bg1/button/Background", "sliced-sprite", 874.764f, 144.022f, 147.6f, 50f, "box3")
        text("Canvas/Layer/bg1/button/Background/Label", "확인", 898.564f, 152.022f, 100f, 40f)
}

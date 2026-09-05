    package com.jojo.game.presentation.battle.edit.evidence

import com.jojo.game.RenderEventLog
import com.jojo.game.presentation.battle.edit.BattleEditLayer2

    internal object BattleEditLayer2WeatherRenderEvents {
        private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    fun append(log: RenderEventLog, phase: String) {
        /**
         * 공개 메서드 `d`
         *
         * ### 파라미터
        - `layer` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
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
            layer: String,
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
        d(
            "HallLayer",
            "Canvas/Layer/panel0/bg",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "default_sprite_splash",
            opacity = .392f
        )
        d("HallLayer", "Canvas/Layer/panel0/list0", "sliced-sprite", 767.878f, 308.794f, 169.8f, 179.5f, "box1")
        d(
            "HallLayer",
            "Canvas/Layer/panel0/list0/scrollview",
            "tiled-sprite",
            767.878f,
            308.794f,
            169.8f,
            179.5f,
            "Logo_12-1"
        )
        BattleEditLayer2.weatherNames.forEachIndexed { index, text ->
            val y = 463.854f - index * 50f
            val w = when (text.length) {
                1 -> 34.6f; 2 -> 69.2f; else -> 103.8f
            }
            d(
                "HallLayer",
                "Canvas/Layer/panel0/list0/scrollview/view/content/item",
                "sliced-sprite",
                767.878f,
                y,
                169.8f,
                50f,
                "box1"
            )
            d(
                "HallLayer",
                "Canvas/Layer/panel0/list0/scrollview/view/content/item/label",
                "label",
                852.778f - w / 2f,
                y - .2f,
                w,
                50.4f,
                text = text,
                blend = alphaBlend
            )
        }
    }
    }

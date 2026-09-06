// Battle
    package com.jojo.game.presentation.battle.edit.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog
import com.jojo.game.presentation.battle.edit.BattleEditLayer2
    /**
     * `BattleEditLayer2WeatherRenderEvents`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal object BattleEditLayer2WeatherRenderEvents {
        /**
         * `alphaBlend` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, phase: String) {

        /**
         * `d`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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

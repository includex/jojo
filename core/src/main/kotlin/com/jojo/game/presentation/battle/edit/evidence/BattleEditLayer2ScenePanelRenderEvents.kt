// Battle
    package com.jojo.game.presentation.battle.edit.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog
    internal object BattleEditLayer2ScenePanelRenderEvents {
        private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    fun append(log: RenderEventLog, phase: String) {

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
            log.draw(phase, "HallLayer", path, type, x, y, w, h, asset, opacity, blend, true, text)
        d("Canvas/Layer/panel0/bg", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .392f)
        d("Canvas/Layer/panel0/list0", "sliced-sprite", 715.136f, 298.894f, 250f, 179.5f, "box1")
        d("Canvas/Layer/panel0/list0/scrollview", "tiled-sprite", 715.136f, 298.894f, 250f, 179.5f, "Logo_12-1")
        val names = listOf(
            "영천의 전투",
            "사수관 전투",
            "호로관 전투",
            "동탁 추격전",
            "청주 황건 토벌전",
            "서주 복수전",
            "복양의 전투",
            "복양의 전투 2",
            "복양의 전투 3",
            "황제 구출 전투"
        )
        names.forEachIndexed { index, name ->
            val y = 428.394f - index * 50f
            d("Canvas/Layer/panel0/list0/scrollview/view/content/item", "sliced-sprite", 715.136f, y, 250f, 50f, "box1")
            d(
                "Canvas/Layer/panel0/list0/scrollview/view/content/item/label",
                "label",
                720.136f,
                y + 9.88f,
                240f,
                30.24f,
                text = "$index $name",
                blend = alphaBlend
            )
        }
    }
    }

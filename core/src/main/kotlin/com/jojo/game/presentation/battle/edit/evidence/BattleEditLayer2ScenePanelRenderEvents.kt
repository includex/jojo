// Battle
    package com.jojo.game.presentation.battle.edit.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog
    /**
     * `BattleEditLayer2ScenePanelRenderEvents`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal object BattleEditLayer2ScenePanelRenderEvents {
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

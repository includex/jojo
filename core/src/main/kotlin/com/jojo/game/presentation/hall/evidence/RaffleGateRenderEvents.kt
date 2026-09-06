// Presentation
package com.jojo.game.presentation.hall.evidence
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/**
 * `RaffleGateRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object RaffleGateRenderEvents {
    /**
     * `PHASE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val PHASE = "hall-raffle-gated-stable"
    /**
     * `alpha` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val alpha = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun jsonl(): String = RenderEventLog().apply {

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
            draw(PHASE, layer, path, type, x, y, w, h, asset, opacity, blend, true, text)

        /**
         * `e`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun e(
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
            d("SettingLayer", path, type, x, y, w, h, asset, text, opacity, blend)

        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun label(path: String, text: String, x: Float, y: Float, w: Float, h: Float = 50.4f) =
            e(path, "label", x, y, w, h, text = text, blend = alpha)

        d(
            "HallLayer",
            "Canvas/Layer/map",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        d(
            "HallLayer",
            "Canvas/Layer/Panel_cancel",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "default_sprite_splash",
            opacity = .118f
        )
        e("Canvas/Layer/bg", "tiled-sprite", 195.686f, 41f, 1097f, 718f, "Logo_9-1")
        e("Canvas/Layer/bg/box1", "tiled-sprite", 195.686f, 41f, 1097f, 718f, "box1")
        e("Canvas/Layer/bg/bg1", "sprite", 195.686f, 709f, 1097f, 50f, "bg1")
        label("Canvas/Layer/bg/bg1/label", "환경 설정", 200.686f, 708.8f, 149.51f)
        e("Canvas/Layer/bg/scrollview", "sliced-sprite", 203.686f, 110f, 1081f, 596f, "box2")
        label(
            "Canvas/Layer/bg/scrollview/view/content/label",
            "항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.",
            65.851f,
            650.189f,
            1078.67f
        )

        val toggles = listOf(
            Triple(floatArrayOf(218.29f, 611f, 252.29f), "배경 음악 듣기", true),
            Triple(floatArrayOf(218.29f, 546f, 252.29f), "효과음 듣기", true),
            Triple(floatArrayOf(218.29f, 482f, 252.186f), "전투 시 전장 축소 이미지가 자동으로 표시됩니다.", true),
            Triple(floatArrayOf(218.186f, 417f, 252.186f), "대화창 자동 닫힘", false),
            Triple(floatArrayOf(218.186f, 353f, 252.186f), "체력 바가 유닛 위에 있습니다", false),
        )
        toggles.forEachIndexed { i, (r, text, checked) ->
            val p = "Canvas/Layer/bg/scrollview/view/content/button$i/toggle"
            e("$p/Background", "sprite", r[0], r[1], 28f, 28f, "default_toggle_normal")
            if (checked) e(
                "$p/checkmark",
                "sprite",
                if (i == 2) 218.186f else r[0],
                r[1],
                28f,
                28f,
                "default_toggle_checkmark"
            )
            label("$p/Label", text, r[2], r[1] - 6f, 526f, 40f)
        }

        /**
         * `panel`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun panel(i: Int, y: Float, bx: Float, by: Float, bw: Float, title: String) {
            val p = "Canvas/Layer/bg/scrollview/view/content/panel$i"; e(
                p,
                "sliced-sprite",
                793.336f,
                y,
                479.7f,
                100f,
                "box1"
            ); e(
                "$p/bg1",
                "sprite",
                if (i == 3) 833.325f else 831.428f,
                by + .2f,
                if (i == 3) 210f else 166f,
                50f,
                "bg1"
            ); label("$p/bg1/label", title, bx, by, bw)
        }

        /**
         * `radios`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun radios(i: Int, y: Float, selected: Int, values: List<String>) {
            val xs = floatArrayOf(818.346f, 974.286f, 1119.419f)
            val lx = floatArrayOf(855.731f, 1024.671f, 1169.804f); values.forEachIndexed { j, v ->
                val p = "Canvas/Layer/bg/scrollview/view/content/panel$i/toggleContainer/toggle$j"; e(
                "$p/Background",
                "sprite",
                xs[j],
                y,
                32f,
                32f,
                "default_radio_button_off"
            ); if (j == selected) e(
                "$p/checkmark",
                "sprite",
                xs[j],
                y,
                32f,
                32f,
                "default_radio_button_on"
            ); label("$p/Label", v, lx[j], y - 4f, 90f, 40f)
            }
        }
        panel(0, 520.389f, 822.373f, 595.481f, 184.11f, "텍스트 속도"); radios(0, 545.638f, 1, listOf("느림", "중", "빠르게"))
        panel(1, 388.389f, 839.673f, 463.481f, 149.51f, "게임 속도")
        e(
            "Canvas/Layer/bg/scrollview/view/content/panel1/slider/Background",
            "sliced-sprite",
            816.186f,
            418.346f,
            434f,
            20f,
            "default_scrollbar"
        )
        e(
            "Canvas/Layer/bg/scrollview/view/content/panel1/slider/Handle",
            "sliced-sprite",
            800.186f,
            412.346f,
            32f,
            32f,
            "default_radio_button_off"
        )
        panel(2, 256.389f, 839.673f, 331.481f, 149.51f, "정보 설명"); radios(2, 281.638f, 1, listOf("자세히", "보통", "요약"))
        /**
         * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val p = "Canvas/Layer/bg/scrollview/view/content/panel3"
        e(p, "sliced-sprite", 793.336f, 81.389f, 479.7f, 142f, "box1"); e(
        "$p/bg1",
        "sprite",
        833.325f,
        198.167f,
        210f,
        50f,
        "bg1"
    ); label("$p/bg1/label", "대화창 색상", 846.27f, 197.967f, 184.11f)
        floatArrayOf(831.12f, 933.12f, 1035.12f, 1137.12f).forEachIndexed { i, x ->
            /**
             * `q` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val q = "$p/item$i"; e(
            q,
            "sliced-sprite",
            x,
            92.703f,
            100f,
            100f,
            "box1"
        ); e("$q/Logo_9-1", "sprite", x + 2f, 94.703f, 96f, 96f, "Logo_${9 + i}-1"); if (i == 0) e(
            "$q/box6",
            "tiled-sprite",
            x,
            92.703f,
            100f,
            100f,
            "box6"
        )
        }
        e("Canvas/Layer/bg/button1/Background", "sliced-sprite", 1130.186f, 47f, 156f, 56f, "box3")
        label("Canvas/Layer/bg/button1/Background/Label", "확인", 1158.186f, 57.261f, 100f, 40f)
    }.jsonl()
}

// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** ScenarioPropertyEvidenceRecorder: 원본에 작성된 상태 비보유 PropertyLayer 순회를 기록한다. */
internal class ScenarioPropertyEvidenceRecorder {
    /** append: 검증 이벤트와 산출물을 기록한다. */
    fun append(log: RenderEventLog, view: ScenarioStaticHallEvidenceView) {
        check(view.kind == ScenarioStaticHallEvidenceKind.PROPERTY)
        val scale = .86f
        /** draw: 검증 렌더 이벤트를 구성하고 반환한다. */
        fun draw(path: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") {
            val type = when {
                text.isNotEmpty() -> "label"
                asset == "Logo_9-1" -> "tiled-sprite"
                asset in setOf("box1", "box2", "box3", "box4") -> "sliced-sprite"
                else -> "sprite"
            }
            log.draw(
                "hall-property-stable", "PropertyLayer", path, type,
                x * scale, y * scale, w * scale, h * scale, asset,
                blend = if (text.isEmpty()) listOf(770, 771) else listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
                text = text,
            )
        }
        draw("Canvas/Layer/bg", 247.186f, 47f, 994f, 706f, "Logo_9-1")
        draw("Canvas/Layer/bg/box1", 247.186f, 47f, 994f, 706f, "box1")
        draw("Canvas/Layer/bg/bg1", 247.186f, 703f, 994f, 50f, "bg1")
        draw("Canvas/Layer/bg/bg1/label", 669.431f, 702.8f, 149.51f, 50.4f, text = "창고 일람")
        draw("Canvas/Layer/bg/panel0", 249.186f, 117f, 990f, 524f, "box4")
        val headers = listOf(
            floatArrayOf(251.236f, 637.9f, 376.9f, 60f, 405.086f, 642.7f, 69.2f, 50.4f) to "이름",
            floatArrayOf(628.636f, 638f, 195.1f, 60f, 691.586f, 642.8f, 69.2f, 50.4f) to "속성",
            floatArrayOf(823.736f, 638f, 106.9f, 60f, 842.586f, 642.8f, 69.2f, 50.4f) to "레벨",
            floatArrayOf(931.586f, 638f, 101.2f, 60f, 930.286f, 642.8f, 103.8f, 50.4f) to "경험치",
            floatArrayOf(1031.986f, 638f, 206.4f, 60f, 1083.286f, 642.8f, 103.8f, 50.4f) to "소지자",
        )
        headers.forEach { (g, value) ->
            draw("Canvas/Layer/bg/panel0/title0", g[0], g[1], g[2], g[3], "box3")
            draw("Canvas/Layer/bg/panel0/title0/label", g[4], g[5], g[6], g[7], text = value)
        }
        val rows = listOf(
            listOf("단검", "조조", "검", "1", "0") to "1-1",
            listOf("단검", "병사 01", "검", "1", "0") to "1-1",
            listOf("돌로 만든 보검", "허자장", "보검", "1", "0") to "19-1",
        )
        rows.forEachIndexed { index, (values, icon) ->
            val y = 560f - index * 78f
            draw("Canvas/Layer/bg/panel0/scrollview/view/content/item", 253.186f, y, 984f, 76f, "box3")
            draw("Canvas/Layer/bg/panel0/scrollview/view/content/item/box2", 260.288f, y + 9.597f, 60f, 60f, "box2")
            draw("Canvas/Layer/bg/panel0/scrollview/view/content/item/box2/icon", 262.538f, y + 11.547f, 55.7f, 55.9f, icon)
            val labelRects = arrayOf(
                floatArrayOf(329.286f, y + 12.8f, 288.1f, 50.4f), floatArrayOf(1037.986f, y + 12.8f, 190.8f, 50.4f),
                floatArrayOf(633.386f, y + 12.8f, 186.2f, 50.4f), floatArrayOf(866.061f, y + 12.8f, 22.25f, 50.4f),
                floatArrayOf(971.061f, y + 12.8f, 22.25f, 50.4f),
            )
            values.forEachIndexed { column, value ->
                val g = labelRects[column]
                draw("Canvas/Layer/bg/panel0/scrollview/view/content/item/label$column", g[0], g[1], g[2], g[3], text = value)
            }
        }
        listOf(820.971f, 625.468f, 927.065f, 1029.026f).forEach { x ->
            draw("Canvas/Layer/bg/panel0/vline", x, 122.75f, 6f, 515.38f, "vline")
        }
        val tabs = listOf(
            Triple(267.99f, "무기", floatArrayOf(314.292f, 69.2f)),
            Triple(415.99f, "방어구", floatArrayOf(444.992f, 103.8f)),
            Triple(563.99f, "보조", floatArrayOf(610.292f, 69.2f)),
            Triple(711.99f, "아이템", floatArrayOf(740.992f, 103.8f)),
        )
        tabs.forEachIndexed { index, (radioX, value, labelGeometry) ->
            draw("Canvas/Layer/bg/toggleContainer/toggle$index/Background", radioX, 67.577f, 32f, 32f, "default_radio_button_off")
            if (index == 0) draw("Canvas/Layer/bg/toggleContainer/toggle0/checkmark", radioX, 67.577f, 32f, 32f, "default_radio_button_on")
            draw("Canvas/Layer/bg/toggleContainer/toggle$index/label", labelGeometry[0], 58.377f, labelGeometry[1], 50.4f, text = value)
        }
        draw("Canvas/Layer/bg/button0/Background", 1084.386f, 54.5f, 144.8f, 54f, "box3")
        draw("Canvas/Layer/bg/button0/Background/Label", 1106.786f, 59.5f, 100f, 50f, text = "확인")
    }
}

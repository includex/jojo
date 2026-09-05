package com.jojo.game.presentation.scenario.evidence

import com.jojo.game.RenderEventLog

/** Immutable input for the Hall equipment-change confirmation overlay. */
internal data class ScenarioEquipConfirmationEvidenceView(
    val fixture: String?,
    val values: List<Int>,
    val actionLabel: String,
)

/** Appends the source-order equipment confirmation records to an existing Hall trace. */
internal class ScenarioEquipConfirmationEvidenceRecorder {
    fun append(log: RenderEventLog, view: ScenarioEquipConfirmationEvidenceView) {
        val scale = .86f
        val spriteBlend = listOf(770, 771)
        val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun event(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", opacity: Float = 1f
        ) =
            log.draw(
                "hall-${view.fixture ?: "equip-confirm"}-stable", "HallLayer", path, type,
                x * scale, y * scale, w * scale, h * scale, asset, opacity,
                if (type == "label") labelBlend else spriteBlend, text = text
            )

        fun label(path: String, value: String, x: Float, y: Float, w: Float, h: Float = 50.4f) =
            event(path, "label", x, y, w, h, text = value)

        event("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .157f)
        event("Canvas/Layer/baseInfo", "sliced-sprite", 483.686f, 234.5f, 521f, 331f, "bg1")
        event("Canvas/Layer/baseInfo/box3", "sliced-sprite", 483.686f, 234.5f, 521f, 331f, "box3")
        val boxes = listOf(
            floatArrayOf(879.977f, 317f, 921.352f, 316.8f, 22.25f),
            floatArrayOf(638.686f, 317f, 668.381f, 316.8f, 45.61f),
            floatArrayOf(879.977f, 376f, 921.352f, 375.8f, 22.25f),
            floatArrayOf(638.686f, 376f, 680.061f, 375.8f, 22.25f),
            floatArrayOf(879.977f, 436f, 909.672f, 435.8f, 45.61f),
            floatArrayOf(638.686f, 436f, 680.061f, 435.8f, 22.25f),
            floatArrayOf(879.977f, 495f, 914.692f, 494.8f, 35.57f),
            floatArrayOf(638.686f, 495f, 657.261f, 494.8f, 67.85f),
        )
        boxes.forEachIndexed { drawIndex, box ->
            val node = 7 - drawIndex
            event("Canvas/Layer/baseInfo/bg$node", "sliced-sprite", box[0], box[1], 105f, 50f, "box2")
            val value = view.values.getOrElse(node) { 0 }
            val text = if (value > 0) "+$value" else value.toString()
            val width = when (text) {
                "0" -> 22.25f; "+1", "+2" -> 45.61f; "-5" -> 35.57f; "+10" -> 67.85f; else -> box[4]
            }
            label("Canvas/Layer/baseInfo/bg$node/label", text, box[0] + (105f - width) / 2f, box[3], width)
        }
        listOf(
            arrayOf<Any>("이동력", 760.093f, 316.707f, 103.8f), arrayOf<Any>("사기", 507.393f, 316.707f, 69.2f),
            arrayOf<Any>("폭발력", 760.093f, 375.707f, 103.8f), arrayOf<Any>("방어력", 510.093f, 375.707f, 103.8f),
            arrayOf<Any>("정신력", 760.093f, 435.707f, 103.8f), arrayOf<Any>("공격력", 510.093f, 435.707f, 103.8f),
            arrayOf<Any>("MP", 751.993f, 494.707f, 60f), arrayOf<Any>("HP", 502.208f, 494.707f, 55.57f),
        ).forEach {
            label(
                "Canvas/Layer/baseInfo/label",
                it[0] as String,
                it[1] as Float,
                it[2] as Float,
                it[3] as Float
            )
        }
        listOf(
            Triple("button0", view.actionLabel, 549.186f),
            Triple("button1", "취소", 789.186f)
        ).forEach { (button, text, x) ->
            event("Canvas/Layer/baseInfo/$button/Background", "sliced-sprite", x, 251.901f, 150f, 50f, "box3")
            label("Canvas/Layer/baseInfo/$button/Background/Label", text, x + 25f, 259.901f, 100f, 40f)
        }
    }
}

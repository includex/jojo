package com.jojo.game

import com.badlogic.gdx.utils.Align

/** Pure source-order plan for the EquipConfirmLayer overlay. */
internal object HallEquipConfirmationRenderPlan {
    fun commands(view: HallEquipConfirmationView): List<HallEquipConfirmationDrawCommand> = buildList {
        add(HallEquipConfirmationDrawCommand(HallEquipConfirmationDrawKind.OVERLAY))
        patch("maps/ui/unit-info/bg1.png", 483.686f, 234.5f, 521f, 331f)
        patch("maps/ui/unit-info/box3.png", 483.686f, 234.5f, 521f, 331f)
        valueBoxes.forEachIndexed { drawIndex, (x, y) ->
            patch("maps/ui/start-battle/box2.png", x, y, 105f, 50f)
            val value = view.values[7 - drawIndex]
            text(
                if (value > 0) "+$value" else value.toString(),
                x,
                y + 38f,
                105f,
                color = when {
                    value < 0 -> HallEquipConfirmationTextColor.GREEN
                    value > 0 -> HallEquipConfirmationTextColor.RED
                    else -> HallEquipConfirmationTextColor.BLACK
                },
            )
        }
        statLabels.forEach { (label, x, y) -> text(label, x, y + 38f) }
        listOf(
            Triple(549.186f, view.actionLabel, 574.186f),
            Triple(789.186f, "취소", 814.186f),
        ).forEach { (x, label, labelX) ->
            patch("maps/ui/unit-info/box3.png", x, 251.901f, 150f, 50f)
            text(label, labelX, 290.901f, 100f)
        }
    }

    private fun MutableList<HallEquipConfirmationDrawCommand>.patch(
        asset: String, x: Float, y: Float, width: Float, height: Float,
    ) {
        add(HallEquipConfirmationDrawCommand(
            kind = HallEquipConfirmationDrawKind.PATCH,
            asset = asset,
            x = x,
            y = y,
            width = width,
            height = height,
        ))
    }

    private fun MutableList<HallEquipConfirmationDrawCommand>.text(
        value: String,
        x: Float,
        y: Float,
        width: Float = 0f,
        color: HallEquipConfirmationTextColor = HallEquipConfirmationTextColor.BLACK,
    ) {
        add(HallEquipConfirmationDrawCommand(HallEquipConfirmationDrawKind.TEXT, text = value, x = x, y = y, width = width, color = color))
    }

    private val valueBoxes = listOf(
        879.977f to 317f, 638.686f to 317f,
        879.977f to 376f, 638.686f to 376f,
        879.977f to 436f, 638.686f to 436f,
        879.977f to 495f, 638.686f to 495f,
    )
    private val statLabels = listOf(
        Triple("이동력", 760.093f, 316.707f), Triple("사기", 507.393f, 316.707f),
        Triple("폭발력", 760.093f, 375.707f), Triple("방어력", 510.093f, 375.707f),
        Triple("정신력", 760.093f, 435.707f), Triple("공격력", 510.093f, 435.707f),
        Triple("MP", 751.993f, 494.707f), Triple("HP", 502.208f, 494.707f),
    )
}

internal data class HallEquipConfirmationDrawCommand(
    val kind: HallEquipConfirmationDrawKind,
    val asset: String = "",
    val text: String = "",
    val x: Float = 0f,
    val y: Float = 0f,
    val width: Float = 0f,
    val height: Float = 0f,
    val color: HallEquipConfirmationTextColor = HallEquipConfirmationTextColor.BLACK,
)

internal enum class HallEquipConfirmationDrawKind { OVERLAY, PATCH, TEXT }
internal enum class HallEquipConfirmationTextColor { BLACK, GREEN, RED }

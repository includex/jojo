package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** Pure, source-ordered draw plan for BuyLayer's right-side unit card. */
internal object HallBuyUnitSummaryRenderPlan {
    private const val X = 701.77f
    private const val Y = 132.81f

    fun commands(view: HallBuyUnitSummaryView): List<HallBuyUnitSummaryDrawCommand> = buildList {
        portrait(view.portraitId, X + 5f, Y + 225f, 165f, 206f)
        text(view.name, X + 202f, Y + 407f, 240f, Align.left)
        text(view.postName, X + 202f, Y + 360f, 240f, Align.left)
        text("Lv  ${view.level}", X + 202f, Y + 310f, 240f, Align.left)
        text("Exp", X + 202f, Y + 265f, 240f, Align.left)
        patch("maps/ui/start-battle/box1.png", X + 265f, Y + 237f, 125f, 34f)
        sprite("maps/ui/start-battle/box2.png", X + 270f, Y + 245f, 115f, 18f, HallBuyUnitSummaryTint.MUTED)
        text("0/100", X + 270f, Y + 267f, 115f)
        stat("HP", view.hitPoints, X, Y + 208f)
        stat("MP", view.magicPoints, X + 214f, Y + 208f)
        view.stats.take(6).forEachIndexed { index, stat ->
            val column = index % 2
            val row = index / 2
            stat(stat.name, stat.value, X + column * 214f, Y + 162f - row * 51f)
        }
    }

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.stat(name: String, value: Int, x: Float, y: Float) {
        text(name, x, y, 240f, Align.left)
        patch("maps/ui/start-battle/box1.png", x + 105f, y - 31f, 72f, 43f)
        text(value.toString(), x + 105f, y, 72f)
    }

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.portrait(
        id: Int, x: Float, y: Float, width: Float, height: Float,
    ) = add(HallBuyUnitSummaryDrawCommand(HallBuyUnitSummaryDrawKind.PORTRAIT, portraitId = id, x = x, y = y, width = width, height = height))

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.patch(asset: String, x: Float, y: Float, width: Float, height: Float) =
        add(HallBuyUnitSummaryDrawCommand(HallBuyUnitSummaryDrawKind.PATCH, asset = asset, x = x, y = y, width = width, height = height))

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.sprite(
        asset: String, x: Float, y: Float, width: Float, height: Float, tint: HallBuyUnitSummaryTint,
    ) = add(HallBuyUnitSummaryDrawCommand(HallBuyUnitSummaryDrawKind.SPRITE, asset = asset, x = x, y = y, width = width, height = height, tint = tint))

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.text(value: String, x: Float, y: Float, width: Float, align: Int = Align.center) =
        add(HallBuyUnitSummaryDrawCommand(HallBuyUnitSummaryDrawKind.TEXT, text = value, x = x, y = y, width = width, align = align))
}

internal data class HallBuyUnitSummaryDrawCommand(
    val kind: HallBuyUnitSummaryDrawKind,
    val asset: String = "",
    val portraitId: Int = 0,
    val text: String = "",
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 0f,
    val tint: HallBuyUnitSummaryTint = HallBuyUnitSummaryTint.WHITE,
    val align: Int = Align.center,
)

internal enum class HallBuyUnitSummaryDrawKind { PORTRAIT, PATCH, SPRITE, TEXT }

internal enum class HallBuyUnitSummaryTint { WHITE, MUTED }

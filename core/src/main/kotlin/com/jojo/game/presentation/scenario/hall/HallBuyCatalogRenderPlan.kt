package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** Pure source-order plan for the read-only BuyLayer catalog pane. */
internal object HallBuyCatalogRenderPlan {
    fun commands(view: HallBuyCatalogView): List<HallBuyCatalogDrawCommand> = buildList {
        patch("maps/ui/start-battle/box1.png", 176.42f, 89.01f, 480.74f, 503.1f)
        text("상품 목록", 193.72f, 595.19f, 240f, align = Align.left)
        button("무기점", 183.25f, 521.28f, 154.8f)
        button("상점", 337.85f, 521.28f, 154.8f)
        if (view.propertyTab) propertyRows(view.rows) else equipmentRows(view.rows)
    }

    private fun MutableList<HallBuyCatalogDrawCommand>.equipmentRows(rows: List<HallBuyCatalogRowView>) {
        rows.forEachIndexed { index, row ->
            val y = 370.80f - index * 153.08f
            patch("maps/ui/start-battle/box1.png", 184.64f, y, 461.82f, 151.36f)
            patch("maps/ui/start-battle/box1.png", 190.50f, y + 57.95f, 86f, 86f)
            sprite("maps/item-icons/${row.icon}.png", 194.8f, y + 62.25f, 77.4f, 77.4f)
            text(row.name, 283.10f, y + 142f, 188f, align = Align.left)
            text("레벨:", 475.86f, y + 142f, 80f, align = Align.left)
            text("1", 562.72f, y + 142f, 42f, align = Align.left)
            text("속성:", 283.10f, y + 97f, 90f, align = Align.left)
            text(row.typeName, 371.68f, y + 97f, 90f, align = Align.left)
            text("인벤토리:", 193.70f, y + 46f, 112f, align = Align.left)
            text(row.inventory.toString(), 280.56f, y + 46f, 44f, align = Align.left)
            text("총합:", 338.62f, y + 46f, 80f, align = Align.left)
            text(row.total.toString(), 425.48f, y + 46f, 44f, align = Align.left)
            text("가격:", 475.86f, y + 46f, 80f, align = Align.left)
            text(row.price, 562.72f, y + 46f, 76f, align = Align.left)
        }
    }

    private fun MutableList<HallBuyCatalogDrawCommand>.propertyRows(rows: List<HallBuyCatalogRowView>) {
        rows.forEachIndexed { index, row ->
            val y = 456f - index * 108f
            patch("maps/ui/start-battle/box1.png", 184.64f, y, 461.82f, 106.64f)
            patch("maps/ui/start-battle/box1.png", 190.5f, y + 16f, 74f, 74f)
            sprite("maps/item-icons/${row.icon}.png", 197f, y + 22f, 62f, 62f)
            text(row.name, 282f, y + 73f, 180f, align = Align.left)
            text("인벤토리: ${row.inventory}", 445f, y + 73f, 165f, align = Align.left)
            text("가격: ${row.price}", 338f, y + 30f, 220f, align = Align.left)
        }
    }

    private fun MutableList<HallBuyCatalogDrawCommand>.patch(
        asset: String, x: Float, y: Float, width: Float, height: Float, inset: Int = 3,
    ) {
        add(HallBuyCatalogDrawCommand(HallBuyCatalogDrawKind.PATCH, asset = asset, x = x, y = y, width = width, height = height, inset = inset))
    }

    private fun MutableList<HallBuyCatalogDrawCommand>.sprite(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallBuyCatalogDrawCommand(HallBuyCatalogDrawKind.SPRITE, asset = asset, x = x, y = y, width = width, height = height))
    }

    private fun MutableList<HallBuyCatalogDrawCommand>.text(value: String, x: Float, y: Float, width: Float, align: Int = Align.center) {
        add(HallBuyCatalogDrawCommand(HallBuyCatalogDrawKind.TEXT, text = value, x = x, y = y, width = width, align = align))
    }

    private fun MutableList<HallBuyCatalogDrawCommand>.button(value: String, x: Float, y: Float, width: Float) {
        patch("maps/ui/start-battle/button.png", x, y, width, 43f, inset = 9)
        text(value, x, y + 31f, width)
    }
}

internal data class HallBuyCatalogDrawCommand(
    val kind: HallBuyCatalogDrawKind,
    val asset: String = "",
    val text: String = "",
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 0f,
    val inset: Int = 0,
    val align: Int = Align.center,
)

internal enum class HallBuyCatalogDrawKind { PATCH, SPRITE, TEXT }

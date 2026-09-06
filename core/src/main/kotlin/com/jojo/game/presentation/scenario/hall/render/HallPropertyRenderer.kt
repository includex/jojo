// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.hall.HallPropertyRenderPlan
import com.jojo.game.presentation.scenario.hall.HallPropertyView

/** HallPropertyRenderer: 거점 속성 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallPropertyRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallPropertyView) {
        val draw = HallRenderPrimitives(assets, batch)
        val x = 212.42f
        val y = 40.42f
        val width = 854.84f
        val height = 607.16f
        batch.color = com.badlogic.gdx.graphics.Color.WHITE
        draw.ui("logo9")?.let { draw.tiled(it, x, y, width, height) }
        draw.patch("box1")?.draw(batch, x, y, width, height)
        draw.patch("title", 5)?.draw(batch, x, y + height - 51.6f, width, 51.6f)
        draw.title("창고 일람", x, y + height - 8f, width)
        table(draw, batch, view, x, y, width, height)
        tabs(draw, batch, view.selectedTab)
        draw.button("확인", x + width - 135f, y + 5f, 125f)
        draw.resetColor()
    }

    private fun table(draw: HallRenderPrimitives, batch: SpriteBatch, view: HallPropertyView, x: Float, y: Float, width: Float, height: Float) {
        val widths = floatArrayOf(323.06f, 168.13f, 91.24f, 87.69f, 176.44f)
        val headers = listOf("이름", "속성", "레벨", "경험치", "소지자")
        var cellX = x + 5f
        headers.forEachIndexed { index, value ->
            draw.cell(cellX, y + height - 101f, widths[index], 48f)
            draw.text(value, cellX, y + height - 67f, widths[index], centered = true)
            cellX += widths[index]
        }
        view.rows.forEachIndexed { row, item ->
            val rowY = HallPropertyRenderPlan.rowY(row)
            val values = listOf(item.name, item.typeName, item.level, item.experience, item.owner)
            cellX = x + 5f
            values.forEachIndexed { index, value ->
                draw.cell(cellX, rowY, widths[index], 65.36f)
                if (index == 0) {
                    draw.asset("maps/item-icons/${item.icon}.png")?.let { batch.draw(it, cellX + 6f, rowY + 7f, 50f, 50f) }
                    draw.text(value, cellX + 63f, rowY + 43f, widths[index] - 69f)
                } else draw.text(value, cellX + 4f, rowY + 43f, widths[index] - 8f, centered = true)
                cellX += widths[index]
            }
        }
    }

    private fun tabs(draw: HallRenderPrimitives, batch: SpriteBatch, selectedTab: Int) {
        listOf("무기", "방어구", "보조", "아이템").forEachIndexed { index, value ->
            val centerX = 244.23f + index * 127.28f
            val state = if (selectedTab == index) "on" else "off"
            draw.asset("maps/ui/title/setting/radio-$state.png")?.let {
                batch.draw(it, centerX - 13.76f, 58.12f, 27.52f, 27.52f)
            }
            draw.text(value, centerX + 29.7f, 88f, 95f)
        }
    }
}

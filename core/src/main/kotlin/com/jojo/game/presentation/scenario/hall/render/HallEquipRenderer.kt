// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.badlogic.gdx.utils.viewport.Viewport
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.hall.HallEquipmentRenderPlan
import com.jojo.game.presentation.scenario.hall.HallEquipUnitView
import com.jojo.game.presentation.scenario.hall.HallEquipView

/** HallEquipRenderer: 거점 Equip 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallEquipRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, viewport: Viewport, view: HallEquipView) {
        val draw = HallRenderPrimitives(assets, batch)
        batch.color = Color.WHITE
        draw.ui("logo9")?.let { draw.tiled(it, 118.84f, 28.81f, 1042.32f, 630.38f) }
        draw.patch("button", 9)?.draw(batch, 118.84f, 28.81f, 1042.32f, 630.38f)
        draw.patch("title", 5)?.draw(batch, 118.84f, 616.19f, 1042.32f, 43f)
        draw.title("장비", 118.84f, 654.19f, 1042.32f)
        footer(draw)
        tabs(draw, batch, view.selectedTab)
        inventory(draw, batch, view)
        unitHeader(draw, batch, view.unit)
        clipped(viewport, batch) { unitSummary(draw, batch, assets, view.unit) }
        view.notice?.let { notice ->
            assets.bodyFont.color = Color(.55f, .05f, .05f, 1f)
            assets.bodyFont.draw(batch, notice, 136.84f, 607.19f, 1006.32f, com.badlogic.gdx.utils.Align.right, false)
        }
        draw.resetColor()
    }

    private fun footer(draw: HallRenderPrimitives) {
        draw.button("이전 무장", 842.53f, 37.84f, 152.22f, 43f, 31f)
        draw.button("다음 무장", 994.75f, 37.84f, 152.22f, 43f, 31f)
        draw.button("종료", 643.73f, 37.84f, 83.42f, 43f, 31f)
        draw.button("모두 해제", 493.37f, 37.84f, 148.95f, 43f, 31f)
        draw.button("정보", 125.35f, 37.84f, 85.74f, 43f, 31f)
    }

    private fun tabs(draw: HallRenderPrimitives, batch: SpriteBatch, selectedTab: Int) {
        listOf("전부", "무기", "보구", "보조").forEachIndexed { index, value ->
            val x = 124f + index * 129f
            draw.button(value, x, 566.74f, 129f, 43f, 31f)
            if (selectedTab == index) {
                batch.color = Color(0f, 0f, 0f, .10f)
                draw.ui("box2")?.let { batch.draw(it, 128f + index * 129f, 570.74f, 121f, 35f) }
                batch.color = Color.WHITE
                draw.text(value, x, 597.74f, 129f, centered = true)
            }
        }
        draw.patch("box1")?.draw(batch, 124.26f, 85.96f, 604.92f, 481.69f)
        draw.patch("box2")?.draw(batch, 124.26f, 85.96f, 604.92f, 481.69f)
        draw.ui("box2")?.let { batch.draw(it, 730.56f, 33.84f, 5.16f, 582.31f) }
    }

    private fun inventory(draw: HallRenderPrimitives, batch: SpriteBatch, view: HallEquipView) {
        view.inventoryRows.forEachIndexed { index, item ->
            val y = HallEquipmentRenderPlan.inventoryRowY(index)
            draw.patch("box1")?.draw(batch, 132f, y - 48f, 582f, 62f)
            draw.asset("maps/item-icons/${item.icon}.png")?.let { batch.draw(it, 141f, y - 40f, 52f, 52f) }
            draw.text(item.name, 207f, y + 8f, 260f)
            draw.text(item.typeName, 430f, y + 8f, 135f)
            draw.text(item.level, 568f, y + 8f, 54f, centered = true)
            draw.text(item.experience, 626f, y + 8f, 72f, centered = true)
        }
    }

    private fun unitHeader(draw: HallRenderPrimitives, batch: SpriteBatch, unit: HallEquipUnitView) {
        draw.patch("button", 9)?.draw(batch, 794.8f, 565.88f, 309.6f, 48.16f)
        draw.ui("box2")?.let { batch.draw(it, 947.02f, 571.17f, 5.16f, 41.02f) }
        draw.text(unit.name, 842.32f, 604f, 59.51f, centered = true)
        draw.text(unit.armName, 998.89f, 604f, 59.51f, centered = true)
    }

    private fun unitSummary(draw: HallRenderPrimitives, batch: SpriteBatch, assets: ScenarioSceneAssets, unit: HallEquipUnitView) {
        assets.portraitTexture(unit.portraitId)?.let { batch.draw(it, 769.54f, 355.47f, 165.12f, 206.4f) }
        draw.patch("box2")?.draw(batch, 748.90f, 357.19f, 206.4f, 202.96f)
        draw.text(unit.name, 965.08f, 551.88f, 59.51f)
        draw.text(unit.armName, 965.08f, 508.88f, 80f)
        draw.text("Exp", 965.08f, 422.02f, 59.28f)
        draw.patch("box2")?.draw(batch, 1029.58f, 387.79f, 115.24f, 20.64f)
        draw.text("0/100", 1044.16f, 423.20f, 86.09f, centered = true)
        draw.text("Lv", 964.97f, 465.02f, 36.34f)
        draw.text(unit.level, 1019.15f, 465.07f, 19.14f, centered = true)
        val labels = listOf(755.42f to 343.76f, 968.52f to 343.76f, 762.21f to 293.02f, 975.49f to 293.02f, 762.21f to 241.42f, 975.49f to 241.42f, 759.88f to 190.68f, 975.49f to 190.68f)
        val values = listOf(867.04f to 309.53f, 1081.18f to 309.53f, 867.04f to 258.79f, 1081.18f to 258.79f, 867.04f to 207.19f, 1081.18f to 207.19f, 867.04f to 156.45f, 1081.18f to 156.45f)
        unit.stats.zip(labels.zip(values)).forEach { (stat, positions) ->
            val (label, value) = positions
            draw.text(stat.name, label.first, label.second, if (stat.name.length <= 2) 59.51f else 89.27f)
            draw.patch("box2")?.draw(batch, value.first, value.second, 68.8f, 43f)
            draw.text(stat.value, value.first, value.second + 34.23f, 68.8f, centered = true)
        }
        unit.slots.forEach { slot -> drawSlot(draw, batch, slot.index, slot.label, slot.name, slot.icon, slot.level, slot.experience) }
    }

    private fun drawSlot(draw: HallRenderPrimitives, batch: SpriteBatch, index: Int, label: String, name: String, icon: Int?, level: String?, experience: String?) {
        val y = HallEquipmentRenderPlan.slotY(index)
        draw.patch("box1")?.draw(batch, 745.74f, y, 402.57f, 129f)
        draw.text(label, if (index == 0) 901.05f else 894f, y + 118.42f, 76f)
        draw.text(name, 966.8f, y + 118.68f, 177.16f)
        draw.patch("box1")?.draw(batch, 752.32f, y + 8.04f, 115.91f, 116.19f)
        icon?.let { draw.asset("maps/item-icons/$it.png") }?.let { batch.draw(it, 755.24f, y + 11.10f, 110.08f, 110.08f) }
        if (level != null && experience != null) {
            draw.text("Lv", 875.64f, y + 78.86f, 36.34f)
            draw.text(level, 933.26f, y + 78.86f, 19.14f)
            draw.text("Exp", 875.64f, y + 39.30f, 59.28f)
            draw.patch("box2")?.draw(batch, 949.60f, y + 4.22f, 175.44f, 20.64f)
            draw.text(experience, 994.28f, y + 39.63f, 86.09f, centered = true)
        }
    }

    private fun clipped(viewport: Viewport, batch: SpriteBatch, draw: () -> Unit) {
        val scissors = Rectangle()
        ScissorStack.calculateScissors(viewport.camera, batch.transformMatrix, Rectangle(739.76f, 89.44f, 414.52f, 474.72f), scissors)
        batch.flush()
        if (ScissorStack.pushScissors(scissors)) {
            draw()
            batch.flush()
            ScissorStack.popScissors()
        }
    }
}

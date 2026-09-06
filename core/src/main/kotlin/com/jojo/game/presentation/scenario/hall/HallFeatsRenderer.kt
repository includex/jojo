// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

internal object HallFeatsRenderer {
    private const val SCALE = .86f

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallFeatsView) {
        fun texture(name: String): Texture? {
            val paths = when (name) {
                "bg1", "box3" -> listOf("maps/ui/unit-info/$name.png", "maps/ui/win-condition/$name.png")
                "vline" -> listOf("maps/ui/terrain-layer/vline.png", "maps/ui/title/load/vline.png")
                "logo3" -> listOf("maps/ui/win-condition/logo3.png")
                else -> listOf("maps/ui/start-battle/$name.png")
            }
            return paths.firstNotNullOfOrNull(assets::hallTexture)
        }
        fun patch(name: String): NinePatch? =
            (texture(name) ?: when (name) { "box4" -> texture("box1"); "mark5" -> texture("button"); else -> null })
                ?.let { NinePatch(it, 3, 3, 3, 3) }
        fun tiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
            val tileWidth = texture.width * SCALE
            val tileHeight = texture.height * SCALE
            var dy = 0f
            while (dy < height - .01f) {
                val drawHeight = minOf(tileHeight, height - dy)
                val sourceHeight = (drawHeight / SCALE).toInt().coerceIn(1, texture.height)
                var dx = 0f
                while (dx < width - .01f) {
                    val drawWidth = minOf(tileWidth, width - dx)
                    val sourceWidth = (drawWidth / SCALE).toInt().coerceIn(1, texture.width)
                    batch.draw(texture, x + dx, y + dy, drawWidth, drawHeight, 0, 0, sourceWidth, sourceHeight, false, false)
                    dx += tileWidth
                }
                dy += tileHeight
            }
        }
        fun label(value: String, x: Float, y: Float, width: Float, align: Int = Align.center, wrap: Boolean = false) {
            assets.bodyFont.color = Color.BLACK
            assets.bodyFont.draw(batch, value, x * SCALE, (y + 42f) * SCALE, width * SCALE, align, wrap)
        }
        fun header(x: Float, y: Float, width: Float, height: Float, value: String, labelX: Float, labelWidth: Float) {
            patch("bg1")?.draw(batch, x * SCALE, y * SCALE, width * SCALE, height * SCALE)
            patch("box3")?.draw(batch, x * SCALE, y * SCALE, width * SCALE, height * SCALE)
            label(value, labelX, 607.081f, labelWidth)
        }

        batch.color = Color.WHITE
        texture("logo9")?.let { tiled(it, 267.686f * SCALE, 83.5f * SCALE, 953f * SCALE, 633f * SCALE) }
        patch("box4")?.draw(batch, 267.686f * SCALE, 83.5f * SCALE, 953f * SCALE, 633f * SCALE)
        texture("bg1")?.let { batch.draw(it, 267.686f * SCALE, 656.5f * SCALE, 953f * SCALE, 60f * SCALE) }
        patch("box3")?.draw(batch, 267.686f * SCALE, 656.5f * SCALE, 953f * SCALE, 60f * SCALE)
        label("공훈", 669.686f, 662.3f, 71.2f)
        texture("logo9")?.let { tiled(it, 277.686f * SCALE, 158.45f * SCALE, 933f * SCALE, 442.7f * SCALE) }
        texture("box2")?.let { tiled(it, 277.686f * SCALE, 158.45f * SCALE, 933f * SCALE, 442.7f * SCALE) }
        view.rows.forEachIndexed { index, row ->
            val rowY = 529.15f - index * 74f
            patch("box2")?.draw(batch, 279.686f * SCALE, rowY * SCALE, 929f * SCALE, 70f * SCALE)
            val agility = row.title == "민첩성"
            label(row.title, if (agility) 290.286f else 307.586f, rowY + 9.8f, if (agility) 107.8f else 73.2f)
            label(row.ability, 462.941f, rowY + 9.8f, 48.49f)
            label(row.phaseLabel, 1086.816f, rowY + 9.8f, 70.74f)
            patch("bg1")?.draw(batch, 572.186f * SCALE, (rowY + 20f) * SCALE, 446f * SCALE, 30f * SCALE)
            patch("box2")?.draw(batch, 574.186f * SCALE, (rowY + 20f) * SCALE, 442f * SCALE, 30f * SCALE)
            patch("mark5")?.draw(batch, 574.186f * SCALE, (rowY + 22f) * SCALE, 442f * row.progressRatio * SCALE, 26f * SCALE)
            label(row.progressLabel, 743.136f, rowY + 18.454f, 104.1f)
        }
        texture("vline")?.let { line ->
            listOf(410.859f, 555.31f, 1027.419f).forEach { x ->
                batch.draw(line, x * SCALE, 160.25f * SCALE, 6f * SCALE, 450.3f * SCALE)
            }
        }
        header(272.836f, 601.45f, 142.7f, 55.1f, "능력 이름", 269.431f, 149.51f)
        header(415.436f, 601.45f, 143.5f, 55.1f, "능력치", 435.286f, 103.8f)
        header(559.136f, 601.5f, 472.1f, 55f, "현재/업그레이드 필요 공훈", 588.216f, 413.94f)
        header(1030.886f, 601.45f, 182.6f, 55.1f, "상위 단계로 승급하는 데 필요함", 875.061f, 494.25f)
        patch("box3")?.draw(batch, 1059.386f * SCALE, 96f * SCALE, 147.6f * SCALE, 56f * SCALE)
        label("확인", 1083.186f, 104f, 100f)
        patch("box3")?.draw(batch, 904.386f * SCALE, 96f * SCALE, 147.6f * SCALE, 56f * SCALE)
        label("설명", 928.186f, 104f, 100f)
        if (view.helpOpen) {
            texture("logo9")?.let { tiled(it, 426.686f * SCALE, 252f * SCALE, 635f * SCALE, 296f * SCALE) }
            patch("box3")?.draw(batch, 426.686f * SCALE, 252f * SCALE, 635f * SCALE, 296f * SCALE)
            texture("logo3")?.let { batch.draw(it, 453.005f * SCALE, 373.951f * SCALE, 106f * SCALE, 124f * SCALE) }
            label(view.helpText, 573.686f, 335f, 463f, Align.left, true)
            patch("box3")?.draw(batch, 654.186f * SCALE, 271.285f * SCALE, 180f * SCALE, 50f * SCALE)
            label("예", 657.586f, 279.085f, 169.4f)
        }
        batch.color = Color.WHITE
    }
}

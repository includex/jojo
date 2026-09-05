package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** Pure source-order plan for Hall/scene/UnitListLayer. */
internal object HallUnitRosterRenderPlan {
    fun commands(view: HallUnitRosterView): List<HallUnitRosterDrawCommand> = buildList {
        tiled("maps/ui/start-battle/logo9.png", 924.186f, 248.3f, 360f, 409.7f)
        sprite("maps/ui/start-battle/vline.png", 1101.186f, 249.85f, 6f, 406.5f)
        patch("maps/ui/start-battle/box1.png", 924.186f, 248.3f, 360f, 409.7f)
        view.rows.take(6).forEachIndexed { index, row ->
            val y = 607f - index * 52f
            patch("maps/ui/start-battle/box2.png", 924.186f, y, 360f, 50f)
            text(row.name, 924.186f, y, 181f)
            text(row.postName, 1105.186f, y, 179f)
        }
    }

    private fun MutableList<HallUnitRosterDrawCommand>.tiled(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallUnitRosterDrawCommand(HallUnitRosterDrawKind.TILED, asset = asset, x = x, y = y, width = width, height = height))
    }

    private fun MutableList<HallUnitRosterDrawCommand>.sprite(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallUnitRosterDrawCommand(HallUnitRosterDrawKind.SPRITE, asset = asset, x = x, y = y, width = width, height = height))
    }

    private fun MutableList<HallUnitRosterDrawCommand>.patch(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallUnitRosterDrawCommand(HallUnitRosterDrawKind.PATCH, asset = asset, x = x, y = y, width = width, height = height))
    }

    private fun MutableList<HallUnitRosterDrawCommand>.text(value: String, x: Float, y: Float, width: Float) {
        add(HallUnitRosterDrawCommand(HallUnitRosterDrawKind.TEXT, text = value, x = x, y = y, width = width, align = Align.center))
    }
}

internal data class HallUnitRosterDrawCommand(
    val kind: HallUnitRosterDrawKind,
    val asset: String = "",
    val text: String = "",
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 0f,
    val align: Int = Align.center,
)

internal enum class HallUnitRosterDrawKind { TILED, SPRITE, PATCH, TEXT }

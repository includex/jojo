// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** HallTreasureRenderPlan: 거점 Treasure 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallTreasureRenderPlan {
    fun commands(view: HallTreasureView): List<HallTreasureDrawCommand> = buildList {
        tiled("maps/ui/start-battle/logo9.png", 222.9f, 72.24f, 834.2f, 543.52f)
        patch("maps/ui/start-battle/box1.png", 222.9f, 72.24f, 834.2f, 543.52f)
        patch("maps/ui/start-battle/title.png", 222.9f, 564.16f, 834.2f, 51.6f, inset = 5)
        text("보물 도감", 230.9f, 607.76f, 818.2f, HallTreasureFont.TITLE, Align.left)
        view.entries.forEachIndexed { index, entry ->
            val x = 232.10f + index % 2 * 410.22f
            val y = 413.23f - index / 2 * 165.98f
            patch("maps/ui/start-battle/box2.png", x, y, 405.06f, 163.40f)
            patch("maps/ui/start-battle/box2.png", x + 9.94f, y + 36.55f, 97.18f, 90.3f)
            if (entry.discovered) sprite("maps/item-icons/${entry.icon}.png", x + 32.7f, y + 54f, 45.5f, 42.2f)
            text(if (entry.discovered) entry.name else "발견되지 않음", x + 115.5f, y + 146.5f, 282.9f)
        }
        text(
            "지금까지 발견한 보물 ${view.discoveredCount.toString().padStart(2, '0')} / ${view.totalCount}",
            230.9f,
            109.24f,
            520f,
            align = Align.left,
        )
        patch("maps/ui/start-battle/button.png", 921.05f, 78.22f, 129.52f, 44.29f, inset = 9)
        text("종료", 921.05f, 111.5f, 129.52f)
    }

    private fun MutableList<HallTreasureDrawCommand>.tiled(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallTreasureDrawCommand(HallTreasureDrawKind.TILED, asset = asset, x = x, y = y, width = width, height = height))
    }

    private fun MutableList<HallTreasureDrawCommand>.patch(
        asset: String, x: Float, y: Float, width: Float, height: Float, inset: Int = 3,
    ) {
        add(HallTreasureDrawCommand(HallTreasureDrawKind.PATCH, asset = asset, x = x, y = y, width = width, height = height, inset = inset))
    }

    private fun MutableList<HallTreasureDrawCommand>.sprite(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallTreasureDrawCommand(HallTreasureDrawKind.SPRITE, asset = asset, x = x, y = y, width = width, height = height))
    }

    private fun MutableList<HallTreasureDrawCommand>.text(
        value: String, x: Float, y: Float, width: Float,
        font: HallTreasureFont = HallTreasureFont.BODY,
        align: Int = Align.center,
    ) {
        add(HallTreasureDrawCommand(HallTreasureDrawKind.TEXT, text = value, x = x, y = y, width = width, font = font, align = align))
    }
}

internal data class HallTreasureDrawCommand(
    val kind: HallTreasureDrawKind,
    val asset: String = "",
    val text: String = "",
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 0f,
    val inset: Int = 0,
    val font: HallTreasureFont = HallTreasureFont.BODY,
    val align: Int = Align.center,
)

internal enum class HallTreasureDrawKind { TILED, PATCH, SPRITE, TEXT }
internal enum class HallTreasureFont { TITLE, BODY }

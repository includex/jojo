// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** HallForcesRenderPlan: 거점 부대 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallForcesRenderPlan {
    private const val X = 142.49f
    private const val Y = 68.37f
    private const val WIDTH = 995.02f
    private const val HEIGHT = 551.26f
    private val columnWidths = floatArrayOf(120f, 151f, 85f, 137f, 87f, 84f, 84f, 84f, 84f, 84f)
    private val headers = listOf("무장명", "부대 속성", "레벨", "체력", "체력", "공격", "방어", "정신", "폭발", "사기")

    fun commands(view: HallForcesView): List<HallForcesDrawCommand> = buildList {
        tiled("maps/ui/start-battle/logo9.png", X, Y, WIDTH, HEIGHT)
        patch("maps/ui/start-battle/box1.png", X, Y, WIDTH, HEIGHT)
        patch("maps/ui/start-battle/title.png", X, Y + HEIGHT - 51.6f, WIDTH, 51.6f, inset = 5)
        title("부대 정보 일람", X, Y + HEIGHT - 8f, WIDTH)
        var x = X + 5f
        headers.forEachIndexed { index, value ->
            cell(x, Y + HEIGHT - 101f, columnWidths[index], 48f)
            text(value, x, Y + HEIGHT - 67f, columnWidths[index], Align.center)
            x += columnWidths[index]
        }
        view.rows.take(7).forEachIndexed { row, rowView ->
            x = X + 5f
            val y = Y + HEIGHT - 150f - row * 49f
            rowView.values.take(columnWidths.size).forEachIndexed { column, value ->
                cell(x, y, columnWidths[column], 49f)
                text(value, x + 3f, y + 36f, columnWidths[column] - 6f, if (column >= 2) Align.center else Align.left)
                x += columnWidths[column]
            }
        }
        patch("maps/ui/start-battle/button.png", X + WIDTH - 164f, Y + 5f, 155f, 51.6f, inset = 9)
        text("폐쇄", X + WIDTH - 164f, Y + 40f, 155f, Align.center)
    }

    private fun MutableList<HallForcesDrawCommand>.tiled(asset: String, x: Float, y: Float, width: Float, height: Float) =
        add(HallForcesDrawCommand(HallForcesDrawKind.TILED, asset = asset, x = x, y = y, width = width, height = height))

    private fun MutableList<HallForcesDrawCommand>.cell(x: Float, y: Float, width: Float, height: Float) =
        patch("maps/ui/start-battle/box2.png", x, y, width, height)

    private fun MutableList<HallForcesDrawCommand>.patch(
        asset: String, x: Float, y: Float, width: Float, height: Float, inset: Int = 3,
    ) = add(HallForcesDrawCommand(HallForcesDrawKind.PATCH, asset = asset, x = x, y = y, width = width, height = height, inset = inset))

    private fun MutableList<HallForcesDrawCommand>.title(value: String, x: Float, y: Float, width: Float) =
        add(HallForcesDrawCommand(HallForcesDrawKind.TITLE, text = value, x = x, y = y, width = width))

    private fun MutableList<HallForcesDrawCommand>.text(value: String, x: Float, y: Float, width: Float, align: Int) =
        add(HallForcesDrawCommand(HallForcesDrawKind.TEXT, text = value, x = x, y = y, width = width, align = align))
}

internal data class HallForcesDrawCommand(
    val kind: HallForcesDrawKind,
    val asset: String = "",
    val text: String = "",
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 0f,
    val inset: Int = 0,
    val align: Int = Align.left,
)

internal enum class HallForcesDrawKind { TILED, PATCH, TITLE, TEXT }

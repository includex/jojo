// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** HallTerrainRenderPlan: 거점 지형 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallTerrainRenderPlan {
    /**
     * `X` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val X = 235.84f
    /**
     * `Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val Y = 86f
    /**
     * `WIDTH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val WIDTH = 878.15f
    /**
     * `HEIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val HEIGHT = 516f
    /**
     * `COLUMN_WIDTH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val COLUMN_WIDTH = 51.6f

    /**
     * `commands`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun commands(view: HallTerrainView): List<HallTerrainDrawCommand> = buildList {
        tiled("maps/ui/start-battle/logo9.png", X, Y, WIDTH, HEIGHT)
        patch("maps/ui/start-battle/box1.png", X, Y, WIDTH, HEIGHT)
        patch("maps/ui/start-battle/title.png", X, Y + HEIGHT - 51.6f, WIDTH, 51.6f, inset = 5)
        text("지형 정보 일람", X + 8f, Y + HEIGHT - 8f, WIDTH - 16f, font = HallTerrainFont.TITLE, align = Align.left)

        val rowX = X + 13.16f
        val leftWidth = 264.02f
        patch("maps/ui/start-battle/box2.png", rowX, Y + HEIGHT - 87f, leftWidth, 42f)
        text("이름", rowX, Y + HEIGHT - 57f, leftWidth, font = HallTerrainFont.SMALL)
        headers.forEachIndexed { index, value ->
            val headerX = X + 201.12f + index * COLUMN_WIDTH
            patch("maps/ui/start-battle/box2.png", headerX, Y + HEIGHT - 87f, COLUMN_WIDTH, 42f)
            text(value, headerX, Y + HEIGHT - 57f, COLUMN_WIDTH, font = HallTerrainFont.SMALL)
        }

        view.rows.forEachIndexed { row, terrain ->
            val rowY = Y + HEIGHT - 148.44f - row * 64.5f
            patch("maps/ui/terrain-layer/row-${if (row % 2 == 0) "even" else "odd"}.png", rowX, rowY, 854.07f, 64.5f, 1)
            sprite("maps/terrain-icons/${terrain.iconIndex}.png", X + 15f, rowY + 3f, 58.48f, 58.48f)
            text(terrain.name, X + 88f, rowY + 43f, 108f, align = Align.left)
            terrain.enabledSkills.forEachIndexed { index, enabled ->
                sprite(
                    "maps/ui/terrain-layer/skill${index + 1}${if (enabled) "" else "-disabled"}.png",
                    X + 81.58f + index * 28.38f,
                    rowY + 5.59f,
                    25.8f,
                    25.8f,
                )
            }
            terrain.values.forEachIndexed { column, value ->
                text(value, X + 201.12f + column * COLUMN_WIDTH, rowY + 48f, COLUMN_WIDTH, color = value.color())
            }
        }

        button("지형 효과", 245.48f, 95.46f, 169.16f)
        button("기동력 소모", 422.64f, 95.46f, 191.52f)
        button("확인", 1001.72f, 95.46f, 103.2f)
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallTerrainDrawCommand>.tiled(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallTerrainDrawCommand(HallTerrainDrawKind.TILED, asset, x = x, y = y, width = width, height = height))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallTerrainDrawCommand>.patch(
        asset: String, x: Float, y: Float, width: Float, height: Float, inset: Int = 3,
    ) {
        add(HallTerrainDrawCommand(HallTerrainDrawKind.PATCH, asset, x = x, y = y, width = width, height = height, inset = inset))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallTerrainDrawCommand>.sprite(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallTerrainDrawCommand(HallTerrainDrawKind.SPRITE, asset, x = x, y = y, width = width, height = height))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallTerrainDrawCommand>.text(
        value: String, x: Float, y: Float, width: Float,
        font: HallTerrainFont = HallTerrainFont.BODY,
        color: HallTerrainTextColor = HallTerrainTextColor.BLACK,
        align: Int = Align.center,
    ) {
        add(HallTerrainDrawCommand(HallTerrainDrawKind.TEXT, text = value, x = x, y = y, width = width, font = font, color = color, align = align))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallTerrainDrawCommand>.button(value: String, x: Float, y: Float, width: Float) {
        patch("maps/ui/start-battle/button.png", x, y, width, 51.6f, inset = 9)
        text(value, x, y + 35f, width)
    }

    /**
     * `String`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun String.color(): HallTerrainTextColor = when (this) {
        "★", "◎" -> HallTerrainTextColor.ORANGE
        "○" -> HallTerrainTextColor.GREEN
        else -> HallTerrainTextColor.BLACK
    }

    /**
     * `headers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val headers = listOf("마왕", "보병", "기병", "궁기", "포차", "무술", "군주", "보병", "기병", "궁기", "포차", "무술", "무술")
}

/**
 * `HallTerrainDrawCommand`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallTerrainDrawCommand(
    val kind: HallTerrainDrawKind,
    val asset: String = "",
    val text: String = "",
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 0f,
    val inset: Int = 0,
    val font: HallTerrainFont = HallTerrainFont.BODY,
    val color: HallTerrainTextColor = HallTerrainTextColor.BLACK,
    val align: Int = Align.center,
)

/**
 * `HallTerrainDrawKind`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallTerrainDrawKind { TILED, PATCH, SPRITE, TEXT }
/**
 * `HallTerrainFont`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallTerrainFont { TITLE, BODY, SMALL }
/**
 * `HallTerrainTextColor`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallTerrainTextColor { BLACK, ORANGE, GREEN }

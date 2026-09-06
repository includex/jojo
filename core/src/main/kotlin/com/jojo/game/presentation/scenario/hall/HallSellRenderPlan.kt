// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** HallSellRenderPlan: 거점 Sell 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallSellRenderPlan {
    /**
     * `X` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val X = 267.84f
    /**
     * `Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val Y = 65.36f
    /**
     * `WIDTH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val WIDTH = 744.76f
    /**
     * `HEIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val HEIGHT = 557.28f

    /**
     * `commands`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun commands(view: HallSellView): List<HallSellDrawCommand> = buildList {
        tiled("maps/ui/start-battle/logo9.png", X, Y, WIDTH, HEIGHT)
        patch("maps/ui/start-battle/box1.png", X, Y, WIDTH, HEIGHT)
        patch("maps/ui/start-battle/title.png", X, Y + HEIGHT - 43f, WIDTH, 43f, inset = 5)
        text("판매하기", X, Y + HEIGHT - 5f, WIDTH, font = HallSellFont.TITLE)
        text("창고 목록", X + 25f, Y + HEIGHT - 66f, 240f, align = Align.left)
        patch("maps/ui/start-battle/box1.png", X + 8f, Y + 99f, WIDTH - 16f, HEIGHT - 172f)
        view.rows.forEachIndexed { index, row ->
            val cardX = X + 9f + index % 2 * 360f
            val cardY = Y + HEIGHT - 283f - index / 2 * 157f
            patch("maps/ui/start-battle/box1.png", cardX, cardY, 359.48f, 154.8f)
            patch("maps/ui/start-battle/box1.png", cardX + 8f, cardY + 39f, 77.4f, 77.4f)
            sprite("maps/item-icons/${row.icon}.png", cardX + 14f, cardY + 45f, 65f, 65f)
            text(row.name, cardX + 94f, cardY + 128f, 172f, align = Align.left)
            text(row.primaryDetail, cardX + 94f, cardY + 82f, if (row.secondaryDetail == null) 230f else 150f, align = Align.left)
            row.secondaryDetail?.let { text(it, cardX + 220f, cardY + 82f, 130f, align = Align.left) }
            text("판매가: ${row.salePrice}", cardX + 94f, cardY + 37f, 250f, align = Align.left)
        }
        text("현금", X + 20f, Y + 31f, 240f, align = Align.left)
        text(view.money.toString(), X + 112f, Y + 31f, 150f)
        button("무기점", 522.98f, 75.28f, 172f)
        button("상점", 694.98f, 75.28f, 172f)
        button("종료", 870.47f, 75.14f, 129f)
        view.notice?.let { text(it, X + 18f, Y + HEIGHT - 52f, WIDTH - 36f, align = Align.right, color = HallSellTextColor.NOTICE) }
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallSellDrawCommand>.tiled(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallSellDrawCommand(HallSellDrawKind.TILED, asset = asset, x = x, y = y, width = width, height = height))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallSellDrawCommand>.patch(
        asset: String, x: Float, y: Float, width: Float, height: Float, inset: Int = 3,
    ) {
        add(HallSellDrawCommand(HallSellDrawKind.PATCH, asset = asset, x = x, y = y, width = width, height = height, inset = inset))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallSellDrawCommand>.sprite(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallSellDrawCommand(HallSellDrawKind.SPRITE, asset = asset, x = x, y = y, width = width, height = height))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallSellDrawCommand>.text(
        value: String, x: Float, y: Float, width: Float,
        font: HallSellFont = HallSellFont.BODY,
        align: Int = Align.center,
        color: HallSellTextColor = HallSellTextColor.BLACK,
    ) {
        add(HallSellDrawCommand(HallSellDrawKind.TEXT, text = value, x = x, y = y, width = width, font = font, align = align, color = color))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallSellDrawCommand>.button(value: String, x: Float, y: Float, width: Float) {
        patch("maps/ui/start-battle/button.png", x, y, width, 43f, inset = 9)
        text(value, x, y + 31f, width)
    }
}

/**
 * `HallSellDrawCommand`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallSellDrawCommand(
    val kind: HallSellDrawKind,
    val asset: String = "",
    val text: String = "",
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 0f,
    val inset: Int = 0,
    val font: HallSellFont = HallSellFont.BODY,
    val align: Int = Align.center,
    val color: HallSellTextColor = HallSellTextColor.BLACK,
)

/**
 * `HallSellDrawKind`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallSellDrawKind { TILED, PATCH, SPRITE, TEXT }
/**
 * `HallSellFont`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallSellFont { TITLE, BODY }
/**
 * `HallSellTextColor`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallSellTextColor { BLACK, NOTICE }

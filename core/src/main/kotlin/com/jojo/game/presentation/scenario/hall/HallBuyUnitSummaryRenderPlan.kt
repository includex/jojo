// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** HallBuyUnitSummaryRenderPlan: 거점 Buy 유닛 Summary 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallBuyUnitSummaryRenderPlan {
    /**
     * `X` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val X = 701.77f
    /**
     * `Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val Y = 132.81f

    /**
     * `commands`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.stat(name: String, value: Int, x: Float, y: Float) {
        text(name, x, y, 240f, Align.left)
        patch("maps/ui/start-battle/box1.png", x + 105f, y - 31f, 72f, 43f)
        text(value.toString(), x + 105f, y, 72f)
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.portrait(
        id: Int, x: Float, y: Float, width: Float, height: Float,
    ) = add(HallBuyUnitSummaryDrawCommand(HallBuyUnitSummaryDrawKind.PORTRAIT, portraitId = id, x = x, y = y, width = width, height = height))

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.patch(asset: String, x: Float, y: Float, width: Float, height: Float) =
        add(HallBuyUnitSummaryDrawCommand(HallBuyUnitSummaryDrawKind.PATCH, asset = asset, x = x, y = y, width = width, height = height))

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.sprite(
        asset: String, x: Float, y: Float, width: Float, height: Float, tint: HallBuyUnitSummaryTint,
    ) = add(HallBuyUnitSummaryDrawCommand(HallBuyUnitSummaryDrawKind.SPRITE, asset = asset, x = x, y = y, width = width, height = height, tint = tint))

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallBuyUnitSummaryDrawCommand>.text(value: String, x: Float, y: Float, width: Float, align: Int = Align.center) =
        add(HallBuyUnitSummaryDrawCommand(HallBuyUnitSummaryDrawKind.TEXT, text = value, x = x, y = y, width = width, align = align))
}

/**
 * `HallBuyUnitSummaryDrawCommand`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

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

/**
 * `HallBuyUnitSummaryDrawKind`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallBuyUnitSummaryDrawKind { PORTRAIT, PATCH, SPRITE, TEXT }

/**
 * `HallBuyUnitSummaryTint`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallBuyUnitSummaryTint { WHITE, MUTED }

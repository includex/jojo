// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.utils.Align

/** HallUnitRosterRenderPlan: 거점 유닛 명단 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallUnitRosterRenderPlan {
    /**
     * `commands`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallUnitRosterDrawCommand>.tiled(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallUnitRosterDrawCommand(HallUnitRosterDrawKind.TILED, asset = asset, x = x, y = y, width = width, height = height))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallUnitRosterDrawCommand>.sprite(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallUnitRosterDrawCommand(HallUnitRosterDrawKind.SPRITE, asset = asset, x = x, y = y, width = width, height = height))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallUnitRosterDrawCommand>.patch(asset: String, x: Float, y: Float, width: Float, height: Float) {
        add(HallUnitRosterDrawCommand(HallUnitRosterDrawKind.PATCH, asset = asset, x = x, y = y, width = width, height = height))
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<HallUnitRosterDrawCommand>.text(value: String, x: Float, y: Float, width: Float) {
        add(HallUnitRosterDrawCommand(HallUnitRosterDrawKind.TEXT, text = value, x = x, y = y, width = width, align = Align.center))
    }
}

/**
 * `HallUnitRosterDrawCommand`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

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

/**
 * `HallUnitRosterDrawKind`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallUnitRosterDrawKind { TILED, SPRITE, PATCH, TEXT }

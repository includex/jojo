// Game
package com.jojo.game.presentation.shared.overlay

/** TerrainLayerChromeRenderContract: TerrainLayer 표 본문을 둘러싼 원본 프리팹 장식 영역이다. 실제 TerrainLayer-open 자료의 좌표를 보존하며 입력과 지형 자료에서 독립적으로 렌더링할 수 있다. */

object TerrainLayerChromeRenderContract {

    /**
     * `Patch`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Patch(
        /**
         * `path` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val path: String,
        /**
         * `x` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Float,
        /**
         * `y` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Float,
        /**
         * `width` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val width: Float,
        /**
         * `height` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val height: Float,
        /**
         * `capInset` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val capInset: Int,
    )

    /**
     * `PANEL_X` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_X = 274.236f
    /**
     * `PANEL_Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_Y = 100f
    /**
     * `PANEL_WIDTH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_WIDTH = 1021.1f
    /**
     * `PANEL_HEIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_HEIGHT = 600f

    /**
     * `outerBox` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val outerBox = Patch("maps/ui/terrain-layer/outer-box.png", PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, 3)
    /**
     * `titleStrip` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val titleStrip = Patch("maps/ui/terrain-layer/title-strip.png", PANEL_X, 650f, PANEL_WIDTH, 50f, 5)

    /** 지형 창 배경 요소의 표시 순서를 나타낸다. */
    fun chrome(): List<Patch> = listOf(outerBox, titleStrip)
}

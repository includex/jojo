// Game
package com.jojo.game.presentation.shared.overlay

/** TerrainLayerChromeRenderContract: TerrainLayer 표 본문을 둘러싼 원본 프리팹 장식 영역이다. 실제 TerrainLayer-open 자료의 좌표를 보존하며 입력과 지형 자료에서 독립적으로 렌더링할 수 있다. */

object TerrainLayerChromeRenderContract {

    data class Patch(
        val path: String,
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val capInset: Int,
    )

    const val PANEL_X = 274.236f
    const val PANEL_Y = 100f
    const val PANEL_WIDTH = 1021.1f
    const val PANEL_HEIGHT = 600f

    val outerBox = Patch("maps/ui/terrain-layer/outer-box.png", PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, 3)
    val titleStrip = Patch("maps/ui/terrain-layer/title-strip.png", PANEL_X, 650f, PANEL_WIDTH, 50f, 5)

    /** 지형 창 배경 요소의 표시 순서를 나타낸다. */
    fun chrome(): List<Patch> = listOf(outerBox, titleStrip)
}

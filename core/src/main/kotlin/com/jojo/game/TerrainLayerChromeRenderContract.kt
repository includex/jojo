package com.jojo.game

/**
 * Source-prefab chrome which surrounds TerrainLayer's box4 table body.
 *
 * The values are from the live `TerrainLayer-open` fixture, not inferred from
 * the game's former rounded panel coordinates.  This stays independent of
 * BattleScreen so the production renderer can adopt it without changing input
 * or terrain data behaviour.
 */
/**
 * object  `TerrainLayerChromeRenderContract`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object TerrainLayerChromeRenderContract {
    /**
     * data class  `Patch`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /** Cocos child order: tiled Logo_9, outer box1, then title bg1. */
    fun chrome(): List<Patch> = listOf(outerBox, titleStrip)
}

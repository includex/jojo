package com.jojo.game

/** Source BattleScreen._touchProcess map-local coordinate conversion. */
object BattleTileInput {
    /**
     * data class  `Tile`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Tile(val x: Int, val y: Int)

    fun tileAt(
        worldX: Float,
        worldY: Float,
        boardLeft: Float,
        boardBottom: Float,
        tileSize: Float,
        mapTilesHigh: Int,
    ): Tile {
        require(tileSize > 0f)
        require(mapTilesHigh > 0)
        val mapBottom = boardBottom - (mapTilesHigh - 1) * tileSize
        return Tile(
            // Math.trunc(u / tileSize) in the recovered source.
            x = ((worldX - boardLeft) / tileSize).toInt(),
            // Preserve the source operation order exactly:
            //   p = Math.trunc((worldY - mapBottom) / tileSize)
            //   y = mapH - (p + 1)
            // Rewriting this as floor(boardBottom + tileSize - worldY)
            // changes ownership of every exact horizontal tile boundary.
            y = mapTilesHigh - 1 - ((worldY - mapBottom) / tileSize).toInt(),
        )
    }
}

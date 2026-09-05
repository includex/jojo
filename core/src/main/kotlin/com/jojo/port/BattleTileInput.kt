package com.jojo.port

/** Source BattleLayer._touchProcess map-local coordinate conversion. */
object BattleTileInput {
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

package com.jojo.game.presentation.battle.input

/** 화면 좌표를 전투 맵 타일 좌표로 변환합니다. */
object BattleTileInput {
    /** 맵에서 선택된 타일 좌표입니다. */
    data class Tile(val x: Int, val y: Int)

    /** 월드 좌표와 맵 배치 정보로 타일을 계산합니다. */
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
            // 원본은 타일 크기로 나눈 뒤 소수부를 버립니다.
            x = ((worldX - boardLeft) / tileSize).toInt(),
            // 원본 연산 순서를 유지해야 경계선 타일의 소유가 달라지지 않습니다.
            y = mapTilesHigh - 1 - ((worldY - mapBottom) / tileSize).toInt(),
        )
    }
}

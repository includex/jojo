package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattleTileInputTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleTileInputTest {
    @Test fun `rendered unit centre maps back to its source tile`() {
        val boardLeft = -320f - 111.860474f
        val boardBottom = 1728f - 928f
        val tileSize = 96f
        val caoX = 10
        val caoY = 5
        val renderedCentreX = boardLeft + caoX * tileSize + tileSize / 2f
        val renderedCentreY = boardBottom - caoY * tileSize + tileSize / 2f

        assertEquals(
            BattleTileInput.Tile(caoX, caoY),
            BattleTileInput.tileAt(renderedCentreX, renderedCentreY, boardLeft, boardBottom, tileSize, 20),
        )
    }

    @Test fun `camera translation does not change selected tile`() {
        val tileSize = 96f
        listOf(0f to 0f, -111.860474f to -928f, 250f to -340f).forEach { (cameraX, cameraY) ->
            val left = -320f + cameraX
            val bottom = 1728f + cameraY
            val centreX = left + 8 * tileSize + tileSize / 2f
            val centreY = bottom - 2 * tileSize + tileSize / 2f
            assertEquals(
                BattleTileInput.Tile(8, 2),
                BattleTileInput.tileAt(centreX, centreY, left, bottom, tileSize, 20),
            )
        }
    }

    @Test fun `adjacent rendered rows map to distinct source rows`() {
        val left = -320f
        val bottom = 1728f
        val size = 96f
        assertEquals(BattleTileInput.Tile(7, 19), BattleTileInput.tileAt(400f, -48f, left, bottom, size, 24))
        assertEquals(BattleTileInput.Tile(7, 20), BattleTileInput.tileAt(400f, -144f, left, bottom, size, 24))
    }

    @Test fun `exact horizontal boundaries preserve source trunc row ownership`() {
        val size = 96f
        val left = SourceBattleMapGeometry.boardLeft(20, 0f)
        val bottom = SourceBattleMapGeometry.boardBottom(20, 0f)

        assertEquals(BattleTileInput.Tile(0, 0), BattleTileInput.tileAt(left, bottom, left, bottom, size, 20))
        assertEquals(BattleTileInput.Tile(0, 1), BattleTileInput.tileAt(left, bottom - size, left, bottom, size, 20))
        assertEquals(BattleTileInput.Tile(0, 19), BattleTileInput.tileAt(left, -96f, left, bottom, size, 20))
        assertEquals(BattleTileInput.Tile(0, -1), BattleTileInput.tileAt(left, 1824f, left, bottom, size, 20))
    }

    @Test fun `S00 S52 and S57 rendered centres round trip through their actual heights`() {
        listOf(20 to 20, 20 to 24, 40 to 40).forEach { (width, height) ->
            val left = SourceBattleMapGeometry.boardLeft(width, 173f)
            val bottom = SourceBattleMapGeometry.boardBottom(height, -211f)
            listOf(0 to 0, width / 2 to height / 2, width - 1 to height - 1).forEach { (x, y) ->
                val (worldX, worldY) = SourceBattleMapGeometry.tileCenter(x.toFloat(), y.toFloat(), width, height, 173f, -211f)
                assertEquals(
                    BattleTileInput.Tile(x, y),
                    BattleTileInput.tileAt(worldX, worldY, left, bottom, 96f, height),
                    "$width x $height tile ($x,$y)",
                )
            }
        }
    }
}

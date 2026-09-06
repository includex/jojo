// Test
package com.jojo.game

import com.jojo.game.presentation.battle.render.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleMapRendererTest {
    @Test
    fun `overlay command order preserves source layers and frame names`() {
        val view = BattleMapView(
            boardLeft = 10f,
            boardBottom = 100f,
            tileSize = 96f,
            selectionTiles = listOf(
                BattleMapSelection(2, 3, "range-blue"),
                BattleMapSelection(4, 3, "range-red-box"),
            ),
            cursor = BattleMapCursor(1, 2),
            terrainImpacts = listOf(BattleMapTerrainImpact(2, 3, 80)),
            harmNumbers = listOf(BattleMapHarmNumber(2f, 3f, -12, isHp = true)),
        )

        assertEquals(
            listOf(
                BattleMapRenderEvent(BattleMapRenderLayer.SELECTION, 2f, 3f, frame = "range-blue"),
                BattleMapRenderEvent(BattleMapRenderLayer.SELECTION, 4f, 3f, frame = "range-red-box"),
                BattleMapRenderEvent(BattleMapRenderLayer.CURSOR, 1f, 2f),
                BattleMapRenderEvent(BattleMapRenderLayer.TERRAIN_IMPACT, 2f, 3f, text = "80"),
                BattleMapRenderEvent(BattleMapRenderLayer.HARM, 2f, 3f, text = "12"),
            ),
            BattleMapRenderer.orderedEvents(view),
        )
    }
}

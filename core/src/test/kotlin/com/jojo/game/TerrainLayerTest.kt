package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class TerrainLayerTest {
    private val terrain = (0..28).map { id ->
        TerrainLayer.Terrain(id, "T$id", flag = if (id == 0) 0b0101 else 0, magic = if (id == 0) 0b1010 else 0)
    }
    private val arms = (0..13).map { id ->
        TerrainLayer.Arm(id, "A$id", terrainRise = mapOf(0 to when (id) {
            0 -> 80; 1 -> 100; 2 -> 110; else -> 131
        }), terrainExpend = mapOf(0 to when (id) {
            0 -> 1; 1 -> 5; 2 -> 201; else -> 7
        }))
    }

    @Test fun `rise panel follows 28 terrain and 13 arm source limits with original grades`() {
        val layer = TerrainLayer(terrain, arms)
        val panel = layer.select(TerrainLayer.Tab.RISE)
        assertEquals(28, panel.rows.size)
        val row = panel.rows.first()
        assertEquals(0, row.iconIndex)
        assertEquals((0..27).toList(), panel.rows.map { it.iconIndex })
        assertEquals(listOf(true, false, true, false), row.enabledSkills)
        assertEquals(13, row.values.size)
        assertEquals(listOf("★", "◎", "○", "--"), row.values.take(4).map { it.text })
        assertEquals(listOf(0, 1, 2, 5), row.values.take(4).map { it.grade })
    }

    @Test fun `expend panel uses magic bits and over 200 becomes dash`() {
        val row = TerrainLayer(terrain, arms).select(TerrainLayer.Tab.EXPEND).rows.first()
        assertEquals(listOf(false, true, false, true), row.enabledSkills)
        assertEquals(listOf("1", "5", "--", "--"), row.values.take(4).map { it.text })
    }

    @Test fun `tabs initialize lazily and are cached like source flag`() {
        val layer = TerrainLayer(terrain, arms)
        assertFalse(layer.isInitialized(TerrainLayer.Tab.RISE))
        val first = layer.select(TerrainLayer.Tab.RISE)
        assertTrue(layer.isInitialized(TerrainLayer.Tab.RISE))
        assertFalse(layer.isInitialized(TerrainLayer.Tab.EXPEND))
        assertSame(first, layer.select(TerrainLayer.Tab.RISE))
        layer.select(TerrainLayer.Tab.EXPEND)
        assertTrue(layer.isInitialized(TerrainLayer.Tab.EXPEND))
    }

    @Test fun `original data builds full terrain layer source feed`() {
        val layer = GameDataCatalog.load().terrainLayer()
        val rise = layer.select(TerrainLayer.Tab.RISE)
        val expend = layer.select(TerrainLayer.Tab.EXPEND)
        assertEquals(28, rise.rows.size)
        assertEquals(13, rise.rows.first().values.size)
        assertEquals(rise.rows.map { it.terrainId }, expend.rows.map { it.terrainId })
    }

    @Test fun `prefab buttons route rise expend close and consume modal body`() {
        assertEquals(TerrainLayerInput.Action.Rise, TerrainLayerInput.tap(300f, 140f))
        assertEquals(TerrainLayerInput.Action.Expend, TerrainLayerInput.tap(550f, 140f))
        assertEquals(TerrainLayerInput.Action.Close, TerrainLayerInput.tap(1200f, 140f))
        assertEquals(TerrainLayerInput.Action.Consume, TerrainLayerInput.tap(500f, 400f))
        assertEquals(null, TerrainLayerInput.tap(20f, 20f))
    }

    @Test fun `spriteframe icon layout keeps first and alternating prefab row positions`() {
        assertEquals(291f, TerrainLayerSpriteLayout.ICON_X)
        assertEquals(531f, TerrainLayerSpriteLayout.iconY(0))
        assertEquals(456f, TerrainLayerSpriteLayout.iconY(1))
        assertEquals(67f, TerrainLayerSpriteLayout.ICON_SIZE)
    }

    @Test fun `actual route draw inventory includes modal mask clipped ninth row and controls`() {
        val events = TerrainLayerRenderEvents.jsonl(GameDataCatalog.load().terrainLayer()).lineSequence().filter { it.isNotBlank() }.toList()
        assertEquals(216, events.size)
        assertTrue(events.first().contains("Canvas/Layer/Panel_cancel"))
        assertTrue(events.any { it.contains("scrollview0/view/content/item0") && it.contains("\"y\":-72.602") })
        assertTrue(events.any { it.contains("\"text\":\"다리\"") })
        assertTrue(events.last().contains("Canvas/Layer/bg/button2/Background/Label"))
        assertTrue(events.last().contains("\"text\":\"확인\"") )
    }

    @Test fun `terrain chrome preserves the live source atlas paths cap insets and fractional origin`() {
        assertEquals(
            listOf(
                TerrainLayerChromeRenderContract.Patch("maps/ui/terrain-layer/outer-box.png", 274.236f, 100f, 1021.1f, 600f, 3),
                TerrainLayerChromeRenderContract.Patch("maps/ui/terrain-layer/title-strip.png", 274.236f, 650f, 1021.1f, 50f, 5),
            ),
            TerrainLayerChromeRenderContract.chrome(),
        )
    }
}

package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FeatsLayerTest {
    private fun layer() = FeatsLayer(FeatsLayer.TITLES.mapIndexed { index, title ->
        FeatsLayer.Row(title, 11 + index, 7 + index, 14 + index, if (index == 4) 0 else 2 + index)
    })

    @Test fun `rows expose source progress and max labels`() {
        val rows = layer().view().rows
        assertEquals("7/14", rows.first().progressLabel)
        assertEquals(.5f, rows.first().progressRatio)
        assertEquals("MAX", rows.last().phaseLabel)
    }

    @Test fun `help and close only respond to touch end`() {
        val layer = layer()
        assertFalse(layer.onButton(1, 1))
        assertNull(layer.consumeRoute())
        assertTrue(layer.onButton(1, FeatsLayer.TOUCH_END))
        assertEquals(FeatsLayer.Route.HELP, layer.consumeRoute())
        assertTrue(layer.onCancel(FeatsLayer.TOUCH_END))
        assertFalse(layer.attached)
    }

    @Test fun `enabled actual unit info button routes to feats then help and close`() {
        val unit = UnitInfoLayer.Unit(0, "조조", "군주", 1, 100, 100, 30, 30, 41, 49, 46, 40, 42)
        val unitInfo = UnitInfoLayer(listOf(unit), featsEnabled = true)
        unitInfo.onCreate()
        assertTrue(unitInfo.onButton(8, UnitInfoLayer.TOUCH_END))
        assertEquals(UnitInfoLayer.Route.FEATS, unitInfo.takeRoutes().single().route)

        val feats = FeatsLayer(FeatsLayer.TITLES.mapIndexed { index, title ->
            FeatsLayer.Row(title, listOf(41, 49, 46, 40, 42)[index], 0, 100, 127)
        })
        assertTrue(feats.onButton(1, FeatsLayer.TOUCH_END))
        assertEquals(FeatsLayer.Route.HELP, feats.consumeRoute())
        assertTrue(feats.onButton(0, FeatsLayer.TOUCH_END))
        assertFalse(feats.attached)
    }
}

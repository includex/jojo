// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.hall.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** HallUnitListLayerTest: HallUnitListLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class HallUnitListLayerTest {
    @Test
    fun `row selection is source ordered and closes the layer`() {
        val layer = HallUnitListLayer(listOf(181, 0, 157))
        assertEquals(listOf(0, 157, 181), layer.rows)
        assertNull(layer.onRow(1, 1))
        assertEquals(157, layer.onRow(1, HallUnitListLayer.TOUCH_END))
        assertEquals(157, layer.selectedUnitId)
        assertFalse(layer.attached)
    }

    @Test
    fun `cancel only closes on touch end and does not select`() {
        val layer = HallUnitListLayer(listOf(0, 157))
        assertFalse(layer.onCancel(1))
        assertTrue(layer.attached)
        assertTrue(layer.onCancel(HallUnitListLayer.TOUCH_END))
        assertFalse(layer.attached)
        assertNull(layer.onRow(0, HallUnitListLayer.TOUCH_END))
    }
}

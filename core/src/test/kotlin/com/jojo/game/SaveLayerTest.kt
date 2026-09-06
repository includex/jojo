// Test
package com.jojo.game
import com.jojo.game.presentation.shared.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** SaveLayerTest: SaveLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class SaveLayerTest {
    private class Repository(private val values: Map<Int, String> = emptyMap()) : SaveLayer.Repository {
        val saved = mutableListOf<Int>()
        override fun load(index: Int) = values[index]
        override fun save(index: Int) { saved += index }
    }

    @Test fun `source first save page has 22 slots then later pages have 20 offset by two`() {
        val layer = SaveLayer(Repository()).also { it.onCreate(savedPage = 0) }
        assertEquals((0..21).toList(), layer.view().rows.map { it.index }.sorted())
        assertEquals((22..41).toList(), layer.refPage(1).rows.map { it.index }.sorted())
    }

    @Test fun `rows sort by time but retain original slot number and model game stage`() {
        val repo = Repository(mapOf(
            1 to """{"time":9,"name":"후기","model":{"game":{"stage":6}}}""",
            4 to """{"time":2,"name":"초기","model":{"property2":[0,3]}}""",
        ))
        val layer = SaveLayer(repo).also { it.onCreate() }
        val early = layer.view().rows.first { it.index == 4 }
        assertEquals("No.  5", early.number); assertEquals("전역2", early.stage)
        assertEquals("진행 상황 저장 안 함", layer.view().rows.first { !it.occupied }.name)
        assertTrue(layer.view().rows.indexOf(early) < layer.view().rows.indexOfFirst { it.index == 1 })
    }

    @Test fun `only touch end plus confirm saves then callback removes layer while cancel does not save`() {
        val repo = Repository(); var callbacks = 0
        val layer = SaveLayer(repo).also { it.onCreate({ callbacks++ }, showCompleteTip = false) }
        assertFalse(layer.onRowTouch(0, 1)); assertNull(layer.pendingSlot())
        assertTrue(layer.onRowTouch(0, SaveLayer.TOUCH_END)); assertEquals(0, layer.pendingSlot())
        assertFalse(layer.onConfirm(1)); assertTrue(layer.view().attached)
        assertTrue(layer.onRowTouch(0, SaveLayer.TOUCH_END)); assertTrue(layer.onConfirm(0))
        assertEquals(listOf(0), repo.saved); assertEquals(1, callbacks); assertFalse(layer.view().attached)
    }

    @Test fun `default completion tip delays callback and removal until its own touch end`() {
        val repo = Repository(); var callbacks = 0
        val layer = SaveLayer(repo).also { it.onCreate({ callbacks++ }) }
        layer.onRowTouch(3, SaveLayer.TOUCH_END); assertTrue(layer.onConfirm(0))
        assertEquals(listOf(3), repo.saved); assertTrue(layer.completionTipOpen()); assertTrue(layer.view().attached)
        assertTrue(layer.onCompletionTip(SaveLayer.TOUCH_END))
        assertEquals(1, callbacks); assertFalse(layer.view().attached)
    }
}

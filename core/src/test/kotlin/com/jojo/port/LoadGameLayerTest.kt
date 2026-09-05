package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoadGameLayerTest {
    private class Repo(private val slots: Map<Int, String?> = emptyMap()) : LoadGameLayer.Repository {
        var page = 0; var restored: Int? = null; var route: LoadGameLayer.RestoreRoute? = null
        override fun load(index: Int) = slots[index]
        override fun savedPage() = page
        override fun savePage(page: Int) { this.page = page }
        override fun featureEnabled(name: String) = name == "ZDBHSW"
        override fun versionCode() = 4
        override fun restore(index: Int, raw: String, route: LoadGameLayer.RestoreRoute): Boolean { restored = index; this.route = route; return true }
    }
    private fun save(time: Long, name: String, version: Int = 1, stage: Int = 4) =
        "{\"time\":$time,\"name\":\"$name\",\"model\":{\"version\":$version,\"property2\":[0,$stage]}}"

    @Test fun `source page zero has 22 rows and newest first`() {
        val layer = LoadGameLayer(Repo(mapOf(0 to save(4, "old"), 3 to save(9, "new"))))
        val view = layer.onCreate()
        assertEquals(22, view.rows.size); assertEquals(3, view.rows.first().index)
        assertEquals("전역3", view.rows.first().stage); assertTrue(view.pageTogglesVisible)
    }
    @Test fun `later pages start at source offset plus two`() {
        val layer = LoadGameLayer(Repo()); layer.onCreate(); val view = layer.refPage(2)
        assertEquals(42, view.rows.first().index); assertEquals(61, view.rows.last().index)
    }
    @Test fun `touch end confirm is required before manager restore`() {
        val repo = Repo(mapOf(5 to save(1, "조조"))); val layer = LoadGameLayer(repo); layer.onCreate()
        assertFalse(layer.onRowTouch(5, 0)); assertTrue(layer.onRowTouch(5, LoadGameLayer.TOUCH_END))
        assertEquals(5, layer.view().confirmation?.index); assertFalse(layer.onConfirm(1)); assertEquals(null, repo.restored)
        assertTrue(layer.onRowTouch(5, LoadGameLayer.TOUCH_END)); assertTrue(layer.onConfirm(0)); assertEquals(5, repo.restored)
    }
    @Test fun `newer source model is rejected`() {
        val repo = Repo(mapOf(0 to save(1, "future", version = 5))); val layer = LoadGameLayer(repo); layer.onCreate()
        layer.onRowTouch(0, LoadGameLayer.TOUCH_END)
        assertFalse(layer.onConfirm(0)); assertEquals("저장이 호환되지 않아 불러오기에 실패했습니다!", layer.view().notice)
    }
    @Test fun `source battle field selects exact scene branch`() {
        val repo = Repo(mapOf(0 to (save(1, "battle") + " ").replace("} ", ",\"battle\":2}")))
        val layer = LoadGameLayer(repo); layer.onCreate(); layer.onRowTouch(0, LoadGameLayer.TOUCH_END)
        assertTrue(layer.onConfirm(0)); assertEquals(LoadGameLayer.RestoreRoute.HALL_AFTER_BATTLE, repo.route)
    }
}

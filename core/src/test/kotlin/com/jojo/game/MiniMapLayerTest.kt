// Test
package com.jojo.game

import com.jojo.game.presentation.battle.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** MiniMapLayerTest: MiniMapLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class MiniMapLayerTest {
    @Test
    fun `authored button toggles persistent layer without selecting the tactical map`() {
        var loaded = 0
        val layer = MiniMapLayer(setting = 0) { loaded++ }
        layer.onCreate(weather = 0, initialPoolNodes = 0)
        layer.load(120, 120)
        assertFalse(layer.shown)
        assertEquals(522, layer.bgX)
        assertEquals(1, loaded)

        layer.touch(1)
        assertFalse(layer.shown)
        layer.touch(2)
        assertTrue(layer.shown)
        assertTrue(layer.sliding)
        assertEquals(522, layer.bgX)
        layer.advance(.59f)
        assertEquals(522, layer.bgX)
        // 테스트 근거: 연출 프레임과 콜백 처리 순서을 검증한다.
        layer.advance(.02f)
        assertEquals(278, layer.bgX)
        assertFalse(layer.sliding)
        layer.touch(2)
        assertFalse(layer.shown)
    }

    @Test
    fun `yingchuan markers preserve source insertion order and coordinate transforms`() {
        val layer = MiniMapLayer(setting = 16)
        layer.onCreate(weather = 0, initialPoolNodes = 0)
        layer.load(120, 120)
        layer.visible(1, "mine", "normal", "normal", false, 20, 17)
        layer.visible(2, "enemy", "normal", "normal", false, 19, 11)
        assertEquals(listOf(1, 2), layer.map.keys.toList())
        assertEquals(MiniMapLayer.Marker(60, -42, "sf0"), layer.map[1])
        assertEquals(MiniMapLayer.Marker(54, -6, "sf8"), layer.map[2])
    }

    @Test
    fun `stable shown and hidden render contracts include only actual visible submissions`() {
        val shown = MiniMapRenderEvents.jsonl(shown = true).lineSequence().filter(String::isNotBlank).toList()
        val hidden = MiniMapRenderEvents.jsonl(shown = false).lineSequence().filter(String::isNotBlank).toList()
        assertEquals(25, shown.size)
        assertEquals(2, hidden.size)
        assertTrue(shown[1].contains("\"opacity\":0.659"))
        assertTrue(shown[21].contains("\"opacity\":0.498"))
        assertTrue(hidden.all { "Canvas/Layer/bg/btn/Background" in it })
    }

    /**
     * 색 기록 유지: 미니맵의 모든 행이 색을 적고, 그 값은 흰색뿐이다.
     *
     * 원본 `MiniMapLayer` 프리팹의 어느 노드에도 `_color`가 없고 포트도
     * `BattleGridMapSurfaceRenderer.drawMiniMap`에서 알파만 낮출 뿐 RGB는 흰색으로 둔다.
     * `null`을 남기면 비교가 그 행을 조용히 건너뛰고, 다른 색이 생기면 근거 없는 값이다.
     */
    @Test
    fun `mini map rows all record untinted white`() {
        listOf(true, false).forEach { shown ->
            val rows = MiniMapRenderEvents.jsonl(shown).lineSequence().filter(String::isNotBlank).toList()
            assertTrue(rows.isNotEmpty(), "shown=$shown recorded no rows")
            rows.forEach { row ->
                val colour = Regex("\"color\":(\"[^\"]*\"|null)").find(row)?.groupValues?.get(1)?.trim('"')
                    ?: error("row has no color field: $row")
                assertEquals("#ffffff", colour, row)
            }
        }
    }

    /** 투명도 분리: 168/255·127/255로 낮춘 두 행도 색에는 알파를 섞지 않는다. */
    @Test
    fun `dimmed mini map rows keep opacity out of the colour string`() {
        val shown = MiniMapRenderEvents.jsonl(shown = true).lineSequence().filter(String::isNotBlank).toList()

        assertTrue(shown[1].contains("\"opacity\":0.659") && shown[1].contains("\"color\":\"#ffffff\""), shown[1])
        assertTrue(shown[21].contains("\"opacity\":0.498") && shown[21].contains("\"color\":\"#ffffff\""), shown[21])
    }
}

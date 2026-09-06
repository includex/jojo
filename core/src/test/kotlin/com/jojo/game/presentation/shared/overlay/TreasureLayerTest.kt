// Test
package com.jojo.game.presentation.shared.overlay
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.campaign.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** TreasureLayerTest: TreasureLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class TreasureLayerTest {
    @Test fun `source discovery set controls visible labels but numbering follows all treasure order`() {
        val layer=TreasureLayer(listOf(TreasureLayer.Item(4,"A",5,false,"a"),TreasureLayer.Item(9,"B",10,false,"b")),setOf(9))
        assertEquals("지금까지 발견한 보물 01 / 2",layer.title)
        assertNull(layer.rows[0].label0)
        assertEquals("No.2：B",layer.rows[1].label0)
        assertEquals(10,layer.select(9)?.icon)
        assertNull(layer.select(4))
    }
    @Test fun `campaign treasure persistence is separate from inventory and resets`() {
        val state=CampaignState(); state.inventory.addItem(4, 99); state.inventory.restoreDiscoveredTreasures(listOf(9))
        assertEquals(setOf(9),state.inventory.discoveredTreasures); state.reset(); assertEquals(emptySet(),state.inventory.discoveredTreasures)
    }
    @Test fun `original treasure catalogue reads the source treasure flag rather than acquired inventory`() {
        val treasures = GameDataCatalog.load().treasureProfiles()
        assertTrue(treasures.isNotEmpty())
        assertTrue(treasures.all { it.treasure })
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        val layer = TreasureLayer(treasures.map { TreasureLayer.Item(it.id,it.name,it.icon,false,"") }, emptySet())
        assertEquals(treasures.size, layer.rows.size)
    }
}

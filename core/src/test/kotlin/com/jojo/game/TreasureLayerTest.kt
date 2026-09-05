package com.jojo.game

import com.jojo.game.domain.campaign.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `TreasureLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
        // The UI denominator comes from this full source list, not the
        // currently acquired CampaignState.items keys.
        val layer = TreasureLayer(treasures.map { TreasureLayer.Item(it.id,it.name,it.icon,false,"") }, emptySet())
        assertEquals(treasures.size, layer.rows.size)
    }
}

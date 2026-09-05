package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
        val state=CampaignState(); state.items[4]=99; state.discoveredTreasures+=9
        assertEquals(setOf(9),state.discoveredTreasures); state.reset(); assertEquals(emptySet(),state.discoveredTreasures)
    }
    @Test fun `original treasure catalogue reads the source treasure flag rather than acquired inventory`() {
        val treasures = OriginalGameData.load().treasureProfiles()
        assertTrue(treasures.isNotEmpty())
        assertTrue(treasures.all { it.treasure })
        // The UI denominator comes from this full source list, not the
        // currently acquired CampaignState.items keys.
        val layer = TreasureLayer(treasures.map { TreasureLayer.Item(it.id,it.name,it.icon,false,"") }, emptySet())
        assertEquals(treasures.size, layer.rows.size)
    }
}

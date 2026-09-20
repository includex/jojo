// Test
package com.jojo.game

import com.jojo.game.presentation.battle.overlay.*

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** OtherUnitInfoLayerTest: OtherUnitInfoLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class OtherUnitInfoLayerTest {
    @Test fun `actual result panel copies unit values and completes once after point three seconds`() {
        var callbacks = 0
        val unit = BattleUnit("43", "보병 ", Faction.FRIEND, 10, 17, 119, 119, 11, 11, level = 1)
        val layer = OtherUnitInfoLayer()
        val view = layer.onCreate(unit, "경보병") { callbacks++ }
        assertEquals(listOf("보병 ", "1", "경보병", "119", "119", "11", "11"),
            listOf(view.name, view.level.toString(), view.post, view.hp.toString(), view.maxHp.toString(), view.mp.toString(), view.maxMp.toString()))
        assertEquals(.3f, view.completionDelay)
        assertEquals(20, OtherUnitInfoRenderEvents.jsonl(view).lineSequence().count { it.isNotBlank() })
        layer.complete(); layer.complete()
        assertFalse(layer.view().attached)
        assertEquals(1, callbacks)
    }

    @Test
    fun `other prefab contract keeps the shorter source panel and hp mp geometry`() {
        assertEquals(
            listOf(
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/bg2.png", 736f, 96f, 471f, 193.5f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/box1.png", 736f, 96f, 471f, 193f, 2),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark7.png", 747.5f, 179.75f, 48f, 40f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/progress-bg.png", 808.5f, 177.75f, 374f, 24f, 3),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark3.png", 810.5f, 179.75f, 370f, 20f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark8.png", 746.5f, 121.75f, 48f, 48f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/progress-bg.png", 808.5f, 119.75f, 374f, 24f, 3),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark2.png", 810.5f, 121.75f, 370f, 20f),
            ),
            SettlementInfoRenderContract.sprites(SettlementInfoRenderContract.Panel.OTHER),
        )
    }

    /**
     * 원본 `OtherUnitInfoLayer` 프리팹(60e799d9…1b511)도 `_color`를 적은 노드가 투명한
     * `Panel_cancel`뿐이라 라벨은 기본 흰색이다. 그리기 쪽과 같은 상수를 읽는지 확인한다.
     */
    @Test
    fun `other panel labels record the drawing font colour and sprites record none`() {
        assertEquals("#ffffffff", SettlementInfoRenderContract.LABEL_COLOR)
        val unit = BattleUnit("43", "보병 ", Faction.FRIEND, 10, 17, 119, 119, 11, 11, level = 1)
        val rows = OtherUnitInfoRenderEvents.jsonl(OtherUnitInfoLayer().onCreate(unit, "경보병"))
            .lineSequence().filter { it.isNotBlank() }.toList()
        val labels = rows.filter { it.contains("\"drawType\":\"label\"") }
        assertEquals(10, labels.size)
        assertTrue(labels.all { it.contains("\"color\":\"${SettlementInfoRenderContract.LABEL_COLOR}\"") })
        assertTrue(rows.filterNot { it.contains("\"drawType\":\"label\"") }.all { it.contains("\"color\":null") })
    }
}

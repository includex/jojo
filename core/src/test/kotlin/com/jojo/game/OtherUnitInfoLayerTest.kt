package com.jojo.game

import com.jojo.game.presentation.battle.overlay.*

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * class  `OtherUnitInfoLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/box1.png", 736f, 96f, 471f, 193f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark7.png", 747.5f, 179.75f, 48f, 40f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/progress-bg.png", 808.5f, 177.75f, 374f, 24f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark3.png", 810.5f, 179.75f, 370f, 20f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark8.png", 746.5f, 121.75f, 48f, 48f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/progress-bg.png", 808.5f, 119.75f, 374f, 24f),
                SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark2.png", 810.5f, 121.75f, 370f, 20f),
            ),
            SettlementInfoRenderContract.sprites(SettlementInfoRenderContract.Panel.OTHER),
        )
    }
}

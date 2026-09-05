package com.jojo.game

import com.jojo.game.presentation.battle.overlay.BattleUnitInfoJiqiRoute
import com.jojo.game.presentation.battle.overlay.JiQiLayer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `JiQiLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class JiQiLayerTest {
    private fun battleInfo(edit: Boolean = true) = UnitInfoLayer(
        units = listOf(UnitInfoLayer.Unit(0, "조조", "군주", 3, 85, 85, 24, 24, 85, 57, 39, 95, 24)),
        flag = UnitInfoLayer.BATTLE_FLAG,
        editEnabled = edit,
    ).also { it.onCreate() }

    @Test
    fun `battle UnitInfo button9 TOUCH_END opens id27 and Panel cancel closes it`() {
        val info = battleInfo()
        val rates = listOf(85, 57, 39, 95, 24, 22, 99, 48)
        val layer = BattleUnitInfoJiqiRoute.open(info, rates, UnitInfoLayer.TOUCH_END)
        assertEquals(rates, layer?.rates)
        assertTrue(requireNotNull(layer).attached)
        assertFalse(layer.onCancel(1))
        assertTrue(layer.attached)
        assertTrue(layer.onCancel(JiQiLayer.TOUCH_END))
        assertFalse(layer.attached)
    }

    @Test
    fun `non battle or locked UnitInfo cannot open JiQi`() {
        assertNull(BattleUnitInfoJiqiRoute.open(battleInfo(edit = false), List(8) { 0 }, UnitInfoLayer.TOUCH_END))
        assertNull(BattleUnitInfoJiqiRoute.open(battleInfo(), List(8) { 0 }, 1))
    }
}

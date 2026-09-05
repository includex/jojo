package com.jojo.game

import com.jojo.game.presentation.scenario.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `ExclusiveLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ExclusiveLayerTest {
    @Test
    fun `equip information button routes to Global126 only on touch end`() {
        assertEquals(null, EquipExclusiveRoute.openFromInformationButton(1))
        val child = EquipExclusiveRoute.openFromInformationButton(ExclusiveLayer.TOUCH_END)
        assertEquals(ExclusiveLayer.Tab.SET_LIST, child?.selectedTab)
        assertTrue(child?.attached == true)
    }

    @Test
    fun `tabs are selected lazily by touch end and confirm closes`() {
        val layer = ExclusiveLayer()
        assertEquals(ExclusiveLayer.Tab.SET_LIST, layer.selectedTab)
        layer.onButton(1, 1)
        assertEquals(ExclusiveLayer.Tab.SET_LIST, layer.selectedTab)
        layer.onButton(1, ExclusiveLayer.TOUCH_END)
        assertEquals(ExclusiveLayer.Tab.EXCLUSIVE_LIST, layer.selectedTab)
        assertTrue(layer.attached)
        layer.onButton(2, ExclusiveLayer.TOUCH_END)
        assertFalse(layer.attached)
    }

    @Test
    fun `full canvas cancel closes on touch end`() {
        val layer = ExclusiveLayer(ExclusiveLayer.Tab.EXCLUSIVE_LIST)
        layer.onCancel(1)
        assertTrue(layer.attached)
        layer.onCancel(ExclusiveLayer.TOUCH_END)
        assertFalse(layer.attached)
    }
}

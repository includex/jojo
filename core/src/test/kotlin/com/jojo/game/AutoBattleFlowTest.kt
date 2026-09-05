package com.jojo.game

import com.jojo.game.presentation.battle.AutoBattleFlow

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `AutoBattleFlowTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class AutoBattleFlowTest {
    @Test fun `actual MenuLayer HHJS command opens END_ROUND confirmation instead of ending immediately`() {
        val menu = MenuLayer().also {
            it.onCreate(MenuLayer.CreateData(MenuLayer.Weather.QING, 1, 20, "영천 전투"))
        }
        val flow = AutoBattleFlow()
        val command = menu.onCommand(MenuLayer.Command.HHJS, MenuLayer.TOUCH_END)
        assertEquals(MenuLayer.Command.HHJS, command)
        assertFalse(menu.view().attached)
        if (command == MenuLayer.Command.HHJS) flow.openEndRoundPrompt()
        assertEquals(AutoBattleFlow.Overlay.PROMPT, flow.view().overlay)
        assertEquals(0, flow.view().endRoundRequests)
    }

    @Test fun `HHJS waits for confirmation and starts ordinary end round only after OK`() {
        val flow = AutoBattleFlow()
        flow.openEndRoundPrompt()
        assertEquals(AutoBattleFlow.Overlay.PROMPT, flow.view().overlay)
        assertEquals(0, flow.view().endRoundRequests)
        assertFalse(flow.answer(0, 1))
        assertEquals(0, flow.view().endRoundRequests)
        assertTrue(flow.answer(0, AutoBattleFlow.TOUCH_END))
        assertEquals(AutoBattleFlow.Overlay.NONE, flow.view().overlay)
        assertEquals(1, flow.view().endRoundRequests)
        assertFalse(flow.view().collocation)
    }

    @Test fun `checked OK persists TUOGUAN and cancel overlay clears collocation`() {
        val flow = AutoBattleFlow()
        flow.openEndRoundPrompt(); flow.toggle()
        flow.answer(0, AutoBattleFlow.TOUCH_END)
        assertEquals(AutoBattleFlow.Overlay.TUOGUAN, flow.view().overlay)
        assertTrue(flow.view().stored)
        assertTrue(flow.view().collocation)
        assertFalse(flow.cancelTuoGuan(1))
        assertTrue(flow.cancelTuoGuan(AutoBattleFlow.TOUCH_END))
        assertEquals(AutoBattleFlow.Overlay.NONE, flow.view().overlay)
        assertFalse(flow.view().collocation)
    }

    @Test fun `cancel persists toggle but never ends round`() {
        val flow = AutoBattleFlow()
        flow.openEndRoundPrompt(); flow.toggle()
        flow.answer(1, AutoBattleFlow.TOUCH_END)
        assertTrue(flow.view().stored)
        assertEquals(0, flow.view().endRoundRequests)
        flow.openEndRoundPrompt()
        assertTrue(flow.view().checked)
    }
}

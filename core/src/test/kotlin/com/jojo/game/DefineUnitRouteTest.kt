package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `DefineUnitRouteTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class DefineUnitRouteTest {
    @Test fun `reqEffect zero pauses and finish yes resumes exactly once`() {
        var resumes=0;val flow=DefineUnitFlow{resumes++}
        assertTrue(flow.reqEffect(0));assertTrue(flow.paused);assertTrue(flow.attached)
        assertTrue(flow.touchButton(0,true));assertEquals(DefineUnitFlow.Prompt.FINISH,flow.prompt)
        flow.answer(true);assertFalse(flow.paused);assertFalse(flow.attached);assertEquals(1,resumes)
    }
    @Test fun `reset confirmation preserves lifecycle and restores authored values`() {
        val flow=DefineUnitFlow();assertTrue(flow.reqEffect(1));flow.abilities[0]=99
        assertTrue(flow.touchButton(1,true));flow.answer(true)
        assertEquals(listOf(41,49,46,40,42),flow.abilities);assertEquals(25,flow.score)
        assertTrue(flow.paused);assertTrue(flow.attached)
    }
    @Test fun `cancelled prompt does not mutate or resume`() {
        var resumes=0;val flow=DefineUnitFlow{resumes++};flow.reqEffect(0);flow.abilities[0]=77
        flow.touchButton(1,true);flow.answer(false)
        assertEquals(77,flow.abilities[0]);assertEquals(0,resumes);assertTrue(flow.attached)
    }
    @Test fun `unsupported effects and non release input are ignored`() {
        val flow=DefineUnitFlow();assertFalse(flow.reqEffect(2));assertFalse(flow.touchButton(0,true))
        flow.reqEffect(0);assertFalse(flow.touchButton(0,false));assertEquals(DefineUnitFlow.Prompt.NONE,flow.prompt)
    }
}

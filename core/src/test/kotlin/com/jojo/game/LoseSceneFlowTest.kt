package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `LoseSceneFlowTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class LoseSceneFlowTest {
    @Test fun `prompt appears at three seconds and answer zero opens login not battle restart`() {
        var login = 0
        var exit = 0
        val flow = LoseSceneFlow({ login++ }, { exit++ })
        flow.update(2.999f)
        assertEquals(LoseSceneFlow.State.LOGO, flow.state)
        flow.update(.001f)
        assertEquals(LoseSceneFlow.State.PROMPT, flow.state)
        flow.answer(0)
        flow.answer(0)
        assertEquals(LoseSceneFlow.State.LOGIN, flow.state)
        assertEquals(1, login)
        assertEquals(0, exit)
        assertEquals(1, flow.answerCount)
    }

    @Test fun `cancel dispatches end game while hidden ignore tag is inert`() {
        var login = 0
        var exit = 0
        val flow = LoseSceneFlow({ login++ }, { exit++ })
        flow.update(3f)
        flow.answer(2)
        assertEquals(LoseSceneFlow.State.PROMPT, flow.state)
        flow.answer(1)
        flow.answer(1)
        assertEquals(LoseSceneFlow.State.EXIT, flow.state)
        assertEquals(0, login)
        assertEquals(1, exit)
        assertEquals(1, flow.answerCount)
    }
}

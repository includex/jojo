package com.jojo.game

import com.jojo.game.presentation.battle.preparation.HallPreparationFlow

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `StorySkipFlowTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class StorySkipFlowTest {
    @Test fun `TGJQ Hall create attaches id14 behavior and confirmed touch dispatches SKIP`() {
        val hall=HallPreparationFlow(featureSkip=true).also { it.onCreate(0) }
        assertEquals(listOf("SkipLayer"),hall.layers)
        var prompt="";var answer:((Int)->Unit)?=null;val events=mutableListOf<String>()
        val skip=StorySkipFlow(object:StorySkipFlow.Sink {
            override fun msgBox(text:String,reply:(Int)->Unit){prompt=text;answer=reply}
            override fun dispatch(name:String){events+=name}
        })
        skip.onCreate();assertEquals(999,skip.zIndex);assertTrue(skip.button);assertFalse(skip.panel)
        skip.touch(1);assertEquals("",prompt)
        skip.touch(2);assertEquals("스토리를 건너뛸까요?",prompt)
        answer?.invoke(1);assertTrue(skip.button);assertFalse(skip.panel)
        skip.touch(2);answer?.invoke(0)
        assertFalse(skip.button);assertTrue(skip.panel);assertEquals(listOf("SKIP"),events)
        skip.swap();assertTrue(skip.button);assertFalse(skip.panel)
    }
}

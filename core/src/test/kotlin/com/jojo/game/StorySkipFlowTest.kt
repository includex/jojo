package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

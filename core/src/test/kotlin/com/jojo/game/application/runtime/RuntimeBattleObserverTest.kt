// Test
package com.jojo.game.application.runtime

import kotlin.test.Test
import kotlin.test.assertEquals

class RuntimeBattleObserverTest {
    @Test
    fun `observer receives immutable frame and completion values`() {
        val frames = mutableListOf<RuntimeBattleFrameSnapshot>()
        var receivedCompletion: RuntimeBattleCompletion? = null
        val observer = object : RuntimeBattleObserver {
            override fun onFrame(snapshot: RuntimeBattleFrameSnapshot) { frames += snapshot }
            override fun onCompleted(completion: RuntimeBattleCompletion) { receivedCompletion = completion }
        }

        observer.onFrame(RuntimeBattleFrameSnapshot(3, 1.5f, .25f, "{}"))
        observer.onCompleted(RuntimeBattleCompletion("done", 1, "out.json", exitRequested = true))

        assertEquals(RuntimeBattleFrameSnapshot(3, 1.5f, .25f, "{}"), frames.single())
        assertEquals(RuntimeBattleCompletion("done", 1, "out.json", true), receivedCompletion)
    }
}

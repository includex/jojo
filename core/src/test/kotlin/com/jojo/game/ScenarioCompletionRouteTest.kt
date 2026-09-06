// Test
package com.jojo.game

import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** ScenarioCompletionRouteTest: ScenarioCompletionRoute의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class ScenarioCompletionRouteTest {
    @Test fun `ordinary completed hall scene immediately enters implicit battle`() {
        assertTrue(ScenarioCompletionRoute.shouldRoute(PlaybackState.COMPLETE, false, false, null))
    }

    @Test fun `menu completion waits while end and jump always route`() {
        assertFalse(ScenarioCompletionRoute.shouldRoute(PlaybackState.COMPLETE, true, false, null))
        assertTrue(ScenarioCompletionRoute.shouldRoute(PlaybackState.COMPLETE, true, true, null))
        assertTrue(ScenarioCompletionRoute.shouldRoute(PlaybackState.COMPLETE, true, false, 0))
        assertFalse(ScenarioCompletionRoute.shouldRoute(PlaybackState.DIALOGUE, false, true, 0))
    }
}

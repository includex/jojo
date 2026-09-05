package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `ScenarioCompletionRouteTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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

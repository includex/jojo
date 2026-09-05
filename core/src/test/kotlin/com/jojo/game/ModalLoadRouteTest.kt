package com.jojo.game
import com.jojo.game.presentation.shared.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `ModalLoadRouteTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ModalLoadRouteTest {
    @Test fun `getSystemTime owns actual modal load lifecycle`() {
        val route = ModalLoadProductionRoute()
        route.getSystemTimeStarted()
        assertTrue(route.attached)
        assertEquals("검증 중……", route.text)
        route.requestCompleted()
        assertFalse(route.attached)
    }

    @Test fun `load layer exposes label only when caller supplies text`() {
        val layer = LoadLayer()
        assertEquals(LoadLayer.View(true, "검증 중……", "rotateBy:2:360:repeatForever"), layer.onCreate("검증 중……"))
        assertFalse(layer.onCreate(null).labelActive)
    }
}

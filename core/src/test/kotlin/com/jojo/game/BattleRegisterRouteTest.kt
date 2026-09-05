package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleRegisterRouteTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleRegisterRouteTest {
    @Test fun `six actual title touch ends attach Global139`() {
        val route = BattleRegisterRoute()
        repeat(5) { route.titleTouchEnd() }
        assertFalse(route.view().registerAttached)
        route.titleTouchEnd()
        assertTrue(route.view().registerAttached)
        assertEquals(6, route.view().titleTouchCount)
    }

    @Test fun `seventh touch wraps recovered counter`() {
        val route = BattleRegisterRoute()
        repeat(7) { route.titleTouchEnd() }
        assertEquals(0, route.view().titleTouchCount)
        assertTrue(route.view().registerAttached)
    }

    @Test fun `register cancel removes the attached layer`() {
        val route = BattleRegisterRoute()
        repeat(6) { route.titleTouchEnd() }
        route.cancelTouchEnd()
        assertFalse(route.view().registerAttached)
    }
}

package com.jojo.game

import com.jojo.game.presentation.battle.preparation.StartBattleSortRoute

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleSortRouteTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleSortRouteTest {
    @Test fun `touch end opens menu below sort button`() {
        val route = StartBattleSortRoute()
        assertTrue(route.openFromButton(100f, 80f, 40f, false).isEmpty())
        assertEquals(listOf(StartBattleSortRoute.Effect.Open(100f, 60f)), route.openFromButton(100f, 80f, 40f, true))
        assertTrue(route.open)
    }

    @Test fun `five tags close and callback while repeated field toggles direction`() {
        val route = StartBattleSortRoute()
        route.openFromButton(0f, 0f, 20f, true)
        assertEquals(listOf(StartBattleSortRoute.Effect.Sort(0, false)), route.select(0, true))
        assertFalse(route.open)
        route.openFromButton(0f, 0f, 20f, true)
        assertEquals(listOf(StartBattleSortRoute.Effect.Sort(3, false)), route.select(3, true))
        assertEquals(3, route.selectedField)
    }

    @Test fun `panel cancel closes without sort callback`() {
        val route = StartBattleSortRoute()
        route.openFromButton(0f, 0f, 20f, true)
        assertTrue(route.cancel(false).isEmpty())
        assertEquals(listOf(StartBattleSortRoute.Effect.Close), route.cancel(true))
        assertFalse(route.open)
    }
}

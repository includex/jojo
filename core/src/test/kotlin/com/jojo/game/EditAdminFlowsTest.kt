package com.jojo.game

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * class  `EditAdminFlowsTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class EditAdminFlowsTest {
    @Test fun `roster selector distinguishes active absent and departed units`() {
        val flow = EditRosterFlow(
            listOf(EditRosterFlow.UnitRow(0, "조조", false), EditRosterFlow.UnitRow(1, "하후돈", true)),
            listOf("조조", "하후돈", "전위"),
        )
        assertEquals(listOf(EditRosterFlow.Effect.Info("이 무장은 이미 대기열에 있습니다.")), flow.selectUnit(0))
        assertEquals(listOf(EditRosterFlow.Effect.AskJoin(1, "그를 부활시키시겠습니까?")), flow.selectUnit(1))
        assertEquals(listOf(EditRosterFlow.Effect.Join(1), EditRosterFlow.Effect.Refresh), flow.joinAnswer(0))
        assertEquals(listOf(EditRosterFlow.Effect.AskJoin(2, "~하게 할까요?전위팀에 합류할까요?")), flow.selectUnit(2))
    }

    @Test fun `roster row leave and bulk join preserve source effect order`() {
        val flow = EditRosterFlow(listOf(EditRosterFlow.UnitRow(0, "조조", false)), listOf("조조", "하후돈", "전위"))
        assertEquals(listOf(EditRosterFlow.Effect.AskLeave(0, "~하게 할까요?조조떠나다?")), flow.tapRow(0))
        assertEquals(listOf(
            EditRosterFlow.Effect.Leave(0), EditRosterFlow.Effect.Toast("조조팀을 떠났습니다"), EditRosterFlow.Effect.Refresh,
        ), flow.leaveAnswer(0))
        assertEquals(listOf(
            EditRosterFlow.Effect.Join(1), EditRosterFlow.Effect.Toast("하후돈 대열에 합류합니다"), EditRosterFlow.Effect.Refresh,
            EditRosterFlow.Effect.Join(2), EditRosterFlow.Effect.Toast("전위 대열에 합류합니다"), EditRosterFlow.Effect.Refresh,
        ), flow.button(3))
    }

    @Test fun `hall edit gate routes tag eight and both render states`() {
        assertEquals(false, HallEditRosterRoute(false).touch(8, true))
        assertEquals(false, HallEditRosterRoute(true).touch(9, true))
        assertEquals(true, HallEditRosterRoute(true).touch(8, true))
        EditRosterRoute.entries.forEach { route ->
            assertEquals(route, EditRosterRoute.parse("hall-${route.key}-fixture"))
            assertEquals(36, EditRosterRenderEvents.jsonl(route).lineSequence().count(String::isNotBlank))
        }
    }
}

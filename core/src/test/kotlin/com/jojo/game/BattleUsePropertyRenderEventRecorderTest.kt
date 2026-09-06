// Test
package com.jojo.game

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.evidence.BattleUsePropertyDetailView
import com.jojo.game.presentation.battle.evidence.BattleUsePropertyProfileView
import com.jojo.game.presentation.battle.evidence.BattleUsePropertyRenderEventRecorder
import com.jojo.game.presentation.battle.evidence.BattleUsePropertyRenderEventView
import com.jojo.game.presentation.battle.evidence.BattleUsePropertyRowView
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 아이템 사용 증거 기록기 검증: 경로별 목록·상세 JSONL의 출력 조건과 그리기 순서를 확인한다. */
class BattleUsePropertyRenderEventRecorderTest {
    /** 경로 없음: 캡처 대상이 아니므로 빈 JSONL을 반환한다. */
    @Test
    fun `missing route produces empty evidence`() {
        assertEquals("", BattleUsePropertyRenderEventRecorder.jsonl(view(route = null)))
    }

    /** 목록 없음: 전장 이벤트만 남기고 아이템 패널은 기록하지 않는다. */
    @Test
    fun `missing item list keeps only battlefield`() {
        val rows = events(view(rows = null))

        assertEquals(1, rows.size)
        assertTrue(rows.single().contains("content/map"))
        assertTrue(rows.single().contains("\"phase\":\"battle-use-property-select\""))
    }

    /** 목록: 행별 아이콘·효과·재고와 취소 버튼의 원본 순서를 보존한다. */
    @Test
    fun `item list preserves item rows and cancel order`() {
        val rows = events(view())
        val json = rows.joinToString("\n")

        assertEquals(23, rows.size)
        assertOrdered(json, "content/map", "Panel_cancel", "회복용 콩", "\"text\":\"3\"", "회복용 밀", "\"text\":\"2\"", "button/Background/Label")
        assertTrue(rows.all { it.contains("\"phase\":\"battle-use-property-select\"") })
    }

    /** 상세: 상세 정보가 완성된 경우 설명·직위·확인 버튼을 목록 뒤에 추가한다. */
    @Test
    fun `detail appends authored item information after list`() {
        val rows = events(view(
            route = RuntimeBattleRoute.USE_PROPERTY_DETAIL,
            detail = BattleUsePropertyDetailView("회복용 콩", "HP 회복", 88),
            profile = BattleUsePropertyProfileView(120, "체력을 회복한다."),
        ))
        val json = rows.joinToString("\n")

        assertEquals(99, rows.size)
        assertOrdered(json, "button/Background/Label", "Canvas/Layer/bg1", "\"text\":\"회복용 콩\"", "체력을 회복한다.", "장착 가능한 부대입니다.", "button1/Background/Label")
        assertTrue(rows.all { it.contains("\"phase\":\"battle-use-property-detail\"") })
    }

    /** 입력 구성: 목록과 직위명을 가진 재현 가능한 증거 입력을 만든다. */
    private fun view(
        route: RuntimeBattleRoute? = RuntimeBattleRoute.USE_PROPERTY_SELECT,
        rows: List<BattleUsePropertyRowView>? = listOf(
            BattleUsePropertyRowView("회복용 콩", "HP 회복", 3, 88),
            BattleUsePropertyRowView("회복용 밀", "HP 회복", 2, 89),
        ),
        detail: BattleUsePropertyDetailView? = null,
        profile: BattleUsePropertyProfileView? = null,
    ) = BattleUsePropertyRenderEventView(route, rows, detail, profile, (0 until 39).map { "직위$it" })

    /** 행 분해: 빈 줄을 제외한 렌더 이벤트 목록을 반환한다. */
    private fun events(view: BattleUsePropertyRenderEventView): List<String> =
        BattleUsePropertyRenderEventRecorder.jsonl(view).lineSequence().filter(String::isNotBlank).toList()

    /** 순서 검증: 지정 단편이 직전 단편 뒤에서 발견되는지 확인한다. */
    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}

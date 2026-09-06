// Test
package com.jojo.game

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.evidence.BattleMagickDetailView
import com.jojo.game.presentation.battle.evidence.BattleMagickListView
import com.jojo.game.presentation.battle.evidence.BattleMagickRenderEventRecorder
import com.jojo.game.presentation.battle.evidence.BattleMagickRenderEventView
import com.jojo.game.presentation.battle.evidence.BattleMagickRowView
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 마법 증거 기록기 검증: 목록·상세 화면의 JSONL 행 수와 원본 그리기 순서를 확인한다. */
class BattleMagickRenderEventRecorderTest {
    /** 목록 없음: 마법 레이어가 없으면 캡처할 이벤트도 만들지 않는다. */
    @Test
    fun `missing magick list produces empty evidence`() {
        assertEquals("", BattleMagickRenderEventRecorder.jsonl(BattleMagickRenderEventView(RuntimeBattleRoute.MAGICK_LIST, null, null)))
    }

    /** 목록: 전장 뒤에 패널·마법 카드·취소 버튼을 원본 순서로 기록한다. */
    @Test
    fun `magick list preserves card painter order and row count`() {
        val rows = rows(view())
        val json = rows.joinToString("\n")

        assertEquals(27, rows.size)
        assertTrue(rows.all { it.contains("\"phase\":\"battle-magick-list-list\"") })
        assertOrdered(json, "content/map", "Panel_cancel", "progressBar0", "화염탄", "피해 계수", "빙결탄", "button/Background/Label")
    }

    /** 상세: 목록 뒤에 선택 마법의 범위 이미지와 확인 버튼을 이어서 기록한다. */
    @Test
    fun `magick detail appends authored detail panel after list`() {
        val rows = rows(view(detail = BattleMagickDetailView("화염탄", 8, 130, 6, 2, 3, "화염 피해를 준다.")))
        val json = rows.joinToString("\n")

        assertEquals(49, rows.size)
        assertTrue(rows.all { it.contains("\"phase\":\"battle-magick-list-detail\"") })
        assertOrdered(json, "button/Background/Label", "Canvas/Layer/bg1", "화염 피해를 준다.", "Game/Hitarea/3-1", "Game/Effarea/4-1", "확인")
    }

    /** 입력 구성: 두 개 마법 카드로 재현 가능한 목록 증거 값을 만든다. */
    private fun view(detail: BattleMagickDetailView? = null) = BattleMagickRenderEventView(
        if (detail == null) RuntimeBattleRoute.MAGICK_LIST else RuntimeBattleRoute.MAGICK_DETAIL,
        BattleMagickListView(listOf(BattleMagickRowView("화염탄", 8, 130, 6), BattleMagickRowView("빙결탄", 12, null, 7))),
        detail,
    )

    /** 행 분해: JSONL의 빈 줄을 제외한 렌더 이벤트 목록을 반환한다. */
    private fun rows(view: BattleMagickRenderEventView): List<String> =
        BattleMagickRenderEventRecorder.jsonl(view).lineSequence().filter(String::isNotBlank).toList()

    /** 순서 검증: 지정 단편이 이전 단편 뒤에 나타나는지 확인한다. */
    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}

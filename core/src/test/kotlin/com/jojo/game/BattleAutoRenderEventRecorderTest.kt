// Test
package com.jojo.game

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.overlay.AutoBattleFlow
import com.jojo.game.presentation.battle.evidence.BattleAutoRenderEventInput
import com.jojo.game.presentation.battle.evidence.BattleAutoRenderEventRecorder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 자동 전투 증거 기록기 검증: 경로별 JSONL phase와 원본 계층·노드 기록 순서를 확인한다. */
class BattleAutoRenderEventRecorderTest {
    /** 위임 해제 확인 창: 토글 표시 없이 기존 열한 개 이벤트 순서를 기록한다. */
    @Test
    fun `unchecked prompt keeps authored draw order without checkmark`() {
        val rows = rows(RuntimeBattleRoute.AUTO_PROMPT_OFF, AutoBattleFlow.Overlay.PROMPT, checked = false)

        assertEquals(11, rows.size)
        assertTrue(rows.all { it.contains("\"phase\":\"battle-auto-battle-prompt-off\"") })
        assertOrdered(rows.joinToString("\n"), "content/map", "bg0\",", "Logo_3-1", "tuoguan/Background", "button1/Background", "button0/Background")
        assertFalse(rows.any { it.contains("tuoguan/checkmark") })
    }

    /** 위임 선택 확인 창: 체크 마크가 토글 배경 뒤와 위임 문구 앞에 삽입된다. */
    @Test
    fun `checked prompt inserts checkmark at authored position`() {
        val rows = rows(RuntimeBattleRoute.AUTO_PROMPT_ON, AutoBattleFlow.Overlay.PROMPT, checked = true)

        assertEquals(12, rows.size)
        assertTrue(rows.all { it.contains("\"phase\":\"battle-auto-battle-prompt-on\"") })
        assertOrdered(rows.joinToString("\n"), "tuoguan/Background", "tuoguan/checkmark", "tuoguan/label", "button1/Background")
    }

    /** 위임 진행: 전장 뒤에 자동 진행 배너와 중앙 이미지가 기존 순서로 기록된다. */
    @Test
    fun `active route keeps battlefield and delegation banner order`() {
        val rows = rows(RuntimeBattleRoute.AUTO_ACTIVE, AutoBattleFlow.Overlay.TUOGUAN, checked = true)

        assertEquals(3, rows.size)
        assertTrue(rows.all { it.contains("\"phase\":\"battle-auto-battle-active\"") })
        assertOrdered(rows.joinToString("\n"), "content/map", "Canvas/Layer/img2\"", "Canvas/Layer/img2/img3")
    }

    /** 비표시 상태: 자동 전투 경로여도 표시할 오버레이가 없으면 빈 JSONL을 반환한다. */
    @Test
    fun `empty overlay produces no render events`() {
        assertEquals("", BattleAutoRenderEventRecorder.jsonl(input(RuntimeBattleRoute.AUTO_ACTIVE, AutoBattleFlow.Overlay.NONE)))
    }

    /** 입력 생성: 기록기만 검증하도록 자동 전투 흐름의 불변 표시 상태를 직접 구성한다. */
    private fun input(route: RuntimeBattleRoute, overlay: AutoBattleFlow.Overlay, checked: Boolean = false) =
        BattleAutoRenderEventInput(route, AutoBattleFlow.View(overlay, checked, stored = checked, collocation = false, endRoundRequests = 0))

    /** 행 분해: 빈 줄을 제외한 JSONL 이벤트 목록을 반환한다. */
    private fun rows(route: RuntimeBattleRoute, overlay: AutoBattleFlow.Overlay, checked: Boolean): List<String> =
        BattleAutoRenderEventRecorder.jsonl(input(route, overlay, checked)).lineSequence().filter(String::isNotBlank).toList()

    /** 순서 검증: 지정 노드 조각들이 이전 조각 뒤에 기록됐는지 확인한다. */
    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}

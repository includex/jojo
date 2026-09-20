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

    /**
     * 색 기록 유지: 세 경로의 모든 행이 색을 적고, 원본 프리팹에 근거가 있는 다섯 값만 쓴다.
     *
     * 하네스는 행마다 `node.color`를 내보내므로 `null`을 남기는 행은 비교에서 조용히 빠진다.
     * 근거 없는 여섯 번째 색이 생겨도 여기서 걸린다.
     */
    @Test
    fun `every recorded row carries one of the authored prompt colours`() {
        val authored = setOf("#ffffff", "#936100", "#0005ff", "#fc0000", "#026e00")
        listOf(
            Triple(RuntimeBattleRoute.AUTO_PROMPT_OFF, AutoBattleFlow.Overlay.PROMPT, false),
            Triple(RuntimeBattleRoute.AUTO_PROMPT_ON, AutoBattleFlow.Overlay.PROMPT, true),
            Triple(RuntimeBattleRoute.AUTO_ACTIVE, AutoBattleFlow.Overlay.TUOGUAN, true),
        ).forEach { (route, overlay, checked) ->
            val colours = rows(route, overlay, checked).map(::colourOf)
            assertTrue(colours.isNotEmpty(), "$route recorded no rows")
            assertTrue(
                colours.none { it == "null" },
                "$route left a row without colour; the comparison would skip it",
            )
            assertEquals(
                emptySet(), colours.toSet() - authored,
                "$route invented a colour with no MsgBox4/TuoGuanLayer node behind it",
            )
        }
    }

    /** MsgBox4 문구: 네 라벨만 프리팹 `_color`를 쓰고 스프라이트는 흰색으로 남는다. */
    @Test
    fun `prompt labels carry their prefab node colours and sprites stay white`() {
        val rows = rows(RuntimeBattleRoute.AUTO_PROMPT_ON, AutoBattleFlow.Overlay.PROMPT, checked = true)

        assertEquals("#936100", colourOf(rows.single { it.contains("bg0/label\"") }))
        assertEquals("#0005ff", colourOf(rows.single { it.contains("tuoguan/label") }))
        assertEquals("#fc0000", colourOf(rows.single { it.contains("button1/Background/Label") }))
        assertEquals("#026e00", colourOf(rows.single { it.contains("button0/Background/Label") }))
        rows.filter { it.contains("\"drawType\":\"label\"").not() }
            .forEach { assertEquals("#ffffff", colourOf(it), it) }
    }

    /** 위임 배너: `TuoGuanLayer` 프리팹에는 `_color`가 없어 두 스프라이트 모두 흰색이다. */
    @Test
    fun `delegation banner records untinted white for both sprites`() {
        rows(RuntimeBattleRoute.AUTO_ACTIVE, AutoBattleFlow.Overlay.TUOGUAN, checked = true)
            .forEach { assertEquals("#ffffff", colourOf(it), it) }
    }

    /** 색 추출: 한 행의 `color` 값을 문자열로 돌려준다. */
    private fun colourOf(row: String): String =
        Regex("\"color\":(\"[^\"]*\"|null)").find(row)?.groupValues?.get(1)?.trim('"')
            ?: error("row has no color field: $row")

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

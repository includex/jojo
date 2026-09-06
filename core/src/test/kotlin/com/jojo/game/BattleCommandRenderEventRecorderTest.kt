// Test
package com.jojo.game

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.evidence.BattleCommandRenderEventRecorder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 전투 명령 증거 기록기 검증: 경로별 고정 JSONL의 phase·그리기 순서·항목 수를 확인한다. */
class BattleCommandRenderEventRecorderTest {
    /** 취소 경로: 전장만 남기고 명령 패널 이벤트를 추가하지 않는다. */
    @Test
    fun `cancel command records only the battlefield`() {
        val rows = rows(RuntimeBattleRoute.COMMAND_CANCEL)

        assertEquals(1, rows.size)
        assertTrue(rows.single().contains("\"phase\":\"battle-command-cancel\""))
        assertTrue(rows.single().contains("ScrollView/view/content/map"))
    }

    /** 마법 경로: 전장 뒤에 흐림막·마법 목록·첫 마법·취소 버튼을 원본 순서로 기록한다. */
    @Test
    fun `magick command keeps authored panel draw order`() {
        val rows = rows(RuntimeBattleRoute.COMMAND_MAGICK)

        assertEquals(20, rows.size)
        assertOrdered(rows.joinToString("\n"), "content/map", "Panel_cancel", "progressBar0", "skill_0", "피해 계수", "button/Background/Label")
        assertTrue(rows.all { it.contains("\"phase\":\"battle-command-magick\"") })
    }

    /** 아이템 경로: 아이템 두 행의 표시 순서와 재고 수를 보존한다. */
    @Test
    fun `property command keeps both authored item rows`() {
        val rows = rows(RuntimeBattleRoute.COMMAND_PROPERTY)

        assertEquals(23, rows.size)
        assertOrdered(rows.joinToString("\n"), "회복용 콩", "인벤토리", "\"text\":\"3\"", "회복용 밀", "\"text\":\"2\"", "button/Background/Label")
    }

    /** 기본 명령 경로: 여섯 명령과 취소 버튼의 이중 아이콘을 기록한다. */
    @Test
    fun `initial command keeps all command buttons and dual icons`() {
        val rows = rows(RuntimeBattleRoute.COMMAND_INITIAL)

        assertEquals(30, rows.size)
        assertOrdered(rows.joinToString("\n"), "button0/Background", "\"text\":\"공격\"", "button5/Background", "\"text\":\"대기\"", "button6/Background")
        assertEquals(2, rows.count { it.contains("button0/Background/img") && it.contains("command1") })
        assertTrue(rows.all { it.contains("\"phase\":\"battle-command-initial\"") })
    }

    /** 행 분해: JSONL의 빈 줄을 제외한 렌더 이벤트 목록을 반환한다. */
    private fun rows(route: RuntimeBattleRoute): List<String> =
        BattleCommandRenderEventRecorder.jsonl(route).lineSequence().filter(String::isNotBlank).toList()

    /** 순서 검증: 지정 단편이 이전 단편 뒤에 나타나는지 확인한다. */
    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}

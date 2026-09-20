// Test
package com.jojo.game

import com.jojo.game.application.battle.BattleCommandFlow
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

    /**
     * 색 기록 유지: 모든 경로의 모든 행이 색을 적고, 원본 근거가 있는 세 값만 쓴다.
     *
     * 하네스는 행마다 색을 내보내므로 `null`을 남기는 행은 비교에서 조용히 빠진다.
     * 반대로 근거 없는 네 번째 색이 생기면 그것도 여기서 걸린다.
     */
    @Test
    fun `every recorded row carries one of the three authored colours`() {
        RuntimeBattleRoute.entries
            .filter { it.name.startsWith("COMMAND_") }
            .forEach { route ->
                val colours = rows(route).map(::colourOf)
                assertTrue(colours.isNotEmpty(), "$route recorded no rows")
                assertTrue(
                    colours.none { it == "null" },
                    "$route left a row without colour; the comparison would skip it",
                )
                assertEquals(
                    emptySet(), colours.toSet() - setOf("#ffffff", "#000000", "#a0a0a0"),
                    "$route invented a colour with no source node behind it",
                )
            }
    }

    /**
     * 비활성 명령: 문구만 원본 `cc.color(10526880)` 회색이고 아이콘 노드 색은 흰색으로 남는다.
     *
     * 원본은 아이콘에 회색조 material만 갈아끼우고 노드 색은 건드리지 않는다. 포트가
     * 계산한 회색을 적으면 하네스의 흰색과 맞부딪혀 거짓 불일치가 된다.
     */
    @Test
    fun `disabled command greys only the label and leaves icon nodes white`() {
        val rows = rows(RuntimeBattleRoute.COMMAND_DISABLED, buttons(disabled = setOf(2)))

        assertEquals("#a0a0a0", colourOf(rows.single { it.contains("button2/Background/Label") }))
        assertEquals("#000000", colourOf(rows.single { it.contains("button0/Background/Label") }))
        rows.filter { it.contains("button2/Background/img") }.also { icons ->
            assertEquals(2, icons.size)
            icons.forEach { assertEquals("#ffffff", colourOf(it)) }
        }
        assertEquals("#ffffff", colourOf(rows.single { it.contains("button2/Background\",") }))
    }

    /** 흐림막: 원본 Panel_cancel 노드는 검정이고 투명도만 다르다. 색에 투명도를 섞지 않는다. */
    @Test
    fun `dismiss panel records black without folding opacity into colour`() {
        val row = rows(RuntimeBattleRoute.COMMAND_INITIAL).single { it.contains("Panel_cancel") }

        assertEquals("#000000", colourOf(row))
        assertTrue(row.contains("\"opacity\":0.039"), row)
    }

    /** 색 추출: 한 행의 `color` 값을 문자열로 돌려준다. */
    private fun colourOf(row: String): String =
        Regex("\"color\":(\"[^\"]*\"|null)").find(row)?.groupValues?.get(1)?.trim('"')
            ?: error("row has no color field: $row")

    /** 버튼 상태: 지정한 tag만 비활성인 일곱 버튼 목록을 만든다. */
    private fun buttons(disabled: Set<Int> = emptySet()): List<BattleCommandFlow.Button> =
        BattleCommandFlow.Command.entries.map { command ->
            BattleCommandFlow.Button(command, command.tag !in disabled, grayscale = command.tag in disabled)
        }

    /** 행 분해: JSONL의 빈 줄을 제외한 렌더 이벤트 목록을 반환한다. */
    private fun rows(
        route: RuntimeBattleRoute,
        buttons: List<BattleCommandFlow.Button> = buttons(),
    ): List<String> =
        BattleCommandRenderEventRecorder.jsonl(route, buttons).lineSequence().filter(String::isNotBlank).toList()

    /** 순서 검증: 지정 단편이 이전 단편 뒤에 나타나는지 확인한다. */
    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}

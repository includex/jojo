// Test
package com.jojo.game

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.evidence.BattleRoundRenderEventInput
import com.jojo.game.presentation.battle.evidence.BattleRoundRenderEventRecorder
import com.jojo.game.presentation.battle.overlay.RoundLayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 라운드 화면 증거 기록기 검증: 경로별 문구와 고정 렌더 이벤트 순서를 확인한다. */
class BattleRoundRenderEventRecorderTest {
    /** 일반 라운드: 아군 문구와 턴 문구의 그림자·본문 순서를 보존한다. */
    @Test
    fun `normal round records allied labels and round text in painter order`() {
        val rows = rows(
            BattleRoundRenderEventInput(
                RuntimeBattleRoute.ROUND_NORMAL,
                RoundLayer.View(roundLabelsVisible = true, campLabelsVisible = false, roundText = "제3턴"),
            ),
        )

        assertEquals(6, rows.size)
        assertTrue(rows.all { it.contains("\"phase\":\"battle-round-normal\"") })
        assertOrdered(rows.joinToString("\n"), "content/map", "Panel_cancel", "label02", "label01", "label12", "label11")
        assertTrue(rows.any { it.contains("\"text\":\"제3턴\"") })
    }

    /** 적군 라운드: 적군 문구 두 개만 고정 순서로 기록한다. */
    @Test
    fun `enemy round records enemy labels without allied round text`() {
        val rows = rows(
            BattleRoundRenderEventInput(
                RuntimeBattleRoute.ROUND_ENEMY,
                RoundLayer.View(roundLabelsVisible = false, campLabelsVisible = true, roundText = ""),
            ),
        )

        assertEquals(4, rows.size)
        assertTrue(rows.all { it.contains("\"phase\":\"battle-round-enemy\"") })
        assertOrdered(rows.joinToString("\n"), "content/map", "Panel_cancel", "label22", "label21")
        assertTrue(rows.none { it.contains("아군 단계") })
    }

    /** 비활성 라운드: 표시 상태가 없으면 빈 JSONL을 반환한다. */
    @Test
    fun `missing round view produces empty jsonl`() {
        assertEquals(
            "",
            BattleRoundRenderEventRecorder.jsonl(BattleRoundRenderEventInput(RuntimeBattleRoute.ROUND_FINAL, null)),
        )
    }

    /**
     * 색 기록 유지: 세 경로의 모든 행이 색을 적고, 근거가 있는 네 값만 쓴다.
     *
     * 하네스는 행마다 `node.color`를 내보내므로 `null`을 남기는 행은 비교에서 조용히 빠진다.
     */
    @Test
    fun `every recorded row carries one of the authored round colours`() {
        val authored = setOf("#ffffff", "#000000", "#ff0000", "#837f7f")
        listOf(
            RuntimeBattleRoute.ROUND_NORMAL to RoundLayer.View(true, false, "제3턴"),
            RuntimeBattleRoute.ROUND_FINAL to RoundLayer.View(true, false, "최종 턴"),
            RuntimeBattleRoute.ROUND_ENEMY to RoundLayer.View(false, true, ""),
        ).forEach { (route, view) ->
            val colours = rows(BattleRoundRenderEventInput(route, view)).map(::colourOf)
            assertTrue(colours.isNotEmpty(), "$route recorded no rows")
            assertTrue(
                colours.none { it == "null" },
                "$route left a row without colour; the comparison would skip it",
            )
            assertEquals(
                emptySet(), colours.toSet() - authored,
                "$route invented a colour with no RoundLayer node behind it",
            )
        }
    }

    /**
     * 그림자 라벨: `label02`/`label22`만 빨강이고 앞 글자는 흰색이다. 턴 수 그림자 `label12`는
     * 포트가 실제로 그리는 (255,128,128)을 적는다. 원본 프리팹 값은 (131,127,127)이라
     * 이 행은 색 비교에서 어긋나며, 그것이 기록해 두려는 결함이다.
     */
    @Test
    fun `round banner records the shadow colours the port actually draws`() {
        val rows = rows(
            BattleRoundRenderEventInput(
                RuntimeBattleRoute.ROUND_NORMAL,
                RoundLayer.View(roundLabelsVisible = true, campLabelsVisible = false, roundText = "제3턴"),
            ),
        )

        assertEquals("#ff0000", colourOf(rows.single { it.contains("label02") }))
        assertEquals("#ffffff", colourOf(rows.single { it.contains("label01") }))
        // 원본 프리팹 `label12`의 `_color` 4286545795 = (131,127,127). 단계 그림자만 빨강이다.
        assertEquals("#837f7f", colourOf(rows.single { it.contains("label12") }))
        assertEquals("#ffffff", colourOf(rows.single { it.contains("label11") }))
    }

    /** 흐림막: 원본 Panel_cancel 노드는 검정이고 투명도만 다르다. 색에 투명도를 섞지 않는다. */
    @Test
    fun `dismiss panel records black without folding opacity into colour`() {
        val row = rows(
            BattleRoundRenderEventInput(
                RuntimeBattleRoute.ROUND_ENEMY,
                RoundLayer.View(roundLabelsVisible = false, campLabelsVisible = true, roundText = ""),
            ),
        ).single { it.contains("Panel_cancel") }

        assertEquals("#000000", colourOf(row))
        assertTrue(row.contains("\"opacity\":0.314"), row)
    }

    /** 색 추출: 한 행의 `color` 값을 문자열로 돌려준다. */
    private fun colourOf(row: String): String =
        Regex("\"color\":(\"[^\"]*\"|null)").find(row)?.groupValues?.get(1)?.trim('"')
            ?: error("row has no color field: $row")

    /** 행 분해: JSONL의 빈 줄을 제외한 렌더 이벤트 목록을 반환한다. */
    private fun rows(input: BattleRoundRenderEventInput): List<String> =
        BattleRoundRenderEventRecorder.jsonl(input).lineSequence().filter(String::isNotBlank).toList()

    /** 순서 검증: 지정 단편이 이전 단편 뒤에 나타나는지 확인한다. */
    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}

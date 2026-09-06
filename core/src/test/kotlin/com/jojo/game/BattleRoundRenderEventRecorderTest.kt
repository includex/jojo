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

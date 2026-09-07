// Test
package com.jojo.game

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ScenarioLabelContinuationTest: `goto`로 되돌아온 레이블이 바깥 블록의 남은 문장까지
 * 이어서 실행하는지 검증한다.
 *
 * 원본은 평탄한 명령열을 건너뛰므로 레이블이 속한 블록이 끝나면 바깥 블록으로 이어진다.
 * 진입점을 블록 안쪽으로만 잘라 두면 R_00 설정 메뉴에서 항목을 한 번이라도 누른 뒤
 * `게임 시작`을 고를 때 `model.unitJoin(0, 0, 2)` 앞에서 시나리오가 끝나, 아군이 한 명도
 * 없는 채로 영천 전투에 들어가 즉시 패배한다.
 */
class ScenarioLabelContinuationTest {
    /** R_00 도입부를 설정 메뉴에서 지정한 만큼 항목을 누른 뒤 `게임 시작`으로 끝낸다. */
    private fun playPrelude(modePick: Int, toggleRow: Int, toggleCount: Int): CampaignState {
        val campaign = CampaignState()
        campaign.reset()
        val runtime = ScenarioInterpreter.load("R_00", campaign)
        runtime.start("scene1")
        var steps = 0
        var toggles = 0
        while (runtime.state != PlaybackState.COMPLETE) {
            check(steps++ < 10_000) { "R_00 도입부가 끝나지 않았습니다." }
            when (runtime.state) {
                PlaybackState.DIALOGUE -> runtime.advanceDialogue()
                PlaybackState.CHOICE -> {
                    val options = runtime.currentChoice?.options.orEmpty()
                    val start = options.indexOfFirst { it.contains("게임 시작") }
                    val pick = when {
                        options.any { it.contains("클래식 모드") } -> modePick
                        start >= 0 && toggles < toggleCount -> { toggles++; toggleRow }
                        start >= 0 -> start
                        else -> 0
                    }
                    runtime.selectChoice(pick)
                    runtime.confirmChoice()
                }

                PlaybackState.DELAY -> runtime.skipDelay()
                PlaybackState.MODAL -> runtime.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
        return campaign
    }

    @Test fun `setup menu toggles still join Cao Cao before the Yingchuan battle`() {
        // 곧바로 시작하는 경로는 원래도 조조를 합류시켰다.
        assertEquals(listOf(0), playPrelude(modePick = 0, toggleRow = 0, toggleCount = 0).joinedUnits.toList())
        // 훈련 모드·난이도·능력 재계산은 모두 goto('lab345')로 메뉴로 되돌아온다.
        assertEquals(listOf(0), playPrelude(modePick = 0, toggleRow = 0, toggleCount = 1).joinedUnits.toList())
        assertEquals(listOf(0), playPrelude(modePick = 0, toggleRow = 1, toggleCount = 1).joinedUnits.toList())
        assertEquals(listOf(0), playPrelude(modePick = 0, toggleRow = 2, toggleCount = 1).joinedUnits.toList())
        assertEquals(listOf(0), playPrelude(modePick = 0, toggleRow = 1, toggleCount = 5).joinedUnits.toList())
        // 확장 모드도 같은 레이블 구조를 쓴다.
        assertEquals(listOf(0), playPrelude(modePick = 1, toggleRow = 0, toggleCount = 0).joinedUnits.toList())
    }
}

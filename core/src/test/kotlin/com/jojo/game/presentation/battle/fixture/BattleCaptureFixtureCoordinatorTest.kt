// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.battle.route.BattlePresentationConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals

/** 전투 캡처 fixture 조정기가 화면 어댑터에 필요한 대사 진행만 요청하는지 검증한다. */
class BattleCaptureFixtureCoordinatorTest {
    /** 컷신 진행 검증: 공격 대사와 477 대사를 각각 원본 순서로 한 단계씩 진행한다. */
    @Test
    fun `cutscene capture advances attack then speaker 477 through the port`() {
        val coordinator = coordinator(RuntimeBattleRoute.CUTSCENE_477)
        val port = FixturePort().apply {
            onFixtureAdvance = {
                if (fixtureAdvanceCount == 1) state = PlaybackState.DELAY
                else {
                    state = PlaybackState.DIALOGUE
                    speakerId = "477"
                }
            }
        }

        coordinator.update(frame(0f), port)
        port.state = PlaybackState.DIALOGUE
        coordinator.update(frame(3.1f), port)

        assertEquals(2, port.fixtureAdvanceCount)
        assertEquals("477", port.speakerId)
    }

    /** 모달 진행 검증: opening 대사는 기준 시각 이후 한 번만 넘긴다. */
    @Test
    fun `modal capture dismisses opening dialogue only once`() {
        val coordinator = coordinator(RuntimeBattleRoute.MODAL_TERRAIN)
        val port = FixturePort().apply {
            onFixtureAdvance = { state = PlaybackState.DELAY }
        }

        coordinator.update(frame(.1f), port)
        coordinator.update(frame(1f), port)

        assertEquals(1, port.fixtureAdvanceCount)
    }

    /** fixture 조정기 생성: 지정한 런타임 경로의 캡처 정책을 적용한다. */
    private fun coordinator(route: RuntimeBattleRoute) = BattleCaptureFixtureCoordinator(
        BattleCaptureFixtureConfiguration(RuntimeBattlePresentation(route)),
        BattlePresentationConfiguration(RuntimeBattlePresentation(route)),
    )

    /** fixture 프레임 생성: 대사가 표시 중이고 아직 완전히 드러나지 않은 기본 입력을 만든다. */
    private fun frame(elapsed: Float) = BattleCaptureFixtureCoordinator.Frame(
        elapsed = elapsed,
        dialogueState = PlaybackState.DIALOGUE,
        dialogueVisible = true,
        dialogueComplete = false,
    )

    /** 가짜 화면 어댑터: 조정기의 대사 진행 요청과 스크립트 상태 조회를 검증 가능하게 만든다. */
    private class FixturePort : BattleCaptureFixtureCoordinator.Port {
        var state = PlaybackState.DIALOGUE
        var speakerId: String? = null
        var fixtureAdvanceCount = 0
        var dialogueStepCount = 0
        var onFixtureAdvance: () -> Unit = {}

        override fun advanceFixtureDialogue() {
            fixtureAdvanceCount++
            onFixtureAdvance()
        }

        override fun advanceDialogueStep() {
            dialogueStepCount++
        }

        override fun playbackState(): PlaybackState = state
        override fun dialogueSpeakerId(): String? = speakerId
    }
}

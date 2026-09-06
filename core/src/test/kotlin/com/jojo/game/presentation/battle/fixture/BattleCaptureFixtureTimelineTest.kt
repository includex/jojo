// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.battle.route.BattlePresentationConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 전투 캡처 시간선이 검증 전용 대사 진행과 기록 시점을 일관되게 계산하는지 확인한다. */
class BattleCaptureFixtureTimelineTest {
    @Test
    fun `컷신 477은 공격 시작 후 다음 대사에서만 진행한다`() {
        val timeline = timeline(RuntimeBattleRoute.CUTSCENE_477)

        assertEquals(
            listOf(BattleCaptureFixtureTimeline.DialogueAdvance.CUTSCENE_ATTACK),
            timeline.dialogueAdvances(frame(0f, PlaybackState.DIALOGUE)),
        )
        assertEquals(Float.MAX_VALUE, timeline.captureAt(frame(1f, PlaybackState.DELAY)))
        assertTrue(timeline.dialogueAdvances(frame(2.99f, PlaybackState.DIALOGUE)).isEmpty())
        assertEquals(
            listOf(BattleCaptureFixtureTimeline.DialogueAdvance.CUTSCENE_477),
            timeline.dialogueAdvances(frame(3.1f, PlaybackState.DIALOGUE)),
        )
        assertEquals(6.1f, timeline.captureAt(frame(3.1f, PlaybackState.DIALOGUE)))
    }

    @Test
    fun `대사 단계 캡처는 첫 입력 뒤 두 번째 입력을 지연한다`() {
        val timeline = BattleCaptureFixtureTimeline(
            BattleCaptureFixtureConfiguration(RuntimeBattlePresentation(dialogueStep = 2)),
            BattlePresentationConfiguration(RuntimeBattlePresentation(dialogueStep = 2)),
        )

        assertEquals(
            listOf(BattleCaptureFixtureTimeline.DialogueAdvance.DIALOGUE_STEP),
            timeline.dialogueAdvances(frame(0f, PlaybackState.DIALOGUE)),
        )
        assertTrue(timeline.dialogueAdvances(frame(0.08f, PlaybackState.DIALOGUE)).isEmpty())
        assertEquals(
            listOf(BattleCaptureFixtureTimeline.DialogueAdvance.DIALOGUE_STEP),
            timeline.dialogueAdvances(frame(0.14f, PlaybackState.DIALOGUE)),
        )
    }

    @Test
    fun `진단 기록은 경로별로 한 번만 소비한다`() {
        val action = BattleCaptureFixtureTimeline(
            BattleCaptureFixtureConfiguration(
                RuntimeBattlePresentation(actionSample = com.jojo.game.application.runtime.RuntimeBattleActionSample(6, 0f)),
            ),
            BattlePresentationConfiguration(
                RuntimeBattlePresentation(actionSample = com.jojo.game.application.runtime.RuntimeBattleActionSample(6, 0f)),
            ),
        )
        val selection = timeline(RuntimeBattleRoute.SELECTION)

        assertFalse(action.consumeActionLog(1f))
        assertTrue(action.consumeActionLog(1.01f))
        assertFalse(action.consumeActionLog(2f))
        assertTrue(selection.consumeSelectionLog(6.01f, 6f))
        assertFalse(selection.consumeSelectionLog(7f, 6f))
    }

    private fun timeline(route: RuntimeBattleRoute) = BattleCaptureFixtureTimeline(
        BattleCaptureFixtureConfiguration(RuntimeBattlePresentation(route)),
        BattlePresentationConfiguration(RuntimeBattlePresentation(route)),
    )

    private fun frame(elapsed: Float, state: PlaybackState) = BattleCaptureFixtureTimeline.Frame(
        elapsed = elapsed,
        dialogueState = state,
        dialogueVisible = state == PlaybackState.DIALOGUE,
        dialogueComplete = false,
    )
}

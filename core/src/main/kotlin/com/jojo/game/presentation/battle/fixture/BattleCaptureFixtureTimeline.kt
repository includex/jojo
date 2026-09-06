// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.battle.route.BattlePresentationConfiguration

/**
 * 전투 캡처 경로의 시간 흐름을 관리한다.
 * 캡처마다 필요한 대사 진행, 프레임 기록 시점, 한 번만 남겨야 하는 진단 기록을 화면 렌더링과 분리한다.
 */
internal class BattleCaptureFixtureTimeline(
    private val configuration: BattleCaptureFixtureConfiguration,
    private val routeConfiguration: BattlePresentationConfiguration,
) {
    /** 캡처 시간 계산에 필요한 현재 화면 상태를 묶는다. */
    data class Frame(
        val elapsed: Float,
        val dialogueState: PlaybackState,
        val dialogueVisible: Boolean,
        val dialogueComplete: Boolean,
    )

    /** 캡처 경로가 현재 프레임에서 요구하는 대사 진행 작업을 구분한다. */
    enum class DialogueAdvance {
        CUTSCENE_ATTACK,
        CUTSCENE_477,
        DIALOGUE_STEP,
    }

    private var cutsceneAttackStartedAt: Float? = null
    private var cutscene477StartedAt: Float? = null
    private var dialogueStepStartedAt: Float? = null
    private var dialogueStepInputs = 0
    private var actionCaptureLogged = false
    private var dialogueCaptureLogged = false
    private var selectionCaptureLogged = false

    /** 현재 대사 상태에서 자동 진행해야 할 캡처 작업을 반환한다. */
    fun dialogueAdvances(frame: Frame): List<DialogueAdvance> = buildList {
        if (configuration.cutsceneCapture && cutsceneAttackStartedAt == null &&
            frame.dialogueState == PlaybackState.DIALOGUE
        ) {
            cutsceneAttackStartedAt = frame.elapsed
            add(DialogueAdvance.CUTSCENE_ATTACK)
        }
        if (configuration.cutscene477Capture && cutsceneAttackStartedAt != null &&
            cutscene477StartedAt == null && frame.dialogueState == PlaybackState.DIALOGUE &&
            frame.elapsed - requireNotNull(cutsceneAttackStartedAt) >= 3f
        ) {
            cutscene477StartedAt = frame.elapsed
            add(DialogueAdvance.CUTSCENE_477)
        }
        configuration.dialogueStepCapture?.let { targetStep ->
            if (frame.dialogueState != PlaybackState.DIALOGUE) {
                dialogueStepStartedAt = null
                return@let
            }
            val startedAt = dialogueStepStartedAt ?: frame.elapsed.also { dialogueStepStartedAt = it }
            val inputDelay = when (dialogueStepInputs) {
                0 -> 0f
                1 -> 0.05f
                else -> 3.2f
            }
            if (dialogueStepInputs < targetStep && frame.elapsed - startedAt >= inputDelay) {
                dialogueStepInputs++
                dialogueStepStartedAt = null
                add(DialogueAdvance.DIALOGUE_STEP)
            }
        }
    }

    /** 현재 경로의 프레임 캡처 기준 시간을 계산한다. */
    fun captureAt(frame: Frame): Float = when {
        routeConfiguration.openingSayRoute -> 0.55f
        routeConfiguration.hudRoute -> 6f
        routeConfiguration.battleDialogueBlendRoute -> if (
            frame.dialogueVisible && frame.dialogueComplete && frame.elapsed > 0.6f
        ) 0.6f else Float.MAX_VALUE
        configuration.actionSampleMode -> 1f
        configuration.cutsceneAttackCapture -> (cutsceneAttackStartedAt ?: Float.MAX_VALUE) + 0.9f
        configuration.cutscenePostHitCapture -> (cutsceneAttackStartedAt ?: Float.MAX_VALUE) + 3f
        configuration.cutscene477Capture -> (cutscene477StartedAt ?: Float.MAX_VALUE) + 3f
        configuration.dialogueStepCapture != null -> (dialogueStepStartedAt ?: Float.MAX_VALUE) + 3.4f
        else -> 6f
    }

    /** 액션 표본의 진단 정보를 첫 캡처 프레임에만 기록하도록 판별한다. */
    fun consumeActionLog(elapsed: Float): Boolean {
        if (!configuration.actionSampleMode || actionCaptureLogged || elapsed <= 1f) return false
        actionCaptureLogged = true
        return true
    }

    /** 대사 캡처의 상태 진단을 한 번만 기록하도록 판별한다. */
    fun consumeDialogueLog(elapsed: Float, captureAt: Float): Boolean {
        if (elapsed <= captureAt || dialogueCaptureLogged ||
            (!configuration.cutscene477Capture && configuration.dialogueStepCapture == null)
        ) return false
        dialogueCaptureLogged = true
        return true
    }

    /** 선택 영역 캡처의 상태 진단을 한 번만 기록하도록 판별한다. */
    fun consumeSelectionLog(elapsed: Float, captureAt: Float): Boolean {
        if (elapsed <= captureAt || !configuration.selectionOverlayCapture || selectionCaptureLogged) return false
        selectionCaptureLogged = true
        return true
    }
}

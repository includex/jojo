// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.battle.route.BattlePresentationConfiguration

/** 전투 캡처 fixture 조정기: 대사·모달·액션·선택 영역 캡처의 시간 정책과 화면 어댑터 호출을 한곳에서 관리한다. */
internal class BattleCaptureFixtureCoordinator(
    private val configuration: BattleCaptureFixtureConfiguration,
    private val routeConfiguration: BattlePresentationConfiguration,
) {
    /** 캡처 프레임 입력: 현재 경과 시간과 대사 표시 상태를 화면에서 전달한다. */
    data class Frame(
        val elapsed: Float,
        val dialogueState: PlaybackState,
        val dialogueVisible: Boolean,
        val dialogueComplete: Boolean,
    )

    /** 화면 캡처 어댑터: 조정기가 필요로 하는 대사 진행과 현재 스크립트 상태만 노출한다. */
    interface Port {
        /** 컷신·모달 캡처용 대사를 원본처럼 한 단계 진행한다. */
        fun advanceFixtureDialogue()

        /** 일반 대사 단계 캡처의 사용자 입력 흐름을 진행한다. */
        fun advanceDialogueStep()

        /** 현재 스크립트 재생 상태를 반환한다. */
        fun playbackState(): PlaybackState

        /** 현재 대사 화자 식별자를 반환한다. */
        fun dialogueSpeakerId(): String?
    }

    private val timeline = BattleCaptureFixtureTimeline(configuration, routeConfiguration)
    private var modalOpeningSayDismissed = false

    /** fixture 갱신: 현재 프레임에서 필요한 컷신·대사·모달 진행을 화면 어댑터에 순서대로 요청한다. */
    fun update(frame: Frame, port: Port) {
        timeline.dialogueAdvances(frame.toTimelineFrame()).forEach { advance ->
            when (advance) {
                BattleCaptureFixtureTimeline.DialogueAdvance.CUTSCENE_ATTACK -> {
                    port.advanceFixtureDialogue()
                    check(port.playbackState() == PlaybackState.DELAY) {
                        "영천 공격 캡처가 공격 대기 상태에 진입하지 않았습니다: ${port.playbackState()}"
                    }
                }

                BattleCaptureFixtureTimeline.DialogueAdvance.CUTSCENE_477 -> {
                    port.advanceFixtureDialogue()
                    check(port.dialogueSpeakerId() == "477") {
                        "영천 477 캡처가 다음 원본 대사에 도달하지 않았습니다: ${port.dialogueSpeakerId()}"
                    }
                }

                BattleCaptureFixtureTimeline.DialogueAdvance.DIALOGUE_STEP -> port.advanceDialogueStep()
            }
        }
        if (configuration.modalRenderCapture && !modalOpeningSayDismissed && frame.elapsed >= MODAL_DISMISS_AT &&
            port.playbackState() == PlaybackState.DIALOGUE
        ) {
            port.advanceFixtureDialogue()
            check(port.playbackState() == PlaybackState.DELAY) {
                "모달 원본 렌더 캡처가 opening SayLayer 뒤의 delay에 진입하지 않았습니다: ${port.playbackState()}"
            }
            modalOpeningSayDismissed = true
        }
    }

    /** 캡처 시각 계산: 현재 대사 상태에 맞는 fixture의 첫 안정 프레임을 반환한다. */
    fun captureAt(frame: Frame): Float = timeline.captureAt(frame.toTimelineFrame())

    /** 액션 진단 기록 소비: 액션 표본의 상태 로그를 이번 프레임에 한 번만 허용한다. */
    fun consumeActionLog(elapsed: Float): Boolean = timeline.consumeActionLog(elapsed)

    /** 대사 진단 기록 소비: 대사 fixture의 상태 로그를 이번 프레임에 한 번만 허용한다. */
    fun consumeDialogueLog(elapsed: Float, captureAt: Float): Boolean = timeline.consumeDialogueLog(elapsed, captureAt)

    /** 선택 영역 진단 기록 소비: 선택 overlay의 상태 로그를 이번 프레임에 한 번만 허용한다. */
    fun consumeSelectionLog(elapsed: Float, captureAt: Float): Boolean = timeline.consumeSelectionLog(elapsed, captureAt)

    /** 시간선 입력 변환: 화면 입력을 기존 캡처 시간 정책이 소비하는 형태로 바꾼다. */
    private fun Frame.toTimelineFrame() = BattleCaptureFixtureTimeline.Frame(
        elapsed = elapsed,
        dialogueState = dialogueState,
        dialogueVisible = dialogueVisible,
        dialogueComplete = dialogueComplete,
    )

    private companion object {
        const val MODAL_DISMISS_AT = .1f
    }
}

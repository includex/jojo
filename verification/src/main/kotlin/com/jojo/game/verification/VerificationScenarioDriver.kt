// Verification
package com.jojo.game.verification

import com.jojo.game.application.runtime.RuntimeScenarioCommand
import com.jojo.game.application.runtime.RuntimeScenarioDriver
import com.jojo.game.application.runtime.RuntimeScenarioFrame
import com.jojo.game.application.runtime.RuntimeScenarioPresentation
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import com.jojo.game.application.runtime.RuntimeScenarioCommand.Present
import com.jojo.game.application.runtime.RuntimeScenarioCommand.ShowOverlay
import com.jojo.game.verification.scenario.ScenarioFixtureInstaller
import com.jojo.game.domain.scenario.PlaybackState

/** VerificationScenarioDriver: 검증 전용 재생 안정화 정책이며 경로 이름은 core 바깥에서 관리한다. */
class VerificationScenarioDriver(private val state: String?) : RuntimeScenarioDriver {
    /** presentationSent: 검증 대상의 현재 상태 값을 담는다. */
    private var presentationSent = false

    /** commands: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    override fun commands(frame: RuntimeScenarioFrame): List<RuntimeScenarioCommand> {
        presentationCommand()?.let { command ->
            if (!presentationSent) {
                presentationSent = true
                return listOf(command)
            }
        }
        if (state !in settlingStates) return emptyList()
        return when (frame.playback) {
            PlaybackState.DIALOGUE -> listOf(RuntimeScenarioCommand.AdvanceDialogue)
            PlaybackState.MODAL -> listOf(RuntimeScenarioCommand.ResumeModal)
            PlaybackState.DELAY -> listOf(RuntimeScenarioCommand.SkipDelay)
            PlaybackState.CHOICE -> listOf(RuntimeScenarioCommand.ConfirmChoice)
            PlaybackState.COMPLETE -> emptyList()
        }
    }

    private companion object {
        /**
         * `settlingStates` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val settlingStates = setOf("scenario-dialogue", "map-info")
        /**
         * `streetStages` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val streetStages = listOf("panel", "portrait", "speaker", "text", "background", "characters")
        /**
         * `overlayNames` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val overlayNames = RuntimeScenarioOverlay.values().associateBy { it.name.lowercase().replace('_', '-') }
    }

    /** presentationCommand: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun presentationCommand(): RuntimeScenarioCommand? = when (state) {
        "hall-palace-fixture" -> Present(RuntimeScenarioPresentation.PALACE, scene = ScenarioFixtureInstaller.palaceScene())
        "hall-section-fixture" -> Present(RuntimeScenarioPresentation.SECTION, scene = ScenarioFixtureInstaller.sectionScene())
        // 거리 대화 단계는 원본 하네스가 세워 두는 회관 장면과 같은 대사를 보여야 한다.
        // 장면을 넘기지 않으면 이식본은 살아 있는 R_00 대사를 그대로 이어 그려서
        // 화자와 본문이 원본과 다른 문장이 된다.
        else -> state?.removePrefix("street-")?.let(streetStages::indexOf)
            ?.takeIf { it >= 0 }
            ?.let { Present(RuntimeScenarioPresentation.STREET, it, scene = ScenarioFixtureInstaller.hallScene()) }
            ?: overlayCommand()
    }

    /** overlayCommand: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun overlayCommand(): RuntimeScenarioCommand? = state
        ?.removePrefix("hall-")
        ?.removeSuffix("-fixture")
        ?.let { name -> overlayNames[name]?.let { ShowOverlay(it, ScenarioFixtureInstaller.scene(it).copy(modal = ScenarioFixtureInstaller.modal(it))) } }
        ?: state?.let { name -> overlayNames[name]?.let { ShowOverlay(it, ScenarioFixtureInstaller.scene(it).copy(modal = ScenarioFixtureInstaller.modal(it))) } }

}

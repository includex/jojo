// Scenario Trace
package com.jojo.game.presentation.scenario.trace

import com.jojo.game.application.runtime.RuntimeScenarioCommand
import com.jojo.game.application.runtime.RuntimeScenarioCommand.Present
import com.jojo.game.application.runtime.RuntimeScenarioCommand.ShowOverlay
import com.jojo.game.application.runtime.RuntimeScenarioDriver
import com.jojo.game.application.runtime.RuntimeScenarioFrame
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import com.jojo.game.application.runtime.RuntimeScenarioPresentation
import com.jojo.game.application.runtime.RuntimeScenarioScene
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.application.scenario.ScenarioChoiceTrace
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioRandomTrace
import com.jojo.game.domain.scenario.PlaybackState

/** ScenarioRandomTraceConfiguration: 검증 실행이 시나리오 난수 추적 뒤에 멈추는 조건을 보관한다. */
data class ScenarioRandomTraceConfiguration(
    val stopAfterNextTrace: Boolean,
    val stopAfterTraceCount: Int?,
) {
    /** interpreter에 count 우선의 기존 난수 추적 중단 정책을 적용한다. */
    fun applyTo(interpreter: ScenarioInterpreter) = configure(
        stopAfterTraceCount = interpreter::stopAfterRandomTrace,
        stopAfterNextTrace = interpreter::stopAfterNextRandomTrace,
    )

    /** 테스트와 adapter가 공유하는 난수 추적 중단 정책을 실행한다. */
    internal fun configure(
        stopAfterTraceCount: (Int) -> Unit,
        stopAfterNextTrace: () -> Unit,
    ) {
        when {
            this.stopAfterTraceCount != null -> stopAfterTraceCount(this.stopAfterTraceCount)
            this.stopAfterNextTrace -> stopAfterNextTrace()
        }
    }
}

/** ScenarioRuntimeTraceProbeInput: Screen UI 상태를 런타임 검증 관측값으로 옮길 불변 snapshot이다. */
internal data class ScenarioRuntimeTraceProbeInput(
    val module: String,
    val elapsedSeconds: Float,
    val playback: PlaybackState,
    val options: List<String>,
    val selectedChoice: Int,
    val sceneIndex: Int,
    val startedScenes: List<Int>,
    val backgroundId: Int,
    val unitIds: Set<Int>,
    val campaignStage: Int,
    val menuVisible: Boolean,
    val dialogueText: String?,
    val hallBattleScenePending: Boolean,
    val battleButtonScreenX: Int,
    val battleButtonScreenY: Int,
    val choiceTrace: List<ScenarioChoiceTrace>,
    val randomTrace: List<ScenarioRandomTrace>,
    val randomDrawCount: Int,
    val remainingInjectedRandomCount: Int,
) {
    /** 기존 verification observer가 소비하는 runtime probe 형식으로 변환한다. */
    fun toRuntimeProbe(): ScenarioRuntimeProbe = ScenarioRuntimeProbe(
        module = module,
        elapsedSeconds = elapsedSeconds,
        playback = playback,
        options = options,
        selectedChoice = selectedChoice,
        sceneIndex = sceneIndex,
        startedScenes = startedScenes,
        backgroundId = backgroundId,
        unitIds = unitIds,
        campaignStage = campaignStage,
        menuVisible = menuVisible,
        dialogueText = dialogueText,
        hallBattleScenePending = hallBattleScenePending,
        battleButtonScreenX = battleButtonScreenX,
        battleButtonScreenY = battleButtonScreenY,
        choiceTrace = choiceTrace,
        randomTrace = randomTrace,
        randomDrawCount = randomDrawCount,
        remainingInjectedRandomCount = remainingInjectedRandomCount,
    )
}

/** ScenarioRuntimeTraceCoordinator: runtime driver 명령·verification 판별·probe 조립을 Screen Port 밖으로 분리한다. */
internal class ScenarioRuntimeTraceCoordinator(
    private val driver: RuntimeScenarioDriver?,
    private val port: Port,
) {
    /** Port: coordinator가 필요한 Screen UI snapshot과 최소 동작만 노출한다. */
    internal interface Port {
        fun runtimeFrame(): RuntimeScenarioFrame
        fun runtimeProbeInput(): ScenarioRuntimeTraceProbeInput
        fun keepsScenarioOpen(): Boolean
        fun playbackState(): PlaybackState
        fun applyPresentation(mode: RuntimeScenarioPresentation, detail: Int, scene: RuntimeScenarioScene)
        fun showOverlay(overlay: RuntimeScenarioOverlay, scene: RuntimeScenarioScene)
        fun advanceDialogue()
        fun resumeModal()
        fun skipDelay()
        fun confirmChoice()
        fun resetDialogueReveal()
    }

    /** runtime observer가 화면을 유지하도록 요청했는지 반환한다. */
    fun isVerificationRun(): Boolean = port.keepsScenarioOpen()

    /** 현재 UI snapshot을 observer가 소비하는 안정된 scenario probe로 조립한다. */
    fun runtimeProbe(): ScenarioRuntimeProbe = port.runtimeProbeInput().toRuntimeProbe()

    /** 설치된 driver 명령을 기존 playback-state guard와 함께 Screen Port에 적용한다. */
    fun applyRuntimeCommands() {
        driver?.commands(port.runtimeFrame()).orEmpty().forEach { command ->
            when (command) {
                is Present -> port.applyPresentation(command.presentation, command.detail, command.scene)
                is ShowOverlay -> port.showOverlay(command.overlay, command.scene)
                is RuntimeScenarioCommand.SetPresentation -> port.applyPresentation(command.mode, command.detail, RuntimeScenarioScene())
                RuntimeScenarioCommand.AdvanceDialogue -> if (port.playbackState() == PlaybackState.DIALOGUE) port.advanceDialogue()
                RuntimeScenarioCommand.ResumeModal -> if (port.playbackState() == PlaybackState.MODAL) port.resumeModal()
                RuntimeScenarioCommand.SkipDelay -> if (port.playbackState() == PlaybackState.DELAY) port.skipDelay()
                RuntimeScenarioCommand.ConfirmChoice -> if (port.playbackState() == PlaybackState.CHOICE) port.confirmChoice()
                RuntimeScenarioCommand.RevealDialogue -> port.resetDialogueReveal()
            }
        }
    }
}

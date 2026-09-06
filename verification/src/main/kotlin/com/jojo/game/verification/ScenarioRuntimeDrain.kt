package com.jojo.game.verification
import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.PlaybackState

internal object ScenarioRuntimeDrain {
    /** 지연 상태를 제한된 횟수만큼 건너뛰어 안정 상태로 만든다. */
    fun settleTimedDelay(runtime: ScenarioInterpreter, limit: Int = 10_000) {
        var steps = 0
        while (runtime.state == PlaybackState.DELAY && steps++ < limit) runtime.skipDelay()
        check(runtime.state != PlaybackState.DELAY) { "timed scenario state did not settle" }
    }

    /** 대화·선택·모달 단계를 진행해 시나리오를 완료 상태로 만든다. */
    fun toCompletion(
        runtime: ScenarioInterpreter,
        limit: Int = 10_000,
        chooseGameStart: Boolean = false,
        failureMessage: String,
    ) {
        var steps = 0
        while (runtime.state != PlaybackState.COMPLETE && steps++ < limit) {
            when (runtime.state) {
                PlaybackState.DIALOGUE -> runtime.advanceDialogue()
                PlaybackState.CHOICE -> {
                    if (chooseGameStart) {
                        runtime.currentChoice?.options
                            ?.indexOfFirst { it.contains("게임 시작") }
                            ?.takeIf { it >= 0 }
                            ?.let(runtime::selectChoice)
                    }
                    runtime.confirmChoice()
                }
                PlaybackState.DELAY -> runtime.skipDelay()
                PlaybackState.MODAL -> runtime.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
        check(runtime.state == PlaybackState.COMPLETE) { failureMessage }
    }
}

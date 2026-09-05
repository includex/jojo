package com.jojo.game.verification

import com.jojo.game.PlaybackState
import com.jojo.game.ScenarioInterpreter

internal object ScenarioRuntimeDrain {
    fun settleTimedDelay(runtime: ScenarioInterpreter, limit: Int = 10_000) {
        var steps = 0
        while (runtime.state == PlaybackState.DELAY && steps++ < limit) runtime.skipDelay()
        check(runtime.state != PlaybackState.DELAY) { "timed scenario state did not settle" }
    }

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

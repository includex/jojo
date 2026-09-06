// Verification
package com.jojo.game.verification
import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.PlaybackState

/** ScenarioRuntimeDrain: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal object ScenarioRuntimeDrain {
    /** settleTimedDelay: 지연 상태를 제한된 횟수만큼 건너뛰어 안정 상태로 만든다. */
    fun settleTimedDelay(runtime: ScenarioInterpreter, limit: Int = 10_000) {
        var steps = 0
        while (runtime.state == PlaybackState.DELAY && steps++ < limit) runtime.skipDelay()
        check(runtime.state != PlaybackState.DELAY) { "timed scenario state did not settle" }
    }

    /** toCompletion: 대화·선택·모달 단계를 진행해 시나리오를 완료 상태로 만든다. */
    fun toCompletion(
        runtime: ScenarioInterpreter,
        limit: Int = 10_000,
        chooseGameStart: Boolean = false,
        failureMessage: String,
    ) {
        /**
         * `steps` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

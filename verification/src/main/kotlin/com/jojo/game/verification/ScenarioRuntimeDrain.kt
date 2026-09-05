package com.jojo.game.verification
import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.PlaybackState

internal object ScenarioRuntimeDrain {
/**
 * 공개 메서드 `settleTimedDelay`
 *
 * ### 파라미터
- `runtime` (`ScenarioInterpreter`): 구현 기준으로 역할 및 허용 값 정의 필요
- `limit` (`Int = 10_000`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

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

// Scenario
package com.jojo.game.domain.scenario

import com.jojo.game.domain.scenario.*

/** ScenarioCompletionRoute: 완료한 시나리오와 다음 진입 시나리오를 연결하는 캠페인 진행 규칙이다. */
object ScenarioCompletionRoute {
    fun shouldRoute(
        state: PlaybackState,
        menuVisible: Boolean,
        endedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = state == PlaybackState.COMPLETE &&
            (endedByScript || sceneJumpTarget != null || !menuVisible)
}

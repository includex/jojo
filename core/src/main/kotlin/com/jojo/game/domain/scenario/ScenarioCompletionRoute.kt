// Scenario
package com.jojo.game.domain.scenario

import com.jojo.game.domain.scenario.*

/** ScenarioCompletionRoute: 완료한 시나리오와 다음 진입 시나리오를 연결하는 캠페인 진행 규칙이다. */
object ScenarioCompletionRoute {
    /**
     * `shouldRoute`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun shouldRoute(
        state: PlaybackState,
        menuVisible: Boolean,
        endedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = state == PlaybackState.COMPLETE &&
            (endedByScript || sceneJumpTarget != null || !menuVisible)
}

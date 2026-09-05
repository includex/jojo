package com.jojo.game

import com.jojo.game.domain.scenario.*

/** Source HallLayer `_scriptOver` routing decision after a recovered R script returns. */
object ScenarioCompletionRoute {
    fun shouldRoute(
        state: PlaybackState,
        menuVisible: Boolean,
        endedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = state == PlaybackState.COMPLETE &&
            (endedByScript || sceneJumpTarget != null || !menuVisible)
}

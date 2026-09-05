package com.jojo.game.application.runtime

import com.badlogic.gdx.Screen

/** Immutable artifact request emitted at the application boundary. */
sealed interface RuntimeArtifactEvent {
    val state: String?

    data class Frame(
        override val state: String?,
        val screen: Screen?,
    ) : RuntimeArtifactEvent

    data class EventLog(
        override val state: String?,
        val screen: Screen?,
    ) : RuntimeArtifactEvent

    data class MapSidecar(override val state: String?) : RuntimeArtifactEvent

    data class OverlayStack(
        override val state: String?,
        val requested: String,
        val requestedPresent: Boolean,
        val dialogue: Boolean,
        val choice: Boolean,
        val modalCount: Int,
    ) : RuntimeArtifactEvent
}

/** Optional external sink for immutable artifact requests. */
interface RuntimeArtifactObserver {
    val wantsFrame: Boolean get() = false
    val wantsEventLog: Boolean get() = false
    fun onArtifact(event: RuntimeArtifactEvent)

    /** Called after a frame has been presented; external runtimes own timing. */
    fun onFrame(screen: Screen?, probe: RuntimeScreenProbe) = Unit
}

/** Neutral battle frame feed; consumers decide whether and how to persist it. */
data class RuntimeBattleFrameSnapshot(
    val frame: Long,
    val elapsed: Float,
    val delta: Float,
    val payload: String,
)

data class RuntimeBattleCompletion(
    val reason: String,
    val frameCount: Long,
    val payload: String? = null,
    val exitRequested: Boolean = false,
)

interface RuntimeBattleObserver {
    fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {}
    fun onCompleted(completion: RuntimeBattleCompletion) {}
}

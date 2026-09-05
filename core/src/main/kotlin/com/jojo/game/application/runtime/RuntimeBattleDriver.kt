package com.jojo.game.application.runtime

/** Immutable frame input exposed to an optional external battle driver. */
data class RuntimeBattleFrame(
    val delta: Float,
    val elapsed: Float,
)

/** Neutral commands an external runtime may request after observing a frame. */
sealed interface RuntimeBattleCommand {
    data object AdvanceDialogue : RuntimeBattleCommand
    data class Tap(val x: Float, val y: Float) : RuntimeBattleCommand
    data object EndTurn : RuntimeBattleCommand
}

/** Optional external input driver; ordinary gameplay remains driver-free. */
fun interface RuntimeBattleDriver {
    fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand>
}

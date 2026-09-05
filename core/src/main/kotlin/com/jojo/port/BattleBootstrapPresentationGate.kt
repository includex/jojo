package com.jojo.port

/** Callback-owning stage work which may delay the UNKNOWN -> Mine hand-off. */
internal data class BattleBootstrapCallbackState(
    val move: Boolean = false,
    val attackAction: Boolean = false,
    val hide: Boolean = false,
    val show: Boolean = false,
    val fight: Boolean = false,
) {
    fun blockingReasons(): List<String> = buildList {
        if (move) add("move")
        if (attackAction) add("attackAction")
        if (hide) add("hide")
        if (show) add("show")
        if (fight) add("fight")
    }
}

/** BattleLayer._execControlScript(true)'s completion callback. */
internal fun completeInitialBattleOperation(stage: ScenarioStage) {
    if (!stage.battleOperationStarted) stage.startOperation()
}

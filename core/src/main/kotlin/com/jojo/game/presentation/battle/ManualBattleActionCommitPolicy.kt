package com.jojo.game.presentation.battle

/** Keeps a manually selected action deferred until its source combat presentation has finished. */
internal object ManualBattleActionCommitPolicy {
    fun shouldCommit(
        pendingScriptPasses: Int,
        actionCommitted: Boolean,
        scriptComplete: Boolean,
        combatPresentationBusy: Boolean,
    ): Boolean = pendingScriptPasses > 0 && !actionCommitted && scriptComplete && !combatPresentationBusy
}

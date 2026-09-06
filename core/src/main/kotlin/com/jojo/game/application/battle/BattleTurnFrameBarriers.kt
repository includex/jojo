package com.jojo.game.application.battle

internal class EmptyAiCampFrameBarrier {
    private var pending = false


    fun begin(hasActor: Boolean) {
        pending = !hasActor
    }


    fun yieldEntryFrame(): Boolean {
        if (!pending) return false
        pending = false
        return true
    }
}

internal class CommittedPlayerMoveFrameBarrier {
    private var exposed = false


    fun beginActor() {
        exposed = false
    }


    fun yieldCompletionFrame(isPlayer: Boolean, moved: Boolean): Boolean {
        if (exposed || !isPlayer || !moved) return false
        exposed = true
        return true
    }
}

internal class ActionStatusFrameBarrier {
    private var settlementExposed = false


    fun beginActor() {
        settlementExposed = false
    }

    /**
     * After `_jiesuan(g_charinfo)` has synchronously published XD, source
     * returns to the `_ai2` generator scheduler before selecting the next
     * actor.  Keep that settled actor observable in the current episode;
     * otherwise the next actor's decision and the previous actor's XD edge
     * are sampled in one game frame and the state edge is attributed to the
     * wrong episode.
     */

    fun yieldAfterCommit(hasAction: Boolean): Boolean {
        if (settlementExposed || !hasAction) return false
        settlementExposed = true
        return true
    }
}

internal class CounterattackSettlementFrameBarrier {
    private var idleFramePending = false


    fun beginActor(hasPhysicalCounter: Boolean) {
        idleFramePending = hasPhysicalCounter
    }


    fun yieldIdleBeforeCommit(): Boolean {
        if (!idleFramePending) return false
        idleFramePending = false
        return true
    }
}

internal class ScriptedMovementCampTransitionFrameBarrier {
    private var completedMoveFramePending = false

    fun observe(
        inCampScript: Boolean,
        scriptWasPending: Boolean,
        scriptCompleted: Boolean,
        movementWasActive: Boolean,
        movementIsActive: Boolean,
    ) {
        if (inCampScript && scriptWasPending && scriptCompleted && movementWasActive && !movementIsActive) {
            completedMoveFramePending = true
        }
    }


    fun yieldBeforeCampTransition(): Boolean {
        if (!completedMoveFramePending) return false
        completedMoveFramePending = false
        return true
    }
}

internal class ConsecutiveNoResultFrameGate {
    private var completedInCurrentRender = false


    fun beginRender() {
        completedInCurrentRender = false
    }


    fun markCompleted() {
        completedInCurrentRender = true
    }


    fun shouldYieldBefore(nextIsNoResult: Boolean): Boolean =
        completedInCurrentRender && nextIsNoResult
}

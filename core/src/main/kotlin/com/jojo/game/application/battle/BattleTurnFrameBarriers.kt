// Battle
package com.jojo.game.application.battle

/** EmptyAiCampFrameBarrier: 행동 유닛이 없는 AI 진영에서 한 프레임을 보존하는 동기화 장벽이다. */
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

/** CommittedPlayerMoveFrameBarrier: 플레이어 이동 확정 뒤 완료 상태를 한 프레임 노출하는 장벽이다. */
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

/** ActionStatusFrameBarrier: 행동 상태 정산 결과를 다음 처리 전에 한 프레임 노출하는 장벽이다. */
internal class ActionStatusFrameBarrier {
    private var settlementExposed = false


    fun beginActor() {
        settlementExposed = false
    }

    /** yieldAfterCommit: 행동 확정 결과를 아직 노출하지 않았을 때 단일 대기 프레임을 요청한다. */

    fun yieldAfterCommit(hasAction: Boolean): Boolean {
        if (settlementExposed || !hasAction) return false
        settlementExposed = true
        return true
    }
}

/** CounterattackSettlementFrameBarrier: 물리 반격 정산 전의 유휴 프레임을 보존하는 장벽이다. */
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

/** ScriptedMovementCampTransitionFrameBarrier: 스크립트 이동 완료와 진영 전환 사이의 표시 프레임을 보존한다. */
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

/** ConsecutiveNoResultFrameGate: 같은 렌더 주기에서 연속된 무결과 처리가 겹치지 않도록 제한한다. */
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

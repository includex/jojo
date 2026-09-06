// Battle
package com.jojo.game.application.battle

/** EmptyAiCampFrameBarrier: 행동 유닛이 없는 AI 진영에서 한 프레임을 보존하는 동기화 장벽이다. */
internal class EmptyAiCampFrameBarrier {
    /**
     * `pending` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var pending = false


    /**
     * `begin`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun begin(hasActor: Boolean) {
        pending = !hasActor
    }


    /**
     * `yieldEntryFrame`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun yieldEntryFrame(): Boolean {
        if (!pending) return false
        pending = false
        return true
    }
}

/** CommittedPlayerMoveFrameBarrier: 플레이어 이동 확정 뒤 완료 상태를 한 프레임 노출하는 장벽이다. */
internal class CommittedPlayerMoveFrameBarrier {
    /**
     * `exposed` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var exposed = false


    /**
     * `beginActor`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun beginActor() {
        exposed = false
    }


    /**
     * `yieldCompletionFrame`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun yieldCompletionFrame(isPlayer: Boolean, moved: Boolean): Boolean {
        if (exposed || !isPlayer || !moved) return false
        exposed = true
        return true
    }
}

/** ActionStatusFrameBarrier: 행동 상태 정산 결과를 다음 처리 전에 한 프레임 노출하는 장벽이다. */
internal class ActionStatusFrameBarrier {
    /**
     * `settlementExposed` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var settlementExposed = false


    /**
     * `beginActor`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
    /**
     * `idleFramePending` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var idleFramePending = false


    /**
     * `beginActor`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun beginActor(hasPhysicalCounter: Boolean) {
        idleFramePending = hasPhysicalCounter
    }


    /**
     * `yieldIdleBeforeCommit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun yieldIdleBeforeCommit(): Boolean {
        if (!idleFramePending) return false
        idleFramePending = false
        return true
    }
}

/** ScriptedMovementCampTransitionFrameBarrier: 스크립트 이동 완료와 진영 전환 사이의 표시 프레임을 보존한다. */
internal class ScriptedMovementCampTransitionFrameBarrier {
    /**
     * `completedMoveFramePending` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var completedMoveFramePending = false

    /**
     * `observe`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `yieldBeforeCampTransition`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun yieldBeforeCampTransition(): Boolean {
        if (!completedMoveFramePending) return false
        completedMoveFramePending = false
        return true
    }
}

/** ConsecutiveNoResultFrameGate: 같은 렌더 주기에서 연속된 무결과 처리가 겹치지 않도록 제한한다. */
internal class ConsecutiveNoResultFrameGate {
    /**
     * `completedInCurrentRender` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var completedInCurrentRender = false


    /**
     * `beginRender`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun beginRender() {
        completedInCurrentRender = false
    }


    /**
     * `markCompleted`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun markCompleted() {
        completedInCurrentRender = true
    }


    /**
     * `shouldYieldBefore`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun shouldYieldBefore(nextIsNoResult: Boolean): Boolean =
        completedInCurrentRender && nextIsNoResult
}

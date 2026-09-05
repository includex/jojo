package com.jojo.game.application.battle

internal class EmptyAiCampFrameBarrier {
    private var pending = false

    /**
     * 공개 메서드 `begin`
     *
     * ### 파라미터
    - `hasActor` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun begin(hasActor: Boolean) {
        pending = !hasActor
    }

    /**
     * 공개 메서드 `yieldEntryFrame`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldEntryFrame(): Boolean {
        if (!pending) return false
        pending = false
        return true
    }
}

internal class CommittedPlayerMoveFrameBarrier {
    private var exposed = false

    /**
     * 공개 메서드 `beginActor`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun beginActor() {
        exposed = false
    }

    /**
     * 공개 메서드 `yieldCompletionFrame`
     *
     * ### 파라미터
    - `isPlayer` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `moved` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldCompletionFrame(isPlayer: Boolean, moved: Boolean): Boolean {
        if (exposed || !isPlayer || !moved) return false
        exposed = true
        return true
    }
}

internal class ActionStatusFrameBarrier {
    private var settlementExposed = false

    /**
     * 공개 메서드 `beginActor`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
    /**
     * 공개 메서드 `yieldAfterCommit`
     *
     * ### 파라미터
    - `hasAction` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldAfterCommit(hasAction: Boolean): Boolean {
        if (settlementExposed || !hasAction) return false
        settlementExposed = true
        return true
    }
}

internal class CounterattackSettlementFrameBarrier {
    private var idleFramePending = false

    /**
     * 공개 메서드 `beginActor`
     *
     * ### 파라미터
    - `hasPhysicalCounter` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun beginActor(hasPhysicalCounter: Boolean) {
        idleFramePending = hasPhysicalCounter
    }

    /**
     * 공개 메서드 `yieldIdleBeforeCommit`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `yieldBeforeCampTransition`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldBeforeCampTransition(): Boolean {
        if (!completedMoveFramePending) return false
        completedMoveFramePending = false
        return true
    }
}

internal class ConsecutiveNoResultFrameGate {
    private var completedInCurrentRender = false

    /**
     * 공개 메서드 `beginRender`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun beginRender() {
        completedInCurrentRender = false
    }

    /**
     * 공개 메서드 `markCompleted`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun markCompleted() {
        completedInCurrentRender = true
    }

    /**
     * 공개 메서드 `shouldYieldBefore`
     *
     * ### 파라미터
    - `nextIsNoResult` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun shouldYieldBefore(nextIsNoResult: Boolean): Boolean =
        completedInCurrentRender && nextIsNoResult
}

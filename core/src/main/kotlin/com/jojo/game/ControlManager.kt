package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.command.*

/** Injectable implementation of recovered-js/modules/battle/ControlManager.js. */
class ControlManager(
    private val state: UnitState,
    private val factory: Factory,
) {
    /**
     * interface  `UnitState`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface UnitState {
        /**
         * 공개 메서드 `isControlled`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Boolean`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun isControlled(): Boolean

        /**
         * 공개 메서드 `ai`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun ai(): Int

        /**
         * 공개 메서드 `targetIndex`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun targetIndex(): Int

        /**
         * 공개 메서드 `targetX`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun targetX(): Int

        /**
         * 공개 메서드 `targetY`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun targetY(): Int

        /**
         * 공개 메서드 `targetExists`
         *
         * ### 파라미터
        - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Boolean`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun targetExists(index: Int): Boolean
    }

    /**
     * interface  `Factory`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface Factory {
        /**
         * 공개 메서드 `create`
         *
         * ### 파라미터
        - `ai` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Driver`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun create(ai: Int): Driver
    }

    /**
     * interface  `Driver`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface Driver {
        /**
         * 공개 메서드 `setManager`
         *
         * ### 파라미터
        - `manager` (`ControlManager`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun setManager(manager: ControlManager)

        /**
         * 공개 메서드 `setWithData`
         *
         * ### 파라미터
        - `targetIndex` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun setWithData(targetIndex: Int, x: Int, y: Int)

        /** Control.selectMovePoint result: 0 complete, 1 select another controller, 2 stop. */
        fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int
    }

    var result: Control.Result? = null
        private set

    /**
     * The actual Control subclass which completed the current selection.
     *
     * This is intentionally not [UnitState.ai].  ControlManager may retry
     * through a temporary controller (for example CtrlYDDZDDJS) without
     * changing the unit's persistent AI field.  BattleUnit.AIValue is written
     * only by CtrlZDCJ/CtrlJSYD._AIProcess4, so callers must use this value
     * rather than the persisted configuration.
     */
    var activeAi: Int? = null
        private set
    private var control: Driver? = null
    private var points: List<Control.Point> = emptyList()
    private var pointHash: Set<Control.Point> = emptySet()

    /**
     * 공개 메서드 `setResult`
     *
     * ### 파라미터
    - `value` (`Control.Result?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setResult(value: Control.Result?) {
        result = value
    }

    /** Direct Kotlin implementation of ControlManager.setControl: replace the live driver now. */
    fun setControl(ai: Int, targetIndex: Int = -1, x: Int = -1, y: Int = -1) {
        activeAi = ai
        control = factory.create(ai).also {
            it.setManager(this)
            it.setWithData(targetIndex, x, y)
        }
    }

    /**
     * Source chooses active AI for controlled units, otherwise uses saved AI
     * and passes target index only if that target still exists. It retries
     * only when a controller returns 1, at most five times.
     */
    /**
     * 공개 메서드 `selectMovePoint`
     *
     * ### 파라미터
    - `points` (`List<Control.Point>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `pointHash` (`Set<Control.Point>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int {
        this.points = points
        this.pointHash = pointHash
        var status = 0
        val ai = if (state.isControlled()) AI_ACTIVE else state.ai()
        val target = state.targetIndex().takeIf(state::targetExists) ?: -1
        setControl(ai, target, state.targetX(), state.targetY())
        repeat(5) {
            status = requireNotNull(control).selectMovePoint(this.points, this.pointHash)
            if (status != 1) return status
        }
        return status
    }

    companion object {
        /** BattleConfg.AI.ZHU_DONG_CHU_JI. */
        const val AI_ACTIVE = 1
    }
}

package com.jojo.game.domain.battle.command

/**
 * Injectable implementation of recovered-js/modules/battle/Control.js.
 *
 * The scoring implementation is supplied by the battle layer because the
 * source's `_AIProcess` reads map and hit-area services from its manager.
 * This class preserves Control's state, priority order, and manager calls.
 */
/**
 * class  `Control`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class Control {
    /**
     * data class  `Point`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Point(val x: Int, val y: Int)

    /**
     * data class  `Result`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Result(
        val x: Int,
        val y: Int,
        val targetIndex: Int? = null,
        val kind: String? = null,
        val value: Int = 0
    )

    /**
     * interface  `Manager`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface Manager {
        /**
         * 공개 메서드 `currentPoint`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Point`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun currentPoint(): Point

        /**
         * 공개 메서드 `isParalyzed`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Boolean`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun isParalyzed(): Boolean

        /** Control._isBBW: every QUN_XIONG neighbor is occupied. */
        fun isSurrounded(): Boolean

        /**
         * 공개 메서드 `setControl`
         *
         * ### 파라미터
        - `ai` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `targetIndex` (`Int = -1`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Int = -1`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Int = -1`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun setControl(ai: Int, targetIndex: Int = -1, x: Int = -1, y: Int = -1)

        /**
         * 공개 메서드 `setResult`
         *
         * ### 파라미터
        - `result` (`Result`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun setResult(result: Result)

        /** Direct dependency-injected equivalent of `_AIProcess()`. */
        fun selectByAi(): Result?
    }

    private var manager: Manager? = null
    var targetIndex: Int = -1
        private set
    var targetX: Int = -1
        private set
    var targetY: Int = -1
        private set

    /** Control.setManager. */
    fun setManager(value: Manager) {
        manager = value
    }

    /** Control.setWithData(t=-1,e=-1,r=-1). */
    fun setWithData(targetIndex: Int = -1, x: Int = -1, y: Int = -1) {
        this.targetIndex = targetIndex
        this.targetX = x
        this.targetY = y
    }

    /** Control.getUnitByTargetUnit is represented by the retained source index. */
    fun targetUnitIndex(): Int = targetIndex

    /**
     * Control.selectMovePoint: initialize with the current point, then run
     * `_process1`, `_selectMovePoint2` (currently source stub: 0), and only
     * then `_AIProcess`.
     */
    /**
     * 공개 메서드 `selectMovePoint`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectMovePoint(): Int {
        val current = requireNotNull(manager) { "Control manager has not been set" }
        current.setResult(Result(current.currentPoint().x, current.currentPoint().y))
        if (process1(current)) return 1
        current.selectByAi()?.let(current::setResult)
        return 0
    }

    /** Direct Kotlin implementation of `_process1`. */
    private fun process1(manager: Manager): Boolean {
        if (manager.isParalyzed() || manager.isSurrounded()) {
            manager.setControl(AI_JIAN_SHOU_YUAN_DI)
            return true
        }
        return false
    }

    companion object {
        /** BattleConfg.AI.JIAN_SHOU_YUAN_DI. */
        const val AI_JIAN_SHOU_YUAN_DI = 2
    }
}

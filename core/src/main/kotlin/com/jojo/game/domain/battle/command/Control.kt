// Battle
package com.jojo.game.domain.battle.command

/**
 * `Control` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class Control {

    /**
     * `Point` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Point(val x: Int, val y: Int)


    /**
     * `Result` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Result(
        /**
         * `x` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Int,
        /**
         * `y` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Int,
        /**
         * `targetIndex` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetIndex: Int? = null,
        /**
         * `kind` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val kind: String? = null,
        /**
         * `value` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Int = 0
    )


    /**
     * `Manager` 계약 인터페이스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    interface Manager {

        /**
         * `currentPoint`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun currentPoint(): Point


        /**
         * `isParalyzed`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun isParalyzed(): Boolean
        /**
         * `isSurrounded`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun isSurrounded(): Boolean


        /**
         * `setControl`: 현재 상태를 갱신한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun setControl(ai: Int, targetIndex: Int = -1, x: Int = -1, y: Int = -1)


        /**
         * `setResult`: 현재 상태를 갱신한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun setResult(result: Result)

        /** selectByAi: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
        fun selectByAi(): Result?
    }

    /**
     * `manager` (Manager?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var manager: Manager? = null
    /**
     * `targetIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var targetIndex: Int = -1
        private set
    /**
     * `targetX` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var targetX: Int = -1
        private set
    /**
     * `targetY` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var targetY: Int = -1
        private set
    /**
     * `setManager`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setManager(value: Manager) {
        manager = value
    }
    /**
     * `setWithData`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setWithData(targetIndex: Int = -1, x: Int = -1, y: Int = -1) {
        this.targetIndex = targetIndex
        this.targetX = x
        this.targetY = y
    }
    /**
     * `targetUnitIndex`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun targetUnitIndex(): Int = targetIndex

    /** selectMovePoint: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */

    fun selectMovePoint(): Int {
        val current = requireNotNull(manager) { "Control manager has not been set" }
        current.setResult(Result(current.currentPoint().x, current.currentPoint().y))
        if (process1(current)) return 1
        current.selectByAi()?.let(current::setResult)
        return 0
    }
    /**
     * `process1`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun process1(manager: Manager): Boolean {
        if (manager.isParalyzed() || manager.isSurrounded()) {
            manager.setControl(AI_JIAN_SHOU_YUAN_DI)
            return true
        }
        return false
    }

    companion object {
                /**
                 * `AI_JIAN_SHOU_YUAN_DI` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
                 */

                const val AI_JIAN_SHOU_YUAN_DI = 2
    }
}

// Battle
package com.jojo.game.domain.battle.command

import com.jojo.game.domain.battle.*
/**
 * `ControlManager` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class ControlManager(
    /**
     * `state` (UnitState,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val state: UnitState,
    /**
     * `factory` (Factory,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val factory: Factory,
) {

    /**
     * `UnitState` 계약 인터페이스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    interface UnitState {

        /**
         * `isControlled`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun isControlled(): Boolean


        /**
         * `ai`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun ai(): Int


        /**
         * `targetIndex`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun targetIndex(): Int


        /**
         * `targetX`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun targetX(): Int


        /**
         * `targetY`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun targetY(): Int


        /**
         * `targetExists`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun targetExists(index: Int): Boolean
    }


    /**
     * `Factory` 계약 인터페이스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    interface Factory {

        /**
         * `create`: 필요한 객체나 결과를 생성한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun create(ai: Int): Driver
    }


    /**
     * `Driver` 계약 인터페이스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    interface Driver {

        /**
         * `setManager`: 현재 상태를 갱신한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun setManager(manager: ControlManager)


        /**
         * `setWithData`: 현재 상태를 갱신한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun setWithData(targetIndex: Int, x: Int, y: Int)

        /** selectMovePoint: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
        fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int
    }

    /**
     * `result` (Control.Result?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var result: Control.Result? = null
        private set
    /**
     * `activeAi` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var activeAi: Int? = null
        private set
    /**
     * `control` (Driver?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var control: Driver? = null
    /**
     * `points` (List<Control.Point>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var points: List<Control.Point> = emptyList()
    /**
     * `pointHash` (Set<Control.Point>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var pointHash: Set<Control.Point> = emptySet()


    /**
     * `setResult`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setResult(value: Control.Result?) {
        result = value
    }
    /**
     * `setControl`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setControl(ai: Int, targetIndex: Int = -1, x: Int = -1, y: Int = -1) {
        activeAi = ai
        control = factory.create(ai).also {
            it.setManager(this)
            it.setWithData(targetIndex, x, y)
        }
    }

    /** selectMovePoint: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */

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
                /**
                 * `AI_ACTIVE` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
                 */

                const val AI_ACTIVE = 1
    }
}

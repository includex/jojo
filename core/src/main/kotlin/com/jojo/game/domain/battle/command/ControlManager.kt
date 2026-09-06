// Battle
package com.jojo.game.domain.battle.command

import com.jojo.game.domain.battle.*
class ControlManager(
    private val state: UnitState,
    private val factory: Factory,
) {

    interface UnitState {

        fun isControlled(): Boolean


        fun ai(): Int


        fun targetIndex(): Int


        fun targetX(): Int


        fun targetY(): Int


        fun targetExists(index: Int): Boolean
    }


    interface Factory {

        fun create(ai: Int): Driver
    }


    interface Driver {

        fun setManager(manager: ControlManager)


        fun setWithData(targetIndex: Int, x: Int, y: Int)

        /** selectMovePoint: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
        fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int
    }

    var result: Control.Result? = null
        private set
    var activeAi: Int? = null
        private set
    private var control: Driver? = null
    private var points: List<Control.Point> = emptyList()
    private var pointHash: Set<Control.Point> = emptySet()


    fun setResult(value: Control.Result?) {
        result = value
    }
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
                const val AI_ACTIVE = 1
    }
}

// Battle
package com.jojo.game.domain.battle.command

class Control {

    data class Point(val x: Int, val y: Int)


    data class Result(
        val x: Int,
        val y: Int,
        val targetIndex: Int? = null,
        val kind: String? = null,
        val value: Int = 0
    )


    interface Manager {

        fun currentPoint(): Point


        fun isParalyzed(): Boolean
        fun isSurrounded(): Boolean


        fun setControl(ai: Int, targetIndex: Int = -1, x: Int = -1, y: Int = -1)


        fun setResult(result: Result)

        /** selectByAi: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
        fun selectByAi(): Result?
    }

    private var manager: Manager? = null
    var targetIndex: Int = -1
        private set
    var targetX: Int = -1
        private set
    var targetY: Int = -1
        private set
    fun setManager(value: Manager) {
        manager = value
    }
    fun setWithData(targetIndex: Int = -1, x: Int = -1, y: Int = -1) {
        this.targetIndex = targetIndex
        this.targetX = x
        this.targetY = y
    }
    fun targetUnitIndex(): Int = targetIndex

    /** selectMovePoint: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */

    fun selectMovePoint(): Int {
        val current = requireNotNull(manager) { "Control manager has not been set" }
        current.setResult(Result(current.currentPoint().x, current.currentPoint().y))
        if (process1(current)) return 1
        current.selectByAi()?.let(current::setResult)
        return 0
    }
    private fun process1(manager: Manager): Boolean {
        if (manager.isParalyzed() || manager.isSurrounded()) {
            manager.setControl(AI_JIAN_SHOU_YUAN_DI)
            return true
        }
        return false
    }

    companion object {
                const val AI_JIAN_SHOU_YUAN_DI = 2
    }
}

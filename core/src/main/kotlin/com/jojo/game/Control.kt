package com.jojo.game

/**
 * Injectable implementation of recovered-js/modules/battle/Control.js.
 *
 * The scoring implementation is supplied by the battle layer because the
 * source's `_AIProcess` reads map and hit-area services from its manager.
 * This class preserves Control's state, priority order, and manager calls.
 */
class Control {
    data class Point(val x: Int, val y: Int)
    data class Result(val x: Int, val y: Int, val targetIndex: Int? = null, val kind: String? = null, val value: Int = 0)

    interface Manager {
        fun currentPoint(): Point
        fun isParalyzed(): Boolean
        /** Control._isBBW: every QUN_XIONG neighbor is occupied. */
        fun isSurrounded(): Boolean
        fun setControl(ai: Int, targetIndex: Int = -1, x: Int = -1, y: Int = -1)
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
    fun setManager(value: Manager) { manager = value }

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

package com.jojo.port

/** Injectable port of recovered-js/modules/battle/ControlManager.js. */
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

    fun setResult(value: Control.Result?) { result = value }

    /** Direct port of ControlManager.setControl: replace the live driver now. */
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

package com.jojo.game

/**
 * Injectable, side-effect-free control policies for the ten subclasses created in
 * BattleScreen.onCreate.  The recovered game keeps one instance of each class
 * in `_controls`; a ControlManager changes the current instance and retries
 * when a controller returns 1.  This file deliberately represents that
 * protocol as values, so every branch can be compared without a Cocos scene.
 */
object ControlAi {
    const val PASSIVE = 0 // CtrlBDCJ
    const val ACTIVE = 1 // CtrlZDCJ
    const val HOLD = 2 // CtrlJSYD
    const val ATTACK_UNIT = 3 // CtrlGJWJ
    const val GO_TO = 4 // CtrlDZDD
    const val FOLLOW = 5 // CtrlGSWJ
    const val RETREAT_TO = 6 // CtrlTZZDD
    const val MOVE_ATTACK = 7 // CtrlYDDZDDJS
    const val MOVE_MAGIC = 8 // CtrlYDDZDDBM
    const val MOVE_ATTACK_UNIT = 9 // CtrlYDDZDDGJ
}

data class ControlTarget(val index: Int, val point: Control.Point, val mine: Boolean, val distance: Int)
data class ControlData(val targetIndex: Int = -1, val target: Control.Point = Control.Point(-1, -1))
data class ControlTransition(val ai: Int, val data: ControlData = ControlData())
data class ControllerStep(
    /** Source selectMovePoint result: 0 complete, 1 re-enter manager, 2 stop. */
    val status: Int,
    val transition: ControlTransition? = null,
    val result: Control.Result? = null,
    val aiValue: Int? = null,
)

/** Dependencies read by Control and its nine derived modules. */
interface BattleControlContext {
    fun currentPoint(): Control.Point
    fun isParalyzed(): Boolean
    fun isSurrounded(): Boolean
    fun isMine(): Boolean
    fun setPersistentAi(ai: Int)
    fun target(index: Int): ControlTarget?
    fun hasAttackTargets(targetIndex: Int? = null): Boolean
    /** Control._cxpl; null means the original did not change controller. */
    fun exhaustedRetreat(): ControlTransition?
    /** Control._searchNearUnit(0): nearest living opposite-side unit. */
    fun nearestOpponent(): ControlTarget?
    /** First win-condition rect centre, only for a player unit. */
    fun winRectCentre(): Control.Point?
    /** Control._zdmdd: destination tile and MO_YU_JIAN3 empty choice. */
    fun destinationPoint(target: Control.Point): Control.Point?
    /** Control._ganlu(..., 9). */
    fun nearPoint(target: Control.Point): Control.Point?
    /** Control._ganlu fallback AStar(..., 5), first blocking enemy. */
    fun blockingEnemy(target: Control.Point): Int?
    /** Base Control._AIProcess, with its optional `2` flag for CtrlYDDZDDBM. */
    fun chooseAi(mode: Int = 0): Control.Result?
}

/** Base Control.selectMovePoint, with overridable source hooks. */
abstract class SourceControlController {
    fun step(context: BattleControlContext, data: ControlData): ControllerStep {
        val here = context.currentPoint()
        if (process1(context)) return ControllerStep(1, ControlTransition(ControlAi.HOLD), Control.Result(here.x, here.y))
        selectMovePoint2(context, data)?.let { return it }
        return ControllerStep(0, result = processAi(context, data))
    }

    protected open fun process1(context: BattleControlContext): Boolean = context.isParalyzed() || context.isSurrounded()
    protected open fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = context.chooseAi()
    protected open fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? = null

    /** Control._ganlu exactly chooses 9 / 8 / 7 from bit flags. */
    protected fun ganlu(context: BattleControlContext, target: Control.Point, flags: Int): ControllerStep? {
        context.nearPoint(target)?.let { point ->
            val ai = when {
                flags and 2 != 0 -> ControlAi.MOVE_ATTACK_UNIT
                flags and 1 != 0 -> ControlAi.MOVE_MAGIC
                else -> ControlAi.MOVE_ATTACK
            }
            return ControllerStep(1, ControlTransition(ai, ControlData(if (ai == ControlAi.MOVE_ATTACK_UNIT) -1 else -1, point)))
        }
        context.blockingEnemy(target)?.let { return ControllerStep(1, ControlTransition(ControlAi.ATTACK_UNIT, ControlData(it))) }
        return null
    }

    /** Control._zdmdd: no candidate still returns 1, retaining current tile. */
    protected fun zdmdd(context: BattleControlContext, target: Control.Point, flags: Int): ControllerStep? {
        val point = context.destinationPoint(target) ?: return null
        val ai = if (flags and 1 != 0) ControlAi.MOVE_ATTACK else ControlAi.MOVE_MAGIC
        return ControllerStep(1, ControlTransition(ai, ControlData(-1, point)))
    }

    protected fun retreat(context: BattleControlContext): ControllerStep? =
        context.exhaustedRetreat()?.let { ControllerStep(1, it) }
}

/** CtrlBDCJ: passive controller stops when it cannot attack. */
class CtrlBDCJ : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        return if (!context.hasAttackTargets()) ControllerStep(2) else null
    }
}

/** CtrlZDCJ: active controller, including objective-centre fallback. */
class CtrlZDCJ : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        if (context.hasAttackTargets()) return null
        val target = context.nearestOpponent()
        if (target != null) return ganlu(context, target.point, 0) ?: ControllerStep(1, ControlTransition(ControlAi.HOLD))
        if (context.isMine()) context.winRectCentre()?.let { return ControllerStep(1, ControlTransition(ControlAi.GO_TO, ControlData(-1, it))) }
        return ControllerStep(1, ControlTransition(ControlAi.HOLD))
    }

    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? =
        if (context.exhaustedRetreat() == null) context.chooseAi() else null
}

/** CtrlJSYD: holding position disables base paralysis/surrounding replacement. */
class CtrlJSYD : SourceControlController() {
    override fun process1(context: BattleControlContext) = false
}

/** CtrlGJWJ: attack/follow a designated unit. */
class CtrlGJWJ : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        val target = context.target(data.targetIndex)
            ?: return ControllerStep(1, ControlTransition(ControlAi.ACTIVE)).also { context.setPersistentAi(ControlAi.ACTIVE) }
        if (target.mine == context.isMine()) {
            if (target.distance < 3) return ControllerStep(1, ControlTransition(ControlAi.PASSIVE))
        } else if (context.hasAttackTargets(target.index)) return null
        return ganlu(context, target.point, 2)
    }
}

/** CtrlDZDD: go to point, attack from an available destination tile. */
class CtrlDZDD : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        if (data.target == context.currentPoint()) {
            context.setPersistentAi(ControlAi.PASSIVE)
            return ControllerStep(1, ControlTransition(ControlAi.PASSIVE))
        }
        return zdmdd(context, data.target, 1) ?: ganlu(context, data.target, 0)
    }
    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = null
}

/** CtrlGSWJ: follow a designated unit. */
class CtrlGSWJ : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        val target = context.target(data.targetIndex)
            ?: return ControllerStep(1, ControlTransition(ControlAi.ACTIVE)).also { context.setPersistentAi(ControlAi.ACTIVE) }
        if (target.distance < 3) return ControllerStep(1, ControlTransition(ControlAi.PASSIVE))
        return ganlu(context, target.point, 0)
    }
}

/** CtrlTZZDD: retreat to a point; source uses magic movement and never _cxpl. */
class CtrlTZZDD : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        if (data.target == context.currentPoint()) {
            context.setPersistentAi(ControlAi.PASSIVE)
            return ControllerStep(1, ControlTransition(ControlAi.PASSIVE))
        }
        return zdmdd(context, data.target, 2) ?: ganlu(context, data.target, 1)
    }
    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = null
}

/** CtrlYDDZDDJS. */
open class CtrlYDDZDDJS : SourceControlController()

/** CtrlYDDZDDBM: base AI receives flag 2. */
class CtrlYDDZDDBM : CtrlYDDZDDJS() {
    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = context.chooseAi(2)
}

/** CtrlYDDZDDGJ: adds designated-target score in original _AIProcess2. */
class CtrlYDDZDDGJ : CtrlYDDZDDJS() {
    fun targetScore(candidateIndex: Int, data: ControlData, attackTargetValue: Int): Int =
        if (candidateIndex == data.targetIndex) attackTargetValue else 0
}

/** Exact BattleScreen `_controls` order, suitable for injection into a manager adapter. */
object ControlControllerFactory {
    fun create(ai: Int): SourceControlController = when (ai) {
        ControlAi.PASSIVE -> CtrlBDCJ(); ControlAi.ACTIVE -> CtrlZDCJ(); ControlAi.HOLD -> CtrlJSYD()
        ControlAi.ATTACK_UNIT -> CtrlGJWJ(); ControlAi.GO_TO -> CtrlDZDD(); ControlAi.FOLLOW -> CtrlGSWJ()
        ControlAi.RETREAT_TO -> CtrlTZZDD(); ControlAi.MOVE_ATTACK -> CtrlYDDZDDJS()
        ControlAi.MOVE_MAGIC -> CtrlYDDZDDBM(); ControlAi.MOVE_ATTACK_UNIT -> CtrlYDDZDDGJ()
        else -> error("Unknown source AI: $ai")
    }
}

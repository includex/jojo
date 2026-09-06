// Battle
package com.jojo.game.domain.battle.command

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
    val status: Int,
    val transition: ControlTransition? = null,
    val result: Control.Result? = null,
    val aiValue: Int? = null,
)

/** BattleControlContext: 전투 제어 Context이며, 전투 계층 사이에서 필요한 동작과 데이터를 약속한다. */
interface BattleControlContext {

    fun currentPoint(): Control.Point


    fun isParalyzed(): Boolean


    fun isSurrounded(): Boolean


    fun isMine(): Boolean


    fun setPersistentAi(ai: Int)


    fun target(index: Int): ControlTarget?


    fun hasAttackTargets(targetIndex: Int? = null): Boolean
    fun exhaustedRetreat(): ControlTransition?
    fun nearestOpponent(): ControlTarget?
    fun winRectCentre(): Control.Point?
    fun destinationPoint(target: Control.Point): Control.Point?
    fun nearPoint(target: Control.Point): Control.Point?
    fun blockingEnemy(target: Control.Point): Int?

    /** chooseAi: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun chooseAi(mode: Int = 0): Control.Result?
}
abstract class SourceControlController {

    fun step(context: BattleControlContext, data: ControlData): ControllerStep {
        val here = context.currentPoint()
        if (process1(context)) return ControllerStep(
            1,
            ControlTransition(ControlAi.HOLD),
            Control.Result(here.x, here.y)
        )
        selectMovePoint2(context, data)?.let { return it }
        return ControllerStep(0, result = processAi(context, data))
    }

    protected open fun process1(context: BattleControlContext): Boolean =
        context.isParalyzed() || context.isSurrounded()

    protected open fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = context.chooseAi()
    protected open fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? = null
    protected fun ganlu(context: BattleControlContext, target: Control.Point, flags: Int): ControllerStep? {
        context.nearPoint(target)?.let { point ->
            val ai = when {
                flags and 2 != 0 -> ControlAi.MOVE_ATTACK_UNIT
                flags and 1 != 0 -> ControlAi.MOVE_MAGIC
                else -> ControlAi.MOVE_ATTACK
            }
            return ControllerStep(
                1,
                ControlTransition(ai, ControlData(if (ai == ControlAi.MOVE_ATTACK_UNIT) -1 else -1, point))
            )
        }
        context.blockingEnemy(target)
            ?.let { return ControllerStep(1, ControlTransition(ControlAi.ATTACK_UNIT, ControlData(it))) }
        return null
    }
    protected fun zdmdd(context: BattleControlContext, target: Control.Point, flags: Int): ControllerStep? {
        val point = context.destinationPoint(target) ?: return null
        val ai = if (flags and 1 != 0) ControlAi.MOVE_ATTACK else ControlAi.MOVE_MAGIC
        return ControllerStep(1, ControlTransition(ai, ControlData(-1, point)))
    }

    protected fun retreat(context: BattleControlContext): ControllerStep? =
        context.exhaustedRetreat()?.let { ControllerStep(1, it) }
}
class CtrlBDCJ : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        return if (!context.hasAttackTargets()) ControllerStep(2) else null
    }
}
class CtrlZDCJ : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        if (context.hasAttackTargets()) return null
        val target = context.nearestOpponent()
        if (target != null) return ganlu(context, target.point, 0) ?: ControllerStep(
            1,
            ControlTransition(ControlAi.HOLD)
        )
        if (context.isMine()) context.winRectCentre()
            ?.let { return ControllerStep(1, ControlTransition(ControlAi.GO_TO, ControlData(-1, it))) }
        return ControllerStep(1, ControlTransition(ControlAi.HOLD))
    }

    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? =
        if (context.exhaustedRetreat() == null) context.chooseAi() else null
}
class CtrlJSYD : SourceControlController() {
    override fun process1(context: BattleControlContext) = false
}
class CtrlGJWJ : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        val target = context.target(data.targetIndex)
            ?: return ControllerStep(
                1,
                ControlTransition(ControlAi.ACTIVE)
            ).also { context.setPersistentAi(ControlAi.ACTIVE) }
        if (target.mine == context.isMine()) {
            if (target.distance < 3) return ControllerStep(1, ControlTransition(ControlAi.PASSIVE))
        } else if (context.hasAttackTargets(target.index)) return null
        return ganlu(context, target.point, 2)
    }
}
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
class CtrlGSWJ : SourceControlController() {
    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        val target = context.target(data.targetIndex)
            ?: return ControllerStep(
                1,
                ControlTransition(ControlAi.ACTIVE)
            ).also { context.setPersistentAi(ControlAi.ACTIVE) }
        if (target.distance < 3) return ControllerStep(1, ControlTransition(ControlAi.PASSIVE))
        return ganlu(context, target.point, 0)
    }
}
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
open class CtrlYDDZDDJS : SourceControlController()
class CtrlYDDZDDBM : CtrlYDDZDDJS() {
    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = context.chooseAi(2)
}
class CtrlYDDZDDGJ : CtrlYDDZDDJS() {

    fun targetScore(candidateIndex: Int, data: ControlData, attackTargetValue: Int): Int =
        if (candidateIndex == data.targetIndex) attackTargetValue else 0
}
object ControlControllerFactory {

    fun create(ai: Int): SourceControlController = when (ai) {
        ControlAi.PASSIVE -> CtrlBDCJ(); ControlAi.ACTIVE -> CtrlZDCJ(); ControlAi.HOLD -> CtrlJSYD()
        ControlAi.ATTACK_UNIT -> CtrlGJWJ(); ControlAi.GO_TO -> CtrlDZDD(); ControlAi.FOLLOW -> CtrlGSWJ()
        ControlAi.RETREAT_TO -> CtrlTZZDD(); ControlAi.MOVE_ATTACK -> CtrlYDDZDDJS()
        ControlAi.MOVE_MAGIC -> CtrlYDDZDDBM(); ControlAi.MOVE_ATTACK_UNIT -> CtrlYDDZDDGJ()
        else -> error("Unknown source AI: $ai")
    }
}

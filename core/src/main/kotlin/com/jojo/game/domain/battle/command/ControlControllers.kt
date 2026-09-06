// Battle
package com.jojo.game.domain.battle.command

/**
 * `ControlAi` 싱글턴 객체: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

object ControlAi {
    /**
     * `PASSIVE` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val PASSIVE = 0 // CtrlBDCJ
    /**
     * `ACTIVE` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val ACTIVE = 1 // CtrlZDCJ
    /**
     * `HOLD` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val HOLD = 2 // CtrlJSYD
    /**
     * `ATTACK_UNIT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val ATTACK_UNIT = 3 // CtrlGJWJ
    /**
     * `GO_TO` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val GO_TO = 4 // CtrlDZDD
    /**
     * `FOLLOW` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val FOLLOW = 5 // CtrlGSWJ
    /**
     * `RETREAT_TO` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val RETREAT_TO = 6 // CtrlTZZDD
    /**
     * `MOVE_ATTACK` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val MOVE_ATTACK = 7 // CtrlYDDZDDJS
    /**
     * `MOVE_MAGIC` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val MOVE_MAGIC = 8 // CtrlYDDZDDBM
    /**
     * `MOVE_ATTACK_UNIT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val MOVE_ATTACK_UNIT = 9 // CtrlYDDZDDGJ
}


/**
 * `ControlTarget` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ControlTarget(val index: Int, val point: Control.Point, val mine: Boolean, val distance: Int)


/**
 * `ControlData` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ControlData(val targetIndex: Int = -1, val target: Control.Point = Control.Point(-1, -1))


/**
 * `ControlTransition` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ControlTransition(val ai: Int, val data: ControlData = ControlData())


/**
 * `ControllerStep` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ControllerStep(
    val status: Int,
    val transition: ControlTransition? = null,
    val result: Control.Result? = null,
    val aiValue: Int? = null,
)

/** BattleControlContext: 전투 제어 Context이며, 전투 계층 사이에서 필요한 동작과 데이터를 약속한다. */
interface BattleControlContext {

    /**
     * `currentPoint`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun currentPoint(): Control.Point


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
     * `isMine`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun isMine(): Boolean


    /**
     * `setPersistentAi`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setPersistentAi(ai: Int)


    /**
     * `target`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun target(index: Int): ControlTarget?


    /**
     * `hasAttackTargets`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hasAttackTargets(targetIndex: Int? = null): Boolean
    /**
     * `exhaustedRetreat`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun exhaustedRetreat(): ControlTransition?
    /**
     * `nearestOpponent`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun nearestOpponent(): ControlTarget?
    /**
     * `winRectCentre`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun winRectCentre(): Control.Point?
    /**
     * `destinationPoint`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun destinationPoint(target: Control.Point): Control.Point?
    /**
     * `nearPoint`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun nearPoint(target: Control.Point): Control.Point?
    /**
     * `blockingEnemy`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun blockingEnemy(target: Control.Point): Int?

    /** chooseAi: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun chooseAi(mode: Int = 0): Control.Result?
}
/**
 * `SourceControlController` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

abstract class SourceControlController {

    /**
     * `step`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `process1`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected open fun process1(context: BattleControlContext): Boolean =
        context.isParalyzed() || context.isSurrounded()

    /**
     * `processAi`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected open fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = context.chooseAi()
    /**
     * `selectMovePoint2`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected open fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? = null
    /**
     * `ganlu`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
    /**
     * `zdmdd`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected fun zdmdd(context: BattleControlContext, target: Control.Point, flags: Int): ControllerStep? {
        val point = context.destinationPoint(target) ?: return null
        val ai = if (flags and 1 != 0) ControlAi.MOVE_ATTACK else ControlAi.MOVE_MAGIC
        return ControllerStep(1, ControlTransition(ai, ControlData(-1, point)))
    }

    /**
     * `retreat`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected fun retreat(context: BattleControlContext): ControllerStep? =
        context.exhaustedRetreat()?.let { ControllerStep(1, it) }
}
/**
 * `CtrlBDCJ` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlBDCJ : SourceControlController() {
    /**
     * `selectMovePoint2`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        return if (!context.hasAttackTargets()) ControllerStep(2) else null
    }
}
/**
 * `CtrlZDCJ` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlZDCJ : SourceControlController() {
    /**
     * `selectMovePoint2`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `processAi`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? =
        if (context.exhaustedRetreat() == null) context.chooseAi() else null
}
/**
 * `CtrlJSYD` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlJSYD : SourceControlController() {
    /**
     * `process1`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun process1(context: BattleControlContext) = false
}
/**
 * `CtrlGJWJ` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlGJWJ : SourceControlController() {
    /**
     * `selectMovePoint2`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
/**
 * `CtrlDZDD` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlDZDD : SourceControlController() {
    /**
     * `selectMovePoint2`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        retreat(context)?.let { return it }
        if (data.target == context.currentPoint()) {
            context.setPersistentAi(ControlAi.PASSIVE)
            return ControllerStep(1, ControlTransition(ControlAi.PASSIVE))
        }
        return zdmdd(context, data.target, 1) ?: ganlu(context, data.target, 0)
    }

    /**
     * `processAi`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = null
}
/**
 * `CtrlGSWJ` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlGSWJ : SourceControlController() {
    /**
     * `selectMovePoint2`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
/**
 * `CtrlTZZDD` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlTZZDD : SourceControlController() {
    /**
     * `selectMovePoint2`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun selectMovePoint2(context: BattleControlContext, data: ControlData): ControllerStep? {
        if (data.target == context.currentPoint()) {
            context.setPersistentAi(ControlAi.PASSIVE)
            return ControllerStep(1, ControlTransition(ControlAi.PASSIVE))
        }
        return zdmdd(context, data.target, 2) ?: ganlu(context, data.target, 1)
    }

    /**
     * `processAi`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = null
}
/**
 * `CtrlYDDZDDJS` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

open class CtrlYDDZDDJS : SourceControlController()
/**
 * `CtrlYDDZDDBM` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlYDDZDDBM : CtrlYDDZDDJS() {
    /**
     * `processAi`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun processAi(context: BattleControlContext, data: ControlData): Control.Result? = context.chooseAi(2)
}
/**
 * `CtrlYDDZDDGJ` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class CtrlYDDZDDGJ : CtrlYDDZDDJS() {

    /**
     * `targetScore`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun targetScore(candidateIndex: Int, data: ControlData, attackTargetValue: Int): Int =
        if (candidateIndex == data.targetIndex) attackTargetValue else 0
}
/**
 * `ControlControllerFactory` 싱글턴 객체: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

object ControlControllerFactory {

    /**
     * `create`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun create(ai: Int): SourceControlController = when (ai) {
        ControlAi.PASSIVE -> CtrlBDCJ(); ControlAi.ACTIVE -> CtrlZDCJ(); ControlAi.HOLD -> CtrlJSYD()
        ControlAi.ATTACK_UNIT -> CtrlGJWJ(); ControlAi.GO_TO -> CtrlDZDD(); ControlAi.FOLLOW -> CtrlGSWJ()
        ControlAi.RETREAT_TO -> CtrlTZZDD(); ControlAi.MOVE_ATTACK -> CtrlYDDZDDJS()
        ControlAi.MOVE_MAGIC -> CtrlYDDZDDBM(); ControlAi.MOVE_ATTACK_UNIT -> CtrlYDDZDDGJ()
        else -> error("Unknown source AI: $ai")
    }
}

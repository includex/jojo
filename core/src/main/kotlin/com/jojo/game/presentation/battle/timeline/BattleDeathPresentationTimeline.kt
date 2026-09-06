// Battle
package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.*

/** BattleDeathPresentationTimeline: 전투 사망 표현 시간 흐름이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */
internal class BattleDeathPresentationTimeline(
    private val port: Port,
) {
    internal enum class Checkpoint { CAMP_START, CAMP_RESTORE, ROUND_START }
    internal data class DeathUnit(
        val unitId: String,
        val direction: Int,
        val sourceAction: Int,
        val duration: Float,
        val originalHp: Int,
        val showRetireMessage: Boolean,
        val dialogueCharacterId: String?,
        val retireMessage: String?,
    )

    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        val now: Float
        val scriptComplete: Boolean
        val dialogueActive: Boolean

        fun collectDyingUnits(): List<DeathUnit>
        fun runScript()
        fun focusUnit(unitId: String)
        fun presentRetireDialogue(unit: DeathUnit)
        fun startDeathAnimation(unit: DeathUnit, startsAt: Float, endsAt: Float)
        fun completeDeathAnimation(unit: DeathUnit)
        fun completeCheckpoint(checkpoint: Checkpoint)
    }
    private enum class Stage { NONE, PRE_SCRIPT, HIDING, POST_SCRIPT }

    private val pending = ArrayDeque<DeathUnit>()
    private var active: Active? = null
    private var awaitingDialogue: DeathUnit? = null
    private var checkpoint: Checkpoint? = null
    private var stage = Stage.NONE
    private var postActionDeathsStarted = false
    private data class Active(val unit: DeathUnit, val endsAt: Float)
    internal fun queuePostAction(units: List<DeathUnit>): Boolean {
        pending.clear()
        pending.addAll(units)
        if (pending.isEmpty()) return false
        postActionDeathsStarted = true
        drive()
        return true
    }

    /** begin: 전투 단계의 시작 상태를 만들고 필요한 값을 초기화한다. */
    internal fun begin(nextCheckpoint: Checkpoint): Boolean {
        check(checkpoint == null) { "overlapping lifecycle unitDeath checkpoints" }
        checkpoint = nextCheckpoint
        if (nextCheckpoint == Checkpoint.CAMP_START) {
            val units = port.collectDyingUnits()
            if (units.isEmpty()) {
                clearBarrier()
                completeCheckpoint(nextCheckpoint)
                return true
            }
            stage = Stage.HIDING
            beginQueued(units)
        } else {
            stage = Stage.PRE_SCRIPT
            port.runScript()
        }
        return false
    }

    /** driveScriptBarrier: 현재 전투 상태를 다음 처리 단계로 진행한다. */
    internal fun driveScriptBarrier() {
        val current = checkpoint ?: return
        if (!port.scriptComplete) return
        when (stage) {
            Stage.PRE_SCRIPT -> {
                val units = port.collectDyingUnits()
                if (units.isEmpty()) {
                    clearBarrier()
                    completeCheckpoint(current)
                } else {
                    stage = Stage.HIDING
                    beginQueued(units)
                }
            }

            Stage.POST_SCRIPT -> {
                clearBarrier()
                completeCheckpoint(current)
            }

            else -> Unit
        }
    }

    /** tick: 현재 전투 상태를 다음 처리 단계로 진행한다. */
    internal fun tick(now: Float) {
        active?.let { running ->
            if (now < running.endsAt) return
            port.completeDeathAnimation(running.unit)
            active = null
        }
        awaitingDialogue?.let { unit ->
            if (port.dialogueActive) return
            awaitingDialogue = null
            startAnimation(unit, now)
            return
        }
        if (active != null) return
        val unit = pending.removeFirstOrNull()
        if (unit == null) {
            completeCheckpointIfReady()
            return
        }
        port.focusUnit(unit.unitId)
        if (unit.showRetireMessage && unit.retireMessage != null) {
            awaitingDialogue = unit
            port.presentRetireDialogue(unit)
        } else {
            startAnimation(unit, now)
        }
    }

    /** finishPostActionCallbacks: 진행 중인 전투 처리를 완료하고 후속 상태를 반영한다. */
    internal fun finishPostActionCallbacks() {
        postActionDeathsStarted = false
        if (checkpoint == null) {
            pending.clear()
            active = null
            awaitingDialogue = null
        }
    }

    internal fun reset() {
        pending.clear()
        active = null
        awaitingDialogue = null
        checkpoint = null
        stage = Stage.NONE
        postActionDeathsStarted = false
    }

    internal fun isBusy(): Boolean =
        pending.isNotEmpty() || active != null || awaitingDialogue != null

    internal fun hasActiveAnimation(): Boolean = active != null

    internal fun containsPending(unitId: String): Boolean = pending.any { it.unitId == unitId }

    internal fun startedPostActionDeaths(): Boolean = postActionDeathsStarted

    private fun beginQueued(units: List<DeathUnit>) {
        pending.clear()
        pending.addAll(units)
        postActionDeathsStarted = true
        drive()
    }

    private fun drive() {
        tick(portNow())
    }

    private fun portNow(): Float = port.now

    private fun startAnimation(unit: DeathUnit, now: Float) {
        val endsAt = now + unit.duration
        port.startDeathAnimation(unit, now, endsAt)
        active = Active(unit, endsAt)
    }

    private fun completeCheckpointIfReady() {
        val current = checkpoint ?: return
        if (pending.isNotEmpty() || active != null || awaitingDialogue != null) return
        if (stage == Stage.HIDING) {
            stage = Stage.POST_SCRIPT
            postActionDeathsStarted = false
            port.runScript()
            return
        }
    }

    private fun clearBarrier() {
        checkpoint = null
        stage = Stage.NONE
        postActionDeathsStarted = false
    }

    private fun completeCheckpoint(checkpoint: Checkpoint) {
        port.completeCheckpoint(checkpoint)
    }
}

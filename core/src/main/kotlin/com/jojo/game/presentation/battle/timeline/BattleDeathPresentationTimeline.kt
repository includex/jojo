package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.*

/**
 * Callback-driven presentation of the source `unitDeath` generator.
 *
 * The timeline owns the serial queue and the two script barriers.  It does
 * not know Battle, Scenario, LibGDX, or a mutable screen; the host supplies
 * an immutable death batch and a narrow port for the authored side effects.
 */
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

    /** Starts a post-action unitDeath sequence without a turn checkpoint. */
    internal fun queuePostAction(units: List<DeathUnit>): Boolean {
        pending.clear()
        pending.addAll(units)
        if (pending.isEmpty()) return false
        postActionDeathsStarted = true
        drive()
        return true
    }

    /** Starts the lifecycle-owned death barrier at the source checkpoint. */
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

    /** Advances the pre/post script callback barrier once the script returns. */
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

    /** Advances one serial death/dialogue/animation callback chain. */
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

    /** Clears a manual post-action callback after its second script pass. */
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
        // POST_SCRIPT is an external coroutine barrier. Only
        // driveScriptBarrier may publish its completion.
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

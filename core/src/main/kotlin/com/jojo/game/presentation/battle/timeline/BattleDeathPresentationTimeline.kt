// Battle
package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.*

/** BattleDeathPresentationTimeline: 전투 사망 표현 시간 흐름이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */
internal class BattleDeathPresentationTimeline(
    /** `port` (Port): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val port: Port,
) {
    /**
     * `Checkpoint`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal enum class Checkpoint { CAMP_START, CAMP_RESTORE, ROUND_START }
    /**
     * `DeathUnit`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class DeathUnit(
        /**
         * `unitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unitId: String,
        /**
         * `direction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val direction: Int,
        /**
         * `sourceAction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sourceAction: Int,
        /**
         * `duration` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val duration: Float,
        /**
         * `originalHp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val originalHp: Int,
        /**
         * `showRetireMessage` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val showRetireMessage: Boolean,
        /**
         * `dialogueCharacterId` (String?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val dialogueCharacterId: String?,
        /**
         * `retireMessage` (String?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val retireMessage: String?,
    )

    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        /**
         * `now` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val now: Float
        /**
         * `scriptComplete` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scriptComplete: Boolean
        /**
         * `dialogueActive` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val dialogueActive: Boolean

        /**
         * `collectDyingUnits`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun collectDyingUnits(): List<DeathUnit>
        /**
         * `runScript`: 흐름을 실행하거나 다음 단계로 전달한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun runScript()
        /**
         * `focusUnit`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focusUnit(unitId: String)
        /**
         * `presentRetireDialogue`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun presentRetireDialogue(unit: DeathUnit)
        /**
         * `startDeathAnimation`: 흐름을 실행하거나 다음 단계로 전달한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun startDeathAnimation(unit: DeathUnit, startsAt: Float, endsAt: Float)
        /**
         * `completeDeathAnimation`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun completeDeathAnimation(unit: DeathUnit)
        /**
         * `completeCheckpoint`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun completeCheckpoint(checkpoint: Checkpoint)
    }
    /**
     * `Stage`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private enum class Stage { NONE, PRE_SCRIPT, HIDING, POST_SCRIPT }

    /**
     * `pending` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val pending = ArrayDeque<DeathUnit>()
    /**
     * `active` (Active?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var active: Active? = null
    /**
     * `awaitingDialogue` (DeathUnit?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var awaitingDialogue: DeathUnit? = null
    /**
     * `checkpoint` (Checkpoint?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var checkpoint: Checkpoint? = null
    /**
     * `stage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var stage = Stage.NONE
    /**
     * `postActionDeathsStarted` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var postActionDeathsStarted = false
    /**
     * `Active`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class Active(val unit: DeathUnit, val endsAt: Float)
    /**
     * `queuePostAction`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `reset`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun reset() {
        pending.clear()
        active = null
        awaitingDialogue = null
        checkpoint = null
        stage = Stage.NONE
        postActionDeathsStarted = false
    }

    /**
     * `isBusy`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun isBusy(): Boolean =
        pending.isNotEmpty() || active != null || awaitingDialogue != null

    /**
     * `hasActiveAnimation`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun hasActiveAnimation(): Boolean = active != null

    /**
     * `containsPending`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun containsPending(unitId: String): Boolean = pending.any { it.unitId == unitId }

    /**
     * `startedPostActionDeaths`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun startedPostActionDeaths(): Boolean = postActionDeathsStarted

    /**
     * `beginQueued`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun beginQueued(units: List<DeathUnit>) {
        pending.clear()
        pending.addAll(units)
        postActionDeathsStarted = true
        drive()
    }

    /**
     * `drive`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drive() {
        tick(portNow())
    }

    /**
     * `portNow`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun portNow(): Float = port.now

    /**
     * `startAnimation`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun startAnimation(unit: DeathUnit, now: Float) {
        val endsAt = now + unit.duration
        port.startDeathAnimation(unit, now, endsAt)
        active = Active(unit, endsAt)
    }

    /**
     * `completeCheckpointIfReady`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `clearBarrier`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun clearBarrier() {
        checkpoint = null
        stage = Stage.NONE
        postActionDeathsStarted = false
    }

    /**
     * `completeCheckpoint`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun completeCheckpoint(checkpoint: Checkpoint) {
        port.completeCheckpoint(checkpoint)
    }
}

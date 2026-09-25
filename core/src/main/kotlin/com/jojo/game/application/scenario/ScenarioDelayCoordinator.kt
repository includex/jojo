// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.presentation.i18n.GameText

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

data class ScenarioHallUnitReadinessEntry(val index: Int, val command: ScenarioCommand.ShowUnit)
data class ScenarioHallUnitReadinessRequest(val token: Long, val entries: List<ScenarioHallUnitReadinessEntry>)

/** 시나리오 지연과 외부 연출 완료 시점을 조율한다. */
internal class ScenarioDelayCoordinator(
    /**
     * `stage` (ScenarioStage,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val stage: ScenarioStage,
    /**
     * `dialogueCoordinator` (ScenarioDialogueCoordinator,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val dialogueCoordinator: ScenarioDialogueCoordinator,
    /**
     * `modalController` (ScenarioModalController,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val modalController: ScenarioModalController,
    /**
     * `getState` (() -> PlaybackState,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val getState: () -> PlaybackState,
    /**
     * `onSetState` ((PlaybackState) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onSetState: (PlaybackState) -> Unit,
    /**
     * `onResumeExecution` (() -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onResumeExecution: () -> Unit,
    /**
     * `getDelayRemainingSeconds` (() -> Float,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val getDelayRemainingSeconds: () -> Float,
    /**
     * `onSetDelayRemainingSeconds` ((Float) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onSetDelayRemainingSeconds: (Float) -> Unit,
) {
    /**
     * `delayRemainingSeconds` (Float get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val delayRemainingSeconds: Float get() = getDelayRemainingSeconds()
    /**
     * `pendingBattleBackgroundLoadIndex` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pendingBattleBackgroundLoadIndex: Int? = null
        private set
    /**
     * `externalFightPresentation` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var externalFightPresentation: Boolean = false

    private var pendingHallMoveIds: Set<Int> = emptySet()
    private var nextHallUnitReadinessToken = 1L
    private var pendingHallUnitReadiness: ScenarioHallUnitReadinessRequest? = null
    private val readyHallUnitEntryIndexes = mutableSetOf<Int>()
    private var stageDelayDurationSeconds: Double? = null
    private var stageDelayElapsedSeconds = 0.0
    private var stageDelayPrimed = false
    private var isUpdating = false

    val hallUnitReadinessRequest: ScenarioHallUnitReadinessRequest?
        get() = pendingHallUnitReadiness

    /**
     * `hasPendingBattleBackgroundLoad` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val hasPendingBattleBackgroundLoad: Boolean get() = pendingBattleBackgroundLoadIndex != null

    /**
     * `requestedBattleBackgroundMapIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val requestedBattleBackgroundMapIndex: Int
        get() = pendingBattleBackgroundLoadIndex ?: stage.battleMapIndex

    /** 지연 시간과 대기 중인 전장 배경 요청을 초기화한다. */
    fun reset() {
        onSetDelayRemainingSeconds(0f)
        pendingBattleBackgroundLoadIndex = null
        pendingHallMoveIds = emptySet()
        pendingHallUnitReadiness = null
        readyHallUnitEntryIndexes.clear()
        clearStageDelay()
        isUpdating = false
    }

    /** 남은 지연 시간을 설정한다. */
    fun setDelayRemainingSeconds(seconds: Float) {
        clearStageDelay()
        onSetDelayRemainingSeconds(seconds)
    }

    /** 지정 시간 동안 시나리오 실행을 일시 정지한다. */
    fun suspendFor(seconds: Float) {
        clearStageDelay()
        pendingHallMoveIds = emptySet()
        onSetDelayRemainingSeconds(seconds.coerceAtLeast(0f))
        onSetState(PlaybackState.DELAY)
    }

    /** Cocos CallbackTimer와 같이 stage.delay만 Double 누적과 첫 tick prime을 사용한다. */
    fun suspendForStageDelay(ticks: Int) {
        pendingHallMoveIds = emptySet()
        stageDelayDurationSeconds = ticks.coerceAtLeast(0) * 0.1
        stageDelayElapsedSeconds = 0.0
        stageDelayPrimed = isUpdating
        onSetDelayRemainingSeconds(stageDelayDurationSeconds!!.toFloat())
        onSetState(PlaybackState.DELAY)
    }

    /** Hall 스크립트는 예상 시간이 아니라 실제 이동 완료까지 기다린다. */
    fun suspendForHallMoves(unitIds: Set<Int>) {
        clearStageDelay()
        pendingHallMoveIds = unitIds.filterTo(mutableSetOf()) { stage.unit(it).moveDuration > 0f }
        if (pendingHallMoveIds.isEmpty()) return
        onSetDelayRemainingSeconds(Float.MAX_VALUE)
        onSetState(PlaybackState.DELAY)
    }

    fun suspendForHallUnitReadiness(commands: List<ScenarioCommand.ShowUnit>) {
        clearStageDelay()
        check(pendingHallUnitReadiness == null) { "Hall unit readiness request is already pending" }
        val token = nextHallUnitReadinessToken++
        pendingHallUnitReadiness = ScenarioHallUnitReadinessRequest(
            token,
            commands.mapIndexed { index, command -> ScenarioHallUnitReadinessEntry(index, command) },
        )
        readyHallUnitEntryIndexes.clear()
        onSetDelayRemainingSeconds(Float.MAX_VALUE)
        onSetState(PlaybackState.DELAY)
    }

    fun completeHallUnitReadiness(token: Long, entryIndex: Int): Boolean {
        val request = pendingHallUnitReadiness ?: return false
        if (request.token != token || entryIndex !in request.entries.indices) return false
        if (!readyHallUnitEntryIndexes.add(entryIndex)) return false
        stage.apply(request.entries[entryIndex].command)
        if (readyHallUnitEntryIndexes.size < request.entries.size) return true
        pendingHallUnitReadiness = null
        readyHallUnitEntryIndexes.clear()
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
        return true
    }

    /** 전장 배경이 준비될 때까지 시나리오 실행을 일시 정지한다. */
    fun suspendForBattleBackgroundLoad(mapIndex: Int) {
        clearStageDelay()
        check(!hasPendingBattleBackgroundLoad) { GameText.S_0CC0A4F490 }
        pendingBattleBackgroundLoadIndex = mapIndex
        onSetDelayRemainingSeconds(Float.MAX_VALUE)
        onSetState(PlaybackState.DELAY)
    }

    /** 외부 전투 연출이 활성화된 경우 실행을 일시 정지한다. */
    fun suspendForExternalFightCommand() {
        if (externalFightPresentation) suspendFor(Float.MAX_VALUE)
    }

    /** 지연·연출·모달 대기 상태를 한 프레임 갱신한다. */
    fun update(delta: Float, autoCloseUi: Boolean = true) {
        val wasUpdating = isUpdating
        isUpdating = true
        try {
            updateInternal(delta, autoCloseUi)
        } finally {
            isUpdating = wasUpdating
        }
    }

    private fun updateInternal(delta: Float, autoCloseUi: Boolean) {
        stage.updateAnimations(delta)
        when (getState()) {
            PlaybackState.DELAY -> {
                if (hasPendingBattleBackgroundLoad) {
                    stage.snapshotHallRenderSelection()
                    return
                }
                if (pendingHallUnitReadiness != null) {
                    stage.snapshotHallRenderSelection()
                    return
                }
                // AnimationManager precedes timer-driven dialogue callbacks in the source scheduler.
                stage.snapshotHallRenderSelection()
                if (dialogueCoordinator.handleDelayTick()) return
                if (pendingHallMoveIds.isNotEmpty()) {
                    if (pendingHallMoveIds.any { stage.unit(it).moveDuration > 0f }) {
                        return
                    }
                    pendingHallMoveIds = emptySet()
                    onSetDelayRemainingSeconds(0f)
                    onResumeExecution()
                    // Source ActionManager completes moves and runs their continuation before
                    // AnimationManager samples the new Hall action/direction in the same frame.
                    stage.snapshotHallRenderSelection()
                    return
                }
                // AnimationManager is a scheduler update target. CallbackTimer-based dialogue and
                // stage delays run afterwards, so their action changes become visible next frame.
                stageDelayDurationSeconds?.let { duration ->
                    if (!stageDelayPrimed) {
                        stageDelayPrimed = true
                        return
                    }
                    stageDelayElapsedSeconds += delta.coerceAtLeast(0f).toDouble()
                    val remaining = (duration - stageDelayElapsedSeconds).coerceAtLeast(0.0)
                    onSetDelayRemainingSeconds(remaining.toFloat())
                    if (stageDelayElapsedSeconds >= duration) {
                        clearStageDelay()
                        onSetDelayRemainingSeconds(0f)
                        onResumeExecution()
                    }
                    return
                }
                val remaining = getDelayRemainingSeconds() - delta.coerceAtLeast(0f)
                if (remaining <= 0f) {
                    onSetDelayRemainingSeconds(0f)
                    onResumeExecution()
                } else {
                    onSetDelayRemainingSeconds(remaining)
                }
            }

            PlaybackState.MODAL -> {
                stage.snapshotHallRenderSelection()
                modalController.update(delta, autoCloseUi)
            }
            else -> stage.snapshotHallRenderSelection()
        }
    }

    /** 건너뛸 수 있는 지연과 애니메이션을 즉시 완료한다. */
    fun skipDelay() {
        if (getState() != PlaybackState.DELAY) return
        if (hasPendingBattleBackgroundLoad) return
        if (pendingHallUnitReadiness != null) return
        stage.finishAnimations()
        pendingHallMoveIds = emptySet()
        clearStageDelay()
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

    /** 외부 연출 완료 후 지연 상태에서 실행을 재개한다. */
    fun resumeExternalDelay() {
        check(getState() == PlaybackState.DELAY) { GameText.S_38B7597664 }
        check(!hasPendingBattleBackgroundLoad) {
            GameText.S_514FD95472
        }
        check(pendingHallUnitReadiness == null) {
            GameText.S_DF70B8A5DA
        }
        pendingHallMoveIds = emptySet()
        clearStageDelay()
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

    /** 전장 배경 요청을 완료하고 선택한 맵으로 실행을 재개한다. */
    fun completeBattleBackgroundLoad() {
        val mapIndex = requireNotNull(pendingBattleBackgroundLoadIndex) {
            GameText.S_FD44645376
        }
        check(getState() == PlaybackState.DELAY) { GameText.S_DBCA581BF5 }
        stage.selectBattleMap(mapIndex)
        pendingBattleBackgroundLoadIndex = null
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

    private fun clearStageDelay() {
        stageDelayDurationSeconds = null
        stageDelayElapsedSeconds = 0.0
        stageDelayPrimed = false
    }
}

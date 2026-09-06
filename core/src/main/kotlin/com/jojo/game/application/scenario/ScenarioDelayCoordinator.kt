// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

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
    }

    /** 남은 지연 시간을 설정한다. */
    fun setDelayRemainingSeconds(seconds: Float) {
        onSetDelayRemainingSeconds(seconds)
    }

    /** 지정 시간 동안 시나리오 실행을 일시 정지한다. */
    fun suspendFor(seconds: Float) {
        onSetDelayRemainingSeconds(seconds.coerceAtLeast(0f))
        onSetState(PlaybackState.DELAY)
    }

    /** 전장 배경이 준비될 때까지 시나리오 실행을 일시 정지한다. */
    fun suspendForBattleBackgroundLoad(mapIndex: Int) {
        check(!hasPendingBattleBackgroundLoad) { "동시에 두 개의 loadBg 콜백이 대기 중입니다." }
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
        stage.updateAnimations(delta)
        when (getState()) {
            PlaybackState.DELAY -> {
                if (hasPendingBattleBackgroundLoad) return
                if (dialogueCoordinator.handleDelayTick()) return
                val remaining = getDelayRemainingSeconds() - delta.coerceAtLeast(0f)
                if (remaining <= 0f) {
                    onSetDelayRemainingSeconds(0f)
                    onResumeExecution()
                } else {
                    onSetDelayRemainingSeconds(remaining)
                }
            }

            PlaybackState.MODAL -> modalController.update(delta, autoCloseUi)
            else -> Unit
        }
    }

    /** 건너뛸 수 있는 지연과 애니메이션을 즉시 완료한다. */
    fun skipDelay() {
        if (getState() != PlaybackState.DELAY) return
        if (hasPendingBattleBackgroundLoad) return
        stage.finishAnimations()
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

    /** 외부 연출 완료 후 지연 상태에서 실행을 재개한다. */
    fun resumeExternalDelay() {
        check(getState() == PlaybackState.DELAY) { "재개할 외부 애니메이션 대기가 없습니다." }
        check(!hasPendingBattleBackgroundLoad) {
            "loadBg는 BattleScreen의 맵/아바타 완료 콜백으로만 재개해야 합니다."
        }
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

    /** 전장 배경 요청을 완료하고 선택한 맵으로 실행을 재개한다. */
    fun completeBattleBackgroundLoad() {
        val mapIndex = requireNotNull(pendingBattleBackgroundLoadIndex) {
            "완료할 loadBg 콜백이 없습니다."
        }
        check(getState() == PlaybackState.DELAY) { "loadBg 완료 콜백은 Script pause 중에만 가능합니다." }
        stage.selectBattleMap(mapIndex)
        pendingBattleBackgroundLoadIndex = null
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }
}

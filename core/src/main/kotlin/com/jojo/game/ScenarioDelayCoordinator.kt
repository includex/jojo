package com.jojo.game

import com.jojo.game.domain.scenario.*

internal class ScenarioDelayCoordinator(
    private val stage: ScenarioStage,
    private val dialogueCoordinator: ScenarioDialogueCoordinator,
    private val modalController: ScenarioModalController,
    private val getState: () -> PlaybackState,
    private val onSetState: (PlaybackState) -> Unit,
    private val onResumeExecution: () -> Unit,
    private val getDelayRemainingSeconds: () -> Float,
    private val onSetDelayRemainingSeconds: (Float) -> Unit,
) {
    val delayRemainingSeconds: Float get() = getDelayRemainingSeconds()
    var pendingBattleBackgroundLoadIndex: Int? = null
        private set
    var externalFightPresentation: Boolean = false

    val hasPendingBattleBackgroundLoad: Boolean get() = pendingBattleBackgroundLoadIndex != null

    val requestedBattleBackgroundMapIndex: Int
        get() = pendingBattleBackgroundLoadIndex ?: stage.battleMapIndex

    /**
     * 공개 메서드 `reset`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun reset() {
        onSetDelayRemainingSeconds(0f)
        pendingBattleBackgroundLoadIndex = null
    }

    /**
     * 공개 메서드 `setDelayRemainingSeconds`
     *
     * ### 파라미터
    - `seconds` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setDelayRemainingSeconds(seconds: Float) {
        onSetDelayRemainingSeconds(seconds)
    }

    /**
     * 공개 메서드 `suspendFor`
     *
     * ### 파라미터
    - `seconds` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun suspendFor(seconds: Float) {
        onSetDelayRemainingSeconds(seconds.coerceAtLeast(0f))
        onSetState(PlaybackState.DELAY)
    }

    /**
     * 공개 메서드 `suspendForBattleBackgroundLoad`
     *
     * ### 파라미터
    - `mapIndex` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun suspendForBattleBackgroundLoad(mapIndex: Int) {
        check(!hasPendingBattleBackgroundLoad) { "동시에 두 개의 loadBg 콜백이 대기 중입니다." }
        pendingBattleBackgroundLoadIndex = mapIndex
        onSetDelayRemainingSeconds(Float.MAX_VALUE)
        onSetState(PlaybackState.DELAY)
    }

    /**
     * 공개 메서드 `suspendForExternalFightCommand`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun suspendForExternalFightCommand() {
        if (externalFightPresentation) suspendFor(Float.MAX_VALUE)
    }

    /**
     * 공개 메서드 `update`
     *
     * ### 파라미터
    - `delta` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `autoCloseUi` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `skipDelay`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun skipDelay() {
        if (getState() != PlaybackState.DELAY) return
        if (hasPendingBattleBackgroundLoad) return
        stage.finishAnimations()
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

    /**
     * 공개 메서드 `resumeExternalDelay`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun resumeExternalDelay() {
        check(getState() == PlaybackState.DELAY) { "재개할 외부 애니메이션 대기가 없습니다." }
        check(!hasPendingBattleBackgroundLoad) {
            "loadBg는 BattleScreen의 맵/아바타 완료 콜백으로만 재개해야 합니다."
        }
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

    /**
     * 공개 메서드 `completeBattleBackgroundLoad`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

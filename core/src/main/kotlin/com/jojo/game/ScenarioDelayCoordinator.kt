package com.jojo.game

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

    fun reset() {
        onSetDelayRemainingSeconds(0f)
        pendingBattleBackgroundLoadIndex = null
    }

    fun setDelayRemainingSeconds(seconds: Float) {
        onSetDelayRemainingSeconds(seconds)
    }

    fun suspendFor(seconds: Float) {
        onSetDelayRemainingSeconds(seconds.coerceAtLeast(0f))
        onSetState(PlaybackState.DELAY)
    }

    fun suspendForBattleBackgroundLoad(mapIndex: Int) {
        check(!hasPendingBattleBackgroundLoad) { "동시에 두 개의 loadBg 콜백이 대기 중입니다." }
        pendingBattleBackgroundLoadIndex = mapIndex
        onSetDelayRemainingSeconds(Float.MAX_VALUE)
        onSetState(PlaybackState.DELAY)
    }

    fun suspendForExternalFightCommand() {
        if (externalFightPresentation) suspendFor(Float.MAX_VALUE)
    }

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

    fun skipDelay() {
        if (getState() != PlaybackState.DELAY) return
        if (hasPendingBattleBackgroundLoad) return
        stage.finishAnimations()
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

    fun resumeExternalDelay() {
        check(getState() == PlaybackState.DELAY) { "재개할 외부 애니메이션 대기가 없습니다." }
        check(!hasPendingBattleBackgroundLoad) {
            "loadBg는 BattleScreen의 맵/아바타 완료 콜백으로만 재개해야 합니다."
        }
        onSetDelayRemainingSeconds(0f)
        onResumeExecution()
    }

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

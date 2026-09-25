// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.presentation.i18n.SystemMessage

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

import java.util.*

/** 시나리오 모달의 표시 내용, 타이핑, 자동 닫힘 시간을 관리한다. */
internal class ScenarioModalController(
    /**
     * `stage` (ScenarioStage,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val stage: ScenarioStage,
    /**
     * `onStateChange` ((PlaybackState) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onStateChange: (PlaybackState) -> Unit,
    /**
     * `onResumeExecution` (() -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onResumeExecution: () -> Unit,
) {
    /**
     * `currentModalText` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentModalText: String? = null
        internal set
    /**
     * `currentModalKind` (ScenarioModalKind?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentModalKind: ScenarioModalKind? = null
        internal set
    /**
     * `currentModalFixedText` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentModalFixedText: String = ""
        internal set
    /** InfoLayer가 현재까지 공개한 본문이다. EVENT/INFO 외 모달은 화면 세션이 직접 계산한다. */
    var currentModalVisibleText: String = ""
        internal set
    /** InfoLayer의 타이핑 완료 여부다. */
    var currentModalTextComplete: Boolean = true
        internal set
    /**
     * `ambitionFrom` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var ambitionFrom: Int = 0
        internal set
    /**
     * `ambitionTo` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var ambitionTo: Int = 0
        internal set
    /**
     * `ambitionElapsedSeconds` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var ambitionElapsedSeconds: Float = 0f
        internal set
    /**
     * `ambitionIndicatorEnabled` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var ambitionIndicatorEnabled: Boolean = true
        internal set

    /**
     * `modalNextText` (String?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal var modalNextText: String? = null
    /**
     * `modalQueuedTexts` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal val modalQueuedTexts = ArrayDeque<String>()
    /**
     * `mapInfoContent` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal var mapInfoContent = ""
    /**
     * `modalRemainingSeconds` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal var modalRemainingSeconds = 0f
    /**
     * `modalPostTypingDelaySeconds` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal var modalPostTypingDelaySeconds = 1f
    /** 원본 CallbackTimer처럼 생성 직후 첫 update를 elapsed=0 초기화에만 쓰는지 나타낸다. */
    private var modalTypingPrimed = false
    /** 다음 InfoLayer 글자 단위를 기다린 시간이다. */
    private var modalTypingElapsedSeconds = 0.0
    /** InfoLayer 원문에서 다음에 공개할 UTF-16 위치다. */
    private var modalTypingCursor = 0
    /** 검증 fixture가 전달한 총 remainingSeconds를 자연 타이핑 후 지연으로 바꾸지 않도록 보존한다. */
    private var explicitModalRemaining = false
    /** 자연 InfoLayer가 타이핑 완료 뒤 닫힘까지 누적한 시간이다. */
    private var infoCloseElapsedSeconds = 0.0
    /** 자연 InfoLayer가 post-typing 닫힘 callback을 예약했는지 나타낸다. */
    private var infoCloseScheduled = false
    /** 입력에서 예약한 타이머는 다음 update를 초기화에만 사용한다. */
    private var infoClosePrimed = false

    /** 모달 표시와 대기 상태를 초기화한다. */
    fun reset() {
        currentModalText = null
        currentModalKind = null
        currentModalFixedText = ""
        currentModalVisibleText = ""
        currentModalTextComplete = true
        ambitionFrom = 0
        ambitionTo = 0
        ambitionElapsedSeconds = 0f
        ambitionIndicatorEnabled = true
        modalNextText = null
        modalQueuedTexts.clear()
        modalRemainingSeconds = 0f
        modalPostTypingDelaySeconds = 1f
        resetInfoTyping()
    }

    /** 모달 시간과 자동 닫힘 여부를 한 프레임 갱신한다. */
    fun update(delta: Float, autoCloseUi: Boolean) {
        val elapsed = delta.coerceAtLeast(0f)
        if (currentModalKind == ScenarioModalKind.AMBITION) {
            ambitionElapsedSeconds += elapsed
        }
        if (isInfoLayerModal()) {
            if (!currentModalTextComplete) {
                updateInfoTyping(elapsed)
                if (!explicitModalRemaining || currentModalText == null) return
            }
            if (explicitModalRemaining) updateModalClose(elapsed, autoCloseUi)
            else updateInfoClose(elapsed, autoCloseUi)
            return
        }

        updateModalClose(elapsed, autoCloseUi)
    }

    /** 자동 닫힘이 허용된 모달의 남은 시간을 진행한다. */
    private fun updateModalClose(delta: Float, autoCloseUi: Boolean) {
        if (modalRemainingSeconds > 0f && ScenarioInterpreter.modalMayAutoClose(
                currentModalKind,
                currentModalText,
                autoCloseUi
            )
        ) {
            modalRemainingSeconds -= delta
            if (modalRemainingSeconds <= 0f) resumeModal()
        }
    }

    /** 자연 InfoLayer의 scheduleOnce 지연을 Double 누적으로 판정한다. */
    private fun updateInfoClose(delta: Float, autoCloseUi: Boolean) {
        if (!infoCloseScheduled || !ScenarioInterpreter.modalMayAutoClose(
                currentModalKind,
                currentModalText,
                autoCloseUi,
            )
        ) return
        if (!infoClosePrimed) {
            infoClosePrimed = true
            return
        }
        infoCloseElapsedSeconds += delta.toDouble()
        modalRemainingSeconds = (modalPostTypingDelaySeconds.toDouble() - infoCloseElapsedSeconds)
            .coerceAtLeast(0.0)
            .toFloat()
        if (infoCloseElapsedSeconds >= modalPostTypingDelaySeconds.toDouble()) resumeModal()
    }

    /** 승리 조건 모달의 다음 페이지를 표시하거나 실행을 재개한다. */
    fun resumeModal() {
        modalNextText?.let { next ->
            currentModalText = next
            modalNextText = null
            modalRemainingSeconds = 3f
            resetInfoTyping()
            return
        }
        if (modalQueuedTexts.isNotEmpty()) {
            currentModalText = modalQueuedTexts.removeFirst()
            startInfoTyping()
            return
        }
        currentModalText = null
        currentModalKind = null
        currentModalFixedText = ""
        currentModalVisibleText = ""
        currentModalTextComplete = true
        modalRemainingSeconds = 0f
        resetInfoTyping()
        onStateChange(PlaybackState.COMPLETE)
        onResumeExecution()
    }

    /** 승리 조건 표시를 요청하고 시나리오 실행을 멈춘다. */
    fun suspendForWinCondition(text: String) {
        stage.showWinCondition(text)
        onStateChange(PlaybackState.MODAL)
    }

    /** 첫 입력은 모달을 닫지 않고 타이핑만 완료한다. */
    fun completeModalTyping() {
        if (!isInfoLayerModal()) {
            modalRemainingSeconds = modalPostTypingDelaySeconds
            return
        }
        modalTypingCursor = currentModalText.orEmpty().length
        currentModalVisibleText = visibleInfoText()
        currentModalTextComplete = true
        explicitModalRemaining = false
        scheduleInfoClose(primed = false)
    }

    /**
     * `suspendForInfo`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun suspendForInfo(
        text: String,
        kind: ScenarioModalKind = ScenarioModalKind.EVENT,
        postTypingDelaySeconds: Float = 1f,
    ) {
        /**
         * `pages` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val pages = if (kind == ScenarioModalKind.INFO) splitInfoPages(text) else listOf(text)
        currentModalText = pages.firstOrNull().orEmpty()
        pages.drop(1).forEach(modalQueuedTexts::addLast)
        currentModalKind = kind
        currentModalFixedText = ""
        modalPostTypingDelaySeconds = postTypingDelaySeconds
        startInfoTyping()
        onStateChange(PlaybackState.MODAL)
    }

    /** 지도 정보의 누적 본문과 자동 닫힘 시간을 설정한다. */
    fun suspendForMapInfo(text: String, changePage: Boolean, wepon: Boolean, wait: Boolean) {
        if (changePage) mapInfoContent = ""
        val separator = if (!changePage && wepon && mapInfoContent.isNotEmpty()) "\n" else ""
        currentModalFixedText = mapInfoContent
        val appended = separator + text
        currentModalText = appended
        currentModalKind = ScenarioModalKind.MAP_INFO
        modalPostTypingDelaySeconds = if (wait) 5f else 1f
        modalRemainingSeconds = appended.length * 0.04f + modalPostTypingDelaySeconds
        mapInfoContent += appended
        stage.setBottomText(mapInfoContent)
        onStateChange(PlaybackState.MODAL)
    }

    /** 장 번호와 이름을 순서대로 표시한다. */
    fun suspendForSection(index: Int, name: String) {
        val digits = listOf(SystemMessage.S_BDA0208A5C, SystemMessage.S_06CF3E90DE, "2", SystemMessage.S_59463E84FB, SystemMessage.S_CA5EEA5B52, SystemMessage.S_FBBD1D1816, SystemMessage.S_959F80BB16, SystemMessage.S_4259DD769C, SystemMessage.S_C6D6E215B1, SystemMessage.S_E8B701A283)
        var value = index
        var chapter = if (value > 0) SystemMessage.S_22EF795E23 else SystemMessage.S_9CA0190DEC
        while (value > 0) {
            chapter = digits[value % 10] + chapter
            value /= 10
        }
        if (index > 0) chapter = "제$chapter"
        currentModalText = chapter
        currentModalKind = ScenarioModalKind.SECTION
        modalNextText = name
        modalRemainingSeconds = 3f
        onStateChange(PlaybackState.MODAL)
    }

    /** 야망 변화 모달을 표시하고 지속 시간을 설정한다. */
    fun suspendForAmbition(delta: Int) {
        ambitionFrom = stage.ambition
        stage.addAmbition(delta)
        ambitionTo = stage.ambition
        ambitionElapsedSeconds = 0f
        ambitionIndicatorEnabled = true
        currentModalText = "ambition"
        currentModalKind = ScenarioModalKind.AMBITION
        currentModalFixedText = ""
        modalNextText = null
        modalRemainingSeconds = 2.5f
        onStateChange(PlaybackState.MODAL)
    }

    /**
     * `splitInfoPages`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun splitInfoPages(text: String): List<String> {
        val pages = mutableListOf<String>()
        var page = ""
        text.split('\n').forEach { line ->
            page = if (page.isEmpty()) line else "$page\n$line"
            if (page.length > 100) {
                pages += page
                page = ""
            }
        }
        if (page.isNotEmpty()) pages += page
        return pages.ifEmpty { listOf("") }
    }

    /** 외부 화면이 사용할 장 표시 상태를 설정한다. */
    fun setSectionPresentation(chapter: String, nextText: String, remainingSeconds: Float) {
        currentModalText = chapter
        currentModalKind = ScenarioModalKind.SECTION
        currentModalFixedText = ""
        modalNextText = nextText
        modalRemainingSeconds = remainingSeconds
        onStateChange(PlaybackState.MODAL)
    }

    /** 외부 화면이 사용할 일반 모달 표시 상태를 설정한다. */
    fun setModalPresentation(text: String, kind: ScenarioModalKind, remainingSeconds: Float) {
        currentModalText = text
        currentModalKind = kind
        currentModalFixedText = ""
        modalNextText = null
        modalQueuedTexts.clear()
        modalRemainingSeconds = remainingSeconds
        if (isInfoLayerModal()) {
            startInfoTyping(explicitRemainingSeconds = remainingSeconds)
        } else {
            resetInfoTyping()
        }
        onStateChange(PlaybackState.MODAL)
    }

    /** EVENT/INFO의 원본 CallbackTimer 한 회를 진행한다. */
    private fun updateInfoTyping(delta: Float) {
        if (!modalTypingPrimed) {
            modalTypingPrimed = true
            modalTypingElapsedSeconds = 0.0
            return
        }
        modalTypingElapsedSeconds += delta.toDouble()
        if (modalTypingElapsedSeconds < INFO_TYPING_INTERVAL_SECONDS) return
        modalTypingElapsedSeconds = 0.0
        revealNextInfoUnit()
        if (modalTypingCursor < currentModalText.orEmpty().length) return

        currentModalTextComplete = true
        if (!explicitModalRemaining) scheduleInfoClose()
    }

    /** 일반 문자 하나 또는 `<...>` 리치 텍스트 태그 하나를 공개한다. */
    private fun revealNextInfoUnit() {
        val text = currentModalText.orEmpty()
        if (modalTypingCursor >= text.length) return
        modalTypingCursor = if (text[modalTypingCursor] == '<') {
            text.indexOf('>', modalTypingCursor).let { close -> if (close == -1) text.length else close + 1 }
        } else {
            modalTypingCursor + 1
        }
        currentModalVisibleText = visibleInfoText()
    }

    /** 현재 공개된 원문에서 렌더러가 사용하지 않는 리치 텍스트 태그를 제거한다. */
    private fun visibleInfoText(): String =
        currentModalText.orEmpty().substring(0, modalTypingCursor).replace(INFO_RICH_TEXT_TAG, "")

    /** 새 InfoLayer 페이지의 타이핑 스케줄러를 초기화한다. */
    private fun startInfoTyping(explicitRemainingSeconds: Float? = null) {
        modalTypingCursor = 0
        modalTypingElapsedSeconds = 0.0
        modalTypingPrimed = false
        currentModalVisibleText = ""
        currentModalTextComplete = false
        explicitModalRemaining = explicitRemainingSeconds != null
        infoCloseElapsedSeconds = 0.0
        infoCloseScheduled = false
        infoClosePrimed = false
        modalRemainingSeconds = explicitRemainingSeconds ?: 0f
    }

    /** InfoLayer가 아닌 모달로 전환할 때 타이핑 전용 상태를 비운다. */
    private fun resetInfoTyping() {
        modalTypingCursor = 0
        modalTypingElapsedSeconds = 0.0
        modalTypingPrimed = false
        explicitModalRemaining = false
        infoCloseElapsedSeconds = 0.0
        infoCloseScheduled = false
        infoClosePrimed = false
    }

    /** 현재 update의 delta를 재사용하지 않고 다음 update부터 닫힘 지연을 센다. */
    private fun scheduleInfoClose(primed: Boolean = true) {
        infoClosePrimed = primed
        infoCloseElapsedSeconds = 0.0
        infoCloseScheduled = true
        modalRemainingSeconds = modalPostTypingDelaySeconds
    }

    private fun isInfoLayerModal(): Boolean =
        currentModalKind == ScenarioModalKind.EVENT || currentModalKind == ScenarioModalKind.INFO

    /** 외부 화면이 사용할 야망 모달 진행 상태를 설정한다. */
    fun setAmbitionPresentation(elapsed: Float, indicatorEnabled: Boolean, remainingSeconds: Float) {
        ambitionElapsedSeconds = elapsed
        ambitionIndicatorEnabled = indicatorEnabled
        modalRemainingSeconds = remainingSeconds
    }

    private companion object {
        const val INFO_TYPING_INTERVAL_SECONDS = .04
        val INFO_RICH_TEXT_TAG = Regex("<[^>]*>")
    }
}

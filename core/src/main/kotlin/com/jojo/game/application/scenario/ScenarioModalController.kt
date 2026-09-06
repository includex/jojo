// Scenario
package com.jojo.game.application.scenario

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

    /** 모달 표시와 대기 상태를 초기화한다. */
    fun reset() {
        currentModalText = null
        currentModalKind = null
        currentModalFixedText = ""
        ambitionFrom = 0
        ambitionTo = 0
        ambitionElapsedSeconds = 0f
        ambitionIndicatorEnabled = true
        modalNextText = null
        modalQueuedTexts.clear()
        modalRemainingSeconds = 0f
        modalPostTypingDelaySeconds = 1f
    }

    /** 모달 시간과 자동 닫힘 여부를 한 프레임 갱신한다. */
    fun update(delta: Float, autoCloseUi: Boolean) {
        if (currentModalKind == ScenarioModalKind.AMBITION) {
            ambitionElapsedSeconds += delta.coerceAtLeast(0f)
        }
        if (modalRemainingSeconds > 0f && ScenarioInterpreter.modalMayAutoClose(
                currentModalKind,
                currentModalText,
                autoCloseUi
            )
        ) {
            modalRemainingSeconds -= delta.coerceAtLeast(0f)
            if (modalRemainingSeconds <= 0f) resumeModal()
        }
    }

    /** 승리 조건 모달의 다음 페이지를 표시하거나 실행을 재개한다. */
    fun resumeModal() {
        modalNextText?.let { next ->
            currentModalText = next
            modalNextText = null
            modalRemainingSeconds = 3f
            return
        }
        if (modalQueuedTexts.isNotEmpty()) {
            currentModalText = modalQueuedTexts.removeFirst()
            modalRemainingSeconds = (currentModalText.orEmpty().length * 0.04f + modalPostTypingDelaySeconds + .35f)
                .coerceAtLeast(modalPostTypingDelaySeconds + .65f)
            return
        }
        currentModalText = null
        currentModalKind = null
        currentModalFixedText = ""
        modalRemainingSeconds = 0f
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
        modalRemainingSeconds = modalPostTypingDelaySeconds
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
        modalRemainingSeconds = (currentModalText.orEmpty().length * 0.04f + postTypingDelaySeconds + .35f)
            .coerceAtLeast(postTypingDelaySeconds + .65f)
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
        val digits = listOf("십", "일", "2", "삼", "넷", "다섯", "육", "칠", "팔", "구")
        var value = index
        var chapter = if (value > 0) "장막" else "서막"
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
        onStateChange(PlaybackState.MODAL)
    }

    /** 외부 화면이 사용할 야망 모달 진행 상태를 설정한다. */
    fun setAmbitionPresentation(elapsed: Float, indicatorEnabled: Boolean, remainingSeconds: Float) {
        ambitionElapsedSeconds = elapsed
        ambitionIndicatorEnabled = indicatorEnabled
        modalRemainingSeconds = remainingSeconds
    }
}

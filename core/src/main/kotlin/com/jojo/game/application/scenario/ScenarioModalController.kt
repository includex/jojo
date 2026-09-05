package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

import java.util.*

/**
 * Manages modal presentations (INFO, SECTION, MAP_INFO, AMBITION, WIN_CONDITION)
 * and their multi-page typing / auto-close lifetimes in the scenario interpreter.
 */
internal class ScenarioModalController(
    private val stage: ScenarioStage,
    private val onStateChange: (PlaybackState) -> Unit,
    private val onResumeExecution: () -> Unit,
) {
    var currentModalText: String? = null
        internal set
    var currentModalKind: ScenarioModalKind? = null
        internal set
    var currentModalFixedText: String = ""
        internal set
    var ambitionFrom: Int = 0
        internal set
    var ambitionTo: Int = 0
        internal set
    var ambitionElapsedSeconds: Float = 0f
        internal set
    var ambitionIndicatorEnabled: Boolean = true
        internal set

    internal var modalNextText: String? = null
    internal val modalQueuedTexts = ArrayDeque<String>()
    internal var mapInfoContent = ""
    internal var modalRemainingSeconds = 0f
    internal var modalPostTypingDelaySeconds = 1f

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

    /**
     * 공개 메서드 `update`
     *
     * ### 파라미터
    - `delta` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `autoCloseUi` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /** BattleScreen.showWinCondition: `pause(); addLayer(... { fn: resume })`. */
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

    /** Production entry for BattleScreen.showWinCondition's pause + layer request pair. */
    fun suspendForWinCondition(text: String) {
        stage.showWinCondition(text)
        onStateChange(PlaybackState.MODAL)
    }

    /** A first panel click finishes typing; it must not also close the layer. */
    fun completeModalTyping() {
        modalRemainingSeconds = modalPostTypingDelaySeconds
    }

    fun suspendForInfo(
        text: String,
        kind: ScenarioModalKind = ScenarioModalKind.EVENT,
        postTypingDelaySeconds: Float = 1f,
    ) {
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

    /** Exact MapInfoLayer.setData accumulation and auto-close contract. */
    fun suspendForMapInfo(text: String, changePage: Boolean, wepon: Boolean, wait: Boolean) {
        if (changePage) mapInfoContent = ""
        val separator = if (!changePage && wepon && mapInfoContent.isNotEmpty()) "\n" else ""
        currentModalFixedText = mapInfoContent
        val appended = separator + text
        currentModalText = appended
        currentModalKind = ScenarioModalKind.MAP_INFO
        // Source types one rich-text token every .04 s, then waits 1 s (5 s
        // when `wait` is set) before AUTO_CLOSE advances the script.
        modalPostTypingDelaySeconds = if (wait) 5f else 1f
        modalRemainingSeconds = appended.length * 0.04f + modalPostTypingDelaySeconds
        mapInfoContent += appended
        stage.setBottomText(mapInfoContent)
        onStateChange(PlaybackState.MODAL)
    }

    /**
     * 공개 메서드 `suspendForSection`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /** HallLayer.addAmbition opens its complete HallMenuLayer for 2.5 s. */
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

    /**
     * 공개 메서드 `setSectionFixture`
     *
     * ### 파라미터
    - `chapter` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `nextText` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `remainingSeconds` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setSectionFixture(chapter: String, nextText: String, remainingSeconds: Float) {
        currentModalText = chapter
        currentModalKind = ScenarioModalKind.SECTION
        currentModalFixedText = ""
        modalNextText = nextText
        modalRemainingSeconds = remainingSeconds
        onStateChange(PlaybackState.MODAL)
    }

    /**
     * 공개 메서드 `setModalFixture`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `kind` (`ScenarioModalKind`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `remainingSeconds` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setModalFixture(text: String, kind: ScenarioModalKind, remainingSeconds: Float) {
        currentModalText = text
        currentModalKind = kind
        currentModalFixedText = ""
        modalNextText = null
        modalQueuedTexts.clear()
        modalRemainingSeconds = remainingSeconds
        onStateChange(PlaybackState.MODAL)
    }

    /**
     * 공개 메서드 `setAmbitionFixture`
     *
     * ### 파라미터
    - `elapsed` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `indicatorEnabled` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `remainingSeconds` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setAmbitionFixture(elapsed: Float, indicatorEnabled: Boolean, remainingSeconds: Float) {
        ambitionElapsedSeconds = elapsed
        ambitionIndicatorEnabled = indicatorEnabled
        modalRemainingSeconds = remainingSeconds
    }
}

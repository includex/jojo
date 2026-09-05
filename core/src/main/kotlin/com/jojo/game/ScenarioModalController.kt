package com.jojo.game

import java.util.ArrayDeque

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
    var currentModalKind: ScenarioInterpreter.ModalKind? = null
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

    fun update(delta: Float, autoCloseUi: Boolean) {
        if (currentModalKind == ScenarioInterpreter.ModalKind.AMBITION) {
            ambitionElapsedSeconds += delta.coerceAtLeast(0f)
        }
        if (modalRemainingSeconds > 0f && ScenarioInterpreter.modalMayAutoClose(currentModalKind, currentModalText, autoCloseUi)) {
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
        kind: ScenarioInterpreter.ModalKind = ScenarioInterpreter.ModalKind.EVENT,
        postTypingDelaySeconds: Float = 1f,
    ) {
        val pages = if (kind == ScenarioInterpreter.ModalKind.INFO) splitInfoPages(text) else listOf(text)
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
        currentModalKind = ScenarioInterpreter.ModalKind.MAP_INFO
        // Source types one rich-text token every .04 s, then waits 1 s (5 s
        // when `wait` is set) before AUTO_CLOSE advances the script.
        modalPostTypingDelaySeconds = if (wait) 5f else 1f
        modalRemainingSeconds = appended.length * 0.04f + modalPostTypingDelaySeconds
        mapInfoContent += appended
        stage.setBottomText(mapInfoContent)
        onStateChange(PlaybackState.MODAL)
    }

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
        currentModalKind = ScenarioInterpreter.ModalKind.SECTION
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
        currentModalKind = ScenarioInterpreter.ModalKind.AMBITION
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

    fun setSectionFixture(chapter: String, nextText: String, remainingSeconds: Float) {
        currentModalText = chapter
        currentModalKind = ScenarioInterpreter.ModalKind.SECTION
        currentModalFixedText = ""
        modalNextText = nextText
        modalRemainingSeconds = remainingSeconds
        onStateChange(PlaybackState.MODAL)
    }

    fun setModalFixture(text: String, kind: ScenarioInterpreter.ModalKind, remainingSeconds: Float) {
        currentModalText = text
        currentModalKind = kind
        currentModalFixedText = ""
        modalNextText = null
        modalQueuedTexts.clear()
        modalRemainingSeconds = remainingSeconds
        onStateChange(PlaybackState.MODAL)
    }

    fun setAmbitionFixture(elapsed: Float, indicatorEnabled: Boolean, remainingSeconds: Float) {
        ambitionElapsedSeconds = elapsed
        ambitionIndicatorEnabled = indicatorEnabled
        modalRemainingSeconds = remainingSeconds
    }
}

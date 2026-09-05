package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

/**
 * class  `ScenarioDialogueCoordinator`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ScenarioDialogueCoordinator(
    private val stage: ScenarioStage,
    private val onStateChange: (PlaybackState) -> Unit,
    private val onResumeExecution: () -> Unit,
    private val onSetDelayRemainingSeconds: (Float) -> Unit,
) {
    var currentDialogue: Dialogue? = null
        private set
    var dialogueRevision: Long = 0
        private set
    var dialogueLifecycleRevision: Long = 0
        private set
    var currentDialogueSourceText: String? = null
        private set
    var currentDialogueSide: Int = 0
        private set
    var currentDialogueAtTop: Boolean = false
        private set
    var lastDialogueSpeakerId: Int = -1
        private set
    var dialogueSpeakerIndex: Int = 0
        private set
    val pendingDialogues = ArrayDeque<Dialogue>()

    var dialogueCallbackFramePending = false
        private set
    var dialogueCallbackReturnState: PlaybackState? = null
        private set
    var externalDialogueReturnState: PlaybackState? = null
        private set

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
        currentDialogue = null
        currentDialogueSourceText = null
        pendingDialogues.clear()
        dialogueCallbackFramePending = false
        dialogueCallbackReturnState = null
        externalDialogueReturnState = null
    }

    /**
     * 공개 메서드 `resetSpeakers`
     *
     * ### 파라미터
    - `speakerIndex` (`Int = 0`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun resetSpeakers(speakerIndex: Int = 0) {
        dialogueSpeakerIndex = speakerIndex
        lastDialogueSpeakerId = -1
    }

    /**
     * 공개 메서드 `advanceDialogue`
     *
     * ### 파라미터
    - `deferCloseCallbackFrame` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `currentState` (`PlaybackState`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun advanceDialogue(deferCloseCallbackFrame: Boolean = false, currentState: PlaybackState) {
        check(currentState == PlaybackState.DIALOGUE) { "대기 중인 대사가 없습니다." }
        if (pendingDialogues.isNotEmpty()) {
            presentDialogue(pendingDialogues.removeFirst())
            return
        }
        currentDialogue = null
        currentDialogueSourceText = null
        if (deferCloseCallbackFrame) {
            dialogueCallbackReturnState = externalDialogueReturnState
            if (externalDialogueReturnState == null) onSetDelayRemainingSeconds(0f)
            externalDialogueReturnState = null
            dialogueCallbackFramePending = true
            onStateChange(PlaybackState.DELAY)
            return
        }
        externalDialogueReturnState?.let {
            externalDialogueReturnState = null
            onStateChange(it)
            return
        }
        onResumeExecution()
    }

    /**
     * 공개 메서드 `presentExternalBattleDialogue`
     *
     * ### 파라미터
    - `dialogue` (`Dialogue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `currentState` (`PlaybackState`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun presentExternalBattleDialogue(dialogue: Dialogue, currentState: PlaybackState) {
        check(currentDialogue == null && currentState != PlaybackState.DIALOGUE) { "이미 대사가 표시 중입니다." }
        check(externalDialogueReturnState == null) { "외부 전투 대사가 이미 대기 중입니다." }
        externalDialogueReturnState = currentState
        beginDialogueLifecycle(dialogue.speakerId?.let { "&$it\n${dialogue.text}" } ?: dialogue.text)
        presentDialogue(dialogue)
        onStateChange(PlaybackState.DIALOGUE)
    }

    /**
     * 공개 메서드 `presentDialogue`
     *
     * ### 파라미터
    - `dialogue` (`Dialogue`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun presentDialogue(dialogue: Dialogue) {
        currentDialogueAtTop = false
        dialogue.speakerId?.toIntOrNull()?.let { speakerId ->
            if (speakerId in stage.units) {
                if (speakerId != lastDialogueSpeakerId) dialogueSpeakerIndex++
                if (speakerId == 0) dialogueSpeakerIndex = 0
                lastDialogueSpeakerId = speakerId
                val unit = stage.units.getValue(speakerId)
                currentDialogueAtTop = 424f - 4f * (unit.visualX + unit.visualY) < -50f
            }
        }
        currentDialogueSide = Math.floorMod(dialogueSpeakerIndex, 2)
        currentDialogue = dialogue
        dialogueRevision++
    }

    /**
     * 공개 메서드 `beginDialogueLifecycle`
     *
     * ### 파라미터
    - `sourceText` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun beginDialogueLifecycle(sourceText: String) {
        currentDialogueSourceText = sourceText
        dialogueLifecycleRevision++
    }

    /**
     * 공개 메서드 `startSay`
     *
     * ### 파라미터
    - `sourceText` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun startSay(sourceText: String) {
        val dialogues = parseDialogueBlocks(sourceText)
        beginDialogueLifecycle(sourceText)
        presentDialogue(dialogues.firstOrNull() ?: Dialogue(null, ""))
        dialogues.drop(1).forEach(pendingDialogues::addLast)
        onStateChange(PlaybackState.DIALOGUE)
    }

    /**
     * 공개 메서드 `startTalk`
     *
     * ### 파라미터
    - `primary` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `fallback` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `activeCharacterIds` (`Set<Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun startTalk(primary: Int, fallback: Int, text: String, activeCharacterIds: Set<Int>) {
        val speaker = if (primary in activeCharacterIds) primary else fallback
        val sourceText = "&$speaker\n$text"
        beginDialogueLifecycle(sourceText)
        presentDialogue(Dialogue(speaker.toString(), text))
        onStateChange(PlaybackState.DIALOGUE)
    }

    /**
     * 공개 메서드 `handleDelayTick`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun handleDelayTick(): Boolean {
        if (dialogueCallbackFramePending) {
            dialogueCallbackFramePending = false
            return true
        }
        dialogueCallbackReturnState?.let {
            dialogueCallbackReturnState = null
            onStateChange(it)
            return true
        }
        return false
    }

    companion object {
        /**
         * 공개 메서드 `parseDialogueBlocks`
         *
         * ### 파라미터
        - `raw` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `List<Dialogue>`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun parseDialogueBlocks(raw: String): List<Dialogue> {
            val tags = Regex("""(?m)^&(\d+)\n""").findAll(raw).toList()
            if (tags.isEmpty()) return listOf(Dialogue(null, raw))
            val result = mutableListOf<Dialogue>()
            val preamble = raw.substring(0, tags.first().range.first).trim()
            if (preamble.isNotEmpty()) result += Dialogue(null, preamble)
            tags.forEachIndexed { index, tag ->
                val end = tags.getOrNull(index + 1)?.range?.first ?: raw.length
                result += Dialogue(tag.groupValues[1], raw.substring(tag.range.last + 1, end).trim())
            }
            return result.filter { it.text.isNotEmpty() }
        }
    }
}

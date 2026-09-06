// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*


/**
 * `ScenarioDialogueCoordinator` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class ScenarioDialogueCoordinator(
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
    /**
     * `onSetDelayRemainingSeconds` ((Float) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onSetDelayRemainingSeconds: (Float) -> Unit,
) {
    /**
     * `currentDialogue` (Dialogue?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentDialogue: Dialogue? = null
        private set
    /**
     * `dialogueRevision` (Long): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var dialogueRevision: Long = 0
        private set
    /**
     * `dialogueLifecycleRevision` (Long): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var dialogueLifecycleRevision: Long = 0
        private set
    /**
     * `currentDialogueSourceText` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentDialogueSourceText: String? = null
        private set
    /**
     * `currentDialogueSide` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentDialogueSide: Int = 0
        private set
    /**
     * `currentDialogueAtTop` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentDialogueAtTop: Boolean = false
        private set
    /**
     * `lastDialogueSpeakerId` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lastDialogueSpeakerId: Int = -1
        private set
    /**
     * `dialogueSpeakerIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var dialogueSpeakerIndex: Int = 0
        private set
    /**
     * `pendingDialogues` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val pendingDialogues = ArrayDeque<Dialogue>()

    /**
     * `dialogueCallbackFramePending` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var dialogueCallbackFramePending = false
        private set
    /**
     * `dialogueCallbackReturnState` (PlaybackState?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var dialogueCallbackReturnState: PlaybackState? = null
        private set
    /**
     * `externalDialogueReturnState` (PlaybackState?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var externalDialogueReturnState: PlaybackState? = null
        private set


    /**
     * `reset`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
     * `resetSpeakers`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun resetSpeakers(speakerIndex: Int = 0) {
        dialogueSpeakerIndex = speakerIndex
        lastDialogueSpeakerId = -1
    }


    /**
     * `advanceDialogue`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
     * `presentExternalBattleDialogue`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
     * `presentDialogue`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
     * `beginDialogueLifecycle`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun beginDialogueLifecycle(sourceText: String) {
        currentDialogueSourceText = sourceText
        dialogueLifecycleRevision++
    }


    /**
     * `startSay`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun startSay(sourceText: String) {
        val dialogues = parseDialogueBlocks(sourceText)
        beginDialogueLifecycle(sourceText)
        presentDialogue(dialogues.firstOrNull() ?: Dialogue(null, ""))
        dialogues.drop(1).forEach(pendingDialogues::addLast)
        onStateChange(PlaybackState.DIALOGUE)
    }


    /**
     * `startTalk`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun startTalk(primary: Int, fallback: Int, text: String, activeCharacterIds: Set<Int>) {
        val speaker = if (primary in activeCharacterIds) primary else fallback
        val sourceText = "&$speaker\n$text"
        beginDialogueLifecycle(sourceText)
        presentDialogue(Dialogue(speaker.toString(), text))
        onStateChange(PlaybackState.DIALOGUE)
    }


    /**
     * `handleDelayTick`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
         * `parseDialogueBlocks`: 입력을 규칙에 따라 계산·변환한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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

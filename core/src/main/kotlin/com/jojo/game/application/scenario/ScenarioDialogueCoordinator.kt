// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*


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


    fun reset() {
        currentDialogue = null
        currentDialogueSourceText = null
        pendingDialogues.clear()
        dialogueCallbackFramePending = false
        dialogueCallbackReturnState = null
        externalDialogueReturnState = null
    }


    fun resetSpeakers(speakerIndex: Int = 0) {
        dialogueSpeakerIndex = speakerIndex
        lastDialogueSpeakerId = -1
    }


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


    fun presentExternalBattleDialogue(dialogue: Dialogue, currentState: PlaybackState) {
        check(currentDialogue == null && currentState != PlaybackState.DIALOGUE) { "이미 대사가 표시 중입니다." }
        check(externalDialogueReturnState == null) { "외부 전투 대사가 이미 대기 중입니다." }
        externalDialogueReturnState = currentState
        beginDialogueLifecycle(dialogue.speakerId?.let { "&$it\n${dialogue.text}" } ?: dialogue.text)
        presentDialogue(dialogue)
        onStateChange(PlaybackState.DIALOGUE)
    }


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


    fun beginDialogueLifecycle(sourceText: String) {
        currentDialogueSourceText = sourceText
        dialogueLifecycleRevision++
    }


    fun startSay(sourceText: String) {
        val dialogues = parseDialogueBlocks(sourceText)
        beginDialogueLifecycle(sourceText)
        presentDialogue(dialogues.firstOrNull() ?: Dialogue(null, ""))
        dialogues.drop(1).forEach(pendingDialogues::addLast)
        onStateChange(PlaybackState.DIALOGUE)
    }


    fun startTalk(primary: Int, fallback: Int, text: String, activeCharacterIds: Set<Int>) {
        val speaker = if (primary in activeCharacterIds) primary else fallback
        val sourceText = "&$speaker\n$text"
        beginDialogueLifecycle(sourceText)
        presentDialogue(Dialogue(speaker.toString(), text))
        onStateChange(PlaybackState.DIALOGUE)
    }


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

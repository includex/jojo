package com.jojo.game

class ScenarioPlayback(val timeline: ScenarioTimeline) {
    val stage = ScenarioStage()
    var state: PlaybackState = PlaybackState.COMPLETE
        private set
    var currentDialogue: Dialogue? = null
        private set
    var currentChoice: Choice? = null
        private set
    var selectedChoice: Int = 0
        private set
    var chosenOption: String? = null
        private set
    private var nextCommandIndex = 0

    init {
        runUntilInput()
    }

    fun advanceDialogue() {
        check(state == PlaybackState.DIALOGUE) { "대기 중인 대사가 없습니다." }
        currentDialogue = null
        runUntilInput()
    }

    fun selectPrevious() {
        val options = currentChoice?.options ?: return
        selectedChoice = Math.floorMod(selectedChoice - 1, options.size)
    }

    fun selectNext() {
        val options = currentChoice?.options ?: return
        selectedChoice = Math.floorMod(selectedChoice + 1, options.size)
    }

    fun confirmChoice() {
        check(state == PlaybackState.CHOICE) { "대기 중인 선택지가 없습니다." }
        chosenOption = currentChoice!!.options[selectedChoice]
        currentChoice = null
        state = PlaybackState.COMPLETE
    }

    private fun runUntilInput() {
        while (nextCommandIndex < timeline.commands.size) {
            when (val command = timeline.commands[nextCommandIndex++]) {
                is ScenarioCommand.DialogueLine -> {
                    currentDialogue = command.dialogue
                    state = PlaybackState.DIALOGUE
                    return
                }
                is ScenarioCommand.Choose -> {
                    currentChoice = command.choice
                    selectedChoice = 0
                    state = PlaybackState.CHOICE
                    return
                }
                else -> stage.apply(command)
            }
        }
        state = PlaybackState.COMPLETE
    }
}

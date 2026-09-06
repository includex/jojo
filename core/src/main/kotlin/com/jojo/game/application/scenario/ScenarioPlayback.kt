// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

/** 입력 대기가 필요한 시나리오 명령을 순서대로 재생한다. */
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

    /** 현재 대사를 닫고 다음 입력 지점까지 진행한다. */
    fun advanceDialogue() {
        check(state == PlaybackState.DIALOGUE) { "대기 중인 대사가 없습니다." }
        currentDialogue = null
        runUntilInput()
    }

    /** 선택지를 이전 항목으로 순환한다. */
    fun selectPrevious() {
        val options = currentChoice?.options ?: return
        selectedChoice = Math.floorMod(selectedChoice - 1, options.size)
    }

    /** 선택지를 다음 항목으로 순환한다. */
    fun selectNext() {
        val options = currentChoice?.options ?: return
        selectedChoice = Math.floorMod(selectedChoice + 1, options.size)
    }

    /** 현재 선택지를 확정하고 재생을 마친다. */
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

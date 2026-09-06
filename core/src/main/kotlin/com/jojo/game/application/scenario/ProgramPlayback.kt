// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.domain.scenario.*

import java.util.*

/** 대화형 스크립트의 대입, 선택, 조건 분기를 재생한다. */
class ProgramPlayback(val program: ScenarioScript) {
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
    private var pendingChoiceVariable: String? = null
    private val variables = mutableMapOf<String, Int>()
    private val queue = ArrayDeque<ScriptStep>()

    init {
        queue.addAll(program.steps)
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
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice - 1, it.size) }
    }

    /** 선택지를 다음 항목으로 순환한다. */
    fun selectNext() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice + 1, it.size) }
    }

    /** 현재 선택지를 변수에 기록하고 재생을 이어간다. */
    fun confirmChoice() {
        check(state == PlaybackState.CHOICE) { "대기 중인 선택지가 없습니다." }
        val choice = requireNotNull(currentChoice)
        chosenOption = choice.options[selectedChoice]
        variables[requireNotNull(pendingChoiceVariable)] = selectedChoice + 1 // 원본 스크립트는 선택지를 1부터 센다.
        currentChoice = null
        pendingChoiceVariable = null
        runUntilInput()
    }

    private fun runUntilInput() {
        while (queue.isNotEmpty()) {
            when (val step = queue.removeFirst()) {
                is ScriptStep.Command -> when (val command = step.command) {
                    is ScenarioCommand.DialogueLine -> {
                        currentDialogue = command.dialogue
                        state = PlaybackState.DIALOGUE
                        return
                    }

                    else -> stage.apply(command)
                }

                is ScriptStep.PromptChoice -> {
                    currentChoice = step.choice
                    selectedChoice = 0
                    pendingChoiceVariable = step.variable
                    state = PlaybackState.CHOICE
                    return
                }

                is ScriptStep.AssignInt -> variables[step.variable] = step.value
                is ScriptStep.Conditional -> prepend(if (variables[step.variable] == step.expected) step.whenTrue else step.whenFalse)
            }
        }
        state = PlaybackState.COMPLETE
    }

    private fun prepend(steps: List<ScriptStep>) {
        steps.asReversed().forEach(queue::addFirst)
    }
}

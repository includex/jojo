// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.domain.scenario.*

import java.util.*

/** 대화형 스크립트의 대입, 선택, 조건 분기를 재생한다. */
class ProgramPlayback(val program: ScenarioScript) {
    /**
     * `stage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val stage = ScenarioStage()
    /**
     * `state` (PlaybackState): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var state: PlaybackState = PlaybackState.COMPLETE
        private set
    /**
     * `currentDialogue` (Dialogue?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentDialogue: Dialogue? = null
        private set
    /**
     * `currentChoice` (Choice?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var currentChoice: Choice? = null
        private set
    /**
     * `selectedChoice` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var selectedChoice: Int = 0
        private set
    /**
     * `chosenOption` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var chosenOption: String? = null
        private set
    /**
     * `pendingChoiceVariable` (String?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var pendingChoiceVariable: String? = null
    /**
     * `variables` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val variables = mutableMapOf<String, Int>()
    /**
     * `queue` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

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

    /**
     * `runUntilInput`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `prepend`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun prepend(steps: List<ScriptStep>) {
        steps.asReversed().forEach(queue::addFirst)
    }
}

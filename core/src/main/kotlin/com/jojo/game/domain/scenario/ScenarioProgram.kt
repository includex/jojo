package com.jojo.game.domain.scenario

import com.jojo.game.*

import java.util.*

sealed interface ScriptStep {
    /**
     * data class  `Command`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Command(val command: ScenarioCommand) : ScriptStep

    /**
     * data class  `PromptChoice`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class PromptChoice(val variable: String, val choice: Choice) : ScriptStep

    /**
     * data class  `AssignInt`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class AssignInt(val variable: String, val value: Int) : ScriptStep

    /**
     * data class  `Conditional`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Conditional(
        val variable: String,
        val expected: Int,
        val whenTrue: List<ScriptStep>,
        val whenFalse: List<ScriptStep>
    ) : ScriptStep
}

/**
 * data class  `ScenarioScript`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class ScenarioScript(val moduleName: String, val steps: List<ScriptStep>, val displayText: List<String>)

/**
 * Restricted source-level Python executor. It currently models the control
 * flow used by dialogue sections: assignments, choice result variables and
 * equality branches. Unsupported game calls are intentionally skipped until
 * their matching Stage API is implemented, rather than being faked.
 */
/**
 * object  `ScenarioProgram`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */


/**
 * class  `ProgramPlayback`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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

    /**
     * 공개 메서드 `advanceDialogue`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun advanceDialogue() {
        check(state == PlaybackState.DIALOGUE) { "대기 중인 대사가 없습니다." }
        currentDialogue = null
        runUntilInput()
    }

    /**
     * 공개 메서드 `selectPrevious`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectPrevious() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice - 1, it.size) }
    }

    /**
     * 공개 메서드 `selectNext`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectNext() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice + 1, it.size) }
    }

    /**
     * 공개 메서드 `confirmChoice`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun confirmChoice() {
        check(state == PlaybackState.CHOICE) { "대기 중인 선택지가 없습니다." }
        val choice = requireNotNull(currentChoice)
        chosenOption = choice.options[selectedChoice]
        variables[requireNotNull(pendingChoiceVariable)] = selectedChoice + 1 // Original scripts use one-based choices.
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

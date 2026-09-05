package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

/**
 * class  `ScenarioPlayback`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
        val options = currentChoice?.options ?: return
        selectedChoice = Math.floorMod(selectedChoice - 1, options.size)
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
        val options = currentChoice?.options ?: return
        selectedChoice = Math.floorMod(selectedChoice + 1, options.size)
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

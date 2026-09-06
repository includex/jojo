// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue
import java.util.*

/**
 * `ScenarioChoiceCoordinator` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal class ScenarioChoiceCoordinator(
    /**
     * `onStateChange` ((PlaybackState) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onStateChange: (PlaybackState) -> Unit,
) {
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
     * `isAskChoice` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var isAskChoice: Boolean = false
        private set
    /**
     * `chosenOption` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var chosenOption: String? = null
        private set
    /**
     * `currentChoiceFunction` (String?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var currentChoiceFunction: String? = null
    /**
     * `currentChoiceLine` (Int?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var currentChoiceLine: Int? = null
    /**
     * `choiceTrace` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val choiceTrace = mutableListOf<ScenarioChoiceTrace>()

    /**
     * `pendingChoiceTarget` (JsonValue?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pendingChoiceTarget: JsonValue? = null
        private set
    /**
     * `pendingAskStatement` (JsonValue?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pendingAskStatement: JsonValue? = null
        private set
    /**
     * `pendingAskFrame` (Frame?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pendingAskFrame: Frame? = null
        private set
    /**
     * `pendingAskResult` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pendingAskResult: Int? = null
        private set


    /**
     * `reset`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun reset() {
        currentChoice = null
        selectedChoice = 0
        isAskChoice = false
        chosenOption = null
        currentChoiceFunction = null
        currentChoiceLine = null
        choiceTrace.clear()
        pendingChoiceTarget = null
        pendingAskStatement = null
        pendingAskFrame = null
        pendingAskResult = null
    }


    /**
     * `setDirectChoice`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setDirectChoice(choice: Choice?, selected: Int, isAsk: Boolean) {
        currentChoice = choice
        selectedChoice = selected
        isAskChoice = isAsk
    }


    /**
     * `selectPrevious`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectPrevious() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice - 1, it.size) }
    }


    /**
     * `selectNext`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectNext() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice + 1, it.size) }
    }


    /**
     * `selectChoice`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectChoice(index: Int) {
        val choice = currentChoice ?: return
        selectedChoice = index.coerceIn(0, choice.options.lastIndex)
    }


    /**
     * `setChoiceSource`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setChoiceSource(node: JsonValue, frame: Frame, moduleName: String) {
        currentChoiceFunction = frame.sourceFunction
        currentChoiceLine = node.get("location")?.getInt("line", -1)?.takeIf { it > 0 }
            ?: error("$moduleName ${frame.function.name} choice has no source line")
    }

    /**
     * `startChoice`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startChoice(
        choice: Choice,
        node: JsonValue,
        frame: Frame,
        moduleName: String,
        target: JsonValue? = null,
    ) {
        currentChoice = choice
        selectedChoice = 0
        setChoiceSource(node, frame, moduleName)
        pendingChoiceTarget = target
        onStateChange(PlaybackState.CHOICE)
    }

    /**
     * `startAsk`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startAsk(
        ask: JsonValue,
        statement: JsonValue,
        frame: Frame,
        moduleName: String,
    ) {
        currentChoice = Choice(listOf("예", "비"), null)
        selectedChoice = 0
        isAskChoice = true
        setChoiceSource(ask, frame, moduleName)
        pendingAskStatement = statement
        pendingAskFrame = frame
        onStateChange(PlaybackState.CHOICE)
    }

    /**
     * `confirmChoice`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun confirmChoice(
        currentState: PlaybackState,
        moduleName: String,
        frames: ArrayDeque<Frame>,
        evalBoolean: (JsonValue, Frame) -> Boolean,
        assign: (JsonValue, Any?, Frame) -> Unit,
        onResumeExecution: () -> Unit,
    ) {
        check(currentState == PlaybackState.CHOICE) { "대기 중인 선택지가 없습니다." }
        /**
         * `choice` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val choice = requireNotNull(currentChoice)
        choiceTrace += ScenarioChoiceTrace(
            module = moduleName,
            function = requireNotNull(currentChoiceFunction) { "choice source function is missing" },
            line = requireNotNull(currentChoiceLine) { "choice source line is missing" },
            option = selectedChoice,
            optionCount = choice.options.size,
        )
        chosenOption = choice.options[selectedChoice]
        pendingAskStatement?.let { statement ->
            /**
             * `sourceFrame` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val sourceFrame = requireNotNull(pendingAskFrame)
            pendingAskResult = if (selectedChoice == 0) 1 else 0
            /**
             * `selected` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val selected = if (evalBoolean(statement.field("test"), sourceFrame)) {
                statement.field("body")
            } else {
                statement.field("orelse")
            }
            pendingAskResult = null
            pendingAskStatement = null
            pendingAskFrame = null
            isAskChoice = false
            currentChoice = null
            /**
             * `statements` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val statements = selected.children().toList()
            if (statements.isNotEmpty()) frames.addLast(
                Frame(
                    RuntimeFunction("<if>", statements, emptyMap()),
                    0,
                    sourceFrame.locals,
                    sourceFrame.sourceFunction
                ),
            )
            onResumeExecution()
            return
        }
        pendingChoiceTarget?.let { target ->
            /**
             * `targetFrame` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val targetFrame = frames.peekLast() ?: error("실행 프레임이 없습니다.")
            assign(target, selectedChoice + 1, targetFrame)
        }
        pendingChoiceTarget = null
        currentChoice = null
        onResumeExecution()
    }

    companion object {

        /**
         * `isStageChoice`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun isStageChoice(node: JsonValue): Boolean =
            node.typeName() == "Call" && node.field("func").expressionPath() == "stage.choice"


        /**
         * `findStageAsk`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun findStageAsk(node: JsonValue): JsonValue? {
            if (node.get("type") != null && node.typeName() == "Call" && node.field("func")
                    .expressionPath() == "stage.ask"
            ) return node
            var child = node.child
            while (child != null) {
                findStageAsk(child)?.let { return it }
                child = child.next
            }
            return null
        }
    }
}

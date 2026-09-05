package com.jojo.game

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue
import java.util.*

internal class ScenarioChoiceCoordinator(
    private val onStateChange: (PlaybackState) -> Unit,
) {
    var currentChoice: Choice? = null
        private set
    var selectedChoice: Int = 0
        private set
    var isAskChoice: Boolean = false
        private set
    var chosenOption: String? = null
        private set
    private var currentChoiceFunction: String? = null
    private var currentChoiceLine: Int? = null
    val choiceTrace = mutableListOf<ScenarioInterpreter.ChoiceTrace>()

    var pendingChoiceTarget: JsonValue? = null
        private set
    var pendingAskStatement: JsonValue? = null
        private set
    var pendingAskFrame: Frame? = null
        private set
    var pendingAskResult: Int? = null
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
     * 공개 메서드 `setDirectChoice`
     *
     * ### 파라미터
    - `choice` (`Choice?`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `selected` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `isAsk` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setDirectChoice(choice: Choice?, selected: Int, isAsk: Boolean) {
        currentChoice = choice
        selectedChoice = selected
        isAskChoice = isAsk
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
     * 공개 메서드 `selectChoice`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectChoice(index: Int) {
        val choice = currentChoice ?: return
        selectedChoice = index.coerceIn(0, choice.options.lastIndex)
    }

    /**
     * 공개 메서드 `setChoiceSource`
     *
     * ### 파라미터
    - `node` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `moduleName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setChoiceSource(node: JsonValue, frame: Frame, moduleName: String) {
        currentChoiceFunction = frame.sourceFunction
        currentChoiceLine = node.get("location")?.getInt("line", -1)?.takeIf { it > 0 }
            ?: error("$moduleName ${frame.function.name} choice has no source line")
    }

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

    fun confirmChoice(
        currentState: PlaybackState,
        moduleName: String,
        frames: ArrayDeque<Frame>,
        evalBoolean: (JsonValue, Frame) -> Boolean,
        assign: (JsonValue, Any?, Frame) -> Unit,
        onResumeExecution: () -> Unit,
    ) {
        check(currentState == PlaybackState.CHOICE) { "대기 중인 선택지가 없습니다." }
        val choice = requireNotNull(currentChoice)
        choiceTrace += ScenarioInterpreter.ChoiceTrace(
            module = moduleName,
            function = requireNotNull(currentChoiceFunction) { "choice source function is missing" },
            line = requireNotNull(currentChoiceLine) { "choice source line is missing" },
            option = selectedChoice,
            optionCount = choice.options.size,
        )
        chosenOption = choice.options[selectedChoice]
        pendingAskStatement?.let { statement ->
            val sourceFrame = requireNotNull(pendingAskFrame)
            pendingAskResult = if (selectedChoice == 0) 1 else 0
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
            val targetFrame = frames.peekLast() ?: error("실행 프레임이 없습니다.")
            assign(target, selectedChoice + 1, targetFrame)
        }
        pendingChoiceTarget = null
        currentChoice = null
        onResumeExecution()
    }

    companion object {
        /**
         * 공개 메서드 `isStageChoice`
         *
         * ### 파라미터
        - `node` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Boolean`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun isStageChoice(node: JsonValue): Boolean =
            node.typeName() == "Call" && node.field("func").expressionPath() == "stage.choice"

        /**
         * 공개 메서드 `findStageAsk`
         *
         * ### 파라미터
        - `node` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `JsonValue?`
         * - 반환값: 동작 결과의 도메인 값입니다.
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

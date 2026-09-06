// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

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
    val choiceTrace = mutableListOf<ScenarioChoiceTrace>()

    var pendingChoiceTarget: JsonValue? = null
        private set
    var pendingAskStatement: JsonValue? = null
        private set
    var pendingAskFrame: Frame? = null
        private set
    var pendingAskResult: Int? = null
        private set


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


    fun setDirectChoice(choice: Choice?, selected: Int, isAsk: Boolean) {
        currentChoice = choice
        selectedChoice = selected
        isAskChoice = isAsk
    }


    fun selectPrevious() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice - 1, it.size) }
    }


    fun selectNext() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice + 1, it.size) }
    }


    fun selectChoice(index: Int) {
        val choice = currentChoice ?: return
        selectedChoice = index.coerceIn(0, choice.options.lastIndex)
    }


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
        choiceTrace += ScenarioChoiceTrace(
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

        fun isStageChoice(node: JsonValue): Boolean =
            node.typeName() == "Call" && node.field("func").expressionPath() == "stage.choice"


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

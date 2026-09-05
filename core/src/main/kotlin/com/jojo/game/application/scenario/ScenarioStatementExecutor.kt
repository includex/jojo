package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

internal class ScenarioStatementExecutor(
    private val moduleName: String,
    private val maxStatementsPerStart: Int = 100_000,
) {
    fun runUntilInput(
        callStack: ScenarioCallStack,
        isEnded: () -> Boolean,
        getState: () -> PlaybackState,
        setState: (PlaybackState) -> Unit,
        executeStatement: (JsonValue, Frame) -> Unit,
    ) {
        setState(PlaybackState.COMPLETE)
        var executedStatements = 0
        while (callStack.frames.isNotEmpty() && !isEnded()) {
            check(++executedStatements <= maxStatementsPerStart) {
                "$moduleName 실행이 $maxStatementsPerStart 문장을 초과했습니다. 무한 goto/호출을 확인하세요."
            }
            val frame = callStack.frames.peekLast() ?: break
            if (frame.index >= frame.function.statements.size) {
                callStack.frames.removeLast()
                continue
            }
            val statement = frame.function.statements[frame.index++]
            executeStatement(statement, frame)
            if (getState() != PlaybackState.COMPLETE) return
        }
    }

    fun executeStatement(
        statement: JsonValue,
        frame: Frame,
        choiceCoordinator: ScenarioChoiceCoordinator,
        eval: (JsonValue, Frame) -> Any?,
        evalBoolean: (JsonValue, Frame) -> Boolean,
        evalArguments: (JsonValue, Frame) -> List<Any?>,
        assign: (JsonValue, Any?, Frame) -> Unit,
        callStack: ScenarioCallStack,
    ) {
        when (statement.typeName()) {
            "Expr" -> eval(statement.field("value"), frame)
            "Assign" -> {
                val valueNode = statement.field("value")
                if (ScenarioChoiceCoordinator.isStageChoice(valueNode)) {
                    val args = evalArguments(valueNode.field("args"), frame)
                    choiceCoordinator.startChoice(
                        choice = Choice(
                            args.firstOrNull().asText().lineSequence().map(String::trim).filter(String::isNotEmpty)
                                .toList(),
                            args.getOrNull(1)?.asInt()?.takeIf { it >= 0 },
                        ),
                        node = valueNode,
                        frame = frame,
                        moduleName = moduleName,
                        target = statement.field("targets").children().firstOrNull(),
                    )
                } else {
                    val value = eval(valueNode, frame)
                    statement.field("targets").children().forEach { assign(it, value, frame) }
                }
            }

            "AugAssign" -> {
                val target = statement.field("target")
                val current = eval(target, frame).asInt()
                val value = eval(statement.field("value"), frame).asInt()
                val result = when (statement.field("op").typeName()) {
                    "Add" -> current + value
                    "Sub" -> current - value
                    "Mult" -> current * value
                    "Mod" -> current % value
                    else -> current
                }
                assign(target, result, frame)
            }

            "If" -> {
                val ask = ScenarioChoiceCoordinator.findStageAsk(statement.field("test"))
                if (ask != null && choiceCoordinator.pendingAskResult == null) {
                    choiceCoordinator.startAsk(ask, statement, frame, moduleName)
                    return
                }
                val selected = if (evalBoolean(
                        statement.field("test"),
                        frame
                    )
                ) statement.field("body") else statement.field("orelse")
                val statements = selected.children().toList()
                if (statements.isNotEmpty()) callStack.frames.addLast(
                    Frame(RuntimeFunction("<if>", statements, emptyMap()), 0, frame.locals, frame.sourceFunction),
                )
            }

            "For" -> executeFor(statement, frame, eval, assign, callStack)
            "Return" -> callStack.frames.removeLast()
        }
    }

    private fun executeFor(
        statement: JsonValue,
        frame: Frame,
        eval: (JsonValue, Frame) -> Any?,
        assign: (JsonValue, Any?, Frame) -> Unit,
        callStack: ScenarioCallStack,
    ) {
        val iterable = eval(statement.field("iter"), frame) as? List<*> ?: emptyList<Any?>()
        val body = statement.field("body").children().toList()
        iterable.asReversed().forEach { item ->
            val scoped = frame.locals.toMutableMap()
            assign(statement.field("target"), item, Frame(frame.function, frame.index, scoped))
            callStack.frames.addLast(Frame(RuntimeFunction("<for>", body, emptyMap()), 0, scoped, frame.sourceFunction))
        }
    }
}

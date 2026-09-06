// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

/**
 * `ScenarioStatementExecutor` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal class ScenarioStatementExecutor(
    /**
     * `moduleName` (String,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val moduleName: String,
    /**
     * `maxStatementsPerStart` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val maxStatementsPerStart: Int = 100_000,
) {
    /**
     * `runUntilInput`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun runUntilInput(
        callStack: ScenarioCallStack,
        isEnded: () -> Boolean,
        getState: () -> PlaybackState,
        setState: (PlaybackState) -> Unit,
        executeStatement: (JsonValue, Frame) -> Unit,
    ) {
        setState(PlaybackState.COMPLETE)
        /**
         * `executedStatements` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var executedStatements = 0
        while (callStack.frames.isNotEmpty() && !isEnded()) {
            check(++executedStatements <= maxStatementsPerStart) {
                "$moduleName 실행이 $maxStatementsPerStart 문장을 초과했습니다. 무한 goto/호출을 확인하세요."
            }
            /**
             * `frame` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val frame = callStack.frames.peekLast() ?: break
            if (frame.index >= frame.function.statements.size) {
                callStack.frames.removeLast()
                continue
            }
            /**
             * `statement` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val statement = frame.function.statements[frame.index++]
            executeStatement(statement, frame)
            if (getState() != PlaybackState.COMPLETE) return
        }
    }

    /**
     * `executeStatement`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
                /**
                 * `valueNode` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val valueNode = statement.field("value")
                if (ScenarioChoiceCoordinator.isStageChoice(valueNode)) {
                    /**
                     * `args` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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
                    /**
                     * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val value = eval(valueNode, frame)
                    statement.field("targets").children().forEach { assign(it, value, frame) }
                }
            }

            "AugAssign" -> {
                /**
                 * `target` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val target = statement.field("target")
                /**
                 * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val current = eval(target, frame).asInt()
                /**
                 * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val value = eval(statement.field("value"), frame).asInt()
                /**
                 * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
                /**
                 * `ask` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val ask = ScenarioChoiceCoordinator.findStageAsk(statement.field("test"))
                if (ask != null && choiceCoordinator.pendingAskResult == null) {
                    choiceCoordinator.startAsk(ask, statement, frame, moduleName)
                    return
                }
                /**
                 * `selected` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val selected = if (evalBoolean(
                        statement.field("test"),
                        frame
                    )
                ) statement.field("body") else statement.field("orelse")
                /**
                 * `statements` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val statements = selected.children().toList()
                if (statements.isNotEmpty()) callStack.frames.addLast(
                    Frame(RuntimeFunction("<if>", statements, emptyMap()), 0, frame.locals, frame.sourceFunction),
                )
            }

            "For" -> executeFor(statement, frame, eval, assign, callStack)
            "Return" -> callStack.frames.removeLast()
        }
    }

    /**
     * `executeFor`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun executeFor(
        statement: JsonValue,
        frame: Frame,
        eval: (JsonValue, Frame) -> Any?,
        assign: (JsonValue, Any?, Frame) -> Unit,
        callStack: ScenarioCallStack,
    ) {
        /**
         * `iterable` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val iterable = eval(statement.field("iter"), frame) as? List<*> ?: emptyList<Any?>()
        /**
         * `body` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val body = statement.field("body").children().toList()
        iterable.asReversed().forEach { item ->
            /**
             * `scoped` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val scoped = frame.locals.toMutableMap()
            assign(statement.field("target"), item, Frame(frame.function, frame.index, scoped))
            callStack.frames.addLast(Frame(RuntimeFunction("<for>", body, emptyMap()), 0, scoped, frame.sourceFunction))
        }
    }
}

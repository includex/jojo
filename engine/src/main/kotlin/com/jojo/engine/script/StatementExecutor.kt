package com.jojo.engine.script

import com.badlogic.gdx.utils.JsonValue

/** Executes the imported AST dialect; the host decides when execution yields. */
class StatementExecutor(
    private val moduleName: String,
    private val maxStatementsPerStart: Int = 100_000,
) {
    /**
     * Runs until the host yields, ends, or exhausts all frames. The step budget
     * includes completed-frame removal, preserving the original dispatch loop.
     */
    fun runUntilYield(
        callStack: ScriptCallStack,
        isEnded: () -> Boolean,
        shouldYield: () -> Boolean,
        executeStatement: (JsonValue, ScriptFrame) -> Unit,
    ) {
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
            if (shouldYield()) return
        }
    }

    /** A consumed host statement is not evaluated or assigned a second time. */
    fun executeStatement(
        statement: JsonValue,
        frame: ScriptFrame,
        eval: (JsonValue, ScriptFrame) -> Any?,
        evalBoolean: (JsonValue, ScriptFrame) -> Boolean,
        assign: (JsonValue, Any?, ScriptFrame) -> Unit,
        callStack: ScriptCallStack,
        interceptStatement: (JsonValue, ScriptFrame) -> Boolean = { _, _ -> false },
    ) {
        if (interceptStatement(statement, frame)) return
        when (statement.typeName()) {
            "Expr" -> eval(statement.field("value"), frame)
            "Assign" -> {
                val value = eval(statement.field("value"), frame)
                statement.field("targets").children().forEach { assign(it, value, frame) }
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
                val selected = if (evalBoolean(statement.field("test"), frame)) {
                    statement.field("body")
                } else {
                    statement.field("orelse")
                }
                val statements = selected.children().toList()
                if (statements.isNotEmpty()) callStack.frames.addLast(
                    ScriptFrame(ScriptFunction("<if>", statements, emptyMap()), 0, frame.locals, frame.sourceFunction),
                )
            }
            "For" -> executeFor(statement, frame, eval, assign, callStack)
            "Return" -> callStack.frames.removeLast()
        }
    }

    private fun executeFor(
        statement: JsonValue,
        frame: ScriptFrame,
        eval: (JsonValue, ScriptFrame) -> Any?,
        assign: (JsonValue, Any?, ScriptFrame) -> Unit,
        callStack: ScriptCallStack,
    ) {
        val iterable = eval(statement.field("iter"), frame) as? List<*> ?: emptyList<Any?>()
        val body = statement.field("body").children().toList()
        iterable.asReversed().forEach { item ->
            val scoped = frame.locals.toMutableMap()
            assign(statement.field("target"), item, ScriptFrame(frame.function, frame.index, scoped))
            callStack.frames.addLast(ScriptFrame(ScriptFunction("<for>", body, emptyMap()), 0, scoped, frame.sourceFunction))
        }
    }
}

package com.jojo.engine.script

import com.badlogic.gdx.utils.JsonValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ScriptControlFlowTest {
    private fun node(type: String, vararg fields: Pair<String, JsonValue>): JsonValue =
        JsonValue(JsonValue.ValueType.`object`).also { root ->
            root.addChild("type", JsonValue(type))
            root.addChild("fields", JsonValue(JsonValue.ValueType.`object`).also { values ->
                fields.forEach { (name, value) -> values.addChild(name, value) }
            })
        }
    private fun integer(value: Int) = node("Constant", "value" to JsonValue(value.toLong()))
    private fun name(value: String) = node("Name", "id" to JsonValue(value))
    private fun array(vararg values: JsonValue) = JsonValue(JsonValue.ValueType.array).also { result ->
        values.forEach(result::addChild)
    }
    private fun assignment(variable: String, value: Int) = node("Assign", "targets" to array(name(variable)), "value" to integer(value))
    private fun increment(variable: String, value: Int) = node("AugAssign", "target" to name(variable), "value" to integer(value), "op" to node("Add"))
    private fun hostCall() = node("Expr", "value" to node("Call"))

    private fun execute(
        executor: StatementExecutor,
        stack: ScriptCallStack,
        env: ExpressionEnvironment,
        shouldYield: () -> Boolean = { false },
        intercept: (JsonValue, ScriptFrame) -> Boolean = { _, _ -> false },
    ) = executor.runUntilYield(stack, { false }, shouldYield) { statement, frame ->
        executor.executeStatement(
            statement, frame,
            eval = { expression, active -> ExpressionEvaluator.eval(expression, active, env) },
            evalBoolean = { expression, active -> ExpressionEvaluator.evalBoolean(expression, active, env) },
            assign = { target, value, active -> ExpressionEvaluator.assign(target, value, active, env) },
            callStack = stack,
            interceptStatement = intercept,
        )
    }

    @Test
    fun `host interception yields before assignment and resumes at the following statement`() {
        val first = assignment("selection", 9)
        val function = ScriptFunction("entry", listOf(first, assignment("done", 1)), emptyMap())
        val frame = ScriptFrame(function)
        val stack = ScriptCallStack().also { it.frames.add(frame) }
        val executor = StatementExecutor("test")
        val env = ExpressionEnvironment(globalVariables = emptyMap(), invokeCall = { _, _ -> null })
        var yielded = false
        execute(executor, stack, env, { yielded }) { statement, active ->
            if (statement === first) {
                assertSame(frame, active)
                yielded = true
                true
            } else false
        }
        assertEquals(1, frame.index)
        assertFalse("selection" in frame.locals)
        assertFalse("done" in frame.locals)
        frame.locals["selection"] = 2
        yielded = false
        execute(executor, stack, env, { yielded })
        assertEquals(mapOf<String, Any?>("selection" to 2, "done" to 1), frame.locals)
        assertTrue(stack.frames.isEmpty())
    }

    @Test
    fun `if shares locals while for iterations execute FIFO with separate captured scopes`() {
        val branch = node("If", "test" to integer(1), "body" to array(increment("seed", 4)), "orelse" to array())
        val loop = node("For", "target" to name("item"), "iter" to node("List", "elts" to array(integer(2), integer(3))),
            "body" to array(increment("seed", 1), hostCall()))
        val frame = ScriptFrame(ScriptFunction("entry", listOf(assignment("seed", 1), branch, loop, hostCall()), emptyMap()))
        val stack = ScriptCallStack().also { it.frames.add(frame) }
        val observed = mutableListOf<Pair<Any?, Any?>>()
        val env = ExpressionEnvironment(globalVariables = emptyMap(), invokeCall = { _, active ->
            observed += active.locals["item"] to active.locals["seed"]
            0
        })
        execute(StatementExecutor("test"), stack, env)
        assertEquals(listOf<Pair<Any?, Any?>>(2 to 6, 3 to 6, null to 5), observed)
        assertEquals(mapOf<String, Any?>("seed" to 5), frame.locals)
    }

    @Test
    fun `return preserves imported dialect behavior by removing the active block frame only`() {
        val branch = node("If", "test" to integer(1), "body" to array(node("Return"), hostCall()), "orelse" to array())
        val stack = ScriptCallStack().also { it.frames.add(ScriptFrame(ScriptFunction("entry", listOf(branch, hostCall()), emptyMap()))) }
        var calls = 0
        val env = ExpressionEnvironment(globalVariables = emptyMap(), invokeCall = { _, _ -> calls++; 0 })
        execute(StatementExecutor("test"), stack, env)
        assertEquals(1, calls)
        assertTrue(stack.frames.isEmpty())
    }

    @Test
    fun `execution budget stops an infinite instruction loop and resets on the next run`() {
        val frame = ScriptFrame(ScriptFunction("entry", listOf(hostCall()), emptyMap()))
        val stack = ScriptCallStack().also { it.frames.add(frame) }
        val executor = StatementExecutor("test", maxStatementsPerStart = 3)
        var calls = 0
        repeat(2) {
            assertFailsWith<IllegalStateException> {
                executor.runUntilYield(stack, { false }, { false }) { _, active ->
                    calls++
                    active.index = 0
                }
            }
        }
        assertEquals(6, calls)
    }

    @Test
    fun `goto prunes nested source frames while preserving an outer caller`() {
        val stack = ScriptCallStack()
        val caller = ScriptFrame(ScriptFunction("caller", emptyList(), emptyMap()))
        val entry = ScriptFunction("entry", listOf(integer(1), integer(2)), mapOf("loop" to 0))
        stack.frames.add(caller)
        stack.frames.add(ScriptFrame(entry))
        stack.frames.add(ScriptFrame(ScriptFunction("<if>", emptyList(), emptyMap()), sourceFunction = "entry"))
        stack.jumpToLabel("loop", mapOf("entry" to entry))
        assertEquals(2, stack.frames.size)
        assertSame(caller, stack.frames.first)
        assertSame(entry, stack.frames.last.function)
        assertEquals(1, stack.frames.last.index)
    }

    @Test
    fun `label entrypoints take precedence and missing named labels fail explicitly`() {
        val body = listOf(integer(1))
        val alternate = listOf(integer(2))
        val function = ScriptFunction("entry", body, emptyMap(), mapOf("alternate" to alternate))
        val stack = ScriptCallStack()
        stack.pushFunction("entry", "alternate", mapOf("entry" to function), "module")
        assertSame(alternate, stack.frames.last.function.statements)
        assertFailsWith<IllegalStateException> { stack.pushFunction("entry", "missing", mapOf("entry" to function), "module") }
    }
}

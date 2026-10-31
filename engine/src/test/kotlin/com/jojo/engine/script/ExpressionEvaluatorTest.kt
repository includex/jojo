package com.jojo.engine.script

import com.badlogic.gdx.utils.JsonValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ExpressionEvaluatorTest {
    private fun node(type: String, vararg fields: Pair<String, JsonValue>): JsonValue =
        JsonValue(JsonValue.ValueType.`object`).also { root ->
            root.addChild("type", JsonValue(type))
            root.addChild("fields", JsonValue(JsonValue.ValueType.`object`).also { values ->
                fields.forEach { (name, value) -> values.addChild(name, value) }
            })
        }

    private fun integer(value: Int) = node("Constant", "value" to JsonValue(value.toLong()))
    private fun name(value: String) = node("Name", "id" to JsonValue(value))
    private fun array(vararg nodes: JsonValue) = JsonValue(JsonValue.ValueType.array).also { result ->
        nodes.forEach { result.addChild(it) }
    }
    private fun frame() = ScriptFrame(ScriptFunction("entry", emptyList(), emptyMap()))

    @Test
    fun `host bindings retain precedence and reference identity while ordinary names use local then global scopes`() {
        val bank = mutableMapOf<Int, Any?>(7 to 12)
        val frame = frame().also { it.locals["bank"] = "shadow"; it.locals["score"] = 3 }
        val env = ExpressionEnvironment(
            bindings = mapOf("bank" to bank),
            globalVariables = mapOf("score" to 8, "world" to 42),
            invokeCall = { _, _ -> error("No host call expected") },
        )
        assertSame(bank, ExpressionEvaluator.eval(name("bank"), frame, env))
        assertEquals(3, ExpressionEvaluator.eval(name("score"), frame, env))
        assertEquals(42, ExpressionEvaluator.eval(name("world"), frame, env))
        assertEquals(0, ExpressionEvaluator.eval(name("missing"), frame, env))

        val target = node("Subscript", "value" to name("bank"), "slice" to integer(7))
        ExpressionEvaluator.assign(target, 24, frame, env)
        assertEquals(24, bank[7])
        bank[7] = 31
        assertEquals(31, ExpressionEvaluator.eval(target, frame, env))
    }

    @Test
    fun `short circuit preserves host side effects and calls receive the active frame`() {
        val frame = frame()
        var calls = 0
        val env = ExpressionEnvironment(globalVariables = emptyMap(), invokeCall = { _, active ->
            assertSame(frame, active)
            calls++
            9
        })
        val and = node("BoolOp", "op" to node("And"), "values" to array(integer(0), node("Call")))
        val or = node("BoolOp", "op" to node("Or"), "values" to array(integer(1), node("Call")))
        assertEquals(false, ExpressionEvaluator.eval(and, frame, env))
        assertEquals(true, ExpressionEvaluator.eval(or, frame, env))
        assertEquals(0, calls)
        assertEquals(9, ExpressionEvaluator.eval(node("Call"), frame, env))
        assertEquals(1, calls)
    }

    @Test
    fun `subscript misses and null locals preserve legacy fallback semantics`() {
        val frame = frame().also { it.locals["score"] = null }
        val env = ExpressionEnvironment(
            bindings = mapOf("rows" to listOf(4, 5)),
            globalVariables = mapOf("score" to 7),
            invokeCall = { _, _ -> null },
        )
        assertEquals(7, ExpressionEvaluator.eval(name("score"), frame, env))
        assertEquals(0, ExpressionEvaluator.eval(node("Subscript", "value" to name("rows"), "slice" to integer(8)), frame, env))
        assertEquals(5, ExpressionEvaluator.eval(node("Subscript", "value" to name("rows"), "slice" to integer(1)), frame, env))
    }
}

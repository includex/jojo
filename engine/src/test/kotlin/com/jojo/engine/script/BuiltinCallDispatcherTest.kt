package com.jojo.engine.script

import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import kotlin.test.Test
import kotlin.test.assertEquals

class BuiltinCallDispatcherTest {
    private val frame = ScriptFrame(ScriptFunction("entry", emptyList(), emptyMap()))
    private val call: JsonValue get() = JsonReader().parse("""{"type":"Call","fields":{"func":{"type":"Name","fields":{"id":"host"}}}}""")

    @Test
    fun `generic call and goto builtins delegate control flow while unknown calls retain counters`() {
        val events = mutableListOf<String>()
        val unhandled = mutableMapOf<String, Int>()
        val env = BuiltinCallEnvironment(
            functions = mapOf("entry" to frame.function),
            unhandledCalls = unhandled,
            jumpToLabel = { events += "jump:$it" },
            pushFunction = { events += "call:$it" },
            eval = { _, _ -> null },
        )
        BuiltinCallDispatcher.dispatch("goto", call, listOf("resume"), frame, env)
        BuiltinCallDispatcher.dispatch("entry", call, emptyList(), frame, env)
        BuiltinCallDispatcher.dispatch("call", call, listOf("other"), frame, env)
        repeat(2) { BuiltinCallDispatcher.dispatch("unknown", call, emptyList(), frame, env) }
        assertEquals(listOf("jump:resume", "call:entry", "call:other"), events)
        assertEquals(mapOf("unknown" to 2), unhandled)
        assertEquals(true, BuiltinCallDispatcher.dispatch("hasFunc", call, listOf("entry"), frame, env).value)
    }

    @Test
    fun `append mutates the evaluated receiver and range preserves its exclusive endpoint`() {
        val values = mutableListOf<Any?>(1)
        val append = JsonReader().parse("""{"type":"Call","fields":{"func":{"type":"Attribute","fields":{"value":{"type":"Name","fields":{"id":"items"}},"attr":"append"}}}}""")
        val env = BuiltinCallEnvironment(
            functions = emptyMap(), unhandledCalls = mutableMapOf(),
            jumpToLabel = {}, pushFunction = {}, eval = { _, active ->
                assertEquals(frame, active)
                values
            },
        )
        BuiltinCallDispatcher.dispatch("items.append", append, listOf(4), frame, env)
        assertEquals(listOf<Any?>(1, 4), values)
        assertEquals(listOf(2, 3), BuiltinCallDispatcher.dispatch("range", call, listOf(2, 4), frame, env).value)
    }
}

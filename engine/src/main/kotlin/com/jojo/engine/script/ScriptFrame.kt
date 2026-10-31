package com.jojo.engine.script

import com.badlogic.gdx.utils.JsonValue

/** Parsed function body and its label entrypoints, independent of game host calls. */
data class ScriptFunction(
    val name: String,
    val statements: List<JsonValue>,
    val labels: Map<String, Int>,
    val labelEntrypoints: Map<String, List<JsonValue>> = emptyMap(),
)

/** Instruction cursor and local scope of one invocation or nested statement block. */
data class ScriptFrame(
    val function: ScriptFunction,
    var index: Int = 0,
    val locals: MutableMap<String, Any?> = mutableMapOf(),
    val sourceFunction: String = function.name,
)

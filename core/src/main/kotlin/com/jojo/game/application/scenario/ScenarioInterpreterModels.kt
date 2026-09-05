package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

internal data class RuntimeFunction(
    val name: String,
    val statements: List<JsonValue>,
    val labels: Map<String, Int>,
    /** Nested label continuations used by recovered menu state machines. */
    val labelEntrypoints: Map<String, List<JsonValue>> = emptyMap(),
)

internal data class Frame(
    val function: RuntimeFunction,
    var index: Int = 0,
    val locals: MutableMap<String, Any?> = mutableMapOf(),
    /** Original source function survives synthetic if/for execution frames. */
    val sourceFunction: String = function.name,
)

internal data class HeadReference(val id: Int)

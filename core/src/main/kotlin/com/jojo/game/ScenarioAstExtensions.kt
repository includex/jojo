package com.jojo.game

import com.badlogic.gdx.utils.JsonValue

internal fun JsonValue.typeName(): String = getString("type")

internal fun JsonValue.field(name: String): JsonValue = get("fields").get(name)

internal fun JsonValue.children(): Sequence<JsonValue> = sequence {
    var item = child
    while (item != null) {
        yield(item)
        item = item.next
    }
}

internal fun JsonValue.expressionPath(): String? = when (typeName()) {
    "Name" -> field("id").asString()
    "Attribute" -> field("value").expressionPath()?.plus(".")?.plus(field("attr").asString())
    "Call" -> field("func").expressionPath()?.plus("()")
    else -> null
}

internal fun JsonValue.value(): Any? = when (type()) {
    JsonValue.ValueType.nullValue -> null
    JsonValue.ValueType.booleanValue -> asBoolean()
    JsonValue.ValueType.longValue -> asLong().toInt()
    JsonValue.ValueType.doubleValue -> asDouble()
    else -> asString()
}

internal fun Any?.asText(): String = when (this) {
    null -> ""
    else -> toString()
}

internal fun Any?.asInt(): Int = when (this) {
    is Number -> toInt()
    is Boolean -> if (this) 1 else 0
    is String -> toIntOrNull() ?: 0
    else -> 0
}

internal fun Any?.asBooleanValue(): Boolean = when (this) {
    is Boolean -> this
    is Number -> toInt() != 0
    is String -> this.equals("true", ignoreCase = true) || this.toIntOrNull()?.let { it != 0 } == true
    else -> false
}

internal fun Any?.asList(): List<Any?> = this as? List<Any?> ?: emptyList()

internal fun List<Any?>.intAt(index: Int): Int = getOrNull(index).asInt()

package com.jojo.engine.script

import com.badlogic.gdx.utils.JsonValue

/** Accessors for the portable AST envelope emitted by the script importer. */
fun JsonValue.typeName(): String = getString("type")
fun JsonValue.field(name: String): JsonValue = get("fields").get(name)

fun JsonValue.children(): Sequence<JsonValue> = sequence {
    var item = child
    while (item != null) {
        yield(item)
        item = item.next
    }
}

/** Resolve an AST reference without invoking the host object or function. */
fun JsonValue.expressionPath(): String? = when (typeName()) {
    "Name" -> field("id").asString()
    "Attribute" -> field("value").expressionPath()?.plus(".")?.plus(field("attr").asString())
    "Call" -> field("func").expressionPath()?.plus("()")
    else -> null
}

fun JsonValue.value(): Any? = when (type()) {
    JsonValue.ValueType.nullValue -> null
    JsonValue.ValueType.booleanValue -> asBoolean()
    JsonValue.ValueType.longValue -> asLong().toInt()
    JsonValue.ValueType.doubleValue -> asDouble()
    else -> asString()
}

/** These coercions preserve the imported script dialect's existing value rules. */
fun Any?.asText(): String = when (this) {
    null -> ""
    else -> toString()
}

fun Any?.asInt(): Int = when (this) {
    is Number -> toInt()
    is Boolean -> if (this) 1 else 0
    is String -> toIntOrNull() ?: 0
    else -> 0
}

fun Any?.asBooleanValue(): Boolean = when (this) {
    is Boolean -> this
    is Number -> toInt() != 0
    is String -> this.equals("true", ignoreCase = true) || this.toIntOrNull()?.let { it != 0 } == true
    else -> false
}

fun Any?.asList(): List<Any?> = this as? List<Any?> ?: emptyList()
fun List<Any?>.intAt(index: Int): Int = getOrNull(index).asInt()

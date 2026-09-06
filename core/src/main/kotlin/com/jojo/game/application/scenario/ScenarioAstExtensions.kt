// Scenario
package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue

/**
 * `JsonValue`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun JsonValue.typeName(): String = getString("type")

/**
 * `JsonValue`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun JsonValue.field(name: String): JsonValue = get("fields").get(name)

/**
 * `JsonValue`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun JsonValue.children(): Sequence<JsonValue> = sequence {
    var item = child
    while (item != null) {
        yield(item)
        item = item.next
    }
}

/**
 * `JsonValue`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun JsonValue.expressionPath(): String? = when (typeName()) {
    "Name" -> field("id").asString()
    "Attribute" -> field("value").expressionPath()?.plus(".")?.plus(field("attr").asString())
    "Call" -> field("func").expressionPath()?.plus("()")
    else -> null
}

/**
 * `JsonValue`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun JsonValue.value(): Any? = when (type()) {
    JsonValue.ValueType.nullValue -> null
    JsonValue.ValueType.booleanValue -> asBoolean()
    JsonValue.ValueType.longValue -> asLong().toInt()
    JsonValue.ValueType.doubleValue -> asDouble()
    else -> asString()
}

/**
 * `Any`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun Any?.asText(): String = when (this) {
    null -> ""
    else -> toString()
}

/**
 * `Any`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun Any?.asInt(): Int = when (this) {
    is Number -> toInt()
    is Boolean -> if (this) 1 else 0
    is String -> toIntOrNull() ?: 0
    else -> 0
}

/**
 * `Any`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun Any?.asBooleanValue(): Boolean = when (this) {
    is Boolean -> this
    is Number -> toInt() != 0
    is String -> this.equals("true", ignoreCase = true) || this.toIntOrNull()?.let { it != 0 } == true
    else -> false
}

/**
 * `Any`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun Any?.asList(): List<Any?> = this as? List<Any?> ?: emptyList()

/**
 * `List`: 조건과 입력 상태를 검증한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun List<Any?>.intAt(index: Int): Int = getOrNull(index).asInt()

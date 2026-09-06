// Game
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.utils.JsonValue

/** 복호화된 원본 테이블을 읽기 전용으로 제공한다. */
internal abstract class GameDataCatalogTableDomain(tables: GameDataTableBundle) {
    protected val units = tables.units
    protected val arms = tables.arms
    protected val posts = tables.posts
    protected val hitAreas = tables.hitAreas
    protected val effectAreas = tables.effectAreas
    protected val magics = tables.magics
    protected val items = tables.items
    protected val itemSkills = tables.itemSkills
    protected val unitPostSkills = tables.unitPostSkills
    protected val defineSkills = tables.defineSkills
    protected val shops = tables.shops
    protected val config = tables.config
    protected val gameConfig = tables.gameConfig

    protected fun JsonValue.string(key: String): String? = get(key)?.asString()
    protected fun JsonValue.int(key: String, fallback: Int = 0): Int = get(key)?.asInt() ?: fallback
    protected fun stringValues(value: JsonValue): List<String> = when {
        value.isArray -> generateSequence(value.child) { it.next }.map { it.asString() }.toList()
        value.isString -> listOf(value.asString())
        else -> emptyList()
    }

    protected fun intValues(value: JsonValue): List<Int> = when {
        value.isArray -> generateSequence(value.child) { it.next }.map { it.asInt() }.toList()
        else -> emptyList()
    }

    protected fun numericChildren(value: JsonValue?, prefix: String = ""): Map<Int, Int> =
        generateSequence(value?.child) { it.next }
            .mapNotNull { child -> child.name.removePrefix(prefix).toIntOrNull()?.let { it to child.asInt() } }
            .toMap()

    protected fun indexed(value: JsonValue?, index: Int): JsonValue? =
        generateSequence(value?.child) { it.next }.elementAtOrNull(index)
}

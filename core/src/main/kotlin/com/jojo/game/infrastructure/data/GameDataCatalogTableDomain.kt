// Game
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.utils.JsonValue

/** 복호화된 원본 테이블을 읽기 전용으로 제공한다. */
internal abstract class GameDataCatalogTableDomain(tables: GameDataTableBundle) {
    /**
     * `units` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val units = tables.units
    /**
     * `arms` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val arms = tables.arms
    /**
     * `posts` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val posts = tables.posts
    /**
     * `hitAreas` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val hitAreas = tables.hitAreas
    /**
     * `effectAreas` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val effectAreas = tables.effectAreas
    /**
     * `magics` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val magics = tables.magics
    /**
     * `items` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val items = tables.items
    /**
     * `itemSkills` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val itemSkills = tables.itemSkills
    /**
     * `unitPostSkills` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val unitPostSkills = tables.unitPostSkills
    /**
     * `defineSkills` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val defineSkills = tables.defineSkills
    /**
     * `shops` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val shops = tables.shops
    /**
     * `config` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val config = tables.config
    /**
     * `gameConfig` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    protected val gameConfig = tables.gameConfig

    /**
     * `JsonValue`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    protected fun JsonValue.string(key: String): String? = get(key)?.asString()
    /**
     * `JsonValue`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    protected fun JsonValue.int(key: String, fallback: Int = 0): Int = get(key)?.asInt() ?: fallback
    /**
     * `stringValues`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected fun stringValues(value: JsonValue): List<String> = when {
        value.isArray -> generateSequence(value.child) { it.next }.map { it.asString() }.toList()
        value.isString -> listOf(value.asString())
        else -> emptyList()
    }

    /**
     * `intValues`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected fun intValues(value: JsonValue): List<Int> = when {
        value.isArray -> generateSequence(value.child) { it.next }.map { it.asInt() }.toList()
        else -> emptyList()
    }

    /**
     * `numericChildren`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected fun numericChildren(value: JsonValue?, prefix: String = ""): Map<Int, Int> =
        generateSequence(value?.child) { it.next }
            .mapNotNull { child -> child.name.removePrefix(prefix).toIntOrNull()?.let { it to child.asInt() } }
            .toMap()

    /**
     * `indexed`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    protected fun indexed(value: JsonValue?, index: Int): JsonValue? =
        generateSequence(value?.child) { it.next }.elementAtOrNull(index)
}

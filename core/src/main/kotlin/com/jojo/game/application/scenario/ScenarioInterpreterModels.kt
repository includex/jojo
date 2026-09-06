// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

internal data class RuntimeFunction(
    val name: String,
    val statements: List<JsonValue>,
    val labels: Map<String, Int>,
    /** labelEntrypoints: 레이블 진입 시 실행할 문장 묶음을 이름별로 빠르게 찾는 색인이다. */
    val labelEntrypoints: Map<String, List<JsonValue>> = emptyMap(),
)

internal data class Frame(
    val function: RuntimeFunction,
    var index: Int = 0,
    val locals: MutableMap<String, Any?> = mutableMapOf(),
    /** sourceFunction: 호출 스택 추적에 남길 원본 함수 이름이다. */
    val sourceFunction: String = function.name,
)

internal data class HeadReference(val id: Int)

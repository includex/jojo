// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

/**
 * `RuntimeFunction` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class RuntimeFunction(
    val name: String,
    val statements: List<JsonValue>,
    val labels: Map<String, Int>,
    /** labelEntrypoints: 레이블 진입 시 실행할 문장 묶음을 이름별로 빠르게 찾는 색인이다. */
    val labelEntrypoints: Map<String, List<JsonValue>> = emptyMap(),
)

/**
 * `Frame` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class Frame(
    val function: RuntimeFunction,
    var index: Int = 0,
    val locals: MutableMap<String, Any?> = mutableMapOf(),
    /** sourceFunction: 호출 스택 추적에 남길 원본 함수 이름이다. */
    val sourceFunction: String = function.name,
)

/**
 * `HeadReference` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class HeadReference(val id: Int)

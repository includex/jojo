// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.domain.scenario.Choice
import com.jojo.game.domain.scenario.Dialogue
import com.jojo.game.domain.scenario.PlaybackState

/** ScenarioModalKind: 스크립트가 중단해 표시하는 모달의 용도와 재개 방식을 구분한다. */
enum class ScenarioModalKind { EVENT, INFO, SECTION, MAP_INFO, AMBITION }
/**
 * `ScenarioChoiceTrace` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioChoiceTrace(val module: String, val function: String, val line: Int, val option: Int, val optionCount: Int)
/**
 * `ScenarioRandomTrace` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioRandomTrace(val module: String, val function: String, val line: Int, val value: Int)
/**
 * `ScenarioBattleScriptContext` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioBattleScriptContext(
    val round: Int, val camp: Int, val maxRound: Int = 99, val playerDefeated: Boolean = false,
    val enemyDefeated: Boolean = false, val clickedCharacterId: Int? = null,
    val positions: Map<Int, Pair<Int, Int>> = emptyMap(), val stagePositions: Map<Int, Pair<Int, Int>> = positions,
    val positionsByCamp: Map<Int, List<Pair<Int, Int>>> = emptyMap(), val campByCharacterId: Map<Int, Int> = emptyMap(),
    val attackOffsets: Map<Int, Set<Pair<Int, Int>>> = emptyMap(),
    val infantryNearOffsets: Set<Pair<Int, Int>> = ScenarioConditionEvaluator.DEFAULT_INFANTRY_NEAR_OFFSETS,
    val activeCharacterIds: Set<Int> = emptySet(), val attributes: Map<Int, Map<Int, Int>> = emptyMap(), val enabledFeatures: Int = 0,
)
/**
 * `ScenarioUnitReference` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioUnitReference(val id: Int)
/**
 * `ScenarioFightReference` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioFightReference(val id: Long)
/**
 * `ScenarioPlaybackSnapshot` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class ScenarioPlaybackSnapshot(
    val state: PlaybackState, val dialogue: Dialogue?, val choice: Choice?, val modalText: String?,
    val modalKind: ScenarioModalKind?, val selectedChoice: Int, val isAskChoice: Boolean,
)

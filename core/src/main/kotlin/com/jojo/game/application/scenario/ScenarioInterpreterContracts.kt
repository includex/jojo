package com.jojo.game.application.scenario

import com.jojo.game.domain.scenario.Choice
import com.jojo.game.domain.scenario.Dialogue
import com.jojo.game.domain.scenario.PlaybackState

/** Stable contracts shared by scenario orchestration, presentation, and verification. */
enum class ScenarioModalKind { EVENT, INFO, SECTION, MAP_INFO, AMBITION }
data class ScenarioChoiceTrace(val module: String, val function: String, val line: Int, val option: Int, val optionCount: Int)
data class ScenarioRandomTrace(val module: String, val function: String, val line: Int, val value: Int)
data class ScenarioBattleScriptContext(
    val round: Int, val camp: Int, val maxRound: Int = 99, val playerDefeated: Boolean = false,
    val enemyDefeated: Boolean = false, val clickedCharacterId: Int? = null,
    val positions: Map<Int, Pair<Int, Int>> = emptyMap(), val stagePositions: Map<Int, Pair<Int, Int>> = positions,
    val positionsByCamp: Map<Int, List<Pair<Int, Int>>> = emptyMap(), val campByCharacterId: Map<Int, Int> = emptyMap(),
    val attackOffsets: Map<Int, Set<Pair<Int, Int>>> = emptyMap(),
    val infantryNearOffsets: Set<Pair<Int, Int>> = ScenarioConditionEvaluator.DEFAULT_INFANTRY_NEAR_OFFSETS,
    val activeCharacterIds: Set<Int> = emptySet(), val attributes: Map<Int, Map<Int, Int>> = emptyMap(), val enabledFeatures: Int = 0,
)
data class ScenarioUnitReference(val id: Int)
data class ScenarioFightReference(val id: Long)
data class ScenarioPlaybackSnapshot(
    val state: PlaybackState, val dialogue: Dialogue?, val choice: Choice?, val modalText: String?,
    val modalKind: ScenarioModalKind?, val selectedChoice: Int, val isAskChoice: Boolean,
)

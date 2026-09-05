package com.jojo.game.domain.battle

import com.jojo.game.*
import com.jojo.game.domain.campaign.*

/**
 * data class  `TurnTrigger`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class TurnTrigger(val round: Int, val faction: Faction)

/**
 * class  `BattleEvent`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

/**
 * data class  `TurnResult`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class TurnResult(val round: Int, val activeFaction: Faction, val firedEvents: List<String>)

/**
 * data class  `RoundAdvance`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class RoundAdvance(val completedRound: Int, val round: Int)

/**
 * data class  `WeatherTransition`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class WeatherTransition(val previous: BattleWeather, val current: BattleWeather) {
    val changed: Boolean get() = previous != current
}

/**
 * data class  `AiTurnResult`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class AiTurnResult(val moves: Int, val attacks: Int, val holds: Int)

/** One source `_ai2` actor pass, retained so the renderer can await it. */
data class AiUnitResolution(
    val actorId: String,
    val fromX: Int,
    val fromY: Int,
    val toX: Int,
    val toY: Int,
    val path: List<Pair<Int, Int>>,
    val targetId: String? = null,
    val magicId: Int? = null,
    val result: TacticalActionResult? = null,
    val healthBeforeAction: Map<String, Int> = emptyMap(),
    val moveArea: List<Pair<Int, Int>> = emptyList(),
    val actionArea: List<Pair<Int, Int>> = emptyList(),
)

/**
 * Read-only observation of the same candidate planner that [runEnemyTurn]
 * delegates to through ControlManager.  This deliberately exposes the raw
 * score: it is an evidence record, not a compatibility-normalized result.
 */
/**
 * data class  `AiPlannerTrace`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class AiPlannerTrace(
    val characterId: Int,
    val ai: Int,
    val x: Int,
    val y: Int,
    val value: Int?,
    val actionValue: Int?,
    val targetId: String?,
    val magicId: Int?,
)

/**
 * enum class  `BattleOutcome`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class BattleOutcome { PLAYER_VICTORY, ENEMY_VICTORY }

// Battle
package com.jojo.game.domain.battle

import com.jojo.game.*
import com.jojo.game.domain.campaign.*


/** TurnTrigger: 특정 라운드와 진영에서 실행할 전투 이벤트의 조건을 나타낸다. */
data class TurnTrigger(val round: Int, val faction: Faction)



/** TurnResult: 진영 턴 처리 결과로, 실행한 라운드·진영·이벤트 식별자를 전달한다. */
data class TurnResult(val round: Int, val activeFaction: Faction, val firedEvents: List<String>)


/** RoundAdvance: 라운드 경계 통과 결과로, 끝난 라운드와 새 라운드 번호를 함께 보관한다. */
data class RoundAdvance(val completedRound: Int, val round: Int)


/** WeatherTransition: 라운드 전환 중 날씨 변화 전후 값을 보관하고, 실제 변경 여부를 제공한다. */
data class WeatherTransition(val previous: BattleWeather, val current: BattleWeather) {
    /**
     * `changed` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val changed: Boolean get() = previous != current
}


/** AiTurnResult: AI 진영이 수행한 이동·공격·대기 횟수를 요약해 표현 단계에 전달한다. */
data class AiTurnResult(val moves: Int, val attacks: Int, val holds: Int)

/** AiUnitResolution: AI 유닛 한 명의 이동·대상 선택·전술 행동 결과를 보관한다. */
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

/** AiPlannerTrace: AI 계획기의 입력 좌표·평가값·선택 대상을 기록해 재현과 검증에 사용한다. */

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


/** BattleOutcome: 전투 종료 시 승리한 진영을 나타내며, 보상과 장면 전환을 결정한다. */
enum class BattleOutcome { PLAYER_VICTORY, ENEMY_VICTORY }

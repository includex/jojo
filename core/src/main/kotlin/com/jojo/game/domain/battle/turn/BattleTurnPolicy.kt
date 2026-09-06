// Battle
package com.jojo.game.domain.battle.turn

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.PhysicalAttackPassKind
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.domain.battle.isPlayerSide
import com.jojo.game.domain.scenario.PlaybackState

/**
 * `BattleTurnEntryRequest` 클래스: turn 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class BattleTurnEntryRequest(
    val phase: BattleTurnPhase,
    val activeFaction: Faction,
    val outcome: BattleOutcome?,
)

/**
 * `BattleCampTransitionRequest` 클래스: turn 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class BattleCampTransitionRequest(val previous: Faction, val current: Faction)

/** BattleTurnPolicy: 전투 턴 정책이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
object BattleTurnPolicy {
    /**
     * `acceptsPlayerEnd`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun acceptsPlayerEnd(request: BattleTurnEntryRequest): Boolean =
        request.phase == BattleTurnPhase.PLAYER_INPUT &&
            request.activeFaction == Faction.PLAYER && request.outcome == null

    /**
     * `campCardFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun campCardFor(request: BattleCampTransitionRequest): Boolean =
        request.previous.isPlayerSide() != request.current.isPlayerSide()
}

/**
 * `CollocatedPlayerMoveScriptEnd` 싱글턴 객체: turn 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object CollocatedPlayerMoveScriptEnd {
    /**
     * `finishesAiTurn`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun finishesAiTurn(
        camp: Faction,
        moveCallbackStarted: Boolean,
        scriptState: PlaybackState,
        battleEndedByScript: Boolean,
        scriptedOutcome: BattleOutcome?,
        observedOutcome: BattleOutcome?,
    ): Boolean = camp == Faction.PLAYER && moveCallbackStarted &&
        scriptState == PlaybackState.COMPLETE && battleEndedByScript &&
        scriptedOutcome != null && observedOutcome != null
}

/**
 * `TacticalActionResult`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun TacticalActionResult?.hasPhysicalCounterPass(): Boolean =
    (this as? TacticalActionResult.Attack)?.physicalPasses?.any {
        it.kind == PhysicalAttackPassKind.COUNTER || it.kind == PhysicalAttackPassKind.COUNTER_FOLLOW_UP
    } == true

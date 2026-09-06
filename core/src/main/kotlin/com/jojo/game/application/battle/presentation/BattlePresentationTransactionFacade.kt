// Battle
package com.jojo.game.application.battle.presentation

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleActionSnapshot

/**
 * `BattleDeferredMoveResult` 클래스: presentation 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class BattleDeferredMoveResult(
    val result: TacticalActionResult,
    val path: List<Pair<Int, Int>>,
)

/** BattlePresentationTransactionFacade: 전투 표현 Transaction 진입점이며, 관련 전투 기능을 묶어 안정적인 호출 경로를 제공한다. */
class BattlePresentationTransactionFacade internal constructor(
    /**
     * `battlefield` (Battlefield,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val battlefield: Battlefield,
    /**
     * `units` (() -> Map<String, BattleUnit>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val units: () -> Map<String, BattleUnit>,
    skillTemps: BattleSkillTemp,
    journal: BattleStateJournal,
    /**
     * `moveUnitOperation` ((String, Int, Int) -> TacticalActionResult,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val moveUnitOperation: (String, Int, Int) -> TacticalActionResult,
    /**
     * `lastMovePath` ((String) -> List<Pair<Int, Int>>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val lastMovePath: (String) -> List<Pair<Int, Int>>,
    /**
     * `attackOperation` ((String, String) -> TacticalActionResult,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val attackOperation: (String, String) -> TacticalActionResult,
    /**
     * `castMagicOperation` ((String, String, Int) -> TacticalActionResult,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val castMagicOperation: (String, String, Int) -> TacticalActionResult,
    /**
     * `usePropertyOperation` ((String, String, Int) -> TacticalActionResult,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val usePropertyOperation: (String, String, Int) -> TacticalActionResult,
    /**
     * `isBattleEnded` (() -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val isBattleEnded: () -> Boolean,
    /**
     * `activeFaction` (() -> Faction,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val activeFaction: () -> Faction,
    /**
     * `onUnitRetreat` ((BattleUnit) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onUnitRetreat: (BattleUnit) -> Unit,
) {
    /**
     * `environment` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val environment = BattlePresentationEnvironmentAssembler.build(battlefield, units, skillTemps, journal)

    /**
     * `runtimeSnapshot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun runtimeSnapshot(): BattleActionSnapshot = BattlePresentationCoordinator.runtimeSnapshot(environment)

    /**
     * `restoreRuntime`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun restoreRuntime(snapshot: BattleActionSnapshot) =
        BattlePresentationCoordinator.restoreRuntime(snapshot, environment)

    /**
     * `createActionTransaction`: 필요한 객체나 결과를 생성한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun createActionTransaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit>,
        completionSideEffects: List<() -> Unit>,
    ): BattleActionTransaction = BattlePresentationCoordinator.createActionTransaction(
        actorId, before, after, hitSideEffects, completionSideEffects, environment,
    )

    /**
     * `moveUnit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moveUnit(id: String, targetX: Int, targetY: Int): BattleDeferredMoveResult {
        val (result, path) = BattlePresentationCoordinator.moveUnitForPresentation(
            id, targetX, targetY, moveUnitOperation, lastMovePath, environment,
        )
        return BattleDeferredMoveResult(result, path)
    }

    /**
     * `attack`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun attack(attackerId: String, targetId: String): TacticalActionResult =
        BattlePresentationCoordinator.attackForPresentation(attackerId, targetId, attackOperation, environment)

    /**
     * `castMagic`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
    ): TacticalActionResult = BattlePresentationCoordinator.castMagicForPresentation(
        attackerId, targetId, magicId, castMagicOperation, environment,
    )

    /**
     * `useProperty`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun useProperty(
        userId: String,
        targetId: String,
        itemId: Int,
    ): TacticalActionResult = BattlePresentationCoordinator.usePropertyForPresentation(
        userId, targetId, itemId, usePropertyOperation, environment,
    )

    /**
     * `hasPendingAiUnits`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hasPendingAiUnits(): Boolean =
        BattlePresentationCoordinator.hasPendingAiUnits(isBattleEnded(), activeFaction(), units().values)

    /**
     * `presentationUnit`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun presentationUnit(id: String): BattleUnit? = battlefield.presentationUnit(id)
    /**
     * `pendingPresentationUnits`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun pendingPresentationUnits(): Collection<BattleUnit> = battlefield.pendingPresentationUnits()
    /**
     * `presentationUnits`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun presentationUnits(): List<BattleUnit> = battlefield.allPresentationUnits()
    /**
     * `clearPresentationUnit`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun clearPresentationUnit(id: String) {
        battlefield.clearRetained(id)
    }

    /**
     * `completeScriptedUnitHide`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeScriptedUnitHide(id: String) {
        battlefield.hideForPresentation(id)
    }

    /**
     * `restorePresentationUnit`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun restorePresentationUnit(id: String): BattleUnit? = battlefield.restore(id)

    /**
     * `incrementUnitRetreat`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun incrementUnitRetreat(unit: BattleUnit) {
        unit.retreatCount++
        onUnitRetreat(unit)
    }
}

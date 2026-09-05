package com.jojo.game
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*

data class BattleDeferredMoveResult(
    val result: TacticalActionResult,
    val path: List<Pair<Int, Int>>,
)

/** Owns runtime snapshots and deferred presentation transactions for a battle. */
class BattlePresentationTransactionFacade internal constructor(
    private val battlefield: Battlefield,
    private val units: () -> Map<String, BattleUnit>,
    skillTemps: BattleSkillTemp,
    journal: BattleStateJournal,
    private val moveUnitOperation: (String, Int, Int) -> TacticalActionResult,
    private val lastMovePath: (String) -> List<Pair<Int, Int>>,
    private val attackOperation: (String, String) -> TacticalActionResult,
    private val castMagicOperation: (String, String, Int) -> TacticalActionResult,
    private val usePropertyOperation: (String, String, Int) -> TacticalActionResult,
    private val isBattleEnded: () -> Boolean,
    private val activeFaction: () -> Faction,
    private val onUnitRetreat: (BattleUnit) -> Unit,
) {
    private val environment = BattlePresentationEnvironmentAssembler.build(battlefield, units, skillTemps, journal)

    internal fun runtimeSnapshot(): BattleActionSnapshot = BattlePresentationCoordinator.runtimeSnapshot(environment)

    internal fun restoreRuntime(snapshot: BattleActionSnapshot) =
        BattlePresentationCoordinator.restoreRuntime(snapshot, environment)

    internal fun createActionTransaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit>,
        completionSideEffects: List<() -> Unit>,
    ): BattleActionTransaction = BattlePresentationCoordinator.createActionTransaction(
        actorId, before, after, hitSideEffects, completionSideEffects, environment,
    )

    fun moveUnit(id: String, targetX: Int, targetY: Int): BattleDeferredMoveResult {
        val (result, path) = BattlePresentationCoordinator.moveUnitForPresentation(
            id, targetX, targetY, moveUnitOperation, lastMovePath, environment,
        )
        return BattleDeferredMoveResult(result, path)
    }

    fun attack(attackerId: String, targetId: String): TacticalActionResult =
        BattlePresentationCoordinator.attackForPresentation(attackerId, targetId, attackOperation, environment)

    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
    ): TacticalActionResult = BattlePresentationCoordinator.castMagicForPresentation(
        attackerId, targetId, magicId, castMagicOperation, environment,
    )

    fun useProperty(
        userId: String,
        targetId: String,
        itemId: Int,
    ): TacticalActionResult = BattlePresentationCoordinator.usePropertyForPresentation(
        userId, targetId, itemId, usePropertyOperation, environment,
    )

    fun hasPendingAiUnits(): Boolean =
        BattlePresentationCoordinator.hasPendingAiUnits(isBattleEnded(), activeFaction(), units().values)

    fun presentationUnit(id: String): BattleUnit? = battlefield.presentationUnit(id)
    fun pendingPresentationUnits(): Collection<BattleUnit> = battlefield.pendingPresentationUnits()
    fun presentationUnits(): List<BattleUnit> = battlefield.allPresentationUnits()
    fun clearPresentationUnit(id: String) {
        battlefield.clearRetained(id)
    }

    fun completeScriptedUnitHide(id: String) {
        battlefield.hideForPresentation(id)
    }

    fun restorePresentationUnit(id: String): BattleUnit? = battlefield.restore(id)

    fun incrementUnitRetreat(unit: BattleUnit) {
        unit.retreatCount++
        onUnitRetreat(unit)
    }
}

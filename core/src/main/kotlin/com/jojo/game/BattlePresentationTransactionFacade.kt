package com.jojo.game
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*

/** Owns runtime snapshots and deferred presentation transactions for a battle. */
internal class BattlePresentationTransactionFacade(
    battlefield: Battlefield,
    units: () -> Map<String, BattleUnit>,
    skillTemps: BattleSkillTemp,
    journal: BattleStateJournal,
) {
    private val environment = BattlePresentationEnvironmentAssembler.build(battlefield, units, skillTemps, journal)

    fun runtimeSnapshot(): BattleActionSnapshot = BattlePresentationCoordinator.runtimeSnapshot(environment)

    fun restoreRuntime(snapshot: BattleActionSnapshot) =
        BattlePresentationCoordinator.restoreRuntime(snapshot, environment)

    fun createActionTransaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit>,
        completionSideEffects: List<() -> Unit>,
    ): BattleActionTransaction = BattlePresentationCoordinator.createActionTransaction(
        actorId, before, after, hitSideEffects, completionSideEffects, environment,
    )

    fun moveUnit(
        id: String,
        targetX: Int,
        targetY: Int,
        moveUnit: (String, Int, Int) -> TacticalActionResult,
        lastMovePath: (String) -> List<Pair<Int, Int>>,
    ): Pair<TacticalActionResult, List<Pair<Int, Int>>> = BattlePresentationCoordinator.moveUnitForPresentation(
        id, targetX, targetY, moveUnit, lastMovePath, environment,
    )

    fun attack(attackerId: String, targetId: String, attack: (String, String) -> TacticalActionResult) =
        BattlePresentationCoordinator.attackForPresentation(attackerId, targetId, attack, environment)

    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
        castMagic: (String, String, Int) -> TacticalActionResult,
    ) = BattlePresentationCoordinator.castMagicForPresentation(attackerId, targetId, magicId, castMagic, environment)

    fun useProperty(
        userId: String,
        targetId: String,
        itemId: Int,
        useProperty: (String, String, Int) -> TacticalActionResult,
    ) = BattlePresentationCoordinator.usePropertyForPresentation(userId, targetId, itemId, useProperty, environment)

    fun hasPendingAiUnits(isEnded: Boolean, activeFaction: Faction, units: Collection<BattleUnit>): Boolean =
        BattlePresentationCoordinator.hasPendingAiUnits(isEnded, activeFaction, units)
}

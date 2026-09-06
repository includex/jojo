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
import com.jojo.game.domain.battle.BattleUnitMemento
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleActionSnapshot

internal data class BattlePresentationEnvironment(
    val battlefield: Battlefield,
    val units: () -> Map<String, BattleUnit>,
    val playerMoney: () -> Int,
    val setPlayerMoney: (Int) -> Unit,
    val enemyMoney: () -> Int,
    val setEnemyMoney: (Int) -> Unit,
    val skillTemps: BattleSkillTemp,
    val moveLength: () -> Int,
    val setMoveLength: (Int) -> Unit,
    val lastMovePaths: MutableMap<String, List<Pair<Int, Int>>>,
    val traceActions: MutableList<String>,
    val getPendingActionTransaction: () -> BattleActionTransaction?,
    val setPendingActionTransaction: (BattleActionTransaction?) -> Unit,
    val getStagedHitSideEffects: () -> MutableList<() -> Unit>?,
    val setStagedHitSideEffects: (MutableList<() -> Unit>?) -> Unit,
    val getStagedCompletionSideEffects: () -> MutableList<() -> Unit>?,
    val setStagedCompletionSideEffects: (MutableList<() -> Unit>?) -> Unit,
)

internal object BattlePresentationCoordinator {


    fun runtimeSnapshot(env: BattlePresentationEnvironment): BattleActionSnapshot {
        val all = linkedMapOf<String, BattleUnit>().apply {
            putAll(env.units())
            env.battlefield.pendingPresentationUnits().forEach { unit -> put(unit.id, unit) }
        }
        return BattleActionSnapshot(
            topology = env.battlefield.snapshotTopology(),
            states = all.mapValues { (_, unit) -> BattleUnitMemento.capture(unit) },
            playerMoney = env.playerMoney(),
            enemyMoney = env.enemyMoney(),
            skillTemps = env.skillTemps.snapshot(),
            moveLength = env.moveLength(),
            lastMovePaths = env.lastMovePaths.mapValues { it.value.toList() },
            traceActions = env.traceActions.toList(),
        )
    }


    fun restoreRuntime(snapshot: BattleActionSnapshot, env: BattlePresentationEnvironment) {
        snapshot.states.values.forEach(BattleUnitMemento::restore)
        env.battlefield.restoreTopology(snapshot.topology, snapshot.states.mapValues { it.value.unit })
        env.setPlayerMoney(snapshot.playerMoney)
        env.setEnemyMoney(snapshot.enemyMoney)
        env.skillTemps.restore(snapshot.skillTemps)
        env.setMoveLength(snapshot.moveLength)
        env.lastMovePaths.clear()
        env.lastMovePaths.putAll(snapshot.lastMovePaths)
        env.traceActions.clear()
        env.traceActions.addAll(snapshot.traceActions)
    }

    fun createActionTransaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit>,
        completionSideEffects: List<() -> Unit>,
        env: BattlePresentationEnvironment,
    ): BattleActionTransaction = BattleActionTransaction(
        actorId = actorId,
        before = before,
        after = after,
        hitSideEffects = hitSideEffects,
        completionSideEffects = completionSideEffects,
        restoreSnapshot = { restoreRuntime(it, env) },
        adjustEconomy = { playerDelta, enemyDelta ->
            env.setPlayerMoney(env.playerMoney() + playerDelta)
            env.setEnemyMoney(env.enemyMoney() + enemyDelta)
        },
        presentationUnit = env.battlefield::presentationUnit,
        activeUnit = env.battlefield::activeUnit,
        onCompleted = { transaction ->
            if (env.getPendingActionTransaction() === transaction) env.setPendingActionTransaction(null)
        },
    )

    fun <T : TacticalActionResult> resolveDeferredAction(
        actorId: String,
        env: BattlePresentationEnvironment,
        resolve: () -> T,
    ): T {
        check(env.getPendingActionTransaction() == null) { "previous deferred battle action has not completed" }
        val before = runtimeSnapshot(env)
        env.setStagedHitSideEffects(mutableListOf())
        env.setStagedCompletionSideEffects(mutableListOf())
        val result = try {
            resolve()
        } catch (failure: Throwable) {
            env.setStagedHitSideEffects(null)
            env.setStagedCompletionSideEffects(null)
            restoreRuntime(before, env)
            throw failure
        }
        val hitSideEffects = env.getStagedHitSideEffects().orEmpty().toList()
        val completionSideEffects = env.getStagedCompletionSideEffects().orEmpty().toList()
        env.setStagedHitSideEffects(null)
        env.setStagedCompletionSideEffects(null)
        if (result is TacticalActionResult.Rejected) {
            restoreRuntime(before, env)
            return result
        }
        val after = runtimeSnapshot(env)
        restoreRuntime(before, env)
        env.setPendingActionTransaction(
            createActionTransaction(
                actorId,
                before,
                after,
                hitSideEffects,
                completionSideEffects,
                env
            )
        )
        return result
    }

    fun moveUnitForPresentation(
        id: String,
        targetX: Int,
        targetY: Int,
        moveUnit: (String, Int, Int) -> TacticalActionResult,
        lastMovePath: (String) -> List<Pair<Int, Int>>,
        env: BattlePresentationEnvironment,
    ): Pair<TacticalActionResult, List<Pair<Int, Int>>> {
        var path = emptyList<Pair<Int, Int>>()
        val result = resolveDeferredAction(id, env) {
            moveUnit(id, targetX, targetY).also {
                if (it !is TacticalActionResult.Rejected) path = lastMovePath(id).toList()
            }
        }
        return result to path
    }

    fun attackForPresentation(
        attackerId: String,
        targetId: String,
        attack: (String, String) -> TacticalActionResult,
        env: BattlePresentationEnvironment,
    ): TacticalActionResult =
        resolveDeferredAction(attackerId, env) { attack(attackerId, targetId) }

    fun castMagicForPresentation(
        attackerId: String,
        targetId: String,
        magicId: Int,
        castMagic: (String, String, Int) -> TacticalActionResult,
        env: BattlePresentationEnvironment,
    ): TacticalActionResult =
        resolveDeferredAction(attackerId, env) { castMagic(attackerId, targetId, magicId) }

    fun usePropertyForPresentation(
        userId: String,
        targetId: String,
        itemId: Int,
        useProperty: (String, String, Int) -> TacticalActionResult,
        env: BattlePresentationEnvironment,
    ): TacticalActionResult =
        resolveDeferredAction(userId, env) { useProperty(userId, targetId, itemId) }

    fun hasPendingAiUnits(
        isEnded: Boolean,
        activeFaction: Faction,
        units: Collection<BattleUnit>,
    ): Boolean = !isEnded && units.any {
        it.visible && it.effectiveFaction() == activeFaction && !it.hasActed
    }
}

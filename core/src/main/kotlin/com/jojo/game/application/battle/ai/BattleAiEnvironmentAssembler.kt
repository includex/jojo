// Battle
package com.jojo.game.application.battle.ai

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
internal object BattleAiEnvironmentAssembler {
    fun build(battle: Battle): BattleAiCoordinatorEnvironment = BattleAiCoordinatorEnvironment(
        units = { battle.units },
        unitAt = battle::unitAt,
        areAllied = battle::areAllied,
        weather = { battle.weather },
        round = { battle.round },
        terrain = battle.configuration.terrain,
        terrainResumeRates = battle.configuration.terrainResumeRates,
        terrainMagicFlags = battle.configuration.terrainMagicFlags,
        probabilityResolver = battle.probabilityResolver,
        basePhysicalDamageContext = { attacker, target, splash ->
            BattlePhysicalContextBuilder.basePhysicalDamageContext(
                attacker, target, splash, env = battle.combat.physicalContext(),
            )
        },
        reachableTiles = battle.movement::reachableTiles,
        traceActions = battle.journal.mutableTraceActions(),
        movementOffsets = battle.configuration.movementOffsets,
        enemyMasterUnitId = battle.configuration.enemyMasterUnitId,
        findMovementPath = { unit, targetX, targetY, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget ->
            battle.movement.findMovementPath(unit, targetX, targetY, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget)
        },
        findReachableEmptyPosition = battle.movement::findReachableEmptyPosition,
        movePoints = { unit, movement -> battle.movement.movePoints(unit, movement, null, null) },
        outcome = battle::outcome,
        activeFaction = { battle.activeFaction },
        moveUnit = { id, targetX, targetY -> battle.movement.moveUnit(id, targetX, targetY, null) },
        attack = { attackerId, targetId -> battle.combat.attack(attackerId, targetId, null) },
        castMagic = { attackerId, targetId, magicId, reaction, bypassCondition ->
            battle.combat.castMagic(attackerId, targetId, magicId, reaction, bypassCondition)
        },
        lastMovePath = battle::lastMovePath,
        aiTurnOrder = battle.journal::aiTurnOrder,
        clearAiTurnOrder = { battle.journal.recordAiTurnOrder(null) },
        setLastAiUnitResolution = battle.journal::recordLastAiUnitResolution,
        lastAiUnitResolution = battle.journal::lastAiUnitResolution,
        runtimeSnapshot = battle.presentation::runtimeSnapshot,
        restoreRuntime = battle.presentation::restoreRuntime,
        setPendingActionTransaction = battle.journal::recordPendingActionTransaction,
        pendingActionTransaction = battle.journal::pendingActionTransaction,
        stagedHitSideEffects = battle.journal::stagedHitSideEffects,
        setStagedHitSideEffects = battle.journal::recordStagedHitSideEffects,
        stagedCompletionSideEffects = battle.journal::stagedCompletionSideEffects,
        setStagedCompletionSideEffects = battle.journal::recordStagedCompletionSideEffects,
        createActionTransaction = battle.presentation::createActionTransaction,
    )
}

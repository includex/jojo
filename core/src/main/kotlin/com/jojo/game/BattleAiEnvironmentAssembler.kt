package com.jojo.game

/** Assembles the AI coordinator boundary without owning AI progress. */
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
                attacker, target, splash, env = BattleCombatEnvironmentAssembler.physicalContext(battle),
            )
        },
        reachableTiles = battle::reachableTiles,
        traceActions = battle.journal.mutableTraceActions(),
        movementOffsets = battle.configuration.movementOffsets,
        enemyMasterUnitId = battle.configuration.enemyMasterUnitId,
        findMovementPath = { unit, targetX, targetY, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget ->
            battle.findMovementPath(unit, targetX, targetY, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget)
        },
        findReachableEmptyPosition = battle::findReachableEmptyPosition,
        movePoints = { unit, movement -> battle.movePoints(unit, movement) },
        outcome = battle::outcome,
        activeFaction = { battle.activeFaction },
        moveUnit = { id, targetX, targetY -> battle.moveUnit(id, targetX, targetY) },
        attack = { attackerId, targetId -> battle.attack(attackerId, targetId) },
        castMagic = { attackerId, targetId, magicId, reaction, bypassCondition ->
            battle.castMagic(attackerId, targetId, magicId, reaction, bypassCondition)
        },
        lastMovePath = battle::lastMovePath,
        aiTurnOrder = battle.journal::aiTurnOrder,
        clearAiTurnOrder = { battle.journal.recordAiTurnOrder(null) },
        setLastAiUnitResolution = battle.journal::recordLastAiUnitResolution,
        lastAiUnitResolution = battle.journal::lastAiUnitResolution,
        runtimeSnapshot = battle::runtimeSnapshot,
        restoreRuntime = battle::restoreRuntime,
        setPendingActionTransaction = battle.journal::recordPendingActionTransaction,
        pendingActionTransaction = battle.journal::pendingActionTransaction,
        stagedHitSideEffects = battle.journal::stagedHitSideEffects,
        setStagedHitSideEffects = battle.journal::recordStagedHitSideEffects,
        stagedCompletionSideEffects = battle.journal::stagedCompletionSideEffects,
        setStagedCompletionSideEffects = battle.journal::recordStagedCompletionSideEffects,
        createActionTransaction = battle::createActionTransaction,
    )
}

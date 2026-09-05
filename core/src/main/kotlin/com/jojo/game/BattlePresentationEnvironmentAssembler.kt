package com.jojo.game
import com.jojo.game.domain.battle.Battlefield

/** Builds the state bridge shared by presentation transactions and AI deferral. */
internal object BattlePresentationEnvironmentAssembler {
    fun build(
        battlefield: Battlefield,
        units: () -> Map<String, BattleUnit>,
        skillTemps: BattleSkillTemp,
        journal: BattleStateJournal,
    ): BattlePresentationEnvironment = BattlePresentationEnvironment(
        battlefield = battlefield,
        units = units,
        playerMoney = { journal.playerMoney },
        setPlayerMoney = journal::setPlayerMoney,
        enemyMoney = { journal.enemyMoney },
        setEnemyMoney = journal::setEnemyMoney,
        skillTemps = skillTemps,
        moveLength = journal::currentMoveLength,
        setMoveLength = journal::setMoveLength,
        lastMovePaths = journal.mutableLastMovePaths(),
        traceActions = journal.mutableTraceActions(),
        getPendingActionTransaction = journal::pendingActionTransaction,
        setPendingActionTransaction = journal::recordPendingActionTransaction,
        getStagedHitSideEffects = journal::stagedHitSideEffects,
        setStagedHitSideEffects = journal::recordStagedHitSideEffects,
        getStagedCompletionSideEffects = journal::stagedCompletionSideEffects,
        setStagedCompletionSideEffects = journal::recordStagedCompletionSideEffects,
    )
}

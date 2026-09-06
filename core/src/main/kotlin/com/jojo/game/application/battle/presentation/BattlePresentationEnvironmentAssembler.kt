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
import com.jojo.game.domain.battle.BattleSkillTemp
import com.jojo.game.domain.battle.Battlefield
/**
 * `BattlePresentationEnvironmentAssembler` 싱글턴 객체: presentation 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object BattlePresentationEnvironmentAssembler {
    /**
     * `build`: 필요한 객체나 결과를 생성한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

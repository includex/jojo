package com.jojo.game.domain.battle
import com.jojo.game.domain.battle.BattleUnitMemento
import com.jojo.game.domain.battle.Battlefield

import com.jojo.game.*

/** Complete mutable aggregate state captured around one calculated battle action. */
internal data class BattleActionSnapshot(
    val topology: Battlefield.TopologySnapshot,
    val states: Map<String, BattleUnitMemento>,
    val playerMoney: Int,
    val enemyMoney: Int,
    val skillTemps: Map<String, Map<Int, Pair<Int, Int>>>,
    val moveLength: Int,
    val lastMovePaths: Map<String, List<Pair<Int, Int>>>,
    val traceActions: List<String>,
)

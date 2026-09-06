// Battle
package com.jojo.game.domain.battle
import com.jojo.game.domain.battle.BattleUnitMemento
import com.jojo.game.domain.battle.Battlefield

/** BattleActionSnapshot: 한 전술 동작 전의 복원 지점으로, 전장 배치·유닛 상태·자금·임시 효과를 함께 보존한다. */
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

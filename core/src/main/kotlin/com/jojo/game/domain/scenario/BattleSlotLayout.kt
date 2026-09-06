// Scenario
package com.jojo.game.domain.scenario

import com.jojo.game.domain.battle.Faction

/** BattleSlotLayout: 전투 준비 화면에서 진영별 편성 칸의 좌표·최대 인원을 계산하는 배치 규칙이다. */
internal object BattleSlotLayout {
    const val mineCount = 20
    const val friendEnd = 40
    const val enemyStart = mineCount + friendEnd
    private const val enemyBlockCount = 3
    const val enemyBlockLength = 80
    const val enemyEnd = enemyBlockCount * enemyBlockLength

    fun slotFor(faction: ScenarioUnitFaction, instanceId: Int, enemyBlockStart: Int = enemyStart): Int = when (faction) {
        ScenarioUnitFaction.MINE -> instanceId
        ScenarioUnitFaction.FRIEND -> friendEnd + instanceId
        ScenarioUnitFaction.ENEMY -> enemyBlockStart + instanceId
    }

    fun battleId(faction: ScenarioUnitFaction, battleSlot: Int): String = when (faction) {
        ScenarioUnitFaction.MINE -> "mine-$battleSlot"
        ScenarioUnitFaction.FRIEND -> "friend-${battleSlot - friendEnd}"
        ScenarioUnitFaction.ENEMY -> "enemy-${battleSlot - enemyStart}"
    }

    fun stageKey(faction: ScenarioUnitFaction, battleSlot: Int): String =
        "${faction.name}:${battleId(faction, battleSlot).substringAfter('-')}"

    fun rangeFor(camp: Faction): IntRange = when (camp) {
        Faction.PLAYER -> 0 until mineCount
        Faction.FRIEND -> mineCount until friendEnd
        Faction.ENEMY, Faction.REINFORCEMENTS -> enemyStart until enemyEnd
    }
}

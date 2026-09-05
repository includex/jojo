package com.jojo.game

import com.jojo.game.domain.scenario.*

/** Encodes source camp selectors used by rectangle AI and hide operations. */
internal object ScenarioStageBattleUnitSelection {
    fun matchesAiCamp(unit: ScenarioBattleUnit, camp: Int): Boolean = when (camp) {
        0, 1, 2, 3 -> scriptCamp(unit) == camp
        4 -> unit.faction != ScenarioUnitFaction.ENEMY
        5 -> unit.faction == ScenarioUnitFaction.ENEMY
        6 -> true
        else -> false
    }

    fun inRectangle(unit: ScenarioBattleUnit, x1: Int, y1: Int, x2: Int, y2: Int): Boolean =
        unit.x in x1..x2 && unit.y in y1..y2

    private fun scriptCamp(unit: ScenarioBattleUnit): Int = when (unit.faction) {
        ScenarioUnitFaction.MINE -> 0
        ScenarioUnitFaction.FRIEND -> 1
        ScenarioUnitFaction.ENEMY -> if (unit.reinforcement) 3 else 2
    }
}

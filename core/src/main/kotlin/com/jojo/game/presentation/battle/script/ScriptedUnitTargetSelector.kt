package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.isEnemySide
import com.jojo.game.domain.battle.isPlayerSide

/** Applies BattleScreen's authored getItem selector rules without exposing its roster state. */
internal class ScriptedUnitTargetSelector(
    private val visibleUnits: () -> List<BattleUnit>,
    private val isMineMaster: (String) -> Boolean,
    private val byCharacter: (Int) -> BattleUnit?,
) {
    fun select(selector: Int): BattleUnit? {
        val units = visibleUnits()
        return when (selector) {
            1024 -> units.firstOrNull()
            1025 -> units.firstOrNull(BattleUnit::isPlayerSide)
            1026 -> units.firstOrNull { it.type().isEnemySide() }
            1027 -> units.firstOrNull { isMineMaster(it.id) }
                ?: units.firstOrNull(BattleUnit::isPlayerSide)
            else -> byCharacter(selector)
        }
    }
}

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
import com.jojo.game.domain.battle.isEnemySide
import com.jojo.game.domain.battle.isPlayerSide
internal class BattleOutcomeCoordinator(
    private val units: () -> Collection<BattleUnit>,
    private val getRound: () -> Int,
    private val enabledFeatures: () -> Int,
    initialMaxRounds: Int = 99,
) {
    var maxRounds: Int = initialMaxRounds
        private set

    var scriptedOutcome: BattleOutcome? = null
        private set


    fun outcome(): BattleOutcome? {
        scriptedOutcome?.let { return it }
        val playerRemaining = units().any { it.effectiveFaction().isPlayerSide() }
        val enemyRemaining = units().any { it.effectiveFaction().isEnemySide() }
        return when {
            getRound() >= maxRounds -> BattleOutcome.ENEMY_VICTORY
            !enemyRemaining && playerRemaining -> BattleOutcome.PLAYER_VICTORY
            !playerRemaining && enemyRemaining -> BattleOutcome.ENEMY_VICTORY
            else -> null
        }
    }
    fun setMaxRounds(value: Int) {
        val extra = if (enabledFeatures() and ENABLED_FEATURE_ZJHH != 0) 4 else 0
        maxRounds = (value + extra).coerceAtLeast(1)
    }
    fun setResolvedMaxRounds(value: Int) {
        maxRounds = value.coerceAtLeast(1)
    }
    fun setScriptedOutcome(value: BattleOutcome) {
        scriptedOutcome = value
    }

    fun syncScriptedOutcome(value: BattleOutcome?) {
        value?.let { scriptedOutcome = it }
    }

    companion object {
        const val ENABLED_FEATURE_ZJHH = 8
    }
}

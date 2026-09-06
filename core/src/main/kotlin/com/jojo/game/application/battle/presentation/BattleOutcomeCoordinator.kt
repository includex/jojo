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

/**
 * Adjudicates scenario outcome conditions, maximum round limits, and scripted overrides.
 */
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
        // BATTLE_UNIT_FALG.HIDE changes rendering/targeting, not isExist().
        // Yingchuan's only Mine actor is hidden until round two and must still
        // prevent the opening cut-scene from being adjudicated as a loss.
        val playerRemaining = units().any { it.effectiveFaction().isPlayerSide() }
        val enemyRemaining = units().any { it.effectiveFaction().isEnemySide() }
        return when {
            getRound() >= maxRounds -> BattleOutcome.ENEMY_VICTORY
            !enemyRemaining && playerRemaining -> BattleOutcome.PLAYER_VICTORY
            !playerRemaining && enemyRemaining -> BattleOutcome.ENEMY_VICTORY
            else -> null
        }
    }

    /** BattleScreen.setMaxRound: ZJHH contributes exactly four turns. */
    fun setMaxRounds(value: Int) {
        val extra = if (enabledFeatures() and ENABLED_FEATURE_ZJHH != 0) 4 else 0
        maxRounds = (value + extra).coerceAtLeast(1)
    }

    /** A ScenarioStage setMaxRound value has already applied BattleScreen.eFlag(). */
    fun setResolvedMaxRounds(value: Int) {
        maxRounds = value.coerceAtLeast(1)
    }

    /** Scenario scripts can end a battle through reward()/lose() without eliminating every enemy. */
    fun setScriptedOutcome(value: BattleOutcome) {
        scriptedOutcome = value
    }

    /**
     * Mirrors a ScenarioStage result without clearing an outcome on ordinary
     * scene1 passes which have not called reward/lose.
     */

    fun syncScriptedOutcome(value: BattleOutcome?) {
        value?.let { scriptedOutcome = it }
    }

    companion object {
        const val ENABLED_FEATURE_ZJHH = 8
    }
}

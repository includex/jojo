package com.jojo.game.presentation.battle.render

/**
 * Renderer-independent HP display state for BattleScreen._attack3.  Tactical
 * state may already contain the final value, but this model exposes only the
 * value that the authored BRAnime `hit` event has made visible.
 */

class BattleHealthPresentation {

    data class Transition(val fromHp: Int, val toHp: Int, val revealAt: Float)

    private val transitions = linkedMapOf<String, Transition>()


    fun schedule(unitId: String, fromHp: Int, toHp: Int, revealAt: Float) {
        transitions[unitId] = Transition(fromHp, toHp, revealAt)
    }


    fun shownHp(unitId: String, now: Float, fallbackHp: Int): Int = transitions[unitId]?.let {
        if (now < it.revealAt) it.fromHp else it.toHp
    } ?: fallbackHp

    /** A follow-up/ counterattack replaces the prior visible transition. */
    fun clear(unitId: String) {
        transitions.remove(unitId)
    }
}

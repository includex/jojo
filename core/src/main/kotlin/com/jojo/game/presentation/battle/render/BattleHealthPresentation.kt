// Battle
package com.jojo.game.presentation.battle.render

class BattleHealthPresentation {
    data class Transition(val fromHp: Int, val toHp: Int, val revealAt: Float)

    private val transitions = linkedMapOf<String, Transition>()


    fun schedule(unitId: String, fromHp: Int, toHp: Int, revealAt: Float) {
        transitions[unitId] = Transition(fromHp, toHp, revealAt)
    }


    fun shownHp(unitId: String, now: Float, fallbackHp: Int): Int = transitions[unitId]?.let {
        if (now < it.revealAt) it.fromHp else it.toHp
    } ?: fallbackHp
    fun clear(unitId: String) {
        transitions.remove(unitId)
    }
}

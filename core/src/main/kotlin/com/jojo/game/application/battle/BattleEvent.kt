package com.jojo.game.application.battle

import com.jojo.game.domain.battle.TurnTrigger

/** A scheduled action evaluated against the mutable battle coordinator. */
class BattleEvent(
    val id: String,
    val trigger: TurnTrigger,
    private val action: (Battle) -> Unit,
) {
    fun matches(state: Battle): Boolean =
        state.round >= trigger.round && state.activeFaction == trigger.faction

    fun execute(state: Battle) = action(state)
}

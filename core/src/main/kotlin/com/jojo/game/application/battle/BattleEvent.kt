// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.TurnTrigger

/** BattleEvent: 가변 전투 상태에 대해 실행할 예약 작업으로, 시점별 시나리오 효과를 적용한다. */
class BattleEvent(
    val id: String,
    val trigger: TurnTrigger,
    private val action: (Battle) -> Unit,
) {
    fun matches(state: Battle): Boolean =
        state.round >= trigger.round && state.activeFaction == trigger.faction

    fun execute(state: Battle) = action(state)
}

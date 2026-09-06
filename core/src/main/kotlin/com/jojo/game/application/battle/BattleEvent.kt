// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.TurnTrigger

/** BattleEvent: 가변 전투 상태에 대해 실행할 예약 작업으로, 시점별 시나리오 효과를 적용한다. */
class BattleEvent(
    val id: String,
    val trigger: TurnTrigger,
    /**
     * `action` ((Battle) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val action: (Battle) -> Unit,
) {
    /**
     * `matches`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun matches(state: Battle): Boolean =
        state.round >= trigger.round && state.activeFaction == trigger.faction

    /**
     * `execute`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun execute(state: Battle) = action(state)
}

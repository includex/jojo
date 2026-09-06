// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.settlement.ResolvedBattleReward

/** BattleRewardFlow: 전투 보상 표시 흐름으로, 금전·아이템·종료 보상을 순서대로 공개한다. */
class BattleRewardFlow(val reward: ResolvedBattleReward) {
    /**
     * `Phase` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    enum class Phase { MONEY, ITEMS, END, COMPLETE }

    /**
     * `phase` (Phase): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var phase: Phase = when {
        reward.end -> Phase.END
        reward.money > 0 -> Phase.MONEY
        reward.itemIds.isNotEmpty() -> Phase.ITEMS
        else -> Phase.COMPLETE
    }
        private set
    /**
     * `visibleItemCount` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var visibleItemCount: Int = if (phase == Phase.ITEMS) 1 else 0
        private set

    /**
     * `complete` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val complete: Boolean get() = phase == Phase.COMPLETE

    /** advance: 현재 보상 단계를 진행하고, 다음 보상 또는 완료 상태로 전환한다. */
    fun advance() {
        phase = when (phase) {
            Phase.MONEY -> if (reward.itemIds.isNotEmpty()) Phase.ITEMS else Phase.COMPLETE
            Phase.ITEMS -> if (visibleItemCount < reward.itemIds.size) {
                visibleItemCount++
                Phase.ITEMS
            } else Phase.COMPLETE
            Phase.END, Phase.COMPLETE -> Phase.COMPLETE
        }
        if (phase == Phase.ITEMS && visibleItemCount == 0) visibleItemCount = 1
    }
}

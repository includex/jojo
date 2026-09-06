// Verification
package com.jojo.game.presentation.battle.verification

import com.jojo.game.presentation.battle.overlay.WinConditionsLayer

/** 검증용 전투 유닛의 조작 가능 여부와 생존 상태를 보관한다. */
data class BattleScreenIsolatedUnit(
    val control: Boolean,
    val exist: Boolean,
    val acted: Boolean,
)

/** 검증용 전투 화면의 일시 정지, 모달, 다음 행동 상태와 이벤트를 보관한다. */
data class BattleScreenIsolatedView(
    val paused: Boolean,
    val modal: Boolean,
    val action: Boolean,
    val events: List<String>,
)

/** 검증 harness가 전투 화면의 승리 조건 모달과 다음 행동 상태를 재현하도록 조정한다. */
class BattleScreenIsolatedContract(
    /** `units` (List<BattleScreenIsolatedUnit>): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val units: List<BattleScreenIsolatedUnit>,
    /** `collocation` (Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val collocation: Boolean,
    /** `round` (Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val round: Int,
) {
    /**
     * `paused` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var paused = false
    /**
     * `modal` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var modal = false
    /**
     * `pending` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val pending = mutableListOf<String>()

    /** 승리 조건 모달을 열고 검증 이벤트를 기록한다. */
    fun showWinCondition(text: String) {
        paused = true
        modal = true
        pending += "pause"
        pending += "layer:WinConditionsLayer:$text:$round"
    }

    /** 승리 조건 모달의 닫기 입력을 반영한다. */
    fun cancel(event: Int) {
        if (event == WinConditionsLayer.TOUCH_END && modal) {
            modal = false
            paused = false
            pending += "resume"
        }
    }

    /** 다음으로 조작할 수 있는 아군 유닛이 있는지 판별한다. */
    fun nextNotOperUnit(camp: Int) =
        !collocation && camp == 0 && units.any { it.control && it.exist && !it.acted }

    /** 현재 검증 상태를 반환하고 누적 이벤트를 비운다. */
    fun view() = BattleScreenIsolatedView(
        paused,
        modal,
        nextNotOperUnit(0),
        pending.toList().also { pending.clear() },
    )
}

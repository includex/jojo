// Battle
package com.jojo.game.presentation.battle.edit

import com.jojo.game.application.campaign.RegistrationFlow
/**
 * `BattleRegisterRoute`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class BattleRegisterRoute {

    /** View: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
    data class View(val titleTouchCount: Int, val registerAttached: Boolean, val input: List<String>)

    /**
     * `count` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var count = 0
    /**
     * `register` (RegistrationFlow?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var register: RegistrationFlow? = null
    /**
     * `input` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val input = mutableListOf<String>()


    /**
     * `titleTouchEnd`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun titleTouchEnd() {
        input += "EditLayer3.bg1.label TOUCH_END"
        count++
        if (count == 6) register = RegistrationFlow()
        count %= 7
    }


    /**
     * `cancelTouchEnd`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cancelTouchEnd() {
        register?.touch(1, 2)
        if (register?.removed == 1) register = null
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() = View(count, register != null, input.toList())
}

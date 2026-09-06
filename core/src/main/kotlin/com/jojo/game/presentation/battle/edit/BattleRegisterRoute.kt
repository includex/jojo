// Battle
package com.jojo.game.presentation.battle.edit

import com.jojo.game.application.campaign.RegistrationFlow
class BattleRegisterRoute {

    /** View: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
    data class View(val titleTouchCount: Int, val registerAttached: Boolean, val input: List<String>)

    private var count = 0
    private var register: RegistrationFlow? = null
    private val input = mutableListOf<String>()


    fun titleTouchEnd() {
        input += "EditLayer3.bg1.label TOUCH_END"
        count++
        if (count == 6) register = RegistrationFlow()
        count %= 7
    }


    fun cancelTouchEnd() {
        register?.touch(1, 2)
        if (register?.removed == 1) register = null
    }


    fun view() = View(count, register != null, input.toList())
}

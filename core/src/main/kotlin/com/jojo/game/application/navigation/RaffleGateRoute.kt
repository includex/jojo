package com.jojo.game.application.navigation

class RaffleGateRoute {

    enum class Layer { HALL, HALL_MENU, SETTING, RAFFLE }

    data class View(val layer: Layer, val supportAdCode: Int?, val raffleAttached: Boolean, val input: List<String>)

    private var layer = Layer.HALL
    private var supportAdCode: Int? = null
    private val input = mutableListOf<String>()

    fun openHallMenu(touchEnd: Boolean) {
        if (touchEnd && layer == Layer.HALL) {
            input += "HallLayer menu TOUCH_END"
            layer = Layer.HALL_MENU
        }
    }

    fun hallMenuButton(tag: Int, touchEnd: Boolean) {
        if (touchEnd && layer == Layer.HALL_MENU && tag == 3) {
            input += "HallMenuLayer button3 TOUCH_END"
            layer = Layer.SETTING
        }
    }

    fun settingButton(tag: Int, touchEnd: Boolean, helperCode: Int) {
        if (!touchEnd || layer != Layer.SETTING || tag != 8) return
        input += "SettingLayer button13(tag8) TOUCH_END"
        supportAdCode = helperCode
        if (helperCode >= 8) layer = Layer.RAFFLE
    }

    fun view() = View(layer, supportAdCode, layer == Layer.RAFFLE, input.toList())
}

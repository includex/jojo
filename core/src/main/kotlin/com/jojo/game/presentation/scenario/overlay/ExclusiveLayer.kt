package com.jojo.game.presentation.scenario.overlay

/** Interaction state for Global/scene/ExclusiveLayer (source layer id 126). */
class ExclusiveLayer(initialTab: Tab = Tab.SET_LIST) {

    enum class Tab { SET_LIST, EXCLUSIVE_LIST }

    var selectedTab: Tab = initialTab
        private set
    var attached: Boolean = true
        private set

    /** Mirrors the source Button callback: only TOUCH_END mutates the layer. */
    fun onButton(index: Int, event: Int) {
        if (!attached || event != TOUCH_END) return
        when (index) {
            0 -> selectedTab = Tab.SET_LIST
            1 -> selectedTab = Tab.EXCLUSIVE_LIST
            2 -> attached = false
        }
    }


    fun onCancel(event: Int) {
        if (event == TOUCH_END) attached = false
    }

    companion object {
        const val TOUCH_END = 2
    }
}

/** EquipLayer.button14's production child-layer route. */
object EquipExclusiveRoute {

    fun openFromInformationButton(event: Int): ExclusiveLayer? =
        if (event == ExclusiveLayer.TOUCH_END) ExclusiveLayer() else null
}

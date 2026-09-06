// Scenario
package com.jojo.game.presentation.scenario.overlay

/** ExclusiveLayer: 세트 목록과 전용 목록 탭을 전환하고, 닫기 입력을 처리하는 전용 장비 모달이다. */
class ExclusiveLayer(initialTab: Tab = Tab.SET_LIST) {

    enum class Tab { SET_LIST, EXCLUSIVE_LIST }

    var selectedTab: Tab = initialTab
        private set
    var attached: Boolean = true
        private set

    /** onButton: 탭 또는 닫기 버튼 터치에 따라 전용 장비 모달의 선택 상태를 변경한다. */
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

/** EquipExclusiveRoute: Equip 전용 경로이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
object EquipExclusiveRoute {

    fun openFromInformationButton(event: Int): ExclusiveLayer? =
        if (event == ExclusiveLayer.TOUCH_END) ExclusiveLayer() else null
}

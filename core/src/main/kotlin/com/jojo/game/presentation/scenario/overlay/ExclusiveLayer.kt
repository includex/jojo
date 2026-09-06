// Scenario
package com.jojo.game.presentation.scenario.overlay

/** ExclusiveLayer: 세트 목록과 전용 목록 탭을 전환하고, 닫기 입력을 처리하는 전용 장비 모달이다. */
class ExclusiveLayer(initialTab: Tab = Tab.SET_LIST) {

    /**
     * `Tab`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class Tab { SET_LIST, EXCLUSIVE_LIST }

    /**
     * `selectedTab` (Tab): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var selectedTab: Tab = initialTab
        private set
    /**
     * `attached` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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


    /**
     * `onCancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCancel(event: Int) {
        if (event == TOUCH_END) attached = false
    }

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
    }
}

/** EquipExclusiveRoute: Equip 전용 경로이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
object EquipExclusiveRoute {

    /**
     * `openFromInformationButton`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openFromInformationButton(event: Int): ExclusiveLayer? =
        if (event == ExclusiveLayer.TOUCH_END) ExclusiveLayer() else null
}

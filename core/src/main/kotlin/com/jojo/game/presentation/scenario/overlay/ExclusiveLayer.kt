package com.jojo.game.presentation.scenario.overlay

/** Interaction state for Global/scene/ExclusiveLayer (source layer id 126). */
class ExclusiveLayer(initialTab: Tab = Tab.SET_LIST) {
    /**
     * enum class  `Tab`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * 공개 메서드 `onCancel`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCancel(event: Int) {
        if (event == TOUCH_END) attached = false
    }

    companion object {
        const val TOUCH_END = 2
    }
}

/** EquipLayer.button14's production child-layer route. */
object EquipExclusiveRoute {
    /**
     * 공개 메서드 `openFromInformationButton`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `ExclusiveLayer?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun openFromInformationButton(event: Int): ExclusiveLayer? =
        if (event == ExclusiveLayer.TOUCH_END) ExclusiveLayer() else null
}

package com.jojo.game

/** Exact state/event implementation of battle/BattleInitLayer.js; prefab owns its animation. */
class BattleInitLayer(private val effects: Effects = Effects.NONE) {
    /**
     * interface  `Effects`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface Effects {
        fun playInitBattle()
        fun stopAllEffects()

        companion object {
            val NONE = object : Effects {
                override fun playInitBattle() = Unit
                override fun stopAllEffects() = Unit
            }
        }
    }

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(val flag: Int, val attached: Boolean, val labels: List<String>)

    private var flag = 0
    private var attached = false
    private var labels = listOf("", "")

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(value: Int): View {
        flag = value; attached = true; effects.playInitBattle(); return view()
    }

    /** BATTLE_LOAD_BGMAP listener updates both prefab labels. */
    fun onLoadBgMap(name: String): View {
        labels = List(2) { name + if (flag and 1 != 0) " ▪ 훈련" else "" }; return view()
    }

    /**
     * 공개 메서드 `onDestroy`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onDestroy() {
        attached = false; effects.stopAllEffects()
    }

    fun view() = View(flag, attached, labels)
}

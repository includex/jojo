// Battle
package com.jojo.game.presentation.battle.bootstrap

/** BattleInitLayer: 전투 시작 전 지도와 안내 문구를 보여 주는 초기화 레이어다. */
class BattleInitLayer(private val effects: Effects = Effects.NONE) {
    /** 초기화 레이어의 생성·해제 시점에 필요한 사운드 효과 포트다. */
    interface Effects {
        /** 전투 초기화 사운드를 재생한다. */
        fun playInitBattle()

        /** 남아 있는 효과음을 중지한다. */
        fun stopAllEffects()

        companion object {
            /** 외부 사운드 연결이 없을 때 사용하는 무동작 구현이다. */
            val NONE = object : Effects {
                /**
                 * `playInitBattle`: 타입의 핵심 동작을 수행한다.
                 * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
                 */

                override fun playInitBattle() = Unit
                /**
                 * `stopAllEffects`: 입력을 규칙에 따라 계산·변환한다.
                 * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
                 */

                override fun stopAllEffects() = Unit
            }
        }
    }

    /** 초기화 레이어를 그릴 때 필요한 불변 화면 상태다. */
    data class View(val flag: Int, val attached: Boolean, val labels: List<String>)

    /**
     * `flag` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var flag = 0
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = false
    /**
     * `labels` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var labels = listOf("", "")

    /** 초기화 레이어를 부착하고 전투 시작 사운드를 재생한다. */
    fun onCreate(value: Int): View {
        flag = value
        attached = true
        effects.playInitBattle()
        return view()
    }

    /** 지도 이름을 두 안내 라벨에 반영하고 훈련 전투 표기를 덧붙인다. */
    fun onLoadBgMap(name: String): View {
        labels = List(2) { name + if (flag and 1 != 0) " ▪ 훈련" else "" }
        return view()
    }

    /** 레이어를 해제하고 초기화 중 재생한 효과음을 정리한다. */
    fun onDestroy() {
        attached = false
        effects.stopAllEffects()
    }

    /** 현재 초기화 레이어 상태를 불변 화면 모델로 반환한다. */
    fun view() = View(flag, attached, labels)
}

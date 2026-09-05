package com.jojo.game.presentation.battle.edit

import com.jojo.game.RegistrationFlow

/** Actual EditLayer3 hidden-title route that owns Global139 RegisterLayer. */
class BattleRegisterRoute {
    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(val titleTouchCount: Int, val registerAttached: Boolean, val input: List<String>)

    private var count = 0
    private var register: RegistrationFlow? = null
    private val input = mutableListOf<String>()

    /**
     * 공개 메서드 `titleTouchEnd`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun titleTouchEnd() {
        input += "EditLayer3.bg1.label TOUCH_END"
        count++
        if (count == 6) register = RegistrationFlow()
        count %= 7
    }

    /**
     * 공개 메서드 `cancelTouchEnd`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun cancelTouchEnd() {
        register?.touch(1, 2)
        if (register?.removed == 1) register = null
    }

    /**
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view() = View(count, register != null, input.toList())
}

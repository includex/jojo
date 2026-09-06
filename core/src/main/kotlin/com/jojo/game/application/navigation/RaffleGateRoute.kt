// Navigation
package com.jojo.game.application.navigation
import com.jojo.game.presentation.shared.overlay.*

/** RaffleGateRoute: 홀 메뉴에서 추첨·설정 화면으로 이동할 때 입력 순서와 표시 계층을 유지한다. */
class RaffleGateRoute {

    /**
     * `Layer` 클래스: navigation 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    enum class Layer { HALL, HALL_MENU, SETTING, RAFFLE }

    /**
     * `View` 클래스: navigation 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class View(val layer: Layer, val supportAdCode: Int?, val raffleAttached: Boolean, val input: List<String>)

    /**
     * `layer` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var layer = Layer.HALL
    /**
     * `supportAdCode` (Int?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var supportAdCode: Int? = null
    /**
     * `input` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val input = mutableListOf<String>()

    /**
     * `openHallMenu`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun openHallMenu(touchEnd: Boolean) {
        if (touchEnd && layer == Layer.HALL) {
            input += "HallLayer menu TOUCH_END"
            layer = Layer.HALL_MENU
        }
    }

    /**
     * `hallMenuButton`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hallMenuButton(tag: Int, touchEnd: Boolean) {
        if (touchEnd && layer == Layer.HALL_MENU && tag == 3) {
            input += "HallMenuLayer button3 TOUCH_END"
            layer = Layer.SETTING
        }
    }

    /**
     * `settingButton`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun settingButton(tag: Int, touchEnd: Boolean, helperCode: Int) {
        if (!touchEnd || layer != Layer.SETTING || tag != 8) return
        input += "SettingLayer button13(tag8) TOUCH_END"
        supportAdCode = helperCode
        if (helperCode >= 8) layer = Layer.RAFFLE
    }

    /**
     * `view`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun view() = View(layer, supportAdCode, layer == Layer.RAFFLE, input.toList())
}

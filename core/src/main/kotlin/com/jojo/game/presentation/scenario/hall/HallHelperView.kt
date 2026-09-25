// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.presentation.i18n.GameText
import com.jojo.game.presentation.shared.overlay.*

/** HallHelperView: 거점 도움말 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallHelperView(val text: String) {
    companion object {
        /**
         * `default` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val default = HallHelperView(
            GameText.S_DE036B2104 +
                GameText.S_6C9CBA423C +
                GameText.S_360FBBD2E1 +
                GameText.S_F5CE653B43 +
                GameText.S_79B24282A6 +
                GameText.S_1DF67FC361 +
                GameText.S_37E8F20B08 +
                GameText.S_42FA751364 +
                GameText.S_51F600E95C,
        )
    }
}

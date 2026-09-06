// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallEquipConfirmationView: 장비 교체 확인 모달의 대상, 변화 수치와 행동 문구를 전달한다. */
internal data class HallEquipConfirmationView(
    val values: List<Int>,
    val actionLabel: String,
) {
    companion object {
        /**
         * `from`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun from(values: List<Int>, actionLabel: String): HallEquipConfirmationView = HallEquipConfirmationView(
            values = List(8) { index -> values.getOrElse(index) { 0 } },
            actionLabel = actionLabel,
        )
    }
}

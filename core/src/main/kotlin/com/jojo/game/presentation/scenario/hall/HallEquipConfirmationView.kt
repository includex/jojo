// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallEquipConfirmationView: 거점 Equip Confirmation 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallEquipConfirmationView(
    val values: List<Int>,
    val actionLabel: String,
) {
    companion object {
        fun from(values: List<Int>, actionLabel: String): HallEquipConfirmationView = HallEquipConfirmationView(
            values = List(8) { index -> values.getOrElse(index) { 0 } },
            actionLabel = actionLabel,
        )
    }
}

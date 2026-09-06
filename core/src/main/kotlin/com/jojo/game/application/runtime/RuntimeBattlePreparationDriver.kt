// Runtime
package com.jojo.game.application.runtime

/** RuntimeBattlePreparationDriver: 전투 준비 화면이 표시할 자동 선택·정렬 상태를 공급하는 계약이다. */
interface RuntimeBattlePreparationDriver {
    fun presentation(): BattlePreparationPresentation = BattlePreparationPresentation()
}

/** BattlePreparationPresentation: 준비 화면의 상세·지도·정렬 메뉴 표시 상태를 모은 값이다. */
data class BattlePreparationPresentation(
    val detailsVisible: Boolean = false,
    val mapVisible: Boolean = false,
    val sortMenu: SortMenuState = SortMenuState.CLOSED,
) {
    enum class SortMenuState { CLOSED, OPEN, SELECT_THIRD, CANCELED }
}

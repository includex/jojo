// Runtime
package com.jojo.game.application.runtime

/** RuntimeBattlePreparationDriver: 전투 준비 화면이 표시할 자동 선택·정렬 상태를 공급하는 계약이다. */
interface RuntimeBattlePreparationDriver {
    /**
     * `presentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun presentation(): BattlePreparationPresentation = BattlePreparationPresentation()
}

/** BattlePreparationPresentation: 준비 화면의 상세·지도·정렬 메뉴 표시 상태를 모은 값이다. */
data class BattlePreparationPresentation(
    val detailsVisible: Boolean = false,
    val mapVisible: Boolean = false,
    val sortMenu: SortMenuState = SortMenuState.CLOSED,
) {
    /**
     * `SortMenuState` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    enum class SortMenuState { CLOSED, OPEN, SELECT_THIRD, CANCELED }
}

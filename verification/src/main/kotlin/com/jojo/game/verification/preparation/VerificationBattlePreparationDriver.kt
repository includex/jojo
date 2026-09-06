// Verification
package com.jojo.game.verification.preparation

import com.jojo.game.application.runtime.BattlePreparationPresentation
import com.jojo.game.application.runtime.RuntimeBattlePreparationDriver

/** VerificationBattlePreparationDriver: 검증 전용 시작 경로 이름을 중립적인 준비 화면 사실로 매핑한다. */
internal class VerificationBattlePreparationDriver(
    /** route: 검증 실행 계획을 담는다. */
    private val route: String?,
) : RuntimeBattlePreparationDriver {
    /** presentation: 표현 상태를 검증 출력으로 변환한다. */
    override fun presentation(): BattlePreparationPresentation = when (route) {
        "start-battle-unit-info-fixture" -> BattlePreparationPresentation(detailsVisible = true)
        "battle-view-fixture" -> BattlePreparationPresentation(mapVisible = true)
        "start-battle-sort-open-fixture" -> BattlePreparationPresentation(
            sortMenu = BattlePreparationPresentation.SortMenuState.OPEN,
        )
        "start-battle-sort-select-fixture" -> BattlePreparationPresentation(
            sortMenu = BattlePreparationPresentation.SortMenuState.SELECT_THIRD,
        )
        "start-battle-sort-cancel-fixture" -> BattlePreparationPresentation(
            sortMenu = BattlePreparationPresentation.SortMenuState.CANCELED,
        )
        else -> BattlePreparationPresentation()
    }
}

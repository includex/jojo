package com.jojo.game.verification.preparation

import com.jojo.game.application.runtime.BattlePreparationPresentation
import com.jojo.game.application.runtime.RuntimeBattlePreparationDriver

/** Maps verification-only startup route names to neutral preparation rendering facts. */
internal class VerificationBattlePreparationDriver(
    private val route: String?,
) : RuntimeBattlePreparationDriver {
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

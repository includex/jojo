package com.jojo.game.application.runtime

/** Optional external request for a preparation presentation variant. */
interface RuntimeBattlePreparationDriver {
    fun presentation(): BattlePreparationPresentation = BattlePreparationPresentation()
}

/** Immutable rendering facts supplied before a preparation screen is created. */
data class BattlePreparationPresentation(
    val detailsVisible: Boolean = false,
    val mapVisible: Boolean = false,
    val sortMenu: SortMenuState = SortMenuState.CLOSED,
) {
    enum class SortMenuState { CLOSED, OPEN, SELECT_THIRD, CANCELED }
}

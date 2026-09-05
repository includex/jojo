package com.jojo.game.presentation.scenario.hall

/** Immutable catalog and discovery projection for the Hall treasure panel. */
internal data class HallTreasureView(
    val entries: List<HallTreasureEntryView>,
    val discoveredCount: Int,
    val totalCount: Int,
)

internal data class HallTreasureEntryView(
    val name: String,
    val icon: Int,
    val discovered: Boolean,
)

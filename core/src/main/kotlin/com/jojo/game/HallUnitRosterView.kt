package com.jojo.game

/** Immutable display snapshot of the equipment screen's unit-selection overlay. */
internal data class HallUnitRosterView(val rows: List<HallUnitRosterRowView>)

internal data class HallUnitRosterRowView(
    val name: String,
    val postName: String,
)

package com.jojo.game.presentation.scenario.hall

/** Immutable ForcesListLayer data captured from the campaign before rendering. */
internal data class HallForcesView(
    val rows: List<HallForcesRowView>,
)

internal data class HallForcesRowView(
    val values: List<String>,
)

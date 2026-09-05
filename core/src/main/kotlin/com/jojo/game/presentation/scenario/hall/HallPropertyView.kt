package com.jojo.game.presentation.scenario.hall

/** Ready-to-draw PropertyLayer table snapshot. */
internal data class HallPropertyView(
    val selectedTab: Int,
    val rows: List<HallPropertyRowView>,
)

internal data class HallPropertyRowView(
    val itemId: Int,
    val name: String,
    val icon: Int,
    val typeName: String,
    val level: String,
    val experience: String,
    val owner: String,
)

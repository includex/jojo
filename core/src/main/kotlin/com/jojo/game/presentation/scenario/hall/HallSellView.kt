package com.jojo.game.presentation.scenario.hall

/** Immutable display projection for the Hall sell-management screen. */
internal data class HallSellView(
    val rows: List<HallSellRowView>,
    val money: Int,
    val notice: String?,
)

internal data class HallSellRowView(
    val name: String,
    val icon: Int,
    val primaryDetail: String,
    val secondaryDetail: String?,
    val salePrice: String,
)

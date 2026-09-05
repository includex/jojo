package com.jojo.game

/** Immutable display projection for BuyLayer's left-hand catalog pane. */
internal data class HallBuyCatalogView(
    val propertyTab: Boolean,
    val rows: List<HallBuyCatalogRowView>,
)

internal data class HallBuyCatalogRowView(
    val name: String,
    val icon: Int,
    val typeName: String,
    val inventory: Int,
    val total: Int,
    val price: String,
)

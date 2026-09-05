package com.jojo.game.presentation.scenario.hall

/** Read-only source data for BuyLayer's right-hand unit summary. */
internal data class HallBuyUnitSummaryView(
    val portraitId: Int,
    val name: String,
    val postName: String,
    val level: Int,
    val hitPoints: Int,
    val magicPoints: Int,
    val stats: List<HallBuyUnitSummaryStat>,
)

internal data class HallBuyUnitSummaryStat(
    val name: String,
    val value: Int,
)

package com.jojo.game.presentation.scenario.hall

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.FeatsLayer

/** Immutable data consumed by the hall feature-layer renderers. */
internal data class HallExclusiveView(
    val selectedTab: Tab,
) {
    enum class Tab { SET_LIST, EXCLUSIVE_LIST }

    companion object {
        fun from(layer: ExclusiveLayer) = HallExclusiveView(
            selectedTab = when (layer.selectedTab) {
                ExclusiveLayer.Tab.SET_LIST -> Tab.SET_LIST
                ExclusiveLayer.Tab.EXCLUSIVE_LIST -> Tab.EXCLUSIVE_LIST
            },
        )
    }
}

internal data class HallFeatsView(
    val rows: List<Row>,
    val helpOpen: Boolean,
    val helpText: String,
) {
    internal data class Row(
        val title: String,
        val ability: String,
        val progressRatio: Float,
        val progressLabel: String,
        val phaseLabel: String,
    )

    companion object {
        fun from(layer: FeatsLayer, helpOpen: Boolean): HallFeatsView = HallFeatsView(
            rows = layer.view().rows.map { row ->
                Row(
                    title = row.title,
                    ability = row.ability.toString(),
                    progressRatio = row.progressRatio,
                    progressLabel = row.progressLabel,
                    phaseLabel = row.phaseLabel,
                )
            },
            helpOpen = helpOpen,
            helpText = FeatsLayer.HELP_TEXT,
        )
    }
}

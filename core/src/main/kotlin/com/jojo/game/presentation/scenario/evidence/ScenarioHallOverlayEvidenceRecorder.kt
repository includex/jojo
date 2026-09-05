package com.jojo.game.presentation.scenario.evidence

import com.jojo.game.RenderEventLog

/** Immutable screen projection for source-authored Hall overlay evidence. */
internal data class ScenarioHallOverlayEvidenceInput(
    val fixture: String,
    val featsRows: List<ScenarioHallFeatEvidenceRow>,
    val featsHelpText: String,
    val magic: ScenarioHallMagicEvidence?,
    val modalText: String,
    val items: Map<Int, ScenarioHallOverlayItemEvidence>,
    val postsNames: List<String>,
)

internal data class ScenarioHallFeatEvidenceRow(val title: String, val ability: Int, val phaseLabel: String, val progressRatio: Float, val progressLabel: String)
internal data class ScenarioHallMagicEvidence(val name: String, val power: Int, val cost: Int, val intro: String, val icon: Int, val hit: Int, val eff: Int)
internal data class ScenarioHallOverlayItemEvidence(val name: String, val icon: Int, val typeName: String, val purchasePrice: Int, val intro: String)

internal class ScenarioHallOverlayEvidenceRecorder(private val input: ScenarioHallOverlayEvidenceInput) {
    fun append(log: RenderEventLog) = ScenarioHallOverlayEventWriter(log, input).append()
}

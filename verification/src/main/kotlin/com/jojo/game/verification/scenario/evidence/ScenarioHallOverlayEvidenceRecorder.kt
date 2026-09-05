package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.RenderEventLog

internal class ScenarioHallOverlayEvidenceRecorder(private val input: ScenarioHallOverlayEvidenceInput) {
    fun append(log: RenderEventLog) = ScenarioHallOverlayEventWriter(log, input).append()
}

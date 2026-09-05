package com.jojo.game.presentation.scenario.evidence

/** Immutable presentation projection consumed by Scenario evidence recorders. */
internal data class ScenarioEvidenceView(
    val moduleName: String,
    val playbackState: String,
    val backgroundId: Int,
    val units: List<ScenarioEvidenceUnit>,
    val heads: List<ScenarioEvidenceHead>,
    val dialogue: ScenarioEvidenceDialogue?,
    val modal: ScenarioEvidenceModal?,
    val hallMenu: ScenarioEvidenceHallMenu?,
    val hallCommandVisible: Boolean,
    val hallManagement: ScenarioEvidenceHallManagement?,
    val hallInfo: ScenarioEvidenceHallInfo?,
)

internal data class ScenarioEvidenceUnit(
    val id: Int,
    val scriptX: Float,
    val scriptY: Float,
    val direction: Int,
    val action: Int,
    val avatarId: Int,
)

internal data class ScenarioEvidenceHead(
    val characterId: Int,
    val scriptX: Float,
    val scriptY: Float,
    val opacity: Float,
)

internal data class ScenarioEvidenceDialogue(
    val side: Int,
    val atTop: Boolean,
    val speakerId: Int?,
    val visibleText: String,
)

internal data class ScenarioEvidenceModal(val kind: String, val text: String)

internal data class ScenarioEvidenceHallMenu(
    val ambitionFrom: Int,
    val ambitionTo: Int,
    val displayedAmbition: Float,
)

internal enum class ScenarioEvidenceHallManagement { EQUIP, BUY, SELL }

internal data class ScenarioEvidenceHallInfo(
    val kind: String,
    val contentRects: List<ScenarioEvidenceRect>,
)

internal data class ScenarioEvidenceRect(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
)

/** Immutable selector projected by ScenarioScreen for authored hall-info evidence. */
internal enum class ScenarioStaticHallEvidenceKind { PROPERTY, TERRAIN, TREASURE }

/**
 * Deliberately small boundary for scene-authored hall evidence.
 *
 * The three fixtures contain no runtime screen state; the kind is all a writer
 * needs to choose the recovered node traversal.
 */
internal data class ScenarioStaticHallEvidenceView(
    val kind: ScenarioStaticHallEvidenceKind,
)

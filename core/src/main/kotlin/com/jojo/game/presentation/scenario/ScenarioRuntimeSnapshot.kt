package com.jojo.game.presentation.scenario

import com.jojo.game.application.runtime.RuntimeScenarioOverlay

/**
 * Immutable renderer observation.  It deliberately contains presentation data
 * only; formatting it as an artifact is a verification concern.
 */
data class ScenarioRuntimeSnapshot(
    val frame: ScenarioFrameEvidenceInput,
    val composition: ScenarioEvidenceView,
)

data class ScenarioEvidenceView(val moduleName: String, val playbackState: String, val backgroundId: Int, val units: List<ScenarioEvidenceUnit>, val heads: List<ScenarioEvidenceHead>, val dialogue: ScenarioEvidenceDialogue?, val modal: ScenarioEvidenceModal?, val hallMenu: ScenarioEvidenceHallMenu?, val hallCommandVisible: Boolean, val hallManagement: ScenarioEvidenceHallManagement?, val hallInfo: ScenarioEvidenceHallInfo?)
data class ScenarioEvidenceUnit(val id: Int, val scriptX: Float, val scriptY: Float, val direction: Int, val action: Int, val avatarId: Int)
data class ScenarioEvidenceHead(val characterId: Int, val scriptX: Float, val scriptY: Float, val opacity: Float)
data class ScenarioEvidenceDialogue(val side: Int, val atTop: Boolean, val speakerId: Int?, val visibleText: String)
data class ScenarioEvidenceModal(val kind: String, val text: String)
data class ScenarioEvidenceHallMenu(val ambitionFrom: Int, val ambitionTo: Int, val displayedAmbition: Float)
enum class ScenarioEvidenceHallManagement { EQUIP, BUY, SELL }
data class ScenarioEvidenceHallInfo(val kind: String, val contentRects: List<ScenarioEvidenceRect>)
data class ScenarioEvidenceRect(val x: Float, val y: Float, val width: Float, val height: Float)
enum class ScenarioStaticHallEvidenceKind { PROPERTY, TERRAIN, TREASURE }
data class ScenarioStaticHallEvidenceView(val kind: ScenarioStaticHallEvidenceKind)

data class ScenarioFrameUnitEvidence(val id: Int, val visualX: Float, val visualY: Float, val direction: Int, val avatar: Int)
data class ScenarioFrameBackgroundEvidence(val id: Int, val equipFixture: Boolean)
enum class ScenarioFrameHallInfo { FORCES, HELPER, PROPERTY, TERRAIN, TREASURE }
data class ScenarioFrameEvidenceInput(val variant: RuntimeScenarioOverlay?, val palace: Boolean, val section: Boolean, val street: ScenarioStoryEvidenceView.StreetDialogue?, val overlay: ScenarioHallOverlayEvidenceInput?, val hallInfo: ScenarioFrameHallInfo?, val background: ScenarioFrameBackgroundEvidence, val units: List<ScenarioFrameUnitEvidence>, val management: ScenarioHallManagementEvidenceInput?, val equip: ScenarioHallEquipEvidenceInput?, val unitList: List<ScenarioHallUnitListEvidenceRow>?, val confirmation: ScenarioEquipConfirmationEvidenceView?, val commandVisible: Boolean)

data class ScenarioHallOverlayEvidenceInput(val variant: RuntimeScenarioOverlay, val featsRows: List<ScenarioHallFeatEvidenceRow>, val featsHelpText: String, val magic: ScenarioHallMagicEvidence?, val modalText: String, val items: Map<Int, ScenarioHallOverlayItemEvidence>, val postsNames: List<String>)
data class ScenarioHallFeatEvidenceRow(val title: String, val ability: Int, val phaseLabel: String, val progressRatio: Float, val progressLabel: String)
data class ScenarioHallMagicEvidence(val name: String, val power: Int, val cost: Int, val intro: String, val icon: Int, val hit: Int, val eff: Int)
data class ScenarioHallOverlayItemEvidence(val name: String, val icon: Int, val typeName: String, val purchasePrice: Int, val intro: String)

data class ScenarioHallEquipEvidenceSlot(val name: String, val level: Int, val experience: Int, val icon: Int?)
data class ScenarioHallEquipEvidenceInput(val variant: RuntimeScenarioOverlay?, val unitName: String, val postsName: String, val faceFrame: Int, val level: Int, val stats: List<Int>, val slots: List<ScenarioHallEquipEvidenceSlot>)
enum class ScenarioHallManagementEvidenceKind { BUY, SELL }
data class ScenarioHallManagementBuyRow(val name: String, val typeName: String, val inventoryCount: Int, val purchasePrice: Int)
data class ScenarioHallManagementEquipment(val name: String, val level: Int)
data class ScenarioHallManagementUnitEvidence(val name: String, val postsName: String, val level: Int, val stats: List<Int>, val weapon: ScenarioHallManagementEquipment?)
data class ScenarioHallManagementEvidenceInput(val kind: ScenarioHallManagementEvidenceKind, val money: Int, val buyRows: List<ScenarioHallManagementBuyRow>, val unit: ScenarioHallManagementUnitEvidence)
data class ScenarioHallUnitListEvidenceRow(val name: String, val posts: String)
data class ScenarioEquipConfirmationEvidenceView(val variant: RuntimeScenarioOverlay?, val values: List<Int>, val actionLabel: String)
sealed interface ScenarioStoryEvidenceView {
    data object Palace : ScenarioStoryEvidenceView
    data object Section : ScenarioStoryEvidenceView
    data class StreetDialogue(val stage: String, val dialogueVisible: Boolean, val visibleText: String, val speakerName: String) : ScenarioStoryEvidenceView
}

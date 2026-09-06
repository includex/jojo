// 시나리오 실행 증거 스냅샷 모델
package com.jojo.game.presentation.scenario

import com.jojo.game.application.runtime.RuntimeScenarioOverlay

/** ScenarioRuntimeSnapshot: 프레임 관측 입력과 화면 구성 증거를 함께 보관하는 검증용 불변 스냅샷이다. */
data class ScenarioRuntimeSnapshot(
    /** renderer 호출 전의 현재 프레임 입력이다. */
    val frame: ScenarioFrameEvidenceInput,
    /** 화면에 실제로 조립된 장면·대화·거점 구성 값이다. */
    val composition: ScenarioEvidenceView,
)

/** ScenarioEvidenceView: runtime 시나리오의 장면 요소를 직렬 비교 가능한 값으로 평탄화한다. */
data class ScenarioEvidenceView(val moduleName: String, val playbackState: String, val backgroundId: Int, val units: List<ScenarioEvidenceUnit>, val heads: List<ScenarioEvidenceHead>, val dialogue: ScenarioEvidenceDialogue?, val modal: ScenarioEvidenceModal?, val hallMenu: ScenarioEvidenceHallMenu?, val hallCommandVisible: Boolean, val hallManagement: ScenarioEvidenceHallManagement?, val hallInfo: ScenarioEvidenceHallInfo?)
/** ScenarioEvidenceUnit: 한 시나리오 유닛의 스크립트 좌표·방향·스프라이트 식별값이다. */
data class ScenarioEvidenceUnit(val id: Int, val scriptX: Float, val scriptY: Float, val direction: Int, val action: Int, val avatarId: Int)
/** ScenarioEvidenceHead: 대화 초상화의 인물·위치·투명도 관측값이다. */
data class ScenarioEvidenceHead(val characterId: Int, val scriptX: Float, val scriptY: Float, val opacity: Float)
/** ScenarioEvidenceDialogue: 현재 대사창의 화자 방향과 공개된 본문이다. */
data class ScenarioEvidenceDialogue(val side: Int, val atTop: Boolean, val speakerId: Int?, val visibleText: String)
/** ScenarioEvidenceModal: 활성 모달의 종류와 화면 문구다. */
data class ScenarioEvidenceModal(val kind: String, val text: String)
/**
 * `ScenarioEvidenceHallMenu`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioEvidenceHallMenu(val ambitionFrom: Int, val ambitionTo: Int, val displayedAmbition: Float)
/**
 * `ScenarioEvidenceHallManagement`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class ScenarioEvidenceHallManagement { EQUIP, BUY, SELL }
/**
 * `ScenarioEvidenceHallInfo`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioEvidenceHallInfo(val kind: String, val contentRects: List<ScenarioEvidenceRect>)
/**
 * `ScenarioEvidenceRect`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioEvidenceRect(val x: Float, val y: Float, val width: Float, val height: Float)
/**
 * `ScenarioStaticHallEvidenceKind`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class ScenarioStaticHallEvidenceKind { PROPERTY, TERRAIN, TREASURE }
/**
 * `ScenarioStaticHallEvidenceView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioStaticHallEvidenceView(val kind: ScenarioStaticHallEvidenceKind)

/**
 * `ScenarioFrameUnitEvidence`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioFrameUnitEvidence(val id: Int, val visualX: Float, val visualY: Float, val direction: Int, val avatar: Int)
/**
 * `ScenarioFrameBackgroundEvidence`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioFrameBackgroundEvidence(val id: Int, val equipFixture: Boolean)
/**
 * `ScenarioFrameHallInfo`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class ScenarioFrameHallInfo { FORCES, HELPER, PROPERTY, TERRAIN, TREASURE }
/**
 * `ScenarioFrameEvidenceInput`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioFrameEvidenceInput(val variant: RuntimeScenarioOverlay?, val palace: Boolean, val section: Boolean, val street: ScenarioStoryEvidenceView.StreetDialogue?, val overlay: ScenarioHallOverlayEvidenceInput?, val hallInfo: ScenarioFrameHallInfo?, val background: ScenarioFrameBackgroundEvidence, val units: List<ScenarioFrameUnitEvidence>, val management: ScenarioHallManagementEvidenceInput?, val equip: ScenarioHallEquipEvidenceInput?, val unitList: List<ScenarioHallUnitListEvidenceRow>?, val confirmation: ScenarioEquipConfirmationEvidenceView?, val commandVisible: Boolean)

/**
 * `ScenarioHallOverlayEvidenceInput`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallOverlayEvidenceInput(val variant: RuntimeScenarioOverlay, val featsRows: List<ScenarioHallFeatEvidenceRow>, val featsHelpText: String, val magic: ScenarioHallMagicEvidence?, val modalText: String, val items: Map<Int, ScenarioHallOverlayItemEvidence>, val postsNames: List<String>)
/**
 * `ScenarioHallFeatEvidenceRow`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallFeatEvidenceRow(val title: String, val ability: Int, val phaseLabel: String, val progressRatio: Float, val progressLabel: String)
/**
 * `ScenarioHallMagicEvidence`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallMagicEvidence(val name: String, val power: Int, val cost: Int, val intro: String, val icon: Int, val hit: Int, val eff: Int)
/**
 * `ScenarioHallOverlayItemEvidence`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallOverlayItemEvidence(val name: String, val icon: Int, val typeName: String, val purchasePrice: Int, val intro: String)

/**
 * `ScenarioHallEquipEvidenceSlot`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallEquipEvidenceSlot(val name: String, val level: Int, val experience: Int, val icon: Int?)
/**
 * `ScenarioHallEquipEvidenceInput`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallEquipEvidenceInput(val variant: RuntimeScenarioOverlay?, val unitName: String, val postsName: String, val faceFrame: Int, val level: Int, val stats: List<Int>, val slots: List<ScenarioHallEquipEvidenceSlot>)
/**
 * `ScenarioHallManagementEvidenceKind`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class ScenarioHallManagementEvidenceKind { BUY, SELL }
/**
 * `ScenarioHallManagementBuyRow`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallManagementBuyRow(val name: String, val typeName: String, val inventoryCount: Int, val purchasePrice: Int)
/**
 * `ScenarioHallManagementEquipment`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallManagementEquipment(val name: String, val level: Int)
/**
 * `ScenarioHallManagementUnitEvidence`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallManagementUnitEvidence(val name: String, val postsName: String, val level: Int, val stats: List<Int>, val weapon: ScenarioHallManagementEquipment?)
/**
 * `ScenarioHallManagementEvidenceInput`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallManagementEvidenceInput(val kind: ScenarioHallManagementEvidenceKind, val money: Int, val buyRows: List<ScenarioHallManagementBuyRow>, val unit: ScenarioHallManagementUnitEvidence)
/**
 * `ScenarioHallUnitListEvidenceRow`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioHallUnitListEvidenceRow(val name: String, val posts: String)
/**
 * `ScenarioEquipConfirmationEvidenceView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class ScenarioEquipConfirmationEvidenceView(val variant: RuntimeScenarioOverlay?, val values: List<Int>, val actionLabel: String)
/**
 * `ScenarioStoryEvidenceView`: 관련 상태와 동작을 묶는 interface다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

sealed interface ScenarioStoryEvidenceView {
    /**
     * `Palace`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object Palace : ScenarioStoryEvidenceView
    /**
     * `Section`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object Section : ScenarioStoryEvidenceView
    /**
     * `StreetDialogue`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class StreetDialogue(val stage: String, val dialogueVisible: Boolean, val visibleText: String, val speakerName: String) : ScenarioStoryEvidenceView
}

// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.domain.scenario.PlaybackState

/** RuntimeScenarioDriver: 시나리오 프레임을 읽고 대화·선택·오버레이 자동 조작을 생성하는 전략 계약이다. */
fun interface RuntimeScenarioDriver {
    fun commands(frame: RuntimeScenarioFrame): List<RuntimeScenarioCommand>
}

/** RuntimeScenarioFrame: 자동 시나리오 구동 판단에 필요한 모듈·경과 시간·입력 대기 상태다. */
data class RuntimeScenarioFrame(
    val module: String,
    val elapsedSeconds: Float,
    val playback: PlaybackState,
    val choiceAvailable: Boolean,
)

/** RuntimeScenarioCommand: 자동 시나리오 구동기가 화면에 전달하는 진행·표시 명령의 공통 타입이다. */
sealed interface RuntimeScenarioCommand {
    /** Present: 지정 시간만큼 현재 시나리오 화면을 유지하도록 요청하는 명령이다. */
    data class Present(
        val presentation: RuntimeScenarioPresentation,
        val detail: Int = -1,
        val scene: RuntimeScenarioScene = RuntimeScenarioScene(),
    ) : RuntimeScenarioCommand

    /** ShowOverlay: 검증할 시나리오 오버레이를 화면에 열도록 요청하는 명령이다. */
    data class ShowOverlay(
        val overlay: RuntimeScenarioOverlay,
        val scene: RuntimeScenarioScene = RuntimeScenarioScene(),
    ) : RuntimeScenarioCommand

    /** SetPresentation: 화면 배경과 연출 방식을 지정한 표현 유형으로 바꾸는 명령이다. */
    data class SetPresentation(
        val mode: RuntimeScenarioPresentation,
        val detail: Int = -1,
    ) : RuntimeScenarioCommand
    data object AdvanceDialogue : RuntimeScenarioCommand
    data object ResumeModal : RuntimeScenarioCommand
    data object SkipDelay : RuntimeScenarioCommand
    data object ConfirmChoice : RuntimeScenarioCommand
    data object RevealDialogue : RuntimeScenarioCommand
}

/** RuntimeScenarioPresentation: 자동 재생에서 선택할 시나리오 장면 표현 유형이다. */
enum class RuntimeScenarioPresentation { STANDARD, STREET, PALACE, SECTION }

/** RuntimeScenarioOverlay: 자동 재생 중 열 수 있는 시나리오 보조 화면의 종류다. */
enum class RuntimeScenarioOverlay {
    HALL,
    INFO,
    GET_ITEM_EQUIPMENT,
    GET_ITEM_PROPERTY,
    ITEM_EQUIPMENT,
    ITEM_PROPERTY,
    ITEM_DISCARD_CONFIRM,
    CHOICE,
    MAP_INFO,
    AMBITION,
    ASK,
    COMMAND,
    MENU,
    SAVE,
    SAVE_CONFIRM,
    EQUIP,
    UNIT_LIST,
    UNIT_LIST_SELECT,
    UNIT_LIST_CLOSE,
    EQUIP_CONFIRM,
    EQUIP_CONFIRM_UNLOAD,
    EXCLUSIVE,
    EXCLUSIVE_TAB1,
    MAGIC,
    FEATS,
    FEATS_HELP,
    BUY,
    SELL,
    FORCES,
    PROPERTY,
    TERRAIN,
    TREASURE,
    HELPER,
    SKIP_OPEN,
}

/** RuntimeScenarioScene: 자동 재생이 현재 보여 줄 배경·유닛·대화·모달의 조합이다. */
data class RuntimeScenarioScene(
    val backgroundId: Int? = null,
    val units: List<RuntimeScenarioUnit> = emptyList(),
    val dialogueText: String? = null,
    val modal: RuntimeScenarioModal? = null,
)

/** RuntimeScenarioUnit: 자동 시나리오 장면에 배치할 유닛의 식별자·좌표·방향이다. */
data class RuntimeScenarioUnit(
    val id: Int,
    val x: Int,
    val y: Int,
    val direction: Int,
)

/** RuntimeScenarioModal: 자동 장면에 표시할 모달의 종류·문구·유지 시간을 나타낸다. */
data class RuntimeScenarioModal(
    val kind: String,
    val text: String,
    val seconds: Float = 5f,
)

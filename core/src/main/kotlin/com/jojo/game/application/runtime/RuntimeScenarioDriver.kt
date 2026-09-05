package com.jojo.game.application.runtime

import com.jojo.game.domain.scenario.PlaybackState

/** External scenario input source; core owns validation and command application. */
fun interface RuntimeScenarioDriver {
    fun commands(frame: RuntimeScenarioFrame): List<RuntimeScenarioCommand>
}

/** Immutable scenario state available to an external driver. */
data class RuntimeScenarioFrame(
    val module: String,
    val elapsedSeconds: Float,
    val playback: PlaybackState,
    val choiceAvailable: Boolean,
)

/** Bounded, input-equivalent playback commands. */
sealed interface RuntimeScenarioCommand {
    /** Supplies a complete, renderer-neutral scene projection. */
    data class Present(
        val presentation: RuntimeScenarioPresentation,
        val detail: Int = -1,
        val scene: RuntimeScenarioScene = RuntimeScenarioScene(),
    ) : RuntimeScenarioCommand

    /** Selects an externally-owned Hall projection.  The enum is deliberately
     * not tied to launch/capture route spelling. */
    data class ShowOverlay(
        val overlay: RuntimeScenarioOverlay,
        val scene: RuntimeScenarioScene = RuntimeScenarioScene(),
    ) : RuntimeScenarioCommand

    /** Kept for source compatibility with the first neutral-driver API. */
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

/** Renderer-neutral visual mode selected by an external runtime. */
enum class RuntimeScenarioPresentation { STANDARD, STREET, PALACE, SECTION }

/** Renderer-neutral Hall projection selected by an external runtime. */
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

/** Small presentation payload used by verification and other external runtimes. */
data class RuntimeScenarioScene(
    val backgroundId: Int? = null,
    val units: List<RuntimeScenarioUnit> = emptyList(),
    val dialogueText: String? = null,
    val modal: RuntimeScenarioModal? = null,
)

data class RuntimeScenarioUnit(
    val id: Int,
    val x: Int,
    val y: Int,
    val direction: Int,
)

data class RuntimeScenarioModal(
    val kind: String,
    val text: String,
    val seconds: Float = 5f,
)

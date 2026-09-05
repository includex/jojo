package com.jojo.game.application.runtime

/** Immutable frame input exposed to an optional external battle driver. */
data class RuntimeBattleFrame(
    val delta: Float,
    val elapsed: Float,
)

/** Stable presentation choices supplied by a runtime integration. */
enum class RuntimeBattleRoute {
    NONE,
    REWARD_BASIC, REWARD_CARD1, REWARD_CARD2, ITEM_UPGRADE, LOSE_RESTART,
    ROUND_NORMAL, ROUND_FINAL, ROUND_ENEMY,
    WIN_COMPACT, WIN_FULL,
    MINI_MAP_SHOWN, MINI_MAP_HIDDEN,
    AUTO_PROMPT_OFF, AUTO_PROMPT_ON, AUTO_ACTIVE,
    COMMAND_INITIAL, COMMAND_DISABLED, COMMAND_CANCEL, COMMAND_MAGICK, COMMAND_PROPERTY,
    CHARACTER_HP_CAMPS, CHARACTER_OUTLINE, CHARACTER_HIT, CHARACTER_CLEANUP,
    CHARACTER_DEATH_ACTION, CHARACTER_DEATH_HIDDEN,
    EDIT_INITIAL, EDIT_WEATHER, EDIT_ROUND, EDIT_APPLY, EDIT_CHILD, EDIT_CHILD_SCENE, EDIT_REGISTER,
    OTHER_UNIT_INFO, MINE_UNIT_INFO,
    ACTION_6_F0, ACTION_6_F1, ACTION_6_F2, ACTION_6_F3,
    ACTION_25_F0, ACTION_25_F1, ACTION_25_F2, ACTION_25_F3,
    ACTION_48_F0, ACTION_48_F1, ACTION_48_F2, ACTION_48_F3,
    CUTSCENE_ATTACK, CUTSCENE_POST_HIT, CUTSCENE_477,
    DIALOGUE_BLEND, INITIAL, TERRAIN, MENU,
    DIALOGUE_COMPONENT_BACKGROUND, DIALOGUE_COMPONENT_CHARACTERS,
    DIALOGUE_COMPONENT_LABELS, DIALOGUE_COMPONENT_DIALOGUE,
    MAP_ONLY, SELECTION, MODAL_TERRAIN, MODAL_PROPERTY, MODAL_TREASURE,
    MODAL_SETTING, MODAL_SAVE, MODAL_LOAD, MODAL_FORCES,
    JIQI, MAGICK_LIST, MAGICK_DETAIL,
    USE_PROPERTY_LIST, USE_PROPERTY_DETAIL, USE_PROPERTY_SELECT, USE_PROPERTY_CANCEL,
}

data class RuntimeBattleActionSample(val action: Int, val sample: Float)

/** Immutable runtime-selected presentation data; no route parsing belongs in core. */
data class RuntimeBattlePresentation(
    val route: RuntimeBattleRoute = RuntimeBattleRoute.NONE,
    val actionSample: RuntimeBattleActionSample? = null,
    val dialogueStep: Int? = null,
)

/** Neutral commands an external runtime may request after observing a frame. */
sealed interface RuntimeBattleCommand {
    data object AdvanceDialogue : RuntimeBattleCommand
    data class Tap(val x: Float, val y: Float) : RuntimeBattleCommand
    data object EndTurn : RuntimeBattleCommand
}

/** Optional external input driver; ordinary gameplay remains driver-free. */
fun interface RuntimeBattleDriver {
    fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand>
}

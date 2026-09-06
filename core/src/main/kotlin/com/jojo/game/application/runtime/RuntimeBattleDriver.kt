// Runtime
package com.jojo.game.application.runtime

/** RuntimeBattleFrame: 자동 전투 명령을 계산할 때 전달하는 현재 프레임의 경과 시간이다. */
data class RuntimeBattleFrame(
    val delta: Float,
    val elapsed: Float,
)

/** RuntimeBattleRoute: 자동 전투 재생이 따를 입력 시나리오의 종류를 구분한다. */
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
    DIALOGUE_BLEND, INITIAL, TERRAIN, MENU, HELPER, WIN_MODAL, UNIT_INFO, RESULT_LOSE, RESULT_WIN,
    OPENING_SAY, HUD, DIALOGUE_ONE, ENEMY_TURN,
    DIALOGUE_COMPONENT_BACKGROUND, DIALOGUE_COMPONENT_CHARACTERS,
    DIALOGUE_COMPONENT_LABELS, DIALOGUE_COMPONENT_DIALOGUE,
    MAP_ONLY, SELECTION, MODAL_TERRAIN, MODAL_PROPERTY, MODAL_TREASURE,
    MODAL_SETTING, MODAL_SAVE, MODAL_LOAD, MODAL_FORCES,
    JIQI, MAGICK_LIST, MAGICK_DETAIL,
    USE_PROPERTY_LIST, USE_PROPERTY_DETAIL, USE_PROPERTY_SELECT, USE_PROPERTY_CANCEL,
}

/** RuntimeBattleActionSample: 지정 행동을 재현할 애니메이션 시간 표본이다. */
data class RuntimeBattleActionSample(val action: Int, val sample: Float)

/** RuntimeBattlePresentation: 자동 구동 중 화면에 보여 줄 전투 경로·행동·대화 위치를 묶는다. */
data class RuntimeBattlePresentation(
    val route: RuntimeBattleRoute = RuntimeBattleRoute.NONE,
    val actionSample: RuntimeBattleActionSample? = null,
    val dialogueStep: Int? = null,
)

/** RuntimeBattleCommand: 자동 구동기가 전투 화면에 전달하는 조작 명령의 공통 타입이다. */
sealed interface RuntimeBattleCommand {
    data object AdvanceDialogue : RuntimeBattleCommand
    data class Tap(val x: Float, val y: Float) : RuntimeBattleCommand
    data object EndTurn : RuntimeBattleCommand
}

/** RuntimeBattleDriver: 프레임과 전장 탐침을 읽어 다음 자동 조작 명령을 결정하는 전략 계약이다. */
fun interface RuntimeBattleDriver {
    fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand>
}

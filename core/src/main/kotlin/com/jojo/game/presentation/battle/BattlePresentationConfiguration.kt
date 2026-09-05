package com.jojo.game.presentation.battle

import com.jojo.game.application.runtime.RuntimeBattleActionSample
import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.edit.BattleEditLayer2Route
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState

/**
 * Converts an immutable runtime presentation selection into screen decisions.
 * Route parsing and external naming stay at the runtime boundary.
 */
internal class BattlePresentationConfiguration(
    private val presentation: RuntimeBattlePresentation,
) {
    private val route get() = presentation.route
    val rewardRouteState = route.takeIf { it in REWARD_ROUTES }
    val itemUpgradeRouteState = route.takeIf { it == RuntimeBattleRoute.ITEM_UPGRADE }
    val loseRestartRoute = route == RuntimeBattleRoute.LOSE_RESTART
    val roundRouteState = route.takeIf { it in ROUND_ROUTES }
    val winConditionRouteState = route.takeIf { it in WIN_ROUTES }
    val miniMapRouteState = route.takeIf { it in MINI_MAP_ROUTES }
    val autoBattleRouteState = route.takeIf { it in AUTO_ROUTES }
    val battleCommandRouteState = route.takeIf { it in COMMAND_ROUTES }
    val battleCharacterRouteState = when (route) {
        RuntimeBattleRoute.CHARACTER_HP_CAMPS -> BattleCharacterStrictState.HP_CAMPS_PARTIAL
        RuntimeBattleRoute.CHARACTER_OUTLINE -> BattleCharacterStrictState.OUTLINE_HIGHLIGHT
        RuntimeBattleRoute.CHARACTER_HIT -> BattleCharacterStrictState.HIT_IMPACT
        RuntimeBattleRoute.CHARACTER_CLEANUP -> BattleCharacterStrictState.CLEANUP
        RuntimeBattleRoute.CHARACTER_DEATH_ACTION -> BattleCharacterStrictState.DEATH_ACTION
        RuntimeBattleRoute.CHARACTER_DEATH_HIDDEN -> BattleCharacterStrictState.DEATH_HIDDEN
        else -> null
    }
    val battleEdit2RouteState = when (route) {
        RuntimeBattleRoute.EDIT_INITIAL -> BattleEditLayer2Route.INITIAL
        RuntimeBattleRoute.EDIT_WEATHER -> BattleEditLayer2Route.WEATHER
        RuntimeBattleRoute.EDIT_ROUND -> BattleEditLayer2Route.ROUND
        RuntimeBattleRoute.EDIT_APPLY -> BattleEditLayer2Route.APPLY
        RuntimeBattleRoute.EDIT_CHILD -> BattleEditLayer2Route.CHILD
        RuntimeBattleRoute.EDIT_CHILD_SCENE -> BattleEditLayer2Route.CHILD_SCENE
        RuntimeBattleRoute.EDIT_REGISTER -> BattleEditLayer2Route.REGISTER
        else -> null
    }
    val otherUnitInfoRoute = route == RuntimeBattleRoute.OTHER_UNIT_INFO
    val mineUnitInfoRoute = route == RuntimeBattleRoute.MINE_UNIT_INFO
    val actionSample: RuntimeBattleActionSample? = presentation.actionSample
    val actionSampleMode get() = actionSample != null
    val cutsceneAttackCapture = route == RuntimeBattleRoute.CUTSCENE_ATTACK
    val cutscenePostHitCapture = route == RuntimeBattleRoute.CUTSCENE_POST_HIT
    val cutscene477Capture = route == RuntimeBattleRoute.CUTSCENE_477
    val battleDialogueBlendRoute = route == RuntimeBattleRoute.DIALOGUE_BLEND
    val battleInitRoute = route == RuntimeBattleRoute.INITIAL
    val battleTerrainRoute = route == RuntimeBattleRoute.TERRAIN
    val battleMenuRoute = route == RuntimeBattleRoute.MENU
    val dialogueStepCapture: Int? = presentation.dialogueStep
    val dialogueComponentStage = route.takeIf { it in DIALOGUE_COMPONENT_ROUTES }
    val mapOnlyCapture = route == RuntimeBattleRoute.MAP_ONLY
    val selectionOverlayCapture = route == RuntimeBattleRoute.SELECTION
    val modalRenderCapture = route in MODAL_ROUTES
    val jiqiRoute = route == RuntimeBattleRoute.JIQI
    val magickRoute = route.takeIf { it == RuntimeBattleRoute.MAGICK_LIST || it == RuntimeBattleRoute.MAGICK_DETAIL }
    val usePropertyRoute = route.takeIf { it in USE_PROPERTY_ROUTES }

    fun animationClock(elapsed: Float, battleElapsed: Float): Float = when {
        rewardRouteState != null || winConditionRouteState != null -> 0f
        actionSampleMode -> elapsed
        else -> battleElapsed
    }

    fun mapObjectAnimationClock(battleElapsed: Float): Float = when {
        rewardRouteState != null || winConditionRouteState != null -> 0f
        else -> battleElapsed
    }

    companion object {
        private val REWARD_ROUTES = setOf(
            RuntimeBattleRoute.REWARD_BASIC,
            RuntimeBattleRoute.REWARD_CARD1,
            RuntimeBattleRoute.REWARD_CARD2,
        )
        private val ROUND_ROUTES = setOf(
            RuntimeBattleRoute.ROUND_NORMAL,
            RuntimeBattleRoute.ROUND_FINAL,
            RuntimeBattleRoute.ROUND_ENEMY,
        )
        private val WIN_ROUTES = setOf(RuntimeBattleRoute.WIN_COMPACT, RuntimeBattleRoute.WIN_FULL)
        private val MINI_MAP_ROUTES = setOf(RuntimeBattleRoute.MINI_MAP_SHOWN, RuntimeBattleRoute.MINI_MAP_HIDDEN)
        private val AUTO_ROUTES = setOf(
            RuntimeBattleRoute.AUTO_PROMPT_OFF,
            RuntimeBattleRoute.AUTO_PROMPT_ON,
            RuntimeBattleRoute.AUTO_ACTIVE,
        )
        private val COMMAND_ROUTES = setOf(
            RuntimeBattleRoute.COMMAND_INITIAL,
            RuntimeBattleRoute.COMMAND_DISABLED,
            RuntimeBattleRoute.COMMAND_CANCEL,
            RuntimeBattleRoute.COMMAND_MAGICK,
            RuntimeBattleRoute.COMMAND_PROPERTY,
        )
        private val DIALOGUE_COMPONENT_ROUTES = setOf(
            RuntimeBattleRoute.DIALOGUE_COMPONENT_BACKGROUND,
            RuntimeBattleRoute.DIALOGUE_COMPONENT_CHARACTERS,
            RuntimeBattleRoute.DIALOGUE_COMPONENT_LABELS,
            RuntimeBattleRoute.DIALOGUE_COMPONENT_DIALOGUE,
        )
        private val MODAL_ROUTES = setOf(
            RuntimeBattleRoute.MODAL_TERRAIN,
            RuntimeBattleRoute.MODAL_PROPERTY,
            RuntimeBattleRoute.MODAL_TREASURE,
            RuntimeBattleRoute.MODAL_SETTING,
            RuntimeBattleRoute.MODAL_SAVE,
            RuntimeBattleRoute.MODAL_LOAD,
            RuntimeBattleRoute.MODAL_FORCES,
        )
        private val USE_PROPERTY_ROUTES = setOf(
            RuntimeBattleRoute.USE_PROPERTY_LIST,
            RuntimeBattleRoute.USE_PROPERTY_DETAIL,
            RuntimeBattleRoute.USE_PROPERTY_SELECT,
            RuntimeBattleRoute.USE_PROPERTY_CANCEL,
        )
    }
}

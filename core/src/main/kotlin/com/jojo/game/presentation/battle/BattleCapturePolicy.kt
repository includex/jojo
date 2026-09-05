package com.jojo.game.presentation.battle

import com.jojo.game.presentation.battle.edit.BattleEditLayer2Route
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState

/**
 * Capture/fixture route classification kept outside the live battle state.
 * The policy is inert when no requested state is supplied; gameplay callers
 * can therefore use the same screen without importing verification concerns.
 */
internal class BattlePresentationRoutePolicy(private val requestedState: String?) {
    val rewardRouteState: String? = requestedState?.takeIf { it in REWARD_ROUTE_STATES }
    val itemUpgradeRouteState: String? = requestedState?.takeIf { it == ITEM_UPGRADE_ROUTE_STATE }
    val loseRestartRoute: Boolean = requestedState == LOSE_RESTART_ROUTE_STATE
    val roundRouteState: String? = requestedState?.takeIf { it in ROUND_ROUTE_STATES }
    val winConditionRouteState: String? = requestedState?.takeIf { it in WIN_CONDITION_ROUTE_STATES }
    val miniMapRouteState: String? = requestedState?.takeIf { it in MINI_MAP_ROUTE_STATES }
    val autoBattleRouteState: String? = requestedState?.takeIf { it in AUTO_BATTLE_ROUTE_STATES }
    val battleCommandRouteState: String? = requestedState?.takeIf { it in BATTLE_COMMAND_ROUTE_STATES }
    val battleCharacterRouteState: BattleCharacterStrictState? = parseBattleCharacterRoute(requestedState)
    val battleEdit2RouteState: BattleEditLayer2Route? = BattleEditLayer2Route.parse(requestedState)
    val otherUnitInfoRoute: Boolean = requestedState == "battle-other-unit-info-fixture"
    val mineUnitInfoRoute: Boolean = requestedState == "battle-mine-unit-info-fixture"

    val actionSample: CaptureActionSample? = when (requestedState) {
        "attack6-f0" -> CaptureActionSample(6, 1f / 24f)
        "attack6-f1" -> CaptureActionSample(6, 7f / 24f)
        "attack6-f2" -> CaptureActionSample(6, 9f / 24f)
        "attack6-f3" -> CaptureActionSample(6, 11f / 24f)
        "attack25-f0" -> CaptureActionSample(25, 1f / 24f)
        "attack25-f1" -> CaptureActionSample(25, 10f / 24f)
        "attack25-f2" -> CaptureActionSample(25, 12f / 24f)
        "attack25-f3" -> CaptureActionSample(25, 14f / 24f)
        "attack48-f0" -> CaptureActionSample(48, 1f / 24f)
        "attack48-f1" -> CaptureActionSample(48, 19f / 24f)
        "attack48-f2" -> CaptureActionSample(48, 21f / 24f)
        "attack48-f3" -> CaptureActionSample(48, 23f / 24f)
        else -> null
    }
    val cutsceneAttackCapture = requestedState == "yingchuan-attack"
    val cutscenePostHitCapture = requestedState == "yingchuan-action4"
    val cutscene477Capture = requestedState == "yingchuan-477"
    val battleDialogueBlendRoute = requestedState == "battle-dialogue-blending-fixture"
    val battleInitRoute = requestedState == "battle-init-fixture"
    val battleTerrainRoute = requestedState == "battle-terrain-layer-fixture"
    val battleMenuRoute = requestedState == "battle-menu-fixture"
    val dialogueStepCapture: Int? = requestedState?.removePrefix("yingchuan-dialogue-")
        ?.takeIf { requestedState.startsWith("yingchuan-dialogue-") }
        ?.toIntOrNull()
    val dialogueComponentStage: String? = requestedState?.removePrefix("yingchuan-dialogue-components-")
        ?.takeIf { requestedState.startsWith("yingchuan-dialogue-components-") }
    val mapOnlyCapture = requestedState == "map-only"
    val selectionOverlayCapture = requestedState == "yingchuan-selection"
    val modalRenderCapture = requestedState in MODAL_RENDER_STATES
    val actionSampleMode get() = actionSample != null

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
        private val REWARD_ROUTE_STATES = setOf(
            "yingchuan-reward-basic-route", "yingchuan-reward-card1-route", "yingchuan-reward-card2-route",
        )
        private val ROUND_ROUTE_STATES = setOf(
            "battle-round-normal-fixture", "battle-round-final-fixture", "battle-round-enemy-fixture",
        )
        private val WIN_CONDITION_ROUTE_STATES = setOf(
            "battle-win-condition-compact-fixture", "battle-win-condition-full-fixture",
        )
        private val MINI_MAP_ROUTE_STATES = setOf(
            "battle-mini-map-shown-fixture", "battle-mini-map-hidden-fixture",
        )
        private val AUTO_BATTLE_ROUTE_STATES = setOf(
            "battle-auto-battle-prompt-off-fixture", "battle-auto-battle-prompt-on-fixture", "battle-auto-battle-active-fixture",
        )
        private val BATTLE_COMMAND_ROUTE_STATES = setOf(
            "battle-command-initial-fixture", "battle-command-disabled-fixture", "battle-command-cancel-fixture",
            "battle-command-magick-fixture", "battle-command-property-fixture",
        )
        private val MODAL_RENDER_STATES = setOf(
            "yingchuan-terrain", "yingchuan-property", "yingchuan-treasure", "yingchuan-setting",
            "yingchuan-save", "yingchuan-load", "yingchuan-forces",
        )
        private const val ITEM_UPGRADE_ROUTE_STATE = "yingchuan-item-upgrade-panel-route"
        private const val LOSE_RESTART_ROUTE_STATE = "yingchuan-lose-restart"

        private fun parseBattleCharacterRoute(state: String?): BattleCharacterStrictState? {
            val route = state?.removeSuffix("-fixture")?.removePrefix("battle-character-") ?: return null
            if (!state.removeSuffix("-fixture").startsWith("battle-character-")) return null
            return BattleCharacterStrictState.entries.firstOrNull { it.route == route }
        }
    }
}

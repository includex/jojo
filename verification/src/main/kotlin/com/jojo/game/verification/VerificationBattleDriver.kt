package com.jojo.game.verification

import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeBattleCommand
import com.jojo.game.application.runtime.RuntimeBattleFrame
import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleActionSample
import com.jojo.game.application.runtime.RuntimeBattleRoute

/** Verification-owned deterministic input driver for externally named battle runs. */
internal class VerificationBattleDriver(private val state: String?) : RuntimeBattleDriver {
    private var endTurnIssued = false

    override fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand> {
        if (state == "enemy-turn" && !endTurnIssued && probe.turnPhase == "PLAYER_INPUT" && probe.outcome == null) {
            endTurnIssued = true
            return listOf(RuntimeBattleCommand.EndTurn)
        }
        return emptyList()
    }
}

/** Keeps externally named presentation routes out of the production screen. */
internal object VerificationBattlePresentation {
    fun from(state: String?): RuntimeBattlePresentation {
        val action = when (state) {
            "attack6-f0" -> RuntimeBattleActionSample(6, 1f / 24f)
            "attack6-f1" -> RuntimeBattleActionSample(6, 7f / 24f)
            "attack6-f2" -> RuntimeBattleActionSample(6, 9f / 24f)
            "attack6-f3" -> RuntimeBattleActionSample(6, 11f / 24f)
            "attack25-f0" -> RuntimeBattleActionSample(25, 1f / 24f)
            "attack25-f1" -> RuntimeBattleActionSample(25, 10f / 24f)
            "attack25-f2" -> RuntimeBattleActionSample(25, 12f / 24f)
            "attack25-f3" -> RuntimeBattleActionSample(25, 14f / 24f)
            "attack48-f0" -> RuntimeBattleActionSample(48, 1f / 24f)
            "attack48-f1" -> RuntimeBattleActionSample(48, 19f / 24f)
            "attack48-f2" -> RuntimeBattleActionSample(48, 21f / 24f)
            "attack48-f3" -> RuntimeBattleActionSample(48, 23f / 24f)
            else -> null
        }
        val route = when {
            state in setOf("yingchuan-reward-basic-route", "yingchuan-reward-card1-route", "yingchuan-reward-card2-route") -> when (state) {
                "yingchuan-reward-card1-route" -> RuntimeBattleRoute.REWARD_CARD1
                "yingchuan-reward-card2-route" -> RuntimeBattleRoute.REWARD_CARD2
                else -> RuntimeBattleRoute.REWARD_BASIC
            }
            state == "yingchuan-item-upgrade-panel-route" -> RuntimeBattleRoute.ITEM_UPGRADE
            state == "yingchuan-lose-restart" -> RuntimeBattleRoute.LOSE_RESTART
            state == "battle-round-final-fixture" -> RuntimeBattleRoute.ROUND_FINAL
            state == "battle-round-enemy-fixture" -> RuntimeBattleRoute.ROUND_ENEMY
            state == "battle-round-normal-fixture" -> RuntimeBattleRoute.ROUND_NORMAL
            state == "battle-win-condition-compact-fixture" -> RuntimeBattleRoute.WIN_COMPACT
            state == "battle-win-condition-full-fixture" -> RuntimeBattleRoute.WIN_FULL
            state == "battle-mini-map-shown-fixture" -> RuntimeBattleRoute.MINI_MAP_SHOWN
            state == "battle-mini-map-hidden-fixture" -> RuntimeBattleRoute.MINI_MAP_HIDDEN
            state == "battle-auto-battle-prompt-off-fixture" -> RuntimeBattleRoute.AUTO_PROMPT_OFF
            state == "battle-auto-battle-prompt-on-fixture" -> RuntimeBattleRoute.AUTO_PROMPT_ON
            state == "battle-auto-battle-active-fixture" -> RuntimeBattleRoute.AUTO_ACTIVE
            state == "battle-command-initial-fixture" -> RuntimeBattleRoute.COMMAND_INITIAL
            state == "battle-command-disabled-fixture" -> RuntimeBattleRoute.COMMAND_DISABLED
            state == "battle-command-cancel-fixture" -> RuntimeBattleRoute.COMMAND_CANCEL
            state == "battle-command-magick-fixture" -> RuntimeBattleRoute.COMMAND_MAGICK
            state == "battle-command-property-fixture" -> RuntimeBattleRoute.COMMAND_PROPERTY
            state?.removeSuffix("-fixture") == "battle-character-hp-camps-partial" -> RuntimeBattleRoute.CHARACTER_HP_CAMPS
            state?.removeSuffix("-fixture") == "battle-character-outline-highlight" -> RuntimeBattleRoute.CHARACTER_OUTLINE
            state?.removeSuffix("-fixture") == "battle-character-hit-impact" -> RuntimeBattleRoute.CHARACTER_HIT
            state?.removeSuffix("-fixture") == "battle-character-cleanup" -> RuntimeBattleRoute.CHARACTER_CLEANUP
            state?.removeSuffix("-fixture") == "battle-character-death-action" -> RuntimeBattleRoute.CHARACTER_DEATH_ACTION
            state?.removeSuffix("-fixture") == "battle-character-death-hidden" -> RuntimeBattleRoute.CHARACTER_DEATH_HIDDEN
            state == "battle-other-unit-info-fixture" -> RuntimeBattleRoute.OTHER_UNIT_INFO
            state == "battle-mine-unit-info-fixture" -> RuntimeBattleRoute.MINE_UNIT_INFO
            state == "yingchuan-attack" -> RuntimeBattleRoute.CUTSCENE_ATTACK
            state == "yingchuan-action4" -> RuntimeBattleRoute.CUTSCENE_POST_HIT
            state == "yingchuan-477" -> RuntimeBattleRoute.CUTSCENE_477
            state == "battle-dialogue-blending-fixture" -> RuntimeBattleRoute.DIALOGUE_BLEND
            state == "battle-init-fixture" -> RuntimeBattleRoute.INITIAL
            state == "battle-terrain-layer-fixture" -> RuntimeBattleRoute.TERRAIN
            state == "battle-menu-fixture" -> RuntimeBattleRoute.MENU
            state == "map-only" -> RuntimeBattleRoute.MAP_ONLY
            state == "yingchuan-selection" -> RuntimeBattleRoute.SELECTION
            state == "yingchuan-terrain" -> RuntimeBattleRoute.MODAL_TERRAIN
            state == "yingchuan-property" -> RuntimeBattleRoute.MODAL_PROPERTY
            state == "yingchuan-treasure" -> RuntimeBattleRoute.MODAL_TREASURE
            state == "yingchuan-setting" -> RuntimeBattleRoute.MODAL_SETTING
            state == "yingchuan-save" -> RuntimeBattleRoute.MODAL_SAVE
            state == "yingchuan-load" -> RuntimeBattleRoute.MODAL_LOAD
            state == "yingchuan-forces" -> RuntimeBattleRoute.MODAL_FORCES
            state == "battle-jiqi-fixture" -> RuntimeBattleRoute.JIQI
            state == "battle-magick-list-fixture" -> RuntimeBattleRoute.MAGICK_LIST
            state == "battle-magick-detail-fixture" -> RuntimeBattleRoute.MAGICK_DETAIL
            state == "battle-use-property-list-fixture" -> RuntimeBattleRoute.USE_PROPERTY_LIST
            state == "battle-use-property-detail-fixture" -> RuntimeBattleRoute.USE_PROPERTY_DETAIL
            state == "battle-use-property-select-fixture" -> RuntimeBattleRoute.USE_PROPERTY_SELECT
            state == "battle-use-property-cancel-fixture" -> RuntimeBattleRoute.USE_PROPERTY_CANCEL
            state?.startsWith("yingchuan-dialogue-components-") == true -> when (state.removePrefix("yingchuan-dialogue-components-")) {
                "background" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_BACKGROUND
                "characters" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_CHARACTERS
                "labels" -> RuntimeBattleRoute.DIALOGUE_COMPONENT_LABELS
                else -> RuntimeBattleRoute.DIALOGUE_COMPONENT_DIALOGUE
            }
            state?.startsWith("battle-edit2-") == true -> when (state.removePrefix("battle-edit2-").removeSuffix("-fixture")) {
                "initial" -> RuntimeBattleRoute.EDIT_INITIAL
                "weather" -> RuntimeBattleRoute.EDIT_WEATHER
                "round" -> RuntimeBattleRoute.EDIT_ROUND
                "apply" -> RuntimeBattleRoute.EDIT_APPLY
                "child" -> RuntimeBattleRoute.EDIT_CHILD
                "child-scene" -> RuntimeBattleRoute.EDIT_CHILD_SCENE
                else -> RuntimeBattleRoute.NONE
            }
            state == "battle-register-open" -> RuntimeBattleRoute.EDIT_REGISTER
            else -> RuntimeBattleRoute.NONE
        }
        val dialogueStep = state?.removePrefix("yingchuan-dialogue-")
            ?.takeIf { state.startsWith("yingchuan-dialogue-") }
            ?.toIntOrNull()
        return RuntimeBattlePresentation(route, action, dialogueStep)
    }
}

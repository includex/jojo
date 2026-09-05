package com.jojo.game

/**
 * Single source of truth for BattleScreen's map-input pause gate.
 *
 * Say/choice layers consume their own input, while DELAY and MODAL are true
 * source-script pauses and must never leak a click into tactical selection.
 * Tactical map input additionally belongs only to ctrl_mine/PLAYER_INPUT.
 */
object BattleInteractiveInput {
    enum class Route { DIALOGUE, CHOICE, SCRIPT_PAUSED, TURN_PAUSED, PLAYER_INPUT }

    fun route(script: PlaybackState, turn: BattleTurnController.Phase): Route = when (script) {
        PlaybackState.DIALOGUE -> Route.DIALOGUE
        PlaybackState.CHOICE -> Route.CHOICE
        PlaybackState.DELAY, PlaybackState.MODAL -> Route.SCRIPT_PAUSED
        PlaybackState.COMPLETE -> if (turn == BattleTurnController.Phase.PLAYER_INPUT) {
            Route.PLAYER_INPUT
        } else {
            Route.TURN_PAUSED
        }
    }

    /** Deterministic state trace used by the no-framebuffer regression test. */
    fun trace(): String = listOf(
        PlaybackState.DIALOGUE to BattleTurnController.Phase.PLAYER_INPUT,
        PlaybackState.DELAY to BattleTurnController.Phase.PLAYER_INPUT,
        PlaybackState.MODAL to BattleTurnController.Phase.PLAYER_INPUT,
        PlaybackState.COMPLETE to BattleTurnController.Phase.BOOTSTRAP,
        PlaybackState.COMPLETE to BattleTurnController.Phase.CAMP_SCRIPT,
        PlaybackState.COMPLETE to BattleTurnController.Phase.AI,
        PlaybackState.COMPLETE to BattleTurnController.Phase.PLAYER_INPUT,
    ).joinToString("\n") { (script, turn) ->
        "{\"script\":\"$script\",\"turn\":\"$turn\",\"route\":\"${route(script, turn)}\",\"paused\":${script != PlaybackState.COMPLETE || turn != BattleTurnController.Phase.PLAYER_INPUT}}"
    } + "\n"
}

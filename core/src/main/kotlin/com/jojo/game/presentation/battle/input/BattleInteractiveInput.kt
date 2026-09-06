// Battle
package com.jojo.game.presentation.battle.input

import com.jojo.game.domain.battle.turn.BattleTurnPhase
import com.jojo.game.domain.scenario.*

object BattleInteractiveInput {

    /** Route: 전투 화면 흐름에서 현재 처리 종류를 구분한다. */
    enum class Route { DIALOGUE, CHOICE, SCRIPT_PAUSED, TURN_PAUSED, PLAYER_INPUT }


    fun route(script: PlaybackState, turn: BattleTurnPhase): Route = when (script) {
        PlaybackState.DIALOGUE -> Route.DIALOGUE
        PlaybackState.CHOICE -> Route.CHOICE
        PlaybackState.DELAY, PlaybackState.MODAL -> Route.SCRIPT_PAUSED
        PlaybackState.COMPLETE -> if (turn == BattleTurnPhase.PLAYER_INPUT) {
            Route.PLAYER_INPUT
        } else {
            Route.TURN_PAUSED
        }
    }
    fun trace(): String = listOf(
        PlaybackState.DIALOGUE to BattleTurnPhase.PLAYER_INPUT,
        PlaybackState.DELAY to BattleTurnPhase.PLAYER_INPUT,
        PlaybackState.MODAL to BattleTurnPhase.PLAYER_INPUT,
        PlaybackState.COMPLETE to BattleTurnPhase.BOOTSTRAP,
        PlaybackState.COMPLETE to BattleTurnPhase.CAMP_SCRIPT,
        PlaybackState.COMPLETE to BattleTurnPhase.AI,
        PlaybackState.COMPLETE to BattleTurnPhase.PLAYER_INPUT,
    ).joinToString("\n") { (script, turn) ->
        "{\"script\":\"$script\",\"turn\":\"$turn\",\"route\":\"${
            route(
                script,
                turn
            )
        }\",\"paused\":${script != PlaybackState.COMPLETE || turn != BattleTurnPhase.PLAYER_INPUT}}"
    } + "\n"
}

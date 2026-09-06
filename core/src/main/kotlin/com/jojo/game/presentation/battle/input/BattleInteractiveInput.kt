// Battle
package com.jojo.game.presentation.battle.input

import com.jojo.game.domain.battle.turn.BattleTurnPhase
import com.jojo.game.domain.scenario.*

/**
 * `BattleInteractiveInput`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object BattleInteractiveInput {

    /** Route: 전투 화면 흐름에서 현재 처리 종류를 구분한다. */
    enum class Route { DIALOGUE, CHOICE, SCRIPT_PAUSED, TURN_PAUSED, PLAYER_INPUT }


    /**
     * `route`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
    /**
     * `trace`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

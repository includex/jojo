package com.jojo.game.presentation.battle.input

import com.jojo.game.domain.battle.turn.BattleTurnPhase
import com.jojo.game.domain.scenario.*

/**
 * Single source of truth for BattleScreen's map-input pause gate.
 *
 * Say/choice layers consume their own input, while DELAY and MODAL are true
 * source-script pauses and must never leak a click into tactical selection.
 * Tactical map input additionally belongs only to ctrl_mine/PLAYER_INPUT.
 */
/**
 * object  `BattleInteractiveInput`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object BattleInteractiveInput {
    /**
     * enum class  `Route`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Route { DIALOGUE, CHOICE, SCRIPT_PAUSED, TURN_PAUSED, PLAYER_INPUT }

    /**
     * 공개 메서드 `route`
     *
     * ### 파라미터
    - `script` (`PlaybackState`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `turn` (`BattleTurnPhase`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Route`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    /** Deterministic state trace used by the no-framebuffer regression test. */
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

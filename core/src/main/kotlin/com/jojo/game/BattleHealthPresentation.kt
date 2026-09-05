package com.jojo.game

/**
 * Renderer-independent HP display state for BattleScreen._attack3.  Tactical
 * state may already contain the final value, but this model exposes only the
 * value that the authored BRAnime `hit` event has made visible.
 */
/**
 * class  `BattleHealthPresentation`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleHealthPresentation {
    /**
     * data class  `Transition`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Transition(val fromHp: Int, val toHp: Int, val revealAt: Float)

    private val transitions = linkedMapOf<String, Transition>()

    /**
     * 공개 메서드 `schedule`
     *
     * ### 파라미터
    - `unitId` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `fromHp` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `toHp` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `revealAt` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun schedule(unitId: String, fromHp: Int, toHp: Int, revealAt: Float) {
        transitions[unitId] = Transition(fromHp, toHp, revealAt)
    }

    /**
     * 공개 메서드 `shownHp`
     *
     * ### 파라미터
    - `unitId` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `now` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `fallbackHp` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun shownHp(unitId: String, now: Float, fallbackHp: Int): Int = transitions[unitId]?.let {
        if (now < it.revealAt) it.fromHp else it.toHp
    } ?: fallbackHp

    /** A follow-up/ counterattack replaces the prior visible transition. */
    fun clear(unitId: String) {
        transitions.remove(unitId)
    }
}

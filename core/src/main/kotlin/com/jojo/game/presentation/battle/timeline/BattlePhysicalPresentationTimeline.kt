package com.jojo.game.presentation.battle.timeline

import com.jojo.game.*
import com.jojo.game.domain.battle.*

/**
 * Pure timing implementation of BattleScreen._attack2's `for (T.targets)` loop.
 * Every _attack3 completion gates the next target, so area hits are strictly
 * sequential rather than simultaneous with the primary target reaction.
 */
/**
 * object  `BattlePhysicalPresentationTimeline`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object BattlePhysicalPresentationTimeline {
    /**
     * data class  `Hit`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Hit(val targetId: String, val damage: Int, val startsAt: Float, val endsAt: Float)

    fun sequence(
        primaryId: String,
        primaryDamage: Int,
        splash: List<PhysicalTarget>,
        hitAt: Float,
        durationFor: (String) -> Float,
    ): List<Hit> {
        var startsAt = hitAt
        return buildList {
            /**
             * 공개 메서드 `append`
             *
             * ### 파라미터
            - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `damage` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun append(id: String, damage: Int) {
                val endsAt = startsAt + durationFor(id)
                add(Hit(id, damage, startsAt, endsAt))
                startsAt = endsAt
            }
            append(primaryId, primaryDamage)
            splash.forEach { append(it.targetId, it.damage) }
        }
    }

    /**
     * `BattleScreen.playAtkAnime` does not wait for the attacking clip's
     * FINISHED event.  Its coroutine is resumed by the authored `hit` event,
     * starts the target reaction there, and restores the attacker to its
     * default action when that reaction finishes.  Consequently anime21 and
     * anime25 are deliberately cut short in the source.
     *
     * These are the exact no-delay BRAnime ticks used by StageLayer's
     * cinematic attackAction calls. Bit 0 selects HIT_ATTACK and bit 1 makes
     * the target guard instead of playing SHOU_GONG_JI3.
     */
    /**
     * 공개 메서드 `scriptedAttackDuration`
     *
     * ### 파라미터
    - `flags` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Float`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun scriptedAttackDuration(flags: Int): Float = when {
        flags and 1 != 0 && flags and 2 != 0 -> (22 + 17) / 24f
        flags and 1 != 0 -> (22 + 14) / 24f
        flags and 2 != 0 -> (11 + 17) / 24f
        else -> (11 + 14) / 24f
    }
}


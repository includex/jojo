// Battle
package com.jojo.game.presentation.battle.timeline

import com.jojo.game.*
import com.jojo.game.domain.battle.PhysicalTarget

/** BattlePhysicalPresentationTimeline: 전투 물리 표현 시간 흐름이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */

object BattlePhysicalPresentationTimeline {
    /**
     * `Hit`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Hit(val targetId: String, val damage: Int, val startsAt: Float, val endsAt: Float)

    /**
     * `sequence`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun sequence(
        primaryId: String,
        primaryDamage: Int,
        splash: List<PhysicalTarget>,
        hitAt: Float,
        durationFor: (String) -> Float,
    ): List<Hit> {
        /**
         * `startsAt` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var startsAt = hitAt
        return buildList {

            /**
             * `append`: 타입의 핵심 동작을 수행한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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
     * `scriptedAttackDuration`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun scriptedAttackDuration(flags: Int): Float = when {
        flags and 1 != 0 && flags and 2 != 0 -> (22 + 17) / 24f
        flags and 1 != 0 -> (22 + 14) / 24f
        flags and 2 != 0 -> (11 + 17) / 24f
        else -> (11 + 14) / 24f
    }
}

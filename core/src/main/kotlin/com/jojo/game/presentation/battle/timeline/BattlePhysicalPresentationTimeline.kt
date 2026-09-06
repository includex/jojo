// Battle
package com.jojo.game.presentation.battle.timeline

import com.jojo.game.*
import com.jojo.game.domain.battle.PhysicalTarget

/** BattlePhysicalPresentationTimeline: 전투 물리 표현 시간 흐름이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */

object BattlePhysicalPresentationTimeline {
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

            fun append(id: String, damage: Int) {
                val endsAt = startsAt + durationFor(id)
                add(Hit(id, damage, startsAt, endsAt))
                startsAt = endsAt
            }
            append(primaryId, primaryDamage)
            splash.forEach { append(it.targetId, it.damage) }
        }
    }

    fun scriptedAttackDuration(flags: Int): Float = when {
        flags and 1 != 0 && flags and 2 != 0 -> (22 + 17) / 24f
        flags and 1 != 0 -> (22 + 14) / 24f
        flags and 2 != 0 -> (11 + 17) / 24f
        else -> (11 + 14) / 24f
    }
}

package com.jojo.game.presentation.battle.timeline

import com.jojo.game.*
import com.jojo.game.domain.battle.PhysicalTarget

/**
 * Pure timing implementation of BattleScreen._attack2's `for (T.targets)` loop.
 * Every _attack3 completion gates the next target, so area hits are strictly
 * sequential rather than simultaneous with the primary target reaction.
 */

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

    fun scriptedAttackDuration(flags: Int): Float = when {
        flags and 1 != 0 && flags and 2 != 0 -> (22 + 17) / 24f
        flags and 1 != 0 -> (22 + 14) / 24f
        flags and 2 != 0 -> (11 + 17) / 24f
        else -> (11 + 14) / 24f
    }
}

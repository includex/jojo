// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.magic.BattleMagicProfile

import java.util.*

/** BattleRandomSource: 전투 확률 계산에 필요한 난수 공급 계약으로, 재현 가능한 전투 기록을 지원한다. */
interface BattleRandomSource {
    fun random(min: Int, max: Int, flag: Int = 0): Int
}

/** BattleRateGauge: 확률 게이지의 누적 상태를 나타내며, 반복 판정의 성공 보정을 유지한다. */
internal enum class BattleRateGauge(val index: Int) {
    PHYSICAL_HIT(0),
    PHYSICAL_GUARD(1),
    CONTINUOUS_ATTACK(2),
    CONTINUOUS_GUARD(3),
    MAGIC_HIT(4),
    MAGIC_GUARD(5),
    CRITICAL_HIT(6),
    CRITICAL_GUARD(7),
}

/** BattleProbabilityResolver: 명중·필살·상태 이상 등의 확률을 난수원과 누적 게이지로 판정한다. */
internal class BattleProbabilityResolver(
    private val fallbackRandom: Random,
    private val sourceRandomStreams: BattleRandomSource?,
) {
    fun countRate(
        attacker: BattleUnit,
        defender: BattleUnit,
        attackerGauge: BattleRateGauge,
        defenderGauge: BattleRateGauge,
        rate: Int,
    ): Boolean {
        var incoming = rate
        if (attacker.skills[111]?.and(255)?.let { it != 255 } == true) incoming = incoming shl 1
        var own = (attacker.rateAccumulators[attackerGauge.index] ?: 0) + incoming
        var other = (defender.rateAccumulators[defenderGauge.index] ?: 0) + 100 - rate
        val success = other < own
        if (success) own -= 100 else other -= 100
        attacker.rateAccumulators[attackerGauge.index] = own.coerceIn(0, 255)
        defender.rateAccumulators[defenderGauge.index] = other.coerceIn(0, 255)
        return success
    }
    fun defaultRandom(min: Int, max: Int): Int =
        sourceRandomStreams?.random(min, max, 0) ?: (fallbackRandom.nextInt(max - min + 1) + min)
    fun flagRandom(min: Int, max: Int): Int =
        sourceRandomStreams?.random(min, max, 1) ?: (fallbackRandom.nextInt(max - min + 1) + min)


    fun random100(): Int = defaultRandom(0, 100)


    fun initializeRateGauges(unit: BattleUnit) {
        if (unit.rateAccumulators.isNotEmpty()) return
        BattleRateGauge.entries.forEach { gauge ->
            unit.rateAccumulators[gauge.index] = random100()
        }
    }
    fun rollStatusDuration(): Int =
        if (sourceRandomStreams != null) defaultRandom(1, 3) else 3


    fun physicalHitRate(attackerCritical: Int, defenderCritical: Int): Int {
        val attacker = attackerCritical.toDouble()
        val defender = defenderCritical.coerceAtLeast(1).toDouble()
        val rate = when {
            attacker >= 2 * defender -> 100.0
            attacker >= defender -> 10 * (attacker - defender) / defender + 90
            attacker >= defender / 2 -> 30 * (attacker - defender / 2) / (defender / 2) + 60
            else -> 30 * (attacker - defender / 3) / (defender / 3) + 30
        }
        return rate.toInt().coerceIn(25, 100)
    }


    fun physicalHitRate(attacker: BattleUnit, target: BattleUnit): Int {
        val baseline = physicalHitRate(
            BattleAttributeCalculator.effective(attacker, BattleAttribute.CRITICAL),
            BattleAttributeCalculator.defenseAgainst(attacker, target, BattleAttribute.CRITICAL),
        )
        return (
                baseline - effect(target, 64) - effect(target, 71) + effect(attacker, 66)
                ).coerceIn(25, 100)
    }
    fun physicalHit(attacker: BattleUnit, target: BattleUnit, hitRate: Int): Boolean {
        val rolled = countRate(
            attacker,
            target,
            BattleRateGauge.PHYSICAL_HIT,
            BattleRateGauge.PHYSICAL_GUARD,
            hitRate,
        )
        if (attacker.remoteAttack && hasSkill(target, 48)) return false
        if (hasSkill(attacker, 92)) return true
        if (BattleStatus.CONFUSION in target.statuses) return true
        return rolled
    }


    fun criticalRate(attackerMorale: Int, defenderMorale: Int): Int {
        val attacker = attackerMorale.coerceAtLeast(1)
        val defender = defenderMorale.coerceAtLeast(1)
        val rate = when {
            attacker >= 3 * defender -> 100
            attacker >= 2 * defender -> ((attacker.toDouble() / defender * .8 - 1.4) * 100).toInt()
            attacker >= defender -> ((attacker.toDouble() / defender * .18 - .16) * 100).toInt()
            else -> 0
        }
        return rate.coerceIn(0, 100)
    }


    fun criticalHit(attacker: BattleUnit, target: BattleUnit): Boolean {
        val rate = if (hasSkill(attacker, 270)) {
            100
        } else {
            criticalRate(
                BattleAttributeCalculator.effective(attacker, BattleAttribute.MORALE),
                BattleAttributeCalculator.defenseAgainst(attacker, target, BattleAttribute.MORALE),
            )
        }
        return countRate(
            attacker,
            target,
            BattleRateGauge.CRITICAL_HIT,
            BattleRateGauge.CRITICAL_GUARD,
            rate,
        )
    }

    fun magicHitRate(
        attackerSpirit: Int,
        attackerMorale: Int,
        defenderSpirit: Int,
        defenderMorale: Int,
    ): Int {
        val attacker = (attackerSpirit + attackerMorale).toDouble()
        val defender = (defenderSpirit + defenderMorale).coerceAtLeast(1).toDouble()
        val rate = when {
            attacker >= 2 * defender -> 100.0
            attacker >= defender -> 10 * (attacker - defender) / defender + 90
            attacker >= defender / 2 -> 30 * (attacker - defender / 2) / (defender / 2) + 60
            else -> 30 * (attacker - defender / 3) / (defender / 3) + 30
        }
        return rate.toInt().coerceIn(25, 100)
    }

    fun magicHitRate(attacker: BattleUnit, target: BattleUnit, magic: BattleMagicProfile): Int {
        val base = magicHitRate(
            BattleAttributeCalculator.effective(attacker, BattleAttribute.SPIRIT),
            BattleAttributeCalculator.effective(attacker, BattleAttribute.MORALE),
            BattleAttributeCalculator.effective(target, BattleAttribute.SPIRIT),
            BattleAttributeCalculator.effective(target, BattleAttribute.MORALE),
        )
        val limit = magic.hitRateLimit
        if (limit == 0 || limit == 64) return 100
        val famousCap = target.famous && limit in 3..4
        var rate = when (limit) {
            1 -> base * 90 / 100
            2 -> base
            3 -> minOf(base, if (target.famous) 0 else 50)
            4 -> minOf(base, if (target.famous) 0 else 34)
            else -> base * limit
        }
        if (!famousCap) {
            if (hasSkill(attacker, 15)) rate = 100
            rate += effect(attacker, 56)
            rate -= effect(target, 55)
            rate -= effect(target, 71)
        }
        return rate.coerceIn(25, 100)
    }
    fun magicHit(attacker: BattleUnit, target: BattleUnit, hitRate: Int): Boolean {
        val rolled = countRate(
            attacker,
            target,
            BattleRateGauge.MAGIC_HIT,
            BattleRateGauge.MAGIC_GUARD,
            hitRate,
        )
        return rolled && target.skills[17]?.and(255)?.let { it == 255 } != false
    }
    fun continuousAttack(attacker: BattleUnit, target: BattleUnit): Boolean {
        val forced = hasSkill(attacker, 197) || hasSkill(attacker, 276)
        val own = BattleAttributeCalculator.effective(attacker, BattleAttribute.CRITICAL).toDouble()
        val opponent = BattleAttributeCalculator.defenseAgainst(
            attacker,
            target,
            BattleAttribute.CRITICAL,
        ).coerceAtLeast(1).toDouble()
        val rate = when {
            own >= 3 * opponent -> 100
            own >= 2 * opponent -> ((own / opponent * .8 - 1.4) * 100).toInt()
            own >= opponent -> ((own / opponent * .18 - .16) * 100).toInt()
            else -> 0
        }.coerceIn(0, 100)
        val rolled = countRate(
            attacker,
            target,
            BattleRateGauge.CONTINUOUS_ATTACK,
            BattleRateGauge.CONTINUOUS_GUARD,
            rate,
        )
        return forced || rolled
    }

    private fun hasSkill(unit: BattleUnit, skillId: Int): Boolean =
        unit.skills[skillId]?.and(255)?.let { it != 255 } == true

    private fun effect(unit: BattleUnit, skillId: Int): Int =
        unit.skills[skillId]?.and(255)?.takeIf { it != 255 } ?: 0
}

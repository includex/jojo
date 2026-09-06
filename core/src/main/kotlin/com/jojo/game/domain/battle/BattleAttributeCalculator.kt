// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit

/** BattleAttributeCalculator: 지형·상태·증감 효과를 반영해 유닛의 실제 전투 능력치와 피해 보정을 계산한다. */
internal object BattleAttributeCalculator {
    fun effective(unit: BattleUnit, attribute: BattleAttribute): Int {

        fun baseOf(value: BattleAttribute): Int = when (value) {
            BattleAttribute.ATTACK -> unit.attack
            BattleAttribute.DEFENSE -> unit.defense
            BattleAttribute.SPIRIT -> unit.spirit
            BattleAttribute.CRITICAL -> unit.critical
            BattleAttribute.MORALE -> unit.morale
            BattleAttribute.MOVEMENT -> unit.movement
        }

        var base = baseOf(attribute)
        val abilitySupport = unit.skills[157]?.and(255)?.takeIf { it != 255 } ?: 0
        when (attribute) {
            BattleAttribute.ATTACK -> {
                if (abilitySupport and 1 != 0) base += baseOf(BattleAttribute.SPIRIT) / 5
                if (abilitySupport and 8 != 0) base += baseOf(BattleAttribute.MORALE) / 5
                if (abilitySupport and 16 != 0) base += unit.maxHitPoints / 5
            }

            BattleAttribute.DEFENSE -> if (abilitySupport and 2 != 0) {
                base += baseOf(BattleAttribute.SPIRIT) / 5
            }

            BattleAttribute.SPIRIT -> if (abilitySupport and 4 != 0) {
                base += baseOf(BattleAttribute.ATTACK) / 5
            }

            else -> Unit
        }
        return when (unit.attributeLifts[attribute]) {
            -1 -> base * 4 / 5
            1 -> base + base / 5
            else -> base
        }
    }
    fun defenseAgainst(attacker: BattleUnit, defender: BattleUnit, attribute: BattleAttribute): Int {
        val usesLowestDefense = attacker.skills[165]?.and(255)?.let { it != 255 } == true
        if (!usesLowestDefense) return effective(defender, attribute)
        return listOf(
            BattleAttribute.ATTACK,
            BattleAttribute.DEFENSE,
            BattleAttribute.SPIRIT,
            BattleAttribute.CRITICAL,
            BattleAttribute.MORALE,
        ).minOf { effective(defender, it) }
    }
    fun effectiveMovement(unit: BattleUnit): Int = when (unit.attributeLifts[BattleAttribute.MOVEMENT]) {
        -1 -> maxOf(0, unit.movement - 2)
        1 -> unit.movement + 2
        else -> unit.movement
    }
    fun finalMovement(unit: BattleUnit, weather: BattleWeather): Int {
        var result = effectiveMovement(unit)
        if (
            weather == BattleWeather.WINDY ||
            (weather == BattleWeather.HEAVY_RAIN && unit.skills[268]?.and(255)?.let { it != 255 } != true)
        ) {
            result -= 1
        }
        return result
    }
    fun physicalDamageAfterResistance(rawDamage: Int, attacker: BattleUnit, defender: BattleUnit): Int {
        var rate = 100
        if (attacker.remoteAttack && attacker.skills[230]?.and(255)?.let { it != 255 } != true) {
            defender.skills[119]?.and(255)?.takeIf { it != 255 }?.let { rate -= it }
        }
        return maxOf(1, rawDamage * maxOf(0, rate) / 100)
    }
}

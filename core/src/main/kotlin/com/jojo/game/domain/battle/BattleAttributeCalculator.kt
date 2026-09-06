// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit

/** BattleAttributeCalculator: 지형·상태·증감 효과를 반영해 유닛의 실제 전투 능력치와 피해 보정을 계산한다. */
internal object BattleAttributeCalculator {
    /**
     * `effective`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun effective(unit: BattleUnit, attribute: BattleAttribute): Int {

        /**
         * `baseOf`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun baseOf(value: BattleAttribute): Int = when (value) {
            BattleAttribute.ATTACK -> unit.attack
            BattleAttribute.DEFENSE -> unit.defense
            BattleAttribute.SPIRIT -> unit.spirit
            BattleAttribute.CRITICAL -> unit.critical
            BattleAttribute.MORALE -> unit.morale
            BattleAttribute.MOVEMENT -> unit.movement
        }

        /**
         * `base` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var base = baseOf(attribute)
        /**
         * `abilitySupport` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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
    /**
     * `defenseAgainst`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
    /**
     * `effectiveMovement`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun effectiveMovement(unit: BattleUnit): Int = when (unit.attributeLifts[BattleAttribute.MOVEMENT]) {
        -1 -> maxOf(0, unit.movement - 2)
        1 -> unit.movement + 2
        else -> unit.movement
    }
    /**
     * `finalMovement`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
    /**
     * `physicalDamageAfterResistance`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun physicalDamageAfterResistance(rawDamage: Int, attacker: BattleUnit, defender: BattleUnit): Int {
        var rate = 100
        if (attacker.remoteAttack && attacker.skills[230]?.and(255)?.let { it != 255 } != true) {
            defender.skills[119]?.and(255)?.takeIf { it != 255 }?.let { rate -= it }
        }
        return maxOf(1, rawDamage * maxOf(0, rate) / 100)
    }
}

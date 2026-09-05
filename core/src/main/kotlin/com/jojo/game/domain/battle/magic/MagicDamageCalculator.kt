package com.jojo.game.domain.battle.magic

import com.jojo.game.BattleUnit
import com.jojo.game.domain.battle.*

/** Pure magic damage, terrain, weather and condition policy. */
internal object MagicDamageCalculator {
    fun magicTerrainAllowed(magic: BattleMagicProfile, target: BattleUnit): Boolean = true

    fun magicConditionReason(attacker: BattleUnit, magic: BattleMagicProfile, weather: BattleWeather): String? {
        fun active(skill: Int) = attacker.skills[skill]?.and(255)?.let { it != 255 } == true
        if (magic.condition in 2..5 && active(136)) return null
        if (magic.condition == 1 && attacker.hitPoints < 40) return "HP가 40 미만이면 사용할 수 없는 전략입니다."
        val weatherAllowed = when (magic.condition) {
            0 -> weather in setOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY)
            2 -> weather in setOf(BattleWeather.HEAVY_RAIN, BattleWeather.SNOW)
            3 -> weather == BattleWeather.CLEAR
            4 -> weather == BattleWeather.CLOUDY
            else -> true
        }
        if (!weatherAllowed && !active(20)) return "현재 날씨에서는 사용할 수 없는 전략입니다."
        return if (magic.condition == 5) "이 전략의 특수 사용 조건을 충족하지 못했습니다." else null
    }

    fun magicWeatherRate(magic: BattleMagicProfile, weather: BattleWeather): Int = if (when (magic.condition) {
        0 -> weather in setOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY)
        2 -> weather in setOf(BattleWeather.HEAVY_RAIN, BattleWeather.SNOW)
        3 -> weather == BattleWeather.CLEAR
        4 -> weather == BattleWeather.CLOUDY
        else -> true
    }) 100 else 85

    fun offensiveMagicTerrainRate(target: BattleUnit, magic: BattleMagicProfile, terrain: BattleTerrainGrid?, terrainMagicFlags: Map<Int, Int>): Int {
        if (magic.type !in 0..3) return 100
        val terrainId = terrain?.terrainAt(target.tileX, target.tileY) ?: return 100
        return if ((terrainMagicFlags[terrainId] ?: 0) and (1 shl magic.type) != 0) 100 else 85
    }

    fun healingTerrainRate(attacker: BattleUnit, magic: BattleMagicProfile, terrain: BattleTerrainGrid?, terrainMagicFlags: Map<Int, Int>): Int {
        if (magic.type !in 0..3 || terrainMagicFlags.isEmpty()) return 100
        val terrainId = terrain?.terrainAt(attacker.tileX, attacker.tileY) ?: return 100
        val flag = terrainMagicFlags[terrainId] ?: return 100
        if (flag and (1 shl magic.type) != 0) return 100
        return if (attacker.skills[19]?.and(255)?.let { it != 255 } == true) 85 else 0
    }

    fun magicFlatSkillDamage(attacker: BattleUnit, magic: BattleMagicProfile): Int {
        fun effect(skill: Int) = attacker.skills[skill]?.and(255)?.takeIf { it != 255 }
        var addition = effect(141)?.let { BattleAttributeCalculator.effective(attacker, BattleAttribute.ATTACK) * it / 100 } ?: 0
        if (magic.type == 0) addition += effect(107) ?: 0
        return addition
    }

    fun magicSkillDamageRate(attacker: BattleUnit, target: BattleUnit, magic: BattleMagicProfile, flagRandomBonus: Int = 0): Int {
        fun effect(unit: BattleUnit, skill: Int) = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        var rate = 100
        effect(attacker, 292)?.let { rate += 10 + flagRandomBonus }
        if (magic.type in 0..3) rate += effect(attacker, 75) ?: 0
        if (magic.type == 0 && magic.effectAreaId == 0) rate += effect(attacker, 128) ?: 0
        if (magic.type in 4..18) rate += effect(attacker, 62) ?: 0
        effect(attacker, 145)?.takeIf { attacker.hitPoints >= attacker.magicPoints / 2 }?.let { rate += it }
        rate -= effect(target, 115) ?: 0
        effect(target, 245)?.let { rate -= (target.maxHitPoints - target.hitPoints.coerceAtMost(target.maxHitPoints)) * 100 / target.maxHitPoints.coerceAtLeast(1) }
        return maxOf(1, rate)
    }

    fun statusEffect(category: Int): BattleStatus? = when (category) {
        8 -> BattleStatus.CONFUSION; 9 -> BattleStatus.POISON; 10 -> BattleStatus.PARALYSIS; 11 -> BattleStatus.SILENCE; else -> null
    }

    fun attributeChange(category: Int): Pair<BattleAttribute, Int>? = when (category) {
        4 -> BattleAttribute.CRITICAL to -1; 5 -> BattleAttribute.MORALE to -1; 6 -> BattleAttribute.ATTACK to -1; 7 -> BattleAttribute.DEFENSE to -1
        16 -> BattleAttribute.MOVEMENT to 1; 17 -> BattleAttribute.CRITICAL to 1; 18 -> BattleAttribute.MORALE to 1; 19 -> BattleAttribute.ATTACK to 1; 20 -> BattleAttribute.DEFENSE to 1
        else -> null
    }
}

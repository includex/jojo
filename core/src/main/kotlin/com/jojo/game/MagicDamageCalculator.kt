package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleAttributeCalculator

/**
 * Pure Kotlin formulas and modifiers for magic combat:
 * weather modifiers, terrain modifiers, skill damage rate/flat additions, and condition testing.
 */
internal object MagicDamageCalculator {

    /**
     * 공개 메서드 `magicTerrainAllowed`
     *
     * ### 파라미터
    - `magic` (`GameDataCatalog.MagicProfile`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `target` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun magicTerrainAllowed(magic: GameDataCatalog.MagicProfile, target: BattleUnit): Boolean = true

    fun magicConditionReason(
        attacker: BattleUnit,
        magic: GameDataCatalog.MagicProfile,
        weather: BattleWeather,
    ): String? {
        /**
         * 공개 메서드 `active`
         *
         * ### 파라미터
        - `skill` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun active(skill: Int) = attacker.skills[skill]?.and(255)?.let { it != 255 } == true
        if (magic.condition in 2..5 && active(136)) return null // KYJZ: condition restriction bypass
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

    /**
     * 공개 메서드 `magicWeatherRate`
     *
     * ### 파라미터
    - `magic` (`GameDataCatalog.MagicProfile`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `weather` (`BattleWeather`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun magicWeatherRate(magic: GameDataCatalog.MagicProfile, weather: BattleWeather): Int {
        val allowed = when (magic.condition) {
            0 -> weather in setOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY)
            2 -> weather in setOf(BattleWeather.HEAVY_RAIN, BattleWeather.SNOW)
            3 -> weather == BattleWeather.CLEAR
            4 -> weather == BattleWeather.CLOUDY
            else -> true
        }
        return if (allowed) 100 else 85
    }

    fun offensiveMagicTerrainRate(
        target: BattleUnit,
        magic: GameDataCatalog.MagicProfile,
        terrain: BattleTerrainGrid?,
        terrainMagicFlags: Map<Int, Int>,
    ): Int {
        if (magic.type !in 0..3) return 100
        val terrainId = terrain?.terrainAt(target.tileX, target.tileY) ?: return 100
        val flag = terrainMagicFlags[terrainId] ?: 0
        return if (flag and (1 shl magic.type) != 0) 100 else 85
    }

    fun healingTerrainRate(
        attacker: BattleUnit,
        magic: GameDataCatalog.MagicProfile,
        terrain: BattleTerrainGrid?,
        terrainMagicFlags: Map<Int, Int>,
    ): Int {
        if (magic.type !in 0..3 || terrainMagicFlags.isEmpty()) return 100
        val terrainId = terrain?.terrainAt(attacker.tileX, attacker.tileY) ?: return 100
        val flag = terrainMagicFlags[terrainId] ?: return 100
        if (flag and (1 shl magic.type) != 0) return 100
        return if (attacker.skills[19]?.and(255)?.let { it != 255 } == true) 85 else 0
    }

    /**
     * 공개 메서드 `magicFlatSkillDamage`
     *
     * ### 파라미터
    - `attacker` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `magic` (`GameDataCatalog.MagicProfile`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun magicFlatSkillDamage(attacker: BattleUnit, magic: GameDataCatalog.MagicProfile): Int {
        /**
         * 공개 메서드 `effect`
         *
         * ### 파라미터
        - `skill` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun effect(skill: Int) = attacker.skills[skill]?.and(255)?.takeIf { it != 255 }
        var addition =
            effect(141)?.let { BattleAttributeCalculator.effective(attacker, BattleAttribute.ATTACK) * it / 100 } ?: 0
        if (magic.type == 0) addition += effect(107) ?: 0
        return addition
    }

    fun magicSkillDamageRate(
        attacker: BattleUnit,
        target: BattleUnit,
        magic: GameDataCatalog.MagicProfile,
        flagRandomBonus: Int = 0,
    ): Int {
        /**
         * 공개 메서드 `effect`
         *
         * ### 파라미터
        - `unit` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `skill` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun effect(unit: BattleUnit, skill: Int) = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        var rate = 100
        effect(attacker, 292)?.let { rate += 10 + flagRandomBonus }
        if (magic.type in 0..3) rate += effect(attacker, 75) ?: 0
        if (magic.type == 0 && magic.effectAreaId == 0) rate += effect(attacker, 128) ?: 0
        if (magic.type in 4..18) rate += effect(attacker, 62) ?: 0
        effect(attacker, 145)?.takeIf { attacker.hitPoints >= attacker.magicPoints / 2 }?.let { rate += it }
        rate -= effect(target, 115) ?: 0
        effect(target, 245)?.let {
            rate -= target.hitPoints.coerceAtMost(target.maxHitPoints).let { hp ->
                (target.maxHitPoints - hp) * 100 / target.maxHitPoints.coerceAtLeast(1)
            }
        }
        return maxOf(1, rate)
    }

    /**
     * 공개 메서드 `statusEffect`
     *
     * ### 파라미터
    - `category` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `BattleStatus?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun statusEffect(category: Int): BattleStatus? = when (category) {
        8 -> BattleStatus.CONFUSION
        9 -> BattleStatus.POISON
        10 -> BattleStatus.PARALYSIS
        11 -> BattleStatus.SILENCE
        else -> null
    }

    /**
     * 공개 메서드 `attributeChange`
     *
     * ### 파라미터
    - `category` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Pair<BattleAttribute, Int>?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun attributeChange(category: Int): Pair<BattleAttribute, Int>? = when (category) {
        4 -> BattleAttribute.CRITICAL to -1
        5 -> BattleAttribute.MORALE to -1
        6 -> BattleAttribute.ATTACK to -1
        7 -> BattleAttribute.DEFENSE to -1
        16 -> BattleAttribute.MOVEMENT to 1
        17 -> BattleAttribute.CRITICAL to 1
        18 -> BattleAttribute.MORALE to 1
        19 -> BattleAttribute.ATTACK to 1
        20 -> BattleAttribute.DEFENSE to 1
        else -> null
    }
}

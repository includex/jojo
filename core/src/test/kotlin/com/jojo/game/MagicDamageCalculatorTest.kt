package com.jojo.game

import com.jojo.game.domain.battle.*


import com.jojo.game.domain.battle.magic.MagicDamageCalculator
import com.jojo.game.domain.battle.magic.BattleMagicHitAreaValue
import com.jojo.game.domain.battle.magic.BattleMagicProfileValue
import com.jojo.game.domain.battle.BattleTerrainGrid

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull

/**
 * class  `MagicDamageCalculatorTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class MagicDamageCalculatorTest {

    private fun unit(
        id: String = "unit",
        faction: Faction = Faction.PLAYER,
        attack: Int = 100,
        defense: Int = 50,
        spirit: Int = 50,
        level: Int = 10,
        hitPoints: Int = 100,
        maxHitPoints: Int = 100,
        magicPoints: Int = 50,
        maxMagicPoints: Int = 50,
        tileX: Int = 2,
        tileY: Int = 3,
        skills: Map<Int, Int> = emptyMap(),
    ) = BattleUnit(
        id = id,
        name = id,
        faction = faction,
        tileX = tileX,
        tileY = tileY,
        attack = attack,
        defense = defense,
        spirit = spirit,
        level = level,
        hitPoints = hitPoints,
        maxHitPoints = maxHitPoints,
        magicPoints = magicPoints,
        maxMagicPoints = maxMagicPoints,
        skills = skills,
    )

    private fun magic(
        id: Int = 1,
        name: String = "TestMagic",
        type: Int = 0,
        category: Int = 0,
        condition: Int = 0,
        power: Int = 100,
        effectAreaId: Int = 0,
    ) = BattleMagicProfileValue(
        id = id,
        name = name,
        type = type,
        category = category,
        condition = condition,
        power = power,
        effectAreaId = effectAreaId,
        target = 0,
        expendMp = 10,
        harmType = 0,
        hitArea = BattleMagicHitAreaValue(0, emptySet(), false),
        effectOffsets = emptySet(),
    )

    @Test
    fun `magicConditionReason evaluates hp and weather conditions`() {
        val lowHpUnit = unit(hitPoints = 30)
        val fullHpUnit = unit(hitPoints = 100)
        val hpMagic = magic(condition = 1)

        assertEquals("HP가 40 미만이면 사용할 수 없는 전략입니다.", MagicDamageCalculator.magicConditionReason(lowHpUnit, hpMagic, BattleWeather.CLEAR))
        assertNull(MagicDamageCalculator.magicConditionReason(fullHpUnit, hpMagic, BattleWeather.CLEAR))

        val rainMagic = magic(condition = 2)
        assertEquals("현재 날씨에서는 사용할 수 없는 전략입니다.", MagicDamageCalculator.magicConditionReason(fullHpUnit, rainMagic, BattleWeather.CLEAR))
        assertNull(MagicDamageCalculator.magicConditionReason(fullHpUnit, rainMagic, BattleWeather.HEAVY_RAIN))

        // Skill 20 bypasses weather
        val weatherBypassUnit = unit(hitPoints = 100, skills = mapOf(20 to 1))
        assertNull(MagicDamageCalculator.magicConditionReason(weatherBypassUnit, rainMagic, BattleWeather.CLEAR))

        // Skill 136 bypasses condition 2..5
        val specialBypassUnit = unit(hitPoints = 100, skills = mapOf(136 to 1))
        val specialMagic = magic(condition = 5)
        assertEquals("이 전략의 특수 사용 조건을 충족하지 못했습니다.", MagicDamageCalculator.magicConditionReason(fullHpUnit, specialMagic, BattleWeather.CLEAR))
        assertNull(MagicDamageCalculator.magicConditionReason(specialBypassUnit, specialMagic, BattleWeather.CLEAR))
    }

    @Test
    fun `magicWeatherRate returns 100 when allowed and 85 when restricted`() {
        val rainMagic = magic(condition = 2)
        assertEquals(100, MagicDamageCalculator.magicWeatherRate(rainMagic, BattleWeather.HEAVY_RAIN))
        assertEquals(100, MagicDamageCalculator.magicWeatherRate(rainMagic, BattleWeather.SNOW))
        assertEquals(85, MagicDamageCalculator.magicWeatherRate(rainMagic, BattleWeather.CLEAR))
    }

    @Test
    fun `terrain modifiers adjust offensive and healing magic`() {
        val terrain = BattleTerrainGrid(width = 10, height = 10, rows = List(10) { IntArray(10) { 1 } }) // terrain ID 1
        val terrainFlags = mapOf(1 to (1 shl 0)) // flag has bit 0 set (type 0)
        val u = unit(tileX = 2, tileY = 2)

        val fireMagic = magic(type = 0)
        val waterMagic = magic(type = 1)

        assertEquals(100, MagicDamageCalculator.offensiveMagicTerrainRate(u, fireMagic, terrain, terrainFlags))
        assertEquals(85, MagicDamageCalculator.offensiveMagicTerrainRate(u, waterMagic, terrain, terrainFlags))

        assertEquals(100, MagicDamageCalculator.healingTerrainRate(u, fireMagic, terrain, terrainFlags))
        assertEquals(0, MagicDamageCalculator.healingTerrainRate(u, waterMagic, terrain, terrainFlags))

        val skill19Unit = unit(tileX = 2, tileY = 2, skills = mapOf(19 to 1))
        assertEquals(85, MagicDamageCalculator.healingTerrainRate(skill19Unit, waterMagic, terrain, terrainFlags))
    }

    @Test
    fun `flat additions and rate scaling calculate correctly`() {
        val attacker = unit(attack = 200, skills = mapOf(141 to 50, 107 to 20, 75 to 15))
        val target = unit(skills = mapOf(115 to 10))
        val fireMagic = magic(type = 0)

        // LRHY: 200 * 50 / 100 = 100, plus HXCLZS: 20 -> 120
        assertEquals(120, MagicDamageCalculator.magicFlatSkillDamage(attacker, fireMagic))

        // Base rate 100 + 15 (skill 75) - 10 (target skill 115) = 105
        assertEquals(105, MagicDamageCalculator.magicSkillDamageRate(attacker, target, fireMagic))
    }

    @Test
    fun `status effect and attribute change mapping returns expected domain types`() {
        assertEquals(BattleStatus.CONFUSION, MagicDamageCalculator.statusEffect(8))
        assertEquals(BattleStatus.POISON, MagicDamageCalculator.statusEffect(9))
        assertEquals(BattleStatus.PARALYSIS, MagicDamageCalculator.statusEffect(10))
        assertEquals(BattleStatus.SILENCE, MagicDamageCalculator.statusEffect(11))
        assertNull(MagicDamageCalculator.statusEffect(0))

        assertEquals(BattleAttribute.ATTACK to -1, MagicDamageCalculator.attributeChange(6))
        assertEquals(BattleAttribute.ATTACK to 1, MagicDamageCalculator.attributeChange(19))
        assertEquals(BattleAttribute.DEFENSE to 1, MagicDamageCalculator.attributeChange(20))
        assertNull(MagicDamageCalculator.attributeChange(0))
    }
}

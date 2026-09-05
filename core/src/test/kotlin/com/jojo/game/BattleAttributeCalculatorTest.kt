package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleAttributeCalculator

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattleAttributeCalculatorTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleAttributeCalculatorTest {
    private fun unit(
        attack: Int = 45,
        defense: Int = 25,
        spirit: Int = 35,
        critical: Int = 35,
        morale: Int = 35,
        movement: Int = 3,
        maxHitPoints: Int = 100,
        remoteAttack: Boolean = false,
        skills: Map<Int, Int> = emptyMap(),
        lifts: Map<BattleAttribute, Int> = emptyMap(),
    ) = BattleUnit(
        id = "unit",
        name = "unit",
        faction = Faction.PLAYER,
        tileX = 0,
        tileY = 0,
        attack = attack,
        defense = defense,
        spirit = spirit,
        critical = critical,
        morale = morale,
        movement = movement,
        maxHitPoints = maxHitPoints,
        remoteAttack = remoteAttack,
        skills = skills,
        attributeLifts = lifts.toMutableMap(),
    )

    @Test
    fun `ability support is accumulated before the attribute lift`() {
        val supported = unit(
            attack = 101,
            spirit = 24,
            morale = 19,
            maxHitPoints = 54,
            skills = mapOf(157 to (1 or 8 or 16)),
            lifts = mapOf(BattleAttribute.ATTACK to 1),
        )

        assertEquals(141, BattleAttributeCalculator.effective(supported, BattleAttribute.ATTACK))
        assertEquals(
            82,
            BattleAttributeCalculator.effective(
                unit(critical = 103, lifts = mapOf(BattleAttribute.CRITICAL to -1)),
                BattleAttribute.CRITICAL,
            ),
        )
    }

    @Test
    fun `private defense selects the lowest final combat ability`() {
        val defender = unit(
            attack = 70,
            defense = 80,
            spirit = 50,
            critical = 45,
            morale = 60,
            lifts = mapOf(BattleAttribute.SPIRIT to -1),
        )

        assertEquals(
            80,
            BattleAttributeCalculator.defenseAgainst(unit(), defender, BattleAttribute.DEFENSE),
        )
        assertEquals(
            40,
            BattleAttributeCalculator.defenseAgainst(
                unit(skills = mapOf(165 to 0)),
                defender,
                BattleAttribute.DEFENSE,
            ),
        )
    }

    @Test
    fun `movement applies lift before weather and preserves the heavy rain exception`() {
        val lifted = unit(
            movement = 5,
            lifts = mapOf(BattleAttribute.MOVEMENT to 1),
        )
        assertEquals(7, BattleAttributeCalculator.effectiveMovement(lifted))
        assertEquals(6, BattleAttributeCalculator.finalMovement(lifted, BattleWeather.WINDY))
        assertEquals(6, BattleAttributeCalculator.finalMovement(lifted, BattleWeather.HEAVY_RAIN))

        val rainReady = lifted.copy(skills = mapOf(268 to 0))
        assertEquals(7, BattleAttributeCalculator.finalMovement(rainReady, BattleWeather.HEAVY_RAIN))
        assertEquals(6, BattleAttributeCalculator.finalMovement(rainReady, BattleWeather.WINDY))
        val depleted = unit(movement = 1, lifts = mapOf(BattleAttribute.MOVEMENT to -1))
        assertEquals(0, BattleAttributeCalculator.effectiveMovement(depleted))
        assertEquals(-1, BattleAttributeCalculator.finalMovement(depleted, BattleWeather.WINDY))
    }

    @Test
    fun `ranged resistance is ignored by melee and bypassed by skill 230`() {
        val defender = unit(skills = mapOf(119 to 25))
        val ranged = unit(remoteAttack = true)
        assertEquals(75, BattleAttributeCalculator.physicalDamageAfterResistance(101, ranged, defender))
        assertEquals(101, BattleAttributeCalculator.physicalDamageAfterResistance(101, unit(), defender))
        assertEquals(
            101,
            BattleAttributeCalculator.physicalDamageAfterResistance(
                101,
                unit(remoteAttack = true, skills = mapOf(230 to 0)),
                defender,
            ),
        )
        assertEquals(
            1,
            BattleAttributeCalculator.physicalDamageAfterResistance(
                40,
                ranged,
                unit(skills = mapOf(119 to 150)),
            ),
        )
    }
}

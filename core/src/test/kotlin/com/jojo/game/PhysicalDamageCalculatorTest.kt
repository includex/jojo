// Test
package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.combat.*

import kotlin.test.Test
import kotlin.test.assertEquals

/** PhysicalDamageCalculatorTest: PhysicalDamageCalculator의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class PhysicalDamageCalculatorTest {

    private fun unit(
        id: String = "unit",
        faction: Faction = Faction.PLAYER,
        attack: Int = 100,
        defense: Int = 50,
        spirit: Int = 50,
        critical: Int = 50,
        morale: Int = 50,
        martial: Int = 50,
        level: Int = 10,
        maxHitPoints: Int = 200,
        hitPoints: Int = 200,
        maxMagicPoints: Int = 100,
        magicPoints: Int = 50,
        movement: Int = 5,
        armId: Int = 1,
        armType: Int = 0,
        armMoveSound: Int = 0,
        tileX: Int = 5,
        tileY: Int = 5,
        direction: Int = 0,
        skills: Map<Int, Int> = emptyMap(),
        armRestraints: Map<Int, Int> = emptyMap(),
        statuses: Map<BattleStatus, Int> = emptyMap(),
    ) = BattleUnit(
        id = id,
        name = id,
        faction = faction,
        tileX = tileX,
        tileY = tileY,
        direction = direction,
        attack = attack,
        defense = defense,
        spirit = spirit,
        critical = critical,
        morale = morale,
        martial = martial,
        level = level,
        maxHitPoints = maxHitPoints,
        hitPoints = hitPoints,
        maxMagicPoints = maxMagicPoints,
        magicPoints = magicPoints,
        movement = movement,
        armId = armId,
        armType = armType,
        armMoveSound = armMoveSound,
        skills = skills,
        armRestraints = armRestraints,
        statuses = statuses.toMutableMap(),
    )

    @Test
    fun `base physical damage computes terrain adjusted damage and splash deduction`() {
        val attacker = unit(attack = 120, level = 10)
        val target = unit(defense = 60)

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val normalDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BasePhysicalDamageContext(
                attackTerrainImpact = 110,
                defenseTerrainImpact = 90,
                splash = false,
            ),
        )
        assertEquals(74, normalDamage)

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val splashDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BasePhysicalDamageContext(
                attackTerrainImpact = 110,
                defenseTerrainImpact = 90,
                splash = true,
            ),
        )
        assertEquals(56, splashDamage)
    }

    @Test
    fun `base physical damage honors physical defense rules`() {
        val attacker = unit(attack = 100, skills = mapOf(165 to 0))
        val target = unit(defense = 80, attack = 80, spirit = 40, critical = 80, morale = 80)

        // 테스트 근거: 전투 계산·난수 소비·경계값 (ATTACKER_AWARE)을 검증한다.
        val awareDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BasePhysicalDamageContext(defenseRule = PhysicalDefenseRule.ATTACKER_AWARE),
        )
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (INTRINSIC)을 검증한다.
        val intrinsicDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BasePhysicalDamageContext(defenseRule = PhysicalDefenseRule.INTRINSIC),
        )

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(65, awareDamage)
        assertEquals(45, intrinsicDamage)
    }

    @Test
    fun `enemy base damage has minimum floor based on player unit count`() {
        val enemyAttacker = unit(faction = Faction.ENEMY, attack = 10, maxHitPoints = 500, armType = 2)
        val target = unit(defense = 200)

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val damage = PhysicalDamageCalculator.basePhysicalDamage(
            enemyAttacker,
            target,
            BasePhysicalDamageContext(visiblePlayerUnitCount = 4),
        )
        assertEquals(20, damage)
    }

    @Test
    fun `arm restraint applies base restraint and skills 316 and 133`() {
        val attacker = unit(armRestraints = mapOf(2 to 115))
        val target = unit(armId = 2)

        assertEquals(115, PhysicalDamageCalculator.physicalArmRestraint(attacker, target))

        val attacker133 = attacker.copy(skills = mapOf(133 to 15))
        assertEquals(130, PhysicalDamageCalculator.physicalArmRestraint(attacker133, target))

        val target133 = target.copy(skills = mapOf(133 to 10))
        assertEquals(120, PhysicalDamageCalculator.physicalArmRestraint(attacker133, target133))

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val attacker316 = attacker.copy(skills = mapOf(316 to 0))
        assertEquals(130, PhysicalDamageCalculator.physicalArmRestraint(attacker316, target))

        val target316 = target.copy(skills = mapOf(316 to 0))
        assertEquals(70, PhysicalDamageCalculator.physicalArmRestraint(attacker, target316))
    }

    @Test
    fun `armor piercing and capped damage and minimum floor`() {
        val attacker = unit(skills = mapOf(174 to 40))
        val target = unit(maxHitPoints = 250, skills = mapOf(242 to 70))

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(100, PhysicalDamageCalculator.armorPiercingMinimumDamage(attacker, target, 50))
        assertEquals(120, PhysicalDamageCalculator.armorPiercingMinimumDamage(attacker, target, 120))

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        assertEquals(70, PhysicalDamageCalculator.cappedPhysicalDamage(target, 100))
        assertEquals(50, PhysicalDamageCalculator.cappedPhysicalDamage(target, 50))

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val enemy = unit(faction = Faction.ENEMY, armType = 2, maxHitPoints = 400)
        assertEquals(16, PhysicalDamageCalculator.physicalMinimumDamage(enemy, visibleFamousPlayerCount = 4))
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        val player = unit(faction = Faction.PLAYER, maxHitPoints = 400)
        assertEquals(1, PhysicalDamageCalculator.physicalMinimumDamage(player, visibleFamousPlayerCount = 4))
    }

    @Test
    fun `flat physical damage aggregates skill and stat bonuses`() {
        val attacker = unit(
            hitPoints = 150,
            martial = 60,
            level = 20,
            skills = mapOf(
                9 to 10,   // BIAO_HAN: 150 * 10 / 100 = 15
                183 to 5,  // QXJD: 60 * 5 * 10 / 100 = 30
                26 to 2,   // CHGJ: charge * 2
                25 to 4,   // CFGJ: (moveLength - 1) * 4
                109 to 8,  // JDGJ: adjacentCount * 8
                95 to 0,   // GDZS: 20/2 + 15 = 25
            ),
        )
        val target = unit(
            magicPoints = 80,
            skills = mapOf(
                95 to 0, // GDZS reduction: -(level/2 + 15) = -(10/2 + 15) = -20
            ),
            level = 10,
        )
        // 테스트 근거: 전투 계산·난수 소비·경계값 (DI_FA)을 검증한다.
        val attackerWithDiFa = attacker.copy(skills = attacker.skills + mapOf(33 to 15))

        val context = FlatPhysicalDamageContext(
            activeAttack = true,
            charge = 3,
            moveLength = 4,
            adjacentOccupiedCount = 2,
        )
        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val flat = PhysicalDamageCalculator.physicalFlatSkillDamage(attackerWithDiFa, target, context)
        assertEquals(96, flat)
    }

    @Test
    fun `physical damage rate applies attacker and defender modifiers`() {
        val attacker = unit(
            tileX = 2,
            tileY = 2,
            direction = 0,
            skills = mapOf(
                176 to 15, // QI_LING: if no nearby ally
                129 to 20, // JMGJ: target armMoveSound == 0
                292 to 0,  // Random roll: 10 + bonus
            ),
        )
        val target = unit(
            tileX = 2,
            tileY = 3,
            direction = 0,
            armMoveSound = 0,
            statuses = mapOf(BattleStatus.CONFUSION to 1),
        )

        val context = PhysicalDamageRateContext(
            targetHasNearbyAlly = false,
            targetFinalMovement = 4,
            hasSplashTarget = false,
            hasBackPosition = false,
            incomingDirection = 0,
            skill292RandomBonus = 3,
        )

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val rate = PhysicalDamageCalculator.physicalDamageRate(attacker, target, context)
        assertEquals(158, rate)
    }

    @Test
    fun `physical critical rate computes critical, counter, continuous and direction bonuses`() {
        val attacker = unit(skills = mapOf(271 to 1, 217 to 30))
        val target = unit(direction = 0)

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val critContext = PhysicalCriticalRateContext(
            critical = true,
            incomingDirection = 0,
        )
        assertEquals(210, PhysicalDamageCalculator.physicalCriticalRate(attacker, target, critContext))

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val counterContext = PhysicalCriticalRateContext(
            counter = true,
            counterSkill46Bonus = 15,
            incomingDirection = 1, // orthogonal to 0: bonus - 10 = 20
        )
        // 100 + 15 - 25 + 20 = 110
        assertEquals(110, PhysicalDamageCalculator.physicalCriticalRate(attacker, target, counterContext))

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val comboContext = PhysicalCriticalRateContext(
            continuous = true,
            splash = true,
            incomingDirection = 2, // opposite to 0 (same parity): bonus - 20 = 10
        )
        // 100 - 25 - 20 + 10 = 65
        assertEquals(65, PhysicalDamageCalculator.physicalCriticalRate(attacker, target, comboContext))
    }
}

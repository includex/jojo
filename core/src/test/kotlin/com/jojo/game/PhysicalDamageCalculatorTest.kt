package com.jojo.game
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `PhysicalDamageCalculatorTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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

        // attack = 120 * 110 / 100 = 132
        // defense = 60 * 90 / 100 = 54
        // base = max(1, (132 - 54)/2 + 25 + 10) = 39 + 35 = 74
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

        // splash deducts 25% of base damage: 74 - 74/4 = 74 - 18 = 56
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

        // ATTACKER_AWARE: skill 165 on attacker picks target's lowest attribute (spirit 40)
        val awareDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BasePhysicalDamageContext(defenseRule = PhysicalDefenseRule.ATTACKER_AWARE),
        )
        // INTRINSIC: uses target.defense directly (80)
        val intrinsicDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BasePhysicalDamageContext(defenseRule = PhysicalDefenseRule.INTRINSIC),
        )

        // With defense 40: (100 - 40)/2 + 25 + 10 = 30 + 35 = 65
        // With defense 80: (100 - 80)/2 + 25 + 10 = 10 + 35 = 45
        assertEquals(65, awareDamage)
        assertEquals(45, intrinsicDamage)
    }

    @Test
    fun `enemy base damage has minimum floor based on player unit count`() {
        val enemyAttacker = unit(faction = Faction.ENEMY, attack = 10, maxHitPoints = 500, armType = 2)
        val target = unit(defense = 200)

        // Standard damage would be 1
        // Minimum floor for enemy non-armType 1: max(1, 500 * min(7, 4) / 100) = 20
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

        // Skill 316 overrides all
        val attacker316 = attacker.copy(skills = mapOf(316 to 0))
        assertEquals(130, PhysicalDamageCalculator.physicalArmRestraint(attacker316, target))

        val target316 = target.copy(skills = mapOf(316 to 0))
        assertEquals(70, PhysicalDamageCalculator.physicalArmRestraint(attacker, target316))
    }

    @Test
    fun `armor piercing and capped damage and minimum floor`() {
        val attacker = unit(skills = mapOf(174 to 40))
        val target = unit(maxHitPoints = 250, skills = mapOf(242 to 70))

        // Armor piercing minimum: 40% of 250 = 100
        assertEquals(100, PhysicalDamageCalculator.armorPiercingMinimumDamage(attacker, target, 50))
        assertEquals(120, PhysicalDamageCalculator.armorPiercingMinimumDamage(attacker, target, 120))

        // Capped damage: min(damage, 70)
        assertEquals(70, PhysicalDamageCalculator.cappedPhysicalDamage(target, 100))
        assertEquals(50, PhysicalDamageCalculator.cappedPhysicalDamage(target, 50))

        // Minimum damage for enemy
        val enemy = unit(faction = Faction.ENEMY, armType = 2, maxHitPoints = 400)
        assertEquals(16, PhysicalDamageCalculator.physicalMinimumDamage(enemy, visibleFamousPlayerCount = 4))
        // Player side always minimum 1
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
        // Add DI_FA skill 33 on attacker: 80 * 15 / 100 = 12
        val attackerWithDiFa = attacker.copy(skills = attacker.skills + mapOf(33 to 15))

        val context = FlatPhysicalDamageContext(
            activeAttack = true,
            charge = 3,
            moveLength = 4,
            adjacentOccupiedCount = 2,
        )
        // 15 (skill 9) + 12 (skill 33) + 30 (skill 183) + 6 (skill 26: 3*2) + 12 (skill 25: 3*4) + 16 (skill 109: 2*8) + 25 (att 95) - 20 (tgt 95) = 96
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

        // Base: 100
        // Confusion: +10
        // Skill 176: +15
        // Skill 129: +20
        // Skill 292: +10 + 3 = +13
        // Total = 100 + 10 + 15 + 20 + 13 = 158
        val rate = PhysicalDamageCalculator.physicalDamageRate(attacker, target, context)
        assertEquals(158, rate)
    }

    @Test
    fun `physical critical rate computes critical, counter, continuous and direction bonuses`() {
        val attacker = unit(skills = mapOf(271 to 1, 217 to 30))
        val target = unit(direction = 0)

        // Critical: +50, skill 271 != 0: +30 => 180
        // Skill 217 incomingDirection == target.direction: +30 => 210
        val critContext = PhysicalCriticalRateContext(
            critical = true,
            incomingDirection = 0,
        )
        assertEquals(210, PhysicalDamageCalculator.physicalCriticalRate(attacker, target, critContext))

        // Counter without skill 181: -25, plus counterSkill46Bonus
        val counterContext = PhysicalCriticalRateContext(
            counter = true,
            counterSkill46Bonus = 15,
            incomingDirection = 1, // orthogonal to 0: bonus - 10 = 20
        )
        // 100 + 15 - 25 + 20 = 110
        assertEquals(110, PhysicalDamageCalculator.physicalCriticalRate(attacker, target, counterContext))

        // Continuous without skill 291: -25
        // Splash: -20
        val comboContext = PhysicalCriticalRateContext(
            continuous = true,
            splash = true,
            incomingDirection = 2, // opposite to 0 (same parity): bonus - 20 = 10
        )
        // 100 - 25 - 20 + 10 = 65
        assertEquals(65, PhysicalDamageCalculator.physicalCriticalRate(attacker, target, comboContext))
    }
}

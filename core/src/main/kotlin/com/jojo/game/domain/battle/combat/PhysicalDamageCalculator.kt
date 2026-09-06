// Battle
package com.jojo.game.domain.battle.combat

import com.jojo.game.domain.battle.*

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleAttributeCalculator

import kotlin.math.abs

internal data class BasePhysicalDamageContext(
    val attackTerrainImpact: Int = 100,
    val defenseTerrainImpact: Int = 100,
    val visiblePlayerUnitCount: Int = 0,
    val splash: Boolean = false,
    val defenseRule: PhysicalDefenseRule = PhysicalDefenseRule.ATTACKER_AWARE,
)

internal enum class PhysicalDefenseRule { ATTACKER_AWARE, INTRINSIC }

internal data class FlatPhysicalDamageContext(
    val activeAttack: Boolean = false,
    val charge: Int = 0,
    val moveLength: Int = 0,
    val adjacentOccupiedCount: Int = 0,
)

internal data class PhysicalDamageRateContext(
    val targetHasNearbyAlly: Boolean,
    val targetFinalMovement: Int,
    val hasSplashTarget: Boolean,
    val hasBackPosition: Boolean,
    val incomingDirection: Int,
    val skill292RandomBonus: Int? = null,
)

internal data class PhysicalCriticalRateContext(
    val critical: Boolean = false,
    val counter: Boolean = false,
    val continuous: Boolean = false,
    val splash: Boolean = false,
    val incomingDirection: Int,
    val counterSkill46Bonus: Int = 0,
)

/** PhysicalDamageCalculator: 물리 피해 계산기이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
internal object PhysicalDamageCalculator {
    fun basePhysicalDamage(
        attacker: BattleUnit,
        target: BattleUnit,
        context: BasePhysicalDamageContext,
    ): Int {
        val attack = BattleAttributeCalculator.effective(attacker, BattleAttribute.ATTACK) *
                context.attackTerrainImpact / 100
        val targetDefense = when (context.defenseRule) {
            PhysicalDefenseRule.ATTACKER_AWARE ->
                BattleAttributeCalculator.defenseAgainst(attacker, target, BattleAttribute.DEFENSE)

            PhysicalDefenseRule.INTRINSIC ->
                BattleAttributeCalculator.effective(target, BattleAttribute.DEFENSE)
        }
        val defense = targetDefense * context.defenseTerrainImpact / 100
        var damage = maxOf(1, (attack - defense) / 2 + 25 + attacker.level)
        if (context.splash) damage -= damage / 4
        val minimum = if (!attacker.isPlayerSide() && attacker.armType != 1) {
            maxOf(1, attacker.maxHitPoints * minOf(7, context.visiblePlayerUnitCount) / 100)
        } else {
            1
        }
        return maxOf(minimum, damage)
    }

    fun armorPiercingMinimumDamage(attacker: BattleUnit, target: BattleUnit, currentDamage: Int): Int {
        val percent = effect(attacker, 174) ?: return currentDamage
        return maxOf(currentDamage, percent * target.maxHitPoints / 100)
    }

    fun cappedPhysicalDamage(target: BattleUnit, currentDamage: Int): Int =
        effect(target, 242)?.let { minOf(currentDamage, it) } ?: currentDamage

    fun physicalMinimumDamage(attacker: BattleUnit, visibleFamousPlayerCount: Int): Int {
        if (attacker.isPlayerSide() || attacker.armType == 1) return 1
        return maxOf(1, attacker.maxHitPoints * minOf(7, visibleFamousPlayerCount) / 100)
    }

    fun physicalArmRestraint(attacker: BattleUnit, target: BattleUnit): Int {
        if (hasSkill(attacker, 316)) return 130
        if (hasSkill(target, 316)) return 70
        return (attacker.armRestraints[target.armId] ?: 100) +
                (effect(attacker, 133) ?: 0) -
                (effect(target, 133) ?: 0)
    }

    fun physicalFlatSkillDamage(
        attacker: BattleUnit,
        target: BattleUnit,
        context: FlatPhysicalDamageContext,
    ): Int {
        var addition = 0
        effect(attacker, 9)?.let { addition += attacker.hitPoints * it / 100 }
        effect(attacker, 141)?.let {
            addition += BattleAttributeCalculator.effective(attacker, BattleAttribute.SPIRIT) * it / 100
        }
        effect(attacker, 33)?.let { addition += target.magicPoints * it / 100 }
        effect(attacker, 183)?.let { addition += attacker.martial * it * 10 / 100 }
        if (context.activeAttack) effect(attacker, 26)?.let { addition += context.charge * it }
        if (context.activeAttack && context.moveLength >= 2) {
            effect(attacker, 25)?.let { addition += (context.moveLength - 1) * it }
        }
        effect(attacker, 109)?.let { addition += it * context.adjacentOccupiedCount }
        if (hasSkill(attacker, 95)) addition += attacker.level / 2 + 15
        if (hasSkill(target, 95)) addition -= target.level / 2 + 15
        listOf(
            80 to BattleAttribute.ATTACK,
            79 to BattleAttribute.DEFENSE,
            81 to BattleAttribute.SPIRIT,
            78 to BattleAttribute.CRITICAL,
            83 to BattleAttribute.MORALE,
        ).forEach { (skill, attribute) ->
            effect(attacker, skill)?.let {
                addition += BattleAttributeCalculator.effective(attacker, attribute) * it / 100
            }
        }
        return addition
    }

    fun physicalDamageRate(
        attacker: BattleUnit,
        target: BattleUnit,
        context: PhysicalDamageRateContext,
    ): Int {
        var rate = 100
        if (BattleStatus.CONFUSION in target.statuses) rate += 10
        if (!context.targetHasNearbyAlly) effect(attacker, 176)?.let { rate += it }
        when (target.armMoveSound) {
            0 -> effect(attacker, 129)?.let { rate += it }
            1 -> effect(attacker, 164)?.let { rate += it }
            2 -> effect(attacker, 11)?.let { rate += it }
        }
        if (attacker.statuses.isNotEmpty()) effect(attacker, 99)?.let { rate += it }
        effect(attacker, 110)?.let { rate += (14 - context.targetFinalMovement) * it }
        effect(attacker, 312)?.let { value ->
            rate += 5 * (value - if (BattleStatus.PARALYSIS in target.statuses) 0 else context.targetFinalMovement)
        }
        effect(attacker, 104)?.let { value ->
            rate += directionalAttackBonus(value, context.incomingDirection, attacker.direction)
        }
        if (!context.hasSplashTarget) effect(attacker, 126)?.let { rate += it }
        val dx = abs(attacker.tileX - target.tileX)
        val dy = abs(attacker.tileY - target.tileY)
        val sameLine = dx == 0 || dy == 0
        if (sameLine && dx + dy < 3) effect(attacker, 234)?.let { rate += it }
        effect(attacker, 184)?.let { rate += 5 * (14 - target.movement) }
        if (!context.hasBackPosition) effect(attacker, 221)?.let { rate += it }
        effect(attacker, 114)?.let { rate += it }
        effect(attacker, 292)?.let { rate += 10 + requireNotNull(context.skill292RandomBonus) }
        if (sameLine) effect(target, 6)?.let { rate -= it }
        if (!sameLine) effect(target, 121)?.let { rate -= it }
        if (dx == 1 && dy == 1) effect(target, 132)?.let { rate -= it }
        effect(target, 118)?.let { rate -= it }
        effect(target, 245)?.let {
            rate -= (target.maxHitPoints - target.hitPoints) * 100 / target.maxHitPoints.coerceAtLeast(1)
        }
        effect(target, 247)?.let { rate += target.movement * it }
        if (attacker.armMoveSound == 0) effect(target, 139)?.let { rate -= it }
        effect(target, 250)?.let { rate -= if (context.hasBackPosition) it else it / 2 }
        effect(target, 275)?.let { value ->
            rate -= directionalDefenseReduction(value, context.incomingDirection, target.direction)
        }
        return rate
    }

    fun physicalCriticalRate(
        attacker: BattleUnit,
        target: BattleUnit,
        context: PhysicalCriticalRateContext,
    ): Int {
        var rate = 100
        if (context.critical) {
            rate += 50
            if ((attacker.skills[271]?.and(255) ?: 255) != 0) rate += 30
        }
        if (context.counter) {
            rate += context.counterSkill46Bonus
            if (!hasSkill(attacker, 181)) rate -= 25
        }
        if (context.continuous && !hasSkill(attacker, 291)) rate -= 25
        if (context.splash) rate -= 20
        effect(attacker, 217)?.let { bonus ->
            rate += when {
                context.incomingDirection == target.direction -> bonus
                context.incomingDirection % 2 == target.direction % 2 -> bonus - 20
                else -> bonus - 10
            }
        }
        return rate
    }

    private fun directionalAttackBonus(value: Int, incoming: Int, facing: Int): Int = when {
        incoming == facing -> value / 3
        incoming % 2 != facing % 2 -> value / 2
        else -> value
    }

    private fun directionalDefenseReduction(value: Int, incoming: Int, facing: Int): Int = when {
        incoming == facing -> value / 3
        incoming % 2 == facing % 2 -> 0
        else -> 2 * (value / 3)
    }

    private fun effect(unit: BattleUnit, skillId: Int): Int? =
        unit.skills[skillId]?.and(255)?.takeIf { it != 255 }

    fun calculatePhysicalDamage(
        attacker: BattleUnit,
        target: BattleUnit,
        baseDamage: Int,
        damageRateContext: PhysicalDamageRateContext,
        flatContext: FlatPhysicalDamageContext,
        criticalRateContext: PhysicalCriticalRateContext,
        visibleFamousPlayerCount: Int,
        overrideDamage: Int? = null,
        bonusFlatDamage: Int = 0,
    ): Int {
        if (overrideDamage == 0) return 0
        var normalDamage = overrideDamage?.coerceAtLeast(0)
            ?: maxOf(1, baseDamage * physicalArmRestraint(attacker, target) / 100)
        normalDamage = normalDamage * physicalDamageRate(attacker, target, damageRateContext) / 100
        normalDamage = BattleAttributeCalculator.physicalDamageAfterResistance(normalDamage, attacker, target)
        normalDamage += physicalFlatSkillDamage(attacker, target, flatContext) + bonusFlatDamage
        normalDamage = maxOf(1, normalDamage)
        normalDamage = armorPiercingMinimumDamage(attacker, target, normalDamage)
        normalDamage = cappedPhysicalDamage(target, normalDamage)
        return maxOf(
            physicalMinimumDamage(attacker, visibleFamousPlayerCount),
            normalDamage * physicalCriticalRate(attacker, target, criticalRateContext) / 100,
        )
    }

    private fun hasSkill(unit: BattleUnit, skillId: Int): Boolean = effect(unit, skillId) != null
}

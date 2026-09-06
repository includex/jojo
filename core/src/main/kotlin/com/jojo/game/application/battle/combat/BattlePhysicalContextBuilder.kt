package com.jojo.game.application.battle.combat

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.BattleMrspDamage
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge
import com.jojo.game.domain.battle.BattleAttributeCalculator

internal data class BattlePhysicalContextEnvironment(
    val units: () -> Collection<BattleUnit>,
    val unitAt: (Int, Int) -> BattleUnit?,
    val terrain: BattleTerrainGrid?,
    val weather: () -> BattleWeather,
    val infantryOffsets: Set<Pair<Int, Int>>,
    val skillTemp: (String, Int, Int) -> Int,
    val setSkillTemp: (String, Int, Int) -> Unit,
    val incSkillTemp: (String, Int) -> Int,
    val moveLength: () -> Int,
    val backPosition: (BattleUnit, BattleUnit) -> Pair<Int, Int>?,
    val facingDirection: (Int, Int, Int, Int) -> Int,
    val hasPhysicalEffectTargets: (BattleUnit, BattleUnit) -> Boolean,
    val probabilityResolver: BattleProbabilityResolver,
)

internal object BattlePhysicalContextBuilder {

    fun basePhysicalDamageContext(
        attacker: BattleUnit,
        target: BattleUnit,
        splash: Boolean,
        defenseRule: PhysicalDefenseRule = PhysicalDefenseRule.ATTACKER_AWARE,
        env: BattlePhysicalContextEnvironment,
    ): BasePhysicalDamageContext = BasePhysicalDamageContext(
        attackTerrainImpact = attacker.terrainImpacts[env.terrain?.terrainAt(attacker.tileX, attacker.tileY)] ?: 100,
        defenseTerrainImpact = target.terrainImpacts[env.terrain?.terrainAt(target.tileX, target.tileY)] ?: 100,
        visiblePlayerUnitCount = env.units().count { it.visible && it.isPlayerSide() },
        splash = splash,
        defenseRule = defenseRule,
    )

    fun flatPhysicalDamageContext(
        attacker: BattleUnit,
        activeAttack: Boolean,
        env: BattlePhysicalContextEnvironment,
    ): FlatPhysicalDamageContext = FlatPhysicalDamageContext(
        activeAttack = activeAttack,
        charge = if (activeAttack) env.skillTemp(attacker.id, 26, 0) else 0,
        moveLength = env.moveLength(),
        adjacentOccupiedCount = env.infantryOffsets.count { (dx, dy) ->
            env.unitAt(attacker.tileX + dx, attacker.tileY + dy)?.visible == true
        },
    )


    fun visibleFamousPlayerCount(env: BattlePhysicalContextEnvironment): Int =
        env.units().count { it.visible && it.isPlayerSide() && it.famous }


    fun consumeMpAttackSkill(attacker: BattleUnit) {
        if (attacker.skills[4]?.and(255)?.let { it != 255 } == true) attacker.addMpcur(-1)
    }


    fun consumeXuShiDamage(attacker: BattleUnit, env: BattlePhysicalContextEnvironment): Int {
        val effect = attacker.skills[243]?.and(255)?.takeIf { it != 255 } ?: return 0
        val stored = env.skillTemp(attacker.id, 243, 0)
        if (stored < 1) return 0
        env.setSkillTemp(attacker.id, 243, 0)
        return stored * effect
    }


    fun accumulateChargeWhenHit(defender: BattleUnit, activeAttack: Boolean, env: BattlePhysicalContextEnvironment) {
        if (activeAttack && defender.skills[26]?.and(255)?.let { it != 255 } == true) {
            env.incSkillTemp(defender.id, 26)
        }
    }


    fun mrspDamage(attacker: BattleUnit, target: BattleUnit, random100: () -> Int): Int? {
        if (attacker.skills[156]?.and(255)?.let { it != 255 } != true) return null
        return target.maxHitPoints * BattleMrspDamage.percent(random100()) / 100
    }

    fun physicalDamageRateContext(
        attacker: BattleUnit,
        target: BattleUnit,
        env: BattlePhysicalContextEnvironment,
    ): PhysicalDamageRateContext {
        val targetIsPlayerSide = target.isPlayerSide()
        val targetHasNearbyAlly = env.infantryOffsets.any { (dx, dy) ->
            env.unitAt(target.tileX + dx, target.tileY + dy)?.let { it.isPlayerSide() == targetIsPlayerSide } == true
        }
        val hasBackPosition = env.backPosition(target, attacker) != null
        val skill292RandomBonus = attacker.skills[292]?.and(255)?.takeIf { it != 255 }
            ?.let { env.probabilityResolver.flagRandom(0, 5) }
        return PhysicalDamageRateContext(
            targetHasNearbyAlly = targetHasNearbyAlly,
            targetFinalMovement = BattleAttributeCalculator.finalMovement(target, env.weather()),
            hasSplashTarget = env.hasPhysicalEffectTargets(attacker, target),
            hasBackPosition = hasBackPosition,
            incomingDirection = env.facingDirection(attacker.tileX, attacker.tileY, target.tileX, target.tileY),
            skill292RandomBonus = skill292RandomBonus,
        )
    }

    fun physicalCriticalRateContext(
        attacker: BattleUnit,
        target: BattleUnit,
        critical: Boolean,
        counter: Boolean = false,
        continuous: Boolean = false,
        splash: Boolean = false,
        env: BattlePhysicalContextEnvironment,
    ): PhysicalCriticalRateContext {
        var counterSkill46Bonus = 0
        if (counter) {
            attacker.skills[46]?.and(255)?.takeIf { it != 255 }?.let { bonus ->
                if (env.skillTemp(attacker.id, 46, 0) != 0) {
                    env.setSkillTemp(attacker.id, 46, 0)
                    counterSkill46Bonus = bonus
                }
            }
        }
        return PhysicalCriticalRateContext(
            critical = critical,
            counter = counter,
            continuous = continuous,
            splash = splash,
            incomingDirection = env.facingDirection(attacker.tileX, attacker.tileY, target.tileX, target.tileY),
            counterSkill46Bonus = counterSkill46Bonus,
        )
    }
}

package com.jojo.game.domain.battle.combat

import com.jojo.game.domain.battle.*

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge

internal data class PhysicalCombatEnvironment(
    val probabilityResolver: BattleProbabilityResolver,
    val units: () -> Collection<BattleUnit>,
    val unitAt: (x: Int, y: Int) -> BattleUnit?,
    val areAllied: (BattleUnit, BattleUnit) -> Boolean,
    val canAttack: (BattleUnit, BattleUnit) -> Boolean,
    val facingDirection: (fromX: Int, fromY: Int, toX: Int, toY: Int) -> Int,
    val visibleFamousPlayerCount: () -> Int,
    val basePhysicalDamageContext: (attacker: BattleUnit, target: BattleUnit, splash: Boolean, defenseRule: PhysicalDefenseRule) -> BasePhysicalDamageContext,
    val physicalDamageRateContext: (attacker: BattleUnit, target: BattleUnit) -> PhysicalDamageRateContext,
    val physicalCriticalRateContext: (attacker: BattleUnit, target: BattleUnit, critical: Boolean, counter: Boolean, continuous: Boolean, splash: Boolean) -> PhysicalCriticalRateContext,
    val flatPhysicalDamageContext: (attacker: BattleUnit, activeAttack: Boolean) -> FlatPhysicalDamageContext,
    val rollAttackStatusBatch: (attacker: BattleUnit) -> AttackStatusBatch,
    val resolvePhysicalTarget: (attacker: BattleUnit, target: BattleUnit, resolvedHarm: Int, statuses: AttackStatusBatch, activeAttack: Boolean) -> PhysicalAttackTargetResult,
    val resolveCriticalSpeech: (unit: BattleUnit, critical: Boolean) -> String?,
    val castReactionMagic: (caster: BattleUnit, target: BattleUnit, magicId: Int) -> TacticalActionResult.Magic?,
    val battleExperience: (attacker: BattleUnit, target: BattleUnit, defeated: Boolean) -> Int,
    val equipmentExperienceAmount: (recipient: BattleUnit, opponent: BattleUnit, resolvedHarm: Int, kind: BattleEquipmentExperienceKind) -> Int,
    val notifyBattleExperience: (unit: BattleUnit, amount: Int) -> Unit,
    val notifyEquipmentExperienceAward: (recipient: BattleUnit, opponent: BattleUnit, amount: Int, kind: BattleEquipmentExperienceKind) -> Unit,
    val notifyPhysicalDamage: (attacker: BattleUnit, target: BattleUnit, damage: Int) -> Unit,
    val notifyUnitDefeated: (attacker: BattleUnit, target: BattleUnit) -> Unit,
    val onDefeat: (unitId: String) -> Unit,
    val consumeXuShiDamage: (attacker: BattleUnit) -> Int,
    val consumeMpAttackSkill: (attacker: BattleUnit) -> Unit,
    val mrspDamage: (attacker: BattleUnit, target: BattleUnit) -> Int?,
)

/**
 * Orchestrates multi-pass physical combat resolution: active attacks, continuous/follow-ups,
 * counter attacks, counter follow-ups, damage transfers, and splash attacks.
 */
internal object PhysicalCombatResolver {

    fun executeAttack(
        attacker: BattleUnit,
        target: BattleUnit,
        damage: Int? = null,
        env: PhysicalCombatEnvironment,
    ): TacticalActionResult.Attack {
        val accumulator = CombatSettlementAccumulator(env)
        val plannedContinuousAttack = env.probabilityResolver.continuousAttack(attacker, target)
        val attackStatusBatch = env.rollAttackStatusBatch(attacker)
        val criticalRoll = damage == null && env.probabilityResolver.criticalHit(attacker, target)
        val hitRate = env.probabilityResolver.physicalHitRate(attacker, target)
        val hit = env.probabilityResolver.physicalHit(attacker, target, hitRate)
        val baseDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            env.basePhysicalDamageContext(attacker, target, false, PhysicalDefenseRule.ATTACKER_AWARE),
        )
        val critical = hit && criticalRoll &&
                !(target.skills[49]?.and(255)?.let { it != 255 } == true && attacker.skills[227]?.and(255)
                    ?.let { it != 255 } != true)
        val xuShiDamage = if (damage == null) env.consumeXuShiDamage(attacker) else 0
        val specialDamage = if (hit && damage == null) env.mrspDamage(attacker, target) else null
        val resolvedDamage = if (hit) {
            specialDamage ?: PhysicalDamageCalculator.calculatePhysicalDamage(
                attacker = attacker,
                target = target,
                baseDamage = baseDamage,
                damageRateContext = env.physicalDamageRateContext(attacker, target),
                flatContext = env.flatPhysicalDamageContext(attacker, true),
                criticalRateContext = env.physicalCriticalRateContext(attacker, target, critical, false, false, false),
                visibleFamousPlayerCount = env.visibleFamousPlayerCount(),
                overrideDamage = damage,
                bonusFlatDamage = xuShiDamage,
            )
        } else 0
        if (hit && specialDamage == null) env.consumeMpAttackSkill(attacker)

        val primarySplashHarms = if (damage == null) {
            PhysicalAttackAreaResolver.computePhysicalSplashHarms(attacker, target, criticalRoll, env = env)
        } else emptyList()

        val primaryRecord = accumulator.recordPass(
            kind = PhysicalAttackPassKind.ACTIVE,
            passAttacker = attacker,
            passTarget = target,
            criticalRoll = criticalRoll,
            resolvedHarm = resolvedDamage,
            hit = hit,
            attackStatusBatch = attackStatusBatch,
            splashHarms = primarySplashHarms,
            isCounter = false,
            isActiveAttack = damage == null,
            collectSplashTargets = true,
        )
        val primaryResolution = primaryRecord.result
        val primaryResolvedHarm = primaryRecord.primaryResolvedHarm

        val primaryHpDamage = when {
            primaryResolution.mpShieldDamage > 0 -> 0
            primaryResolution.moneyShieldSpent > 0 -> primaryResolution.damage
            else -> primaryResolvedHarm
        }
        val mpShieldDamage = primaryResolution.mpShieldDamage
        attacker.markActionComplete()
        var defeated = target.hitPoints <= 0
        var followUpDamage = 0
        var followUpMpShieldDamage = 0
        var followUpCritical = false

        val criticalFollowUp = criticalRoll && attacker.skills[7]?.and(255)?.let { it != 255 } == true
        if (attacker.hitPoints > 0 && !defeated && (plannedContinuousAttack || criticalFollowUp)) {
            val followUpCriticalRoll = env.probabilityResolver.criticalHit(attacker, target)
            val followUpHit = target.skills[47]?.and(255)?.let { it != 255 } != true &&
                    env.probabilityResolver.physicalHit(attacker, target, hitRate)
            val followUpIsCritical = followUpHit && followUpCriticalRoll
            followUpCritical = followUpIsCritical
            val followUpSpecialDamage = if (followUpHit) env.mrspDamage(attacker, target) else null
            val followUpResolvedHarm = if (followUpHit) {
                followUpSpecialDamage ?: PhysicalDamageCalculator.calculatePhysicalDamage(
                    attacker = attacker,
                    target = target,
                    baseDamage = baseDamage,
                    damageRateContext = env.physicalDamageRateContext(attacker, target),
                    flatContext = env.flatPhysicalDamageContext(attacker, false),
                    criticalRateContext = env.physicalCriticalRateContext(
                        attacker,
                        target,
                        followUpIsCritical,
                        false,
                        true,
                        false
                    ),
                    visibleFamousPlayerCount = env.visibleFamousPlayerCount(),
                )
            } else 0
            if (followUpHit && followUpSpecialDamage == null) env.consumeMpAttackSkill(attacker)
            val followUpSplashHarms = if (damage == null) {
                PhysicalAttackAreaResolver.computePhysicalSplashHarms(
                    attacker, target, followUpCriticalRoll, activeAttack = true, continuous = true, env = env,
                )
            } else emptyList()

            val followUpRecord = accumulator.recordPass(
                kind = PhysicalAttackPassKind.ACTIVE_FOLLOW_UP,
                passAttacker = attacker,
                passTarget = target,
                criticalRoll = followUpCriticalRoll,
                resolvedHarm = followUpResolvedHarm,
                hit = followUpHit,
                attackStatusBatch = attackStatusBatch,
                splashHarms = followUpSplashHarms,
                isCounter = false,
                isActiveAttack = damage == null,
                collectSplashTargets = true,
            )
            val followUpPrimary = followUpRecord.result
            followUpDamage = followUpPrimary.damage
            followUpMpShieldDamage = followUpPrimary.mpShieldDamage
            defeated = target.hitPoints <= 0
        }

        val counterMagic = target.skills[13]?.and(255)?.takeIf { it != 255 }
            ?.let { magicId -> env.castReactionMagic(target, attacker, magicId) }
        val canCounter = counterMagic == null && attacker.hitPoints > 0 && !defeated && target.visible &&
                attacker.skills[226]?.and(255)?.let { it == 255 } != false && env.canAttack(target, attacker) &&
                BattleStatus.PARALYSIS !in target.statuses && BattleStatus.CONFUSION !in target.statuses
        var counterDamage = 0
        var counterFollowUpDamage = 0
        var counterMpShieldDamage = 0
        var counterFollowUpMpShieldDamage = 0
        var counterCriticalResult = false
        var counterFollowUpCritical = false

        if (canCounter) {
            val counterStatusBatch = env.rollAttackStatusBatch(target)
            val counterHitRate = env.probabilityResolver.physicalHitRate(target, attacker)
            val counterCriticalRoll = env.probabilityResolver.criticalHit(target, attacker)
            val counterHit = env.probabilityResolver.physicalHit(target, attacker, counterHitRate)
            val counterBase = PhysicalDamageCalculator.basePhysicalDamage(
                target,
                attacker,
                env.basePhysicalDamageContext(target, attacker, false, PhysicalDefenseRule.ATTACKER_AWARE),
            )
            val counterCritical = counterHit && counterCriticalRoll
            counterCriticalResult = counterCritical
            val counterResolvedHarm = if (counterHit) {
                PhysicalDamageCalculator.calculatePhysicalDamage(
                    attacker = target,
                    target = attacker,
                    baseDamage = counterBase,
                    damageRateContext = env.physicalDamageRateContext(target, attacker),
                    flatContext = env.flatPhysicalDamageContext(target, false),
                    criticalRateContext = env.physicalCriticalRateContext(
                        target,
                        attacker,
                        counterCritical,
                        true,
                        false,
                        false
                    ),
                    visibleFamousPlayerCount = env.visibleFamousPlayerCount(),
                )
            } else 0
            if (counterHit) env.consumeMpAttackSkill(target)

            val counterSplashHarms = PhysicalAttackAreaResolver.computePhysicalSplashHarms(
                attacker = target, primaryTarget = attacker, critical = counterCriticalRoll,
                activeAttack = false, counter = true, continuous = false, env = env,
            )
            val counterRecord = accumulator.recordPass(
                kind = PhysicalAttackPassKind.COUNTER,
                passAttacker = target,
                passTarget = attacker,
                criticalRoll = counterCriticalRoll,
                resolvedHarm = counterResolvedHarm,
                hit = counterHit,
                attackStatusBatch = counterStatusBatch,
                splashHarms = counterSplashHarms,
                isCounter = true,
                isActiveAttack = false,
                collectSplashTargets = false,
            )
            val counterPrimary = counterRecord.result
            counterDamage = counterPrimary.damage
            counterMpShieldDamage = counterPrimary.mpShieldDamage

            val forcedCounterFollowUp =
                listOf(197, 43).any { target.skills[it]?.and(255)?.let { value -> value != 255 } == true } ||
                        (counterCriticalRoll && target.skills[7]?.and(255)?.let { value -> value != 255 } == true)
            if (attacker.hitPoints > 0 && forcedCounterFollowUp) {
                val secondCriticalRoll = env.probabilityResolver.criticalHit(target, attacker)
                val secondHit = env.probabilityResolver.physicalHit(target, attacker, counterHitRate)
                counterFollowUpCritical = secondHit && secondCriticalRoll
                val counterFollowUpResolvedHarm = if (secondHit) {
                    PhysicalDamageCalculator.calculatePhysicalDamage(
                        attacker = target,
                        target = attacker,
                        baseDamage = counterBase,
                        damageRateContext = env.physicalDamageRateContext(target, attacker),
                        flatContext = env.flatPhysicalDamageContext(target, false),
                        criticalRateContext = env.physicalCriticalRateContext(
                            target,
                            attacker,
                            secondCriticalRoll,
                            true,
                            true,
                            false
                        ),
                        visibleFamousPlayerCount = env.visibleFamousPlayerCount(),
                    )
                } else 0
                if (secondHit) env.consumeMpAttackSkill(target)

                val counterFollowUpSplashHarms = PhysicalAttackAreaResolver.computePhysicalSplashHarms(
                    attacker = target, primaryTarget = attacker, critical = secondCriticalRoll,
                    activeAttack = false, counter = true, continuous = true, env = env,
                )
                val secondRecord = accumulator.recordPass(
                    kind = PhysicalAttackPassKind.COUNTER_FOLLOW_UP,
                    passAttacker = target,
                    passTarget = attacker,
                    criticalRoll = secondCriticalRoll,
                    resolvedHarm = counterFollowUpResolvedHarm,
                    hit = secondHit,
                    attackStatusBatch = counterStatusBatch,
                    splashHarms = counterFollowUpSplashHarms,
                    isCounter = true,
                    isActiveAttack = false,
                    collectSplashTargets = false,
                )
                val secondPrimary = secondRecord.result
                counterFollowUpDamage = secondPrimary.damage
                counterFollowUpMpShieldDamage = secondPrimary.mpShieldDamage
            }
        }

        val attackerDefeated = attacker.hitPoints <= 0
        accumulator.applySettlement(attacker)

        return TacticalActionResult.Attack(
            damage = primaryHpDamage,
            defeated = defeated,
            hitRate = hitRate,
            hit = hit,
            critical = critical,
            counterDamage = counterDamage,
            attackerDefeated = attackerDefeated,
            lifeStealHealing = accumulator.lifeStealHealing,
            followUpDamage = followUpDamage,
            followUpMpShieldDamage = followUpMpShieldDamage,
            counterFollowUpDamage = counterFollowUpDamage,
            counterMpShieldDamage = counterMpShieldDamage,
            counterFollowUpMpShieldDamage = counterFollowUpMpShieldDamage,
            counterLifeStealHealing = accumulator.counterLifeStealHealing,
            followUpCritical = followUpCritical,
            counterCritical = counterCriticalResult,
            counterFollowUpCritical = counterFollowUpCritical,
            splashTargets = accumulator.splashTargets,
            mpShieldDamage = mpShieldDamage,
            qxlHealing = accumulator.qxlHealing,
            recoilDamage = accumulator.recoilDamage,
            blockRetaliationDamage = accumulator.blockRetaliationDamage,
            moneyShieldSpent = accumulator.moneyShieldSpent,
            playerMoneyDelta = accumulator.playerMoneyDelta,
            enemyMoneyDelta = accumulator.enemyMoneyDelta,
            counterMagic = counterMagic,
            counterMagicId = counterMagic?.let { target.skills[13]?.and(255) },
            automaticProperty = accumulator.automaticProperty,
            physicalPasses = accumulator.physicalPasses,
        )
    }

}

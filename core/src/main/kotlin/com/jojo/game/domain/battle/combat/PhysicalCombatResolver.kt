// Battle
package com.jojo.game.domain.battle.combat

import com.jojo.game.domain.battle.*

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge

/**
 * `PhysicalCombatEnvironment` 클래스: combat 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

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

/** PhysicalCombatResolver: 물리 전투 처리 판별기이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
internal object PhysicalCombatResolver {

    /**
     * `executeAttack`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun executeAttack(
        attacker: BattleUnit,
        target: BattleUnit,
        damage: Int? = null,
        env: PhysicalCombatEnvironment,
    ): TacticalActionResult.Attack {
        /**
         * `accumulator` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val accumulator = CombatSettlementAccumulator(env)
        /**
         * `plannedContinuousAttack` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val plannedContinuousAttack = env.probabilityResolver.continuousAttack(attacker, target)
        /**
         * `attackStatusBatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attackStatusBatch = env.rollAttackStatusBatch(attacker)
        /**
         * `criticalRoll` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val criticalRoll = damage == null && env.probabilityResolver.criticalHit(attacker, target)
        /**
         * `hitRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitRate = env.probabilityResolver.physicalHitRate(attacker, target)
        /**
         * `hit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hit = env.probabilityResolver.physicalHit(attacker, target, hitRate)
        /**
         * `baseDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val baseDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            env.basePhysicalDamageContext(attacker, target, false, PhysicalDefenseRule.ATTACKER_AWARE),
        )
        /**
         * `critical` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val critical = hit && criticalRoll &&
                !(target.skills[49]?.and(255)?.let { it != 255 } == true && attacker.skills[227]?.and(255)
                    ?.let { it != 255 } != true)
        /**
         * `xuShiDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val xuShiDamage = if (damage == null) env.consumeXuShiDamage(attacker) else 0
        /**
         * `specialDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val specialDamage = if (hit && damage == null) env.mrspDamage(attacker, target) else null
        /**
         * `resolvedDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

        /**
         * `primarySplashHarms` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val primarySplashHarms = if (damage == null) {
            PhysicalAttackAreaResolver.computePhysicalSplashHarms(attacker, target, criticalRoll, env = env)
        } else emptyList()

        /**
         * `primaryRecord` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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
        /**
         * `primaryResolution` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val primaryResolution = primaryRecord.result
        /**
         * `primaryResolvedHarm` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val primaryResolvedHarm = primaryRecord.primaryResolvedHarm

        /**
         * `primaryHpDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val primaryHpDamage = when {
            primaryResolution.mpShieldDamage > 0 -> 0
            primaryResolution.moneyShieldSpent > 0 -> primaryResolution.damage
            else -> primaryResolvedHarm
        }
        /**
         * `mpShieldDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mpShieldDamage = primaryResolution.mpShieldDamage
        attacker.markActionComplete()
        /**
         * `defeated` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var defeated = target.hitPoints <= 0
        /**
         * `followUpDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var followUpDamage = 0
        /**
         * `followUpMpShieldDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var followUpMpShieldDamage = 0
        /**
         * `followUpCritical` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var followUpCritical = false

        /**
         * `criticalFollowUp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val criticalFollowUp = criticalRoll && attacker.skills[7]?.and(255)?.let { it != 255 } == true
        if (attacker.hitPoints > 0 && !defeated && (plannedContinuousAttack || criticalFollowUp)) {
            /**
             * `followUpCriticalRoll` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val followUpCriticalRoll = env.probabilityResolver.criticalHit(attacker, target)
            /**
             * `followUpHit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val followUpHit = target.skills[47]?.and(255)?.let { it != 255 } != true &&
                    env.probabilityResolver.physicalHit(attacker, target, hitRate)
            /**
             * `followUpIsCritical` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val followUpIsCritical = followUpHit && followUpCriticalRoll
            followUpCritical = followUpIsCritical
            /**
             * `followUpSpecialDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val followUpSpecialDamage = if (followUpHit) env.mrspDamage(attacker, target) else null
            /**
             * `followUpResolvedHarm` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
            /**
             * `followUpSplashHarms` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val followUpSplashHarms = if (damage == null) {
                PhysicalAttackAreaResolver.computePhysicalSplashHarms(
                    attacker, target, followUpCriticalRoll, activeAttack = true, continuous = true, env = env,
                )
            } else emptyList()

            /**
             * `followUpRecord` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
            /**
             * `followUpPrimary` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val followUpPrimary = followUpRecord.result
            followUpDamage = followUpPrimary.damage
            followUpMpShieldDamage = followUpPrimary.mpShieldDamage
            defeated = target.hitPoints <= 0
        }

        /**
         * `counterMagic` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val counterMagic = target.skills[13]?.and(255)?.takeIf { it != 255 }
            ?.let { magicId -> env.castReactionMagic(target, attacker, magicId) }
        /**
         * `canCounter` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val canCounter = counterMagic == null && attacker.hitPoints > 0 && !defeated && target.visible &&
                attacker.skills[226]?.and(255)?.let { it == 255 } != false && env.canAttack(target, attacker) &&
                BattleStatus.PARALYSIS !in target.statuses && BattleStatus.CONFUSION !in target.statuses
        /**
         * `counterDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var counterDamage = 0
        /**
         * `counterFollowUpDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var counterFollowUpDamage = 0
        /**
         * `counterMpShieldDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var counterMpShieldDamage = 0
        /**
         * `counterFollowUpMpShieldDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var counterFollowUpMpShieldDamage = 0
        /**
         * `counterCriticalResult` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var counterCriticalResult = false
        /**
         * `counterFollowUpCritical` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var counterFollowUpCritical = false

        if (canCounter) {
            /**
             * `counterStatusBatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val counterStatusBatch = env.rollAttackStatusBatch(target)
            /**
             * `counterHitRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val counterHitRate = env.probabilityResolver.physicalHitRate(target, attacker)
            /**
             * `counterCriticalRoll` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val counterCriticalRoll = env.probabilityResolver.criticalHit(target, attacker)
            /**
             * `counterHit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val counterHit = env.probabilityResolver.physicalHit(target, attacker, counterHitRate)
            /**
             * `counterBase` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val counterBase = PhysicalDamageCalculator.basePhysicalDamage(
                target,
                attacker,
                env.basePhysicalDamageContext(target, attacker, false, PhysicalDefenseRule.ATTACKER_AWARE),
            )
            /**
             * `counterCritical` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val counterCritical = counterHit && counterCriticalRoll
            counterCriticalResult = counterCritical
            /**
             * `counterResolvedHarm` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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

            /**
             * `counterSplashHarms` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val counterSplashHarms = PhysicalAttackAreaResolver.computePhysicalSplashHarms(
                attacker = target, primaryTarget = attacker, critical = counterCriticalRoll,
                activeAttack = false, counter = true, continuous = false, env = env,
            )
            /**
             * `counterRecord` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
            /**
             * `counterPrimary` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val counterPrimary = counterRecord.result
            counterDamage = counterPrimary.damage
            counterMpShieldDamage = counterPrimary.mpShieldDamage

            /**
             * `forcedCounterFollowUp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val forcedCounterFollowUp =
                listOf(197, 43).any { target.skills[it]?.and(255)?.let { value -> value != 255 } == true } ||
                        (counterCriticalRoll && target.skills[7]?.and(255)?.let { value -> value != 255 } == true)
            if (attacker.hitPoints > 0 && forcedCounterFollowUp) {
                /**
                 * `secondCriticalRoll` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val secondCriticalRoll = env.probabilityResolver.criticalHit(target, attacker)
                /**
                 * `secondHit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val secondHit = env.probabilityResolver.physicalHit(target, attacker, counterHitRate)
                counterFollowUpCritical = secondHit && secondCriticalRoll
                /**
                 * `counterFollowUpResolvedHarm` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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

                /**
                 * `counterFollowUpSplashHarms` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val counterFollowUpSplashHarms = PhysicalAttackAreaResolver.computePhysicalSplashHarms(
                    attacker = target, primaryTarget = attacker, critical = secondCriticalRoll,
                    activeAttack = false, counter = true, continuous = true, env = env,
                )
                /**
                 * `secondRecord` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
                /**
                 * `secondPrimary` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val secondPrimary = secondRecord.result
                counterFollowUpDamage = secondPrimary.damage
                counterFollowUpMpShieldDamage = secondPrimary.mpShieldDamage
            }
        }

        /**
         * `attackerDefeated` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

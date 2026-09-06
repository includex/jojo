// Battle
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

/** BattlePhysicalContextEnvironment: 물리 공격 계산에 필요한 전장 조회·상태 변경·확률 의존성을 전달한다. */
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

/** BattlePhysicalContextBuilder: 물리 피해·피해율·필살 계산에 쓸 문맥 값을 유닛과 전장에서 조립한다. */
internal object BattlePhysicalContextBuilder {

    /**
     * `basePhysicalDamageContext`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `flatPhysicalDamageContext`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `visibleFamousPlayerCount`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun visibleFamousPlayerCount(env: BattlePhysicalContextEnvironment): Int =
        env.units().count { it.visible && it.isPlayerSide() && it.famous }


    /**
     * `consumeMpAttackSkill`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeMpAttackSkill(attacker: BattleUnit) {
        if (attacker.skills[4]?.and(255)?.let { it != 255 } == true) attacker.addMpcur(-1)
    }


    /**
     * `consumeXuShiDamage`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeXuShiDamage(attacker: BattleUnit, env: BattlePhysicalContextEnvironment): Int {
        val effect = attacker.skills[243]?.and(255)?.takeIf { it != 255 } ?: return 0
        val stored = env.skillTemp(attacker.id, 243, 0)
        if (stored < 1) return 0
        env.setSkillTemp(attacker.id, 243, 0)
        return stored * effect
    }


    /**
     * `accumulateChargeWhenHit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun accumulateChargeWhenHit(defender: BattleUnit, activeAttack: Boolean, env: BattlePhysicalContextEnvironment) {
        if (activeAttack && defender.skills[26]?.and(255)?.let { it != 255 } == true) {
            env.incSkillTemp(defender.id, 26)
        }
    }


    /**
     * `mrspDamage`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun mrspDamage(attacker: BattleUnit, target: BattleUnit, random100: () -> Int): Int? {
        if (attacker.skills[156]?.and(255)?.let { it != 255 } != true) return null
        return target.maxHitPoints * BattleMrspDamage.percent(random100()) / 100
    }

    /**
     * `physicalDamageRateContext`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun physicalDamageRateContext(
        attacker: BattleUnit,
        target: BattleUnit,
        env: BattlePhysicalContextEnvironment,
    ): PhysicalDamageRateContext {
        /**
         * `targetIsPlayerSide` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetIsPlayerSide = target.isPlayerSide()
        /**
         * `targetHasNearbyAlly` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetHasNearbyAlly = env.infantryOffsets.any { (dx, dy) ->
            env.unitAt(target.tileX + dx, target.tileY + dy)?.let { it.isPlayerSide() == targetIsPlayerSide } == true
        }
        /**
         * `hasBackPosition` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hasBackPosition = env.backPosition(target, attacker) != null
        /**
         * `skill292RandomBonus` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

    /**
     * `physicalCriticalRateContext`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun physicalCriticalRateContext(
        attacker: BattleUnit,
        target: BattleUnit,
        critical: Boolean,
        counter: Boolean = false,
        continuous: Boolean = false,
        splash: Boolean = false,
        env: BattlePhysicalContextEnvironment,
    ): PhysicalCriticalRateContext {
        /**
         * `counterSkill46Bonus` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

// Battle
package com.jojo.game.domain.battle.combat

import com.jojo.game.domain.battle.*

import com.jojo.game.*

/** ForcedPhysicalCombatResolver: 강제 물리 전투 처리 판별기이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
internal object ForcedPhysicalCombatResolver {
/**
 * `executeForcedAttack`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

fun executeForcedAttack(
        attacker: BattleUnit,
        target: BattleUnit,
        env: PhysicalCombatEnvironment,
    ): TacticalActionResult {
        if (!attacker.visible || !target.visible || env.areAllied(attacker, target)) {
            return TacticalActionResult.Rejected("강제 공격 대상을 찾을 수 없습니다.")
        }
        /**
         * `hitRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitRate = env.probabilityResolver.physicalHitRate(attacker, target)
        /**
         * `criticalRoll` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val criticalRoll = env.probabilityResolver.criticalHit(attacker, target)
        /**
         * `hit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hit = env.probabilityResolver.physicalHit(attacker, target, hitRate)
        /**
         * `base` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val base = PhysicalDamageCalculator.basePhysicalDamage(
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
         * `specialDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val specialDamage = if (hit) env.mrspDamage(attacker, target) else null
        /**
         * `damage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val damage = if (hit) {
            specialDamage ?: PhysicalDamageCalculator.calculatePhysicalDamage(
                attacker = attacker,
                target = target,
                baseDamage = base,
                damageRateContext = env.physicalDamageRateContext(attacker, target),
                flatContext = env.flatPhysicalDamageContext(attacker, false),
                criticalRateContext = env.physicalCriticalRateContext(attacker, target, critical, false, false, false),
                visibleFamousPlayerCount = env.visibleFamousPlayerCount(),
            )
        } else 0
        if (hit && specialDamage == null) env.consumeMpAttackSkill(attacker)
        target.addHpcur(-damage)
        /**
         * `lifeStealHealing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val lifeStealHealing = attacker.skills[238]?.and(255)?.takeIf { it != 255 && damage > 0 }
            ?.let { minOf(attacker.maxHitPoints - attacker.hitPoints, it * damage / 100) } ?: 0
        attacker.addHpcur(lifeStealHealing)
        /**
         * `defeated` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val defeated = target.hitPoints <= 0
        if (hit) env.notifyPhysicalDamage(attacker, target, damage)
        if (defeated) {
            env.notifyUnitDefeated(attacker, target)
            env.onDefeat(target.id)
        }
        return TacticalActionResult.Attack(
            damage = damage,
            defeated = defeated,
            hitRate = hitRate,
            hit = hit,
            critical = critical,
            counterDamage = 0,
            attackerDefeated = false,
            lifeStealHealing = lifeStealHealing,
        )
    }
}

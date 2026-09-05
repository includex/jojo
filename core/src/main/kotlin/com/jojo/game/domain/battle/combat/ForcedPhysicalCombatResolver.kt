package com.jojo.game.domain.battle.combat

import com.jojo.game.domain.battle.*

import com.jojo.game.*

/** Resolves the source's forced single-pass physical attack route. */
internal object ForcedPhysicalCombatResolver {
fun executeForcedAttack(
        attacker: BattleUnit,
        target: BattleUnit,
        env: PhysicalCombatEnvironment,
    ): TacticalActionResult {
        if (!attacker.visible || !target.visible || env.areAllied(attacker, target)) {
            return TacticalActionResult.Rejected("강제 공격 대상을 찾을 수 없습니다.")
        }
        val hitRate = env.probabilityResolver.physicalHitRate(attacker, target)
        val criticalRoll = env.probabilityResolver.criticalHit(attacker, target)
        val hit = env.probabilityResolver.physicalHit(attacker, target, hitRate)
        val base = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            env.basePhysicalDamageContext(attacker, target, false, PhysicalDefenseRule.ATTACKER_AWARE),
        )
        val critical = hit && criticalRoll &&
                !(target.skills[49]?.and(255)?.let { it != 255 } == true && attacker.skills[227]?.and(255)
                    ?.let { it != 255 } != true)
        val specialDamage = if (hit) env.mrspDamage(attacker, target) else null
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
        val lifeStealHealing = attacker.skills[238]?.and(255)?.takeIf { it != 255 && damage > 0 }
            ?.let { minOf(attacker.maxHitPoints - attacker.hitPoints, it * damage / 100) } ?: 0
        attacker.addHpcur(lifeStealHealing)
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

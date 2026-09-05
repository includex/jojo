package com.jojo.game.domain.battle.combat

import com.jojo.game.*
import com.jojo.game.domain.battle.*

/**
 * Pure Kotlin area-of-effect calculations for physical combat:
 * directional splash area geometry, splash target harm resolution, and damage transfer.
 */
internal object PhysicalAttackAreaResolver {

    fun physicalEffectPositions(attacker: BattleUnit, target: BattleUnit): Set<Pair<Int, Int>> {
        val effectArea =
            attacker.attackEffectAreaId ?: return attacker.attackEffectOffsets.mapTo(linkedSetOf()) { (dx, dy) ->
                target.tileX + dx to target.tileY + dy
            }
        if (effectArea == 0 || effectArea == 12) return emptySet()
        val dx = (target.tileX - attacker.tileX).compareTo(0)
        val dy = (target.tileY - attacker.tileY).compareTo(0)
        val dynamic = when (effectArea) {
            4, 5, 7 -> List(if (effectArea == 4) 1 else if (effectArea == 5) 5 else 2) { index ->
                target.tileX + dx * (index + 1) to target.tileY + dy * (index + 1)
            }

            9 -> when {
                dx == 0 && dy == 0 -> emptyList()
                dx == 0 -> listOf(target.tileX - 1 to target.tileY, target.tileX + 1 to target.tileY)
                dy == 0 -> listOf(target.tileX to target.tileY - 1, target.tileX to target.tileY + 1)
                else -> listOf(target.tileX + dx to target.tileY, target.tileX to target.tileY + dy)
            }

            11 -> {
                val side = when {
                    dx == 0 && dy == 0 -> emptyList()
                    dx == 0 -> listOf(target.tileX - 1 to target.tileY, target.tileX + 1 to target.tileY)
                    dy == 0 -> listOf(target.tileX to target.tileY - 1, target.tileX to target.tileY + 1)
                    else -> listOf(target.tileX + dx to target.tileY, target.tileX to target.tileY + dy)
                }
                side + List(2) { index -> target.tileX + dx * (index + 1) to target.tileY + dy * (index + 1) }
            }

            else -> emptyList()
        }
        if (dynamic.isNotEmpty()) return dynamic.toCollection(linkedSetOf())
        val anchor = if (effectArea == 10) attacker else target
        return attacker.attackEffectOffsets.mapTo(linkedSetOf()) { (x, y) -> anchor.tileX + x to anchor.tileY + y }
    }

    fun hasPhysicalEffectTargets(
        attacker: BattleUnit,
        target: BattleUnit,
        unitAt: (x: Int, y: Int) -> BattleUnit?,
        areAllied: (BattleUnit, BattleUnit) -> Boolean,
    ): Boolean = physicalEffectPositions(attacker, target).asSequence()
        .mapNotNull { (x, y) -> unitAt(x, y) }
        .any { it !== target && it.visible && !areAllied(attacker, it) }

    fun physicalDamageTransfer(
        attacker: BattleUnit,
        defender: BattleUnit,
        resolvedHarm: Int,
        units: () -> Collection<BattleUnit>,
        unitAt: (x: Int, y: Int) -> BattleUnit?,
        areAllied: (BattleUnit, BattleUnit) -> Boolean,
    ): Pair<BattleUnit, Int>? {
        val percent = defender.skills[277]?.and(255)?.takeIf { it != 255 } ?: return null
        if (resolvedHarm < defender.level || BattleStatus.CONFUSION in defender.statuses) return null
        val candidates = (if (defender.attackAllScreen) {
            units().asSequence()
        } else {
            defender.attackOffsets.asSequence().mapNotNull { (dx, dy) ->
                unitAt(defender.tileX + dx, defender.tileY + dy)
            }
        }).distinct()
            .filter { it !== attacker && !areAllied(defender, it) }
            .toList()
            .let { found -> if (found.size > 1) found.sortedBy { it.hitPoints } else found }
        val recipient = candidates.firstOrNull() ?: return null
        return recipient to (resolvedHarm * percent / 100)
    }

    fun computePhysicalSplashHarms(
        attacker: BattleUnit,
        primaryTarget: BattleUnit,
        critical: Boolean,
        activeAttack: Boolean = true,
        counter: Boolean = false,
        continuous: Boolean = false,
        env: PhysicalCombatEnvironment,
    ): List<Pair<BattleUnit, Int>> = physicalEffectPositions(attacker, primaryTarget).asSequence()
        .mapNotNull { (x, y) -> env.unitAt(x, y) }
        .filter { it !== primaryTarget && it.visible && !env.areAllied(attacker, it) }
        .map { affected ->
            val special = env.mrspDamage(attacker, affected)
            val harm = special ?: run {
                val base = PhysicalDamageCalculator.basePhysicalDamage(
                    attacker,
                    affected,
                    env.basePhysicalDamageContext(attacker, affected, false, PhysicalDefenseRule.INTRINSIC),
                )
                PhysicalDamageCalculator.calculatePhysicalDamage(
                    attacker = attacker,
                    target = affected,
                    baseDamage = base,
                    damageRateContext = env.physicalDamageRateContext(attacker, affected),
                    flatContext = env.flatPhysicalDamageContext(attacker, activeAttack),
                    criticalRateContext = env.physicalCriticalRateContext(
                        attacker,
                        affected,
                        critical,
                        counter,
                        continuous,
                        true
                    ),
                    visibleFamousPlayerCount = env.visibleFamousPlayerCount(),
                )
            }
            affected to harm
        }
        .toList()
}

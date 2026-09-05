package com.jojo.game

internal data class BattleExperienceEnvironment(
    val units: () -> Map<String, BattleUnit>,
    val onEquipmentExperienceAward: ((BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> List<CampaignEquipmentExperienceResult>)?,
    val onEquipmentExperience: (BattleUnit, BattleUnit, Int) -> List<CampaignEquipmentExperienceResult>,
    val onPhysicalDamage: (BattleUnit, BattleUnit, Int) -> Unit,
    val onUnitDefeated: (BattleUnit, BattleUnit) -> Unit,
    val onBattleExperience: (BattleUnit, Int) -> CampaignExperienceResult?,
    val experienceLimit: (Int) -> Int,
    val levelLimit: Int,
    val onBattleLevelUp: (BattleUnit) -> Unit,
    val enemyMasterUnitId: String?,
    val equipmentUpgrades: MutableList<CampaignEquipmentExperienceResult>,
    val stagedHitSideEffects: () -> MutableList<() -> Unit>?,
    val stagedCompletionSideEffects: () -> MutableList<() -> Unit>?,
)

internal object BattleExperienceCoordinator {

    fun consumeEquipmentUpgrade(equipmentUpgrades: MutableList<CampaignEquipmentExperienceResult>): CampaignEquipmentExperienceResult? =
        equipmentUpgrades.removeFirstOrNull()

    fun equipmentExperienceAmount(
        recipient: BattleUnit,
        opponent: BattleUnit,
        resolvedHarm: Int,
        kind: BattleEquipmentExperienceKind,
    ): Int = when (kind) {
        BattleEquipmentExperienceKind.WEAPON -> if (resolvedHarm == 0) 1 else if (recipient.level <= opponent.level) 3 else 2
        BattleEquipmentExperienceKind.ARMOR -> if (resolvedHarm == 0) 1 else if (recipient.level <= opponent.level) 4 else 3
    }

    fun battleExperience(attacker: BattleUnit, target: BattleUnit, defeated: Boolean, enemyMasterUnitId: String?): Int {
        val difference = kotlin.math.abs(target.level - attacker.level)
        var result = if (target.level >= attacker.level) 8 + maxOf(1, 2 * difference)
        else maxOf(1, 8 - difference)
        if (defeated) {
            result *= 4
            if (target.id == enemyMasterUnitId) result *= 2
        }
        attacker.skills[67]?.and(255)?.takeIf { it != 255 }?.let { result += it }
        return result
    }

    fun addEquipmentExperience(attackerId: String, targetId: String, damage: Int, env: BattleExperienceEnvironment) {
        val attacker = env.units()[attackerId] ?: return
        val target = env.units()[targetId] ?: return
        val apply = {
            val results = env.onEquipmentExperienceAward?.let { award ->
                buildList {
                    addAll(award(attacker, target, equipmentExperienceAmount(attacker, target, damage, BattleEquipmentExperienceKind.WEAPON), BattleEquipmentExperienceKind.WEAPON))
                    addAll(award(target, attacker, equipmentExperienceAmount(target, attacker, damage, BattleEquipmentExperienceKind.ARMOR), BattleEquipmentExperienceKind.ARMOR))
                }
            } ?: env.onEquipmentExperience(attacker, target, damage)
            results.filterTo(env.equipmentUpgrades) { it.leveledUp }
            Unit
        }
        env.stagedHitSideEffects()?.add(apply) ?: apply()
    }

    fun notifyPhysicalDamage(attacker: BattleUnit, target: BattleUnit, damage: Int, env: BattleExperienceEnvironment) {
        val apply = {
            env.onPhysicalDamage(attacker, target, damage)
            if (env.onEquipmentExperienceAward == null) {
                env.onEquipmentExperience(attacker, target, damage).filterTo(env.equipmentUpgrades) { it.leveledUp }
            }
            Unit
        }
        env.stagedHitSideEffects()?.add(apply) ?: apply()
    }

    fun notifyEquipmentExperienceAward(
        recipient: BattleUnit,
        opponent: BattleUnit,
        amount: Int,
        kind: BattleEquipmentExperienceKind,
        env: BattleExperienceEnvironment,
    ) {
        val award = env.onEquipmentExperienceAward ?: return
        val apply = { award(recipient, opponent, amount, kind).filterTo(env.equipmentUpgrades) { it.leveledUp }; Unit }
        env.stagedCompletionSideEffects()?.add(apply) ?: apply()
    }

    fun notifyUnitDefeated(winner: BattleUnit, defeated: BattleUnit, env: BattleExperienceEnvironment) {
        val apply = { env.onUnitDefeated(winner, defeated) }
        env.stagedCompletionSideEffects()?.add(apply) ?: apply()
    }

    fun notifyBattleExperience(unit: BattleUnit, amount: Int, env: BattleExperienceEnvironment) {
        if (amount <= 0) return
        val apply = {
            val oldLevel = unit.level
            val persistent = env.onBattleExperience(unit, amount)
            if (persistent != null) {
                unit.level = persistent.level
                unit.experience = persistent.experience
            } else {
                var remaining = amount
                while (remaining > 0) {
                    val limit = env.experienceLimit(unit.level).coerceAtLeast(1)
                    val gained = minOf(remaining, (limit - unit.experience).coerceAtLeast(0))
                    unit.experience += gained
                    remaining -= gained
                    if (unit.experience >= limit && unit.level < env.levelLimit) {
                        unit.level++
                        unit.experience = 0
                    } else break
                }
            }
            if (unit.level != oldLevel) env.onBattleLevelUp(unit)
            Unit
        }
        env.stagedCompletionSideEffects()?.add(apply) ?: apply()
    }
}

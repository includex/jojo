// Battle
package com.jojo.game.application.battle.experience

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.BattleEquipmentExperienceKind

/** BattleExperienceEnvironment: 전투 경험치와 장비 성장 정산에 필요한 콜백·유닛·임시 효과 저장소를 전달한다. */
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

/** BattleExperienceCoordinator: 피해·격파 결과를 유닛과 장비 경험치로 계산하고, 표현 시점에 맞춰 반영한다. */
internal object BattleExperienceCoordinator {


    /**
     * `consumeEquipmentUpgrade`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeEquipmentUpgrade(equipmentUpgrades: MutableList<CampaignEquipmentExperienceResult>): CampaignEquipmentExperienceResult? =
        equipmentUpgrades.removeFirstOrNull()

    /**
     * `equipmentExperienceAmount`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun equipmentExperienceAmount(
        recipient: BattleUnit,
        opponent: BattleUnit,
        resolvedHarm: Int,
        kind: BattleEquipmentExperienceKind,
    ): Int = when (kind) {
        BattleEquipmentExperienceKind.WEAPON -> if (resolvedHarm == 0) 1 else if (recipient.level <= opponent.level) 3 else 2
        BattleEquipmentExperienceKind.ARMOR -> if (resolvedHarm == 0) 1 else if (recipient.level <= opponent.level) 4 else 3
    }


    /**
     * `battleExperience`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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


    /**
     * `addEquipmentExperience`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addEquipmentExperience(attackerId: String, targetId: String, damage: Int, env: BattleExperienceEnvironment) {
        val attacker = env.units()[attackerId] ?: return
        val target = env.units()[targetId] ?: return
        val apply = {
            val results = env.onEquipmentExperienceAward?.let { award ->
                buildList {
                    addAll(
                        award(
                            attacker,
                            target,
                            equipmentExperienceAmount(attacker, target, damage, BattleEquipmentExperienceKind.WEAPON),
                            BattleEquipmentExperienceKind.WEAPON
                        )
                    )
                    addAll(
                        award(
                            target,
                            attacker,
                            equipmentExperienceAmount(target, attacker, damage, BattleEquipmentExperienceKind.ARMOR),
                            BattleEquipmentExperienceKind.ARMOR
                        )
                    )
                }
            } ?: env.onEquipmentExperience(attacker, target, damage)
            results.filterTo(env.equipmentUpgrades) { it.leveledUp }
            Unit
        }
        env.stagedHitSideEffects()?.add(apply) ?: apply()
    }


    /**
     * `notifyPhysicalDamage`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `notifyEquipmentExperienceAward`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun notifyEquipmentExperienceAward(
        recipient: BattleUnit,
        opponent: BattleUnit,
        amount: Int,
        kind: BattleEquipmentExperienceKind,
        env: BattleExperienceEnvironment,
    ) {
        /**
         * `award` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val award = env.onEquipmentExperienceAward ?: return
        /**
         * `apply` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val apply = { award(recipient, opponent, amount, kind).filterTo(env.equipmentUpgrades) { it.leveledUp }; Unit }
        env.stagedCompletionSideEffects()?.add(apply) ?: apply()
    }


    /**
     * `notifyUnitDefeated`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun notifyUnitDefeated(winner: BattleUnit, defeated: BattleUnit, env: BattleExperienceEnvironment) {
        val apply = { env.onUnitDefeated(winner, defeated) }
        env.stagedCompletionSideEffects()?.add(apply) ?: apply()
    }


    /**
     * `notifyBattleExperience`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

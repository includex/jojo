package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*

internal data class CombatPassRecord(
    val result: PhysicalAttackTargetResult,
    val primarySourceHarm: Int,
)

internal class CombatSettlementAccumulator(
    private val env: PhysicalCombatEnvironment,
) {
    val physicalPasses = mutableListOf<PhysicalAttackPass>()
    val splashTargets = mutableListOf<PhysicalTarget>()
    private val experienceByAttacker = linkedMapOf<String, Pair<BattleUnit, Int>>()
    private val equipmentByRecipient = linkedMapOf<Pair<String, BattleEquipmentExperienceKind>, EquipmentRecord>()

    var moneyShieldSpent = 0
    var blockRetaliationDamage = 0
    var lifeStealHealing = 0
    var qxlHealing = 0
    var recoilDamage = 0
    var playerMoneyDelta = 0
    var enemyMoneyDelta = 0
    var automaticProperty: TacticalActionResult.Item? = null
    var counterLifeStealHealing = 0

    private data class EquipmentRecord(
        val recipient: BattleUnit,
        val opponent: BattleUnit,
        val kind: BattleEquipmentExperienceKind,
        val amount: Int,
    )

    fun recordPass(
        kind: PhysicalAttackPassKind,
        passAttacker: BattleUnit,
        passTarget: BattleUnit,
        criticalRoll: Boolean,
        sourceHarm: Int,
        hit: Boolean,
        attackStatusBatch: AttackStatusBatch,
        splashHarms: List<Pair<BattleUnit, Int>>,
        isCounter: Boolean,
        isActiveAttack: Boolean,
        collectSplashTargets: Boolean,
    ): CombatPassRecord {
        val passTargets = mutableListOf<PhysicalAttackTargetResult>()
        val transfer = if (hit) PhysicalAttackAreaResolver.physicalDamageTransfer(
            passAttacker, passTarget, sourceHarm, env.units, env.unitAt, env.areAllied,
        ) else null
        val primaryHarm = sourceHarm - (transfer?.second ?: 0)
        val primaryResult =
            env.resolvePhysicalTarget(passAttacker, passTarget, primaryHarm, attackStatusBatch, isActiveAttack)
        recordResolution(primaryResult, isCounter)
        passTargets += primaryResult
        recordExperience(passAttacker, passTarget, primaryResult.defeated)
        recordPhysicalEquipment(passAttacker, passTarget, primaryResult.resolvedHarm)

        transfer?.let { (affected, harm) ->
            val result = env.resolvePhysicalTarget(passAttacker, affected, harm, attackStatusBatch, isActiveAttack)
            recordResolution(result, isCounter)
            passTargets += result
            recordExperience(passAttacker, affected, result.defeated)
            recordPhysicalEquipment(passAttacker, affected, result.resolvedHarm)
        }
        splashHarms.forEach { (affected, harm) ->
            val result = env.resolvePhysicalTarget(passAttacker, affected, harm, attackStatusBatch, true)
            recordResolution(result, isCounter)
            passTargets += result
            if (collectSplashTargets) splashTargets += PhysicalTarget(result.targetId, harm)
            recordExperience(passAttacker, affected, result.defeated)
            recordPhysicalEquipment(passAttacker, affected, result.resolvedHarm)
        }
        val speech = env.resolveCriticalSpeech(passAttacker, criticalRoll)
        physicalPasses += PhysicalAttackPass(
            kind = kind,
            attackerId = passAttacker.id,
            critical = criticalRoll,
            targets = passTargets,
            primaryTargetId = passTarget.id,
            criticalSpeech = speech,
        )
        return CombatPassRecord(primaryResult, primaryHarm)
    }

    /**
     * 공개 메서드 `applySettlement`
     *
     * ### 파라미터
    - `attacker` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun applySettlement(attacker: BattleUnit) {
        if (attacker.hitPoints <= 0) env.onDefeat(attacker.id)
        experienceByAttacker.values.forEach { (u, reward) -> env.notifyBattleExperience(u, reward) }
        equipmentByRecipient.values.forEach { record ->
            env.notifyEquipmentExperienceAward(record.recipient, record.opponent, record.amount, record.kind)
        }
    }

    private fun recordResolution(result: PhysicalAttackTargetResult, isCounter: Boolean) {
        moneyShieldSpent += result.moneyShieldSpent
        blockRetaliationDamage += result.blockRetaliations.sumOf { it.damage }
        if (isCounter) counterLifeStealHealing += result.lifeStealHealing else lifeStealHealing += result.lifeStealHealing
        qxlHealing += result.qxlHealing
        recoilDamage += result.recoilDamage
        playerMoneyDelta += result.playerMoneyDelta
        enemyMoneyDelta += result.enemyMoneyDelta
        if (automaticProperty == null) automaticProperty = result.automaticProperty
    }

    private fun recordExperience(source: BattleUnit, victim: BattleUnit, victimDefeated: Boolean) {
        val reward = env.battleExperience(source, victim, victimDefeated)
        experienceByAttacker[source.id] = source to maxOf(experienceByAttacker[source.id]?.second ?: 0, reward)
    }

    private fun recordPhysicalEquipment(source: BattleUnit, victim: BattleUnit, harm: Int) {
        recordEquipment(victim, source, harm, BattleEquipmentExperienceKind.ARMOR)
        if (source.armType != 1) recordEquipment(source, victim, harm, BattleEquipmentExperienceKind.WEAPON)
    }

    private fun recordEquipment(
        recipient: BattleUnit,
        opponent: BattleUnit,
        harm: Int,
        kind: BattleEquipmentExperienceKind
    ) {
        val amount = env.equipmentExperienceAmount(recipient, opponent, harm, kind)
        val key = recipient.id to kind
        if (amount > (equipmentByRecipient[key]?.amount ?: 0)) {
            equipmentByRecipient[key] = EquipmentRecord(recipient, opponent, kind, amount)
        }
    }
}

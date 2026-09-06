// Battle
package com.jojo.game.domain.battle.combat

import com.jojo.game.domain.battle.*

import com.jojo.game.*

/** CombatPassRecord: 한 물리 공격 패스에서 확정한 주대상 결과와 실제 피해량을 기록한다. */
internal data class CombatPassRecord(
    val result: PhysicalAttackTargetResult,
    val primaryResolvedHarm: Int,
)

/** CombatSettlementAccumulator: 여러 물리 공격 패스의 피해·회복·자금·경험치·장비 정산을 누적한다. */
internal class CombatSettlementAccumulator(
    /**
     * `env` (PhysicalCombatEnvironment,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val env: PhysicalCombatEnvironment,
) {
    /**
     * `physicalPasses` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val physicalPasses = mutableListOf<PhysicalAttackPass>()
    /**
     * `splashTargets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val splashTargets = mutableListOf<PhysicalTarget>()
    /**
     * `experienceByAttacker` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val experienceByAttacker = linkedMapOf<String, Pair<BattleUnit, Int>>()
    /**
     * `equipmentByRecipient` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val equipmentByRecipient = linkedMapOf<Pair<String, BattleEquipmentExperienceKind>, EquipmentRecord>()

    /**
     * `moneyShieldSpent` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moneyShieldSpent = 0
    /**
     * `blockRetaliationDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var blockRetaliationDamage = 0
    /**
     * `lifeStealHealing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lifeStealHealing = 0
    /**
     * `qxlHealing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var qxlHealing = 0
    /**
     * `recoilDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var recoilDamage = 0
    /**
     * `playerMoneyDelta` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var playerMoneyDelta = 0
    /**
     * `enemyMoneyDelta` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var enemyMoneyDelta = 0
    /**
     * `automaticProperty` (TacticalActionResult.Item?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var automaticProperty: TacticalActionResult.Item? = null
    /**
     * `counterLifeStealHealing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var counterLifeStealHealing = 0

    /**
     * `EquipmentRecord` 클래스: combat 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    private data class EquipmentRecord(
        /**
         * `recipient` (BattleUnit,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val recipient: BattleUnit,
        /**
         * `opponent` (BattleUnit,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val opponent: BattleUnit,
        /**
         * `kind` (BattleEquipmentExperienceKind,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val kind: BattleEquipmentExperienceKind,
        /**
         * `amount` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val amount: Int,
    )

    /**
     * `recordPass`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun recordPass(
        kind: PhysicalAttackPassKind,
        passAttacker: BattleUnit,
        passTarget: BattleUnit,
        criticalRoll: Boolean,
        resolvedHarm: Int,
        hit: Boolean,
        attackStatusBatch: AttackStatusBatch,
        splashHarms: List<Pair<BattleUnit, Int>>,
        isCounter: Boolean,
        isActiveAttack: Boolean,
        collectSplashTargets: Boolean,
    ): CombatPassRecord {
        /**
         * `passTargets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val passTargets = mutableListOf<PhysicalAttackTargetResult>()
        /**
         * `transfer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val transfer = if (hit) PhysicalAttackAreaResolver.physicalDamageTransfer(
            passAttacker, passTarget, resolvedHarm, env.units, env.unitAt, env.areAllied,
        ) else null
        /**
         * `primaryHarm` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val primaryHarm = resolvedHarm - (transfer?.second ?: 0)
        /**
         * `primaryResult` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val primaryResult =
            env.resolvePhysicalTarget(passAttacker, passTarget, primaryHarm, attackStatusBatch, isActiveAttack)
        recordResolution(primaryResult, isCounter)
        passTargets += primaryResult
        recordExperience(passAttacker, passTarget, primaryResult.defeated)
        recordPhysicalEquipment(passAttacker, passTarget, primaryResult.resolvedHarm)

        transfer?.let { (affected, harm) ->
            /**
             * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val result = env.resolvePhysicalTarget(passAttacker, affected, harm, attackStatusBatch, isActiveAttack)
            recordResolution(result, isCounter)
            passTargets += result
            recordExperience(passAttacker, affected, result.defeated)
            recordPhysicalEquipment(passAttacker, affected, result.resolvedHarm)
        }
        splashHarms.forEach { (affected, harm) ->
            /**
             * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val result = env.resolvePhysicalTarget(passAttacker, affected, harm, attackStatusBatch, true)
            recordResolution(result, isCounter)
            passTargets += result
            if (collectSplashTargets) splashTargets += PhysicalTarget(result.targetId, harm)
            recordExperience(passAttacker, affected, result.defeated)
            recordPhysicalEquipment(passAttacker, affected, result.resolvedHarm)
        }
        /**
         * `speech` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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
     * `applySettlement`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun applySettlement(attacker: BattleUnit) {
        if (attacker.hitPoints <= 0) env.onDefeat(attacker.id)
        experienceByAttacker.values.forEach { (u, reward) -> env.notifyBattleExperience(u, reward) }
        equipmentByRecipient.values.forEach { record ->
            env.notifyEquipmentExperienceAward(record.recipient, record.opponent, record.amount, record.kind)
        }
    }

    /**
     * `recordResolution`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `recordExperience`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun recordExperience(source: BattleUnit, victim: BattleUnit, victimDefeated: Boolean) {
        val reward = env.battleExperience(source, victim, victimDefeated)
        experienceByAttacker[source.id] = source to maxOf(experienceByAttacker[source.id]?.second ?: 0, reward)
    }

    /**
     * `recordPhysicalEquipment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun recordPhysicalEquipment(source: BattleUnit, victim: BattleUnit, harm: Int) {
        recordEquipment(victim, source, harm, BattleEquipmentExperienceKind.ARMOR)
        if (source.armType != 1) recordEquipment(source, victim, harm, BattleEquipmentExperienceKind.WEAPON)
    }

    /**
     * `recordEquipment`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun recordEquipment(
        recipient: BattleUnit,
        opponent: BattleUnit,
        harm: Int,
        kind: BattleEquipmentExperienceKind
    ) {
        /**
         * `amount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val amount = env.equipmentExperienceAmount(recipient, opponent, harm, kind)
        /**
         * `key` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val key = recipient.id to kind
        if (amount > (equipmentByRecipient[key]?.amount ?: 0)) {
            equipmentByRecipient[key] = EquipmentRecord(recipient, opponent, kind, amount)
        }
    }
}

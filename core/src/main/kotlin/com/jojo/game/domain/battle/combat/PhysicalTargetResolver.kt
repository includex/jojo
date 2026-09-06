// Battle
package com.jojo.game.domain.battle.combat

import com.jojo.game.domain.battle.*

import com.jojo.game.*

/**
 * `AttackStatusBatch` 클래스: combat 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class AttackStatusBatch(
    val statuses: Set<BattleStatus>,
    val downAttributes: Set<BattleAttribute>,
)

/**
 * `PhysicalTargetEnvironment` 클래스: combat 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class PhysicalTargetEnvironment(
    val random100: () -> Int,
    val statusDuration: (BattleStatus, BattleUnit) -> Int,
    val canAttack: (attacker: BattleUnit, target: BattleUnit) -> Boolean,
    val backPosition: (target: BattleUnit, attacker: BattleUnit) -> Pair<Int, Int>?,
    val activeFaction: Faction,
    val getPlayerMoney: () -> Int,
    val setPlayerMoney: (Int) -> Unit,
    val getEnemyMoney: () -> Int,
    val setEnemyMoney: (Int) -> Unit,
    val propertyItem: (itemId: Int) -> BattlePropertyItem?,
    val zdsyGlobalValue: Int,
    val notifyPhysicalDamage: (attacker: BattleUnit, target: BattleUnit, damage: Int) -> Unit,
    val notifyConsumeAutomaticProperty: (itemId: Int) -> Unit,
    val notifyUnitDefeated: (attacker: BattleUnit, target: BattleUnit) -> Unit,
    val onDefeat: (unitId: String) -> Unit,
    val incSkillTemp: (unitId: String, skillId: Int) -> Int,
    val applyProperty: (item: BattlePropertyItem, target: BattleUnit, consume: () -> Boolean) -> TacticalActionResult.Item?,
)

/** PhysicalTargetResolver: 물리 대상 판별기이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
internal object PhysicalTargetResolver {
    /**
     * `rollAttackStatusBatch`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun rollAttackStatusBatch(attacker: BattleUnit, random100: () -> Int): AttackStatusBatch {
        val statuses = linkedSetOf<BattleStatus>()

        /**
         * `chance`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun chance(skillId: Int, status: BattleStatus) {
            attacker.skills[skillId]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                if (random100() < rate) statuses += status
            }
        }
        chance(105, BattleStatus.CONFUSION)
        chance(144, BattleStatus.PARALYSIS)
        chance(127, BattleStatus.SILENCE)
        attacker.skills[272]?.and(255)?.takeIf { it != 255 }?.let { rate ->
            if (random100() <= rate) statuses += BattleStatus.POISON
        }
        if (attacker.skills[204]?.and(255)?.let { it != 255 } == true) {
            listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                .filterNot(statuses::contains)
                .forEach { status -> if (random100() > 70) statuses += status }
        }
        /**
         * `staticAttributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val staticAttributes = linkedSetOf<BattleAttribute>()
        mapOf(
            170 to BattleAttribute.ATTACK, 169 to BattleAttribute.DEFENSE, 171 to BattleAttribute.SPIRIT,
            168 to BattleAttribute.CRITICAL, 172 to BattleAttribute.MORALE, 173 to BattleAttribute.MOVEMENT,
        ).forEach { (skill, attribute) ->
            if (attacker.skills[skill]?.and(255)?.let { it != 255 } == true) staticAttributes += attribute
        }
        /**
         * `down` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val down = staticAttributes.toMutableSet()
        if (attacker.skills[203]?.and(255)?.let { it != 255 } == true) {
            /**
             * `threshold` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            var threshold = 60
            BattleAttribute.entries.forEach { attribute ->
                if (attribute !in staticAttributes && random100() > threshold) down += attribute
                threshold += 5
            }
        }
        return AttackStatusBatch(statuses, down)
    }
    /**
     * `applyIncomingAttackStatuses`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun applyIncomingAttackStatuses(
        batch: AttackStatusBatch,
        target: BattleUnit,
        statusDuration: (BattleStatus, BattleUnit) -> Int,
    ) {
        /**
         * `newlyApplied` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val newlyApplied = batch.statuses.filterTo(linkedSetOf()) { it !in target.statuses }
        batch.statuses.forEach { status -> target.statuses[status] = statusDuration(status, target) }
        if (target.skills[42]?.and(255)?.let { it != 255 } == true) newlyApplied.forEach(target.statuses::remove)
        if (target.skills[122]?.and(255)?.let { it != 255 } != true) {
            batch.downAttributes.forEach { attribute ->
                target.applyAttributeLift(attribute, -1, 3)
            }
        }
    }

    /** resolve: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun resolve(
        attacker: BattleUnit,
        target: BattleUnit,
        resolvedHarm: Int,
        statuses: AttackStatusBatch,
        activeAttack: Boolean,
        env: PhysicalTargetEnvironment,
    ): PhysicalAttackTargetResult {
        /**
         * `targetXBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetXBefore = target.tileX
        /**
         * `targetYBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetYBefore = target.tileY
        /**
         * `statusesBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val statusesBefore = target.statuses.toMap()
        /**
         * `liftsBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val liftsBefore = target.attributeLifts.toMap()
        /**
         * `liftRoundsBefore` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val liftRoundsBefore = target.attributeLiftRounds.toMap()
        /**
         * `n` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var n = resolvedHarm.coerceAtLeast(0)
        /**
         * `blockRetaliations` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val blockRetaliations = mutableListOf<PhysicalBlockRetaliation>()
        /**
         * `mpShieldDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var mpShieldDamage = 0
        /**
         * `moneyShieldSpent` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var moneyShieldSpent = 0
        /**
         * `hpDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var hpDamage = 0
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
         * `playerMoneyDelta` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var playerMoneyDelta = 0
        /**
         * `enemyMoneyDelta` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var enemyMoneyDelta = 0

        if (n == 0) {
            target.skills[153]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                /**
                 * `harm` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val harm = attacker.maxHitPoints * rate / 100
                attacker.addHpcur(-harm)
                attacker.statuses[BattleStatus.CONFUSION] = env.statusDuration(BattleStatus.CONFUSION, attacker)
                blockRetaliations += PhysicalBlockRetaliation(
                    PhysicalBlockRetaliationKind.MENG_JI_CONFUSION,
                    harm,
                )
            }
            target.skills[161]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                /**
                 * `harm` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val harm = attacker.maxHitPoints * rate / 100
                attacker.addHpcur(-harm)
                attacker.statuses[BattleStatus.PARALYSIS] = env.statusDuration(BattleStatus.PARALYSIS, attacker)
                blockRetaliations += PhysicalBlockRetaliation(
                    PhysicalBlockRetaliationKind.NI_FAN_PARALYSIS,
                    harm,
                )
            }
        } else {
            applyIncomingAttackStatuses(statuses, target, env.statusDuration)
            if (target.skills[2]?.and(255)?.let { it != 255 } == true && target.magicPoints > 0) {
                n = n.coerceIn(0, target.magicPoints)
                mpShieldDamage = n
                target.addMpcur(-n)
            } else {
                target.skills[125]?.and(255)?.takeIf { it != 255 }?.let { costPerDamage ->
                    if (target.hitPoints >= costPerDamage) {
                        /**
                         * `price` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                         */

                        val price = kotlin.math.abs(n) * costPerDamage
                        /**
                         * `available` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                         */

                        val available = if (target.isPlayerSide()) env.getPlayerMoney() else env.getEnemyMoney()
                        if (available >= price) {
                            if (target.isPlayerSide()) {
                                env.setPlayerMoney(env.getPlayerMoney() - price)
                            } else {
                                env.setEnemyMoney(env.getEnemyMoney() - price)
                            }
                            moneyShieldSpent = price
                            n = 1
                        }
                    }
                }
                n = n.coerceIn(0, target.hitPoints)
                hpDamage = n
                target.addHpcur(-n)

                attacker.skills[238]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                    /**
                     * `resolvedRate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    var resolvedRate = rate
                    if (!env.canAttack(attacker, target)) resolvedRate /= 2
                    /**
                     * `healing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    var healing = resolvedRate * n / 100
                    /**
                     * `attackerIsMine` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val attackerIsMine = attacker.isPlayerSide()
                    /**
                     * `currentCampIsMine` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val currentCampIsMine = env.activeFaction.isPlayerSide()
                    if (attackerIsMine != currentCampIsMine) healing = minOf(rate, healing)
                    lifeStealHealing = minOf(attacker.maxHitPoints - attacker.hitPoints, healing)
                    attacker.addHpcur(lifeStealHealing)
                }
                attacker.skills[298]?.and(255)?.takeIf { it != 255 }?.let {
                    qxlHealing = minOf(attacker.maxHitPoints - attacker.hitPoints, n)
                    attacker.addHpcur(qxlHealing)
                }
                attacker.skills[237]?.and(255)?.takeIf { it != 255 }?.let { effect ->
                    /**
                     * `amount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val amount = n * effect
                    if (amount >= 1) {
                        if (attacker.isPlayerSide()) {
                            env.setPlayerMoney(env.getPlayerMoney() + amount)
                            env.setEnemyMoney(env.getEnemyMoney() - amount)
                            playerMoneyDelta = amount
                            enemyMoneyDelta = -amount
                        } else {
                            env.setPlayerMoney(env.getPlayerMoney() - amount)
                            env.setEnemyMoney(env.getEnemyMoney() + amount)
                            playerMoneyDelta = -amount
                            enemyMoneyDelta = amount
                        }
                    }
                }
            }

            if (attacker.skills[221]?.and(255)?.let { it != 255 } == true) {
                env.backPosition(target, attacker)?.let { (x, y) ->
                    target.tileX = x
                    target.tileY = y
                }
            }
            if (activeAttack && target.skills[26]?.and(255)?.let { it != 255 } == true) {
                env.incSkillTemp(target.id, 26)
            }
        }

        env.notifyPhysicalDamage(attacker, target, n)

        /**
         * `recoilDamage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val recoilDamage = target.skills[40]?.and(255)?.takeIf { it != 255 && n > 0 }
            ?.let { n * it / 100 }
            ?.takeIf { it >= 1 }
            ?: 0
        if (recoilDamage > 0) attacker.addHpcur(-recoilDamage, keepAlive = true)

        /**
         * `automaticPropertyId` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var automaticPropertyId: Int? = null
        /**
         * `automaticPropertyHpDelta` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var automaticPropertyHpDelta = 0
        /**
         * `automaticPropertyMpDelta` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var automaticPropertyMpDelta = 0
        /**
         * `automaticPropertyCallbackCount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var automaticPropertyCallbackCount = 0
        /**
         * `automaticProperty` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val automaticProperty = if (n > 0) {
            target.skills[284]?.and(255)?.takeIf { itemId ->
                itemId != 255 && target.hitPoints > 0 && target.hitPoints < target.maxHitPoints
            }?.let { itemId ->
                automaticPropertyId = itemId
                /**
                 * `hpBeforeProperty` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val hpBeforeProperty = target.hitPoints
                /**
                 * `mpBeforeProperty` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val mpBeforeProperty = target.magicPoints
                if (target.faction == Faction.PLAYER && env.zdsyGlobalValue == 0) {
                    env.notifyConsumeAutomaticProperty(itemId)
                    automaticPropertyCallbackCount++
                }
                env.propertyItem(itemId)?.let { item ->
                    env.applyProperty(item, target) { true }
                }.also {
                    automaticPropertyHpDelta = target.hitPoints - hpBeforeProperty
                    automaticPropertyMpDelta = target.magicPoints - mpBeforeProperty
                    if (it != null && env.propertyItem(itemId)?.itemType in setOf(42, 43)) {
                        automaticPropertyCallbackCount++
                    }
                }
            }
        } else {
            null
        }

        /**
         * `defeated` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val defeated = target.hitPoints <= 0
        if (defeated) {
            env.onDefeat(target.id)
            env.notifyUnitDefeated(attacker, target)
        }
        /**
         * `backMove` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val backMove = if (target.tileX != targetXBefore || target.tileY != targetYBefore) {
            PhysicalBackMove(targetXBefore, targetYBefore, target.tileX, target.tileY)
        } else null
        /**
         * `localStatusSettlement` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val localStatusSettlement = if (n > 0 &&
            (statuses.statuses.isNotEmpty() || statuses.downAttributes.isNotEmpty())
        ) {
            MagicLocalSettlement(
                listOf(
                    MagicLocalSettlementEntry(
                        targetId = target.id,
                        statusesBefore = statusesBefore,
                        statusesAfter = target.statuses.toMap(),
                        attributeLiftsBefore = liftsBefore,
                        attributeLiftsAfter = target.attributeLifts.toMap(),
                        hasStatesPayload = true,
                        attributeLiftRoundsBefore = liftRoundsBefore,
                        attributeLiftRoundsAfter = target.attributeLiftRounds.toMap(),
                    )
                )
            )
        } else MagicLocalSettlement(emptyList())

        return PhysicalAttackTargetResult(
            targetId = target.id,
            resolvedHarm = n,
            damage = hpDamage,
            mpShieldDamage = mpShieldDamage,
            moneyShieldSpent = moneyShieldSpent,
            lifeStealHealing = lifeStealHealing,
            qxlHealing = qxlHealing,
            recoilDamage = recoilDamage,
            blockRetaliations = blockRetaliations,
            playerMoneyDelta = playerMoneyDelta,
            enemyMoneyDelta = enemyMoneyDelta,
            automaticPropertyId = automaticPropertyId,
            automaticProperty = automaticProperty,
            automaticPropertyHpDelta = automaticPropertyHpDelta,
            automaticPropertyMpDelta = automaticPropertyMpDelta,
            automaticPropertyCallbackCount = automaticPropertyCallbackCount,
            backMove = backMove,
            localStatusSettlement = localStatusSettlement,
            hasLocalStatusSettlement = localStatusSettlement.entries.isNotEmpty(),
            defeated = defeated,
        )
    }
}

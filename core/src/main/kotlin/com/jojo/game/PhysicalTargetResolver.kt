package com.jojo.game

internal data class AttackStatusBatch(
    val statuses: Set<BattleStatus>,
    val downAttributes: Set<BattleAttribute>,
)

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

/**
 * Resolves single-target physical combat effects: damage application, MP/money shields,
 * life steal, recoil, knockback, status effects, and automated consumable usage.
 */
internal object PhysicalTargetResolver {

    /** BattleUnit.getAtkStatus plus attacker's random supplementary lists. */
    fun rollAttackStatusBatch(attacker: BattleUnit, random100: () -> Int): AttackStatusBatch {
        val statuses = linkedSetOf<BattleStatus>()
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
        val staticAttributes = linkedSetOf<BattleAttribute>()
        mapOf(
            170 to BattleAttribute.ATTACK, 169 to BattleAttribute.DEFENSE, 171 to BattleAttribute.SPIRIT,
            168 to BattleAttribute.CRITICAL, 172 to BattleAttribute.MORALE, 173 to BattleAttribute.MOVEMENT,
        ).forEach { (skill, attribute) ->
            if (attacker.skills[skill]?.and(255)?.let { it != 255 } == true) staticAttributes += attribute
        }
        val down = staticAttributes.toMutableSet()
        if (attacker.skills[203]?.and(255)?.let { it != 255 } == true) {
            var threshold = 60
            BattleAttribute.entries.forEach { attribute ->
                if (attribute !in staticAttributes && random100() > threshold) down += attribute
                threshold += 5
            }
        }
        return AttackStatusBatch(statuses, down)
    }

    /** Applies incoming attack status ailments and attribute down effects to target. */
    fun applyIncomingAttackStatuses(
        batch: AttackStatusBatch,
        target: BattleUnit,
        statusDuration: (BattleStatus, BattleUnit) -> Int,
    ) {
        val newlyApplied = batch.statuses.filterTo(linkedSetOf()) { it !in target.statuses }
        batch.statuses.forEach { status -> target.statuses[status] = statusDuration(status, target) }
        if (target.skills[42]?.and(255)?.let { it != 255 } == true) newlyApplied.forEach(target.statuses::remove)
        if (target.skills[122]?.and(255)?.let { it != 255 } != true) {
            batch.downAttributes.forEach { attribute ->
                target.applyAttributeLift(attribute, -1, 3)
            }
        }
        target.presentation.refreshStatus(target.statuses, target.attributeLifts)
    }

    /** Resolves one physical target, including every target-local secondary effect. */
    fun resolve(
        attacker: BattleUnit,
        target: BattleUnit,
        resolvedHarm: Int,
        statuses: AttackStatusBatch,
        activeAttack: Boolean,
        env: PhysicalTargetEnvironment,
    ): PhysicalAttackTargetResult {
        val targetXBefore = target.tileX
        val targetYBefore = target.tileY
        val statusesBefore = target.statuses.toMap()
        val liftsBefore = target.attributeLifts.toMap()
        val liftRoundsBefore = target.attributeLiftRounds.toMap()
        var n = resolvedHarm.coerceAtLeast(0)
        val blockRetaliations = mutableListOf<BattlePhysicalCallbackPlan.BlockRetaliation>()
        var mpShieldDamage = 0
        var moneyShieldSpent = 0
        var hpDamage = 0
        var lifeStealHealing = 0
        var qxlHealing = 0
        var playerMoneyDelta = 0
        var enemyMoneyDelta = 0

        if (n == 0) {
            target.skills[153]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                val harm = attacker.maxHitPoints * rate / 100
                attacker.addHpcur(-harm)
                attacker.statuses[BattleStatus.CONFUSION] = env.statusDuration(BattleStatus.CONFUSION, attacker)
                blockRetaliations += BattlePhysicalCallbackPlan.BlockRetaliation(
                    BattlePhysicalCallbackPlan.BlockRetaliationKind.MENG_JI_CONFUSION,
                    harm,
                )
            }
            target.skills[161]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                val harm = attacker.maxHitPoints * rate / 100
                attacker.addHpcur(-harm)
                attacker.statuses[BattleStatus.PARALYSIS] = env.statusDuration(BattleStatus.PARALYSIS, attacker)
                blockRetaliations += BattlePhysicalCallbackPlan.BlockRetaliation(
                    BattlePhysicalCallbackPlan.BlockRetaliationKind.NI_FAN_PARALYSIS,
                    harm,
                )
            }
            attacker.presentation.refreshStatus(attacker.statuses, attacker.attributeLifts)
        } else {
            applyIncomingAttackStatuses(statuses, target, env.statusDuration)
            if (target.skills[2]?.and(255)?.let { it != 255 } == true && target.magicPoints > 0) {
                n = n.coerceIn(0, target.magicPoints)
                mpShieldDamage = n
                target.addMpcur(-n)
            } else {
                target.skills[125]?.and(255)?.takeIf { it != 255 }?.let { costPerDamage ->
                    if (target.hitPoints >= costPerDamage) {
                        val price = kotlin.math.abs(n) * costPerDamage
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
                    var resolvedRate = rate
                    if (!env.canAttack(attacker, target)) resolvedRate /= 2
                    var healing = resolvedRate * n / 100
                    val attackerIsMine = attacker.isPlayerSide()
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

        val recoilDamage = target.skills[40]?.and(255)?.takeIf { it != 255 && n > 0 }
            ?.let { n * it / 100 }
            ?.takeIf { it >= 1 }
            ?: 0
        if (recoilDamage > 0) attacker.addHpcur(-recoilDamage, keepAlive = true)

        var automaticPropertyId: Int? = null
        var automaticPropertyHpDelta = 0
        var automaticPropertyMpDelta = 0
        var automaticPropertyCallbackCount = 0
        val automaticProperty = if (n > 0) {
            target.skills[284]?.and(255)?.takeIf { itemId ->
                itemId != 255 && target.hitPoints > 0 && target.hitPoints < target.maxHitPoints
            }?.let { itemId ->
                automaticPropertyId = itemId
                val hpBeforeProperty = target.hitPoints
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

        val defeated = target.hitPoints <= 0
        if (defeated) {
            env.onDefeat(target.id)
            env.notifyUnitDefeated(attacker, target)
        }
        val backMove = if (target.tileX != targetXBefore || target.tileY != targetYBefore) {
            PhysicalBackMove(targetXBefore, targetYBefore, target.tileX, target.tileY)
        } else null
        val localStatusSettlement = if (n > 0 &&
            (statuses.statuses.isNotEmpty() || statuses.downAttributes.isNotEmpty())
        ) {
            MagicLocalSettlement(listOf(MagicLocalSettlementEntry(
                targetId = target.id,
                statusesBefore = statusesBefore,
                statusesAfter = target.statuses.toMap(),
                attributeLiftsBefore = liftsBefore,
                attributeLiftsAfter = target.attributeLifts.toMap(),
                hasStatesPayload = true,
                attributeLiftRoundsBefore = liftRoundsBefore,
                attributeLiftRoundsAfter = target.attributeLiftRounds.toMap(),
            )))
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

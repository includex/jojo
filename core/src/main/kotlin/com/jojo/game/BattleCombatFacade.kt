package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*

/** Owns the tactical combat entry points and the combat context they share. */
class BattleCombatFacade internal constructor(private val battle: Battle) {
    private val tacticalEnvironment by lazy { BattleCombatEnvironmentAssembler.tactical(battle) }
    private val physicalContext by lazy { BattleCombatEnvironmentAssembler.physicalContext(battle) }

    fun attack(attackerId: String, targetId: String, damage: Int? = null): TacticalActionResult =
        BattleTacticalActionExecutor.attack(attackerId, targetId, damage, tacticalEnvironment)

    fun useProperty(userId: String, targetId: String, itemId: Int): TacticalActionResult =
        BattleTacticalActionExecutor.useProperty(userId, targetId, itemId, tacticalEnvironment)

    fun applyProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        consume: () -> Boolean,
        notifyPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit,
    ): TacticalActionResult.Item? =
        BattleTacticalActionExecutor.applyProperty(item, target, consume, notifyPermanentProperty)

    fun forcedAttack(attackerId: String, targetId: String): TacticalActionResult =
        BattleTacticalActionExecutor.forcedAttack(attackerId, targetId, tacticalEnvironment)

    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
        reaction: Boolean = false,
        bypassCondition: Boolean = false,
    ): TacticalActionResult = BattleTacticalActionExecutor.castMagic(
        attackerId, targetId, magicId, reaction, bypassCondition, tacticalEnvironment,
    )

    fun castMagicAt(attackerId: String, targetX: Int, targetY: Int, magicId: Int): TacticalActionResult =
        BattleTacticalActionExecutor.castMagicAt(attackerId, targetX, targetY, magicId, tacticalEnvironment)

    fun physicalDamagePreview(attackerId: String, targetId: String): Int {
        val attacker = battle.units[attackerId] ?: return 0
        val target = battle.units[targetId] ?: return 0
        return PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BattlePhysicalContextBuilder.basePhysicalDamageContext(
                attacker, target, splash = false, env = physicalContext,
            ),
        )
    }

    internal fun physicalContext(): BattlePhysicalContextEnvironment = physicalContext
}

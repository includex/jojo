package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.*

/**
 * Owns battle EXP notifications and their deferred side effects.
 *
 * The facade keeps the callback-rich environment alive for the lifetime of a
 * battle.  Its journal-backed queues therefore remain the single source of
 * truth while callers retain the legacy Battle entry points.
 */
class BattleExperienceFacade internal constructor(
    configuration: BattleConfiguration,
    private val journal: BattleStateJournal,
    units: () -> Map<String, BattleUnit>,
) {
    private val environment = BattleExperienceEnvironmentAssembler.build(configuration, journal, units)

    fun consumeEquipmentUpgrade(): CampaignEquipmentExperienceResult? =
        journal.consumeEquipmentUpgrade()

    fun addEquipmentExperience(attackerId: String, targetId: String, damage: Int) =
        BattleExperienceCoordinator.addEquipmentExperience(attackerId, targetId, damage, environment)

    fun notifyPhysicalDamage(attacker: BattleUnit, target: BattleUnit, damage: Int) =
        BattleExperienceCoordinator.notifyPhysicalDamage(attacker, target, damage, environment)

    fun notifyEquipmentExperienceAward(
        recipient: BattleUnit,
        opponent: BattleUnit,
        amount: Int,
        kind: BattleEquipmentExperienceKind,
    ) = BattleExperienceCoordinator.notifyEquipmentExperienceAward(recipient, opponent, amount, kind, environment)

    fun equipmentExperienceAmount(
        recipient: BattleUnit,
        opponent: BattleUnit,
        resolvedHarm: Int,
        kind: BattleEquipmentExperienceKind,
    ): Int = BattleExperienceCoordinator.equipmentExperienceAmount(recipient, opponent, resolvedHarm, kind)

    fun notifyUnitDefeated(winner: BattleUnit, defeated: BattleUnit) =
        BattleExperienceCoordinator.notifyUnitDefeated(winner, defeated, environment)

    fun notifyBattleExperience(unit: BattleUnit, amount: Int) =
        BattleExperienceCoordinator.notifyBattleExperience(unit, amount, environment)

    fun battleExperience(attacker: BattleUnit, target: BattleUnit, defeated: Boolean): Int =
        BattleExperienceCoordinator.battleExperience(attacker, target, defeated, environment.enemyMasterUnitId)

    fun notifyConsumeAutomaticProperty(itemId: Int, consume: (Int) -> Unit) {
        journal.stageHitSideEffect { consume(itemId) }
    }

    fun notifyPermanentProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        apply: (BattlePropertyItem, BattleUnit) -> Unit
    ) {
        journal.stageHitSideEffect { apply(item, target) }
    }

    fun consumeSelectedProperty(itemId: Int, consume: (Int) -> Boolean): Boolean {
        if (journal.hasStagedCompletionSideEffects()) {
            journal.stageCompletionSideEffect { consume(itemId); Unit }
            return true
        }
        return consume(itemId)
    }
}

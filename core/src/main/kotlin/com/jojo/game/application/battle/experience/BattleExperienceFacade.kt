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

/** BattleExperienceFacade: 전투 경험치 진입점이며, 관련 전투 기능을 묶어 안정적인 호출 경로를 제공한다. */
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

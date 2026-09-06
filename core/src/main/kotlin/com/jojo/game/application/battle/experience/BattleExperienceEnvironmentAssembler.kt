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
internal object BattleExperienceEnvironmentAssembler {
    fun build(
        configuration: BattleConfiguration,
        journal: BattleStateJournal,
        units: () -> Map<String, BattleUnit>,
    ): BattleExperienceEnvironment = BattleExperienceEnvironment(
        units = units,
        onEquipmentExperienceAward = configuration.onEquipmentExperienceAward,
        onEquipmentExperience = configuration.onEquipmentExperience,
        onPhysicalDamage = configuration.onPhysicalDamage,
        onUnitDefeated = configuration.onUnitDefeated,
        onBattleExperience = configuration.onBattleExperience,
        experienceLimit = configuration.experienceLimit,
        levelLimit = configuration.levelLimit,
        onBattleLevelUp = configuration.onBattleLevelUp,
        enemyMasterUnitId = configuration.enemyMasterUnitId,
        equipmentUpgrades = journal.mutableEquipmentUpgrades(),
        stagedHitSideEffects = journal::stagedHitSideEffects,
        stagedCompletionSideEffects = journal::stagedCompletionSideEffects,
    )
}

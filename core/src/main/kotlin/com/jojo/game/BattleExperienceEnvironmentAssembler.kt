package com.jojo.game

/** Builds the callback-rich environment used only by battle EXP settlement. */
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

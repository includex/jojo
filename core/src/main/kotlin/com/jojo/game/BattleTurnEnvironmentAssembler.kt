package com.jojo.game
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.application.battle.BattleTurnSettlementEnvironment

/** Builds the environment for start/end-of-camp state settlement. */
internal object BattleTurnEnvironmentAssembler {
    fun build(
        configuration: BattleConfiguration,
        journal: BattleStateJournal,
        battlefield: Battlefield,
        units: () -> Collection<BattleUnit>,
    ): BattleTurnSettlementEnvironment = BattleTurnSettlementEnvironment(
        units = units,
        presentationUnit = battlefield::presentationUnit,
        defeatUnit = battlefield::defeat,
        terrain = configuration.terrain,
        terrainResumeRates = configuration.terrainResumeRates,
        terrainResumeMp = configuration.terrainResumeMp,
        weather = { journal.weather },
        enabledFeatures = { configuration.enabledFeatures },
        infantryOffsets = configuration.infantryOffsets,
        statusRoundFor = configuration.statusRoundFor,
        attributeStatusRoundFor = configuration.attributeStatusRoundFor,
        onRestoreUnitExperience = configuration.onRestoreUnitExperience,
        onRestoreEquipmentExperience = configuration.onRestoreEquipmentExperience,
        onEquipmentUpgrade = journal::queueEquipmentUpgrade,
    )
}

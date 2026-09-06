// Battle
package com.jojo.game.application.battle.round

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.application.battle.BattleTurnSettlementEnvironment
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

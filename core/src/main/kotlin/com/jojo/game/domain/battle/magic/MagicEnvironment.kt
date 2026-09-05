package com.jojo.game.domain.battle.magic

import com.jojo.game.*
import com.jojo.game.domain.battle.*

internal data class MagicEnvironment(
    val probabilityResolver: BattleProbabilityResolver,
    val units: () -> Collection<BattleUnit>,
    val pendingPresentationUnits: () -> Collection<BattleUnit>,
    val unitAt: (x: Int, y: Int) -> BattleUnit?,
    val areAllied: (BattleUnit, BattleUnit) -> Boolean,
    val weather: () -> BattleWeather,
    val setWeather: (BattleWeather) -> Unit,
    val terrain: BattleTerrainGrid?,
    val terrainMagicFlags: Map<Int, Int>,
    val activeFaction: () -> Faction,
    val isBattleEnded: () -> Boolean,
    val statusDuration: (BattleStatus, BattleUnit) -> Int,
    val resolveCriticalSpeech: (unit: BattleUnit, critical: Boolean) -> String?,
    val battleExperience: (attacker: BattleUnit, target: BattleUnit, defeated: Boolean) -> Int,
    val equipmentExperienceAmount: (recipient: BattleUnit, opponent: BattleUnit, resolvedHarm: Int, kind: BattleEquipmentExperienceKind) -> Int,
    val notifyBattleExperience: (unit: BattleUnit, amount: Int) -> Unit,
    val notifyEquipmentExperienceAward: (recipient: BattleUnit, opponent: BattleUnit, amount: Int, kind: BattleEquipmentExperienceKind) -> Unit,
    val notifyUnitDefeated: (attacker: BattleUnit, target: BattleUnit) -> Unit,
    val onDefeat: (unitId: String) -> Unit,
)

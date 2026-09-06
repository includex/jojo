// Battle
package com.jojo.game.domain.battle.magic

import com.jojo.game.domain.battle.*

import com.jojo.game.*

/** MagicEnvironment: 마법 해결기가 대상·지형·날씨·확률·정산 콜백에 접근하도록 제공하는 실행 문맥이다. */
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

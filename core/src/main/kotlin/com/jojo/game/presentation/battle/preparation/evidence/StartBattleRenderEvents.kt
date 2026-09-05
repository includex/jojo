package com.jojo.game.presentation.battle.preparation.evidence

import com.jojo.game.RenderEventLog

/** Canonical authored traversal for Hall/scene/StartBattleScreen and UnitInfoLayer. */
internal fun appendStartBattleRenderEvents(
    log: RenderEventLog,
    unitInfo: Boolean,
    phaseOverride: String? = null,
    scale: Float = .86f,
    startBattleScreen: String = "StartBattleScreen",
    spiritSorted: Boolean = false,
) {
    val phase = phaseOverride ?: if (unitInfo) "hall-start-battle-unit-info-stable" else "hall-start-battle-stable"
    val context = StartBattleRenderEventContext(log, phase, scale, startBattleScreen, spiritSorted)
    writeStartBattleBackdropEvents(context)
    writeStartBattleRosterEvents(context)
    writeStartBattleDetailEvents(context)
    if (!unitInfo) return
    writeUnitInfoChromeEvents(context)
    writeUnitInfoProfileEvents(context)
    writeUnitInfoAbilityEvents(context)
}

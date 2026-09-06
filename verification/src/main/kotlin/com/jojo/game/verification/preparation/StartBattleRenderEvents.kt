// Verification
package com.jojo.game.verification.preparation

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** appendStartBattleRenderEvents: Hall·scene·StartBattleScreen과 UnitInfoLayer의 원본 순회를 기준 형태로 재현한다. */
internal fun appendStartBattleRenderEvents(
    log: RenderEventLog,
    unitInfo: Boolean,
    phaseOverride: String? = null,
    scale: Float = .86f,
    startBattleScreen: String = "StartBattleScreen",
    spiritSorted: Boolean = false,
) {
    /**
     * `phase` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val phase = phaseOverride ?: if (unitInfo) "hall-start-battle-unit-info-stable" else "hall-start-battle-stable"
    /**
     * `context` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val context = StartBattleRenderEventContext(log, phase, scale, startBattleScreen, spiritSorted)
    writeStartBattleBackdropEvents(context)
    writeStartBattleRosterEvents(context)
    writeStartBattleDetailEvents(context)
    if (!unitInfo) return
    writeUnitInfoChromeEvents(context)
    writeUnitInfoProfileEvents(context)
    writeUnitInfoAbilityEvents(context)
}

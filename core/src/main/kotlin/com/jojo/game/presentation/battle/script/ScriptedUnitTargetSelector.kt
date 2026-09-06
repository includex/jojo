// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.isEnemySide
import com.jojo.game.domain.battle.isPlayerSide

/** 스크립트 대상 선택 규칙을 전투 화면의 명단과 분리해 적용합니다. */
internal class ScriptedUnitTargetSelector(
    /** `visibleUnits` (() -> List<BattleUnit>): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val visibleUnits: () -> List<BattleUnit>,
    /** `isMineMaster` ((String) -> Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val isMineMaster: (String) -> Boolean,
    /** `byCharacter` ((Int) -> BattleUnit?): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val byCharacter: (Int) -> BattleUnit?,
) {
    /** 선택자 번호에 해당하는 표시 대상 유닛을 반환합니다. */
    fun select(selector: Int): BattleUnit? {
        val units = visibleUnits()
        return when (selector) {
            1024 -> units.firstOrNull()
            1025 -> units.firstOrNull(BattleUnit::isPlayerSide)
            1026 -> units.firstOrNull { it.type().isEnemySide() }
            1027 -> units.firstOrNull { isMineMaster(it.id) }
                ?: units.firstOrNull(BattleUnit::isPlayerSide)
            else -> byCharacter(selector)
        }
    }
}

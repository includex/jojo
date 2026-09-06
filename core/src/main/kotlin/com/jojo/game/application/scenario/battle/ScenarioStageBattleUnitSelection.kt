// Scenario
package com.jojo.game.application.scenario.battle

import com.jojo.game.domain.scenario.*

/** ScenarioStageBattleUnitSelection: 스크립트의 진영·사각형 선택 조건에 맞는 전투 유닛을 판별한다. */
internal object ScenarioStageBattleUnitSelection {
    /**
     * `matchesAiCamp`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun matchesAiCamp(unit: ScenarioBattleUnit, camp: Int): Boolean = when (camp) {
        0, 1, 2, 3 -> scriptCamp(unit) == camp
        4 -> unit.faction != ScenarioUnitFaction.ENEMY
        5 -> unit.faction == ScenarioUnitFaction.ENEMY
        6 -> true
        else -> false
    }

    /**
     * `inRectangle`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun inRectangle(unit: ScenarioBattleUnit, x1: Int, y1: Int, x2: Int, y2: Int): Boolean =
        unit.x in x1..x2 && unit.y in y1..y2

    /**
     * `scriptCamp`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun scriptCamp(unit: ScenarioBattleUnit): Int = when (unit.faction) {
        ScenarioUnitFaction.MINE -> 0
        ScenarioUnitFaction.FRIEND -> 1
        ScenarioUnitFaction.ENEMY -> if (unit.reinforcement) 3 else 2
    }
}

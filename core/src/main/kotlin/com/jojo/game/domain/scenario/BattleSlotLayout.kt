// Scenario
package com.jojo.game.domain.scenario

import com.jojo.game.domain.battle.Faction

/** BattleSlotLayout: 전투 준비 화면에서 진영별 편성 칸의 좌표·최대 인원을 계산하는 배치 규칙이다. */
internal object BattleSlotLayout {
    /**
     * `mineCount` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val mineCount = 20
    /**
     * `friendEnd` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val friendEnd = 40
    /**
     * `enemyStart` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val enemyStart = mineCount + friendEnd
    /**
     * `enemyBlockCount` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private const val enemyBlockCount = 3
    /**
     * `enemyBlockLength` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val enemyBlockLength = 80
    /**
     * `enemyEnd` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val enemyEnd = enemyBlockCount * enemyBlockLength

    /**
     * `slotFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun slotFor(faction: ScenarioUnitFaction, instanceId: Int, enemyBlockStart: Int = enemyStart): Int = when (faction) {
        ScenarioUnitFaction.MINE -> instanceId
        ScenarioUnitFaction.FRIEND -> friendEnd + instanceId
        ScenarioUnitFaction.ENEMY -> enemyBlockStart + instanceId
    }

    /**
     * `battleId`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleId(faction: ScenarioUnitFaction, battleSlot: Int): String = when (faction) {
        ScenarioUnitFaction.MINE -> "mine-$battleSlot"
        ScenarioUnitFaction.FRIEND -> "friend-${battleSlot - friendEnd}"
        ScenarioUnitFaction.ENEMY -> "enemy-${battleSlot - enemyStart}"
    }

    /**
     * `stageKey`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun stageKey(faction: ScenarioUnitFaction, battleSlot: Int): String =
        "${faction.name}:${battleId(faction, battleSlot).substringAfter('-')}"

    /**
     * `rangeFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun rangeFor(camp: Faction): IntRange = when (camp) {
        Faction.PLAYER -> 0 until mineCount
        Faction.FRIEND -> mineCount until friendEnd
        Faction.ENEMY, Faction.REINFORCEMENTS -> enemyStart until enemyEnd
    }
}

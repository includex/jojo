// Game
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.TacticalUnit
import com.jojo.game.domain.campaign.*
import com.jojo.game.application.scenario.battle.ScenarioStageBattleUnitFactory
import com.jojo.game.application.scenario.battle.ScenarioStageBattleUnitSelection

/** ScenarioStageUnitRegistry: 전장 유닛 식별자와 인스턴스 조회 색인을 관리한다. */
internal class ScenarioStageUnitRegistry {
    /**
     * `units` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val units = linkedMapOf<Int, TacticalUnit>()

    /** 진영별 원본 슬롯 오프셋을 키로 사용한다. */
    val battleUnits = linkedMapOf<String, ScenarioBattleUnit>()

    /** 인물 식별자는 등록된 첫 번째 전장 유닛으로 해석한다. */
    private val firstBattleUnitKeyByCharacterId = linkedMapOf<Int, String>()
    /**
     * `battleUnitFactory` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val battleUnitFactory = ScenarioStageBattleUnitFactory()

    /**
     * `clearUnits`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun clearUnits() = units.clear()

    /**
     * `unit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unit(id: Int): TacticalUnit = units.getOrPut(id) {
        TacticalUnit(id, 16 + id % 40, 20 + (id * 7) % 55)
    }

    /**
     * `seedBattleUnitPosition`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun seedBattleUnitPosition(id: Int, x: Int, y: Int) {
        unit(id).apply {
            this.x = x
            this.y = y
            moveToX = x
            moveToY = y
            visualX = x.toFloat()
            visualY = y.toFloat()
            moveFromX = visualX
            moveFromY = visualY
            moveElapsed = 0f
            moveDuration = 0f
            movePath = emptyList()
        }
    }

    /**
     * `setUnitDirection`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitDirection(id: Int, direction: Int, onScriptedDirection: (Pair<Int, Int>) -> Unit) {
        unit(id).direction = direction
        onScriptedDirection(id to direction)
    }

    /**
     * `setUnit`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnit(id: Int, x: Int, y: Int, direction: Int, onScriptedDirection: (Pair<Int, Int>) -> Unit) {
        unit(id).apply {
            this.x = x
            this.y = y
            moveToX = x
            moveToY = y
            visualX = x.toFloat()
            visualY = y.toFloat()
            moveDuration = 0f
            movePath = emptyList()
        }
        setUnitDirection(id, direction, onScriptedDirection)
    }

    /**
     * `createBattleUnits`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun createBattleUnits(faction: ScenarioUnitFaction, entries: List<Any?>, campaign: CampaignState) {
        val enemyBlockStart =
            if (faction == ScenarioUnitFaction.ENEMY) nextEnemyBlockStart() else BattleSlotLayout.enemyStart
        entries.forEachIndexed { fallbackIndex, raw ->
            val created = battleUnitFactory.create(faction, raw, fallbackIndex, campaign, enemyBlockStart)
                ?: return@forEachIndexed
            val battleUnit = created.battleUnit
            battleUnits[battleUnit.stageKey] = battleUnit
            if (firstBattleUnitKeyByCharacterId.putIfAbsent(battleUnit.characterId, battleUnit.stageKey) == null) {
                bindFirstActor(battleUnit, created.initiallyVisible)
            }
        }
    }

    /** 인물에 등록된 첫 번째 전장 유닛을 찾는다. */
    fun battleUnitForCharacterId(characterId: Int): ScenarioBattleUnit? =
        firstBattleUnitKeyByCharacterId[characterId]?.let(battleUnits::get)
            ?: battleUnits.values.firstOrNull { it.characterId == characterId }

    /** 고정 전장 인스턴스 슬롯으로 유닛을 찾는다. */
    fun battleUnitForSlot(battleSlot: Int): ScenarioBattleUnit? =
        battleUnits.values.firstOrNull { it.battleSlot == battleSlot }

    /**
     * `setBattleAi`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setBattleAi(
        camp: Int,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        ai: Int,
        targetId: Int = -1,
        targetX: Int = 0,
        targetY: Int = 0,
    ) {
        battleUnits.values
            .filter {
                ScenarioStageBattleUnitSelection.matchesAiCamp(
                    it,
                    camp
                ) && ScenarioStageBattleUnitSelection.inRectangle(it, x1, y1, x2, y2)
            }
            .forEach { selected ->
                selected.setAi(ai, targetId, targetX, targetY)
                if (battleUnitForCharacterId(selected.characterId) === selected) {
                    unit(selected.characterId).setAi(ai, targetId, targetX, targetY)
                }
            }
    }

    /**
     * `setUnitAi`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitAi(unitId: Int, ai: Int, targetId: Int = -1, targetX: Int = 0, targetY: Int = 0) {
        unit(unitId).setAi(ai, targetId, targetX, targetY)
        battleUnitForCharacterId(unitId)?.setAi(ai, targetId, targetX, targetY)
    }

    /**
     * `setUnitRetreatTextEnabled`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitRetreatTextEnabled(unitId: Int, enabled: Boolean) {
        battleUnitForCharacterId(unitId)?.deathMessageEnabled = enabled
    }

    /**
     * `hideBattleRect`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hideBattleRect(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int) {
        battleUnits.values
            .filter {
                ScenarioStageBattleUnitSelection.matchesAiCamp(
                    it,
                    camp
                ) && ScenarioStageBattleUnitSelection.inRectangle(it, x1, y1, x2, y2)
            }
            .forEach { selected ->
                selected.hidden = true
                if (battleUnitForCharacterId(selected.characterId) === selected) unit(selected.characterId).visible =
                    false
            }
    }

    /**
     * `setBattleUnitVisibility`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setBattleUnitVisibility(unitId: Int, visible: Boolean) {
        unit(unitId).visible = visible
        battleUnitForCharacterId(unitId)?.hidden = !visible
    }

    /**
     * `ScenarioBattleUnit`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun ScenarioBattleUnit.matchesAiCamp(camp: Int): Boolean =
        ScenarioStageBattleUnitSelection.matchesAiCamp(this, camp)

    /**
     * `nextEnemyBlockStart`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun nextEnemyBlockStart(): Int =
        generateSequence(BattleSlotLayout.enemyStart) { it + BattleSlotLayout.enemyBlockLength }
            .first { candidate -> battleUnits.values.none { it.battleSlot == candidate } }

    /**
     * `bindFirstActor`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun bindFirstActor(battleUnit: ScenarioBattleUnit, visible: Boolean) {
        unit(battleUnit.characterId).apply {
            x = battleUnit.x
            y = battleUnit.y
            moveToX = battleUnit.x
            moveToY = battleUnit.y
            visualX = battleUnit.x.toFloat()
            visualY = battleUnit.y.toFloat()
            direction = battleUnit.direction
            this.visible = visible
            setAi(battleUnit.ai, battleUnit.aiTargetId, battleUnit.aiTargetX, battleUnit.aiTargetY)
        }
    }

    /**
     * `TacticalUnit`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun TacticalUnit.setAi(ai: Int, targetId: Int, targetX: Int, targetY: Int) {
        this.ai = ai
        aiTargetId = targetId
        aiTargetX = targetX
        aiTargetY = targetY
    }

    /**
     * `ScenarioBattleUnit`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun ScenarioBattleUnit.setAi(ai: Int, targetId: Int, targetX: Int, targetY: Int) {
        this.ai = ai
        aiTargetId = targetId
        aiTargetX = targetX
        aiTargetY = targetY
    }
}

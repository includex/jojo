package com.jojo.game

import com.jojo.game.domain.scenario.*
import com.jojo.game.presentation.scenario.TacticalUnit
import com.jojo.game.domain.campaign.*

/** Owns live tactical-unit identity and battle-instance lookup indexes. */
internal class ScenarioStageUnitRegistry {
    val units = linkedMapOf<Int, TacticalUnit>()

    /** Keys are camp-local source-slot offsets: ENEMY:0, ENEMY:80, ENEMY:160. */
    val battleUnits = linkedMapOf<String, ScenarioBattleUnit>()

    /** BattleScreen._unitIds: a character ID always resolves to its first actor. */
    private val firstBattleUnitKeyByCharacterId = linkedMapOf<Int, String>()
    private val battleUnitFactory = ScenarioStageBattleUnitFactory()

    fun clearUnits() = units.clear()

    fun unit(id: Int): TacticalUnit = units.getOrPut(id) {
        TacticalUnit(id, 16 + id % 40, 20 + (id * 7) % 55)
    }

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

    fun setUnitDirection(id: Int, direction: Int, onScriptedDirection: (Pair<Int, Int>) -> Unit) {
        unit(id).direction = direction
        onScriptedDirection(id to direction)
    }

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

    /** Resolves the first actor registered for a character. */
    fun battleUnitForCharacterId(characterId: Int): ScenarioBattleUnit? =
        firstBattleUnitKeyByCharacterId[characterId]?.let(battleUnits::get)
            ?: battleUnits.values.firstOrNull { it.characterId == characterId }

    /** Resolves an actor directly by its stable battle-instance slot. */
    fun battleUnitForSlot(battleSlot: Int): ScenarioBattleUnit? =
        battleUnits.values.firstOrNull { it.battleSlot == battleSlot }

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

    fun setUnitAi(unitId: Int, ai: Int, targetId: Int = -1, targetX: Int = 0, targetY: Int = 0) {
        unit(unitId).setAi(ai, targetId, targetX, targetY)
        battleUnitForCharacterId(unitId)?.setAi(ai, targetId, targetX, targetY)
    }

    fun setUnitRetreatTextEnabled(unitId: Int, enabled: Boolean) {
        battleUnitForCharacterId(unitId)?.deathMessageEnabled = enabled
    }

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

    fun setBattleUnitVisibility(unitId: Int, visible: Boolean) {
        unit(unitId).visible = visible
        battleUnitForCharacterId(unitId)?.hidden = !visible
    }

    internal fun ScenarioBattleUnit.matchesAiCamp(camp: Int): Boolean =
        ScenarioStageBattleUnitSelection.matchesAiCamp(this, camp)

    private fun nextEnemyBlockStart(): Int =
        generateSequence(BattleSlotLayout.enemyStart) { it + BattleSlotLayout.enemyBlockLength }
            .first { candidate -> battleUnits.values.none { it.battleSlot == candidate } }

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

    private fun TacticalUnit.setAi(ai: Int, targetId: Int, targetX: Int, targetY: Int) {
        this.ai = ai
        aiTargetId = targetId
        aiTargetX = targetX
        aiTargetY = targetY
    }

    private fun ScenarioBattleUnit.setAi(ai: Int, targetId: Int, targetX: Int, targetY: Int) {
        this.ai = ai
        aiTargetId = targetId
        aiTargetX = targetX
        aiTargetY = targetY
    }
}

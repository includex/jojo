package com.jojo.game

internal class ScenarioStageUnitRegistry {
    val units = linkedMapOf<Int, TacticalUnit>()
    /** Keys are camp-local source-slot offsets: ENEMY:0, ENEMY:80, ENEMY:160. */
    val battleUnits = linkedMapOf<String, ScenarioBattleUnit>()
    /** BattleScreen._unitIds: a character ID always resolves to its first actor. */
    private val firstBattleUnitKeyByCharacterId = linkedMapOf<Int, String>()

    fun clearUnits() = units.clear()

    fun unit(id: Int): TacticalUnit = units.getOrPut(id) {
        TacticalUnit(id, 16 + id % 40, 20 + (id * 7) % 55)
    }

    /** Bind a lazy script proxy to the live BattleUnit before a scene runs. */
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

    fun createBattleUnits(
        faction: ScenarioUnitFaction,
        entries: List<Any?>,
        campaign: CampaignState,
    ) {
        val enemyBlockStart = if (faction == ScenarioUnitFaction.ENEMY) {
            generateSequence(BattleSlotLayout.enemyStart) { it + BattleSlotLayout.enemyBlockLength }
                .first { candidate -> battleUnits.values.none { it.battleSlot == candidate } }
        } else null
        entries.forEachIndexed { fallbackIndex, raw ->
            @Suppress("UNCHECKED_CAST")
            val entry = raw as? Map<String, Any?> ?: return@forEachIndexed
            val instanceId = entry["i"].asIntOr(fallbackIndex)
            val rosterIndex = entry["idx"].asIntOr(instanceId)
            val characterId = if (faction == ScenarioUnitFaction.MINE && "id" !in entry) {
                campaign.roster.battleRoster.getOrNull(rosterIndex) ?: return@forEachIndexed
            } else entry["id"].asIntOr(instanceId)
            val initialAi = when {
                faction == ScenarioUnitFaction.MINE -> 1
                "ai" in entry -> entry["ai"].asIntOr(0)
                else -> 2
            }
            val battleSlot = BattleSlotLayout.slotFor(
                faction,
                if (faction == ScenarioUnitFaction.MINE) rosterIndex else instanceId,
                enemyBlockStart ?: BattleSlotLayout.enemyStart,
            )
            val battleUnit = ScenarioBattleUnit(
                instanceId = instanceId,
                characterId = characterId,
                faction = faction,
                x = entry["x"].asIntOr(0),
                y = entry["y"].asIntOr(0),
                authoredX = "x" in entry,
                authoredY = "y" in entry,
                direction = entry["dir"].asIntOr(2),
                level = entry["lv"].asIntOr(0),
                reinforcement = faction == ScenarioUnitFaction.ENEMY && entry["yj"].asIntOr(0) != 0,
                hidden = entry["hide"].asIntOr(0) != 0,
                ai = initialAi,
                aiTargetId = entry["targetId"].asIntOr(-1),
                aiTargetX = entry["targetX"].asIntOr(0),
                aiTargetY = entry["targetY"].asIntOr(0),
                battleSlot = battleSlot,
            )
            battleUnits[battleUnit.stageKey] = battleUnit
            if (firstBattleUnitKeyByCharacterId.putIfAbsent(characterId, battleUnit.stageKey) == null) unit(characterId).apply {
                x = battleUnit.x
                y = battleUnit.y
                moveToX = battleUnit.x
                moveToY = battleUnit.y
                visualX = battleUnit.x.toFloat()
                visualY = battleUnit.y.toFloat()
                direction = battleUnit.direction
                visible = entry["hide"].asIntOr(0) == 0
                ai = initialAi
                aiTargetId = battleUnit.aiTargetId
                aiTargetX = battleUnit.aiTargetX
                aiTargetY = battleUnit.aiTargetY
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
        battleUnits.values.filter { it.matchesAiCamp(camp) && it.x in x1..x2 && it.y in y1..y2 }
            .forEach {
                it.ai = ai
                it.aiTargetId = targetId
                it.aiTargetX = targetX
                it.aiTargetY = targetY
                if (battleUnitForCharacterId(it.characterId) === it) unit(it.characterId).apply {
                    this.ai = ai
                    aiTargetId = targetId
                    aiTargetX = targetX
                    aiTargetY = targetY
                }
            }
    }

    fun setUnitAi(unitId: Int, ai: Int, targetId: Int = -1, targetX: Int = 0, targetY: Int = 0) {
        unit(unitId).apply {
            this.ai = ai
            aiTargetId = targetId
            aiTargetX = targetX
            aiTargetY = targetY
        }
        battleUnitForCharacterId(unitId)?.let {
            it.ai = ai
            it.aiTargetId = targetId
            it.aiTargetX = targetX
            it.aiTargetY = targetY
        }
    }

    fun setUnitRetreatTextEnabled(unitId: Int, enabled: Boolean) {
        battleUnitForCharacterId(unitId)?.deathMessageEnabled = enabled
    }

    fun hideBattleRect(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int) {
        battleUnits.values.filter { it.matchesAiCamp(camp) && it.x in x1..x2 && it.y in y1..y2 }
            .forEach {
                it.hidden = true
                if (battleUnitForCharacterId(it.characterId) === it) unit(it.characterId).visible = false
            }
    }

    fun setBattleUnitVisibility(unitId: Int, visible: Boolean) {
        unit(unitId).visible = visible
        battleUnitForCharacterId(unitId)?.hidden = !visible
    }

    private fun ScenarioBattleUnit.toScriptCamp(): Int = when (faction) {
        ScenarioUnitFaction.MINE -> 0
        ScenarioUnitFaction.FRIEND -> 1
        ScenarioUnitFaction.ENEMY -> if (reinforcement) 3 else 2
    }

    internal fun ScenarioBattleUnit.matchesAiCamp(camp: Int): Boolean = when (camp) {
        0, 1, 2, 3 -> toScriptCamp() == camp
        4 -> faction != ScenarioUnitFaction.ENEMY
        5 -> faction == ScenarioUnitFaction.ENEMY
        6 -> true
        else -> false
    }

    private fun Any?.asIntOr(default: Int): Int = when (this) {
        is Number -> toInt()
        is Boolean -> if (this) 1 else 0
        is String -> toIntOrNull() ?: default
        else -> default
    }
}

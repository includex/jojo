package com.jojo.game.domain.scenario

/** An original StageLayer script can suspend for an input, timer, or native modal. */
enum class PlaybackState { DIALOGUE, CHOICE, DELAY, MODAL, COMPLETE }

enum class ScenarioUnitFaction { FRIEND, ENEMY, MINE }

data class ScenarioBattleUnit(
    val instanceId: Int,
    val characterId: Int,
    val faction: ScenarioUnitFaction,
    val x: Int,
    val y: Int,
    val authoredX: Boolean = true,
    val authoredY: Boolean = true,
    val direction: Int = 2,
    val level: Int = 0,
    val reinforcement: Boolean = false,
    var hidden: Boolean = false,
    var ai: Int = 0,
    var aiTargetId: Int = -1,
    var aiTargetX: Int = 0,
    var aiTargetY: Int = 0,
    var deathMessageEnabled: Boolean = faction == ScenarioUnitFaction.MINE,
    val battleSlot: Int = BattleSlotLayout.slotFor(faction, instanceId),
)

val ScenarioBattleUnit.battleId: String
    get() = BattleSlotLayout.battleId(faction, battleSlot)

val ScenarioBattleUnit.stageKey: String
    get() = BattleSlotLayout.stageKey(faction, battleSlot)

/** One authored Stage.setObjects/setObject invocation, before presentation filtering. */
data class ScenarioMapObjectsCall(
    val enabled: Boolean,
    val terrainId: Int,
    val objects: List<Object>,
) {
    data class Object(val objectId: Int, val x: Int, val y: Int)
}

data class ScriptedAttackAction(val attackerId: Int, val targetId: Int, val flag: Int)

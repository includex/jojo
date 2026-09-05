package com.jojo.game
import com.jojo.game.domain.campaign.*

internal data class ScenarioStageBattleUnitCreation(
    val battleUnit: ScenarioBattleUnit,
    val initiallyVisible: Boolean,
)

/** Translates authored createMine/createFriend/createEnemy rows into battle units. */
internal class ScenarioStageBattleUnitFactory {
    fun create(
        faction: ScenarioUnitFaction,
        raw: Any?,
        fallbackIndex: Int,
        campaign: CampaignState,
        enemyBlockStart: Int,
    ): ScenarioStageBattleUnitCreation? {
        @Suppress("UNCHECKED_CAST")
        val entry = raw as? Map<String, Any?> ?: return null
        val instanceId = entry["i"].asIntOr(fallbackIndex)
        val rosterIndex = entry["idx"].asIntOr(instanceId)
        val characterId = if (faction == ScenarioUnitFaction.MINE && "id" !in entry) {
            campaign.roster.battleRoster.getOrNull(rosterIndex) ?: return null
        } else entry["id"].asIntOr(instanceId)
        val initialAi = when {
            faction == ScenarioUnitFaction.MINE -> 1
            "ai" in entry -> entry["ai"].asIntOr(0)
            else -> 2
        }
        val battleSlot = BattleSlotLayout.slotFor(
            faction,
            if (faction == ScenarioUnitFaction.MINE) rosterIndex else instanceId,
            enemyBlockStart,
        )
        val hidden = entry["hide"].asIntOr(0) != 0
        return ScenarioStageBattleUnitCreation(
            ScenarioBattleUnit(
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
                hidden = hidden,
                ai = initialAi,
                aiTargetId = entry["targetId"].asIntOr(-1),
                aiTargetX = entry["targetX"].asIntOr(0),
                aiTargetY = entry["targetY"].asIntOr(0),
                battleSlot = battleSlot,
            ),
            initiallyVisible = !hidden,
        )
    }

    private fun Any?.asIntOr(default: Int): Int = when (this) {
        is Number -> toInt()
        is Boolean -> if (this) 1 else 0
        is String -> toIntOrNull() ?: default
        else -> default
    }
}

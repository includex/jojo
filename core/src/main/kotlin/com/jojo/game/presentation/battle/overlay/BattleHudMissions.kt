package com.jojo.game.presentation.battle.overlay

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.SafeConstructor

/** Optional authored HUD captions; full scripted conditions still govern battle outcomes. */
data class BattleHudMission(val victory: String, val defeat: String)

object BattleHudMissions {
    fun parse(source: String): Map<String, BattleHudMission> {
        val root = Yaml(SafeConstructor(LoaderOptions())).load<Any?>(source) as? Map<*, *> ?: return emptyMap()
        val missions = root["missions"] as? Map<*, *> ?: return emptyMap()
        return missions.entries.mapNotNull { (key, value) ->
            val name = key as? String ?: return@mapNotNull null
            val fields = value as? Map<*, *> ?: return@mapNotNull null
            val victory = fields["victory"] as? String ?: return@mapNotNull null
            name to BattleHudMission(victory, fields["defeat"] as? String ?: "")
        }.toMap()
    }
}

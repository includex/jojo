// Game
package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.TacticalUnit
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.BattleAvatarResolver

import java.util.*

/** ScenarioStageCampaignPresentationChanges: 렌더러 콜백이 필요한 캠페인 변경 기록을 관리한다. */
internal class ScenarioStageCampaignPresentationChanges {
    private val unitPostsRequests = ArrayDeque<ScenarioUnitPostsRequest>()
    private val scriptedUnitLevelChanges = ArrayDeque<CampaignUnitLevelChange>()
    private val scriptedUnitPostsChanges = ArrayDeque<CampaignUnitPostsChange>()
    var lastBattleUnitPostsRequiresPause: Boolean = false
        private set

    fun setBattleUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int,
        data: GameDataCatalog,
        enabledFeatures: Int,
        campaign: CampaignState,
        unitProvider: (Int) -> TacticalUnit,
        battleUnitForCharacterId: (Int) -> ScenarioBattleUnit?,
    ): CampaignUnitPostsChange? {
        lastBattleUnitPostsRequiresPause = false
        val oldPosts = campaign.unitAttribute(unitId, 17, data.unitProfile(unitId)?.posts ?: 0)
        val change = campaign.setUnitPosts(unitId, posts, flags, data, enabledFeatures) ?: return null
        unitProvider(unitId).posts = posts
        scriptedUnitPostsChanges.addLast(change)
        val battleUnit = battleUnitForCharacterId(unitId) ?: return change
        val oldAvatar = battleAvatarId(battleUnit, oldPosts, data)
        val newAvatar = battleAvatarId(battleUnit, posts, data)
        if (oldAvatar != null && newAvatar != null && oldAvatar != newAvatar) {
            val pausesScript = flags and 16 != 0
            unitPostsRequests.addLast(ScenarioUnitPostsRequest(unitId, oldAvatar, newAvatar, pausesScript))
            lastBattleUnitPostsRequiresPause = pausesScript
        }
        return change
    }

    fun setModelUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int,
        data: GameDataCatalog,
        enabledFeatures: Int,
        campaign: CampaignState,
        unitProvider: (Int) -> TacticalUnit,
    ): CampaignUnitPostsChange? {
        val change = campaign.setUnitPosts(unitId, posts, flags, data, enabledFeatures) ?: return null
        unitProvider(unitId).posts = posts
        scriptedUnitPostsChanges.addLast(change)
        return change
    }

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? =
        if (unitPostsRequests.isEmpty()) null else unitPostsRequests.removeFirst()

    fun addUnitLevels(
        unitId: Int,
        delta: Int,
        registeredFeatures: Int,
        campaign: CampaignState,
    ): CampaignUnitLevelChange? =
        campaign.addUnitLevels(unitId, delta, GameDataCatalog.load(), registeredFeatures)
            ?.also(scriptedUnitLevelChanges::addLast)

    fun consumeUnitLevelChanges(): List<CampaignUnitLevelChange> =
        scriptedUnitLevelChanges.toList().also { scriptedUnitLevelChanges.clear() }

    fun consumeUnitPostsChanges(): List<CampaignUnitPostsChange> =
        scriptedUnitPostsChanges.toList().also { scriptedUnitPostsChanges.clear() }

    private fun battleAvatarId(unit: ScenarioBattleUnit, posts: Int, data: GameDataCatalog): Int? {
        val faction = when (unit.faction) {
            ScenarioUnitFaction.MINE -> Faction.PLAYER
            ScenarioUnitFaction.FRIEND -> Faction.FRIEND
            ScenarioUnitFaction.ENEMY -> if (unit.reinforcement) Faction.REINFORCEMENTS else Faction.ENEMY
        }
        val armId = if (posts < 60) posts.floorDiv(3) else posts - 40
        return BattleAvatarResolver.resolve(data, unit.characterId, posts, armId, faction)
    }
}

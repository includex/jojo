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
    /**
     * `unitPostsRequests` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val unitPostsRequests = ArrayDeque<ScenarioUnitPostsRequest>()
    /**
     * `scriptedUnitLevelChanges` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val scriptedUnitLevelChanges = ArrayDeque<CampaignUnitLevelChange>()
    /**
     * `scriptedUnitPostsChanges` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val scriptedUnitPostsChanges = ArrayDeque<CampaignUnitPostsChange>()
    /**
     * `lastBattleUnitPostsRequiresPause` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lastBattleUnitPostsRequiresPause: Boolean = false
        private set

    /**
     * `setBattleUnitPosts`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
        /**
         * `oldPosts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val oldPosts = campaign.unitAttribute(unitId, 17, data.unitProfile(unitId)?.posts ?: 0)
        /**
         * `change` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val change = campaign.setUnitPosts(unitId, posts, flags, data, enabledFeatures) ?: return null
        unitProvider(unitId).posts = posts
        scriptedUnitPostsChanges.addLast(change)
        /**
         * `battleUnit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleUnit = battleUnitForCharacterId(unitId) ?: return change
        /**
         * `oldAvatar` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val oldAvatar = battleAvatarId(battleUnit, oldPosts, data)
        /**
         * `newAvatar` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val newAvatar = battleAvatarId(battleUnit, posts, data)
        if (oldAvatar != null && newAvatar != null && oldAvatar != newAvatar) {
            /**
             * `pausesScript` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val pausesScript = flags and 16 != 0
            unitPostsRequests.addLast(ScenarioUnitPostsRequest(unitId, oldAvatar, newAvatar, pausesScript))
            lastBattleUnitPostsRequiresPause = pausesScript
        }
        return change
    }

    /**
     * `setModelUnitPosts`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setModelUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int,
        data: GameDataCatalog,
        enabledFeatures: Int,
        campaign: CampaignState,
        unitProvider: (Int) -> TacticalUnit,
    ): CampaignUnitPostsChange? {
        /**
         * `change` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val change = campaign.setUnitPosts(unitId, posts, flags, data, enabledFeatures) ?: return null
        unitProvider(unitId).posts = posts
        scriptedUnitPostsChanges.addLast(change)
        return change
    }

    /**
     * `consumeUnitPostsRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? =
        if (unitPostsRequests.isEmpty()) null else unitPostsRequests.removeFirst()

    /**
     * `addUnitLevels`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun addUnitLevels(
        unitId: Int,
        delta: Int,
        registeredFeatures: Int,
        campaign: CampaignState,
    ): CampaignUnitLevelChange? =
        campaign.addUnitLevels(unitId, delta, GameDataCatalog.load(), registeredFeatures)
            ?.also(scriptedUnitLevelChanges::addLast)

    /**
     * `consumeUnitLevelChanges`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitLevelChanges(): List<CampaignUnitLevelChange> =
        scriptedUnitLevelChanges.toList().also { scriptedUnitLevelChanges.clear() }

    /**
     * `consumeUnitPostsChanges`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitPostsChanges(): List<CampaignUnitPostsChange> =
        scriptedUnitPostsChanges.toList().also { scriptedUnitPostsChanges.clear() }

    /**
     * `battleAvatarId`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

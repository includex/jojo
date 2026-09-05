package com.jojo.game

import java.util.ArrayDeque

internal class ScenarioStagePresentationCoordinator {
    private val unitHideRequests = ArrayDeque<ScenarioUnitHideRequest>()
    private var unitShowRequest: ScenarioUnitShowRequest? = null
    private val unitPostsRequests = ArrayDeque<ScenarioUnitPostsRequest>()
    var lastBattleUnitPostsRequiresPause: Boolean = false
        private set
    private var mapPresentationRequest: ScenarioMapPresentationRequest? = null
    private val cameraCenterRequests = ArrayDeque<ScenarioCameraCenterRequest>()
    private val scriptPresentationRequests = ArrayDeque<ScenarioScriptPresentationRequest>()

    val scriptedAttacks = mutableListOf<ScriptedAttackAction>()
    val scriptedUnitActions = mutableListOf<ScriptedUnitAction>()
    private val scriptedUnitLevelChanges = ArrayDeque<CampaignUnitLevelChange>()
    private val scriptedUnitPostsChanges = ArrayDeque<CampaignUnitPostsChange>()
    val scriptedUnitDirections = mutableListOf<Pair<Int, Int>>()

    fun requestUnitHide(unitId: Int, hideType: Int) {
        unitHideRequests.addLast(ScenarioUnitHideRequest(unitId, hideType.coerceIn(0, 2)))
    }

    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? =
        if (unitHideRequests.isEmpty()) null else unitHideRequests.removeFirst()

    fun requestRectUnitHide(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        camp: Int,
        hideType: Int,
        battleUnits: Map<String, ScenarioBattleUnit>,
        mineMasterInstanceId: Int,
        matchesAiCamp: (ScenarioBattleUnit, Int) -> Boolean,
    ): Int {
        val selected = battleUnits.entries
            .filter { (_, unit) ->
                !unit.hidden && matchesAiCamp(unit, camp) &&
                    unit.x in minOf(x1, x2)..maxOf(x1, x2) &&
                    unit.y in minOf(y1, y2)..maxOf(y1, y2)
            }
            .sortedWith(compareBy({ it.value.y }, { it.value.x }))
        var effectiveHideType = hideType.coerceIn(0, 2)
        selected.forEachIndexed { index, (_, unit) ->
            val showsRetireMessage = effectiveHideType == 1
            if (showsRetireMessage && unit.faction == ScenarioUnitFaction.MINE &&
                unit.characterId == mineMasterInstanceId
            ) effectiveHideType = 2
            unitHideRequests.addLast(
                ScenarioUnitHideRequest(
                    unitId = unit.characterId,
                    hideType = effectiveHideType,
                    battleUnitId = unit.battleId,
                    resumesScript = index == selected.lastIndex,
                    showsRetireMessage = showsRetireMessage,
                ),
            )
        }
        return selected.size
    }

    fun completeUnitHide(
        request: ScenarioUnitHideRequest,
        battleUnits: Map<String, ScenarioBattleUnit>,
        unitProvider: (Int) -> TacticalUnit,
        setBattleUnitVisibility: (Int, Boolean) -> Unit,
    ) {
        val exact = request.battleUnitId
        if (exact == null) {
            setBattleUnitVisibility(request.unitId, false)
            return
        }
        battleUnits.values.firstOrNull {
            it.battleId == exact
        }?.hidden = true
        unitProvider(request.unitId).visible = battleUnits.values.any {
            it.characterId == request.unitId && !it.hidden
        }
    }

    fun requestUnitShow(
        request: ScenarioUnitShowRequest,
        battleUnitForCharacterId: (Int) -> ScenarioBattleUnit?,
    ) {
        check(unitShowRequest == null) { "unit show callback is already pending" }
        unitShowRequest = request
        battleUnitForCharacterId(request.unitId)?.hidden = false
    }

    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? =
        unitShowRequest.also { unitShowRequest = null }

    fun setBattleUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 19,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0,
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
        flags: Int = 3,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0,
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

    private fun battleAvatarId(unit: ScenarioBattleUnit, posts: Int, data: GameDataCatalog): Int? {
        val faction = when (unit.faction) {
            ScenarioUnitFaction.MINE -> Faction.PLAYER
            ScenarioUnitFaction.FRIEND -> Faction.FRIEND
            ScenarioUnitFaction.ENEMY -> if (unit.reinforcement) Faction.REINFORCEMENTS else Faction.ENEMY
        }
        val armId = if (posts < 60) posts.floorDiv(3) else posts - 40
        return BattleAvatarResolver.resolve(data, unit.characterId, posts, armId, faction)
    }

    fun requestMapPresentation(request: ScenarioMapPresentationRequest) {
        check(mapPresentationRequest == null) { "map presentation callback is already pending" }
        mapPresentationRequest = request
    }

    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? =
        mapPresentationRequest.also { mapPresentationRequest = null }

    fun requestCameraCenter(x: Int, y: Int) {
        cameraCenterRequests.addLast(ScenarioCameraCenterRequest(x, y))
    }

    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> =
        cameraCenterRequests.toList().also { cameraCenterRequests.clear() }

    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) {
        scriptPresentationRequests.addLast(request)
    }

    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        if (scriptPresentationRequests.isEmpty()) null else scriptPresentationRequests.removeFirst()

    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        scriptPresentationRequests.toList().also { scriptPresentationRequests.clear() }

    fun attackAction(attackerId: Int, targetId: Int, flag: Int) {
        scriptedAttacks += ScriptedAttackAction(attackerId, targetId, flag)
    }

    fun setScriptedUnitAction(
        unitId: Int,
        action: Int,
        direction: Int = -1,
        loop: Boolean = false,
        unitProvider: (Int) -> TacticalUnit,
        setUnitDirection: (Int, Int) -> Unit,
    ) {
        unitProvider(unitId).action = action
        if (direction >= 0) setUnitDirection(unitId, direction)
        scriptedUnitActions += ScriptedUnitAction(unitId, action, direction, loop)
    }

    fun consumeScriptedAttacks(): List<ScriptedAttackAction> =
        scriptedAttacks.toList().also { scriptedAttacks.clear() }

    fun consumeScriptedUnitActions(): List<ScriptedUnitAction> =
        scriptedUnitActions.toList().also { scriptedUnitActions.clear() }

    fun consumeScriptedUnitDirections(): List<Pair<Int, Int>> =
        scriptedUnitDirections.toList().also { scriptedUnitDirections.clear() }

    fun addUnitLevels(
        unitId: Int,
        delta: Int,
        registeredFeatures: Int = 0,
        campaign: CampaignState,
    ): CampaignUnitLevelChange? =
        campaign.addUnitLevels(unitId, delta, GameDataCatalog.load(), registeredFeatures)
            ?.also(scriptedUnitLevelChanges::addLast)

    fun consumeScriptedUnitLevelChanges(): List<CampaignUnitLevelChange> =
        scriptedUnitLevelChanges.toList().also { scriptedUnitLevelChanges.clear() }

    fun consumeScriptedUnitPostsChanges(): List<CampaignUnitPostsChange> =
        scriptedUnitPostsChanges.toList().also { scriptedUnitPostsChanges.clear() }
}

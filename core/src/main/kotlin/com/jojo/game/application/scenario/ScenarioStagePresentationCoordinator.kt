// Game
package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.TacticalUnit
import com.jojo.game.domain.campaign.*

/** ScenarioStagePresentationCoordinator: 독립된 표시 요청 대기열과 캠페인 변경을 조합한다. */
internal class ScenarioStagePresentationCoordinator {
    private val requests = ScenarioStagePresentationRequestQueue()
    private val actions = ScenarioStageScriptedActions()
    private val campaignChanges = ScenarioStageCampaignPresentationChanges()

    val scriptedAttacks: MutableList<ScriptedAttackAction> get() = actions.attacks
    val scriptedUnitActions: MutableList<ScriptedUnitAction> get() = actions.unitActions
    val scriptedUnitDirections: MutableList<Pair<Int, Int>> get() = actions.unitDirections
    val lastBattleUnitPostsRequiresPause: Boolean get() = campaignChanges.lastBattleUnitPostsRequiresPause

    fun requestUnitHide(unitId: Int, hideType: Int) = requests.requestUnitHide(unitId, hideType)
    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? = requests.consumeUnitHideRequest()
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
    ): Int =
        requests.requestRectUnitHide(x1, y1, x2, y2, camp, hideType, battleUnits, mineMasterInstanceId, matchesAiCamp)

    fun completeUnitHide(
        request: ScenarioUnitHideRequest,
        battleUnits: Map<String, ScenarioBattleUnit>,
        unitProvider: (Int) -> TacticalUnit,
        setBattleUnitVisibility: (Int, Boolean) -> Unit,
    ) = requests.completeUnitHide(request, battleUnits, unitProvider, setBattleUnitVisibility)

    fun requestUnitShow(request: ScenarioUnitShowRequest, battleUnitForCharacterId: (Int) -> ScenarioBattleUnit?) =
        requests.requestUnitShow(request, battleUnitForCharacterId)

    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? = requests.consumeUnitShowRequest()

    fun setBattleUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 19,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0,
        campaign: CampaignState,
        unitProvider: (Int) -> TacticalUnit,
        battleUnitForCharacterId: (Int) -> ScenarioBattleUnit?,
    ): CampaignUnitPostsChange? = campaignChanges.setBattleUnitPosts(
        unitId, posts, flags, data, enabledFeatures, campaign, unitProvider, battleUnitForCharacterId,
    )

    fun setModelUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 3,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0,
        campaign: CampaignState,
        unitProvider: (Int) -> TacticalUnit,
    ): CampaignUnitPostsChange? = campaignChanges.setModelUnitPosts(
        unitId, posts, flags, data, enabledFeatures, campaign, unitProvider,
    )

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? = campaignChanges.consumeUnitPostsRequest()
    fun requestMapPresentation(request: ScenarioMapPresentationRequest) = requests.requestMapPresentation(request)
    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? = requests.consumeMapPresentationRequest()
    fun requestCameraCenter(x: Int, y: Int) = requests.requestCameraCenter(x, y)
    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> = requests.consumeCameraCenterRequests()
    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) =
        requests.requestScriptPresentation(request)

    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        requests.consumeScriptPresentationRequest()

    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        requests.consumeScriptPresentationRequests()

    fun attackAction(attackerId: Int, targetId: Int, flag: Int) = actions.attack(attackerId, targetId, flag)
    fun setScriptedUnitAction(
        unitId: Int,
        action: Int,
        direction: Int = -1,
        loop: Boolean = false,
        unitProvider: (Int) -> TacticalUnit,
        setUnitDirection: (Int, Int) -> Unit,
    ) = actions.setUnitAction(unitId, action, direction, loop, unitProvider, setUnitDirection)

    fun consumeScriptedAttacks(): List<ScriptedAttackAction> = actions.consumeAttacks()
    fun consumeScriptedUnitActions(): List<ScriptedUnitAction> = actions.consumeUnitActions()
    fun consumeScriptedUnitDirections(): List<Pair<Int, Int>> = actions.consumeUnitDirections()

    fun addUnitLevels(
        unitId: Int,
        delta: Int,
        registeredFeatures: Int = 0,
        campaign: CampaignState,
    ): CampaignUnitLevelChange? = campaignChanges.addUnitLevels(unitId, delta, registeredFeatures, campaign)

    fun consumeScriptedUnitLevelChanges(): List<CampaignUnitLevelChange> = campaignChanges.consumeUnitLevelChanges()
    fun consumeScriptedUnitPostsChanges(): List<CampaignUnitPostsChange> = campaignChanges.consumeUnitPostsChanges()
}

// Game
package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.TacticalUnit
import com.jojo.game.domain.campaign.*

/** ScenarioStagePresentationCoordinator: 독립된 표시 요청 대기열과 캠페인 변경을 조합한다. */
internal class ScenarioStagePresentationCoordinator {
    /**
     * `requests` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val requests = ScenarioStagePresentationRequestQueue()
    /**
     * `actions` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val actions = ScenarioStageScriptedActions()
    /**
     * `campaignChanges` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val campaignChanges = ScenarioStageCampaignPresentationChanges()

    /**
     * `scriptedAttacks` (MutableList<ScriptedAttackAction> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val scriptedAttacks: MutableList<ScriptedAttackAction> get() = actions.attacks
    /**
     * `scriptedUnitActions` (MutableList<ScriptedUnitAction> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val scriptedUnitActions: MutableList<ScriptedUnitAction> get() = actions.unitActions
    /**
     * `scriptedUnitDirections` (MutableList<Pair<Int, Int>> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val scriptedUnitDirections: MutableList<Pair<Int, Int>> get() = actions.unitDirections
    /**
     * `lastBattleUnitPostsRequiresPause` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val lastBattleUnitPostsRequiresPause: Boolean get() = campaignChanges.lastBattleUnitPostsRequiresPause

    /**
     * `requestUnitHide`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestUnitHide(unitId: Int, hideType: Int) = requests.requestUnitHide(unitId, hideType)
    /**
     * `consumeUnitHideRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? = requests.consumeUnitHideRequest()
    /**
     * `requestRectUnitHide`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `completeUnitHide`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun completeUnitHide(
        request: ScenarioUnitHideRequest,
        battleUnits: Map<String, ScenarioBattleUnit>,
        unitProvider: (Int) -> TacticalUnit,
        setBattleUnitVisibility: (Int, Boolean) -> Unit,
    ) = requests.completeUnitHide(request, battleUnits, unitProvider, setBattleUnitVisibility)

    /**
     * `requestUnitShow`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestUnitShow(request: ScenarioUnitShowRequest, battleUnitForCharacterId: (Int) -> ScenarioBattleUnit?) =
        requests.requestUnitShow(request, battleUnitForCharacterId)

    /**
     * `consumeUnitShowRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? = requests.consumeUnitShowRequest()

    /**
     * `setBattleUnitPosts`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `setModelUnitPosts`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `consumeUnitPostsRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? = campaignChanges.consumeUnitPostsRequest()
    /**
     * `requestMapPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestMapPresentation(request: ScenarioMapPresentationRequest) = requests.requestMapPresentation(request)
    /**
     * `consumeMapPresentationRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? = requests.consumeMapPresentationRequest()
    /**
     * `requestCameraCenter`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestCameraCenter(x: Int, y: Int) = requests.requestCameraCenter(x, y)
    /**
     * `consumeCameraCenterRequests`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> = requests.consumeCameraCenterRequests()
    /**
     * `requestScriptPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) =
        requests.requestScriptPresentation(request)

    /**
     * `consumeScriptPresentationRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        requests.consumeScriptPresentationRequest()

    /**
     * `consumeScriptPresentationRequests`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        requests.consumeScriptPresentationRequests()

    /**
     * `attackAction`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun attackAction(attackerId: Int, targetId: Int, flag: Int) = actions.attack(attackerId, targetId, flag)
    /**
     * `setScriptedUnitAction`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setScriptedUnitAction(
        unitId: Int,
        action: Int,
        direction: Int = -1,
        loop: Boolean = false,
        unitProvider: (Int) -> TacticalUnit,
        setUnitDirection: (Int, Int) -> Unit,
    ) = actions.setUnitAction(unitId, action, direction, loop, unitProvider, setUnitDirection)

    /**
     * `consumeScriptedAttacks`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedAttacks(): List<ScriptedAttackAction> = actions.consumeAttacks()
    /**
     * `consumeScriptedUnitActions`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedUnitActions(): List<ScriptedUnitAction> = actions.consumeUnitActions()
    /**
     * `consumeScriptedUnitDirections`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedUnitDirections(): List<Pair<Int, Int>> = actions.consumeUnitDirections()

    /**
     * `addUnitLevels`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun addUnitLevels(
        unitId: Int,
        delta: Int,
        registeredFeatures: Int = 0,
        campaign: CampaignState,
    ): CampaignUnitLevelChange? = campaignChanges.addUnitLevels(unitId, delta, registeredFeatures, campaign)

    /**
     * `consumeScriptedUnitLevelChanges`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedUnitLevelChanges(): List<CampaignUnitLevelChange> = campaignChanges.consumeUnitLevelChanges()
    /**
     * `consumeScriptedUnitPostsChanges`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedUnitPostsChanges(): List<CampaignUnitPostsChange> = campaignChanges.consumeUnitPostsChanges()
}

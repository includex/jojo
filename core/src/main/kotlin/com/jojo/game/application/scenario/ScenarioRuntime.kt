// Game
package com.jojo.game.application.scenario

import com.jojo.game.infrastructure.data.HallPathGrid

import com.jojo.game.*
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.battle.*


import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.ScenarioHead
import com.jojo.game.domain.scenario.TacticalUnit
import com.jojo.game.domain.campaign.*
import com.jojo.game.application.scenario.battle.ScenarioStageBattleAccess
import com.jojo.game.application.scenario.battle.ScenarioStageBattleSetup

/** ScenarioStage: 시나리오가 사용하는 최소한의 무대 상태를 제공한다. */
class ScenarioStage private constructor(
    /**
     * `campaign` (CampaignState,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val campaign: CampaignState,
    /**
     * `battleSetup` (ScenarioStageBattleSetup,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val battleSetup: ScenarioStageBattleSetup,
    /**
     * `worldState` (ScenarioStageWorldState,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val worldState: ScenarioStageWorldState,
) : ScenarioStageBattleAccess by battleSetup, ScenarioStageWorldAccess by worldState {
    constructor(campaign: CampaignState = CampaignState()) : this(
        campaign,
        ScenarioStageBattleSetup(campaign),
        ScenarioStageWorldState(),
    )

    /**
     * `unitRegistry` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val unitRegistry = ScenarioStageUnitRegistry()
    /**
     * `movementCoordinator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val movementCoordinator = ScenarioStageMovementCoordinator()
    /**
     * `presentationCoordinator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val presentationCoordinator = ScenarioStagePresentationCoordinator()
    /**
     * `fightCoordinator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val fightCoordinator = ScenarioStageFightCoordinator()
    // --- 시나리오 전용 상태 ---
    var backgroundId: Int = 0; private set
    /**
     * `backgroundVariant` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var backgroundVariant: Int = 0; private set
    /**
     * `eventName` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var eventName: String = ""; private set
    /**
     * `stageName` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var stageName: String = ""; private set
    /**
     * `menuVisible` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var menuVisible: Boolean = true; private set
    /**
     * `ambition` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var ambition: Int = 50; private set
    /**
     * `lastBattleUnitPostsRequiresPause` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val lastBattleUnitPostsRequiresPause: Boolean get() = presentationCoordinator.lastBattleUnitPostsRequiresPause
    /**
     * `bottomText` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var bottomText: String = ""; private set
    /**
     * `fightInitialized` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val fightInitialized: Boolean get() = fightCoordinator.fightInitialized
    /**
     * `backgroundSound` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val backgroundSound: Int get() = fightCoordinator.backgroundSound
    /**
     * `sceneIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var sceneIndex: Int = 0; private set
    /**
     * `face` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var face: Int = 0; private set
    /**
     * `section` (Pair<Int, String>?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var section: Pair<Int, String>? = null; private set
    /**
     * `endingId` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var endingId: Int? = null; private set
    /**
     * `sceneJumpTarget` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var sceneJumpTarget: Int? = null; private set
    /**
     * `sceneJumpStage` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var sceneJumpStage: Int? = null; private set
    /**
     * `activeFightId` (Long? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val activeFightId: Long? get() = fightCoordinator.activeFightId
    /**
     * `units` (MutableMap<Int, TacticalUnit> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val units: MutableMap<Int, TacticalUnit> get() = unitRegistry.units
    /**
     * `battleUnits` (MutableMap<String, ScenarioBattleUnit> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleUnits: MutableMap<String, ScenarioBattleUnit> get() = unitRegistry.battleUnits
    /**
     * `heads` (MutableMap<Int, ScenarioHead> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val heads: MutableMap<Int, ScenarioHead> get() = movementCoordinator.heads
    /**
     * `scriptedAttacks` (MutableList<ScriptedAttackAction> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val scriptedAttacks: MutableList<ScriptedAttackAction> get() = presentationCoordinator.scriptedAttacks
    /**
     * `scriptedUnitActions` (MutableList<ScriptedUnitAction> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val scriptedUnitActions: MutableList<ScriptedUnitAction> get() = presentationCoordinator.scriptedUnitActions
    /**
     * `joinedUnits` (MutableSet<Int> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val joinedUnits: MutableSet<Int> get() = campaign.joinedUnits
    /**
     * `joinedEquipment` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val joinedEquipment = linkedMapOf<Int, ScenarioJoinEquipment>()
    /**
     * `unitAttributes` (MutableMap<Int, MutableMap<Int, Int>> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitAttributes: MutableMap<Int, MutableMap<Int, Int>> get() = campaign.unitAttributes

    // --- 단순 설정 함수 ---
    fun clearUnits() = unitRegistry.clearUnits()
    /**
     * `setMenuVisible`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setMenuVisible(visible: Boolean) {
        menuVisible = visible
    }

    /**
     * `setStageName`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setStageName(name: String) {
        stageName = name
    }

    /**
     * `addAmbition`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addAmbition(delta: Int) {
        ambition += delta
    }

    /**
     * `setBottomText`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setBottomText(text: String) {
        bottomText = text
    }

    /**
     * `incrementSceneIndex`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun incrementSceneIndex() {
        sceneIndex++
    }

    /**
     * `setFace`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setFace(faceId: Int) {
        face = faceId
    }

    /**
     * `setSection`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setSection(number: Int, name: String) {
        section = number to name
    }

    // --- 다른 상태를 포함하는 공개 Stage 형태의 전투 설정 어댑터 ---
    fun getItem(itemId: Int, suppliedCountOrLevel: Int = 0, addToInventory: Boolean = true): String =
        battleSetup.getItem(itemId, suppliedCountOrLevel, addToInventory, acquiredItems)

    /**
     * `battleItemCompletionMessage`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleItemCompletionMessage(itemId: Int): String = battleSetup.battleItemCompletionMessage(itemId)
    /**
     * `setJoinEquip`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setJoinEquip(unitId: Int, weapon: Int, weaponLevel: Int, armor: Int, armorLevel: Int, auxiliary: Int) =
        battleSetup.setJoinEquip(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary, joinedEquipment)

    /**
     * `ending`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun ending(id: Int) = battleSetup.ending(id) { endingId = it }
    /**
     * `infoTransfer`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun infoTransfer(type: Int, payload: String, selectedUnitId: Int = 0) =
        battleSetup.infoTransfer(type, payload, selectedUnitId, infoTransfers)

    /**
     * `jumpScene`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun jumpScene(target: Int) = battleSetup.jumpScene(target, { sceneJumpTarget = it }, { sceneJumpStage = it })
    /**
     * `resetLocalVariables`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun resetLocalVariables() = Unit
    // --- 표시 조정자 위임 함수 ---
    fun requestUnitHide(unitId: Int, hideType: Int) = presentationCoordinator.requestUnitHide(unitId, hideType)
    /**
     * `consumeUnitHideRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? = presentationCoordinator.consumeUnitHideRequest()
    /**
     * `requestRectUnitHide`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestRectUnitHide(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int, hideType: Int): Int =
        presentationCoordinator.requestRectUnitHide(
            x1,
            y1,
            x2,
            y2,
            camp,
            hideType,
            battleUnits,
            mineMasterInstanceId
        ) { unit, c -> with(unitRegistry) { unit.matchesAiCamp(c) } }

    /**
     * `completeUnitHide`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeUnitHide(request: ScenarioUnitHideRequest) =
        presentationCoordinator.completeUnitHide(request, battleUnits, ::unit, ::setBattleUnitVisibility)

    /**
     * `requestUnitShow`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestUnitShow(request: ScenarioUnitShowRequest) =
        presentationCoordinator.requestUnitShow(request, ::battleUnitForCharacterId)

    /**
     * `consumeUnitShowRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? = presentationCoordinator.consumeUnitShowRequest()
    /**
     * `setBattleUnitPosts`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setBattleUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 19,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0
    ): CampaignUnitPostsChange? =
        presentationCoordinator.setBattleUnitPosts(
            unitId,
            posts,
            flags,
            data,
            enabledFeatures,
            campaign,
            ::unit,
            ::battleUnitForCharacterId
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
        enabledFeatures: Int = 0
    ): CampaignUnitPostsChange? =
        presentationCoordinator.setModelUnitPosts(unitId, posts, flags, data, enabledFeatures, campaign, ::unit)

    /**
     * `consumeUnitPostsRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? = presentationCoordinator.consumeUnitPostsRequest()
    /**
     * `requestMapPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestMapPresentation(request: ScenarioMapPresentationRequest) =
        presentationCoordinator.requestMapPresentation(request)

    /**
     * `consumeMapPresentationRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? =
        presentationCoordinator.consumeMapPresentationRequest()

    /**
     * `requestCameraCenter`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestCameraCenter(x: Int, y: Int) = presentationCoordinator.requestCameraCenter(x, y)
    /**
     * `consumeCameraCenterRequests`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> =
        presentationCoordinator.consumeCameraCenterRequests()

    /**
     * `requestScriptPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) =
        presentationCoordinator.requestScriptPresentation(request)

    /**
     * `consumeScriptPresentationRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        presentationCoordinator.consumeScriptPresentationRequest()

    /**
     * `consumeScriptPresentationRequests`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        presentationCoordinator.consumeScriptPresentationRequests()

    /**
     * `setBattleUnitVisibility`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setBattleUnitVisibility(unitId: Int, visible: Boolean) = unitRegistry.setBattleUnitVisibility(unitId, visible)
    /**
     * `addUnitLevels`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addUnitLevels(unitId: Int, delta: Int, registeredFeatures: Int = 0): CampaignUnitLevelChange? =
        presentationCoordinator.addUnitLevels(unitId, delta, registeredFeatures, campaign)

    /**
     * `consumeScriptedUnitLevelChanges`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedUnitLevelChanges(): List<CampaignUnitLevelChange> =
        presentationCoordinator.consumeScriptedUnitLevelChanges()

    /**
     * `consumeScriptedUnitPostsChanges`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedUnitPostsChanges(): List<CampaignUnitPostsChange> =
        presentationCoordinator.consumeScriptedUnitPostsChanges()

    /**
     * `attackAction`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun attackAction(attackerId: Int, targetId: Int, flag: Int) =
        presentationCoordinator.attackAction(attackerId, targetId, flag)

    /**
     * `setScriptedUnitAction`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setScriptedUnitAction(unitId: Int, action: Int, direction: Int = -1, loop: Boolean = false) =
        presentationCoordinator.setScriptedUnitAction(unitId, action, direction, loop, ::unit, ::setUnitDirection)

    /**
     * `consumeScriptedAttacks`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedAttacks(): List<ScriptedAttackAction> = presentationCoordinator.consumeScriptedAttacks()
    /**
     * `consumeScriptedUnitActions`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedUnitActions(): List<ScriptedUnitAction> = presentationCoordinator.consumeScriptedUnitActions()
    /**
     * `consumeScriptedUnitDirections`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptedUnitDirections(): List<Pair<Int, Int>> = presentationCoordinator.consumeScriptedUnitDirections()

    // --- 전투 조정자 위임 함수 ---
    fun initFight() = fightCoordinator.initFight()
    /**
     * `startFight`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun startFight(firstUnitId: Int, secondUnitId: Int, backgroundIndex: Int): Long =
        fightCoordinator.startFight(firstUnitId, secondUnitId, backgroundIndex)

    /**
     * `enqueueFightCommand`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun enqueueFightCommand(command: ScenarioFightCommand) = fightCoordinator.enqueueFightCommand(command)
    /**
     * `consumeFightCommands`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeFightCommands(): List<ScenarioFightCommand> = fightCoordinator.consumeFightCommands()
    /**
     * `setBackgroundSound`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setBackgroundSound(soundId: Int) = fightCoordinator.setBackgroundSound(soundId)
    /**
     * `effectSound`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun effectSound(soundId: Int, mode: Int = 1) = fightCoordinator.effectSound(soundId, mode)
    /**
     * `consumeSoundEffects`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeSoundEffects(): List<ScenarioSoundEffect> = fightCoordinator.consumeSoundEffects()

    // --- 이동 조정자 위임 함수 ---
    fun enableBattleMovementTimeline() {
        movementCoordinator.battleMovementTimeline = true
    }

    /**
     * `setBattleMovePathResolver`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setBattleMovePathResolver(resolver: (Int, Int, Int) -> List<Pair<Int, Int>>?) {
        movementCoordinator.battleMovePathResolver = resolver
    }

    /**
     * `head`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun head(id: Int): ScenarioHead = movementCoordinator.head(id)
    /**
     * `moveHead`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moveHead(id: Int, x: Int, y: Int): Float = movementCoordinator.moveHead(id, x, y)
    /**
     * `showHead`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showHead(id: Int, x: Int, y: Int): Float = movementCoordinator.showHead(id, x, y)
    /**
     * `hideHead`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hideHead(id: Int): Float = movementCoordinator.hideHead(id)
    /**
     * `countDirection`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun countDirection(fromId: Int, toId: Int): Int = movementCoordinator.countDirection(fromId, toId, ::unit)
    /**
     * `moveDuration`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moveDuration(id: Int, x: Int, y: Int): Float = movementCoordinator.moveDuration(id, x, y, units)
    /**
     * `moveUnits`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moveUnits(requests: List<ScenarioCommand.MoveUnit>): Float =
        movementCoordinator.moveUnits(requests, units) { presentationCoordinator.scriptedUnitDirections += it }

    /**
     * `updateAnimations`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun updateAnimations(delta: Float) = movementCoordinator.updateAnimations(delta, units)
    /**
     * `finishAnimations`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun finishAnimations() = movementCoordinator.finishAnimations(units)

    // --- 유닛 등록부 위임 함수 ---
    fun createBattleUnits(faction: ScenarioUnitFaction, entries: List<Any?>) =
        unitRegistry.createBattleUnits(faction, entries, campaign)

    /**
     * `battleUnitForCharacterId`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleUnitForCharacterId(characterId: Int): ScenarioBattleUnit? =
        unitRegistry.battleUnitForCharacterId(characterId)

    /**
     * `battleUnitForSlot`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleUnitForSlot(battleSlot: Int): ScenarioBattleUnit? = unitRegistry.battleUnitForSlot(battleSlot)
    /**
     * `setBattleAi`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setBattleAi(
        camp: Int,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        ai: Int,
        targetId: Int = -1,
        targetX: Int = 0,
        targetY: Int = 0
    ) = unitRegistry.setBattleAi(camp, x1, y1, x2, y2, ai, targetId, targetX, targetY)

    /**
     * `setUnitAi`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitAi(unitId: Int, ai: Int, targetId: Int = -1, targetX: Int = 0, targetY: Int = 0) =
        unitRegistry.setUnitAi(unitId, ai, targetId, targetX, targetY)

    /**
     * `setUnitRetreatTextEnabled`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitRetreatTextEnabled(unitId: Int, enabled: Boolean) =
        unitRegistry.setUnitRetreatTextEnabled(unitId, enabled)

    /**
     * `hideBattleRect`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hideBattleRect(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int) =
        unitRegistry.hideBattleRect(x1, y1, x2, y2, camp)

    /**
     * `unit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unit(id: Int): TacticalUnit = unitRegistry.unit(id)
    /**
     * `seedBattleUnitPosition`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun seedBattleUnitPosition(id: Int, x: Int, y: Int) = unitRegistry.seedBattleUnitPosition(id, x, y)
    /**
     * `setUnitDirection`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitDirection(id: Int, direction: Int) =
        unitRegistry.setUnitDirection(id, direction) { presentationCoordinator.scriptedUnitDirections += it }

    // --- 명령 분배 ---
    fun apply(command: ScenarioCommand) {
        when (command) {
            is ScenarioCommand.LoadBackground -> {
                backgroundId = when (command.backgroundId) {
                    0 -> command.variant + 1; 1 -> 115; 2 -> command.variant + 41; else -> command.variant
                }
                backgroundVariant = command.variant
                movementCoordinator.hallPathGrid =
                    if (command.backgroundId == 2) HallPathGrid.loadOrNull(command.variant) else null
            }

            is ScenarioCommand.SetEventName -> eventName = command.name
            is ScenarioCommand.ShowUnit -> unitRegistry.setUnit(
                command.unitId,
                command.x,
                command.y,
                command.direction
            ) { presentationCoordinator.scriptedUnitDirections += it }

            is ScenarioCommand.MoveUnit -> movementCoordinator.moveUnit(
                command.unitId,
                command.x,
                command.y,
                command.direction,
                units
            ) { presentationCoordinator.scriptedUnitDirections += it }

            is ScenarioCommand.SetUnitAction -> setScriptedUnitAction(command.unitId, command.action)
            is ScenarioCommand.DialogueLine, is ScenarioCommand.Choose -> Unit
        }
    }
}

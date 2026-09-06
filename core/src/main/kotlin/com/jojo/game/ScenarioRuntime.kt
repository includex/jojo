package com.jojo.game

import com.jojo.game.domain.battle.*


import com.jojo.game.domain.scenario.*
import com.jojo.game.presentation.scenario.ScenarioHead
import com.jojo.game.presentation.scenario.TacticalUnit
import com.jojo.game.domain.campaign.*
import com.jojo.game.application.scenario.battle.ScenarioStageBattleAccess
import com.jojo.game.application.scenario.battle.ScenarioStageBattleSetup

/** 시나리오가 사용하는 최소한의 무대 상태를 제공한다. */
class ScenarioStage private constructor(
    private val campaign: CampaignState,
    private val battleSetup: ScenarioStageBattleSetup,
    private val worldState: ScenarioStageWorldState,
) : ScenarioStageBattleAccess by battleSetup, ScenarioStageWorldAccess by worldState {
    constructor(campaign: CampaignState = CampaignState()) : this(
        campaign,
        ScenarioStageBattleSetup(campaign),
        ScenarioStageWorldState(),
    )

    private val unitRegistry = ScenarioStageUnitRegistry()
    private val movementCoordinator = ScenarioStageMovementCoordinator()
    private val presentationCoordinator = ScenarioStagePresentationCoordinator()
    private val fightCoordinator = ScenarioStageFightCoordinator()
    // --- 시나리오 전용 상태 ---
    var backgroundId: Int = 0; private set
    var backgroundVariant: Int = 0; private set
    var eventName: String = ""; private set
    var stageName: String = ""; private set
    var menuVisible: Boolean = true; private set
    var ambition: Int = 50; private set
    val lastBattleUnitPostsRequiresPause: Boolean get() = presentationCoordinator.lastBattleUnitPostsRequiresPause
    var bottomText: String = ""; private set
    val fightInitialized: Boolean get() = fightCoordinator.fightInitialized
    val backgroundSound: Int get() = fightCoordinator.backgroundSound
    var sceneIndex: Int = 0; private set
    var face: Int = 0; private set
    var section: Pair<Int, String>? = null; private set
    var endingId: Int? = null; private set
    var sceneJumpTarget: Int? = null; private set
    var sceneJumpStage: Int? = null; private set
    val activeFightId: Long? get() = fightCoordinator.activeFightId
    val units: MutableMap<Int, TacticalUnit> get() = unitRegistry.units
    val battleUnits: MutableMap<String, ScenarioBattleUnit> get() = unitRegistry.battleUnits
    val heads: MutableMap<Int, ScenarioHead> get() = movementCoordinator.heads
    val scriptedAttacks: MutableList<ScriptedAttackAction> get() = presentationCoordinator.scriptedAttacks
    val scriptedUnitActions: MutableList<ScriptedUnitAction> get() = presentationCoordinator.scriptedUnitActions
    val joinedUnits: MutableSet<Int> get() = campaign.joinedUnits
    val joinedEquipment = linkedMapOf<Int, ScenarioJoinEquipment>()
    val unitAttributes: MutableMap<Int, MutableMap<Int, Int>> get() = campaign.unitAttributes

    // --- 단순 설정 함수 ---
    fun clearUnits() = unitRegistry.clearUnits()
    fun setMenuVisible(visible: Boolean) {
        menuVisible = visible
    }

    fun setStageName(name: String) {
        stageName = name
    }

    fun addAmbition(delta: Int) {
        ambition += delta
    }

    fun setBottomText(text: String) {
        bottomText = text
    }

    fun incrementSceneIndex() {
        sceneIndex++
    }

    fun setFace(faceId: Int) {
        face = faceId
    }

    fun setSection(number: Int, name: String) {
        section = number to name
    }

    // --- 다른 상태를 포함하는 공개 Stage 형태의 전투 설정 어댑터 ---
    fun getItem(itemId: Int, suppliedCountOrLevel: Int = 0, addToInventory: Boolean = true): String =
        battleSetup.getItem(itemId, suppliedCountOrLevel, addToInventory, acquiredItems)

    fun battleItemCompletionMessage(itemId: Int): String = battleSetup.battleItemCompletionMessage(itemId)
    fun setJoinEquip(unitId: Int, weapon: Int, weaponLevel: Int, armor: Int, armorLevel: Int, auxiliary: Int) =
        battleSetup.setJoinEquip(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary, joinedEquipment)

    fun ending(id: Int) = battleSetup.ending(id) { endingId = it }
    fun infoTransfer(type: Int, payload: String, selectedUnitId: Int = 0) =
        battleSetup.infoTransfer(type, payload, selectedUnitId, infoTransfers)

    fun jumpScene(target: Int) = battleSetup.jumpScene(target, { sceneJumpTarget = it }, { sceneJumpStage = it })
    fun resetLocalVariables() = Unit
    // --- 표시 조정자 위임 함수 ---
    fun requestUnitHide(unitId: Int, hideType: Int) = presentationCoordinator.requestUnitHide(unitId, hideType)
    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? = presentationCoordinator.consumeUnitHideRequest()
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

    fun completeUnitHide(request: ScenarioUnitHideRequest) =
        presentationCoordinator.completeUnitHide(request, battleUnits, ::unit, ::setBattleUnitVisibility)

    fun requestUnitShow(request: ScenarioUnitShowRequest) =
        presentationCoordinator.requestUnitShow(request, ::battleUnitForCharacterId)

    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? = presentationCoordinator.consumeUnitShowRequest()
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

    fun setModelUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 3,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0
    ): CampaignUnitPostsChange? =
        presentationCoordinator.setModelUnitPosts(unitId, posts, flags, data, enabledFeatures, campaign, ::unit)

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? = presentationCoordinator.consumeUnitPostsRequest()
    fun requestMapPresentation(request: ScenarioMapPresentationRequest) =
        presentationCoordinator.requestMapPresentation(request)

    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? =
        presentationCoordinator.consumeMapPresentationRequest()

    fun requestCameraCenter(x: Int, y: Int) = presentationCoordinator.requestCameraCenter(x, y)
    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> =
        presentationCoordinator.consumeCameraCenterRequests()

    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) =
        presentationCoordinator.requestScriptPresentation(request)

    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        presentationCoordinator.consumeScriptPresentationRequest()

    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        presentationCoordinator.consumeScriptPresentationRequests()

    fun setBattleUnitVisibility(unitId: Int, visible: Boolean) = unitRegistry.setBattleUnitVisibility(unitId, visible)
    fun addUnitLevels(unitId: Int, delta: Int, registeredFeatures: Int = 0): CampaignUnitLevelChange? =
        presentationCoordinator.addUnitLevels(unitId, delta, registeredFeatures, campaign)

    fun consumeScriptedUnitLevelChanges(): List<CampaignUnitLevelChange> =
        presentationCoordinator.consumeScriptedUnitLevelChanges()

    fun consumeScriptedUnitPostsChanges(): List<CampaignUnitPostsChange> =
        presentationCoordinator.consumeScriptedUnitPostsChanges()

    fun attackAction(attackerId: Int, targetId: Int, flag: Int) =
        presentationCoordinator.attackAction(attackerId, targetId, flag)

    fun setScriptedUnitAction(unitId: Int, action: Int, direction: Int = -1, loop: Boolean = false) =
        presentationCoordinator.setScriptedUnitAction(unitId, action, direction, loop, ::unit, ::setUnitDirection)

    fun consumeScriptedAttacks(): List<ScriptedAttackAction> = presentationCoordinator.consumeScriptedAttacks()
    fun consumeScriptedUnitActions(): List<ScriptedUnitAction> = presentationCoordinator.consumeScriptedUnitActions()
    fun consumeScriptedUnitDirections(): List<Pair<Int, Int>> = presentationCoordinator.consumeScriptedUnitDirections()

    // --- 전투 조정자 위임 함수 ---
    fun initFight() = fightCoordinator.initFight()
    fun startFight(firstUnitId: Int, secondUnitId: Int, backgroundIndex: Int): Long =
        fightCoordinator.startFight(firstUnitId, secondUnitId, backgroundIndex)

    fun enqueueFightCommand(command: ScenarioFightCommand) = fightCoordinator.enqueueFightCommand(command)
    fun consumeFightCommands(): List<ScenarioFightCommand> = fightCoordinator.consumeFightCommands()
    fun setBackgroundSound(soundId: Int) = fightCoordinator.setBackgroundSound(soundId)
    fun effectSound(soundId: Int, mode: Int = 1) = fightCoordinator.effectSound(soundId, mode)
    fun consumeSoundEffects(): List<ScenarioSoundEffect> = fightCoordinator.consumeSoundEffects()

    // --- 이동 조정자 위임 함수 ---
    fun enableBattleMovementTimeline() {
        movementCoordinator.battleMovementTimeline = true
    }

    fun setBattleMovePathResolver(resolver: (Int, Int, Int) -> List<Pair<Int, Int>>?) {
        movementCoordinator.battleMovePathResolver = resolver
    }

    fun head(id: Int): ScenarioHead = movementCoordinator.head(id)
    fun moveHead(id: Int, x: Int, y: Int): Float = movementCoordinator.moveHead(id, x, y)
    fun showHead(id: Int, x: Int, y: Int): Float = movementCoordinator.showHead(id, x, y)
    fun hideHead(id: Int): Float = movementCoordinator.hideHead(id)
    fun countDirection(fromId: Int, toId: Int): Int = movementCoordinator.countDirection(fromId, toId, ::unit)
    fun moveDuration(id: Int, x: Int, y: Int): Float = movementCoordinator.moveDuration(id, x, y, units)
    fun moveUnits(requests: List<ScenarioCommand.MoveUnit>): Float =
        movementCoordinator.moveUnits(requests, units) { presentationCoordinator.scriptedUnitDirections += it }

    fun updateAnimations(delta: Float) = movementCoordinator.updateAnimations(delta, units)
    fun finishAnimations() = movementCoordinator.finishAnimations(units)

    // --- 유닛 등록부 위임 함수 ---
    fun createBattleUnits(faction: ScenarioUnitFaction, entries: List<Any?>) =
        unitRegistry.createBattleUnits(faction, entries, campaign)

    fun battleUnitForCharacterId(characterId: Int): ScenarioBattleUnit? =
        unitRegistry.battleUnitForCharacterId(characterId)

    fun battleUnitForSlot(battleSlot: Int): ScenarioBattleUnit? = unitRegistry.battleUnitForSlot(battleSlot)
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

    fun setUnitAi(unitId: Int, ai: Int, targetId: Int = -1, targetX: Int = 0, targetY: Int = 0) =
        unitRegistry.setUnitAi(unitId, ai, targetId, targetX, targetY)

    fun setUnitRetreatTextEnabled(unitId: Int, enabled: Boolean) =
        unitRegistry.setUnitRetreatTextEnabled(unitId, enabled)

    fun hideBattleRect(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int) =
        unitRegistry.hideBattleRect(x1, y1, x2, y2, camp)

    fun unit(id: Int): TacticalUnit = unitRegistry.unit(id)
    fun seedBattleUnitPosition(id: Int, x: Int, y: Int) = unitRegistry.seedBattleUnitPosition(id, x, y)
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

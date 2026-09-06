package com.jojo.game.presentation.scenario
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.presentation.battle.preparation.HallPreparationFlow
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioBattleScriptContext
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.application.hall.HallManagementCommandAdapter
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.application.runtime.RuntimeScenarioCommand
import com.jojo.game.application.runtime.RuntimeScenarioFrame
import com.jojo.game.application.runtime.RuntimeScenarioPresentation
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import com.jojo.game.application.runtime.RuntimeScenarioScene
import com.jojo.game.application.runtime.RuntimeScenarioCommand.ShowOverlay
import com.jojo.game.application.runtime.RuntimeScenarioCommand.Present

import com.jojo.game.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.campaign.CampaignEquippedItem
import com.jojo.game.domain.scenario.*
import com.jojo.game.presentation.scenario.hall.*
import com.jojo.game.presentation.scenario.hall.render.*
import com.jojo.game.presentation.scenario.ScenarioPlaybackController
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.input.*
import com.jojo.game.presentation.scenario.render.*
import com.jojo.game.presentation.scenario.story.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport


class ScenarioScreen(
    internal val game: JojoGame,
    internal val moduleName: String,
    private val scriptedRandomValues: List<Int>,
    private val scriptedInfoTransferRandomValues: List<Int>,
    private val scriptedGlobals: Map<Int, Int>,
    private val scriptedUnitAttributes: List<Triple<Int, Int, Int>>,
    private val scriptedVariables: Map<Int, Int>,
    private val scriptedAmbition: Int?,
    private val scriptedBattleRound: Int,
    private val scriptedBattleCamp: Int,
    private val scriptedBattleAttributes: Map<Int, Map<Int, Int>>,
    private val scriptedBattlePositions: Map<Int, Pair<Int, Int>>,
    private val scriptedBattlePositionsByCamp: Map<Int, List<Pair<Int, Int>>>,
    private val scriptedBattleEnemyDefeated: Boolean,
    private val scriptedStartScene: String,
    private val scriptedStartLabel: String?,
    private val stopAfterRandomTrace: Boolean,
    private val stopAfterRandomTraceCount: Int?,
    internal val campaign: CampaignState,
) : ScreenAdapter(), ScenarioInputPort, ScenarioHallInteractionPort {

    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    private val shapes = ShapeRenderer()
    private val batch = SpriteBatch()
    internal val playback = ScenarioInterpreter.load(moduleName, campaign).apply {
        // `campaign.enter()` prepares a fresh module state. Apply explicit
        // External globals are applied after campaign entry so a supplied
        // loses its recovered source guard inputs at scene entry.
        scriptedGlobals.forEach { (id, value) -> campaign.globalVariables[id] = value }
        scriptedUnitAttributes.forEach { (unitId, attribute, value) ->
            campaign.setUnitAttribute(
                unitId,
                attribute,
                value
            )
        }
        campaign.setInfoTransferRandomSequence(scriptedInfoTransferRandomValues)
        scriptedAmbition?.let { stage.addAmbition(it - stage.ambition) }
        setRandomSequence(scriptedRandomValues)
        if (stopAfterRandomTraceCount != null) {
            stopAfterRandomTrace(stopAfterRandomTraceCount)
        } else if (stopAfterRandomTrace) {
            stopAfterNextRandomTrace()
        }
        setScriptVariables(scriptedVariables)
        setBattleContext(
            ScenarioBattleScriptContext(
                round = scriptedBattleRound,
                camp = scriptedBattleCamp,
                attributes = scriptedBattleAttributes,
                positions = scriptedBattlePositions,
                positionsByCamp = scriptedBattlePositionsByCamp,
                enemyDefeated = scriptedBattleEnemyDefeated,
            ),
        )
        game.scenarioStarted(moduleName, scriptedStartScene.removePrefix("scene").toIntOrNull() ?: 0)
        start(scriptedStartScene, scriptedStartLabel)
    }
    internal val gameDataCatalog = GameDataCatalog.load()
    private val hallManagementCommands = HallManagementCommandAdapter(campaign, gameDataCatalog)
    private val sceneAssets = ScenarioSceneAssets {
        buildString {
            append("삼국지 조조전 LibGDX 게임 개발 직접 읽은 한국어 시나리오 인물 내레이션 선택 선택완료 Enter Space 클릭 다음 확정 처음으로 재능의 첫 징후 전투 병영 원본 궁정 대화 UI 비교 조조가 수저우 도겸과 전투를 벌였을 때 장비 장비 정보 매입 판매하기 상품 목록 창고 목록 무기점 상점 현금 종료 모두 해제 자동 장비 전부 무기 보구 보조 정보 조조 군웅 이전 무장 다음 무장 공격력 정신력 방어력 폭발력 사기 이동력 레벨 속성 검 이벤트 총합 가격 인벤토리 판매가 없음 부대 정보 일람 무장명 부대 속성 체력 공격 방어 정신 폭발 폐쇄 창고 일람 이름 경험치 소지자 아이템 확인 지형 정보 효과 기동력 소모 마왕 보병 기병 궁기 포차 무술 보물 도감 발견되지 않음 지금까지 발견한 역사 단축키 설명 메뉴 설정 단계 속도 변화 전용 목록 세트 목록 특수 효과 진영에 따라 다른 색상의 체력 바를 표시합니다 ★◎○△×—☆●")
            append(gameDataCatalog.allUnitNames().joinToString(separator = ""))
            append(gameDataCatalog.allEquipmentProfiles().joinToString(separator = "") { it.name })
            append(Gdx.files.internal("scenarios/$moduleName.py").readString("UTF-8"))
        }
    }
    private val titleFont get() = sceneAssets.titleFont
    private val sectionFont get() = sceneAssets.sectionFont
    private val bodyFont get() = sceneAssets.bodyFont
    private val smallUiFont get() = sceneAssets.smallUiFont
    private val streetDialogueFont get() = sceneAssets.streetDialogueFont
    private val streetSpeakerFont get() = sceneAssets.streetSpeakerFont
    private val hallMenuTextures get() = sceneAssets.hallMenuTextures
    private val overlayPixel get() = sceneAssets.overlayPixel
    private val choicePanelTexture get() = sceneAssets.choicePanelTexture
    private val choiceRowTexture get() = sceneAssets.choiceRowTexture
    private val dialoguePanelTexture get() = sceneAssets.dialoguePanelTexture
    private val streetSpeechBubbleTexture get() = sceneAssets.streetSpeechBubbleTexture
    internal var runtimePresentation = RuntimeScenarioPresentation.STANDARD
        private set
    internal var runtimePresentationDetail = -1
        private set
    private val infoPanelPatch get() = sceneAssets.infoPanelPatch
    private val audio = GameAudioPlayer()
    private val playbackController = ScenarioPlaybackController(playback, audio::sync, audio::dispose)
    private val scenarioNavigation = ScenarioNavigationCoordinator(
        game = game,
        moduleName = moduleName,
        campaign = campaign,
        playback = playback,
        initialSceneIndex = scriptedStartScene.removePrefix("scene").toIntOrNull() ?: 0,
    )
    private val playbackFrame = ScenarioPlaybackFrameUpdater(
        playback = playback,
        playbackController = playbackController,
        navigation = scenarioNavigation,
        isVerificationRun = ::isVerificationRun,
        isStreetPresentation = { runtimePresentation == RuntimeScenarioPresentation.STREET },
        autoCloseSettingEnabled = {
            settingsPreferences.getInteger(
                SettingLayer.GAME_SETTING,
                SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
            ) and SettingLayer.AUTO_CLOSE != 0
        },
        onAdvance = ::advance,
    )
    internal val scenarioViewState get() = playbackController.viewState
    private val glyphLayout = GlyphLayout()
    private val settingsPreferences by lazy { game.settingsPreferences() }
    private var runtimeOverlayInstalled = false
    private var pendingRuntimeOverlayScene = RuntimeScenarioScene()
    private val hallInteraction = HallInteractionController()
    private val hallInteractionView get() = hallInteraction.view
    internal val hallMenuOpen get() = hallInteractionView.menuOpen
    private val hallManagementFlow by lazy {
        HallManagementCoordinator(
            campaign,
            gameDataCatalog,
            hallInteraction,
            hallManagementCommands,
            HallManagementViewFactory(campaign, gameDataCatalog, moduleName, hallOverlayVariant),
        )
    }
    private val hallInformationFlow by lazy {
        HallInformationCoordinator(
            campaign,
            gameDataCatalog,
            hallManagementCommands,
            hallManagementFlow.views,
            hallManagementFlow::equipUnitIds,
        )
    }
    private val hallOverlayInteraction = HallOverlayInteractionController()
    internal val hallViews get() = hallManagementFlow.views
    internal var hallManagement: HallManagement?
        get() = hallManagementFlow.management
        set(value) { hallManagementFlow.management = value }
    private var hallManagementNotice: String?
        get() = hallManagementFlow.notice
        set(value) { hallManagementFlow.notice = value }
    private val hallSaveLayer by lazy {
        SaveLayer(object : SaveLayer.Repository {
            override fun load(index: Int): String? = game.savedCampaignSlot(index)
            override fun save(index: Int) {
                game.saveCampaign(index)
            }
        })
    }
    private var hallSaveOpen = false

    private var hallItemDetail: HallItemDetail?
        get() = hallInformationFlow.itemDetail
        set(value) { hallInformationFlow.itemDetail = value }
    private var hallItemLayer: ItemLayer?
        get() = hallInformationFlow.itemLayer
        set(value) { hallInformationFlow.itemLayer = value }
    private var hallEquipUnitIndex: Int
        get() = hallManagementFlow.equipUnitIndex
        set(value) { hallManagementFlow.equipUnitIndex = value }
    private var hallEquipUnequipConfirmation: Boolean
        get() = hallManagementFlow.unequipConfirmationOpen
        set(value) { hallManagementFlow.unequipConfirmationOpen = value }
    internal var hallUnitListLayer: HallUnitListLayer?
        get() = hallManagementFlow.unitListLayer
        set(value) { hallManagementFlow.unitListLayer = value }
    internal var hallEquipConfirmation: HallEquipConfirmation?
        get() = hallManagementFlow.equipConfirmation
        set(value) { hallManagementFlow.equipConfirmation = value }
    private var hallExclusiveLayer: ExclusiveLayer?
        get() = hallManagementFlow.exclusiveLayer
        set(value) { hallManagementFlow.exclusiveLayer = value }
    internal var hallMagicLayer: MagicInfoLayer?
        get() = hallInformationFlow.magicLayer
        set(value) { hallInformationFlow.magicLayer = value }
    private var hallUnitInfoLayer: UnitInfoLayer?
        get() = hallInformationFlow.unitInfoLayer
        set(value) { hallInformationFlow.unitInfoLayer = value }
    internal var hallFeatsLayer: FeatsLayer?
        get() = hallInformationFlow.featsLayer
        set(value) { hallInformationFlow.featsLayer = value }
    private var hallFeatsHelpOpen: Boolean
        get() = hallInformationFlow.featsHelpOpen
        set(value) { hallInformationFlow.featsHelpOpen = value }
    internal var hallInfo: HallInfo?
        get() = hallInformationFlow.info
        set(value) { hallInformationFlow.info = value }
    internal var hallPropertyTab: HallPropertyTab
        get() = hallInformationFlow.propertyTab
        set(value) { hallInformationFlow.propertyTab = value }
    private var hallTerrainTab: TerrainLayer.Tab
        get() = hallInformationFlow.terrainTab
        set(value) { hallInformationFlow.terrainTab = value }
    private val hallBuyTab get() = hallInteractionView.buyTabIndex
    private val hallSellTab get() = hallInteractionView.sellTabIndex
    private fun prepareHallManagementDefaultEquipment(kind: HallManagement) = hallManagementFlow.prepareDefaultEquipment(kind)
    private fun prepareHallForcesDefaultEquipment() = hallManagementFlow.prepareForcesDefaultEquipment()
    private fun hallEquipUnitIds(): List<Int> = hallManagementFlow.equipUnitIds()
    internal fun hallEquipUnitId(): Int = hallManagementFlow.equipUnitId()

    private var runtimeOverlayState: RuntimeScenarioOverlay? = null
    internal val runtimeOverlay: RuntimeScenarioOverlay? get() = runtimeOverlayState
    internal val hallOverlayVariant: RuntimeScenarioOverlay?
        get() = runtimeOverlay?.takeUnless { it == RuntimeScenarioOverlay.HALL }
    private val hallSkipDispatches = mutableListOf<String>()
    internal val hallSkipLayer: StorySkipFlow? = if (runtimeOverlay == RuntimeScenarioOverlay.SKIP_OPEN) {
        val hall = HallPreparationFlow(featureSkip = true).also { it.onCreate(0) }
        check("SkipLayer" in hall.layers)
        StorySkipFlow(object : StorySkipFlow.Sink {
            override fun msgBox(text: String, reply: (Int) -> Unit) { /* initial state does not open confirmation */
            }

            override fun dispatch(name: String) {
                hallSkipDispatches += name
            }
        }).also { it.onCreate() }
    } else null

    init {
        Gdx.input.inputProcessor = ScenarioGdxInputAdapter(ScenarioInputController(this)) { screenX, screenY ->
            viewport.unproject(com.badlogic.gdx.math.Vector3(screenX.toFloat(), screenY.toFloat(), 0f)).let { it.x to it.y }
        }
        Gdx.app.log("JojoGame", "Loaded $moduleName Python AST runtime")
    }
    override fun render(delta: Float) {
        playbackFrame.advanceClock(delta)
        applyRuntimeScenarioCommands()
        if (!runtimeOverlayInstalled && runtimeOverlay != null) {
            runtimeOverlayInstalled = true
            when (runtimeOverlay) {
                RuntimeScenarioOverlay.HALL -> playback.presentRuntimeScene(pendingRuntimeOverlayScene)
                else -> {
                    playback.presentRuntimeScene(pendingRuntimeOverlayScene)
                    if (runtimeOverlay == RuntimeScenarioOverlay.MENU) hallInteraction.openMenu()
                    if (runtimeOverlay == RuntimeScenarioOverlay.SAVE || runtimeOverlay == RuntimeScenarioOverlay.SAVE_CONFIRM) {
                        hallSaveLayer.onCreate(savedPage = 0)
                        hallSaveOpen = true
                        if (runtimeOverlay == RuntimeScenarioOverlay.SAVE_CONFIRM) hallSaveLayer.onRowTouch(0, SaveLayer.TOUCH_END)
                    }
                    when (runtimeOverlay) {
                        RuntimeScenarioOverlay.ITEM_EQUIPMENT -> openHallItem(0, "1", 0, canDrop = false)
                        RuntimeScenarioOverlay.ITEM_PROPERTY -> {
                            campaign.inventory.addItem(150, count = 2)
                            openHallItem(150, "1", 0, canDrop = false)
                        }

                        RuntimeScenarioOverlay.ITEM_DISCARD_CONFIRM -> {
                            campaign.inventory.addItem(4, level = 0)
                            openHallItem(4, "---", 0, canDrop = true)
                            hallItemLayer?.onButton(1, ItemLayer.TOUCH_END)
                        }

                        else -> Unit
                    }
                    hallManagement = when (runtimeOverlay) {
                        RuntimeScenarioOverlay.EQUIP, RuntimeScenarioOverlay.UNIT_LIST, RuntimeScenarioOverlay.UNIT_LIST_SELECT, RuntimeScenarioOverlay.UNIT_LIST_CLOSE -> HallManagement.EQUIP
                        RuntimeScenarioOverlay.BUY -> HallManagement.BUY
                        RuntimeScenarioOverlay.SELL -> HallManagement.SELL
                        else -> null
                    }
                    hallManagement?.let(::prepareHallManagementDefaultEquipment)
                    if (runtimeOverlay in setOf(RuntimeScenarioOverlay.UNIT_LIST, RuntimeScenarioOverlay.UNIT_LIST_SELECT, RuntimeScenarioOverlay.UNIT_LIST_CLOSE)) {
                        val layer = HallUnitListLayer(hallEquipUnitIds())
                        when (runtimeOverlay) {
                            RuntimeScenarioOverlay.UNIT_LIST_SELECT -> layer.onRow(1, HallUnitListLayer.TOUCH_END)?.let { selectedId ->
                                hallEquipUnitIndex = hallEquipUnitIds().indexOf(selectedId)
                                prepareHallManagementDefaultEquipment(HallManagement.EQUIP)
                            }

                            RuntimeScenarioOverlay.UNIT_LIST_CLOSE -> layer.onCancel(HallUnitListLayer.TOUCH_END)
                            else -> Unit
                        }
                        hallUnitListLayer = layer.takeIf { it.attached }
                    }
                    hallEquipConfirmation = when (runtimeOverlay) {
                        RuntimeScenarioOverlay.EQUIP_CONFIRM -> HallEquipConfirmation(listOf(10, -5, 0, 2, 0, 0, 1, 0), "장비")
                        RuntimeScenarioOverlay.EQUIP_CONFIRM_UNLOAD -> HallEquipConfirmation(List(8) { 0 }, "해제")
                        else -> null
                    }
                    hallExclusiveLayer = when (runtimeOverlay) {
                        RuntimeScenarioOverlay.EXCLUSIVE -> ExclusiveLayer()
                        RuntimeScenarioOverlay.EXCLUSIVE_TAB1 -> ExclusiveLayer(ExclusiveLayer.Tab.EXCLUSIVE_LIST)
                        else -> null
                    }
                    if (runtimeOverlay == RuntimeScenarioOverlay.MAGIC) {
                        val profile =
                            requireNotNull(gameDataCatalog.allMagicProfiles().firstOrNull { it.name == "회오리" })
                        val magic = MagicUiList.Magic(
                            profile.id, profile.name, profile.expendMp, profile.power,
                            profile.icon, profile.hitArea.id, profile.effectAreaId, profile.intro,
                        )
                        val unitInfo = UnitInfoLayer(
                            listOf(
                                UnitInfoLayer.Unit(
                                    id = 0, name = "조조", post = "", level = 3,
                                    hp = 1, maxHp = 1, mp = 1, maxMp = 1,
                                    attack = 1, defense = 1, spirit = 1, critical = 1, morale = 1,
                                    magic = listOf(magic.name),
                                )
                            )
                        )
                        unitInfo.onCreate()
                        hallMagicLayer = UnitInfoMagicRoute.open(unitInfo, listOf(magic))
                    }
                    if (runtimeOverlay == RuntimeScenarioOverlay.FEATS || runtimeOverlay == RuntimeScenarioOverlay.FEATS_HELP) {
                        campaign.globalVariables[4074] = 1
                        openHallUnitInfo(0)
                        openHallFeatsFromUnitInfo()
                        if (runtimeOverlay == RuntimeScenarioOverlay.FEATS_HELP) openHallFeatsHelp()
                        // Render-event projections isolate Global127 after
                        // exercising the actual Forces/UnitInfo route.
                        hallInfo = null
                        hallUnitInfoLayer = null
                    }
                    hallInfo = when (runtimeOverlay) {
                        RuntimeScenarioOverlay.FORCES -> HallInfo.FORCES
                        RuntimeScenarioOverlay.PROPERTY -> HallInfo.PROPERTY
                        RuntimeScenarioOverlay.TERRAIN -> HallInfo.TERRAIN
                        RuntimeScenarioOverlay.TREASURE -> HallInfo.TREASURE
                        RuntimeScenarioOverlay.HELPER -> HallInfo.HELPER
                        else -> null
                    }
                    if (hallInfo == HallInfo.FORCES) prepareHallForcesDefaultEquipment()
                }

            }
        }
        if (playbackFrame.updatePlayback(delta) == ScenarioRenderPhaseResult.ROUTED) return
        if (playbackFrame.elapsed > 0.15f && runtimeOverlay == RuntimeScenarioOverlay.CHOICE) {
            advanceSourceUntilChoice()
            playbackController.resetDialogueReveal()
        }
        playbackFrame.updatePresentation(delta)
        if (renderScenarioFrame() == ScenarioRenderPhaseResult.CAPTURED) return
    }

    /** Draws the post-update scene and reports capture completion to its caller. */
    private fun renderScenarioFrame(): ScenarioRenderPhaseResult {
        if (runtimePresentation == RuntimeScenarioPresentation.STREET) Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        else Gdx.gl.glClearColor(0.08f, 0.11f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        if (runtimePresentation == RuntimeScenarioPresentation.STREET) {
            val stageIndex = runtimePresentationDetail
            if (stageIndex >= 0) {
                if (stageIndex >= ScenarioStreetDialogueStages.backgroundIndex()) {
                    drawBattlefield(
                        drawCharacters = stageIndex >= ScenarioStreetDialogueStages.charactersIndex(),
                        drawUnits = false,
                    )
                }
                batch.projectionMatrix = viewport.camera.combined
                batch.begin()
                ScenarioStoryRenderer.drawStreetDialogue(sceneAssets, batch, streetDialogueView(), stageIndex)
                batch.end()
                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
            }
        } else {
            if (runtimePresentation == RuntimeScenarioPresentation.PALACE) {
                drawBattlefield(drawCharacters = true, drawUnits = true)
                playback.currentDialogue?.let { dialogue ->
                    batch.projectionMatrix = viewport.camera.combined
                    ScenarioStoryRenderer.drawPalaceFixture(
                        sceneAssets,
                        batch,
                        ScenarioPalaceFixtureView(dialogue.text, dialoguePortraitId(0), "조조"),
                    )
                }
            } else {
                val isolatedHallOverlay = ScenarioRenderPolicy.isStandaloneHallOverlay(hallOverlayVariant)
                drawBattlefield(drawCharacters = !isolatedHallOverlay, drawUnits = !isolatedHallOverlay)
                drawOverlay()
            }
        }

        return ScenarioRenderPhaseResult.CONTINUE
    }

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)

    override fun dispose() {
        playbackController.dispose()
        sceneAssets.dispose()
        batch.dispose()
        shapes.dispose()
    }

    override fun advance() {
        playbackController.advance(
            onConfirmChoice = ::confirmChoice,
            closeHallMenu = hallInteraction::closeMenu,
            beginHallBattleScene = scenarioNavigation::beginHallBattleScene,
            onRoute = scenarioNavigation::routeAfterScenario,
        )
    }

    /** Read-only presentation snapshot; mutations still enter through the installed InputProcessor. */
    internal fun runtimeProbe(): ScenarioRuntimeProbe {
        val battleButton = viewport.project(com.badlogic.gdx.math.Vector3(936.86f, 43f, 0f))
        return ScenarioRuntimeProbe(
            module = moduleName,
            elapsedSeconds = playbackFrame.elapsed,
            playback = playback.state,
            options = playback.currentChoice?.options.orEmpty(),
            selectedChoice = playback.selectedChoice,
            sceneIndex = scenarioNavigation.naturalSceneIndex,
            startedScenes = scenarioNavigation.startedScenes(),
            backgroundId = playback.stage.backgroundId,
            unitIds = playback.stage.units.keys.toSet(),
            campaignStage = game.campaignStage(),
            menuVisible = playback.stage.menuVisible,
            dialogueText = playback.currentDialogue?.text,
            hallBattleScenePending = scenarioNavigation.hallBattleScenePending,
            battleButtonScreenX = battleButton.x.toInt(),
            battleButtonScreenY = (Gdx.graphics.height - battleButton.y).toInt(),
            choiceTrace = playback.choiceTrace.toList(),
            randomTrace = playback.randomTrace.toList(),
            randomDrawCount = playback.randomDrawCount,
            remainingInjectedRandomCount = playback.remainingInjectedRandomCount,
        )
    }

    private fun confirmChoice() {
        playback.confirmChoice()
        playback.chosenOption?.let { game.recordChoice(moduleName, it) }
    }

    private fun isVerificationRun(): Boolean = game.externalScenarioDriverKeepsScreenOpen()

    private fun applyRuntimeScenarioCommands() {
        val frame = RuntimeScenarioFrame(
            module = moduleName,
            elapsedSeconds = playbackFrame.elapsed,
            playback = playback.state,
            choiceAvailable = playback.currentChoice != null,
        )
        game.runtimeScenarioDriver()?.commands(frame).orEmpty().forEach { command ->
            when (command) {
                is Present -> applyRuntimePresentation(command.presentation, command.detail, command.scene)
                is ShowOverlay -> {
                    runtimeOverlayState = command.overlay
                    pendingRuntimeOverlayScene = command.scene
                }
                is RuntimeScenarioCommand.SetPresentation -> applyRuntimePresentation(command.mode, command.detail, RuntimeScenarioScene())
                RuntimeScenarioCommand.AdvanceDialogue -> if (playback.state == PlaybackState.DIALOGUE) playback.advanceDialogue()
                RuntimeScenarioCommand.ResumeModal -> if (playback.state == PlaybackState.MODAL) playback.resumeModal()
                RuntimeScenarioCommand.SkipDelay -> if (playback.state == PlaybackState.DELAY) playback.skipDelay()
                RuntimeScenarioCommand.ConfirmChoice -> if (playback.state == PlaybackState.CHOICE) confirmChoice()
                RuntimeScenarioCommand.RevealDialogue -> playbackController.resetDialogueReveal()
            }
        }
    }

    private fun applyRuntimePresentation(
        mode: RuntimeScenarioPresentation,
        detail: Int,
        scene: RuntimeScenarioScene,
    ) {
        runtimePresentation = mode
        runtimePresentationDetail = detail
        if (scene != RuntimeScenarioScene()) playback.presentRuntimeScene(scene)
    }

    private fun advanceSourceUntilChoice() {
        var guard = 0
        while (playback.state != PlaybackState.CHOICE && playback.state != PlaybackState.COMPLETE) {
            check(++guard <= 10_000) { "$moduleName choice did not settle" }
            when (playback.state) {
                PlaybackState.DIALOGUE -> playback.advanceDialogue()
                PlaybackState.DELAY -> playback.skipDelay()
                PlaybackState.MODAL -> playback.resumeModal()
                PlaybackState.CHOICE, PlaybackState.COMPLETE -> Unit
            }
        }
    }

    private fun drawBattlefield(drawCharacters: Boolean = true, drawUnits: Boolean = drawCharacters) {
        ScenarioBattlefieldRenderer.draw(
            sceneAssets,
            batch,
            shapes,
            viewport.camera,
            battlefieldView(drawCharacters, drawUnits),
        )
    }

    private fun battlefieldView(drawCharacters: Boolean, drawUnits: Boolean): ScenarioBattlefieldRenderView {
        val speakerId = playback.currentDialogue?.speakerId?.toIntOrNull()
        val units = playback.stage.units.values.mapIndexed { index, unit ->
            val avatar = gameDataCatalog.unitProfile(unit.id)?.mapAvatar ?: unit.id
            val animationTime = if (unit.action == 20) unit.animationElapsed else playbackFrame.elapsed
            val frame = HallUnitRender.frame(avatar, unit.action, unit.direction, animationTime)
            ScenarioBattlefieldUnitView(
                id = unit.id,
                visualX = unit.visualX,
                visualY = unit.visualY,
                visible = unit.visible,
                zIndex = unit.moveZIndex,
                siblingOrder = index,
                textureAssetId = frame.textureAssetId,
                frameRow = frame.row,
                flipX = frame.flipX,
                showSpeechBubble = playback.state == PlaybackState.DIALOGUE && speakerId == unit.id,
            )
        }
        val headOrder = if (drawUnits) units.size else 0
        val heads = playback.stage.heads.values.mapIndexed { index, head ->
            ScenarioBattlefieldHeadView(
                portraitId = dialoguePortraitId(head.characterId),
                visualX = head.visualX,
                visualY = head.visualY,
                opacity = head.opacity,
                zIndex = -head.y.toFloat(),
                siblingOrder = headOrder + index,
            )
        }
        return ScenarioBattlefieldRenderView(playback.stage.backgroundId, drawCharacters, drawUnits, units, heads)
    }


    private fun drawOverlay() {
        scenarioOverlayView()?.let { view ->
            ScenarioOverlayRenderer.draw(sceneAssets, batch, shapes, viewport.camera.combined, view)
            if (view.modal?.kind == ScenarioOverlayModalKind.AMBITION) {
                batch.projectionMatrix = viewport.camera.combined
                batch.begin(); drawHallMenu(); batch.end()
            }
        } ?: drawHallCompletionOverlay()
    }

    private fun scenarioOverlayView(): ScenarioOverlayRenderView? {
        val state = when (playback.state) {
            PlaybackState.DIALOGUE -> ScenarioOverlayState.DIALOGUE
            PlaybackState.CHOICE -> ScenarioOverlayState.CHOICE
            PlaybackState.DELAY -> ScenarioOverlayState.DELAY
            PlaybackState.MODAL -> ScenarioOverlayState.MODAL
            PlaybackState.COMPLETE -> return null
        }
        val modal = playback.currentModalText?.let { text ->
            val kind = when (playback.currentModalKind) {
                ScenarioModalKind.EVENT -> ScenarioOverlayModalKind.EVENT
                ScenarioModalKind.INFO -> ScenarioOverlayModalKind.INFO
                ScenarioModalKind.MAP_INFO -> ScenarioOverlayModalKind.MAP_INFO
                ScenarioModalKind.SECTION -> ScenarioOverlayModalKind.SECTION
                ScenarioModalKind.AMBITION -> ScenarioOverlayModalKind.AMBITION
                else -> ScenarioOverlayModalKind.OTHER
            }
            ScenarioModalRenderView(kind, text, scenarioViewState.modalVisibleText, playback.currentModalFixedText, hallOverlayVariant)
        }
        val choice = playback.currentChoice?.let {
            ScenarioChoiceRenderView(playback.isAskChoice, it.faceId?.let(::dialoguePortraitId), it.options)
        }
        return ScenarioOverlayRenderView(state, streetDialogueView(), choice, modal)
    }

    private fun drawHallCompletionOverlay() {
        shapes.projectionMatrix = viewport.camera.combined
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        if (hallFeatsLayer != null || hallMagicLayer != null || hallExclusiveLayer != null || hallManagement != null || hallSaveOpen || hallItemLayer != null) {
            shapes.color = Color(0f, 0f, 0f, 100f / 255f); shapes.rect(0f, 0f, 1280f, 688f)
        } else if (hallMenuOpen) {
            shapes.color = Color(0f, 0f, 0f, 30f / 255f); shapes.rect(0f, 0f, 1280f, 688f)
        }
        shapes.end(); Gdx.gl.glDisable(GL20.GL_BLEND)
        batch.projectionMatrix = viewport.camera.combined; batch.begin()
        if (playback.stage.menuVisible) {
            if (hallFeatsLayer != null) drawFeatsLayer(requireNotNull(hallFeatsLayer))
            else if (hallMagicLayer != null) drawMagicLayer(requireNotNull(hallMagicLayer))
            else if (hallExclusiveLayer != null) { hallManagement?.let(::drawHallManagement); drawExclusiveLayer(requireNotNull(hallExclusiveLayer)) }
            else hallItemDetail?.let(::drawHallItem) ?: hallInfo?.let(::drawHallInfo) ?: hallManagement?.let(::drawHallManagement)
                ?: hallEquipConfirmation?.let { drawEquipConfirmation(it) } ?: if (hallSaveOpen) drawHallSave() else {
                    drawHallCommand()
                    if (hallMenuOpen) drawHallMenu(interactive = true)
                    Unit
                }
        } else drawCompletion()
        batch.end()
    }

    private fun streetDialogueView(): ScenarioStreetDialogueView {
        val dialogue = playback.currentDialogue
        val speakerId = dialogue?.speakerId?.toIntOrNull()
        return ScenarioStreetDialogueView(
            hasDialogue = dialogue != null,
            portraitId = speakerId?.let(::dialoguePortraitId),
            speaker = speakerId?.let(::unitName).orEmpty(),
            visibleText = scenarioViewState.dialogueVisibleText,
            isLeft = playback.currentDialogueSide == 0,
            isAtTop = playback.currentDialogueAtTop,
        )
    }

    private fun drawChoice() {
        val choice = playback.currentChoice ?: return
        choice.faceId?.let(::dialoguePortrait)?.let { texture ->
            batch.color = Color.WHITE
            batch.draw(texture, 231.08f, 240.21f, 165.12f, 206.4f)
        }
        choice.options.take(3).forEachIndexed { index, option ->
            // ChooseLayer has no keyboard focus/selected-row tint.  All
            // source labels keep the same dark color until clicked.
            bodyFont.color = Color(0.06f, 0.06f, 0.06f, 1f)
            bodyFont.draw(batch, option, 482.88f, 407f - index * 42.14f)
        }
    }

    /** Original Hall/scene/HallMenuLayer addAmbition presentation. */
    private fun drawHallMenu(interactive: Boolean = false) {
        HallMenuRenderer.draw(
            sceneAssets,
            batch,
            HallMenuRenderView(
                eventName = playback.stage.eventName,
                stageName = playback.stage.stageName,
                ambitionFrom = if (interactive) playback.stage.ambition else playback.ambitionFrom,
                ambitionTo = if (interactive) playback.stage.ambition else playback.ambitionTo,
                ambitionElapsedSeconds = playback.ambitionElapsedSeconds,
                indicatorEnabled = playback.ambitionIndicatorEnabled,
                interactive = interactive,
                variant = hallOverlayVariant,
            ),
        )
    }

    private fun drawHallCommand() {
        fun texture(name: String) = sceneAssets.hallTexture("maps/ui/hall-command/$name.png")
        HallCommandRenderer.draw(
            batch,
            HallCommandRenderView(texture("menu"), texture("battle"), texture("equip"), texture("buy"), texture("sell")),
        )
    }

    private fun drawHallSave() {
        val save = hallSaveLayer.view()
        HallSaveRenderer.draw(
            sceneAssets,
            batch,
            HallSaveRenderView(
                rows = save.rows.take(8).map { HallSaveRowRenderView(it.number, it.stage, it.name) },
                pendingPrompt = hallSaveLayer.pendingSlot()?.let { hallSaveLayer.pendingPrompt().orEmpty() },
                completionTipOpen = hallSaveLayer.completionTipOpen(),
            ),
        )
    }

    private fun drawExclusiveLayer(layer: ExclusiveLayer) {
        HallExclusiveRenderer.draw(sceneAssets, batch, HallExclusiveView.from(layer))
    }

    /** Global127 opened from UnitInfoLayer's GVar4074-gated button8. */
    private fun drawFeatsLayer(layer: FeatsLayer) {
        HallFeatsRenderer.draw(sceneAssets, batch, HallFeatsView.from(layer, hallFeatsHelpOpen))
    }

    private fun drawMagicLayer(layer: MagicInfoLayer) {
        HallMagicRenderer.draw(sceneAssets, batch, HallMagicView.from(layer.magic))
    }

    /** EquipLayer / BuyLayer / SellLayer shown by HallCommandLayer buttons 1..3. */
    private fun drawHallManagement(kind: HallManagement) {
        when (kind) {
            HallManagement.SELL -> HallManagementRenderer.draw(
                sceneAssets,
                batch,
                HallManagementRenderView.Sell(hallViews.sell(hallSellTab, hallManagementNotice)),
            )

            HallManagement.BUY -> HallBuyManagementRenderer.draw(
                sceneAssets,
                batch,
                HallBuyManagementRenderView(
                    catalog = hallViews.buyCatalog(hallBuyTab),
                    summary = hallViews.buyUnitSummary(campaign.joinedUnits.firstOrNull() ?: 0),
                    money = campaign.money.toString(),
                    notice = hallManagementNotice,
                ),
            )

            HallManagement.EQUIP -> {
                HallManagementRenderer.draw(
                    sceneAssets,
                    batch,
                    HallManagementRenderView.Equip(
                        hallViews.equip(
                            unitId = hallEquipUnitId(),
                            selectedTab = hallInteractionView.equipTabIndex,
                            notice = hallManagementNotice,
                        ),
                    ),
                    viewport,
                )
                if (hallEquipUnequipConfirmation) {
                    HallEquipOverlayRenderer.drawUnequipConfirmation(sceneAssets, batch)
                } else if (hallUnitListLayer != null) {
                    HallUnitRosterRenderer.draw(
                        sceneAssets,
                        batch,
                        hallViews.unitRoster(requireNotNull(hallUnitListLayer).rows),
                    )
                }
                hallEquipConfirmation?.let(::drawEquipConfirmation)
            }
        }
    }

    /** Source Hall/scene/EquipConfirmLayer, transformed from 1488.372x800 by .86. */
    private fun drawEquipConfirmation(confirmation: HallEquipConfirmation) {
        HallEquipConfirmationRenderer.draw(
            sceneAssets,
            batch,
            HallEquipConfirmationView.from(confirmation.values, confirmation.actionLabel),
        )
    }

    /** Source Global information layers opened by HallMenuLayer tags 4..9. */
    /** ItemLayer uses Model.cfgItemTypeName(Item.itemType()) for consumables. */
    private fun propertyEffectName(item: GameDataCatalog.EquipmentProfile): String = when (item.id) {
        150 -> "HP 회복"
        else -> gameDataCatalog.equipmentTypeName(item.itemType)
    }

    private fun drawHallItem(detail: HallItemDetail) {
        val item = gameDataCatalog.equipmentProfile(detail.itemId) ?: return
        val category = gameDataCatalog.equipmentCategory(item)
        val effect = if (category == 3) propertyEffectName(item) else {
            val value = item.value + ((detail.level.toIntOrNull() ?: 1) - 1) * item.upgradePerLevel
            "${if (category == 1) "방어력" else "공격력"} +$value\n없음"
        }
        HallItemRenderer.draw(
            sceneAssets, batch, HallItemView(
                itemName = item.name,
                category = category,
                level = detail.level,
                experience = detail.experience,
                experienceLimit = detail.experienceLimit,
                typeName = if (category == 3) "아이템" else gameDataCatalog.equipmentTypeName(item.itemType),
                price = gameDataCatalog.purchasePrice(item).let { if (it == 255) "---" else it.toString() },
                effect = effect,
                intro = item.intro,
                postNames = (0 until 36).map(gameDataCatalog::postsName),
                canDrop = hallItemLayer?.canDrop == true,
                discardConfirmationOpen = hallItemLayer?.discardConfirmationOpen == true,
                logoTexture = hallItemTexture("maps/ui/start-battle/logo9.png"),
                buttonTexture = hallItemTexture("maps/ui/start-battle/button.png"),
                box1Texture = hallItemTexture("maps/ui/start-battle/box1.png"),
                box2Texture = hallItemTexture("maps/ui/start-battle/box2.png"),
                titleTexture = hallItemTexture("maps/ui/start-battle/title.png"),
                itemIconTexture = hallItemTexture("maps/item-icons/${item.icon}.png"),
            )
        )
    }

    private fun hallItemTexture(path: String): Texture? = hallMenuTextures[path] ?: Gdx.files.internal(path)
        .takeIf { it.exists() }
        ?.let(::Texture)
        ?.also {
            it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            hallMenuTextures[path] = it
        }

    private fun drawHallInfo(kind: HallInfo) {
        when (kind) {
            HallInfo.FORCES -> HallInfoRenderer.draw(
                sceneAssets,
                batch,
                HallInfoRenderView.Forces(hallViews.forces()),
            )

            HallInfo.PROPERTY -> HallInfoRenderer.draw(
                sceneAssets,
                batch,
                HallInfoRenderView.Property(hallViews.property(hallPropertyTab.ordinal)),
            )

            HallInfo.TERRAIN -> HallInfoRenderer.draw(
                sceneAssets,
                batch,
                HallInfoRenderView.Terrain(
                    HallTerrainView.from(
                        hallTerrainTab,
                        gameDataCatalog.terrainLayer().select(hallTerrainTab).rows,
                    ),
                ),
            )

            HallInfo.TREASURE -> {
                val treasures = gameDataCatalog.treasureProfiles()
                val discovered = campaign.inventory.discoveredTreasures
                HallInfoRenderer.draw(
                    sceneAssets,
                    batch,
                    HallInfoRenderView.Treasure(
                        HallTreasureView(
                            entries = treasures.take(6).map { item ->
                                HallTreasureEntryView(item.name, item.icon, item.id in discovered)
                            },
                            discoveredCount = discovered.size,
                            totalCount = treasures.size,
                        ),
                    ),
                )
            }

            HallInfo.HELPER -> HallInfoRenderer.draw(
                sceneAssets,
                batch,
                HallInfoRenderView.Helper(HallHelperView.default),
            )
        }
    }

    private fun openHallItem(itemId: Int, level: String, experience: Int, canDrop: Boolean) =
        hallInformationFlow.openItem(itemId, level, experience, canDrop)
    private fun openHallUnitInfo(selectedUnitId: Int) = hallInformationFlow.openUnitInfo(selectedUnitId)
    private fun openHallFeatsFromUnitInfo() = hallInformationFlow.openFeatsFromUnitInfo()
    private fun openHallFeatsHelp() = hallInformationFlow.openFeatsHelp()

    override fun hallState() = ScenarioInputRouter.HallState(
        playback.state == PlaybackState.COMPLETE && playback.stage.menuVisible,
        hallFeatsLayer != null, hallUnitInfoLayer != null, hallMagicLayer != null, hallItemLayer != null,
        hallSaveOpen, hallInfo != null, hallExclusiveLayer != null,
        hallManagement?.let { ScenarioInputRouter.Management.valueOf(it.name) }, hallUnitListLayer != null,
    )
    override fun playbackState(): PlaybackState = playback.state
    override fun isAskChoice(): Boolean = playback.isAskChoice
    override fun choiceCount(): Int = playback.currentChoice?.options?.size ?: 0
    override fun selectPrevious() = playback.selectPrevious()
    override fun selectNext() = playback.selectNext()
    override fun selectAndConfirm(index: Int) { playback.selectChoice(index); confirmChoice() }

    override fun dismissHallOverlay(): Boolean {
        if (hallFeatsLayer == null && hallUnitInfoLayer == null && hallMagicLayer == null && hallExclusiveLayer == null && hallInfo == null && hallManagement == null) return false
        if (hallFeatsLayer != null) { hallFeatsLayer = null; hallFeatsHelpOpen = false }
        if (hallMagicLayer != null) hallMagicLayer = null
        else if (hallExclusiveLayer != null) hallExclusiveLayer = null
        else { hallInfo = null; hallManagement = null }
        return true
    }

    override fun routeHallTouch(route: ScenarioInputRouter.Touch.Hall, x: Float, y: Float) {
        when (route.layer) {
            ScenarioInputRouter.HallLayer.FEATS -> hallFeatsLayer?.let { hallInformationFlow.handleFeatsTap(x, y) }
            ScenarioInputRouter.HallLayer.UNIT_INFO -> hallUnitInfoLayer?.let { hallInformationFlow.handleUnitInfoTap(x, y) }
            ScenarioInputRouter.HallLayer.MAGIC -> hallMagicLayer?.let { hallInformationFlow.handleMagicTap(x, y) }
            ScenarioInputRouter.HallLayer.ITEM -> hallInformationFlow.handleItemTap(x, y)
            ScenarioInputRouter.HallLayer.INFO -> hallInfo?.let { hallInformationFlow.handleInfoTap(it, x, y) }
            ScenarioInputRouter.HallLayer.SAVE -> applySaveInput(ScenarioHallSaveInputRouter.route(x, y, hallSaveLayer.completionTipOpen(), hallSaveLayer.pendingSlot() != null, hallSaveLayer.view().rows.size))
            ScenarioInputRouter.HallLayer.EXCLUSIVE -> applyExclusiveInput(ScenarioExclusiveInputRouter.route(hallOverlayInteraction.exclusiveTap(x, y)))
            ScenarioInputRouter.HallLayer.MANAGEMENT -> hallManagement?.let { if (route.closesManagement) hallManagementFlow.close() else hallManagementFlow.handleTap(it, x, y) }
            ScenarioInputRouter.HallLayer.MAIN -> ScenarioHallInteractionExecutor.execute(hallInteraction.mainTap(x, y), this)
        }
    }

    private fun applySaveInput(command: ScenarioHallSaveInputRouter.Command) = when (command) {
        ScenarioHallSaveInputRouter.Command.CompletionTip -> hallSaveLayer.onCompletionTip(SaveLayer.TOUCH_END)
        is ScenarioHallSaveInputRouter.Command.Confirm -> hallSaveLayer.onConfirm(if (command.accepted) 1 else 0)
        ScenarioHallSaveInputRouter.Command.Cancel -> { hallSaveLayer.onCancel(SaveLayer.TOUCH_END); hallSaveOpen = false }
        is ScenarioHallSaveInputRouter.Command.SelectRow -> hallSaveLayer.view().rows.getOrNull(command.index)?.let { hallSaveLayer.onRowTouch(it.index, SaveLayer.TOUCH_END) }
        ScenarioHallSaveInputRouter.Command.None -> Unit
    }

    private fun applyExclusiveInput(command: ScenarioExclusiveInputRouter.Command) {
        val layer = hallExclusiveLayer ?: return
        when (command) {
            ScenarioExclusiveInputRouter.Command.SET_LIST -> layer.onButton(0, ExclusiveLayer.TOUCH_END)
            ScenarioExclusiveInputRouter.Command.EXCLUSIVE_LIST -> layer.onButton(1, ExclusiveLayer.TOUCH_END)
            ScenarioExclusiveInputRouter.Command.CLOSE -> layer.onCancel(ExclusiveLayer.TOUCH_END)
            ScenarioExclusiveInputRouter.Command.NONE -> Unit
        }
        if (!layer.attached) hallExclusiveLayer = null
    }

    override fun startBattle() { if (!scenarioNavigation.beginHallBattleScene()) scenarioNavigation.routeAfterScenario() }
    override fun openManagement(kindName: String) { hallManagementFlow.open(HallManagement.valueOf(kindName)) }
    override fun selectHallMenu(index: Int) {
        when (index) {
            0 -> game.showTitleScreen()
            1 -> { hallSaveLayer.onCreate(onComplete = { hallSaveOpen = false }, savedPage = 0); hallSaveOpen = true }
            2 -> game.showTitleLoadScreen()
            3 -> game.showTitleSettingScreen(moduleName)
            4 -> { prepareHallForcesDefaultEquipment(); hallInfo = HallInfo.FORCES }
            5 -> hallInfo = HallInfo.PROPERTY
            6 -> hallInfo = HallInfo.TERRAIN
            7 -> hallInfo = HallInfo.TREASURE
            8 -> hallInfo = HallInfo.HELPER
            else -> Unit
        }
    }

    private fun drawCompletion() {
        titleFont.color = Color(0.98f, 0.85f, 0.52f, 1f)
        titleFont.draw(batch, "선택 완료", 95f, 192f)
        bodyFont.color = Color.WHITE
        bodyFont.draw(batch, playback.chosenOption ?: "시나리오 구간 완료", 95f, 145f)
    }

    /** Immutable, serializer-free observation used by external artifact sinks. */
    fun runtimeSnapshot(): ScenarioRuntimeSnapshot = ScenarioRuntimeSnapshot(
        frame = ScenarioRuntimeSnapshotProjector.renderInput(this),
        composition = ScenarioRuntimeCompositionProjector.project(this),
    )

    /** HallLayer.turnPos: source's 100×100 isometric Hall coordinate transform. */
    private fun mapX(x: Int, y: Int): Float = (x - y + 42) * 16f
    private fun mapY(x: Int, y: Int): Float = 1073.28f - (x + y) * 6.88f
    private fun mapX(x: Float, y: Float): Float = (x - y + 42f) * 16f
    private fun mapY(x: Float, y: Float): Float = 1073.28f - (x + y) * 6.88f
    private fun unitName(id: Int): String =
        gameDataCatalog.unitProfile(id)?.name?.takeIf(String::isNotBlank) ?: "유닛 $id"
    private fun nextModule(): String = offsetModule(1)
    private fun previousModule(): String = offsetModule(-1)
    private fun offsetModule(delta: Int): String {
        val modules = ScenarioCatalog.moduleNames().filter { it.startsWith("R_") }
        val index = modules.indexOf(moduleName).takeIf { it >= 0 } ?: 0
        return modules[Math.floorMod(index + delta, modules.size)]
    }

    private fun matchingBattleModule(): String {
        val candidate = moduleName.replaceFirst("R_", "S_")
        return candidate.takeIf { it in ScenarioCatalog.sModuleNames() } ?: "S_00"
    }

    private fun portraitTexture(characterId: Int): Texture? = sceneAssets.portraitTexture(characterId)

    /** Model.unitAttrFace2, which DialogueLayer uses before loading Head/<id>. */
    private fun dialoguePortrait(unitId: Int): Texture? = portraitTexture(dialoguePortraitId(unitId))

    private fun dialoguePortraitId(unitId: Int): Int {
        val face = gameDataCatalog.unitProfile(unitId)?.face ?: return unitId
        return if (unitId == 0 && face <= 3) face + 1 else face + 8
    }

    private fun backgroundTexture(backgroundId: Int): Texture? = sceneAssets.backgroundTexture(backgroundId)
    private fun unitTexture(assetId: Int): Texture? = sceneAssets.unitTexture(assetId)
}

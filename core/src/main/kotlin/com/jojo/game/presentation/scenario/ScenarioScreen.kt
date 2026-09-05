package com.jojo.game.presentation.scenario
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioBattleScriptContext
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.application.hall.HallManagementCommandAdapter
import com.jojo.game.application.runtime.ScenarioRuntimeProbe

import com.jojo.game.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.campaign.CampaignEquippedItem
import com.jojo.game.domain.scenario.*
import com.jojo.game.presentation.scenario.hall.*
import com.jojo.game.presentation.scenario.hall.render.*
import com.jojo.game.presentation.scenario.ScenarioPlaybackController
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.evidence.*
import com.jojo.game.presentation.scenario.render.*
import com.jojo.game.presentation.scenario.story.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
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

/**
 * class  `ScenarioScreen`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ScenarioScreen(
    private val game: JojoGame,
    private val moduleName: String,
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
    private val campaign: CampaignState,
) : ScreenAdapter() {

    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    private val shapes = ShapeRenderer()
    private val batch = SpriteBatch()
    private val playback = ScenarioInterpreter.load(moduleName, campaign).apply {
        // `campaign.enter()` prepares a fresh module state. Apply explicit
        // verification globals afterwards so a CLI fixture never silently
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
    private val gameDataCatalog = GameDataCatalog.load()
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
    private val streetCaptureStage = game.requestedCaptureState()
        ?.removePrefix("street-")
        ?.takeIf { game.requestedCaptureState()?.startsWith("street-") == true }
    private val hallPalaceFixture = game.requestedCaptureState() == "hall-palace-fixture"
    private val hallSectionFixture = game.requestedCaptureState() == "hall-section-fixture"
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
        game = game,
        playback = playback,
        playbackController = playbackController,
        navigation = scenarioNavigation,
        isVerificationRun = ::isVerificationRun,
        streetCaptureStage = streetCaptureStage,
        autoCloseSettingEnabled = {
            settingsPreferences.getInteger(
                SettingLayer.GAME_SETTING,
                SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
            ) and SettingLayer.AUTO_CLOSE != 0
        },
        onAdvance = ::advance,
    )
    private val scenarioViewState get() = playbackController.viewState
    private val storyEvidenceRecorder = ScenarioStoryEvidenceRecorder()
    private val equipConfirmationEvidenceRecorder = ScenarioEquipConfirmationEvidenceRecorder()
    private val propertyEvidenceRecorder = ScenarioPropertyEvidenceRecorder()
    private val terrainEvidenceRecorder = ScenarioTerrainEvidenceRecorder()
    private val treasureEvidenceRecorder = ScenarioTreasureEvidenceRecorder()
    private val staticHallInfoEvidenceRecorder = ScenarioStaticHallInfoEvidenceRecorder()
    private val glyphLayout = GlyphLayout()
    private val settingsPreferences by lazy { game.settingsPreferences() }
    private var hallFixtureInstalled = false
    private val hallInteraction = HallInteractionController()
    private val hallInteractionView get() = hallInteraction.view
    private val hallMenuOpen get() = hallInteractionView.menuOpen
    private val hallManagementFlow by lazy {
        HallManagementCoordinator(
            campaign,
            gameDataCatalog,
            hallInteraction,
            hallManagementCommands,
            HallManagementViewFactory(campaign, gameDataCatalog, moduleName, hallOverlayFixture),
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
    private val hallViews get() = hallManagementFlow.views
    private var hallManagement: HallManagement?
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
    private var hallUnitListLayer: HallUnitListLayer?
        get() = hallManagementFlow.unitListLayer
        set(value) { hallManagementFlow.unitListLayer = value }
    private var hallEquipConfirmation: HallEquipConfirmation?
        get() = hallManagementFlow.equipConfirmation
        set(value) { hallManagementFlow.equipConfirmation = value }
    private var hallExclusiveLayer: ExclusiveLayer?
        get() = hallManagementFlow.exclusiveLayer
        set(value) { hallManagementFlow.exclusiveLayer = value }
    private var hallMagicLayer: MagicInfoLayer?
        get() = hallInformationFlow.magicLayer
        set(value) { hallInformationFlow.magicLayer = value }
    private var hallUnitInfoLayer: UnitInfoLayer?
        get() = hallInformationFlow.unitInfoLayer
        set(value) { hallInformationFlow.unitInfoLayer = value }
    private var hallFeatsLayer: FeatsLayer?
        get() = hallInformationFlow.featsLayer
        set(value) { hallInformationFlow.featsLayer = value }
    private var hallFeatsHelpOpen: Boolean
        get() = hallInformationFlow.featsHelpOpen
        set(value) { hallInformationFlow.featsHelpOpen = value }
    private var hallInfo: HallInfo?
        get() = hallInformationFlow.info
        set(value) { hallInformationFlow.info = value }
    private var hallPropertyTab: HallPropertyTab
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
    private fun hallEquipUnitId(): Int = hallManagementFlow.equipUnitId()

    private val hallOverlayFixture = game.requestedCaptureState()
        ?.removePrefix("hall-")
        ?.removeSuffix("-fixture")
        ?.takeIf {
            it in setOf(
                "info",
                "get-item-equipment",
                "get-item-property",
                "item-equipment",
                "item-property",
                "item-discard-confirm",
                "choice",
                "map-info",
                "ambition",
                "ask",
                "command",
                "menu",
                "save",
                "save-confirm",
                "equip",
                "unit-list",
                "unit-list-select",
                "unit-list-close",
                "equip-confirm",
                "equip-confirm-unload",
                "exclusive",
                "exclusive-tab1",
                "magic",
                "feats",
                "feats-help",
                "buy",
                "sell",
                "forces",
                "property",
                "terrain",
                "treasure",
                "helper",
                "skip-open"
            )
        }
    private val hallSkipDispatches = mutableListOf<String>()
    private val hallSkipLayer: StorySkipFlow? = if (hallOverlayFixture == "skip-open") {
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
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun keyDown(keycode: Int): Boolean {
                if (keycode == Input.Keys.ESCAPE && (hallFeatsLayer != null || hallUnitInfoLayer != null || hallMagicLayer != null || hallExclusiveLayer != null || hallInfo != null || hallManagement != null)) {
                    if (hallFeatsLayer != null) {
                        hallFeatsLayer = null; hallFeatsHelpOpen = false
                    }
                    if (hallMagicLayer != null) hallMagicLayer = null
                    else if (hallExclusiveLayer != null) hallExclusiveLayer = null
                    else {
                        hallInfo = null; hallManagement = null
                    }
                    return true
                }
                when (keycode) {
                    Input.Keys.UP -> playback.selectPrevious()
                    Input.Keys.DOWN -> playback.selectNext()
                    Input.Keys.ENTER, Input.Keys.SPACE -> advance()
                }
                return true
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val world = viewport.unproject(com.badlogic.gdx.math.Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
                if (playback.state == PlaybackState.COMPLETE && playback.stage.menuVisible) {
                    hallFeatsLayer?.let {
                        handleHallFeatsTap(it, world.x, world.y)
                        return true
                    }
                    hallUnitInfoLayer?.let {
                        handleHallUnitInfoTap(it, world.x, world.y)
                        return true
                    }
                    hallMagicLayer?.let {
                        handleMagicTap(it, world.x, world.y)
                        return true
                    }
                    hallItemLayer?.let {
                        handleHallItemTap(world.x, world.y)
                        return true
                    }
                    if (hallSaveOpen) {
                        handleHallSaveTap(world.x, world.y)
                        return true
                    }
                    if (hallInfo != null) {
                        handleHallInfoTap(requireNotNull(hallInfo), world.x, world.y)
                        return true
                    }
                    hallExclusiveLayer?.let {
                        handleExclusiveTap(it, world.x, world.y)
                        return true
                    }
                    hallManagement?.let { management ->
                        // UnitListLayer owns a full-canvas cancel target while
                        // visible, so underlying Equip buttons must not fire.
                        if (management == HallManagement.EQUIP && hallUnitListLayer != null) {
                            handleHallManagementTap(management, world.x, world.y)
                            return true
                        }
                        val closes = when (management) {
                            HallManagement.EQUIP -> world.x in 642f..730f && world.y in 35f..84f
                            HallManagement.BUY -> world.x in 529f..653f && world.y in 35f..86f
                            HallManagement.SELL -> world.x in 869f..1000f && world.y in 75f..130f
                        }
                        if (closes) {
                            hallManagementFlow.close()
                        } else {
                            handleHallManagementTap(management, world.x, world.y)
                        }
                        return true
                    }
                    executeHallInteraction(hallInteraction.mainTap(world.x, world.y))
                    return true
                }
                if (playback.state == PlaybackState.CHOICE && world.x in 463f..1059f) {
                    if (playback.isAskChoice) {
                        val askButton = when {
                            world.x in 482.84f..628.18f && world.y in 306.16f..349.16f -> 0
                            world.x in 646.27f..791.61f && world.y in 306.16f..349.16f -> 1
                            else -> -1
                        }
                        if (askButton >= 0) {
                            playback.selectChoice(askButton)
                            confirmChoice()
                        }
                        return true
                    }
                    // ChooseLayer's VerticalLayout uses 44-unit rows.  Its
                    // source Button callback both chooses the row and closes
                    // the layer, so a mouse click must not always commit row 0.
                    val row = ((401f - world.y) / 44f).toInt()
                    val visible = playback.currentChoice?.options?.take(3).orEmpty()
                    if (row in visible.indices) {
                        playback.selectChoice(row)
                        confirmChoice()
                    }
                } else advance()
                return true
            }

            override fun scrolled(amountX: Float, amountY: Float): Boolean {
                if (playback.state == PlaybackState.CHOICE) {
                    if (amountY > 0f) playback.selectNext() else if (amountY < 0f) playback.selectPrevious()
                    return true
                }
                return false
            }
        }
        Gdx.app.log("JojoGame", "Loaded $moduleName Python AST runtime")
    }

    override fun render(delta: Float) {
        playbackFrame.advanceClock(delta)
        if (!hallFixtureInstalled &&
            ScenarioRenderPolicy.shouldInstallHallFixture(
                game.requestedCaptureState(),
                streetCaptureStage,
                hallOverlayFixture,
            )
        ) {
            hallFixtureInstalled = true
            when (game.requestedCaptureState()) {
                "hall-palace-fixture" -> playback.installPalaceFixture()
                "hall-section-fixture" -> playback.installSectionFixture()
                "hall-info-fixture", "hall-get-item-equipment-fixture", "hall-get-item-property-fixture", "hall-item-equipment-fixture", "hall-item-property-fixture", "hall-item-discard-confirm-fixture", "hall-choice-fixture", "hall-map-info-fixture", "hall-ambition-fixture", "hall-ask-fixture", "hall-command-fixture", "hall-menu-fixture", "hall-save-fixture", "hall-save-confirm-fixture", "hall-equip-fixture", "hall-unit-list-fixture", "hall-unit-list-select-fixture", "hall-unit-list-close-fixture", "hall-equip-confirm-fixture", "hall-equip-confirm-unload-fixture", "hall-exclusive-fixture", "hall-exclusive-tab1-fixture", "hall-magic-fixture", "hall-feats-fixture", "hall-feats-help-fixture", "hall-buy-fixture", "hall-sell-fixture", "hall-forces-fixture", "hall-property-fixture", "hall-terrain-fixture", "hall-treasure-fixture", "hall-helper-fixture", "hall-skip-open-fixture" -> {
                    playback.installOverlayFixture(requireNotNull(hallOverlayFixture))
                    if (hallOverlayFixture == "menu") hallInteraction.openMenu()
                    if (hallOverlayFixture == "save" || hallOverlayFixture == "save-confirm") {
                        hallSaveLayer.onCreate(savedPage = 0)
                        hallSaveOpen = true
                        if (hallOverlayFixture == "save-confirm") hallSaveLayer.onRowTouch(0, SaveLayer.TOUCH_END)
                    }
                    when (hallOverlayFixture) {
                        "item-equipment" -> openHallItem(0, "1", 0, canDrop = false)
                        "item-property" -> {
                            campaign.inventory.addItem(150, count = 2)
                            openHallItem(150, "1", 0, canDrop = false)
                        }

                        "item-discard-confirm" -> {
                            campaign.inventory.addItem(4, level = 0)
                            openHallItem(4, "---", 0, canDrop = true)
                            hallItemLayer?.onButton(1, ItemLayer.TOUCH_END)
                        }

                        else -> Unit
                    }
                    hallManagement = when (hallOverlayFixture) {
                        "equip", "unit-list", "unit-list-select", "unit-list-close" -> HallManagement.EQUIP
                        "buy" -> HallManagement.BUY
                        "sell" -> HallManagement.SELL
                        else -> null
                    }
                    hallManagement?.let(::prepareHallManagementDefaultEquipment)
                    if (hallOverlayFixture in setOf("unit-list", "unit-list-select", "unit-list-close")) {
                        val layer = HallUnitListLayer(hallEquipUnitIds())
                        when (hallOverlayFixture) {
                            "unit-list-select" -> layer.onRow(1, HallUnitListLayer.TOUCH_END)?.let { selectedId ->
                                hallEquipUnitIndex = hallEquipUnitIds().indexOf(selectedId)
                                prepareHallManagementDefaultEquipment(HallManagement.EQUIP)
                            }

                            "unit-list-close" -> layer.onCancel(HallUnitListLayer.TOUCH_END)
                        }
                        hallUnitListLayer = layer.takeIf { it.attached }
                    }
                    hallEquipConfirmation = when (hallOverlayFixture) {
                        "equip-confirm" -> HallEquipConfirmation(listOf(10, -5, 0, 2, 0, 0, 1, 0), "장비")
                        "equip-confirm-unload" -> HallEquipConfirmation(List(8) { 0 }, "해제")
                        else -> null
                    }
                    hallExclusiveLayer = when (hallOverlayFixture) {
                        "exclusive" -> ExclusiveLayer()
                        "exclusive-tab1" -> ExclusiveLayer(ExclusiveLayer.Tab.EXCLUSIVE_LIST)
                        else -> null
                    }
                    if (hallOverlayFixture == "magic") {
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
                    if (hallOverlayFixture == "feats" || hallOverlayFixture == "feats-help") {
                        campaign.globalVariables[4074] = 1
                        openHallUnitInfo(0)
                        openHallFeatsFromUnitInfo()
                        if (hallOverlayFixture == "feats-help") openHallFeatsHelp()
                        // Render-event fixtures isolate Global127 after
                        // exercising the actual Forces/UnitInfo route.
                        hallInfo = null
                        hallUnitInfoLayer = null
                    }
                    hallInfo = when (hallOverlayFixture) {
                        "forces" -> HallInfo.FORCES
                        "property" -> HallInfo.PROPERTY
                        "terrain" -> HallInfo.TERRAIN
                        "treasure" -> HallInfo.TREASURE
                        "helper" -> HallInfo.HELPER
                        else -> null
                    }
                    if (hallInfo == HallInfo.FORCES) prepareHallForcesDefaultEquipment()
                }

                else -> playback.installHallFixture()
            }
        }
        if (playbackFrame.updatePlayback(delta) == ScenarioRenderPhaseResult.ROUTED) return
        if (playbackFrame.elapsed > 0.15f && game.requestedCaptureState() == "choice") {
            advanceSourceUntilChoice()
            playbackController.resetDialogueReveal()
        }
        playbackFrame.updatePresentation(delta)
        if (renderScenarioFrame() == ScenarioRenderPhaseResult.CAPTURED) return
    }

    /** Draws the post-update scene and reports capture completion to its caller. */
    private fun renderScenarioFrame(): ScenarioRenderPhaseResult {
        if (streetCaptureStage != null) Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        else Gdx.gl.glClearColor(0.08f, 0.11f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        if (streetCaptureStage != null) {
            val stageIndex = ScenarioStreetDialogueStages.indexOf(streetCaptureStage)
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
            if (playbackFrame.elapsed > 1f && game.writeRenderEventLogIfRequested()) return ScenarioRenderPhaseResult.CAPTURED
            if (playbackFrame.elapsed > 1f && game.captureFrameIfRequested()) return ScenarioRenderPhaseResult.CAPTURED
        } else {
            if (hallPalaceFixture) {
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
                val isolatedHallOverlay = ScenarioRenderPolicy.isIsolatedHallOverlay(hallOverlayFixture)
                drawBattlefield(drawCharacters = !isolatedHallOverlay, drawUnits = !isolatedHallOverlay)
                drawOverlay()
            }
            if (playbackFrame.elapsed > 1f && game.writeRenderEventLogIfRequested()) return ScenarioRenderPhaseResult.CAPTURED
            if (playbackFrame.elapsed > 1f && game.captureFrameIfRequested()) return ScenarioRenderPhaseResult.CAPTURED
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

    private fun advance() {
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

    private fun advanceSourceUntilChoice() {
        var guard = 0
        while (playback.state != PlaybackState.CHOICE && playback.state != PlaybackState.COMPLETE) {
            check(++guard <= 10_000) { "$moduleName choice fixture did not settle" }
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
            ScenarioModalRenderView(kind, text, scenarioViewState.modalVisibleText, playback.currentModalFixedText, hallOverlayFixture)
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
                fixture = hallOverlayFixture,
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

    private fun handleHallInfoTap(kind: HallInfo, x: Float, y: Float) = hallInformationFlow.handleInfoTap(kind, x, y)

    private fun openHallItem(itemId: Int, level: String, experience: Int, canDrop: Boolean) =
        hallInformationFlow.openItem(itemId, level, experience, canDrop)

    private fun handleHallItemTap(x: Float, y: Float) = hallInformationFlow.handleItemTap(x, y)

    private fun handleExclusiveTap(layer: ExclusiveLayer, x: Float, y: Float) {
        when (hallOverlayInteraction.exclusiveTap(x, y)) {
            HallLayerTapIntent.PRIMARY -> layer.onButton(0, ExclusiveLayer.TOUCH_END)
            HallLayerTapIntent.SECONDARY -> layer.onButton(1, ExclusiveLayer.TOUCH_END)
            HallLayerTapIntent.CLOSE -> layer.onCancel(ExclusiveLayer.TOUCH_END)
            HallLayerTapIntent.CANCEL -> layer.onCancel(ExclusiveLayer.TOUCH_END)
            HallLayerTapIntent.NONE -> Unit
        }
        if (!layer.attached) hallExclusiveLayer = null
    }

    private fun handleMagicTap(layer: MagicInfoLayer, x: Float, y: Float) = hallInformationFlow.handleMagicTap(x, y)
    private fun openHallUnitInfo(selectedUnitId: Int) = hallInformationFlow.openUnitInfo(selectedUnitId)
    private fun openHallFeatsFromUnitInfo() = hallInformationFlow.openFeatsFromUnitInfo()
    private fun openHallFeatsHelp() = hallInformationFlow.openFeatsHelp()
    private fun handleHallUnitInfoTap(layer: UnitInfoLayer, x: Float, y: Float) = hallInformationFlow.handleUnitInfoTap(x, y)
    private fun handleHallFeatsTap(layer: FeatsLayer, x: Float, y: Float) = hallInformationFlow.handleFeatsTap(x, y)
    private fun handleHallManagementTap(kind: HallManagement, x: Float, y: Float) = hallManagementFlow.handleTap(kind, x, y)

    private fun executeHallInteraction(intent: HallInteractionIntent) {
        when (intent) {
            HallInteractionIntent.None,
            HallInteractionIntent.MenuClosed,
            HallInteractionIntent.OpenMenu -> Unit

            HallInteractionIntent.StartBattle -> if (!scenarioNavigation.beginHallBattleScene()) scenarioNavigation.routeAfterScenario()
            is HallInteractionIntent.OpenManagement -> hallManagementFlow.open(HallManagement.valueOf(intent.kind.name))
            is HallInteractionIntent.MenuSelection -> when (intent.index) {
                0 -> game.showTitleScreen()
                1 -> {
                    hallSaveLayer.onCreate(onComplete = { hallSaveOpen = false }, savedPage = 0)
                    hallSaveOpen = true
                }

                2 -> game.showTitleLoadScreen()
                3 -> game.showTitleSettingScreen(moduleName)
                4 -> {
                    prepareHallForcesDefaultEquipment()
                    hallInfo = HallInfo.FORCES
                }
                5 -> hallInfo = HallInfo.PROPERTY
                6 -> hallInfo = HallInfo.TERRAIN
                7 -> hallInfo = HallInfo.TREASURE
                // The source hides EditLayer4 unless its paid feature flag is
                // enabled, so the ninth visible icon is HelperLayer (tag 9).
                8 -> hallInfo = HallInfo.HELPER
                else -> Unit
            }
        }
    }

    private fun handleHallSaveTap(x: Float, y: Float) {
        val sourceX = x / .86f
        val sourceY = y / .86f
        if (hallSaveLayer.completionTipOpen()) {
            if (sourceX in 654.186f..834.186f && sourceY in 271.285f..321.285f) {
                hallSaveLayer.onCompletionTip(SaveLayer.TOUCH_END)
            }
            return
        }
        if (hallSaveLayer.pendingSlot() != null) {
            when {
                sourceX in 554.186f..734.186f && sourceY in 271.285f..321.285f -> hallSaveLayer.onConfirm(1)
                sourceX in 754.186f..934.186f && sourceY in 271.285f..321.285f -> hallSaveLayer.onConfirm(0)
            }
            return
        }
        if (sourceX in 1045.855f..1193.455f && sourceY in 100.162f..156.162f) {
            hallSaveLayer.onCancel(SaveLayer.TOUCH_END)
            hallSaveOpen = false
            return
        }
        if (sourceX !in 289.186f..1197.186f) return
        val row = hallSaveLayer.view().rows.take(8).indexOfFirst { visibleRow ->
            val index = hallSaveLayer.view().rows.indexOf(visibleRow)
            val rowY = 547.534f - index * 52f
            sourceY in rowY..(rowY + 50f)
        }
        hallSaveLayer.view().rows.getOrNull(row)?.let { hallSaveLayer.onRowTouch(it.index, SaveLayer.TOUCH_END) }
    }

    private fun drawCompletion() {
        titleFont.color = Color(0.98f, 0.85f, 0.52f, 1f)
        titleFont.draw(batch, "선택 완료", 95f, 192f)
        bodyFont.color = Color.WHITE
        bodyFont.draw(batch, playback.chosenOption ?: "시나리오 구간 완료", 95f, 145f)
    }

    /** Records a completed immutable frame snapshot through small evidence recorders. */
    fun renderEventLog(): String = ScenarioFrameEvidenceRecorder(
        storyEvidenceRecorder,
        staticHallInfoEvidenceRecorder,
        propertyEvidenceRecorder,
        terrainEvidenceRecorder,
        treasureEvidenceRecorder,
    ).record(renderEvidenceSnapshot())

    private fun renderEvidenceSnapshot(): ScenarioFrameEvidenceInput {
        val dialogue = playback.currentDialogue
        if (hallOverlayFixture == "skip-open") {
            check(requireNotNull(hallSkipLayer).button && !hallSkipLayer.panel && hallSkipLayer.zIndex == 999)
        }
        val overlayFixtures = setOf(
            "info", "get-item-equipment", "get-item-property", "item-equipment", "item-property",
            "item-discard-confirm", "map-info", "choice", "ambition", "ask", "command", "menu",
            "save", "save-confirm", "exclusive", "exclusive-tab1", "magic", "feats", "feats-help",
        )
        val unitList = hallUnitListLayer?.rows?.take(6)?.map { id ->
            val unit = gameDataCatalog.unitProfile(id)
            ScenarioHallUnitListEvidenceRow(
                campaign.unitNames[id] ?: if (id == 181) "병사 " else unit?.name ?: "무장",
                gameDataCatalog.postsName(campaign.unitAttribute(id, 17, unit?.posts ?: 0)),
            )
        }
        return ScenarioFrameEvidenceInput(
            fixture = game.requestedCaptureState()?.removeSuffix("-fixture"),
            palace = hallPalaceFixture,
            section = hallSectionFixture,
            street = streetCaptureStage?.let { stage -> ScenarioStoryEvidenceView.StreetDialogue(
                stage, dialogue != null, scenarioViewState.dialogueVisibleText,
                dialogue?.speakerId?.toIntOrNull()?.let(::unitName).orEmpty(),
            ) },
            overlay = hallOverlayFixture?.takeIf(overlayFixtures::contains)?.let(::hallOverlayEvidenceInput),
            hallInfo = hallInfo?.let { ScenarioFrameHallInfo.valueOf(it.name) },
            background = ScenarioFrameBackgroundEvidence(
                playback.stage.backgroundId,
                hallManagement == HallManagement.EQUIP || hallEquipConfirmation != null,
            ),
            units = playback.stage.units.values.filter { it.visible }.map { unit ->
                ScenarioFrameUnitEvidence(
                    unit.id, unit.visualX, unit.visualY, unit.direction,
                    gameDataCatalog.unitProfile(unit.id)?.mapAvatar ?: unit.id,
                )
            },
            management = hallManagement?.takeIf { it != HallManagement.EQUIP }?.let(::hallManagementEvidenceInput),
            equip = hallManagement?.takeIf { it == HallManagement.EQUIP }?.let { hallEquipEvidenceInput() },
            unitList = unitList,
            confirmation = hallEquipConfirmation?.let { confirmation ->
                ScenarioEquipConfirmationEvidenceView(hallOverlayFixture, confirmation.values, confirmation.actionLabel)
            },
            commandVisible = hallInfo == null && hallManagement == null && hallEquipConfirmation == null &&
                playback.state == PlaybackState.COMPLETE && playback.stage.menuVisible,
        )
    }

    private fun hallOverlayEvidenceInput(fixture: String): ScenarioHallOverlayEvidenceInput =
        ScenarioHallOverlayEvidenceInput(
            fixture = fixture,
            featsRows = hallFeatsLayer?.view()?.rows.orEmpty().map {
                ScenarioHallFeatEvidenceRow(it.title, it.ability, it.phaseLabel, it.progressRatio, it.progressLabel)
            },
            featsHelpText = FeatsLayer.HELP_TEXT,
            magic = hallMagicLayer?.magic?.let {
                ScenarioHallMagicEvidence(it.name, it.power ?: 0, it.cost, it.intro, it.icon, it.hit, it.eff)
            },
            modalText = sanitizeInfoText(playback.currentModalText.orEmpty()),
            items = listOf(0, 4, 150).mapNotNull { id -> gameDataCatalog.equipmentProfile(id)?.let { item ->
                id to ScenarioHallOverlayItemEvidence(item.name, item.icon, gameDataCatalog.equipmentTypeName(item.itemType), gameDataCatalog.purchasePrice(item), item.intro)
            } }.toMap(),
            postsNames = (0..80).map(gameDataCatalog::postsName),
        )

    private fun hallEquipEvidenceInput(): ScenarioHallEquipEvidenceInput {
        val unitId = hallEquipUnitId()
        val unit = gameDataCatalog.unitProfile(unitId) ?: gameDataCatalog.unitProfile(0)
        campaign.inventory.ensureDefaultEquipment(unitId, gameDataCatalog)
        val level = campaign.unitAttribute(unitId, 18, unit?.level ?: 1)
        val posts = campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)
        val profile = unit?.let { gameDataCatalog.battleProfile(it.id, (level - 1).coerceAtLeast(0), posts) }
        val bonus = campaign.inventory.equipment[unitId]?.let { gameDataCatalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1) } ?: GameDataCatalog.EquipmentBonus()
        val equipped = campaign.inventory.equippedItems().filter { it.unitId == unitId }
        fun slot(type: (Int) -> Boolean): ScenarioHallEquipEvidenceSlot {
            val item = equipped.firstOrNull { equipment -> gameDataCatalog.equipmentProfile(equipment.itemId)?.itemType?.let(type) == true }
            val itemProfile = item?.let { gameDataCatalog.equipmentProfile(it.itemId) }
            return ScenarioHallEquipEvidenceSlot(itemProfile?.name ?: "없음", item?.level ?: 1, item?.experience ?: 0, itemProfile?.icon)
        }
        val face = when (unitId) { 0 -> if ((unit?.face ?: 0) <= 3) (unit?.face ?: 0) + 1 else unit?.face ?: unitId; 157 -> 214; else -> unit?.face ?: unitId }
        return ScenarioHallEquipEvidenceInput(
            hallOverlayFixture, campaign.unitNames[unitId] ?: unit?.name ?: "조조",
            if (unitId == 0) "군웅" else gameDataCatalog.armProfile(profile?.arm?.id ?: posts)?.name ?: "군웅",
            face, profile?.level ?: 1,
            listOf(profile?.maxHitPoints ?: 0, profile?.maxMagicPoints ?: 0, (profile?.attack ?: 0) + bonus.attack, (profile?.spirit ?: 0) + bonus.spirit, (profile?.defense ?: 0) + bonus.defense, profile?.critical ?: 0, profile?.morale ?: 0, profile?.movement ?: 0),
            listOf(slot { it < 20 }, slot { it in 20..25 }, ScenarioHallEquipEvidenceSlot("없음", 1, 0, null)),
        )
    }

    private fun hallManagementEvidenceInput(kind: HallManagement): ScenarioHallManagementEvidenceInput {
        val unitId = hallEquipUnitId(); val unit = gameDataCatalog.unitProfile(unitId)
        val level = campaign.unitAttribute(unitId, 18, unit?.level ?: 1)
        val profile = unit?.let { gameDataCatalog.battleProfile(unitId, (level - 1).coerceAtLeast(0), campaign.unitAttribute(unitId, 17, it.posts)) }
        campaign.inventory.ensureDefaultEquipment(unitId, gameDataCatalog)
        val bonus = campaign.inventory.equipment[unitId]?.let { gameDataCatalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1) } ?: GameDataCatalog.EquipmentBonus()
        val weapon = campaign.inventory.equippedItems().firstOrNull { it.unitId == unitId }?.let { equipped -> gameDataCatalog.equipmentProfile(equipped.itemId)?.let { ScenarioHallManagementEquipment(it.name, equipped.level) } }
        return ScenarioHallManagementEvidenceInput(
            ScenarioHallManagementEvidenceKind.valueOf(kind.name), campaign.money,
            hallViews.buyCandidates().take(3).map { item -> ScenarioHallManagementBuyRow(item.name, gameDataCatalog.equipmentTypeName(item.itemType), campaign.inventory.items[item.id] ?: 0, gameDataCatalog.purchasePrice(item)) },
            ScenarioHallManagementUnitEvidence(unit?.name ?: "조조", gameDataCatalog.postsName(campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)).ifEmpty { "군웅" }, level, listOf(profile?.maxHitPoints ?: 0, profile?.maxMagicPoints ?: 0, (profile?.attack ?: 0) + bonus.attack, profile?.spirit ?: 0, (profile?.defense ?: 0) + bonus.defense, profile?.critical ?: 0, profile?.morale ?: 0, profile?.movement ?: 0), weapon),
        )
    }

    fun compositionTrace(): String = ScenarioCompositionEvidenceRecorder().record(
        ScenarioEvidenceView(
            moduleName, playback.state.toString(), playback.stage.backgroundId,
            playback.stage.units.values.filter { it.visible }.map { unit -> ScenarioEvidenceUnit(unit.id, unit.visualX, unit.visualY, unit.direction, unit.action, gameDataCatalog.unitProfile(unit.id)?.mapAvatar ?: unit.id) },
            playback.stage.heads.values.filter { it.opacity > 0f }.map { ScenarioEvidenceHead(it.characterId, it.visualX, it.visualY, it.opacity) },
            playback.currentDialogue?.let { ScenarioEvidenceDialogue(playback.currentDialogueSide, playback.currentDialogueAtTop, it.speakerId?.toIntOrNull(), scenarioViewState.dialogueVisibleText) },
            if (playback.state == PlaybackState.MODAL && playback.currentModalKind != null) ScenarioEvidenceModal(playback.currentModalKind.toString(), playback.currentModalText.orEmpty()) else null,
            hallEvidenceMenu(), playback.state == PlaybackState.COMPLETE && playback.stage.menuVisible,
            hallManagement?.let { ScenarioEvidenceHallManagement.valueOf(it.name) }, hallEvidenceInfo(),
        ),
    )

    private fun hallEvidenceMenu(): ScenarioEvidenceHallMenu? {
        val ambition = playback.state == PlaybackState.MODAL && playback.currentModalKind == ScenarioModalKind.AMBITION
        if (!hallMenuOpen && !ambition) return null
        val tween = ((playback.ambitionElapsedSeconds - 1.2f) / 1f).coerceIn(0f, 1f)
        return ScenarioEvidenceHallMenu(playback.ambitionFrom, playback.ambitionTo, if (hallMenuOpen) playback.stage.ambition.toFloat() else playback.ambitionFrom + (playback.ambitionTo - playback.ambitionFrom) * tween)
    }

    private fun hallEvidenceInfo(): ScenarioEvidenceHallInfo? = hallInfo?.let { kind ->
        val rows = when (kind) {
            HallInfo.FORCES -> campaign.joinedUnits.take(7).indices.map { ScenarioEvidenceRect(147.49f, 469.63f - it * 49f, 985.02f, 49f) }
            HallInfo.PROPERTY -> propertyEvidenceRows()
            HallInfo.TERRAIN -> (0 until 6).map { ScenarioEvidenceRect(249f, 453.56f - it * 64.5f, 854.07f, 64.5f) }
            HallInfo.TREASURE -> (0 until 6).map { ScenarioEvidenceRect(232.10f + it % 2 * 410.22f, 413.23f - it / 2 * 165.98f, 405.06f, 163.40f) }
            HallInfo.HELPER -> listOf(ScenarioEvidenceRect(139f, 103.07f, 1001.98f, 494.86f))
        }; ScenarioEvidenceHallInfo(kind.name.lowercase(), rows)
    }

    private fun propertyEvidenceRows(): List<ScenarioEvidenceRect> {
        fun accepts(id: Int): Boolean = gameDataCatalog.equipmentProfile(id)?.itemType?.let { type -> when (hallPropertyTab) { HallPropertyTab.WEAPON -> type < 20; HallPropertyTab.ARMOR -> type in 20..25; HallPropertyTab.AUXILIARY -> type > 45 && id < 150; HallPropertyTab.PROPERTY -> id >= 150 || type in 26..45 } } ?: false
        val equipped = if (hallPropertyTab == HallPropertyTab.PROPERTY) 0 else campaign.inventory.equippedItems().count { accepts(it.itemId) }
        return (0 until (equipped + campaign.inventory.items.count { accepts(it.key) }).coerceAtMost(7)).map { ScenarioEvidenceRect(217.42f, 481.58f - it * 67.08f, 846.56f, 65.36f) }
    }

    /** HallLayer.turnPos: source's 100×100 isometric Hall coordinate transform. */
    private fun mapX(x: Int, y: Int): Float = (x - y + 42) * 16f
    private fun mapY(x: Int, y: Int): Float = 1073.28f - (x + y) * 6.88f
    private fun mapX(x: Float, y: Float): Float = (x - y + 42f) * 16f
    private fun mapY(x: Float, y: Float): Float = 1073.28f - (x + y) * 6.88f
    private fun unitName(id: Int): String =
        gameDataCatalog.unitProfile(id)?.name?.takeIf(String::isNotBlank) ?: "유닛 $id"

    private fun sanitizeInfoText(text: String): String = text
        .replace(Regex("\\[C[0-9A-Fa-f]+"), "")
        .replace('☆', '★')

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

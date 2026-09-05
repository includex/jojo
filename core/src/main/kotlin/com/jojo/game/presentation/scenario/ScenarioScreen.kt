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
    private val verifyMode: Boolean,
    private val branchVerifyMode: Boolean,
    private val alternateBranchVerifyMode: Boolean,
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
                    Input.Keys.B -> if (verifyMode) game.showBattleSandbox(matchingBattleModule(), moduleName)
                    Input.Keys.H -> if (verifyMode) game.showCampaignHall(moduleName)
                    Input.Keys.LEFT_BRACKET -> if (verifyMode) game.showScenario(previousModule())
                    Input.Keys.RIGHT_BRACKET -> if (verifyMode) game.showScenario(nextModule())
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
        runVerificationChecks()
    }

    private fun runVerificationChecks() {
        if (playbackFrame.elapsed <= .8f) return
        if (verifyMode) {
            val scenarioCount = ScenarioCatalog.verifyEmbeddedSources()
            check(scenarioCount == 119) { "Expected 119 restored scenarios, got $scenarioCount" }
            check(playback.stage.backgroundId != 0) { "$moduleName background command was not executed" }
            check(playback.stage.units.isNotEmpty()) { "$moduleName unit commands were not executed" }
            check(playback.currentDialogue?.text?.isNotBlank() == true) { "$moduleName Korean dialogue extraction failed" }
            if (moduleName == "R_00") {
                check(playback.stage.units.keys.containsAll(listOf(0, 157, 181, 182))) { "R_00 grouped unit commands were not executed" }
                check(playback.currentDialogue?.text?.contains("대장님") == true) { "R_00 dialogue did not match" }
                check(Gdx.files.internal("maps/heads/181.png").exists()) { "R_00 speaker portrait was not bundled" }
                check(Gdx.files.internal("maps/2.jpg").exists()) { "R_00 loadBg source image was not bundled" }
            }
            Gdx.app.log("JojoGame", "VERIFY_OK: $scenarioCount scenario sources + ASTs embedded; $moduleName AST runtime loaded")
            Gdx.app.exit()
        }
        if (branchVerifyMode) {
            advanceSourceUntilChoice()
            check(playback.state == PlaybackState.CHOICE) { "R_00 first choice was not reached" }
            playback.confirmChoice()
            completeSourceDelays()
            check(playback.currentDialogue?.text?.contains("내가 남자로 태어났을 때부터") == true) {
                "R_00 sel == 1 branch was not executed"
            }
            Gdx.app.log(
                "JojoGame",
                "VERIFY_BRANCH_OK: R_00 first choice selected and sel == 1 dialogue branch executed"
            )
            Gdx.app.exit()
        }
        if (alternateBranchVerifyMode) {
            advanceSourceUntilChoice()
            check(playback.state == PlaybackState.CHOICE) { "R_00 first choice was not reached" }
            playback.selectNext()
            playback.confirmChoice()
            completeSourceDelays()
            check(playback.currentDialogue?.text?.contains("간웅이라고? 지금 단정 짓기엔 너무 이르지 않나") == true) {
                "R_00 sel == 2 branch was not executed"
            }
            Gdx.app.log(
                "JojoGame",
                "VERIFY_BRANCH_2_OK: R_00 second choice selected and sel == 2 dialogue branch executed"
            )
            Gdx.app.exit()
        }
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

    private fun isVerificationRun(): Boolean = verifyMode || branchVerifyMode || alternateBranchVerifyMode ||
        game.externalScenarioDriverKeepsScreenOpen()

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

    private fun completeSourceDelays() {
        var guard = 0
        while (playback.state == PlaybackState.DELAY) {
            check(++guard <= 10_000) { "$moduleName branch delay did not settle" }
            playback.skipDelay()
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

    /** Ordered draw-call metadata for deterministic source/game comparison. */
    fun renderEventLog(): String {
        if (game.requestedCaptureState()?.removeSuffix("-fixture") == "street-walk-direction") {
            return HallUnitRender.walkingRenderEventLog()
        }
        if (game.requestedCaptureState()?.removeSuffix("-fixture") == "street-walk-motion") {
            return HallUnitRender.walkingMotionRenderEventLog()
        }
        val log = RenderEventLog()
        if (hallPalaceFixture) {
            return storyEvidenceRecorder.record(ScenarioStoryEvidenceView.Palace)
        }
        if (hallSectionFixture) {
            return storyEvidenceRecorder.record(ScenarioStoryEvidenceView.Section)
        }
        streetCaptureStage?.let {
            val dialogue = playback.currentDialogue
            return storyEvidenceRecorder.record(
                ScenarioStoryEvidenceView.StreetDialogue(
                    stage = it,
                    dialogueVisible = dialogue != null,
                    visibleText = scenarioViewState.dialogueVisibleText,
                    speakerName = dialogue?.speakerId?.toIntOrNull()?.let(::unitName).orEmpty(),
                ),
            )
        }
        if (hallOverlayFixture == "skip-open") {
            check(requireNotNull(hallSkipLayer).button && !hallSkipLayer.panel && hallSkipLayer.zIndex == 999)
            // Keep the trace in the source layer's 1488.372 x 800 logical
            // coordinate space. The runtime viewport applies its normal
            // 1280 x 688 fit separately; logging before that transform avoids
            // introducing decimal-rounding drift into strict comparisons.
            log.draw(
                "hall-skip-open", "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
                "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
            )
            log.draw(
                "hall-skip-open", "HallLayer", "Canvas/Layer/button/Background", "sprite",
                1386.356f, 361f, 92f, 78f, "skip"
            )
            return log.jsonl()
        }
        hallOverlayFixture?.takeIf {
            it in setOf(
                "info",
                "get-item-equipment",
                "get-item-property",
                "item-equipment",
                "item-property",
                "item-discard-confirm",
                "map-info",
                "choice",
                "ambition",
                "ask",
                "command",
                "menu",
                "save",
                "save-confirm",
                "exclusive",
                "exclusive-tab1",
                "magic",
                "feats",
                "feats-help",
                "skip-open"
            )
        }?.let {
            appendHallOverlayFixtureRenderEvents(log, it)
            return log.jsonl()
        }
        if (hallInfo != null) {
            // Hall information fixtures share the same stable source map and
            // full-screen modal blocker. The actors underneath keep animating
            // in Cocos but are not part of the information-layer draw contract.
            log.draw(
                "hall-info", "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1280f, 688f,
                "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
            )
            log.draw(
                "hall-info", "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f,
                "default_sprite_splash", opacity = .392f
            )
            appendHallInfoRenderEvents(log, requireNotNull(hallInfo))
        } else {
            val backgroundId = playback.stage.backgroundId
            val hallEquipFixture = hallManagement == HallManagement.EQUIP || hallEquipConfirmation != null
            log.draw(
                "background", "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1280f, 688f,
                if (hallEquipFixture && backgroundId == 71) {
                    "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
                } else "maps/$backgroundId.jpg",
                blend = if (hallEquipFixture) listOf(770, 771) else "DISABLED"
            )
            // Source overlay fixtures intentionally omit mutable Hall actors: the
            // management layer is compared independently of map-unit state.
            if (hallManagement == null && hallInfo == null && hallEquipConfirmation == null) playback.stage.units.values.filter { it.visible }
                .forEach { unit ->
                    val profile = gameDataCatalog.unitProfile(unit.id)
                    log.draw(
                        "characters", "HallLayer", "Canvas/Layer/map/unit-${unit.id}", "sprite",
                        mapX(unit.visualX, unit.visualY) - 41.28f, mapY(unit.visualX, unit.visualY) - 55.04f,
                        82.56f, 110.08f, "map-avatar:${profile?.mapAvatar ?: unit.id}:direction:${unit.direction}"
                    )
                }
        }
        if (hallManagement != null) {
            appendHallManagementRenderEvents(log, requireNotNull(hallManagement))
        } else if (hallEquipConfirmation != null) {
            val confirmation = requireNotNull(hallEquipConfirmation)
            equipConfirmationEvidenceRecorder.append(
                log,
                ScenarioEquipConfirmationEvidenceView(
                    hallOverlayFixture,
                    confirmation.values,
                    confirmation.actionLabel
                ),
            )
        } else if (hallInfo == null && playback.state == PlaybackState.COMPLETE && playback.stage.menuVisible) {
            log.draw(
                "controls", "HallCommandLayer", "Canvas/HallCommandLayer/menu", "sprite", 31f, 318.2f, 51.6f, 51.6f,
                "maps/ui/hall-command/menu.png"
            )
            listOf("battle", "equip", "buy", "sell").forEachIndexed { index, name ->
                log.draw(
                    "controls", "HallCommandLayer", "Canvas/HallCommandLayer/$name", "sprite",
                    895.58f + index * 82.56f, 1.72f, 82.56f, 82.56f, "maps/ui/hall-command/$name.png"
                )
            }
        }
        return log.jsonl()
    }


    /** Source-authored non-management Hall overlays in exact Cocos traversal order. */
    private fun appendHallOverlayFixtureRenderEvents(log: RenderEventLog, fixture: String) {
        ScenarioHallOverlayEvidenceRecorder(hallOverlayEvidenceInput(fixture)).append(log)
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
                id to ScenarioHallOverlayItemEvidence(
                    item.name, item.icon, gameDataCatalog.equipmentTypeName(item.itemType),
                    gameDataCatalog.purchasePrice(item), item.intro,
                )
            } }.toMap(),
            postsNames = (0..80).map(gameDataCatalog::postsName),
        )

    private fun appendHallInfoRenderEvents(log: RenderEventLog, kind: HallInfo) = when (kind) {
        HallInfo.FORCES -> staticHallInfoEvidenceRecorder.appendForces(log)
        HallInfo.HELPER -> staticHallInfoEvidenceRecorder.appendHelper(log)
        HallInfo.PROPERTY -> propertyEvidenceRecorder.append(
            log, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.PROPERTY),
        )
        HallInfo.TERRAIN -> terrainEvidenceRecorder.append(
            log, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.TERRAIN),
        )
        HallInfo.TREASURE -> treasureEvidenceRecorder.append(
            log, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.TREASURE),
        )
    }

    private fun appendEquipRenderEvents(
        log: RenderEventLog,
        phase: String = "hall-equip-stable",
        layer: String = "EquipLayer",
    ) {
        val scale = .86f
        val spriteBlend = listOf(770, 771)
        val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun event(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            visible: Boolean = true,
            opacity: Float = 1f,
        ) = log.draw(
            phase, layer, path, type,
            x * scale, y * scale, w * scale, h * scale, asset,
            opacity = opacity,
            blend = if (type == "label") labelBlend else spriteBlend,
            visible = visible,
            text = text,
        )

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float = 50.4f`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `visible` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(
            path: String,
            value: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float = 50.4f,
            visible: Boolean = true
        ) =
            event(path, "label", x, y, w, h, text = value, visible = visible)

        /**
         * 공개 메서드 `button`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `labelX` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `labelY` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `labelW` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun button(
            path: String,
            value: String,
            x: Float,
            y: Float,
            w: Float,
            labelX: Float,
            labelY: Float,
            labelW: Float
        ) {
            event("$path/Background", "sliced-sprite", x, y, w, 50f, "box3")
            label("$path/Background/Label", value, labelX, labelY, labelW, 40f)
        }

        val unitId = hallEquipUnitId()
        val unit = gameDataCatalog.unitProfile(unitId) ?: gameDataCatalog.unitProfile(0)
        campaign.inventory.ensureDefaultEquipment(unitId, gameDataCatalog)
        val zeroBasedLevel = (campaign.unitAttribute(unitId, 18, unit?.level ?: 1) - 1).coerceAtLeast(0)
        val posts = campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)
        val profile = unit?.let { gameDataCatalog.battleProfile(it.id, zeroBasedLevel, posts) }
        val bonus = campaign.inventory.equipment[unitId]
            ?.let { gameDataCatalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1) }
            ?: GameDataCatalog.EquipmentBonus()
        val unitName = campaign.unitNames[unitId] ?: unit?.name ?: "조조"
        // Source UnitInfoBase uses the pre-promotion display class for the
        // initial Jojo fixture even though the battle profile resolves its
        // internal arm record as "군주".
        val postsName = if (unitId == 0) "군웅"
        else gameDataCatalog.armProfile(profile?.arm?.id ?: posts)?.name ?: "군웅"

        /**
         * 공개 메서드 `rosterLabelWidth`
         *
         * ### 파라미터
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Float`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun rosterLabelWidth(value: String): Float = when (value) {
            "조조", "군웅" -> 69.2f
            "허자장", "풍수사" -> 103.8f
            else -> 103.8f
        }

        val unitNameWidth = rosterLabelWidth(unitName)
        val postsNameWidth = rosterLabelWidth(postsName)

        event("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .392f)
        event("Canvas/Layer/bg1", "tiled-sprite", 138.186f, 33.5f, 1212f, 733f, "Logo_9-1")
        event("Canvas/Layer/bg1/box3", "sliced-sprite", 138.186f, 33.5f, 1212f, 733f, "box3")
        event("Canvas/Layer/bg1/title", "sprite", 138.186f, 716.5f, 1212f, 50f, "bg1")
        label("Canvas/Layer/bg1/title/label", "장비", 709.586f, 716.3f, 69.2f)
        button("Canvas/Layer/bg1/button5", "이전 무장", 979.686f, 44f, 177f, 988.186f, 52f, 160f)
        button("Canvas/Layer/bg1/button6", "다음 무장", 1156.686f, 44f, 177f, 1165.186f, 52f, 160f)
        button("Canvas/Layer/bg1/button7", "종료", 748.527f, 44f, 97f, 747.027f, 52f, 100f)
        button("Canvas/Layer/bg1/button8", "모두 해제", 573.685f, 44f, 173.2f, 580.285f, 52f, 160f)
        listOf("전부", "무기", "보구", "보조").forEachIndexed { index, value ->
            val x = 144.186f + index * 150f
            button("Canvas/Layer/bg1/button${10 + index}", value, x, 659f, 150f, x - 5f, 667f, 160f)
        }
        button("Canvas/Layer/bg1/button14", "정보", 145.76f, 44f, 99.7f, 115.61f, 52f, 160f)
        event("Canvas/Layer/bg1/box1", "sliced-sprite", 144.486f, 99.95f, 703.4f, 560.1f, "box1")
        event("Canvas/Layer/bg1/box1/box2", "sliced-sprite", 144.486f, 99.95f, 703.4f, 560.1f, "box2")
        event("Canvas/Layer/bg1/vline", "sprite", 849.486f, 39.35f, 6f, 677.1f, "vline")
        event("Canvas/Layer/bg1/button0", "sliced-sprite", 924.186f, 658f, 360f, 56f, "box3")
        event("Canvas/Layer/bg1/button0/vline", "sprite", 1101.186f, 664.15f, 6f, 47.7f, "vline")
        label("Canvas/Layer/bg1/button0/label0", unitName, 1014.039f - unitNameWidth / 2f, 663.8f, unitNameWidth)
        label("Canvas/Layer/bg1/button0/label1", postsName, 1196.1f - postsNameWidth / 2f, 663.8f, postsNameWidth)

        val base = "Canvas/Layer/bg1/scrollview/view/content/box1"
        val faceFrame = when (unitId) {
            0 -> if ((unit?.face ?: 0) <= 3) (unit?.face ?: 0) + 1 else unit?.face ?: unitId
            157 -> 214
            else -> unit?.face ?: unitId
        }
        event("$base/face", "sprite", 894.812f, 413.337f, 192f, 240f, faceFrame.toString())
        if (hallOverlayFixture == "unit-list-close") {
            event("$base/face/bg0", "sliced-sprite", 894.812f, 413.337f, 192f, 240f, "box2")
        } else {
            event("$base/face/bg0", "sliced-sprite", 870.812f, 415.337f, 240f, 236f, "box2")
        }
        label("$base/label0", unitName, 1122.186f, 601.72f, unitNameWidth)
        label("$base/label1", postsName, 1122.186f, 551.72f, postsNameWidth)
        label("$base/label", "Exp", 1122.186f, 450.72f, 68.93f)
        event("$base/progressBar", "sliced-sprite", 1197.186f, 450.92f, 134f, 24f, "default_scrollbar_bg")
        event("$base/progressBar/bar", "sliced-sprite", 1199.186f, 452.92f, 0f, 20f, "Mark_6-1")
        label("$base/progressBar/label", "0/100", 1214.136f, 452.094f, 100.1f)
        label("$base/label", "Lv", 1122.061f, 500.72f, 42.25f)
        label("$base/label2", (profile?.level ?: 1).toString(), 1185.061f, 500.778f, 22.25f)
        label("$base/label", "HP", 878.401f, 359.72f, 55.57f)
        label("$base/label", "MP", 1126.186f, 359.72f, 60f)
        label("$base/label", "공격력", 886.286f, 300.72f, 103.8f)
        label("$base/label", "정신력", 1134.286f, 300.72f, 103.8f)
        label("$base/label", "방어력", 886.286f, 240.72f, 103.8f)
        label("$base/label", "폭발력", 1134.286f, 240.72f, 103.8f)
        label("$base/label", "사기", 883.586f, 181.72f, 69.2f)
        label("$base/label", "이동력", 1134.286f, 181.72f, 103.8f)
        val stats = listOf(
            profile?.maxHitPoints ?: 0,
            profile?.maxMagicPoints ?: 0,
            (profile?.attack ?: 0) + bonus.attack,
            (profile?.spirit ?: 0) + bonus.spirit,
            (profile?.defense ?: 0) + bonus.defense,
            profile?.critical ?: 0,
            profile?.morale ?: 0,
            profile?.movement ?: 0,
        )
        val statBoxes = listOf(
            floatArrayOf(1008.186f, 359.92f, 1014.816f, 359.72f, 66.74f),
            floatArrayOf(1257.186f, 359.92f, 1274.941f, 359.72f, 44.49f),
            floatArrayOf(1008.186f, 300.92f, 1025.941f, 300.72f, 44.49f),
            floatArrayOf(1257.186f, 300.92f, 1274.941f, 300.72f, 44.49f),
            floatArrayOf(1008.186f, 240.92f, 1025.941f, 240.72f, 44.49f),
            floatArrayOf(1257.186f, 240.92f, 1274.941f, 240.72f, 44.49f),
            floatArrayOf(1008.186f, 181.92f, 1025.941f, 181.72f, 44.49f),
            floatArrayOf(1257.186f, 181.92f, 1286.061f, 181.72f, 22.25f),
        )
        statBoxes.forEachIndexed { index, pos ->
            event("$base/bg$index", "sliced-sprite", pos[0], pos[1], 80f, 50f, "box2")
            val value = stats[index].toString()
            val width = if (value == "115" || value == "112") 63.77f else pos[4]
            label("$base/bg$index/label", value, pos[0] + (80f - width) / 2f, pos[3], width)
        }

        val equipped = campaign.inventory.equippedItems().filter { it.unitId == unitId }
        val weapon =
            equipped.firstOrNull { gameDataCatalog.equipmentProfile(it.itemId)?.itemType?.let { type -> type < 20 } == true }
        val armor =
            equipped.firstOrNull { gameDataCatalog.equipmentProfile(it.itemId)?.itemType?.let { type -> type in 20..25 } == true }

        /**
         * 공개 메서드 `equipmentSlot`
         *
         * ### 파라미터
        - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `item` (`CampaignEquippedItem?`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `slotLabel` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun equipmentSlot(index: Int, item: CampaignEquippedItem?, slotLabel: String) {
            val rootY = 24.38f - index * 158f
            val itemProfile = item?.let { gameDataCatalog.equipmentProfile(it.itemId) }
            val slotVisible = index < 2
            val detailVisible = index == 0
            val slotRoot = "Canvas/Layer/bg1/scrollview/view/content/bg$index"
            event(slotRoot, "sliced-sprite", 867.136f, rootY, 468.1f, 150f, "box1", visible = slotVisible)
            val labelY = floatArrayOf(122.083f, -38.415f, -194.883f)[index]
            val valueY = floatArrayOf(122.38f, -38.62f, -194.62f)[index]
            val frameY = floatArrayOf(33.733f, -126.765f, -283.233f)[index]
            label(
                "$slotRoot/label", slotLabel, if (index == 0) 1047.737f else 1039.506f, labelY,
                if (index == 0) 80.31f else 91.43f, visible = slotVisible
            )
            label("$slotRoot/label0", itemProfile?.name ?: "없음", 1124.186f, valueY, 206f, 50f, visible = slotVisible)
            event("$slotRoot/box2", "sliced-sprite", 874.796f, frameY, 134.78f, 135.1f, "box2", visible = slotVisible)
            if (index < 2 && itemProfile != null) {
                event(
                    "$slotRoot/box2/icon", "sprite", 878.186f, 37.283f - index * 160.498f,
                    128f, 128f, "${itemProfile.icon}-1"
                )
            }
            if (index < 2) {
                label("$slotRoot/label_0", "Lv", 1018.186f, 76.083f - index * 160.498f, 42.25f, visible = detailVisible)
                label(
                    "$slotRoot/label1",
                    (item?.level ?: 1).toString(),
                    1085.186f,
                    76.083f - index * 160.498f,
                    22.25f,
                    visible = detailVisible
                )
                label(
                    "$slotRoot/label_1",
                    "Exp",
                    1018.186f,
                    30.083f - index * 160.498f,
                    68.93f,
                    visible = detailVisible
                )
                event(
                    "$slotRoot/progressBar", "sliced-sprite", 1104.186f, 29.283f - index * 160.498f,
                    204f, 24f, "default_scrollbar_bg", visible = detailVisible
                )
                event(
                    "$slotRoot/progressBar/bar", "sliced-sprite", 1106.186f, 31.283f - index * 160.498f,
                    0f, 20f, "Mark_6-1", visible = detailVisible
                )
                label(
                    "$slotRoot/progressBar/label", "${item?.experience ?: 0}/100", 1156.136f,
                    30.457f - index * 160.498f, 100.1f, visible = detailVisible
                )
            }
        }
        equipmentSlot(0, weapon, "무기:")
        equipmentSlot(1, armor, "보구: ")
        equipmentSlot(2, null, "보조: ")
    }

    /** Hall id 9 opened through UnitInfoBaseLayer.button0. */
    private fun appendHallUnitListRenderEvents(log: RenderEventLog) {
        val scale = .86f
        val spriteBlend = listOf(770, 771)
        val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun event(
            layer: String,
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            visible: Boolean = true,
            opacity: Float = 1f,
        ) = log.draw(
            "hall-unit-list-stable", layer, path, type,
            x * scale, y * scale, w * scale, h * scale, asset,
            opacity = opacity,
            blend = if (type == "label") labelBlend else spriteBlend,
            visible = visible,
            text = text,
        )
        event(
            "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "default_sprite_splash", visible = false, opacity = 0f
        )
        event("UnitListLayer", "Canvas/Layer/bg1", "tiled-sprite", 924.186f, 248.3f, 360f, 409.7f, "Logo_9-1")
        event("UnitListLayer", "Canvas/Layer/bg1/vline", "sprite", 1101.186f, 249.85f, 6f, 406.5f, "vline")
        event("UnitListLayer", "Canvas/Layer/bg1/box3", "sliced-sprite", 924.186f, 248.3f, 360f, 409.7f, "box1")

        val rows = requireNotNull(hallUnitListLayer).rows.take(6)
        rows.forEachIndexed { index, id ->
            val unit = gameDataCatalog.unitProfile(id)
            val name = campaign.unitNames[id] ?: if (id == 181) "병사 " else unit?.name ?: "무장"
            val posts = gameDataCatalog.postsName(campaign.unitAttribute(id, 17, unit?.posts ?: 0))
            val y = 607f - index * 52f
            val nameWidth = when (name) {
                "조조" -> 69.2f; "허자장" -> 103.8f; "병사 " -> 80.31f; else -> 103.8f
            }
            val postsWidth = when (posts) {
                "군웅" -> 69.2f; else -> 103.8f
            }
            event(
                "UnitListLayer", "Canvas/Layer/bg1/scrollview/view/content/item", "sprite", 924.186f, y, 360f, 50f,
                "885a69b4-08ed-4c78-8896-ffb04eb2bd20"
            )
            event(
                "UnitListLayer", "Canvas/Layer/bg1/scrollview/view/content/item/label0", "label",
                1013.669f - nameWidth / 2f, y - .2f, nameWidth, 50.4f, text = name
            )
            event(
                "UnitListLayer", "Canvas/Layer/bg1/scrollview/view/content/item/label1", "label",
                1194.669f - postsWidth / 2f, y - .2f, postsWidth, 50.4f, text = posts
            )
        }
    }

    private fun appendHallManagementRenderEvents(log: RenderEventLog, kind: HallManagement) {
        if (kind != HallManagement.EQUIP) {
            ScenarioHallManagementEvidenceRecorder(hallManagementEvidenceInput(kind)).append(log)
            return
        }
        if (kind == HallManagement.EQUIP) {
            val unitListOpen = hallUnitListLayer != null
            appendEquipRenderEvents(
                log,
                phase = if (unitListOpen) "hall-unit-list-stable" else "hall-equip-stable",
                layer = if (unitListOpen) "UnitListLayer" else "EquipLayer",
            )
            if (unitListOpen) appendHallUnitListRenderEvents(log)
            return
        }
    }

    private fun hallManagementEvidenceInput(kind: HallManagement): ScenarioHallManagementEvidenceInput {
        val unitId = hallEquipUnitId()
        val unit = gameDataCatalog.unitProfile(unitId)
        val level = campaign.unitAttribute(unitId, 18, unit?.level ?: 1)
        val profile = unit?.let {
            gameDataCatalog.battleProfile(unitId, (level - 1).coerceAtLeast(0), campaign.unitAttribute(unitId, 17, it.posts))
        }
        campaign.inventory.ensureDefaultEquipment(unitId, gameDataCatalog)
        val bonus = campaign.inventory.equipment[unitId]?.let {
            gameDataCatalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1)
        } ?: GameDataCatalog.EquipmentBonus()
        val weapon = campaign.inventory.equippedItems().firstOrNull { it.unitId == unitId }?.let {
            gameDataCatalog.equipmentProfile(it.itemId)?.let { item -> ScenarioHallManagementEquipment(item.name, it.level) }
        }
        return ScenarioHallManagementEvidenceInput(
            kind = ScenarioHallManagementEvidenceKind.valueOf(kind.name),
            money = campaign.money,
            buyRows = hallViews.buyCandidates().take(3).map { item ->
                ScenarioHallManagementBuyRow(
                    item.name, gameDataCatalog.equipmentTypeName(item.itemType),
                    campaign.inventory.items[item.id] ?: 0, gameDataCatalog.purchasePrice(item),
                )
            },
            unit = ScenarioHallManagementUnitEvidence(
                unit?.name ?: "조조",
                gameDataCatalog.postsName(campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)).ifEmpty { "군웅" },
                level,
                listOf(
                    profile?.maxHitPoints ?: 0, profile?.maxMagicPoints ?: 0, (profile?.attack ?: 0) + bonus.attack,
                    profile?.spirit ?: 0, (profile?.defense ?: 0) + bonus.defense, profile?.critical ?: 0,
                    profile?.morale ?: 0, profile?.movement ?: 0,
                ),
                weapon,
            ),
        )
    }

    /**
     * Renderer-side frame log for source/game composition comparison. Values
     * describe the quads actually submitted in the current 1280x688 viewport;
     * screenshots remain a separate second-stage visual oracle.
     */
    /**
     * 공개 메서드 `compositionTrace`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    /** Delegates serialization to a pure recorder over an immutable projection. */
    fun compositionTrace(): String = ScenarioCompositionEvidenceRecorder().record(evidenceView())


    private fun evidenceView(): ScenarioEvidenceView = ScenarioEvidenceView(
        moduleName = moduleName,
        playbackState = playback.state.toString(),
        backgroundId = playback.stage.backgroundId,
        units = playback.stage.units.values.filter { it.visible }.map { unit ->
            ScenarioEvidenceUnit(
                id = unit.id,
                scriptX = unit.visualX,
                scriptY = unit.visualY,
                direction = unit.direction,
                action = unit.action,
                avatarId = gameDataCatalog.unitProfile(unit.id)?.mapAvatar ?: unit.id,
            )
        },
        heads = playback.stage.heads.values.filter { it.opacity > 0f }.map { head ->
            ScenarioEvidenceHead(head.characterId, head.visualX, head.visualY, head.opacity)
        },
        dialogue = playback.currentDialogue?.let { dialogue ->
            ScenarioEvidenceDialogue(
                side = playback.currentDialogueSide,
                atTop = playback.currentDialogueAtTop,
                speakerId = dialogue.speakerId?.toIntOrNull(),
                visibleText = scenarioViewState.dialogueVisibleText,
            )
        },
        modal = if (playback.state == PlaybackState.MODAL && playback.currentModalKind != null) {
            ScenarioEvidenceModal(playback.currentModalKind.toString(), playback.currentModalText.orEmpty())
        } else null,
        hallMenu = hallEvidenceMenu(),
        hallCommandVisible = playback.state == PlaybackState.COMPLETE && playback.stage.menuVisible,
        hallManagement = hallManagement?.let { ScenarioEvidenceHallManagement.valueOf(it.name) },
        hallInfo = hallEvidenceInfo(),
    )

    private fun hallEvidenceMenu(): ScenarioEvidenceHallMenu? {
        val isAmbitionModal = playback.state == PlaybackState.MODAL &&
                playback.currentModalKind == ScenarioModalKind.AMBITION
        if (!hallMenuOpen && !isAmbitionModal) return null
        val tween = ((playback.ambitionElapsedSeconds - 1.2f) / 1f).coerceIn(0f, 1f)
        val value = if (hallMenuOpen) playback.stage.ambition.toFloat()
        else playback.ambitionFrom + (playback.ambitionTo - playback.ambitionFrom) * tween
        return ScenarioEvidenceHallMenu(playback.ambitionFrom, playback.ambitionTo, value)
    }

    private fun hallEvidenceInfo(): ScenarioEvidenceHallInfo? = hallInfo?.let { kind ->
        val rows = when (kind) {
            HallInfo.FORCES -> campaign.joinedUnits.take(7).indices.map { row ->
                ScenarioEvidenceRect(147.49f, 469.63f - row * 49f, 985.02f, 49f)
            }

            HallInfo.PROPERTY -> propertyEvidenceRows()
            HallInfo.TERRAIN -> (0 until 6).map { row ->
                ScenarioEvidenceRect(249f, 453.56f - row * 64.5f, 854.07f, 64.5f)
            }

            HallInfo.TREASURE -> (0 until 6).map { index ->
                ScenarioEvidenceRect(232.10f + index % 2 * 410.22f, 413.23f - index / 2 * 165.98f, 405.06f, 163.40f)
            }

            HallInfo.HELPER -> listOf(ScenarioEvidenceRect(139f, 103.07f, 1001.98f, 494.86f))
        }
        ScenarioEvidenceHallInfo(kind.name.lowercase(), rows)
    }

    private fun propertyEvidenceRows(): List<ScenarioEvidenceRect> {
        fun accepts(id: Int): Boolean {
            val itemType = gameDataCatalog.equipmentProfile(id)?.itemType ?: return false
            return when (hallPropertyTab) {
                HallPropertyTab.WEAPON -> itemType < 20
                HallPropertyTab.ARMOR -> itemType in 20..25
                HallPropertyTab.AUXILIARY -> itemType > 45 && id < 150
                HallPropertyTab.PROPERTY -> id >= 150 || itemType in 26..45
            }
        }

        val equippedCount = if (hallPropertyTab == HallPropertyTab.PROPERTY) 0
        else campaign.inventory.equippedItems().count { accepts(it.itemId) }
        val count = (equippedCount + campaign.inventory.items.count { (id, _) -> accepts(id) }).coerceAtMost(7)
        return (0 until count).map { row ->
            ScenarioEvidenceRect(217.42f, 481.58f - row * 67.08f, 846.56f, 65.36f)
        }
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

    private fun portraitTexture(characterId: Int): Texture? {
        return sceneAssets.portraitTexture(characterId)
    }

    /** Model.unitAttrFace2, which DialogueLayer uses before loading Head/<id>. */
    private fun dialoguePortrait(unitId: Int): Texture? = portraitTexture(dialoguePortraitId(unitId))

    private fun dialoguePortraitId(unitId: Int): Int {
        val face = gameDataCatalog.unitProfile(unitId)?.face ?: return unitId
        return if (unitId == 0 && face <= 3) face + 1 else face + 8
    }

    private fun backgroundTexture(backgroundId: Int): Texture? {
        return sceneAssets.backgroundTexture(backgroundId)
    }

    private fun unitTexture(assetId: Int): Texture? {
        return sceneAssets.unitTexture(assetId)
    }

}

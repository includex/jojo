package com.jojo.game.presentation.scenario

import com.jojo.game.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.campaign.CampaignEquippedItem
import com.jojo.game.presentation.scenario.hall.*
import com.jojo.game.presentation.scenario.ScenarioPlaybackController
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.evidence.*
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
import java.nio.file.Files
import java.nio.file.Path

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
    private val scriptedChoices: List<Int>,
    private val allowPendingChoiceAfterScript: Boolean,
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
    private val choiceTracePath: String?,
    private val randomTracePath: String?,
    private val stopAfterRandomTrace: Boolean,
    private val stopAfterRandomTraceCount: Int?,
    private val campaign: CampaignState,
) : ScreenAdapter() {
    private enum class HallManagement { EQUIP, BUY, SELL }
    private enum class HallEquipTab { ALL, WEAPON, ARMOR, AUXILIARY }
    private enum class HallInfo { FORCES, PROPERTY, TERRAIN, TREASURE, HELPER }
    private enum class HallPropertyTab { WEAPON, ARMOR, AUXILIARY, PROPERTY }
    private data class EquipConfirmation(
        val values: List<Int>,
        val actionLabel: String,
        val itemId: Int? = null,
        val unequipSlot: CampaignEquipmentSlot? = null,
    )

    private data class HallDrawEntry(
        val zIndex: Float,
        val siblingOrder: Int,
        val head: ScenarioHead? = null,
        val unit: TacticalUnit? = null,
    )

    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    private val shapes = ShapeRenderer()
    private val batch = SpriteBatch()
    private val campaignE2eStartedScenes = mutableListOf<Int>()
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
            ScenarioInterpreter.BattleScriptContext(
                round = scriptedBattleRound,
                camp = scriptedBattleCamp,
                attributes = scriptedBattleAttributes,
                positions = scriptedBattlePositions,
                positionsByCamp = scriptedBattlePositionsByCamp,
                enemyDefeated = scriptedBattleEnemyDefeated,
            ),
        )
        game.campaignE2eScenarioStarted(moduleName, scriptedStartScene.removePrefix("scene").toIntOrNull() ?: 0)
        start(scriptedStartScene, scriptedStartLabel)
        campaignE2eStartedScenes += scriptedStartScene.removePrefix("scene").toIntOrNull() ?: 0
    }
    private val gameDataCatalog = GameDataCatalog.load()
    private val equipConfirmationFlow = EquipConfirmationFlow(campaign, gameDataCatalog)
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
    private val scenarioViewState get() = playbackController.viewState
    private val storyEvidenceRecorder = ScenarioStoryEvidenceRecorder()
    private val equipConfirmationEvidenceRecorder = ScenarioEquipConfirmationEvidenceRecorder()
    private val glyphLayout = GlyphLayout()
    private val settingsPreferences by lazy { game.settingsPreferences() }
    private var elapsed = 0f
    private val routedAfterCompletion get() = scenarioViewState.routedAfterCompletion
    private var naturalSceneIndex = scriptedStartScene.removePrefix("scene").toIntOrNull() ?: 0

    /** True between HallLayer's battle command and sceneN's source completion. */
    private var hallBattleScenePending = false
    private var nextEntryFlowInputAt = 0f
    private var hallFixtureInstalled = false
    private val hallInteraction = HallInteractionController()
    private val hallInteractionView get() = hallInteraction.view
    private val hallMenuOpen get() = hallInteractionView.menuOpen
    private var hallManagement: HallManagement? = null
    private var hallManagementNotice: String? = null
    private val hallSaveLayer by lazy {
        SaveLayer(object : SaveLayer.Repository {
            override fun load(index: Int): String? = game.savedCampaignSlot(index)
            override fun save(index: Int) {
                game.saveCampaign(index)
            }
        })
    }
    private var hallSaveOpen = false

    private data class HallItemDetail(val itemId: Int, val level: String, val experience: Int, val experienceLimit: Int)

    private var hallItemDetail: HallItemDetail? = null
    private var hallItemLayer: ItemLayer? = null

    /** EquipLayer starts on its weapon tab and the first Model.unitsIter row. */
    private val hallEquipTab get() = HallEquipTab.entries[hallInteractionView.equipTabIndex]
    private var hallEquipUnitIndex = 0
    private var hallEquipUnequipConfirmation = false
    private var hallUnitListLayer: HallUnitListLayer? = null
    private var hallEquipConfirmation: EquipConfirmation? = null
    private var hallExclusiveLayer: ExclusiveLayer? = null
    private var hallMagicLayer: MagicInfoLayer? = null
    private var hallUnitInfoLayer: UnitInfoLayer? = null
    private var hallFeatsLayer: FeatsLayer? = null
    private var hallFeatsHelpOpen = false
    private var hallInfo: HallInfo? = null
    private var hallPropertyTab = HallPropertyTab.WEAPON
    private var hallTerrainTab = TerrainLayer.Tab.RISE
    private val hallBuyTab get() = hallInteractionView.buyTabIndex
    private val hallSellTab get() = hallInteractionView.sellTabIndex
    private fun currentStageIndex(): Int = moduleName.substringAfter('_').toIntOrNull() ?: 0
    private fun hallBuyCandidates(): List<GameDataCatalog.EquipmentProfile> {
        // The isolated source fixture feeds 0..itemCount (255 is its sentinel).
        // Cocos' vertical layout leaves the tail at the top of the viewport,
        // hence 254, 253, 252 are the three visible rows.
        if (hallOverlayFixture == "buy") return gameDataCatalog.allEquipmentProfiles()
            .asReversed()
            .filter { it.id != 255 && gameDataCatalog.equipmentCategory(it) <= 2 && it.price != 255 }
        return gameDataCatalog.hallBuyProfiles(currentStageIndex(), campaign.averageJoinedLevel())
            .filter { gameDataCatalog.equipmentCategory(it) <= 2 }
    }

    private fun hallBuyProperties(): List<GameDataCatalog.EquipmentProfile> =
        (if (hallOverlayFixture == "buy") gameDataCatalog.allEquipmentProfiles()
        else gameDataCatalog.hallBuyProfiles(currentStageIndex(), campaign.averageJoinedLevel()))
            .filter { gameDataCatalog.equipmentCategory(it) == 3 && it.price != 255 }
            .sortedBy { it.id }

    private fun hallBuyCatalogView(): HallBuyCatalogView {
        val propertyTab = hallBuyTab != 0
        val profiles = if (propertyTab) hallBuyProperties().take(4) else hallBuyCandidates().take(3)
        return HallBuyCatalogView(
            propertyTab = propertyTab,
            rows = profiles.map { item ->
                val inventory = campaign.inventory.items[item.id] ?: 0
                HallBuyCatalogRowView(
                    name = item.name,
                    icon = item.icon,
                    typeName = gameDataCatalog.equipmentTypeName(item.itemType),
                    inventory = inventory,
                    total = inventory + campaign.inventory.equippedItems().count { it.itemId == item.id },
                    price = gameDataCatalog.purchasePrice(item).let { price -> if (item.price == 255) "---" else price.toString() },
                )
            },
        )
    }

    /** Immutable right-side BuyLayer unit card; drawing never prepares inventory state. */
    private fun hallBuyUnitSummaryView(): HallBuyUnitSummaryView {
        val unitId = campaign.joinedUnits.firstOrNull() ?: 0
        val unit = gameDataCatalog.unitProfile(unitId) ?: gameDataCatalog.unitProfile(0)
        val zeroBasedLevel = (campaign.unitAttribute(unitId, 18, unit?.level ?: 1) - 1).coerceAtLeast(0)
        val profile = unit?.let {
            gameDataCatalog.battleProfile(
                it.id,
                zeroBasedLevel,
                campaign.unitAttribute(it.id, 17, it.posts),
            )
        }
        val bonus = campaign.inventory.equipment[unitId]
            ?.let { gameDataCatalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1) }
            ?: GameDataCatalog.EquipmentBonus()
        return HallBuyUnitSummaryView(
            portraitId = dialoguePortraitId(unitId),
            name = campaign.unitNames[unitId] ?: unit?.name ?: "조조",
            postName = gameDataCatalog.postsName(campaign.unitAttribute(unitId, 17, unit?.posts ?: 0))
                .ifEmpty { "군웅" },
            level = profile?.level ?: 1,
            hitPoints = profile?.maxHitPoints ?: 0,
            magicPoints = profile?.maxMagicPoints ?: 0,
            stats = listOf(
                HallBuyUnitSummaryStat("공격력", (profile?.attack ?: 0) + bonus.attack),
                HallBuyUnitSummaryStat("정신력", (profile?.spirit ?: 0) + bonus.spirit),
                HallBuyUnitSummaryStat("방어력", (profile?.defense ?: 0) + bonus.defense),
                HallBuyUnitSummaryStat("폭발력", profile?.critical ?: 0),
                HallBuyUnitSummaryStat("사기", profile?.morale ?: 0),
                HallBuyUnitSummaryStat("이동력", profile?.movement ?: 0),
            ),
        )
    }

    /** State transition hook for unit cards that require the campaign's default slots. */
    private fun prepareHallManagementDefaultEquipment(kind: HallManagement) {
        val unitId = if (kind == HallManagement.EQUIP) hallEquipUnitId() else campaign.joinedUnits.firstOrNull() ?: 0
        campaign.inventory.ensureDefaultEquipment(unitId, gameDataCatalog)
    }

    /** Immutable ForcesListLayer snapshot; the renderer does not mutate campaign inventory. */
    private fun hallForcesView(): HallForcesView = HallForcesView(
        rows = campaign.joinedUnits.take(7).mapNotNull { id ->
            val unit = gameDataCatalog.unitProfile(id) ?: return@mapNotNull null
            val level = campaign.unitAttribute(id, 18, unit.level)
            val profile = gameDataCatalog.battleProfile(
                id,
                (level - 1).coerceAtLeast(0),
                campaign.unitAttribute(id, 17, unit.posts),
            ) ?: return@mapNotNull null
            val bonus = campaign.inventory.equipment[id]?.let {
                gameDataCatalog.equipmentBonus(it.asScriptValues(), profile.level)
            } ?: GameDataCatalog.EquipmentBonus()
            HallForcesRowView(
                values = listOf(
                    campaign.unitNames[id] ?: GameDataCatalog.sayLayerUnitName(unit.name),
                    profile.arm.name,
                    profile.level.toString(),
                    "${profile.maxHitPoints}/${profile.maxHitPoints}",
                    "${profile.maxMagicPoints}/${profile.maxMagicPoints}",
                    (profile.attack + bonus.attack).toString(),
                    (profile.defense + bonus.defense).toString(),
                    (profile.spirit + bonus.spirit).toString(),
                    profile.critical.toString(),
                    profile.morale.toString(),
                ),
            )
        },
    )

    private fun prepareHallForcesDefaultEquipment() {
        campaign.joinedUnits.forEach { campaign.inventory.ensureDefaultEquipment(it, gameDataCatalog) }
    }

    private fun hallSellCandidates(): List<Map.Entry<Int, Int>> = campaign.inventory.items.entries
        .filter { (_, count) -> count > 0 }
        .filter { (id, _) ->
            gameDataCatalog.equipmentProfile(id)?.let(gameDataCatalog::equipmentCategory)?.let { category ->
                if (hallSellTab == 0) category <= 2 else category == 3
            } == true
        }
        .sortedBy { it.key }

    private fun hallSellView(): HallSellView {
        val equipmentTab = hallSellTab == 0
        return HallSellView(
            rows = hallSellCandidates().take(5).mapNotNull { (itemId, count) ->
                val item = gameDataCatalog.equipmentProfile(itemId) ?: return@mapNotNull null
                HallSellRowView(
                    name = item.name,
                    icon = item.icon,
                    primaryDetail = if (equipmentTab) "Lv: ${campaign.inventory.itemLevels(itemId).firstOrNull() ?: 1}" else "인벤토리: $count",
                    secondaryDetail = if (equipmentTab) "Exp: 0" else null,
                    salePrice = if (item.price == 255) "---" else gameDataCatalog.sellingPrice(item).toString(),
                )
            },
            money = campaign.money,
            notice = hallManagementNotice,
        )
    }

    private fun hallEquipUnitIds(): List<Int> = campaign.joinedUnits.toList().ifEmpty { listOf(0) }
    private fun hallEquipUnitId(): Int {
        val units = hallEquipUnitIds()
        hallEquipUnitIndex = ((hallEquipUnitIndex % units.size) + units.size) % units.size
        return units[hallEquipUnitIndex]
    }

    private fun hallEquipInventory(): List<Map.Entry<Int, Int>> = campaign.inventory.items.entries
        .filter { (itemId, _) ->
            val type = gameDataCatalog.equipmentProfile(itemId)?.itemType ?: return@filter false
            when (hallEquipTab) {
                HallEquipTab.ALL -> type < 150
                HallEquipTab.WEAPON -> type in 0..19
                HallEquipTab.ARMOR -> type in 20..25
                HallEquipTab.AUXILIARY -> type in 26..149
            }
        }
        // UnitInfoBaseLayer: treasure, type, then descending item id.
        .sortedWith(compareBy<Map.Entry<Int, Int>> {
            if (gameDataCatalog.equipmentProfile(it.key)?.price == 255) 0 else 1
        }.thenBy {
            gameDataCatalog.equipmentProfile(it.key)?.itemType ?: 255
        }.thenByDescending { it.key })

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
                            hallManagement = null
                            hallManagementNotice = null
                            hallEquipUnequipConfirmation = false
                            hallUnitListLayer = null
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
        elapsed += delta
        if (!hallFixtureInstalled &&
            (game.requestedCaptureState() in setOf("hall-fixture", "hall-palace-fixture", "hall-section-fixture") ||
                    streetCaptureStage != null || hallOverlayFixture != null)
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
                        "equip-confirm" -> EquipConfirmation(listOf(10, -5, 0, 2, 0, 0, 1, 0), "장비")
                        "equip-confirm-unload" -> EquipConfirmation(List(8) { 0 }, "해제")
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
        // Component-isolation fixtures begin at the first SayLayer. The live
        // title route still presents the preceding source InfoLayer, while
        // this staged oracle intentionally excludes it.
        if (streetCaptureStage != null) {
            var fixtureGuard = 0
            while ((playback.state == PlaybackState.MODAL || playback.state == PlaybackState.DELAY) && fixtureGuard++ < 1000) {
                if (playback.state == PlaybackState.MODAL) playback.resumeModal() else playback.skipDelay()
            }
            playback.stage.finishAnimations()
        }
        // StageLayer.delay() pauses the original script component and resumes
        // it from the game update loop.  Advance the restored AST on every
        // frame for the same reason; without this, R_00 can stop before its
        // BattleHall / Yingchuan transition whenever a source delay occurs.
        playbackController.updatePlayback(
            delta, autoCloseUi = settingsPreferences.getInteger(
                SettingLayer.GAME_SETTING,
                SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
            ) and SettingLayer.AUTO_CLOSE != 0
        )
        // R_01 scene8 returns true only after its departure SayLayer closes
        // and bgSound(-1) runs. HallLayer consumes that return immediately;
        // it does not wait for another keyboard event before opening battle.
        if (hallBattleScenePending && playback.state == PlaybackState.COMPLETE && !playback.stage.menuVisible) {
            routeAfterScenario()
            return
        }
        // RControlScript.__dispatch__ continues sceneN -> sceneN+1 until a
        // source menu boundary or the first missing function.
        if (!isVerificationRun() && !game.hasFrameCaptureRequest() &&
            playback.state == PlaybackState.COMPLETE && (naturalSceneIndex == 0 || !playback.stage.menuVisible) &&
            !playback.stage.battleEndedByScript && playback.stage.sceneJumpTarget == null
        ) {
            val next = "scene${naturalSceneIndex + 1}"
            if (next in playback.functionNames) {
                naturalSceneIndex++
                game.campaignE2eScenarioStarted(moduleName, naturalSceneIndex)
                playback.start(next)
                campaignE2eStartedScenes += naturalSceneIndex
            }
        }
        // Deterministic driver for the real R_00 screen route.  Every action
        // goes through the same advance/choice functions as keyboard/touch;
        // delays remain wall-clock driven and no AST fast-forward is used.
        if (moduleName == "R_00" && game.requestedYingchuanEntryFlowTracePath() != null && elapsed >= nextEntryFlowInputAt) {
            nextEntryFlowInputAt = elapsed + .04f
            when (playback.state) {
                PlaybackState.DIALOGUE -> advance()
                PlaybackState.CHOICE -> {
                    val start = playback.currentChoice?.options?.indexOfFirst { it.contains("게임 시작") } ?: -1
                    playback.selectChoice(if (start >= 0) start else 0)
                    advance()
                }

                PlaybackState.MODAL -> advance()
                PlaybackState.COMPLETE -> advance()
                PlaybackState.DELAY -> Unit
            }
        }
        // routeAfterScenario replaces and disposes this screen.  Do not
        // submit another frame through its released SpriteBatch.
        if (routedAfterCompletion) return
        if (game.requestedCaptureState() == "scenario-dialogue") {
            var guard = 0
            while (playback.state != PlaybackState.DIALOGUE && playback.state != PlaybackState.COMPLETE && guard++ < 1000) {
                when (playback.state) {
                    PlaybackState.MODAL -> playback.resumeModal()
                    PlaybackState.DELAY -> playback.skipDelay()
                    PlaybackState.CHOICE -> playback.confirmChoice()
                    PlaybackState.DIALOGUE, PlaybackState.COMPLETE -> Unit
                }
            }
        }
        if (game.requestedCaptureState() == "map-info") {
            var guard = 0
            while (!(playback.state == PlaybackState.MODAL &&
                        playback.currentModalKind == ScenarioInterpreter.ModalKind.MAP_INFO) &&
                playback.state != PlaybackState.COMPLETE && guard++ < 1000
            ) {
                when (playback.state) {
                    PlaybackState.DIALOGUE -> playback.advanceDialogue()
                    PlaybackState.CHOICE -> playback.confirmChoice()
                    PlaybackState.DELAY -> playback.skipDelay()
                    PlaybackState.MODAL -> playback.resumeModal()
                    PlaybackState.COMPLETE -> Unit
                }
            }
        }
        // HallLayer._scriptOver transitions immediately after stage.end().
        // Keep deterministic capture/verifier screens stationary, but never
        // expose the migration-era completion placeholder in normal play.
        if (!routedAfterCompletion && !isVerificationRun() && !game.hasFrameCaptureRequest() &&
            ScenarioCompletionRoute.shouldRoute(
                playback.state,
                playback.stage.menuVisible,
                playback.stage.battleEndedByScript,
                playback.stage.sceneJumpTarget,
            )
        ) {
            routeAfterScenario()
            return
        }
        if (elapsed > 0.15f && game.requestedCaptureState() == "choice") {
            advanceSourceUntilChoice()
            playbackController.resetDialogueReveal()
        }
        val autoCloseEnabled = !isVerificationRun() && !game.hasFrameCaptureRequest() &&
                !game.hasRenderEventLogRequest() && settingsPreferences.getInteger(
            SettingLayer.GAME_SETTING,
            SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
        ) and SettingLayer.AUTO_CLOSE != 0
        playbackController.updatePresentation(
            delta = delta,
            autoCloseEnabled = autoCloseEnabled,
            revealDialogueForCapture = streetCaptureStage != null && game.hasRenderEventLogRequest(),
            onAdvance = ::advance,
        )
        if (streetCaptureStage != null) Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        else Gdx.gl.glClearColor(0.08f, 0.11f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        if (streetCaptureStage != null) {
            drawStreetDialogueIsolation(streetCaptureStage)
            if (elapsed > 1f && game.writeRenderEventLogIfRequested()) return
            if (elapsed > 1f && game.captureFrameIfRequested()) return
        } else {
            if (hallPalaceFixture) drawPalaceFixture() else {
                val isolatedHallOverlay = hallOverlayFixture in setOf(
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
                    "save-confirm"
                )
                drawBattlefield(drawCharacters = !isolatedHallOverlay, drawUnits = !isolatedHallOverlay)
                drawOverlay()
            }
            if (elapsed > 1f && game.writeRenderEventLogIfRequested()) return
            if (elapsed > 1f && game.captureFrameIfRequested()) return
        }

        if (verifyMode && elapsed > 0.8f) {
            val scenarioCount = ScenarioCatalog.verifyEmbeddedSources()
            check(scenarioCount == 119) { "Expected 119 restored scenarios, got $scenarioCount" }
            check(playback.stage.backgroundId != 0) { "$moduleName background command was not executed" }
            check(playback.stage.units.isNotEmpty()) { "$moduleName unit commands were not executed" }
            check(playback.currentDialogue?.text?.isNotBlank() == true) { "$moduleName Korean dialogue extraction failed" }
            if (moduleName == "R_00") {
                check(
                    playback.stage.units.keys.containsAll(
                        listOf(
                            0,
                            157,
                            181,
                            182
                        )
                    )
                ) { "R_00 grouped unit commands were not executed" }
                check(playback.currentDialogue?.text?.contains("대장님") == true) { "R_00 dialogue did not match" }
                check(Gdx.files.internal("maps/heads/181.png").exists()) { "R_00 speaker portrait was not bundled" }
                check(Gdx.files.internal("maps/2.jpg").exists()) { "R_00 loadBg source image was not bundled" }
            }
            Gdx.app.log(
                "JojoGame",
                "VERIFY_OK: $scenarioCount scenario sources + ASTs embedded; $moduleName AST runtime loaded"
            )
            Gdx.app.exit()
        }
        if (branchVerifyMode && elapsed > 0.8f) {
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
        if (alternateBranchVerifyMode && elapsed > 0.8f) {
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
        if (scriptedChoices.isNotEmpty() && elapsed > 0.8f) {
            scriptedChoices.forEachIndexed { step, choiceIndex ->
                advanceSourceUntilChoice()
                val choice = requireNotNull(playback.currentChoice) {
                    "$moduleName choice script step $step reached completion before a choice"
                }
                require(choiceIndex in choice.options.indices) {
                    "$moduleName choice script step $step selected $choiceIndex, options=${choice.options.size}"
                }
                playback.selectChoice(choiceIndex)
                confirmChoice()
            }
            advanceSourceUntilChoice()
            check(playback.state == PlaybackState.COMPLETE || (allowPendingChoiceAfterScript && playback.state == PlaybackState.CHOICE)) {
                "$moduleName choice script ended with an unconsumed choice"
            }
            choiceTracePath?.let(::writeChoiceTrace)
            randomTracePath?.let(::writeRandomTrace)
            Gdx.app.log(
                "JojoGame",
                "VERIFY_CHOICE_SCRIPT_OK: $moduleName choices=${scriptedChoices.joinToString(",")} " +
                        "random=${scriptedRandomValues.joinToString(",")} draws=${playback.randomDrawCount} " +
                        "randomRemaining=${playback.remainingInjectedRandomCount} round=$scriptedBattleRound camp=$scriptedBattleCamp pendingChoice=${playback.state == PlaybackState.CHOICE}",
            )
            Gdx.app.exit()
        }
        if (scriptedChoices.isEmpty() && randomTracePath != null && elapsed > 0.8f) {
            advanceSourceUntilChoice()
            writeRandomTrace(randomTracePath)
            Gdx.app.log("JojoGame", "VERIFY_RANDOM_TRACE_OK: $moduleName draws=${playback.randomTrace.size}")
            Gdx.app.exit()
        }
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
            beginHallBattleScene = ::beginHallBattleScene,
            onRoute = ::routeAfterScenario,
        )
    }

    /** Read-only observation; all E2E mutations still enter through the installed InputProcessor. */
    internal fun campaignE2eState(): CampaignE2eScenarioState {
        val battleButton = viewport.project(com.badlogic.gdx.math.Vector3(936.86f, 43f, 0f))
        return CampaignE2eScenarioState(
            module = moduleName,
            playback = playback.state,
            options = playback.currentChoice?.options.orEmpty(),
            selectedChoice = playback.selectedChoice,
            sceneIndex = naturalSceneIndex,
            startedScenes = campaignE2eStartedScenes.toList(),
            campaignStage = game.campaignStage(),
            menuVisible = playback.stage.menuVisible,
            dialogueText = playback.currentDialogue?.text,
            hallBattleScenePending = hallBattleScenePending,
            battleButtonScreenX = battleButton.x.toInt(),
            battleButtonScreenY = (Gdx.graphics.height - battleButton.y).toInt(),
        )
    }

    /**
     * Source HallLayer does not jump straight to StartBattleScreen. Its battle
     * button dispatches sceneN once more with battleTest() true; R_01 scene8
     * hides the menu, says "출발.", stops the BGM, and only then returns true.
     */
    private fun beginHallBattleScene(): Boolean {
        if (routedAfterCompletion || hallBattleScenePending ||
            playback.state != PlaybackState.COMPLETE || !playback.stage.menuVisible ||
            playback.stage.joinBattleLimit == null
        ) return false
        val nextIndex = naturalSceneIndex + 1
        val nextScene = "scene$nextIndex"
        if (nextScene !in playback.functionNames) return false

        playback.selectHallBattleCommand()
        naturalSceneIndex = nextIndex
        hallBattleScenePending = true
        game.campaignE2eScenarioStarted(moduleName, nextIndex)
        campaignE2eStartedScenes += nextIndex
        playback.start(nextScene)
        return true
    }

    private fun isVerificationRun(): Boolean = verifyMode || branchVerifyMode || alternateBranchVerifyMode ||
            scriptedChoices.isNotEmpty() || choiceTracePath != null || randomTracePath != null

    private fun routeAfterScenario() {
        playbackController.routeOnce {
            hallBattleScenePending = false
            val jump = playback.stage.sceneJumpTarget
            if (jump != null) {
                val targetStage = checkNotNull(playback.stage.sceneJumpStage) {
                    "$moduleName jumpScene($jump) did not resolve its source Model stage"
                }
                game.setCampaignStage(targetStage)
                val targetIndex = targetStage / 2
                val target = "%s_%02d".format(if (targetStage % 2 == 0) "R" else "S", targetIndex)
                if (target.startsWith("R_")) game.showScenario(target)
                else game.showBattleSandbox(target, "R_%02d".format(targetIndex + 1))
            } else playback.stage.joinBattleLimit?.let { limit ->
                val entry = campaign.roster.configureBattleRoster(limit)
                game.advanceCampaignStage()
                if (entry.directBattleRoster != null) {
                    // HallLayer._startBattle skips StartBattleScreen when its
                    // mandatory roster already fills the clamped authored max.
                    game.showBattleSandbox(matchingBattleModule(), moduleName)
                } else {
                    game.showBattlePreparation(
                        moduleName,
                        matchingBattleModule(),
                        entry.selectionLimit,
                        playback.stage.backgroundId,
                    )
                }
            } ?: run {
                // R_00 does not call setJoinBattle. Source HallLayer._startBattle
                // derives an implicit one-unit roster and enters S_00 directly.
                // Preserve that state mutation before BattleScreen.createMine.
                campaign.roster.prepareImplicitSingleUnitBattle()
                game.advanceCampaignStage()
                game.showBattleSandbox(matchingBattleModule(), moduleName)
            }
        }
    }

    private fun confirmChoice() {
        playback.confirmChoice()
        playback.chosenOption?.let { game.recordChoice(moduleName, it) }
    }

    /**
     * Capture/branch fixtures follow the original verifier's "next" route:
     * it completes each SayLayer and waits for every StageLayer.delay before
     * inspecting the actual stage.choice suspension.  In a deterministic
     * fixture, skipDelay is that completed source delay; it never invents a
     * UI state or bypasses the recovered Python statements.
     */
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

    private fun writeChoiceTrace(rawPath: String) {
        val entries = playback.choiceTrace.joinToString(",") { trace ->
            "{\"module\":\"${trace.module}\",\"function\":\"${trace.function}\",\"line\":${trace.line},\"option\":${trace.option},\"optionCount\":${trace.optionCount}}"
        }
        val path = Path.of(rawPath)
        path.parent?.let(Files::createDirectories)
        Files.writeString(path, "{\"choices\":[${entries}]}\n")
    }

    private fun writeRandomTrace(rawPath: String) {
        val entries = playback.randomTrace.joinToString(",") { trace ->
            "{\"module\":\"${trace.module}\",\"function\":\"${trace.function}\",\"line\":${trace.line},\"value\":${trace.value}}"
        }
        val path = Path.of(rawPath)
        path.parent?.let(Files::createDirectories)
        Files.writeString(path, "{\"random\":[${entries}]}\n")
    }

    private fun drawBattlefield(drawCharacters: Boolean = true, drawUnits: Boolean = drawCharacters) {
        val background = backgroundTexture(playback.stage.backgroundId)
        val palette = when (playback.stage.backgroundId) {
            2 -> Color(0.49f, 0.39f, 0.24f, 1f)
            else -> Color(0.25f, 0.33f, 0.28f, 1f)
        }
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        background?.let {
            batch.color = Color.WHITE
            batch.draw(it, 0f, 0f, 1280f, 688f)
        }
        batch.end()
        shapes.projectionMatrix = viewport.camera.combined
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        if (background == null) {
            shapes.color = palette
            shapes.rect(0f, 0f, 1280f, 688f)
        }
        // Source scene backgrounds are complete images.  Do not put the
        // migration-era checkerboard on top of them; it obscures original art.
        if (drawUnits) playback.stage.units.values.filter { unit ->
            val avatar = gameDataCatalog.unitProfile(unit.id)?.mapAvatar ?: unit.id
            val animationTime = if (unit.action == 20) unit.animationElapsed else elapsed
            unit.visible && unitTexture(
                HallUnitRender.frame(
                    avatar,
                    unit.action,
                    unit.direction,
                    animationTime
                ).textureAssetId
            ) == null
        }.forEach(::drawUnit)
        shapes.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        bodyFont.color = Color.WHITE
        val drawEntries = mutableListOf<HallDrawEntry>()
        if (drawCharacters) {
            if (drawUnits) playback.stage.units.values.filter { it.visible }.forEachIndexed { index, unit ->
                // HallUnit zIndex = -turnPos(...).y. With the default
                // 1280x800 Hall root, local Y is 424 - 4*(x+y).
                drawEntries += HallDrawEntry(unit.moveZIndex, index, unit = unit)
            }
            val headOrder = if (drawUnits) playback.stage.units.size else 0
            playback.stage.heads.values.filter { it.opacity > 0f }.forEachIndexed { index, head ->
                // Head.move assigns zIndex from the destination Y before
                // starting its tween, exactly as the source component does.
                drawEntries += HallDrawEntry(-head.y.toFloat(), headOrder + index, head = head)
            }
        }
        drawEntries.sortedWith(compareBy<HallDrawEntry> { it.zIndex }.thenBy { it.siblingOrder }).forEach { entry ->
            entry.head?.let { head ->
                dialoguePortrait(head.characterId)?.let { texture ->
                    // Head.setPos uses HallLayer.convertPos inside the 640x400
                    // map node. That node is scaled 2x and centred in Cocos'
                    // 1488.372x800 SHOW_ALL canvas; the game's 1280x688 viewport
                    // is the same transform at 0.86. Head then fits the original
                    // 192x240 face to an 80px minimum side, yielding 160x200
                    // source-canvas pixels (137.6x172 here).
                    // Hall root is stretched to the 1488.372-wide SHOW_ALL
                    // canvas. convertPos uses that live width, then map scale=2;
                    // after the game's .86 projection the X centre is exactly
                    // twice the script coordinate (not the old 640-wide guess).
                    // Head/face is authored at local (32,-40) below its parent.
                    // The map's 2x scale and .86 viewport transform make that a
                    // (+55.04,-68.8) offset in game coordinates.
                    val centerX = head.visualX * 2f + 55.04f
                    val centerY = 688f - head.visualY * 1.72f - 68.8f
                    batch.color = Color(1f, 1f, 1f, head.opacity)
                    // Head.prefab clips its 80x100 face child through the parent
                    // node's 64x80 Mask.  Drawing the complete child made every
                    // script-positioned scene portrait 25% too large.
                    val clipBounds = Rectangle(centerX - 55.04f, centerY - 68.8f, 110.08f, 137.6f)
                    val scissors = Rectangle()
                    ScissorStack.calculateScissors(viewport.camera, batch.transformMatrix, clipBounds, scissors)
                    batch.flush()
                    if (ScissorStack.pushScissors(scissors)) {
                        batch.draw(texture, centerX - 68.8f, centerY - 86f, 137.6f, 172f)
                        batch.flush()
                        ScissorStack.popScissors()
                    }
                }
            }
            entry.unit?.let { unit ->
                val x = mapX(unit.visualX, unit.visualY)
                val y = mapY(unit.visualX, unit.visualY)
                val profile = gameDataCatalog.unitProfile(unit.id)
                val animationTime = if (unit.action == 20) unit.animationElapsed else elapsed
                val spriteFrame =
                    HallUnitRender.frame(profile?.mapAvatar ?: unit.id, unit.action, unit.direction, animationTime)
                unitTexture(spriteFrame.textureAssetId)?.let { texture ->
                    batch.color = Color.WHITE
                    val sourceY = spriteFrame.row * 64
                    // HallLayer's map node has scale=(2,2). After the 688/800
                    // viewport transform, a 48x64 HallUnit is 82.56x110.08.
                    batch.draw(
                        texture, x - 41.28f, y - 55.04f, 82.56f, 110.08f,
                        0, sourceY, 48, 64, spriteFrame.flipX, false,
                    )
                    if (playback.state == PlaybackState.DIALOGUE && playback.currentDialogue?.speakerId?.toIntOrNull() == unit.id) {
                        // pmapobj/img0 local=(24,32), size=24x24 under map scale 2.
                        streetSpeechBubbleTexture?.let { bubble ->
                            batch.draw(bubble, x + 20.64f, y + 34.4f, 41.28f, 41.28f)
                        }
                    }
                }
            }
        }
        batch.color = Color.WHITE
        batch.end()
    }

    private fun drawUnit(unit: TacticalUnit) {
        val x = mapX(unit.visualX, unit.visualY)
        val y = mapY(unit.visualX, unit.visualY)
        val color = when (unit.id) {
            0 -> Color(0.23f, 0.45f, 0.20f, 1f)
            157 -> Color(0.32f, 0.24f, 0.60f, 1f)
            else -> Color(0.72f, 0.12f, 0.10f, 1f)
        }
        shapes.color = Color(0.05f, 0.05f, 0.06f, 0.38f)
        shapes.circle(x + 30f, y - 4f, 26f)
        shapes.color = color
        shapes.circle(x + 30f, y + 30f, 25f)
        shapes.rect(x + 8f, y, 44f, 38f)
    }

    private fun drawOverlay() {
        shapes.projectionMatrix = viewport.camera.combined
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        if (hallFeatsLayer != null || hallMagicLayer != null || hallExclusiveLayer != null || hallManagement != null || hallSaveOpen || hallItemLayer != null) {
            // Equip/Buy/Sell share the source Layer's full-screen
            // Panel_cancel (opacity 100/255) behind their authored panel.
            shapes.color = Color(0f, 0f, 0f, 100f / 255f)
            shapes.rect(0f, 0f, 1280f, 688f)
        } else if (hallMenuOpen) {
            shapes.color = Color(0f, 0f, 0f, 30f / 255f)
            shapes.rect(0f, 0f, 1280f, 688f)
        } else if (playback.state == PlaybackState.CHOICE) {
            // ChooseLayer and MsgBox2 both leave their serialized full-canvas
            // blocker at opacity zero in the stable fixture frame.
        } else if (playback.state == PlaybackState.DIALOGUE) {
            // DialogueLayer is composed from its source sprites below.
        } else if (playback.state == PlaybackState.MODAL && playback.currentModalText != null &&
            playback.currentModalKind in setOf(ScenarioInterpreter.ModalKind.EVENT, ScenarioInterpreter.ModalKind.INFO)
        ) {
            val text = sanitizeInfoText(scenarioViewState.modalVisibleText.ifEmpty {
                playback.currentModalText.orEmpty().take(1)
            })
            glyphLayout.setText(titleFont, text)
            // InfoLayer grows bg from the Cocos RichText measurement. The
            // source font's horizontal advance is eight logical pixels wider
            // for this seven-glyph fixture than FreeType's otherwise matching
            // raster, while the authored padding remains constant.
            val width = when (hallOverlayFixture) {
                // Exact Cocos RichText advances measured by the deterministic
                // HallLayer.getItem source fixtures, plus 40 source pixels of
                // serialized InfoLayer padding.
                "get-item-equipment" -> (259.72f + 40f) * .86f
                "get-item-property" -> (324.47f + 40f) * .86f
                else -> (glyphLayout.width + 42.4f).coerceIn(64.2f, 1120f)
            }
            val height = if (hallOverlayFixture in setOf("get-item-equipment", "get-item-property")) {
                83f * .86f
            } else {
                (glyphLayout.height + 34.4f).coerceAtLeast(71.38f)
            }
            val x = (1280f - width) / 2f
            // Source bg anchor=(.5,.28), not the usual centred sprite anchor.
            // With its node at canvas centre this raises the lower edge by
            // (.5-.28)*height ~= 15.7 logical pixels.
            val y = (688f - height) / 2f + height * 0.22f
            shapes.end()
            batch.projectionMatrix = viewport.camera.combined
            batch.begin()
            batch.color = Color.WHITE
            infoPanelPatch?.draw(batch, x, y, width, height)
                ?: dialoguePanelTexture?.let { batch.draw(it, x, y, width, height) }
            batch.end()
            shapes.begin(ShapeRenderer.ShapeType.Filled)
        } else if (playback.state == PlaybackState.MODAL && playback.currentModalText != null) {
            if (playback.currentModalKind == ScenarioInterpreter.ModalKind.MAP_INFO) {
                // Direct MapInfoLayer prefab render: bg1's Widget stretches
                // across the visible canvas and its node opacity is 127.
                shapes.color = Color(0f, 0f, 0f, 127f / 255f)
                shapes.rect(0f, 0f, 1280f, 138.46f)
            } else if (playback.currentModalKind == ScenarioInterpreter.ModalKind.SECTION) {
                // Direct source prefab render: SectionLayer is an opaque
                // black intertitle, not a stretched InfoLayer frame.
                shapes.color = Color.BLACK
                shapes.rect(0f, 0f, 1280f, 688f)
            } else if (playback.currentModalKind == ScenarioInterpreter.ModalKind.AMBITION) {
                // HallMenuLayer's full-canvas Panel_cancel is black at
                // opacity 30 while the 146px command strip remains live.
                shapes.color = Color(0f, 0f, 0f, 30f / 255f)
                shapes.rect(0f, 0f, 1280f, 688f)
            } else {
                shapes.color = Color(0.035f, 0.045f, 0.055f, 0.94f)
                shapes.rect(0f, 0f, 1280f, 688f)
            }
        }
        shapes.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        when (playback.state) {
            PlaybackState.DIALOGUE -> drawDialogue()
            PlaybackState.CHOICE -> {
                if (playback.isAskChoice) drawAskBox() else {
                    drawChoicePanel()
                    drawChoice()
                }
            }

            PlaybackState.DELAY -> Unit
            PlaybackState.MODAL -> playback.currentModalText?.let { text ->
                when (playback.currentModalKind) {
                    ScenarioInterpreter.ModalKind.SECTION -> {
                        glyphLayout.setText(sectionFont, text)
                        val x = (1280f - glyphLayout.width) / 2f
                        val y = (688f + glyphLayout.height) / 2f
                        // Source Label is white with LabelShadow #949494 at
                        // offset (2,-2), transformed by the .86 viewport.
                        sectionFont.color = Color(0.58f, 0.58f, 0.58f, 1f)
                        sectionFont.draw(batch, glyphLayout, x + 1.72f, y - 1.72f)
                        sectionFont.color = Color.WHITE
                        sectionFont.draw(batch, glyphLayout, x, y)
                    }

                    ScenarioInterpreter.ModalKind.INFO -> {
                        val visible = sanitizeInfoText(scenarioViewState.modalVisibleText.ifEmpty { text.take(1) })
                        glyphLayout.setText(titleFont, visible)
                        titleFont.color = Color.BLACK
                        // InfoLayer bg/richtext is authored at local y=18.5
                        // on the 800-high Cocos canvas: 18.5 * .86 = 15.91.
                        titleFont.draw(
                            batch,
                            glyphLayout,
                            (1280f - glyphLayout.width) / 2f,
                            (688f + glyphLayout.height) / 2f + 15.91f
                        )
                    }

                    ScenarioInterpreter.ModalKind.MAP_INFO -> {
                        streetDialogueFont.color = Color.WHITE
                        val visible =
                            playback.currentModalFixedText + scenarioViewState.modalVisibleText.ifEmpty { text.take(1) }
                        // Source RichText's left widget inset is about 30
                        // logical pixels; baseline is 73px above the panel
                        // centre, mapping to this 688px viewport.
                        // Source richtext is 25 physical pixels closer to the
                        // panel's top edge than the old inferred baseline.
                        streetDialogueFont.draw(batch, sanitizeInfoText(visible), 26f, 119f)
                    }

                    ScenarioInterpreter.ModalKind.AMBITION -> drawHallMenu()
                    else -> {
                        val visible = scenarioViewState.modalVisibleText.ifEmpty { text.take(1) }
                        glyphLayout.setText(titleFont, visible)
                        titleFont.color = Color.BLACK
                        titleFont.draw(
                            batch,
                            glyphLayout,
                            (1280f - glyphLayout.width) / 2f,
                            (688f + glyphLayout.height) / 2f + 15.91f
                        )
                    }
                }
            }

            PlaybackState.COMPLETE -> if (playback.stage.menuVisible) {
                if (hallFeatsLayer != null) {
                    drawFeatsLayer(requireNotNull(hallFeatsLayer))
                } else if (hallMagicLayer != null) {
                    drawMagicLayer(requireNotNull(hallMagicLayer))
                } else if (hallExclusiveLayer != null) {
                    // Global126 is pushed over (not instead of) EquipLayer.
                    // The isolated fixture deliberately has no parent layer.
                    hallManagement?.let(::drawHallManagement)
                    drawExclusiveLayer(requireNotNull(hallExclusiveLayer))
                } else {
                    hallItemDetail?.let(::drawHallItem)
                        ?: hallInfo?.let(::drawHallInfo)
                        ?: hallManagement?.let(::drawHallManagement)
                        ?: hallEquipConfirmation?.let { drawEquipConfirmation(it) }
                        ?: if (hallSaveOpen) {
                            drawHallSave()
                        } else run {
                            drawHallCommand()
                            if (hallMenuOpen) drawHallMenu(interactive = true)
                        }
                }
            } else drawCompletion()
        }
        batch.end()
    }

    /** Exact source frames from ChooseLayer's Cocos dynamic atlas. */
    private fun drawChoicePanel() {
        batch.color = Color.WHITE
        // Canvas SHOW_ALL -> game viewport is an exact .86 transform.
        // Source scrollview: centre=(866.186,400), size=(747,183.7).
        choicePanelTexture?.let { batch.draw(it, 423.71f, 265.01f, 642.42f, 157.98f) }
        choiceRowTexture?.let { texture ->
            batch.color = Color.WHITE
            // The recovered corpus has at most three rows; this is also the
            // source ScrollView's complete visible area.  A fourth row would
            // extend below the authored backing panel.
            val visibleCount = playback.currentChoice?.options?.take(3)?.size ?: 0
            // Source items: centre=(884.186,461-49*i), size=(690.6,45).
            repeat(visibleCount) { index -> batch.draw(texture, 463.44f, 377.11f - index * 42.14f, 593.92f, 38.7f) }
        }
        batch.color = Color.WHITE
    }

    /** Source Global/scene/MsgBox2, transformed from 1280x800 by .86. */
    private fun drawAskBox() {
        /**
         * 공개 메서드 `sourceTexture`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Texture?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun sourceTexture(path: String): Texture? = hallMenuTextures[path] ?: Gdx.files.internal(path)
            .takeIf { it.exists() }
            ?.let(::Texture)
            ?.also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                hallMenuTextures[path] = it
            }

        val logo = sourceTexture("maps/ui/start-battle/logo9.png")
        val panel = sourceTexture("maps/ui/hall-menu/inner.png")?.let { NinePatch(it, 3, 3, 3, 3) }
        val title = sourceTexture("maps/ui/hall-menu/panel.png")
        val button = sourceTexture("maps/ui/hall-menu/button.png")?.let { NinePatch(it, 9, 9, 7, 11) }

        batch.color = Color.WHITE
        logo?.let { batch.draw(it, 464.13f, 276.92f, 351.74f, 134.16f) }
        title?.let { batch.draw(it, 464.13f, 368.08f, 351.74f, 43f) }
        titleFont.color = Color.BLACK
        glyphLayout.setText(titleFont, "확인")
        titleFont.draw(batch, glyphLayout, 498.19f - glyphLayout.width / 2f, 389.58f + glyphLayout.height / 2f)
        panel?.draw(batch, 464.13f, 276.92f, 351.74f, 134.16f)
        button?.draw(batch, 482.84f, 306.16f, 145.34f, 43f)
        listOf(555.51f to "예", 718.94f to "비").forEach { (x, text) ->
            if (text == "비") button?.draw(batch, 646.27f, 306.16f, 145.34f, 43f)
            glyphLayout.setText(titleFont, text)
            titleFont.draw(batch, glyphLayout, x - glyphLayout.width / 2f, 328.39f + glyphLayout.height / 2f)
        }
    }

    private fun drawDialogue() {
        ScenarioStoryRenderer.drawStreetDialogue(sceneAssets, batch, streetDialogueView(), 3)
    }

    /** Stable Palace Hall frame: background plus the source's upper bg0 dialogue. */
    private fun drawPalaceFixture() {
        // The live Palace Hall keeps its three map units underneath the
        // upper dialogue layer.  The retained-tree event fixture only lists
        // the static HallLayer children, but the framebuffer still contains
        // these Pmapobj2 draws; omitting them made event parity hide a visible
        // composition error.
        drawBattlefield(drawCharacters = true, drawUnits = true)
        val dialogue = playback.currentDialogue ?: return
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.setBlendFunctionSeparate(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA,
            GL20.GL_ONE,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        )
        batch.color = Color.WHITE
        dialoguePortrait(0)?.let { batch.draw(it, 98.628f * .86f, 496f * .86f, 192f * .86f, 240f * .86f) }
        dialoguePanelTexture?.let { texture ->
            batch.draw(
                texture,
                319.233f * .86f,
                498.5f * .86f,
                798f * .86f,
                191f * .86f,
                0,
                0,
                texture.width,
                texture.height,
                true,
                false
            )
        }
        streetDialogueFont.color = Color.BLACK
        streetDialogueFont.draw(batch, dialogue.text, 382.487f * .86f, (587.814f + 52.92f) * .86f)
        streetSpeakerFont.color = Color.WHITE
        streetSpeakerFont.draw(batch, "조조", 403.896f * .86f, (633.52f + 54.4f) * .86f)
        batch.end()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    }

    /**
     * R_00.scene1 street-dialogue composition oracle. Source Cocos uses a
     * 1488.372×800 visible canvas; this game viewport is 1280×688, an exact
     * 0.86 transform on both axes. No source framebuffer is reused here.
     */
    private fun drawStreetDialogueIsolation(stage: String) {
        val index = ScenarioStreetDialogueStages.indexOf(stage)
        if (index < 0) return
        if (index >= ScenarioStreetDialogueStages.backgroundIndex()) {
            // The source staged oracle adds the two Hall Head nodes at the
            // final step, not the underlying animated HallUnit nodes.
            drawBattlefield(drawCharacters = index >= ScenarioStreetDialogueStages.charactersIndex(), drawUnits = false)
        }
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        ScenarioStoryRenderer.drawStreetDialogue(sceneAssets, batch, streetDialogueView(), index)
        batch.end()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
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
        /**
         * 공개 메서드 `texture`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Texture?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun texture(name: String): Texture? = hallMenuTextures[name] ?: Gdx.files
            .internal("maps/ui/hall-menu/$name.png")
            .takeIf { it.exists() }
            ?.let(::Texture)
            ?.also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                hallMenuTextures[name] = it
            }

        /**
         * 공개 메서드 `patch`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `left` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `right` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `top` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `bottom` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `NinePatch?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun patch(name: String, left: Int, right: Int, top: Int, bottom: Int): NinePatch? =
            texture(name)?.let { NinePatch(it, left, right, top, bottom) }

        // Canvas SHOW_ALL scales every authored HallMenu coordinate by .86.
        texture("panel")?.let { batch.draw(it, 0f, 0f, 1280f, 125.56f) }
        patch("inner", 3, 3, 3, 3)?.draw(batch, 0f, 0f, 1280f, 125.56f)

        val buttonCenters =
            floatArrayOf(55.107f, 143.365f, 231.846f, 320.74f, 423.317f, 511.575f, 600.056f, 690.441f, 789.44f)
        val icons = listOf("tool1", "tool2", "tool3", "tool4", "tool5", "tool6", "tool7", "tool8", "help")
        val commandButton = patch("button", 7, 8, 7, 7)

        patch("label-box", 3, 3, 3, 3)?.draw(batch, 99.72f, 4.25f, 261.44f, 37.84f)
        patch("label-mark", 1, 1, 3, 3)?.draw(batch, 101.44f, 5.97f, 258f, 34.4f)
        val showMenuLabels = !(interactive && hallOverlayFixture == "menu")
        if (showMenuLabels) {
            bodyFont.color = Color.WHITE
            glyphLayout.setText(bodyFont, playback.stage.eventName)
            bodyFont.draw(batch, glyphLayout, 230.44f - glyphLayout.width / 2f, 23.17f + glyphLayout.height / 2f)
        }
        patch("label-box", 3, 3, 3, 3)?.draw(batch, 366.95f, 4.23f, 278.64f, 37.84f)
        patch("label-mark", 1, 1, 3, 3)?.draw(batch, 368.67f, 5.95f, 275.2f, 34.4f)
        if (showMenuLabels) {
            glyphLayout.setText(bodyFont, playback.stage.stageName)
            bodyFont.draw(batch, glyphLayout, 505.67f - glyphLayout.width / 2f, 23.13f + glyphLayout.height / 2f)
        }

        val to = (if (interactive) playback.stage.ambition else playback.ambitionTo).coerceIn(0, 100)
        val from = (if (interactive) playback.stage.ambition else playback.ambitionFrom).coerceIn(0, 100)
        val (rootName, valueName) = if (interactive) "bar-blue" to "bar-red" else when {
            to < 16 -> "bar-red" to "bar-yellow"
            to > 84 -> "bar-yellow" to "bar-blue"
            else -> "bar-blue" to "bar-red"
        }
        patch(rootName, 1, 1, 1, 1)?.draw(batch, 717.4f, 16.70f, 258f, 12.9f)
        val tween = ((playback.ambitionElapsedSeconds - 1.2f) / 1f).coerceIn(0f, 1f)
        val value = if (hallOverlayFixture == "ambition") to.toFloat() else from + (to - from) * tween
        patch(valueName, 1, 1, 1, 1)?.draw(batch, 717.4f, 16.70f, 258f * value / 100f, 12.9f)

        // Three .2s hide/show cycles precede the one-second bar tween.
        val flagVisible = playback.ambitionElapsedSeconds >= 1.2f ||
                ((playback.ambitionElapsedSeconds / 0.2f).toInt() and 1) == 1
        if (!interactive && hallOverlayFixture != "ambition" && playback.ambitionIndicatorEnabled && flagVisible) {
            val decreasing = to < from
            texture(if (decreasing) "flag-right" else "flag-left")?.let { flag ->
                val centerX = if (decreasing) 1003.48f else 690.81f
                batch.draw(flag, centerX - 6.88f, 16.70f, 13.76f, 12.9f)
            }
        } else if (interactive) {
            texture("flag-left")?.let { batch.draw(it, 787.27f * .86f, 11.917f * .86f, 27.52f, 25.8f) }
            texture("flag-right")?.let { batch.draw(it, 1150.837f * .86f, 11.917f * .86f, 27.52f, 25.8f) }
        }
        buttonCenters.zip(icons).forEachIndexed { index, (sourceX, icon) ->
            val centerX = sourceX * 0.86f
            commandButton?.draw(batch, centerX - 37.84f, 52.137f * .86f, 75.68f, 75.68f)
            val iconY = if (index == icons.lastIndex) 60.137f else 60.419f
            texture(icon)?.let { batch.draw(it, centerX - 30.96f, iconY * .86f, 61.92f, 61.92f) }
        }
        batch.color = Color.WHITE
    }

    /** Source HallCommandLayer shown after the R script releases control. */
    private fun drawHallCommand() {
        HallRenderer.drawMainCommands(
            batch, HallViewState(
                menuTexture = hallCommandTexture("menu"),
                battleTexture = hallCommandTexture("battle"),
                equipTexture = hallCommandTexture("equip"),
                buyTexture = hallCommandTexture("buy"),
                sellTexture = hallCommandTexture("sell"),
            )
        )
    }

    private fun hallCommandTexture(name: String): Texture? {
        val path = "maps/ui/hall-command/$name.png"
        return hallMenuTextures[path] ?: Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)?.also {
            it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            hallMenuTextures[path] = it
        }
    }

    /** Global SaveLayer opened from the Hall menu, transformed by SHOW_ALL. */
    private fun drawHallSave() {
        /**
         * 공개 메서드 `texture`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Texture?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun texture(path: String): Texture? = hallMenuTextures[path] ?: Gdx.files.internal(path)
            .takeIf { it.exists() }
            ?.let(::Texture)
            ?.also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                hallMenuTextures[path] = it
            }

        /**
         * 공개 메서드 `start`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun start(name: String) = texture("maps/ui/start-battle/$name.png")

        /**
         * 공개 메서드 `patch`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `inset` (`Int = 3`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun patch(name: String, inset: Int = 3) = start(name)?.let { NinePatch(it, inset, inset, inset, inset) }

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `centered` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(value: String, x: Float, y: Float, w: Float, centered: Boolean = false) {
            bodyFont.color = Color.BLACK
            bodyFont.draw(
                batch, value, x * .86f, y * .86f + 35f, w * .86f,
                if (centered) Align.center else Align.left, false
            )
        }

        /**
         * 공개 메서드 `tiled`
         *
         * ### 파라미터
        - `tex` (`Texture`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun tiled(tex: Texture, x: Float, y: Float, w: Float, h: Float) {
            val px = x * .86f
            val py = y * .86f
            val width = w * .86f
            val height = h * .86f
            val tw = tex.width * .86f
            val th = tex.height * .86f
            var dy = 0f
            while (dy < height - .01f) {
                var dx = 0f
                while (dx < width - .01f) {
                    val dw = minOf(tw, width - dx)
                    val dh = minOf(th, height - dy)
                    batch.draw(
                        tex, px + dx, py + dy, dw, dh, 0, 0,
                        (dw / .86f).toInt().coerceAtLeast(1), (dh / .86f).toInt().coerceAtLeast(1), false, false
                    )
                    dx += tw
                }
                dy += th
            }
        }

        batch.color = Color.WHITE
        start("logo9")?.let { tiled(it, 278.186f, 83f, 932f, 634f) }
        patch("button", 8)?.draw(batch, 278.186f * .86f, 83f * .86f, 932f * .86f, 634f * .86f)
        start("title")?.let { batch.draw(it, 278.186f * .86f, 667f * .86f, 932f * .86f, 43f) }
        label("진행 상황 유지", 288.186f, 666.8f, 229.83f)
        label("어떤 진행 상황을 저장할지 선택해 주세요.", 286.785f, 612.805f, 654.88f)
        patch("box2")?.draw(batch, 287.186f * .86f, 172.534f * .86f, 912f * .86f, 428f * .86f)
        val rowTexture = choiceRowTexture
        hallSaveLayer.view().rows.take(8).forEachIndexed { index, row ->
            val y = 547.534f - index * 52f
            rowTexture?.let { batch.draw(it, 289.186f * .86f, y * .86f, 908f * .86f, 43f) }
            label(row.number, 295.448f, y - .2f, 117.85f)
            label(row.stage, 434.615f, y - .2f, 124.49f)
            label(row.name, 577.886f, y, 616.3f)
        }
        texture("maps/ui/title/load/vline.png")?.let { line ->
            listOf(422.057f, 566.695f).forEach { x ->
                batch.draw(
                    line,
                    x * .86f,
                    174.634f * .86f,
                    6f * .86f,
                    423.8f * .86f
                )
            }
        }
        label("따뜻한 알림: 오래된 저장 파일일수록 앞에 표시됩니다.", 131.555f, 105.399f, 850.11f)
        patch("button", 8)?.draw(batch, 1045.855f * .86f, 100.162f * .86f, 147.6f * .86f, 56f * .86f)
        label("취소", 1069.655f, 108.162f, 100f, centered = true)

        hallSaveLayer.pendingSlot()?.let { slot ->
            start("logo9")?.let { tiled(it, 426.686f, 252f, 635f, 296f) }
            patch("button", 8)?.draw(batch, 426.686f * .86f, 252f * .86f, 635f * .86f, 296f * .86f)
            texture("maps/ui/title/load/eagle.png")?.let {
                batch.draw(
                    it,
                    453.005f * .86f,
                    373.951f * .86f,
                    106f * .86f,
                    124f * .86f
                )
            }
            label(hallSaveLayer.pendingPrompt().orEmpty(), 573.686f, 335f, 463f)
            listOf(Triple(554.186f, "됐어", 557.336f), Triple(754.186f, "저장", 757.586f)).forEach { (x, value, tx) ->
                patch("button", 8)?.draw(batch, x * .86f, 271.285f * .86f, 180f * .86f, 43f)
                label(value, tx, 279.085f, if (value == "됐어") 168.1f else 169.4f, centered = true)
            }
        }
        if (hallSaveLayer.completionTipOpen()) {
            start("logo9")?.let { tiled(it, 426.686f, 252f, 635f, 296f) }
            patch("button", 8)?.draw(batch, 426.686f * .86f, 252f * .86f, 635f * .86f, 296f * .86f)
            label("저장 완료.", 573.686f, 385f, 463f, centered = true)
            patch("button", 8)?.draw(batch, 654.186f * .86f, 271.285f * .86f, 180f * .86f, 43f)
            label("확인", 657.586f, 279.085f, 169.4f, centered = true)
        }
        batch.color = Color.WHITE
    }

    /** Global126 opened by EquipLayer.button14. */
    private fun drawExclusiveLayer(layer: ExclusiveLayer) {
        val scale = .86f

        /**
         * 공개 메서드 `texture`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Texture?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun texture(name: String): Texture? {
            val path = "maps/ui/start-battle/$name.png"
            return hallMenuTextures[path] ?: Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)?.also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                hallMenuTextures[path] = it
            }
        }

        /**
         * 공개 메서드 `patch`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `inset` (`Int = 3`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `NinePatch?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun patch(name: String, inset: Int = 3): NinePatch? =
            texture(name)?.let { NinePatch(it, inset, inset, inset, inset) }

        /**
         * 공개 메서드 `tiled`
         *
         * ### 파라미터
        - `tex` (`Texture`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `height` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun tiled(tex: Texture, x: Float, y: Float, width: Float, height: Float) {
            val tw = tex.width * scale
            val th = tex.height * scale
            var dy = 0f
            while (dy < height - .01f) {
                val dh = minOf(th, height - dy)
                val sh = (dh / scale).toInt().coerceIn(1, tex.height)
                var dx = 0f
                while (dx < width - .01f) {
                    val dw = minOf(tw, width - dx)
                    val sw = (dw / scale).toInt().coerceIn(1, tex.width)
                    batch.draw(tex, x + dx, y + dy, dw, dh, 0, 0, sw, sh, false, false)
                    dx += tw
                }
                dy += th
            }
        }

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(value: String, x: Float, y: Float, width: Float) {
            bodyFont.color = Color.BLACK
            bodyFont.draw(batch, value, x * scale, (y + 43f) * scale, width * scale, Align.center, false)
        }

        /**
         * 공개 메서드 `header`
         *
         * ### 파라미터
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun header(x: Float, y: Float, width: Float, value: String) {
            patch("box4")?.draw(batch, x * scale, y * scale, width * scale, 60f * scale)
            label(value, x, y + 3f, width)
        }

        /**
         * 공개 메서드 `button`
         *
         * ### 파라미터
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `labelWidth` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun button(x: Float, value: String, labelWidth: Float) {
            patch("box3")?.draw(batch, x * scale, 54.533f * scale, 200f * scale, 54f * scale)
            label(value, x + (200f - labelWidth) / 2f, 59.533f, labelWidth)
        }

        batch.color = Color.WHITE
        texture("logo9")?.let { tiled(it, 136.186f * scale, 47f * scale, 1216f * scale, 706f * scale) }
        patch("box1")?.draw(batch, 136.186f * scale, 47f * scale, 1216f * scale, 706f * scale)
        texture("title")?.let { batch.draw(it, 136.186f * scale, 703f * scale, 1216f * scale, 50f * scale) }
        label("장비 정보", 669.431f, 702.8f, 149.51f)
        if (layer.selectedTab == ExclusiveLayer.Tab.SET_LIST) {
            patch("box4")?.draw(batch, 138.186f * scale, 117.5f * scale, 1212f * scale, 585f * scale)
            texture("vline")?.let { line ->
                listOf(371.375f, 604.197f, 840.498f).forEach { x ->
                    batch.draw(line, x * scale, 120.254f * scale, 6f * scale, 524.8f * scale)
                }
            }
            header(138.586f, 642.1f, 236f, "무기")
            header(374.586f, 642.1f, 233f, "보구")
            header(607.636f, 642.1f, 236.5f, "보조")
            header(844.036f, 642.1f, 506.1f, "특수 효과")
        } else {
            patch("box4")?.draw(batch, 140.186f * scale, 117.45f * scale, 1208f * scale, 585.7f * scale)
            texture("vline")?.let { line ->
                listOf(321.257f to 119.319f, 565.153f to 119.205f).forEach { (x, y) ->
                    batch.draw(line, x * scale, y * scale, 6f * scale, 524.8f * scale)
                }
            }
            header(140.85f, 643.3f, 185f, "소지자")
            header(324.236f, 643.3f, 243.9f, "이름")
            header(568.186f, 643.3f, 780f, "특수 효과")
        }
        button(354.241f, "전용 목록", 167f)
        button(147.282f, "세트 목록", 167f)
        button(1141.864f, "확인", 100f)
        batch.color = Color.WHITE
    }

    /** Global127 opened from UnitInfoLayer's GVar4074-gated button8. */
    private fun drawFeatsLayer(layer: FeatsLayer) {
        val scale = .86f

        /**
         * 공개 메서드 `texture`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Texture?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun texture(name: String): Texture? {
            val candidates = when (name) {
                "bg1", "box3" -> listOf("maps/ui/unit-info/$name.png", "maps/ui/win-condition/$name.png")
                "vline" -> listOf("maps/ui/terrain-layer/vline.png", "maps/ui/title/load/vline.png")
                "logo3" -> listOf("maps/ui/win-condition/logo3.png")
                else -> listOf("maps/ui/start-battle/$name.png")
            }
            return candidates.firstNotNullOfOrNull { path ->
                hallMenuTextures[path] ?: Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)?.also {
                    it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                    hallMenuTextures[path] = it
                }
            }
        }

        /**
         * 공개 메서드 `patch`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `inset` (`Int = 3`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `NinePatch?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun patch(name: String, inset: Int = 3): NinePatch? =
            (texture(name) ?: when (name) {
                "box4" -> texture("box1"); "mark5" -> texture("button"); else -> null
            })
                ?.let { NinePatch(it, inset, inset, inset, inset) }

        /**
         * 공개 메서드 `tiled`
         *
         * ### 파라미터
        - `tex` (`Texture`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `height` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun tiled(tex: Texture, x: Float, y: Float, width: Float, height: Float) {
            val tw = tex.width * scale
            val th = tex.height * scale
            var dy = 0f
            while (dy < height - .01f) {
                val dh = minOf(th, height - dy)
                val sh = (dh / scale).toInt().coerceIn(1, tex.height)
                var dx = 0f
                while (dx < width - .01f) {
                    val dw = minOf(tw, width - dx)
                    val sw = (dw / scale).toInt().coerceIn(1, tex.width)
                    batch.draw(tex, x + dx, y + dy, dw, dh, 0, 0, sw, sh, false, false)
                    dx += tw
                }
                dy += th
            }
        }

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `align` (`Int = Align.center`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `wrap` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(value: String, x: Float, y: Float, width: Float, align: Int = Align.center, wrap: Boolean = false) {
            bodyFont.color = Color.BLACK
            bodyFont.draw(batch, value, x * scale, (y + 42f) * scale, width * scale, align, wrap)
        }
        batch.color = Color.WHITE
        texture("logo9")?.let { tiled(it, 267.686f * scale, 83.5f * scale, 953f * scale, 633f * scale) }
        patch("box4")?.draw(batch, 267.686f * scale, 83.5f * scale, 953f * scale, 633f * scale)
        texture("bg1")?.let { batch.draw(it, 267.686f * scale, 656.5f * scale, 953f * scale, 60f * scale) }
        patch("box3")?.draw(batch, 267.686f * scale, 656.5f * scale, 953f * scale, 60f * scale)
        label("공훈", 669.686f, 662.3f, 71.2f)
        texture("logo9")?.let { tiled(it, 277.686f * scale, 158.45f * scale, 933f * scale, 442.7f * scale) }
        texture("box2")?.let { tiled(it, 277.686f * scale, 158.45f * scale, 933f * scale, 442.7f * scale) }
        layer.view().rows.forEachIndexed { index, row ->
            val rowY = 529.15f - index * 74f
            patch("box2")?.draw(batch, 279.686f * scale, rowY * scale, 929f * scale, 70f * scale)
            label(
                row.title, if (row.title == "민첩성") 290.286f else 307.586f, rowY + 9.8f,
                if (row.title == "민첩성") 107.8f else 73.2f
            )
            label(row.ability.toString(), 462.941f, rowY + 9.8f, 48.49f)
            label(row.phaseLabel, 1086.816f, rowY + 9.8f, 70.74f)
            patch("bg1")?.draw(batch, 572.186f * scale, (rowY + 20f) * scale, 446f * scale, 30f * scale)
            patch("box2")?.draw(batch, 574.186f * scale, (rowY + 20f) * scale, 442f * scale, 30f * scale)
            patch("mark5")?.draw(
                batch,
                574.186f * scale,
                (rowY + 22f) * scale,
                442f * row.progressRatio * scale,
                26f * scale
            )
            label(row.progressLabel, 743.136f, rowY + 18.454f, 104.1f)
        }
        texture("vline")?.let { line ->
            listOf(410.859f, 555.31f, 1027.419f).forEach { x ->
                batch.draw(line, x * scale, 160.25f * scale, 6f * scale, 450.3f * scale)
            }
        }
        /**
         * 공개 메서드 `header`
         *
         * ### 파라미터
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `lx` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `lw` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun header(x: Float, y: Float, w: Float, h: Float, value: String, lx: Float, lw: Float) {
            patch("bg1")?.draw(batch, x * scale, y * scale, w * scale, h * scale)
            patch("box3")?.draw(batch, x * scale, y * scale, w * scale, h * scale)
            label(value, lx, 607.081f, lw)
        }
        header(272.836f, 601.45f, 142.7f, 55.1f, "능력 이름", 269.431f, 149.51f)
        header(415.436f, 601.45f, 143.5f, 55.1f, "능력치", 435.286f, 103.8f)
        header(559.136f, 601.5f, 472.1f, 55f, "현재/업그레이드 필요 공훈", 588.216f, 413.94f)
        header(1030.886f, 601.45f, 182.6f, 55.1f, "상위 단계로 승급하는 데 필요함", 875.061f, 494.25f)
        patch("box3")?.draw(batch, 1059.386f * scale, 96f * scale, 147.6f * scale, 56f * scale)
        label("확인", 1083.186f, 104f, 100f)
        patch("box3")?.draw(batch, 904.386f * scale, 96f * scale, 147.6f * scale, 56f * scale)
        label("설명", 928.186f, 104f, 100f)
        if (hallFeatsHelpOpen) {
            texture("logo9")?.let { tiled(it, 426.686f * scale, 252f * scale, 635f * scale, 296f * scale) }
            patch("box3")?.draw(batch, 426.686f * scale, 252f * scale, 635f * scale, 296f * scale)
            texture("logo3")?.let { batch.draw(it, 453.005f * scale, 373.951f * scale, 106f * scale, 124f * scale) }
            label(FeatsLayer.HELP_TEXT, 573.686f, 335f, 463f, Align.left, true)
            patch("box3")?.draw(batch, 654.186f * scale, 271.285f * scale, 180f * scale, 50f * scale)
            label("예", 657.586f, 279.085f, 169.4f)
        }
        batch.color = Color.WHITE
    }

    private fun drawMagicLayer(layer: MagicInfoLayer) {
        HallMagicRenderer.draw(sceneAssets, batch, HallMagicView.from(layer.magic))
    }

    /** EquipLayer / BuyLayer / SellLayer shown by HallCommandLayer buttons 1..3. */
    private fun drawHallManagement(kind: HallManagement) {
        if (kind == HallManagement.SELL) {
            HallSellRenderer.draw(sceneAssets, batch, hallSellView())
            return
        }
        /**
         * 공개 메서드 `texture`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Texture?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun texture(name: String): Texture? {
            val path = "maps/ui/start-battle/$name.png"
            return hallMenuTextures[path] ?: Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)?.also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                hallMenuTextures[path] = it
            }
        }

        /**
         * 공개 메서드 `patch`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `inset` (`Int = 3`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `NinePatch?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun patch(name: String, inset: Int = 3): NinePatch? =
            texture(name)?.let { NinePatch(it, inset, inset, inset, inset) }

        /**
         * 공개 메서드 `tiled`
         *
         * ### 파라미터
        - `tex` (`Texture`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `height` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun tiled(tex: Texture, x: Float, y: Float, width: Float, height: Float) {
            val tw = tex.width * .86f
            val th = tex.height * .86f
            var dy = 0f
            while (dy < height - .01f) {
                val dh = minOf(th, height - dy)
                val sh = (dh / .86f).toInt().coerceIn(1, tex.height)
                var dx = 0f
                while (dx < width - .01f) {
                    val dw = minOf(tw, width - dx)
                    val sw = (dw / .86f).toInt().coerceIn(1, tex.width)
                    batch.draw(tex, x + dx, y + dy, dw, dh, 0, 0, sw, sh, false, false)
                    dx += tw
                }
                dy += th
            }
        }

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float = 240f`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `centered` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(text: String, x: Float, y: Float, width: Float = 240f, centered: Boolean = false) {
            bodyFont.color = Color.BLACK
            bodyFont.draw(batch, text, x, y, width, if (centered) Align.center else Align.left, false)
        }

        /**
         * 공개 메서드 `button`
         *
         * ### 파라미터
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun button(text: String, x: Float, y: Float, width: Float) {
            patch("button", 9)?.draw(batch, x, y, width, 43f)
            label(text, x, y + 31f, width, centered = true)
        }

        /**
         * 공개 메서드 `panel`
         *
         * ### 파라미터
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `height` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun panel(x: Float, y: Float, width: Float, height: Float) {
            patch("box1")?.draw(batch, x, y, width, height)
        }

        /**
         * 공개 메서드 `clipped`
         *
         * ### 파라미터
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `height` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `draw` (`(`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun clipped(x: Float, y: Float, width: Float, height: Float, draw: () -> Unit) {
            val scissors = Rectangle()
            ScissorStack.calculateScissors(
                viewport.camera,
                batch.transformMatrix,
                Rectangle(x, y, width, height),
                scissors
            )
            batch.flush()
            if (ScissorStack.pushScissors(scissors)) {
                draw()
                batch.flush()
                ScissorStack.popScissors()
            }
        }

        /**
         * 공개 메서드 `itemIcon`
         *
         * ### 파라미터
        - `itemId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Texture?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun itemIcon(itemId: Int): Texture? {
            val icon = gameDataCatalog.equipmentProfile(itemId)?.icon ?: return null
            val path = "maps/item-icons/$icon.png"
            return hallMenuTextures[path] ?: Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)?.also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                hallMenuTextures[path] = it
            }
        }

        val unitId = if (kind == HallManagement.EQUIP) hallEquipUnitId() else campaign.joinedUnits.firstOrNull() ?: 0
        val unit = gameDataCatalog.unitProfile(unitId) ?: gameDataCatalog.unitProfile(0)
        val zeroBasedLevel = (campaign.unitAttribute(unitId, 18, unit?.level ?: 1) - 1).coerceAtLeast(0)
        val profile = unit?.let {
            gameDataCatalog.battleProfile(
                it.id,
                zeroBasedLevel,
                campaign.unitAttribute(it.id, 17, it.posts)
            )
        }

        /**
         * 공개 메서드 `drawUnitSummary`
         *
         * ### 파라미터
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `showEquipment` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun drawUnitSummary() {
            val bonus = campaign.inventory.equipment[unitId]
                ?.let { gameDataCatalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1) }
                ?: GameDataCatalog.EquipmentBonus()
            // EquipLayer's UnitInfoBase uses absolute authored positions;
                // its ScrollView then clips the equipment cards at the lower
                // boundary. Keep those coordinates instead of reflowing the
                // panel as a conventional game-side card list.
                dialoguePortrait(unitId)?.let { batch.draw(it, 769.54f, 355.47f, 165.12f, 206.4f) }
                patch("box2")?.draw(batch, 748.90f, 357.19f, 206.4f, 202.96f)
                label(campaign.unitNames[unitId] ?: unit?.name ?: "조조", 965.08f, 551.88f, 59.51f)
                label(if (unitId == 0) "군웅" else profile?.arm?.name ?: "군웅", 965.08f, 508.88f, 80f)
                label("Exp", 965.08f, 422.02f, 59.28f)
                patch("box2")?.draw(batch, 1029.58f, 387.79f, 115.24f, 20.64f)
                label("0/100", 1044.16f, 423.20f, 86.09f, true)
                label("Lv", 964.97f, 465.02f, 36.34f)
                label((profile?.level ?: 1).toString(), 1019.15f, 465.07f, 19.14f, true)
                val names = listOf("HP", "MP", "공격력", "정신력", "방어력", "폭발력", "사기", "이동력")
                val values = listOf(
                    profile?.maxHitPoints ?: 0, profile?.maxMagicPoints ?: 0,
                    (profile?.attack ?: 0) + bonus.attack, (profile?.spirit ?: 0) + bonus.spirit,
                    (profile?.defense ?: 0) + bonus.defense, profile?.critical ?: 0,
                    profile?.morale ?: 0, profile?.movement ?: 0,
                )
                val namePos = listOf(
                    755.42f to 343.76f, 968.52f to 343.76f,
                    762.21f to 293.02f, 975.49f to 293.02f,
                    762.21f to 241.42f, 975.49f to 241.42f,
                    759.88f to 190.68f, 975.49f to 190.68f,
                )
                val boxPos = listOf(
                    867.04f to 309.53f, 1081.18f to 309.53f,
                    867.04f to 258.79f, 1081.18f to 258.79f,
                    867.04f to 207.19f, 1081.18f to 207.19f,
                    867.04f to 156.45f, 1081.18f to 156.45f,
                )
                names.forEachIndexed { index, name ->
                    val (lx, ly) = namePos[index]
                    label(name, lx, ly, if (name.length <= 2) 59.51f else 89.27f)
                    val (bx, by) = boxPos[index]
                    patch("box2")?.draw(batch, bx, by, 68.8f, 43f)
                    label(values[index].toString(), bx, by + 34.23f, 68.8f, true)
                }
                val equipment = campaign.inventory.equippedItems().filter { it.unitId == unitId }
                val slots = listOf(
                    Triple(
                        "무기:",
                        equipment.firstOrNull { gameDataCatalog.equipmentProfile(it.itemId)?.itemType?.let { t -> t < 20 } == true },
                        20.97f
                    ),
                    Triple(
                        "보구: ",
                        equipment.firstOrNull { gameDataCatalog.equipmentProfile(it.itemId)?.itemType?.let { t -> t in 20..25 } == true },
                        -114.91f
                    ),
                    Triple(
                        "보조: ",
                        equipment.firstOrNull { gameDataCatalog.equipmentProfile(it.itemId)?.itemType?.let { t -> t > 25 } == true },
                        -250.79f
                    ),
                )
                slots.forEachIndexed { index, (slotName, equipped, sy) ->
                    if (index == 2 && equipped == null) return@forEachIndexed
                    val item = equipped?.let { gameDataCatalog.equipmentProfile(it.itemId) }
                    panel(745.74f, sy, 402.57f, 129f)
                    label(slotName, if (index == 0) 901.05f else 894f, sy + 118.42f, 76f)
                    label(item?.name ?: "없음", 966.8f, sy + 118.68f, 177.16f)
                    panel(752.32f, sy + 8.04f, 115.91f, 116.19f)
                    item?.let { itemIcon(it.id) }?.let { batch.draw(it, 755.24f, sy + 11.10f, 110.08f, 110.08f) }
                    if (index == 0) {
                        label("Lv", 875.64f, sy + 78.86f, 36.34f)
                        label((equipped?.level ?: 1).toString(), 933.26f, sy + 78.86f, 19.14f)
                        label("Exp", 875.64f, sy + 39.30f, 59.28f)
                        patch("box2")?.draw(batch, 949.60f, sy + 4.22f, 175.44f, 20.64f)
                        label("${equipped?.experience ?: 0}/100", 994.28f, sy + 39.63f, 86.09f, true)
                    }
                }
        }
        val (rootX, rootY, rootW, rootH, title) = when (kind) {
            HallManagement.EQUIP -> listOf(118.84f, 28.81f, 1042.32f, 630.38f, 0f)
            HallManagement.BUY -> listOf(168.72f, 28.81f, 943.42f, 630.38f, 1f)
            HallManagement.SELL -> error("sell is rendered before the shared management scaffold")
        }.let { values -> arrayOf(values[0], values[1], values[2], values[3], values[4]) }
        val titleText = when (title.toInt()) {
            0 -> "장비"; 1 -> "매입"; else -> "판매하기"
        }
        batch.color = Color.WHITE
        texture("logo9")?.let { tiled(it, rootX, rootY, rootW, rootH) }
        patch(if (kind == HallManagement.EQUIP) "button" else "box1", if (kind == HallManagement.EQUIP) 9 else 3)
            ?.draw(batch, rootX, rootY, rootW, rootH)
        patch("title", 5)?.draw(batch, rootX, rootY + rootH - 43f, rootW, 43f)
        titleFont.color = Color.BLACK
        glyphLayout.setText(titleFont, titleText)
        titleFont.draw(batch, glyphLayout, rootX + (rootW - glyphLayout.width) / 2f, rootY + rootH - 5f)

        when (kind) {
            HallManagement.EQUIP -> {
                val splitX = 739.76f
                // Source traversal paints the footer controls before the
                // content nodes. This matters at their clipped boundaries.
                button("이전 무장", 842.53f, 37.84f, 152.22f)
                button("다음 무장", 994.75f, 37.84f, 152.22f)
                button("종료", 643.73f, 37.84f, 83.42f)
                button("모두 해제", 493.37f, 37.84f, 148.95f)
                listOf("전부", "무기", "보구", "보조").forEachIndexed { i, text ->
                    button(text, 124f + i * 129f, 566.74f, 129f)
                    if (hallEquipTab.ordinal == i) {
                        batch.color = Color(0f, 0f, 0f, .10f)
                        texture("box2")?.let { batch.draw(it, 128f + i * 129f, 570.74f, 121f, 35f) }
                        batch.color = Color.WHITE
                        label(text, 124f + i * 129f, 597.74f, 129f, centered = true)
                    }
                }
                button("정보", 125.35f, 37.84f, 85.74f)
                panel(124.26f, 85.96f, 604.92f, 481.69f)
                patch("box2")?.draw(batch, 124.26f, 85.96f, 604.92f, 481.69f)
                texture("box2")?.let { batch.draw(it, 730.56f, 33.84f, 5.16f, 582.31f) }
                patch("button", 9)?.draw(batch, 794.8f, 565.88f, 309.6f, 48.16f)
                texture("box2")?.let { batch.draw(it, 947.02f, 571.17f, 5.16f, 41.02f) }
                label(campaign.unitNames[unitId] ?: unit?.name ?: "조조", 842.32f, 604f, 59.51f, true)
                label(if (unitId == 0) "군웅" else profile?.arm?.name ?: "군웅", 998.89f, 604f, 59.51f, true)
                hallEquipInventory().take(6).forEachIndexed { index, (itemId, count) ->
                    val item = gameDataCatalog.equipmentProfile(itemId) ?: return@forEachIndexed
                    val iy = 515f - index * 68f
                    panel(132f, iy - 48f, 582f, 62f)
                    itemIcon(itemId)?.let { batch.draw(it, 141f, iy - 40f, 52f, 52f) }
                    label(item.name + if (count > 1) "  ×$count" else "", 207f, iy + 8f, 260f)
                    label(gameDataCatalog.equipmentTypeName(item.itemType), 430f, iy + 8f, 135f)
                    val level = campaign.inventory.itemLevels(itemId).firstOrNull() ?: 1
                    val exp = campaign.inventory.itemExperiences(itemId).firstOrNull() ?: 0
                    val limit = gameDataCatalog.equipmentExperienceLimit(itemId, level)
                    label(level.toString(), 568f, iy + 8f, 54f, true)
                    label(if (exp >= limit) "MAX" else exp.toString(), 626f, iy + 8f, 72f, true)
                }
                // UnitInfoBase's equipment ScrollView is masked by the right
                // content panel; without this the first card painted over the
                // previous/next buttons at the bottom.
                clipped(splitX, 89.44f, 414.52f, 474.72f) {
                    drawUnitSummary()
                }
            }

            HallManagement.BUY -> {
                val splitX = 673.77f
                HallBuyCatalogRenderer.draw(sceneAssets, batch, hallBuyCatalogView())
                panel(splitX, 89.44f, 414.52f, 474.72f)
                HallBuyUnitSummaryRenderer.draw(sceneAssets, batch, hallBuyUnitSummaryView())
                label("현금", rootX + 22f, rootY + 23f); label(
                    campaign.money.toString(),
                    rootX + 170f,
                    rootY + 23f,
                    140f,
                    true
                )
                button("종료", 530.78f, 36.98f, 120.4f)
                button("이전 무장", 678.70f, 36.98f, 146.2f)
                button("다음 무장", 838.66f, 36.98f, 146.2f)
            }

            HallManagement.SELL -> error("sell is rendered before the shared management scaffold")
        }
        hallManagementNotice?.let { notice ->
            bodyFont.color = Color(0.55f, 0.05f, 0.05f, 1f)
            bodyFont.draw(batch, notice, rootX + 18f, rootY + rootH - 52f, rootW - 36f, Align.right, false)
        }
        if (kind == HallManagement.EQUIP && hallEquipUnequipConfirmation) {
            val x = 421f
            val y = 275f
            val width = 438f
            val height = 139f
            patch("box1")?.draw(batch, x, y, width, height)
            patch("title", 5)?.draw(batch, x, y + height - 43f, width, 43f)
            label("확인", x, y + height - 11f, width, centered = true)
            label("모두에게 장비를 해제하도록 확정하시겠습니까?", x + 14f, y + 86f, width - 28f, centered = true)
            button("예", x + 18f, y + 16f, 184f)
            button("비", x + 236f, y + 16f, 184f)
        } else if (kind == HallManagement.EQUIP && hallUnitListLayer != null) {
            HallUnitRosterRenderer.draw(sceneAssets, batch, hallUnitRosterView(requireNotNull(hallUnitListLayer)))
        }
        if (kind == HallManagement.EQUIP) hallEquipConfirmation?.let(::drawEquipConfirmation)
        batch.color = Color.WHITE
    }

    private fun hallUnitRosterView(unitList: HallUnitListLayer): HallUnitRosterView = HallUnitRosterView(
        unitList.rows.take(6).map { id ->
            val unit = gameDataCatalog.unitProfile(id)
            HallUnitRosterRowView(
                name = campaign.unitNames[id] ?: if (id == 181) "병사 " else unit?.name ?: "무장",
                postName = gameDataCatalog.postsName(campaign.unitAttribute(id, 17, unit?.posts ?: 0)),
            )
        },
    )

    /** Source Hall/scene/EquipConfirmLayer, transformed from 1488.372x800 by .86. */
    private fun drawEquipConfirmation(confirmation: EquipConfirmation) {
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
        if (kind == HallInfo.FORCES) {
            HallForcesRenderer.draw(sceneAssets, batch, hallForcesView())
            return
        }
        if (kind == HallInfo.TERRAIN) {
            HallTerrainRenderer.draw(
                sceneAssets,
                batch,
                HallTerrainView.from(
                    hallTerrainTab,
                    gameDataCatalog.terrainLayer().select(hallTerrainTab).rows,
                ),
            )
            return
        }
        if (kind == HallInfo.TREASURE) {
            val treasures = gameDataCatalog.treasureProfiles()
            val discovered = campaign.inventory.discoveredTreasures
            HallTreasureRenderer.draw(
                sceneAssets,
                batch,
                HallTreasureView(
                    entries = treasures.take(6).map { item ->
                        HallTreasureEntryView(item.name, item.icon, item.id in discovered)
                    },
                    discoveredCount = discovered.size,
                    totalCount = treasures.size,
                ),
            )
            return
        }
        /**
         * 공개 메서드 `texture`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Texture?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun texture(path: String): Texture? =
            hallMenuTextures[path] ?: Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)?.also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                hallMenuTextures[path] = it
            }

        /**
         * 공개 메서드 `ui`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun ui(name: String) = texture("maps/ui/start-battle/$name.png")

        /**
         * 공개 메서드 `patch`
         *
         * ### 파라미터
        - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `inset` (`Int = 3`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun patch(name: String, inset: Int = 3) = ui(name)?.let { NinePatch(it, inset, inset, inset, inset) }

        /**
         * 공개 메서드 `tiled`
         *
         * ### 파라미터
        - `tex` (`Texture`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `height` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun tiled(tex: Texture, x: Float, y: Float, width: Float, height: Float) {
            val tw = tex.width * .86f
            val th = tex.height * .86f
            var dy = 0f
            while (dy < height - .01f) {
                val dh = minOf(th, height - dy)
                val sh = (dh / .86f).toInt().coerceIn(1, tex.height)
                var dx = 0f
                while (dx < width - .01f) {
                    val dw = minOf(tw, width - dx)
                    val sw = (dw / .86f).toInt().coerceIn(1, tex.width)
                    batch.draw(tex, x + dx, y + dy, dw, dh, 0, 0, sw, sh, false, false); dx += tw
                }
                dy += th
            }
        }

        /**
         * 공개 메서드 `text`
         *
         * ### 파라미터
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `center` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `small` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun text(value: String, x: Float, y: Float, width: Float, center: Boolean = false, small: Boolean = false) {
            val font = if (small) smallUiFont else bodyFont
            font.color = Color.BLACK
            font.draw(batch, value, x, y, width, if (center) Align.center else Align.left, false)
        }

        /**
         * 공개 메서드 `cell`
         *
         * ### 파라미터
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `height` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun cell(x: Float, y: Float, width: Float, height: Float) = patch("box2")?.draw(batch, x, y, width, height)

        /**
         * 공개 메서드 `button`
         *
         * ### 파라미터
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `width` (`Float = 130f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun button(value: String, x: Float, y: Float, width: Float = 130f) {
            patch("button", 9)?.draw(batch, x, y, width, 51.6f); text(value, x, y + 35f, width, center = true)
        }

        val geometry = when (kind) {
            HallInfo.FORCES -> floatArrayOf(142.49f, 68.37f, 995.02f, 551.26f)
            HallInfo.PROPERTY -> floatArrayOf(212.42f, 40.42f, 854.84f, 607.16f)
            HallInfo.HELPER -> floatArrayOf(127f, 21.07f, 1025.98f, 645.86f)
            HallInfo.TERRAIN -> error("terrain is rendered before the shared hall-info scaffold")
            HallInfo.TREASURE -> error("treasure is rendered before the shared hall-info scaffold")
        }
        val (x, y, w, h) = geometry
        batch.color = Color.WHITE
        ui("logo9")?.let { tiled(it, x, y, w, h) }
        patch("box1")?.draw(batch, x, y, w, h)

        when (kind) {
            HallInfo.FORCES -> error("forces is rendered before the shared hall-info scaffold")
            HallInfo.PROPERTY -> {
                patch("title", 5)?.draw(batch, x, y + h - 51.6f, w, 51.6f)
                titleFont.color = Color.BLACK; glyphLayout.setText(titleFont, "창고 일람")
                titleFont.draw(batch, glyphLayout, x + (w - glyphLayout.width) / 2f, y + h - 8f)
                val widths = floatArrayOf(323.06f, 168.13f, 91.24f, 87.69f, 176.44f)
                val headers = listOf("이름", "속성", "레벨", "경험치", "소지자")
                var cx = x + 5f
                headers.forEachIndexed { index, header ->
                    cell(cx, y + h - 101f, widths[index], 48f); text(
                    header,
                    cx,
                    y + h - 67f,
                    widths[index],
                    true
                ); cx += widths[index]
                }
                campaign.joinedUnits.forEach { campaign.inventory.ensureDefaultEquipment(it, gameDataCatalog) }
                /**
                 * data class  `PropertyRow`
                 *
                 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
                 *
                 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
                 */

                data class PropertyRow(
                    val id: Int,
                    val count: Int,
                    val level: String,
                    val experience: String,
                    val owner: String
                )

                /**
                 * 공개 메서드 `accepts`
                 *
                 * ### 파라미터
                - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Boolean`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun accepts(id: Int): Boolean {
                    val item = gameDataCatalog.equipmentProfile(id) ?: return false
                    return when (hallPropertyTab) {
                        HallPropertyTab.WEAPON -> item.itemType < 20
                        HallPropertyTab.ARMOR -> item.itemType in 20..25
                        HallPropertyTab.AUXILIARY -> item.itemType > 45 && id < 150
                        HallPropertyTab.PROPERTY -> id >= 150 || item.itemType in 26..45
                    }
                }

                val equippedRows =
                    if (hallPropertyTab == HallPropertyTab.PROPERTY) emptyList() else campaign.inventory.equippedItems()
                        .filter { accepts(it.itemId) }
                val rows = equippedRows.sortedWith(compareBy<CampaignEquippedItem> { it.itemId }.thenBy { it.unitId })
                    .map { equipped ->
                        val owner = campaign.unitNames[equipped.unitId]
                            ?: gameDataCatalog.unitProfile(equipped.unitId)?.name.orEmpty()
                        PropertyRow(
                            equipped.itemId,
                            1,
                            if (hallPropertyTab == HallPropertyTab.AUXILIARY) "---" else equipped.level.toString(),
                            if (hallPropertyTab == HallPropertyTab.AUXILIARY) "---" else equipped.experience.toString(),
                            owner
                        )
                    } + campaign.inventory.items.entries.sortedBy { it.key }.filter { (id, _) -> accepts(id) }
                    .map { (id, count) ->
                        PropertyRow(
                            id,
                            count,
                            if (hallPropertyTab == HallPropertyTab.AUXILIARY || hallPropertyTab == HallPropertyTab.PROPERTY) "---" else (campaign.inventory.itemLevels(
                                id
                            ).firstOrNull() ?: 1).toString(),
                            if (hallPropertyTab == HallPropertyTab.AUXILIARY || hallPropertyTab == HallPropertyTab.PROPERTY) "---" else (campaign.inventory.itemExperiences(
                                id
                            ).firstOrNull() ?: 0).toString(),
                            "창고"
                        )
                    }
                rows.take(7).forEachIndexed { row, entry ->
                    val item = gameDataCatalog.equipmentProfile(entry.id) ?: return@forEachIndexed
                    cx = x + 5f
                    val ry = y + h - 166f - row * 67.08f
                    val values = listOf(
                        "${item.name}${if (entry.count > 1) " ×${entry.count}" else ""}",
                        gameDataCatalog.equipmentTypeName(item.itemType),
                        entry.level,
                        entry.experience,
                        entry.owner
                    )
                    values.forEachIndexed { index, value ->
                        cell(cx, ry, widths[index], 65.36f)
                        if (index == 0) {
                            texture("maps/item-icons/${item.icon}.png")?.let {
                                batch.draw(
                                    it,
                                    cx + 6f,
                                    ry + 7f,
                                    50f,
                                    50f
                                )
                            }
                            text(value, cx + 63f, ry + 43f, widths[index] - 69f)
                        } else text(value, cx + 4f, ry + 43f, widths[index] - 8f, true)
                        cx += widths[index]
                    }
                }
                val radioOn = texture("maps/ui/title/setting/radio-on.png")
                val radioOff = texture("maps/ui/title/setting/radio-off.png")
                listOf("무기", "방어구", "보조", "아이템").forEachIndexed { index, value ->
                    val centerX = 244.23f + index * 127.28f
                    (if (hallPropertyTab.ordinal == index) radioOn else radioOff)?.let {
                        batch.draw(it, centerX - 13.76f, 58.12f, 27.52f, 27.52f)
                    }
                    text(value, centerX + 29.7f, 88f, 95f)
                }
                button("확인", x + w - 135f, y + 5f, 125f)
            }

            HallInfo.HELPER -> {
                patch("title", 5)?.draw(batch, x, y + h - 62f, w, 62f)
                titleFont.color = Color(0.65f, 0f, 0.68f, 1f); titleFont.draw(batch, "역사 정보", x + 8f, y + h - 12f)
                cell(x + 12f, y + 82f, w - 24f, h - 151f)
                val help =
                    "6 [단축키 설명]\n☆ 일부 단축키 기능은 메뉴 — 설정을 통해 직접 설정할 수 있습니다.\n☆ 번호 0-4: 단계 속도 변화. 0가 원래 속도이며, 1-4가 가속.\n☆ 번호 5: 진영에 따라 다른 색상의 체력 바를 표시합니다.\n☆ 번호 6: 문자 BUFF와 DEBUFF를 표시합니다.\n☆ 번호 7: 왼쪽 하단에 캐릭터 능력과 장비를 표시합니다.\n☆ 숫자 8: 더블 히트의 치명타 확률과 카운터 관계를 표시합니다.\n☆ 번호 9: 지형 적응 및 이동 비용을 표시합니다.\n☆ 문자 A: 턴 시작 시 자동으로 저장됩니다.\n☆ 문자 B: 속성 인터페이스는 모든 가능한 전략과 학습 수준을 표시합니다."
                bodyFont.color = Color.BLACK; bodyFont.draw(
                    batch,
                    help,
                    x + 18f,
                    y + h - 90f,
                    w - 36f,
                    Align.left,
                    true
                )
                button("확인", x + w - 145f, y + 7f, 135f)
            }

            HallInfo.TERRAIN -> error("terrain is rendered before the shared hall-info scaffold")
            HallInfo.TREASURE -> error("treasure is rendered before the shared hall-info scaffold")
        }
        batch.color = Color.WHITE
    }

    private fun handleHallInfoTap(kind: HallInfo, x: Float, y: Float) {
        val close = when (kind) {
            HallInfo.FORCES -> x in 973.5f..1128.5f && y in 73f..125f
            HallInfo.PROPERTY -> x in 932f..1058f && y in 45f..98f
            HallInfo.TERRAIN -> x in 979f..1105f && y in 91f..145f
            HallInfo.TREASURE -> x in 912f..1048f && y in 77f..132f
            HallInfo.HELPER -> x in 1008f..1144f && y in 27f..82f
        }
        if (close) {
            hallInfo = null
            return
        }
        when (kind) {
            HallInfo.FORCES -> if (x in 147f..1134f) {
                val row = (0 until 7).firstOrNull { index ->
                    val rowY = 469.63f - index * 53.32f
                    y in rowY..(rowY + 53.32f)
                }
                row?.let { hallEquipUnitIds().sorted().getOrNull(it) }?.let(::openHallUnitInfo)
            }

            HallInfo.PROPERTY -> if (y in 40f..100f) {
                val tab = ((x - 226f) / 150f).toInt()
                HallPropertyTab.entries.getOrNull(tab)?.let { hallPropertyTab = it }
            } else if (x in 217f..1062f) {
                val row = (0 until 7).firstOrNull { index ->
                    val rowY = 481.58f - index * 67.08f
                    y in rowY..(rowY + 65.36f)
                }
                row?.let { hallPropertyItemIds().getOrNull(it) }?.let { itemId ->
                    val level =
                        if (hallPropertyTab >= HallPropertyTab.AUXILIARY) "---" else (campaign.inventory.itemLevels(
                            itemId
                        ).firstOrNull() ?: 1).toString()
                    val exp =
                        if (hallPropertyTab >= HallPropertyTab.AUXILIARY) 0 else campaign.inventory.itemExperiences(
                            itemId
                        ).firstOrNull() ?: 0
                    openHallItem(
                        itemId,
                        level,
                        exp,
                        canDrop = campaign.inventory.items[itemId]?.let { it > 0 } == true && gameDataCatalog.equipmentCategory(
                            requireNotNull(gameDataCatalog.equipmentProfile(itemId))
                        ) != 3)
                }
            }

            HallInfo.TERRAIN -> if (y in 91f..145f) {
                when {
                    x in 246f..411f -> hallTerrainTab = TerrainLayer.Tab.RISE
                    x in 420f..620f -> hallTerrainTab = TerrainLayer.Tab.EXPEND
                }
            }

            HallInfo.TREASURE -> {
                gameDataCatalog.treasureProfiles().take(6).forEachIndexed { index, item ->
                    val cx = 232.10f + index % 2 * 410.22f
                    val cy = 413.23f - index / 2 * 165.98f
                    if (x in cx..(cx + 405.06f) && y in cy..(cy + 163.40f) && item.id in campaign.inventory.discoveredTreasures) {
                        openHallItem(item.id, "1", 0, canDrop = false)
                        return
                    }
                }
            }

            else -> Unit
        }
    }

    private fun hallPropertyItemIds(): List<Int> {
        /**
         * 공개 메서드 `accepts`
         *
         * ### 파라미터
        - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Boolean`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun accepts(id: Int): Boolean {
            val item = gameDataCatalog.equipmentProfile(id) ?: return false
            return when (hallPropertyTab) {
                HallPropertyTab.WEAPON -> item.itemType < 20
                HallPropertyTab.ARMOR -> item.itemType in 20..25
                HallPropertyTab.AUXILIARY -> item.itemType > 45 && id < 150
                HallPropertyTab.PROPERTY -> id >= 150 || item.itemType in 26..45
            }
        }

        val equipped =
            if (hallPropertyTab == HallPropertyTab.PROPERTY) emptyList() else campaign.inventory.equippedItems()
                .filter { accepts(it.itemId) }
                .sortedWith(compareBy<CampaignEquippedItem> { it.itemId }.thenBy { it.unitId }).map { it.itemId }
        return equipped + campaign.inventory.items.entries.sortedBy { it.key }.filter { accepts(it.key) }.map { it.key }
    }

    private fun openHallItem(itemId: Int, level: String, experience: Int, canDrop: Boolean) {
        val profile = gameDataCatalog.equipmentProfile(itemId) ?: return
        hallItemDetail = HallItemDetail(
            itemId,
            level,
            experience,
            gameDataCatalog.equipmentExperienceLimit(itemId, level.toIntOrNull() ?: 1)
        )
        hallItemLayer = ItemLayer(itemId, profile.name, canDrop, object : ItemLayer.Repository {
            override fun discard(itemId: Int): Boolean = campaign.inventory.consumeItem(itemId)
        })
    }

    private fun handleHallItemTap(x: Float, y: Float) {
        val layer = hallItemLayer ?: return
        val sx = x / .86f
        val sy = y / .86f
        if (layer.discardConfirmationOpen) {
            when {
                sx in 554.186f..734.186f && sy in 271.285f..321.285f -> layer.onDiscardAnswer(1)
                sx in 754.186f..934.186f && sy in 271.285f..321.285f -> layer.onDiscardAnswer(0)
            }
        } else when {
            sx in 1065.827f..1215.827f && sy in 97.824f..147.824f -> layer.onButton(0, ItemLayer.TOUCH_END)
            sx in 901.312f..1051.312f && sy in 97.824f..147.824f -> layer.onButton(1, ItemLayer.TOUCH_END)
        }
        if (!layer.attached) {
            hallItemLayer = null
            hallItemDetail = null
        }
    }

    private fun handleExclusiveTap(layer: ExclusiveLayer, x: Float, y: Float) {
        val sourceX = x / .86f
        val sourceY = y / .86f
        when {
            sourceX in 147.282f..347.282f && sourceY in 54.533f..108.533f -> layer.onButton(0, ExclusiveLayer.TOUCH_END)
            sourceX in 354.241f..554.241f && sourceY in 54.533f..108.533f -> layer.onButton(1, ExclusiveLayer.TOUCH_END)
            sourceX in 1141.864f..1341.864f && sourceY in 54.533f..108.533f -> layer.onButton(
                2,
                ExclusiveLayer.TOUCH_END
            )

            sourceX !in 136.186f..1352.186f || sourceY !in 47f..753f -> layer.onCancel(ExclusiveLayer.TOUCH_END)
        }
        if (!layer.attached) hallExclusiveLayer = null
    }

    private fun handleMagicTap(layer: MagicInfoLayer, x: Float, y: Float) {
        val sourceX = x / .86f
        val sourceY = y / .86f
        if (sourceX in 874.764f..1022.364f && sourceY in 144.022f..194.022f ||
            sourceX !in 452.686f..1035.686f || sourceY !in 130f..670f
        ) layer.close(UnitInfoLayer.TOUCH_END)
        if (!layer.attached) hallMagicLayer = null
    }

    private fun openHallUnitInfo(selectedUnitId: Int) {
        val ids = hallEquipUnitIds().sorted()
        val rows = ids.mapNotNull { id ->
            val unit = gameDataCatalog.unitProfile(id) ?: return@mapNotNull null
            val level = campaign.unitAttribute(id, 18, unit.level)
            val battle = gameDataCatalog.battleProfile(
                id,
                (level - 1).coerceAtLeast(0),
                campaign.unitAttribute(id, 17, unit.posts)
            )
            UnitInfoLayer.Unit(
                id, campaign.unitNames[id] ?: if (id == 181) "병사 " else unit.name,
                gameDataCatalog.postsName(campaign.unitAttribute(id, 17, unit.posts)), level,
                battle?.maxHitPoints ?: unit.maxHitPoints, battle?.maxHitPoints ?: unit.maxHitPoints,
                battle?.maxMagicPoints ?: unit.maxMagicPoints, battle?.maxMagicPoints ?: unit.maxMagicPoints,
                battle?.attack ?: unit.attack, battle?.defense ?: unit.defense, battle?.spirit ?: unit.spirit,
                battle?.critical ?: unit.critical, battle?.morale ?: unit.morale,
            )
        }
        if (rows.isEmpty()) return
        hallUnitInfoLayer = UnitInfoLayer(
            rows,
            featsEnabled = campaign.globalVariables[4074].toString().toIntOrNull() != 0,
        ).also { it.onCreate(rows.indexOfFirst { row -> row.id == selectedUnitId }.coerceAtLeast(0)) }
    }

    private fun featsRows(unit: UnitInfoLayer.Unit): List<FeatsLayer.Row> {
        val abilities = if (unit.id == 0) listOf(41, 49, 46, 40, 42)
        else listOf(unit.attack, unit.defense, unit.spirit, unit.critical, unit.morale)
        return FeatsLayer.TITLES.mapIndexed { index, title ->
            FeatsLayer.Row(title, abilities[index], 0, 100, 127)
        }
    }

    private fun openHallFeatsFromUnitInfo() {
        val unitInfo = hallUnitInfoLayer ?: return
        if (!unitInfo.onButton(8, UnitInfoLayer.TOUCH_END)) return
        if (unitInfo.takeRoutes().none { it.route == UnitInfoLayer.Route.FEATS }) return
        hallFeatsLayer = FeatsLayer(featsRows(unitInfo.ref().unit))
    }

    private fun openHallFeatsHelp() {
        val layer = hallFeatsLayer ?: return
        if (layer.onButton(1, FeatsLayer.TOUCH_END) && layer.consumeRoute() == FeatsLayer.Route.HELP) {
            hallFeatsHelpOpen = true
        }
    }

    private fun handleHallUnitInfoTap(layer: UnitInfoLayer, x: Float, y: Float) {
        // Global123's bottom-row button8 is exposed only when GVar4074 is set.
        if (x in 505f..655f && y in 36f..83f) openHallFeatsFromUnitInfo()
        else if (x !in 169f..1162f || y !in 10f..678f) layer.onCancel(UnitInfoLayer.TOUCH_END)
        if (!layer.ref().attached) hallUnitInfoLayer = null
    }

    private fun handleHallFeatsTap(layer: FeatsLayer, x: Float, y: Float) {
        val sx = x / .86f
        val sy = y / .86f
        if (hallFeatsHelpOpen) {
            // MsgBox has one confirmation button for flag=5.
            if (sx in 654.186f..834.186f && sy in 271.285f..321.285f) hallFeatsHelpOpen = false
            return
        }
        when {
            sx in 1059.386f..1206.986f && sy in 96f..152f -> layer.onButton(0, FeatsLayer.TOUCH_END)
            sx in 904.386f..1051.986f && sy in 96f..152f -> openHallFeatsHelp()
            sx !in 267.686f..1220.686f || sy !in 83.5f..716.5f -> layer.onCancel(FeatsLayer.TOUCH_END)
        }
        if (!layer.attached) hallFeatsLayer = null
    }

    private fun handleHallManagementTap(kind: HallManagement, x: Float, y: Float) {
        val unitId = if (kind == HallManagement.EQUIP) hallEquipUnitId() else campaign.joinedUnits.firstOrNull() ?: 0
        when (kind) {
            HallManagement.EQUIP -> {
                hallEquipConfirmation?.let { confirmation ->
                    if (x in 472.30f..601.30f && y in 216.63f..259.63f) {
                        val changed = if (confirmation.itemId != null || confirmation.unequipSlot != null)
                            equipConfirmationFlow.answer(unitId, accept = true) else false
                        if (changed) hallManagementNotice =
                            if (confirmation.actionLabel == "해제") "장비를 해제했습니다." else "장비를 변경했습니다."
                    } else equipConfirmationFlow.cancel()
                    // EquipConfirmLayer's full-canvas Panel_cancel and cancel
                    // button both dismiss without applying the preview.
                    hallEquipConfirmation = null
                    return
                }
                if (hallEquipUnequipConfirmation) {
                    when {
                        x in 439f..623f && y in 291f..334f -> {
                            val count = campaign.inventory.unequipAllEquipment()
                            hallEquipUnequipConfirmation = false
                            hallManagementNotice = if (count == 0) "해제할 장비가 없습니다." else "장비 ${count}개를 모두 해제했습니다."
                        }

                        x in 657f..841f && y in 291f..334f -> hallEquipUnequipConfirmation = false
                    }
                    return
                }
                hallUnitListLayer?.let { unitList ->
                    val row = if (x in (924.186f * .86f)..(1284.186f * .86f)) {
                        (0 until unitList.rows.size.coerceAtMost(6)).firstOrNull { index ->
                            y in ((607f - index * 52f) * .86f)..((657f - index * 52f) * .86f)
                        }
                    } else null
                    val selectedId = row?.let { unitList.onRow(it, HallUnitListLayer.TOUCH_END) }
                    if (selectedId != null) {
                        hallEquipUnitIndex = hallEquipUnitIds().indexOf(selectedId)
                        prepareHallManagementDefaultEquipment(HallManagement.EQUIP)
                    }
                    if (unitList.attached) unitList.onCancel(HallUnitListLayer.TOUCH_END)
                    hallUnitListLayer = null
                    hallManagementNotice = null
                    return
                }
                if (hallInteraction.selectEquipTabAt(x, y)) {
                    hallManagementNotice = null
                    return
                }
                when {
                    x in 125f..212f && y in 37f..82f -> {
                        // Source EquipLayer.button14 opens Global126. Keep the
                        // Equip layer resident beneath it and remove the old
                        // game-only notice substitute.
                        hallExclusiveLayer = EquipExclusiveRoute.openFromInformationButton(ExclusiveLayer.TOUCH_END)
                        hallManagementNotice = null
                        return
                    }

                    x in 493f..642f && y in 37f..82f -> {
                        hallEquipUnequipConfirmation = true
                        hallManagementNotice = null
                        return
                    }

                    x in 842f..995f && y in 37f..82f -> {
                        hallEquipUnitIndex--
                        prepareHallManagementDefaultEquipment(HallManagement.EQUIP)
                        hallManagementNotice = null
                        return
                    }

                    x in 995f..1148f && y in 37f..82f -> {
                        hallEquipUnitIndex++
                        prepareHallManagementDefaultEquipment(HallManagement.EQUIP)
                        hallManagementNotice = null
                        return
                    }
                    // UnitInfoBaseLayer button0 opens the roster. Until that
                    // list overlay is drawn, tapping either half still cycles
                    // through the same source-ordered roster.
                    x in 820f..1125f && y in 566f..610f -> {
                        hallUnitListLayer = HallUnitListLayer(hallEquipUnitIds())
                        hallManagementNotice = null
                        return
                    }
                    // The visible weapon card is UnitInfoBaseLayer's equip(0)
                    // target. A short click opens ItemLayer in Cocos; TOUCH_END
                    // after a press previews removal through EquipConfirmLayer.
                    x in 745f..1149f && y in 85f..151f -> {
                        equipConfirmationFlow.requestUnequip(unitId, CampaignEquipmentSlot.WEAPON)?.let { preview ->
                            hallEquipConfirmation = EquipConfirmation(
                                preview.values,
                                preview.actionLabel,
                                unequipSlot = preview.unequipSlot,
                            )
                        }
                        return
                    }
                }
                if (x !in 124f..729f) return
                val index = ((529f - y) / 68f).toInt()
                val itemId = hallEquipInventory().getOrNull(index)?.key ?: return
                val preview = equipConfirmationFlow.requestEquip(unitId, itemId)
                if (preview == null) hallManagementNotice = "이 물품은 장착할 수 없습니다."
                else {
                    hallEquipConfirmation =
                        EquipConfirmation(preview.values, preview.actionLabel, itemId = preview.itemId)
                    hallManagementNotice = null
                }
            }

            HallManagement.BUY -> {
                if (hallInteraction.selectBuyTabAt(x, y)) {
                    hallManagementNotice = null
                    return
                }
                if (x !in 176f..657f) return
                val item = if (hallBuyTab == 0) {
                    if (y !in 118f..577f) return
                    hallBuyCandidates().getOrNull(((522.16f - y) / 153.08f).toInt())
                } else {
                    if (y !in 132f..563f) return
                    hallBuyProperties().getOrNull(((562.64f - y) / 108f).toInt())
                } ?: return
                val price = gameDataCatalog.purchasePrice(item)
                if (price == 255) {
                    hallManagementNotice = "값으로 매길 수 없는 보물이므로 구매할 수 없습니다."
                    return
                }
                if (campaign.money < price) {
                    hallManagementNotice = "금화가 부족하여 구매할 수 없습니다"
                } else {
                    campaign.addMoney(-price)
                    campaign.inventory.addItem(item.id)
                    hallManagementNotice = "${item.name} 구매"
                }
            }

            HallManagement.SELL -> {
                if (hallInteraction.selectSellTabAt(x, y)) {
                    hallManagementNotice = null
                    return
                }
                if (x !in 271f..1009f) return
                if (y !in 182f..495f) return
                val col = if (x >= 636f) 1 else 0
                val row = ((495f - y) / 157f).toInt()
                val itemId = hallSellCandidates().getOrNull(row * 2 + col)?.key ?: return
                val item = gameDataCatalog.equipmentProfile(itemId) ?: return
                if (item.price == 255) {
                    hallManagementNotice = "판매할 수 없는 물품입니다."
                } else if (campaign.inventory.consumeItem(itemId)) {
                    campaign.addMoney(gameDataCatalog.sellingPrice(item))
                    hallManagementNotice = "${item.name} 판매"
                }
            }
        }
    }

    private fun executeHallInteraction(intent: HallInteractionIntent) {
        when (intent) {
            HallInteractionIntent.None,
            HallInteractionIntent.MenuClosed,
            HallInteractionIntent.OpenMenu -> Unit

            HallInteractionIntent.StartBattle -> if (!beginHallBattleScene()) routeAfterScenario()
            is HallInteractionIntent.OpenManagement -> {
                hallManagement = HallManagement.valueOf(intent.kind.name)
                prepareHallManagementDefaultEquipment(requireNotNull(hallManagement))
            }
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
            opacity: Float = 1f,
            visible: Boolean = true,
        ) = log.draw(
            "hall-$fixture-stable", layer, path, type,
            x * scale, y * scale, w * scale, h * scale, asset,
            opacity = opacity,
            blend = if (type == "label" || type == "rich-text") labelBlend else spriteBlend,
            visible = visible,
            text = text,
        )

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `layer` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `visible` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(
            layer: String,
            path: String,
            value: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            visible: Boolean = true
        ) =
            event(layer, path, "label", x, y, w, h, text = value, visible = visible)

        event(
            "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>",
        )
        when (fixture) {
            "feats", "feats-help" -> {
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .392f
                )
                val root = "Canvas/Layer/Logo_12-1"
                event("HallLayer", root, "tiled-sprite", 267.686f, 83.5f, 953f, 633f, "Logo_9-1")
                event("HallLayer", "$root/box4", "sliced-sprite", 267.686f, 83.5f, 953f, 633f, "box4")
                event("HallLayer", "$root/bg1", "sprite", 267.686f, 656.5f, 953f, 60f, "bg1")
                event("HallLayer", "$root/bg1/box3", "sliced-sprite", 267.686f, 656.5f, 953f, 60f, "box3")
                label("HallLayer", "$root/bg1/label", "공훈", 669.686f, 662.3f, 71.2f, 52.4f)
                event("HallLayer", "$root/scrollview", "tiled-sprite", 277.686f, 158.45f, 933f, 442.7f, "Logo_12-1")
                event("HallLayer", "$root/scrollview/box2", "tiled-sprite", 277.686f, 158.45f, 933f, 442.7f, "box2")
                requireNotNull(hallFeatsLayer).view().rows.forEachIndexed { index, row ->
                    val rowY = 529.15f - index * 74f
                    val titleX = if (row.title == "민첩성") 290.286f else 307.586f
                    val titleW = if (row.title == "민첩성") 107.8f else 73.2f
                    val item = "$root/scrollview/view/content/item0"
                    event(
                        "HallLayer",
                        item,
                        "sprite",
                        279.686f,
                        rowY,
                        929f,
                        70f,
                        "885a69b4-08ed-4c78-8896-ffb04eb2bd20"
                    )
                    label("HallLayer", "$item/label0", row.title, titleX, rowY + 9.8f, titleW, 54.4f)
                    label("HallLayer", "$item/label1", row.ability.toString(), 462.941f, rowY + 9.8f, 48.49f, 54.4f)
                    label("HallLayer", "$item/label2", row.phaseLabel, 1086.816f, rowY + 9.8f, 70.74f, 54.4f)
                    event(
                        "HallLayer",
                        "$item/Feats/progressBar",
                        "sliced-sprite",
                        572.186f,
                        rowY + 20f,
                        446f,
                        30f,
                        "bg1"
                    )
                    event(
                        "HallLayer",
                        "$item/Feats/progressBar/bg1",
                        "sliced-sprite",
                        574.186f,
                        rowY + 20f,
                        442f,
                        30f,
                        "box2"
                    )
                    event(
                        "HallLayer",
                        "$item/Feats/progressBar/bar",
                        "sliced-sprite",
                        574.186f,
                        rowY + 22f,
                        442f * row.progressRatio,
                        26f,
                        "Mark_5-1"
                    )
                    label("HallLayer", "$item/Feats/label", row.progressLabel, 743.136f, rowY + 18.454f, 104.1f, 54.4f)
                }
                listOf(410.859f, 555.31f, 1027.419f).forEach { x ->
                    event("HallLayer", "$root/vline", "sliced-sprite", x, 160.25f, 6f, 450.3f, "vline")
                }
                /**
                 * 공개 메서드 `header`
                 *
                 * ### 파라미터
                - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `lx` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `lw` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Unit`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun header(x: Float, y: Float, w: Float, h: Float, lx: Float, lw: Float, value: String) {
                    event("HallLayer", "$root/box3", "sliced-sprite", x, y, w, h, "bg1")
                    event("HallLayer", "$root/box3/box3", "sliced-sprite", x, y, w, h, "box3")
                    label("HallLayer", "$root/box3/label", value, lx, 607.081f, lw, 50.4f)
                }
                header(272.836f, 601.45f, 142.7f, 55.1f, 269.431f, 149.51f, "능력 이름")
                header(415.436f, 601.45f, 143.5f, 55.1f, 435.286f, 103.8f, "능력치")
                header(559.136f, 601.5f, 472.1f, 55f, 588.216f, 413.94f, "현재/업그레이드 필요 공훈")
                header(1030.886f, 601.45f, 182.6f, 55.1f, 875.061f, 494.25f, "상위 단계로 승급하는 데 필요함")
                event("HallLayer", "$root/button0/Background", "sliced-sprite", 1059.386f, 96f, 147.6f, 56f, "box3")
                label("HallLayer", "$root/button0/Background/Label", "확인", 1083.186f, 104f, 100f, 40f)
                event("HallLayer", "$root/button1/Background", "sliced-sprite", 904.386f, 96f, 147.6f, 56f, "box3")
                label("HallLayer", "$root/button1/Background/Label", "설명", 928.186f, 104f, 100f, 40f)
                if (fixture == "feats-help") {
                    event(
                        "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                        "default_sprite_splash", opacity = 0f, visible = false
                    )
                    event("FeatsLayer", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
                    event("FeatsLayer", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
                    event(
                        "FeatsLayer",
                        "Canvas/Layer/bg0/Logo_3-1",
                        "sprite",
                        453.005f,
                        373.951f,
                        106f,
                        124f,
                        "Logo_3-1"
                    )
                    label("FeatsLayer", "Canvas/Layer/bg0/label", FeatsLayer.HELP_TEXT, 573.686f, 335f, 463f, 190f)
                    event(
                        "FeatsLayer",
                        "Canvas/Layer/bg0/btns/button0/Background",
                        "sliced-sprite",
                        654.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "FeatsLayer",
                        "Canvas/Layer/bg0/btns/button0/Background/Label",
                        "예",
                        657.586f,
                        279.085f,
                        169.4f,
                        40f
                    )
                }
            }

            "magic" -> appendMagicRenderEvents(log, event = ::event, label = ::label)
            "exclusive", "exclusive-tab1" -> {
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .392f
                )
                event("ExclusiveLayer", "Canvas/Layer/bg", "tiled-sprite", 136.186f, 47f, 1216f, 706f, "Logo_9-1")
                event("ExclusiveLayer", "Canvas/Layer/bg/box1", "sliced-sprite", 136.186f, 47f, 1216f, 706f, "box1")
                event("ExclusiveLayer", "Canvas/Layer/bg/bg1", "sprite", 136.186f, 703f, 1216f, 50f, "bg1")
                label("ExclusiveLayer", "Canvas/Layer/bg/bg1/label", "장비 정보", 669.431f, 702.8f, 149.51f, 50.4f)
                /**
                 * 공개 메서드 `header`
                 *
                 * ### 파라미터
                - `panel` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `labelX` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `labelWidth` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Unit`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun header(
                    panel: String,
                    x: Float,
                    y: Float,
                    width: Float,
                    labelX: Float,
                    labelWidth: Float,
                    value: String
                ) {
                    event("ExclusiveLayer", "Canvas/Layer/bg/$panel/item", "sliced-sprite", x, y, width, 60f, "box4")
                    label(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/$panel/item/label",
                        value,
                        labelX,
                        y + 4.8f,
                        labelWidth,
                        50.4f
                    )
                }
                if (fixture == "exclusive") {
                    event(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/panel0",
                        "sliced-sprite",
                        138.186f,
                        117.5f,
                        1212f,
                        585f,
                        "box4"
                    )
                    listOf(371.375f, 604.197f, 840.498f).forEach { x ->
                        event(
                            "ExclusiveLayer",
                            "Canvas/Layer/bg/panel0/vline",
                            "sprite",
                            x,
                            120.254f,
                            6f,
                            524.8f,
                            "vline"
                        )
                    }
                    header("panel0", 138.586f, 642.1f, 236f, 221.986f, 69.2f, "무기")
                    header("panel0", 374.586f, 642.1f, 233f, 456.486f, 69.2f, "보구")
                    header("panel0", 607.636f, 642.1f, 236.5f, 691.286f, 69.2f, "보조")
                    header("panel0", 844.036f, 642.1f, 506.1f, 1022.331f, 149.51f, "특수 효과")
                } else {
                    event(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/panel1",
                        "sliced-sprite",
                        140.186f,
                        117.45f,
                        1208f,
                        585.7f,
                        "box4"
                    )
                    event(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/panel1/vline",
                        "sprite",
                        321.257f,
                        119.319f,
                        6f,
                        524.8f,
                        "vline"
                    )
                    event(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/panel1/vline",
                        "sprite",
                        565.153f,
                        119.205f,
                        6f,
                        524.8f,
                        "vline"
                    )
                    header("panel1", 140.85f, 643.3f, 185f, 181.286f, 103.8f, "소지자")
                    header("panel1", 324.236f, 643.3f, 243.9f, 411.586f, 69.2f, "이름")
                    header("panel1", 568.186f, 643.3f, 780f, 883.431f, 149.51f, "특수 효과")
                }
                /**
                 * 공개 메서드 `button`
                 *
                 * ### 파라미터
                - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `labelX` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `labelWidth` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
                - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Unit`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun button(name: String, x: Float, labelX: Float, labelWidth: Float, value: String) {
                    val path = "Canvas/Layer/bg/$name/Background"
                    event("ExclusiveLayer", path, "sliced-sprite", x, 54.533f, 200f, 54f, "box3")
                    label("ExclusiveLayer", "$path/Label", value, labelX, 59.533f, labelWidth, 50f)
                }
                button("button1", 354.241f, 370.741f, 167f, "전용 목록")
                button("button0", 147.282f, 163.782f, 167f, "세트 목록")
                button("button2", 1141.864f, 1191.864f, 100f, "확인")
            }

            "info", "get-item-equipment", "get-item-property" -> {
                val value = sanitizeInfoText(playback.currentModalText.orEmpty())
                val richTextWidth = when (fixture) {
                    "get-item-equipment" -> 259.72f
                    "get-item-property" -> 324.47f
                    else -> 229.83f
                }
                val richTextX = (1488.372f - richTextWidth) / 2f
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
                    opacity = 0f, visible = false
                )
                event(
                    "InfoLayer",
                    "Canvas/Layer/bg",
                    "sliced-sprite",
                    richTextX - 20f,
                    376.76f,
                    richTextWidth + 40f,
                    83f,
                    "bg"
                )
                event(
                    "InfoLayer",
                    "Canvas/Layer/bg/richtext",
                    "rich-text",
                    richTextX,
                    387f,
                    richTextWidth,
                    63f,
                    text = value
                )
                label(
                    "InfoLayer",
                    "Canvas/Layer/bg/richtext/RICHTEXT_CHILD",
                    value,
                    richTextX,
                    387f,
                    richTextWidth,
                    63f
                )
            }

            "item-equipment", "item-property", "item-discard-confirm" -> {
                val property = fixture == "item-property"
                val discard = fixture == "item-discard-confirm"
                val item =
                    requireNotNull(gameDataCatalog.equipmentProfile(if (property) 150 else if (discard) 4 else 0))
                val name = item.name
                val level = if (discard) "---" else "1"
                val type = if (property) "아이템" else gameDataCatalog.equipmentTypeName(item.itemType)
                val price = gameDataCatalog.purchasePrice(item).let { if (it == 255) "---" else it.toString() }
                val effect = when {
                    property -> "HP 회복"
                    discard -> "공격력 +38\n없음"
                    else -> "공격력 +10\n없음"
                }

                /**
                 * 공개 메서드 `measuredWidth`
                 *
                 * ### 파라미터
                - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Float`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun measuredWidth(value: String): Float =
                    value.count { it != ' ' } * 27.68f + value.count { it == ' ' } * 8.89f
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .392f
                )
                event("ItemLayer", "Canvas/Layer/bg1", "tiled-sprite", 253.186f, 80f, 982f, 640f, "Logo_9-1")
                event("ItemLayer", "Canvas/Layer/bg1/box2", "sliced-sprite", 253.186f, 80f, 982f, 640f, "box3")
                label("ItemLayer", "Canvas/Layer/bg1/label0", name, 420.186f, 658.8f, 203.1f, 50.4f)
                if (!property) {
                    label("ItemLayer", "Canvas/Layer/bg1/label_1", "Lv", 673.411f, 658.483f, 42.25f, 50.4f)
                    label(
                        "ItemLayer",
                        "Canvas/Layer/bg1/label1",
                        level,
                        723.411f,
                        658.483f,
                        if (discard) 39.96f else 22.25f,
                        50.4f
                    )
                    label("ItemLayer", "Canvas/Layer/bg1/label_2", "Exp", 420.186f, 604.8f, 68.93f, 50.4f)
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg1/bar",
                        "sliced-sprite",
                        500.965f,
                        614.855f,
                        204f,
                        24f,
                        "default_scrollbar_bg"
                    )
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg1/bar/bar",
                        "sliced-sprite",
                        502.965f,
                        616.855f,
                        0f,
                        20f,
                        "Mark_6-1"
                    )
                    label("ItemLayer", "Canvas/Layer/bg1/bar/label1", "0/100", 552.915f, 606.655f, 100.1f, 50.4f)
                }
                event("ItemLayer", "Canvas/Layer/bg1/bg4", "sliced-sprite", 265.778f, 564.802f, 144f, 144f, "box2")
                event(
                    "ItemLayer",
                    "Canvas/Layer/bg1/bg4/icon",
                    "sprite",
                    273.778f,
                    572.802f,
                    128f,
                    128f,
                    "${item.icon}-1"
                )
                event("ItemLayer", "Canvas/Layer/bg1/bg0", "sliced-sprite", 420.536f, 498.55f, 343.5f, 100.9f, "box1")
                label("ItemLayer", "Canvas/Layer/bg1/bg0/label", "속성:", 432.137f, 548.543f, 80.31f, 50.4f)
                label("ItemLayer", "Canvas/Layer/bg1/bg0/label0", type, 522.525f, 548.543f, type.length * 34.6f, 50.4f)
                label("ItemLayer", "Canvas/Layer/bg1/bg0/label", "가격:", 432.137f, 503.543f, 80.31f, 50.4f)
                label(
                    "ItemLayer",
                    "Canvas/Layer/bg1/bg0/label1",
                    price,
                    522.525f,
                    503.543f,
                    if (price.length == 4) 88.98f else 66.74f,
                    50.4f
                )
                event("ItemLayer", "Canvas/Layer/bg1/bg1", "sliced-sprite", 261.686f, 92.5f, 501f, 377f, "box1")
                event("ItemLayer", "Canvas/Layer/bg1/bg1/bg1", "sprite", 470.286f, 447.7f, 83.8f, 40f, "bg1")
                label("ItemLayer", "Canvas/Layer/bg1/bg1/bg1/label", "효과", 477.586f, 442.5f, 69.2f, 50.4f)
                label(
                    "ItemLayer", "Canvas/Layer/bg1/bg1/scrollview/view/content/label", effect, 265.686f,
                    if (property) 389.966f else 345.966f, 493f, if (property) 55.44f else 99.44f
                )
                event("ItemLayer", "Canvas/Layer/bg1/bg2", "sliced-sprite", 770.186f, 157.5f, 448f, 247f, "box2")
                event("ItemLayer", "Canvas/Layer/bg1/bg2/bg1", "sprite", 943.336f, 369.55f, 89.7f, 40.9f, "bg1")
                label("ItemLayer", "Canvas/Layer/bg1/bg2/bg1/label", "설명", 953.586f, 378.8f, 69.2f, 50.4f)
                val introHeight = if (discard) 99.44f else 187.44f
                label(
                    "ItemLayer", "Canvas/Layer/bg1/bg2/scrollview/view/content/label", item.intro,
                    774.186f, 378.7f - introHeight, 440f, introHeight
                )
                event("ItemLayer", "Canvas/Layer/bg1/bg3", "sliced-sprite", 770.186f, 427f, 448f, 260f, "box1")
                event("ItemLayer", "Canvas/Layer/bg1/bg3/bg1", "sprite", 871.686f, 664.273f, 245f, 45f, "bg1")
                label(
                    "ItemLayer",
                    "Canvas/Layer/bg1/bg3/bg1/label",
                    "장착 가능한 부대입니다.",
                    804.516f,
                    661.573f,
                    379.34f,
                    50.4f
                )
                (0 until 27).forEach { row ->
                    val rowY = 609.55f - row * 52f
                    val visible = row < 13
                    val path = "Canvas/Layer/bg1/bg3/scrollview/view/content/item"
                    event(
                        "ItemLayer", path, "sliced-sprite", 772.186f, rowY, 444f, 50f,
                        if (row % 2 == 0) "885a69b4-08ed-4c78-8896-ffb04eb2bd20" else "bg2", visible = visible
                    )
                    (0..2).forEach { column ->
                        val value = if (row == 26 && column == 2) "패왕" else gameDataCatalog.postsName(row * 3 + column)
                        val width = measuredWidth(value)
                        val center = when (column) {
                            0 -> 851.186f; 1 -> 994.186f; else -> 1138.186f
                        }
                        label(
                            "ItemLayer",
                            "$path/label$column",
                            value,
                            center - width / 2f,
                            rowY + 4.84f,
                            width,
                            40.32f,
                            visible
                        )
                    }
                }
                event(
                    "ItemLayer",
                    "Canvas/Layer/bg1/button1/Background",
                    "sliced-sprite",
                    1065.827f,
                    97.824f,
                    150f,
                    50f,
                    "box3"
                )
                label("ItemLayer", "Canvas/Layer/bg1/button1/Background/Label", "확인", 1090.827f, 104.824f, 100f, 40f)
                if (discard) {
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg1/button2/Background",
                        "sliced-sprite",
                        901.312f,
                        97.824f,
                        150f,
                        50f,
                        "box3"
                    )
                    label(
                        "ItemLayer",
                        "Canvas/Layer/bg1/button2/Background/Label",
                        "버리기",
                        926.312f,
                        104.824f,
                        100f,
                        40f
                    )
                    event(
                        "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                        "default_sprite_splash", opacity = 0f, visible = false
                    )
                    event("ItemLayer", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
                    event("ItemLayer", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg0/Logo_3-1",
                        "sprite",
                        453.005f,
                        373.951f,
                        106f,
                        124f,
                        "Logo_3-1"
                    )
                    label("ItemLayer", "Canvas/Layer/bg0/label", "버릴 것을 결정하시겠습니까?$name?", 573.686f, 335f, 463f, 190f)
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg0/btns/button1/Background",
                        "sliced-sprite",
                        554.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "ItemLayer",
                        "Canvas/Layer/bg0/btns/button1/Background/Label",
                        "비",
                        557.336f,
                        279.085f,
                        168.1f,
                        40f
                    )
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg0/btns/button0/Background",
                        "sliced-sprite",
                        754.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "ItemLayer",
                        "Canvas/Layer/bg0/btns/button0/Background/Label",
                        "예",
                        757.586f,
                        279.085f,
                        169.4f,
                        40f
                    )
                }
            }

            "map-info" -> {
                val value = sanitizeInfoText(playback.currentModalText.orEmpty())
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = 0f, visible = false
                )
                event(
                    "MapInfoLayer", "Canvas/Layer/bg1", "sprite", 0f, 0f, 1488.372f, 161f,
                    "default_sprite_splash", opacity = .498f
                )
                event(
                    "MapInfoLayer",
                    "Canvas/Layer/bg0/richtext",
                    "rich-text",
                    30.319f,
                    103.285f,
                    558.25f,
                    50.4f,
                    text = value
                )
                label(
                    "MapInfoLayer",
                    "Canvas/Layer/bg0/richtext/RICHTEXT_CHILD",
                    value,
                    30.319f,
                    103.285f,
                    558.25f,
                    50.4f
                )
            }

            "choice" -> {
                event(
                    "HallLayer",
                    "Canvas/Layer/Panel_cancel",
                    "sprite",
                    0f,
                    0f,
                    1488.372f,
                    800f,
                    "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
                    opacity = 0f,
                    visible = false
                )
                event("ChooseLayer", "Canvas/Layer/bg/face", "sprite", 268.693f, 279.314f, 192f, 240f, "1")
                event(
                    "ChooseLayer",
                    "Canvas/Layer/bg/scrollview",
                    "sprite",
                    492.686f,
                    308.15f,
                    747f,
                    183.7f,
                    "U_select_10-1"
                )
                val options = listOf(
                    Triple("바로 이게 제가 바라는 거예요", 438.5f, 367.72f),
                    Triple("이건 너무 이른 것 같아", 389.5f, 284.68f),
                )
                options.forEach { (value, y, childWidth) ->
                    val row = "Canvas/Layer/bg/scrollview/view/content/item/bg6"
                    event(
                        "ChooseLayer",
                        row,
                        "sprite",
                        538.886f,
                        y,
                        690.6f,
                        45f,
                        "885a69b4-08ed-4c78-8896-ffb04eb2bd20"
                    )
                    event("ChooseLayer", "$row/richtext", "rich-text", 561.486f, y + .45f, 642.6f, 44.1f, text = value)
                    label("ChooseLayer", "$row/richtext/RICHTEXT_CHILD", value, 561.486f, y + .45f, childWidth, 44.1f)
                }
            }

            "ask" -> {
                event(
                    "HallLayer",
                    "Canvas/Layer/Panel_cancel",
                    "sprite",
                    0f,
                    0f,
                    1488.372f,
                    800f,
                    "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
                    opacity = 0f,
                    visible = false
                )
                event("MsgBox2", "Canvas/Layer/bg0", "tiled-sprite", 539.686f, 322f, 409f, 156f, "Logo_9-1")
                event("MsgBox2", "Canvas/Layer/bg0/bg1", "sprite", 539.686f, 428f, 409f, 50f, "bg1")
                label("MsgBox2", "Canvas/Layer/bg0/bg1/label", "확인", 544.686f, 427.8f, 69.2f, 50.4f)
                event("MsgBox2", "Canvas/Layer/bg0/box3", "sliced-sprite", 539.686f, 322f, 409f, 156f, "box1")
                listOf(
                    Triple(561.438f, 595.938f, "예"),
                    Triple(751.471f, 785.971f, "비")
                ).forEachIndexed { index, (x, labelX, value) ->
                    val path = "Canvas/Layer/bg0/button$index/Background"
                    event("MsgBox2", path, "sliced-sprite", x, 356f, 169f, 50f, "box3")
                    label("MsgBox2", "$path/Label", value, labelX, 361.844f, 100f, 40f)
                }
            }

            "command" -> {
                repeat(4) { index ->
                    event(
                        "HallLayer", "Canvas/Layer/button$index/Background", "sliced-sprite",
                        1041.372f + index * 96f, 2f, 96f, 96f, index.toString()
                    )
                }
                event("HallLayer", "Canvas/Layer/button/Background", "sprite", 36.047f, 370f, 60f, 60f, "menu")
            }

            "save", "save-confirm" -> {
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .392f
                )
                event("SaveLayer", "Canvas/Layer/bg1", "tiled-sprite", 278.186f, 83f, 932f, 634f, "Logo_9-1")
                event("SaveLayer", "Canvas/Layer/bg1/bg", "sliced-sprite", 278.186f, 83f, 932f, 634f, "box3")
                event("SaveLayer", "Canvas/Layer/bg1/bg1", "sprite", 278.186f, 667f, 932f, 50f, "bg1")
                label("SaveLayer", "Canvas/Layer/bg1/bg1/label", "진행 상황 유지", 288.186f, 666.8f, 229.83f, 50.4f)
                label(
                    "SaveLayer",
                    "Canvas/Layer/bg1/label",
                    "어떤 진행 상황을 저장할지 선택해 주세요.",
                    286.785f,
                    612.805f,
                    654.88f,
                    50.4f
                )
                event("SaveLayer", "Canvas/Layer/bg1/box2", "sliced-sprite", 287.186f, 172.534f, 912f, 428f, "box2")
                repeat(22) { index ->
                    val y = 547.534f - index * 52f
                    val visible = index < 12
                    val path = "Canvas/Layer/bg1/box2/scrollview/view/content/item"
                    event(
                        "SaveLayer", path, "sprite", 289.186f, y, 908f, 50f,
                        "885a69b4-08ed-4c78-8896-ffb04eb2bd20", visible = visible
                    )
                    label(
                        "SaveLayer",
                        "$path/label0",
                        "No.${(index + 1).toString().padStart(3, ' ')}",
                        295.448f,
                        y - .2f,
                        117.85f,
                        50.4f,
                        visible
                    )
                    label("SaveLayer", "$path/label1", "---", 434.615f, y - .2f, 124.49f, 50.4f, visible)
                    label("SaveLayer", "$path/label2", "진행 상황 저장 안 함", 577.886f, y, 616.3f, 50f, visible)
                }
                event(
                    "SaveLayer",
                    "Canvas/Layer/bg1/box2/vline",
                    "sliced-sprite",
                    422.057f,
                    174.634f,
                    6f,
                    423.8f,
                    "vline"
                )
                event(
                    "SaveLayer",
                    "Canvas/Layer/bg1/box2/vline",
                    "sliced-sprite",
                    566.695f,
                    174.634f,
                    6f,
                    423.8f,
                    "vline"
                )
                label(
                    "SaveLayer",
                    "Canvas/Layer/bg1/label",
                    "따뜻한 알림: 오래된 저장 파일일수록 앞에 표시됩니다.",
                    131.555f,
                    105.399f,
                    850.11f,
                    50.4f
                )
                event(
                    "SaveLayer",
                    "Canvas/Layer/bg1/button/Background",
                    "sliced-sprite",
                    1045.855f,
                    100.162f,
                    147.6f,
                    56f,
                    "box3"
                )
                label("SaveLayer", "Canvas/Layer/bg1/button/Background/Label", "취소", 1069.655f, 108.162f, 100f, 40f)
                if (fixture == "save-confirm") {
                    event(
                        "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                        "default_sprite_splash", opacity = 0f, visible = false
                    )
                    event("SaveLayer", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
                    event("SaveLayer", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
                    event(
                        "SaveLayer",
                        "Canvas/Layer/bg0/Logo_3-1",
                        "sprite",
                        453.005f,
                        373.951f,
                        106f,
                        124f,
                        "Logo_3-1"
                    )
                    label(
                        "SaveLayer",
                        "Canvas/Layer/bg0/label",
                        "진행도 No.1:진행 상황 저장 안 함저장할 수 있나요?",
                        573.686f,
                        335f,
                        463f,
                        190f
                    )
                    event(
                        "SaveLayer",
                        "Canvas/Layer/bg0/btns/button1/Background",
                        "sliced-sprite",
                        554.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "SaveLayer",
                        "Canvas/Layer/bg0/btns/button1/Background/Label",
                        "됐어",
                        557.336f,
                        279.085f,
                        168.1f,
                        40f
                    )
                    event(
                        "SaveLayer",
                        "Canvas/Layer/bg0/btns/button0/Background",
                        "sliced-sprite",
                        754.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "SaveLayer",
                        "Canvas/Layer/bg0/btns/button0/Background/Label",
                        "저장",
                        757.586f,
                        279.085f,
                        169.4f,
                        40f
                    )
                }
            }

            "ambition", "menu" -> {
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .118f
                )
                event("HallMenuLayer", "Canvas/Layer/bg", "sprite", 0f, 0f, 1488.372f, 146f, "bg1")
                event("HallMenuLayer", "Canvas/Layer/bg/box1", "sliced-sprite", 0f, 0f, 1488.372f, 146f, "box1")
                event("HallMenuLayer", "Canvas/Layer/bg/bg0", "sliced-sprite", 115.955f, 4.946f, 304f, 44f, "box2")
                event(
                    "HallMenuLayer",
                    "Canvas/Layer/bg/bg0/Mark_64-1",
                    "sprite",
                    117.955f,
                    6.946f,
                    300f,
                    40f,
                    "Mark_64-1"
                )
                if (fixture == "ambition") label(
                    "HallMenuLayer",
                    "Canvas/Layer/bg/bg0/label",
                    "조조가 군대를 일으키다",
                    129.87f,
                    8.046f,
                    276.17f,
                    37.8f
                )
                event("HallMenuLayer", "Canvas/Layer/bg/bg1", "sliced-sprite", 425.986f, 4.9f, 324f, 44f, "box2")
                event(
                    "HallMenuLayer",
                    "Canvas/Layer/bg/bg1/Mark_64-1",
                    "sprite",
                    427.986f,
                    6.9f,
                    320f,
                    40f,
                    "Mark_64-1"
                )
                if (fixture == "ambition") label(
                    "HallMenuLayer",
                    "Canvas/Layer/bg/bg1/label",
                    "사수관 조조군 주진영",
                    462.876f,
                    8f,
                    250.22f,
                    37.8f
                )
                event("HallMenuLayer", "Canvas/Layer/bg/bar", "sliced-sprite", 834.186f, 19.417f, 300f, 15f, "Mark_4-1")
                event(
                    "HallMenuLayer", "Canvas/Layer/bg/bar/bar", "sliced-sprite", 834.186f, 19.417f,
                    if (fixture == "ambition") 165f else 150f, 15f, "Mark_1-1"
                )
                if (fixture == "menu") {
                    event("HallMenuLayer", "Canvas/Layer/bg/bar/flag0", "sprite", 787.27f, 11.917f, 32f, 30f, "flag1")
                    event("HallMenuLayer", "Canvas/Layer/bg/bar/flag1", "sprite", 1150.837f, 11.917f, 32f, 30f, "flag2")
                }
                val buttonXs =
                    floatArrayOf(11.107f, 99.365f, 187.846f, 276.74f, 379.317f, 467.575f, 556.056f, 646.441f, 745.44f)
                val buttonIds = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 9)
                buttonXs.forEachIndexed { index, x ->
                    val button = buttonIds[index]
                    val icon = if (button == 9) "help" else "tool${button + 1}"
                    val path = "Canvas/Layer/bg/button$button/Background"
                    event("HallMenuLayer", path, "sliced-sprite", x, 52.137f, 88f, 88f, "box3")
                    event(
                        "HallMenuLayer", "$path/${if (button == 9) "help" else "tool1"}", "sprite",
                        x + 8f, if (button == 9) 60.137f else 60.419f, 72f, 72f, icon
                    )
                }
            }
        }
    }

    private fun appendMagicRenderEvents(
        log: RenderEventLog,
        event: (String, String, String, Float, Float, Float, Float, String?, String, Float, Boolean) -> Unit,
        label: (String, String, String, Float, Float, Float, Float, Boolean) -> Unit,
    ) {
        val magic = requireNotNull(hallMagicLayer).magic

        /**
         * 공개 메서드 `sprite`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `opacity` (`Float = 1f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun sprite(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String,
            opacity: Float = 1f
        ) =
            event("MagicLayer", path, type, x, y, w, h, asset, "", opacity, true)

        /**
         * 공개 메서드 `text`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float = 50.4f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun text(path: String, value: String, x: Float, y: Float, w: Float, h: Float = 50.4f) =
            label("MagicLayer", path, value, x, y, w, h, true)

        event(
            "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "default_sprite_splash", "", .392f, true
        )
        sprite("Canvas/Layer/bg1", "tiled-sprite", 452.686f, 130f, 583f, 540f, "Logo_9-1")
        sprite("Canvas/Layer/bg1/box2", "sliced-sprite", 452.686f, 130f, 583f, 540f, "box3")
        text("Canvas/Layer/bg1/label", magic.name, 577.509f, 604.008f, 103.8f)
        sprite("Canvas/Layer/bg1/skill_0", "sprite", 478.186f, 562f, 80f, 80f, "${magic.icon + 1}-1")
        sprite("Canvas/Layer/bg1/bg0", "sliced-sprite", 465.636f, 434f, 340.3f, 100f, "box1")
        text("Canvas/Layer/bg1/bg0/label", "위력:", 476.336f, 479.826f, 80.31f)
        text("Canvas/Layer/bg1/bg0/label0", "${magic.power ?: 0}%", 566.719f, 480.13f, 80.06f)
        text("Canvas/Layer/bg1/bg0/label", "MP 소모:", 470.776f, 436.826f, 151.43f)
        text("Canvas/Layer/bg1/bg0/label1", magic.cost.toString(), 627.053f, 436.675f, 22.25f)
        sprite("Canvas/Layer/bg1/bg1", "sliced-sprite", 465.636f, 147f, 340.3f, 274f, "box2")
        text("Canvas/Layer/bg1/bg1/scrollview/view/content/label", magic.intro, 470.786f, 144.76f, 330f, 275.44f)
        sprite("Canvas/Layer/bg1/bg2", "sliced-sprite", 814.213f, 436.061f, 200f, 200f, "box1")
        sprite("Canvas/Layer/bg1/bg2/bg", "sliced-sprite", 830.713f, 614.117f, 167f, 40f, "bg1")
        text("Canvas/Layer/bg1/bg2/bg/label", "가능 범위", 839.654f, 611.005f, 149.51f)
        sprite("Canvas/Layer/bg1/bg2/img", "sprite", 834.213f, 450.755f, 160f, 160f, "${magic.hit + 1}-1")
        sprite("Canvas/Layer/bg1/bg3", "sliced-sprite", 814.213f, 204.673f, 200f, 200f, "box1")
        sprite("Canvas/Layer/bg1/bg3/bg", "sliced-sprite", 831.713f, 384.673f, 165f, 40f, "bg1")
        text("Canvas/Layer/bg1/bg3/bg/label", "영향 범위", 839.654f, 381.561f, 149.51f)
        sprite("Canvas/Layer/bg1/bg3/img", "sprite", 834.213f, 219.367f, 160f, 160f, "${magic.eff + 1}-1")
        sprite("Canvas/Layer/bg1/button/Background", "sliced-sprite", 874.764f, 144.022f, 147.6f, 50f, "box3")
        text("Canvas/Layer/bg1/button/Background/Label", "확인", 898.564f, 152.022f, 100f, 40f)
    }


    private fun appendHallInfoRenderEvents(log: RenderEventLog, kind: HallInfo) {
        if (kind == HallInfo.FORCES) {
            appendSourceForcesRenderEvents(log)
            return
        }
        if (kind == HallInfo.PROPERTY) {
            appendSourcePropertyRenderEvents(log)
            return
        }
        if (kind == HallInfo.TREASURE) {
            appendSourceTreasureRenderEvents(log)
            return
        }
        if (kind == HallInfo.TERRAIN) {
            appendSourceTerrainRenderEvents(log)
            return
        }
        if (kind == HallInfo.HELPER) {
            appendSourceHelperRenderEvents(log)
            return
        }
        val (layer, root) = when (kind) {
            HallInfo.FORCES -> "ForcesListLayer" to floatArrayOf(142.49f, 68.37f, 995.02f, 551.26f)
            HallInfo.PROPERTY -> "PropertyLayer" to floatArrayOf(212.42f, 40.42f, 854.84f, 607.16f)
            HallInfo.TERRAIN -> "TerrainLayer" to floatArrayOf(235.84f, 86f, 878.15f, 516f)
            HallInfo.TREASURE -> "TreasureLayer" to floatArrayOf(222.9f, 72.24f, 834.2f, 543.52f)
            HallInfo.HELPER -> "HelperLayer" to floatArrayOf(127f, 21.07f, 1025.98f, 645.86f)
        }
        /**
         * 공개 메서드 `event`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `visible` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun event(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String = "",
            text: String = "",
            visible: Boolean = true
        ) =
            log.draw(
                "hall-info",
                layer,
                "Canvas/Layer/$layer/$path",
                type,
                x,
                y,
                w,
                h,
                asset,
                visible = visible,
                text = text
            )

        /**
         * 공개 메서드 `cell`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun cell(path: String, x: Float, y: Float, w: Float, h: Float) =
            event(path, "nine-patch", x, y, w, h, "maps/ui/start-battle/box2.png")

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float = 43f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(path: String, value: String, x: Float, y: Float, w: Float, h: Float = 43f) =
            event(path, "text", x, y, w, h, "font:body", value)

        /**
         * 공개 메서드 `button`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float = 51.6f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun button(path: String, value: String, x: Float, y: Float, w: Float, h: Float = 51.6f) {
            event(path, "nine-patch", x, y, w, h, "maps/ui/start-battle/button.png")
            label("$path/Label", value, x, y, w, h)
        }
        event("bg1/logo9", "tiled-sprite", root[0], root[1], root[2], root[3], "maps/ui/start-battle/logo9.png")
        event("bg1", "nine-patch", root[0], root[1], root[2], root[3], "maps/ui/start-battle/box1.png")
        when (kind) {
            HallInfo.FORCES -> {
                event(
                    "bg1/title",
                    "nine-patch",
                    root[0],
                    root[1] + root[3] - 51.6f,
                    root[2],
                    51.6f,
                    "maps/ui/start-battle/title.png"
                )
                label("bg1/title/label", "부대 정보 일람", root[0], root[1] + root[3] - 51.6f, root[2])
                val widths = floatArrayOf(120f, 151f, 85f, 137f, 87f, 84f, 84f, 84f, 84f, 84f)
                val headers = listOf("무장명", "부대 속성", "레벨", "체력", "체력", "공격", "방어", "정신", "폭발", "사기")
                var cx = root[0] + 5f
                headers.forEachIndexed { index, value ->
                    cell("bg1/header/$index", cx, root[1] + root[3] - 101f, widths[index], 48f)
                    label("bg1/header/$index/label", value, cx, root[1] + root[3] - 101f, widths[index], 48f)
                    cx += widths[index]
                }
                campaign.joinedUnits.take(7).forEachIndexed { row, id ->
                    val unit = gameDataCatalog.unitProfile(id) ?: return@forEachIndexed
                    val level = campaign.unitAttribute(id, 18, unit.level)
                    val profile = gameDataCatalog.battleProfile(
                        id,
                        (level - 1).coerceAtLeast(0),
                        campaign.unitAttribute(id, 17, unit.posts)
                    ) ?: return@forEachIndexed
                    val bonus = campaign.inventory.equipment[id]?.let {
                        gameDataCatalog.equipmentBonus(
                            it.asScriptValues(),
                            profile.level
                        )
                    } ?: GameDataCatalog.EquipmentBonus()
                    val values = listOf(
                        campaign.unitNames[id] ?: GameDataCatalog.sayLayerUnitName(unit.name),
                        profile.arm.name,
                        profile.level.toString(),
                        "${profile.maxHitPoints}/${profile.maxHitPoints}",
                        "${profile.maxMagicPoints}/${profile.maxMagicPoints}",
                        (profile.attack + bonus.attack).toString(),
                        (profile.defense + bonus.defense).toString(),
                        (profile.spirit + bonus.spirit).toString(),
                        profile.critical.toString(),
                        profile.morale.toString(),
                    )
                    cx = root[0] + 5f
                    val ry = root[1] + root[3] - 150f - row * 49f
                    values.forEachIndexed { column, value ->
                        cell("bg1/content/row$row/cell$column", cx, ry, widths[column], 49f)
                        label("bg1/content/row$row/cell$column/label", value, cx, ry, widths[column], 49f)
                        cx += widths[column]
                    }
                }
                button("bg1/button7", "폐쇄", root[0] + root[2] - 164f, root[1] + 5f, 155f)
            }

            HallInfo.PROPERTY -> {
                event(
                    "bg1/title",
                    "nine-patch",
                    root[0],
                    root[1] + root[3] - 51.6f,
                    root[2],
                    51.6f,
                    "maps/ui/start-battle/title.png"
                )
                label("bg1/title/label", "창고 일람", root[0], root[1] + root[3] - 51.6f, root[2])
                val widths = floatArrayOf(323.06f, 168.13f, 91.24f, 87.69f, 176.44f)
                val headers = listOf("이름", "속성", "레벨", "경험치", "소지자")
                var cx = root[0] + 5f
                headers.forEachIndexed { index, value ->
                    cell("bg1/header/$index", cx, root[1] + root[3] - 101f, widths[index], 48f)
                    label("bg1/header/$index/label", value, cx, root[1] + root[3] - 101f, widths[index], 48f)
                    cx += widths[index]
                }
                campaign.joinedUnits.forEach { campaign.inventory.ensureDefaultEquipment(it, gameDataCatalog) }
                /**
                 * data class  `TraceRow`
                 *
                 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
                 *
                 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
                 */

                data class TraceRow(val id: Int, val count: Int, val level: String, val exp: String, val owner: String)

                /**
                 * 공개 메서드 `accepts`
                 *
                 * ### 파라미터
                - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Boolean`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun accepts(id: Int): Boolean {
                    val item = gameDataCatalog.equipmentProfile(id) ?: return false
                    return when (hallPropertyTab) {
                        HallPropertyTab.WEAPON -> item.itemType < 20
                        HallPropertyTab.ARMOR -> item.itemType in 20..25
                        HallPropertyTab.AUXILIARY -> item.itemType > 45 && id < 150
                        HallPropertyTab.PROPERTY -> id >= 150 || item.itemType in 26..45
                    }
                }

                val equipped =
                    if (hallPropertyTab == HallPropertyTab.PROPERTY) emptyList() else campaign.inventory.equippedItems()
                        .filter { accepts(it.itemId) }
                val rows = equipped.sortedWith(compareBy<CampaignEquippedItem> { it.itemId }.thenBy { it.unitId }).map {
                    TraceRow(
                        it.itemId, 1, if (hallPropertyTab == HallPropertyTab.AUXILIARY) "---" else it.level.toString(),
                        if (hallPropertyTab == HallPropertyTab.AUXILIARY) "---" else it.experience.toString(),
                        campaign.unitNames[it.unitId] ?: gameDataCatalog.unitProfile(it.unitId)?.name.orEmpty()
                    )
                } + campaign.inventory.items.entries.sortedBy { it.key }.filter { accepts(it.key) }.map { (id, count) ->
                    TraceRow(
                        id,
                        count,
                        if (hallPropertyTab >= HallPropertyTab.AUXILIARY) "---" else (campaign.inventory.itemLevels(id)
                            .firstOrNull() ?: 1).toString(),
                        if (hallPropertyTab >= HallPropertyTab.AUXILIARY) "---" else (campaign.inventory.itemExperiences(
                            id
                        ).firstOrNull() ?: 0).toString(),
                        "창고"
                    )
                }
                rows.take(7).forEachIndexed { row, entry ->
                    val item = gameDataCatalog.equipmentProfile(entry.id) ?: return@forEachIndexed
                    val ry = root[1] + root[3] - 166f - row * 67.08f
                    val values = listOf(
                        item.name + if (entry.count > 1) " ×${entry.count}" else "",
                        gameDataCatalog.equipmentTypeName(item.itemType),
                        entry.level,
                        entry.exp,
                        entry.owner
                    )
                    cx = root[0] + 5f
                    values.forEachIndexed { column, value ->
                        cell("bg1/content/row$row/cell$column", cx, ry, widths[column], 65.36f)
                        if (column == 0) event(
                            "bg1/content/row$row/icon",
                            "sprite",
                            cx + 6f,
                            ry + 7f,
                            50f,
                            50f,
                            "maps/item-icons/${item.icon}.png"
                        )
                        label(
                            "bg1/content/row$row/cell$column/label",
                            value,
                            if (column == 0) cx + 63f else cx,
                            ry,
                            if (column == 0) widths[column] - 69f else widths[column],
                            65.36f
                        )
                        cx += widths[column]
                    }
                }
                listOf("무기", "방어구", "보조", "아이템").forEachIndexed { index, value ->
                    val centerX = 244.23f + index * 127.28f
                    event(
                        "bg1/tab$index/radio", "sprite", centerX - 13.76f, 58.12f, 27.52f, 27.52f,
                        "maps/ui/title/setting/radio-${if (hallPropertyTab.ordinal == index) "on" else "off"}.png"
                    )
                    label("bg1/tab$index/label", value, centerX + 29.7f, 58.12f, 95f)
                }
                button("bg1/button7", "확인", root[0] + root[2] - 135f, root[1] + 5f, 125f)
            }

            HallInfo.TERRAIN -> {
                event(
                    "bg1/title",
                    "nine-patch",
                    root[0],
                    root[1] + root[3] - 51.6f,
                    root[2],
                    51.6f,
                    "maps/ui/start-battle/title.png"
                )
                label("bg1/title/label", "지형 정보 일람", root[0] + 8f, root[1] + root[3] - 51.6f, root[2] - 16f)
                val leftW = 264.02f
                val columnW = 51.6f
                val rowX = root[0] + 13.16f
                cell("bg1/header/name", rowX, root[1] + root[3] - 87f, leftW, 42f)
                label("bg1/header/name/label", "이름", rowX, root[1] + root[3] - 87f, leftW, 42f)
                val headers = listOf("마왕", "보병", "기병", "궁기", "포차", "무술", "군주", "보병", "기병", "궁기", "포차", "무술", "무술")
                headers.forEachIndexed { index, value ->
                    val hx = root[0] + 201.12f + index * columnW
                    cell("bg1/header/arm$index", hx, root[1] + root[3] - 87f, columnW, 42f)
                    label("bg1/header/arm$index/label", value, hx, root[1] + root[3] - 87f, columnW, 42f)
                }
                val names = listOf("평원", "초원", "숲", "황지", "산지", "암산")
                gameDataCatalog.terrainLayer().select(hallTerrainTab).rows.take(6).forEachIndexed { row, terrain ->
                    val ry = root[1] + root[3] - 148.44f - row * 64.5f
                    event(
                        "bg1/content/row$row",
                        "nine-patch",
                        rowX,
                        ry,
                        854.07f,
                        64.5f,
                        "maps/ui/terrain-layer/row-${if (row % 2 == 0) "even" else "odd"}.png"
                    )
                    event(
                        "bg1/content/row$row/icon",
                        "sprite",
                        root[0] + 15f,
                        ry + 3f,
                        58.48f,
                        58.48f,
                        "maps/terrain-icons/${terrain.iconIndex}.png"
                    )
                    label(
                        "bg1/content/row$row/name",
                        names.getOrElse(row) { terrain.terrainName.trim() },
                        root[0] + 88f,
                        ry,
                        108f,
                        64.5f
                    )
                    terrain.enabledSkills.forEachIndexed { index, enabled ->
                        event(
                            "bg1/content/row$row/skill$index",
                            "sprite",
                            root[0] + 81.58f + index * 28.38f,
                            ry + 5.59f,
                            25.8f,
                            25.8f,
                            "maps/ui/terrain-layer/skill${index + 1}${if (enabled) "" else "-disabled"}.png"
                        )
                    }
                    terrain.values.take(13).forEachIndexed { column, value ->
                        label(
                            "bg1/content/row$row/value$column",
                            value.text,
                            root[0] + 201.12f + column * columnW,
                            ry,
                            columnW,
                            64.5f
                        )
                    }
                }
                button("bg1/button0", "지형 효과", 245.48f, 95.46f, 169.16f)
                button("bg1/button1", "기동력 소모", 422.64f, 95.46f, 191.52f)
                button("bg1/button7", "확인", 1001.72f, 95.46f, 103.2f)
            }

            HallInfo.TREASURE -> {
                event(
                    "bg1/title",
                    "nine-patch",
                    root[0],
                    root[1] + root[3] - 51.6f,
                    root[2],
                    51.6f,
                    "maps/ui/start-battle/title.png"
                )
                label("bg1/title/label", "보물 도감", root[0] + 8f, root[1] + root[3] - 51.6f, root[2] - 16f)
                gameDataCatalog.treasureProfiles().take(6).forEachIndexed { index, item ->
                    val x = 232.10f + index % 2 * 410.22f
                    val y = 413.23f - index / 2 * 165.98f
                    cell("bg1/content/item$index", x, y, 405.06f, 163.40f)
                    cell("bg1/content/item$index/icon-frame", x + 9.94f, y + 36.55f, 97.18f, 90.3f)
                    val found = item.id in campaign.inventory.discoveredTreasures
                    event(
                        "bg1/content/item$index/icon", "sprite", x + 32.7f, y + 54f, 45.5f, 42.2f,
                        "maps/item-icons/${item.icon}.png", visible = found
                    )
                    label(
                        "bg1/content/item$index/label",
                        if (found) item.name else "발견되지 않음",
                        x + 115.5f,
                        y + 103.5f,
                        282.9f,
                        43f
                    )
                }
                label(
                    "bg1/footer",
                    "지금까지 발견한 보물 ${
                        campaign.inventory.discoveredTreasures.size.toString().padStart(2, '0')
                    } / ${gameDataCatalog.treasureProfiles().size}",
                    root[0] + 8f,
                    root[1],
                    520f
                )
                button("bg1/button7", "종료", 921.05f, 78.22f, 129.52f, 44.29f)
            }

            HallInfo.HELPER -> {
                event(
                    "bg1/title",
                    "nine-patch",
                    root[0],
                    root[1] + root[3] - 62f,
                    root[2],
                    62f,
                    "maps/ui/start-battle/title.png"
                )
                label("bg1/title/label", "역사 정보", root[0] + 8f, root[1] + root[3] - 62f, root[2] - 16f, 62f)
                cell("bg1/content", root[0] + 12f, root[1] + 82f, root[2] - 24f, root[3] - 151f)
                val help =
                    "6 [단축키 설명]\n☆ 일부 단축키 기능은 메뉴 — 설정을 통해 직접 설정할 수 있습니다.\n☆ 번호 0-4: 단계 속도 변화. 0가 원래 속도이며, 1-4가 가속.\n☆ 번호 5: 진영에 따라 다른 색상의 체력 바를 표시합니다.\n☆ 번호 6: 문자 BUFF와 DEBUFF를 표시합니다.\n☆ 번호 7: 왼쪽 하단에 캐릭터 능력과 장비를 표시합니다.\n☆ 숫자 8: 더블 히트의 치명타 확률과 카운터 관계를 표시합니다.\n☆ 번호 9: 지형 적응 및 이동 비용을 표시합니다.\n☆ 문자 A: 턴 시작 시 자동으로 저장됩니다.\n☆ 문자 B: 속성 인터페이스는 모든 가능한 전략과 학습 수준을 표시합니다."
                label("bg1/content/label", help, root[0] + 18f, root[1] + 82f, root[2] - 36f, root[3] - 172f)
                button("bg1/button7", "확인", root[0] + root[2] - 145f, root[1] + 7f, 135f)
            }
        }
    }

    /** ForcesListLayer's authored traversal, including rows before table headers. */
    private fun appendSourceForcesRenderEvents(log: RenderEventLog) {
        val scale = .86f

        /**
         * 공개 메서드 `draw`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun draw(path: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") {
            val type = when {
                text.isNotEmpty() -> "label"
                asset == "Logo_9-1" -> "tiled-sprite"
                asset in setOf(
                    "box1",
                    "box2",
                    "box3",
                    "box4",
                    "bg2",
                    "885a69b4-08ed-4c78-8896-ffb04eb2bd20"
                ) -> "sliced-sprite"

                else -> "sprite"
            }
            log.draw(
                "hall-forces-stable",
                "ForcesListLayer",
                path,
                type,
                x * scale,
                y * scale,
                w * scale,
                h * scale,
                asset,
                blend = if (text.isEmpty()) listOf(770, 771) else listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
                text = text
            )
        }
        draw("Canvas/Layer/bg1", 165.686f, 79.5f, 1157f, 641f, "box3")
        draw("Canvas/Layer/bg1/bg1", 165.686f, 79.5f, 1157f, 641f, "Logo_9-1")
        draw("Canvas/Layer/bg1/bg1/bg1", 165.686f, 670.5f, 1157f, 50f, "bg1")
        draw("Canvas/Layer/bg1/bg1/bg1/label", 629.271f, 670.3f, 229.83f, 50.4f, text = "부대 정보 일람")
        draw("Canvas/Layer/bg1/bg1/bg1", 165.686f, 79.5f, 1157f, 641f, "box1")
        draw("Canvas/Layer/bg1/bg1/box2", 169.686f, 139.5f, 1149f, 527f, "box2")

        val rows = listOf(
            listOf("조조", "군웅", "3", "123/123", "36/36", "60", "68", "55", "49", "51"),
            listOf("허자장", "풍수사", "3", "115/115", "112/112", "33", "41", "49", "36", "36"),
            listOf("병사 ", "경보병", "3", "127/127", "103/103", "46", "49", "36", "36", "36"),
        )
        val labelX = floatArrayOf(
            180.186f,
            315.186f,
            486.286f,
            584.136f,
            739.486f,
            842.786f,
            939.786f,
            1035.786f,
            1132.786f,
            1228.786f
        )
        val labelW = floatArrayOf(120f, 160f, 86.8f, 147.3f, 92.2f, 82.8f, 82.8f, 82.8f, 82.8f, 82.8f)
        rows.forEachIndexed { row, values ->
            val item = if (row % 2 == 0) "item0" else "item1"
            val path = "Canvas/Layer/bg1/bg1/box2/scrollview/view/content/$item"
            val y = 544.85f - row * 62f
            draw(path, 171.686f, y, 1145f, 60f, if (row % 2 == 0) "bg2" else "885a69b4-08ed-4c78-8896-ffb04eb2bd20")
            values.forEachIndexed { column, value ->
                draw("$path/label$column", labelX[column], y + 4.8f, labelW[column], 50.4f, text = value)
            }
        }

        val headers = listOf(
            floatArrayOf(172.286f, 605.5f, 133.8f, 60f, 187.286f, 610.3f, 103.8f) to "무장명",
            floatArrayOf(307.016f, 605.5f, 174.7f, 60f, 319.611f, 610.3f, 149.51f) to "부대 속성",
            floatArrayOf(482.42f, 605.5f, 96.1f, 60f, 495.87f, 610.3f, 69.2f) to "레벨",
            floatArrayOf(579.086f, 606f, 157f, 60f, 622.986f, 610.8f, 69.2f) to "체력",
            floatArrayOf(736.236f, 606f, 99.9f, 60f, 751.586f, 610.8f, 69.2f) to "체력",
            floatArrayOf(836.136f, 606f, 96.1f, 60f, 849.586f, 610.8f, 69.2f) to "공격",
            floatArrayOf(933.136f, 606f, 96.1f, 60f, 946.586f, 610.8f, 69.2f) to "방어",
            floatArrayOf(1029.136f, 606f, 96.1f, 60f, 1042.586f, 610.8f, 69.2f) to "정신",
            floatArrayOf(1126.136f, 606f, 96.1f, 60f, 1139.586f, 610.8f, 69.2f) to "폭발",
            floatArrayOf(1222.136f, 606f, 96.1f, 60f, 1235.586f, 610.8f, 69.2f) to "사기",
        )
        headers.forEach { (g, value) ->
            draw("Canvas/Layer/bg1/bg1/box2/box3", g[0], g[1], g[2], g[3], "box3")
            draw("Canvas/Layer/bg1/bg1/box2/box3/label", g[4], g[5], g[6], 50.4f, text = value)
        }
        listOf(
            304.213f,
            477.727f,
            575.217f,
            733.131f,
            833.195f,
            928.965f,
            1025.903f,
            1122.841f,
            1218.611f
        ).forEach { x ->
            draw("Canvas/Layer/bg1/bg1/box2/vline", x, 141.345f, 6f, 464.25f, "vline")
        }
        draw("Canvas/Layer/bg1/button0/Background", 1129.071f, 85.823f, 180f, 50f, "box3")
        draw("Canvas/Layer/bg1/button0/Background/Label", 1169.071f, 90.823f, 100f, 40f, text = "폐쇄")
    }

    /** TerrainLayer's full 28-row authored scroll content and header traversal. */
    private fun appendSourceTerrainRenderEvents(log: RenderEventLog) {
        val scale = .86f
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", visible: Boolean = true,
        ) = log.draw(
            "hall-terrain-stable", "TerrainLayer", path, type,
            x * scale, y * scale, w * scale, h * scale, asset,
            blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
            visible = visible, text = text,
        )

        /**
         * 공개 메서드 `sprite`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `visible` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `sliced` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun sprite(
            path: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String,
            visible: Boolean = true,
            sliced: Boolean = false
        ) =
            draw(path, if (sliced) "sliced-sprite" else "sprite", x, y, w, h, asset, visible = visible)

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `visible` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(path: String, value: String, x: Float, y: Float, w: Float, h: Float, visible: Boolean = true) =
            draw(path, "label", x, y, w, h, text = value, visible = visible)

        draw("Canvas/Layer/bg", "tiled-sprite", 274.236f, 100f, 1021.1f, 600f, "Logo_9-1")
        sprite("Canvas/Layer/bg/box1", 274.236f, 100f, 1021.1f, 600f, "box1", sliced = true)
        sprite("Canvas/Layer/bg/bg1", 274.236f, 650f, 1021.1f, 50f, "bg1")
        label("Canvas/Layer/bg/bg1/label", "지형 정보 일람", 282.086f, 649.8f, 229.83f, 50.4f)
        sprite("Canvas/Layer/bg/panel", 285.538f, 183.098f, 1001.1f, 459.3f, "box4", sliced = true)

        val names = listOf(
            "평원", "초원", "숲", "황지", "산지", "암산", "절벽", "설원", "다리", "얕은 물가", "늪지대", "연못", "작은 강", "대하",
            "울타리", "성벽", "성내", "성문", "성채", "관문", "사슴덧", "마을", "병영", "민가", "보물 창고", "연못", "화염", "배",
        )
        val nameWidths = floatArrayOf(
            62.28f, 62.28f, 31.14f, 62.28f, 62.28f, 62.28f, 62.28f, 62.28f, 62.28f,
            134.56f, 93.42f, 62.28f, 103.42f, 62.28f, 93.42f, 62.28f, 62.28f, 62.28f, 62.28f, 62.28f,
            93.42f, 62.28f, 62.28f, 62.28f, 134.56f, 62.28f, 62.28f, 31.14f
        )
        val terrainValues = listOf(
            "◎◎◎○○◎◎◎◎◎◎◎◎", "○◎◎○○◎◎◎◎◎◎○◎", "★◎◎★◎★◎○◎◎○★◎", "★◎◎★◎★◎○★★○★★",
            "★◎◎★★★★○★★○★★", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎★◎◎", "◎◎◎◎◎◎★★★◎★◎★",
            "○◎◎○○○◎◎◎◎◎○◎", "★◎◎★★★◎★◎◎★★○", "★◎◎★★★◎★★★★★○", "◎◎◎◎◎◎◎◎◎◎◎◎◎",
            "◎◎◎◎◎◎◎◎◎◎◎◎◎", "★◎◎★★◎○★★◎★★○", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎",
            "◎○○◎◎○◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "○○○○○○○○○○○○○", "○○○○○○○○○○○○○",
            "○○○○○○○○○○○○○", "○○○○○○○○○○○○○", "○○○○○○○○○○○○○", "◎○○★★◎○○○○○★○",
            "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎",
        )
        val valueX = floatArrayOf(
            516.463f, 576.463f, 636.463f, 696.463f, 756.463f, 816.463f, 875.463f,
            935.463f, 995.463f, 1055.463f, 1115.463f, 1175.463f, 1235.463f
        )
        names.indices.forEach { row ->
            val even = row % 2 == 0
            val item = if (even) "item0" else "item1"
            val path = "Canvas/Layer/bg/panel/scrollview0/view/content/$item"
            val y = 527.398f - row * 75f
            val itemVisible = row <= 8
            val childVisible = row <= 7
            sprite(
                path, 289.538f, y, 993.1f, 75f,
                if (even) "885a69b4-08ed-4c78-8896-ffb04eb2bd20" else "bg2", itemVisible, sliced = true
            )
            val childX = if (even) 292.488f else 292.919f
            sprite("$path/icon", childX, y + 3.9f, 67.2f, 67.2f, row.toString(), childVisible)
            val nameX = if (even) 376.088f else 375.651f
            val nameY = y + if (even) 34.82f else 34.598f
            label("$path/label", names[row], nameX, nameY, nameWidths[row], 45.36f, itemVisible)
            repeat(4) { skill ->
                sprite(
                    "$path/skill/skill_$skill",
                    369.088f + skill * 33f,
                    y + 6.5f,
                    30f,
                    30f,
                    "${skill + 1}-1",
                    childVisible
                )
            }
            terrainValues[row].forEachIndexed { column, symbol ->
                val narrow = symbol == '○'
                val x = valueX[column] + if (narrow) 6.525f else 0f
                val w = if (narrow) 30.2f else 43.25f
                val fractionalHeight = !even && column in setOf(1, 2, 4, 5, 7, 8, 10, 11, 12)
                label(
                    "$path/label$column",
                    symbol.toString(),
                    x,
                    y + 6f,
                    w,
                    if (fractionalHeight) 63.001f else 63f,
                    childVisible
                )
            }
        }
        listOf(
            505.588f, 564.788f, 624.288f, 684.788f, 745.288f, 804.788f, 865.488f, 924.888f,
            984.388f, 1044.488f, 1103.888f, 1103.888f, 1164.588f, 1223.588f
        ).forEach { x ->
            sprite("Canvas/Layer/bg/panel/vline", x, 189.448f, 6f, 448.6f, "vline")
        }
        /**
         * data class  `Header`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Header(val id: String, val x: Float, val y: Float, val text: String)

        val headers = listOf(
            Header("button", 285.588f, 602.358f, "이름"), Header("button0", 508.088f, 602.358f, "마왕"),
            Header("button1", 568.397f, 602.183f, "보병"), Header("button2", 628.116f, 602.183f, "기병"),
            Header("button3", 688.268f, 602.183f, "궁기"), Header("button4", 748.137f, 602.183f, "포차"),
            Header("button5", 808.101f, 602.183f, "무술"), Header("button11", 1167.145f, 602.358f, "무술"),
            Header("button10", 1107.125f, 602.183f, "포차"), Header("button9", 1047.297f, 602.183f, "궁기"),
            Header("button8", 987.145f, 602.183f, "기병"), Header("button7", 927.426f, 602.183f, "보병"),
            Header("button6", 867.443f, 602.183f, "군주"), Header("button12", 1227.088f, 602.358f, "무술"),
        )
        headers.forEachIndexed { index, h ->
            val width = if (index == 0) 223f else 60f
            val labelX = if (index == 0) 347.088f else h.x - 20f
            val path = "Canvas/Layer/bg/panel/${h.id}/Background"
            sprite(path, h.x, h.y, width, 40f, "box4", sliced = true)
            label("$path/Label", h.text, labelX, h.y, 100f, 40f)
        }
        val buttons = listOf(
            floatArrayOf(285.436f, 110.1f, 196.7f, 61.8f, 301.386f, 121f, 164.8f, 40f) to "지형 효과",
            floatArrayOf(491.436f, 109.4f, 222.7f, 63.2f, 498.186f, 116.2f, 209.2f, 49.6f) to "기동력 소모",
            floatArrayOf(1164.786f, 111f, 120f, 60f, 1174.786f, 121f, 100f, 40f) to "확인",
        )
        buttons.forEachIndexed { index, (g, value) ->
            val path = "Canvas/Layer/bg/button$index/Background"
            sprite(path, g[0], g[1], g[2], g[3], "box3", sliced = true)
            label("$path/Label", value, g[4], g[5], g[6], g[7])
        }
    }

    /** HelperLayer's rich-text expansion exactly as authored by Cocos. */
    private fun appendSourceHelperRenderEvents(log: RenderEventLog) {
        val scale = .86f

        /**
         * 공개 메서드 `draw`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `visible` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun draw(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            visible: Boolean = true
        ) =
            log.draw(
                "hall-helper-stable", "HelperLayer", path, type, x * scale, y * scale, w * scale, h * scale, asset,
                blend = if (type == "label" || type == "rich-text") listOf(
                    "SRC_ALPHA",
                    "ONE_MINUS_SRC_ALPHA"
                ) else listOf(770, 771),
                visible = visible, text = text
            )
        draw("Canvas/Layer/Logo_12-1", "tiled-sprite", 147.686f, 24.5f, 1193.0f, 751.0f, "Logo_9-1", "", true)
        draw("Canvas/Layer/Logo_12-1/box4", "sliced-sprite", 147.686f, 24.5f, 1193.0f, 751.0f, "box4", "", true)
        draw("Canvas/Layer/Logo_12-1/bg1", "sprite", 147.686f, 715.5f, 1193.0f, 60.0f, "bg1", "", true)
        draw("Canvas/Layer/Logo_12-1/bg1/box3", "sliced-sprite", 147.686f, 715.5f, 1193.0f, 60.0f, "box3", "", true)
        draw("Canvas/Layer/Logo_12-1/bg1/label", "label", 155.139f, 721.3f, 151.51f, 52.4f, null, "역사 정보", true)
        draw(
            "Canvas/Layer/Logo_12-1/scrollview",
            "tiled-sprite",
            163.686f,
            99.0f,
            1161.0f,
            616.0f,
            "Logo_12-1",
            "",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/box2",
            "tiled-sprite",
            163.686f,
            99.0f,
            1161.0f,
            616.0f,
            "box2",
            "",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext",
            "rich-text",
            165.686f,
            -500.0f,
            1157.0f,
            1213.0f,
            null,
            "<color=#000000>6<color=#0000ab> [단축키 설명</color>]<br/><color=#0000ab>☆</color>의 일부 단축키 기능은 <color=#c30000> 메뉴 — 설정</color>을 통해 직접 설정할 수 있습니다.<br/><color=#0000ab>☆</color> 번호 <color=#c30000>0-4</color>: 단계 속도 변화; <color=#0000ab>0</color>가 원래 속도이며, <color=#0000ab>1-4</color>가 가속, <color=#0000ab>1</color>이 가장 빠릅니다.<br/><color=#0000ab>☆</color> 번호 <color=#c30000>5</color>: 진영에 따라 다른 색상의 체력 바를 표시합니다.<br/><color=#0000ab>☆</color> 번호 <color=#c30000>6</color>: 문자 <color=#0000ab>BUFF</color>와 <color=#c30000>DEBUFF</color>를 표시합니다.<br/><color=#0000ab>☆</color> 번호 <color=#c30000>7</color>: 왼쪽 하단에 캐릭터 능력과 장비를 표시합니다.<br/><color=#0000ab>☆</color> 숫자 <color=#c30000>8</color>: 더블 히트의 치명타 확률과 카운터 관계를 표시합니다.<br/><color=#0000ab>☆</color> 번호 <color=#c30000>9</color>: 지형 적응 및 이동 비용을 표시합니다.<br/><color=#0000ab>☆</color> 문자 <color=#c30000>A</color>: 턴 시작 시 자동으로 저장되며, 저장 시퀀스 881-900.<br/><color=#0000ab>☆</color> 문자 <color=#c30000>B</color>: 속성 인터페이스는 모든 가능한 전략과 학습 수준을 표시합니다.<br/><color=#0000ab>☆</color> 글자 <color=#c30000>C</color>: 공격자의 스트로크를 표시한다.<br/><color=#0000ab>☆</color> 문자 <color=#c30000>D</color>: 자동 장착 시 강제 배치 불가의 장비를 제거하지 않습니다.<br/><color=#0000ab>☆</color> 문자 <color=#c30000>E</color>: 아군 클릭 후 제어 불가능한 유닛 행동 명령이 표시됩니다.<br/><color=#0000ab>☆</color> 편지 <color=#c30000>F</color>: 통제 불가능한 유닛을 클릭하면 다른 유닛에 대한 증오 수치를 표시한다.<br/><color=#0000ab>☆</color> 글자 <color=#c30000>G</color>: 아군을 클릭한 후 빨간색으로 표시하고 적군 수 있습니다.<br/><color=#0000ab>☆</color> 문자 <color=#c30000>H</color>: 업그레이드 정보를 표시합니다.<br/><color=#0000ab>☆</color> 문자 <color=#c30000>Z</color>: 초가속, 그리고 Z 버튼을 눌러서 취소.<br/><color=#0000ab>☆</color> <color=#c30000>Shift</color>: 스토리 대사를 전속력으로 건너뛰고, 숫자 0을 초기화하세요.<br/><color=#0000ab>☆</color> <color=#c30000> 화살표 키</color>: S가 전장에 있을 때 창 화면 크기를 변경합니다.<br/><color=#0000ab>☆</color> <color=#c30000> 왼쪽 키보드 + - 키</color>: BGM 볼륨 조절.</color><br/>",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            650.0f,
            22.25f,
            63.0f,
            null,
            "6",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            187.936f,
            650.0f,
            206.34f,
            63.0f,
            null,
            " [단축키 설명",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            394.276f,
            650.0f,
            11.11f,
            63.0f,
            null,
            "]",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            600.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            600.0f,
            355.85f,
            63.0f,
            null,
            "의 일부 단축키 기능은 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            556.136f,
            600.0f,
            211.74f,
            63.0f,
            null,
            " 메뉴 — 설정",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            767.876f,
            600.0f,
            516.48f,
            63.0f,
            null,
            "을 통해 직접 설정할 수 있습니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            550.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            550.0f,
            91.43f,
            63.0f,
            null,
            " 번호 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            550.0f,
            57.81f,
            63.0f,
            null,
            "0-4",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            349.526f,
            550.0f,
            274.28f,
            63.0f,
            null,
            ": 단계 속도 변화; ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            623.806f,
            550.0f,
            22.25f,
            63.0f,
            null,
            "0",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            646.056f,
            550.0f,
            286.65f,
            63.0f,
            null,
            "가 원래 속도이며, ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            932.706f,
            550.0f,
            57.81f,
            63.0f,
            null,
            "1-4",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            990.516f,
            550.0f,
            137.14f,
            63.0f,
            null,
            "가 가속, ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            1127.656f,
            550.0f,
            22.25f,
            63.0f,
            null,
            "1",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            1149.906f,
            550.0f,
            160.63f,
            63.0f,
            null,
            "이 가장 빠",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            500.0f,
            114.91f,
            63.0f,
            null,
            "릅니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            450.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            450.0f,
            91.43f,
            63.0f,
            null,
            " 번호 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            450.0f,
            22.25f,
            63.0f,
            null,
            "5",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            313.966f,
            450.0f,
            757.42f,
            63.0f,
            null,
            ": 진영에 따라 다른 색상의 체력 바를 표시합니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            400.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            400.0f,
            91.43f,
            63.0f,
            null,
            " 번호 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            400.0f,
            22.25f,
            63.0f,
            null,
            "6",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            313.966f,
            400.0f,
            102.54f,
            63.0f,
            null,
            ": 문자 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            416.506f,
            400.0f,
            104.43f,
            63.0f,
            null,
            "BUFF",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            520.936f,
            400.0f,
            45.71f,
            63.0f,
            null,
            "와 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            566.646f,
            400.0f,
            160.0f,
            63.0f,
            null,
            "DEBUFF",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            726.646f,
            400.0f,
            229.83f,
            63.0f,
            null,
            "를 표시합니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            350.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            350.0f,
            91.43f,
            63.0f,
            null,
            " 번호 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            350.0f,
            22.25f,
            63.0f,
            null,
            "7",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            313.966f,
            350.0f,
            746.31f,
            63.0f,
            null,
            ": 왼쪽 하단에 캐릭터 능력과 장비를 표시합니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            300.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            300.0f,
            91.43f,
            63.0f,
            null,
            " 숫자 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            300.0f,
            22.25f,
            63.0f,
            null,
            "8",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            313.966f,
            300.0f,
            861.22f,
            63.0f,
            null,
            ": 더블 히트의 치명타 확률과 카운터 관계를 표시합니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            250.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            250.0f,
            91.43f,
            63.0f,
            null,
            " 번호 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            250.0f,
            22.25f,
            63.0f,
            null,
            "9",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            313.966f,
            250.0f,
            607.91f,
            63.0f,
            null,
            ": 지형 적응 및 이동 비용을 표시합니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            200.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            200.0f,
            91.43f,
            63.0f,
            null,
            " 문자 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            200.0f,
            26.68f,
            63.0f,
            null,
            "A",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            318.396f,
            200.0f,
            857.24f,
            63.0f,
            null,
            ": 턴 시작 시 자동으로 저장되며, 저장 시퀀스 881-900.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            150.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            150.0f,
            91.43f,
            63.0f,
            null,
            " 문자 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            150.0f,
            26.68f,
            63.0f,
            null,
            "B",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            318.396f,
            150.0f,
            999.62f,
            63.0f,
            null,
            ": 속성 인터페이스는 모든 가능한 전략과 학습 수준을 표시합니다",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            100.0f,
            11.11f,
            63.0f,
            null,
            ".",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            50.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            50.0f,
            91.43f,
            63.0f,
            null,
            " 글자 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            50.0f,
            28.89f,
            63.0f,
            null,
            "C",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            320.606f,
            50.0f,
            505.37f,
            63.0f,
            null,
            ": 공격자의 스트로크를 표시한다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            0.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            0.0f,
            91.43f,
            63.0f,
            null,
            " 문자 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            0.0f,
            28.89f,
            63.0f,
            null,
            "D",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            320.606f,
            0.0f,
            918.05f,
            63.0f,
            null,
            ": 자동 장착 시 강제 배치 불가의 장비를 제거하지 않습니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -50.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            -50.0f,
            91.43f,
            63.0f,
            null,
            " 문자 ",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            -50.0f,
            26.68f,
            63.0f,
            null,
            "E",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            318.396f,
            -50.0f,
            918.05f,
            63.0f,
            null,
            ": 아군 클릭 후 제어 불가능한 유닛 행동 명령이 표시됩니다.",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -100.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            -100.0f,
            91.43f,
            63.0f,
            null,
            " 편지 ",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            -100.0f,
            24.43f,
            63.0f,
            null,
            "F",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            316.146f,
            -100.0f,
            987.25f,
            63.0f,
            null,
            ": 통제 불가능한 유닛을 클릭하면 다른 유닛에 대한 증오 수치를 ",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -150.0f,
            149.51f,
            63.0f,
            null,
            "표시한다.",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -200.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            -200.0f,
            91.43f,
            63.0f,
            null,
            " 글자 ",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            -200.0f,
            31.11f,
            63.0f,
            null,
            "G",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            322.826f,
            -200.0f,
            906.93f,
            63.0f,
            null,
            ": 아군을 클릭한 후 빨간색으로 표시하고 적군 수 있습니다.",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -250.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            -250.0f,
            91.43f,
            63.0f,
            null,
            " 문자 ",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            -250.0f,
            28.89f,
            63.0f,
            null,
            "H",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            320.606f,
            -250.0f,
            505.37f,
            63.0f,
            null,
            ": 업그레이드 정보를 표시합니다.",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -300.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            -300.0f,
            91.43f,
            63.0f,
            null,
            " 문자 ",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.716f,
            -300.0f,
            24.43f,
            63.0f,
            null,
            "Z",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            316.146f,
            -300.0f,
            608.85f,
            63.0f,
            null,
            ": 초가속, 그리고 Z 버튼을 눌러서 취소.",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -350.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            -350.0f,
            11.11f,
            63.0f,
            null,
            " ",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            211.396f,
            -350.0f,
            80.04f,
            63.0f,
            null,
            "Shift",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            291.436f,
            -350.0f,
            963.78f,
            63.0f,
            null,
            ": 스토리 대사를 전속력으로 건너뛰고, 숫자 0을 초기화하세요.",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -400.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            -400.0f,
            11.11f,
            63.0f,
            null,
            " ",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            211.396f,
            -400.0f,
            160.63f,
            63.0f,
            null,
            " 화살표 키",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            372.026f,
            -400.0f,
            760.61f,
            63.0f,
            null,
            ": S가 전장에 있을 때 창 화면 크기를 변경합니다.",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            165.686f,
            -450.0f,
            34.6f,
            63.0f,
            null,
            "☆",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            200.286f,
            -450.0f,
            11.11f,
            63.0f,
            null,
            " ",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            211.396f,
            -450.0f,
            299.85f,
            63.0f,
            null,
            " 왼쪽 키보드 + - 키",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/scrollview/view/content/richtext/RICHTEXT_CHILD",
            "label",
            511.246f,
            -450.0f,
            285.08f,
            63.0f,
            null,
            ": BGM 볼륨 조절.",
            false
        )
        draw(
            "Canvas/Layer/Logo_12-1/button0/Background",
            "sliced-sprite",
            1172.451f,
            32.187f,
            147.6f,
            56.0f,
            "box3",
            "",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/button0/Background/Label",
            "label",
            1196.251f,
            41.187f,
            100.0f,
            40.0f,
            null,
            "확인",
            true
        )
    }

    /** PropertyLayer's authored node order and bounds, transformed by SHOW_ALL. */
    private fun appendSourcePropertyRenderEvents(log: RenderEventLog) {
        val scale = .86f

        /**
         * 공개 메서드 `draw`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun draw(path: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") {
            val type = when {
                text.isNotEmpty() -> "label"
                asset == "Logo_9-1" -> "tiled-sprite"
                asset in setOf("box1", "box2", "box3", "box4") -> "sliced-sprite"
                else -> "sprite"
            }
            log.draw(
                "hall-property-stable",
                "PropertyLayer",
                path,
                type,
                x * scale,
                y * scale,
                w * scale,
                h * scale,
                asset,
                blend = if (text.isEmpty()) listOf(770, 771) else listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
                text = text
            )
        }
        draw("Canvas/Layer/bg", 247.186f, 47f, 994f, 706f, "Logo_9-1")
        draw("Canvas/Layer/bg/box1", 247.186f, 47f, 994f, 706f, "box1")
        draw("Canvas/Layer/bg/bg1", 247.186f, 703f, 994f, 50f, "bg1")
        draw("Canvas/Layer/bg/bg1/label", 669.431f, 702.8f, 149.51f, 50.4f, text = "창고 일람")
        draw("Canvas/Layer/bg/panel0", 249.186f, 117f, 990f, 524f, "box4")
        val headers = listOf(
            floatArrayOf(251.236f, 637.9f, 376.9f, 60f, 405.086f, 642.7f, 69.2f, 50.4f) to "이름",
            floatArrayOf(628.636f, 638f, 195.1f, 60f, 691.586f, 642.8f, 69.2f, 50.4f) to "속성",
            floatArrayOf(823.736f, 638f, 106.9f, 60f, 842.586f, 642.8f, 69.2f, 50.4f) to "레벨",
            floatArrayOf(931.586f, 638f, 101.2f, 60f, 930.286f, 642.8f, 103.8f, 50.4f) to "경험치",
            floatArrayOf(1031.986f, 638f, 206.4f, 60f, 1083.286f, 642.8f, 103.8f, 50.4f) to "소지자",
        )
        headers.forEach { (g, value) ->
            draw("Canvas/Layer/bg/panel0/title0", g[0], g[1], g[2], g[3], "box3")
            draw("Canvas/Layer/bg/panel0/title0/label", g[4], g[5], g[6], g[7], text = value)
        }
        val rows = listOf(
            listOf("단검", "조조", "검", "1", "0") to "1-1",
            listOf("단검", "병사 01", "검", "1", "0") to "1-1",
            listOf("돌로 만든 보검", "허자장", "보검", "1", "0") to "19-1",
        )
        rows.forEachIndexed { index, (values, icon) ->
            val y = 560f - index * 78f
            draw("Canvas/Layer/bg/panel0/scrollview/view/content/item", 253.186f, y, 984f, 76f, "box3")
            draw("Canvas/Layer/bg/panel0/scrollview/view/content/item/box2", 260.288f, y + 9.597f, 60f, 60f, "box2")
            draw(
                "Canvas/Layer/bg/panel0/scrollview/view/content/item/box2/icon",
                262.538f,
                y + 11.547f,
                55.7f,
                55.9f,
                icon
            )
            val labelRects = arrayOf(
                floatArrayOf(329.286f, y + 12.8f, 288.1f, 50.4f), floatArrayOf(1037.986f, y + 12.8f, 190.8f, 50.4f),
                floatArrayOf(633.386f, y + 12.8f, 186.2f, 50.4f), floatArrayOf(866.061f, y + 12.8f, 22.25f, 50.4f),
                floatArrayOf(971.061f, y + 12.8f, 22.25f, 50.4f),
            )
            values.forEachIndexed { column, value ->
                val g = labelRects[column]
                draw(
                    "Canvas/Layer/bg/panel0/scrollview/view/content/item/label$column",
                    g[0],
                    g[1],
                    g[2],
                    g[3],
                    text = value
                )
            }
        }
        listOf(820.971f, 625.468f, 927.065f, 1029.026f).forEach { x ->
            draw("Canvas/Layer/bg/panel0/vline", x, 122.75f, 6f, 515.38f, "vline")
        }
        val tabs = listOf(
            Triple(267.99f, "무기", floatArrayOf(314.292f, 69.2f)),
            Triple(415.99f, "방어구", floatArrayOf(444.992f, 103.8f)),
            Triple(563.99f, "보조", floatArrayOf(610.292f, 69.2f)),
            Triple(711.99f, "아이템", floatArrayOf(740.992f, 103.8f)),
        )
        tabs.forEachIndexed { index, (radioX, value, labelGeometry) ->
            draw(
                "Canvas/Layer/bg/toggleContainer/toggle$index/Background",
                radioX,
                67.577f,
                32f,
                32f,
                "default_radio_button_off"
            )
            if (index == 0) draw(
                "Canvas/Layer/bg/toggleContainer/toggle0/checkmark",
                radioX,
                67.577f,
                32f,
                32f,
                "default_radio_button_on"
            )
            draw(
                "Canvas/Layer/bg/toggleContainer/toggle$index/label",
                labelGeometry[0],
                58.377f,
                labelGeometry[1],
                50.4f,
                text = value
            )
        }
        draw("Canvas/Layer/bg/button0/Background", 1084.386f, 54.5f, 144.8f, 54f, "box3")
        draw("Canvas/Layer/bg/button0/Background/Label", 1106.786f, 59.5f, 100f, 50f, text = "확인")
    }

    /** TreasureLayer creates all 50 scroll cards; offscreen nodes remain in draw order. */
    private fun appendSourceTreasureRenderEvents(log: RenderEventLog) {
        val scale = .86f

        /**
         * 공개 메서드 `draw`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun draw(path: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") {
            val visible = x + w > 0f && y + h > 0f && x < 1488.372f && y < 800f
            val type = when {
                text.isNotEmpty() -> "label"
                asset == "Logo_9-1" -> "tiled-sprite"
                asset in setOf("box1", "box2", "box3", "box4") -> "sliced-sprite"
                else -> "sprite"
            }
            log.draw(
                "hall-treasure-stable",
                "TreasureLayer",
                path,
                type,
                x * scale,
                y * scale,
                w * scale,
                h * scale,
                asset,
                blend = if (text.isEmpty()) listOf(770, 771) else listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
                visible = visible,
                text = text
            )
        }
        draw("Canvas/Layer/bg1", 259.186f, 84f, 970f, 632f, "Logo_9-1")
        draw("Canvas/Layer/bg1/box3", 259.186f, 84f, 970f, 632f, "box3")
        draw("Canvas/Layer/bg1/title", 259.186f, 671f, 970f, 50f, "bg1")
        draw("Canvas/Layer/bg1/title/box1", 259.186f, 671f, 970f, 50f, "box1")
        draw("Canvas/Layer/bg1/title/label", 264.141f, 670.8f, 149.51f, 50.4f, text = "보물 도감")
        draw("Canvas/Layer/bg1/button7/Background", 1070.986f, 90.95f, 150.6f, 51.5f, "box3")
        draw("Canvas/Layer/bg1/button7/Background/Label", 1096.286f, 98.7f, 100f, 40f, text = "종료")
        draw("Canvas/Layer/bg1/label", 266.194f, 91.731f, 467.06f, 50.4f, text = "지금까지 발견한 보물 00 / 50")
        repeat(50) { index ->
            val column = index % 2
            val row = index / 2
            val x = 270.186f + column * 477f
            val y = 480.5f - row * 193f
            val itemPath = "Canvas/Layer/bg1/scrollview/view/content/item"
            draw(itemPath, x, y, 471f, 190f, "box3")
            draw("$itemPath/New Node", x, y, 471f, 190f, "Logo_9-1")
            draw("$itemPath/New Node/box3", x, y, 471f, 190f, "box3")
            draw("$itemPath/box2", x + 11.256f, y + 42.5f, 113f, 105f, "box2")
            draw("$itemPath/label0", x + 134f, y + 131f, 329f, 50f, text = "발견되지 않음")
        }
    }

    /**
     * EquipLayer's initial frame, in the exact traversal order used by the
     * recovered Cocos scene. Coordinates below are authored 1488.372x800
     * coordinates converted to the game's 1280x688 logical viewport.
     * Invisible, clipped descendants are retained because they are part of
     * the source render-event contract even though no pixels are submitted.
     */
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
        val (layer, root) = when (kind) {
            HallManagement.EQUIP -> "EquipLayer" to floatArrayOf(118.84f, 28.81f, 1042.32f, 630.38f)
            HallManagement.BUY -> "BuyLayer" to floatArrayOf(168.72f, 28.81f, 943.42f, 630.38f)
            HallManagement.SELL -> "SellLayer" to floatArrayOf(267.84f, 65.36f, 744.76f, 557.28f)
        }
        /**
         * 공개 메서드 `event`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun event(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String = "",
            text: String = ""
        ) =
            log.draw("management", layer, "Canvas/Layer/$layer/$path", type, x, y, w, h, asset, text = text)

        /**
         * 공개 메서드 `panel`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun panel(path: String, x: Float, y: Float, w: Float, h: Float) =
            event(path, "nine-patch", x, y, w, h, "maps/ui/start-battle/box1.png")

        /**
         * 공개 메서드 `button`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `label` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun button(path: String, label: String, x: Float, y: Float, w: Float) {
            event(path, "nine-patch", x, y, w, 43f, "maps/ui/start-battle/button.png")
            event("$path/Label", "text", x, y, w, 43f, "font:body", label)
        }
        // BUY/SELL have a source render-event oracle.  Record the authored
        // Cocos paths, draw order, stable frame identities, and transformed
        // (.86) geometry rather than an approximate logical summary.
        if (kind == HallManagement.BUY || kind == HallManagement.SELL) {
            fun source(
                path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                asset: String? = null, text: String = "", label: Boolean = false
            ) =
                log.draw(
                    "management", layer, "Canvas/Layer/$path", type, x, y, w, h, asset,
                    blend = if (label) listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771), text = text
                )

            /**
             * 공개 메서드 `label`
             *
             * ### 파라미터
            - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `h` (`Float = 43.344f`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun label(path: String, text: String, x: Float, y: Float, w: Float, h: Float = 43.344f) =
                source(path, "label", x, y, w, h, text = text, label = true)
            if (kind == HallManagement.SELL) {
                source("bg1", "tiled-sprite", 267.62f, 65.36f, 744.76f, 557.28f, "Logo_9-1")
                source("bg1/box3", "sliced-sprite", 267.62f, 65.36f, 744.76f, 557.28f, "box3")
                source("bg1/title", "sprite", 267.62f, 579.64f, 744.76f, 43f, "bg1")
                label("bg1/title/label", "판매하기", 605.60f, 579.468f, 119.024f)
                label("bg1/label", "현금", 283.444f, 80.266f, 59.512f)
                source("bg1/box2", "sliced-sprite", 350.309f, 80.438f, 142.502f, 43f, "box2")
                label("bg1/box2/label", campaign.money.toString(), 467.929f, 80.266f, 19.135f)
                source("bg1/button7/Background", "sliced-sprite", 870.48f, 76.138f, 129f, 51.6f, "box3")
                label("bg1/button7/Background/Label", "종료", 891.98f, 87.318f, 86f, 34.4f)
                source("bg1/box1", "sliced-sprite", 271.06f, 68.8f, 737.88f, 493.64f, "box1")
                source("bg1/box1/bg1", "sliced-sprite", 281.568f, 538.325f, 140.352f, 37.324f, "bg1")
                label("bg1/box1/bg1/label", "창고 목록", 287.454f, 535.315f, 128.579f)
                source("bg1/box1/button0/Background", "sliced-sprite", 522.18f, 76.138f, 172f, 51.6f, "box3")
                label("bg1/box1/button0/Background/Label", "무기점", 566.129f, 87.318f, 111.8f, 34.4f)
                source("bg1/box1/button0/Background/command1", "sprite", 535.16f, 89.947f, 27.52f, 27.52f, "command1")
                source("bg1/box1/button1/Background", "sliced-sprite", 694.18f, 76.138f, 172f, 51.6f, "box3")
                label("bg1/box1/button1/Background/Label", "상점", 738.129f, 87.318f, 111.8f, 34.4f)
                source("bg1/box1/button1/Background/command1", "sprite", 708.02f, 90.807f, 25.8f, 25.8f, "command3")
                source("bg1/box1/box0", "sliced-sprite", 274.50f, 137.6f, 731f, 399.04f, "box2")
                return
            }

            source("bg1", "tiled-sprite", 168.290f, 28.81f, 943.42f, 630.38f, "Logo_9-1")
            source("bg1/box3", "sliced-sprite", 168.290f, 28.81f, 943.42f, 630.38f, "box3")
            source("bg1/title", "sprite", 168.290f, 616.19f, 943.42f, 43f, "bg1")
            label("bg1/title/label", "매입", 610.244f, 616.018f, 59.512f)
            label("bg1/label", "현금", 188.844f, 39.388f, 59.512f)
            source("bg1/box2", "sliced-sprite", 258.676f, 39.56f, 141.556f, 43f, "box2")
            label("bg1/box2/label", campaign.money.toString(), 378.107f, 39.388f, 19.135f)
            fun sourceButton(
                path: String,
                text: String,
                x: Float,
                y: Float,
                w: Float,
                tx: Float,
                tw: Float,
                labelYOffset: Float = 8.6f,
            ) {
                source("$path/Background", "sliced-sprite", x, y, w, 48.16f, "box3")
                label("$path/Background/Label", text, tx, y + labelYOffset, tw, 34.4f)
            }
            sourceButton("bg1/button5", "이전 무장", 678.700f, 36.98f, 146.2f, 683f, 137.6f)
            sourceButton("bg1/button6", "다음 무장", 838.660f, 36.98f, 146.2f, 842.96f, 137.6f)
            sourceButton("bg1/button7", "종료", 530.780f, 36.98f, 120.4f, 547.98f, 86f)
            source("bg1/box1", "sliced-sprite", 176.030f, 89.01f, 480.74f, 503.1f, "box1")
            source("bg1/box1/bg1", "sliced-sprite", 187.478f, 574.188f, 137.772f, 36.464f, "bg1")
            label("bg1/box1/bg1/label", "상품 목록", 192.075f, 570.748f, 128.579f)
            sourceButton("bg1/box1/button0", "무기점", 183.045f, 521.159f, 154.8f, 221.874f, 105.78f, 9.002f)
            source("bg1/box1/button0/Background/command1", "sprite", 191.667f, 533.248f, 27.52f, 27.52f, "command1")
            sourceButton("bg1/box1/button1", "상점", 337.547f, 521.159f, 154.8f, 376.376f, 105.78f, 9.002f)
            source("bg1/box1/button1/Background/command1", "sprite", 347.030f, 534.108f, 25.8f, 25.8f, "command3")
            source("bg1/box1/box0", "sliced-sprite", 182.910f, 95.331f, 465.26f, 426.818f, "box2")
            hallBuyCandidates().take(3).forEachIndexed { index, item ->
                val y = 369.069f - index * 153.08f
                val path = "bg1/box1/box0/scrollview/view/content/item"
                source(path, "sliced-sprite", 184.630f, y, 461.82f, 151.36f, "box3")
                source("$path/box2", "sliced-sprite", 190.905f, y + 57.955f, 86f, 86f, "box2")
                source("$path/box2/icon", "sprite", 195.377f, y + 62.427f, 77.056f, 77.056f, "1-1")
                label("$path/label0", item.name, 283.1f, y + 103.888f, 195.53f)
                label("$path/label", "속성: ", 283.1f, y + 59.168f, 78.63f)
                label("$path/label1", gameDataCatalog.equipmentTypeName(item.itemType), 371.68f, y + 59.168f, 29.756f)
                label("$path/label", "레벨:", 475.74f, y + 103.888f, 69.067f)
                label("$path/label2", "1", 562.6f, y + 103.888f, 19.135f)
                label("$path/label", "인벤토리: ", 193.66f, y + 8.428f, 138.142f)
                label("$path/label3", (campaign.inventory.items[item.id] ?: 0).toString(), 280.52f, y + 8.428f, 19.135f)
                label("$path/label", "총합: ", 338.14f, y + 8.428f, 78.63f)
                label("$path/label4", "0", 425f, y + 8.428f, 19.135f)
                label("$path/label", "가격:", 475.74f, y + 8.428f, 69.067f)
                label("$path/label5", gameDataCatalog.purchasePrice(item).toString(), 562.6f, y + 8.428f, 19.135f)
            }
            val unitId = hallEquipUnitId()
            val unit = gameDataCatalog.unitProfile(unitId)
            val level = campaign.unitAttribute(unitId, 18, unit?.level ?: 1)
            val profile = unit?.let {
                gameDataCatalog.battleProfile(
                    unitId,
                    (level - 1).coerceAtLeast(0),
                    campaign.unitAttribute(unitId, 17, it.posts)
                )
            }
            campaign.inventory.ensureDefaultEquipment(unitId, gameDataCatalog)
            val bonus = campaign.inventory.equipment[unitId]?.let {
                gameDataCatalog.equipmentBonus(
                    it.asScriptValues(),
                    profile?.level ?: 1
                )
            }
                ?: GameDataCatalog.EquipmentBonus()
            val stats = listOf(
                profile?.maxHitPoints ?: 0,
                profile?.maxMagicPoints ?: 0,
                (profile?.attack ?: 0) + bonus.attack,
                profile?.spirit ?: 0,
                (profile?.defense ?: 0) + bonus.defense,
                profile?.critical ?: 0,
                profile?.morale ?: 0,
                profile?.movement ?: 0
            )
            source("bg1/vline", "sprite", 664.568f, 33.841f, 5.16f, 582.306f, "vline")
            source("bg1/button0", "sliced-sprite", 726.230f, 565.88f, 309.6f, 48.16f, "box3")
            source("bg1/button0/vline", "sprite", 878.450f, 571.169f, 5.16f, 41.022f, "vline")
            label("bg1/button0/label0", unit?.name ?: "조조", 773.747f, 570.868f, 59.512f)
            val postsName =
                gameDataCatalog.postsName(campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)).ifEmpty { "군웅" }
            label("bg1/button0/label1", postsName, 930.320f, 570.868f, 59.512f)
            source("bg1/scrollview/view/content/box1/face", "sprite", 703.548f, 355.470f, 165.12f, 206.4f, "1")
            source(
                "bg1/scrollview/view/content/box1/face/bg0",
                "sliced-sprite",
                682.908f,
                355.470f,
                206.4f,
                206.4f,
                "box2"
            )
            label("bg1/scrollview/view/content/box1/label0", unit?.name ?: "조조", 899.090f, 517.479f, 59.512f)
            label("bg1/scrollview/view/content/box1/label1", postsName, 899.090f, 464.159f, 59.512f)
            label("bg1/scrollview/view/content/box1/label", "Exp", 899.090f, 357.519f, 59.28f)
            source(
                "bg1/scrollview/view/content/box1/progressBar",
                "sliced-sprite",
                963.590f,
                368.871f,
                115.24f,
                20.64f,
                "default_scrollbar_bg"
            )
            source(
                "bg1/scrollview/view/content/box1/progressBar/bar",
                "sliced-sprite",
                965.310f,
                370.591f,
                0f,
                17.2f,
                "Mark_6-1"
            )
            label("bg1/scrollview/view/content/box1/progressBar/label", "0/100", 978.167f, 369.881f, 86.086f)
            label("bg1/scrollview/view/content/box1/label", "Lv", 898.982f, 410.839f, 36.335f)
            label("bg1/scrollview/view/content/box1/label2", level.toString(), 953.162f, 410.839f, 19.135f)
            val statNames = listOf("HP", "MP", "공격력", "정신력", "방어력", "폭발력", "사기", "이동력")
            val statLabelRects = listOf(
                689.434f to 309.359f, 902.530f to 309.359f, 696.216f to 258.619f, 909.496f to 258.619f,
                696.216f to 207.019f, 909.496f to 207.019f, 693.894f to 156.279f, 909.496f to 156.279f
            )
            val boxRects = listOf(
                801.050f to 309.531f, 1015.190f to 309.531f, 801.050f to 258.791f, 1015.190f to 258.791f,
                801.050f to 207.191f, 1015.190f to 207.191f, 801.050f to 156.451f, 1015.190f to 156.451f
            )
            val statLabelWidths = listOf(47.7902f, 51.6f, 89.268f, 89.268f, 89.268f, 89.268f, 59.512f, 89.268f)
            statNames.forEachIndexed { index, name ->
                val (lx, ly) = statLabelRects[index]
                label("bg1/scrollview/view/content/box1/label", name, lx, ly, statLabelWidths[index])
            }
            stats.forEachIndexed { index, value ->
                val (bx, by) = boxRects[index]
                source("bg1/scrollview/view/content/box1/bg$index", "sliced-sprite", bx, by, 68.8f, 43f, "box2")
                val textWidth = when (value.toString().length) {
                    1 -> 19.135f; 2 -> 38.261f; else -> 57.396f
                }
                label(
                    "bg1/scrollview/view/content/box1/bg$index/label",
                    value.toString(),
                    bx + (68.8f - textWidth) / 2f,
                    by - .172f,
                    textWidth
                )
            }
            campaign.inventory.equippedItems().firstOrNull { it.unitId == unitId }?.let { equipped ->
                val item = gameDataCatalog.equipmentProfile(equipped.itemId) ?: return@let
                source("bg1/scrollview/view/content/bg0", "sliced-sprite", 679.747f, 20.967f, 402.566f, 129f, "box1")
                label("bg1/scrollview/view/content/bg0/label", "무기:", 834.760f, 101.326f, 69.067f)
                label("bg1/scrollview/view/content/bg0/label0", item.name, 897.066f, 101.025f, 59.512f)
                source(
                    "bg1/scrollview/view/content/bg0/box2",
                    "sliced-sprite",
                    685.515f,
                    28.484f,
                    115.911f,
                    116.186f,
                    "box2"
                )
                source("bg1/scrollview/view/content/bg0/box2/icon", "sprite", 688.43f, 31.537f, 110.08f, 110.08f, "1-1")
                label("bg1/scrollview/view/content/bg0/label_0", "Lv", 810.295f, 60.248f, 36.335f)
                label("bg1/scrollview/view/content/bg0/label1", equipped.level.toString(), 864.032f, 60.142f, 19.135f)
                label("bg1/scrollview/view/content/bg0/label_1", "Exp", 812.27f, 26.298f, 59.28f)
                source(
                    "bg1/scrollview/view/content/bg0/progressBar",
                    "sliced-sprite",
                    880.21f,
                    25.61f,
                    175.44f,
                    20.64f,
                    "default_scrollbar_bg"
                )
                source(
                    "bg1/scrollview/view/content/bg0/progressBar/bar",
                    "sliced-sprite",
                    881.93f,
                    27.33f,
                    0f,
                    17.2f,
                    "Mark_6-1"
                )
                label("bg1/scrollview/view/content/bg0/progressBar/label", "0/100", 924.887f, 26.62f, 86.086f)
            }
            return
        }
        event("bg1/logo9", "tiled-sprite", root[0], root[1], root[2], root[3], "maps/ui/start-battle/logo9.png")
        panel("bg1", root[0], root[1], root[2], root[3])
        event(
            "bg1/title",
            "nine-patch",
            root[0],
            root[1] + root[3] - 43f,
            root[2],
            43f,
            "maps/ui/start-battle/title.png"
        )
        val title = when (kind) {
            HallManagement.EQUIP -> "장비"; HallManagement.BUY -> "매입"; HallManagement.SELL -> "판매하기"
        }
        event("bg1/title/label", "text", root[0], root[1] + root[3] - 43f, root[2], 43f, "font:title", title)
        when (kind) {
            HallManagement.EQUIP -> {
                panel("bg1/box1", 124.26f, 85.96f, 604.92f, 481.69f)
                panel("bg1/scrollview", 739.76f, 89.44f, 414.52f, 474.72f)
                listOf("전부", "무기", "보구", "보조").forEachIndexed { index, label ->
                    button("bg1/button${10 + index}", label, 123.12f + index * 129f, 566.74f, 129f)
                }
                button("bg1/button14", "정보", 125.35f, 37.84f, 85.74f)
                button("bg1/button8", "모두 해제", 493.37f, 37.84f, 148.95f)
                button("bg1/button7", "종료", 643.73f, 37.84f, 83.42f)
                val unitId = hallEquipUnitId()
                val unit = gameDataCatalog.unitProfile(unitId)
                button("bg1/button0/name", campaign.unitNames[unitId] ?: unit?.name ?: "조조", 820f, 566.74f, 150f)
                val posts = campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)
                button("bg1/button0/posts", gameDataCatalog.armProfile(posts)?.name ?: "군웅", 975f, 566.74f, 150f)
                hallEquipInventory().take(6).forEachIndexed { index, (itemId, _) ->
                    val item = gameDataCatalog.equipmentProfile(itemId) ?: return@forEachIndexed
                    val iy = 515f - index * 68f
                    panel("bg1/box1/content/item$index", 132f, iy - 48f, 582f, 62f)
                    event(
                        "bg1/box1/content/item$index/icon",
                        "sprite",
                        141f,
                        iy - 40f,
                        52f,
                        52f,
                        "maps/item-icons/${item.icon}.png"
                    )
                    event("bg1/box1/content/item$index/name", "text", 207f, iy - 35f, 260f, 43f, "font:body", item.name)
                }
                event(
                    "bg1/scrollview/content/box1/face", "sprite", 768.76f, 357.81f, 165f, 206f,
                    "portrait:${unit?.face ?: unitId}"
                )
                campaign.inventory.equippedItems().firstOrNull { it.unitId == unitId }?.let { equipped ->
                    val item = gameDataCatalog.equipmentProfile(equipped.itemId) ?: return@let
                    panel("bg1/scrollview/content/bg0", 761.76f, 59.81f, 397f, 90f)
                    event(
                        "bg1/scrollview/content/bg0/icon", "sprite", 785.76f, 74.81f, 58f, 58f,
                        "maps/item-icons/${item.icon}.png"
                    )
                    event(
                        "bg1/scrollview/content/bg0/label0",
                        "text",
                        873.76f,
                        78f,
                        269f,
                        43f,
                        "font:body",
                        "무기:${item.name}"
                    )
                }
                button("bg1/button5", "이전 무장", 842.52f, 37.84f, 152.22f)
                button("bg1/button6", "다음 무장", 994.75f, 37.84f, 152.22f)
            }

            HallManagement.BUY -> {
                panel("bg1/box1", 176.42f, 89.01f, 480.74f, 503.1f)
                panel("bg1/scrollview", 673.77f, 89.44f, 414.52f, 474.72f)
                button("bg1/button0", "무기점", 183.25f, 521.28f, 154.8f)
                button("bg1/button1", "상점", 337.85f, 521.28f, 154.8f)
                hallBuyCandidates().take(3).forEachIndexed { index, item ->
                    val iy = root[1] + root[3] - 245f - index * 139f
                    panel("bg1/box1/content/item$index", root[0] + 20f, iy - 91f, 635f, 132f)
                    event(
                        "bg1/box1/content/item$index/icon",
                        "sprite",
                        root[0] + 31f,
                        iy - 23f,
                        86f,
                        86f,
                        "maps/item-icons/${item.icon}.png"
                    )
                    event(
                        "bg1/box1/content/item$index/name",
                        "text",
                        root[0] + 125f,
                        iy,
                        510f,
                        43f,
                        "font:body",
                        item.name
                    )
                }
                button("bg1/button7", "종료", 530.78f, 36.98f, 120.4f)
                button("bg1/button5", "이전 무장", 678.70f, 36.98f, 146.2f)
                button("bg1/button6", "다음 무장", 838.66f, 36.98f, 146.2f)
            }

            HallManagement.SELL -> {
                panel("bg1/box1", root[0] + 8f, root[1] + 99f, root[2] - 16f, root[3] - 172f)
                campaign.inventory.items.entries.sortedBy { it.key }.take(5).forEachIndexed { index, (itemId, _) ->
                    val item = gameDataCatalog.equipmentProfile(itemId) ?: return@forEachIndexed
                    val iy = root[1] + root[3] - 160f - index * 92f
                    panel("bg1/box1/content/item$index", root[0] + 12f, iy - 61f, root[2] - 24f, 86f)
                    event(
                        "bg1/box1/content/item$index/icon",
                        "sprite",
                        root[0] + 23f,
                        iy - 45f,
                        69f,
                        69f,
                        "maps/item-icons/${item.icon}.png"
                    )
                    event(
                        "bg1/box1/content/item$index/name",
                        "text",
                        root[0] + 105f,
                        iy - 30f,
                        300f,
                        43f,
                        "font:body",
                        item.name
                    )
                }
                button("bg1/button0", "무기점", 522.98f, 75.28f, 172f)
                button("bg1/button1", "상점", 694.98f, 75.28f, 172f)
                button("bg1/button7", "종료", 870.47f, 75.14f, 129f)
            }
        }
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
                playback.currentModalKind == ScenarioInterpreter.ModalKind.AMBITION
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

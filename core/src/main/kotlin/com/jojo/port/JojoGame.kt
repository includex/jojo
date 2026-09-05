package com.jojo.port

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.JsonReader

/**
 * Fresh-profile prerequisite used by both full-battle engines.  The original
 * harness calls Model.unitJoin(id, 0, 2) and Model.setBattleUnits(..., 7):
 * Yingchuan needs Cao Cao only. Later battles use the corresponding R-module
 * setJoinBattle contract, including its maximum, required and excluded IDs.
 * Scenario Python remains responsible for the actual map, camps, positions
 * and reinforcements.
 */
internal fun prepareDirectFullBattleTraceCampaign(
    state: CampaignState,
    scenario: String,
    entryLimit: ScenarioJoinBattleLimit? = null,
): List<Int> {
    val match = Regex("S_(\\d{2})").matchEntire(scenario)
    val index = match?.groupValues?.get(1)?.toIntOrNull()
    require(index != null && index in 0..57) { "full-battle scenario must be S_00 through S_57: $scenario" }
    val seeded = if (index == 0) {
        listOf(0)
    } else {
        val limit = requireNotNull(entryLimit) {
            "$scenario direct full-battle trace requires its authored R-module setJoinBattle contract"
        }
        val excluded = limit.excludedUnitIds.toSet()
        val mandatory = buildList {
            if (0 !in excluded) add(0)
            limit.requiredUnitIds.forEach { id -> if (id !in excluded && id !in this) add(id) }
        }
        require(mandatory.size <= limit.maximum) {
            "$scenario has ${mandatory.size} mandatory units but maximum is ${limit.maximum}"
        }
        (mandatory + (0..511).filter { it !in excluded && it !in mandatory })
            .take(limit.maximum)
    }
    state.reset()
    state.joinedUnits += seeded
    state.battleRoster += seeded
    val data = OriginalGameData.load()
    seeded.forEach {
        state.setUnitAttribute(it, 18, 3)
        // The source trace harness seeds through Model.unitJoin, which has
        // already constructed concrete default Item instances.  Preserve
        // their initial level instead of recomputing defaults after a live
        // unit level-up.
        state.ensureDefaultEquipment(it, data)
    }
    return seeded
}

class JojoGame(
    private val verifyMode: Boolean,
    private val battleVerifyMode: Boolean = false,
    private val scenarioBranchVerifyMode: Boolean = false,
    private val alternateScenarioBranchVerifyMode: Boolean = false,
    private val scenarioChoiceScript: List<Int> = emptyList(),
    private val scenarioAllowPendingChoiceAfterScript: Boolean = false,
    private val scenarioGlobals: Map<Int, Int> = emptyMap(),
    private val scenarioUnitAttributes: List<Triple<Int, Int, Int>> = emptyList(),
    private val scenarioVariables: Map<Int, Int> = emptyMap(),
    private val scenarioAmbition: Int? = null,
    private val scenarioRandomSequence: List<Int> = emptyList(),
    private val scenarioInfoTransferRandomSequence: List<Int> = emptyList(),
    private val scenarioBattleRound: Int = 1,
    private val scenarioBattleCamp: Int = 1,
    private val scenarioBattleAttributes: Map<Int, Map<Int, Int>> = emptyMap(),
    private val scenarioBattlePositions: Map<Int, Pair<Int, Int>> = emptyMap(),
    private val scenarioBattlePositionsByCamp: Map<Int, List<Pair<Int, Int>>> = emptyMap(),
    private val scenarioBattleEnemyDefeated: Boolean = false,
    private val scenarioStartScene: String = "scene1",
    private val scenarioStartLabel: String? = null,
    private val scenarioChoiceTracePath: String? = null,
    private val scenarioRandomTracePath: String? = null,
    private val scenarioStopAfterRandomTrace: Boolean = false,
    private val scenarioStopAfterRandomTraceCount: Int? = null,
    private val initialScenario: String = "R_00",
    private val battleReturnScenario: String? = null,
    private val initialScenarioExplicit: Boolean = false,
    private val startAtTitle: Boolean = false,
    private val startAtBattle: Boolean = false,
    private val allScenariosVerifyMode: Boolean = false,
    private val scriptedBattleVerifyMode: Boolean = false,
    private val screenshotPath: String? = null,
    private val rawCapturePath: String? = null,
    private val mapTextureDumpPath: String? = null,
    private val mapDither: String? = null,
    private val mapFilter: String? = null,
    private val mapSampler: String? = null,
    private val mapSampleOffset: Pair<Float, Float>? = null,
    private val compositionTracePath: String? = null,
    private val renderEventLogPath: String? = null,
    private val screenshotState: String? = null,
    private val fullBattleTraceConfig: FullBattleTraceConfig? = null,
    private val yingchuanEntryFlowTracePath: String? = null,
    private val campaignE2eTraceConfig: CampaignE2eTraceConfig? = null,
    private val automatedRun: Boolean = false,
) : Game() {
    private val preferenceProvider = GamePreferenceProvider(automatedRun) { name -> Gdx.app.getPreferences(name) }
    private val campaign by lazy { CampaignStore(preferences("jojo-original-campaign")) }
    private val campaignE2eDriver by lazy { campaignE2eTraceConfig?.let(::CampaignE2eDriver) }
    private val standaloneBattleInputDriver by lazy {
        fullBattleTraceConfig?.takeIf { campaignE2eTraceConfig == null }
            ?.let { config ->
                ProductionBattleInputDriver(
                    inputIntervalSeconds = config.driverIntervalSeconds,
                    onInput = { context -> (screen as? BattleLayer)?.recordFullBattleInput(context) },
                    // The original standalone full-battle harness enters
                    // entrusted control through END_ROUND/MsgBox4 without
                    // committing a player move first.  Campaign E2E keeps
                    // its separate manual-move proof, while S52/S57 ignore
                    // this limit because they require authored room input.
                    manualMoveAttemptLimit = 0,
                )
            }
    }

    internal fun campaignE2eScenarioStarted(module: String, index: Int) {
        campaignE2eDriver?.scenarioStarted(module, index)
    }

    internal fun preferences(name: String) = preferenceProvider.get(name)

    override fun create() {
        if (yingchuanEntryFlowTracePath != null) campaign.state.reset()
        // Everything selected by screenshotState before the normal routing block is a
        // render oracle entered only by an explicit desktop capture argument. It must
        // never be treated as evidence that a player can reach that screen from Title.
        if (screenshotState == "hall-achievements-fixture") {
            replaceScreen(AchievementsFixtureScreen(this))
            return
        }
        if (screenshotState == "hall-attribute-fixture") {
            replaceScreen(AttributeFixtureScreen(this))
            return
        }
        if (screenshotState == "hall-generic-list-fixture") {
            replaceScreen(GenericListFixtureScreen(this))
            return
        }
        if (screenshotState?.removeSuffix("-fixture") == "login-modal-load") {
            replaceScreen(ModalLoadRouteScreen(this))
            return
        }
        if (screenshotState?.removeSuffix("-fixture") == "raffle-gated") {
            replaceScreen(RaffleGateRouteScreen(this))
            return
        }
        LoginOptionalOverlayRoute.parse(screenshotState)?.let { route ->
            replaceScreen(TitleScreen(this, initialSettingOpen = true, optionalOverlayRoute = route))
            return
        }
        CmdRoute.parse(screenshotState)?.let { route ->
            replaceScreen(CmdRouteScreen(this, route))
            return
        }
        TerminalSceneRoute.parse(screenshotState)?.let { route ->
            replaceScreen(TerminalSceneRouteScreen(this, route))
            return
        }
        LearnUnitSkillRoute.parse(screenshotState)?.let { route ->
            replaceScreen(LearnUnitSkillRouteScreen(this, route))
            return
        }
        DefineUnitRoute.parse(screenshotState)?.let { route ->
            replaceScreen(DefineUnitRouteScreen(this, route))
            return
        }
        BattleUnitEditRoute.parse(screenshotState)?.let { route ->
            replaceScreen(BattleUnitEditRouteScreen(this, route))
            return
        }
        EditRosterRoute.parse(screenshotState)?.let { route ->
            replaceScreen(EditRosterRouteScreen(this,route))
            return
        }
        if (screenshotState in setOf(
                "hall-palace-fixture", "hall-section-fixture",
                "hall-forces-fixture", "hall-property-fixture", "hall-terrain-fixture",
                "hall-treasure-fixture", "hall-helper-fixture", "hall-equip-fixture", "hall-unit-list-fixture",
                "hall-unit-list-select-fixture", "hall-unit-list-close-fixture",
                "hall-equip-confirm-fixture", "hall-equip-confirm-unload-fixture",
                "hall-exclusive-fixture", "hall-exclusive-tab1-fixture",
                "hall-magic-fixture", "hall-feats-fixture", "hall-feats-help-fixture",
                "hall-buy-fixture", "hall-sell-fixture", "hall-skip-open-fixture",
            )) {
            // Source Hall overlay fixtures reach this point after the same
            // fresh R_00 prelude. Never inherit a developer save here.
            campaign.state.reset()
            campaign.state.joinedUnits += listOf(0, 157, 181)
            listOf(0, 157, 181).forEach { campaign.state.setUnitAttribute(it, 18, 3) }
        }
        if (screenshotState == "sprite-verify") {
            BattleAvatarConformance.verify()
            Gdx.app.exit()
            return
        }
        spriteFixtureRequest()?.let { fixture ->
            replaceScreen(BattleSpriteFixtureScreen(this, fixture.characterId, fixture.action, fixture.direction, fixture.frameTick, fixture.faction))
            return
        }
        if (screenshotState == "info-layer-r00-first-tick" || screenshotState == "info-layer-r00-full-autopending" || screenshotState == "info-layer-r00-panel-touch" || screenshotState == "info-layer-r00-skip") {
            replaceScreen(InfoLayerFixtureScreen(this))
            return
        }
        screenshotState?.removeSuffix("-fixture")?.takeIf {
            it in setOf("notice-hidden", "notice-shown", "notice-messages", "notice-hidden-clear")
        }?.let { noticeState ->
            replaceScreen(NoticeInfoFixtureScreen(this, noticeState))
            return
        }
        if (screenshotState in setOf("start-battle-fixture", "start-battle-unit-info-fixture", "battle-view-fixture", "start-battle-sort-open-fixture", "start-battle-sort-select-fixture", "start-battle-sort-cancel-fixture")) {
            campaign.state.reset()
            campaign.state.joinedUnits += listOf(0, 157, 181, 182)
            listOf(0, 157, 181, 182).forEach { campaign.state.setUnitAttribute(it, 18, 3) }
            showBattlePreparation("R_00", "S_00", ScenarioJoinBattleLimit(1, 4, listOf(0), emptyList()), 71)
            return
        }
        screenshotState?.removeSuffix("-fixture")?.takeIf {
            it in setOf("reward-basic", "reward-card-1", "reward-card-2")
        }?.let { rewardState ->
            replaceScreen(RewardFixtureScreen(this, rewardState))
            return
        }
        screenshotState?.removeSuffix("-fixture")?.takeIf {
            it in setOf("dialogue-left", "dialogue-right", "dialogue-skip", "dialogue-auto-close")
        }?.let { dialogueState ->
            replaceScreen(DialogueFixtureScreen(this, dialogueState))
            return
        }
        screenshotState?.removeSuffix("-fixture")?.takeIf {
            it in setOf("choose2-open", "choose2-select")
        }?.let { choose2State ->
            replaceScreen(Choose2FixtureScreen(this, choose2State))
            return
        }
        screenshotState?.removeSuffix("-fixture")?.takeIf {
            it in setOf("input-box-empty", "input-box-filled")
        }?.let { inputBoxState ->
            replaceScreen(InputBoxFixtureScreen(this, inputBoxState))
            return
        }
        screenshotState?.removeSuffix("-fixture")?.takeIf {
            it in setOf("quantity-buy-initial", "quantity-sell-edited")
        }?.let { quantityState ->
            replaceScreen(MsgBox3FixtureScreen(this, quantityState))
            return
        }
        screenshotState?.removeSuffix("-fixture")?.takeIf {
            it in setOf(
                "msgbox-ok", "msgbox-confirm", "toast-stable",
                "progress-0", "progress-23", "progress-100",
                "loading-default", "loading-flag1-before", "loading-flag1-after5", "loading-flag2-hidden",
            )
        }?.let { overlayState ->
            replaceScreen(SystemOverlayFixtureScreen(this, overlayState))
            return
        }
        // Visual-regression captures must not inherit a developer's persisted
        // save.  Reproduce the same R_00 → battle-preparation route used by
        // the source Electron harness before entering S_00.
        // `--battle` is a desktop shortcut for the first real battle.  It
        // must still run the source R_00 prelude, because S_00.createMine()
        // resolves its player units from that persistent roster.  Previously
        // only screenshot fixtures initialized it, leaving an interactive
        // direct battle with no player force.
        // `map-only` is an R_00 render fixture, not the S_00 shortcut.  It
        // deliberately captures the fresh map before any R_00 choice loop;
        // preparing the Yingchuan roster here re-enters its choice sequence
        // and can never complete in a map-only run.
        val directBattleScenario = initialScenario.replaceFirst("R_", "S_")
        if (startAtBattle && Regex("S_(?:[0-4][0-9]|5[0-7])").matches(directBattleScenario) && screenshotState != "map-only") {
            if (fullBattleTraceConfig != null) {
                val routeIndex = directBattleScenario.removePrefix("S_").toInt()
                val entryLimit = if (routeIndex == 0) null else
                    PythonAstExtractor.loadLastJoinBattleLimit("R_%02d".format(routeIndex))
                prepareDirectFullBattleTraceCampaign(campaign.state, directBattleScenario, entryLimit)
            } else prepareYingchuanCaptureCampaign()
        }
        scenarioGlobals.forEach { (id, value) -> campaign.state.globalVariables[id] = value }
        when {
            allScenariosVerifyMode -> replaceScreen(ScenarioBatchVerificationScreen())
            battleVerifyMode || scriptedBattleVerifyMode || startAtBattle -> showBattleSandbox()
            startAtTitle -> showTitleScreen()
            // An explicit desktop verification scenario must never be replaced
            // by a persisted campaign route. The title/continue path remains
            // the only route that resumes snapshot.currentScenario.
            // Verification is a deterministic R_00 fixture and must not
            // inherit a prior interactive save (the packaged app may have
            // one even when no scenario argument was supplied).
            else -> showScenario(if (!verifyMode && !initialScenarioExplicit && initialScenario == "R_00") campaign.snapshot.currentScenario else initialScenario)
        }
    }

    override fun render() {
        super.render()
        campaignE2eDriver?.update(Gdx.graphics.deltaTime, screen)
        if (campaignE2eDriver == null) {
            (screen as? BattleLayer)?.let { battle ->
                standaloneBattleInputDriver?.update(Gdx.graphics.deltaTime, battle.campaignE2eState())
            }
        }
    }

    private fun prepareYingchuanCaptureCampaign() {
        val state = campaign.state
        state.reset()
        val prelude = PythonAstRuntime.load("R_00", state)
        prelude.start("scene1")
        var steps = 0
        while (prelude.state != PlaybackState.COMPLETE && steps++ < 10_000) {
            when (prelude.state) {
                PlaybackState.DIALOGUE -> prelude.advanceDialogue()
                PlaybackState.CHOICE -> {
                    // R_00's setup menu intentionally loops when its first
                    // row toggles training mode. The source player proceeds
                    // by selecting the explicit game-start row; all earlier
                    // story/config choices retain the deterministic first
                    // selection used by this capture bootstrap.
                    prelude.currentChoice?.options
                        ?.indexOfFirst { it.contains("게임 시작") }
                        ?.takeIf { it >= 0 }
                        ?.let(prelude::selectChoice)
                    prelude.confirmChoice()
                }
                PlaybackState.DELAY -> prelude.skipDelay()
                PlaybackState.MODAL -> prelude.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
        check(prelude.state == PlaybackState.COMPLETE) { "영천 캡처용 R_00 도입을 완료하지 못했습니다." }
        check(state.joinedUnits.isNotEmpty()) { "영천 캡처용 아군 명단이 비어 있습니다." }
        state.battleRoster.clear()
        if (screenshotState?.startsWith("yingchuan-") != true) {
            // Interactive --battle needs an explicit direct-launch roster.
            // The original Yingchuan framebuffer oracle does not: its five
            // authored allies come from S_00.createFriend(), while its empty
            // BattleHall selection produces no extra createMine actors.
            // Keep visual fixtures on that exact source composition.
            state.battleRoster += state.joinedUnits.take(15)
        }
    }

    fun showTitleScreen() = replaceScreen(TitleScreen(
        this,
        initialSettingOpen = screenshotState == "login-setting",
        initialLoadOpen = screenshotState == "login-load" || screenshotState?.startsWith("login-load-row") == true,
        initialLoadRow = screenshotState?.removePrefix("login-load-row")?.takeIf { screenshotState.startsWith("login-load-row") }?.toIntOrNull(),
    ))
    fun showTitleLoadScreen() = replaceScreen(TitleScreen(this, initialLoadOpen = true))
    fun showTitleSettingScreen(returnScenario: String? = null) = replaceScreen(TitleScreen(
        this,
        initialSettingOpen = true,
        settingSceneName = if (returnScenario == null) "Login" else "Hall",
        settingReturnScenario = returnScenario,
    ))

    /** The source title screen resets Model before entering the first scene. */
    fun startNewGame() {
        campaign.newGame()
        showScenario("R_00", "scene0")
    }

    /** Continue is deliberately separate from new game, as on the original title screen. */
    fun loadCampaign() = showScenario(campaign.snapshot.currentScenario)
    fun loadCampaignSlot(index: Int): String? = campaign.loadSlot(index)
    fun savedLoadPage(): Int = campaign.savedPage()
    fun saveLoadPage(page: Int) = campaign.savePage(page)
    fun titleLoadGameLayer(): LoadGameLayer = LoadGameLayer(object : LoadGameLayer.Repository {
        override fun load(index: Int): String? = campaign.loadSlot(index)
        override fun savedPage(): Int = campaign.savedPage()
        override fun savePage(page: Int) = campaign.savePage(page)
        override fun featureEnabled(name: String): Boolean = false
        override fun versionCode(): Int = 1
        override fun restore(index: Int, raw: String, route: LoadGameLayer.RestoreRoute): Boolean =
            restoreCampaignSlot(index, raw, route)
    })

    /**
     * Desktop replacement for Login.registerCheck. The original waits on the platform Python
     * manager and may call register with a returned activation payload. This port deliberately
     * has no network/hot-update backend, so it completes asynchronously with "not registered";
     * TitleScreen still preserves the source LoadingLayer attach/callback/detach lifecycle.
     */
    internal fun requestRegistrationCheck(complete: (Boolean) -> Unit) {
        Gdx.app.log("JojoPort", "CHECK_REGISTER requested; platform registerCheck is unsupported")
        Gdx.app.postRunnable { complete(false) }
    }

    /** Desktop helper returns no supportAd entitlement, but the source-gated route stays live. */
    internal fun settingFeatureEnvironment(sceneName: String) = SettingLayer.FeatureEnvironment(
        sceneName = sceneName,
        supportAdCode = 0,
        nowSeconds = (System.currentTimeMillis() / 1000L).toInt(),
    )
    fun restoreCampaignSlot(index: Int, raw: String, route: LoadGameLayer.RestoreRoute): Boolean {
        if (!campaign.restoreSlot(index, raw)) return false
        // `_loadGame`: battle=2 increments its stage then Hall; a nonzero
        // battle opens BattleScene with the complete save envelope; no field
        // opens HallScene.  CampaignStore already restored the model before
        // this transition, so only the source scene choice remains here.
        when (route) {
            LoadGameLayer.RestoreRoute.BATTLE -> showBattleSandbox(
                sourceScenario = campaign.snapshot.currentScenario.replaceFirst("R_", "S_"),
                returnScenario = campaign.snapshot.currentScenario,
            )
            LoadGameLayer.RestoreRoute.HALL -> showCampaignHall(campaign.snapshot.currentScenario)
            LoadGameLayer.RestoreRoute.HALL_AFTER_BATTLE -> {
                campaign.incStage() // Model.instance().incStage() before HallScene.
                showCampaignHall(campaign.snapshot.currentScenario)
            }
        }
        return true
    }

    fun showScenario(moduleName: String = initialScenario, entryScene: String = scenarioStartScene) {
        campaign.persist()
        campaign.enter(moduleName)
        replaceScreen(
        ScenarioPreviewScreen(this, moduleName, verifyMode, scenarioBranchVerifyMode, alternateScenarioBranchVerifyMode, scenarioChoiceScript, scenarioAllowPendingChoiceAfterScript, scenarioRandomSequence, scenarioInfoTransferRandomSequence, scenarioGlobals, scenarioUnitAttributes, scenarioVariables, scenarioAmbition, scenarioBattleRound, scenarioBattleCamp, scenarioBattleAttributes, scenarioBattlePositions, scenarioBattlePositionsByCamp, scenarioBattleEnemyDefeated, entryScene, scenarioStartLabel, scenarioChoiceTracePath, scenarioRandomTracePath, scenarioStopAfterRandomTrace, scenarioStopAfterRandomTraceCount, campaign.state)
        )
    }

    /** Source Hall/Battle scene replacement starts the next module dispatcher at scene0. */
    fun showNextScenario(moduleName: String) = showScenario(moduleName, "scene0")
    fun showBattleSandbox(
        sourceScenario: String = initialScenario.replaceFirst("R_", "S_"),
        returnScenario: String = battleReturnScenario ?: initialScenario.replaceFirst("S_", "R_"),
    ) = campaign.persist().let {
        replaceScreen(BattleLayer(this, battleVerifyMode, scriptedBattleVerifyMode, sourceScenario, returnScenario, campaign.state))
    }

    fun showBattlePreparation(returnScenario: String, sourceScenario: String, limit: ScenarioJoinBattleLimit, backgroundId: Int = 71) = campaign.persist().let {
        replaceScreen(BattlePreparationScreen(this, returnScenario, sourceScenario, limit, campaign.state, backgroundId))
    }

    /**
     * Original HallScene immediately resumes the current R script; it never
     * opens the porting-era blue "campaign management" substitute.  Loaded
     * Hall saves and the verification H shortcut therefore re-enter the
     * authored Hall scenario composition directly.
     */
    fun showCampaignHall(returnScenario: String) = showScenario(returnScenario)

    fun recordChoice(scenario: String, choice: String) = campaign.recordChoice(scenario, choice)
    fun completeBattle(scenario: String, nextScenario: String) = campaign.complete(scenario, nextScenario)
    /** Source BattleScene SAVE_GAME route used by MenuLayer's save command. */
    fun saveCampaign() = campaign.persist()
    /** Source Manager.saveGame numbered manual slot used by SaveLayer. */
    fun saveCampaign(index: Int): String = campaign.saveSlot(index)
    fun savedCampaignSlot(index: Int): String? = campaign.loadSlot(index)
    fun requestedCaptureState(): String? = screenshotState
    fun hasFrameCaptureRequest(): Boolean = screenshotPath != null || rawCapturePath != null
    fun hasRenderEventLogRequest(): Boolean = renderEventLogPath != null
    fun requestedFullBattleTrace(): FullBattleTraceConfig? = fullBattleTraceConfig
    fun requestedYingchuanEntryFlowTracePath(): String? = yingchuanEntryFlowTracePath
    fun campaignStage(): Int = campaign.snapshot.stage
    fun advanceCampaignStage() = campaign.incStage()
    fun setCampaignStage(stage: Int) = campaign.setStage(stage)

    /** Writes renderer metadata without framebuffer readback or PNG creation. */
    fun writeRenderEventLogIfRequested(): Boolean {
        val path = renderEventLogPath ?: return false
        val jsonl = when (val current = screen) {
            is TitleScreen -> current.renderEventLog()
            is ScenarioPreviewScreen -> current.renderEventLog()
            is BattlePreparationScreen -> current.renderEventLog()
            is RewardFixtureScreen -> current.renderEventLog()
            is SystemOverlayFixtureScreen -> current.renderEventLog()
            is DialogueFixtureScreen -> current.renderEventLog()
            is Choose2FixtureScreen -> current.renderEventLog()
            is InputBoxFixtureScreen -> current.renderEventLog()
            is MsgBox3FixtureScreen -> current.renderEventLog()
            is EditRosterRouteScreen -> current.renderEventLog()
            is BattleUnitEditRouteScreen -> current.renderEventLog()
            is LearnUnitSkillRouteScreen -> current.renderEventLog()
            is DefineUnitRouteScreen -> current.renderEventLog()
            is CmdRouteScreen -> current.renderEventLog()
            is ModalLoadRouteScreen -> current.renderEventLog()
            is NoticeInfoFixtureScreen -> current.renderEventLog()
            is TerminalSceneRouteScreen -> current.renderEventLog()
            is RaffleGateRouteScreen -> current.renderEventLog()
            is AchievementsFixtureScreen -> current.renderEventLog()
            is AttributeFixtureScreen -> current.renderEventLog()
            is GenericListFixtureScreen -> current.renderEventLog()
            is BattleLayer -> current.renderEventLog()
            else -> RenderEventLog().apply {
                draw("state", current?.javaClass?.simpleName ?: "none", "Canvas", "none", 0f, 0f, 0f, 0f, visible = false)
            }.jsonl()
        }
        Gdx.files.absolute(path).also { it.parent().mkdirs() }.writeString(jsonl, false)
        Gdx.app.log("JojoPort", "RENDER_EVENT_LOG_OK: $path")
        Gdx.app.exit()
        return true
    }
    fun requestedMapTextureDumpPath(): String? = mapTextureDumpPath
    fun requestedMapDither(): Boolean? = when (mapDither) {
        null -> null
        "enabled" -> true
        "disabled" -> false
        else -> error("--map-dither must be enabled or disabled")
    }
    fun requestedMapFilter(): Texture.TextureFilter? = when (mapFilter) {
        null -> null
        "linear" -> Texture.TextureFilter.Linear
        "nearest" -> Texture.TextureFilter.Nearest
        else -> error("--map-filter must be linear or nearest")
    }
    fun requestedCocos8MapSampler(): Boolean = when (mapSampler) {
        null, "cocos8", "frag8" -> true
        "linear" -> false
        else -> error("--map-sampler must be linear, cocos8, or frag8")
    }
    /** Cocos map sampling is aligned to physical framebuffer pixel centres. */
    fun requestedFragmentCoordinateMapSampler(): Boolean = mapSampler == null || mapSampler == "frag8"
    /**
     * Keep the logical Cocos quad unshifted. An explicit map-only option is
     * retained solely for regression sweeps; the source-faithful default uses
     * physical framebuffer pixel-centre sampling in BattleLayer.
     */
    fun requestedMapSampleOffset(): Pair<Float, Float> = mapSampleOffset ?: (0f to 0f)

    /**
     * Isolated R_00 map-quad candidate metadata.  This is emitted by the
     * same live renderer that supplies the framebuffer; it intentionally
     * records the Cocos source texture identity and quad contract rather
     * than treating a PNG position as an oracle.
     */
    fun writeMapQuadCandidateSidecar() {
        val png = screenshotPath ?: return
        if (screenshotState != "map-only") return
        Gdx.files.absolute(png.removeSuffix(".png") + ".sidecar.json").writeString(
            """{"fixtureVersion":1,"state":"R_00-postload-map-only","candidate":"libgdx-cocos-map-quad-final-draw","mapTexture":{"uuid":"4afa0804-1ac2-4d59-97e4-1549a9425953","nativePath":"assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg","size":[960,960]},"mapSpriteFrame":{"rect":[0,0,960,960],"uv":[0,1,1,1,0,0,1,0]},"mapMaterial":{"name":"builtin-2d-sprite","blend":"SRC_ALPHA,ONE_MINUS_SRC_ALPHA","filter":"LINEAR","wrap":"CLAMP_TO_EDGE","sampler":"cocos-8bit-rounded-bilinear/physical-pixel-centre","sampleCenterOffset":[0.0,0.0]},"runtime":{"visible":[1488.3721,800.0],"design":[1280.0,800.0],"drawingBuffer":[${Gdx.graphics.backBufferWidth},${Gdx.graphics.backBufferHeight}],"contentPosition":[-104.18605,0.0],"map":{"active":true,"position":[0.0,0.0],"size":[960.0,960.0],"scale":[2.0,2.0],"anchor":[0.5,0.5]},"mapAncestorTransforms":[{"name":"Canvas","projection":"SHOW_ALL/orthographic"},{"name":"ScrollView","stencilClip":true,"view":[1488.3721,800.0]},{"name":"content","position":[-104.18605,0.0]},{"name":"map","draw":[-320.0,-560.0,1920.0,1920.0],"rasterSampleOffset":[0.0,0.0]}],"activeVisualPaths":["Battle/Canvas/Layer/ScrollView/view/content/map"]}}""",
            false,
        )
    }

    /** `--capture-state=sprite:<character>:<action>:<dir>:<tick>:<camp>` fixture. */
    private fun spriteFixtureRequest(): SpriteFixtureRequest? {
        val parts = screenshotState?.takeIf { it.startsWith("sprite:") }?.split(':') ?: return null
        require(parts.size == 6) { "sprite fixture requires character:action:dir:tick:camp" }
        val faction = when (parts[5].toInt()) {
            0 -> Faction.PLAYER
            1 -> Faction.FRIEND
            2 -> Faction.ENEMY
            3 -> Faction.REINFORCEMENTS
            else -> error("sprite fixture camp must be 0, 1, 2, or 3")
        }
        return SpriteFixtureRequest(parts[1].toInt(), parts[2].toInt(), parts[3].toInt(), parts[4].toInt(), faction)
    }

    /** Native framebuffer capture used for source-versus-port visual regression. */
    fun captureFrameIfRequested(): Boolean {
        val path = screenshotPath ?: return false
        if (screenshotState == "yingchuan-dialogue-1") {
            // Electron's WebGL canvas is created as an opaque presentation
            // buffer: source-over affects RGB, while readPixels reports 255
            // for the final alpha channel.  The LWJGL window exposes a real
            // alpha channel, so normalize only that channel at the readback
            // boundary without changing any rendered RGB or sprite blend.
            Gdx.gl.glColorMask(false, false, false, true)
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            Gdx.gl.glColorMask(true, true, true, true)
        }
        val raw = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight)
        rawCapturePath?.let { rawPath ->
            val bytes = ByteArray(raw.width * raw.height * 4)
            raw.pixels.rewind()
            raw.pixels.get(bytes)
            raw.pixels.rewind()
            Gdx.files.absolute(rawPath).writeBytes(bytes, false)
        }
        compositionTracePath?.let { tracePath ->
            val trace = when (val current = screen) {
                is BattleLayer -> current.compositionTrace()
                is InfoLayerFixtureScreen -> current.compositionTrace()
                is ScenarioPreviewScreen -> current.compositionTrace()
                is BattlePreparationScreen -> current.compositionTrace()
                else -> "{\"state\":\"unavailable\",\"records\":[]}"
            }
            Gdx.files.absolute(tracePath).writeString(trace, false)
        }
        // OpenGL readback is bottom-up; Cocos capturePage and normal desktop
        // screenshots are top-down, so normalize before image comparison.
        val pixmap = Pixmap(raw.width, raw.height, raw.format)
        for (y in 0 until raw.height) for (x in 0 until raw.width) {
            pixmap.drawPixel(x, raw.height - 1 - y, raw.getPixel(x, y))
        }
        raw.dispose()
        PixmapIO.writePNG(Gdx.files.absolute(path), pixmap)
        pixmap.dispose()
        // Strict visual comparisons must bind a PNG to its layer state; a
        // full-frame battle backdrop is not UnitInfoLayer conformance.
        if (screenshotState == "yingchuan-unit-info") {
            Gdx.files.absolute(path.removeSuffix(".png") + "-stack.json").writeString(
                "{\"captureState\":\"yingchuan-unit-info\",\"root\":{\"width\":1488.3720930232557,\"height\":800},\"bg1\":{\"width\":1094,\"height\":776,\"position\":[0,0]},\"selectedRoute\":\"ForcesListLayer.content.children[0] TOUCH_END\",\"port\":true}",
                false,
            )
        }
        Gdx.app.log("JojoPort", "RENDER_CAPTURE_OK: $path")
        Gdx.app.exit()
        return true
    }

    /** Sidecar observation for pixel fixtures; it never changes battle state. */
    fun writeCaptureStack(requested: String, requestedPresent: Boolean, dialogue: Boolean, choice: Boolean, modalCount: Int) {
        val png = screenshotPath ?: return
        val sidecar = png.removeSuffix(".png") + "-stack.json"
        val overlayCount = (if (dialogue) 1 else 0) + (if (choice) 1 else 0) + modalCount
        Gdx.files.absolute(sidecar).writeString(
            """{"requested":"$requested","state":"open","requestedPresent":$requestedPresent,"activeDialogueOverlayCount":${if (dialogue) 1 else 0},"activeChoiceOverlayCount":${if (choice) 1 else 0},"activeModalOverlayCount":$modalCount,"activeOverlayCountAfter":$overlayCount}""",
            false,
        )
    }

    private fun replaceScreen(next: Screen) {
        screen?.dispose()
        setScreen(next)
    }
}

private data class SpriteFixtureRequest(val characterId: Int, val action: Int, val direction: Int, val frameTick: Int, val faction: Faction)

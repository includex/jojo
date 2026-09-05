package com.jojo.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen

class JojoGame(private val configuration: GameLaunchConfiguration = GameLaunchConfiguration()) : Game() {
    private val scenarioRun get() = configuration.scenarioRun
    private val capture get() = configuration.capture
    private val verifyMode get() = configuration.verification.scenario
    private val battleVerifyMode get() = configuration.verification.battle
    private val scenarioBranchVerifyMode get() = configuration.verification.firstBranch
    private val alternateScenarioBranchVerifyMode get() = configuration.verification.alternateBranch
    private val scriptedBattleVerifyMode get() = configuration.verification.scriptedBattle
    private val scenarioChoiceScript get() = scenarioRun.choices
    private val scenarioAllowPendingChoiceAfterScript get() = scenarioRun.allowPendingChoiceAfterScript
    private val scenarioGlobals get() = scenarioRun.globals
    private val scenarioUnitAttributes get() = scenarioRun.unitAttributes
    private val scenarioVariables get() = scenarioRun.variables
    private val scenarioAmbition get() = scenarioRun.ambition
    private val scenarioRandomSequence get() = scenarioRun.randomSequence
    private val scenarioInfoTransferRandomSequence get() = scenarioRun.infoTransferRandomSequence
    private val scenarioBattleRound get() = scenarioRun.battleRound
    private val scenarioBattleCamp get() = scenarioRun.battleCamp
    private val scenarioBattleAttributes get() = scenarioRun.battleAttributes
    private val scenarioBattlePositions get() = scenarioRun.battlePositions
    private val scenarioBattlePositionsByCamp get() = scenarioRun.battlePositionsByCamp
    private val scenarioBattleEnemyDefeated get() = scenarioRun.battleEnemyDefeated
    private val scenarioStartScene get() = scenarioRun.startScene
    private val scenarioStartLabel get() = scenarioRun.startLabel
    private val scenarioChoiceTracePath get() = scenarioRun.choiceTracePath
    private val scenarioRandomTracePath get() = scenarioRun.randomTracePath
    private val scenarioStopAfterRandomTrace get() = scenarioRun.stopAfterRandomTrace
    private val scenarioStopAfterRandomTraceCount get() = scenarioRun.stopAfterRandomTraceCount
    private val initialScenario get() = configuration.initialScenario
    private val battleReturnScenario get() = configuration.battleReturnScenario
    private val screenshotState get() = capture.state
    private val fullBattleTraceConfig get() = configuration.fullBattleTrace
    private val yingchuanEntryFlowTracePath get() = configuration.yingchuanEntryFlowTracePath
    private val campaignE2eTraceConfig get() = configuration.campaignE2eTrace
    private val automatedRun get() = configuration.automatedRun
    private val preferenceProvider = GamePreferenceProvider(automatedRun) { name -> Gdx.app.getPreferences(name) }
    private val campaign by lazy { CampaignStore(preferenceProvider.campaign()) }
    private val campaignE2eDriver by lazy { campaignE2eTraceConfig?.let(::CampaignE2eDriver) }
    private val standaloneBattleInputDriver by lazy {
        fullBattleTraceConfig?.takeIf { campaignE2eTraceConfig == null }
            ?.let { config ->
                ProductionBattleInputDriver(
                    inputIntervalSeconds = config.driverIntervalSeconds,
                    onInput = { context -> (screen as? BattleScreen)?.recordFullBattleInput(context) },
                    // The original standalone full-battle harness enters
                    // entrusted control through END_ROUND/MsgBox4 without
                    // committing a player move first.  Campaign E2E keeps
                    // its separate manual-move proof, while S52/S57 ignore
                    // this limit because they require authored room input.
                    manualMoveAttemptLimit = 0,
                )
            }
    }
    private val renderArtifacts by lazy { RenderArtifactService(capture) }

    internal fun campaignE2eScenarioStarted(module: String, index: Int) {
        campaignE2eDriver?.scenarioStarted(module, index)
    }

    internal fun preferences(name: String) = preferenceProvider.get(name)
    internal fun settingsPreferences() = preferenceProvider.settings()

    override fun create() {
        GameStartupCoordinator(
            configuration = configuration,
            campaignState = campaign.state,
            routeCaptureFixture = {
                CaptureFixtureStartupRouter(
                    game = this,
                    captureState = screenshotState,
                    campaignState = campaign.state,
                    showScreen = ::replaceScreen,
                    showBattlePreparation = { returnScenario, sourceScenario, limit, backgroundId ->
                        showBattlePreparation(returnScenario, sourceScenario, limit, backgroundId)
                    },
                ).route()
            },
            showBattle = { showBattleSandbox() },
            showTitle = { showTitleScreen() },
            showScenario = { showScenario(it) },
            savedScenario = { campaign.snapshot.currentScenario },
        ).start()
    }

    override fun render() {
        super.render()
        campaignE2eDriver?.update(Gdx.graphics.deltaTime, screen)
        if (campaignE2eDriver == null) {
            (screen as? BattleScreen)?.let { battle ->
                standaloneBattleInputDriver?.update(Gdx.graphics.deltaTime, battle.campaignE2eState())
            }
        }
    }

    fun showTitleScreen() {
        val requestedState = screenshotState
        replaceScreen(TitleScreen(
            this,
            initialSettingOpen = requestedState == "login-setting",
            initialLoadOpen = requestedState == "login-load" || requestedState?.startsWith("login-load-row") == true,
            initialLoadRow = requestedState?.removePrefix("login-load-row")
                ?.takeIf { requestedState.startsWith("login-load-row") }
                ?.toIntOrNull(),
        ))
    }
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
     * manager and may call register with a returned activation payload. This game deliberately
     * has no network/hot-update backend, so it completes asynchronously with "not registered";
     * TitleScreen still preserves the source LoadingLayer attach/callback/detach lifecycle.
     */
    internal fun requestRegistrationCheck(complete: (Boolean) -> Unit) {
        Gdx.app.log("JojoGame", "CHECK_REGISTER requested; platform registerCheck is unsupported")
        Gdx.app.postRunnable { complete(false) }
    }

    /** Desktop helper returns no supportAd entitlement, but the source-gated route stays live. */
    internal fun settingFeatureEnvironment(sceneName: String) = SettingLayer.FeatureEnvironment(
        sceneName = sceneName,
        supportAdCode = 0,
        nowSeconds = (System.currentTimeMillis() / 1000L).toInt(),
        battleName = GameDataCatalog.load()::battleName,
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
        ScenarioScreen(this, moduleName, verifyMode, scenarioBranchVerifyMode, alternateScenarioBranchVerifyMode, scenarioChoiceScript, scenarioAllowPendingChoiceAfterScript, scenarioRandomSequence, scenarioInfoTransferRandomSequence, scenarioGlobals, scenarioUnitAttributes, scenarioVariables, scenarioAmbition, scenarioBattleRound, scenarioBattleCamp, scenarioBattleAttributes, scenarioBattlePositions, scenarioBattlePositionsByCamp, scenarioBattleEnemyDefeated, entryScene, scenarioStartLabel, scenarioChoiceTracePath, scenarioRandomTracePath, scenarioStopAfterRandomTrace, scenarioStopAfterRandomTraceCount, campaign.state)
        )
    }

    /** Source Hall/Battle scene replacement starts the next module dispatcher at scene0. */
    fun showNextScenario(moduleName: String) = showScenario(moduleName, "scene0")
    fun showBattleSandbox(
        sourceScenario: String = initialScenario.replaceFirst("R_", "S_"),
        returnScenario: String = battleReturnScenario ?: initialScenario.replaceFirst("S_", "R_"),
    ) = campaign.persist().let {
        replaceScreen(BattleScreen(this, battleVerifyMode, scriptedBattleVerifyMode, sourceScenario, returnScenario, campaign.state))
    }

    fun showBattlePreparation(returnScenario: String, sourceScenario: String, limit: ScenarioJoinBattleLimit, backgroundId: Int = 71) = campaign.persist().let {
        replaceScreen(BattlePreparationScreen(this, returnScenario, sourceScenario, limit, campaign.state, backgroundId))
    }

    /**
     * Original HallScene immediately resumes the current R script; it never
     * opens the obsolete blue "campaign management" substitute. Loaded
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
    fun hasFrameCaptureRequest(): Boolean = renderArtifacts.hasFrameCaptureRequest()
    fun hasRenderEventLogRequest(): Boolean = renderArtifacts.hasRenderEventLogRequest()
    fun requestedFullBattleTrace(): FullBattleTraceConfig? = fullBattleTraceConfig
    fun requestedYingchuanEntryFlowTracePath(): String? = yingchuanEntryFlowTracePath
    fun campaignStage(): Int = campaign.snapshot.stage
    fun advanceCampaignStage() = campaign.incStage()
    fun setCampaignStage(stage: Int) = campaign.setStage(stage)

    /** Writes renderer metadata without framebuffer readback or PNG creation. */
    fun writeRenderEventLogIfRequested(): Boolean = renderArtifacts.writeRenderEventLogIfRequested(screen)
    fun requestedMapTextureDumpPath(): String? = renderArtifacts.requestedMapTextureDumpPath()
    fun requestedMapDither(): Boolean? = renderArtifacts.requestedMapDither()
    fun requestedMapFilter() = renderArtifacts.requestedMapFilter()
    fun requestedCocos8MapSampler(): Boolean = renderArtifacts.requestedCocos8MapSampler()
    /** Cocos map sampling is aligned to physical framebuffer pixel centres. */
    fun requestedFragmentCoordinateMapSampler(): Boolean = renderArtifacts.requestedFragmentCoordinateMapSampler()
    /**
     * Keep the logical Cocos quad unshifted. An explicit map-only option is
     * retained solely for regression sweeps; the source-faithful default uses
     * physical framebuffer pixel-centre sampling in BattleScreen.
     */
    fun requestedMapSampleOffset(): Pair<Float, Float> = renderArtifacts.requestedMapSampleOffset()

    /**
     * Isolated R_00 map-quad candidate metadata.  This is emitted by the
     * same live renderer that supplies the framebuffer; it intentionally
     * records the Cocos source texture identity and quad contract rather
     * than treating a PNG position as an oracle.
     */
    fun writeMapQuadCandidateSidecar() = renderArtifacts.writeMapQuadCandidateSidecar()

    /** Native framebuffer capture used for source-versus-game visual regression. */
    fun captureFrameIfRequested(): Boolean = renderArtifacts.captureFrameIfRequested(screen)

    /** Sidecar observation for pixel fixtures; it never changes battle state. */
    fun writeCaptureStack(requested: String, requestedPresent: Boolean, dialogue: Boolean, choice: Boolean, modalCount: Int) =
        renderArtifacts.writeCaptureStack(requested, requestedPresent, dialogue, choice, modalCount)

    private fun replaceScreen(next: Screen) {
        screen?.dispose()
        setScreen(next)
    }
}

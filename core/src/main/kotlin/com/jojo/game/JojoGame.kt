package com.jojo.game
import com.jojo.game.application.navigation.GameScreenNavigator
import com.jojo.game.infrastructure.data.CampaignStore
import com.jojo.game.presentation.battle.BattleScreen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen

/**
 * class  `JojoGame`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
    private val screenNavigator by lazy { GameScreenNavigator(this, configuration, campaign, ::replaceScreen) }
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

    fun showTitleScreen() = screenNavigator.showTitleScreen()
    fun showTitleLoadScreen() = screenNavigator.showTitleLoadScreen()
    fun showTitleSettingScreen(returnScenario: String? = null) = screenNavigator.showTitleSettingScreen(returnScenario)
    fun startNewGame() = screenNavigator.startNewGame()
    fun loadCampaign() = screenNavigator.loadCampaign()
    fun loadCampaignSlot(index: Int): String? = screenNavigator.loadCampaignSlot(index)
    fun savedLoadPage(): Int = screenNavigator.savedLoadPage()
    fun saveLoadPage(page: Int) = screenNavigator.saveLoadPage(page)
    fun titleLoadGameLayer(): LoadGameLayer = screenNavigator.titleLoadGameLayer()


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

    fun restoreCampaignSlot(index: Int, raw: String, route: LoadGameLayer.RestoreRoute): Boolean =
        screenNavigator.restoreCampaignSlot(index, raw, route)

    fun showScenario(moduleName: String = initialScenario, entryScene: String = scenarioStartScene) =
        screenNavigator.showScenario(moduleName, entryScene)

    fun showNextScenario(moduleName: String) = screenNavigator.showScenario(moduleName, "scene0")
    fun showBattleSandbox(
        sourceScenario: String = initialScenario.replaceFirst("R_", "S_"),
        returnScenario: String = battleReturnScenario ?: initialScenario.replaceFirst("S_", "R_"),
    ) = screenNavigator.showBattleSandbox(sourceScenario, returnScenario)

    fun showBattlePreparation(
        returnScenario: String,
        sourceScenario: String,
        limit: ScenarioJoinBattleLimit,
        backgroundId: Int = 71
    ) =
        screenNavigator.showBattlePreparation(returnScenario, sourceScenario, limit, backgroundId)

    fun showCampaignHall(returnScenario: String) = screenNavigator.showCampaignHall(returnScenario)

    fun recordChoice(scenario: String, choice: String) = screenNavigator.recordChoice(scenario, choice)
    fun completeBattle(scenario: String, nextScenario: String) = screenNavigator.completeBattle(scenario, nextScenario)
    fun saveCampaign() = screenNavigator.saveCampaign()
    fun saveCampaign(index: Int): String = screenNavigator.saveCampaign(index)
    fun savedCampaignSlot(index: Int): String? = screenNavigator.savedCampaignSlot(index)
    fun campaignStage(): Int = screenNavigator.campaignStage()
    fun advanceCampaignStage() = screenNavigator.advanceCampaignStage()
    fun setCampaignStage(stage: Int) = screenNavigator.setCampaignStage(stage)
    fun requestedCaptureState(): String? = screenshotState
    fun hasFrameCaptureRequest(): Boolean = renderArtifacts.hasFrameCaptureRequest()
    fun hasRenderEventLogRequest(): Boolean = renderArtifacts.hasRenderEventLogRequest()
    fun requestedFullBattleTrace(): FullBattleTraceConfig? = fullBattleTraceConfig
    fun requestedYingchuanEntryFlowTracePath(): String? = yingchuanEntryFlowTracePath

    /** Writes renderer metadata without framebuffer readback or PNG creation. */
    fun writeRenderEventLogIfRequested(): Boolean = renderArtifacts.writeRenderEventLogIfRequested(screen)

    /**
     * 공개 메서드 `requestedMapTextureDumpPath`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedMapTextureDumpPath(): String? = renderArtifacts.requestedMapTextureDumpPath()

    /**
     * 공개 메서드 `requestedMapDither`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedMapDither(): Boolean? = renderArtifacts.requestedMapDither()

    /**
     * 공개 메서드 `requestedMapFilter`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedMapFilter() = renderArtifacts.requestedMapFilter()

    /**
     * 공개 메서드 `requestedCocos8MapSampler`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedCocos8MapSampler(): Boolean = renderArtifacts.requestedCocos8MapSampler()

    /** Cocos map sampling is aligned to physical framebuffer pixel centres. */
    fun requestedFragmentCoordinateMapSampler(): Boolean = renderArtifacts.requestedFragmentCoordinateMapSampler()
    /**
     * Keep the logical Cocos quad unshifted. An explicit map-only option is
     * retained solely for regression sweeps; the source-faithful default uses
     * physical framebuffer pixel-centre sampling in BattleScreen.
     */
    /**
     * 공개 메서드 `requestedMapSampleOffset`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Pair<Float, Float>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedMapSampleOffset(): Pair<Float, Float> = renderArtifacts.requestedMapSampleOffset()

    /**
     * Isolated R_00 map-quad candidate metadata.  This is emitted by the
     * same live renderer that supplies the framebuffer; it intentionally
     * records the Cocos source texture identity and quad contract rather
     * than treating a PNG position as an oracle.
     */
    /**
     * 공개 메서드 `writeMapQuadCandidateSidecar`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun writeMapQuadCandidateSidecar() = renderArtifacts.writeMapQuadCandidateSidecar()

    /** Native framebuffer capture used for source-versus-game visual regression. */
    fun captureFrameIfRequested(): Boolean = renderArtifacts.captureFrameIfRequested(screen)

    /** Sidecar observation for pixel fixtures; it never changes battle state. */
    fun writeCaptureStack(
        requested: String,
        requestedPresent: Boolean,
        dialogue: Boolean,
        choice: Boolean,
        modalCount: Int
    ) =
        renderArtifacts.writeCaptureStack(requested, requestedPresent, dialogue, choice, modalCount)

    private fun replaceScreen(next: Screen) {
        screen?.dispose()
        setScreen(next)
    }
}

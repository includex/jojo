// Game
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.infrastructure.preferences.GamePreferenceProvider
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.application.navigation.GameScreenNavigator
import com.jojo.game.application.runtime.runtimeProbe
import com.jojo.game.application.runtime.RuntimeArtifactEvent
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeTitleStartupDriver
import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleObserver
import com.jojo.game.application.runtime.RuntimeBattlePreparationDriver
import com.jojo.game.application.runtime.BattleTraceRuntimeConfig
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.application.runtime.GameStartupCoordinator
import com.jojo.game.application.runtime.RuntimeStartupRouter
import com.jojo.game.infrastructure.data.CampaignStore
import com.jojo.game.presentation.battle.BattleScreen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen


class JojoGame(private val configuration: GameLaunchConfiguration = GameLaunchConfiguration()) : Game() {
    private val scenarioRun get() = configuration.scenarioRun
    private val capture get() = configuration.capture
    private val battleVerifyMode get() = configuration.verification.battle
    private val scriptedBattleVerifyMode get() = configuration.verification.scriptedBattle
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
    private val scenarioStopAfterRandomTrace get() = scenarioRun.stopAfterRandomTrace
    private val scenarioStopAfterRandomTraceCount get() = scenarioRun.stopAfterRandomTraceCount
    private val initialScenario get() = configuration.initialScenario
    private val battleReturnScenario get() = configuration.battleReturnScenario
    private val screenshotState get() = capture.state
    private val battleTraceRuntime get() = configuration.battleTraceRuntime
    private val yingchuanEntryFlowTracePath get() = configuration.yingchuanEntryFlowTracePath
    private val automatedRun get() = configuration.automatedRun
    private val preferenceProvider = GamePreferenceProvider(automatedRun) { name -> Gdx.app.getPreferences(name) }
    private val campaign by lazy { CampaignStore(preferenceProvider.campaign()) }
    private val screenNavigator by lazy { GameScreenNavigator(this, configuration, campaign, ::replaceScreen) }
    private fun notifyArtifact(event: RuntimeArtifactEvent) {
        configuration.runtimeArtifactObserver?.onArtifact(event)
    }

    /** 시나리오 시작 알림을 외부 관찰자에게 전달한다. */
    internal fun scenarioStarted(module: String, index: Int) {
        configuration.runtimeScreenObserver?.scenarioStarted(module, index)
    }

    internal fun externalScenarioDriverKeepsScreenOpen(): Boolean =
        configuration.runtimeScreenObserver?.keepsScenarioOpen == true

    internal fun runtimeScenarioDriver() = configuration.runtimeScenarioDriver

    internal fun preferences(name: String) = preferenceProvider.get(name)
    internal fun settingsPreferences() = preferenceProvider.settings()

    override fun create() {
        GameStartupCoordinator(
            configuration = configuration,
            campaignState = campaign.state,
            routeRuntimeStartup = {
                RuntimeStartupRouter(
                    game = this,
                    state = screenshotState,
                    campaign = campaign.state,
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
        val probe = screen.runtimeProbe()
        configuration.runtimeScreenObserver?.update(Gdx.graphics.deltaTime, probe)
        configuration.runtimeArtifactObserver?.onFrame(screen, probe)
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


    /** Login.registerCheck의 데스크톱 대체 경로이다.
     * 네트워크 갱신 서버가 없으므로 비동기로 미등록 결과를 전달하면서 LoadingLayer의 연결·콜백·해제 수명 주기는 유지한다. */
    internal fun requestRegistrationCheck(complete: (Boolean) -> Unit) {
        Gdx.app.log("JojoGame", "CHECK_REGISTER requested; platform registerCheck is unsupported")
        Gdx.app.postRunnable { complete(false) }
    }

    /** 데스크톱 환경의 광고 지원 권한은 없지만 관련 경로는 유지한다. */
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
    fun hasFrameCaptureRequest(): Boolean = configuration.runtimeArtifactObserver?.wantsFrame == true
    fun hasRenderEventLogRequest(): Boolean = configuration.runtimeArtifactObserver?.wantsEventLog == true
    fun requestedBattleTraceRuntime(): BattleTraceRuntimeConfig? = battleTraceRuntime
    fun requestedYingchuanEntryFlowTracePath(): String? = yingchuanEntryFlowTracePath
    fun runtimeBattleDriver(): RuntimeBattleDriver? = configuration.runtimeBattleDriver
    fun runtimeTitleStartupDriver(): RuntimeTitleStartupDriver? = configuration.runtimeTitleStartupDriver
    fun runtimeBattlePresentation(): RuntimeBattlePresentation = configuration.runtimeBattlePresentation
    fun runtimeBattleObserver(): RuntimeBattleObserver? = configuration.runtimeBattleObserver
    fun runtimeBattlePreparationDriver(): RuntimeBattlePreparationDriver? = configuration.runtimeBattlePreparationDriver

    /** 프레임버퍼를 읽지 않고 렌더러 메타데이터를 기록한다. */
    fun writeRenderEventLogIfRequested(): Boolean {
        if (!hasRenderEventLogRequest()) return false
        notifyArtifact(RuntimeArtifactEvent.EventLog(screenshotState, screen))
        return true
    }

    fun requestedMapDither(): Boolean? = null


    fun requestedMapFilter(): com.badlogic.gdx.graphics.Texture.TextureFilter? = null


    fun requestedCocos8MapSampler(): Boolean = true

    /** 맵 표본 좌표를 실제 프레임버퍼 픽셀 중심에 맞춘다. */
    fun requestedFragmentCoordinateMapSampler(): Boolean = true
    /** 논리적인 Cocos 사각형을 이동하지 않고 유지한다.
     * 지도 전용 옵션은 회귀 검사에만 사용하며, 기본 경로는 BattleScreen의 실제 프레임버퍼 중심을 표본화한다. */

    fun requestedMapSampleOffset(): Pair<Float, Float> = 0f to 0f

    /** R_00 지도 사각형 후보 메타데이터를 별도로 기록한다.
     * 프레임버퍼를 제공하는 실제 렌더러가 원본 텍스처 식별자와 사각형 계약을 기록하며 PNG 위치를 정답으로 간주하지 않는다. */

    fun writeMapQuadCandidateSidecar() = notifyArtifact(RuntimeArtifactEvent.MapSidecar(screenshotState))

    /** 원본과 게임 화면을 비교하는 시각 회귀 검사용 프레임버퍼를 캡처한다. */
    fun captureFrameIfRequested(): Boolean {
        if (!hasFrameCaptureRequest()) return false
        notifyArtifact(RuntimeArtifactEvent.Frame(screenshotState, screen))
        return true
    }

    /** 픽셀 검증용 부가 관측을 기록하며 전투 상태는 바꾸지 않는다. */
    fun writeCaptureStack(
        requested: String,
        requestedPresent: Boolean,
        dialogue: Boolean,
        choice: Boolean,
        modalCount: Int
    ) =
        notifyArtifact(RuntimeArtifactEvent.OverlayStack(screenshotState, requested, requestedPresent, dialogue, choice, modalCount))

    private fun replaceScreen(next: Screen) {
        screen?.dispose()
        setScreen(next)
    }
}

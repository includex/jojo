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


/**
 * `JojoGame`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class JojoGame(private val configuration: GameLaunchConfiguration = GameLaunchConfiguration()) : Game() {
    /**
     * `scenarioRun` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioRun get() = configuration.scenarioRun
    /**
     * `capture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val capture get() = configuration.capture
    /**
     * `battleVerifyMode` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val battleVerifyMode get() = configuration.verification.battle
    /**
     * `scriptedBattleVerifyMode` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scriptedBattleVerifyMode get() = configuration.verification.scriptedBattle
    /**
     * `scenarioGlobals` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioGlobals get() = scenarioRun.globals
    /**
     * `scenarioUnitAttributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioUnitAttributes get() = scenarioRun.unitAttributes
    /**
     * `scenarioVariables` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioVariables get() = scenarioRun.variables
    /**
     * `scenarioAmbition` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioAmbition get() = scenarioRun.ambition
    /**
     * `scenarioRandomSequence` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioRandomSequence get() = scenarioRun.randomSequence
    /**
     * `scenarioInfoTransferRandomSequence` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioInfoTransferRandomSequence get() = scenarioRun.infoTransferRandomSequence
    /**
     * `scenarioBattleRound` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioBattleRound get() = scenarioRun.battleRound
    /**
     * `scenarioBattleCamp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioBattleCamp get() = scenarioRun.battleCamp
    /**
     * `scenarioBattleAttributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioBattleAttributes get() = scenarioRun.battleAttributes
    /**
     * `scenarioBattlePositions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioBattlePositions get() = scenarioRun.battlePositions
    /**
     * `scenarioBattlePositionsByCamp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioBattlePositionsByCamp get() = scenarioRun.battlePositionsByCamp
    /**
     * `scenarioBattleEnemyDefeated` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioBattleEnemyDefeated get() = scenarioRun.battleEnemyDefeated
    /**
     * `scenarioStartScene` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioStartScene get() = scenarioRun.startScene
    /**
     * `scenarioStartLabel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioStartLabel get() = scenarioRun.startLabel
    /**
     * `scenarioStopAfterRandomTrace` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioStopAfterRandomTrace get() = scenarioRun.stopAfterRandomTrace
    /**
     * `scenarioStopAfterRandomTraceCount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scenarioStopAfterRandomTraceCount get() = scenarioRun.stopAfterRandomTraceCount
    /**
     * `initialScenario` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val initialScenario get() = configuration.initialScenario
    /**
     * `battleReturnScenario` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val battleReturnScenario get() = configuration.battleReturnScenario
    /**
     * `screenshotState` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val screenshotState get() = capture.state
    /**
     * `battleTraceRuntime` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val battleTraceRuntime get() = configuration.battleTraceRuntime
    /**
     * `yingchuanEntryFlowTracePath` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val yingchuanEntryFlowTracePath get() = configuration.yingchuanEntryFlowTracePath
    /**
     * `automatedRun` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val automatedRun get() = configuration.automatedRun
    /**
     * `preferenceProvider` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val preferenceProvider = GamePreferenceProvider(automatedRun) { name -> Gdx.app.getPreferences(name) }
    /**
     * `campaign` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val campaign by lazy { CampaignStore(preferenceProvider.campaign()) }
    /**
     * `screenNavigator` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val screenNavigator by lazy { GameScreenNavigator(this, configuration, campaign, ::replaceScreen) }
    /**
     * `notifyArtifact`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun notifyArtifact(event: RuntimeArtifactEvent) {
        configuration.runtimeArtifactObserver?.onArtifact(event)
    }

    /** 시나리오 시작 알림을 외부 관찰자에게 전달한다. */
    internal fun scenarioStarted(module: String, index: Int) {
        configuration.runtimeScreenObserver?.scenarioStarted(module, index)
    }

    /**
     * `externalScenarioDriverKeepsScreenOpen`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun externalScenarioDriverKeepsScreenOpen(): Boolean =
        configuration.runtimeScreenObserver?.keepsScenarioOpen == true

    /**
     * `runtimeScenarioDriver`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun runtimeScenarioDriver() = configuration.runtimeScenarioDriver

    /**
     * `preferences`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun preferences(name: String) = preferenceProvider.get(name)
    /**
     * `settingsPreferences`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun settingsPreferences() = preferenceProvider.settings()

    /**
     * `create`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `render`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun render() {
        super.render()
        val probe = screen.runtimeProbe()
        configuration.runtimeScreenObserver?.update(Gdx.graphics.deltaTime, probe)
        configuration.runtimeArtifactObserver?.onFrame(screen, probe)
    }

    /**
     * `showTitleScreen`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showTitleScreen() = screenNavigator.showTitleScreen()
    /**
     * `showTitleLoadScreen`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showTitleLoadScreen() = screenNavigator.showTitleLoadScreen()
    /**
     * `showTitleSettingScreen`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showTitleSettingScreen(returnScenario: String? = null) = screenNavigator.showTitleSettingScreen(returnScenario)
    /**
     * `startNewGame`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startNewGame() = screenNavigator.startNewGame()
    /**
     * `loadCampaign`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun loadCampaign() = screenNavigator.loadCampaign()
    /**
     * `loadCampaignSlot`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun loadCampaignSlot(index: Int): String? = screenNavigator.loadCampaignSlot(index)
    /**
     * `savedLoadPage`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun savedLoadPage(): Int = screenNavigator.savedLoadPage()
    /**
     * `saveLoadPage`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun saveLoadPage(page: Int) = screenNavigator.saveLoadPage(page)
    /**
     * `titleLoadGameLayer`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `restoreCampaignSlot`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun restoreCampaignSlot(index: Int, raw: String, route: LoadGameLayer.RestoreRoute): Boolean =
        screenNavigator.restoreCampaignSlot(index, raw, route)

    /**
     * `showScenario`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showScenario(moduleName: String = initialScenario, entryScene: String = scenarioStartScene) =
        screenNavigator.showScenario(moduleName, entryScene)

    /**
     * `showNextScenario`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showNextScenario(moduleName: String) = screenNavigator.showScenario(moduleName, "scene0")
    /**
     * `showBattleSandbox`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showBattleSandbox(
        sourceScenario: String = initialScenario.replaceFirst("R_", "S_"),
        returnScenario: String = battleReturnScenario ?: initialScenario.replaceFirst("S_", "R_"),
    ) = screenNavigator.showBattleSandbox(sourceScenario, returnScenario)

    /**
     * `showBattlePreparation`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showBattlePreparation(
        returnScenario: String,
        sourceScenario: String,
        limit: ScenarioJoinBattleLimit,
        backgroundId: Int = 71
    ) =
        screenNavigator.showBattlePreparation(returnScenario, sourceScenario, limit, backgroundId)

    /**
     * `showCampaignHall`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showCampaignHall(returnScenario: String) = screenNavigator.showCampaignHall(returnScenario)

    /**
     * `recordChoice`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun recordChoice(scenario: String, choice: String) = screenNavigator.recordChoice(scenario, choice)
    /**
     * `completeBattle`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun completeBattle(scenario: String, nextScenario: String) = screenNavigator.completeBattle(scenario, nextScenario)
    /**
     * `saveCampaign`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun saveCampaign() = screenNavigator.saveCampaign()
    /**
     * `saveCampaign`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun saveCampaign(index: Int): String = screenNavigator.saveCampaign(index)
    /**
     * `savedCampaignSlot`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun savedCampaignSlot(index: Int): String? = screenNavigator.savedCampaignSlot(index)
    /**
     * `campaignStage`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun campaignStage(): Int = screenNavigator.campaignStage()
    /**
     * `advanceCampaignStage`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun advanceCampaignStage() = screenNavigator.advanceCampaignStage()
    /**
     * `setCampaignStage`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setCampaignStage(stage: Int) = screenNavigator.setCampaignStage(stage)
    /**
     * `requestedCaptureState`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestedCaptureState(): String? = screenshotState
    /**
     * `hasFrameCaptureRequest`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hasFrameCaptureRequest(): Boolean = configuration.runtimeArtifactObserver?.wantsFrame == true
    /**
     * `hasRenderEventLogRequest`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hasRenderEventLogRequest(): Boolean = configuration.runtimeArtifactObserver?.wantsEventLog == true
    /**
     * `requestedBattleTraceRuntime`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestedBattleTraceRuntime(): BattleTraceRuntimeConfig? = battleTraceRuntime
    /**
     * `requestedYingchuanEntryFlowTracePath`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestedYingchuanEntryFlowTracePath(): String? = yingchuanEntryFlowTracePath
    /**
     * `runtimeBattleDriver`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun runtimeBattleDriver(): RuntimeBattleDriver? = configuration.runtimeBattleDriver
    /**
     * `runtimeTitleStartupDriver`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun runtimeTitleStartupDriver(): RuntimeTitleStartupDriver? = configuration.runtimeTitleStartupDriver
    /**
     * `runtimeBattlePresentation`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun runtimeBattlePresentation(): RuntimeBattlePresentation = configuration.runtimeBattlePresentation
    /**
     * `runtimeBattleObserver`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun runtimeBattleObserver(): RuntimeBattleObserver? = configuration.runtimeBattleObserver
    /**
     * `runtimeBattlePreparationDriver`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun runtimeBattlePreparationDriver(): RuntimeBattlePreparationDriver? = configuration.runtimeBattlePreparationDriver

    /** 프레임버퍼를 읽지 않고 렌더러 메타데이터를 기록한다. */
    fun writeRenderEventLogIfRequested(): Boolean {
        if (!hasRenderEventLogRequest()) return false
        notifyArtifact(RuntimeArtifactEvent.EventLog(screenshotState, screen))
        return true
    }

    /**
     * `requestedMapDither`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestedMapDither(): Boolean? = null


    /**
     * `requestedMapFilter`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestedMapFilter(): com.badlogic.gdx.graphics.Texture.TextureFilter? = null


    /**
     * `requestedCocos8MapSampler`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `replaceScreen`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun replaceScreen(next: Screen) {
        screen?.dispose()
        setScreen(next)
    }
}

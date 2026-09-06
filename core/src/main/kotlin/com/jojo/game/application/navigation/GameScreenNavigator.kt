// Navigation
package com.jojo.game.application.navigation
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.JojoGame
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit
import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.presentation.scenario.trace.ScenarioRandomTraceConfiguration
import com.jojo.game.presentation.title.TitleScreen
import com.jojo.game.presentation.battle.BattleScreen
import com.jojo.game.application.runtime.BattleVerificationRuntime
import com.jojo.game.presentation.battle.preparation.BattlePreparationScreen
import com.jojo.game.presentation.shared.overlay.LoadGameLayer
import com.jojo.game.infrastructure.data.CampaignStore
import com.jojo.game.infrastructure.data.BattleTerrainLoader

import com.badlogic.gdx.Screen

/**
 * `CampaignRestoreDestination` 클래스: navigation 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal enum class CampaignRestoreDestination { BATTLE, HALL, HALL_AFTER_BATTLE }

/**
 * `campaignRestoreDestination`: 입력을 규칙에 따라 계산·변환한다.
 * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
 */

internal fun campaignRestoreDestination(route: LoadGameLayer.RestoreRoute): CampaignRestoreDestination = when (route) {
    LoadGameLayer.RestoreRoute.BATTLE -> CampaignRestoreDestination.BATTLE
    LoadGameLayer.RestoreRoute.HALL -> CampaignRestoreDestination.HALL
    LoadGameLayer.RestoreRoute.HALL_AFTER_BATTLE -> CampaignRestoreDestination.HALL_AFTER_BATTLE
}

/** GameScreenNavigator: 캠페인 저장 상태와 시작 설정을 반영해 게임 화면 전환을 수행하는 응용 경로다. */
internal class GameScreenNavigator(
    /**
     * `game` (JojoGame,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val game: JojoGame,
    /**
     * `configuration` (GameLaunchConfiguration,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val configuration: GameLaunchConfiguration,
    /**
     * `campaign` (CampaignStore,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val campaign: CampaignStore,
    /**
     * `replaceScreen` ((Screen) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val replaceScreen: (Screen) -> Unit,
) {
    /**
     * `run` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val run get() = configuration.scenarioRun
    /**
     * `initialScenario` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val initialScenario get() = configuration.initialScenario

    /**
     * `showTitleScreen`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showTitleScreen() {
        val startup = game.runtimeTitleStartupDriver()?.presentation()
        replaceScreen(
            TitleScreen(
            game,
            startup?.settingsOpen == true,
            startup?.loadOpen == true,
            startup?.loadRow,
            useInitialSettings = startup?.useInitialSettings == true,
        )
        )
    }

    /**
     * `showTitleLoadScreen`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showTitleLoadScreen() = replaceScreen(TitleScreen(game, initialLoadOpen = true))
    /**
     * `showTitleSettingScreen`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showTitleSettingScreen(returnScenario: String? = null) = replaceScreen(
        TitleScreen(
            game,
            initialSettingOpen = true,
            settingSceneName = if (returnScenario == null) "Login" else "Hall",
            settingReturnScenario = returnScenario,
        )
    )

    /**
     * `startNewGame`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun startNewGame() {
        campaign.newGame(); showScenario("R_00", "scene0")
    }

    /**
     * `loadCampaign`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun loadCampaign() = showScenario(campaign.snapshot.currentScenario)
    /**
     * `loadCampaignSlot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun loadCampaignSlot(index: Int) = campaign.loadSlot(index)
    /**
     * `savedLoadPage`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun savedLoadPage() = campaign.savedPage()
    /**
     * `saveLoadPage`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun saveLoadPage(page: Int) = campaign.savePage(page)
    /**
     * `titleLoadGameLayer`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun titleLoadGameLayer() = LoadGameLayer(object : LoadGameLayer.Repository {
        /**
         * `load`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun load(index: Int) = campaign.loadSlot(index)
        /**
         * `savedPage`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun savedPage() = campaign.savedPage()
        /**
         * `savePage`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun savePage(page: Int) = campaign.savePage(page)
        /**
         * `featureEnabled`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun featureEnabled(name: String) = false
        /**
         * `versionCode`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun versionCode() = 1
        /**
         * `restore`: 입력을 규칙에 따라 계산·변환한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        override fun restore(index: Int, raw: String, route: LoadGameLayer.RestoreRoute) =
            restoreCampaignSlot(index, raw, route)
    })

    /**
     * `restoreCampaignSlot`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun restoreCampaignSlot(index: Int, raw: String, route: LoadGameLayer.RestoreRoute): Boolean {
        if (!campaign.restoreSlot(index, raw)) return false
        when (campaignRestoreDestination(route)) {
            CampaignRestoreDestination.BATTLE -> showBattleSandbox(
                campaign.snapshot.currentScenario.replaceFirst(
                    "R_",
                    "S_"
                ), campaign.snapshot.currentScenario
            )

            CampaignRestoreDestination.HALL -> showCampaignHall(campaign.snapshot.currentScenario)
            CampaignRestoreDestination.HALL_AFTER_BATTLE -> {
                campaign.incStage(); showCampaignHall(campaign.snapshot.currentScenario)
            }
        }
        return true
    }

    /**
     * `showScenario`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showScenario(moduleName: String = initialScenario, entryScene: String = run.startScene) {
        campaign.persist(); campaign.enter(moduleName)
        replaceScreen(
            ScenarioScreen(
                game,
                moduleName,
                run.randomSequence,
                run.infoTransferRandomSequence,
                run.globals,
                run.unitAttributes,
                run.variables,
                run.ambition,
                run.battleRound,
                run.battleCamp,
                run.battleAttributes,
                run.battlePositions,
                run.battlePositionsByCamp,
                run.battleEnemyDefeated,
                entryScene,
                run.startLabel,
                ScenarioRandomTraceConfiguration(
                    stopAfterNextTrace = run.stopAfterRandomTrace,
                    stopAfterTraceCount = run.stopAfterRandomTraceCount,
                ),
                campaign.state,
            )
        )
    }

    /**
     * `showBattleSandbox`: 화면 표시 상태를 렌더링한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showBattleSandbox(
        sourceScenario: String = initialScenario.replaceFirst("R_", "S_"),
        returnScenario: String = configuration.battleReturnScenario ?: initialScenario.replaceFirst("S_", "R_"),
    ) {
        campaign.persist(); replaceScreen(
            BattleScreen(
                game,
                BattleVerificationRuntime(
                    tutorial = configuration.verification.battle,
                    scripted = configuration.verification.scriptedBattle,
                ),
                sourceScenario,
                returnScenario,
                campaign.state,
                BattleTerrainLoader::load,
            )
        )
    }

    /**
     * `showBattlePreparation`: 화면 표시 상태를 렌더링한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun showBattlePreparation(
        returnScenario: String,
        sourceScenario: String,
        limit: ScenarioJoinBattleLimit,
        backgroundId: Int = 71
    ) {
        campaign.persist(); replaceScreen(
            BattlePreparationScreen(
                game,
                returnScenario,
                sourceScenario,
                limit,
                campaign.state,
                backgroundId
            )
        )
    }

    /**
     * `showCampaignHall`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showCampaignHall(returnScenario: String) = showScenario(returnScenario)
    /**
     * `recordChoice`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun recordChoice(scenario: String, choice: String) = campaign.recordChoice(scenario, choice)
    /**
     * `completeBattle`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeBattle(scenario: String, nextScenario: String) = campaign.complete(scenario, nextScenario)
    /**
     * `saveCampaign`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun saveCampaign() = campaign.persist()
    /**
     * `saveCampaign`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun saveCampaign(index: Int) = campaign.saveSlot(index)
    /**
     * `savedCampaignSlot`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun savedCampaignSlot(index: Int) = campaign.loadSlot(index)
    /**
     * `campaignStage`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun campaignStage() = campaign.snapshot.stage
    /**
     * `advanceCampaignStage`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun advanceCampaignStage() = campaign.incStage()
    /**
     * `setCampaignStage`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setCampaignStage(stage: Int) = campaign.setStage(stage)
}

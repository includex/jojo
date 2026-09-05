package com.jojo.game.application.navigation

import com.jojo.game.GameLaunchConfiguration
import com.jojo.game.JojoGame
import com.jojo.game.LoadGameLayer
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit
import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.presentation.title.TitleScreen
import com.jojo.game.presentation.battle.BattleScreen
import com.jojo.game.presentation.battle.preparation.BattlePreparationScreen
import com.jojo.game.infrastructure.data.CampaignStore
import com.jojo.game.infrastructure.data.BattleTerrainLoader

import com.badlogic.gdx.Screen

internal enum class CampaignRestoreDestination { BATTLE, HALL, HALL_AFTER_BATTLE }

internal fun campaignRestoreDestination(route: LoadGameLayer.RestoreRoute): CampaignRestoreDestination = when (route) {
    LoadGameLayer.RestoreRoute.BATTLE -> CampaignRestoreDestination.BATTLE
    LoadGameLayer.RestoreRoute.HALL -> CampaignRestoreDestination.HALL
    LoadGameLayer.RestoreRoute.HALL_AFTER_BATTLE -> CampaignRestoreDestination.HALL_AFTER_BATTLE
}

/** Owns screen replacement and saved-campaign route selection. */
internal class GameScreenNavigator(
    private val game: JojoGame,
    private val configuration: GameLaunchConfiguration,
    private val campaign: CampaignStore,
    private val replaceScreen: (Screen) -> Unit,
) {
    private val run get() = configuration.scenarioRun
    private val initialScenario get() = configuration.initialScenario
    private val captureState get() = configuration.capture.state

    fun showTitleScreen() {
        val state = captureState
        replaceScreen(
            TitleScreen(
            game,
            state == "login-setting",
            state == "login-load" || state?.startsWith("login-load-row") == true,
            state?.removePrefix("login-load-row")?.takeIf { state.startsWith("login-load-row") }?.toIntOrNull()
        )
        )
    }

    fun showTitleLoadScreen() = replaceScreen(TitleScreen(game, initialLoadOpen = true))
    fun showTitleSettingScreen(returnScenario: String? = null) = replaceScreen(
        TitleScreen(
            game,
            initialSettingOpen = true,
            settingSceneName = if (returnScenario == null) "Login" else "Hall",
            settingReturnScenario = returnScenario,
        )
    )

    fun startNewGame() {
        campaign.newGame(); showScenario("R_00", "scene0")
    }

    fun loadCampaign() = showScenario(campaign.snapshot.currentScenario)
    fun loadCampaignSlot(index: Int) = campaign.loadSlot(index)
    fun savedLoadPage() = campaign.savedPage()
    fun saveLoadPage(page: Int) = campaign.savePage(page)
    fun titleLoadGameLayer() = LoadGameLayer(object : LoadGameLayer.Repository {
        override fun load(index: Int) = campaign.loadSlot(index)
        override fun savedPage() = campaign.savedPage()
        override fun savePage(page: Int) = campaign.savePage(page)
        override fun featureEnabled(name: String) = false
        override fun versionCode() = 1
        override fun restore(index: Int, raw: String, route: LoadGameLayer.RestoreRoute) =
            restoreCampaignSlot(index, raw, route)
    })

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

    fun showScenario(moduleName: String = initialScenario, entryScene: String = run.startScene) {
        campaign.persist(); campaign.enter(moduleName)
        replaceScreen(
            ScenarioScreen(
                game,
                moduleName,
                configuration.verification.scenario,
                configuration.verification.firstBranch,
                configuration.verification.alternateBranch,
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
                run.stopAfterRandomTrace,
                run.stopAfterRandomTraceCount,
                campaign.state,
            )
        )
    }

    fun showBattleSandbox(
        sourceScenario: String = initialScenario.replaceFirst("R_", "S_"),
        returnScenario: String = configuration.battleReturnScenario ?: initialScenario.replaceFirst("S_", "R_"),
    ) {
        campaign.persist(); replaceScreen(
            BattleScreen(
                game,
                configuration.verification.battle,
                configuration.verification.scriptedBattle,
                sourceScenario,
                returnScenario,
                campaign.state,
                BattleTerrainLoader::load,
            )
        )
    }

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

    fun showCampaignHall(returnScenario: String) = showScenario(returnScenario)
    fun recordChoice(scenario: String, choice: String) = campaign.recordChoice(scenario, choice)
    fun completeBattle(scenario: String, nextScenario: String) = campaign.complete(scenario, nextScenario)
    fun saveCampaign() = campaign.persist()
    fun saveCampaign(index: Int) = campaign.saveSlot(index)
    fun savedCampaignSlot(index: Int) = campaign.loadSlot(index)
    fun campaignStage() = campaign.snapshot.stage
    fun advanceCampaignStage() = campaign.incStage()
    fun setCampaignStage(stage: Int) = campaign.setStage(stage)
}

package com.jojo.game.presentation.scenario

import com.jojo.game.JojoGame
import com.jojo.game.ScenarioCatalog
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState

/** Owns source scene progression and the one-way transition out of a scenario. */
internal class ScenarioNavigationCoordinator(
    private val game: JojoGame,
    private val moduleName: String,
    private val campaign: CampaignState,
    private val playback: ScenarioInterpreter,
    initialSceneIndex: Int,
) {
    private var routed = false
    private var nextEntryFlowInputAt = 0f

    var naturalSceneIndex = initialSceneIndex
        private set
    var hallBattleScenePending = false
        private set
    private val startedSceneHistory = mutableListOf(initialSceneIndex)

    val routedAfterCompletion get() = routed
    fun startedScenes(): List<Int> = startedSceneHistory.toList()

    fun continueNaturally(externalRuntimeOpen: Boolean) {
        if (!ScenarioRenderPolicy.shouldContinueNaturally(
                externalRuntimeOpen = externalRuntimeOpen,
                playbackState = playback.state,
                naturalSceneIndex = naturalSceneIndex,
                menuVisible = playback.stage.menuVisible,
                battleEndedByScript = playback.stage.battleEndedByScript,
                sceneJumpTarget = playback.stage.sceneJumpTarget,
            )) return
        val next = "scene${naturalSceneIndex + 1}"
        if (next !in playback.functionNames) return

        naturalSceneIndex++
        game.scenarioStarted(moduleName, naturalSceneIndex)
        playback.start(next)
        startedSceneHistory += naturalSceneIndex
    }

    fun driveYingchuanEntryFlow(elapsed: Float, advance: () -> Unit) {
        if (moduleName != "R_00" || game.requestedYingchuanEntryFlowTracePath() == null || elapsed < nextEntryFlowInputAt) return
        nextEntryFlowInputAt = elapsed + .04f
        when (playback.state) {
            PlaybackState.DIALOGUE, PlaybackState.MODAL, PlaybackState.COMPLETE -> advance()
            PlaybackState.CHOICE -> {
                val start = playback.currentChoice?.options?.indexOfFirst { it.contains("게임 시작") } ?: -1
                playback.selectChoice(if (start >= 0) start else 0)
                advance()
            }
            PlaybackState.DELAY -> Unit
        }
    }

    fun beginHallBattleScene(): Boolean {
        if (routed || hallBattleScenePending || playback.state != PlaybackState.COMPLETE ||
            !playback.stage.menuVisible || playback.stage.joinBattleLimit == null
        ) return false
        val nextIndex = naturalSceneIndex + 1
        val nextScene = "scene$nextIndex"
        if (nextScene !in playback.functionNames) return false

        playback.selectHallBattleCommand()
        naturalSceneIndex = nextIndex
        hallBattleScenePending = true
        game.scenarioStarted(moduleName, nextIndex)
        startedSceneHistory += nextIndex
        playback.start(nextScene)
        return true
    }

    fun routeAfterScenario() {
        if (routed) return
        routed = true
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
                game.showBattleSandbox(matchingBattleModule(), moduleName)
            } else {
                game.showBattlePreparation(moduleName, matchingBattleModule(), entry.selectionLimit, playback.stage.backgroundId)
            }
        } ?: run {
            campaign.roster.prepareImplicitSingleUnitBattle()
            game.advanceCampaignStage()
            game.showBattleSandbox(matchingBattleModule(), moduleName)
        }
    }

    private fun matchingBattleModule(): String {
        val candidate = moduleName.replaceFirst("R_", "S_")
        return candidate.takeIf { it in ScenarioCatalog.sModuleNames() } ?: "S_00"
    }
}

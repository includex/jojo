// 시나리오 장면 전환 조정
package com.jojo.game.presentation.scenario

import com.jojo.game.JojoGame
import com.jojo.game.infrastructure.data.ScenarioCatalog
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState

/** ScenarioNavigationCoordinator: 재생 완료·장면 점프·거점 전투 진입을 다음 시나리오 또는 전투 화면 전환으로 확정한다. */
internal class ScenarioNavigationCoordinator(
    /** `game` (JojoGame): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val game: JojoGame,
    /** `moduleName` (String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val moduleName: String,
    /** `campaign` (CampaignState): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val campaign: CampaignState,
    /** `playback` (ScenarioInterpreter): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val playback: ScenarioInterpreter,
    initialSceneIndex: Int,
) {
    /** 종료 경로를 한 번만 실행했는지 나타내는 보호 플래그다. */
    private var routed = false
    /**
     * `nextEntryFlowInputAt` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var nextEntryFlowInputAt = 0f

    /** 스크립트가 명시적 점프를 하지 않을 때 순차 재생할 현재 장면 번호다. */
    var naturalSceneIndex = initialSceneIndex
        private set
    /**
     * `hallBattleScenePending` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var hallBattleScenePending = false
        private set
    /**
     * `startedSceneHistory` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val startedSceneHistory = mutableListOf(initialSceneIndex)

    /**
     * `routedAfterCompletion` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val routedAfterCompletion get() = routed
    /**
     * `startedScenes`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun startedScenes(): List<Int> = startedSceneHistory.toList()

    /** continueNaturally: 외부 검증 경로가 없고 완료 조건이 맞을 때 다음 scene 함수를 시작한다. */
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

    /** driveYingchuanEntryFlow: 영천 진입 trace에서 요구한 대화·선택 자동 입력을 시간 간격대로 공급한다. */
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

    /** beginHallBattleScene: 거점 메뉴의 전투 선택을 다음 scene 시작으로 연결하고 중복 진입을 막는다. */
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

    /** routeAfterScenario: 점프·편성·즉시 전투 중 스크립트 완료 결과에 맞는 다음 화면을 연다. */
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

    /**
     * `matchingBattleModule`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun matchingBattleModule(): String {
        val candidate = moduleName.replaceFirst("R_", "S_")
        return candidate.takeIf { it in ScenarioCatalog.sModuleNames() } ?: "S_00"
    }
}

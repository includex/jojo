// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.*

import com.jojo.game.application.runtime.RuntimeArtifactObserver
import com.jojo.game.application.runtime.RuntimeBattleObserver
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattlePreparationDriver
import com.jojo.game.application.runtime.RuntimeScenarioDriver
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.RuntimeTitleStartupDriver
import com.jojo.game.application.runtime.BattleTraceRuntimeConfig

/** GameEntryPoint: 실행 직후 열 화면을 제목·시나리오·전투 중 하나로 선택하는 시작 경로다. */
enum class GameEntryPoint {
    TITLE,
    SCENARIO,
    BATTLE,
}

/** ScenarioRunConfiguration: 시나리오 재생 전에 주입할 변수·유닛 상태·난수열을 모은 실행 입력이다. */
data class ScenarioRunConfiguration(
    val globals: Map<Int, Int> = emptyMap(),
    val unitAttributes: List<Triple<Int, Int, Int>> = emptyList(),
    val variables: Map<Int, Int> = emptyMap(),
    val ambition: Int? = null,
    val randomSequence: List<Int> = emptyList(),
    val infoTransferRandomSequence: List<Int> = emptyList(),
    val battleRound: Int = 1,
    val battleCamp: Int = 1,
    val battleAttributes: Map<Int, Map<Int, Int>> = emptyMap(),
    val battlePositions: Map<Int, Pair<Int, Int>> = emptyMap(),
    val battlePositionsByCamp: Map<Int, List<Pair<Int, Int>>> = emptyMap(),
    val battleEnemyDefeated: Boolean = false,
    val startScene: String = "scene1",
    val startLabel: String? = null,
    val stopAfterRandomTrace: Boolean = false,
    val stopAfterRandomTraceCount: Int? = null,
)


/** VerificationConfiguration: 자동 검증에서 전투 또는 스크립트 전용 경로를 켜는 플래그 묶음이다. */
data class VerificationConfiguration(
    val battle: Boolean = false,
    val scriptedBattle: Boolean = false,
)


/** RenderCaptureConfiguration: 화면 캡처와 렌더링 진단 파일의 출력 위치·표본 조건을 지정한다. */
data class RenderCaptureConfiguration(
    val screenshotPath: String? = null,
    val rawCapturePath: String? = null,
    val mapDither: String? = null,
    val mapFilter: String? = null,
    val mapSampler: String? = null,
    val mapSampleOffset: Pair<Float, Float>? = null,
    val compositionTracePath: String? = null,
    val renderEventLogPath: String? = null,
    val state: String? = null,
)

/** GameLaunchConfiguration: 게임 시작 경로와 시나리오·검증·캡처 의존성을 한 번에 전달하는 최상위 설정이다. */
data class GameLaunchConfiguration(
    val entryPoint: GameEntryPoint = GameEntryPoint.TITLE,
    val initialScenario: String = "R_00",
    val battleReturnScenario: String? = null,
    val initialScenarioExplicit: Boolean = false,
    val scenarioRun: ScenarioRunConfiguration = ScenarioRunConfiguration(),
    val verification: VerificationConfiguration = VerificationConfiguration(),
    val capture: RenderCaptureConfiguration = RenderCaptureConfiguration(),
    val battleTraceRuntime: BattleTraceRuntimeConfig? = null,
    val yingchuanEntryFlowTracePath: String? = null,
    /** runtimeScreenObserver: 화면 전환과 프레임 상태를 수집하는 선택적 검증 관찰기다. */
    val runtimeScreenObserver: RuntimeScreenObserver? = null,
    /** runtimeArtifactObserver: 캡처·추적 산출물 이벤트를 수신하는 선택적 관찰기다. */
    val runtimeArtifactObserver: RuntimeArtifactObserver? = null,
    /** runtimeBattleObserver: 전투 프레임과 완료 결과를 받는 선택적 관찰기다. */
    val runtimeBattleObserver: RuntimeBattleObserver? = null,
    /** runtimeBattleDriver: 탐침 상태를 바탕으로 자동 전투 명령을 만드는 구동기다. */
    val runtimeBattleDriver: RuntimeBattleDriver? = null,
    /** runtimeBattlePresentation: 자동 전투가 화면에 재현할 경로와 시간 표본을 지정한다. */
    val runtimeBattlePresentation: RuntimeBattlePresentation = RuntimeBattlePresentation(),
    /** runtimeBattlePreparationDriver: 전투 준비 화면의 자동 조작 상태를 공급한다. */
    val runtimeBattlePreparationDriver: RuntimeBattlePreparationDriver? = null,
    /** runtimeScenarioDriver: 시나리오 재생 중 입력과 표시 명령을 자동으로 생산한다. */
    val runtimeScenarioDriver: RuntimeScenarioDriver? = null,
    /** runtimeTitleStartupDriver: 제목 화면 시작 흐름의 자동 조작 상태를 공급한다. */
    val runtimeTitleStartupDriver: RuntimeTitleStartupDriver? = null,
    val automatedRun: Boolean = false,
)

package com.jojo.game

import com.jojo.game.application.runtime.RuntimeArtifactObserver
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeBattlePreparationDriver
import com.jojo.game.application.runtime.RuntimeScenarioDriver
import com.jojo.game.application.runtime.RuntimeScreenObserver

/** The first application route selected before LibGDX creates a screen. */
enum class GameEntryPoint {
    TITLE,
    SCENARIO,
    BATTLE,
}

/** Explicit scenario inputs used by deterministic diagnostics and scripted runs. */
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

/**
 * data class  `VerificationConfiguration`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class VerificationConfiguration(
    val battle: Boolean = false,
    val scriptedBattle: Boolean = false,
)

/**
 * data class  `RenderCaptureConfiguration`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class RenderCaptureConfiguration(
    val screenshotPath: String? = null,
    val rawCapturePath: String? = null,
    val mapTextureDumpPath: String? = null,
    val mapDither: String? = null,
    val mapFilter: String? = null,
    val mapSampler: String? = null,
    val mapSampleOffset: Pair<Float, Float>? = null,
    val compositionTracePath: String? = null,
    val renderEventLogPath: String? = null,
    val state: String? = null,
)

/** Immutable platform-to-game composition contract. */
data class GameLaunchConfiguration(
    val entryPoint: GameEntryPoint = GameEntryPoint.TITLE,
    val initialScenario: String = "R_00",
    val battleReturnScenario: String? = null,
    val initialScenarioExplicit: Boolean = false,
    val scenarioRun: ScenarioRunConfiguration = ScenarioRunConfiguration(),
    val verification: VerificationConfiguration = VerificationConfiguration(),
    val capture: RenderCaptureConfiguration = RenderCaptureConfiguration(),
    val fullBattleTrace: FullBattleTraceConfig? = null,
    val yingchuanEntryFlowTracePath: String? = null,
    /** Optional external read-only observer; production never depends on its implementation. */
    val runtimeScreenObserver: RuntimeScreenObserver? = null,
    /** Optional neutral sink for immutable renderer artifact requests. */
    val runtimeArtifactObserver: RuntimeArtifactObserver? = null,
    /** Optional neutral battle input driver supplied by an external runtime. */
    val runtimeBattleDriver: RuntimeBattleDriver? = null,
    /** Optional neutral preparation presentation request supplied by an external runtime. */
    val runtimeBattlePreparationDriver: RuntimeBattlePreparationDriver? = null,
    /** Optional neutral scenario input driver supplied by an external runtime. */
    val runtimeScenarioDriver: RuntimeScenarioDriver? = null,
    val automatedRun: Boolean = false,
)

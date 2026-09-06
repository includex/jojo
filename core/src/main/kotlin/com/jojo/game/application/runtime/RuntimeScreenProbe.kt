// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.application.scenario.ScenarioChoiceTrace
import com.jojo.game.application.scenario.ScenarioRandomTrace
import com.jojo.game.presentation.title.TitleViewState
import com.jojo.game.presentation.battle.preparation.BattlePreparationViewState

/** RuntimeScreenObserver: 화면 전환과 프레임 상태를 감시하여 자동 검증 흐름에 개입하는 관찰기 계약이다. */
fun interface RuntimeScreenObserver {
    fun update(delta: Float, screen: RuntimeScreenProbe)

    fun scenarioStarted(module: String, index: Int) = Unit

    /** keepsScenarioOpen: 관찰 작업이 끝날 때까지 시나리오 화면을 닫지 않아야 하는지 나타낸다. */
    val keepsScenarioOpen: Boolean get() = false
}

/** RuntimeScreenProbe: 현재 화면 종류별로 자동 구동에 필요한 읽기 전용 상태를 노출하는 공통 탐침이다. */
sealed interface RuntimeScreenProbe {
    val screenName: String
}

/** TitleRuntimeProbe: 제목 화면의 표시 상태를 자동 시작 구동기에 전달하는 탐침 값이다. */
data class TitleRuntimeProbe(
    val view: TitleViewState,
) : RuntimeScreenProbe {
    override val screenName: String = "TitleScreen"
}

/** ScenarioRuntimeProbe: 시나리오 진행·선택·배경·캠페인 상태를 자동 구동기에 전달하는 탐침 값이다. */
data class ScenarioRuntimeProbe(
    val module: String,
    val elapsedSeconds: Float = 0f,
    val playback: PlaybackState,
    val options: List<String>,
    val selectedChoice: Int,
    val sceneIndex: Int,
    val startedScenes: List<Int>,
    val backgroundId: Int,
    val unitIds: Set<Int>,
    val campaignStage: Int,
    val menuVisible: Boolean,
    val dialogueText: String?,
    val hallBattleScenePending: Boolean,
    val battleButtonScreenX: Int,
    val battleButtonScreenY: Int,
    /** choiceTrace: 현재 재생에서 확정한 선택지의 순서를 검증용으로 보관한다. */
    val choiceTrace: List<ScenarioChoiceTrace> = emptyList(),
    /** randomTrace: 스크립트가 소비한 난수 결과를 검증용으로 보관한다. */
    val randomTrace: List<ScenarioRandomTrace> = emptyList(),
    val randomDrawCount: Int = 0,
    val remainingInjectedRandomCount: Int = 0,
) : RuntimeScreenProbe {
    override val screenName: String = "ScenarioScreen"
}

/** BattlePreparationRuntimeProbe: 전투 준비 화면의 편성 제한·선택·표시 상태를 전달하는 탐침 값이다. */
data class BattlePreparationRuntimeProbe(
    val returnScenario: String,
    val sourceScenario: String,
    val campaignStage: Int,
    val selectedCount: Int,
    val minimum: Int,
    val maximum: Int,
    val cursorSelected: Boolean,
    val canStart: Boolean,
    val view: BattlePreparationViewState,
) : RuntimeScreenProbe {
    override val screenName: String = "BattlePreparationScreen"
}

/** BattleRuntimeScreenProbe: 전투 화면의 진행 단계·모달·입력 좌표·전장 탐침을 묶은 자동 구동 입력이다. */
data class BattleRuntimeScreenProbe(
    val scenario: String,
    val playback: PlaybackState,
    val outcome: BattleOutcome?,
    val bootstrapComplete: Boolean,
    val initialScene1Started: Boolean,
    val resultScene1Started: Boolean,
    val scene2Started: Boolean,
    val rewardOpen: Boolean,
    val winConditionsOpen: Boolean,
    val savePromptOpen: Boolean,
    val losePromptOpen: Boolean,
    val loseTitleScreenX: Int,
    val loseTitleScreenY: Int,
    val playerMoveCommitted: Boolean,
    val campaignStage: Int,
    val turnPhase: String,
    val battleMenuOpen: Boolean,
    val battleCommandOpen: Boolean,
    val battleTargetSelectionOpen: Boolean,
    val magickListOpen: Boolean,
    val magicTargetSelection: Boolean,
    val commandWaitScreenX: Int,
    val commandWaitScreenY: Int,
    val menuEndRoundScreenX: Int,
    val menuEndRoundScreenY: Int,
    val battleMenuButtonScreenX: Int,
    val battleMenuButtonScreenY: Int,
    val autoBattleToggleScreenX: Int,
    val autoBattleToggleScreenY: Int,
    val autoBattleConfirmScreenX: Int,
    val autoBattleConfirmScreenY: Int,
    val autoBattleOverlay: String,
    val autoBattleChecked: Boolean,
    val collocation: Boolean,
    val committedPlayerMove: String?,
    val selectedChoice: Int,
    val selectedUnitId: String?,
    val battle: BattleRuntimeProbe,
) : RuntimeScreenProbe {
    override val screenName: String = "BattleScreen"
    val round: Int get() = battle.snapshot.round
    val activeFaction: Faction get() = battle.snapshot.activeFaction
}

/** OtherRuntimeProbe: 전용 탐침이 없는 화면의 이름만 전달하는 기본 탐침이다. */
data class OtherRuntimeProbe(override val screenName: String) : RuntimeScreenProbe

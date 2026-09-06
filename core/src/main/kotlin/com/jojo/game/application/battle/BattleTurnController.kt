// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.application.battle.Battle
import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.RoundAdvance
import com.jojo.game.domain.battle.TurnResult
import com.jojo.game.domain.battle.WeatherTransition
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.turn.BattleCampCard
import com.jojo.game.domain.battle.turn.BattleDeathCheckpoint
import com.jojo.game.domain.battle.turn.BattleCampTransitionRequest
import com.jojo.game.domain.battle.turn.BattleTurnEntryRequest
import com.jojo.game.domain.battle.turn.BattleTurnPhase
import com.jojo.game.domain.battle.turn.BattleTurnPolicy
import com.jojo.game.domain.battle.turn.BattleTurnSnapshot

/** BattleTurnController: 전투 턴 수명주기 조정기로, 진영 전환·표현 콜백·라운드 처리를 정해진 순서로 진행한다. */
class BattleTurnController(
    /**
     * `battle` (Battle,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val battle: Battle,
    /**
     * `showCamp` ((BattleCampCard) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val showCamp: (BattleCampCard) -> Unit,
    /**
     * `runCampScript` ((Faction) -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val runCampScript: (Faction) -> Boolean,
    /**
     * `runAi` ((Faction) -> AiTurnResult,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val runAi: (Faction) -> AiTurnResult,
    /**
     * `hasPendingAiPresentation` (() -> Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val hasPendingAiPresentation: () -> Boolean = { false },
    /**
     * `presentCampState` ((CampSettlement) -> Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val presentCampState: (CampSettlement) -> Boolean = { true },
    /**
     * `presentDeaths` ((BattleDeathCheckpoint) -> Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val presentDeaths: (BattleDeathCheckpoint) -> Boolean = { true },
    /**
     * `presentCampRestore` ((CampSettlement) -> Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val presentCampRestore: (CampSettlement) -> Boolean = { true },
    /**
     * `runRoundScript` ((RoundAdvance) -> Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val runRoundScript: (RoundAdvance) -> Boolean = { true },
    /** deferSynchronousRoundScriptCompletion: 동기 라운드 스크립트 완료를 다음 프레임으로 미룰지 나타내는 제어 값이다. */
    private val deferSynchronousRoundScriptCompletion: Boolean = false,
    /**
     * `presentWeather` ((WeatherTransition) -> Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val presentWeather: (WeatherTransition) -> Boolean = { true },
    /**
     * `onCampEvents` ((TurnResult) -> Unit): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onCampEvents: (TurnResult) -> Unit = {},
    initialPhase: BattleTurnPhase = BattleTurnPhase.PLAYER_INPUT,
) {
    /**
     * `state` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val state = BattleTurnRuntimeState(initialPhase)

    /** snapshot: 표현 계층과 테스트가 읽는 현재 턴 상태의 불변 투영이다. */
    val snapshot: BattleTurnSnapshot
        get() = state.snapshot()

    /** completeBootstrap: 초기 전투 준비를 끝내고 플레이어 입력 단계로 전환한다. */

    fun completeBootstrap() {
        check(state.phase == BattleTurnPhase.BOOTSTRAP) { "bootstrap completion outside bootstrap phase" }
        battle.roundLifecycle.prepareActiveCampOperation()
        state.phase = BattleTurnPhase.PLAYER_INPUT
    }

    /** endPlayerTurn: 플레이어 턴 종료 요청을 검증하고 진영 복원 단계로 진행한다. */
    fun endPlayerTurn(): Boolean {
        if (!BattleTurnPolicy.acceptsPlayerEnd(
        BattleTurnEntryRequest(state.phase, battle.activeFaction, battle.outcome()),
    )
) return false
        beginCampRestore()
        return true
    }

    /** runCollocatedPlayerTurn: 공동 배치된 플레이어 진영의 AI 처리를 실행하고 후속 복원을 예약한다. */
    fun runCollocatedPlayerTurn(): Boolean {
        if (!BattleTurnPolicy.acceptsPlayerEnd(
        BattleTurnEntryRequest(state.phase, battle.activeFaction, battle.outcome()),
    )
) return false
        state.phase = BattleTurnPhase.AI
        state.lastAiResult = runAi(Faction.PLAYER)
        if (!hasPendingAiPresentation()) beginCampRestore()
        return true
    }

    /** completeCampCard: 진영 안내 카드 표시를 끝내고 진영 시작 상태를 적용한다. */
    fun completeCampCard() {
        check(state.phase == BattleTurnPhase.CAMP_CARD) { "RoundLayer callback outside camp-card phase" }
        beginCampState()
    }


    /**
     * `completeCampStatePresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeCampStatePresentation() {
        check(state.phase == BattleTurnPhase.CAMP_STATE) { "state completion outside camp-state phase" }
        val fired = battle.roundLifecycle.runActiveCampEvents()
        state.lastTurn = state.lastTurn?.copy(firedEvents = fired)
        state.lastTurn?.let(onCampEvents)
        beginCampScript()
    }

    /** completeCampScript: 진영 시작 스크립트 완료 뒤 사망 처리 단계로 전환한다. */
    fun completeCampScript() {
        check(state.phase == BattleTurnPhase.CAMP_SCRIPT) { "camp script completion outside script phase" }
        state.phase = BattleTurnPhase.CAMP_DEATHS
        if (presentDeaths(BattleDeathCheckpoint.CAMP_START)) completeCampDeathPresentation()
    }


    /**
     * `completeCampDeathPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeCampDeathPresentation() {
        check(state.phase == BattleTurnPhase.CAMP_DEATHS) { "death completion outside camp-death phase" }
        if (finishIfBattleEnded()) return
        battle.roundLifecycle.prepareActiveCampOperation()
        val camp = battle.activeFaction
        if (camp == Faction.PLAYER) {
            state.phase = BattleTurnPhase.PLAYER_INPUT
            return
        }
        state.phase = BattleTurnPhase.AI
        state.lastAiResult = runAi(camp)
        if (!hasPendingAiPresentation()) beginCampRestore()
    }

    /** completeAiPresentation: AI 동작 표현 완료를 반영하고 진영 종료 복원으로 진행한다. */
    fun completeAiPresentation(result: AiTurnResult? = null) {
        check(state.phase == BattleTurnPhase.AI) { "AI presentation completion outside AI phase" }
        if (result != null) state.lastAiResult = result
        beginCampRestore()
    }

    /** finishScriptEndedBattle: 스크립트가 확정한 전투 종료 결과를 턴 상태에 반영한다. */

    fun finishScriptEndedBattle() {
        check(state.phase == BattleTurnPhase.AI) { "script-end completion outside AI phase" }
        check(battle.outcome() != null) { "script ended without a battle outcome" }
        state.phase = BattleTurnPhase.FINISHED
    }


    /**
     * `completeCampRestorePresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeCampRestorePresentation() {
        check(state.phase == BattleTurnPhase.CAMP_RESTORE) { "restore completion outside camp-restore state.phase" }
        state.phase = BattleTurnPhase.CAMP_RESTORE_DEATHS
        if (presentDeaths(BattleDeathCheckpoint.CAMP_RESTORE)) completeCampRestoreDeathPresentation()
    }


    /**
     * `completeCampRestoreDeathPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeCampRestoreDeathPresentation() {
        check(state.phase == BattleTurnPhase.CAMP_RESTORE_DEATHS) { "death completion outside restore-death phase" }
        if (finishIfBattleEnded()) return
        if (battle.activeFaction == Faction.REINFORCEMENTS) beginRoundBoundary() else enterNextCamp()
    }


    /**
     * `completeRoundScript`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeRoundScript() {
        check(state.phase == BattleTurnPhase.ROUND_SCRIPT) { "round script completion outside round-script phase" }
        state.phase = BattleTurnPhase.ROUND_DEATHS
        if (presentDeaths(BattleDeathCheckpoint.ROUND_START)) completeRoundDeathPresentation()
    }


    /**
     * `completeRoundDeathPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeRoundDeathPresentation() {
        check(state.phase == BattleTurnPhase.ROUND_DEATHS) { "death completion outside round-death phase" }
        if (finishIfBattleEnded()) return
        battle.roundLifecycle.resetCompletedRoundSkillTemps(requireNotNull(state.lastRoundAdvance).completedRound)
        val transition = battle.roundLifecycle.applyScheduledWeather()
        state.lastWeatherTransition = transition
        state.phase = BattleTurnPhase.WEATHER
        if (presentWeather(transition)) completeWeatherPresentation()
    }


    /**
     * `completeWeatherPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeWeatherPresentation() {
        check(state.phase == BattleTurnPhase.WEATHER) { "weather completion outside weather phase" }
        enterNextCamp()
    }

    /**
     * `beginCampRestore`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun beginCampRestore() {
        // 원본은 종료 조건이 복원·사망 처리를 건너뛰지 않게 한다. 치명적인
        // 독·반동·상태 틱은 종료 상태 전에도 화면에 노출되어야 한다.
        val settlement = battle.roundLifecycle.settleActiveCampEnd()
        state.lastCampSettlement = settlement
        state.phase = BattleTurnPhase.CAMP_RESTORE
        if (presentCampRestore(settlement)) completeCampRestorePresentation()
    }

    /**
     * `beginRoundBoundary`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun beginRoundBoundary() {
        val advance = battle.roundLifecycle.advanceRound()
        state.lastRoundAdvance = advance
        state.phase = BattleTurnPhase.ROUND_SCRIPT
        val completedSynchronously = runRoundScript(advance)
        if (completedSynchronously && !deferSynchronousRoundScriptCompletion) completeRoundScript()
    }

    /**
     * `enterNextCamp`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun enterNextCamp() {
        if (finishIfBattleEnded()) return
        val previous = battle.activeFaction
        state.lastTurn = battle.roundLifecycle.advanceToNextCamp()
        val current = battle.activeFaction
        // 원본 조작 설정은 아군 계열과 적군 사이를 넘을 때만 진영 안내를 만든다.
        // 플레이어에서 우군으로의 전환은 안내 카드 없이 이어진다.
        if (BattleTurnPolicy.campCardFor(BattleCampTransitionRequest(previous, current))) {
            state.phase = BattleTurnPhase.CAMP_CARD
            showCamp(BattleCampCard(requireNotNull(state.lastTurn), showsRoundNumber = current != Faction.ENEMY))
        } else {
            beginCampState()
        }
    }

    /**
     * `beginCampState`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun beginCampState() {
        val settlement = battle.roundLifecycle.settleActiveCampStart()
        state.lastCampSettlement = settlement
        state.phase = BattleTurnPhase.CAMP_STATE
        if (presentCampState(settlement)) completeCampStatePresentation()
    }

    /**
     * `beginCampScript`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun beginCampScript() {
        state.phase = BattleTurnPhase.CAMP_SCRIPT
        if (runCampScript(battle.activeFaction)) completeCampScript()
    }

    /**
     * `finishIfBattleEnded`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun finishIfBattleEnded(): Boolean {
        if (battle.outcome() == null) return false
        state.phase = BattleTurnPhase.FINISHED
        return true
    }
}

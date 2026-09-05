package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*

/**
 * Source `_ai2` enters every authored camp, including an empty
 * REINFORCEMENTS camp, before its completion callback advances the round.
 * The LibGDX update loop can otherwise enter and finish an empty camp in the
 * same render call, making the curCamp=3 edge unobservable.
 */
internal class EmptyAiCampFrameBarrier {
    private var pending = false

    /**
     * 공개 메서드 `begin`
     *
     * ### 파라미터
    - `hasActor` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun begin(hasActor: Boolean) {
        pending = !hasActor
    }

    /**
     * 공개 메서드 `yieldEntryFrame`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldEntryFrame(): Boolean {
        if (!pending) return false
        pending = false
        return true
    }
}

/**
 * A collocated Mine actor's final move callback is observable after its tile
 * mutation has committed and before `_ai2` resumes into post-action scripts.
 * Enemy synchronous continuations do not cross this render boundary.
 */
internal class CommittedPlayerMoveFrameBarrier {
    private var exposed = false

    /**
     * 공개 메서드 `beginActor`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun beginActor() {
        exposed = false
    }

    /**
     * 공개 메서드 `yieldCompletionFrame`
     *
     * ### 파라미터
    - `isPlayer` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `moved` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldCompletionFrame(isPlayer: Boolean, moved: Boolean): Boolean {
        if (exposed || !isPlayer || !moved) return false
        exposed = true
        return true
    }
}

/**
 * `ctrl_mine`'s collocated PLAYER `_ai2` runs scene1 from move2's final
 * callback.  Like the source generator, stop only when that callback has
 * both ended the stage and authored a battle result; an `end()` by itself is
 * not a tactical result and must retain the ordinary action continuation.
 */
internal object CollocatedPlayerMoveScriptEnd {
    fun finishesAiTurn(
        camp: Faction,
        moveCallbackStarted: Boolean,
        scriptState: PlaybackState,
        battleEndedByScript: Boolean,
        scriptedOutcome: BattleOutcome?,
        observedOutcome: BattleOutcome?,
    ): Boolean =
        camp == Faction.PLAYER &&
                moveCallbackStarted &&
                scriptState == PlaybackState.COMPLETE &&
                battleEndedByScript &&
                scriptedOutcome != null &&
                observedOutcome != null
}

/**
 * `_ai2` returns from `_attack2`/`_magic` after the authored action clip has
 * finished, then resumes through `_shifudu -> _jiesuan(g_charinfo)`.  The
 * source exposes the actor's XD/hasActed transition on the action episode's
 * terminal row, then yields before choosing the next actor. Target hit and
 * `_magicProcess` local settlements are independent callbacks.
 */
internal class ActionStatusFrameBarrier {
    private var settlementExposed = false

    /**
     * 공개 메서드 `beginActor`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun beginActor() {
        settlementExposed = false
    }

    /**
     * After `_jiesuan(g_charinfo)` has synchronously published XD, source
     * returns to the `_ai2` generator scheduler before selecting the next
     * actor.  Keep that settled actor observable in the current episode;
     * otherwise the next actor's decision and the previous actor's XD edge
     * are sampled in one game frame and the state edge is attributed to the
     * wrong episode.
     */
    /**
     * 공개 메서드 `yieldAfterCommit`
     *
     * ### 파라미터
    - `hasAction` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldAfterCommit(hasAction: Boolean): Boolean {
        if (settlementExposed || !hasAction) return false
        settlementExposed = true
        return true
    }
}

/**
 * A physical `_attack6` counter finishes its own action callback before the
 * original attacker's `_shifudu -> _jiesuan(g_charinfo)` continuation runs.
 * Cocos exposes the resulting idle row between those callbacks.  The game's
 * render loop otherwise removes the final counter clip and commits XD in the
 * same update, attributing the actor's settlement to the counter episode.
 *
 * This gate is deliberately armed only for an exchange which actually
 * contains a physical COUNTER/COUNTER_FOLLOW_UP pass. Ordinary attacks,
 * misses without a counter, magic and item actions retain their existing
 * completion cadence.
 */
internal class CounterattackSettlementFrameBarrier {
    private var idleFramePending = false

    /**
     * 공개 메서드 `beginActor`
     *
     * ### 파라미터
    - `hasPhysicalCounter` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun beginActor(hasPhysicalCounter: Boolean) {
        idleFramePending = hasPhysicalCounter
    }

    /**
     * 공개 메서드 `yieldIdleBeforeCommit`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldIdleBeforeCommit(): Boolean {
        if (!idleFramePending) return false
        idleFramePending = false
        return true
    }
}

/** The callback plan, rather than aggregate damage, proves `_attack6` ran. */
internal fun TacticalActionResult?.hasPhysicalCounterPass(): Boolean =
    (this as? TacticalActionResult.Attack)?.physicalPasses?.any { pass ->
        pass.kind == PhysicalAttackPassKind.COUNTER ||
                pass.kind == PhysicalAttackPassKind.COUNTER_FOLLOW_UP
    } == true

/**
 * A camp script's final `BattleUnit.move2` callback resumes the generator,
 * but Cocos does not enter `startOper/_firstUnit/centerUnit` in that movement
 * episode. Preserve one completed-move row before the camp-first focus.
 *
 * Only the exact DELAY -> COMPLETE edge which also consumes a live scripted
 * movement arms the gate. A camp script without movement, or one whose move
 * is followed by another authored wait/modal, is not delayed.
 */
internal class ScriptedMovementCampTransitionFrameBarrier {
    private var completedMoveFramePending = false

    fun observe(
        inCampScript: Boolean,
        scriptWasPending: Boolean,
        scriptCompleted: Boolean,
        movementWasActive: Boolean,
        movementIsActive: Boolean,
    ) {
        if (inCampScript && scriptWasPending && scriptCompleted && movementWasActive && !movementIsActive) {
            completedMoveFramePending = true
        }
    }

    /**
     * 공개 메서드 `yieldBeforeCampTransition`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun yieldBeforeCampTransition(): Boolean {
        if (!completedMoveFramePending) return false
        completedMoveFramePending = false
        return true
    }
}

/**
 * `_ai2` may tail-call the next actor in the same engine update, but two
 * consecutive no-result settlements are not both collapsed into that tail.
 * The first publishes XD; the following actor is resumed on the next update.
 */
internal class ConsecutiveNoResultFrameGate {
    private var completedInCurrentRender = false

    /**
     * 공개 메서드 `beginRender`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun beginRender() {
        completedInCurrentRender = false
    }

    /**
     * 공개 메서드 `markCompleted`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun markCompleted() {
        completedInCurrentRender = true
    }

    /**
     * 공개 메서드 `shouldYieldBefore`
     *
     * ### 파라미터
    - `nextIsNoResult` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun shouldYieldBefore(nextIsNoResult: Boolean): Boolean =
        completedInCurrentRender && nextIsNoResult
}

/**
 * Callback-driven implementation of BattleScreen.ctrl_mine's lifecycle.
 *
 * Source order is intentionally represented by separate phases:
 *
 * camp switch/card -> _stateProcess -> pre-death script -> unitDeath ->
 * operation -> restore -> unitDeath -> (after REINFORCEMENTS) addRound -> round script
 * -> unitDeath -> weather -> next camp.
 *
 * A callback returning true means its presentation completed synchronously.
 * Returning false leaves the controller at that phase until the matching
 * complete* method is called. This is the same generator hand-off used by
 * the original Cocos layer and prevents future state becoming visible early.
 */
/**
 * class  `BattleTurnController`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleTurnController(
    private val battle: Battle,
    private val showCamp: (CampCard) -> Unit,
    private val runCampScript: (Faction) -> Boolean,
    private val runAi: (Faction) -> AiTurnResult,
    private val hasPendingAiPresentation: () -> Boolean = { false },
    private val presentCampState: (CampSettlement) -> Boolean = { true },
    private val presentDeaths: (DeathCheckpoint) -> Boolean = { true },
    private val presentCampRestore: (CampSettlement) -> Boolean = { true },
    private val runRoundScript: (RoundAdvance) -> Boolean = { true },
    /** Preserve addRound's curCamp=REINFORCEMENTS observation before callback completion. */
    private val deferSynchronousRoundScriptCompletion: Boolean = false,
    private val presentWeather: (WeatherTransition) -> Boolean = { true },
    private val onCampEvents: (TurnResult) -> Unit = {},
    initialPhase: Phase = Phase.PLAYER_INPUT,
) {
    /**
     * enum class  `Phase`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Phase {
        /** scene0 and the first scene1/startOper callback still own control. */
        BOOTSTRAP,
        PLAYER_INPUT,
        CAMP_CARD,
        CAMP_STATE,
        CAMP_SCRIPT,
        CAMP_DEATHS,
        AI,
        CAMP_RESTORE,
        CAMP_RESTORE_DEATHS,
        ROUND_SCRIPT,
        ROUND_DEATHS,
        WEATHER,
        FINISHED,
    }

    /**
     * enum class  `DeathCheckpoint`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class DeathCheckpoint { CAMP_START, CAMP_RESTORE, ROUND_START }

    /** `_setOper` selects showRoundLayer for PLAYER/FRIEND and showEnemyRound for ENEMY. */
    data class CampCard(val turn: TurnResult, val showsRoundNumber: Boolean)

    var phase: Phase = initialPhase
        private set
    var lastTurn: TurnResult? = null
        private set
    var lastAiResult: AiTurnResult? = null
        private set
    var lastCampSettlement: CampSettlement? = null
        private set
    var lastRoundAdvance: RoundAdvance? = null
        private set
    var lastWeatherTransition: WeatherTransition? = null
        private set

    /**
     * Initial `_execControlScript(true)` enters Mine operation without the
     * ordinary `_setOper/_stateProcess/unitDeath` camp-start chain.
     */
    /**
     * 공개 메서드 `completeBootstrap`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeBootstrap() {
        check(phase == Phase.BOOTSTRAP) { "bootstrap completion outside bootstrap phase" }
        battle.roundLifecycle.prepareActiveCampOperation()
        phase = Phase.PLAYER_INPUT
    }

    /** Player's END_ROUND command. This is the entry corresponding to ctrl_mine. */
    fun endPlayerTurn(): Boolean {
        if (phase != Phase.PLAYER_INPUT || battle.activeFaction != Faction.PLAYER || battle.outcome() != null) return false
        beginCampRestore()
        return true
    }

    /** Source COLLOCATION path: Mine is dispatched through the same _ai2 controller as AI camps. */
    fun runCollocatedPlayerTurn(): Boolean {
        if (phase != Phase.PLAYER_INPUT || battle.activeFaction != Faction.PLAYER || battle.outcome() != null) return false
        phase = Phase.AI
        lastAiResult = runAi(Faction.PLAYER)
        if (!hasPendingAiPresentation()) beginCampRestore()
        return true
    }

    /** RoundLayer's `fn` callback after exactly two seconds. */
    fun completeCampCard() {
        check(phase == Phase.CAMP_CARD) { "RoundLayer callback outside camp-card phase" }
        beginCampState()
    }

    /**
     * 공개 메서드 `completeCampStatePresentation`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeCampStatePresentation() {
        check(phase == Phase.CAMP_STATE) { "state completion outside camp-state phase" }
        val fired = battle.roundLifecycle.runActiveCampEvents()
        lastTurn = lastTurn?.copy(firedEvents = fired)
        lastTurn?.let(onCampEvents)
        beginCampScript()
    }

    /** Called only when the source scene script reports COMPLETE. */
    fun completeCampScript() {
        check(phase == Phase.CAMP_SCRIPT) { "camp script completion outside script phase" }
        phase = Phase.CAMP_DEATHS
        if (presentDeaths(DeathCheckpoint.CAMP_START)) completeCampDeathPresentation()
    }

    /**
     * 공개 메서드 `completeCampDeathPresentation`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeCampDeathPresentation() {
        check(phase == Phase.CAMP_DEATHS) { "death completion outside camp-death phase" }
        if (finishIfBattleEnded()) return
        battle.roundLifecycle.prepareActiveCampOperation()
        val camp = battle.activeFaction
        if (camp == Faction.PLAYER) {
            phase = Phase.PLAYER_INPUT
            return
        }
        phase = Phase.AI
        lastAiResult = runAi(camp)
        if (!hasPendingAiPresentation()) beginCampRestore()
    }

    /** Final callback after BattleScreen has shown every `_ai2` actor pass. */
    fun completeAiPresentation(result: AiTurnResult? = null) {
        check(phase == Phase.AI) { "AI presentation completion outside AI phase" }
        if (result != null) lastAiResult = result
        beginCampRestore()
    }

    /**
     * Finish an AI callback whose first post-action scenario script called
     * `stage.end()`. The source does not run unit-hide or camp-restore after
     * that explicit boundary.
     */
    /**
     * 공개 메서드 `finishScriptEndedBattle`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun finishScriptEndedBattle() {
        check(phase == Phase.AI) { "script-end completion outside AI phase" }
        check(battle.outcome() != null) { "script ended without a battle outcome" }
        phase = Phase.FINISHED
    }

    /**
     * 공개 메서드 `completeCampRestorePresentation`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeCampRestorePresentation() {
        check(phase == Phase.CAMP_RESTORE) { "restore completion outside camp-restore phase" }
        phase = Phase.CAMP_RESTORE_DEATHS
        if (presentDeaths(DeathCheckpoint.CAMP_RESTORE)) completeCampRestoreDeathPresentation()
    }

    /**
     * 공개 메서드 `completeCampRestoreDeathPresentation`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeCampRestoreDeathPresentation() {
        check(phase == Phase.CAMP_RESTORE_DEATHS) { "death completion outside restore-death phase" }
        if (finishIfBattleEnded()) return
        if (battle.activeFaction == Faction.REINFORCEMENTS) beginRoundBoundary() else enterNextCamp()
    }

    /**
     * 공개 메서드 `completeRoundScript`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeRoundScript() {
        check(phase == Phase.ROUND_SCRIPT) { "round script completion outside round-script phase" }
        phase = Phase.ROUND_DEATHS
        if (presentDeaths(DeathCheckpoint.ROUND_START)) completeRoundDeathPresentation()
    }

    /**
     * 공개 메서드 `completeRoundDeathPresentation`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeRoundDeathPresentation() {
        check(phase == Phase.ROUND_DEATHS) { "death completion outside round-death phase" }
        if (finishIfBattleEnded()) return
        battle.roundLifecycle.resetCompletedRoundSkillTemps(requireNotNull(lastRoundAdvance).completedRound)
        val transition = battle.roundLifecycle.applyScheduledWeather()
        lastWeatherTransition = transition
        phase = Phase.WEATHER
        if (presentWeather(transition)) completeWeatherPresentation()
    }

    /**
     * 공개 메서드 `completeWeatherPresentation`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeWeatherPresentation() {
        check(phase == Phase.WEATHER) { "weather completion outside weather phase" }
        enterNextCamp()
    }

    private fun beginCampRestore() {
        // The source never lets isEnd bypass restore/unitDeath.  A lethal
        // poison/recoil/state tick must remain visible before FINISHED.
        val settlement = battle.roundLifecycle.settleActiveCampEnd()
        lastCampSettlement = settlement
        phase = Phase.CAMP_RESTORE
        if (presentCampRestore(settlement)) completeCampRestorePresentation()
    }

    private fun beginRoundBoundary() {
        val advance = battle.roundLifecycle.advanceRound()
        lastRoundAdvance = advance
        phase = Phase.ROUND_SCRIPT
        val completedSynchronously = runRoundScript(advance)
        if (completedSynchronously && !deferSynchronousRoundScriptCompletion) completeRoundScript()
    }

    private fun enterNextCamp() {
        if (finishIfBattleEnded()) return
        val previous = battle.activeFaction
        lastTurn = battle.roundLifecycle.advanceToNextCamp()
        val current = battle.activeFaction
        // BattleScreen._setOper only creates RoundLayer when crossing between
        // MINE/FRIEND and ENEMY. MINE -> FRIEND continues without a card.
        if (previous.isPlayerSide() != current.isPlayerSide()) {
            phase = Phase.CAMP_CARD
            showCamp(CampCard(requireNotNull(lastTurn), showsRoundNumber = current != Faction.ENEMY))
        } else {
            beginCampState()
        }
    }

    private fun beginCampState() {
        val settlement = battle.roundLifecycle.settleActiveCampStart()
        lastCampSettlement = settlement
        phase = Phase.CAMP_STATE
        if (presentCampState(settlement)) completeCampStatePresentation()
    }

    private fun beginCampScript() {
        phase = Phase.CAMP_SCRIPT
        if (runCampScript(battle.activeFaction)) completeCampScript()
    }

    private fun finishIfBattleEnded(): Boolean {
        if (battle.outcome() == null) return false
        phase = Phase.FINISHED
        return true
    }
}

package com.jojo.game.application.battle

import com.jojo.game.AiTurnResult
import com.jojo.game.Battle
import com.jojo.game.BattleOutcome
import com.jojo.game.RoundAdvance
import com.jojo.game.TurnResult
import com.jojo.game.WeatherTransition
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.turn.BattleCampCard
import com.jojo.game.domain.battle.turn.BattleDeathCheckpoint
import com.jojo.game.domain.battle.turn.BattleCampTransitionRequest
import com.jojo.game.domain.battle.turn.BattleTurnEntryRequest
import com.jojo.game.domain.battle.turn.BattleTurnPhase
import com.jojo.game.domain.battle.turn.BattleTurnPolicy
import com.jojo.game.domain.battle.turn.BattleTurnSnapshot

class BattleTurnController(
    private val battle: Battle,
    private val showCamp: (BattleCampCard) -> Unit,
    private val runCampScript: (Faction) -> Boolean,
    private val runAi: (Faction) -> AiTurnResult,
    private val hasPendingAiPresentation: () -> Boolean = { false },
    private val presentCampState: (CampSettlement) -> Boolean = { true },
    private val presentDeaths: (BattleDeathCheckpoint) -> Boolean = { true },
    private val presentCampRestore: (CampSettlement) -> Boolean = { true },
    private val runRoundScript: (RoundAdvance) -> Boolean = { true },
    /** Preserve addRound's curCamp=REINFORCEMENTS observation before callback completion. */
    private val deferSynchronousRoundScriptCompletion: Boolean = false,
    private val presentWeather: (WeatherTransition) -> Boolean = { true },
    private val onCampEvents: (TurnResult) -> Unit = {},
    initialPhase: BattleTurnPhase = BattleTurnPhase.PLAYER_INPUT,
) {
    private val state = BattleTurnRuntimeState(initialPhase)

    /** Immutable lifecycle observation for presentation and tests. */
    val snapshot: BattleTurnSnapshot
        get() = state.snapshot()

    /**
     * Initial `_execControlScript(true)` enters Mine operation without the
     * ordinary `_setOper/_stateProcess/unitDeath` camp-start chain.
     */

    fun completeBootstrap() {
        check(state.phase == BattleTurnPhase.BOOTSTRAP) { "bootstrap completion outside bootstrap phase" }
        battle.roundLifecycle.prepareActiveCampOperation()
        state.phase = BattleTurnPhase.PLAYER_INPUT
    }

    /** Player's END_ROUND command. This is the entry corresponding to ctrl_mine. */
    fun endPlayerTurn(): Boolean {
        if (!BattleTurnPolicy.acceptsPlayerEnd(
        BattleTurnEntryRequest(state.phase, battle.activeFaction, battle.outcome()),
    )
) return false
        beginCampRestore()
        return true
    }

    /** Source COLLOCATION path: Mine is dispatched through the same _ai2 controller as AI camps. */
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

    /** RoundLayer's `fn` callback after exactly two seconds. */
    fun completeCampCard() {
        check(state.phase == BattleTurnPhase.CAMP_CARD) { "RoundLayer callback outside camp-card phase" }
        beginCampState()
    }


    fun completeCampStatePresentation() {
        check(state.phase == BattleTurnPhase.CAMP_STATE) { "state completion outside camp-state phase" }
        val fired = battle.roundLifecycle.runActiveCampEvents()
        state.lastTurn = state.lastTurn?.copy(firedEvents = fired)
        state.lastTurn?.let(onCampEvents)
        beginCampScript()
    }

    /** Called only when the source scene script reports COMPLETE. */
    fun completeCampScript() {
        check(state.phase == BattleTurnPhase.CAMP_SCRIPT) { "camp script completion outside script phase" }
        state.phase = BattleTurnPhase.CAMP_DEATHS
        if (presentDeaths(BattleDeathCheckpoint.CAMP_START)) completeCampDeathPresentation()
    }


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

    /** Final callback after BattleScreen has shown every `_ai2` actor pass. */
    fun completeAiPresentation(result: AiTurnResult? = null) {
        check(state.phase == BattleTurnPhase.AI) { "AI presentation completion outside AI phase" }
        if (result != null) state.lastAiResult = result
        beginCampRestore()
    }

    /**
     * Finish an AI callback whose first post-action scenario script called
     * `stage.end()`. The source does not run unit-hide or camp-restore after
     * that explicit boundary.
     */

    fun finishScriptEndedBattle() {
        check(state.phase == BattleTurnPhase.AI) { "script-end completion outside AI phase" }
        check(battle.outcome() != null) { "script ended without a battle outcome" }
        state.phase = BattleTurnPhase.FINISHED
    }


    fun completeCampRestorePresentation() {
        check(state.phase == BattleTurnPhase.CAMP_RESTORE) { "restore completion outside camp-restore state.phase" }
        state.phase = BattleTurnPhase.CAMP_RESTORE_DEATHS
        if (presentDeaths(BattleDeathCheckpoint.CAMP_RESTORE)) completeCampRestoreDeathPresentation()
    }


    fun completeCampRestoreDeathPresentation() {
        check(state.phase == BattleTurnPhase.CAMP_RESTORE_DEATHS) { "death completion outside restore-death phase" }
        if (finishIfBattleEnded()) return
        if (battle.activeFaction == Faction.REINFORCEMENTS) beginRoundBoundary() else enterNextCamp()
    }


    fun completeRoundScript() {
        check(state.phase == BattleTurnPhase.ROUND_SCRIPT) { "round script completion outside round-script phase" }
        state.phase = BattleTurnPhase.ROUND_DEATHS
        if (presentDeaths(BattleDeathCheckpoint.ROUND_START)) completeRoundDeathPresentation()
    }


    fun completeRoundDeathPresentation() {
        check(state.phase == BattleTurnPhase.ROUND_DEATHS) { "death completion outside round-death phase" }
        if (finishIfBattleEnded()) return
        battle.roundLifecycle.resetCompletedRoundSkillTemps(requireNotNull(state.lastRoundAdvance).completedRound)
        val transition = battle.roundLifecycle.applyScheduledWeather()
        state.lastWeatherTransition = transition
        state.phase = BattleTurnPhase.WEATHER
        if (presentWeather(transition)) completeWeatherPresentation()
    }


    fun completeWeatherPresentation() {
        check(state.phase == BattleTurnPhase.WEATHER) { "weather completion outside weather phase" }
        enterNextCamp()
    }

    private fun beginCampRestore() {
        // The source never lets isEnd bypass restore/unitDeath.  A lethal
        // poison/recoil/state tick must remain visible before FINISHED.
        val settlement = battle.roundLifecycle.settleActiveCampEnd()
        state.lastCampSettlement = settlement
        state.phase = BattleTurnPhase.CAMP_RESTORE
        if (presentCampRestore(settlement)) completeCampRestorePresentation()
    }

    private fun beginRoundBoundary() {
        val advance = battle.roundLifecycle.advanceRound()
        state.lastRoundAdvance = advance
        state.phase = BattleTurnPhase.ROUND_SCRIPT
        val completedSynchronously = runRoundScript(advance)
        if (completedSynchronously && !deferSynchronousRoundScriptCompletion) completeRoundScript()
    }

    private fun enterNextCamp() {
        if (finishIfBattleEnded()) return
        val previous = battle.activeFaction
        state.lastTurn = battle.roundLifecycle.advanceToNextCamp()
        val current = battle.activeFaction
        // BattleScreen._setOper only creates RoundLayer when crossing between
        // MINE/FRIEND and ENEMY. MINE -> FRIEND continues without a card.
        if (BattleTurnPolicy.campCardFor(BattleCampTransitionRequest(previous, current))) {
            state.phase = BattleTurnPhase.CAMP_CARD
            showCamp(BattleCampCard(requireNotNull(state.lastTurn), showsRoundNumber = current != Faction.ENEMY))
        } else {
            beginCampState()
        }
    }

    private fun beginCampState() {
        val settlement = battle.roundLifecycle.settleActiveCampStart()
        state.lastCampSettlement = settlement
        state.phase = BattleTurnPhase.CAMP_STATE
        if (presentCampState(settlement)) completeCampStatePresentation()
    }

    private fun beginCampScript() {
        state.phase = BattleTurnPhase.CAMP_SCRIPT
        if (runCampScript(battle.activeFaction)) completeCampScript()
    }

    private fun finishIfBattleEnded(): Boolean {
        if (battle.outcome() == null) return false
        state.phase = BattleTurnPhase.FINISHED
        return true
    }
}


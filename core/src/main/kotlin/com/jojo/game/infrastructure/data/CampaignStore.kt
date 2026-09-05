package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Preferences
import com.jojo.game.domain.campaign.CampaignState

/** Application-facing campaign state store backed by preferences. */
class CampaignStore(private val preferences: Preferences) {
    data class Snapshot(
        val currentScenario: String = "R_00",
        val completed: Set<String> = emptySet(),
        val choices: Map<String, String> = emptyMap(),
        /** Model.incStage-compatible save metadata used by battle=2 loads. */
        val stage: Int = 0,
    )

    /** Shared live state for all event and battle screens in this Game instance. */
    val state = CampaignState()
    private val persistence = CampaignStorePersistence(preferences, state)
    var snapshot: Snapshot = persistence.read()
        private set

    fun enter(scenario: String) {
        if (!scenario.startsWith("R_")) return
        snapshot = snapshot.copy(currentScenario = scenario)
        persist()
    }

    /** New Game must not inherit any previous Model, roster, or branch state. */
    fun newGame() {
        state.reset()
        snapshot = Snapshot()
        persist()
    }

    fun recordChoice(scenario: String, choice: String) {
        snapshot = snapshot.copy(choices = snapshot.choices + (scenario to choice))
        persist()
    }

    fun complete(scenario: String, nextScenario: String) {
        snapshot = snapshot.copy(currentScenario = nextScenario, completed = snapshot.completed + scenario)
        persist()
    }

    /** Flushes live state before a screen transition. */
    fun persist() = persistence.write(snapshot)

    fun incStage() {
        snapshot = snapshot.copy(stage = snapshot.stage + 1)
        persist()
    }

    /** StageLayer.jumpScene writes the absolute Model stage before replacement. */
    fun setStage(stage: Int) {
        require(stage >= 0) { "campaign stage must be non-negative" }
        snapshot = snapshot.copy(stage = stage)
        persist()
    }

    /** Numbered Manager.saveGame slot used by the source SaveLayer. */
    fun saveSlot(index: Int): String {
        require(index >= 0) { "save slot index must be non-negative" }
        return persistence.saveSlot(index, snapshot)
    }

    /** Manager.loadGame(index) representation consumed by SaveLayer. */
    fun loadSlot(index: Int): String? = persistence.loadSlot(index)

    fun savedPage(): Int = preferences.getInteger(SAVE_PAGE_KEY, 0)

    fun savePage(page: Int) {
        preferences.putInteger(SAVE_PAGE_KEY, page).flush()
    }

    /** Manager.resetGame + Model.loadGame for a numbered desktop slot. */
    fun restoreSlot(index: Int, raw: String): Boolean {
        val restored = persistence.restoreSlot(index, raw) ?: return false
        snapshot = restored
        return true
    }

    private companion object {
        const val SAVE_PAGE_KEY = "SAVE_PAGE"
    }
}

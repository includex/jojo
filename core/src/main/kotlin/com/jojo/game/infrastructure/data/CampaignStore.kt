package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Preferences
import com.jojo.game.domain.campaign.CampaignState

/** 화면 전환 사이의 캠페인 상태와 저장 슬롯을 관리한다. */
class CampaignStore(private val preferences: Preferences) {
    data class Snapshot(
        val currentScenario: String = "R_00",
        val completed: Set<String> = emptySet(),
        val choices: Map<String, String> = emptyMap(),
        /** 전투 진입 시 함께 저장되는 캠페인 단계이다. */
        val stage: Int = 0,
    )

    /** 현재 Game 인스턴스의 이벤트·전투 화면이 공유하는 상태이다. */
    val state = CampaignState()
    private val persistence = CampaignStorePersistence(preferences, state)
    var snapshot: Snapshot = persistence.read()
        private set

    /** 현재 시나리오를 진입 시나리오로 기록한다. */
    fun enter(scenario: String) {
        if (!scenario.startsWith("R_")) return
        snapshot = snapshot.copy(currentScenario = scenario)
        persist()
    }

    /** 새 게임을 시작하고 이전 상태를 모두 초기화한다. */
    fun newGame() {
        state.reset()
        snapshot = Snapshot()
        persist()
    }

    /** 시나리오 선택 결과를 현재 캠페인 스냅샷에 기록한다. */
    fun recordChoice(scenario: String, choice: String) {
        snapshot = snapshot.copy(choices = snapshot.choices + (scenario to choice))
        persist()
    }

    /** 완료한 시나리오와 다음 시나리오를 함께 기록한다. */
    fun complete(scenario: String, nextScenario: String) {
        snapshot = snapshot.copy(currentScenario = nextScenario, completed = snapshot.completed + scenario)
        persist()
    }

    /** 현재 상태를 환경설정 저장소에 즉시 반영한다. */
    fun persist() = persistence.write(snapshot)

    /** 캠페인 단계를 하나 증가시키고 저장한다. */
    fun incStage() {
        snapshot = snapshot.copy(stage = snapshot.stage + 1)
        persist()
    }

    /** 화면 교체 전에 절대 캠페인 단계를 저장한다. */
    fun setStage(stage: Int) {
        require(stage >= 0) { "campaign stage must be non-negative" }
        snapshot = snapshot.copy(stage = stage)
        persist()
    }

    /** 번호가 지정된 저장 슬롯에 현재 상태를 기록한다. */
    fun saveSlot(index: Int): String {
        require(index >= 0) { "save slot index must be non-negative" }
        return persistence.saveSlot(index, snapshot)
    }

    /** 번호가 지정된 저장 슬롯의 원본 레코드를 읽는다. */
    fun loadSlot(index: Int): String? = persistence.loadSlot(index)

    /** 마지막으로 선택한 저장 페이지를 읽는다. */
    fun savedPage(): Int = preferences.getInteger(SAVE_PAGE_KEY, 0)

    /** 저장 화면의 현재 페이지를 기록한다. */
    fun savePage(page: Int) {
        preferences.putInteger(SAVE_PAGE_KEY, page).flush()
    }

    /** 저장 슬롯의 레코드를 검증하고 현재 상태로 복원한다. */
    fun restoreSlot(index: Int, raw: String): Boolean {
        val restored = persistence.restoreSlot(index, raw) ?: return false
        snapshot = restored
        return true
    }

    private companion object {
        const val SAVE_PAGE_KEY = "SAVE_PAGE"
    }
}

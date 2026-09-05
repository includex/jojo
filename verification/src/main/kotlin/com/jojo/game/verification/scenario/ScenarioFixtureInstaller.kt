package com.jojo.game.verification.scenario

import com.jojo.game.application.runtime.RuntimeScenarioModal
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import com.jojo.game.application.runtime.RuntimeScenarioScene
import com.jojo.game.application.runtime.RuntimeScenarioUnit

/** Verification-owned authored projections for Scenario Hall comparisons. */
internal object ScenarioFixtureInstaller {
    fun hallScene(): RuntimeScenarioScene = RuntimeScenarioScene(
        backgroundId = 30,
        units = listOf(
            RuntimeScenarioUnit(0, 45, 48, 0),
            RuntimeScenarioUnit(157, 55, 52, 2),
            RuntimeScenarioUnit(181, 51, 45, 3),
        ),
        dialogueText = "원본 궁정 대화 UI 비교",
    )

    fun palaceScene(): RuntimeScenarioScene = RuntimeScenarioScene(
        backgroundId = 9,
        units = listOf(
            RuntimeScenarioUnit(181, 52, 41, 2),
            RuntimeScenarioUnit(157, 64, 41, 2),
            RuntimeScenarioUnit(0, 58, 70, 0),
        ),
        dialogueText = "원본 궁정 장면 UI 비교",
    )

    fun sectionScene(): RuntimeScenarioScene = RuntimeScenarioScene(
        backgroundId = 71,
        modal = RuntimeScenarioModal("section", "제일장막", 3f),
    )

    fun scene(overlay: RuntimeScenarioOverlay): RuntimeScenarioScene = hallScene()

    fun modal(overlay: RuntimeScenarioOverlay): RuntimeScenarioModal? = when (overlay) {
        RuntimeScenarioOverlay.INFO -> RuntimeScenarioModal("info", "재능의 첫 징후")
        RuntimeScenarioOverlay.GET_ITEM_EQUIPMENT -> RuntimeScenarioModal("info", "얻었다 단창 Lv0")
        RuntimeScenarioOverlay.MAP_INFO -> RuntimeScenarioModal("map-info", "조조가 수저우 도겸과 전투를 벌였을 때,")
        else -> null
    }
}

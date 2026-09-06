// Verification
package com.jojo.game.verification.scenario

import com.jojo.game.application.runtime.RuntimeScenarioModal
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import com.jojo.game.application.runtime.RuntimeScenarioScene
import com.jojo.game.application.runtime.RuntimeScenarioUnit

/** ScenarioFixtureInstaller: 시나리오 Hall 비교에 사용하는 원본 투영을 검증 모듈이 소유한다. */
internal object ScenarioFixtureInstaller {
    /** hallScene: 회관 검증 장면을 생성한다. */
    fun hallScene(): RuntimeScenarioScene = RuntimeScenarioScene(
        backgroundId = 30,
        units = listOf(
            RuntimeScenarioUnit(0, 45, 48, 0),
            RuntimeScenarioUnit(157, 55, 52, 2),
            RuntimeScenarioUnit(181, 51, 45, 3),
        ),
        dialogueText = "원본 궁정 대화 UI 비교",
    )

    /** palaceScene: 궁정 검증 장면을 생성한다. */
    fun palaceScene(): RuntimeScenarioScene = RuntimeScenarioScene(
        backgroundId = 9,
        units = listOf(
            RuntimeScenarioUnit(181, 52, 41, 2),
            RuntimeScenarioUnit(157, 64, 41, 2),
            RuntimeScenarioUnit(0, 58, 70, 0),
        ),
        dialogueText = "원본 궁정 장면 UI 비교",
    )

    /** sectionScene: 장면 구역의 검증 상태를 생성한다. */
    fun sectionScene(): RuntimeScenarioScene = RuntimeScenarioScene(
        backgroundId = 71,
        modal = RuntimeScenarioModal("section", "제일장막", 3f),
    )

    /** scene: 오버레이에 대응하는 검증 장면을 반환한다. */
    fun scene(overlay: RuntimeScenarioOverlay): RuntimeScenarioScene = hallScene()

    /** modal: 오버레이에 대응하는 모달 상태를 반환한다. */
    fun modal(overlay: RuntimeScenarioOverlay): RuntimeScenarioModal? = when (overlay) {
        RuntimeScenarioOverlay.INFO -> RuntimeScenarioModal("info", "재능의 첫 징후")
        RuntimeScenarioOverlay.GET_ITEM_EQUIPMENT -> RuntimeScenarioModal("info", "얻었다 단창 Lv0")
        RuntimeScenarioOverlay.MAP_INFO -> RuntimeScenarioModal("map-info", "조조가 수저우 도겸과 전투를 벌였을 때,")
        else -> null
    }
}

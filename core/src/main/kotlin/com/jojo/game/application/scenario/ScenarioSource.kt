// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.domain.scenario.ScenarioTimeline

/** ScenarioSource: 게임 화면이 사용하는 시나리오 실행 진입점이다. */
object ScenarioSource {

    fun loadFirstInteractiveSegment(moduleName: String = "R_00", functionName: String = "scene1"): ScenarioTimeline =
        ScenarioMetadataReader.loadFirstInteractiveSegment(moduleName, functionName)
}

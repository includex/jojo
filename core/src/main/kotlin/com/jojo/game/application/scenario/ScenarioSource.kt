// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.domain.scenario.ScenarioTimeline

/** ScenarioSource: 게임 화면이 사용하는 시나리오 실행 진입점이다. */
object ScenarioSource {

    /**
     * `loadFirstInteractiveSegment`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun loadFirstInteractiveSegment(moduleName: String = "R_00", functionName: String = "scene1"): ScenarioTimeline =
        ScenarioMetadataReader.loadFirstInteractiveSegment(moduleName, functionName)
}

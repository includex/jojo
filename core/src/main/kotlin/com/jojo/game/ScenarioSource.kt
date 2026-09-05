package com.jojo.game
import com.jojo.game.domain.scenario.*
import com.jojo.game.application.scenario.*

/** Public source-first entry point used by the LibGDX game screens. */
object ScenarioSource {
    /**
     * 공개 메서드 `loadFirstInteractiveSegment`
     *
     * ### 파라미터
    - `moduleName` (`String = "R_00"`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `functionName` (`String = "scene1"`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `ScenarioTimeline`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun loadFirstInteractiveSegment(moduleName: String = "R_00", functionName: String = "scene1"): ScenarioTimeline =
        ScenarioMetadataReader.loadFirstInteractiveSegment(moduleName, functionName)
}

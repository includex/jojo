// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** ScenarioHallOverlayEvidenceRecorder: 선택한 거점 오버레이 입력을 원본 좌표계의 렌더링 이벤트로 기록한다. */
internal class ScenarioHallOverlayEvidenceRecorder(private val input: ScenarioHallOverlayEvidenceInput) {
    /** append: 검증 이벤트와 산출물을 기록한다. */
    fun append(log: RenderEventLog) = ScenarioHallOverlayEventWriter(log, input).append()
}

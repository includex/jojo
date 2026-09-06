// Verification
package com.jojo.game.verification.preparation

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** StartBattleRenderEventContext: 원본 전투 시작 순회에 필요한 입력과 정확한 이벤트 수집기를 함께 제공한다. */
internal data class StartBattleRenderEventContext(
    /** log: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val log: RenderEventLog,
    /** phase: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val phase: String,
    /** scale: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val scale: Float,
    /** startBattleScreen: 전투 검증 상태를 담는다. */
    val startBattleScreen: String,
    /** spiritSorted: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val spiritSorted: Boolean,
) {
    /** draw: 검증 렌더 이벤트를 구성하고 반환한다. */
    fun draw(
        layer: String,
        nodePath: String,
        drawType: String,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        assetId: String?,
        opacity: Float,
        blend: Any,
        visible: Boolean,
        text: String,
    ) = log.draw(phase, layer, nodePath, drawType, x, y, w, h, assetId, opacity, blend, visible, text)
}

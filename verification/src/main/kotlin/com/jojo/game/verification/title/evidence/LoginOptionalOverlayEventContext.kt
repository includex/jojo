// Verification
package com.jojo.game.verification.title.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** LoginOptionalOverlayEventContext: 원본 선택적 로그인 오버레이 이벤트 순서를 받는 공용 수집기이다. */
internal data class LoginOptionalOverlayEventContext(
    /** log: log 상태를 검증 흐름에 전달한다. */
    val log: RenderEventLog,
    /** phase: 진행 단계 상태를 검증 흐름에 전달한다. */
    val phase: String,
    /** layer: 레이어 상태를 검증 흐름에 전달한다. */
    val layer: String,
) {
    /** draw: 검증 렌더 이벤트를 구성하고 반환한다. */
    fun draw(
        path: String,
        type: String,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        asset: String?,
        opacity: Float,
        blend: Any,
        text: String,
    ) = log.draw(phase, layer, path, type, x, y, w, h, asset, opacity = opacity, blend = blend, text = text)
}

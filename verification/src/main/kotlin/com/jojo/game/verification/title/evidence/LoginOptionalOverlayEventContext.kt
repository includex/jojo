package com.jojo.game.verification.title.evidence

import com.jojo.game.RenderEventLog

/** Shared sink for the authored optional-login overlay event sequence. */
internal data class LoginOptionalOverlayEventContext(
    val log: RenderEventLog,
    val phase: String,
    val layer: String,
) {
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

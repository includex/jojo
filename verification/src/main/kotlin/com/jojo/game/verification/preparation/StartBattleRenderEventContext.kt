package com.jojo.game.verification.preparation

import com.jojo.game.RenderEventLog

/** Shared inputs and exact event sink for the authored start-battle traversal. */
internal data class StartBattleRenderEventContext(
    val log: RenderEventLog,
    val phase: String,
    val scale: Float,
    val startBattleScreen: String,
    val spiritSorted: Boolean,
) {
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

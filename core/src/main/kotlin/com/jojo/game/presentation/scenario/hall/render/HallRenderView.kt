package com.jojo.game.presentation.scenario.hall.render

/** Immutable presentation-only inputs for the Hall overlay renderers. */
internal data class HallMenuRenderView(
    val eventName: String,
    val stageName: String,
    val ambitionFrom: Int,
    val ambitionTo: Int,
    val ambitionElapsedSeconds: Float,
    val indicatorEnabled: Boolean,
    val interactive: Boolean,
    val fixture: String?,
)

internal data class HallSaveRowRenderView(val number: String, val stage: String, val name: String)
internal data class HallSaveRenderView(
    val rows: List<HallSaveRowRenderView>,
    val pendingPrompt: String?,
    val completionTipOpen: Boolean,
)

internal data class HallCommandRenderView(
    val menuTexture: com.badlogic.gdx.graphics.Texture?,
    val battleTexture: com.badlogic.gdx.graphics.Texture?,
    val equipTexture: com.badlogic.gdx.graphics.Texture?,
    val buyTexture: com.badlogic.gdx.graphics.Texture?,
    val sellTexture: com.badlogic.gdx.graphics.Texture?,
)

internal sealed interface HallManagementRenderView {
    data class Sell(val catalog: com.jojo.game.presentation.scenario.hall.HallSellView) : HallManagementRenderView
}

/** Source-authored geometry retained independently of mutable Hall controllers. */
internal object HallRenderGeometry {
    val menuButtonCenters = floatArrayOf(55.107f, 143.365f, 231.846f, 320.74f, 423.317f, 511.575f, 600.056f, 690.441f, 789.44f)
    fun saveRowY(index: Int): Float = 547.534f - index * 52f
}

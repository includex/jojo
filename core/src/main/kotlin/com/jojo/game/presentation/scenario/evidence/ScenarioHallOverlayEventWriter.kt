package com.jojo.game.presentation.scenario.evidence

import com.jojo.game.RenderEventLog

internal class ScenarioHallOverlayEventWriter(
    private val log: RenderEventLog,
    val input: ScenarioHallOverlayEvidenceInput,
) {
    val fixture = input.fixture
    private val spriteBlend = listOf(770, 771)
    private val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    fun event(layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "", opacity: Float = 1f, visible: Boolean = true) =
        log.draw("hall-$fixture-stable", layer, path, type, x * .86f, y * .86f, w * .86f, h * .86f, asset, opacity = opacity, blend = if (type == "label" || type == "rich-text") labelBlend else spriteBlend, visible = visible, text = text)

    fun label(layer: String, path: String, value: String, x: Float, y: Float, w: Float, h: Float, visible: Boolean = true) =
        event(layer, path, "label", x, y, w, h, text = value, visible = visible)

    fun append() {
        event("HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f, "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>")
        when (fixture) {
            "feats", "feats-help" -> appendFixture0(this)
            "magic" -> appendMagic(this)
            "exclusive", "exclusive-tab1" -> appendFixture2(this)
            "info", "get-item-equipment", "get-item-property" -> appendFixture3(this)
            "item-equipment", "item-property", "item-discard-confirm" -> appendFixture4(this)
            "map-info" -> appendFixture5(this)
            "choice" -> appendFixture6(this)
            "ask" -> appendFixture7(this)
            "command" -> appendFixture8(this)
            "save", "save-confirm" -> appendFixture9(this)
            "ambition", "menu" -> appendFixture10(this)
        }
    }
}

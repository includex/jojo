package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.RenderEventLog

internal class ScenarioHelperEventWriter(private val log: RenderEventLog) {
    private val scale = .86f
    fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "", visible: Boolean = true) =
        log.draw("hall-helper-stable", "HelperLayer", path, type, x * scale, y * scale, w * scale, h * scale, asset,
            blend = if (type == "label" || type == "rich-text") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771), visible = visible, text = text)
    fun append() {
        appendHelperPart0(this); appendHelperPart1(this); appendHelperPart2(this); appendHelperPart3(this)
        appendHelperPart4(this); appendHelperPart5(this); appendHelperPart6(this)
    }
}

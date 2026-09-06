// Verification
package com.jojo.game.verification.scenario.evidence
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** ScenarioHelperEventWriter: 거점 도움말 레이어의 패널·문구·아이콘 이벤트를 분할 섹션 순서대로 작성한다. */
internal class ScenarioHelperEventWriter(private val log: RenderEventLog) {
    /** scale: 배율 값을 보관한다. */
    private val scale = .86f
    /** draw: 검증 렌더 이벤트를 구성하고 반환한다. */
    fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "", visible: Boolean = true) =
        log.draw("hall-helper-stable", "HelperLayer", path, type, x * scale, y * scale, w * scale, h * scale, asset,
            blend = if (type == "label" || type == "rich-text") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771), visible = visible, text = text)
    /** append: 검증 이벤트와 산출물을 기록한다. */
    fun append() {
        appendHelperPart0(this); appendHelperPart1(this); appendHelperPart2(this); appendHelperPart3(this)
        appendHelperPart4(this); appendHelperPart5(this); appendHelperPart6(this)
    }
}

// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** ScenarioHallOverlayEventWriter: 거점 오버레이 종류에 맞춰 원본 프리팹·문구·좌표 이벤트를 순서대로 작성한다. */
internal class ScenarioHallOverlayEventWriter(
    /** log: log 상태를 검증 흐름에 전달한다. */
    private val log: RenderEventLog,
    /** input: 검증 입력 정보를 담는다. */
    val input: ScenarioHallOverlayEvidenceInput,
) {
    val fixture = input.variant.artifactKey
    /** spriteBlend: 스프라이트 혼합 규칙 상태를 검증 흐름에 전달한다. */
    private val spriteBlend = listOf(770, 771)
    /** labelBlend: 라벨 혼합 규칙 상태를 검증 흐름에 전달한다. */
    private val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    /** event: 단일 렌더 이벤트를 로그에 추가한다. */
    fun event(layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "", opacity: Float = 1f, visible: Boolean = true) =
        log.draw("hall-$fixture-stable", layer, path, type, x * .86f, y * .86f, w * .86f, h * .86f, asset, opacity = opacity, blend = if (type == "label" || type == "rich-text") labelBlend else spriteBlend, visible = visible, text = text)

    /** label: 텍스트 라벨 이벤트를 렌더 로그에 추가한다. */
    fun label(layer: String, path: String, value: String, x: Float, y: Float, w: Float, h: Float, visible: Boolean = true) =
        event(layer, path, "label", x, y, w, h, text = value, visible = visible)

    /** append: 검증 이벤트와 산출물을 기록한다. */
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

/** com: 전투 명령 상태를 검증 흐름에 전달한다. */
internal val com.jojo.game.application.runtime.RuntimeScenarioOverlay.artifactKey: String
    get() = name.lowercase().replace('_', '-')

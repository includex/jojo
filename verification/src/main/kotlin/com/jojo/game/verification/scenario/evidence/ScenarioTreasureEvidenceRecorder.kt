// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** ScenarioTreasureEvidenceRecorder: 원본에 작성된 상태 비보유 TreasureLayer 카드 순회를 기록한다. */
internal class ScenarioTreasureEvidenceRecorder {
    /** append: 검증 이벤트와 산출물을 기록한다. */
    fun append(log: RenderEventLog, view: ScenarioStaticHallEvidenceView) {
        check(view.kind == ScenarioStaticHallEvidenceKind.TREASURE)
        val scale = .86f
        /** draw: 검증 렌더 이벤트를 구성하고 반환한다. */
        fun draw(path: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") {
            val visible = x + w > 0f && y + h > 0f && x < 1488.372f && y < 800f
            val type = when {
                text.isNotEmpty() -> "label"
                asset == "Logo_9-1" -> "tiled-sprite"
                asset in setOf("box1", "box2", "box3", "box4") -> "sliced-sprite"
                else -> "sprite"
            }
            log.draw(
                "hall-treasure-stable", "TreasureLayer", path, type,
                x * scale, y * scale, w * scale, h * scale, asset,
                blend = if (text.isEmpty()) listOf(770, 771) else listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
                visible = visible, text = text,
            )
        }
        draw("Canvas/Layer/bg1", 259.186f, 84f, 970f, 632f, "Logo_9-1")
        draw("Canvas/Layer/bg1/box3", 259.186f, 84f, 970f, 632f, "box3")
        draw("Canvas/Layer/bg1/title", 259.186f, 671f, 970f, 50f, "bg1")
        draw("Canvas/Layer/bg1/title/box1", 259.186f, 671f, 970f, 50f, "box1")
        draw("Canvas/Layer/bg1/title/label", 264.141f, 670.8f, 149.51f, 50.4f, text = "보물 도감")
        draw("Canvas/Layer/bg1/button7/Background", 1070.986f, 90.95f, 150.6f, 51.5f, "box3")
        draw("Canvas/Layer/bg1/button7/Background/Label", 1096.286f, 98.7f, 100f, 40f, text = "종료")
        draw("Canvas/Layer/bg1/label", 266.194f, 91.731f, 467.06f, 50.4f, text = "지금까지 발견한 보물 00 / 50")
        repeat(50) { index ->
            val column = index % 2
            val row = index / 2
            val x = 270.186f + column * 477f
            val y = 480.5f - row * 193f
            val itemPath = "Canvas/Layer/bg1/scrollview/view/content/item"
            draw(itemPath, x, y, 471f, 190f, "box3")
            draw("$itemPath/New Node", x, y, 471f, 190f, "Logo_9-1")
            draw("$itemPath/New Node/box3", x, y, 471f, 190f, "box3")
            draw("$itemPath/box2", x + 11.256f, y + 42.5f, 113f, 105f, "box2")
            draw("$itemPath/label0", x + 134f, y + 131f, 329f, 50f, text = "발견되지 않음")
        }
    }
}

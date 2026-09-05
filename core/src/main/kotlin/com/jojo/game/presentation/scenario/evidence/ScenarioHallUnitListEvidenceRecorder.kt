package com.jojo.game.presentation.scenario.evidence

import com.jojo.game.RenderEventLog

internal data class ScenarioHallUnitListEvidenceRow(val name: String, val posts: String)

internal class ScenarioHallUnitListEvidenceRecorder(
    private val rows: List<ScenarioHallUnitListEvidenceRow>,
) {
    fun append(log: RenderEventLog) {
        val scale = .86f; val sprites = listOf(770, 771); val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun event(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") =
            log.draw("hall-unit-list-stable", "UnitListLayer", path, type, x * scale, y * scale, w * scale, h * scale, asset, blend = if (type == "label") labels else sprites, text = text)
        log.draw(
            "hall-unit-list-stable", "HallLayer", "Canvas/Layer/Panel_cancel", "sprite",
            0f, 0f, 1488.372f * scale, 800f * scale, "default_sprite_splash",
            opacity = 0f, blend = sprites, visible = false,
        )
        event("Canvas/Layer/bg1", "tiled-sprite", 924.186f, 248.3f, 360f, 409.7f, "Logo_9-1")
        event("Canvas/Layer/bg1/vline", "sprite", 1101.186f, 249.85f, 6f, 406.5f, "vline")
        event("Canvas/Layer/bg1/box3", "sliced-sprite", 924.186f, 248.3f, 360f, 409.7f, "box1")
        rows.take(6).forEachIndexed { index, row ->
            val y = 607f - index * 52f
            val nameWidth = when (row.name) { "조조" -> 69.2f; "허자장" -> 103.8f; "병사 " -> 80.31f; else -> 103.8f }
            val postsWidth = if (row.posts == "군웅") 69.2f else 103.8f
            event("Canvas/Layer/bg1/scrollview/view/content/item", "sprite", 924.186f, y, 360f, 50f, "885a69b4-08ed-4c78-8896-ffb04eb2bd20")
            event("Canvas/Layer/bg1/scrollview/view/content/item/label0", "label", 1013.669f - nameWidth / 2f, y - .2f, nameWidth, 50.4f, text = row.name)
            event("Canvas/Layer/bg1/scrollview/view/content/item/label1", "label", 1194.669f - postsWidth / 2f, y - .2f, postsWidth, 50.4f, text = row.posts)
        }
    }
}

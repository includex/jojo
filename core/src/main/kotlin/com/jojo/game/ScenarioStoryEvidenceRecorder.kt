package com.jojo.game

/** Immutable input for a source-story render-event fixture. */
internal sealed interface ScenarioStoryEvidenceView {
    data object Palace : ScenarioStoryEvidenceView
    data object Section : ScenarioStoryEvidenceView

    data class StreetDialogue(
        val stage: String,
        val dialogueVisible: Boolean,
        val visibleText: String,
        val speakerName: String,
    ) : ScenarioStoryEvidenceView
}

/** Pure JSONL recorder for the palace, section, and isolated street-dialogue fixtures. */
internal class ScenarioStoryEvidenceRecorder {
    fun record(view: ScenarioStoryEvidenceView): String = RenderEventLog().also { log ->
        when (view) {
            ScenarioStoryEvidenceView.Palace -> appendPalace(log)
            ScenarioStoryEvidenceView.Section -> appendSection(log)
            is ScenarioStoryEvidenceView.StreetDialogue -> appendStreetDialogue(log, view)
        }
    }.jsonl()

    private fun appendPalace(log: RenderEventLog) {
        val scale = .86f
        val sprites = listOf(770, 771)
        val text = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun draw(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            value: String = ""
        ) =
            log.draw(
                "hall-palace-stable", "HallLayer", path, type, x * scale, y * scale, w * scale, h * scale, asset,
                blend = if (type == "label" || type == "rich-text") text else sprites, text = value
            )
        draw(
            "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/2d/2dbb846d-8694-484d-82f4-89503af77e56.f6e6f.jpg#<unnamed-frame>"
        )
        log.draw(
            "hall-palace-stable", "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f,
            "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
            opacity = 0f, blend = sprites, visible = false
        )
        draw("Canvas/Layer/bg0/face", "sprite", 98.628f, 496f, 192f, 240f, "1")
        draw("Canvas/Layer/bg0/bg2", "sprite", 319.233f, 498.5f, 798f, 191f, "U_select_10-1")
        draw("Canvas/Layer/bg0/bg2/richtext", "rich-text", 382.487f, 587.814f, 728f, 52.92f, value = "원본 궁정 장면 UI 비교")
        draw(
            "Canvas/Layer/bg0/bg2/richtext/RICHTEXT_CHILD",
            "label",
            382.487f,
            587.814f,
            325.13f,
            52.92f,
            value = "원본 궁정 장면 UI 비교"
        )
        draw("Canvas/Layer/bg0/label", "label", 403.896f, 633.52f, 66.28f, 54.4f, value = "조조")
    }

    private fun appendSection(log: RenderEventLog) {
        val scale = .86f
        val sprites = listOf(770, 771)
        fun sprite(
            path: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String,
            visible: Boolean = true,
            opacity: Float = 1f,
            layer: String = "HallLayer"
        ) =
            log.draw(
                "hall-section-stable", layer, path, "sprite", x * scale, y * scale, w * scale, h * scale, asset,
                opacity = opacity, blend = sprites, visible = visible
            )
        log.draw(
            "hall-section-stable", "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1280f, 688f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>", blend = sprites
        )
        sprite("Canvas/Layer/map/head/face", 1053.686f, 180f, 160f, 200f, "214")
        sprite("Canvas/Layer/map/head/face", 402.686f, 200f, 160f, 200f, "1")
        sprite(
            "Canvas/Layer/Panel_cancel",
            0f,
            0f,
            1488.372f,
            800f,
            "default_sprite_splash",
            visible = false,
            opacity = 0f
        )
        sprite("Canvas/Layer/bg0", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", layer = "SectionLayer")
        log.draw(
            "hall-section-stable", "SectionLayer", "Canvas/Layer/bg0/label", "label",
            571.186f * scale, 337f * scale, 346f * scale, 126f * scale,
            blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), text = "제일장막"
        )
    }

    private fun appendStreetDialogue(log: RenderEventLog, view: ScenarioStoryEvidenceView.StreetDialogue) {
        val stages = listOf("panel", "portrait", "speaker", "text", "background", "characters")
        val index = stages.indexOf(view.stage)
        if (index < 0) return
        val spriteBlend = listOf(770, 771)
        val textBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        if (index >= 4) {
            log.draw(
                "hall-${view.stage}-stable",
                "HallLayer",
                "Canvas/Layer/map",
                "sprite",
                0f,
                0f,
                1280f,
                688f,
                "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>",
                blend = spriteBlend
            )
            if (index >= 5) {
                log.draw(
                    "hall-${view.stage}-stable", "HallLayer", "Canvas/Layer/map/head/face", "sprite",
                    1053.686f * .86f, 180f * .86f, 160f * .86f, 200f * .86f, "214", blend = spriteBlend
                )
                log.draw(
                    "hall-${view.stage}-stable", "HallLayer", "Canvas/Layer/map/head/face", "sprite",
                    402.686f * .86f, 200f * .86f, 160f * .86f, 200f * .86f, "1", blend = spriteBlend
                )
            }
        }
        log.draw(
            "hall-${view.stage}-stable", "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f,
            "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
            opacity = 0f, blend = spriteBlend, visible = false
        )
        if (view.dialogueVisible && index >= 1) {
            log.draw(
                "hall-${view.stage}-stable", "DialogueLayer", "Canvas/Layer/bg0/face", "sprite",
                98.628f * .86f, 62f * .86f, 192f * .86f, 240f * .86f, "1", blend = spriteBlend
            )
        }
        log.draw(
            "hall-${view.stage}-stable", "DialogueLayer", "Canvas/Layer/bg0/bg2", "sprite",
            319.233f * .86f, 64.5f * .86f, 798f * .86f, 191f * .86f, "U_select_10-1", blend = spriteBlend
        )
        if (view.dialogueVisible && index >= 3) {
            log.draw(
                "hall-${view.stage}-stable", "DialogueLayer", "Canvas/Layer/bg0/bg2/richtext", "rich-text",
                382.487f * .86f, 153.814f * .86f, 728f * .86f, 52.92f * .86f, blend = textBlend, text = view.visibleText
            )
            log.draw(
                "hall-${view.stage}-stable",
                "DialogueLayer",
                "Canvas/Layer/bg0/bg2/richtext/RICHTEXT_CHILD",
                "label",
                382.487f * .86f,
                153.814f * .86f,
                325.13f * .86f,
                52.92f * .86f,
                blend = textBlend,
                text = view.visibleText
            )
        }
        if (view.dialogueVisible && index >= 2) {
            log.draw(
                "hall-${view.stage}-stable", "DialogueLayer", "Canvas/Layer/bg0/label", "label",
                403.896f * .86f, 199.52f * .86f, 66.28f * .86f, 54.4f * .86f, blend = textBlend, text = view.speakerName
            )
        }
    }
}

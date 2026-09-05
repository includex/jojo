package com.jojo.game.presentation.scenario.evidence

internal fun appendFixture3(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                val value = input.modalText
                val richTextWidth = when (fixture) {
                    "get-item-equipment" -> 259.72f
                    "get-item-property" -> 324.47f
                    else -> 229.83f
                }
                val richTextX = (1488.372f - richTextWidth) / 2f
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
                    opacity = 0f, visible = false
                )
                event(
                    "InfoLayer",
                    "Canvas/Layer/bg",
                    "sliced-sprite",
                    richTextX - 20f,
                    376.76f,
                    richTextWidth + 40f,
                    83f,
                    "bg"
                )
                event(
                    "InfoLayer",
                    "Canvas/Layer/bg/richtext",
                    "rich-text",
                    richTextX,
                    387f,
                    richTextWidth,
                    63f,
                    text = value
                )
                label(
                    "InfoLayer",
                    "Canvas/Layer/bg/richtext/RICHTEXT_CHILD",
                    value,
                    richTextX,
                    387f,
                    richTextWidth,
                    63f
                )
}

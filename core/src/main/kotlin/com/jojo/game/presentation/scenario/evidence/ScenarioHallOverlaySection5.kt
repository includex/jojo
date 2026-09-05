package com.jojo.game.presentation.scenario.evidence

internal fun appendFixture5(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                val value = input.modalText
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = 0f, visible = false
                )
                event(
                    "MapInfoLayer", "Canvas/Layer/bg1", "sprite", 0f, 0f, 1488.372f, 161f,
                    "default_sprite_splash", opacity = .498f
                )
                event(
                    "MapInfoLayer",
                    "Canvas/Layer/bg0/richtext",
                    "rich-text",
                    30.319f,
                    103.285f,
                    558.25f,
                    50.4f,
                    text = value
                )
                label(
                    "MapInfoLayer",
                    "Canvas/Layer/bg0/richtext/RICHTEXT_CHILD",
                    value,
                    30.319f,
                    103.285f,
                    558.25f,
                    50.4f
                )
}

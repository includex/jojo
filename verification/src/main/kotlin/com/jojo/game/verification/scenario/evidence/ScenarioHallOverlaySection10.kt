package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

internal fun appendFixture10(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .118f
                )
                event("HallMenuLayer", "Canvas/Layer/bg", "sprite", 0f, 0f, 1488.372f, 146f, "bg1")
                event("HallMenuLayer", "Canvas/Layer/bg/box1", "sliced-sprite", 0f, 0f, 1488.372f, 146f, "box1")
                event("HallMenuLayer", "Canvas/Layer/bg/bg0", "sliced-sprite", 115.955f, 4.946f, 304f, 44f, "box2")
                event(
                    "HallMenuLayer",
                    "Canvas/Layer/bg/bg0/Mark_64-1",
                    "sprite",
                    117.955f,
                    6.946f,
                    300f,
                    40f,
                    "Mark_64-1"
                )
                if (fixture == "ambition") label(
                    "HallMenuLayer",
                    "Canvas/Layer/bg/bg0/label",
                    "조조가 군대를 일으키다",
                    129.87f,
                    8.046f,
                    276.17f,
                    37.8f
                )
                event("HallMenuLayer", "Canvas/Layer/bg/bg1", "sliced-sprite", 425.986f, 4.9f, 324f, 44f, "box2")
                event(
                    "HallMenuLayer",
                    "Canvas/Layer/bg/bg1/Mark_64-1",
                    "sprite",
                    427.986f,
                    6.9f,
                    320f,
                    40f,
                    "Mark_64-1"
                )
                if (fixture == "ambition") label(
                    "HallMenuLayer",
                    "Canvas/Layer/bg/bg1/label",
                    "사수관 조조군 주진영",
                    462.876f,
                    8f,
                    250.22f,
                    37.8f
                )
                event("HallMenuLayer", "Canvas/Layer/bg/bar", "sliced-sprite", 834.186f, 19.417f, 300f, 15f, "Mark_4-1")
                event(
                    "HallMenuLayer", "Canvas/Layer/bg/bar/bar", "sliced-sprite", 834.186f, 19.417f,
                    if (fixture == "ambition") 165f else 150f, 15f, "Mark_1-1"
                )
                if (fixture == "menu") {
                    event("HallMenuLayer", "Canvas/Layer/bg/bar/flag0", "sprite", 787.27f, 11.917f, 32f, 30f, "flag1")
                    event("HallMenuLayer", "Canvas/Layer/bg/bar/flag1", "sprite", 1150.837f, 11.917f, 32f, 30f, "flag2")
                }
                val buttonXs =
                    floatArrayOf(11.107f, 99.365f, 187.846f, 276.74f, 379.317f, 467.575f, 556.056f, 646.441f, 745.44f)
                val buttonIds = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 9)
                buttonXs.forEachIndexed { index, x ->
                    val button = buttonIds[index]
                    val icon = if (button == 9) "help" else "tool${button + 1}"
                    val path = "Canvas/Layer/bg/button$button/Background"
                    event("HallMenuLayer", path, "sliced-sprite", x, 52.137f, 88f, 88f, "box3")
                    event(
                        "HallMenuLayer", "$path/${if (button == 9) "help" else "tool1"}", "sprite",
                        x + 8f, if (button == 9) 60.137f else 60.419f, 72f, 72f, icon
                    )
                }
}

package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

internal fun appendFixture7(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                event(
                    "HallLayer",
                    "Canvas/Layer/Panel_cancel",
                    "sprite",
                    0f,
                    0f,
                    1488.372f,
                    800f,
                    "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
                    opacity = 0f,
                    visible = false
                )
                event("MsgBox2", "Canvas/Layer/bg0", "tiled-sprite", 539.686f, 322f, 409f, 156f, "Logo_9-1")
                event("MsgBox2", "Canvas/Layer/bg0/bg1", "sprite", 539.686f, 428f, 409f, 50f, "bg1")
                label("MsgBox2", "Canvas/Layer/bg0/bg1/label", "확인", 544.686f, 427.8f, 69.2f, 50.4f)
                event("MsgBox2", "Canvas/Layer/bg0/box3", "sliced-sprite", 539.686f, 322f, 409f, 156f, "box1")
                listOf(
                    Triple(561.438f, 595.938f, "예"),
                    Triple(751.471f, 785.971f, "비")
                ).forEachIndexed { index, (x, labelX, value) ->
                    val path = "Canvas/Layer/bg0/button$index/Background"
                    event("MsgBox2", path, "sliced-sprite", x, 356f, 169f, 50f, "box3")
                    label("MsgBox2", "$path/Label", value, labelX, 361.844f, 100f, 40f)
                }
}

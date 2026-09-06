// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

/** appendFixture6: 검증 이벤트와 산출물을 기록한다. */
internal fun appendFixture6(writer: ScenarioHallOverlayEventWriter) = with(writer) {
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
                event("ChooseLayer", "Canvas/Layer/bg/face", "sprite", 268.693f, 279.314f, 192f, 240f, "1")
                event(
                    "ChooseLayer",
                    "Canvas/Layer/bg/scrollview",
                    "sprite",
                    492.686f,
                    308.15f,
                    747f,
                    183.7f,
                    "U_select_10-1"
                )
                val options = listOf(
                    Triple("바로 이게 제가 바라는 거예요", 438.5f, 367.72f),
                    Triple("이건 너무 이른 것 같아", 389.5f, 284.68f),
                )
                options.forEach { (value, y, childWidth) ->
                    val row = "Canvas/Layer/bg/scrollview/view/content/item/bg6"
                    event(
                        "ChooseLayer",
                        row,
                        "sprite",
                        538.886f,
                        y,
                        690.6f,
                        45f,
                        "885a69b4-08ed-4c78-8896-ffb04eb2bd20"
                    )
                    event("ChooseLayer", "$row/richtext", "rich-text", 561.486f, y + .45f, 642.6f, 44.1f, text = value)
                    label("ChooseLayer", "$row/richtext/RICHTEXT_CHILD", value, 561.486f, y + .45f, childWidth, 44.1f)
                }
}

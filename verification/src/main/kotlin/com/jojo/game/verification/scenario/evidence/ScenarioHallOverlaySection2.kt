// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.presentation.scenario.*

/** appendFixture2: 검증 이벤트와 산출물을 기록한다. */
internal fun appendFixture2(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .392f
                )
                event("ExclusiveLayer", "Canvas/Layer/bg", "tiled-sprite", 136.186f, 47f, 1216f, 706f, "Logo_9-1")
                event("ExclusiveLayer", "Canvas/Layer/bg/box1", "sliced-sprite", 136.186f, 47f, 1216f, 706f, "box1")
                event("ExclusiveLayer", "Canvas/Layer/bg/bg1", "sprite", 136.186f, 703f, 1216f, 50f, "bg1")
                label("ExclusiveLayer", "Canvas/Layer/bg/bg1/label", "장비 정보", 669.431f, 702.8f, 149.51f, 50.4f)

                /** header: 검증 출력 헤더를 구성한다. */
                fun header(
                    panel: String,
                    x: Float,
                    y: Float,
                    width: Float,
                    labelX: Float,
                    labelWidth: Float,
                    value: String
                ) {
                    event("ExclusiveLayer", "Canvas/Layer/bg/$panel/item", "sliced-sprite", x, y, width, 60f, "box4")
                    label(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/$panel/item/label",
                        value,
                        labelX,
                        y + 4.8f,
                        labelWidth,
                        50.4f
                    )
                }
                if (fixture == "exclusive") {
                    event(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/panel0",
                        "sliced-sprite",
                        138.186f,
                        117.5f,
                        1212f,
                        585f,
                        "box4"
                    )
                    listOf(371.375f, 604.197f, 840.498f).forEach { x ->
                        event(
                            "ExclusiveLayer",
                            "Canvas/Layer/bg/panel0/vline",
                            "sprite",
                            x,
                            120.254f,
                            6f,
                            524.8f,
                            "vline"
                        )
                    }
                    header("panel0", 138.586f, 642.1f, 236f, 221.986f, 69.2f, "무기")
                    header("panel0", 374.586f, 642.1f, 233f, 456.486f, 69.2f, "보구")
                    header("panel0", 607.636f, 642.1f, 236.5f, 691.286f, 69.2f, "보조")
                    header("panel0", 844.036f, 642.1f, 506.1f, 1022.331f, 149.51f, "특수 효과")
                } else {
                    event(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/panel1",
                        "sliced-sprite",
                        140.186f,
                        117.45f,
                        1208f,
                        585.7f,
                        "box4"
                    )
                    event(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/panel1/vline",
                        "sprite",
                        321.257f,
                        119.319f,
                        6f,
                        524.8f,
                        "vline"
                    )
                    event(
                        "ExclusiveLayer",
                        "Canvas/Layer/bg/panel1/vline",
                        "sprite",
                        565.153f,
                        119.205f,
                        6f,
                        524.8f,
                        "vline"
                    )
                    header("panel1", 140.85f, 643.3f, 185f, 181.286f, 103.8f, "소지자")
                    header("panel1", 324.236f, 643.3f, 243.9f, 411.586f, 69.2f, "이름")
                    header("panel1", 568.186f, 643.3f, 780f, 883.431f, 149.51f, "특수 효과")
                }

                /** button: 버튼 입력 이벤트를 기록한다. */
                fun button(name: String, x: Float, labelX: Float, labelWidth: Float, value: String) {
                    val path = "Canvas/Layer/bg/$name/Background"
                    event("ExclusiveLayer", path, "sliced-sprite", x, 54.533f, 200f, 54f, "box3")
                    label("ExclusiveLayer", "$path/Label", value, labelX, 59.533f, labelWidth, 50f)
                }
                button("button1", 354.241f, 370.741f, 167f, "전용 목록")
                button("button0", 147.282f, 163.782f, 167f, "세트 목록")
                button("button2", 1141.864f, 1191.864f, 100f, "확인")
}

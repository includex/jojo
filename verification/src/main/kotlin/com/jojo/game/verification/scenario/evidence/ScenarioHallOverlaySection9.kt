package com.jojo.game.verification.scenario.evidence
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.scenario.*

internal fun appendFixture9(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .392f
                )
                event("SaveLayer", "Canvas/Layer/bg1", "tiled-sprite", 278.186f, 83f, 932f, 634f, "Logo_9-1")
                event("SaveLayer", "Canvas/Layer/bg1/bg", "sliced-sprite", 278.186f, 83f, 932f, 634f, "box3")
                event("SaveLayer", "Canvas/Layer/bg1/bg1", "sprite", 278.186f, 667f, 932f, 50f, "bg1")
                label("SaveLayer", "Canvas/Layer/bg1/bg1/label", "진행 상황 유지", 288.186f, 666.8f, 229.83f, 50.4f)
                label(
                    "SaveLayer",
                    "Canvas/Layer/bg1/label",
                    "어떤 진행 상황을 저장할지 선택해 주세요.",
                    286.785f,
                    612.805f,
                    654.88f,
                    50.4f
                )
                event("SaveLayer", "Canvas/Layer/bg1/box2", "sliced-sprite", 287.186f, 172.534f, 912f, 428f, "box2")
                repeat(22) { index ->
                    val y = 547.534f - index * 52f
                    val visible = index < 12
                    val path = "Canvas/Layer/bg1/box2/scrollview/view/content/item"
                    event(
                        "SaveLayer", path, "sprite", 289.186f, y, 908f, 50f,
                        "885a69b4-08ed-4c78-8896-ffb04eb2bd20", visible = visible
                    )
                    label(
                        "SaveLayer",
                        "$path/label0",
                        "No.${(index + 1).toString().padStart(3, ' ')}",
                        295.448f,
                        y - .2f,
                        117.85f,
                        50.4f,
                        visible
                    )
                    label("SaveLayer", "$path/label1", "---", 434.615f, y - .2f, 124.49f, 50.4f, visible)
                    label("SaveLayer", "$path/label2", "진행 상황 저장 안 함", 577.886f, y, 616.3f, 50f, visible)
                }
                event(
                    "SaveLayer",
                    "Canvas/Layer/bg1/box2/vline",
                    "sliced-sprite",
                    422.057f,
                    174.634f,
                    6f,
                    423.8f,
                    "vline"
                )
                event(
                    "SaveLayer",
                    "Canvas/Layer/bg1/box2/vline",
                    "sliced-sprite",
                    566.695f,
                    174.634f,
                    6f,
                    423.8f,
                    "vline"
                )
                label(
                    "SaveLayer",
                    "Canvas/Layer/bg1/label",
                    "따뜻한 알림: 오래된 저장 파일일수록 앞에 표시됩니다.",
                    131.555f,
                    105.399f,
                    850.11f,
                    50.4f
                )
                event(
                    "SaveLayer",
                    "Canvas/Layer/bg1/button/Background",
                    "sliced-sprite",
                    1045.855f,
                    100.162f,
                    147.6f,
                    56f,
                    "box3"
                )
                label("SaveLayer", "Canvas/Layer/bg1/button/Background/Label", "취소", 1069.655f, 108.162f, 100f, 40f)
                if (fixture == "save-confirm") {
                    event(
                        "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                        "default_sprite_splash", opacity = 0f, visible = false
                    )
                    event("SaveLayer", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
                    event("SaveLayer", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
                    event(
                        "SaveLayer",
                        "Canvas/Layer/bg0/Logo_3-1",
                        "sprite",
                        453.005f,
                        373.951f,
                        106f,
                        124f,
                        "Logo_3-1"
                    )
                    label(
                        "SaveLayer",
                        "Canvas/Layer/bg0/label",
                        "진행도 No.1:진행 상황 저장 안 함저장할 수 있나요?",
                        573.686f,
                        335f,
                        463f,
                        190f
                    )
                    event(
                        "SaveLayer",
                        "Canvas/Layer/bg0/btns/button1/Background",
                        "sliced-sprite",
                        554.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "SaveLayer",
                        "Canvas/Layer/bg0/btns/button1/Background/Label",
                        "됐어",
                        557.336f,
                        279.085f,
                        168.1f,
                        40f
                    )
                    event(
                        "SaveLayer",
                        "Canvas/Layer/bg0/btns/button0/Background",
                        "sliced-sprite",
                        754.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "SaveLayer",
                        "Canvas/Layer/bg0/btns/button0/Background/Label",
                        "저장",
                        757.586f,
                        279.085f,
                        169.4f,
                        40f
                    )
                }
}

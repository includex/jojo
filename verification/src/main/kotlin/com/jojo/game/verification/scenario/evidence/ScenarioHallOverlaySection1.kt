// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

/** appendFixture0: 검증 이벤트와 산출물을 기록한다. */
internal fun appendFixture0(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .392f
                )
                val root = "Canvas/Layer/Logo_12-1"
                event("HallLayer", root, "tiled-sprite", 267.686f, 83.5f, 953f, 633f, "Logo_9-1")
                event("HallLayer", "$root/box4", "sliced-sprite", 267.686f, 83.5f, 953f, 633f, "box4")
                event("HallLayer", "$root/bg1", "sprite", 267.686f, 656.5f, 953f, 60f, "bg1")
                event("HallLayer", "$root/bg1/box3", "sliced-sprite", 267.686f, 656.5f, 953f, 60f, "box3")
                label("HallLayer", "$root/bg1/label", "공훈", 669.686f, 662.3f, 71.2f, 52.4f)
                event("HallLayer", "$root/scrollview", "tiled-sprite", 277.686f, 158.45f, 933f, 442.7f, "Logo_12-1")
                event("HallLayer", "$root/scrollview/box2", "tiled-sprite", 277.686f, 158.45f, 933f, 442.7f, "box2")
                input.featsRows.forEachIndexed { index, row ->
                    val rowY = 529.15f - index * 74f
                    val titleX = if (row.title == "민첩성") 290.286f else 307.586f
                    val titleW = if (row.title == "민첩성") 107.8f else 73.2f
                    val item = "$root/scrollview/view/content/item0"
                    event(
                        "HallLayer",
                        item,
                        "sprite",
                        279.686f,
                        rowY,
                        929f,
                        70f,
                        "885a69b4-08ed-4c78-8896-ffb04eb2bd20"
                    )
                    label("HallLayer", "$item/label0", row.title, titleX, rowY + 9.8f, titleW, 54.4f)
                    label("HallLayer", "$item/label1", row.ability.toString(), 462.941f, rowY + 9.8f, 48.49f, 54.4f)
                    label("HallLayer", "$item/label2", row.phaseLabel, 1086.816f, rowY + 9.8f, 70.74f, 54.4f)
                    event(
                        "HallLayer",
                        "$item/Feats/progressBar",
                        "sliced-sprite",
                        572.186f,
                        rowY + 20f,
                        446f,
                        30f,
                        "bg1"
                    )
                    event(
                        "HallLayer",
                        "$item/Feats/progressBar/bg1",
                        "sliced-sprite",
                        574.186f,
                        rowY + 20f,
                        442f,
                        30f,
                        "box2"
                    )
                    event(
                        "HallLayer",
                        "$item/Feats/progressBar/bar",
                        "sliced-sprite",
                        574.186f,
                        rowY + 22f,
                        442f * row.progressRatio,
                        26f,
                        "Mark_5-1"
                    )
                    label("HallLayer", "$item/Feats/label", row.progressLabel, 743.136f, rowY + 18.454f, 104.1f, 54.4f)
                }
                listOf(410.859f, 555.31f, 1027.419f).forEach { x ->
                    event("HallLayer", "$root/vline", "sliced-sprite", x, 160.25f, 6f, 450.3f, "vline")
                }

                /** header: 검증 출력 헤더를 구성한다. */
                fun header(x: Float, y: Float, w: Float, h: Float, lx: Float, lw: Float, value: String) {
                    event("HallLayer", "$root/box3", "sliced-sprite", x, y, w, h, "bg1")
                    event("HallLayer", "$root/box3/box3", "sliced-sprite", x, y, w, h, "box3")
                    label("HallLayer", "$root/box3/label", value, lx, 607.081f, lw, 50.4f)
                }
                header(272.836f, 601.45f, 142.7f, 55.1f, 269.431f, 149.51f, "능력 이름")
                header(415.436f, 601.45f, 143.5f, 55.1f, 435.286f, 103.8f, "능력치")
                header(559.136f, 601.5f, 472.1f, 55f, 588.216f, 413.94f, "현재/업그레이드 필요 공훈")
                header(1030.886f, 601.45f, 182.6f, 55.1f, 875.061f, 494.25f, "상위 단계로 승급하는 데 필요함")
                event("HallLayer", "$root/button0/Background", "sliced-sprite", 1059.386f, 96f, 147.6f, 56f, "box3")
                label("HallLayer", "$root/button0/Background/Label", "확인", 1083.186f, 104f, 100f, 40f)
                event("HallLayer", "$root/button1/Background", "sliced-sprite", 904.386f, 96f, 147.6f, 56f, "box3")
                label("HallLayer", "$root/button1/Background/Label", "설명", 928.186f, 104f, 100f, 40f)
                if (fixture == "feats-help") {
                    event(
                        "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                        "default_sprite_splash", opacity = 0f, visible = false
                    )
                    event("FeatsLayer", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
                    event("FeatsLayer", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
                    event(
                        "FeatsLayer",
                        "Canvas/Layer/bg0/Logo_3-1",
                        "sprite",
                        453.005f,
                        373.951f,
                        106f,
                        124f,
                        "Logo_3-1"
                    )
                    label("FeatsLayer", "Canvas/Layer/bg0/label", input.featsHelpText, 573.686f, 335f, 463f, 190f)
                    event(
                        "FeatsLayer",
                        "Canvas/Layer/bg0/btns/button0/Background",
                        "sliced-sprite",
                        654.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "FeatsLayer",
                        "Canvas/Layer/bg0/btns/button0/Background/Label",
                        "예",
                        657.586f,
                        279.085f,
                        169.4f,
                        40f
                    )
                }
}

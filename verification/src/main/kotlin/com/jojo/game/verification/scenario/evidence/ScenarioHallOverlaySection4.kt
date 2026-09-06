package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

internal fun appendFixture4(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                val property = fixture == "item-property"
                val discard = fixture == "item-discard-confirm"
                val item =
                    requireNotNull(input.items[if (property) 150 else if (discard) 4 else 0])
                val name = item.name
                val level = if (discard) "---" else "1"
                val type = if (property) "아이템" else item.typeName
                val price = item.purchasePrice.let { if (it == 255) "---" else it.toString() }
                val effect = when {
                    property -> "HP 회복"
                    discard -> "공격력 +38\n없음"
                    else -> "공격력 +10\n없음"
                }


                fun measuredWidth(value: String): Float =
                    value.count { it != ' ' } * 27.68f + value.count { it == ' ' } * 8.89f
                event(
                    "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                    "default_sprite_splash", opacity = .392f
                )
                event("ItemLayer", "Canvas/Layer/bg1", "tiled-sprite", 253.186f, 80f, 982f, 640f, "Logo_9-1")
                event("ItemLayer", "Canvas/Layer/bg1/box2", "sliced-sprite", 253.186f, 80f, 982f, 640f, "box3")
                label("ItemLayer", "Canvas/Layer/bg1/label0", name, 420.186f, 658.8f, 203.1f, 50.4f)
                if (!property) {
                    label("ItemLayer", "Canvas/Layer/bg1/label_1", "Lv", 673.411f, 658.483f, 42.25f, 50.4f)
                    label(
                        "ItemLayer",
                        "Canvas/Layer/bg1/label1",
                        level,
                        723.411f,
                        658.483f,
                        if (discard) 39.96f else 22.25f,
                        50.4f
                    )
                    label("ItemLayer", "Canvas/Layer/bg1/label_2", "Exp", 420.186f, 604.8f, 68.93f, 50.4f)
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg1/bar",
                        "sliced-sprite",
                        500.965f,
                        614.855f,
                        204f,
                        24f,
                        "default_scrollbar_bg"
                    )
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg1/bar/bar",
                        "sliced-sprite",
                        502.965f,
                        616.855f,
                        0f,
                        20f,
                        "Mark_6-1"
                    )
                    label("ItemLayer", "Canvas/Layer/bg1/bar/label1", "0/100", 552.915f, 606.655f, 100.1f, 50.4f)
                }
                event("ItemLayer", "Canvas/Layer/bg1/bg4", "sliced-sprite", 265.778f, 564.802f, 144f, 144f, "box2")
                event(
                    "ItemLayer",
                    "Canvas/Layer/bg1/bg4/icon",
                    "sprite",
                    273.778f,
                    572.802f,
                    128f,
                    128f,
                    "${item.icon}-1"
                )
                event("ItemLayer", "Canvas/Layer/bg1/bg0", "sliced-sprite", 420.536f, 498.55f, 343.5f, 100.9f, "box1")
                label("ItemLayer", "Canvas/Layer/bg1/bg0/label", "속성:", 432.137f, 548.543f, 80.31f, 50.4f)
                label("ItemLayer", "Canvas/Layer/bg1/bg0/label0", type, 522.525f, 548.543f, type.length * 34.6f, 50.4f)
                label("ItemLayer", "Canvas/Layer/bg1/bg0/label", "가격:", 432.137f, 503.543f, 80.31f, 50.4f)
                label(
                    "ItemLayer",
                    "Canvas/Layer/bg1/bg0/label1",
                    price,
                    522.525f,
                    503.543f,
                    if (price.length == 4) 88.98f else 66.74f,
                    50.4f
                )
                event("ItemLayer", "Canvas/Layer/bg1/bg1", "sliced-sprite", 261.686f, 92.5f, 501f, 377f, "box1")
                event("ItemLayer", "Canvas/Layer/bg1/bg1/bg1", "sprite", 470.286f, 447.7f, 83.8f, 40f, "bg1")
                label("ItemLayer", "Canvas/Layer/bg1/bg1/bg1/label", "효과", 477.586f, 442.5f, 69.2f, 50.4f)
                label(
                    "ItemLayer", "Canvas/Layer/bg1/bg1/scrollview/view/content/label", effect, 265.686f,
                    if (property) 389.966f else 345.966f, 493f, if (property) 55.44f else 99.44f
                )
                event("ItemLayer", "Canvas/Layer/bg1/bg2", "sliced-sprite", 770.186f, 157.5f, 448f, 247f, "box2")
                event("ItemLayer", "Canvas/Layer/bg1/bg2/bg1", "sprite", 943.336f, 369.55f, 89.7f, 40.9f, "bg1")
                label("ItemLayer", "Canvas/Layer/bg1/bg2/bg1/label", "설명", 953.586f, 378.8f, 69.2f, 50.4f)
                val introHeight = if (discard) 99.44f else 187.44f
                label(
                    "ItemLayer", "Canvas/Layer/bg1/bg2/scrollview/view/content/label", item.intro,
                    774.186f, 378.7f - introHeight, 440f, introHeight
                )
                event("ItemLayer", "Canvas/Layer/bg1/bg3", "sliced-sprite", 770.186f, 427f, 448f, 260f, "box1")
                event("ItemLayer", "Canvas/Layer/bg1/bg3/bg1", "sprite", 871.686f, 664.273f, 245f, 45f, "bg1")
                label(
                    "ItemLayer",
                    "Canvas/Layer/bg1/bg3/bg1/label",
                    "장착 가능한 부대입니다.",
                    804.516f,
                    661.573f,
                    379.34f,
                    50.4f
                )
                (0 until 27).forEach { row ->
                    val rowY = 609.55f - row * 52f
                    val visible = row < 13
                    val path = "Canvas/Layer/bg1/bg3/scrollview/view/content/item"
                    event(
                        "ItemLayer", path, "sliced-sprite", 772.186f, rowY, 444f, 50f,
                        if (row % 2 == 0) "885a69b4-08ed-4c78-8896-ffb04eb2bd20" else "bg2", visible = visible
                    )
                    (0..2).forEach { column ->
                        val value = if (row == 26 && column == 2) "패왕" else input.postsNames.getOrElse(row * 3 + column) { "" }
                        val width = measuredWidth(value)
                        val center = when (column) {
                            0 -> 851.186f; 1 -> 994.186f; else -> 1138.186f
                        }
                        label(
                            "ItemLayer",
                            "$path/label$column",
                            value,
                            center - width / 2f,
                            rowY + 4.84f,
                            width,
                            40.32f,
                            visible
                        )
                    }
                }
                event(
                    "ItemLayer",
                    "Canvas/Layer/bg1/button1/Background",
                    "sliced-sprite",
                    1065.827f,
                    97.824f,
                    150f,
                    50f,
                    "box3"
                )
                label("ItemLayer", "Canvas/Layer/bg1/button1/Background/Label", "확인", 1090.827f, 104.824f, 100f, 40f)
                if (discard) {
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg1/button2/Background",
                        "sliced-sprite",
                        901.312f,
                        97.824f,
                        150f,
                        50f,
                        "box3"
                    )
                    label(
                        "ItemLayer",
                        "Canvas/Layer/bg1/button2/Background/Label",
                        "버리기",
                        926.312f,
                        104.824f,
                        100f,
                        40f
                    )
                    event(
                        "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                        "default_sprite_splash", opacity = 0f, visible = false
                    )
                    event("ItemLayer", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
                    event("ItemLayer", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg0/Logo_3-1",
                        "sprite",
                        453.005f,
                        373.951f,
                        106f,
                        124f,
                        "Logo_3-1"
                    )
                    label("ItemLayer", "Canvas/Layer/bg0/label", "버릴 것을 결정하시겠습니까?$name?", 573.686f, 335f, 463f, 190f)
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg0/btns/button1/Background",
                        "sliced-sprite",
                        554.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "ItemLayer",
                        "Canvas/Layer/bg0/btns/button1/Background/Label",
                        "비",
                        557.336f,
                        279.085f,
                        168.1f,
                        40f
                    )
                    event(
                        "ItemLayer",
                        "Canvas/Layer/bg0/btns/button0/Background",
                        "sliced-sprite",
                        754.186f,
                        271.285f,
                        180f,
                        50f,
                        "box3"
                    )
                    label(
                        "ItemLayer",
                        "Canvas/Layer/bg0/btns/button0/Background/Label",
                        "예",
                        757.586f,
                        279.085f,
                        169.4f,
                        40f
                    )
                }
}

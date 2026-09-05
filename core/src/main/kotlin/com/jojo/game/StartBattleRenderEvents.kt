package com.jojo.game

/** Canonical authored traversal for Hall/scene/StartBattleScreen and its UnitInfoLayer child. */
internal fun appendStartBattleRenderEvents(
    log: RenderEventLog,
    unitInfo: Boolean,
    phaseOverride: String? = null,
    scale: Float = .86f,
    startBattleScreen: String = "StartBattleScreen",
    spiritSorted: Boolean = false,
) {
    val phase = phaseOverride ?: if (unitInfo) "hall-start-battle-unit-info-stable" else "hall-start-battle-stable"
    log.draw(
        phase,
        "HallLayer",
        "Canvas/Layer/map",
        "sprite",
        0.0f * scale,
        0.0f * scale,
        1488.372f * scale,
        800.0f * scale,
        assetId = "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "HallLayer",
        "Canvas/Layer/Panel_cancel",
        "sprite",
        0.0f * scale,
        0.0f * scale,
        1488.372f * scale,
        800.0f * scale,
        assetId = "default_sprite_splash",
        opacity = 0.118f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg",
        "tiled-sprite",
        160.536f * scale,
        50.0f * scale,
        1167.3f * scale,
        700.0f * scale,
        assetId = "Logo_9-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content",
        "sprite",
        167.186f * scale,
        3.5f * scale,
        800.0f * scale,
        736.0f * scale,
        assetId = "U_select_4-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        startBattleScreen,
        "Canvas/Layer/bg/scrollview/view/content/node/testet",
        "sprite",
        176.086f * scale,
        624.602f * scale,
        115.2f * scale,
        115.2f * scale,
        assetId = if (spiritSorted) "assets/Game/native/ab/abd7ecf9-86cb-40a9-93b3-d5f6749a6c0e.c8ba0.png#<unnamed-frame>" else "assets/Game/native/1c/1c30efbe-adcc-4d44-872d-4afaeba84443.b7fcb.png#<unnamed-frame>",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label",
        "label",
        168.744f * scale,
        642.355f * scale,
        58.82f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Lv."
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label0",
        "label",
        272.798f * scale,
        640.198f * scale,
        26.25f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "3"
    )
    log.draw(
        phase,
        startBattleScreen,
        "Canvas/Layer/bg/scrollview/view/content/node/label1",
        "label",
        171.686f * scale,
        598.087f * scale,
        124.0f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = if (spiritSorted) "병사 " else "조조"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/testet",
        "sprite",
        309.086f * scale,
        624.602f * scale,
        115.2f * scale,
        115.2f * scale,
        assetId = "assets/Game/native/ab/abd7ecf9-86cb-40a9-93b3-d5f6749a6c0e.c8ba0.png#<unnamed-frame>",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label",
        "label",
        301.744f * scale,
        642.355f * scale,
        58.82f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Lv."
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label0",
        "label",
        405.798f * scale,
        640.198f * scale,
        26.25f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "3"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label1",
        "label",
        304.686f * scale,
        598.087f * scale,
        124.0f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "병사 "
    )
    log.draw(
        phase,
        startBattleScreen,
        "Canvas/Layer/bg/scrollview/view/content/node/testet",
        "sprite",
        442.086f * scale,
        624.602f * scale,
        115.2f * scale,
        115.2f * scale,
        assetId = if (spiritSorted) "assets/Game/native/31/31c33723-039d-4d93-b4ba-4d41456fda84.dcc7c.png#<unnamed-frame>" else "assets/Game/native/ab/abd7ecf9-86cb-40a9-93b3-d5f6749a6c0e.c8ba0.png#<unnamed-frame>",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label",
        "label",
        434.744f * scale,
        642.355f * scale,
        58.82f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Lv."
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label0",
        "label",
        538.798f * scale,
        640.198f * scale,
        26.25f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "3"
    )
    log.draw(
        phase,
        startBattleScreen,
        "Canvas/Layer/bg/scrollview/view/content/node/label1",
        "label",
        437.686f * scale,
        598.087f * scale,
        124.0f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = if (spiritSorted) "허자장" else "병사 "
    )
    log.draw(
        phase,
        startBattleScreen,
        "Canvas/Layer/bg/scrollview/view/content/node/testet",
        "sprite",
        575.086f * scale,
        624.602f * scale,
        115.2f * scale,
        115.2f * scale,
        assetId = if (spiritSorted) "assets/Game/native/1c/1c30efbe-adcc-4d44-872d-4afaeba84443.b7fcb.png#<unnamed-frame>" else "assets/Game/native/31/31c33723-039d-4d93-b4ba-4d41456fda84.dcc7c.png#<unnamed-frame>",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label",
        "label",
        567.744f * scale,
        642.355f * scale,
        58.82f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Lv."
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/scrollview/view/content/node/label0",
        "label",
        671.798f * scale,
        640.198f * scale,
        26.25f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "3"
    )
    log.draw(
        phase,
        startBattleScreen,
        "Canvas/Layer/bg/scrollview/view/content/node/label1",
        "label",
        570.686f * scale,
        598.087f * scale,
        124.0f * scale,
        54.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = if (spiritSorted) "조조" else "허자장"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/bg",
        "sprite",
        167.336f * scale,
        61.134f * scale,
        800.0f * scale,
        256.0f * scale,
        assetId = "U_select_5-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/bg/box2",
        "sliced-sprite",
        167.336f * scale,
        61.134f * scale,
        800.0f * scale,
        256.0f * scale,
        assetId = "box2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/bg/scrollview/view/content/node/bg",
        "sprite",
        167.336f * scale,
        196.645f * scale,
        100.0f * scale,
        60.0f * scale,
        assetId = "U_select_3-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/bg/scrollview/view/content/node/testet",
        "sprite",
        169.336f * scale,
        225.019f * scale,
        96.0f * scale,
        96.0f * scale,
        assetId = "assets/Game/native/1c/1c30efbe-adcc-4d44-872d-4afaeba84443.b7fcb.png#<unnamed-frame>",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/bg/scrollview/view/content/node/bg",
        "sprite",
        267.336f * scale,
        194.645f * scale,
        100.0f * scale,
        64.0f * scale,
        assetId = "U_select_2-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/bg/scrollview/view/content/node/bg",
        "sprite",
        367.336f * scale,
        194.645f * scale,
        100.0f * scale,
        64.0f * scale,
        assetId = "U_select_1-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/bg/scrollview/view/content/node/bg",
        "sprite",
        467.336f * scale,
        194.645f * scale,
        100.0f * scale,
        64.0f * scale,
        assetId = "U_select_1-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/button1_0/Background",
        "sliced-sprite",
        765.186f * scale,
        321.0f * scale,
        200.0f * scale,
        50.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        startBattleScreen,
        "Canvas/Layer/bg/button1_0/Background/Label",
        "label",
        783.186f * scale,
        329.0f * scale,
        164.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = if (spiritSorted) "정신력" else "부대 속성"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box3",
        "sliced-sprite",
        160.536f * scale,
        50.0f * scale,
        1167.3f * scale,
        700.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/label0",
        "label",
        979.335f * scale,
        690.22f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "조조"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/label1",
        "label",
        1245.602f * scale,
        692.378f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "군웅"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/label2",
        "label",
        170.186f * scale,
        320.8f * scale,
        240.67f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "출진 무장 - 1/4"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1",
        "sliced-sprite",
        970.436f * scale,
        112.55f * scale,
        347.5f * scale,
        558.1f * scale,
        assetId = "box1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/bg1",
        "sprite",
        997.007f * scale,
        648.335f * scale,
        161.7f * scale,
        40.3f * scale,
        assetId = "bg1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/bg1/label",
        "label",
        1003.102f * scale,
        643.285f * scale,
        149.51f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "무장 정보"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1",
        "sliced-sprite",
        980.186f * scale,
        131.65f * scale,
        328.0f * scale,
        513.0f * scale,
        assetId = "box1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/mask/face",
        "sprite",
        986.586f * scale,
        433.558f * scale,
        163.2f * scale,
        204.0f * scale,
        assetId = "1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1193.369f * scale,
        587.95f * scale,
        42.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Lv"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_lv",
        "label",
        1279.936f * scale,
        587.95f * scale,
        22.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "3"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1155.369f * scale,
        536.95f * scale,
        80.04f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "EXP"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_exp",
        "label",
        1279.936f * scale,
        535.95f * scale,
        22.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "0"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1168.369f * scale,
        486.95f * scale,
        66.68f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "HP:"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_hp",
        "label",
        1235.446f * scale,
        483.95f * scale,
        66.74f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "123"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1164.369f * scale,
        435.95f * scale,
        71.11f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "MP:"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_mp",
        "label",
        1257.696f * scale,
        437.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "36"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        983.186f * scale,
        385.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "무력"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_wl",
        "label",
        1096.696f * scale,
        385.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "82"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1155.369f * scale,
        385.95f * scale,
        103.8f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "민첩성"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_mj",
        "label",
        1257.696f * scale,
        385.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "80"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        983.186f * scale,
        335.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "지력"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_zl",
        "label",
        1096.696f * scale,
        335.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "92"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1155.369f * scale,
        335.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "운기"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_yq",
        "label",
        1257.696f * scale,
        335.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "84"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        983.186f * scale,
        284.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "지휘"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_ts",
        "label",
        1096.696f * scale,
        284.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "98"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        983.186f * scale,
        234.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "공격"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_gj",
        "label",
        1096.696f * scale,
        234.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "60"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1155.369f * scale,
        234.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "방어"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_fy",
        "label",
        1257.696f * scale,
        234.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "68"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        983.186f * scale,
        183.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "정신"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_js",
        "label",
        1096.696f * scale,
        183.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "55"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1155.369f * scale,
        183.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "폭발"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_bf",
        "label",
        1257.696f * scale,
        183.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "49"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        983.186f * scale,
        133.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "사기"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_sq",
        "label",
        1096.696f * scale,
        133.95f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "51"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label",
        "label",
        1155.369f * scale,
        133.95f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "이동"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/box1/label_yd",
        "label",
        1279.936f * scale,
        133.95f * scale,
        22.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "6"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/box3",
        "sliced-sprite",
        980.186f * scale,
        -21.35f * scale,
        328.0f * scale,
        150.0f * scale,
        assetId = "box2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/label",
        "label",
        990.186f * scale,
        73.825f * scale,
        80.31f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "무기:"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/label0",
        "label",
        1085.186f * scale,
        73.825f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "단검"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/bg",
        "sliced-sprite",
        990.186f * scale,
        -12.975f * scale,
        80.0f * scale,
        80.0f * scale,
        assetId = "box2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/bg/Item_4-1",
        "sprite",
        993.386f * scale,
        -9.775f * scale,
        73.6f * scale,
        73.6f * scale,
        assetId = "1-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/label2",
        "label",
        1084.186f * scale,
        28.825f * scale,
        42.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Lv"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/label3",
        "label",
        1084.186f * scale,
        -16.175f * scale,
        68.93f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Exp"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/progressBar",
        "sliced-sprite",
        1157.186f * scale,
        -15.975f * scale,
        144.0f * scale,
        24.0f * scale,
        assetId = "default_progressbar_bg",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/progressBar/bar",
        "sliced-sprite",
        1159.186f * scale,
        -13.975f * scale,
        0.0f * scale,
        20.0f * scale,
        assetId = "Mark_6-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/progressBar/label1",
        "label",
        1179.136f * scale,
        -16.175f * scale,
        100.1f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "0/100"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node0/label1",
        "label",
        1140.186f * scale,
        28.825f * scale,
        22.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "1"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/box3",
        "sliced-sprite",
        980.186f * scale,
        -174.35f * scale,
        328.0f * scale,
        150.0f * scale,
        assetId = "box2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/label",
        "label",
        990.186f * scale,
        -79.175f * scale,
        80.31f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "보구:"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/label0",
        "label",
        1085.186f * scale,
        -79.175f * scale,
        149.51f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "가죽 갑옷"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/bg",
        "sliced-sprite",
        990.186f * scale,
        -165.975f * scale,
        80.0f * scale,
        80.0f * scale,
        assetId = "box2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/bg/Item_4-1",
        "sprite",
        993.386f * scale,
        -162.775f * scale,
        73.6f * scale,
        73.6f * scale,
        assetId = "39-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/label2",
        "label",
        1084.186f * scale,
        -124.175f * scale,
        42.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "Lv"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/label3",
        "label",
        1084.186f * scale,
        -169.175f * scale,
        68.93f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "Exp"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/progressBar",
        "sliced-sprite",
        1157.186f * scale,
        -168.975f * scale,
        144.0f * scale,
        24.0f * scale,
        assetId = "default_progressbar_bg",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/progressBar/bar",
        "sliced-sprite",
        1159.186f * scale,
        -166.975f * scale,
        0.0f * scale,
        20.0f * scale,
        assetId = "Mark_6-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/progressBar/label1",
        "label",
        1179.136f * scale,
        -169.175f * scale,
        100.1f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "0/100"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node1/label1",
        "label",
        1140.186f * scale,
        -124.175f * scale,
        22.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "1"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node2/box3",
        "sliced-sprite",
        980.186f * scale,
        -327.35f * scale,
        328.0f * scale,
        150.0f * scale,
        assetId = "box2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node2/label",
        "label",
        990.186f * scale,
        -232.175f * scale,
        80.31f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "보조:"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/box1/scrollview/view/content/node2/bg",
        "sliced-sprite",
        990.186f * scale,
        -318.975f * scale,
        80.0f * scale,
        80.0f * scale,
        assetId = "box2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/button0/Background",
        "sliced-sprite",
        1110.186f * scale,
        58.0f * scale,
        100.0f * scale,
        50.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/button0/Background/Label",
        "label",
        1110.186f * scale,
        66.0f * scale,
        100.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "결정"
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/button1/Background",
        "sliced-sprite",
        1220.186f * scale,
        58.0f * scale,
        100.0f * scale,
        50.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "StartBattleScreen",
        "Canvas/Layer/bg/button1/Background/Label",
        "label",
        1220.186f * scale,
        66.0f * scale,
        100.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "취소"
    )
    if (!unitInfo) return
    log.draw(
        phase,
        "HallLayer",
        "Canvas/Layer/Panel_cancel",
        "sprite",
        0.0f * scale,
        0.0f * scale,
        1488.372f * scale,
        800.0f * scale,
        assetId = "default_sprite_splash",
        opacity = 0.392f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1",
        "tiled-sprite",
        197.186f * scale,
        12.0f * scale,
        1094.0f * scale,
        776.0f * scale,
        assetId = "Logo_9-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/title",
        "sprite",
        197.186f * scale,
        738.0f * scale,
        1094.0f * scale,
        50.0f * scale,
        assetId = "bg1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/title/label",
        "label",
        202.186f * scale,
        737.8f * scale,
        149.51f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "무장 정보"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button0/Background",
        "sliced-sprite",
        825.923f * scale,
        712.65f * scale,
        188.0f * scale,
        60.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button0/Background/Label",
        "label",
        803.423f * scale,
        725.65f * scale,
        233.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "무장 열전"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button1/Background",
        "sliced-sprite",
        1014.008f * scale,
        712.65f * scale,
        193.4f * scale,
        60.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button1/Background/Label",
        "label",
        994.208f * scale,
        725.65f * scale,
        233.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "부대 특성"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button2/Background",
        "sliced-sprite",
        826.481f * scale,
        651.471f * scale,
        130.0f * scale,
        60.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button2/Background/Label",
        "label",
        774.981f * scale,
        664.471f * scale,
        233.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "능력"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button3/Background",
        "sliced-sprite",
        956.444f * scale,
        651.471f * scale,
        130.0f * scale,
        60.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button3/Background/Label",
        "label",
        904.944f * scale,
        664.471f * scale,
        233.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "장비"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button4/Background",
        "sliced-sprite",
        1086.444f * scale,
        651.471f * scale,
        130.0f * scale,
        60.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button4/Background/Label",
        "label",
        1034.944f * scale,
        664.471f * scale,
        233.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "마법"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/face",
        "sprite",
        230.186f * scale,
        490.956f * scale,
        192.0f * scale,
        240.0f * scale,
        assetId = "1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0",
        "sliced-sprite",
        454.186f * scale,
        509.642f * scale,
        358.0f * scale,
        144.0f * scale,
        assetId = "box1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/bg1",
        "sprite",
        467.022f * scale,
        634.38f * scale,
        166.0f * scale,
        40.0f * scale,
        assetId = "bg1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/bg1/label",
        "label",
        475.267f * scale,
        629.18f * scale,
        149.51f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "부대 속성"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/label0",
        "label",
        466.186f * scale,
        578.901f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "군웅"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/label",
        "label",
        682.215f * scale,
        578.901f * scale,
        42.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Lv"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/label1",
        "label",
        778.413f * scale,
        578.901f * scale,
        22.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "3"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/label",
        "label",
        466.186f * scale,
        520.442f * scale,
        68.93f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "Exp"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/bar",
        "sliced-sprite",
        545.186f * scale,
        531.052f * scale,
        254.0f * scale,
        24.0f * scale,
        assetId = "default_progressbar_bg",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/bar/bar",
        "sliced-sprite",
        547.186f * scale,
        533.052f * scale,
        0.0f * scale,
        20.0f * scale,
        assetId = "Mark_6-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg0/bar/label1",
        "label",
        622.136f * scale,
        531.852f * scale,
        100.1f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "0/100"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1",
        "sliced-sprite",
        454.186f * scale,
        339.359f * scale,
        358.0f * scale,
        144.0f * scale,
        assetId = "box1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/bg1",
        "sprite",
        468.944f * scale,
        461.813f * scale,
        85.0f * scale,
        40.8f * scale,
        assetId = "bg1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/bg1/label",
        "label",
        476.844f * scale,
        457.013f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "상태"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/label",
        "label",
        468.186f * scale,
        409.159f * scale,
        55.57f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "HP"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/hp_bar",
        "sliced-sprite",
        547.186f * scale,
        420.359f * scale,
        254.0f * scale,
        24.0f * scale,
        assetId = "default_progressbar_bg",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/hp_bar/bar",
        "sliced-sprite",
        549.186f * scale,
        422.359f * scale,
        250.0f * scale,
        20.0f * scale,
        assetId = "Mark_3-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/hp_bar/label1",
        "label",
        601.891f * scale,
        421.159f * scale,
        144.59f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "123/123"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/label",
        "label",
        468.186f * scale,
        355.159f * scale,
        60.0f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "MP"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/mp_bar",
        "sliced-sprite",
        547.186f * scale,
        364.359f * scale,
        254.0f * scale,
        24.0f * scale,
        assetId = "default_progressbar_bg",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/mp_bar/bar",
        "sliced-sprite",
        549.186f * scale,
        366.359f * scale,
        250.0f * scale,
        20.0f * scale,
        assetId = "Mark_2-1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/bg1/mp_bar/label1",
        "label",
        624.136f * scale,
        365.159f * scale,
        100.1f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "36/36"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/label0",
        "label",
        455.186f * scale,
        678.756f * scale,
        212.4f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "조조"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/box1",
        "sliced-sprite",
        206.136f * scale,
        394.456f * scale,
        241.3f * scale,
        75.0f * scale,
        assetId = "box1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/box1/bg1",
        "sprite",
        216.601f * scale,
        448.425f * scale,
        85.1f * scale,
        39.9f * scale,
        assetId = "bg1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/box1/bg1/label",
        "label",
        224.551f * scale,
        443.175f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "현금"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/box1/box1/label",
        "label",
        418.456f * scale,
        398.558f * scale,
        22.25f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "0"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/vline2",
        "sprite",
        201.186f * scale,
        328.4f * scale,
        620.0f * scale,
        2.0f * scale,
        assetId = "vline2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/label",
        "label",
        436.431f * scale,
        268.0f * scale,
        149.51f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "인물 상태"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/bg2/label",
        "label",
        202.232f * scale,
        202.662f * scale,
        620.0f * scale,
        55.44f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "정상입니다."
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/vline2",
        "sprite",
        201.186f * scale,
        190.56f * scale,
        620.0f * scale,
        2.0f * scale,
        assetId = "vline2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/label",
        "label",
        396.271f * scale,
        130.16f * scale,
        229.83f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "모든 특기 보기"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/scrollview/view/content/label99",
        "label",
        202.441f * scale,
        76.16f * scale,
        617.49f * scale,
        44.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "없음"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/box1",
        "sliced-sprite",
        821.986f * scale,
        71.95f * scale,
        457.0f * scale,
        580.5f * scale,
        assetId = "box1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2",
        "sliced-sprite",
        831.486f * scale,
        431.2f * scale,
        438.0f * scale,
        197.0f * scale,
        assetId = "box1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/bg1",
        "sprite",
        845.841f * scale,
        606.745f * scale,
        163.9f * scale,
        41.2f * scale,
        assetId = "bg1",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/bg1/label",
        "label",
        853.036f * scale,
        602.145f * scale,
        149.51f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "기본 능력"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label",
        "label",
        848.106f * scale,
        548.5f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "무력"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label",
        "label",
        848.106f * scale,
        495.5f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "지력"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label",
        "label",
        848.106f * scale,
        442.5f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "지휘"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label",
        "label",
        1059.486f * scale,
        548.04f * scale,
        103.8f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "민첩성"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label",
        "label",
        1059.486f * scale,
        495.5f * scale,
        69.2f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "운기"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label0",
        "label",
        945.277f * scale,
        548.04f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "82"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label2",
        "label",
        945.277f * scale,
        495.5f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "92"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label1",
        "label",
        945.277f * scale,
        442.5f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "98"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label3",
        "label",
        1155.034f * scale,
        548.04f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "80"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg2/label4",
        "label",
        1155.034f * scale,
        495.5f * scale,
        44.49f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "84"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/label",
        "label",
        975.731f * scale,
        378.8f * scale,
        149.51f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "무장 소개"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg3/label",
        "label",
        835.986f * scale,
        -76.827f * scale,
        430.0f * scale,
        451.44f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "자는 맹덕이다. 문무를 겸비한 한 시대의 간웅으로, 뛰어난 전략적 안목과 예견 능력을 가졌다. 황건군을 평정한 후, 동탁을 정벌하고 천자를 인질 삼아 제후들을 다스렸다. 크고 작은 수십 번의 전투를 거치며 북방을 통일했고, 위나라의 기반을 다졌다."
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/vline2",
        "sliced-sprite",
        821.986f * scale,
        -78.64f * scale,
        457.0f * scale,
        2.0f * scale,
        assetId = "vline2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/label",
        "label",
        935.571f * scale,
        -131.04f * scale,
        229.83f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "인물 특기 일람"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/bg1/label",
        "label",
        835.986f * scale,
        -188.721f * scale,
        430.0f * scale,
        55.44f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = false,
        text = "없음"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/scrollview/view/content/vline2",
        "sliced-sprite",
        821.986f * scale,
        -192.48f * scale,
        457.0f * scale,
        2.0f * scale,
        assetId = "vline2",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = false,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/panel0/label",
        "label",
        850.941f * scale,
        76.326f * scale,
        399.09f * scale,
        50.4f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "출진 횟수 0 / 퇴각 횟수 0"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button5/Background",
        "sliced-sprite",
        822.696f * scale,
        17.207f * scale,
        170.0f * scale,
        50.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button5/Background/Label",
        "label",
        828.196f * scale,
        25.207f * scale,
        159.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "이전 무장"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button6/Background",
        "sliced-sprite",
        996.696f * scale,
        17.207f * scale,
        170.0f * scale,
        50.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button6/Background/Label",
        "label",
        996.696f * scale,
        25.207f * scale,
        170.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "다음 무장"
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button7/Background",
        "sliced-sprite",
        1169.696f * scale,
        17.207f * scale,
        110.0f * scale,
        50.0f * scale,
        assetId = "box3",
        opacity = 1.0f,
        blend = listOf(770, 771),
        visible = true,
        text = ""
    )
    log.draw(
        phase,
        "UnitInfoLayer",
        "Canvas/Layer/bg1/button7/Background/Label",
        "label",
        1174.696f * scale,
        25.207f * scale,
        100.0f * scale,
        40.0f * scale,
        assetId = null,
        opacity = 1.0f,
        blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
        visible = true,
        text = "확인"
    )
}

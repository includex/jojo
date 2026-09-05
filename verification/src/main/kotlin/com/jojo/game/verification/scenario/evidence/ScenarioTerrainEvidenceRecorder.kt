package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** Records the source-authored, state-free TerrainLayer traversal. */
internal class ScenarioTerrainEvidenceRecorder {
    fun append(log: RenderEventLog, view: ScenarioStaticHallEvidenceView) {
        check(view.kind == ScenarioStaticHallEvidenceKind.TERRAIN)
        val scale = .86f
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", visible: Boolean = true,
        ) = log.draw(
            "hall-terrain-stable", "TerrainLayer", path, type,
            x * scale, y * scale, w * scale, h * scale, asset,
            blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
            visible = visible, text = text,
        )
        fun sprite(
            path: String, x: Float, y: Float, w: Float, h: Float, asset: String,
            visible: Boolean = true, sliced: Boolean = false,
        ) = draw(path, if (sliced) "sliced-sprite" else "sprite", x, y, w, h, asset, visible = visible)
        fun label(path: String, value: String, x: Float, y: Float, w: Float, h: Float, visible: Boolean = true) =
            draw(path, "label", x, y, w, h, text = value, visible = visible)

        draw("Canvas/Layer/bg", "tiled-sprite", 274.236f, 100f, 1021.1f, 600f, "Logo_9-1")
        sprite("Canvas/Layer/bg/box1", 274.236f, 100f, 1021.1f, 600f, "box1", sliced = true)
        sprite("Canvas/Layer/bg/bg1", 274.236f, 650f, 1021.1f, 50f, "bg1")
        label("Canvas/Layer/bg/bg1/label", "지형 정보 일람", 282.086f, 649.8f, 229.83f, 50.4f)
        sprite("Canvas/Layer/bg/panel", 285.538f, 183.098f, 1001.1f, 459.3f, "box4", sliced = true)

        val names = listOf(
            "평원", "초원", "숲", "황지", "산지", "암산", "절벽", "설원", "다리", "얕은 물가", "늪지대", "연못", "작은 강", "대하",
            "울타리", "성벽", "성내", "성문", "성채", "관문", "사슴덧", "마을", "병영", "민가", "보물 창고", "연못", "화염", "배",
        )
        val nameWidths = floatArrayOf(
            62.28f, 62.28f, 31.14f, 62.28f, 62.28f, 62.28f, 62.28f, 62.28f, 62.28f,
            134.56f, 93.42f, 62.28f, 103.42f, 62.28f, 93.42f, 62.28f, 62.28f, 62.28f, 62.28f, 62.28f,
            93.42f, 62.28f, 62.28f, 62.28f, 134.56f, 62.28f, 62.28f, 31.14f,
        )
        val terrainValues = listOf(
            "◎◎◎○○◎◎◎◎◎◎◎◎", "○◎◎○○◎◎◎◎◎◎○◎", "★◎◎★◎★◎○◎◎○★◎", "★◎◎★◎★◎○★★○★★",
            "★◎◎★★★★○★★○★★", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎★◎◎", "◎◎◎◎◎◎★★★◎★◎★",
            "○◎◎○○○◎◎◎◎◎○◎", "★◎◎★★★◎★◎◎★★○", "★◎◎★★★◎★★★★★○", "◎◎◎◎◎◎◎◎◎◎◎◎◎",
            "◎◎◎◎◎◎◎◎◎◎◎◎◎", "★◎◎★★◎○★★◎★★○", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎",
            "◎○○◎◎○◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "○○○○○○○○○○○○○", "○○○○○○○○○○○○○",
            "○○○○○○○○○○○○○", "○○○○○○○○○○○○○", "○○○○○○○○○○○○○", "◎○○★★◎○○○○○★○",
            "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎", "◎◎◎◎◎◎◎◎◎◎◎◎◎",
        )
        val valueX = floatArrayOf(
            516.463f, 576.463f, 636.463f, 696.463f, 756.463f, 816.463f, 875.463f,
            935.463f, 995.463f, 1055.463f, 1115.463f, 1175.463f, 1235.463f,
        )
        names.indices.forEach { row ->
            val even = row % 2 == 0
            val item = if (even) "item0" else "item1"
            val path = "Canvas/Layer/bg/panel/scrollview0/view/content/$item"
            val y = 527.398f - row * 75f
            val itemVisible = row <= 8
            val childVisible = row <= 7
            sprite(path, 289.538f, y, 993.1f, 75f, if (even) "885a69b4-08ed-4c78-8896-ffb04eb2bd20" else "bg2", itemVisible, sliced = true)
            val childX = if (even) 292.488f else 292.919f
            sprite("$path/icon", childX, y + 3.9f, 67.2f, 67.2f, row.toString(), childVisible)
            val nameX = if (even) 376.088f else 375.651f
            val nameY = y + if (even) 34.82f else 34.598f
            label("$path/label", names[row], nameX, nameY, nameWidths[row], 45.36f, itemVisible)
            repeat(4) { skill ->
                sprite("$path/skill/skill_$skill", 369.088f + skill * 33f, y + 6.5f, 30f, 30f, "${skill + 1}-1", childVisible)
            }
            terrainValues[row].forEachIndexed { column, symbol ->
                val narrow = symbol == '○'
                val x = valueX[column] + if (narrow) 6.525f else 0f
                val w = if (narrow) 30.2f else 43.25f
                val fractionalHeight = !even && column in setOf(1, 2, 4, 5, 7, 8, 10, 11, 12)
                label("$path/label$column", symbol.toString(), x, y + 6f, w, if (fractionalHeight) 63.001f else 63f, childVisible)
            }
        }
        listOf(
            505.588f, 564.788f, 624.288f, 684.788f, 745.288f, 804.788f, 865.488f, 924.888f,
            984.388f, 1044.488f, 1103.888f, 1103.888f, 1164.588f, 1223.588f,
        ).forEach { x -> sprite("Canvas/Layer/bg/panel/vline", x, 189.448f, 6f, 448.6f, "vline") }

        data class Header(val id: String, val x: Float, val y: Float, val text: String)
        val headers = listOf(
            Header("button", 285.588f, 602.358f, "이름"), Header("button0", 508.088f, 602.358f, "마왕"),
            Header("button1", 568.397f, 602.183f, "보병"), Header("button2", 628.116f, 602.183f, "기병"),
            Header("button3", 688.268f, 602.183f, "궁기"), Header("button4", 748.137f, 602.183f, "포차"),
            Header("button5", 808.101f, 602.183f, "무술"), Header("button11", 1167.145f, 602.358f, "무술"),
            Header("button10", 1107.125f, 602.183f, "포차"), Header("button9", 1047.297f, 602.183f, "궁기"),
            Header("button8", 987.145f, 602.183f, "기병"), Header("button7", 927.426f, 602.183f, "보병"),
            Header("button6", 867.443f, 602.183f, "군주"), Header("button12", 1227.088f, 602.358f, "무술"),
        )
        headers.forEachIndexed { index, h ->
            val width = if (index == 0) 223f else 60f
            val labelX = if (index == 0) 347.088f else h.x - 20f
            val path = "Canvas/Layer/bg/panel/${h.id}/Background"
            sprite(path, h.x, h.y, width, 40f, "box4", sliced = true)
            label("$path/Label", h.text, labelX, h.y, 100f, 40f)
        }
        val buttons = listOf(
            floatArrayOf(285.436f, 110.1f, 196.7f, 61.8f, 301.386f, 121f, 164.8f, 40f) to "지형 효과",
            floatArrayOf(491.436f, 109.4f, 222.7f, 63.2f, 498.186f, 116.2f, 209.2f, 49.6f) to "기동력 소모",
            floatArrayOf(1164.786f, 111f, 120f, 60f, 1174.786f, 121f, 100f, 40f) to "확인",
        )
        buttons.forEachIndexed { index, (g, value) ->
            val path = "Canvas/Layer/bg/button$index/Background"
            sprite(path, g[0], g[1], g[2], g[3], "box3", sliced = true)
            label("$path/Label", value, g[4], g[5], g[6], g[7])
        }
    }
}

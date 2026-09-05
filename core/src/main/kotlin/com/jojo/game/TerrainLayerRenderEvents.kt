package com.jojo.game
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** Deterministic draw inventory for the actual MenuLayer button6 terrain route. */
object TerrainLayerRenderEvents {
    private const val phase = "battle-terrain-layer"
    private const val layer = "TerrainLayer"
    private const val root = "Canvas/Layer/bg"
    private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `terrain` (`TerrainLayer`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(terrain: TerrainLayer): String {
        val log = RenderEventLog()
        val panel = terrain.select(TerrainLayer.Tab.RISE)
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", blend: Any = listOf(770, 771), opacity: Float = 1f
        ) =
            log.draw(
                phase, if (path == "Canvas/Layer/Panel_cancel") "HallLayer" else layer,
                path, type, x, y, w, h, asset, opacity, blend, true, text
            )

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(path: String, x: Float, y: Float, w: Float, h: Float, text: String) =
            draw(path, "label", x, y, w, h, text = text, blend = alphaBlend)

        draw("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .392f)
        draw(root, "tiled-sprite", 274.236f, 100f, 1021.1f, 600f, "Logo_9-1")
        draw("$root/box1", "sliced-sprite", 274.236f, 100f, 1021.1f, 600f, "box1")
        draw("$root/bg1", "sprite", 274.236f, 650f, 1021.1f, 50f, "bg1")
        label("$root/bg1/label", 282.086f, 649.8f, 229.83f, 50.4f, "지형 정보 일람")
        draw("$root/panel", "sliced-sprite", 285.538f, 183.098f, 1001.1f, 459.3f, "box4")

        panel.rows.take(9).forEachIndexed { rowIndex, row ->
            val even = rowIndex % 2 == 0
            val item = if (even) "item0" else "item1"
            val base = "$root/panel/scrollview0/view/content/$item"
            val rowY = 527.398f - rowIndex * 75f
            draw(
                base, "sliced-sprite", 289.538f, rowY, 993.1f, 75f,
                if (even) "885a69b4-08ed-4c78-8896-ffb04eb2bd20" else "bg2"
            )
            // The ninth item is clipped by the viewport; only its background
            // and partly intersecting terrain-name label submit a draw.
            if (rowIndex < 8) {
                draw(
                    "$base/icon",
                    "sprite",
                    if (even) 292.488f else 292.919f,
                    rowY + 3.9f,
                    67.2f,
                    67.2f,
                    row.iconIndex.toString()
                )
            }
            val nameWidth = 31.14f * row.terrainName.length
            label(
                "$base/label", if (even) 376.088f else 375.651f,
                rowY + if (even) 34.82f else 34.598f, nameWidth, 45.36f, row.terrainName
            )
            if (rowIndex >= 8) return@forEachIndexed
            row.enabledSkills.forEachIndexed { index, _ ->
                draw(
                    "$base/skill/skill_$index",
                    "sprite",
                    369.088f + index * 33f,
                    rowY + 6.5f,
                    30f,
                    30f,
                    "${index + 1}-1"
                )
            }
            row.values.forEachIndexed { index, value ->
                val narrow = value.text == "○"
                val width = if (narrow) 30.2f else 43.25f
                // Authored columns 6..12 begin one pixel left of the naïve
                // 60px progression (the prefab has a 59px centre step there).
                val columnCorrection = if (index >= 6) -1f else 0f
                val glyphCorrection = if (narrow) 6.525f else 0f
                val x = 516.463f + index * 60f + columnCorrection + glyphCorrection
                val height = if (!even && (index % 3 != 0 || index == 12)) 63.001f else 63f
                label("$base/label$index", x, rowY + 6f, width, height, value.text)
            }
        }

        val lineXs = listOf(
            505.588f, 564.788f, 624.288f, 684.788f, 745.288f, 804.788f,
            865.488f, 924.888f, 984.388f, 1044.488f, 1103.888f, 1103.888f, 1164.588f, 1223.588f
        )
        lineXs.forEach { draw("$root/panel/vline", "sprite", it, 189.448f, 6f, 448.6f, "vline") }

        val headers = listOf(
            Triple("button", 285.588f, "이름"), Triple("button0", 508.088f, "마왕"),
            Triple("button1", 568.397f, "보병"), Triple("button2", 628.116f, "기병"),
            Triple("button3", 688.268f, "궁기"), Triple("button4", 748.137f, "포차"),
            Triple("button5", 808.101f, "무술"), Triple("button11", 1167.145f, "무술"),
            Triple("button10", 1107.125f, "포차"), Triple("button9", 1047.297f, "궁기"),
            Triple("button8", 987.145f, "기병"), Triple("button7", 927.426f, "보병"),
            Triple("button6", 867.443f, "군주"), Triple("button12", 1227.088f, "무술"),
        )
        headers.forEachIndexed { index, (name, x, text) ->
            val y = if (index in setOf(0, 1, 7, 13)) 602.358f else 602.183f
            val width = if (name == "button") 223f else 60f
            draw("$root/panel/$name/Background", "sliced-sprite", x, y, width, 40f, "box4")
            label(
                "$root/panel/$name/Background/Label",
                if (name == "button") x + 61.5f else x - 20f,
                y,
                100f,
                40f,
                text
            )
        }
        val footer = listOf(
            listOf("button0", "지형 효과", 285.436f, 110.1f, 196.7f, 61.8f, 301.386f, 121f, 164.8f, 40f),
            listOf("button1", "기동력 소모", 491.436f, 109.4f, 222.7f, 63.2f, 498.186f, 116.2f, 209.2f, 49.6f),
            listOf("button2", "확인", 1164.786f, 111f, 120f, 60f, 1174.786f, 121f, 100f, 40f),
        )
        footer.forEach { values ->
            val name = values[0] as String
            val text = values[1] as String
            draw(
                "$root/$name/Background",
                "sliced-sprite",
                values[2] as Float,
                values[3] as Float,
                values[4] as Float,
                values[5] as Float,
                "box3"
            )
            label(
                "$root/$name/Background/Label",
                values[6] as Float,
                values[7] as Float,
                values[8] as Float,
                values[9] as Float,
                text
            )
        }
        return log.jsonl()
    }
}

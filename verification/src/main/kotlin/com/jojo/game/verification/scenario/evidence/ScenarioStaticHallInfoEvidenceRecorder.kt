package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** Static source traversals that intentionally have no mutable screen input. */
internal class ScenarioStaticHallInfoEvidenceRecorder {
    fun appendForces(log: RenderEventLog) {
        val scale = .86f

        /**
         * 공개 메서드 `draw`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String = ""`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun draw(path: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") {
            val type = when {
                text.isNotEmpty() -> "label"
                asset == "Logo_9-1" -> "tiled-sprite"
                asset in setOf(
                    "box1",
                    "box2",
                    "box3",
                    "box4",
                    "bg2",
                    "885a69b4-08ed-4c78-8896-ffb04eb2bd20"
                ) -> "sliced-sprite"

                else -> "sprite"
            }
            log.draw(
                "hall-forces-stable",
                "ForcesListLayer",
                path,
                type,
                x * scale,
                y * scale,
                w * scale,
                h * scale,
                asset,
                blend = if (text.isEmpty()) listOf(770, 771) else listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
                text = text
            )
        }
        draw("Canvas/Layer/bg1", 165.686f, 79.5f, 1157f, 641f, "box3")
        draw("Canvas/Layer/bg1/bg1", 165.686f, 79.5f, 1157f, 641f, "Logo_9-1")
        draw("Canvas/Layer/bg1/bg1/bg1", 165.686f, 670.5f, 1157f, 50f, "bg1")
        draw("Canvas/Layer/bg1/bg1/bg1/label", 629.271f, 670.3f, 229.83f, 50.4f, text = "부대 정보 일람")
        draw("Canvas/Layer/bg1/bg1/bg1", 165.686f, 79.5f, 1157f, 641f, "box1")
        draw("Canvas/Layer/bg1/bg1/box2", 169.686f, 139.5f, 1149f, 527f, "box2")

        val rows = listOf(
            listOf("조조", "군웅", "3", "123/123", "36/36", "60", "68", "55", "49", "51"),
            listOf("허자장", "풍수사", "3", "115/115", "112/112", "33", "41", "49", "36", "36"),
            listOf("병사 ", "경보병", "3", "127/127", "103/103", "46", "49", "36", "36", "36"),
        )
        val labelX = floatArrayOf(
            180.186f,
            315.186f,
            486.286f,
            584.136f,
            739.486f,
            842.786f,
            939.786f,
            1035.786f,
            1132.786f,
            1228.786f
        )
        val labelW = floatArrayOf(120f, 160f, 86.8f, 147.3f, 92.2f, 82.8f, 82.8f, 82.8f, 82.8f, 82.8f)
        rows.forEachIndexed { row, values ->
            val item = if (row % 2 == 0) "item0" else "item1"
            val path = "Canvas/Layer/bg1/bg1/box2/scrollview/view/content/$item"
            val y = 544.85f - row * 62f
            draw(path, 171.686f, y, 1145f, 60f, if (row % 2 == 0) "bg2" else "885a69b4-08ed-4c78-8896-ffb04eb2bd20")
            values.forEachIndexed { column, value ->
                draw("$path/label$column", labelX[column], y + 4.8f, labelW[column], 50.4f, text = value)
            }
        }

        val headers = listOf(
            floatArrayOf(172.286f, 605.5f, 133.8f, 60f, 187.286f, 610.3f, 103.8f) to "무장명",
            floatArrayOf(307.016f, 605.5f, 174.7f, 60f, 319.611f, 610.3f, 149.51f) to "부대 속성",
            floatArrayOf(482.42f, 605.5f, 96.1f, 60f, 495.87f, 610.3f, 69.2f) to "레벨",
            floatArrayOf(579.086f, 606f, 157f, 60f, 622.986f, 610.8f, 69.2f) to "체력",
            floatArrayOf(736.236f, 606f, 99.9f, 60f, 751.586f, 610.8f, 69.2f) to "체력",
            floatArrayOf(836.136f, 606f, 96.1f, 60f, 849.586f, 610.8f, 69.2f) to "공격",
            floatArrayOf(933.136f, 606f, 96.1f, 60f, 946.586f, 610.8f, 69.2f) to "방어",
            floatArrayOf(1029.136f, 606f, 96.1f, 60f, 1042.586f, 610.8f, 69.2f) to "정신",
            floatArrayOf(1126.136f, 606f, 96.1f, 60f, 1139.586f, 610.8f, 69.2f) to "폭발",
            floatArrayOf(1222.136f, 606f, 96.1f, 60f, 1235.586f, 610.8f, 69.2f) to "사기",
        )
        headers.forEach { (g, value) ->
            draw("Canvas/Layer/bg1/bg1/box2/box3", g[0], g[1], g[2], g[3], "box3")
            draw("Canvas/Layer/bg1/bg1/box2/box3/label", g[4], g[5], g[6], 50.4f, text = value)
        }
        listOf(
            304.213f,
            477.727f,
            575.217f,
            733.131f,
            833.195f,
            928.965f,
            1025.903f,
            1122.841f,
            1218.611f
        ).forEach { x ->
            draw("Canvas/Layer/bg1/bg1/box2/vline", x, 141.345f, 6f, 464.25f, "vline")
        }
        draw("Canvas/Layer/bg1/button0/Background", 1129.071f, 85.823f, 180f, 50f, "box3")
        draw("Canvas/Layer/bg1/button0/Background/Label", 1169.071f, 90.823f, 100f, 40f, text = "폐쇄")
    }

    fun appendHelper(log: RenderEventLog) = ScenarioHelperEventWriter(log).append()
}

// Game
package com.jojo.game.presentation.shared.overlay
import com.jojo.game.presentation.shared.overlay.*
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/**
 * TerrainLayerRenderEvents: 지형 메뉴 경로에서 결정적으로 생성되는 그리기 항목이다.
 *
 * 좌표는 하나도 직접 적지 않는다. 모든 상자는 `TerrainLayerChromeRenderContract`에서 읽으며,
 * 같은 계약을 화면 그리기 `BattleTerrainOverlayRenderer`도 읽는다. 그래서 계약의 숫자를 고치면
 * 증거와 그림이 같이 움직이고, 둘이 따로 어긋날 자리가 남지 않는다.
 */
object TerrainLayerRenderEvents {
    /**
     * `phase` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val phase = "battle-terrain-layer"
    /**
     * `layer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val layer = "TerrainLayer"
    /**
     * `root` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val root = "Canvas/Layer/bg"
    /**
     * `alphaBlend` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
    /**
     * `chrome` (상태 값): 이 경로의 모든 상자를 들고 있는 단일 출처다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val chrome = TerrainLayerChromeRenderContract


    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun jsonl(terrain: TerrainLayer): String {
        val log = RenderEventLog()
        val panel = terrain.select(TerrainLayer.Tab.RISE)
        /**
         * `draw`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", blend: Any = listOf(770, 771), opacity: Float = 1f,
            color: String? = null
        ) =
            log.draw(
                phase, if (path == "Canvas/Layer/Panel_cancel") "HallLayer" else layer,
                path, type, x, y, w, h, asset, opacity, blend, true, text, color
            )

        /**
         * `drawBox`: 계약이 들고 있는 상자를 그대로 그리기 항목으로 옮긴다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun drawBox(
            path: String, type: String, box: TerrainLayerChromeRenderContract.Box,
            asset: String? = null, opacity: Float = 1f
        ) = draw(path, type, box.x, box.y, box.width, box.height, asset, opacity = opacity)

        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun label(path: String, x: Float, y: Float, w: Float, h: Float, text: String, color: String) =
            draw(path, "label", x, y, w, h, text = text, blend = alphaBlend, color = color)

        /**
         * `labelBox`: 계약이 들고 있는 문구 상자를 그대로 그리기 항목으로 옮긴다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun labelBox(
            path: String, box: TerrainLayerChromeRenderContract.Box, text: String,
            // 그리기 쪽 `BattleTerrainOverlayRenderer`가 글꼴에 넣는 것과 같은 계약이다.
            color: String = chrome.HEADER_COLOR,
        ) = label(path, box.x, box.y, box.width, box.height, text, color)

        drawBox(
            "Canvas/Layer/Panel_cancel", "sprite", chrome.dimmer,
            "default_sprite_splash", opacity = chrome.DIMMER_OPACITY
        )
        drawBox(root, "tiled-sprite", chrome.outerBox.toBox(), "Logo_9-1")
        drawBox("$root/box1", "sliced-sprite", chrome.outerBox.toBox(), "box1")
        drawBox("$root/bg1", "sprite", chrome.titleStrip.toBox(), "bg1")
        labelBox("$root/bg1/label", chrome.titleLabel, chrome.TITLE_TEXT)
        drawBox("$root/panel", "sliced-sprite", chrome.panelBox, "box4")

        panel.rows.take(chrome.ROW_COUNT).forEachIndexed { rowIndex, row ->
            val even = rowIndex % 2 == 0
            val item = if (even) "item0" else "item1"
            val base = "$root/panel/scrollview0/view/content/$item"
            drawBox(
                base, "sliced-sprite", chrome.rowBox(rowIndex),
                if (even) "885a69b4-08ed-4c78-8896-ffb04eb2bd20" else "bg2"
            )
            // 아홉 번째 항목은 뷰포트에 잘려 배경과 일부 겹치는 지형 이름만 그린다.
            if (rowIndex < chrome.ROW_ICON_COUNT) {
                drawBox("$base/icon", "sprite", chrome.rowIconBox(rowIndex), row.iconIndex.toString())
            }
            labelBox(
                "$base/label", chrome.rowNameBox(rowIndex, row.terrainName.length), row.terrainName,
                chrome.ROW_NAME_COLOR,
            )
            if (rowIndex >= chrome.ROW_ICON_COUNT) return@forEachIndexed
            row.enabledSkills.forEachIndexed { index, _ ->
                drawBox("$base/skill/skill_$index", "sprite", chrome.skillBox(rowIndex, index), "${index + 1}-1")
            }
            row.values.forEachIndexed { index, value ->
                labelBox(
                    "$base/label$index", chrome.valueBox(rowIndex, index, value.text), value.text,
                    chrome.riseColor(value.grade),
                )
            }
        }

        chrome.verticalLineXs.forEach {
            drawBox("$root/panel/vline", "sprite", chrome.verticalLineBox(it), "vline")
        }

        chrome.headers.forEach { header ->
            drawBox("$root/panel/${header.node}/Background", "sliced-sprite", header.box, "box4")
            labelBox("$root/panel/${header.node}/Background/Label", header.labelBox, header.text)
        }
        chrome.footerButtons.forEach { button ->
            drawBox("$root/${button.node}/Background", "sliced-sprite", button.box, "box3")
            labelBox("$root/${button.node}/Background/Label", button.labelBox, button.text)
        }
        return log.jsonl()
    }

    /** 장식 조각을 상자로 본다: 9-패치도 기록에는 좌표와 크기만 남는다. */
    private fun TerrainLayerChromeRenderContract.Patch.toBox() =
        TerrainLayerChromeRenderContract.Box(x, y, width, height)
}

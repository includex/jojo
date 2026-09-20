// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.shared.overlay.TerrainLayerChromeRenderContract

/** 전투 지형 병과 값 표시 정보: 병과별 수치 문자열과 등급 색상 인덱스를 정의한다. */
data class BattleTerrainValueView(val text: String, val grade: Int?)

/** 전투 지형 행 표시 정보: 지형 이름·아이콘·사용 가능 기술·병과별 효과 값을 정의한다. */
data class BattleTerrainRowView(
    val terrainName: String,
    val icon: Texture?,
    val enabledSkills: List<Boolean>,
    val values: List<BattleTerrainValueView>,
)

/** 전투 지형 목록 표시 정보: 병과 열 제목과 지형 효과 행 목록을 정의한다. */
data class BattleTerrainOverlayView(
    val armNames: List<String>,
    val rows: List<BattleTerrainRowView>,
)

/** 전투 지형 목록 자산: 바탕, 패널, 행, 열 구분선을 그릴 그래픽을 보관한다. */
data class BattleTerrainOverlayAssets(
    val background: Texture?,
    val panel: NinePatch?,
    val rowEven: NinePatch?,
    val rowOdd: NinePatch?,
    val verticalLine: NinePatch?,
    /** 특기 아이콘 네 개와 그 회색판이다. 원본은 같은 스프라이트에 material만 갈아끼운다. */
    val skillIcons: List<Texture?> = emptyList(),
    val skillDisabledIcons: List<Texture?> = emptyList(),
)

/**
 * 전투 지형 목록 렌더러: 지형별 기술 가능 여부와 병과 효과를 등급 색상으로 출력한다.
 *
 * 좌표는 하나도 직접 적지 않는다. 모든 상자는 `TerrainLayerChromeRenderContract`에서 읽으며,
 * 같은 계약을 증거 기록기 `TerrainLayerRenderEvents`도 읽는다. 예전에는 이 파일이 자기
 * `PANEL_X` 무리를 따로 들고 있어서 증거가 원본과 맞는 동안에도 화면만 몇 픽셀씩 어긋나 있었다.
 */
class BattleTerrainOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `font` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val font: BitmapFont,
    /** `assets` (BattleTerrainOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleTerrainOverlayAssets,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleTerrainOverlayView) {
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground()
        drawPatch(assets.panel, chrome.panelBox)
        chrome.footerButtons.forEach { drawPatch(assets.panel, it.box) }

        font.color = Color.BLACK
        setFontSize(chrome.TITLE_FONT_SIZE)
        drawText(chrome.TITLE_TEXT, chrome.titleLabel)
        setFontSize(chrome.FOOTER_FONT_SIZE)
        chrome.footerButtons.forEach { drawText(it.text, it.labelBox) }
        setFontSize(chrome.HEADER_FONT_SIZE)
        drawText(chrome.nameHeader.text, chrome.nameHeader.labelBox)
        view.armNames.forEachIndexed { index, name ->
            chrome.armHeaders.getOrNull(index)?.let { drawText(name, it.labelBox) }
        }

        view.rows.forEachIndexed { rowIndex, row -> drawRow(rowIndex, row) }
        chrome.verticalLineXs.forEach { drawPatch(assets.verticalLine, chrome.verticalLineBox(it)) }
        setFontSize(chrome.BASE_FONT_SIZE)
        font.color = Color.WHITE
        batch.color = Color.WHITE
        batch.end()
    }

    /**
     * `drawRow`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawRow(index: Int, row: BattleTerrainRowView) {
        val even = index % 2 == 0
        drawPatch(if (even) assets.rowEven else assets.rowOdd, chrome.rowBox(index))
        row.icon?.let {
            batch.color = Color.WHITE
            val box = chrome.rowIconBox(index)
            batch.draw(it, box.x, box.y, box.width, box.height)
        }
        setFontSize(chrome.ROW_NAME_FONT_SIZE)
        // 원본은 이름 라벨의 노드 색을 건드리지 않아 프리팹 기본 검정이다.
        font.color = Color.valueOf("${chrome.ROW_NAME_COLOR}ff")
        drawText(row.terrainName, chrome.rowNameBox(index, row.terrainName.length))
        setFontSize(chrome.VALUE_FONT_SIZE)
        // 원본 `TerrainLayer.js:109`는 `skill/skill_0..3` 스프라이트에 회색 material을 씌운다.
        // 글자가 아니라 30×30 아이콘이며 노드 색은 늘 흰색이다.
        batch.color = Color.WHITE
        row.enabledSkills.forEachIndexed { bit, enabled ->
            val icons = if (enabled) assets.skillIcons else assets.skillDisabledIcons
            icons.getOrNull(bit)?.let {
                val box = chrome.skillBox(index, bit)
                batch.draw(it, box.x, box.y, box.width, box.height)
            }
        }
        row.values.forEachIndexed { armIndex, value ->
            // 등급별 색은 원본 `TerrainLayer.js:110`의 목록을 그대로 든 계약에서 읽는다.
            font.color = Color.valueOf("${chrome.riseColor(value.grade)}ff")
            drawText(value.text, chrome.valueBox(index, armIndex, value.text))
        }
    }

    /**
     * `drawTiledBackground`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawTiledBackground() {
        assets.background?.let { texture ->
            // 원본은 `bg`를 타일 스프라이트로 깔므로 한 칸은 무늬 자체의 크기다.
            val tileWidth = texture.width.toFloat()
            val tileHeight = texture.height.toFloat()
            val right = chrome.PANEL_X + chrome.PANEL_WIDTH
            val top = chrome.PANEL_Y + chrome.PANEL_HEIGHT
            var y = chrome.PANEL_Y
            while (y < top) {
                var x = chrome.PANEL_X
                while (x < right) {
                    batch.draw(texture, x, y, minOf(tileWidth, right - x), minOf(tileHeight, top - y))
                    x += tileWidth
                }
                y += tileHeight
            }
        }
    }

    /**
     * `drawPatch`: 계약이 들고 있는 상자에 9-패치를 그린다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawPatch(patch: NinePatch?, box: TerrainLayerChromeRenderContract.Box) {
        patch?.draw(batch, box.x, box.y, box.width, box.height)
    }

    /**
     * `drawText`: 계약이 들고 있는 문구 상자에 글자를 그린다.
     * 원본 라벨 노드의 윗변이 비트맵 글꼴의 기준선과 같은 자리라 상자의 top을 쓴다.
     */

    private fun drawText(text: String, box: TerrainLayerChromeRenderContract.Box) {
        font.draw(batch, text, box.x, box.top)
    }

    /**
     * `setFontSize`: 계약이 적어 둔 원본 글자 크기를 이식본 글꼴 배율로 바꾼다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun setFontSize(size: Float) {
        font.data.setScale(size / chrome.BASE_FONT_SIZE)
    }

    private companion object {
        /**
         * `chrome` (상태 값): 이 화면 기하의 단일 출처이며 증거 기록기도 같은 계약을 읽는다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val chrome = TerrainLayerChromeRenderContract
    }
}

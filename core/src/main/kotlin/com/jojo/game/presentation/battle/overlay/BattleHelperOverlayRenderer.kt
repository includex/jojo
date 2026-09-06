// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** 전투 도움말 표시 정보: 서식 있는 본문과 닫기 버튼 문구를 정의한다. */
data class BattleHelperOverlayView(val richText: String, val buttonText: String)

/** 전투 도움말 자산: 문서 바탕, 제목 막대, 스크롤 패널을 그릴 그래픽을 보관한다. */
data class BattleHelperOverlayAssets(
    val background: Texture?,
    val header: NinePatch?,
    val scroll: NinePatch?,
)

/** 전투 도움말 렌더러: 색상 태그가 포함된 역사 정보를 줄바꿈 규칙에 맞춰 출력한다. */
class BattleHelperOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `font` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val font: BitmapFont,
    /** `glyphLayout` (GlyphLayout): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val glyphLayout: GlyphLayout,
    /** `assets` (BattleHelperOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleHelperOverlayAssets,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleHelperOverlayView) {
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground()
        assets.header?.draw(batch, 147.686f, 715.5f, 1193f, 60f)
        assets.scroll?.draw(batch, 163.686f, 99f, 1161f, 616f)
        font.color = Color(0.56f, 0f, 0.62f, 1f)
        font.data.setScale(40f / 26f)
        font.draw(batch, "역사 정보", 160f, 760f)
        drawRichText(view.richText, 165.686f, 690f, 1157f)
        batch.end()

        batch.begin()
        assets.header?.draw(batch, 1172.451f, 33.187f, 147.6f, 56f)
        font.color = Color.BLACK
        font.data.setScale(40f / 26f)
        font.draw(batch, view.buttonText, 1204f, 71f)
        font.data.setScale(1f)
        font.color = Color.WHITE
        batch.end()
    }

    /**
     * `drawRichText`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawRichText(richText: String, x: Float, topY: Float, width: Float) {
        /**
         * `Run`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Run(val text: String, val color: Color)

        val lines = mutableListOf<MutableList<Run>>()
        var line = mutableListOf<Run>()
        lines += line
        val colors = ArrayDeque<Color>().apply { addLast(Color.BLACK) }
        val tags = Regex("<color=(#[0-9a-fA-F]{6})>|</color>|<br\\s*/?>")
        var cursor = 0

        /**
         * `append`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun append(value: String) {
            value.replace("&amp;", "&").split('\n').forEachIndexed { index, piece ->
                if (piece.isNotEmpty()) line += Run(piece, Color(colors.last()))
                if (index < value.count { it == '\n' }) {
                    line = mutableListOf()
                    lines += line
                }
            }
        }

        tags.findAll(richText).forEach { match ->
            append(richText.substring(cursor, match.range.first))
            when {
                match.value.startsWith("<color=") -> colors.addLast(Color.valueOf(match.groupValues[1]))
                match.value == "</color>" && colors.size > 1 -> colors.removeLast()
                match.value.startsWith("<br") -> {
                    line = mutableListOf()
                    lines += line
                }
            }
            cursor = match.range.last + 1
        }
        append(richText.substring(cursor))

        font.data.setScale(40f / 26f)
        /**
         * `lineHeight` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val lineHeight = 50f
        /**
         * `lineIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var lineIndex = 0
        lines.forEach { runs ->
            /**
             * `pen` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            var pen = x
            runs.forEach { run ->
                font.color = run.color
                run.text.forEach { glyph ->
                    glyphLayout.setText(font, glyph.toString())
                    if (pen > x && pen + glyphLayout.width > x + width) {
                        lineIndex += 1
                        pen = x
                    }
                    if (lineIndex < 12) font.draw(batch, glyph.toString(), pen, topY - lineIndex * lineHeight)
                    pen += glyphLayout.width
                }
            }
            lineIndex += 1
        }
        font.color = Color.BLACK
    }

    /**
     * `drawTiledBackground`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawTiledBackground() {
        assets.background?.let { texture ->
            var y = 24.5f
            while (y < 775.5f) {
                var x = 147.686f
                while (x < 1340.686f) {
                    batch.draw(texture, x, y, minOf(96f, 1340.686f - x), minOf(96f, 775.5f - y))
                    x += 96f
                }
                y += 96f
            }
        }
    }
}

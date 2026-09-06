// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** 전투 소지품 행 표시 정보: 아이콘, 이름, 현재 선택 여부를 정의한다. */
data class BattlePropertyRowView(
    val icon: Texture?,
    val label: String,
    val selected: Boolean,
)

/** 전투 소지품 목록 표시 정보: 선택 분류, 첫 행, 표시할 소지품 행을 정의한다. */
data class BattlePropertyOverlayView(
    val selectedTab: Int,
    val firstRow: Int,
    val rows: List<BattlePropertyRowView>,
)

/** 전투 소지품 목록 자산: 바탕, 패널, 행, 열 구분선을 그릴 그래픽을 보관한다. */
data class BattlePropertyOverlayAssets(
    val background: Texture?,
    val panel: NinePatch?,
    val rowEven: NinePatch?,
    val rowOdd: NinePatch?,
    val verticalLine: NinePatch?,
)

/** 전투 소지품 목록 렌더러: 선택 분류와 스크롤 위치에 맞는 장비·아이템 행을 출력한다. */
class BattlePropertyOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `font` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val font: BitmapFont,
    /** `assets` (BattlePropertyOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattlePropertyOverlayAssets,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattlePropertyOverlayView) {
        val x = 247f
        val y = 48f
        val w = 994f
        val h = 706f
        val scale = 40f / 26f
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground(x, y, w, h)
        assets.panel?.draw(batch, 249f, 117f, 990f, 524f)
        listOf(
            floatArrayOf(251.2f, 637.9f, 376.9f),
            floatArrayOf(628.6f, 638f, 195.1f),
            floatArrayOf(824.7f, 638f, 106.9f),
            floatArrayOf(931.4f, 638f, 101.2f),
            floatArrayOf(1032f, 638f, 206.4f),
        ).forEach { (headerX, headerY, headerWidth) -> assets.panel?.draw(batch, headerX, headerY, headerWidth, 60f) }
        font.color = Color.BLACK
        font.data.setScale(scale)
        font.draw(batch, "창고 일람", x + 430f, 740f)
        listOf("이름" to 400f, "속성" to 687f, "레벨" to 838f, "경험치" to 930f, "소지자" to 1083f)
            .forEach { (label, position) -> font.draw(batch, label, position, 680f) }
        listOf(628.468f, 823.971f, 930.065f, 1032.026f)
            .forEach { lineX -> assets.verticalLine?.draw(batch, lineX, 122.75f, 6f, 515.38f) }
        view.rows.drop(view.firstRow).take(7).forEachIndexed { index, row ->
            val rowY = y + 540f - index * 72f
            (if (index % 2 == 0) assets.rowEven else assets.rowOdd)?.draw(batch, x + 9f, rowY - 59f, w - 18f, 72f)
            font.color = if (row.selected) Color(0.05f, .35f, .95f, 1f) else Color.BLACK
            row.icon?.let { batch.draw(it, x + 22f, rowY - 47f, 48f, 48f) }
            font.draw(batch, row.label, x + 86f, rowY)
        }
        font.data.setScale(scale)
        listOf("무기", "방어구", "보조", "아이템").forEachIndexed { index, label ->
            val active = index == view.selectedTab
            font.color = if (active) Color(0.05f, .48f, .94f, 1f) else Color(.2f, .2f, .2f, 1f)
            font.draw(batch, if (active) "●" else "○", x + 28f + index * 146f, y + 30f)
            font.color = Color.BLACK
            font.draw(batch, label, x + 56f + index * 146f, y + 30f)
        }
        assets.panel?.draw(batch, x + w - 158f, y + 10f, 140f, 54f)
        font.color = Color.BLACK
        font.draw(batch, "확인", x + w - 116f, y + 30f)
        font.color = Color.WHITE
        font.data.setScale(1f)
        batch.end()
    }

    /**
     * `drawTiledBackground`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawTiledBackground(x: Float, y: Float, width: Float, height: Float) {
        assets.background?.let { texture ->
            var ty = y
            while (ty < y + height) {
                var tx = x
                while (tx < x + width) {
                    batch.draw(texture, tx, ty, minOf(96f, x + width - tx), minOf(96f, y + height - ty))
                    tx += 96f
                }
                ty += 96f
            }
        }
    }
}

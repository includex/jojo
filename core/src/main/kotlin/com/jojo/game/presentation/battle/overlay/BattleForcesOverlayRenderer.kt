// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** 전투 부대 행 표시 정보: 부대 목록 한 줄의 열별 텍스트 값을 정의한다. */
data class BattleForcesRowView(val values: List<String>)

/** 전투 부대 목록 표시 정보: 선택 진영 탭, 목록 행, 탭 노출 여부를 정의한다. */
data class BattleForcesOverlayView(
    val selectedTab: Int,
    val rows: List<BattleForcesRowView>,
    val tabsVisible: Boolean,
)

/** 전투 부대 목록 자산: 바탕, 패널, 행, 열 구분선을 그릴 그래픽을 보관한다. */
data class BattleForcesOverlayAssets(
    val background: Texture?,
    val panel: NinePatch?,
    val rowEven: NinePatch?,
    val rowOdd: NinePatch?,
    val verticalLine: NinePatch?,
)

/** 전투 부대 목록 렌더러: 확정된 부대 행과 선택 탭을 목록 레이아웃으로 출력한다. */
class BattleForcesOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `font` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val font: BitmapFont,
    /** `assets` (BattleForcesOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleForcesOverlayAssets,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleForcesOverlayView) {
        val x = 165.686f
        val y = 79.5f
        val w = 1157f
        val h = 641f
        val scale = 40f / 26f
        val offsets = listOf(25f, 160f, 325f, 405f, 555f, 665f, 760f, 855f, 950f, 1045f)
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground(x, y, w, h)
        assets.panel?.draw(batch, 170f, 139.5f, 1149f, 527f)
        font.color = Color.BLACK
        font.data.setScale(scale)
        font.draw(batch, "부대 정보 일람", x + 455f, 706f)
        listOf("무장명", "부대 속성", "레벨", "체력", "체력", "공격", "방어", "정신", "폭발", "사기")
            .forEachIndexed { index, label -> font.draw(batch, label, x + offsets[index], 646f) }
        listOf(130f, 285f, 390f, 540f, 645f, 745f, 840f, 935f, 1030f)
            .forEach { assets.verticalLine?.draw(batch, x + it, 139.5f, 6f, 527f) }
        view.rows.take(5).forEachIndexed { index, row ->
            val rowY = 574.85f - index * 62f
            (if (index % 2 == 0) assets.rowEven else assets.rowOdd)?.draw(batch, 171.5f, rowY - 30f, 1145f, 60f)
            font.color = Color.BLACK
            font.data.setScale(scale)
            row.values.take(offsets.size).forEachIndexed { column, value ->
                font.draw(batch, value, x + offsets[column], rowY + 10f)
            }
        }
        if (view.tabsVisible) {
            font.data.setScale(.7f)
            font.color = if (view.selectedTab == 0) Color(0.05f, .48f, .94f, 1f) else Color.DARK_GRAY
            font.draw(batch, if (view.selectedTab == 0) "● 아군" else "○ 아군", x + 72f, y + 28f)
            font.color = if (view.selectedTab == 1) Color(0.05f, .48f, .94f, 1f) else Color.DARK_GRAY
            font.draw(batch, if (view.selectedTab == 1) "● 적군" else "○ 적군", x + 220f, y + 28f)
        }
        assets.panel?.draw(batch, x + w - 185f, y + 10f, 170f, 55f)
        font.color = Color.BLACK
        font.draw(batch, "폐쇄", x + w - 130f, y + 30f)
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

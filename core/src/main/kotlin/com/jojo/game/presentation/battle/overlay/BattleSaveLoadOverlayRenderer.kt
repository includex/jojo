// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.i18n.GameText
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** 저장·불러오기 목록을 소유하는 모달 종류입니다. */
enum class BattleSaveLoadOverlayKind { SAVE, LOAD }

/** 저장·불러오기 목록 한 줄의 표시 정보입니다. */
data class BattleSaveLoadRowView(
    val number: String,
    val stage: String,
    val name: String,
)

/** 저장·불러오기 렌더러가 소비하는 불변 화면 상태입니다. */
data class BattleSaveLoadOverlayView(
    val kind: BattleSaveLoadOverlayKind,
    val rows: List<BattleSaveLoadRowView>,
    val firstRow: Int,
    val pendingSave: Boolean = false,
    val saveConfirmation: String? = null,
    val saveCompletionTip: Boolean = false,
    val loadConfirmation: String? = null,
    val loadNotice: String? = null,
)

/** BattleSaveLoadOverlayAssets: 전투 화면 표시에 사용할 이미지와 자원 경로를 보관한다. */
data class BattleSaveLoadOverlayAssets(
    val background: Texture?,
    val panel: NinePatch?,
    val rowEven: NinePatch?,
    val rowOdd: NinePatch?,
)

/** 저장·불러오기 모달을 공통 슬롯 목록으로 그립니다. */
class BattleSaveLoadOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `font` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val font: BitmapFont,
    /** `assets` (BattleSaveLoadOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleSaveLoadOverlayAssets,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleSaveLoadOverlayView) {
        batch.begin()
        batch.color = Color.WHITE
        when (view.kind) {
            BattleSaveLoadOverlayKind.SAVE -> drawSave(view)
            BattleSaveLoadOverlayKind.LOAD -> drawLoad(view)
        }
        font.color = Color.WHITE
        font.data.setScale(1f)
        batch.end()
    }

    /**
     * `drawSave`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawSave(view: BattleSaveLoadOverlayView) {
        val x = 278f
        val y = 83f
        val w = 932f
        val h = 634f
        drawTiledBackground(x, y, w, h)
        assets.panel?.draw(batch, 287f, 173f, 912f, 428f)
        font.color = Color.BLACK
        font.data.setScale(40f / 26f)
        font.draw(batch, GameText.S_158191141D, 288f, 703f)
        font.draw(batch, GameText.S_591250DCC4, 288f, 651f)
        drawRows(view.rows, view.firstRow, 8, y + 505f)
        font.draw(batch, GameText.S_E1864601EE, 130f, 143f)
        assets.panel?.draw(batch, 1046f, 104f, 148f, 56f)
        font.draw(batch, GameText.S_19B2D19BC1, 1080f, 132f)
        if (view.pendingSave) {
            font.draw(batch, view.saveConfirmation ?: GameText.S_DDA4595886, 500f, 430f)
            font.draw(batch, GameText.S_1F1712ACFF, 620f, 320f)
            font.draw(batch, GameText.S_2DD57A2739, 820f, 320f)
        }
        if (view.saveCompletionTip) {
            font.draw(batch, GameText.S_5AB5BF64DD, 680f, 430f)
            font.draw(batch, GameText.S_468266D639, 620f, 320f)
        }
    }

    /**
     * `drawLoad`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawLoad(view: BattleSaveLoadOverlayView) {
        val x = 278f
        val y = 97.5f
        val w = 932f
        val h = 605f
        drawTiledBackground(x, y, w, h)
        assets.panel?.draw(batch, 287f, 174f, 912f, 428f)
        font.color = Color.BLACK
        font.data.setScale(40f / 26f)
        font.draw(batch, GameText.S_8FB0968760, 288f, 688f)
        font.draw(batch, GameText.S_B53B4895CF, 288f, 640f)
        drawRows(view.rows, view.firstRow, 8, 574f)
        assets.panel?.draw(batch, 1051f, 110f, 148f, 60f)
        font.draw(batch, GameText.S_19B2D19BC1, 1082f, 148f)
        view.loadNotice?.let {
            font.color = Color(.8f, .15f, .15f, 1f)
            font.draw(batch, it, 500f, 240f)
        }
        view.loadConfirmation?.let {
            font.color = Color.BLACK
            font.draw(batch, it, 500f, 430f)
            font.draw(batch, GameText.S_F01A5A5229, 590f, 320f)
            font.draw(batch, GameText.S_19B2D19BC1, 820f, 320f)
        }
    }

    /**
     * `drawRows`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawRows(rows: List<BattleSaveLoadRowView>, firstRow: Int, count: Int, firstBaseline: Float) {
        rows.drop(firstRow).take(count).forEachIndexed { index, row ->
            val rowY = firstBaseline - index * 52f
            (if (index % 2 == 0) assets.rowEven else assets.rowOdd)?.draw(batch, 289f, rowY - 42f, 908f, 52f)
            font.color = Color.BLACK
            font.draw(batch, row.number, 295f, rowY)
            font.draw(batch, row.stage, 478f, rowY)
            font.draw(batch, row.name, 578f, rowY)
        }
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

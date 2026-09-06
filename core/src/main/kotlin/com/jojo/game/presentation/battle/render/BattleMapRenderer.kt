// Battle
package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** 전투 화면이 그리기 전에 투영한 맵 오버레이 상태입니다. */
data class BattleMapView(
    val boardLeft: Float,
    val boardBottom: Float,
    val tileSize: Float,
    val selectionTiles: List<BattleMapSelection>,
    val cursor: BattleMapCursor?,
    val terrainImpacts: List<BattleMapTerrainImpact>,
    val harmNumbers: List<BattleMapHarmNumber>,
)
data class BattleMapSelection(val x: Int, val y: Int, val frame: String)
data class BattleMapCursor(val x: Int, val y: Int)
data class BattleMapTerrainImpact(val x: Int, val y: Int, val value: Int)
data class BattleMapHarmNumber(
    val x: Float,
    val y: Float,
    val amount: Int,
    val isHp: Boolean,
)

/** BattleMapRendererAssets: 선택 테두리와 커서를 그릴 때 사용하는 텍스처 묶음이다. */
data class BattleMapRendererAssets(
    val selectionTextures: Map<String, Texture?>,
    val cursorTexture: Texture?,
)

/** BattleMapRenderLayer: 선택 영역·커서·지형 영향·피해 숫자의 맵 그리기 순서를 구분한다. */
enum class BattleMapRenderLayer { SELECTION, CURSOR, TERRAIN_IMPACT, HARM }
/** BattleMapRenderEvent: 맵 오버레이 한 요소의 레이어·좌표·프레임·문자열을 전달한다. */
data class BattleMapRenderEvent(
    val layer: BattleMapRenderLayer,
    val x: Float,
    val y: Float,
    val frame: String? = null,
    val text: String? = null,
)

/** 전술 맵에 공통으로 사용하는 오버레이 그리기만 담당합니다. */
class BattleMapRenderer(
    private val batch: SpriteBatch,
    private val font: BitmapFont,
    private val assets: BattleMapRendererAssets,
) {
    /** 네 개 그리기 단계에서 사용하는 고정 명령 순서입니다. */
    companion object {
        private val TERRAIN_COLOR = Color(0.94f, 0.97f, 1f, 0.9f)
        private val MP_COLOR = Color(224f / 255f, 224f / 255f, 0f, 1f)

        fun orderedEvents(view: BattleMapView): List<BattleMapRenderEvent> = buildList {
            view.selectionTiles.forEach {
                add(
                    BattleMapRenderEvent(
                        BattleMapRenderLayer.SELECTION,
                        it.x.toFloat(),
                        it.y.toFloat(),
                        frame = it.frame
                    )
                )
            }
            view.cursor?.let { add(BattleMapRenderEvent(BattleMapRenderLayer.CURSOR, it.x.toFloat(), it.y.toFloat())) }
            view.terrainImpacts.forEach {
                add(
                    BattleMapRenderEvent(
                        BattleMapRenderLayer.TERRAIN_IMPACT,
                        it.x.toFloat(),
                        it.y.toFloat(),
                        text = it.value.toString()
                    )
                )
            }
            view.harmNumbers.forEach {
                add(
                    BattleMapRenderEvent(
                        BattleMapRenderLayer.HARM,
                        it.x,
                        it.y,
                        text = kotlin.math.abs(it.amount).toString()
                    )
                )
            }
        }
    }

    fun drawSelection(view: BattleMapView) {
        view.selectionTiles.forEach { tile ->
            assets.selectionTextures[tile.frame]?.let { texture ->
                batch.color = Color.WHITE
                batch.draw(
                    texture,
                    view.boardLeft + tile.x * view.tileSize,
                    tileBottom(view, tile.y),
                    view.tileSize,
                    view.tileSize
                )
            }
        }
        drawCursor(view)
        batch.color = Color.WHITE
    }

    fun drawTerrainImpacts(view: BattleMapView) {
        if (view.terrainImpacts.isEmpty()) return
        font.data.setScale(24f / 26f)
        font.color = TERRAIN_COLOR
        view.terrainImpacts.forEach { impact ->
            font.draw(
                batch,
                impact.value.toString(),
                view.boardLeft + impact.x * view.tileSize + 3f,
                tileBottom(view, impact.y) + 23f,
            )
        }
        font.data.setScale(1f)
        font.color = Color.WHITE
    }

    fun drawHarmNumbers(view: BattleMapView) {
        if (view.harmNumbers.isEmpty()) return
        font.data.setScale(0.5f)
        view.harmNumbers.forEach { harm ->
            font.color = if (harm.isHp) Color.WHITE else MP_COLOR
            font.draw(
                batch,
                kotlin.math.abs(harm.amount).toString(),
                view.boardLeft + harm.x * view.tileSize,
                tileBottom(view, harm.y) + view.tileSize + 24f,
            )
        }
        font.data.setScale(1f)
        font.color = Color.WHITE
    }

    private fun drawCursor(view: BattleMapView) {
        val cursor = view.cursor ?: return
        assets.cursorTexture?.let { texture ->
            batch.color = Color.WHITE
            batch.draw(
                texture,
                view.boardLeft + cursor.x * view.tileSize,
                tileBottom(view, cursor.y),
                view.tileSize,
                view.tileSize
            )
        }
    }

    private fun tileBottom(view: BattleMapView, y: Int): Float = view.boardBottom - y * view.tileSize
    private fun tileBottom(view: BattleMapView, y: Float): Float = view.boardBottom - y * view.tileSize
}

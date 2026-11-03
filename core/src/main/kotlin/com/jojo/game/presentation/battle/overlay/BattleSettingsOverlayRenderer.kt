// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.i18n.SystemMessage
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** 전투 설정 표시 정보: 음향·미니맵 등 비트 설정과 텍스트·안내·배경 선택 값을 정의한다. */
data class BattleSettingsOverlayView(
    val flags: Int,
    val msgSpeed: Int,
    val notifyLevel: Int,
    val background: Int,
)

/** 전투 설정 자산: 설정 창 패널과 배경 색상 견본을 그릴 그래픽을 보관한다. */
data class BattleSettingsOverlayAssets(
    val background: Texture?,
    val panel: NinePatch?,
)

/** 전투 설정 렌더러: 설정 비트와 선택 인덱스를 체크·라디오·색상 견본으로 출력한다. */
class BattleSettingsOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `font` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val font: BitmapFont,
    /** `assets` (BattleSettingsOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleSettingsOverlayAssets,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleSettingsOverlayView) {
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground()
        assets.panel?.draw(batch, 204f, 110f, 1081f, 596f)
        assets.panel?.draw(batch, 793f, 520f, 480f, 100f)
        assets.panel?.draw(batch, 793f, 256f, 480f, 100f)
        BattleOverlayPaper.frame(batch,793f,120f,480f,103f)

        font.color = Color.BLACK
        font.data.setScale(1f)
        font.draw(batch, SystemMessage.S_B6D9331463, 201f, 748f)
        font.draw(batch, SystemMessage.S_8277D3ABFB, 205f, 690f)
        val options = listOf(
            SystemMessage.S_F209C5D3A3 to 0,
            SystemMessage.S_8F62514DB8 to 1,
            SystemMessage.S_CF7DC571E7 to 2,
            SystemMessage.S_3424D32FC7 to 3,
            SystemMessage.S_077AE5CBAB to 4,
        )
        options.forEachIndexed { index, (label, bit) ->
            val enabled = view.flags and (1 shl bit) != 0
            font.color = if (enabled) Color(.28f,.42f,.29f,1f) else Color.DARK_GRAY
            val boxY=621f-index*65f
            BattleOverlayPaper.frame(batch,224f,boxY,24f,24f)
            if(enabled) {
                BattleOverlayPaper.line(batch,229f,boxY+12f,234f,boxY+6f,Color(.28f,.42f,.29f,1f))
                BattleOverlayPaper.line(batch,234f,boxY+6f,244f,boxY+20f,Color(.28f,.42f,.29f,1f))
            }
            font.color = Color.BLACK
            font.draw(batch, label, 261f, 643f - index * 65f)
        }
        drawRadios(SystemMessage.S_0BF12D107C, listOf(SystemMessage.S_D7A0CB68D6, SystemMessage.S_C7E54FE959, SystemMessage.S_F8FC3619DB), view.msgSpeed, 574f)
        drawRadios(SystemMessage.S_B80EE3CF4F, listOf(SystemMessage.S_744C6C3F65, SystemMessage.S_2179DA2CFF, SystemMessage.S_3EA27A4D42), view.notifyLevel, 310f)
        font.color = Color.BLACK
        font.draw(batch, SystemMessage.S_09584A936A, 816f, 211f)
        val swatchColors = listOf(
            Color(1f, 1f, 1f, 1f), Color(.85f, .85f, .85f, 1f),
            Color(.73f, .73f, .78f, 1f), Color(.88f, .84f, .72f, 1f),
        )
        swatchColors.forEachIndexed { index, tint ->
            val x = 816f + index * 105f
            BattleOverlayPaper.frame(batch,x,130f,96f,48f,tint)
            if(view.background==index) {
                BattleOverlayPaper.frame(batch,x-3f,127f,102f,54f,Color(.58f,.43f,.2f,1f))
                BattleOverlayPaper.fill(batch,x+3f,133f,90f,42f,tint)
            }
        }
        assets.panel?.draw(batch, 1130f, 47f, 156f, 56f)
        font.color = Color.BLACK
        font.draw(batch, SystemMessage.S_468266D639, 1158f, 75f)
        font.color = Color.WHITE
        font.data.setScale(1f)
        batch.color = Color.WHITE
        batch.end()
    }

    /**
     * `drawRadios`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawRadios(title: String, labels: List<String>, selected: Int, baseY: Float) {
        font.color = Color.BLACK
        font.draw(batch, title, 822f, baseY + 45f)
        labels.forEachIndexed { index, label ->
            val active = selected == index
            font.color = if (active) Color(.48f,.33f,.12f,1f) else Color.DARK_GRAY
            font.draw(batch, if (active) "●" else "○", 816f + index * 145f, baseY)
            font.color = Color.BLACK
            font.draw(batch, label, 846f + index * 145f, baseY)
        }
    }

    /**
     * `drawTiledBackground`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawTiledBackground() {
        BattleOverlayPaper.frame(batch,196f,41f,1097f,718f)
    }
}

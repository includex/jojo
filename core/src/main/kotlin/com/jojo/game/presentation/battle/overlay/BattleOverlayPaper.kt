package com.jojo.game.presentation.battle.overlay

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.math.atan2
import kotlin.math.sqrt

/** A shared solid surface for readable retro panels; no raster decoration under text. */
object BattleOverlayPaper {
    const val HELP_TEXT="전투 도움말\n\n부대를 선택한 뒤 이동할 칸을 선택하세요.\n명령에서 공격, 전략, 물품, 교환을 사용할 수 있습니다.\n교환은 상하좌우로 인접한 아군에게 소지품 한 개를 전달합니다.\nESC로 선택을 취소하거나 열린 창을 닫을 수 있습니다.\n목표 임무는 전투 메뉴의 승리 조건에서 확인하세요."
    const val UI_GLYPHS=HELP_TEXT+"장수 정보 기본 정보 능력치 특기 책략 열전 경험치 정상 인물 정보 등록된 인물 소개가 없습니다. 특기 등록된 특기가 없습니다. 장착하지 않음 사용 가능한 전략이 없습니다. 아군 적군 전장 부대 정보 기본 능력 출진 퇴각 공격 방어 정신 순발력 사기 닫기 이전 인물 다음 인물 무기 방어구 보조 장비 체력 기력 레벨 이름 병종 Lv 회 … / ·"

    private var pixel: Texture? = null
    val paper = Color(.88f,.82f,.68f,1f)
    val brass = Color(.57f,.45f,.25f,1f)
    private fun texture():Texture = pixel ?: Pixmap(1,1,Pixmap.Format.RGBA8888).let { p ->
        p.setColor(Color.WHITE);p.fill()
        Texture(p).also { p.dispose();pixel=it }
    }
    fun fill(batch:SpriteBatch,x:Float,y:Float,w:Float,h:Float,color:Color=paper) {
        val old=Color(batch.color);batch.color=color;batch.draw(texture(),x,y,w,h);batch.color=old
    }
    fun frame(batch:SpriteBatch,x:Float,y:Float,w:Float,h:Float,color:Color=paper) {
        fill(batch,x,y,w,h,color)
        fill(batch,x,y,w,2f,brass);fill(batch,x,y+h-2,w,2f,brass)
        fill(batch,x,y,2f,h,brass);fill(batch,x+w-2,y,2f,h,brass)
    }
    fun line(batch:SpriteBatch,x1:Float,y1:Float,x2:Float,y2:Float,color:Color) {
        val dx=x2-x1;val dy=y2-y1;val old=Color(batch.color);batch.color=color
        batch.draw(texture(),x1,y1,0f,1.5f,sqrt(dx*dx+dy*dy),3f,1f,1f,
            Math.toDegrees(atan2(dy,dx).toDouble()).toFloat(),0,0,1,1,false,false)
        batch.color=old
    }
    fun dispose() { pixel?.dispose();pixel=null }
}

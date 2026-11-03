package com.jojo.game.presentation.battle.overlay

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align

/** Persistent status HUD. Expanding it does not capture battlefield input outside the panel. */
class BattleHudPanelController {
    enum class Action(val label: String) {
        MENU("메뉴"), RECORD("부대"), MAP("지도"), END_TURN("턴 종료"), TOGGLE("접기")
    }
    var expanded: Boolean = true
        private set
    fun toggle() { expanded = !expanded }
    fun buttonAt(x: Float, y: Float, width: Float, height: Float): Action? =
        BattleHudPanelLayout.buttons(width,height,expanded).firstOrNull { it.contains(x,y) }?.action
    fun contains(x: Float, y: Float, width: Float, height: Float): Boolean {
        val panel=BattleHudPanelLayout.panel(width,height,expanded)
        return x in panel.x..panel.x+panel.width && y in panel.y..panel.y+panel.height
    }
}

object BattleHudPanelLayout {
    data class Rect(val x:Float,val y:Float,val width:Float,val height:Float)
    data class Button(val action:BattleHudPanelController.Action,val rect:Rect) {
        fun contains(x:Float,y:Float)=x in rect.x..rect.x+rect.width && y in rect.y..rect.y+rect.height
    }
    fun panel(width:Float,height:Float,expanded:Boolean):Rect {
        val panelWidth = if(expanded) width-24f else minOf(width-24f,360f)
        val panelHeight = if(expanded) 64f else 40f
        return Rect(12f,height-panelHeight-12f,panelWidth,panelHeight)
    }
    fun buttons(width:Float,height:Float,expanded:Boolean):List<Button> {
        val panel=panel(width,height,expanded)
        val buttonHeight=if(expanded) 38f else 28f
        val toggle=Button(BattleHudPanelController.Action.TOGGLE,
            Rect(panel.x+panel.width-86f,panel.y+(panel.height-buttonHeight)/2f,76f,buttonHeight))
        if(!expanded)return listOf(toggle)
        val buttonWidths=listOf(86f,86f,86f,106f)
        var x=toggle.rect.x-8f-buttonWidths.sum()-8f*3f
        return BattleHudPanelController.Action.entries.dropLast(1).mapIndexed { i,action ->
            Button(action,Rect(x,panel.y+13f,buttonWidths[i],buttonHeight)).also { x+=buttonWidths[i]+8f }
        }+toggle
    }

}

/** A warm, opaque top bar with text-labelled actions and an optional compact state. */
class BattleHudPanelRenderer(private val batch:SpriteBatch,private val shapes:ShapeRenderer,private val font:BitmapFont) {
    fun draw(controller:BattleHudPanelController,width:Float,height:Float,name:String,weather:String,round:Int,limit:Int) {
        val panel=BattleHudPanelLayout.panel(width,height,controller.expanded)
        val buttons=BattleHudPanelLayout.buttons(width,height,controller.expanded)
        shapes.projectionMatrix=batch.projectionMatrix
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color=Color.valueOf("b99b61");shapes.rect(panel.x,panel.y,panel.width,panel.height)
        shapes.color=Color.valueOf("21190f");shapes.rect(panel.x+1f,panel.y+1f,panel.width-2f,panel.height-2f)
        buttons.forEach { b ->
            shapes.color=Color.valueOf(if(b.action==BattleHudPanelController.Action.END_TURN) "685032" else "493b24")
            shapes.rect(b.rect.x,b.rect.y,b.rect.width,b.rect.height)
        }
        shapes.end()
        val scaleX=font.data.scaleX;val scaleY=font.data.scaleY;val color=Color(font.color)
        batch.begin()
        try {
            font.data.setScale(24f/36f);font.color=Color.valueOf("eee2c6")
            val baseline=panel.y+panel.height/2f+font.capHeight/2f
            if(controller.expanded) {
                val textRight=buttons.first().rect.x-16f
                val statusWidth=278f
                val titleWidth=(textRight-panel.x-28f-statusWidth).coerceAtLeast(120f)
                font.draw(batch,name,panel.x+14f,baseline,0,name.length,titleWidth,Align.left,false,"...")
                font.color=Color.valueOf("c8b99a")
                font.data.setScale(22f/36f)
                val status="날씨: $weather   $round / $limit 턴"
                font.draw(batch,status,textRight-statusWidth,baseline,statusWidth,Align.right,false)
            } else {
                val summary="$weather  ·  $round / $limit 턴"
                font.draw(batch,summary,panel.x+14f,baseline,panel.width-112f,Align.left,false)
            }
            font.color=Color.valueOf("eee2c6")
            buttons.forEach { b ->
                font.data.setScale((if(b.action==BattleHudPanelController.Action.TOGGLE) 20f else 23f)/36f)
                val label=if(b.action==BattleHudPanelController.Action.TOGGLE) if(controller.expanded) "접기" else "펼치기" else b.action.label
                font.draw(batch,label,b.rect.x,b.rect.y+b.rect.height/2f+font.capHeight/2f,b.rect.width,Align.center,false)
            }
        } finally {
            font.data.setScale(scaleX,scaleY);font.color=color;batch.end()
        }
    }
}

/** Labels for the existing full battle menu's stable command indices. */
object BattleMenuButtonLabels {
    val labels=listOf("종료","저장","불러오기","설정","아군","적군","지형","보물","턴 종료","승리 조건","지도","미행동","편집","도움말")
}

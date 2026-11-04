package com.jojo.game.presentation.battle.overlay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align

/** Read-only facts for the unit and tile under the pointer. Values come from battle state. */
data class BattleHudContextView(
    val unit: BattleHudUnitView? = null,
    val terrain: BattleHudTerrainView? = null,
    val objective: String = "",
    val defeat: String = "",
    val weatherIcon: Texture? = null,
)
data class BattleHudUnitView(
    val name:String,val post:String,val level:Int,
    val hp:Int,val maxHp:Int,val mp:Int,val maxMp:Int,
    val portrait:Texture? = null,
)
data class BattleHudTerrainView(
    val name:String,val coordinates:String,val effect:String,val movementCost:String,val basis:String = "",
    val thumbnail:TextureRegion? = null,
)

/** Persistent status HUD. Expanding it does not capture battlefield input outside the panel. */
class BattleHudPanelController {
    enum class Action(val label: String) {
        MENU("작전"), RECORD("부대"), MAP("지도"), SETTINGS("환경"), END_TURN("턴 종료"), TOGGLE("접기")
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
        val panelHeight = if(expanded) 156f else 40f
        return Rect(12f,height-panelHeight-12f,panelWidth,panelHeight)
    }
    fun buttons(width:Float,height:Float,expanded:Boolean):List<Button> {
        val panel=panel(width,height,expanded)
        val toggle=Button(BattleHudPanelController.Action.TOGGLE,
            Rect(panel.x+panel.width-86f,panel.y+panel.height-36f,76f,28f))
        if(!expanded)return listOf(toggle)
        val buttonWidths=listOf(76f,76f,76f,76f,96f)
        var x=toggle.rect.x-8f-buttonWidths.sum()-8f*4f
        return BattleHudPanelController.Action.entries.dropLast(1).mapIndexed { i,action ->
            Button(action,Rect(x,panel.y+panel.height-36f,buttonWidths[i],28f)).also { x+=buttonWidths[i]+8f }
        }+toggle
    }
    /** Weather / unit / terrain / mission; geometry stays below the 44px header. */
    fun contextColumns(width:Float,height:Float):List<Rect> {
        val p=panel(width,height,true)
        val contentWidth=p.width-24f
        var x=p.x+12f
        return listOf(.12f,.34f,.23f,.31f).map { fraction ->
            val width=contentWidth*fraction
            Rect(x,p.y+8f,width-10f,96f).also { x+=width }
        }
    }

}

/** Slightly translucent black status strip, with immediate pointer inspection below. */
class BattleHudPanelRenderer(private val batch:SpriteBatch,private val shapes:ShapeRenderer,private val font:BitmapFont,skin:Texture? = null) {
    private val frame=skin?.let { NinePatch(it,64,64,64,64).apply { scale(14f/64f,14f/64f) } }
    fun draw(controller:BattleHudPanelController,width:Float,height:Float,name:String,weather:String,round:Int,limit:Int,
             context:BattleHudContextView = BattleHudContextView()) {
        val panel=BattleHudPanelLayout.panel(width,height,controller.expanded)
        val buttons=BattleHudPanelLayout.buttons(width,height,controller.expanded)
        val columns=BattleHudPanelLayout.contextColumns(width,height)
        shapes.projectionMatrix=batch.projectionMatrix
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color=NAVY;shapes.rect(panel.x,panel.y,panel.width,panel.height)
        shapes.color=DIVIDER
        shapes.rect(panel.x,panel.y,panel.width,1f);shapes.rect(panel.x,panel.y+panel.height-1f,panel.width,1f)
        shapes.rect(panel.x,panel.y,1f,panel.height);shapes.rect(panel.x+panel.width-1f,panel.y,1f,panel.height)
        if(controller.expanded) {
            shapes.color=DIVIDER;shapes.rect(panel.x+1f,panel.y+111f,panel.width-2f,1f)
            columns.drop(1).forEach { shapes.rect(it.x-6f,it.y,1f,it.height) }
            if(context.unit==null) { val r=columns[1];shapes.color=EMPTY;shapes.rect(r.x+2f,r.y+2f,72f,90f) }
            val terrainRect=columns[2]
            shapes.color=if(context.terrain==null) EMPTY else PORTRAIT_BG
            shapes.rect(terrainRect.x+4f,terrainRect.y+12f,72f,72f)
            context.unit?.let { unit ->
                val r=columns[1]
                shapes.color=PORTRAIT_BG;shapes.rect(r.x+2f,r.y+2f,72f,90f)
                val tx=r.x+88f;val valueX=r.x+r.width-104f
                val barX=tx+34f;val barWidth=(valueX-barX-8f).coerceAtLeast(40f)
                for((value,maxValue,y,fill) in listOf(
                    StatBar(unit.hp,unit.maxHp,r.y+31f,HP),StatBar(unit.mp,unit.maxMp,r.y+10f,MP))) {
                    shapes.color=BAR_BORDER;shapes.rect(barX,y,barWidth,10f)
                    shapes.color=TRACK;shapes.rect(barX+1f,y+1f,barWidth-2f,8f)
                    shapes.color=fill;shapes.rect(barX+1f,y+1f,(barWidth-2f)*(value.toFloat()/maxValue.coerceAtLeast(1)).coerceIn(0f,1f),8f)
                }
            }
        }
        buttons.forEach { b ->
            shapes.color=if(b.action==BattleHudPanelController.Action.END_TURN) END_BUTTON else BUTTON
            shapes.rect(b.rect.x,b.rect.y,b.rect.width,b.rect.height)
        }
        shapes.end()
        val scaleX=font.data.scaleX;val scaleY=font.data.scaleY;val color=Color(font.color)
        batch.begin()
        try {
            batch.color=Color.WHITE
            frame?.draw(batch,panel.x,panel.y,panel.width,panel.height)
            font.data.setScale(22f/36f);font.color=IVORY
            val baseline=panel.y+panel.height-22f+font.capHeight/2f
            if(controller.expanded) {
                val textRight=buttons.first().rect.x-16f
                val statusWidth=264f
                ellipsis(name,panel.x+14f,baseline,(textRight-panel.x-28f-statusWidth).coerceAtLeast(120f))
                font.color=MUTED;font.data.setScale(20f/36f)
                font.draw(batch,"현재 $round 턴  ·  남은 ${(limit-round).coerceAtLeast(0)} 턴",textRight-statusWidth,baseline,statusWidth,Align.right,false)
            } else {
                font.draw(batch,"$weather  ·  $round / $limit 턴",panel.x+14f,baseline,panel.width-112f,Align.left,false)
            }
            font.color=IVORY
            buttons.forEach { b ->
                font.data.setScale((if(b.action==BattleHudPanelController.Action.TOGGLE) 19f else 21f)/36f)
                val label=if(b.action==BattleHudPanelController.Action.TOGGLE) if(controller.expanded) "접기" else "펼치기" else b.action.label
                font.draw(batch,label,b.rect.x,b.rect.y+b.rect.height/2f+font.capHeight/2f,b.rect.width,Align.center,false)
            }
            if(controller.expanded) drawContext(context,columns,weather)
        } finally {
            font.data.setScale(scaleX,scaleY);font.color=color;batch.color=Color.WHITE;batch.end()
        }
    }
    private fun drawContext(context:BattleHudContextView,columns:List<BattleHudPanelLayout.Rect>,weather:String) {
        val w=columns[0]
        text("날씨",w.x+6f,w.y+83f,20f,GOLD)
        context.weatherIcon?.let { texture ->
            val scale=minOf((w.width-12f)/texture.width,30f/texture.height)
            batch.color=Color.WHITE
            batch.draw(texture,w.x+6f,w.y+32f,texture.width*scale,texture.height*scale)
        }
        text(weather,w.x+6f,w.y+18f,22f,IVORY)
        val unit=context.unit;val u=columns[1]
        if(unit!=null) {
            unit.portrait?.let { portrait ->
                val scale=minOf(68f/portrait.width,86f/portrait.height)
                val w=portrait.width*scale;val h=portrait.height*scale
                batch.color=Color.WHITE;batch.draw(portrait,u.x+4f+(68f-w)/2f,u.y+4f+(86f-h)/2f,w,h)
            }
            val tx=u.x+88f
            font.data.setScale(23f/36f);font.color=IVORY
            ellipsis(unit.name,tx,u.y+84f,u.width-96f)
            font.data.setScale(18f/36f);font.color=MUTED
            ellipsis("Lv ${unit.level} · ${unit.post}",tx,u.y+62f,u.width-96f)
            text("HP",tx,u.y+42f,18f,IVORY);text("MP",tx,u.y+21f,18f,IVORY)
            font.draw(batch,"${unit.hp}/${unit.maxHp}",u.x+u.width-104f,u.y+42f,100f,Align.right,false)
            font.draw(batch,"${unit.mp}/${unit.maxMp}",u.x+u.width-104f,u.y+21f,100f,Align.right,false)
        }
        val terrain=context.terrain;val t=columns[2]
        if(terrain!=null) {
            terrain.thumbnail?.let { thumbnail ->
                val scale=minOf(70f/thumbnail.regionWidth,70f/thumbnail.regionHeight)
                val w=thumbnail.regionWidth*scale;val h=thumbnail.regionHeight*scale
                batch.color=Color.WHITE
                batch.draw(thumbnail,t.x+5f+(70f-w)/2f,t.y+13f+(70f-h)/2f,w,h)
            }
            val tx=t.x+88f
            val textWidth=t.width-96f
            font.data.setScale(20f/36f);font.color=IVORY
            ellipsis("${terrain.name}  ${terrain.coordinates}",tx,t.y+83f,textWidth)
            font.data.setScale(18f/36f)
            ellipsis("지형 효과 ${terrain.effect}",tx,t.y+61f,textWidth)
            ellipsis("이동 비용 ${terrain.movementCost}",tx,t.y+39f,textWidth)
            font.data.setScale(16f/36f);font.color=MUTED
            ellipsis(terrain.basis,tx,t.y+17f,textWidth)
        }
        val m=columns[3]
        text("임무",m.x+6f,m.y+83f,21f,GOLD)
        text("승리",m.x+6f,m.y+59f,18f,IVORY)
        clippedLines(context.objective.ifBlank { "—" },m.x+51f,m.y+59f,m.width-59f,1,18f)
        text("패배",m.x+6f,m.y+23f,18f,MUTED)
        clippedLines(context.defeat.ifBlank { "—" },m.x+51f,m.y+23f,m.width-59f,1,17f)
    }
    private fun text(value:String,x:Float,y:Float,size:Float,color:Color) {
        font.data.setScale(size/36f);font.color=color;font.draw(batch,value,x,y)
    }
    private fun ellipsis(value:String,x:Float,y:Float,width:Float) {
        font.draw(batch,value,x,y,0,value.length,width,Align.left,false,"...")
    }
    private fun clippedLines(value:String,x:Float,y:Float,width:Float,maxLines:Int,size:Float) {
        font.data.setScale(size/36f);font.color=MUTED
        val normalized=value.replace('\n',' ').trim()
        val layout=GlyphLayout();val lines=mutableListOf<String>();var remaining=normalized
        repeat(maxLines) { index ->
            if(remaining.isBlank())return@repeat
            if(index==maxLines-1) { lines+=remaining;remaining="" }
            else {
                var end=remaining.length
                while(end>1) { layout.setText(font,remaining.substring(0,end));if(layout.width<=width)break;end-- }
                lines+=remaining.substring(0,end).trimEnd();remaining=remaining.substring(end).trimStart()
            }
        }
        lines.forEachIndexed { index,line -> ellipsis(line,x,y-index*(size+2f),width) }
    }
    private data class StatBar(val value:Int,val maxValue:Int,val y:Float,val color:Color)
    companion object {
        const val UI_GLYPHS="작전 환경 현재 남은 메뉴 부대 지도 턴 종료 접기 펼치기 날씨 장수 장수 위에 커서를 올리세요 지형 지형 위에 커서를 올리세요 지형 효과 이동 비용 선택 장수 기준 미표시 통행불가 기준 없음 임무 승리 패배 아군 적군 우군 중립 HP MP Lv — · % / : , 0123456789"
        private val NAVY=Color(.015f,.015f,.015f,.94f)
        private val EMPTY=Color.valueOf("282a2d")
        private val GOLD=Color.valueOf("bfa265")
        private val IVORY=Color.valueOf("f3ebd8")
        private val MUTED=Color.valueOf("b4bdca")
        private val DIVIDER=Color.valueOf("55514a")
        private val PORTRAIT_BG=Color.valueOf("202225")
        private val TRACK=Color.valueOf("33363a")
        private val BAR_BORDER=Color.valueOf("8c826d")
        private val HP=Color.valueOf("7ca44c")
        private val MP=Color.valueOf("5a94bc")
        private val BUTTON=Color.valueOf("303237")
        private val END_BUTTON=Color.valueOf("695735")
    }
}

/** Labels for the existing full battle menu's stable command indices. */
object BattleMenuButtonLabels {
    val labels=listOf("종료","저장","불러오기","설정","아군","적군","지형","보물","턴 종료","승리 조건","지도","미행동","편집","도움말")
}

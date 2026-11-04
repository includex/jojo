package com.jojo.game.presentation.battle.overlay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align

/** Facts are supplied by the live battle; the renderer never invents combat values. */
data class BattleHudContextView(
    val unit:BattleHudUnitView?=null,val terrain:BattleHudTerrainView?=null,
    val objective:String="",val defeat:String="",val weatherIcon:Texture?=null,
    val pointerX:Float?=null,val pointerY:Float?=null,
    val enabledActions:Set<BattleHudPanelController.Action>?=null,
    val weatherSymbol:TextureRegion?=null,val chapterEmblem:TextureRegion?=null,
)
data class BattleHudUnitView(
    val name:String,val post:String,val level:Int,val hp:Int,val maxHp:Int,val mp:Int,val maxMp:Int,
    val portrait:Texture?=null,val faction:String="",val statuses:List<BattleHudStatusView> = emptyList(),
)
data class BattleHudStatusView(val label:String,val icon:TextureRegion?=null)
data class BattleHudTerrainView(
    val name:String,val coordinates:String,val effect:String,val movementCost:String,val basis:String="",
    val thumbnail:TextureRegion?=null,
)

class BattleHudPanelController {
    enum class Action(val label:String) {
        MENU("작전"),RECORD("부대"),MAP("지도"),SETTINGS("환경"),END_TURN("턴 종료"),TOGGLE("접기"),
        UNIT_INFO("장수 정보"),MAGIC("책략"),WAIT("대기"),HELP("도움말")
    }
    var expanded:Boolean=true
        private set
    fun toggle(){expanded=!expanded}
    fun buttonAt(x:Float,y:Float,width:Float,height:Float):Action?=
        BattleHudPanelLayout.buttons(width,height,expanded).firstOrNull{it.contains(x,y)}?.action
    fun contains(x:Float,y:Float,width:Float,height:Float,context:BattleHudContextView?=null):Boolean {
        val layout=BattleHudPanelLayout
        return layout.panel(width,height,expanded).contains(x,y) || layout.weather(width,height).contains(x,y) ||
            (expanded && layout.mission(width,height).contains(x,y)) ||
            layout.buttons(width,height,expanded).any{it.contains(x,y)} ||
            (context?.terrain!=null && layout.terrain(width,height).contains(x,y)) ||
            layout.hoverCard(width,height,context)?.contains(x,y)==true
    }
}

/** Reference composition: compact upper corners, lower terrain/shortcuts and a floating unit card. */
object BattleHudPanelLayout {
    data class Rect(val x:Float,val y:Float,val width:Float,val height:Float) {
        fun contains(px:Float,py:Float)=px in x..x+width && py in y..y+height
    }
    data class Button(val action:BattleHudPanelController.Action,val rect:Rect) {
        fun contains(x:Float,y:Float)=rect.contains(x,y)
    }
    fun panel(width:Float,height:Float,expanded:Boolean)=Rect(8f,height-52f,minOf(544f,width*.43f),44f)
    fun weather(width:Float,height:Float)=Rect(width-244f,height-60f,236f,52f)
    fun mission(width:Float,height:Float)=Rect(8f,height-124f,minOf(544f,width*.43f),66f)
    fun terrain(width:Float,height:Float)=Rect(8f,8f,minOf(560f,width*.42f),44f)
    fun buttons(width:Float,height:Float,expanded:Boolean):List<Button> {
        val header=panel(width,height,expanded);val weather=weather(width,height)
        val sizes=listOf(146f,112f,112f,142f);var x=width-sizes.sum()-24f-8f
        val bottom=listOf(BattleHudPanelController.Action.UNIT_INFO,BattleHudPanelController.Action.MAGIC,
            BattleHudPanelController.Action.WAIT,BattleHudPanelController.Action.END_TURN).mapIndexed{i,a->
            Button(a,Rect(x,8f,sizes[i],44f)).also{x+=sizes[i]+8f}
        }
        return bottom+listOf(
            Button(BattleHudPanelController.Action.MENU,Rect(header.x,header.y,header.width-34f,header.height)),
            Button(BattleHudPanelController.Action.TOGGLE,Rect(header.x+header.width-30f,header.y+8f,24f,28f)),
            Button(BattleHudPanelController.Action.HELP,Rect(weather.x+weather.width-42f,weather.y+10f,32f,32f)),
        )
    }
    /** Anchor beside the current cursor and flip left at the edge; leave corner HUD panels clear. */
    fun hoverCard(width:Float,height:Float,context:BattleHudContextView?):Rect? {
        if(context?.unit==null)return null
        val w=minOf(400f,width-32f);val h=210f
        val px=context.pointerX ?: width*.4f;val py=context.pointerY ?: height*.45f
        val x=(if(px+26f+w<=width-12f)px+26f else px-w-26f).coerceIn(12f,maxOf(12f,width-w-12f))
        val y=(py-18f).coerceIn(64f,maxOf(64f,height-h-132f))
        return Rect(x,y,w,h)
    }
    /** Compatibility for callers inspecting corner geometry. These are not one full-width strip. */
    fun contextColumns(width:Float,height:Float)=listOf(panel(width,height,true),weather(width,height),terrain(width,height),mission(width,height))
}

class BattleHudPanelRenderer(private val batch:SpriteBatch,private val shapes:ShapeRenderer,private val font:BitmapFont,skin:Texture?=null) {
    private val frame=skin?.let{NinePatch(it,64,64,64,64).apply{scale(14f/64f,14f/64f)}}
    fun draw(controller:BattleHudPanelController,width:Float,height:Float,name:String,weather:String,round:Int,limit:Int,
             context:BattleHudContextView=BattleHudContextView()) {
        val layout=BattleHudPanelLayout;val header=layout.panel(width,height,controller.expanded)
        val weatherRect=layout.weather(width,height);val mission=layout.mission(width,height)
        val terrain=context.terrain?.let{layout.terrain(width,height)};val card=layout.hoverCard(width,height,context)
        val buttons=layout.buttons(width,height,controller.expanded)
        val panels=listOfNotNull(header,weatherRect,terrain,card)+(if(controller.expanded)listOf(mission)else emptyList())
        shapes.projectionMatrix=batch.projectionMatrix
        Gdx.gl.glEnable(GL20.GL_BLEND);Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        panels.filter{it!=header}.forEach{panel(it)}
        titlePanel(header)
        buttons.filter{it.action in shortcutActions}.forEach{panel(it.rect)}
        card?.let{r->
            shapes.color=PORTRAIT;shapes.rect(r.x+8f,r.y+66f,106f,132f)
            shapes.color=LINE;shapes.rect(r.x+8f,r.y+62f,r.width-16f,1f)
            context.unit?.let{u->
                val x=r.x+158f;val barWidth=(r.width-216f)/2f
                statBar(x,r.y+82f,barWidth,u.hp,u.maxHp,HP)
                statBar(r.x+r.width-barWidth-8f,r.y+82f,barWidth,u.mp,u.maxMp,MP)
                u.statuses.take(5).forEachIndexed{i,_->
                    shapes.color=ICON_BG;shapes.rect(r.x+52f+i*58f,r.y+12f,40f,40f)
                }
            }
        }
        shapes.end()
        val sx=font.data.scaleX;val sy=font.data.scaleY;val fc=Color(font.color);val bc=Color(batch.color)
        batch.begin()
        try{
            batch.color=Color.WHITE
            panels.filter{it!=mission && it!=header}.forEach{frame?.draw(batch,it.x,it.y,it.width,it.height)}
            buttons.filter{it.action in shortcutActions}.forEach{frame?.draw(batch,it.rect.x,it.rect.y,it.rect.width,it.rect.height)}
            context.chapterEmblem?.let{drawRegionFit(it,header.x+8f,header.y+5f,42f,34f)}
                ?: text("◆",header.x+10f,header.y+29f,24f,GOLD)
            font.data.setScale(24f/36f);font.color=IVORY
            ellipsis(name,header.x+54f,header.y+30f,header.width-249f)
            font.data.setScale(21f/36f)
            font.draw(batch,"$round 턴 / $limit 턴",header.x+header.width-190f,header.y+29f,150f,Align.center,false)
            text(if(controller.expanded)"−"else"+",header.x+header.width-24f,header.y+29f,24f,GOLD)
            context.weatherSymbol?.let{drawRegionFit(it,weatherRect.x+15f,weatherRect.y+7f,48f,38f)}
                ?: context.weatherIcon?.let{t->val scale=minOf(48f/t.width,34f/t.height);batch.draw(t,weatherRect.x+18f,weatherRect.y+9f,t.width*scale,t.height*scale)}
            text(weather,weatherRect.x+78f,weatherRect.y+34f,24f,IVORY)
            text("?",weatherRect.x+weatherRect.width-32f,weatherRect.y+34f,24f,GOLD)
            if(controller.expanded){
                text("◆ 승리 조건",mission.x+8f,mission.y+47f,18f,GOLD)
                font.data.setScale(18f/36f);font.color=IVORY;ellipsis(context.objective,mission.x+108f,mission.y+47f,mission.width-118f)
                text("◆ 패배 조건",mission.x+8f,mission.y+20f,18f,GOLD)
                font.data.setScale(18f/36f);font.color=IVORY;ellipsis(context.defeat,mission.x+108f,mission.y+20f,mission.width-118f)
            }
            terrain?.let{r->context.terrain?.let{t->
                t.thumbnail?.let{batch.draw(it,r.x+12f,r.y+8f,32f,28f)}
                text(t.name,r.x+54f,r.y+29f,23f,IVORY)
                font.data.setScale(18f/36f);font.color=MUTED
                val extra="이동 비용 ${t.movementCost}  |  지형 효과 ${t.effect}"
                ellipsis(extra,r.x+145f,r.y+28f,r.width-157f)
            }}
            buttons.filter{it.action in shortcutActions}.forEach{b->
                val enabled=context.enabledActions?.contains(b.action) ?: true
                val hotkey=when(b.action){BattleHudPanelController.Action.UNIT_INFO->"Z";BattleHudPanelController.Action.MAGIC->"X";BattleHudPanelController.Action.WAIT->"C";else->"V"}
                font.color=if(enabled)GOLD else MUTED;font.data.setScale(23f/36f)
                font.draw(batch,hotkey,b.rect.x+10f,b.rect.y+30f,26f,Align.center,false)
                font.color=if(enabled)IVORY else MUTED;font.data.setScale(20f/36f)
                font.draw(batch,b.action.label,b.rect.x+40f,b.rect.y+29f,b.rect.width-45f,Align.center,false)
            }
            card?.let{drawUnitCard(it,context.unit!!)}
        }finally{font.data.setScale(sx,sy);font.color=fc;batch.color=bc;batch.end()}
    }
    private fun drawUnitCard(r:BattleHudPanelLayout.Rect,u:BattleHudUnitView){
        u.portrait?.let{t->val scale=minOf(102f/t.width,128f/t.height);val w=t.width*scale;val h=t.height*scale;batch.color=Color.WHITE;batch.draw(t,r.x+10f+(102f-w)/2f,r.y+68f+(128f-h)/2f,w,h)}
        font.data.setScale(26f/36f);font.color=GOLD;ellipsis(u.name,r.x+128f,r.y+180f,r.width-228f)
        text("Lv.${u.level}",r.x+r.width-90f,r.y+179f,21f,IVORY)
        font.data.setScale(19f/36f);font.color=IVORY;ellipsis(listOf(u.faction,u.post).filter{it.isNotBlank()}.joinToString("  "),r.x+128f,r.y+142f,r.width-145f)
        text("HP",r.x+127f,r.y+108f,18f,IVORY);text("기력",r.x+r.width-136f,r.y+108f,18f,IVORY)
        font.data.setScale(17f/36f);font.color=IVORY
        val barWidth=(r.width-216f)/2f
        font.draw(batch,"${u.hp}/${u.maxHp}",r.x+158f,r.y+108f,barWidth,Align.center,false)
        font.draw(batch,"${u.mp}/${u.maxMp}",r.x+r.width-barWidth-8f,r.y+108f,barWidth,Align.center,false)
        u.statuses.take(5).forEachIndexed{i,status->
            val x=r.x+52f+i*58f
            status.icon?.let{batch.draw(it,x-2f,r.y+10f,44f,44f)} ?: run{font.data.setScale(13f/36f);font.color=IVORY;ellipsis(status.label,x+3f,r.y+37f,34f)}
        }
    }
    private fun drawRegionFit(region:TextureRegion,x:Float,y:Float,width:Float,height:Float){
        val scale=minOf(width/region.regionWidth,height/region.regionHeight)
        val w=region.regionWidth*scale;val h=region.regionHeight*scale
        batch.draw(region,x+(width-w)/2f,y+(height-h)/2f,w,h)
    }
    /** A restrained bevel echoes the chapter banner without obscuring the battlefield. */
    private fun titlePanel(r:BattleHudPanelLayout.Rect){
        val bevel=12f
        shapes.color=BACKGROUND
        shapes.rect(r.x,r.y,r.width-bevel,r.height)
        shapes.triangle(r.x+r.width-bevel,r.y,r.x+r.width-bevel,r.y+r.height,r.x+r.width,r.y+r.height)
        shapes.color=GOLD
        shapes.rect(r.x,r.y,r.width-bevel,1f)
        shapes.rect(r.x,r.y+r.height-1f,r.width,1f)
        shapes.rect(r.x,r.y,1f,r.height)
        shapes.rectLine(r.x+r.width-bevel,r.y,r.x+r.width,r.y+r.height,1f)
    }
    private fun panel(r:BattleHudPanelLayout.Rect){shapes.color=BACKGROUND;shapes.rect(r.x,r.y,r.width,r.height);shapes.color=GOLD;outline(r)}
    private fun outline(r:BattleHudPanelLayout.Rect){shapes.rect(r.x,r.y,r.width,1f);shapes.rect(r.x,r.y+r.height-1f,r.width,1f);shapes.rect(r.x,r.y,1f,r.height);shapes.rect(r.x+r.width-1f,r.y,1f,r.height)}
    private fun statBar(x:Float,y:Float,w:Float,value:Int,max:Int,fill:Color){shapes.color=LINE;shapes.rect(x,y,w,9f);shapes.color=TRACK;shapes.rect(x+1f,y+1f,w-2f,7f);shapes.color=fill;shapes.rect(x+1f,y+1f,(w-2f)*(value.toFloat()/max.coerceAtLeast(1)).coerceIn(0f,1f),7f)}
    private fun text(value:String,x:Float,y:Float,size:Float,color:Color){font.data.setScale(size/36f);font.color=color;font.draw(batch,value,x,y)}
    private fun ellipsis(value:String,x:Float,y:Float,width:Float){font.draw(batch,value,x,y,0,value.length,width.coerceAtLeast(1f),Align.left,false,"...")}
    companion object{
        const val UI_GLYPHS="작전 환경 현재 남은 메뉴 부대 지도 턴 종료 접기 펼치기 날씨 장수 정보 책략 대기 도움말 지형 효과 이동 비용 선택 장수 기준 통행불가 임무 승리 조건 패배 조건 아군 적군 우군 중립 기력 HP MP Lv Z X C V ◆ − + ? — · % / : , . | 0123456789"
        private val shortcutActions=setOf(BattleHudPanelController.Action.UNIT_INFO,BattleHudPanelController.Action.MAGIC,BattleHudPanelController.Action.WAIT,BattleHudPanelController.Action.END_TURN)
        private val BACKGROUND=Color(.025f,.03f,.04f,.94f)
        private val GOLD=Color.valueOf("bfa265")
        private val IVORY=Color.valueOf("f3ebd8")
        private val MUTED=Color.valueOf("aeb4bc")
        private val PORTRAIT=Color.valueOf("202225")
        private val ICON_BG=Color.valueOf("171b23")
        private val LINE=Color.valueOf("8c826d")
        private val TRACK=Color.valueOf("33363a")
        private val HP=Color.valueOf("7ca44c")
        private val MP=Color.valueOf("5a94bc")
    }
}

object BattleMenuButtonLabels{
    val labels=listOf("종료","저장","불러오기","설정","아군","적군","지형","보물","턴 종료","승리 조건","지도","미행동","편집","도움말")
}

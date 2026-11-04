// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.i18n.SystemMessage

import com.jojo.game.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align

/** 전투 유닛 정보 표시 값: 이름·직위·레벨·체력·기력·전투 능력치를 정의한다. */
data class BattleUnitInfoUnitView(
    val name: String,
    val post: String,
    val level: Int,
    val hp: Int,
    val maxHp: Int,
    val mp: Int,
    val maxMp: Int,
    val attack: Int,
    val defense: Int,
    val spirit: Int,
    val critical: Int,
    val morale: Int,
    /** 아군 유닛인지 나타낸다. 원본은 아군일 때만 출진·퇴각 횟수 줄을 켠다. */
    val mine: Boolean = true,
    /** 출진 횟수다. 원본 `Unit.battle_n()`(유닛 속성 CZCS=14)에 해당한다. */
    val battleCount: Int = 0,
    /** 퇴각 횟수다. 원본 `Unit.retreat_n()`(유닛 속성 CTCS=15)에 해당한다. */
    val retreatCount: Int = 0,
    /** 인물 특기 설명이다. 원본 `_ref0`의 `bg1/label`이다. */
    val skillIntro: String = "",
    /** 무장 소개다. 원본 `_ref0`의 `bg3/label`이다. */
    val unitIntro: String = "",
    val characterId: Int = 0,
)

/** 전투 유닛 정보 표시 정보: 선택 탭, 버튼 활성 상태, 마법 행과 유닛 능력치를 정의한다. */
data class BattleUnitInfoOverlayView(
    val tab: Int,
    val unit: BattleUnitInfoUnitView,
    val buttons: List<Boolean>,
    val magicRows: List<String>,
    val portrait: Texture? = null,
    val equipmentRows: List<String> = emptyList(),
    val rateRows: List<Int> = emptyList(),
    val experience: Int? = null,
    val experienceLimit: Int = 100,
    val statuses: List<BattleHudStatusView> = emptyList(),
)

/** 전투 유닛 정보 자산: 인물 창의 타일·패널·얼굴·게이지·버튼 표식을 보관한다. */
data class BattleUnitInfoOverlayAssets(
    val logo: Texture,
    val box1: Texture,
    val box2: Texture,
    val box3: Texture,
    val background: Texture,
    val verticalLine: Texture,
    val face: Texture,
    val progress: Texture,
    val mark2: Texture,
    val mark3: Texture,
    val mark6: Texture,
    val frame: Texture? = null,
)

/** 전투 유닛 정보 렌더러: 유닛 능력치와 탭별 상세 패널을 고정 원본 레이아웃으로 출력한다. */
class BattleUnitInfoOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `shapes` (ShapeRenderer): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val shapes: ShapeRenderer,
    /** `font` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val font: BitmapFont,
    /** `assets` (BattleUnitInfoOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleUnitInfoOverlayAssets,
    private val baseFontScale: Float = 1f,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleUnitInfoOverlayView) {
        val oldScaleX=font.data.scaleX;val oldScaleY=font.data.scaleY;val oldColor=Color(font.color)
        val panel=BattleUnitInfoLayout.panel
        val portrait=BattleUnitInfoLayout.portrait
        batch.begin()
        try {
            BattleOverlayPaper.fill(batch,0f,0f,1488.3721f,800f,Color(0f,0f,0f,.32f))
            surface(panel.x,panel.y,panel.width,panel.height)
            assets.frame?.let { NinePatch(it,64,64,64,64).apply{scale(.22f,.22f)}.draw(batch,panel.x,panel.y,panel.width,panel.height) }
            text("장수 정보",panel.x,705f,panel.width,1.24f,GOLD,Align.center)
            surface(portrait.x,portrait.y,portrait.width,portrait.height,Color(.09f,.12f,.16f,1f))
            view.portrait?.let { texture ->
                val scale=minOf(portrait.width/texture.width,portrait.height/texture.height)
                val w=texture.width*scale;val h=texture.height*scale
                batch.color=Color.WHITE
                batch.draw(texture,portrait.x+(portrait.width-w)/2,portrait.y+(portrait.height-h)/2,w,h)
            }
            BattleUnitInfoLayout.tabs.forEachIndexed { i,r -> button(r,TAB_LABELS[i],i==view.tab) }
            val u=view.unit
            text(u.name,580f,599f,510f,1.4f,GOLD)
            text("Lv. ${u.level}",1110f,596f,180f,1.03f,IVORY,Align.right)
            text("${if(u.mine) "아군" else "적군"}    ·    병종  ${u.post}",580f,550f,710f,.92f,IVORY)
            divider(580f,526f,710f)
            meter("HP",u.hp,u.maxHp,580f,482f,326f,Color(.48f,.85f,.35f,1f))
            meter("기력",u.mp,u.maxMp,950f,482f,340f,Color(.24f,.67f,.9f,1f))
            view.experience?.let { meter("경험치",it,view.experienceLimit,580f,428f,710f,Color(.69f,.42f,.88f,1f)) }
            surface(560f,156f,748f,228f)
            when(view.tab) {
                0 -> {
                    heading("상태",580f,366f)
                    if(view.statuses.isEmpty()) text("정상",592f,320f,680f,.95f,IVORY)
                    view.statuses.forEachIndexed { i,status ->
                        val x=584f+(i%2)*360f;val y=330f-(i/2)*30f
                        status.icon?.let { batch.color=Color.WHITE;batch.draw(it,x,y-24f,32f,32f) }
                        text(status.label,x+40f,y,308f,.72f,IVORY)
                    }
                }
                1 -> {
                    heading("전투 능력",580f,366f)
                    val values=listOf("공격력" to u.attack,"방어력" to u.defense,"정신력" to u.spirit,"순발력" to u.critical,"사기" to u.morale)
                    values.forEachIndexed { i,(label,value) ->
                        val x=584f+(i%3)*240f;val y=315f-(i/3)*80f
                        text(label,x,y,214f,.88f,IVORY)
                        text(value.toString(),x,y-34f,214f,1.1f,GOLD)
                    }
                }
                2 -> {
                    heading("장비",580f,366f)
                    listOf("무기","방어구","보조 장비").forEachIndexed { i,label ->
                        text(label,584f,319f-i*56f,160f,.88f,GOLD)
                        text(view.equipmentRows.getOrNull(i) ?: "장착하지 않음",752f,319f-i*56f,524f,.91f,IVORY)
                    }
                }
                3 -> {
                    heading("특기 / 책략",580f,366f)
                    paragraph(u.skillIntro.ifBlank{"등록된 특기가 없습니다."},584f,324f,690f,2)
                    paragraph(view.magicRows.joinToString("  ·  ").ifBlank{"사용 가능한 전략이 없습니다."},584f,250f,690f,3)
                }
                else -> {
                    heading("열전",580f,366f)
                    paragraph(u.unitIntro.ifBlank{"등록된 인물 소개가 없습니다."},584f,324f,690f,4)
                }
            }
            button(BattleUnitInfoLayout.close,"X",false)
            button(BattleUnitInfoLayout.previous,"이전 인물",false,view.buttons.getOrElse(5){false})
            button(BattleUnitInfoLayout.next,"다음 인물",false,view.buttons.getOrElse(6){false})
            if(view.buttons.getOrElse(9){false})button(BattleUnitInfoLayout.rates,"기치",false)
        } finally {
            font.data.setScale(oldScaleX,oldScaleY);font.color=oldColor;batch.color=Color.WHITE;batch.end()
        }
    }
    private fun surface(x:Float,y:Float,w:Float,h:Float,color:Color=Color(.035f,.055f,.082f,.98f)) {
        BattleOverlayPaper.fill(batch,x,y,w,h,color)
        BattleOverlayPaper.fill(batch,x,y,w,1f,GOLD_EDGE);BattleOverlayPaper.fill(batch,x,y+h-1f,w,1f,GOLD_EDGE)
        BattleOverlayPaper.fill(batch,x,y,1f,h,GOLD_EDGE);BattleOverlayPaper.fill(batch,x+w-1f,y,1f,h,GOLD_EDGE)
    }
    private fun divider(x:Float,y:Float,width:Float)=BattleOverlayPaper.fill(batch,x,y,width,1f,GOLD_EDGE)
    private fun button(rect:BattleUnitInfoLayout.Rect,label:String,selected:Boolean,enabled:Boolean=true) {
        surface(rect.x,rect.y,rect.width,rect.height,if(selected)Color(.49f,.30f,.08f,1f)else Color(.065f,.095f,.14f,1f))
        text(label,rect.x+5,rect.y+rect.height/2+11f,rect.width-10,.85f,if(!enabled)Color(.43f,.45f,.47f,1f)else if(selected)GOLD else IVORY,Align.center)
    }
    private fun heading(value:String,x:Float,y:Float)=text(value,x,y,690f,.98f,GOLD)
    private fun paragraph(value:String,x:Float,y:Float,width:Float,maxLines:Int) {
        font.data.setScale(.89f*baseFontScale);font.color=IVORY
        val layout=com.badlogic.gdx.graphics.g2d.GlyphLayout()
        layout.setText(font,value,font.color,width,Align.left,true)
        var limit=value.length
        while(layout.height>font.lineHeight*maxLines && limit>0) {
            limit--;layout.setText(font,value.take(limit)+"…",font.color,width,Align.left,true)
        }
        font.draw(batch,layout,x,y)
    }
    private fun text(value:String,x:Float,y:Float,width:Float,scale:Float,color:Color,align:Int=Align.left) {
        font.data.setScale(scale*baseFontScale);font.color=color
        val layout=com.badlogic.gdx.graphics.g2d.GlyphLayout()
        layout.setText(font,value,0,value.length,color,width,align,false,"…");font.draw(batch,layout,x,y)
    }
    private fun meter(label:String,value:Int,max:Int,x:Float,y:Float,width:Float,color:Color) {
        text(label,x,y+34f,width*.4f,.85f,IVORY)
        text("$value / $max",x+width*.4f,y+34f,width*.6f,.85f,IVORY,Align.right)
        surface(x,y-5f,width,10f,Color(.19f,.2f,.23f,1f))
        BattleOverlayPaper.fill(batch,x+2,y-3f,(width-4)*(value.toFloat()/max.coerceAtLeast(1)).coerceIn(0f,1f),6f,color)
    }

    companion object {
        /**
         * `baseLabels`: 기본 탭에 찍을 문구와 자리를 만든다.
         *
         * 다섯 능력치는 원본 유닛 표의 4~8열(`UNIT_ATTR_NAME2.WL~YQ`)이고, 포트는 같은 열을
         * `attack/defense/spirit/critical/morale`로 읽어 둔다. 예전에는 60/70/80이 박혀 있어
         * 어떤 유닛을 열어도 같은 값이 나왔다.
         */
        fun baseLabels(unit: BattleUnitInfoUnitView): List<Pair<String, Pair<Float, Float>>> = buildList {
            add(SystemMessage.S_2C41F8D606 to (927.791f to 627.345f))
            add(SystemMessage.S_D8F7F3C37F to (848.106f to 573.7f))
            add(SystemMessage.S_B08EB69621 to (848.106f to 520.7f))
            add(SystemMessage.S_DAFEF2D567 to (848.106f to 467.7f))
            add(SystemMessage.S_983FFCFEC3 to (1059.486f to 573.24f))
            add(SystemMessage.S_DFC3916173 to (1059.486f to 520.7f))
            add("${unit.attack}" to (945.277f to 573.24f))
            add("${unit.spirit}" to (945.277f to 520.7f))
            add("${unit.defense}" to (945.277f to 467.7f))
            add("${unit.critical}" to (1155.034f to 573.24f))
            add("${unit.morale}" to (1155.034f to 520.7f))
            add(SystemMessage.S_BBEF509C8D to (1050.486f to 404f))
            add(SystemMessage.S_B9F40373E5 to (1050.486f to 290.16f))
            // 원본은 아군 유닛일 때만 이 줄을 켠다(`_ref0`의 `o.node.active = s`). 예전에는
            // 서식 문자열 "출진 횟수 %d / 퇴각 횟수 %d"가 치환 없이 그대로 찍혔다.
            if (unit.mine) {
                add("출진 횟수 ${unit.battleCount} / 퇴각 횟수 ${unit.retreatCount}" to (1050.486f to 101.526f))
            }
        }


        /** 탭 버튼 다섯 개의 원본 좌표다. */
        val TAB_BOXES = BattleUnitInfoLayout.tabs.map { it.x to it.y }

        /** 탭 버튼 문구다. 예전에는 상자만 그리고 문구는 왼쪽에 한 줄로 몰아 찍었다. */
        val TAB_LABELS = listOf("기본 정보", "능력치", "장비", "특기/책략", "열전")
        private val GOLD=Color(.96f,.83f,.52f,1f)
        private val IVORY=Color(.91f,.91f,.86f,1f)
        private val GOLD_EDGE=Color(.58f,.45f,.25f,1f)
    }

    private fun patch(texture: Texture, x: Float, y: Float, width: Float, height: Float, left: Int = 3, right: Int = 3, top: Int = 3, bottom: Int = 3) {
        NinePatch(texture, left, right, top, bottom).draw(batch, x, y, width, height)
    }
}

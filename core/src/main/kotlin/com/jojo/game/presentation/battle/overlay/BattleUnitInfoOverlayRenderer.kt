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
        val oldScaleX = font.data.scaleX
        val oldScaleY = font.data.scaleY
        val oldColor = Color(font.color)
        shapes.projectionMatrix = batch.projectionMatrix
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, .64f)
        shapes.rect(0f, 0f, 1488.3721f, 800f)
        shapes.color = Color(.16f, .115f, .07f, 1f)
        shapes.rect(80f,24f,1328f,752f)
        shapes.color=Color(.88f,.82f,.68f,1f)
        shapes.rect(96f,104f,632f,532f)
        shapes.rect(748f,104f,644f,532f)
        shapes.color=Color(.68f,.58f,.4f,1f)
        shapes.rect(106f,310f,228f,282f)
        val buttonRects=BattleUnitInfoLayout.tabs + listOf(BattleUnitInfoLayout.close,BattleUnitInfoLayout.previous,BattleUnitInfoLayout.next) +
            if(view.buttons.getOrElse(9){false}) listOf(BattleUnitInfoLayout.rates) else emptyList()
        buttonRects.forEach { rect ->
            shapes.color=if(BattleUnitInfoLayout.tabs.getOrNull(view.tab)==rect) Color(.88f,.7f,.35f,1f) else Color(.77f,.69f,.52f,1f)
            shapes.rect(rect.x,rect.y,rect.width,rect.height)
        }
        shapes.end()
        batch.begin()
        batch.color = Color.WHITE
        patch(assets.box1,80f,24f,1328f,752f)
        patch(assets.box2,96f,104f,632f,532f)
        patch(assets.box2,748f,104f,644f,532f)
        text("인물 정보",112f,754f,900f,1.23f,Color(.94f,.84f,.6f,1f))
        BattleUnitInfoLayout.tabs.forEachIndexed { index, rect ->
            button(rect,TAB_LABELS[index],index==view.tab)
        }
        val u=view.unit
        val portrait=BattleUnitInfoLayout.portrait
        patch(assets.box1,portrait.x-6,portrait.y-6,portrait.width+12,portrait.height+12)
        view.portrait?.let { texture ->
            val scale=minOf(portrait.width/texture.width,portrait.height/texture.height)
            val w=texture.width*scale;val h=texture.height*scale
            batch.color=Color.WHITE
            batch.draw(texture,portrait.x+(portrait.width-w)/2,portrait.y+(portrait.height-h)/2,w,h)
        }
        text(u.name,352f,588f,344f,1.3f,Color.BLACK)
        text("${u.post}  ·  Lv ${u.level}",352f,542f,344f,.8f,Color(.3f,.23f,.15f,1f))
        meter("체력",u.hp,u.maxHp,352f,470f,Color(.75f,.19f,.15f,1f))
        meter("기력",u.mp,u.maxMp,352f,396f,Color(.18f,.4f,.67f,1f))
        text(if(u.mine) "아군" else "적군",112f,276f,584f,.78f,Color(.3f,.23f,.15f,1f))
        text(if(u.mine) "출진 ${u.battleCount}회  ·  퇴각 ${u.retreatCount}회" else "전장 부대 정보",112f,234f,584f,.75f,Color.BLACK)
        text("전투 능력",112f,182f,584f,.78f,Color.BLACK)
        text("공격 ${u.attack}   방어 ${u.defense}   정신 ${u.spirit}",112f,144f,584f,.72f,Color.BLACK)
        when(view.tab) {
            0 -> {
                heading("인물 소개",780f,600f)
                paragraph(u.unitIntro.ifBlank { "등록된 인물 소개가 없습니다." },780f,548f,580f,5)
                heading("특기",780f,350f)
                paragraph(u.skillIntro.ifBlank { "등록된 특기가 없습니다." },780f,302f,580f,4)
            }
            1 -> {
                heading("기본 능력",780f,600f)
                listOf("공격력" to u.attack,"정신력" to u.spirit,"방어력" to u.defense,"순발력" to u.critical,"사기" to u.morale).forEachIndexed { i,(label,value) ->
                    text(label,792f,530f-i*68f,340f,.9f,Color.BLACK)
                    text(value.toString(),1160f,530f-i*68f,172f,.95f,Color.BLACK,Align.right)
                }
            }
            2 -> {
                heading("장비",780f,600f)
                listOf("무기","방어구","보조 장비").forEachIndexed { i,label ->
                    text(label,792f,528f-i*116f,540f,.8f,Color(.34f,.26f,.16f,1f))
                    paragraph(view.equipmentRows.getOrNull(i) ?: "장착하지 않음",792f,484f-i*116f,540f,2)
                }
            }
            3 -> {
                heading("전략",780f,600f)
                if(view.magicRows.isEmpty()) paragraph("사용 가능한 전략이 없습니다.",792f,530f,540f,2)
                view.magicRows.take(7).forEachIndexed { i,value -> text(value,792f,534f-i*54f,540f,.85f,Color.BLACK) }
            }
            else -> {
                heading("특기",780f,600f)
                paragraph(u.skillIntro.ifBlank { "등록된 특기가 없습니다." },792f,538f,540f,8)
            }
        }
        button(BattleUnitInfoLayout.close,"닫기",false)
        button(BattleUnitInfoLayout.previous,"이전 인물",false,view.buttons.getOrElse(5){false})
        button(BattleUnitInfoLayout.next,"다음 인물",false,view.buttons.getOrElse(6){false})
        if(view.buttons.getOrElse(9){false}) button(BattleUnitInfoLayout.rates,"기치",false)
        font.data.setScale(oldScaleX,oldScaleY)
        font.color=oldColor
        batch.color=Color.WHITE
        batch.end()
    }

    private fun button(rect: BattleUnitInfoLayout.Rect,label:String,selected:Boolean,enabled:Boolean=true) {
        batch.color=if(selected) Color(1f,.85f,.48f,1f) else if(enabled) Color.WHITE else Color(.6f,.6f,.6f,1f)
        patch(assets.box3,rect.x,rect.y,rect.width,rect.height,9,9,7,11)
        batch.color=Color.WHITE
        text(label,rect.x+8,rect.y+rect.height/2+13,rect.width-16,.8f,if(enabled) Color.BLACK else Color(.45f,.45f,.45f,1f),Align.center)
    }
    private fun heading(value:String,x:Float,y:Float)=text(value,x,y,580f,1.08f,Color(.28f,.18f,.07f,1f))
    private fun paragraph(value:String,x:Float,y:Float,width:Float,maxLines:Int) {
        font.data.setScale(.95f * baseFontScale)
        font.color=Color.BLACK
        val layout=com.badlogic.gdx.graphics.g2d.GlyphLayout()
        // Ellipsis constrains long introductions to their own panel instead of overlapping the next section.
        layout.setText(font,value,font.color,width,Align.left,true)
        val lineHeight=font.lineHeight
        if(layout.height<=lineHeight*maxLines) font.draw(batch,layout,x,y)
        else {
            var limit=value.length
            while(limit>0) {
                limit--
                layout.setText(font,value.take(limit)+"…",font.color,width,Align.left,true)
                if(layout.height<=lineHeight*maxLines) break
            }
            font.draw(batch,layout,x,y)
        }
    }
    private fun text(value:String,x:Float,y:Float,width:Float,scale:Float,color:Color,align:Int=Align.left) {
        font.data.setScale(scale * baseFontScale);font.color=color
        val layout=com.badlogic.gdx.graphics.g2d.GlyphLayout()
        layout.setText(font,value,0,value.length,color,width,align,false,"…")
        font.draw(batch,layout,x,y)
    }
    private fun meter(label:String,value:Int,max:Int,x:Float,y:Float,color:Color) {
        text("$label  $value / $max",x,y+30,344f,.78f,Color.BLACK)
        batch.color=Color(.22f,.18f,.12f,1f);batch.draw(assets.progress,x,y-12,344f,18f)
        batch.color=color;batch.draw(assets.progress,x,y-12,344f*(value.toFloat()/max.coerceAtLeast(1)).coerceIn(0f,1f),18f)
        batch.color=Color.WHITE
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
        val TAB_LABELS = listOf(SystemMessage.S_7F1D8C413D, SystemMessage.S_8F2A423094, SystemMessage.S_E17B206052, SystemMessage.S_913A74B987, SystemMessage.S_D3901EE454)
    }

    private fun patch(texture: Texture, x: Float, y: Float, width: Float, height: Float, left: Int = 3, right: Int = 3, top: Int = 3, bottom: Int = 3) {
        NinePatch(texture, left, right, top, bottom).draw(batch, x, y, width, height)
    }
}

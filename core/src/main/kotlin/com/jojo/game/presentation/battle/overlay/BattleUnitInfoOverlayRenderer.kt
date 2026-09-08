// Battle
package com.jojo.game.presentation.battle.overlay

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
)

/** 전투 유닛 정보 표시 정보: 선택 탭, 버튼 활성 상태, 마법 행과 유닛 능력치를 정의한다. */
data class BattleUnitInfoOverlayView(
    val tab: Int,
    val unit: BattleUnitInfoUnitView,
    val buttons: List<Boolean>,
    val magicRows: List<String>,
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
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleUnitInfoOverlayView) {
        shapes.projectionMatrix = batch.projectionMatrix
        // SpriteBatch.end()가 GL_BLEND를 끄므로 반투명 배경 전에 다시 켠다.
        if (!Gdx.gl.glIsEnabled(GL20.GL_BLEND)) Gdx.gl.glEnable(GL20.GL_BLEND)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, .88f)
        shapes.rect(0f, 0f, 1488.3721f, 800f)
        shapes.color = Color(.16f, .11f, .055f, 1f)
        shapes.rect(197.186f, 12f, 1094f, 776f)
        shapes.color = Color(.07f, .05f, .03f, 1f)
        shapes.rect(821.986f, 71.95f, 457f, 580.5f)
        shapes.end()

        batch.begin()
        batch.color = Color.WHITE
        for (ty in 0..8) for (tx in 0..11) batch.draw(assets.logo, 197.186f + tx * 96f, 12f + ty * 96f, 96f, 96f)
        patch(assets.box1, 821.986f, 71.95f, 457f, 580.5f)
        patch(assets.box1, 831.486f, 431.2f, 438f, 197f)
        patch(assets.background, 845.841f, 606.745f, 163.9f, 41.2f, 5, 5, 5, 5)
        batch.draw(assets.verticalLine, 821.986f, 317.36f, 457f, 2f)
        batch.draw(assets.verticalLine, 821.986f, 203.52f, 457f, 2f)
        batch.draw(assets.face, 230.186f, 490.956f, 192f, 240f)
        when (view.tab) {
            1 -> patch(assets.box1, 760f, 130f, 456f, 581.4f)
            2 -> patch(assets.box1, 760f, 130f, 456f, 571.5f)
            3 -> patch(assets.box2, 760f, 130f, 457f, 576f)
            4 -> patch(assets.box2, 760f, 130f, 458.5f, 576.5f)
        }
        batch.draw(assets.progress, 315f, 455f, 254f, 24f)
        batch.draw(assets.progress, 315f, 397f, 254f, 24f)
        batch.draw(assets.mark6, 300f, 463f, 16f, 16f)
        batch.draw(assets.mark3, 300f, 405f, 16f, 16f)
        batch.draw(assets.mark2, 570f, 405f, 16f, 16f)
        TAB_BOXES.forEach { (x, y) -> patch(assets.box3, x, y, 130f, 60f, 9, 9, 7, 11) }
        if (view.buttons.getOrElse(9) { false }) patch(assets.box3, 700.71f, 17.207f, 110f, 50f, 9, 9, 7, 11)

        val u = view.unit
        font.color = Color.WHITE
        // 제목과 인물 요약은 초상화(230.186, 490.956, 192x240) 오른쪽에 둔다. 예전에는 왼쪽
        // 좁은 자리에 겹쳐 찍혀 초상화와 서로 가렸다.
        font.data.setScale(.85f)
        font.draw(batch, "무장 정보", 230f, 762f)
        font.draw(batch, "${u.name}  ${u.post}  Lv${u.level}", 440f, 700f)
        font.draw(batch, "HP ${u.hp}/${u.maxHp}", 440f, 655f)
        font.draw(batch, "MP ${u.mp}/${u.maxMp}", 440f, 610f)
        // 다섯 능력치는 오른쪽 `기본 능력` 상자가 이미 보여 준다. 여기서 또 찍으면 같은 값이
        // 이름만 바꿔 두 번 나온다.
        TAB_LABELS.forEachIndexed { index, label ->
            // 탭 상자는 밝은 판이라 흰 글씨가 묻힌다. 오른쪽 패널 문구와 같은 검은색을 쓰고
            // 선택된 탭만 더 진하게 둔다.
            font.color = if (index == view.tab) Color.BLACK else Color(.35f, .35f, .35f, 1f)
            val (x, y) = TAB_BOXES[index]
            font.draw(batch, label, x, y + 38f, 130f, Align.center, false)
        }
        font.color = Color.WHITE
        view.magicRows.forEachIndexed { i, magic -> font.draw(batch, magic, 790f, 680f - i * 50f) }
        font.draw(batch, "이전 무장", 980f, 75f)
        font.draw(batch, "다음 무장", 1140f, 75f)
        font.draw(batch, "확인", 785f, 75f)
        if (view.buttons.getOrElse(9) { false }) font.draw(batch, "기력 모으기", 707f, 54f)
        font.data.setScale(1f)
        batch.end()
        if (view.tab == 0) drawBaseLabels(view.unit)
    }

    /**
     * `drawBaseLabels`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawBaseLabels(unit: BattleUnitInfoUnitView) {
        val labels = baseLabels(unit)
        batch.begin()
        font.data.setScale(40f / 26f)
        font.color = Color.BLACK
        labels.forEach { (text, position) -> font.draw(batch, text, position.first - 220f, position.second + 20f, 440f, Align.center, false) }
        // 소개문은 제목과 같은 크기로 찍으면 제목 위에 겹친다. 원본도 본문은 작은 글씨로
        // 구역(가로선 317.36 / 203.52 사이)을 채운다. 제목 아래 남는 높이에 줄바꿈해 넣는다.
        font.data.setScale(1f)
        introBlock(unit.unitIntro, 376f)
        introBlock(unit.skillIntro, 262f)
        font.data.setScale(1f)
        batch.end()
    }

    /** `introBlock`: 소개 본문을 제목 아래 구역에 줄바꿈해 그린다. */
    private fun introBlock(text: String, top: Float) {
        font.draw(batch, text.ifBlank { "없음" }, 830.486f, top, 440f, Align.left, true)
    }

    /**
     * `patch`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    companion object {
        /**
         * `baseLabels`: 기본 탭에 찍을 문구와 자리를 만든다.
         *
         * 다섯 능력치는 원본 유닛 표의 4~8열(`UNIT_ATTR_NAME2.WL~YQ`)이고, 포트는 같은 열을
         * `attack/defense/spirit/critical/morale`로 읽어 둔다. 예전에는 60/70/80이 박혀 있어
         * 어떤 유닛을 열어도 같은 값이 나왔다.
         */
        fun baseLabels(unit: BattleUnitInfoUnitView): List<Pair<String, Pair<Float, Float>>> = buildList {
            add("기본 능력" to (927.791f to 627.345f))
            add("무력" to (848.106f to 573.7f))
            add("지력" to (848.106f to 520.7f))
            add("지휘" to (848.106f to 467.7f))
            add("민첩성" to (1059.486f to 573.24f))
            add("운기" to (1059.486f to 520.7f))
            add("${unit.attack}" to (945.277f to 573.24f))
            add("${unit.spirit}" to (945.277f to 520.7f))
            add("${unit.defense}" to (945.277f to 467.7f))
            add("${unit.critical}" to (1155.034f to 573.24f))
            add("${unit.morale}" to (1155.034f to 520.7f))
            add("무장 소개" to (1050.486f to 404f))
            add("인물 특기 일람" to (1050.486f to 290.16f))
            // 원본은 아군 유닛일 때만 이 줄을 켠다(`_ref0`의 `o.node.active = s`). 예전에는
            // 서식 문자열 "출진 횟수 %d / 퇴각 횟수 %d"가 치환 없이 그대로 찍혔다.
            if (unit.mine) {
                add("출진 횟수 ${unit.battleCount} / 퇴각 횟수 ${unit.retreatCount}" to (1050.486f to 101.526f))
            }
        }


        /** 탭 버튼 다섯 개의 원본 좌표다. */
        val TAB_BOXES = listOf(
            175.737f to 342.65f, 366.522f to 342.65f,
            147.295f to 281.471f, 277.258f to 281.471f, 407.258f to 281.471f,
        ).map { (x, y) -> (640f + x - 65f) to (400f + y - 30f) }

        /** 탭 버튼 문구다. 예전에는 상자만 그리고 문구는 왼쪽에 한 줄로 몰아 찍었다. */
        val TAB_LABELS = listOf("기본", "능력", "장비", "전략", "특기")
    }

    private fun patch(texture: Texture, x: Float, y: Float, width: Float, height: Float, left: Int = 3, right: Int = 3, top: Int = 3, bottom: Int = 3) {
        NinePatch(texture, left, right, top, bottom).draw(batch, x, y, width, height)
    }
}

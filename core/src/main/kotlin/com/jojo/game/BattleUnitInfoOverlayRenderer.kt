package com.jojo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align

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
)

data class BattleUnitInfoOverlayView(
    val tab: Int,
    val unit: BattleUnitInfoUnitView,
    val buttons: List<Boolean>,
    val magicRows: List<String>,
)

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

/** Stateless renderer for the UnitInfoLayer battle overlay. */
class BattleUnitInfoOverlayRenderer(
    private val batch: SpriteBatch,
    private val shapes: ShapeRenderer,
    private val font: BitmapFont,
    private val assets: BattleUnitInfoOverlayAssets,
) {
    fun draw(view: BattleUnitInfoOverlayView) {
        shapes.projectionMatrix = batch.projectionMatrix
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
        listOf(175.737f to 342.65f, 366.522f to 342.65f, 147.295f to 281.471f, 277.258f to 281.471f, 407.258f to 281.471f)
            .forEach { (x, y) -> patch(assets.box3, 640f + x - 65f, 400f + y - 30f, 130f, 60f, 9, 9, 7, 11) }
        if (view.buttons.getOrElse(9) { false }) patch(assets.box3, 700.71f, 17.207f, 110f, 50f, 9, 9, 7, 11)

        val u = view.unit
        font.data.setScale(.65f)
        font.color = Color.WHITE
        font.draw(batch, "무장 정보", 125f, 750f)
        font.draw(batch, "${u.name}  ${u.post}  Lv${u.level}", 150f, 690f)
        font.draw(batch, "HP ${u.hp}/${u.maxHp}     MP ${u.mp}/${u.maxMp}", 150f, 650f)
        font.draw(batch, "공격 ${u.attack}  방어 ${u.defense}  정신 ${u.spirit}  폭발 ${u.critical}  사기 ${u.morale}", 150f, 610f)
        font.draw(batch, "기본   능력   장비   전략   특기", 100f, 685f)
        font.draw(batch, if (view.tab == 3) "장비 / 전략" else "기본 능력치", 150f, 550f)
        view.magicRows.forEachIndexed { i, magic -> font.draw(batch, magic, 790f, 680f - i * 50f) }
        font.draw(batch, "이전 무장", 980f, 75f)
        font.draw(batch, "다음 무장", 1140f, 75f)
        font.draw(batch, "확인", 785f, 75f)
        if (view.buttons.getOrElse(9) { false }) font.draw(batch, "기력 모으기", 707f, 54f)
        font.data.setScale(1f)
        batch.end()
        if (view.tab == 0) drawBaseLabels()
    }

    private fun drawBaseLabels() {
        val labels = listOf(
            "기본 능력" to (927.791f to 627.345f), "무력" to (848.106f to 573.7f), "지력" to (848.106f to 520.7f),
            "지휘" to (848.106f to 467.7f), "민첩성" to (1059.486f to 573.24f), "운기" to (1059.486f to 520.7f),
            "60" to (945.277f to 573.24f), "70" to (945.277f to 520.7f), "80" to (945.277f to 467.7f),
            "60" to (1155.034f to 573.24f), "60" to (1155.034f to 520.7f), "무장 소개" to (1050.486f to 404f),
            "인물 특기 일람" to (1050.486f to 290.16f), "없음" to (1050.986f to 262.719f),
            "출진 횟수 %d / 퇴각 횟수 %d" to (1050.486f to 101.526f),
        )
        batch.begin()
        font.data.setScale(40f / 26f)
        font.color = Color.BLACK
        labels.forEach { (text, position) -> font.draw(batch, text, position.first - 220f, position.second + 20f, 440f, Align.center, false) }
        font.data.setScale(1f)
        batch.end()
    }

    private fun patch(texture: Texture, x: Float, y: Float, width: Float, height: Float, left: Int = 3, right: Int = 3, top: Int = 3, bottom: Int = 3) {
        NinePatch(texture, left, right, top, bottom).draw(batch, x, y, width, height)
    }
}

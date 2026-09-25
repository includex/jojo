// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.i18n.GameText

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.battle.assets.BattleSettlementInfoAssets

/**
 * 원본 `battle/BattleUnitInfoLayer`(프리팹 9145bd88): `BattleUnit.heightLight()`와 유닛 선택이 띄우는
 * 소형 정보창이다. 이름·병종·Lv·진영·지형 이름·지형 효과와 HP/MP 막대를 유닛 옆에 보여 준다.
 * 배경·막대·아이콘 SpriteFrame은 Mine/OtherUnitInfoLayer와 같은 자산이라 `settlement-info`를 다시 쓴다.
 */
data class BattleUnitInfoPopupView(
    val name: String,
    val postsName: String,
    val level: Int,
    /** 원본 `Math.min(2, unit.type())`: 0 아군(MINE), 1 우군, 2 적군. */
    val camp: Int,
    val terrainName: String,
    val terrainImpact: Int,
    val hitPoints: Int,
    val maxHitPoints: Int,
    val magicPoints: Int,
    val maxMagicPoints: Int,
    /** MINE일 때만 보이는 경험치. 한계에 닿으면 원본처럼 "MAX"다. */
    val experience: Int,
    val experienceLimit: Int,
    /** 창 배경(bg) 중심의 월드 좌표. `BattleUnitInfoPopupLayout.place`가 정한다. */
    val centerX: Float,
    val centerY: Float,
)

object BattleUnitInfoPopupLayout {
    const val WIDTH = 471f
    const val HEIGHT = 258f
    private const val LAYER_HALF_WIDTH = 640f
    private const val LAYER_HALF_HEIGHT = 400f

    /**
     * 원본 `_setpos(unitNode)`: 유닛 오른쪽 한 칸(48) 옆에 창 왼쪽을, 유닛 위 48 아래에 창 위를 맞춘다.
     * 아래로 넘치면 `h - 96`만큼 올리고, 오른쪽으로 넘치면 유닛 왼쪽(`w + 96`)으로 뒤집으며, 위아래는 화면 안에 가둔다.
     * 좌표는 원본 Layer(1280×800) 중심 기준 설계 단위다.
     */
    fun place(unitX: Float, unitY: Float): Pair<Float, Float> {
        var x = unitX + 48f + WIDTH / 2f
        var y = unitY - HEIGHT / 2f + 48f
        val halfW = WIDTH / 2f
        val halfH = HEIGHT / 2f
        if (-y + halfH > LAYER_HALF_HEIGHT) y += HEIGHT - 96f
        if (x + halfW > LAYER_HALF_WIDTH) x -= WIDTH + 96f
        y = minOf(y, LAYER_HALF_HEIGHT - halfH)
        y = maxOf(y, -(LAYER_HALF_HEIGHT - halfH))
        return x to y
    }
}

/**
 * 프리팹 좌표는 bg 중심 기준이다. 라벨 노드는 54.4 높이이고 글자 위는 노드 아래 + 42에 놓인다.
 * 글꼴은 모두 fontSize 40에 2px 테두리이며, 이름·진영·지형 이름은 굵다.
 */
class BattleUnitInfoPopupRenderer(
    private val batch: SpriteBatch,
    private val assets: BattleSettlementInfoAssets,
    private val fonts: Fonts,
) {
    class Fonts(
        /** label(Lv)·label2(레벨)·p0/p1 숫자·label5(지형 %): 흰 글자, 검정/회색 테두리. */
        val regular: BitmapFont,
        /** label1(병종): 굵게, 검정 테두리. */
        val bold: BitmapFont,
        /** label0(이름): 굵게, 진영별 테두리 `[0xC00000, 0x1070, 0xC15507]` → 아군 (0,0,192), 우군 (112,16,0), 적군 (7,85,193). */
        val nameByCamp: List<BitmapFont>,
        /** label3(진영): 우군 (240,208,0)/(184,117,7), 적군 (32,140,240)/(16,88,128). 아군은 숨긴다. */
        val campByIndex: Map<Int, BitmapFont>,
        /** label4(지형 이름): (128,212,128) 글자에 (104,144,64) 테두리, 굵게. */
        val terrain: BitmapFont,
        /** label5(지형 %): 흰 글자에 (160,156,120) 테두리. */
        val terrainRate: BitmapFont,
    )

    companion object {
        private const val BG2 = "maps/ui/settlement-info/bg2.png"
        private const val BOX1 = "maps/ui/settlement-info/box1.png"
        private const val PROGRESS_BG = "maps/ui/settlement-info/progress-bg.png"
        private const val HP_BAR = "maps/ui/settlement-info/mark3.png"
        private const val MP_BAR = "maps/ui/settlement-info/mark2.png"
        private const val HP_ICON = "maps/ui/settlement-info/mark7.png"
        private const val MP_ICON = "maps/ui/settlement-info/mark8.png"
        private const val LABEL_TEXT_TOP = 42f - 27.2f
        private val EXP_MAX_COLOR = Color(0f, 204f / 255f, 1f, 1f)
    }

    fun draw(view: BattleUnitInfoPopupView) {
        val cx = view.centerX
        val cy = view.centerY
        batch.color = Color.WHITE
        assets.draw(batch, BG2, cx - 235.5f, cy - 129f, 471f, 258f)
        assets.draw(batch, BOX1, cx - 235.5f, cy - 0.25f - 128.75f, 471f, 257.5f, 2)
        // p0 (27, 27) / p1 (27, -31): 374×24 바탕, 그 안에 370×20 막대. terrain0 아이콘은 (-196, ±) 2배 크기.
        bar(cx + 27f, cy + 27f, HP_ICON, 40f, cy + 37f, HP_BAR, view.hitPoints, view.maxHitPoints)
        bar(cx + 27f, cy - 31f, MP_ICON, 48f, cy - 17f, MP_BAR, view.magicPoints, view.maxMagicPoints)
        // 윗줄 y=94: label0 이름(왼쪽 -229), label1 병종(중심 2.175, 143 너비), Lv(중심 124.035), label2 레벨(오른쪽 223).
        val topRowY = cy + 94f + LABEL_TEXT_TOP
        val nameFont = fonts.nameByCamp[view.camp.coerceIn(0, 2)]
        drawLeft(nameFont, view.name, cx - 229f, topRowY)
        drawCentered(fonts.bold, view.postsName, cx + 2.175f, topRowY)
        drawCentered(fonts.regular, "Lv", cx + 124.035f, topRowY)
        drawRight(fonts.regular, view.level.toString(), cx + 223f, topRowY)
        // 아랫줄 y=-87: label3 진영(중심 -172), label4 지형 이름(중심 12), label5 지형 효과(오른쪽 220).
        val bottomRowY = cy - 87f + LABEL_TEXT_TOP
        if (view.camp == 0) {
            // 아군은 label3 대신 labelx "Exp"(오른쪽 -143)와 label6 경험치(중심 -78)를 보여 준다.
            val expRowY = cy - 89f + 42f - 25.2f
            drawRight(fonts.regular, "Exp", cx - 143f, expRowY)
            val reachedLimit = view.experience >= view.experienceLimit
            fonts.regular.color = if (reachedLimit) EXP_MAX_COLOR else Color.WHITE
            drawCentered(fonts.regular, if (reachedLimit) "MAX" else view.experience.toString(), cx - 78f, expRowY)
            fonts.regular.color = Color.WHITE
        } else {
            fonts.campByIndex[view.camp]?.let { drawCentered(it, listOf(GameText.S_3843E8E488, GameText.S_7627935CE4, GameText.S_93A131140A)[view.camp], cx - 172f, bottomRowY) }
        }
        drawCentered(fonts.terrain, view.terrainName, cx + 12f, bottomRowY)
        drawRight(fonts.terrainRate, "${view.terrainImpact}%", cx + 220f, bottomRowY)
    }

    private fun bar(centerX: Float, centerY: Float, icon: String, iconHeight: Float, iconCenterY: Float, barAsset: String, value: Int, max: Int) {
        assets.draw(batch, icon, centerX - 27f - 196f - 24f, iconCenterY - iconHeight / 2f, 48f, iconHeight)
        val left = centerX - 187f
        val bottom = centerY - 12f
        assets.draw(batch, PROGRESS_BG, left, bottom, 374f, 24f, 3)
        val ratio = (value.toFloat() / max.coerceAtLeast(1)).coerceIn(0f, 1f)
        assets.texture(barAsset)?.let { batch.draw(it, left + 2f, bottom + 2f, 370f * ratio, 20f) }
        // label0 (-23, 12) 오른쪽 정렬, label "/" (0, 12) 중심, label1 (23, 12) 왼쪽 정렬.
        val textTop = centerY + 12f + LABEL_TEXT_TOP
        drawRight(fonts.regular, value.toString(), centerX - 23f, textTop)
        drawCentered(fonts.regular, "/", centerX, textTop)
        drawLeft(fonts.regular, max.toString(), centerX + 23f, textTop)
    }

    private fun drawLeft(font: BitmapFont, text: String, x: Float, top: Float) = font.draw(batch, text, x, top)

    private fun drawRight(font: BitmapFont, text: String, rightX: Float, top: Float) {
        val layout = GlyphLayout(font, text)
        font.draw(batch, text, rightX - layout.width, top)
    }

    private fun drawCentered(font: BitmapFont, text: String, centerX: Float, top: Float) {
        val layout = GlyphLayout(font, text)
        font.draw(batch, text, centerX - layout.width / 2f, top)
    }
}

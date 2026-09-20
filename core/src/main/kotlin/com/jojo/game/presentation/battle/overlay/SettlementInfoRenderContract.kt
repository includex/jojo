// Battle
package com.jojo.game.presentation.battle.overlay

/**
 * `SettlementInfoRenderContract`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object SettlementInfoRenderContract {
    /**
     * `Sprite`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Sprite(
        val path: String,
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        /**
         * 원본 `cc.Sprite._type`이 SLICED(1)인 프레임의 cap inset이다. 0이면 SIMPLE로,
         * 프레임을 그대로 늘여 그린다.
         *
         * `MineUnitInfoLayer` 프리팹에서 SLICED인 노드는 테두리 프레임 `box3`(box1.png)와
         * 진행 막대 바탕 `p0~p2`(progress-bg.png)다. 20x20 테두리와 60x15 둥근 바탕을
         * 471x257.5 / 374x24 로 늘이면 테두리 두께가 그대로 확대돼 배경이 뭉개지므로
         * 가장자리를 고정한 9분할로 그려야 원본과 같아진다.
         */
        val capInset: Int = 0,
    )
    /**
     * `Panel`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class Panel { MINE, OTHER }

    /**
     * `ROOT_X` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val ROOT_X = 736f
    /**
     * `ROOT_Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val ROOT_Y = 96f
    /**
     * `ROOT_W` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val ROOT_W = 471f

    /** InfoBaseLayer._setpos in bottom-left viewport coordinates; preserves its edge flips. */
    fun placementOffset(panel: Panel, nodeX: Float, nodeY: Float, width: Float, height: Float): Pair<Float, Float> {
        val panelHeight = if (panel == Panel.MINE) 258f else 193.5f
        var left = nodeX + 48f
        var bottom = nodeY + 48f - panelHeight
        if (bottom < 0f) bottom += panelHeight - 96f
        if (left + ROOT_W > width) left -= ROOT_W + 96f
        bottom = minOf(bottom, height - panelHeight)
        return left - ROOT_X to bottom - ROOT_Y
    }

    /**
     * `LABEL_COLOR`: 두 패널의 모든 라벨 글자색이다.
     *
     * 원본 프리팹 `MineUnitInfoLayer`(dd2699f7…528ab)와 `OtherUnitInfoLayer`(60e799d9…1b511)에서
     * `_color`를 적어 둔 노드는 투명한 `Panel_cancel`(opacity 0) 하나뿐이고, 라벨 노드에는
     * `_color`가 없어 `cc.Label` 기본값인 흰색이다. 글자 주위의 `cc.LabelOutline._color`는
     * 별도 값(대부분 검정, 이름 라벨만 (192,80,0))이라 노드 색과 섞지 않는다.
     *
     * 그리기 쪽 `BattleScreen.drawSettlementOverlays`와 `Mine/OtherUnitInfoRenderEvents`가
     * 같은 상수를 읽으므로 화면과 증거가 갈라질 수 없다.
     */
    const val LABEL_COLOR = "#ffffffff"

    /**
     * `MAX_LABEL_COLOR`: 무기·방어구 경험치 라벨(label3/label4)이 상한에 닿았을 때의 글자색이다.
     *
     * 원본 `recovered-js/modules/ui/MineUnitInfoLayer.js:173-176`이 `T >= 3 && A[0] == A[1]`인 줄에만
     * `x.string = "MAX"`와 `x.node.color = cc.color(17, 17, 251)`을 준다. 17/17/251 = `#1111fb`.
     * 프리팹에는 없는 값이라 원본 코드가 유일한 출처다.
     *
     * 그리기 쪽 `BattleScreen.drawSettlementOverlays`와 `MineUnitInfoRenderEvents`가 같은 상수를
     * 읽으므로 화면과 증거가 갈라질 수 없다.
     */
    const val MAX_LABEL_COLOR = "#1111fbff"

    /** 원본 `MineUnitInfoLayer.js:173`의 `T >= 3 && A[0] == A[1]` 판정이다. */
    fun equipmentExperienceMaxed(value: Int, limit: Int) = value == limit

    /** 원본 `MineUnitInfoLayer.js:173-176`이 label3/label4에 쓰는 글자다. */
    fun equipmentExperienceText(value: Int, limit: Int) =
        if (equipmentExperienceMaxed(value, limit)) "MAX" else value.toString()

    /** 원본 `MineUnitInfoLayer.js:175`가 label3/label4 노드에 주는 색이다. */
    fun equipmentExperienceColor(value: Int, limit: Int) =
        if (equipmentExperienceMaxed(value, limit)) MAX_LABEL_COLOR else LABEL_COLOR

    /**
     * `BG2` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val BG2 = "maps/ui/settlement-info/bg2.png"
    /**
     * `BOX1` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val BOX1 = "maps/ui/settlement-info/box1.png"
    /**
     * `PROGRESS_BG` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val PROGRESS_BG = "maps/ui/settlement-info/progress-bg.png"
    /**
     * `HP_BAR` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val HP_BAR = "maps/ui/settlement-info/mark3.png"
    /**
     * `MP_BAR` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val MP_BAR = "maps/ui/settlement-info/mark2.png"
    /**
     * `EXP_BAR` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val EXP_BAR = "maps/ui/settlement-info/mark6.png"
    /**
     * `HP_ICON` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val HP_ICON = "maps/ui/settlement-info/mark7.png"
    /**
     * `MP_ICON` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val MP_ICON = "maps/ui/settlement-info/mark8.png"
    /**
     * `EXP_ICON` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val EXP_ICON = "maps/ui/settlement-info/mark9.png"
    /**
     * `WEAPON_ICON` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val WEAPON_ICON = "maps/ui/settlement-info/mark61.png"
    /**
     * `ARMOR_ICON` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val ARMOR_ICON = "maps/ui/settlement-info/mark62.png"

    /**
     * `BOX1_CAP_INSET` (상태 값): 테두리 프레임의 9분할 여백이다.
     *
     * box1.png는 20x20이고 바깥 2px만 불투명한 테두리, 안쪽 16x16은 완전히 투명하다.
     * 배경 색은 아래 깔리는 bg2가 담당하므로 여백을 테두리 두께에 맞춰 고정한다.
     */
    private const val BOX1_CAP_INSET = 2

    /**
     * `PROGRESS_BG_CAP_INSET` (상태 값): 진행 막대 바탕의 9분할 여백이다.
     *
     * progress-bg.png는 60x15이며 네 모서리 1px이 비어 있고 그 안쪽 2px이 밝기가 다른
     * 베벨이다. 3px을 고정해야 둥근 모서리와 베벨이 늘어나지 않는다.
     */
    private const val PROGRESS_BG_CAP_INSET = 3


    /**
     * `sprites`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun sprites(panel: Panel): List<Sprite> = buildList {
        val height = if (panel == Panel.MINE) 258f else 193.5f
        add(Sprite(BG2, ROOT_X, ROOT_Y, ROOT_W, height))
        add(Sprite(BOX1, ROOT_X, ROOT_Y, ROOT_W, if (panel == Panel.MINE) 257.5f else 193f, BOX1_CAP_INSET))
        when (panel) {
            Panel.MINE -> {
                stat(HP_ICON, HP_BAR, 747.5f, 251f, 805.5f, 249f)
                stat(MP_ICON, MP_BAR, 747.5f, 200f, 805.5f, 198f)
                stat(EXP_ICON, EXP_BAR, 747.5f, 149f, 805.5f, 147f)
                add(Sprite(WEAPON_ICON, 769.5f, 108f, 30f, 30f))
                add(Sprite(ARMOR_ICON, 919.5f, 107f, 32f, 32f))
            }

            Panel.OTHER -> {
                stat(HP_ICON, HP_BAR, 747.5f, 179.75f, 808.5f, 177.75f)
                stat(MP_ICON, MP_BAR, 746.5f, 121.75f, 808.5f, 119.75f)
            }
        }
    }

    /**
     * `MutableList`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun MutableList<Sprite>.stat(
        icon: String,
        bar: String,
        iconX: Float,
        iconY: Float,
        backgroundX: Float,
        backgroundY: Float
    ) {
        /**
         * `iconHeight` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val iconHeight = when (icon) {
            HP_ICON -> 40f
            else -> 48f
        }
        add(Sprite(icon, iconX, iconY, 48f, iconHeight))
        add(Sprite(PROGRESS_BG, backgroundX, backgroundY, 374f, 24f, PROGRESS_BG_CAP_INSET))
        add(Sprite(bar, backgroundX + 2f, backgroundY + 2f, 370f, 20f))
    }
}

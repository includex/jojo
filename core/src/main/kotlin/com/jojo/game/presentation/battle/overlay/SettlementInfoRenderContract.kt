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

    data class Sprite(val path: String, val x: Float, val y: Float, val width: Float, val height: Float)
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
     * `sprites`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun sprites(panel: Panel): List<Sprite> = buildList {
        val height = if (panel == Panel.MINE) 258f else 193.5f
        add(Sprite(BG2, ROOT_X, ROOT_Y, ROOT_W, height))
        add(Sprite(BOX1, ROOT_X, ROOT_Y, ROOT_W, if (panel == Panel.MINE) 257.5f else 193f))
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
        add(Sprite(PROGRESS_BG, backgroundX, backgroundY, 374f, 24f))
        add(Sprite(bar, backgroundX + 2f, backgroundY + 2f, 370f, 20f))
    }
}

package com.jojo.game

/**
 * Exact MineUnitInfoLayer / OtherUnitInfoLayer prefab draw inventory.
 *
 * This is a UI-only contract: it neither owns settlement state nor draws from
 * BattleScreen.  Asset paths correspond to the recovered SpriteFrame chains
 * exported by `export_map_assets.py`.
 */
/**
 * object  `SettlementInfoRenderContract`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object SettlementInfoRenderContract {
    /**
     * data class  `Sprite`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Sprite(val path: String, val x: Float, val y: Float, val width: Float, val height: Float)

    /**
     * enum class  `Panel`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Panel { MINE, OTHER }

    private const val ROOT_X = 736f
    private const val ROOT_Y = 96f
    private const val ROOT_W = 471f

    private const val BG2 = "maps/ui/settlement-info/bg2.png"
    private const val BOX1 = "maps/ui/settlement-info/box1.png"
    private const val PROGRESS_BG = "maps/ui/settlement-info/progress-bg.png"
    private const val HP_BAR = "maps/ui/settlement-info/mark3.png"
    private const val MP_BAR = "maps/ui/settlement-info/mark2.png"
    private const val EXP_BAR = "maps/ui/settlement-info/mark6.png"
    private const val HP_ICON = "maps/ui/settlement-info/mark7.png"
    private const val MP_ICON = "maps/ui/settlement-info/mark8.png"
    private const val EXP_ICON = "maps/ui/settlement-info/mark9.png"
    private const val WEAPON_ICON = "maps/ui/settlement-info/mark61.png"
    private const val ARMOR_ICON = "maps/ui/settlement-info/mark62.png"

    /**
     * 공개 메서드 `sprites`
     *
     * ### 파라미터
    - `panel` (`Panel`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Sprite>`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    private fun MutableList<Sprite>.stat(
        icon: String,
        bar: String,
        iconX: Float,
        iconY: Float,
        backgroundX: Float,
        backgroundY: Float
    ) {
        val iconHeight = when (icon) {
            HP_ICON -> 40f
            else -> 48f
        }
        add(Sprite(icon, iconX, iconY, 48f, iconHeight))
        add(Sprite(PROGRESS_BG, backgroundX, backgroundY, 374f, 24f))
        add(Sprite(bar, backgroundX + 2f, backgroundY + 2f, 370f, 20f))
    }
}

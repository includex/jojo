package com.jojo.port

/**
 * Exact MineUnitInfoLayer / OtherUnitInfoLayer prefab draw inventory.
 *
 * This is a UI-only contract: it neither owns settlement state nor draws from
 * BattleLayer.  Asset paths correspond to the recovered SpriteFrame chains
 * exported by `export_map_assets.py`.
 */
object SettlementInfoRenderContract {
    data class Sprite(val path: String, val x: Float, val y: Float, val width: Float, val height: Float)

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

    private fun MutableList<Sprite>.stat(icon: String, bar: String, iconX: Float, iconY: Float, backgroundX: Float, backgroundY: Float) {
        val iconHeight = when (icon) {
            HP_ICON -> 40f
            else -> 48f
        }
        add(Sprite(icon, iconX, iconY, 48f, iconHeight))
        add(Sprite(PROGRESS_BG, backgroundX, backgroundY, 374f, 24f))
        add(Sprite(bar, backgroundX + 2f, backgroundY + 2f, 370f, 20f))
    }
}

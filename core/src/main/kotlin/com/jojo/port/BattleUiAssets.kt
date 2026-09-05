package com.jojo.port

/**
 * Authored Cocos SpriteFrame paths used by the battle's modal UI.
 *
 * The source routes use one-based resource names (`Game/Magic/${icon + 1}-1`,
 * etc.).  Keeping that conversion here prevents renderers from substituting
 * a generic icon or from accidentally treating a magic id as an icon id.
 */
object BattleUiAssets {
    const val CHOICE_PANEL = "maps/ui/choice-panel.png"
    const val CHOICE_ROW = "maps/ui/choice-row.png"
    const val MP_CURRENT_MARK = "maps/marks/1.png"
    const val MP_MAX_MARK = "maps/marks/2.png"

    fun magicIcon(icon: Int): String = "maps/magic-icons/${icon + 1}.png"
    fun hitArea(hitArea: Int): String = "maps/magic-hitareas/${hitArea + 1}.png"
    fun effectArea(effectArea: Int): String = "maps/magic-effareas/${effectArea + 1}.png"
}

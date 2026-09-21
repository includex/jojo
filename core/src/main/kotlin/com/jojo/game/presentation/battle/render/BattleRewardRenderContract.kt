package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Color

/** 원본 BattleLayer 보상 라벨의 프리팹 색. */
object BattleRewardRenderContract {
    const val END_SHADOW_HEX = "#686868"
    const val MONEY_SHADOW_HEX = "#c85500"
    const val MONEY_TEXT_HEX = "#f0f050"
    const val LOOT_SHADOW_HEX = "#0d10ca"
    const val WHITE_HEX = "#ffffff"
    const val SECTION_SHADOW_HEX = "#000000"
    const val ITEM_NAME_HEX = "#001070"

    val endShadow = Color.valueOf("686868ff")
    val moneyShadow = Color.valueOf("c85500ff")
    val moneyText = Color.valueOf("f0f050ff")
    val lootShadow = Color.valueOf("0d10caff")
    val white = Color.WHITE
    val sectionShadow = Color.BLACK
    val itemName = Color.valueOf("001070ff")
}

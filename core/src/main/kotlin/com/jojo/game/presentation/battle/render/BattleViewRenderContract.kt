package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Color

/** BattleView 프리팹의 배경·마커·라벨 색과 선택 투명도. */
object BattleViewRenderContract {
    const val WHITE_HEX = "#ffffff"
    const val BLACK_HEX = "#000000"
    const val SELECTED_HEX = "#ff0000"
    const val SELECTED_OPACITY = 1f
    const val UNSELECTED_OPACITY = 128f / 255f

    val white = Color.WHITE
    val black = Color.BLACK
    val selected = Color.RED
}

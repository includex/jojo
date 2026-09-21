package com.jojo.game.presentation.battle.edit

import com.badlogic.gdx.graphics.Color

/** 전장 편집 프리팹의 노드 색을 실제 그리기와 렌더 증거에 제공한다. */
object BattleEditRenderContract {
    const val WHITE_HEX = "#ffffff"
    const val BLACK_HEX = "#000000"
    const val HEADER_HEX = "#97fee7"
    val header = Color.valueOf("97fee7ff")

    fun color(path: String, type: String): String = when {
        path == "Canvas/Layer/bg/bg1" -> HEADER_HEX
        type == "label" || path == "Canvas/Layer/Panel_cancel" || path.endsWith("/panel0/bg") -> BLACK_HEX
        else -> WHITE_HEX
    }
}

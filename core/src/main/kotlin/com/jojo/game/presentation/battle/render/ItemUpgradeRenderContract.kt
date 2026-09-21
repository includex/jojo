package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Color

/** 강화창의 본문 라벨 색. 그리기와 렌더 증거가 이 값을 함께 쓴다. */
internal object ItemUpgradeRenderContract {
    const val LABEL_HEX = "#000000"
    const val OWNER_HEX = "#0000f0"
    val labelColor: Color = Color.BLACK
    val ownerColor: Color = Color.valueOf("0000f0ff")
}

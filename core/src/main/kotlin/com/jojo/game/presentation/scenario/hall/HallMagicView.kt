package com.jojo.game.presentation.scenario.hall

import com.jojo.game.MagicUiList

internal data class HallMagicView(
    val name: String,
    val power: Int,
    val cost: Int,
    val intro: String,
    val iconFrame: Int,
    val hitAreaFrame: Int,
    val effectAreaFrame: Int,
) {
    companion object {
        fun from(magic: MagicUiList.Magic) = HallMagicView(
            name = magic.name,
            power = magic.power ?: 0,
            cost = magic.cost,
            intro = magic.intro,
            iconFrame = magic.icon + 1,
            hitAreaFrame = magic.hit + 1,
            effectAreaFrame = magic.eff + 1,
        )
    }
}

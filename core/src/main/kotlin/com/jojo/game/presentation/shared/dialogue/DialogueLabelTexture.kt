package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.graphics.Texture

/** A whole-string Canvas raster and its unrounded Cocos Label node dimensions. */
data class DialogueLabelTexture(
    val texture: Texture,
    val nodeWidth: Float,
    val nodeHeight: Float,
)

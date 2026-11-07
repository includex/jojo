package com.jojo.game.presentation.battle.overlay

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4

/** World dimensions of the active orthographic UI projection. */
internal val SpriteBatch.overlayWidth: Float
    get() = 2f / projectionMatrix.`val`[Matrix4.M00]
internal val SpriteBatch.overlayHeight: Float
    get() = 2f / projectionMatrix.`val`[Matrix4.M11]

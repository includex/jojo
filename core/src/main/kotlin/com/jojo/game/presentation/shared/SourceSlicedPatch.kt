package com.jojo.game.presentation.shared

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlin.math.min

/** Draws a Cocos Creator sliced sprite with its source UV and small-node border rules. */
internal class SourceSlicedPatch(
    private val region: TextureRegion,
    private val insetLeft: Int,
    private val insetTop: Int,
    private val insetRight: Int,
    private val insetBottom: Int,
) : NinePatch(region) {
    private val vertices = FloatArray(20)

    override fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float) {
        drawSource(batch, x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
    }

    /** Preserves source double arithmetic until each submitted vertex is converted to Float32. */
    fun drawSource(batch: Batch, x: Double, y: Double, width: Double, height: Double) {
        val horizontalScale = if (insetLeft + insetRight == 0) 1.0 else min(1.0, width / (insetLeft + insetRight))
        val verticalScale = if (insetTop + insetBottom == 0) 1.0 else min(1.0, height / (insetTop + insetBottom))
        val xs = doubleArrayOf(x, x + insetLeft * horizontalScale, x + width - insetRight * horizontalScale, x + width)
        val ys = doubleArrayOf(y, y + insetBottom * verticalScale, y + height - insetTop * verticalScale, y + height)
        val du = (region.u2 - region.u) / region.regionWidth
        val dv = (region.v2 - region.v) / region.regionHeight
        val us = floatArrayOf(region.u, region.u + insetLeft * du, region.u2 - insetRight * du, region.u2)
        val vs = floatArrayOf(region.v2, region.v2 - insetBottom * dv, region.v + insetTop * dv, region.v)
        val batchColor = batch.color
        val patchColor = color
        val packedColor = Color.toFloatBits(
            batchColor.r * patchColor.r,
            batchColor.g * patchColor.g,
            batchColor.b * patchColor.b,
            batchColor.a * patchColor.a,
        )
        for (row in 0 until 3) for (column in 0 until 3) {
            drawCell(batch, xs[column], ys[row], xs[column + 1], ys[row + 1], us[column], vs[row], us[column + 1], vs[row + 1], packedColor)
        }
    }

    private fun drawCell(batch: Batch, x0: Double, y0: Double, x1: Double, y1: Double, u0: Float, v0: Float, u1: Float, v1: Float, color: Float) {
        if (x1 <= x0 || y1 <= y0) return
        // SpriteBatch's fixed indices preserve the source TL-to-BR diagonal.
        vertex(0, x1.toFloat(), y0.toFloat(), color, u1, v0)
        vertex(1, x1.toFloat(), y1.toFloat(), color, u1, v1)
        vertex(2, x0.toFloat(), y1.toFloat(), color, u0, v1)
        vertex(3, x0.toFloat(), y0.toFloat(), color, u0, v0)
        batch.draw(region.texture, vertices, 0, vertices.size)
    }

    private fun vertex(index: Int, x: Float, y: Float, color: Float, u: Float, v: Float) {
        val offset = index * 5
        vertices[offset] = x
        vertices[offset + 1] = y
        vertices[offset + 2] = color
        vertices[offset + 3] = u
        vertices[offset + 4] = v
    }
}

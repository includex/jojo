package com.jojo.game.presentation.shared

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Matrix4
import kotlin.math.min

/** CPU projection used only for axis-aligned 2D orthographic batch matrices. */
internal object SourceSlicedPatchClipMath {
    fun supportsCpuProjection(matrix: FloatArray): Boolean =
        matrix[Matrix4.M01] == 0f && matrix[Matrix4.M02] == 0f &&
            matrix[Matrix4.M10] == 0f && matrix[Matrix4.M12] == 0f &&
            matrix[Matrix4.M20] == 0f && matrix[Matrix4.M21] == 0f &&
            matrix[Matrix4.M30] == 0f && matrix[Matrix4.M31] == 0f &&
            matrix[Matrix4.M32] == 0f && matrix[Matrix4.M33] == 1f &&
            matrix[Matrix4.M00].isFinite() && matrix[Matrix4.M03].isFinite() &&
            matrix[Matrix4.M11].isFinite() && matrix[Matrix4.M13].isFinite() &&
            matrix[Matrix4.M22].isFinite() && matrix[Matrix4.M23].isFinite()

    /** Applies the observed rounding policy: source Float, Double multiply/add, result Float. */
    fun project(source: Double, scale: Float, translation: Float): Float {
        val sourceVertex = source.toFloat()
        return (sourceVertex.toDouble() * scale.toDouble() + translation.toDouble()).toFloat()
    }

    fun setIdentityXyProjection(target: Matrix4, combined: FloatArray) {
        target.idt()
        target.`val`[Matrix4.M23] = combined[Matrix4.M23]
    }
}

/** Draws a Cocos Creator sliced sprite with its source UV and small-node border rules. */
internal class SourceSlicedPatch(
    private val region: TextureRegion,
    private val insetLeft: Int,
    private val insetTop: Int,
    private val insetRight: Int,
    private val insetBottom: Int,
) : NinePatch(region) {
    private val vertices = FloatArray(20)
    private val xs = DoubleArray(4)
    private val ys = DoubleArray(4)
    private val us = FloatArray(4)
    private val vs = FloatArray(4)
    private val clip = Matrix4()
    private val savedProjection = Matrix4()
    private val savedTransform = Matrix4()
    private val cpuProjection = Matrix4()
    private val identityTransform = Matrix4()

    override fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float) {
        drawSource(batch, x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
    }

    /** Preserves source double arithmetic until each submitted vertex is converted to Float32. */
    fun drawSource(batch: Batch, x: Double, y: Double, width: Double, height: Double) {
        val horizontalScale = if (insetLeft + insetRight == 0) 1.0 else min(1.0, width / (insetLeft + insetRight))
        val verticalScale = if (insetTop + insetBottom == 0) 1.0 else min(1.0, height / (insetTop + insetBottom))
        xs[0] = x
        xs[1] = x + insetLeft * horizontalScale
        xs[2] = x + width - insetRight * horizontalScale
        xs[3] = x + width
        ys[0] = y
        ys[1] = y + insetBottom * verticalScale
        ys[2] = y + height - insetTop * verticalScale
        ys[3] = y + height
        val du = (region.u2 - region.u) / region.regionWidth
        val dv = (region.v2 - region.v) / region.regionHeight
        us[0] = region.u
        us[1] = region.u + insetLeft * du
        us[2] = region.u2 - insetRight * du
        us[3] = region.u2
        vs[0] = region.v2
        vs[1] = region.v2 - insetBottom * dv
        vs[2] = region.v + insetTop * dv
        vs[3] = region.v
        val batchColor = batch.color
        val patchColor = color
        val packedColor = Color.toFloatBits(
            batchColor.r * patchColor.r,
            batchColor.g * patchColor.g,
            batchColor.b * patchColor.b,
            batchColor.a * patchColor.a,
        )
        savedProjection.set(batch.projectionMatrix)
        savedTransform.set(batch.transformMatrix)
        clip.set(savedProjection).mul(savedTransform)
        if (!SourceSlicedPatchClipMath.supportsCpuProjection(clip.`val`)) {
            drawCells(batch, packedColor, projectOnCpu = false)
            return
        }

        SourceSlicedPatchClipMath.setIdentityXyProjection(cpuProjection, clip.`val`)
        try {
            batch.projectionMatrix = cpuProjection
            batch.transformMatrix = identityTransform
            drawCells(batch, packedColor, projectOnCpu = true)
        } finally {
            batch.projectionMatrix = savedProjection
            batch.transformMatrix = savedTransform
        }
    }

    private fun drawCells(batch: Batch, color: Float, projectOnCpu: Boolean) {
        for (row in 0 until 3) for (column in 0 until 3) {
            drawCell(
                batch,
                xs[column], ys[row], xs[column + 1], ys[row + 1],
                us[column], vs[row], us[column + 1], vs[row + 1],
                color,
                projectOnCpu,
            )
        }
    }

    private fun drawCell(
        batch: Batch,
        x0: Double,
        y0: Double,
        x1: Double,
        y1: Double,
        u0: Float,
        v0: Float,
        u1: Float,
        v1: Float,
        color: Float,
        projectOnCpu: Boolean,
    ) {
        if (x1 <= x0 || y1 <= y0) return
        // SpriteBatch's fixed indices preserve the source TL-to-BR diagonal.
        vertex(0, x1, y0, color, u1, v0, projectOnCpu)
        vertex(1, x1, y1, color, u1, v1, projectOnCpu)
        vertex(2, x0, y1, color, u0, v1, projectOnCpu)
        vertex(3, x0, y0, color, u0, v0, projectOnCpu)
        batch.draw(region.texture, vertices, 0, vertices.size)
    }

    private fun vertex(index: Int, x: Double, y: Double, color: Float, u: Float, v: Float, projectOnCpu: Boolean) {
        val offset = index * 5
        if (projectOnCpu) {
            val matrix = clip.`val`
            vertices[offset] = SourceSlicedPatchClipMath.project(x, matrix[Matrix4.M00], matrix[Matrix4.M03])
            vertices[offset + 1] = SourceSlicedPatchClipMath.project(y, matrix[Matrix4.M11], matrix[Matrix4.M13])
        } else {
            vertices[offset] = x.toFloat()
            vertices[offset + 1] = y.toFloat()
        }
        vertices[offset + 2] = color
        vertices[offset + 3] = u
        vertices[offset + 4] = v
    }
}

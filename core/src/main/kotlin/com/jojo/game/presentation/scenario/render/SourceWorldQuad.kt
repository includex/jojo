package com.jojo.game.presentation.scenario.render

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Matrix4
import com.jojo.game.presentation.shared.SourceSlicedPatchClipMath

/** Draws Hall quads from their source-canvas coordinates before Float vertex conversion. */
internal class SourceWorldQuad {
    private val vertices = FloatArray(20)
    private val combined = Matrix4()
    private val savedProjection = Matrix4()
    private val savedTransform = Matrix4()
    private val cpuProjection = Matrix4()
    private val identityTransform = Matrix4()
    private var sourceScaleX = 0f
    private var sourceScaleY = 0f
    private var active = false

    fun begin(batch: Batch): Boolean {
        savedProjection.set(batch.projectionMatrix)
        savedTransform.set(batch.transformMatrix)
        combined.set(savedProjection).mul(savedTransform)
        if (!SourceSlicedPatchClipMath.supportsCpuProjection(combined.`val`)) return false

        val matrix = combined.`val`
        sourceScaleX = (matrix[Matrix4.M00].toDouble() * SOURCE_TO_PORT_SCALE).toFloat()
        sourceScaleY = (matrix[Matrix4.M11].toDouble() * SOURCE_TO_PORT_SCALE).toFloat()
        SourceSlicedPatchClipMath.setIdentityXyProjection(cpuProjection, matrix)
        active = true
        try {
            batch.projectionMatrix = cpuProjection
            batch.transformMatrix = identityTransform
        } catch (failure: Throwable) {
            try { end(batch) } catch (cleanup: Throwable) { failure.addSuppressed(cleanup) }
            throw failure
        }
        return true
    }

    fun end(batch: Batch) {
        if (!active) return
        try {
            batch.projectionMatrix = savedProjection
        } finally {
            try {
                batch.transformMatrix = savedTransform
            } finally {
                active = false
            }
        }
    }

    fun draw(
        batch: Batch,
        texture: Texture,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        uLeft: Float,
        vBottom: Float,
        uRight: Float,
        vTop: Float,
    ) {
        val right = x + width
        val top = y + height
        val color = batch.packedColor
        vertex(0, x, y, color, uLeft, vBottom)
        vertex(1, x, top, color, uLeft, vTop)
        vertex(2, right, top, color, uRight, vTop)
        vertex(3, right, y, color, uRight, vBottom)
        batch.draw(texture, vertices, 0, vertices.size)
    }

    fun drawCorners(
        batch: Batch,
        texture: Texture,
        left: Float,
        bottom: Float,
        right: Float,
        top: Float,
        uLeft: Float,
        vBottom: Float,
        uRight: Float,
        vTop: Float,
    ) {
        val color = batch.packedColor
        vertex(0, left.toDouble(), bottom.toDouble(), color, uLeft, vBottom)
        vertex(1, left.toDouble(), top.toDouble(), color, uLeft, vTop)
        vertex(2, right.toDouble(), top.toDouble(), color, uRight, vTop)
        vertex(3, right.toDouble(), bottom.toDouble(), color, uRight, vBottom)
        batch.draw(texture, vertices, 0, vertices.size)
    }

    private fun vertex(index: Int, x: Double, y: Double, color: Float, u: Float, v: Float) {
        val offset = index * 5
        val matrix = combined.`val`
        vertices[offset] = SourceSlicedPatchClipMath.project(x, sourceScaleX, matrix[Matrix4.M03])
        vertices[offset + 1] = SourceSlicedPatchClipMath.project(y, sourceScaleY, matrix[Matrix4.M13])
        vertices[offset + 2] = color
        vertices[offset + 3] = u
        vertices[offset + 4] = v
    }

    private companion object {
        const val SOURCE_TO_PORT_SCALE = 0.86
    }
}

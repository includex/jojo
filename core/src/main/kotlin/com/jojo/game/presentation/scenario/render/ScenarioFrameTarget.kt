package com.jojo.game.presentation.scenario.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.Disposable

/** Compose into RGBA8 before presentation, matching the source's observed framebuffer rasterization. */
internal class ScenarioFrameTarget : Disposable {
    private var target: FrameBuffer? = null
    private val copyBatch = SpriteBatch()
    private val copyProjection = Matrix4()
    private val incomingFramebuffer = BufferUtils.newIntBuffer(1)
    private val incomingViewport = BufferUtils.newIntBuffer(4)
    private val sceneViewport = BufferUtils.newIntBuffer(4)

    fun render(drawScene: () -> Unit) {
        val width = Gdx.graphics.backBufferWidth
        val height = Gdx.graphics.backBufferHeight
        if (width <= 0 || height <= 0) return
        Gdx.gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, incomingFramebuffer)
        Gdx.gl.glGetIntegerv(GL20.GL_VIEWPORT, incomingViewport)
        lateinit var frame: FrameBuffer
        try {
            frame = target?.takeIf { it.width == width && it.height == height } ?: run {
                val replacement = FrameBuffer(Pixmap.Format.RGBA8888, width, height, false)
                replacement.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                target?.dispose()
                target = replacement
                replacement
            }
            frame.begin()
            drawScene()
            Gdx.gl.glGetIntegerv(GL20.GL_VIEWPORT, sceneViewport)
        } finally {
            Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, incomingFramebuffer.get(0))
            Gdx.gl.glViewport(incomingViewport.get(0), incomingViewport.get(1), incomingViewport.get(2), incomingViewport.get(3))
        }
        // One physical texel per backbuffer pixel, without a second blend or filter operation.
        try {
            Gdx.gl.glViewport(0, 0, width, height)
            copyProjection.setToOrtho2D(0f, 0f, width.toFloat(), height.toFloat())
            copyBatch.projectionMatrix = copyProjection
            copyBatch.color = Color.WHITE
            copyBatch.disableBlending()
            copyBatch.begin()
            copyBatch.draw(frame.colorBufferTexture, 0f, 0f, width.toFloat(), height.toFloat(),
                0, 0, width, height, false, true)
        } finally {
            try {
                if (copyBatch.isDrawing) copyBatch.end()
            } finally {
                Gdx.gl.glViewport(sceneViewport.get(0), sceneViewport.get(1), sceneViewport.get(2), sceneViewport.get(3))
            }
        }
    }

    override fun dispose() {
        target?.dispose()
        target = null
        copyBatch.dispose()
    }
}

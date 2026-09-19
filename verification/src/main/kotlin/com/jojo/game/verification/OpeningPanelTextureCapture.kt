package com.jojo.game.verification

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import java.security.MessageDigest

/** Verification-only readback of the actual cached InfoLayer atlas region and its padding. */
internal object OpeningPanelTextureCapture {
    fun capture(directory: FileHandle) {
        fun field(owner: Any, name: String): Any = owner.javaClass.getDeclaredField(name).let {
            it.isAccessible = true; requireNotNull(it.get(owner))
        }
        val screen = (Gdx.app.applicationListener as Game).screen
        val assets = field(screen, "sceneAssets")
        val patch = field(assets, "cachedInfoPanelPatch")
        val region = field(patch, "region") as TextureRegion
        val texture = region.texture
        val x = region.regionX - 1
        val y = region.regionY - 1
        val width = region.regionWidth + 2
        val height = region.regionHeight + 2
        val gl = Gdx.gl
        val previous = BufferUtils.newIntBuffer(1)
        gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, previous)
        val framebuffer = gl.glGenFramebuffer()
        val pixels = BufferUtils.newByteBuffer(width * height * 4)
        try {
            gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer)
            gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D, texture.textureObjectHandle, 0)
            check(gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER) == GL20.GL_FRAMEBUFFER_COMPLETE)
            gl.glReadPixels(x, y, width, height, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels)
        } finally {
            gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, previous[0])
            gl.glDeleteFramebuffer(framebuffer)
        }
        val bytes = ByteArray(width * height * 4)
        pixels.rewind(); pixels.get(bytes)
        val file = "game-panel-texture.rgba"
        directory.child(file).writeBytes(bytes, false)
        val manifest = JsonValue(JsonValue.ValueType.`object`)
        fun scalar(key: String, value: String) = manifest.addChild(key, JsonValue(value))
        fun array(key: String, values: List<Number>) {
            val row = JsonValue(JsonValue.ValueType.array)
            values.forEach { row.addChild(JsonValue(it.toDouble())) }
            manifest.addChild(key, row)
        }
        scalar("contract", "opening-panel-texture/v1")
        scalar("file", file)
        scalar("sha256", MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) })
        scalar("origin", "bottom-left texture coordinates")
        scalar("minFilter", texture.minFilter.name); scalar("magFilter", texture.magFilter.name)
        array("atlasSize", listOf(texture.width, texture.height))
        array("region", listOf(region.regionX, region.regionY, region.regionWidth, region.regionHeight))
        array("cropRect", listOf(x, y, width, height))
        array("uv", listOf(region.u, region.v, region.u2, region.v2))
        val batch = field(screen, "batch") as SpriteBatch
        array("projection", batch.projectionMatrix.`val`.toList())
        array("projectionWithSourceScale", Matrix4(batch.projectionMatrix).scale(.86f, .86f, 1f).`val`.toList())
        directory.child("game-panel-texture.json").writeString(manifest.prettyPrint(JsonWriter.OutputType.json, 120), false)
    }
}

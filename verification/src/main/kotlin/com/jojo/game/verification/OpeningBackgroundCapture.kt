package com.jojo.game.verification

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FileTextureData
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import java.security.MessageDigest

/** Opt-in asset diagnostic; reads the already rendered texture without consuming its TextureData. */
internal object OpeningBackgroundCapture {
    fun capture(directory: FileHandle, backgroundId: Int) {
        fun field(owner: Any, name: String): Any = owner.javaClass.getDeclaredField(name).let {
            it.isAccessible = true
            requireNotNull(it.get(owner))
        }
        val screen = (Gdx.app.applicationListener as Game).screen
        check(screen.javaClass.simpleName == "ScenarioScreen")
        val assets = field(screen, "sceneAssets")
        val cached = field(field(assets, "backgroundTextures"), "values") as Map<*, *>
        val texture = requireNotNull(cached[backgroundId] as? Texture) { "Background was not rendered" }
        val input = (texture.textureData as FileTextureData).fileHandle
        val path = input.path()
        val sourceBytes = input.readBytes()
        val pixmap = Pixmap(input)
        val width = texture.width
        val height = texture.height
        val cpu = ByteArray(width * height * 4)
        try {
            check(pixmap.width == width && pixmap.height == height)
            var index = 0
            val shifts = intArrayOf(24, 16, 8, 0)
            for (y in 0 until height) for (x in 0 until width) {
                val rgba = pixmap.getPixel(x, y)
                for (shift in shifts) cpu[index++] = (rgba ushr shift).toByte()
            }
        } finally { pixmap.dispose() }
        val gl = Gdx.gl
        val previous = BufferUtils.newIntBuffer(1)
        gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, previous)
        val framebuffer = gl.glGenFramebuffer()
        val pixels = BufferUtils.newByteBuffer(cpu.size)
        try {
            gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer)
            gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D, texture.textureObjectHandle, 0)
            check(gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER) == GL20.GL_FRAMEBUFFER_COMPLETE)
            gl.glReadPixels(0, 0, width, height, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels)
        } finally {
            gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, previous[0])
            gl.glDeleteFramebuffer(framebuffer)
        }
        val gpu = ByteArray(cpu.size)
        pixels.rewind(); pixels.get(gpu)
        fun hash(bytes: ByteArray) = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
        fun raw(name: String, bytes: ByteArray): JsonValue {
            directory.child(name).writeBytes(bytes, false)
            return JsonValue(JsonValue.ValueType.`object`).also {
                it.addChild("file", JsonValue(name)); it.addChild("sha256", JsonValue(hash(bytes)))
                it.addChild("byteLength", JsonValue(bytes.size.toLong()))
            }
        }
        val manifest = JsonValue(JsonValue.ValueType.`object`)
        manifest.addChild("contract", JsonValue("opening-background-texture/v1"))
        manifest.addChild("backgroundId", JsonValue(backgroundId.toLong()))
        manifest.addChild("asset", JsonValue(path))
        manifest.addChild("assetSha256", JsonValue(hash(sourceBytes)))
        manifest.addChild("jpegSha256", JsonValue(hash(Gdx.files.internal("maps/$backgroundId.jpg").readBytes())))
        manifest.addChild("width", JsonValue(width.toLong())); manifest.addChild("height", JsonValue(height.toLong()))
        manifest.addChild("origin", JsonValue("top-left image rows; GPU texture y=0 is uploaded first row"))
        manifest.addChild("gpuEvidence", JsonValue("already rendered ScenarioScreen background texture attached to temporary FBO"))
        manifest.addChild("textureHandle", JsonValue(texture.textureObjectHandle.toLong()))
        manifest.addChild("minFilter", JsonValue(texture.minFilter.name)); manifest.addChild("magFilter", JsonValue(texture.magFilter.name))
        manifest.addChild("cpu", raw("game-background-cpu.rgba", cpu))
        manifest.addChild("gpu", raw("game-background-gpu.rgba", gpu))
        directory.child("game-background.json").writeString(manifest.prettyPrint(JsonWriter.OutputType.json, 120), false)
    }
}

package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import java.io.File
import java.security.MessageDigest

/** Read existing GPU textures after the natural framebuffer capture; never load an asset. */
internal object OpeningUnitTextureCapture {
    fun capture(screen: Any, directory: File) {
        fun field(owner: Any, name: String): Any = owner.javaClass.getDeclaredField(name).let {
            it.isAccessible = true; requireNotNull(it.get(owner))
        }
        val assets = field(screen, "sceneAssets")
        val textures = field(field(assets, "unitTextures"), "values") as Map<*, *>
        val report = JsonValue(JsonValue.ValueType.`object`)
        report.addChild("contract", JsonValue("opening-unit-gpu-textures/v1"))
        report.addChild("origin", JsonValue("bottom-left texture coordinates"))
        val rows = JsonValue(JsonValue.ValueType.array)
        for ((actorId, assetId) in listOf(0 to 1, 157 to 316, 181 to 364, 182 to 365)) {
            val texture = textures[assetId] as Texture
            val gl = Gdx.gl
            val previous = BufferUtils.newIntBuffer(1)
            gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, previous)
            val framebuffer = gl.glGenFramebuffer()
            val pixels = BufferUtils.newByteBuffer(texture.width * texture.height * 4)
            try {
                gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer)
                gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D, texture.textureObjectHandle, 0)
                check(gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER) == GL20.GL_FRAMEBUFFER_COMPLETE)
                gl.glReadPixels(0, 0, texture.width, texture.height, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels)
            } finally {
                gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, previous[0])
                gl.glDeleteFramebuffer(framebuffer)
            }
            val bytes = ByteArray(pixels.capacity())
            pixels.rewind(); pixels.get(bytes)
            val file = "game-unit-$actorId-texture.rgba"
            File(directory, file).writeBytes(bytes)
            val row = JsonValue(JsonValue.ValueType.`object`)
            row.addChild("actorId", JsonValue(actorId.toLong()))
            row.addChild("assetId", JsonValue(assetId.toLong()))
            row.addChild("width", JsonValue(texture.width.toLong()))
            row.addChild("height", JsonValue(texture.height.toLong()))
            row.addChild("file", JsonValue(file))
            row.addChild("sha256", JsonValue(MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }))
            row.addChild("minFilter", JsonValue(texture.minFilter.name))
            row.addChild("magFilter", JsonValue(texture.magFilter.name))
            rows.addChild(row)
        }
        report.addChild("textures", rows)
        File(directory, "game-unit-textures.json").writeText(report.prettyPrint(JsonWriter.OutputType.json, 120))
    }
}

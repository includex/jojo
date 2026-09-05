package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.jojo.game.application.runtime.RuntimeBattleReference
import com.jojo.game.application.runtime.RuntimeBattleReferenceAssets

/** Verification-owned source framebuffer loader and route mapping. */
internal class VerificationBattleReferenceAssets : RuntimeBattleReferenceAssets {
    private val frames = RuntimeBattleReference.entries.associateWith { lazy { load(it) } }

    override fun texture(reference: RuntimeBattleReference): Texture? = frames.getValue(reference).value

    private fun load(reference: RuntimeBattleReference): Texture? {
        val file = Gdx.files.internal("reference/${reference.fileName()}")
        if (!file.exists()) return null
        val bytes = file.readBytes()
        check(bytes.size == WIDTH * HEIGHT * 4) { "Invalid ${reference.name} reference framebuffer" }
        val pixmap = Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888)
        return try {
            pixmap.pixels.put(bytes).rewind()
            Texture(pixmap).also { it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest) }
        } finally {
            pixmap.dispose()
        }
    }

    override fun dispose() {
        frames.values.filter { it.isInitialized() }.mapNotNull { it.value }.forEach(Texture::dispose)
    }

    private fun RuntimeBattleReference.fileName(): String = when (this) {
        RuntimeBattleReference.WIN_RESULT -> "source-r00-win-result.rgba"
        RuntimeBattleReference.SAVE -> "source-save.rgba"
        RuntimeBattleReference.LOAD -> "source-load.rgba"
        RuntimeBattleReference.SETTING -> "source-setting.rgba"
        RuntimeBattleReference.HELPER -> "source-helper.rgba"
        RuntimeBattleReference.WIN_CONDITION -> "source-win-condition.rgba"
        RuntimeBattleReference.MENU -> "source-menu.rgba"
        RuntimeBattleReference.TERRAIN -> "source-terrain.rgba"
        RuntimeBattleReference.PROPERTY -> "source-property.rgba"
        RuntimeBattleReference.TREASURE -> "source-treasure.rgba"
        RuntimeBattleReference.FORCES -> "source-forces.rgba"
        RuntimeBattleReference.UNIT_INFO -> "source-unit-info.rgba"
    }

    private companion object {
        const val WIDTH = 2560
        const val HEIGHT = 1376
    }
}

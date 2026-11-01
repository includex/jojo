package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.jojo.game.presentation.shared.KoreanFont
import kotlin.math.roundToInt
import com.jojo.game.infrastructure.data.ScenarioDialogueTextCatalog

/** Renderer-owned resources; loading and glyph expansion happen only when actually drawn. */
internal class DialogueUiResources : Disposable {
    private val textures = mutableMapOf<String, Texture>()
    private val patches = mutableMapOf<Pair<Texture, DialogueNinePatchConfig>, NinePatch>()
    private data class FontEntry(val characters: Set<Char>, val font: BitmapFont)
    private val fonts = mutableMapOf<Pair<DialogueFontConfig, BitmapFont>, FontEntry>()
    private val legacyPatches = mutableMapOf<List<Any>, NinePatch>()
    private var pixel: Texture? = null

    fun font(batch: SpriteBatch, config: DialogueFontConfig, fallback: BitmapFont, text: String, fillColor: Color): BitmapFont {
        val key = config to fallback
        val previous = fonts[key]
        if (previous != null && text.all { it in previous.characters }) return previous.font
        // Queued vertices can still reference the previous glyph atlas.
        batch.flush()
        val characters = previous?.characters.orEmpty() + ScenarioDialogueTextCatalog.fontCharacters.toSet() + text.toSet()
        val inherited = KoreanFont.settings(fallback)
        val font = KoreanFont.create(
            config.size ?: inherited?.size ?: (fallback.data.lineHeight / fallback.data.scaleY).roundToInt().coerceAtLeast(1),
            characters.joinToString(""), config.borderWidth ?: inherited?.borderWidth ?: 0f,
            config.borderColor?.let(Color::valueOf) ?: inherited?.borderColor ?: Color.BLACK,
            config.color?.let(Color::valueOf) ?: fillColor, config.faceIndex ?: when (config.bold) { true -> KoreanFont.BOLD_FACE; false -> 0; null -> inherited?.faceIndex ?: 0 },
            config.path,
        )
        font.data.setScale(config.scaleX ?: fallback.data.scaleX, config.scaleY ?: fallback.data.scaleY)
        previous?.font?.dispose()
        fonts[key] = FontEntry(characters, font)
        return font
    }

    fun drawBackground(batch: SpriteBatch, config: DialogueBackgroundConfig, placement: DialogueComponentPlacement, isLeft: Boolean): Boolean {
        config.fillColor?.let { color ->
            val white = pixel ?: run {
                val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
                pixmap.setColor(Color.WHITE); pixmap.fill()
                Texture(pixmap).also { pixmap.dispose(); pixel = it }
            }
            batch.color = Color.valueOf(color)
            batch.draw(white, placement.panelX, placement.panelY, placement.panelWidth, placement.panelHeight)
        }
        val path = if (isLeft) config.leftImage ?: config.rightImage else config.rightImage ?: config.leftImage
        val texture = path?.let { textures.getOrPut(it) { Texture(Gdx.files.internal(it)) } }
        if (texture != null) {
            batch.color = config.tint?.let(Color::valueOf) ?: Color.WHITE
            if (config.ninePatch.enabled) {
                val patch = patches.getOrPut(texture to config.ninePatch) {
                    val n = config.ninePatch
                    require(n.left + n.right <= texture.width && n.top + n.bottom <= texture.height) { "Dialogue nine-patch borders exceed image dimensions: $path" }
                    NinePatch(texture, n.left, n.right, n.top, n.bottom)
                }
                patch.draw(batch, placement.panelX, placement.panelY, placement.panelWidth, placement.panelHeight)
            } else batch.draw(texture, placement.panelX, placement.panelY, placement.panelWidth, placement.panelHeight)
        }
        batch.color = Color.WHITE
        return texture != null || config.fillColor != null
    }

    fun drawLegacyPatch(batch: SpriteBatch, region: TextureRegion, config: DialogueBackgroundConfig, placement: DialogueComponentPlacement) {
        val patch = legacyPatches.getOrPut(listOf(region.texture, region.u, region.v, region.u2, region.v2, config.ninePatch)) {
            val n = config.ninePatch
            require(n.left + n.right <= region.regionWidth && n.top + n.bottom <= region.regionHeight) { "Dialogue nine-patch borders exceed panel dimensions" }
            NinePatch(region, n.left, n.right, n.top, n.bottom)
        }
        batch.color = config.tint?.let(Color::valueOf) ?: Color.WHITE
        patch.draw(batch, placement.panelX, placement.panelY, placement.panelWidth, placement.panelHeight)
        batch.color = Color.WHITE
    }

    override fun dispose() {
        fonts.values.forEach { it.font.dispose() }; fonts.clear()
        textures.values.forEach(Texture::dispose); textures.clear(); patches.clear(); legacyPatches.clear()
        pixel?.dispose(); pixel = null
    }
}

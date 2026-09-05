package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch

/** Owns the render resources which live for one [ScenarioScreen] instance. */
internal class ScenarioSceneAssets(
    private val requiredGlyphsProvider: () -> String,
) {
    private val requiredGlyphs by lazy(requiredGlyphsProvider)
    private val portraitTextures = ScenarioSceneAssetCache<Int, Texture>(Texture::dispose)
    private val backgroundTextures = ScenarioSceneAssetCache<Int, Texture>(Texture::dispose)
    private val unitTextures = ScenarioSceneAssetCache<Int, Texture>(Texture::dispose)
    val hallMenuTextures = ScenarioSceneTextureCache()

    private var cachedTitleFont: BitmapFont? = null
    private var cachedSectionFont: BitmapFont? = null
    private var cachedBodyFont: BitmapFont? = null
    private var cachedSmallUiFont: BitmapFont? = null
    private var cachedStreetDialogueFont: BitmapFont? = null
    private var cachedStreetSpeakerFont: BitmapFont? = null
    private var cachedOverlayPixel: Texture? = null
    private var cachedChoicePanelTexture: Texture? = null
    private var choicePanelLoaded = false
    private var cachedChoiceRowTexture: Texture? = null
    private var choiceRowLoaded = false
    private var cachedDialoguePanelTexture: Texture? = null
    private var dialoguePanelLoaded = false
    private var cachedStreetSpeechBubbleTexture: Texture? = null
    private var streetSpeechBubbleLoaded = false
    private var cachedInfoPanelPatch: NinePatch? = null
    private var infoPanelPatchLoaded = false
    private var disposed = false

    val titleFont: BitmapFont
        get() = cachedTitleFont ?: KoreanFont.create(34, requiredGlyphs).also { cachedTitleFont = it }
    val sectionFont: BitmapFont
        get() = cachedSectionFont ?: KoreanFont.create(86, requiredGlyphs).also { cachedSectionFont = it }
    val bodyFont: BitmapFont
        get() = cachedBodyFont ?: KoreanFont.create(34, requiredGlyphs).also { cachedBodyFont = it }
    val smallUiFont: BitmapFont
        get() = cachedSmallUiFont ?: KoreanFont.create(19, requiredGlyphs).also { cachedSmallUiFont = it }
    val streetDialogueFont: BitmapFont
        get() = cachedStreetDialogueFont ?: KoreanFont.create(31, requiredGlyphs).also {
            it.data.setScale(544f / 540f, 60f / 56f)
            cachedStreetDialogueFont = it
        }
    val streetSpeakerFont: BitmapFont
        get() = cachedStreetSpeakerFont ?: KoreanFont.create(
            31,
            requiredGlyphs,
            borderWidth = 2f,
            borderColor = Color(102f / 255f, 1f, 1f, 1f),
            fillColor = Color(35f / 255f, 2f / 255f, 234f / 255f, 1f),
        ).also {
            it.data.setScale(110f / 116f, 1f)
            cachedStreetSpeakerFont = it
        }
    val overlayPixel: Texture
        get() = cachedOverlayPixel ?: Pixmap(1, 1, Pixmap.Format.RGBA8888).let { pixmap ->
            pixmap.setColor(Color.WHITE)
            pixmap.fill()
            Texture(pixmap).also {
                pixmap.dispose()
                cachedOverlayPixel = it
            }
        }
    val choicePanelTexture: Texture?
        get() {
            if (!choicePanelLoaded) {
                cachedChoicePanelTexture = loadTexture("maps/ui/choice-panel.png", Texture.TextureFilter.Nearest)
                choicePanelLoaded = true
            }
            return cachedChoicePanelTexture
        }
    val choiceRowTexture: Texture?
        get() {
            if (!choiceRowLoaded) {
                cachedChoiceRowTexture = loadTexture("maps/ui/choice-row.png", Texture.TextureFilter.Nearest)
                choiceRowLoaded = true
            }
            return cachedChoiceRowTexture
        }
    val dialoguePanelTexture: Texture?
        get() {
            if (!dialoguePanelLoaded) {
                cachedDialoguePanelTexture = loadTexture("maps/ui/dialogue-panel.png", Texture.TextureFilter.Linear)
                dialoguePanelLoaded = true
            }
            return cachedDialoguePanelTexture
        }
    val streetSpeechBubbleTexture: Texture?
        get() {
            if (!streetSpeechBubbleLoaded) {
                cachedStreetSpeechBubbleTexture = Gdx.files.internal("maps/ui/street-speech-bubble.png")
                    .takeIf { it.exists() }
                    ?.let(::Texture)
                streetSpeechBubbleLoaded = true
            }
            return cachedStreetSpeechBubbleTexture
        }
    val infoPanelPatch: NinePatch?
        get() {
            if (!infoPanelPatchLoaded) {
                cachedInfoPanelPatch = createInfoPanelPatch()
                infoPanelPatchLoaded = true
            }
            return cachedInfoPanelPatch
        }

    fun portraitTexture(characterId: Int): Texture? = portraitTextures[characterId] ?: loadTexture(
        "maps/heads/$characterId.png",
        Texture.TextureFilter.Linear,
    )?.also { portraitTextures[characterId] = it }

    fun backgroundTexture(backgroundId: Int): Texture? = backgroundTextures[backgroundId] ?: loadTexture(
        "maps/$backgroundId.jpg",
        Texture.TextureFilter.Linear,
    )?.also { backgroundTextures[backgroundId] = it }

    fun unitTexture(assetId: Int): Texture? = unitTextures[assetId] ?: loadTexture(
        "maps/hall-units/$assetId.png",
        Texture.TextureFilter.Linear,
    )?.also { unitTextures[assetId] = it }

    fun hallTexture(path: String): Texture? = hallMenuTextures[path] ?: loadTexture(
        path,
        Texture.TextureFilter.Nearest,
    )?.also { hallMenuTextures[path] = it }

    fun dispose() {
        if (disposed) return
        disposed = true
        portraitTextures.dispose()
        backgroundTextures.dispose()
        unitTextures.dispose()
        hallMenuTextures.dispose()
        cachedOverlayPixel?.dispose()
        cachedChoicePanelTexture?.dispose()
        cachedChoiceRowTexture?.dispose()
        cachedDialoguePanelTexture?.dispose()
        cachedStreetSpeechBubbleTexture?.dispose()
        cachedInfoPanelPatch?.texture?.dispose()
        cachedTitleFont?.dispose()
        cachedSectionFont?.dispose()
        cachedBodyFont?.dispose()
        cachedSmallUiFont?.dispose()
        cachedStreetDialogueFont?.dispose()
        cachedStreetSpeakerFont?.dispose()
    }

    private fun loadTexture(path: String, filter: Texture.TextureFilter): Texture? = Gdx.files.internal(path)
        .takeIf { it.exists() }
        ?.let(::Texture)
        ?.also { it.setFilter(filter, filter) }

    private fun createInfoPanelPatch(): NinePatch? = Gdx.files
        .internal("reference/source-hall-infolayer-bg-frame.rgba")
        .takeIf { it.exists() }
        ?.let { raw ->
            val bytes = raw.readBytes()
            check(bytes.size == 19 * 17 * 4) { "Invalid InfoLayer bg SpriteFrame" }
            val pixmap = Pixmap(19, 17, Pixmap.Format.RGBA8888)
            pixmap.pixels.put(bytes).rewind()
            val texture = Texture(pixmap).also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            }
            pixmap.dispose()
            NinePatch(texture, 8, 8, 7, 7)
        }
}

/** A non-collection facade prevents callers from owning a scene texture cache. */
internal class ScenarioSceneTextureCache {
    private val cache = ScenarioSceneAssetCache<String, Texture>(Texture::dispose)

    operator fun get(path: String): Texture? = cache[path]

    operator fun set(path: String, texture: Texture) {
        cache[path] = texture
    }

    fun dispose() = cache.dispose()
}

internal class ScenarioSceneAssetCache<K, V>(
    private val release: (V) -> Unit,
) {
    private val values = mutableMapOf<K, V>()

    operator fun get(key: K): V? = values[key]

    operator fun set(key: K, value: V) {
        values.put(key, value)?.takeIf { it !== value }?.let(release)
    }

    fun dispose() {
        values.values.forEach(release)
        values.clear()
    }
}

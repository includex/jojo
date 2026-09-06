// Scenario
package com.jojo.game.presentation.scenario.assets

import com.jojo.game.presentation.shared.KoreanFont

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch

/** ScenarioSceneAssets: 시나리오·거점 화면이 공유하는 텍스처와 글꼴을 지연 생성하고 수명 종료 때 해제한다. */
internal class ScenarioSceneAssets(
    /** `requiredGlyphsProvider` (() -> String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val requiredGlyphsProvider: () -> String,
) {
    /** 장면별로 필요한 문자만 포함해 글꼴 생성 비용을 줄이는 글리프 집합이다. */
    private val requiredGlyphs by lazy(requiredGlyphsProvider)
    /**
     * `portraitTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val portraitTextures = ScenarioSceneAssetCache<Int, Texture>(Texture::dispose)
    /**
     * `backgroundTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val backgroundTextures = ScenarioSceneAssetCache<Int, Texture>(Texture::dispose)
    /**
     * `unitTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val unitTextures = ScenarioSceneAssetCache<Int, Texture>(Texture::dispose)
    /** 거점 UI 경로별 텍스처를 경로 키로 재사용하는 캐시다. */
    val hallMenuTextures = ScenarioSceneTextureCache()

    /**
     * `cachedTitleFont` (BitmapFont?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedTitleFont: BitmapFont? = null
    /**
     * `cachedSectionFont` (BitmapFont?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedSectionFont: BitmapFont? = null
    /**
     * `cachedBodyFont` (BitmapFont?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedBodyFont: BitmapFont? = null
    /**
     * `cachedSmallUiFont` (BitmapFont?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedSmallUiFont: BitmapFont? = null
    /**
     * `cachedStreetDialogueFont` (BitmapFont?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedStreetDialogueFont: BitmapFont? = null
    /**
     * `cachedStreetSpeakerFont` (BitmapFont?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedStreetSpeakerFont: BitmapFont? = null
    /**
     * `cachedOverlayPixel` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedOverlayPixel: Texture? = null
    /**
     * `cachedChoicePanelTexture` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedChoicePanelTexture: Texture? = null
    /**
     * `choicePanelLoaded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var choicePanelLoaded = false
    /**
     * `cachedChoiceRowTexture` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedChoiceRowTexture: Texture? = null
    /**
     * `choiceRowLoaded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var choiceRowLoaded = false
    /**
     * `cachedDialoguePanelTexture` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedDialoguePanelTexture: Texture? = null
    /**
     * `dialoguePanelLoaded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var dialoguePanelLoaded = false
    /**
     * `cachedStreetSpeechBubbleTexture` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedStreetSpeechBubbleTexture: Texture? = null
    /**
     * `streetSpeechBubbleLoaded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var streetSpeechBubbleLoaded = false
    /**
     * `cachedInfoPanelPatch` (NinePatch?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cachedInfoPanelPatch: NinePatch? = null
    /**
     * `infoPanelPatchLoaded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var infoPanelPatchLoaded = false
    /**
     * `disposed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var disposed = false

    /**
     * `titleFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val titleFont: BitmapFont
        get() = cachedTitleFont ?: KoreanFont.create(34, requiredGlyphs).also { cachedTitleFont = it }
    /**
     * `sectionFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val sectionFont: BitmapFont
        get() = cachedSectionFont ?: KoreanFont.create(86, requiredGlyphs).also { cachedSectionFont = it }
    /**
     * `bodyFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val bodyFont: BitmapFont
        get() = cachedBodyFont ?: KoreanFont.create(34, requiredGlyphs).also { cachedBodyFont = it }
    /**
     * `smallUiFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val smallUiFont: BitmapFont
        get() = cachedSmallUiFont ?: KoreanFont.create(19, requiredGlyphs).also { cachedSmallUiFont = it }
    /**
     * `streetDialogueFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val streetDialogueFont: BitmapFont
        get() = cachedStreetDialogueFont ?: KoreanFont.create(31, requiredGlyphs).also {
            it.data.setScale(544f / 540f, 60f / 56f)
            cachedStreetDialogueFont = it
        }
    /**
     * `streetSpeakerFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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
    /**
     * `overlayPixel` (Texture): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val overlayPixel: Texture
        get() = cachedOverlayPixel ?: Pixmap(1, 1, Pixmap.Format.RGBA8888).let { pixmap ->
            pixmap.setColor(Color.WHITE)
            pixmap.fill()
            Texture(pixmap).also {
                pixmap.dispose()
                cachedOverlayPixel = it
            }
        }
    /**
     * `choicePanelTexture` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val choicePanelTexture: Texture?
        get() {
            if (!choicePanelLoaded) {
                cachedChoicePanelTexture = loadTexture("maps/ui/choice-panel.png", Texture.TextureFilter.Nearest)
                choicePanelLoaded = true
            }
            return cachedChoicePanelTexture
        }
    /**
     * `choiceRowTexture` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val choiceRowTexture: Texture?
        get() {
            if (!choiceRowLoaded) {
                cachedChoiceRowTexture = loadTexture("maps/ui/choice-row.png", Texture.TextureFilter.Nearest)
                choiceRowLoaded = true
            }
            return cachedChoiceRowTexture
        }
    /**
     * `dialoguePanelTexture` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val dialoguePanelTexture: Texture?
        get() {
            if (!dialoguePanelLoaded) {
                cachedDialoguePanelTexture = loadTexture("maps/ui/dialogue-panel.png", Texture.TextureFilter.Linear)
                dialoguePanelLoaded = true
            }
            return cachedDialoguePanelTexture
        }
    /**
     * `streetSpeechBubbleTexture` (Texture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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
    /**
     * `infoPanelPatch` (NinePatch?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val infoPanelPatch: NinePatch?
        get() {
            if (!infoPanelPatchLoaded) {
                cachedInfoPanelPatch = createInfoPanelPatch()
                infoPanelPatchLoaded = true
            }
            return cachedInfoPanelPatch
        }

    /** portraitTexture: 인물 초상화를 처음 요청할 때만 로드해 캐시에 보관한다. */
    fun portraitTexture(characterId: Int): Texture? = portraitTextures[characterId] ?: loadTexture(
        "maps/heads/$characterId.png",
        Texture.TextureFilter.Linear,
    )?.also { portraitTextures[characterId] = it }

    /** backgroundTexture: 배경 식별자에 맞는 장면 이미지를 지연 로드한다. */
    fun backgroundTexture(backgroundId: Int): Texture? = backgroundTextures[backgroundId] ?: loadTexture(
        "maps/$backgroundId.jpg",
        Texture.TextureFilter.Linear,
    )?.also { backgroundTextures[backgroundId] = it }

    /**
     * `unitTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun unitTexture(assetId: Int): Texture? = unitTextures[assetId] ?: loadTexture(
        "maps/hall-units/$assetId.png",
        Texture.TextureFilter.Linear,
    )?.also { unitTextures[assetId] = it }

    /**
     * `hallTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hallTexture(path: String): Texture? = hallMenuTextures[path] ?: loadTexture(
        path,
        Texture.TextureFilter.Nearest,
    )?.also { hallMenuTextures[path] = it }

    /** dispose: 이 화면 수명주기에서 확보한 캐시·글꼴·텍스처를 한 번만 해제한다. */
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

    /**
     * `loadTexture`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun loadTexture(path: String, filter: Texture.TextureFilter): Texture? = Gdx.files.internal(path)
        .takeIf { it.exists() }
        ?.let(::Texture)
        ?.also { it.setFilter(filter, filter) }

    /**
     * `createInfoPanelPatch`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun createInfoPanelPatch(): NinePatch? = Gdx.files
        .internal("reference/source-hall-infolayer-bg-frame.rgba")
        .takeIf { it.exists() }
        ?.let { raw ->
            /**
             * `bytes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val bytes = raw.readBytes()
            check(bytes.size == 19 * 17 * 4) { "Invalid InfoLayer bg SpriteFrame" }
            /**
             * `pixmap` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val pixmap = Pixmap(19, 17, Pixmap.Format.RGBA8888)
            pixmap.pixels.put(bytes).rewind()
            /**
             * `texture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val texture = Texture(pixmap).also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            }
            pixmap.dispose()
            NinePatch(texture, 8, 8, 7, 7)
        }
}

/** ScenarioSceneTextureCache: 시나리오 장면 텍스처 Cache이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */
internal class ScenarioSceneTextureCache {
    /**
     * `cache` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val cache = ScenarioSceneAssetCache<String, Texture>(Texture::dispose)

    /**
     * `get`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    operator fun get(path: String): Texture? = cache[path]

    /**
     * `set`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    operator fun set(path: String, texture: Texture) {
        cache[path] = texture
    }

    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispose() = cache.dispose()
}

/**
 * `ScenarioSceneAssetCache`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class ScenarioSceneAssetCache<K, V>(
    /** `release` ((V) -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val release: (V) -> Unit,
) {
    /**
     * `values` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val values = mutableMapOf<K, V>()

    /**
     * `get`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    operator fun get(key: K): V? = values[key]

    /**
     * `set`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    operator fun set(key: K, value: V) {
        values.put(key, value)?.takeIf { it !== value }?.let(release)
    }

    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispose() {
        values.values.forEach(release)
        values.clear()
    }
}

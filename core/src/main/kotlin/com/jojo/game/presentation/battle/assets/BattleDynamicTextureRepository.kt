package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.JsonReader
import java.security.MessageDigest

internal data class CocosRichTextTexture(
    val texture: Texture,
    val worldX: Float,
    val worldY: Float,
    val drawWidth: Float,
    val drawHeight: Float,
)

/** Owns battle textures whose resource key is selected from live gameplay state. */
internal class BattleDynamicTextureRepository : Disposable {
    private val unitTextures = mutableMapOf<Int, Texture>()
    private val attackTextures = mutableMapOf<Int, Texture>()
    private val specialTextures = mutableMapOf<Int, Texture>()
    private val effectTextures = mutableMapOf<Int, Texture>()
    private val headTextures = mutableMapOf<Int, Texture>()
    private val battleDialogTextures = mutableMapOf<String, Texture?>()
    private val richTextTextures = mutableMapOf<String, CocosRichTextTexture>()
    private val gateTextures = mutableMapOf<Int, Texture>()
    private val terrainIconTextures = mutableMapOf<Int, Texture>()
    private val itemIconTextures = mutableMapOf<Int, Texture>()

    /** Gameplay movement uses mov2 → mov → flat fallback. */
    fun unitMovement(avatarId: Int): Texture? {
        unitTextures[avatarId]?.let { return it }
        val handle = firstExisting(BattleDynamicTexturePaths.movement(avatarId))
        return handle?.let(::linearTexture)
            ?.also { unitTextures[avatarId] = it }
    }

    /**
     * 공개 메서드 `attack`
     *
     * ### 파라미터
    - `avatarId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun attack(avatarId: Int): Texture? = action("atk", avatarId)

    /**
     * 공개 메서드 `special`
     *
     * ### 파라미터
    - `avatarId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun special(avatarId: Int): Texture? = action("spc", avatarId)

    /** Fight/action atlases use only the authored `{kind}2 → {kind}` fallback. */
    fun action(kind: String, avatarId: Int): Texture? = actionTexture(
        kind,
        avatarId,
        when (kind) {
            "mov" -> unitTextures
            "atk" -> attackTextures
            "spc" -> specialTextures
            else -> error("Unsupported battle action texture kind: $kind")
        },
    )

    /**
     * 공개 메서드 `effect`
     *
     * ### 파라미터
    - `effectId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun effect(effectId: Int): Texture? {
        effectTextures[effectId]?.let { return it }
        val handle = Gdx.files.internal("maps/effects/${effectId + 1}.png")
        return handle.takeIf { it.exists() }?.let(::linearTexture)
            ?.also { effectTextures[effectId] = it }
    }

    /**
     * 공개 메서드 `head`
     *
     * ### 파라미터
    - `faceId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun head(faceId: Int): Texture? {
        headTextures[faceId]?.let { return it }
        val handle = Gdx.files.internal("maps/heads/$faceId.png")
        return handle.takeIf { it.exists() }?.let(::linearTexture)
            ?.also { headTextures[faceId] = it }
    }

    /** Nullable getOrPut deliberately re-probes missing dialog paths on later requests. */
    fun battleDialog(path: String): Texture? = battleDialogTextures.getOrPut(path) {
        Gdx.files.internal(path).takeIf { it.exists() }?.let(::linearTexture)
    }

    /**
     * 공개 메서드 `richText`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `CocosRichTextTexture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun richText(text: String): CocosRichTextTexture? {
        if (text.isEmpty()) return null
        val key = MessageDigest.getInstance("SHA-256")
            .digest(text.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
        richTextTextures[key]?.let { return it }
        val png = Gdx.files.internal("maps/dialogue-text/$key.png")
        val metadata = Gdx.files.internal("maps/dialogue-text/$key.json")
        if (!png.exists() || !metadata.exists()) return null
        val source = JsonReader().parse(metadata)
        val origin = source.get("worldOrigin") ?: return null
        val drawSize = source.get("drawSize") ?: return null
        return CocosRichTextTexture(
            texture = linearTexture(png),
            worldX = origin.getFloat(0),
            worldY = origin.getFloat(1),
            drawWidth = drawSize.getFloat(0),
            drawHeight = drawSize.getFloat(1),
        ).also { richTextTextures[key] = it }
    }

    /**
     * 공개 메서드 `gate`
     *
     * ### 파라미터
    - `objectId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun gate(objectId: Int): Texture? {
        val gateId = (objectId - 4) * 2 + 1
        gateTextures[gateId]?.let { return it }
        val handle = Gdx.files.internal("maps/gates/$gateId.png")
        return handle.takeIf { it.exists() }?.let(::Texture)?.also { gateTextures[gateId] = it }
    }

    /**
     * 공개 메서드 `terrainIcon`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun terrainIcon(index: Int): Texture? = indexedTexture(
        index, terrainIconTextures, "maps/terrain-icons/$index.png",
    )

    /**
     * 공개 메서드 `itemIcon`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun itemIcon(index: Int): Texture? = indexedTexture(
        index, itemIconTextures, "maps/item-icons/$index.png",
    )

    /**
     * 공개 메서드 `movementAtlasUuid`
     *
     * ### 파라미터
    - `avatarId` (`Int?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun movementAtlasUuid(avatarId: Int?): String? = when (avatarId) {
        11 -> "19ac1287-4d09-45f4-bf9a-f5eb8b21795c"
        20 -> "3f8fbf89-4dd0-4d0b-88e0-9c7927fe5693"
        74 -> "ca6577ee-3ca1-4280-9d60-117070dd2d0b"
        93 -> "9eebca65-e81b-4ba4-ad61-7ac20d03661c"
        186 -> "31cc3c95-4d6e-4c10-848f-ef1ca165e78f"
        else -> null
    }

    private fun actionTexture(kind: String, avatarId: Int, cache: MutableMap<Int, Texture>): Texture? {
        cache[avatarId]?.let { return it }
        val handle = firstExisting(BattleDynamicTexturePaths.action(kind, avatarId))
        return handle?.let(::linearTexture)?.also { cache[avatarId] = it }
    }

    private fun firstExisting(paths: List<String>) = paths.asSequence()
        .map(Gdx.files::internal)
        .firstOrNull { it.exists() }

    private fun indexedTexture(index: Int, cache: MutableMap<Int, Texture>, path: String): Texture? {
        cache[index]?.let { return it }
        val handle = Gdx.files.internal(path)
        return handle.takeIf { it.exists() }?.let(::Texture)?.also { cache[index] = it }
    }

    private fun linearTexture(handle: com.badlogic.gdx.files.FileHandle): Texture = Texture(handle).also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    override fun dispose() {
        sequenceOf(unitTextures, attackTextures, specialTextures, effectTextures, headTextures)
            .flatMap { it.values.asSequence() }
            .forEach(Texture::dispose)
        battleDialogTextures.values.filterNotNull().forEach(Texture::dispose)
        richTextTextures.values.forEach { it.texture.dispose() }
        sequenceOf(gateTextures, terrainIconTextures, itemIconTextures)
            .flatMap { it.values.asSequence() }
            .forEach(Texture::dispose)
    }
}

/** Resource candidates are ordered exactly as the authored atlas fallback chain. */
internal object BattleDynamicTexturePaths {
    /**
     * 공개 메서드 `movement`
     *
     * ### 파라미터
    - `avatarId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<String>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun movement(avatarId: Int): List<String> = listOf(
        "maps/units/mov2/$avatarId.png",
        "maps/units/mov/$avatarId.png",
        "maps/units/$avatarId.png",
    )

    /**
     * 공개 메서드 `action`
     *
     * ### 파라미터
    - `kind` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `avatarId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<String>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun action(kind: String, avatarId: Int): List<String> = listOf(
        "maps/units/${kind}2/$avatarId.png",
        "maps/units/$kind/$avatarId.png",
    )
}

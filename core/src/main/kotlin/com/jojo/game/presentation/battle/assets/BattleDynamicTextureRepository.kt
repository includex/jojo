// Battle
package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.JsonReader
import java.security.MessageDigest

/** 리치 텍스트를 이미지로 렌더링하기 위한 텍스처와 배치 정보입니다. */
internal data class CocosRichTextTexture(
    val texture: Texture,
    val worldX: Float,
    val worldY: Float,
    val drawWidth: Float,
    val drawHeight: Float,
)

/** 전투 상태에 따라 선택되는 동적 텍스처를 캐시하고 해제합니다. */
internal class BattleDynamicTextureRepository : Disposable {
    /**
     * `unitTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val unitTextures = mutableMapOf<Int, Texture>()
    /**
     * `attackTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val attackTextures = mutableMapOf<Int, Texture>()
    /**
     * `specialTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val specialTextures = mutableMapOf<Int, Texture>()
    /**
     * `effectTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val effectTextures = mutableMapOf<Int, Texture>()
    /**
     * `headTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val headTextures = mutableMapOf<Int, Texture>()
    /**
     * `battleDialogTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val battleDialogTextures = mutableMapOf<String, Texture?>()
    /**
     * `richTextTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val richTextTextures = mutableMapOf<String, CocosRichTextTexture>()
    /**
     * `gateTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val gateTextures = mutableMapOf<Int, Texture>()
    /**
     * `terrainIconTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val terrainIconTextures = mutableMapOf<Int, Texture>()
    /**
     * `itemIconTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val itemIconTextures = mutableMapOf<Int, Texture>()

    /** 이동 스프라이트를 우선순위 경로에서 찾아 반환합니다. */
    fun unitMovement(avatarId: Int): Texture? {
        unitTextures[avatarId]?.let { return it }
        val handle = firstExisting(BattleDynamicTexturePaths.movement(avatarId))
        return handle?.let(::linearTexture)
            ?.also { unitTextures[avatarId] = it }
    }

    /** 공격 스프라이트를 반환합니다. */
    fun attack(avatarId: Int): Texture? = action("atk", avatarId)

    /** 특수 행동 스프라이트를 반환합니다. */
    fun special(avatarId: Int): Texture? = action("spc", avatarId)

    /** 행동 종류에 맞는 스프라이트를 조회합니다. */
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

    /** 마법 효과 텍스처를 반환합니다. */
    fun effect(effectId: Int): Texture? {
        effectTextures[effectId]?.let { return it }
        val handle = Gdx.files.internal("maps/effects/${effectId + 1}.png")
        return handle.takeIf { it.exists() }?.let(::linearTexture)
            ?.also { effectTextures[effectId] = it }
    }

    /** 인물 얼굴 텍스처를 반환합니다. */
    fun head(faceId: Int): Texture? {
        headTextures[faceId]?.let { return it }
        val handle = Gdx.files.internal("maps/heads/$faceId.png")
        return handle.takeIf { it.exists() }?.let(::linearTexture)
            ?.also { headTextures[faceId] = it }
    }

    /** 대화 텍스처를 조회하며 없는 경로는 다음 요청에서 다시 확인합니다. */
    fun battleDialog(path: String): Texture? = battleDialogTextures.getOrPut(path) {
        Gdx.files.internal(path).takeIf { it.exists() }?.let(::linearTexture)
    }

    /** 대화 문장의 사전 렌더링 텍스처를 조회합니다. */
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

    /** 성문 오브젝트에 대응하는 텍스처를 반환합니다. */
    fun gate(objectId: Int): Texture? {
        val gateId = (objectId - 4) * 2 + 1
        gateTextures[gateId]?.let { return it }
        val handle = Gdx.files.internal("maps/gates/$gateId.png")
        return handle.takeIf { it.exists() }?.let(::Texture)?.also { gateTextures[gateId] = it }
    }

    /** 지형 아이콘 텍스처를 반환합니다. */
    fun terrainIcon(index: Int): Texture? = indexedTexture(
        index, terrainIconTextures, "maps/terrain-icons/$index.png",
    )

    /** 아이템 아이콘 텍스처를 반환합니다. */
    fun itemIcon(index: Int): Texture? = indexedTexture(
        index, itemIconTextures, "maps/item-icons/$index.png",
    )

    /** 특수 이동 아틀라스의 식별자를 반환합니다. */
    fun movementAtlasUuid(avatarId: Int?): String? = when (avatarId) {
        11 -> "19ac1287-4d09-45f4-bf9a-f5eb8b21795c"
        20 -> "3f8fbf89-4dd0-4d0b-88e0-9c7927fe5693"
        74 -> "ca6577ee-3ca1-4280-9d60-117070dd2d0b"
        93 -> "9eebca65-e81b-4ba4-ad61-7ac20d03661c"
        186 -> "31cc3c95-4d6e-4c10-848f-ef1ca165e78f"
        else -> null
    }

    /**
     * `actionTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun actionTexture(kind: String, avatarId: Int, cache: MutableMap<Int, Texture>): Texture? {
        cache[avatarId]?.let { return it }
        val handle = firstExisting(BattleDynamicTexturePaths.action(kind, avatarId))
        return handle?.let(::linearTexture)?.also { cache[avatarId] = it }
    }

    /**
     * `firstExisting`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun firstExisting(paths: List<String>) = paths.asSequence()
        .map(Gdx.files::internal)
        .firstOrNull { it.exists() }

    /**
     * `indexedTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun indexedTexture(index: Int, cache: MutableMap<Int, Texture>, path: String): Texture? {
        cache[index]?.let { return it }
        val handle = Gdx.files.internal(path)
        return handle.takeIf { it.exists() }?.let(::Texture)?.also { cache[index] = it }
    }

    /**
     * `linearTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun linearTexture(handle: com.badlogic.gdx.files.FileHandle): Texture = Texture(handle).also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

/** 원본 아틀라스의 대체 조회 순서를 제공합니다. */
internal object BattleDynamicTexturePaths {
    /** 이동 스프라이트 후보 경로를 반환합니다. */
    fun movement(avatarId: Int): List<String> = listOf(
        "maps/units/mov2/$avatarId.png",
        "maps/units/mov/$avatarId.png",
        "maps/units/$avatarId.png",
    )

    /** 행동 스프라이트 후보 경로를 반환합니다. */
    fun action(kind: String, avatarId: Int): List<String> = listOf(
        "maps/units/${kind}2/$avatarId.png",
        "maps/units/$kind/$avatarId.png",
    )
}

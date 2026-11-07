package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.JsonValue
import java.util.WeakHashMap

/** UV-only conversion. Animation coordinates and the actor's world-space size stay native. */
internal data class BattleSpriteResolution(
    val sourceFrameSize: Int,
    val sourcePitch: Int,
    val sourceInsetY: Int,
    val frameSize: Int,
    val pitch: Int,
    val insetY: Int,
    val frameCount: Int,
) {
    init {
        require(sourceFrameSize > 0 && frameSize > 0 && frameCount > 0)
        require(sourceInsetY >= 0 && sourcePitch >= sourceFrameSize + sourceInsetY)
        require(insetY >= 0 && pitch >= frameSize + insetY)
    }

    fun sourceRect(sourceY: Int): BattleSpriteSourceRect {
        val index = ((sourceY - sourceInsetY).coerceAtLeast(0) / sourcePitch)
            .takeIf { it < frameCount } ?: 0
        return BattleSpriteSourceRect(0, index * pitch + insetY, frameSize, frameSize)
    }

    companion object {
        fun parse(json: JsonValue): BattleSpriteResolution {
            require(json.getString("format") == "battle-sprite-resolution/v1")
            return BattleSpriteResolution(json.getInt("sourceFrameSize"), json.getInt("sourcePitch"),
                json.getInt("sourceInsetY"), json.getInt("frameSize"), json.getInt("pitch"),
                json.getInt("insetY"), json.getInt("frameCount"))
        }
        fun variantPath(nativePath: String): String? = nativePath.takeIf {
            it.startsWith("maps/units/") && !it.contains("runtime-1_3/")
        }?.replaceFirst("maps/units/", "maps/units/runtime-1_3/")
    }
}

internal data class BattleSpriteSourceRect(val x: Int, val y: Int, val width: Int, val height: Int)

/** Scene-owned textures are registered by the loader and forgotten before disposal. */
internal object BattleSpriteResolutionSources {
    private val variants = WeakHashMap<Texture, BattleSpriteResolution>()
    fun register(texture: Texture, mapping: BattleSpriteResolution) {
        require(texture.width == mapping.frameSize && texture.height == mapping.pitch * mapping.frameCount)
        variants[texture] = mapping
    }
    fun forget(texture: Texture) { variants.remove(texture) }
    fun rect(texture: Texture, sourceY: Int, width: Int, height: Int): BattleSpriteSourceRect =
        variants[texture]?.sourceRect(sourceY) ?: BattleSpriteSourceRect(0,
            if (sourceY + height > texture.height) 0 else sourceY,
            minOf(width, texture.width), minOf(height, texture.height))
}

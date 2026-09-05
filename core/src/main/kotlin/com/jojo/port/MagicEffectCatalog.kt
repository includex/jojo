package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

/**
 * Exact frame metadata consumed by the original BattleLayer.meff().
 * The image is Meff_(effectId + 1)-1 and every row in this table describes
 * its source strip row, alpha, placement and hit frame.
 */
data class OriginalMagicEffect(
    val showFrames: Int,
    val frameCount: Int,
    val uses24Fps: Boolean,
    val frameWidth: Int,
    val frameHeight: Int,
    val soundId: Int,
    val frames: List<Frame>,
) {
    data class Frame(
        val sourceIndex: Int,
        val alpha: Int,
        val offsetX: Int,
        val offsetY: Int,
        val hit: Boolean,
    )

    /** Cocos creates the clip at 12/24fps then plays it at speed 1.5. */
    val duration: Float get() = showFrames / (if (uses24Fps) 36f else 18f)

    /** StageLayer.meff's first KEY becomes the sole `__hitFrame` event. */
    val hitTime: Float get() {
        val index = frames.indexOfFirst(Frame::hit)
        return if (index < 0) duration else index / (if (uses24Fps) 36f else 18f)
    }

    fun frameAt(elapsed: Float): Frame? {
        if (frames.isEmpty()) return null
        val frame = (elapsed * if (uses24Fps) 36f else 18f).toInt()
        return frames[frame.coerceIn(0, frames.lastIndex)]
    }
}

class MagicEffectCatalog private constructor(private val effects: List<OriginalMagicEffect>) {
    fun effect(id: Int): OriginalMagicEffect? = effects.getOrNull(id)

    companion object {
        fun load(): MagicEffectCatalog {
            // Rendering uses Gdx's asset resolver, while the required
            // exhaustive conformance suite runs without a LibGDX runtime.
            // Both read the same exported binary; do not make tests depend
            // on a display just to decode original data.
            val raw = MagicEffectCatalog::class.java.classLoader
                .getResourceAsStream("maps/data/Meff.bin")
                ?.use { it.readBytes() }
                ?: Gdx.files.internal("maps/data/Meff.bin").readBytes()
            val decoded = requireNotNull(OriginalDataTableCodec.decode(raw)) { "원본 Meff 테이블 검증 실패" }
            val root = JsonReader().parse(decoded)
            val effects = generateSequence(root.child) { it.next }.map { value ->
                val frames = generateSequence(value.get(6)?.child) { it.next }.map { frame ->
                    val values = generateSequence(frame.child) { it.next }.map { it.asInt() }.toList()
                    OriginalMagicEffect.Frame(
                        sourceIndex = values.getOrElse(0) { -1 },
                        alpha = values.getOrElse(2) { 8 },
                        offsetX = values.getOrElse(3) { 0 },
                        offsetY = values.getOrElse(4) { 0 },
                        hit = values.getOrElse(5) { 0 } != 0,
                    )
                }.toList()
                OriginalMagicEffect(
                    showFrames = value.getInt(0),
                    frameCount = value.getInt(1),
                    uses24Fps = value.getInt(2) == 1,
                    frameWidth = value.getInt(3),
                    frameHeight = value.getInt(4),
                    soundId = value.getInt(5),
                    frames = frames,
                )
            }.toList()
            return MagicEffectCatalog(effects)
        }
    }
}

package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

class MagicEffectCatalog private constructor(private val effects: List<MagicEffectDefinition>) {
    fun effect(id: Int): MagicEffectDefinition? = effects.getOrNull(id)

    companion object {
        fun load(): MagicEffectCatalog {
            // Rendering uses Gdx's asset resolver, while the required
            // exhaustive conformance suite runs without a LibGDX runtime.
            // Both read the same exported binary; do not make tests depend
            // on a display just to decode game data.
            val raw = MagicEffectCatalog::class.java.classLoader
                .getResourceAsStream("maps/data/Meff.bin")
                ?.use { it.readBytes() }
                ?: Gdx.files.internal("maps/data/Meff.bin").readBytes()
            val decoded = requireNotNull(EncryptedGameDataCodec.decode(raw)) { "Meff 테이블 검증 실패" }
            val root = JsonReader().parse(decoded)
            val effects = generateSequence(root.child) { it.next }.map { value ->
                val frames = generateSequence(value.get(6)?.child) { it.next }.map { frame ->
                    val values = generateSequence(frame.child) { it.next }.map { it.asInt() }.toList()
                    MagicEffectDefinition.Frame(
                        sourceIndex = values.getOrElse(0) { -1 },
                        alpha = values.getOrElse(2) { 8 },
                        offsetX = values.getOrElse(3) { 0 },
                        offsetY = values.getOrElse(4) { 0 },
                        hit = values.getOrElse(5) { 0 } != 0,
                    )
                }.toList()
                MagicEffectDefinition(
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

package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.EncryptedGameDataCodec

/** 마법 효과 정의 목록을 보관하고 리소스에서 불러옵니다. */
class MagicEffectCatalog private constructor(private val effects: List<MagicEffectDefinition>) {
    /** 효과 식별자에 해당하는 정의를 반환합니다. */
    fun effect(id: Int): MagicEffectDefinition? = effects.getOrNull(id)

    companion object {
        /** 내장 마법 효과 바이너리를 읽어 카탈로그를 생성합니다. */
        fun load(): MagicEffectCatalog {
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

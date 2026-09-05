package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.EncryptedGameDataCodec

/**
 * class  `MagicEffectCatalog`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class MagicEffectCatalog private constructor(private val effects: List<MagicEffectDefinition>) {
    /**
     * 공개 메서드 `effect`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `MagicEffectDefinition?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun effect(id: Int): MagicEffectDefinition? = effects.getOrNull(id)

    companion object {
        /**
         * 공개 메서드 `load`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `MagicEffectCatalog`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

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

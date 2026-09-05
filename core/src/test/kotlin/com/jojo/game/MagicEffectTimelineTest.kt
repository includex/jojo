package com.jojo.game

import com.jojo.game.presentation.battle.assets.MagicEffectCatalog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import javax.imageio.ImageIO

/**
 * class  `MagicEffectTimelineTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class MagicEffectTimelineTest {
    @Test
    fun `every decoded effect schedules source hit frame or exact finish fallback`() {
        val catalog = MagicEffectCatalog.load()
        // Meff.bin is indexed 0..255 in this title.  Check the entire table
        // rather than a hand-picked spell set.
        (0..255).mapNotNull(catalog::effect).forEach { effect ->
            assertTrue(effect.hitTime >= 0f)
            assertTrue(effect.hitTime <= effect.duration)
            if (effect.frames.any { it.hit }) {
                val first = effect.frames.indexOfFirst { it.hit }
                val rate = if (effect.uses24Fps) 36f else 18f
                assertEquals(first / rate, effect.hitTime)
            } else assertEquals(effect.duration, effect.hitTime)
        }
    }

    @Test
    fun `every magic effect uses contiguous unpadded source rows`() {
        val catalog = MagicEffectCatalog.load()
        var checked = 0
        (0..255).mapNotNull(catalog::effect).forEachIndexed { effectId, effect ->
            assertEquals(effect.showFrames, effect.frames.size, "Meff ${effectId + 1} SHOW_N")
            effect.frames.filter { it.sourceIndex >= 0 }.forEach { frame ->
                assertTrue(frame.sourceIndex < effect.frameCount, "Meff ${effectId + 1} source row ${frame.sourceIndex}")
            }
            val stream = javaClass.classLoader.getResourceAsStream("maps/effects/${effectId + 1}.png")
                ?: return@forEachIndexed
            val image = stream.use(ImageIO::read)
            assertTrue(effect.frameWidth <= image.width, "Meff ${effectId + 1} frame width")
            effect.frames.filter { it.sourceIndex >= 0 }.forEach { frame ->
                // StageLayer.meff uses b = IDX * FRAME_H with no gutter.
                assertTrue((frame.sourceIndex + 1) * effect.frameHeight <= image.height,
                    "Meff ${effectId + 1} row ${frame.sourceIndex} exceeds ${image.height}px")
            }
            checked++
        }
        assertEquals(60, checked, "all shipped Meff strips")
    }
}

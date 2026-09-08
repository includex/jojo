// Infrastructure Test
package com.jojo.game.infrastructure.audio

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 원본 `UIFrame`의 사운드 경로 규칙과 내보낸 자산이 맞는지 검증한다. */
class GameAudioPathsTest {
    @Test
    fun `background and effect paths follow the source UIFrame rules`() {
        assertEquals("audio/02-AudioTrack 02.mp3", GameAudioPaths.background(0))
        assertEquals("audio/10-AudioTrack 10.mp3", GameAudioPaths.background(8))
        assertEquals("audio/Se00.mp3", GameAudioPaths.effect(0))
        assertEquals("audio/Se07.mp3", GameAudioPaths.effect(7))
        assertEquals("audio/Se_m_03.mp3", GameAudioPaths.effect(103))
        assertEquals("audio/Se_e_12.mp3", GameAudioPaths.effect(212))
        assertEquals("audio/ui-click.mp3", GameAudioPaths.ui(UiSound.CLICK))
        assertEquals("audio/ui-cancel.mp3", GameAudioPaths.ui(UiSound.CANCEL))
    }

    @Test
    fun `every exported clip is reachable through those rules`() {
        // 내보낸 파일이 규칙 밖 이름을 쓰면 그 소리는 영영 재생되지 않는다.
        val directory = File("build/generated/audio-assets")
        val clips = directory.listFiles { file -> file.extension == "mp3" }?.map { it.name }.orEmpty()
        assertTrue(clips.isNotEmpty(), "no exported audio clips in ${directory.absolutePath}")
        val reachable = buildSet {
            for (id in 0..99) add(File(GameAudioPaths.effect(id)).name)
            for (id in 100..199) add(File(GameAudioPaths.effect(id)).name)
            for (id in 200..299) add(File(GameAudioPaths.effect(id)).name)
            for (id in -2..99) add(File(GameAudioPaths.background(id)).name)
            UiSound.entries.forEach { add(File(GameAudioPaths.ui(it)).name) }
        }
        assertEquals(emptyList(), clips.filterNot { it in reachable }.sorted())
    }
}

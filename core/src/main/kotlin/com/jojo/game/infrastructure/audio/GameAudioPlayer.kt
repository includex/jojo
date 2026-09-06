// Infrastructure
package com.jojo.game.infrastructure.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.jojo.game.application.scenario.ScenarioStage

/** GameAudioPlayer: 추출한 MP3 자산으로 원본 게임의 사운드 식별자를 재생한다. */
class GameAudioPlayer {
    /** 검증되지 않은 오디오 디코더 오류가 렌더링을 중단하지 않도록 하는 재생 활성화 여부다. */
    private val enabled = System.getProperty("jojo.audio", "false").toBoolean()
    /** 현재 재생 중인 배경음 식별자다. */
    private var playingBackgroundId: Int? = null
    /** 현재 배경음 재생기다. */
    private var background: Music? = null
    /** 효과음 식별자별로 재사용하는 사운드 자원이다. */
    private val effects = mutableMapOf<Int, Sound>()

    /** 나중의 중지 요청을 처리하기 위해 효과음 재생 핸들을 보관한다. */
    private val activeEffects = mutableMapOf<Int, Long>()

    /** 시나리오 단계의 배경음과 대기 중인 효과음을 재생 상태에 반영한다. */
    fun sync(stage: ScenarioStage) {
        if (!enabled) return
        if (playingBackgroundId != stage.backgroundSound) playBackground(stage.backgroundSound)
        stage.consumeSoundEffects().forEach { playEffect(it.soundId, it.mode) }
    }

    /** 전투 화면의 공격·피해·효과 애니메이션에 연결된 효과음을 재생한다. */
    fun playBattleEffect(soundId: Int) {
        if (enabled) playEffect(soundId, 1)
    }

    /**
     * `playBackground`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun playBackground(soundId: Int) {
        playingBackgroundId = soundId
        background?.dispose()
        background = null
        if (soundId < 0) return
        val track = soundId + 2
        val path = "audio/${track.toString().padStart(2, '0')}-AudioTrack ${track.toString().padStart(2, '0')}.mp3"
        val file = Gdx.files.internal(path)
        if (!file.exists()) {
            Gdx.app.log("JojoGame", "Missing game background track: $path")
            return
        }
        background = Gdx.audio.newMusic(file).also { it.isLooping = true; it.play() }
    }

    /**
     * `playEffect`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun playEffect(soundId: Int, mode: Int) {
        if (soundId < 0) return
        if (mode == 0) {
            activeEffects.remove(soundId)?.let { instance -> effects[soundId]?.stop(instance) }
            return
        }
        val sound = effects[soundId] ?: run {
            val file = Gdx.files.internal(effectPath(soundId))
            if (!file.exists()) {
                Gdx.app.log("JojoGame", "Missing game sound effect: ${file.path()}")
                return
            }
            Gdx.audio.newSound(file).also { effects[soundId] = it }
        }
        val instance = if (mode < 0) sound.loop() else sound.play()
        activeEffects[soundId] = instance
    }

    /**
     * `effectPath`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun effectPath(soundId: Int): String = when {
        soundId >= 200 -> "audio/Se_e_${(soundId - 200).toString().padStart(2, '0')}.mp3"
        soundId >= 100 -> "audio/Se_m_${(soundId - 100).toString().padStart(2, '0')}.mp3"
        else -> "audio/Se${soundId.toString().padStart(2, '0')}.mp3"
    }

    /** 재생 중인 오디오 자원을 중지하고 해제한다. */
    fun dispose() {
        background?.dispose()
        effects.values.forEach(Sound::dispose)
        effects.clear()
        activeEffects.clear()
    }
}

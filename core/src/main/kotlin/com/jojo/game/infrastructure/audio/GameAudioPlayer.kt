// Infrastructure
package com.jojo.game.infrastructure.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.jojo.game.domain.scenario.ScenarioSoundEffect

/**
 * GameAudioPlayer: 추출한 MP3 자산으로 원본 게임의 사운드 식별자를 재생한다.
 *
 * 원본 `UIFrame.playBackgroundSound`/`playSoundEffect`는 재생 직전에 사용자 설정을 본다.
 * `musicOn`·`effectOn`은 그 두 설정(`musicon`, `soundon`)에 해당하며, 설정 창에서 값이
 * 바뀌면 바로 반영되도록 그때그때 읽는다.
 */
class GameAudioPlayer(
    /**
     * 재생 활성화 여부다.
     *
     * 자동 실행(검증)에는 소리 장치가 없으므로 꺼 둔다. `jojo.audio` 속성으로 덮어쓸 수 있다.
     */
    enabled: Boolean = true,
    /** 배경음 설정이다. 원본 `musicon`에 해당한다. */
    private val musicOn: () -> Boolean = { true },
    /** 효과음 설정이다. 원본 `soundon`에 해당한다. */
    private val effectOn: () -> Boolean = { true },
) {
    /** 검증되지 않은 오디오 디코더 오류가 렌더링을 중단하지 않도록 하는 재생 활성화 여부다. */
    private val enabled = System.getProperty("jojo.audio")?.toBooleanStrictOrNull() ?: enabled
    /** 현재 재생 중인 배경음 식별자다. */
    private var playingBackgroundId: Int? = null
    /** 현재 배경음 재생기다. */
    private var background: Music? = null
    /** 효과음 식별자별로 재사용하는 사운드 자원이다. */
    private val effects = mutableMapOf<Int, Sound>()

    /** 나중의 중지 요청을 처리하기 위해 효과음 재생 핸들을 보관한다. */
    private val activeEffects = mutableMapOf<Int, Long>()

    /** 효과음별로 마지막에 재생한 시각이다. 원본 `Sound.m_effectFlag`에 해당한다. */
    private val lastEffectPlayedAt = mutableMapOf<Int, Long>()

    /**
     * 시나리오 단계의 배경음과 대기 중인 효과음을 재생 상태에 반영한다.
     *
     * 단계 객체 자체가 아니라 재생에 필요한 값만 받는다. 자원 계층이 상위 계층의
     * 타입을 되짚어 들어가지 않도록 부르는 쪽이 값을 꺼내 넘긴다.
     */
    fun sync(backgroundSound: Int, soundEffects: List<ScenarioSoundEffect>) {
        if (!enabled) return
        if (playingBackgroundId != backgroundSound) playBackground(backgroundSound)
        // 설정을 끄면 원본 `Sound.setMusicON(false)`처럼 재생 중인 배경음도 멈춘다.
        // 식별자는 그대로 두어 다시 켜면 같은 곡이 이어지도록 한다.
        applyMusicSetting()
        soundEffects.forEach { playEffect(it.soundId, it.mode) }
    }

    /**
     * 배경음 설정: 꺼져 있으면 멈추고, 켜져 있는데 멈춰 있으면 다시 재생한다.
     *
     * 원본 `Sound.setMusicON`은 끌 때 `stopMusic`, 켤 때 `playMusic`을 부른다. 이어 듣기가
     * 아니라 곡을 처음부터 다시 트는 것이 원본의 모습이므로 `stop`/`play`를 쓴다.
     */
    private fun applyMusicSetting() {
        val music = background ?: return
        if (musicOn()) {
            if (!music.isPlaying) music.play()
        } else if (music.isPlaying) {
            music.stop()
        }
    }

    /** 전투 화면의 공격·피해·효과 애니메이션에 연결된 효과음을 재생한다. */
    fun playBattleEffect(soundId: Int) {
        if (enabled) playEffect(soundId, 1)
    }

    /** 로드 실패가 화면을 멈추지 않도록 감싼다. 원본에도 자산이 없으면 조용히 넘어간다. */
    private fun <T> loadOrLog(path: String, load: () -> T): T? = runCatching(load).getOrElse { cause ->
        Gdx.app.log("JojoGame", "Unplayable game audio: $path (${cause.message})")
        null
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
        val path = GameAudioPaths.background(soundId)
        val file = Gdx.files.internal(path)
        if (!file.exists()) {
            Gdx.app.log("JojoGame", "Missing game background track: $path")
            return
        }
        background = loadOrLog(path) { Gdx.audio.newMusic(file) }?.also {
            it.isLooping = true
            it.volume = MUSIC_VOLUME
            if (musicOn()) it.play()
        }
    }

    /**
     * `playEffect`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun playEffect(soundId: Int, mode: Int) {
        if (soundId < 0) return
        // 원본은 중지 요청(mode 0)은 설정과 무관하게 처리하고, 재생만 설정을 본다.
        if (mode != 0 && !effectOn()) return
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
            loadOrLog(file.path()) { Gdx.audio.newSound(file) }?.also { effects[soundId] = it } ?: return
        }
        // 원본 `Sound.playEffect`는 같은 효과음을 100밀리초 안에 다시 부르면 무시한다.
        val now = System.currentTimeMillis()
        if (now - (lastEffectPlayedAt[soundId] ?: 0L) < EFFECT_REPEAT_INTERVAL_MS) return
        lastEffectPlayedAt[soundId] = now
        val instance = if (mode < 0) sound.loop() else sound.play()
        activeEffects[soundId] = instance
    }

    /**
     * `effectPath`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun effectPath(soundId: Int): String = GameAudioPaths.effect(soundId)

    private companion object {
        /** 같은 효과음을 잇달아 부를 때 무시하는 간격이다. 원본과 같이 100밀리초다. */
        const val EFFECT_REPEAT_INTERVAL_MS = 100L

        /**
         * 배경음 크기다.
         *
         * 원본 `Sound.getMusicVolume`의 기본값이 0.5다(효과음은 1.0이라 따로 두지 않는다).
         * 설정 창에 크기 조절이 없으므로 저장값 대신 그 기본값을 그대로 쓴다.
         */
        const val MUSIC_VOLUME = .5f
    }

    /** 재생 중인 오디오 자원을 중지하고 해제한다. */
    fun dispose() {
        background?.dispose()
        effects.values.forEach(Sound::dispose)
        effects.clear()
        activeEffects.clear()
        lastEffectPlayedAt.clear()
    }
}

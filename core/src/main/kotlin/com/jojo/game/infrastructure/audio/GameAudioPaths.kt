// Infrastructure
package com.jojo.game.infrastructure.audio

/**
 * GameAudioPaths: 원본의 사운드 식별자를 내보낸 MP3 경로로 바꾼다.
 *
 * 원본 `UIFrame.playBackgroundSound`는 배경음 번호에 2를 더해
 * `Game/SoundTrk/<두 자리>-AudioTrack <두 자리>`를 읽고, `UIFrame.playSoundEffect`는
 * 200 이상이면 `Se_e_`, 100 이상이면 `Se_m_`, 그 밖에는 `Se`를 앞에 붙이고 남은 값을
 * 두 자리로 적어 `Game/Sound/`에서 읽는다. 이 규칙을 그대로 옮겼다.
 */
internal object GameAudioPaths {
    /** 배경음 경로: 번호에 2를 더한 값을 두 자리로 적는다. */
    fun background(soundId: Int): String {
        val track = (soundId + 2).toString().padStart(2, '0')
        return "audio/$track-AudioTrack $track.mp3"
    }

    /** 효과음 경로: 200·100 경계로 접두사를 고르고 남은 값을 두 자리로 적는다. */
    fun effect(soundId: Int): String = when {
        soundId >= 200 -> "audio/Se_e_${(soundId - 200).toString().padStart(2, '0')}.mp3"
        soundId >= 100 -> "audio/Se_m_${(soundId - 100).toString().padStart(2, '0')}.mp3"
        else -> "audio/Se${soundId.toString().padStart(2, '0')}.mp3"
    }
}

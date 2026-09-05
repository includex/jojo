package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

/** Plays the source game's Cocos sound identifiers from the extracted MP3 assets. */
class OriginalAudioPlayer {
    // Several extracted source MP3s use an encoder stream that the desktop
    // JLayer backend rejects during OpenAL's later update callback.  That
    // exception otherwise terminates the render loop.  Audio can be enabled
    // explicitly once the source streams are transcoded/verified; visuals
    // and battle state must never depend on a decoder crash.
    private val enabled = System.getProperty("jojo.audio", "false").toBoolean()
    private var playingBackgroundId: Int? = null
    private var background: Music? = null
    private val effects = mutableMapOf<Int, Sound>()
    /** Cocos keeps a playback handle even for one-shot effects so a later stage.effectSound(..., 0) can stop it. */
    private val activeEffects = mutableMapOf<Int, Long>()

    fun sync(stage: ScenarioStage) {
        if (!enabled) return
        if (playingBackgroundId != stage.backgroundSound) playBackground(stage.backgroundSound)
        stage.consumeSoundEffects().forEach { playEffect(it.soundId, it.mode) }
    }

    /** Direct BattleLayer sound path (attacks, damage and Meff animations). */
    fun playBattleEffect(soundId: Int) {
        if (enabled) playEffect(soundId, 1)
    }

    private fun playBackground(soundId: Int) {
        playingBackgroundId = soundId
        background?.dispose()
        background = null
        if (soundId < 0) return
        val track = soundId + 2
        val path = "audio/${track.toString().padStart(2, '0')}-AudioTrack ${track.toString().padStart(2, '0')}.mp3"
        val file = Gdx.files.internal(path)
        if (!file.exists()) {
            Gdx.app.log("JojoPort", "Missing original background track: $path")
            return
        }
        background = Gdx.audio.newMusic(file).also { it.isLooping = true; it.play() }
    }

    private fun playEffect(soundId: Int, mode: Int) {
        if (soundId < 0) return
        if (mode == 0) {
            activeEffects.remove(soundId)?.let { instance -> effects[soundId]?.stop(instance) }
            return
        }
        val sound = effects[soundId] ?: run {
            val file = Gdx.files.internal(effectPath(soundId))
            if (!file.exists()) {
                Gdx.app.log("JojoPort", "Missing original sound effect: ${file.path()}")
                return
            }
            Gdx.audio.newSound(file).also { effects[soundId] = it }
        }
        val instance = if (mode < 0) sound.loop() else sound.play()
        activeEffects[soundId] = instance
    }

    private fun effectPath(soundId: Int): String = when {
        soundId >= 200 -> "audio/Se_e_${(soundId - 200).toString().padStart(2, '0')}.mp3"
        soundId >= 100 -> "audio/Se_m_${(soundId - 100).toString().padStart(2, '0')}.mp3"
        else -> "audio/Se${soundId.toString().padStart(2, '0')}.mp3"
    }

    fun dispose() {
        background?.dispose()
        effects.values.forEach(Sound::dispose)
        effects.clear()
        activeEffects.clear()
    }
}

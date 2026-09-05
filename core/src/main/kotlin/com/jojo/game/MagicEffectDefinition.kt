package com.jojo.game

/** Frame metadata used to render a battle magic-effect strip. */
data class MagicEffectDefinition(
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

    /** The authored clip uses 12/24fps and playback speed 1.5. */
    val duration: Float get() = showFrames / (if (uses24Fps) 36f else 18f)

    /** The first keyed frame is the effect's hit event. */
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

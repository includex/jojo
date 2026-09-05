package com.jojo.port

/** Exact timing/atlas layout used by UIFrame.CreateAnime2 for U_select_20. */
object BattleObjectAnimationTimeline {
    const val FRAME_SIZE = 48
    const val FRAME_TICKS = 8
    const val SOURCE_FPS = 24f

    /** CreateAnime2 advances one frame every 8 source ticks and loops. */
    fun row(elapsedSeconds: Float, startRow: Int, frameCount: Int): Int {
        require(frameCount > 0)
        val elapsedTicks = (elapsedSeconds.coerceAtLeast(0f) * SOURCE_FPS).toInt()
        return startRow + (elapsedTicks / FRAME_TICKS) % frameCount
    }

    /** U_select_20 is a contiguous 48x48 strip; CreateAnime2's gutter is 0. */
    fun sourceY(row: Int): Int = row * FRAME_SIZE
}

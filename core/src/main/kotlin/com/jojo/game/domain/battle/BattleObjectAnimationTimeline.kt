// Battle
package com.jojo.game.domain.battle

/** BattleObjectAnimationTimeline: 전장 객체의 프레임 위치를 계산하며, 시간에 따른 행과 원본 좌표를 제공한다. */
object BattleObjectAnimationTimeline {
    const val FRAME_SIZE = 48
    const val FRAME_TICKS = 8
    const val SOURCE_FPS = 24f
    fun row(elapsedSeconds: Float, startRow: Int, frameCount: Int): Int {
        require(frameCount > 0)
        val elapsedTicks = (elapsedSeconds.coerceAtLeast(0f) * SOURCE_FPS).toInt()
        return startRow + (elapsedTicks / FRAME_TICKS) % frameCount
    }
    fun sourceY(row: Int): Int = row * FRAME_SIZE
}

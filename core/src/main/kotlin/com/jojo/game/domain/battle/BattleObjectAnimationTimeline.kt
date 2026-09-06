// Battle
package com.jojo.game.domain.battle

/** BattleObjectAnimationTimeline: 전장 객체의 프레임 위치를 계산하며, 시간에 따른 행과 원본 좌표를 제공한다. */
object BattleObjectAnimationTimeline {
    /**
     * `FRAME_SIZE` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val FRAME_SIZE = 48
    /**
     * `FRAME_TICKS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val FRAME_TICKS = 8
    /**
     * `SOURCE_FPS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val SOURCE_FPS = 24f
    /**
     * `row`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun row(elapsedSeconds: Float, startRow: Int, frameCount: Int): Int {
        require(frameCount > 0)
        val elapsedTicks = (elapsedSeconds.coerceAtLeast(0f) * SOURCE_FPS).toInt()
        return startRow + (elapsedTicks / FRAME_TICKS) % frameCount
    }
    /**
     * `sourceY`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun sourceY(row: Int): Int = row * FRAME_SIZE
}

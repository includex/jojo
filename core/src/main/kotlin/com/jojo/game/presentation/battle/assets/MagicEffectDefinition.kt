// Battle
package com.jojo.game.presentation.battle.assets

/** 전투 마법 효과를 그리는 데 필요한 프레임 정의입니다. */
data class MagicEffectDefinition(
    val showFrames: Int,
    val frameCount: Int,
    val uses24Fps: Boolean,
    val frameWidth: Int,
    val frameHeight: Int,
    val soundId: Int,
    val frames: List<Frame>,
) {
    /** 개별 효과 프레임의 원본 위치와 타격 여부입니다. */
    data class Frame(
        /**
         * `sourceIndex` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sourceIndex: Int,
        /**
         * `alpha` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val alpha: Int,
        /**
         * `offsetX` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offsetX: Int,
        /**
         * `offsetY` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offsetY: Int,
        /**
         * `hit` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hit: Boolean,
    )

    /** 원본 클립의 프레임 수를 재생 시간으로 환산합니다. */
    val duration: Float get() = showFrames / (if (uses24Fps) 36f else 18f)

    /** 첫 타격 프레임까지의 시간을 반환합니다. */
    val hitTime: Float
        get() {
            /**
             * `index` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val index = frames.indexOfFirst(Frame::hit)
            return if (index < 0) duration else index / (if (uses24Fps) 36f else 18f)
        }

    /** 경과 시간에 해당하는 효과 프레임을 반환합니다. */
    fun frameAt(elapsed: Float): Frame? {
        if (frames.isEmpty()) return null
        val frame = (elapsed * if (uses24Fps) 36f else 18f).toInt()
        return frames[frame.coerceIn(0, frames.lastIndex)]
    }
}

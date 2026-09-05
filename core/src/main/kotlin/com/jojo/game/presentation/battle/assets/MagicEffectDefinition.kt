package com.jojo.game.presentation.battle.assets

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
    /**
     * data class  `Frame`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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
    val hitTime: Float
        get() {
            val index = frames.indexOfFirst(Frame::hit)
            return if (index < 0) duration else index / (if (uses24Fps) 36f else 18f)
        }

    /**
     * 공개 메서드 `frameAt`
     *
     * ### 파라미터
    - `elapsed` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Frame?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun frameAt(elapsed: Float): Frame? {
        if (frames.isEmpty()) return null
        val frame = (elapsed * if (uses24Fps) 36f else 18f).toInt()
        return frames[frame.coerceIn(0, frames.lastIndex)]
    }
}

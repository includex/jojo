package com.jojo.game

/**
 * Status-effect animation state used by [BattleUnitPresentationState.refreshStatus].
 *
 * The Cocos component only renders the first two abnormal statuses from the
 * MB..ZD range.  Keeping this small state machine outside the renderer makes
 * that otherwise visual-only selection and its `_lastRefState` cache
 * testable.
 */
/**
 * class  `BattleUnitStateAnimation`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleUnitStateAnimation {
    /**
     * data class  `Effect`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Effect(
        /** Indices into the original scene's `state_texture` array. */
        val textureIndices: List<Int>,
        /** `anime_state` uses constant key positions at these two frames. */
        val positions: List<Pair<Int, Int>> = listOf(-16 to 16, 16 to 16),
        val framesPerSecond: Int = 3,
        val loop: Boolean = true,
        val active: Boolean = true,
    ) {
        /**
         * data class  `Sample`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Sample(val textureIndex: Int, val position: Pair<Int, Int>)

        /** `createWithSpriteFrames(frames, 3)` with constant position keys. */
        fun sampleAt(secondsSinceCreate: Float): Sample {
            val frame = ((secondsSinceCreate.coerceAtLeast(0f) * framesPerSecond).toInt() % textureIndices.size)
            return Sample(textureIndices[frame], positions[frame])
        }
    }

    private var lastRefState = 0
    private var effect: Effect? = null

    /** Current source `_state_meff`, or null if no MB..ZD status is active. */
    fun current(): Effect? = effect

    /** Changes visibility without discarding the selected status frames. */
    fun setVisible(visible: Boolean) {
        effect = effect?.copy(active = visible)
    }

    /**
     * `activeStatuses` is ordered exactly as `BATTLE_UNIT_STATUS2.MB..ZD`.
     * Source records at most two active entries and only those entries
     * participate in the cache bit mask.
     */
    /**
     * 공개 메서드 `refresh`
     *
     * ### 파라미터
    - `activeStatuses` (`List<Boolean>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Effect?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun refresh(activeStatuses: List<Boolean>): Effect? {
        val selected = mutableListOf<Int>()
        var mask = 0
        activeStatuses.forEachIndexed { index, active ->
            if (!active || selected.size == 2) return@forEachIndexed
            mask = mask or (1 shl index)
            selected += index
        }
        if (mask == lastRefState) {
            // Source re-enables an existing effect without recreating it.
            if (effect != null && !effect!!.active) effect = effect!!.copy(active = true)
            return effect
        }

        lastRefState = mask
        effect = if (selected.isEmpty()) null else Effect(
            textureIndices = if (selected.size == 1) {
                // It creates two identical SpriteFrames for one status.
                listOf(selected[0], selected[0])
            } else {
                listOf(selected[0], selected[1])
            }
        )
        return effect
    }
}

// Battle
package com.jojo.game.presentation.battle.unit

import com.jojo.game.*
/** BattleUnitStateAnimation: 전투 유닛 상태 애니메이션이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */

class BattleUnitStateAnimation {

    /** Effect: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    data class Effect(
        /**
         * `textureIndices` (List<Int>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val textureIndices: List<Int>,
        /**
         * `positions` (List<Pair<Int, Int>>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val positions: List<Pair<Int, Int>> = listOf(-16 to 16, 16 to 16),
        /**
         * `framesPerSecond` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val framesPerSecond: Int = 3,
        /**
         * `loop` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val loop: Boolean = true,
        /**
         * `active` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val active: Boolean = true,
    ) {

        /** Sample: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
        data class Sample(val textureIndex: Int, val position: Pair<Int, Int>)
        /**
         * `sampleAt`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun sampleAt(secondsSinceCreate: Float): Sample {
            val frame = ((secondsSinceCreate.coerceAtLeast(0f) * framesPerSecond).toInt() % textureIndices.size)
            return Sample(textureIndices[frame], positions[frame])
        }
    }

    /**
     * `lastRefState` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var lastRefState = 0
    /**
     * `effect` (Effect?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var effect: Effect? = null
    /**
     * `current`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun current(): Effect? = effect
    /**
     * `setVisible`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setVisible(visible: Boolean) {
        effect = effect?.copy(active = visible)
    }

    /**
     * `refresh`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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
            if (effect != null && !effect!!.active) effect = effect!!.copy(active = true)
            return effect
        }

        lastRefState = mask
        effect = if (selected.isEmpty()) null else Effect(
            textureIndices = if (selected.size == 1) {
                listOf(selected[0], selected[0])
            } else {
                listOf(selected[0], selected[1])
            }
        )
        return effect
    }
}

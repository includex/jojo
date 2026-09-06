// Battle
package com.jojo.game.presentation.battle.unit

import com.jojo.game.*
/** BattleUnitStateAnimation: 전투 유닛 상태 애니메이션이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */

class BattleUnitStateAnimation {

    /** Effect: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    data class Effect(
        val textureIndices: List<Int>,
        val positions: List<Pair<Int, Int>> = listOf(-16 to 16, 16 to 16),
        val framesPerSecond: Int = 3,
        val loop: Boolean = true,
        val active: Boolean = true,
    ) {

        /** Sample: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
        data class Sample(val textureIndex: Int, val position: Pair<Int, Int>)
        fun sampleAt(secondsSinceCreate: Float): Sample {
            val frame = ((secondsSinceCreate.coerceAtLeast(0f) * framesPerSecond).toInt() % textureIndices.size)
            return Sample(textureIndices[frame], positions[frame])
        }
    }

    private var lastRefState = 0
    private var effect: Effect? = null
    fun current(): Effect? = effect
    fun setVisible(visible: Boolean) {
        effect = effect?.copy(active = visible)
    }

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

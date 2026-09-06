// Battle
package com.jojo.game.presentation.battle.fight
/**
 * `FightUnit`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class FightUnit(
    var parentX: Int,
    var parentY: Int,
    var parentScaleX: Int,
    var nodeX: Int,
    var nodeY: Int,
    var nodeScaleX: Int,
) {
    /**
     * `action` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var action = 0
        private set
    /**
     * `animation` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var animation = ""
        private set
    /**
     * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val events = mutableListOf<String>()
    /**
     * `reset`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun reset() {
        parentX += 4 * nodeX * if (parentScaleX < 0) -1 else 1
        nodeX = 0; nodeY = 0
        parentScaleX *= if (nodeScaleX < 0) -1 else 1
        nodeScaleX = 1
    }


    /**
     * `create`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun create(moveSound: Int, soundEvent: String, shaderEvents: List<Int>) {
        nodeX = 0; nodeY = 0
        if (soundEvent != "yidong" || moveSound != -1) {
            val value = if (soundEvent == "yidong") moveSound else soundEvent.toInt()
            events += if (value > 300) "background:${value - 300}" else "effect:$value"
        }
        shaderEvents.forEach { value ->
            events += when {
                value >= 200 -> "material:gray"
                value >= 100 -> {
                    events += "material:highlight"
                    "value:${(value - 100) / 10.0}"
                }

                value == 0 -> "material:def"
                else -> "value:${value / 10.0}"
            }
        }
    }


    /**
     * `setActionDir`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setActionDir(value: Int, finished: Boolean) {
        action = value; reset(); animation = "anime$value"
        if (finished) events += "finished"
    }
}

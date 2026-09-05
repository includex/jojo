package com.jojo.game

/** State implementation of recovered battle/FightUnit.js, excluding Cocos drawing primitives. */
class FightUnit(
    var parentX: Int,
    var parentY: Int,
    var parentScaleX: Int,
    var nodeX: Int,
    var nodeY: Int,
    var nodeScaleX: Int,
) {
    var action = 0
        private set
    var animation = ""
        private set
    val events = mutableListOf<String>()

    /** Exact `_reset`: parent moves by 4 * child x, child is reset, then signs are normalized. */
    fun reset() {
        parentX += 4 * nodeX * if (parentScaleX < 0) -1 else 1
        nodeX = 0; nodeY = 0
        parentScaleX *= if (nodeScaleX < 0) -1 else 1
        nodeScaleX = 1
    }

    /**
     * 공개 메서드 `create`
     *
     * ### 파라미터
    - `moveSound` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `soundEvent` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `shaderEvents` (`List<Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `setActionDir`
     *
     * ### 파라미터
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `finished` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setActionDir(value: Int, finished: Boolean) {
        action = value; reset(); animation = "anime$value"
        if (finished) events += "finished"
    }
}

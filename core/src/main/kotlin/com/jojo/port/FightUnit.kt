package com.jojo.port

/** State port of recovered battle/FightUnit.js, excluding Cocos drawing primitives. */
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
    fun setActionDir(value: Int, finished: Boolean) {
        action = value; reset(); animation = "anime$value"
        if (finished) events += "finished"
    }
}

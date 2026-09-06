package com.jojo.game.presentation.scenario

/** 렌더링 보간에 사용하는 전술 유닛 상태입니다. */
data class TacticalUnit(
    val id: Int,
    var x: Int,
    var y: Int,
    var direction: Int = 0,
    var action: Int = 0,
    var visible: Boolean = true,
    var posts: Int = 0,
    var ai: Int = 0,
    var aiTargetId: Int = -1,
    var aiTargetX: Int = 0,
    var aiTargetY: Int = 0,
) {
    var visualX: Float = x.toFloat()
    var visualY: Float = y.toFloat()
    var moveFromX: Float = visualX
    var moveFromY: Float = visualY
    var moveElapsed: Float = 0f
    var animationElapsed: Float = 0f
    var moveZIndex: Float = 4f * (x + y) - 424f
    var moveDuration: Float = 0f
    var movePath: List<Pair<Int, Int>> = emptyList()
    var moveToX: Int = x
    var moveToY: Int = y
    var moveFinalDirection: Int = direction
    var moveJustStarted: Boolean = false
}

/** 시나리오 이벤트에 표시되는 초상화의 보간 상태입니다. */
data class ScenarioHead(val characterId: Int, var x: Int = 0, var y: Int = 0, var visible: Boolean = true) {
    var visualX: Float = x.toFloat()
    var visualY: Float = y.toFloat()
    var moveFromX: Float = visualX
    var moveFromY: Float = visualY
    var moveElapsed: Float = 0f
    var moveDuration: Float = 0f
    var opacity: Float = 1f
    var fadeFrom: Float = opacity
    var fadeTo: Float = opacity
    var fadeElapsed: Float = 0f
    var fadeDuration: Float = 0f
}

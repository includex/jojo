// Scenario
package com.jojo.game.domain.scenario

/** TacticalUnit: 시나리오 스테이지에서 유닛의 논리 위치와 이동 보간 상태를 함께 보관한다. */
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
    /** visualX: 화면 보간에 사용하는 현재 가로 좌표이다. */
    var visualX: Float = x.toFloat()
    /** visualY: 화면 보간에 사용하는 현재 세로 좌표이다. */
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

/** ScenarioHead: 시나리오 인물 초상화의 위치·투명도 보간 상태를 보관한다. */
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

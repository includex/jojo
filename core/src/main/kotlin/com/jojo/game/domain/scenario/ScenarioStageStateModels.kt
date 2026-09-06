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
    /**
     * `moveFromX` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveFromX: Float = visualX
    /**
     * `moveFromY` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveFromY: Float = visualY
    /**
     * `moveElapsed` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveElapsed: Float = 0f
    /**
     * `animationElapsed` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var animationElapsed: Float = 0f
    /**
     * `moveZIndex` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveZIndex: Float = 4f * (x + y) - 424f
    /**
     * `moveDuration` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveDuration: Float = 0f
    /**
     * `movePath` (List<Pair<Int, Int>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var movePath: List<Pair<Int, Int>> = emptyList()
    /**
     * `moveToX` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveToX: Int = x
    /**
     * `moveToY` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveToY: Int = y
    /**
     * `moveFinalDirection` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveFinalDirection: Int = direction
    /**
     * `moveJustStarted` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveJustStarted: Boolean = false
}

/** ScenarioHead: 시나리오 인물 초상화의 위치·투명도 보간 상태를 보관한다. */
data class ScenarioHead(val characterId: Int, var x: Int = 0, var y: Int = 0, var visible: Boolean = true) {
    /**
     * `visualX` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var visualX: Float = x.toFloat()
    /**
     * `visualY` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var visualY: Float = y.toFloat()
    /**
     * `moveFromX` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveFromX: Float = visualX
    /**
     * `moveFromY` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveFromY: Float = visualY
    /**
     * `moveElapsed` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveElapsed: Float = 0f
    /**
     * `moveDuration` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveDuration: Float = 0f
    /**
     * `opacity` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var opacity: Float = 1f
    /**
     * `fadeFrom` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var fadeFrom: Float = opacity
    /**
     * `fadeTo` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var fadeTo: Float = opacity
    /**
     * `fadeElapsed` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var fadeElapsed: Float = 0f
    /**
     * `fadeDuration` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var fadeDuration: Float = 0f
}

// Game
package com.jojo.game.presentation.scenario.hall

/** FeatsLayer: 업적 화면의 입력과 행 표시 상태를 관리한다. */
class FeatsLayer(rows: List<Row>) {

    /**
     * `Row`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Row(
        /**
         * `title` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val title: String,
        /**
         * `ability` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val ability: Int,
        /**
         * `progress` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val progress: Int,
        /**
         * `nextProgress` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val nextProgress: Int,
        /**
         * `nextAbilityPhase` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val nextAbilityPhase: Int,
    ) {
        /**
         * `progressRatio` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val progressRatio: Float
            get() = if (nextProgress <= 0) 0f else progress.toFloat() / nextProgress
        /**
         * `progressLabel` (String get()): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val progressLabel: String get() = "$progress/$nextProgress"
        /**
         * `phaseLabel` (String get()): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val phaseLabel: String get() = if (nextAbilityPhase == 0) "MAX" else nextAbilityPhase.toString()
    }


    /**
     * `Route`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class Route { HELP }


    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(val rows: List<Row>, val attached: Boolean, val route: Route?)

    /**
     * `rows` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val rows = rows.toList()
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
        private set
    /**
     * `route` (Route?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var route: Route? = null

    init {
        require(this.rows.size == TITLES.size)
        require(this.rows.map(Row::title) == TITLES)
    }


    /**
     * `onCancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCancel(eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        attached = false
        return true
    }


    /**
     * `onButton`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onButton(index: Int, eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        return when (index) {
            0 -> {
                attached = false; true
            }

            1 -> {
                route = Route.HELP; true
            }

            else -> false
        }
    }


    /**
     * `consumeRoute`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun consumeRoute(): Route? = route.also { route = null }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() = View(rows, attached, route)

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
        /**
         * `TITLES` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val TITLES = listOf("무력", "지휘", "지력", "민첩성", "운기")
        /**
         * `HELP_TEXT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val HELP_TEXT = "공격 시 무력 증가, 방어 시 통솔 증가, 주도/패시브 전략 사용 시 지력 증가, 연타/공격 회피 시 민첩 증가, 치명타/전략 회피 시 운 증가."
    }
}

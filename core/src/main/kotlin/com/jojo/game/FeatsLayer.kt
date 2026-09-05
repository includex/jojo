package com.jojo.game

/** Stateful implementation of Global/scene/FeatsLayer's input and row presentation. */
class FeatsLayer(rows: List<Row>) {
    /**
     * data class  `Row`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Row(
        val title: String,
        val ability: Int,
        val progress: Int,
        val nextProgress: Int,
        val nextAbilityPhase: Int,
    ) {
        val progressRatio: Float
            get() = if (nextProgress <= 0) 0f else progress.toFloat() / nextProgress
        val progressLabel: String get() = "$progress/$nextProgress"
        val phaseLabel: String get() = if (nextAbilityPhase == 0) "MAX" else nextAbilityPhase.toString()
    }

    /**
     * enum class  `Route`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Route { HELP }

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(val rows: List<Row>, val attached: Boolean, val route: Route?)

    private val rows = rows.toList()
    var attached = true
        private set
    private var route: Route? = null

    init {
        require(this.rows.size == TITLES.size)
        require(this.rows.map(Row::title) == TITLES)
    }

    /**
     * 공개 메서드 `onCancel`
     *
     * ### 파라미터
    - `eventType` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCancel(eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        attached = false
        return true
    }

    /**
     * 공개 메서드 `onButton`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `eventType` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `consumeRoute`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Route?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun consumeRoute(): Route? = route.also { route = null }

    /**
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view() = View(rows, attached, route)

    companion object {
        const val TOUCH_END = 2
        val TITLES = listOf("무력", "지휘", "지력", "민첩성", "운기")
        const val HELP_TEXT = "공격 시 무력 증가, 방어 시 통솔 증가, 주도/패시브 전략 사용 시 지력 증가, 연타/공격 회피 시 민첩 증가, 치명타/전략 회피 시 운 증가."
    }
}

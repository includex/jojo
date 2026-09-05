package com.jojo.port

/** Stateful port of Global/scene/FeatsLayer's input and row presentation. */
class FeatsLayer(rows: List<Row>) {
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

    enum class Route { HELP }

    data class View(val rows: List<Row>, val attached: Boolean, val route: Route?)

    private val rows = rows.toList()
    var attached = true
        private set
    private var route: Route? = null

    init {
        require(this.rows.size == TITLES.size)
        require(this.rows.map(Row::title) == TITLES)
    }

    fun onCancel(eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        attached = false
        return true
    }

    fun onButton(index: Int, eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        return when (index) {
            0 -> { attached = false; true }
            1 -> { route = Route.HELP; true }
            else -> false
        }
    }

    fun consumeRoute(): Route? = route.also { route = null }
    fun view() = View(rows, attached, route)

    companion object {
        const val TOUCH_END = 2
        val TITLES = listOf("무력", "지휘", "지력", "민첩성", "운기")
        const val HELP_TEXT = "공격 시 무력 증가, 방어 시 통솔 증가, 주도/패시브 전략 사용 시 지력 증가, 연타/공격 회피 시 민첩 증가, 치명타/전략 회피 시 운 증가."
    }
}

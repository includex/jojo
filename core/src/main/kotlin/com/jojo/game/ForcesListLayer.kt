package com.jojo.game

/** Stateful implementation of `ui/ForcesListLayer.js` (onCreate/_changeSel/_onClick). */
class ForcesListLayer {
    /**
     * data class  `Unit`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Unit(
        val id: Int, val name: String, val post: String, val level: Int,
        val hp: Int, val maxHp: Int, val mp: Int, val maxMp: Int,
        val attack: Int, val defense: Int, val spirit: Int, val critical: Int, val morale: Int,
        val famous: Boolean = false, val status: Map<Int, Int> = emptyMap(),
        /** BattleUnit.index() is distinct from Unit.id() in the original list sorter. */
        val battleIndex: Int = id,
        val poisoned: Boolean = false, val fengZhou: Boolean = false,
    )

    /**
     * data class  `Row`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Row(val unit: Unit, val colors: List<RowColor>, val labels: List<String>)

    /**
     * enum class  `RowColor`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class RowColor { BLACK, RED, BLUE }

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(
        val selectedTab: Int,
        val rows: List<Row>,
        val tabsVisible: Boolean,
        val attached: Boolean,
        val selectedIndex: Int? = null
    )

    private var mine = emptyList<Unit>()
    private var enemy = emptyList<Unit>()
    private var flag = 0
    private var view: View? = null

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `mine` (`List<Unit>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `enemy` (`List<Unit> = emptyList(`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(mine: List<Unit>, enemy: List<Unit> = emptyList(), flag: Int): View {
        this.mine = mine; this.enemy = enemy; this.flag = flag
        return changeSel(0)
    }

    /** Toggles and the original famous/index sorting only exist in battle mode. */
    fun changeSel(tab: Int): View {
        require(tab in 0..1)
        if (tab == 1 && flag and 1 == 0) return view()
        val source = if (tab == 0) mine else enemy
        val ordered =
            if (flag and 1 != 0) source.sortedWith(compareBy<Unit> { !it.famous }.thenBy { it.battleIndex }) else source
        val rows = ordered.map { unit ->
            val values = listOf(
                unit.name,
                unit.post,
                unit.level.toString(),
                "${unit.hp}/${unit.maxHp}",
                "${unit.mp}/${unit.maxMp}",
                unit.attack.toString(),
                unit.defense.toString(),
                unit.spirit.toString(),
                unit.critical.toString(),
                unit.morale.toString()
            )
            Row(unit, values.indices.map { i ->
                if (flag and 1 == 0) RowColor.BLACK else when {
                    // Recovered JS colors label4 for poison and label5 for FengZhou.
                    // Keep the original label indices, even though they look unusual.
                    i == 4 && unit.poisoned -> RowColor.RED
                    i == 5 && unit.fengZhou -> RowColor.RED
                    i < 5 -> RowColor.BLACK
                    unit.status[i - 5] == 0 -> RowColor.RED
                    unit.status[i - 5] == 1 -> RowColor.BLUE
                    else -> RowColor.BLACK
                }
            }, values)
        }
        return View(tab, rows, flag and 1 != 0, true).also { view = it }
    }

    /** item listener opens UnitInfoLayer on TOUCH_END; host consumes selected index. */
    fun onRowTouch(index: Int, event: Int): Unit? {
        if (event != TOUCH_END) return null
        val unit = view().rows.getOrNull(index)?.unit ?: return null
        view = view().copy(selectedIndex = index); return unit
    }

    /** Panel_cancel and button0 both close only on TOUCH_END. */
    fun onClose(event: Int): Boolean {
        if (event != TOUCH_END) return false; view = view().copy(attached = false); return true
    }

    /**
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view(): View = requireNotNull(view)

    companion object {
        const val TOUCH_END = 2
    }
}

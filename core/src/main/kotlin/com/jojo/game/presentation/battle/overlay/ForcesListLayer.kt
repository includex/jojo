// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.*


/** 아군·적군 유닛을 탭별 행으로 정렬하고 능력치와 상태 색상을 계산한다. */
class ForcesListLayer {
    data class Unit(
        val id: Int, val name: String, val post: String, val level: Int,
        val hp: Int, val maxHp: Int, val mp: Int, val maxMp: Int,
        val attack: Int, val defense: Int, val spirit: Int, val critical: Int, val morale: Int,
        val famous: Boolean = false, val status: Map<Int, Int> = emptyMap(),
        val battleIndex: Int = id,
        val poisoned: Boolean = false, val fengZhou: Boolean = false,
    )
    data class Row(val unit: Unit, val colors: List<RowColor>, val labels: List<String>)
    enum class RowColor { BLACK, RED, BLUE }


    /** 선택 탭의 부대 행과 탭 표시 여부를 렌더링 입력으로 제공한다. */
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


    fun onCreate(mine: List<Unit>, enemy: List<Unit> = emptyList(), flag: Int): View {
        this.mine = mine; this.enemy = enemy; this.flag = flag
        return changeSel(0)
    }
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

    /** 선택한 부대 행의 인덱스를 저장하고 해당 유닛을 콜백으로 전달한다. */
    fun onRowTouch(index: Int, event: Int): Unit? {
        if (event != TOUCH_END) return null
        val unit = view().rows.getOrNull(index)?.unit ?: return null
        view = view().copy(selectedIndex = index); return unit
    }

    /** 부대 목록을 닫고 다음 표시 요청에서 연결되지 않은 상태를 반환한다. */
    fun onClose(event: Int): Boolean {
        if (event != TOUCH_END) return false; view = view().copy(attached = false); return true
    }


    fun view(): View = requireNotNull(view)

    companion object {
        const val TOUCH_END = 2
    }
}

// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.*


/** 아군·적군 유닛을 탭별 행으로 정렬하고 능력치와 상태 색상을 계산한다. */
class ForcesListLayer {
    /**
     * `Unit`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Unit(
        /**
         * `id` (Int, val name: String, val post: String, val level: Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int, val name: String, val post: String, val level: Int,
        /**
         * `hp` (Int, val maxHp: Int, val mp: Int, val maxMp: Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hp: Int, val maxHp: Int, val mp: Int, val maxMp: Int,
        /**
         * `attack` (Int, val defense: Int, val spirit: Int, val critical: Int, val morale: Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attack: Int, val defense: Int, val spirit: Int, val critical: Int, val morale: Int,
        /**
         * `famous` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val famous: Boolean = false, val status: Map<Int, Int> = emptyMap(),
        /**
         * `battleIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleIndex: Int = id,
        /**
         * `poisoned` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val poisoned: Boolean = false, val fengZhou: Boolean = false,
    )
    /**
     * `Row`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Row(val unit: Unit, val colors: List<RowColor>, val labels: List<String>)
    /**
     * `RowColor`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class RowColor { BLACK, RED, BLUE }


    /** 선택 탭의 부대 행과 탭 표시 여부를 렌더링 입력으로 제공한다. */
    data class View(
        /**
         * `selectedTab` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val selectedTab: Int,
        /**
         * `rows` (List<Row>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val rows: List<Row>,
        /**
         * `tabsVisible` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val tabsVisible: Boolean,
        /**
         * `attached` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attached: Boolean,
        /**
         * `selectedIndex` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val selectedIndex: Int? = null
    )

    /**
     * `mine` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var mine = emptyList<Unit>()
    /**
     * `enemy` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var enemy = emptyList<Unit>()
    /**
     * `flag` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var flag = 0
    /**
     * `view` (View?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var view: View? = null


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(mine: List<Unit>, enemy: List<Unit> = emptyList(), flag: Int): View {
        this.mine = mine; this.enemy = enemy; this.flag = flag
        return changeSel(0)
    }
    /**
     * `changeSel`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): View = requireNotNull(view)

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
    }
}

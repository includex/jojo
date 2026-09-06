// Shared
package com.jojo.game.presentation.shared.overlay

/** UnitInfoLayer: 유닛 정보 화면의 입력과 표시 상태를 구현한다. */
class UnitInfoLayer(
    /** `units` (List<Unit>): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val units: List<Unit>,
    /** `flag` (Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val flag: Int = 0,
    /** `editEnabled` (Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val editEnabled: Boolean = false,
    /** `defaultTab` (Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val defaultTab: Int = 0,
    /** `featsEnabled` (Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val featsEnabled: Boolean = false,
    /** `singleValueMode` (Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val singleValueMode: Boolean = false,
) {

    /**
     * `Equipment`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Equipment(val name: String)


    /**
     * `Unit`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Unit(
        /**
         * `id` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /**
         * `post` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val post: String,
        /**
         * `level` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val level: Int,
        /**
         * `hp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hp: Int,
        /**
         * `maxHp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxHp: Int,
        /**
         * `mp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mp: Int,
        /**
         * `maxMp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxMp: Int,
        /**
         * `attack` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attack: Int,
        /**
         * `defense` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val defense: Int,
        /**
         * `spirit` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val spirit: Int,
        /**
         * `critical` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val critical: Int,
        /**
         * `morale` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val morale: Int,
        /**
         * `magic` (List<String>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magic: List<String> = emptyList(),
        /**
         * `mine` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mine: Boolean = true,
        /**
         * `battleCount` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleCount: Int = 0,
        /**
         * `retreatCount` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val retreatCount: Int = 0,
        /**
         * `skillIntro` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val skillIntro: String = "",
        /**
         * `unitIntro` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unitIntro: String = "",
        /**
         * `equipment` (List<Equipment?>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val equipment: List<Equipment?> = listOf(null, null, null),
    )


    /**
     * `Route`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class Route { FEATS, JIQI, EDIT, ITEM, MAGIC }


    /**
     * `RouteRequest`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class RouteRequest(val route: Route, val index: Int, val value: String = "")


    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(
        /**
         * `index` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val index: Int,
        /**
         * `tab` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val tab: Int,
        /**
         * `unit` (Unit,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unit: Unit,
        /**
         * `attached` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attached: Boolean,
        /**
         * `panels` (List<Boolean>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val panels: List<Boolean>,
        /**
         * `interactable` (List<Boolean>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val interactable: List<Boolean>,
        /**
         * `buttons` (List<Boolean>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buttons: List<Boolean>,
        /**
         * `values` (List<Int>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val values: List<Int>,
        /**
         * `showRecord` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val showRecord: Boolean,
        /**
         * `magicRows` (List<String>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magicRows: List<String>
    )

    /**
     * `index` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var index = 0
    /**
     * `tab` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var tab = 0
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = false

    /** 유닛 전환 후에도 유지되는 선택 탭 상태이다. */
    private var persistedTab = defaultTab.takeIf { it in 0..4 } ?: 0
    /**
     * `routes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val routes = mutableListOf<RouteRequest>()


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(index: Int = 0): View {
        require(units.isNotEmpty()); this.index = index; attached = true; refUnit(); return ref()
    }


    /**
     * `onCancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCancel(event: Int): Boolean {
        if (!attached || event != TOUCH_END) return false; attached = false; return true
    }


    /**
     * `onButton`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onButton(button: Int, event: Int): Boolean {
        if (!attached || event != TOUCH_END || button !in 0 until buttonCount()) return false
        when (button) {
            in 0..4 -> {
                tab = button; persistedTab = button
            }; 5 -> {
            index--; refUnit()
        }; 6 -> {
            index++; refUnit()
        }; 7 -> attached = false
            8 -> if (current().mine && featsEnabled) routes += RouteRequest(Route.FEATS, button)
            9 -> if (isBattle()) routes += RouteRequest(Route.JIQI, button)
            10 -> if (editEnabled) routes += RouteRequest(Route.EDIT, button, if (isBattle()) "battleUnit" else "unit")
        }
        return true
    }


    /**
     * `onEquipment`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onEquipment(slot: Int, event: Int): Boolean {
        if (!attached || event != TOUCH_END || slot !in 0..2) return false; current().equipment.getOrNull(slot)
            ?.let { routes += RouteRequest(Route.ITEM, slot, it.name) }; return true
    }


    /**
     * `onMagic`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onMagic(row: Int, event: Int): Boolean {
        if (!attached || event != TOUCH_END || row !in current().magic.indices) return false; routes += RouteRequest(
            Route.MAGIC,
            row,
            current().magic[row]
        ); return true
    }


    /**
     * `takeRoutes`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun takeRoutes() = routes.toList().also { routes.clear() }


    /**
     * `ref`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun ref(): View {
        val u = current()
        val values = listOf(
            u.attack,
            u.defense,
            u.spirit,
            u.critical,
            u.morale
        ).map { if (singleValueMode) it shl 1 else it }; return View(
            index,
            tab,
            u,
            attached,
            List(5) { it == tab },
            List(5) { it != tab },
            List(buttonCount()) {
                when (it) {
                    8 -> u.mine && featsEnabled; 9 -> isBattle(); else -> true
                }
            },
            values,
            !isBattle() || u.mine,
            u.magic
        )
    }

    /**
     * `refUnit`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun refUnit() {
        index = ((index % units.size) + units.size) % units.size; tab = persistedTab
    }

    /**
     * `current`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun current() = units[index]
    /**
     * `isBattle`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun isBattle() = flag and BATTLE_FLAG != 0
    /**
     * `buttonCount`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun buttonCount() = if (editEnabled) 11 else 9

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
        /**
         * `BATTLE_FLAG` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val BATTLE_FLAG = 1
    }
}

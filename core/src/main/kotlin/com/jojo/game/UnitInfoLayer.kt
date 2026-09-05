package com.jojo.game

/** Behavioural implementation of recovered `ui/UnitInfoLayer.js`; event 2 is Cocos TOUCH_END. */
class UnitInfoLayer(
    private val units: List<Unit>,
    private val flag: Int = 0,
    private val editEnabled: Boolean = false,
    private val defaultTab: Int = 0,
    private val featsEnabled: Boolean = false,
    private val singleValueMode: Boolean = false,
) {
    /**
     * data class  `Equipment`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Equipment(val name: String)

    /**
     * data class  `Unit`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Unit(
        val id: Int,
        val name: String,
        val post: String,
        val level: Int,
        val hp: Int,
        val maxHp: Int,
        val mp: Int,
        val maxMp: Int,
        val attack: Int,
        val defense: Int,
        val spirit: Int,
        val critical: Int,
        val morale: Int,
        val magic: List<String> = emptyList(),
        val mine: Boolean = true,
        val battleCount: Int = 0,
        val retreatCount: Int = 0,
        val skillIntro: String = "",
        val unitIntro: String = "",
        val equipment: List<Equipment?> = listOf(null, null, null),
    )

    /**
     * enum class  `Route`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Route { FEATS, JIQI, EDIT, ITEM, MAGIC }

    /**
     * data class  `RouteRequest`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class RouteRequest(val route: Route, val index: Int, val value: String = "")

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(
        val index: Int,
        val tab: Int,
        val unit: Unit,
        val attached: Boolean,
        val panels: List<Boolean>,
        val interactable: List<Boolean>,
        val buttons: List<Boolean>,
        val values: List<Int>,
        val showRecord: Boolean,
        val magicRows: List<String>
    )

    private var index = 0
    private var tab = 0
    private var attached = false

    /** Original m_ud[unit_DEF_IDX]; changing a tab persists across unit switches. */
    private var persistedTab = defaultTab.takeIf { it in 0..4 } ?: 0
    private val routes = mutableListOf<RouteRequest>()

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `index` (`Int=0`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(index: Int = 0): View {
        require(units.isNotEmpty()); this.index = index; attached = true; refUnit(); return ref()
    }

    /**
     * 공개 메서드 `onCancel`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCancel(event: Int): Boolean {
        if (!attached || event != TOUCH_END) return false; attached = false; return true
    }

    /**
     * 공개 메서드 `onButton`
     *
     * ### 파라미터
    - `button` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `onEquipment`
     *
     * ### 파라미터
    - `slot` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onEquipment(slot: Int, event: Int): Boolean {
        if (!attached || event != TOUCH_END || slot !in 0..2) return false; current().equipment.getOrNull(slot)
            ?.let { routes += RouteRequest(Route.ITEM, slot, it.name) }; return true
    }

    /**
     * 공개 메서드 `onMagic`
     *
     * ### 파라미터
    - `row` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onMagic(row: Int, event: Int): Boolean {
        if (!attached || event != TOUCH_END || row !in current().magic.indices) return false; routes += RouteRequest(
            Route.MAGIC,
            row,
            current().magic[row]
        ); return true
    }

    /**
     * 공개 메서드 `takeRoutes`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun takeRoutes() = routes.toList().also { routes.clear() }

    /**
     * 공개 메서드 `ref`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    private fun refUnit() {
        index = ((index % units.size) + units.size) % units.size; tab = persistedTab
    }

    private fun current() = units[index]
    private fun isBattle() = flag and BATTLE_FLAG != 0
    private fun buttonCount() = if (editEnabled) 11 else 9

    companion object {
        const val TOUCH_END = 2
        const val BATTLE_FLAG = 1
    }
}

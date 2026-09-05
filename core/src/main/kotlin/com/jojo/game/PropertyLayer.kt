package com.jojo.game

/**
 * Behavioural implementation of `ui/PropertyLayer.js`.
 *
 * `itemType` is ITEM_EQUIP_TYPE (not the four UI-tab index): 0..19 are
 * weapons, 20..25 armour, 26..45 consumables and values above that are
 * auxiliary equipment.  Keeping that distinction here is important because
 * the original Item.type() derives its UI category from this raw value.
 */
/**
 * class  `PropertyLayer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class PropertyLayer(private val items: List<Item>, private val inventory: Map<Int, Int>) {
    /**
     * data class  `Item`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Item(
        val id: Int, val name: String, val itemType: Int, val icon: Int,
        val level: Int = 0, val owner: String? = null, val exp: Int = 0,
        val expLimit: Int = 0, val typeName: String? = null,
    )

    /**
     * enum class  `Tab`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Tab { WEAPON, ARMOR, AUXILIARY, PROPERTY }

    /**
     * data class  `Row`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Row(val item: Item, val quantity: Int, val labels: List<String>)

    var selected = Tab.WEAPON; private set
    var attached = true; private set
    var scrollRow = 0; private set
    private var propertyPanelInitialized = false

    /** `onCreate` ends with the original `_currentSel(0)`. */
    fun onCreate(): List<Row> {
        attached = true; selected = Tab.WEAPON; scrollRow = 0; return rows()
    }

    /**
     * 공개 메서드 `select`
     *
     * ### 파라미터
    - `tab` (`Tab`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Row>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun select(tab: Tab): List<Row> {
        selected = tab
        val result = rows()
        // ScrollView clamps its offset when the source replaces content rows.
        scrollRow = scrollRow.coerceIn(0, (result.size - 1).coerceAtLeast(0))
        return result
    }

    /** Source toggle listener only reacts to TOUCH_END (2). */
    fun onTabTouch(tab: Tab, event: Int): List<Row> = if (event == 2) select(tab) else rows()

    /** Source close-button listener only removes on TOUCH_END (2). */
    fun onCancel(event: Int) {
        if (event == 2) attached = false
    }

    /** ScrollView owns the physical scrolling; expose its clamped row state. */
    fun onScroll(row: Int): Int {
        scrollRow = row.coerceIn(0, (rows().size - 1).coerceAtLeast(0)); return scrollRow
    }

    /** `_onEquipOnClick` adds ItemLayer only for TOUCH_END. */
    fun onRowTouch(index: Int, event: Int): Int? = if (event == 2) rows().getOrNull(index)?.item?.id else null

    /**
     * 공개 메서드 `panelInitialized`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun panelInitialized(): Boolean = propertyPanelInitialized

    /**
     * 공개 메서드 `rows`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Row>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rows(): List<Row> = when (selected) {
        Tab.PROPERTY -> {
            propertyPanelInitialized = true
            items.filter { it.isProperty() && (inventory[it.id] ?: 0) > 0 }.sortedBy { it.id }.map { item ->
                Row(item, inventory[item.id] ?: 0, listOf(item.name, (inventory[item.id] ?: 0).toString()))
            }
        }

        Tab.WEAPON -> equipment { it.isWeapon() }
        Tab.ARMOR -> equipment { it.isArmor() }
        Tab.AUXILIARY -> equipment { it.isAuxiliary() }
    }

    private fun equipment(filter: (Item) -> Boolean) = items.filter(filter).sortedBy { it.id }.map { item ->
        val owner = item.owner ?: "창고"
        // PropertyLayer's switch assigns lv() to both WEAPONS and ARMOR;
        // only AUXILIARY keeps the initial "---" value.
        val level = if (item.isAuxiliary()) "---" else item.level.toString()
        val nameProperty = item.typeName ?: when {
            item.isAuxiliary() -> "보조"; item.isArmor() -> "방어구"; else -> "무기"
        }
        Row(
            item,
            inventory[item.id] ?: 0,
            listOf(item.name, owner, nameProperty, level, if (item.exp >= item.expLimit) "MAX" else item.exp.toString())
        )
    }

    private fun Item.isWeapon() = itemType in 0..19
    private fun Item.isArmor() = itemType in 20..25
    private fun Item.isProperty() = itemType in 26..45
    private fun Item.isAuxiliary() = !isWeapon() && !isArmor() && !isProperty()

    companion object {
        /**
         * 공개 메서드 `fromCatalog`
         *
         * ### 파라미터
        - `data` (`GameDataCatalog`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `inventory` (`Map<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `PropertyLayer`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun fromCatalog(data: GameDataCatalog, inventory: Map<Int, Int>): PropertyLayer = PropertyLayer(
            // Model.itemIter feeds only the player's item-store entries to
            // ui/PropertyLayer.  Keep the class itself generic for its
            // direct source-trace contract; narrow the live battle model at
            // this boundary instead of displaying every catalogue entry.
            data.allEquipmentProfiles().filter { (inventory[it.id] ?: 0) > 0 }
                .map { Item(it.id, it.name, it.itemType, it.icon) }, inventory
        )
    }
}
